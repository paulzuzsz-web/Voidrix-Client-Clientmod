package gg.voidrix.client.ui;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.premium.PremiumManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * Schnellzugriff - die erste Ebene, die per Tastendruck aufgeht.
 *
 * <pre>
 *              VOIDRIX  /  CLIENT
 *        +--------------------------------+
 *        |           MOD-MENUE            |
 *        +--------------------------------+
 *          [o]  [T]  [/]  [%]  [~]
 * </pre>
 *
 * <p>Ueber den breiten Knopf geht das eigentliche Mod-Menue auf, die
 * Icon-Reihe springt direkt in die jeweilige Rubrik. Das Spiel laeuft dabei
 * weiter - der Screen ist nur ein Overlay.</p>
 */
public class QuickAccessScreen extends Screen {

	/** Die Rubriken der Icon-Reihe, in Reihenfolge. */
	private static final Icons.Icon[] ROW_ICONS = {
			Icons.Icon.CAMERA,
			Icons.Icon.SHIRT,
			Icons.Icon.LIGHTNING,
			Icons.Icon.EMOTE,
			Icons.Icon.FRIENDS
	};

	private static final VoidrixMenuScreen.Page[] ROW_PAGES = {
			VoidrixMenuScreen.Page.MODS,
			VoidrixMenuScreen.Page.COSMETICS,
			VoidrixMenuScreen.Page.PLUS,
			VoidrixMenuScreen.Page.EMOTES,
			VoidrixMenuScreen.Page.FRIENDS
	};

	private static final int ICON_BUTTON = 36;
	private static final int ICON_GAP = 7;

	private int buttonX;
	private int buttonY;
	private int buttonWidth;

	private int rowX;
	private int rowY;

	public QuickAccessScreen() {
		super(Component.literal(Voidrix.MOD_NAME));
	}

	@Override
	protected void init() {
		buttonWidth = Math.min(300, this.width - 80);
		buttonX = (this.width - buttonWidth) / 2;
		buttonY = this.height / 2 + 4;

		int rowWidth = ROW_ICONS.length * ICON_BUTTON + (ROW_ICONS.length - 1) * ICON_GAP;
		rowX = (this.width - rowWidth) / 2;
		rowY = buttonY + 28;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		// Blur des Spielgeschehens plus heller Schleier
		extractBlurredBackground(g);
		g.fill(0, 0, this.width, this.height, Theme.OVERLAY);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(g, mouseX, mouseY, partialTick);

		renderWordmark(g);
		renderMenuButton(g, mouseX, mouseY);
		renderIconRow(g, mouseX, mouseY);
	}

	/**
	 * Schriftzug "VOIDRIX / CLIENT" mit dem Blitz als Trenner.
	 *
	 * <p>Deutlich groesser als der normale Fliesstext - Minecrafts Schrift hat
	 * nur eine feste Groesse, deshalb wird ueber die Matrix skaliert.</p>
	 */
	private void renderWordmark(GuiGraphicsExtractor g) {
		String left = "VOIDRIX";
		String right = "CLIENT";

		float scale = 2.4f;
		int gap = 44;

		int leftWidth = Draw.textWidthScaled(left, scale);
		int rightWidth = Draw.textWidthScaled(right, scale);

		int startX = (this.width - (leftWidth + gap + rightWidth)) / 2;
		int y = buttonY - 46;

		// Schatten, damit der Schriftzug auch vor hellem Himmel steht
		Draw.textScaled(g, left, startX, y, 0xFFFFFFFF, scale, true);
		Draw.textScaled(g, right, startX + leftWidth + gap, y, 0xFFFFFFFF, scale, true);

		// Blitz dazwischen - bei Voidrix+ pulsierend
		int markColor = PremiumManager.isPremium()
				? Theme.pulse(System.currentTimeMillis())
				: 0xFFFFFFFF;

		Icons.draw(g, Icons.Icon.LIGHTNING,
				startX + leftWidth + gap / 2 - 11, y - 3, 24, markColor);
	}

	private void renderMenuButton(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		boolean hovered = Draw.isHovered(mouseX, mouseY, buttonX, buttonY, buttonWidth, 20);

		Draw.roundedRect(g, buttonX, buttonY, buttonWidth, 20, Theme.RADIUS_SMALL,
				hovered ? Theme.CARD_HOVER : Theme.CARD);

		Draw.roundedOutline(g, buttonX, buttonY, buttonWidth, 20, Theme.RADIUS_SMALL,
				hovered ? Theme.withAlpha(Theme.ACCENT, 190) : Theme.BORDER_STRONG);

		Draw.textCentered(g, "MOD-MENUE", this.width / 2, buttonY + 6,
				hovered ? Theme.TEXT : Theme.TEXT_DIM);
	}

	private void renderIconRow(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		for (int i = 0; i < ROW_ICONS.length; i++) {
			int x = rowX + i * (ICON_BUTTON + ICON_GAP);
			boolean hovered = Draw.isHovered(mouseX, mouseY, x, rowY, ICON_BUTTON, ICON_BUTTON);

			// Die Blitz-Kachel in der Mitte ist die Voidrix+ Rubrik und
			// wird deshalb dauerhaft hervorgehoben.
			boolean highlighted = ROW_PAGES[i] == VoidrixMenuScreen.Page.PLUS;

			Draw.roundedRect(g, x, rowY, ICON_BUTTON, ICON_BUTTON, Theme.RADIUS_SMALL,
					hovered ? Theme.CARD_HOVER : Theme.CARD);

			Draw.roundedOutline(g, x, rowY, ICON_BUTTON, ICON_BUTTON, Theme.RADIUS_SMALL,
					hovered ? Theme.withAlpha(Theme.ACCENT, 190) : Theme.BORDER);

			int iconColor = highlighted ? Theme.ACCENT : (hovered ? Theme.TEXT : Theme.TEXT_DIM);
			Icons.draw(g, ROW_ICONS[i], x + 10, rowY + 10, 16, iconColor);
		}

		// Hinweis unterhalb der Icon-Reihe, nicht darin
		Draw.textScaledCentered(g, "RECHTE UMSCHALTTASTE SCHLIESST", this.width / 2,
				rowY + ICON_BUTTON + 10, Theme.withAlpha(0xFFFFFFFF, 120), 0.8f, true);
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		double mouseX = event.x();
		double mouseY = event.y();

		if (Draw.isHovered(mouseX, mouseY, buttonX, buttonY, buttonWidth, 20)) {
			this.minecraft.gui.setScreen(new VoidrixMenuScreen());
			return true;
		}

		for (int i = 0; i < ROW_ICONS.length; i++) {
			int x = rowX + i * (ICON_BUTTON + ICON_GAP);

			if (Draw.isHovered(mouseX, mouseY, x, rowY, ICON_BUTTON, ICON_BUTTON)) {
				this.minecraft.gui.setScreen(new VoidrixMenuScreen(ROW_PAGES[i]));
				return true;
			}
		}

		return super.mouseClicked(event, doubled);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
			this.onClose();
			return true;
		}

		return super.keyPressed(event);
	}
}
