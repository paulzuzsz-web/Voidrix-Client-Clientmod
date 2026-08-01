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
import java.util.ArrayList;
import java.util.List;

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

    private static final String DEFAULT_PROFILE = "default";

    private final ModuleManager modules;
    private final Path directory;
    private final Path profileDirectory;
    private String profile = DEFAULT_PROFILE;

    public ConfigManager(ModuleManager modules) {
        this.modules = modules;
        this.directory = FabricLoader.getInstance().getConfigDir().resolve(VoidrixClient.MOD_ID);
        this.profileDirectory = directory.resolve("profiles");
    }

    /** The file the current profile lives in. */
    public Path file() {
        return profileDirectory.resolve(sanitise(profile) + ".json");
    }

    /** True when the player has never run the mod before. */
    public boolean isFirstRun() {
        return !Files.exists(file());
    }

    // -------------------------------------------------------------------------------------
    // Profiles
    // -------------------------------------------------------------------------------------

    public String currentProfile() {
        return profile;
    }

    /** Every profile on disk, always including the default, sorted. */
    public List<String> profiles() {
        List<String> names = new ArrayList<>();
        if (Files.isDirectory(profileDirectory)) {
            try (var stream = Files.list(profileDirectory)) {
                stream.filter(p -> p.getFileName().toString().endsWith(".json"))
                        .map(p -> p.getFileName().toString())
                        .map(n -> n.substring(0, n.length() - 5))
                        .forEach(names::add);
            } catch (IOException e) {
                VoidrixClient.LOGGER.warn("[Voidrix] could not list profiles", e);
            }
        }
        if (!names.contains(DEFAULT_PROFILE)) {
            names.add(DEFAULT_PROFILE);
        }
        names.sort(String::compareToIgnoreCase);
        return names;
    }

    /** Saves the current profile, then loads another one in its place. */
    public void switchTo(String name) {
        if (name == null || name.isBlank() || name.equals(profile)) {
            return;
        }
        save();
        profile = name;
        writeState();
        load();
    }

    /** Creates a profile seeded with the settings that are live right now. */
    public void createProfile(String name) {
        String clean = sanitise(name);
        if (clean.isBlank() || profiles().contains(clean)) {
            return;
        }
        profile = clean;
        writeState();
        save();
    }

    /** Deletes a profile. The default profile cannot be removed. */
    public void deleteProfile(String name) {
        if (DEFAULT_PROFILE.equals(name)) {
            return;
        }
        try {
            Files.deleteIfExists(profileDirectory.resolve(sanitise(name) + ".json"));
        } catch (IOException e) {
            VoidrixClient.LOGGER.warn("[Voidrix] could not delete profile {}", name, e);
        }
        if (name.equals(profile)) {
            switchTo(DEFAULT_PROFILE);
        }
    }

    /** Strips anything that would escape the profiles directory or upset a filesystem. */
    private static String sanitise(String name) {
        return name.trim().replaceAll("[^A-Za-z0-9 _-]", "").trim();
    }

    private void readState() {
        Path state = directory.resolve("state.json");
        if (!Files.exists(state)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(state, StandardCharsets.UTF_8)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            if (json != null && json.has("profile")) {
                String name = sanitise(json.get("profile").getAsString());
                if (!name.isBlank()) {
                    profile = name;
                }
            }
        } catch (IOException | RuntimeException e) {
            VoidrixClient.LOGGER.warn("[Voidrix] could not read state.json, using the default profile", e);
        }
    }

    private void writeState() {
        JsonObject json = new JsonObject();
        json.addProperty("profile", profile);
        try {
            Files.createDirectories(directory);
            try (Writer writer = Files.newBufferedWriter(directory.resolve("state.json"),
                    StandardCharsets.UTF_8)) {
                GSON.toJson(json, writer);
            }
        } catch (IOException e) {
            VoidrixClient.LOGGER.error("[Voidrix] could not write state.json", e);
        }
    }

    // -------------------------------------------------------------------------------------
    // Load
    // -------------------------------------------------------------------------------------

    public void load() {
        readState();
        migrateLegacyConfig();

        Path source = file();
        if (!Files.exists(source)) {
            modules.applyDefaults();
            save();
            return;
        }

        JsonObject root;
        try (Reader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8)) {
            root = GSON.fromJson(reader, JsonObject.class);
        } catch (IOException | RuntimeException e) {
            VoidrixClient.LOGGER.warn("[Voidrix] could not read {} - falling back to defaults", source, e);
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
            Files.createDirectories(profileDirectory);
            // Write beside the target and move into place so a crash mid-write cannot truncate
            // an existing good config.
            Path tmp = profileDirectory.resolve("~write.tmp");
            try (Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
            Files.move(tmp, file(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            VoidrixClient.LOGGER.error("[Voidrix] could not write {}", file(), e);
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

    /**
     * Moves a pre-profiles {@code config.json} into the default profile, so upgrading does not
     * silently reset everything the player had configured.
     */
    private void migrateLegacyConfig() {
        Path legacy = directory.resolve("config.json");
        if (!Files.exists(legacy) || Files.exists(file())) {
            return;
        }
        try {
            Files.createDirectories(profileDirectory);
            Files.move(legacy, profileDirectory.resolve(DEFAULT_PROFILE + ".json"));
            VoidrixClient.LOGGER.info("[Voidrix] migrated config.json into the default profile");
        } catch (IOException e) {
            VoidrixClient.LOGGER.warn("[Voidrix] could not migrate the old config", e);
        }
    }

    /** Keeps HUD coordinates readable in the file instead of dumping full double precision. */
    private static double round(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
