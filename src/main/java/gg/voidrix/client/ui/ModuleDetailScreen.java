package gg.voidrix.client.ui;

import gg.voidrix.client.config.ConfigManager;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.ColorSetting;
import gg.voidrix.client.module.setting.DoubleSetting;
import gg.voidrix.client.module.setting.EnumSetting;
import gg.voidrix.client.module.setting.IntSetting;
import gg.voidrix.client.module.setting.Setting;
import gg.voidrix.client.module.setting.StringSetting;
import gg.voidrix.client.ui.widget.TextField;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Detailseite eines Moduls.
 *
 * <p>Die Oberflaeche wird vollstaendig <b>generisch</b> aus den Einstellungen
 * des Moduls aufgebaut: Je nach Setting-Typ entsteht ein Toggle, ein Slider,
 * ein Dropdown, ein Farbwaehler oder ein Textfeld. Ein neues Setting im Modul
 * erscheint hier also automatisch - hier muss nichts angepasst werden.</p>
 */
public class ModuleDetailScreen extends Screen {

	private static final int ROW_HEIGHT = 24;
	private static final int PADDING = 14;

	private final Screen parent;
	private final Module module;

	/** Textfelder fuer StringSettings, pro Setting eines. */
	private final Map<String, TextField> textFields = new HashMap<>();

	/** Aktuell aufgeklapptes Dropdown (oder null). */
	private EnumSetting<?> openDropdown;

	/** Slider, der gerade gezogen wird. */
	private Setting<?> draggingSlider;

	private double scroll;

	private int panelX;
	private int panelY;
	private int panelWidth;
	private int panelHeight;

	public ModuleDetailScreen(Screen parent, Module module) {
		super(Component.literal(module.getDisplayName()));
		this.parent = parent;
		this.module = module;
	}

	@Override
	protected void init() {
		panelWidth = Math.min(400, this.width - 40);
		panelHeight = Math.min(280, this.height - 40);
		panelX = (this.width - panelWidth) / 2;
		panelY = (this.height - panelHeight) / 2;

		// Textfelder fuer alle StringSettings anlegen
		textFields.clear();

		for (Setting<?> setting : module.getSettings()) {
			if (setting instanceof StringSetting stringSetting) {
				TextField field = new TextField(stringSetting.getDefaultValue(), stringSetting.getMaxLength());
				field.setValue(stringSetting.get());
				field.onChange(() -> {
					stringSetting.set(field.getValue());
					ConfigManager.save();
				});

				textFields.put(setting.getId(), field);
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		extractBlurredBackground(g);
		g.fill(0, 0, this.width, this.height, Theme.OVERLAY);
	}

	// ---------------------------------------------------------------
	// Zeichnen
	// ---------------------------------------------------------------

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(g, mouseX, mouseY, partialTick);

		Draw.glow(g, panelX, panelY, panelWidth, panelHeight, Theme.CORNER_CUT, Theme.ACCENT, 3);
		Draw.cutCornerRect(g, panelX, panelY, panelWidth, panelHeight, Theme.CORNER_CUT, Theme.BASE);
		Draw.cutCornerOutline(g, panelX, panelY, panelWidth, panelHeight, Theme.CORNER_CUT,
				Theme.withAlpha(Theme.ACCENT, 130));

		renderHeader(g, mouseX, mouseY);

		int contentTop = panelY + 54;

		g.enableScissor(panelX, contentTop, panelX + panelWidth, panelY + panelHeight);

		int y = (int) (contentTop + 4 - scroll);

		for (Setting<?> setting : module.getSettings()) {
			if (!setting.isVisible()) {
				continue;
			}

			renderSetting(g, setting, panelX + PADDING, y, panelWidth - PADDING * 2, mouseX, mouseY);
			y += ROW_HEIGHT;
		}

		g.disableScissor();

		// Aufgeklapptes Dropdown zuletzt zeichnen, damit es ueber allem liegt
		if (openDropdown != null) {
			renderDropdownOptions(g, mouseX, mouseY);
		}
	}

	private void renderHeader(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelX + PADDING;

		// Zurueck-Pfeil
		boolean backHovered = Draw.isHovered(mouseX, mouseY, x - 4, panelY + 10, 20, 14);
		Draw.textFlat(g, "<", x, panelY + 12, backHovered ? Theme.ACCENT_2 : Theme.TEXT_DIM);

		Draw.textFlat(g, module.getDisplayName(), x + 14, panelY + 12, Theme.TEXT);
		Draw.textFlat(g, Draw.truncate(module.getDescription(), panelWidth - 40),
				x + 14, panelY + 24, Theme.TEXT_DIM);

		// ON/OFF-Schalter oben rechts
		int toggleX = panelX + panelWidth - PADDING - 40;
		int toggleY = panelY + 12;
		renderToggle(g, toggleX, toggleY, module.isEnabled(), module.isLocked(), mouseX, mouseY);

		// "Zuruecksetzen"
		int resetX = panelX + panelWidth - PADDING - 110;
		boolean resetHovered = Draw.isHovered(mouseX, mouseY, resetX, panelY + 32, 64, 14);

		Draw.textFlat(g, "Zuruecksetzen", resetX, panelY + 34,
				resetHovered ? Theme.ACCENT_2 : Theme.TEXT_MUTED);

		Draw.separator(g, panelX + 8, panelY + 50, panelWidth - 16, Theme.withAlpha(Theme.ACCENT, 110));
	}

	/** Der wiederverwendete ON/OFF-Schalter. */
	private void renderToggle(GuiGraphicsExtractor g, int x, int y, boolean on, boolean locked,
			int mouseX, int mouseY) {
		int width = 40;
		int height = 16;

		boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, height);

		int background = locked
				? Theme.withAlpha(Theme.LOCKED, 90)
				: (on ? Theme.withAlpha(Theme.ACCENT, 190) : Theme.SURFACE_HIGH);

		Draw.cutCornerRect(g, x, y, width, height, 4, background);
		Draw.cutCornerOutline(g, x, y, width, height, 4,
				Theme.withAlpha(on ? Theme.ACCENT_2 : Theme.TEXT_MUTED, hovered ? 200 : 110));

		// Schieber
		int knobX = on ? x + width - 14 : x + 2;
		Draw.rect(g, knobX, y + 2, 12, height - 4, on ? Theme.TEXT : Theme.TEXT_MUTED);

		String label = locked ? "LOCK" : (on ? "ON" : "OFF");
		Draw.textFlat(g, label, x + (on ? 5 : 17), y + 4, on ? Theme.TEXT : Theme.TEXT_DIM);
	}

