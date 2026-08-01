package dev.voidrix.waypoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.voidrix.VoidrixClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

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
 * Stores your waypoints in a file of their own.
 *
 * <p>Separate from the module config on purpose: waypoints are content you built up over a long
 * time, and they should survive resetting your settings or switching profiles.
 */
public final class WaypointManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Path directory;
    private final Path file;
    private final List<Waypoint> waypoints = new ArrayList<>();

    public WaypointManager() {
        this.directory = FabricLoader.getInstance().getConfigDir().resolve(VoidrixClient.MOD_ID);
        this.file = directory.resolve("waypoints.json");
    }

    public List<Waypoint> all() {
        return waypoints;
    }

    /** Waypoints that should currently be drawn, given the dimension the player is in. */
    public List<Waypoint> active() {
        String dimension = currentDimension();
        List<Waypoint> out = new ArrayList<>();
        for (Waypoint waypoint : waypoints) {
            if (waypoint.visible() && waypoint.showsIn(dimension)) {
                out.add(waypoint);
            }
        }
        return out;
    }

    /** The id of the dimension the player is in, or an empty string when not in a world. */
    public static String currentDimension() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return "";
        }
        return mc.level.dimension().identifier().toString();
    }

    public void add(Waypoint waypoint) {
        waypoints.add(waypoint);
        save();
    }

    public void remove(Waypoint waypoint) {
        waypoints.remove(waypoint);
        save();
    }

    // -------------------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------------------

    public void load() {
        waypoints.clear();
        if (!Files.exists(file)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) {
                return;
            }
            JsonArray array = root.getAsJsonArray("waypoints");
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.size(); i++) {
                if (!array.get(i).isJsonObject()) {
                    continue;
                }
                Waypoint waypoint = Waypoint.fromJson(array.get(i).getAsJsonObject());
                // One malformed entry should cost you that entry, not the whole list.
                if (waypoint != null) {
                    waypoints.add(waypoint);
                }
            }
        } catch (IOException | RuntimeException e) {
            VoidrixClient.LOGGER.warn("[Voidrix] could not read {}", file, e);
        }
    }

    public void save() {
        JsonArray array = new JsonArray();
        for (Waypoint waypoint : waypoints) {
            array.add(waypoint.toJson());
        }
        JsonObject root = new JsonObject();
        root.addProperty("version", 1);
        root.add("waypoints", array);

        try {
            Files.createDirectories(directory);
            Path tmp = directory.resolve("waypoints.json.tmp");
            try (Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            VoidrixClient.LOGGER.error("[Voidrix] could not write {}", file, e);
        }
    }
}
