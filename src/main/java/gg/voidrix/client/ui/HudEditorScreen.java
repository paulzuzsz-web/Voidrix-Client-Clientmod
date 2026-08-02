package gg.voidrix.client.ui;

import gg.voidrix.client.config.ConfigManager;
import gg.voidrix.client.hud.HudModule;
import gg.voidrix.client.module.ModuleManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * HUD-Editor: alle aktiven HUD-Elemente lassen sich per Drag &amp; Drop frei
 * positionieren.
 *
 * <p>Beim Ziehen erscheinen Fanglinien zu den Bildschirmkanten und zur Mitte;
 * kommt ein Element nahe genug heran, rastet es ein. Positionen werden als
 * Bruchteil des Bildschirms gespeichert und sind damit aufloesungsunabhaengig.</p>
 */
public class HudEditorScreen extends Screen {

	/** Abstand in Pixeln, ab dem an einer Hilfslinie eingerastet wird. */
	private static final int SNAP_DISTANCE = 6;

	private HudModule dragging;

	/** Mausversatz innerhalb des Elements beim Anfassen. */
	private int grabOffsetX;
	private int grabOffsetY;

	public HudEditorScreen() {
		super(Component.literal("Voidrix HUD-Editor"));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		// Nur leicht abdunkeln - man soll das Spiel darunter noch sehen,
		// um das HUD sinnvoll ausrichten zu koennen.
		g.fill(0, 0, this.width, this.height, Theme.withAlpha(Theme.BASE, 120));
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(g, mouseX, mouseY, partialTick);

		renderHint(g);

		if (dragging != null) {
			renderGuides(g);
		}

		for (HudModule module : ModuleManager.getHudModules()) {
			if (!module.isEnabled()) {
				continue;
			}

			List<String> lines = module.getPreviewLines();

			if (lines.isEmpty()) {
				continue;
			}

			// Das Element selbst zeichnen ...
			module.render(g, lines);

			// ... und darueber den Rahmen zum Anfassen.
			int width = (int) (module.getWidth(lines) * module.getScale());
			int height = (int) (module.getHeight(lines) * module.getScale());

			int x = module.getScreenX(g.guiWidth(), module.getWidth(lines));
			int y = module.getScreenY(g.guiHeight(), module.getHeight(lines));

			boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, height);
			boolean active = dragging == module;

			int color = active ? Theme.ACCENT_2 : (hovered ? Theme.ACCENT : Theme.withAlpha(Theme.TEXT_MUTED, 120));
			Draw.cutCornerOutline(g, x - 1, y - 1, width + 2, height + 2, 3, color);

			if (hovered || active) {
				Draw.textFlat(g, module.getDisplayName(), x, y - 10, color);
			}
		}
	}

	/** Kurzer Hinweistext oben. */
	private void renderHint(GuiGraphicsExtractor g) {
		String title = "HUD-Editor";
		String hint = "Elemente ziehen zum Verschieben  -  R setzt alle zurueck  -  ESC schliesst";

		int boxWidth = Math.max(Draw.textWidth(title), Draw.textWidth(hint)) + 20;
		int boxX = (this.width - boxWidth) / 2;

		// Der Editor liegt ueber der Welt, deshalb hier eine dunkle Flaeche mit
		// hellem Text - nicht das helle Frosted Glass des Menues.
		Draw.roundedRect(g, boxX, 8, boxWidth, 32, Theme.RADIUS, Theme.withAlpha(Theme.BASE, 225));
		Draw.roundedOutline(g, boxX, 8, boxWidth, 32, Theme.RADIUS, Theme.withAlpha(Theme.ACCENT, 150));

		Draw.textCentered(g, Draw.upper(title), this.width / 2, 14, Theme.HUD_TEXT);
		Draw.textCentered(g, hint, this.width / 2, 26, Theme.withAlpha(Theme.HUD_TEXT, 165));
	}

	/** Hilfslinien in der Bildschirmmitte waehrend des Ziehens. */
	private void renderGuides(GuiGraphicsExtractor g) {
		int centerX = this.width / 2;
		int centerY = this.height / 2;

		int color = Theme.withAlpha(Theme.ACCENT_2, 70);

		Draw.rect(g, centerX, 0, 1, this.height, color);
		Draw.rect(g, 0, centerY, this.width, 1, color);
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		double mouseX = event.x();
		double mouseY = event.y();

		// Von oben nach unten suchen, damit das oberste Element gegriffen wird
		List<HudModule> modules = ModuleManager.getHudModules();

		for (int i = modules.size() - 1; i >= 0; i--) {
			HudModule module = modules.get(i);

			if (!module.isEnabled()) {
				continue;
			}

			List<String> lines = module.getPreviewLines();

			if (lines.isEmpty()) {
				continue;
			}

			int width = (int) (module.getWidth(lines) * module.getScale());
			int height = (int) (module.getHeight(lines) * module.getScale());

			int x = module.getScreenX(this.width, module.getWidth(lines));
			int y = module.getScreenY(this.height, module.getHeight(lines));

			if (Draw.isHovered(mouseX, mouseY, x, y, width, height)) {
				dragging = module;
				grabOffsetX = (int) (mouseX - x);
				grabOffsetY = (int) (mouseY - y);
				return true;
			}
		}

		return super.mouseClicked(event, doubled);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (dragging == null) {
			return super.mouseDragged(event, dragX, dragY);
		}

		List<String> lines = dragging.getPreviewLines();

		int width = (int) (dragging.getWidth(lines) * dragging.getScale());
		int height = (int) (dragging.getHeight(lines) * dragging.getScale());

		int newX = (int) (event.x() - grabOffsetX);
		int newY = (int) (event.y() - grabOffsetY);

		// an Kanten und Mitte einrasten
		newX = snap(newX, width, this.width);
		newY = snap(newY, height, this.height);

		// im Bild halten
		newX = Math.max(0, Math.min(this.width - width, newX));
		newY = Math.max(0, Math.min(this.height - height, newY));

		dragging.setPosition(newX / (float) this.width, newY / (float) this.height);
		return true;
	}

	/** Rastet eine Koordinate an Rand oder Mitte ein. */
	private int snap(int value, int size, int screenSize) {
		if (Math.abs(value) < SNAP_DISTANCE) {
			return 0;
		}

		if (Math.abs(value + size - screenSize) < SNAP_DISTANCE) {
			return screenSize - size;
		}

		int centered = (screenSize - size) / 2;

		if (Math.abs(value - centered) < SNAP_DISTANCE) {
			return centered;
		}

		return value;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (dragging != null) {
			dragging = null;
			ConfigManager.save();
			return true;
		}

		return super.mouseReleased(event);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == GLFW.GLFW_KEY_R) {
			// Alle HUD-Elemente wieder untereinander an den linken Rand legen
			float y = 0.02f;

			for (HudModule module : ModuleManager.getHudModules()) {
				module.setPosition(0.02f, y);
				y += 0.04f;
			}

			ConfigManager.save();
			return true;
		}

		return super.keyPressed(event);
	}

	@Override
	public void onClose() {
		ConfigManager.save();
		super.onClose();
	}
}