	/** Zeichnet eine Zeile - je nach Setting-Typ ein anderes Widget. */
	private void renderSetting(GuiGraphicsExtractor g, Setting<?> setting, int x, int y, int width,
			int mouseX, int mouseY) {
		Draw.textFlat(g, setting.getDisplayName(), x, y + 5, Theme.TEXT_DIM);

		int controlX = x + width / 2;
		int controlWidth = width / 2;

		if (setting instanceof BoolSetting boolSetting) {
			renderToggle(g, controlX + controlWidth - 40, y, boolSetting.value(), false, mouseX, mouseY);

		} else if (setting instanceof DoubleSetting doubleSetting) {
			renderSlider(g, controlX, y, controlWidth, doubleSetting.getFraction(),
					String.format("%.2f", doubleSetting.value()), mouseX, mouseY);

		} else if (setting instanceof IntSetting intSetting) {
			renderSlider(g, controlX, y, controlWidth, intSetting.getFraction(),
					String.valueOf(intSetting.value()), mouseX, mouseY);

		} else if (setting instanceof EnumSetting<?> enumSetting) {
			renderDropdown(g, controlX, y, controlWidth, enumSetting, mouseX, mouseY);

		} else if (setting instanceof ColorSetting colorSetting) {
			renderColorPicker(g, controlX, y, controlWidth, colorSetting, mouseX, mouseY);

		} else if (setting instanceof StringSetting) {
			TextField field = textFields.get(setting.getId());

			if (field != null) {
				field.setBounds(controlX, y, controlWidth, 16);
				field.render(g, mouseX, mouseY);
			}
		}
	}

	private void renderSlider(GuiGraphicsExtractor g, int x, int y, int width, double fraction,
			String label, int mouseX, int mouseY) {
		int trackY = y + 7;
		int trackWidth = width - 40;

		// Spur
		Draw.rect(g, x, trackY, trackWidth, 2, Theme.SURFACE_HIGH);

		// gefuellter Teil in der Akzentfarbe
		int filled = (int) (trackWidth * fraction);
		Draw.rect(g, x, trackY, filled, 2, Theme.ACCENT);

		// Griff
		int knobX = x + filled;
		boolean hovered = Draw.isHovered(mouseX, mouseY, knobX - 3, trackY - 4, 7, 10);

		Draw.rect(g, knobX - 2, trackY - 3, 4, 8, hovered ? Theme.ACCENT_2 : Theme.TEXT);

		Draw.textRight(g, label, x + width, y + 4, Theme.TEXT_DIM);
	}

