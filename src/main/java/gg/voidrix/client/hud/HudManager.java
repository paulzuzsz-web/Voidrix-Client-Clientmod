package gg.voidrix.client.hud;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.ui.HudEditorScreen;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * Haengt alle Voidrix-HUD-Module in die HUD-Renderkette von Minecraft ein.
 *
 * <p><b>Hinweis zur API:</b> In aelteren Versionen registrierte man hier
 * {@code HudRenderCallback}. Ab 26.x gibt es diesen Callback nicht mehr - die
 * Fabric-API nutzt jetzt {@link HudElementRegistry} mit
 * {@code HudElement#extractRenderState}. Der Ablauf ist derselbe: Voidrix
 * haengt sich als letztes Element an, zeichnet also ueber dem Vanilla-HUD.</p>
 */
public final class HudManager {

	/** Kennung unseres HUD-Elements innerhalb der Renderkette. */
	public static final Identifier ELEMENT_ID = Identifier.fromNamespaceAndPath(Voidrix.MOD_ID, "hud");

	private HudManager() {
	}

	public static void init() {
		// addLast = nach allen Vanilla-Elementen, also ganz oben
		HudElementRegistry.addLast(ELEMENT_ID, HudManager::renderAll);
	}

	private static void renderAll(GuiGraphicsExtractor g, DeltaTracker delta) {
		Minecraft client = Minecraft.getInstance();

		if (client.player == null || client.level == null) {
			return;
		}

		// Waehrend der HUD-Editor offen ist, zeichnet dieser die Elemente
		// selbst (inklusive Rahmen und Fanglinien) - sonst haetten wir alles
		// doppelt auf dem Bildschirm.
		if (client.gui.screen() instanceof HudEditorScreen) {
			return;
		}

		for (HudModule module : ModuleManager.getHudModules()) {
			if (!module.isEnabled()) {
				continue;
			}

			try {
				List<String> lines = module.getLines();

				if (!lines.isEmpty()) {
					module.render(g, lines);
				}
			} catch (Exception e) {
				// Ein fehlerhaftes HUD-Modul darf das restliche HUD nicht
				// blockieren - Fehler werden einmal geloggt und ignoriert.
				Voidrix.LOGGER.error("Fehler beim Rendern des HUD-Moduls {}", module.getId(), e);
			}
		}
	}
}
