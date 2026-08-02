package gg.voidrix.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.voidrix.client.Voidrix;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.premium.PremiumManager;
import gg.voidrix.client.waypoint.WaypointManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Laedt und speichert {@code config/voidrix.json}.
 *
 * <p>Aufbau der Datei:</p>
 * <pre>{@code
 * {
 *   "configVersion": 1,
 *   "premium": { "unlocked": true, "redeemedAt": 1750000000000 },
 *   "modules": {
 *     "fps": { "enabled": true, "settings": { ... }, "position": { "x": 0.02, "y": 0.02 } }
 *   },
 *   "waypoints": [ { "name": "Base", "x": 100, ... } ]
 * }
 * }</pre>
 *
 * <p>Die Datei ist bewusst gut lesbar formatiert, damit man sie auch von Hand
 * anpassen kann. Unbekannte oder kaputte Eintraege werden ignoriert, statt den
 * Start zu verhindern.</p>
 */
public final class ConfigManager {

	/** Erhoehen, wenn sich das Format inkompatibel aendert. */
	private static final int CONFIG_VERSION = 1;

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();

	private ConfigManager() {
	}

	/** Pfad der Konfigurationsdatei: {@code <minecraft>/config/voidrix.json}. */
	public static Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir().resolve(Voidrix.MOD_ID + ".json");
	}

	// ---------------------------------------------------------------
	// Laden
	// ---------------------------------------------------------------

	public static void load() {
		Path path = getConfigPath();

		if (!Files.exists(path)) {
			Voidrix.LOGGER.info("Keine Konfiguration gefunden - es werden die Standardwerte benutzt.");
			save();
			return;
		}

		try (Reader reader = Files.newBufferedReader(path)) {
			JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

			loadPremium(root);
			loadModules(root);

			if (root.has("waypoints") && root.get("waypoints").isJsonArray()) {
				WaypointManager.load(root.getAsJsonArray("waypoints"));
			}

			Voidrix.LOGGER.info("Konfiguration geladen: {}", path);
		} catch (Exception e) {
			// Eine kaputte Config darf den Start nicht verhindern.
			Voidrix.LOGGER.error("Konfiguration konnte nicht gelesen werden - benutze Standardwerte.", e);
		}
	}

	private static void loadPremium(JsonObject root) {
		if (!root.has("premium") || !root.get("premium").isJsonObject()) {
			return;
		}

		JsonObject premium = root.getAsJsonObject("premium");

		boolean unlocked = premium.has("unlocked") && premium.get("unlocked").getAsBoolean();
		long redeemedAt = premium.has("redeemedAt") ? premium.get("redeemedAt").getAsLong() : 0L;

		PremiumManager.restore(unlocked, redeemedAt);
	}

	private static void loadModules(JsonObject root) {
		if (!root.has("modules") || !root.get("modules").isJsonObject()) {
			return;
		}

		JsonObject modules = root.getAsJsonObject("modules");

		for (Module module : ModuleManager.getModules()) {
			if (modules.has(module.getId()) && modules.get(module.getId()).isJsonObject()) {
				module.load(modules.getAsJsonObject(module.getId()));
			}
		}
	}

	// ---------------------------------------------------------------
	// Speichern
	// ---------------------------------------------------------------

	public static void save() {
		Path path = getConfigPath();

		try {
			Files.createDirectories(path.getParent());

			JsonObject root = new JsonObject();
			root.addProperty("configVersion", CONFIG_VERSION);

			JsonObject premium = new JsonObject();
			premium.addProperty("unlocked", PremiumManager.isPremium());
			premium.addProperty("redeemedAt", PremiumManager.getRedeemedAt());
			root.add("premium", premium);

			JsonObject modules = new JsonObject();

			for (Module module : ModuleManager.getModules()) {
				modules.add(module.getId(), module.save());
			}

			root.add("modules", modules);

			JsonArray waypoints = WaypointManager.save();
			root.add("waypoints", waypoints);

			try (Writer writer = Files.newBufferedWriter(path)) {
				GSON.toJson(root, writer);
			}
		} catch (IOException e) {
			Voidrix.LOGGER.error("Konfiguration konnte nicht gespeichert werden.", e);
		}
	}
}
