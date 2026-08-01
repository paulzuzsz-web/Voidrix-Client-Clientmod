package dev.voidrix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.voidrix.VoidrixClient;
import dev.voidrix.module.HudModule;
import dev.voidrix.module.Module;
import dev.voidrix.module.ModuleManager;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Reads and writes the single local config file.
 *
 * <p>This is the whole of Voidrix's persistence: one JSON file under {@code config/voidrix/}. There
 * is no account, no server, no database and nothing leaves the machine. A malformed or partial file
 * is tolerated - anything that fails to parse keeps its default instead of wiping the config.
 */
public final class ConfigManager {
    private static final int FORMAT_VERSION = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final ModuleManager modules;
    private final Path directory;
    private final Path file;

    public ConfigManager(ModuleManager modules) {
        this.modules = modules;
        this.directory = FabricLoader.getInstance().getConfigDir().resolve(VoidrixClient.MOD_ID);
        this.file = directory.resolve("config.json");
    }

    public Path file() {
        return file;
    }

    /** True when the player has never run the mod before. */
    public boolean isFirstRun() {
        return !Files.exists(file);
    }

    // -------------------------------------------------------------------------------------
    // Load
    // -------------------------------------------------------------------------------------

    public void load() {
        if (!Files.exists(file)) {
            modules.applyDefaults();
            save();
            return;
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            root = GSON.fromJson(reader, JsonObject.class);
        } catch (IOException | RuntimeException e) {
            VoidrixClient.LOGGER.warn("[Voidrix] could not read {} - falling back to defaults", file, e);
            modules.applyDefaults();
            return;
        }

        if (root == null) {
            modules.applyDefaults();
            return;
        }

        JsonObject moduleData = root.getAsJsonObject("modules");
        if (moduleData == null) {
            modules.applyDefaults();
            return;
        }

        for (Module module : modules.all()) {
            JsonObject entry = moduleData.getAsJsonObject(module.id());
            if (entry == null) {
                // A module added by a newer version of the mod: honour its own default.
                if (module.enabledByDefault()) {
                    module.setEnabled(true);
                }
                continue;
            }
            readModule(module, entry);
        }
    }

    private void readModule(Module module, JsonObject entry) {
        if (entry.has("enabled")) {
            module.setEnabled(entry.get("enabled").getAsBoolean());
        }
        if (entry.has("key")) {
            module.setKeyCode(entry.get("key").getAsInt());
        }

        if (module instanceof HudModule hud && entry.has("hud")) {
            JsonObject pos = entry.getAsJsonObject("hud");
            double x = pos.has("x") ? pos.get("x").getAsDouble() : hud.posX();
            double y = pos.has("y") ? pos.get("y").getAsDouble() : hud.posY();
            hud.setPos(x, y);
        }

        JsonObject settings = entry.getAsJsonObject("settings");
        if (settings == null) {
            return;
        }
        for (Setting<?> setting : module.settings()) {
            if (settings.has(setting.id())) {
                try {
                    setting.fromJson(settings.get(setting.id()));
                } catch (RuntimeException e) {
                    VoidrixClient.LOGGER.warn("[Voidrix] bad value for {}.{}, keeping default",
                            module.id(), setting.id());
                }
            }
            if (setting instanceof ColorSetting color) {
                String rainbowKey = setting.id() + "_rainbow";
                if (settings.has(rainbowKey)) {
                    color.rainbow().fromJson(settings.get(rainbowKey));
                }
            }
        }
    }

    // -------------------------------------------------------------------------------------
    // Save
    // -------------------------------------------------------------------------------------

    public void save() {
        JsonObject root = new JsonObject();
        root.addProperty("version", FORMAT_VERSION);
        root.addProperty("_comment", "Voidrix Client settings. Local file, edited by the in-game menu.");

        JsonObject moduleData = new JsonObject();
        for (Module module : modules.all()) {
            moduleData.add(module.id(), writeModule(module));
        }
        root.add("modules", moduleData);

        try {
            Files.createDirectories(directory);
            // Write beside the target and move into place so a crash mid-write cannot truncate
            // an existing good config.
            Path tmp = directory.resolve("config.json.tmp");
            try (Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            VoidrixClient.LOGGER.error("[Voidrix] could not write {}", file, e);
        }
    }

    private JsonObject writeModule(Module module) {
        JsonObject entry = new JsonObject();
        entry.addProperty("enabled", module.isEnabled());
        entry.addProperty("key", module.keyCode());

        if (module instanceof HudModule hud) {
            JsonObject pos = new JsonObject();
            pos.addProperty("x", round(hud.posX()));
            pos.addProperty("y", round(hud.posY()));
            entry.add("hud", pos);
        }

        JsonObject settings = new JsonObject();
        for (Setting<?> setting : module.settings()) {
            settings.add(setting.id(), setting.toJson());
            if (setting instanceof ColorSetting color) {
                settings.add(setting.id() + "_rainbow", color.rainbow().toJson());
            }
        }
        entry.add("settings", settings);
        return entry;
    }

    /** Keeps HUD coordinates readable in the file instead of dumping full double precision. */
    private static double round(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