	private void renderDropdown(GuiGraphicsExtractor g, int x, int y, int width,
			EnumSetting<?> setting, int mouseX, int mouseY) {
		boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, 16);

		Draw.cutCornerRect(g, x, y, width, 16, 4, Theme.SURFACE_HIGH);
		Draw.cutCornerOutline(g, x, y, width, 16, 4,
				Theme.withAlpha(hovered ? Theme.ACCENT : Theme.TEXT_MUTED, 110));

		Draw.textFlat(g, setting.getSelectedName(), x + 5, y + 4, Theme.TEXT);
		Draw.textFlat(g, "v", x + width - 10, y + 4, Theme.TEXT_DIM);
	}

	/** Die aufgeklappte Optionsliste eines Dropdowns. */
	private void renderDropdownOptions(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int[] bounds = getDropdownBounds(openDropdown);

		if (bounds == null) {
			return;
		}

		int x = bounds[0];
		int y = bounds[1] + 17;
		int width = bounds[2];

		String[] names = openDropdown.getOptionNames();

		Draw.cutCornerRect(g, x, y, width, names.length * 14 + 2, 4, Theme.SURFACE_HIGH);
		Draw.cutCornerOutline(g, x, y, width, names.length * 14 + 2, 4,
				Theme.withAlpha(Theme.ACCENT, 150));

		for (int i = 0; i < names.length; i++) {
			int optionY = y + 2 + i * 14;
			boolean hovered = Draw.isHovered(mouseX, mouseY, x, optionY, width, 14);
			boolean selected = i == openDropdown.getSelectedIndex();

			if (hovered) {
				Draw.rect(g, x + 1, optionY, width - 2, 13, Theme.withAlpha(Theme.ACCENT, 60));
			}

			Draw.textFlat(g, names[i], x + 5, optionY + 3,
					selected ? Theme.ACCENT_2 : (hovered ? Theme.TEXT : Theme.TEXT_DIM));
		}
	}

	private void renderColorPicker(GuiGraphicsExtractor g, int x, int y, int width,
			ColorSetting setting, int mouseX, int mouseY) {
		// Farbvorschau
		Draw.cutCornerRect(g, x, y, 24, 16, 4, setting.value());
		Draw.cutCornerOutline(g, x, y, 24, 16, 4, Theme.withAlpha(Theme.TEXT, 80));

		// Regenbogen-Leiste zum Auswaehlen des Farbtons
		int barX = x + 30;
		int barWidth = width - 70;

		for (int i = 0; i < barWidth; i++) {
			float hue = i / (float) barWidth;
			int rgb = java.awt.Color.HSBtoRGB(hue, 0.75f, 1.0f);
			Draw.rect(g, barX + i, y + 4, 1, 8, 0xFF000000 | rgb);
		}

		Draw.textRight(g, String.format("#%06X", setting.value() & 0xFFFFFF),
				x + width, y + 4, Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		double mouseX = event.x();
		double mouseY = event.y();

		// Offenes Dropdown zuerst behandeln - es liegt ueber allem
		if (openDropdown != null && handleDropdownClick(mouseX, mouseY)) {
			return true;
		}

		// Zurueck
		if (Draw.isHovered(mouseX, mouseY, panelX + PADDING - 4, panelY + 10, 20, 14)) {
			this.minecraft.gui.setScreen(parent);
			return true;
		}

		// ON/OFF
		int toggleX = panelX + panelWidth - PADDING - 40;

		if (Draw.isHovered(mouseX, mouseY, toggleX, panelY + 12, 40, 16)) {
			module.toggle();
			ConfigManager.save();
			return true;
		}

		// Zuruecksetzen
		int resetX = panelX + panelWidth - PADDING - 110;

		if (Draw.isHovered(mouseX, mouseY, resetX, panelY + 32, 64, 14)) {
			module.resetToDefaults();
			init();
			ConfigManager.save();
			return true;
		}

		if (handleSettingClick(mouseX, mouseY)) {
			return true;
		}

		return super.mouseClicked(event, doubled);
	}

	private boolean handleSettingClick(double mouseX, double mouseY) {
		int y = (int) (panelY + 58 - scroll);
		int x = panelX + PADDING;
		int width = panelWidth - PADDING * 2;

		int controlX = x + width / 2;
		int controlWidth = width / 2;

		for (Setting<?> setting : module.getSettings()) {
			if (!setting.isVisible()) {
				continue;
			}

			if (setting instanceof BoolSetting boolSetting) {
				if (Draw.isHovered(mouseX, mouseY, controlX + controlWidth - 40, y, 40, 16)) {
					boolSetting.toggle();
					ConfigManager.save();
					return true;
				}

			} else if (setting instanceof DoubleSetting || setting instanceof IntSetting) {
				if (Draw.isHovered(mouseX, mouseY, controlX, y, controlWidth - 40, 16)) {
					draggingSlider = setting;
					applySlider(setting, mouseX, controlX, controlWidth - 40);
					return true;
				}

			} else if (setting instanceof EnumSetting<?> enumSetting) {
				if (Draw.isHovered(mouseX, mouseY, controlX, y, controlWidth, 16)) {
					openDropdown = openDropdown == enumSetting ? null : enumSetting;
					return true;
				}

			} else if (setting instanceof ColorSetting colorSetting) {
				int barX = controlX + 30;
				int barWidth = controlWidth - 70;

				if (Draw.isHovered(mouseX, mouseY, barX, y + 2, barWidth, 12)) {
					float hue = (float) ((mouseX - barX) / barWidth);
					int rgb = java.awt.Color.HSBtoRGB(Math.max(0f, Math.min(1f, hue)), 0.75f, 1.0f);

					colorSetting.setRgb((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
					ConfigManager.save();
					return true;
				}

			} else if (setting instanceof StringSetting) {
				TextField field = textFields.get(setting.getId());

				if (field != null && field.mouseClicked(mouseX, mouseY)) {
					return true;
				}
			}

			y += ROW_HEIGHT;
		}

		return false;
	}

	private boolean handleDropdownClick(double mouseX, double mouseY) {
		int[] bounds = getDropdownBounds(openDropdown);

		if (bounds == null) {
			openDropdown = null;
			return false;
		}

		int x = bounds[0];
		int y = bounds[1] + 17;
		int width = bounds[2];

		String[] names = openDropdown.getOptionNames();

		for (int i = 0; i < names.length; i++) {
			if (Draw.isHovered(mouseX, mouseY, x, y + 2 + i * 14, width, 14)) {
				openDropdown.selectIndex(i);
				openDropdown = null;
				ConfigManager.save();
				return true;
			}
		}

		// Klick daneben schliesst das Dropdown
		openDropdown = null;
		return false;
	}

	/** Bildschirmposition eines Dropdowns: {x, y, breite}. */
	private int[] getDropdownBounds(Setting<?> target) {
		int y = (int) (panelY + 58 - scroll);
		int x = panelX + PADDING;
		int width = panelWidth - PADDING * 2;

		for (Setting<?> setting : module.getSettings()) {
			if (!setting.isVisible()) {
				continue;
			}

			if (setting == target) {
				return new int[] { x + width / 2, y, width / 2 };
			}

			y += ROW_HEIGHT;
		}

		return null;
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (draggingSlider != null) {
			int width = panelWidth - PADDING * 2;
			int controlX = panelX + PADDING + width / 2;

			applySlider(draggingSlider, event.x(), controlX, width / 2 - 40);
			return true;
		}

		return super.mouseDragged(event, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (draggingSlider != null) {
			draggingSlider = null;
			ConfigManager.save();
			return true;
		}

		return super.mouseReleased(event);
	}

	/** Rechnet die Mausposition in einen Slider-Wert um. */
	private void applySlider(Setting<?> setting, double mouseX, int trackX, int trackWidth) {
		double fraction = Math.max(0.0, Math.min(1.0, (mouseX - trackX) / trackWidth));

		if (setting instanceof DoubleSetting doubleSetting) {
			doubleSetting.setFraction(fraction);
		} else if (setting instanceof IntSetting intSetting) {
			intSetting.setFraction(fraction);
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		scroll = Math.max(0, scroll - scrollY * 18);
		return true;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		for (TextField field : textFields.values()) {
			if (field.keyPressed(event.key())) {
				return true;
			}
		}

		if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
			this.minecraft.gui.setScreen(parent);
			return true;
		}

		return super.keyPressed(event);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		char typed = (char) event.codepoint();

		for (TextField field : textFields.values()) {
			if (field.charTyped(typed)) {
				return true;
			}
		}

		return super.charTyped(event);
	}

	@Override
	public void onClose() {
		ConfigManager.save();
		this.minecraft.gui.setScreen(parent);
	}

	/** Nur damit die Liste der Settings auch von aussen lesbar ist. */
	public List<Setting<?>> getSettings() {
		return module.getSettings();
	}
}
