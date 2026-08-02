package gg.voidrix.client;

import gg.voidrix.client.config.ConfigManager;
import gg.voidrix.client.hud.HudManager;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.module.misc.WaypointsModule;
import gg.voidrix.client.module.pvp.CustomCrosshairModule;
import gg.voidrix.client.module.visual.ShulkerPreviewModule;
import gg.voidrix.client.ui.HudEditorScreen;
import gg.voidrix.client.ui.VoidrixMenuScreen;
import gg.voidrix.client.util.TpsTracker;
import gg.voidrix.client.waypoint.Waypoint;
import gg.voidrix.client.waypoint.WaypointManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Einstiegspunkt des Mods.
 *
 * <p>Voidrix ist als <b>reiner Client-Mod</b> gebaut: Es gibt keinen
 * Server-Entrypoint, es werden keine eigenen Pakete verschickt und kein
 * Spielverhalten veraendert, das der Server nicht ohnehin zulaesst. Damit
 * laeuft der Mod auf jedem Server, ohne dort installiert zu sein.</p>
 */
public class VoidrixClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Voidrix.LOGGER.info("Voidrix {} wird gestartet ...", Voidrix.VERSION);

		// 1. Module registrieren - muss vor dem Laden der Config passieren,
		//    weil die Config auf die Modul-IDs zugreift.
		ModuleManager.registerAll();

		// 2. Gespeicherte Einstellungen laden (config/voidrix.json)
		ConfigManager.load();

		// 3. Tasten anmelden
		VoidrixKeys.register();

		// 4. HUD-Elemente einhaengen
		HudManager.init();
		registerExtraHudElements();

		// 5. Tooltip-Erweiterungen (Shulker Preview)
		registerTooltips();

		// 6. Client-Tick: Tasten abfragen und Module ticken lassen
		ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

		// 6. Beim Beenden alles sichern
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ConfigManager.save());

		Voidrix.LOGGER.info("Voidrix ist bereit. Menue oeffnen mit der rechten Shift-Taste.");
	}

	/**
	 * Zusaetzliche HUD-Elemente, die nicht dem generischen HUD-System folgen:
	 * das eigene Fadenkreuz (ersetzt das Vanilla-Element) und die Waypoints.
	 */
	private void registerExtraHudElements() {
		// Waypoints ganz zuletzt zeichnen, damit sie ueber dem HUD liegen
		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath(Voidrix.MOD_ID, "waypoints"),
				(extractor, delta) -> {
					Module module = ModuleManager.get("waypoints");

					if (module instanceof WaypointsModule waypoints && waypoints.isEnabled()) {
						waypoints.render(extractor);
					}
				});

		// Vanillas Fadenkreuz ersetzen: Ist das Modul aus, zeichnet das
		// Original weiter - dadurch bleibt alles kompatibel.
		HudElementRegistry.replaceElement(VanillaHudElements.CROSSHAIR, original ->
				(extractor, delta) -> {
					Module module = ModuleManager.get("custom_crosshair");

					if (module instanceof CustomCrosshairModule crosshair && crosshair.isEnabled()) {
						crosshair.render(extractor);
						return;
					}

					original.extractRenderState(extractor, delta);
				});
	}

	/**
	 * Haengt zusaetzliche Zeilen an Item-Tooltips.
	 *
	 * <p>Fuer die Shulker-Vorschau reicht das Fabric-Event voellig aus - ein
	 * Mixin waere hier unnoetig. Weitere Tooltip-Module lassen sich einfach
	 * unten anhaengen.</p>
	 */
	private void registerTooltips() {
		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
			Module module = ModuleManager.get("shulker_preview");

			if (module instanceof ShulkerPreviewModule preview && preview.isEnabled()) {
				lines.addAll(preview.buildTooltipLines(stack));
			}
		});
	}

	/** Wird einmal pro Client-Tick aufgerufen. */
	private void onClientTick(Minecraft client) {
		// Menue oeffnen
		while (VoidrixKeys.OPEN_MENU.consumeClick()) {
			if (client.gui.screen() == null) {
				client.gui.setScreen(new VoidrixMenuScreen());
			}
		}

		// HUD-Editor oeffnen
		while (VoidrixKeys.OPEN_HUD_EDITOR.consumeClick()) {
			if (client.gui.screen() == null) {
				client.gui.setScreen(new HudEditorScreen());
			}
		}

		// Waypoint an der aktuellen Position setzen
		while (VoidrixKeys.ADD_WAYPOINT.consumeClick()) {
			if (client.player != null && client.gui.screen() == null) {
				addWaypointAtPlayer(client);
			}
		}

		TpsTracker.tick();
		ModuleManager.tick();
	}

	/** Setzt einen automatisch benannten Waypoint und meldet das im Chat. */
	private void addWaypointAtPlayer(Minecraft client) {
		String name = "Punkt " + (WaypointManager.getAll().size() + 1);
		Waypoint waypoint = WaypointManager.createAtPlayer(name);

		if (waypoint == null) {
			return;
		}

		ConfigManager.save();

		if (client.player != null) {
			// Nur lokal im Chat anzeigen - es wird nichts an den Server geschickt.
			client.player.sendSystemMessage(
					Component.literal("[Voidrix] Waypoint \"" + name + "\" gesetzt."));
		}
	}
}
