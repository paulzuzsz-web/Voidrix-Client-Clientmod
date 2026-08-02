package gg.voidrix.client.module;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.hud.HudModule;
import gg.voidrix.client.module.hud.ChatHeadsModule;
import gg.voidrix.client.module.hud.CoordinatesModule;
import gg.voidrix.client.module.hud.CpsModule;
import gg.voidrix.client.module.hud.FpsModule;
import gg.voidrix.client.module.hud.HealthIndicatorModule;
import gg.voidrix.client.module.hud.PingModule;
import gg.voidrix.client.module.hud.TpsModule;
import gg.voidrix.client.module.misc.DiscordPresenceModule;
import gg.voidrix.client.module.misc.WaypointsModule;
import gg.voidrix.client.module.pvp.BlockOutlinesModule;
import gg.voidrix.client.module.pvp.CustomCrosshairModule;
import gg.voidrix.client.module.pvp.DropStackModule;
import gg.voidrix.client.module.pvp.ItemHighlighterModule;
import gg.voidrix.client.module.pvp.NoHurtCamModule;
import gg.voidrix.client.module.pvp.ToggleSprintModule;
import gg.voidrix.client.module.pvp.ZoomModule;
import gg.voidrix.client.module.visual.BorderlessFullscreenModule;
import gg.voidrix.client.module.visual.ItemModelModule;
import gg.voidrix.client.module.visual.ParticlesFilterModule;
import gg.voidrix.client.module.visual.ShinyPotsModule;
import gg.voidrix.client.module.visual.ShulkerPreviewModule;
import gg.voidrix.client.module.visual.SkinPreviewModule;
import gg.voidrix.client.module.visual.WaveyCapesModule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Zentrale Registrierung aller Module.
 *
 * <p>Ein neues Modul wird ausschliesslich in {@link #registerAll()} eingetragen -
 * Menue, HUD-Editor, Config und Tick-Verteilung greifen automatisch darauf zu.</p>
 */
public final class ModuleManager {

	/** Reihenfolge = Reihenfolge im Menue. */
	private static final Map<String, Module> MODULES = new LinkedHashMap<>();

	private ModuleManager() {
	}

	public static void registerAll() {
		if (!MODULES.isEmpty()) {
			return;
		}

		// --- HUD ----------------------------------------------------
		register(new CpsModule());
		register(new FpsModule());
		register(new PingModule());
		register(new CoordinatesModule());
		register(new TpsModule());
		register(new HealthIndicatorModule());
		register(new ChatHeadsModule());

		// --- Gameplay / PvP -----------------------------------------
		register(new ItemHighlighterModule());
		register(new CustomCrosshairModule());
		register(new BlockOutlinesModule());
		register(new NoHurtCamModule());
		register(new DropStackModule());
		register(new ToggleSprintModule());
		register(new ZoomModule());

		// --- Optik ---------------------------------------------------
		register(new SkinPreviewModule());
		register(new ShulkerPreviewModule());
		register(new ShinyPotsModule());
		register(new WaveyCapesModule());
		register(new ItemModelModule());
		register(new ParticlesFilterModule());
		register(new BorderlessFullscreenModule());

		// --- Sonstiges ------------------------------------------------
		register(new DiscordPresenceModule());
		register(new WaypointsModule());

		Voidrix.LOGGER.info("{} Module registriert.", MODULES.size());
	}

	private static void register(Module module) {
		MODULES.put(module.getId(), module);
	}

	// ---------------------------------------------------------------
	// Zugriff
	// ---------------------------------------------------------------

	public static Iterable<Module> getModules() {
		return MODULES.values();
	}

	public static List<Module> getAll() {
		return new ArrayList<>(MODULES.values());
	}

	public static Module get(String id) {
		return MODULES.get(id);
	}

	/** Alle Module einer Kategorie. */
	public static List<Module> byCategory(Category category) {
		List<Module> result = new ArrayList<>();

		for (Module module : MODULES.values()) {
			if (module.getCategory() == category) {
				result.add(module);
			}
		}

		return result;
	}

	/** Alle HUD-Module (fuer HUD-Renderer und Editor). */
	public static List<HudModule> getHudModules() {
		List<HudModule> result = new ArrayList<>();

		for (Module module : MODULES.values()) {
			if (module instanceof HudModule hud) {
				result.add(hud);
			}
		}

		return result;
	}

	/**
	 * Sucht Module nach Name oder Beschreibung - fuer die Suchleiste im Menue.
	 *
	 * @param query Suchbegriff, leer = alle Module
	 */
	public static List<Module> search(String query) {
		if (query == null || query.isBlank()) {
			return getAll();
		}

		String needle = query.toLowerCase(Locale.ROOT).trim();
		List<Module> result = new ArrayList<>();

		for (Module module : MODULES.values()) {
			if (module.getDisplayName().toLowerCase(Locale.ROOT).contains(needle)
					|| module.getDescription().toLowerCase(Locale.ROOT).contains(needle)) {
				result.add(module);
			}
		}

		return result;
	}

	/** Ruft {@link Module#onTick()} fuer alle aktiven Module auf. */
	public static void tick() {
		for (Module module : MODULES.values()) {
			if (module.isEnabled()) {
				try {
					module.onTick();
				} catch (Exception e) {
					// Ein defektes Modul darf nie den ganzen Client mitreissen.
					Voidrix.LOGGER.error("Fehler im Modul {}", module.getId(), e);
				}
			}
		}
	}

	/**
	 * Prueft, ob ein Modul aktiv ist. Von den Mixins benutzt, damit diese den
	 * ModuleManager nicht direkt kennen muessen.
	 */
	public static boolean isActive(String id) {
		Module module = MODULES.get(id);
		return module != null && module.isEnabled();
	}
}
