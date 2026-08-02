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
import java.util.Map;

/**
 * Detailseite eines Moduls.
 *
 * <pre>
 *  &lt; CPS                              [ ON ] [ ZURUECKSETZEN ]
 *  Zeigt deine Klicks pro Sekunde
 *  ----------------------------------------------------------
 *  HUD-SKALIERUNG              [ 1.00 ] x  =========o------
 *  BACKGROUND                            [ BLUR          v ]
 *  TEXT                                  [ {left} {right}  ]
 *  COLOR                                                 [#]
 *  ECKEN ABRUNDEN                                        [x]
 * </pre>
 *
 * <p>Die Oberflaeche wird vollstaendig <b>generisch</b> aus den Einstellungen
 * des Moduls aufgebaut: Je nach Setting-Typ entsteht ein Toggle, ein Slider mit
 * Zahlenfeld, ein Dropdown, ein Farbfeld oder ein Textfeld. Ein neues Setting
 * im Modul erscheint hier also automatisch.</p>
 */
public class ModuleDetailScreen extends Screen {

	private static final int ROW_HEIGHT = 26;
	private static final int PADDING = 14;

	/** Breite des Zahlenfeldes vor einem Slider. */
	private static final int VALUE_BOX = 36;

	private final Screen parent;
	private final Module module;

	private final Map<String, TextField> textFields = new HashMap<>();

	private EnumSetting<?> openDropdown;
	private Setting<?> draggingSlider;

	private double scroll;
	private int contentHeight;

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
		panelWidth = Math.min(460, this.width - 30);
		panelHeight = Math.min(300, this.height - 30);
		panelX = (this.width - panelWidth) / 2;
		panelY = (this.height - panelHeight) / 2;

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

		Draw.panel(g, panelX, panelY, panelWidth, panelHeight);
		renderHeader(g, mouseX, mouseY);

		int contentTop = panelY + 52;
		int viewHeight = panelHeight - 52 - 8;

		g.enableScissor(panelX, contentTop, panelX + panelWidth, contentTop + viewHeight);

		int y = (int) (contentTop + 4 - scroll);
		int visible = 0;

		for (Setting<?> setting : module.getSettings()) {
			if (!setting.isVisible()) {
				continue;
			}

			renderSetting(g, setting, y, mouseX, mouseY);
			y += ROW_HEIGHT;
			visible++;
		}

		contentHeight = visible * ROW_HEIGHT + 8;
		g.disableScissor();

		Draw.scrollbar(g, panelX + panelWidth - 5, contentTop + 2, viewHeight - 4, contentHeight, scroll);

		// Aufgeklapptes Dropdown zuletzt - es liegt ueber allem
		if (openDropdown != null) {
			renderDropdownOptions(g, mouseX, mouseY);
		}
	}

	private void renderHeader(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelX + PADDING;
		int y = panelY + 12;

		boolean backHovered = Draw.isHovered(mouseX, mouseY, x - 4, y - 4, 16, 16);
		Draw.textFlat(g, "<", x, y, backHovered ? Theme.ACCENT : Theme.TEXT_DIM);

		Draw.textFlat(g, Draw.upper(module.getDisplayName()), x + 12, y, Theme.TEXT);

		// ON/OFF und Zuruecksetzen rechts oben
		int resetWidth = Draw.textWidth("ZURUECKSETZEN") + 12;
		int resetX = panelX + panelWidth - PADDING - resetWidth;
		int toggleX = resetX - 40;

		boolean toggleHovered = Draw.isHovered(mouseX, mouseY, toggleX, y - 4, 34, 16);
		Draw.toggle(g, toggleX, y - 4, 34, 16, module.isEnabled(), module.isLocked(), toggleHovered);

		boolean resetHovered = Draw.isHovered(mouseX, mouseY, resetX, y - 4, resetWidth, 16);
		Draw.roundedRect(g, resetX, y - 4, resetWidth, 16, Theme.RADIUS_SMALL,
				resetHovered ? Theme.CARD_HOVER : Theme.INPUT);
		Draw.roundedOutline(g, resetX, y - 4, resetWidth, 16, Theme.RADIUS_SMALL, Theme.BORDER);
		Draw.textCentered(g, "ZURUECKSETZEN", resetX + resetWidth / 2, y,
				resetHovered ? Theme.TEXT : Theme.TEXT_DIM);

		// Beschreibung darunter
		Draw.textFlat(g, module.getDescription(), x, y + 20, Theme.TEXT_DIM);

		Draw.separator(g, panelX + 8, panelY + 46, panelWidth - 16, Theme.BORDER);
	}

	/** Eine Einstellungszeile: Label links, Bedienelement rechts. */
	private void renderSetting(GuiGraphicsExtractor g, Setting<?> setting, int y, int mouseX, int mouseY) {
		Draw.textFlat(g, Draw.upper(setting.getDisplayName()), panelX + PADDING, y + 5, Theme.TEXT_DIM);

		int right = panelX + panelWidth - PADDING - 4;

		if (setting instanceof BoolSetting boolSetting) {
			boolean hovered = Draw.isHovered(mouseX, mouseY, right - 12, y + 2, 12, 12);
			Draw.checkbox(g, right - 12, y + 2, 12, boolSetting.value(), hovered);

		} else if (setting instanceof DoubleSetting doubleSetting) {
			renderValueSlider(g, right, y, doubleSetting.getFraction(),
					String.format("%.2f", doubleSetting.value()), mouseX, mouseY);

		} else if (setting instanceof IntSetting intSetting) {
			renderValueSlider(g, right, y, intSetting.getFraction(),
					String.valueOf(intSetting.value()), mouseX, mouseY);

		} else if (setting instanceof EnumSetting<?> enumSetting) {
			int width = 150;
			boolean hovered = Draw.isHovered(mouseX, mouseY, right - width, y + 2, width, 16);

			Draw.input(g, right - width, y + 2, width, 16, openDropdown == enumSetting);
			Draw.textFlat(g, Draw.upper(enumSetting.getSelectedName()), right - width + 6, y + 6,
					hovered ? Theme.TEXT : Theme.TEXT_DIM);
			Draw.textFlat(g, "v", right - 12, y + 6, Theme.TEXT_MUTED);

		} else if (setting instanceof ColorSetting colorSetting) {
			// Farbfeld rechts, davor eine schlanke Farbtonleiste
			Draw.roundedRect(g, right - 18, y + 3, 18, 14, 2, colorSetting.value());
			Draw.roundedOutline(g, right - 18, y + 3, 18, 14, 2, Theme.BORDER_STRONG);

			int barX = right - 18 - 92;

			for (int i = 0; i < 88; i++) {
				int rgb = java.awt.Color.HSBtoRGB(i / 88f, 0.72f, 1.0f);
				Draw.rect(g, barX + i, y + 6, 1, 8, 0xFF000000 | rgb);
			}

		} else if (setting instanceof StringSetting) {
			TextField field = textFields.get(setting.getId());

			if (field != null) {
				int width = 150;
				field.setBounds(right - width, y + 2, width, 16);
				field.render(g, mouseX, mouseY);
			}
		}
	}

	/** Zahlenfeld, Trenner und Slider - wie im Vorbild. */
	private void renderValueSlider(GuiGraphicsExtractor g, int right, int y, double fraction,
			String label, int mouseX, int mouseY) {
		int trackWidth = 92;
		int trackX = right - trackWidth;
		int boxX = trackX - 12 - VALUE_BOX;

		Draw.input(g, boxX, y + 2, VALUE_BOX, 16, false);
		Draw.textCentered(g, label, boxX + VALUE_BOX / 2, y + 6, Theme.TEXT);

		Draw.textFlat(g, "x", boxX + VALUE_BOX + 4, y + 6, Theme.TEXT_MUTED);

		int trackY = y + 9;
		Draw.roundedRect(g, trackX, trackY, trackWidth, 2, 1, Theme.withAlpha(0xFF000000, 40));

		int filled = (int) (trackWidth * fraction);
		Draw.roundedRect(g, trackX, trackY, filled, 2, 1, Theme.ACCENT);

		// runder Griff auf der Spur
		boolean hovered = Draw.isHovered(mouseX, mouseY, trackX - 4, y + 2, trackWidth + 8, 16);
		int knobX = trackX + filled - 3;

		Draw.roundedRect(g, knobX, trackY - 3, 7, 7, 3, 0xFFFFFFFF);
		Draw.roundedOutline(g, knobX, trackY - 3, 7, 7, 3,
				hovered ? Theme.ACCENT : Theme.BORDER_STRONG);
	}

	/** Die aufgeklappte Optionsliste eines Dropdowns. */
	private void renderDropdownOptions(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int[] bounds = getDropdownBounds(openDropdown);

		if (bounds == null) {
			return;
		}

		int x = bounds[0];
		int y = bounds[1] + 18;
		int width = bounds[2];

		String[] names = openDropdown.getOptionNames();
		int height = names.length * 14 + 4;

		Draw.roundedRect(g, x, y, width, height, Theme.RADIUS_SMALL, 0xF2FFFFFF);
		Draw.roundedOutline(g, x, y, width, height, Theme.RADIUS_SMALL, Theme.BORDER_STRONG);

		for (int i = 0; i < names.length; i++) {
			int optionY = y + 2 + i * 14;
			boolean hovered = Draw.isHovered(mouseX, mouseY, x, optionY, width, 14);
			boolean selected = i == openDropdown.getSelectedIndex();

			if (hovered) {
				Draw.roundedRect(g, x + 2, optionY, width - 4, 13, 2, Theme.withAlpha(Theme.ACCENT, 45));
			}

			Draw.textFlat(g, Draw.upper(names[i]), x + 6, optionY + 3,
					selected ? Theme.ACCENT : Theme.TEXT_DIM);
		}
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		double mouseX = event.x();
		double mouseY = event.y();

		if (openDropdown != null && handleDropdownClick(mouseX, mouseY)) {
			return true;
		}

		int y = panelY + 12;

		if (Draw.isHovered(mouseX, mouseY, panelX + PADDING - 4, y - 4, 16, 16)) {
			this.minecraft.gui.setScreen(parent);
			return true;
		}

		int resetWidth = Draw.textWidth("ZURUECKSETZEN") + 12;
		int resetX = panelX + panelWidth - PADDING - resetWidth;

		if (Draw.isHovered(mouseX, mouseY, resetX - 40, y - 4, 34, 16)) {
			module.toggle();
			ConfigManager.save();
			return true;
		}

		if (Draw.isHovered(mouseX, mouseY, resetX, y - 4, resetWidth, 16)) {
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
		int y = (int) (panelY + 56 - scroll);
		int right = panelX + panelWidth - PADDING - 4;

		for (Setting<?> setting : module.getSettings()) {
			if (!setting.isVisible()) {
				continue;
			}

			if (setting instanceof BoolSetting boolSetting) {
				if (Draw.isHovered(mouseX, mouseY, right - 16, y, 20, 18)) {
					boolSetting.toggle();
					ConfigManager.save();
					return true;
				}

			} else if (setting instanceof DoubleSetting || setting instanceof IntSetting) {
				int trackX = right - 92;

				if (Draw.isHovered(mouseX, mouseY, trackX - 4, y + 2, 100, 16)) {
					draggingSlider = setting;
					applySlider(setting, mouseX, trackX, 92);
					return true;
				}

			} else if (setting instanceof EnumSetting<?> enumSetting) {
				if (Draw.isHovered(mouseX, mouseY, right - 150, y + 2, 150, 16)) {
					openDropdown = openDropdown == enumSetting ? null : enumSetting;
					return true;
				}

			} else if (setting instanceof ColorSetting colorSetting) {
				int barX = right - 18 - 92;

				if (Draw.isHovered(mouseX, mouseY, barX, y + 2, 88, 16)) {
					float hue = (float) ((mouseX - barX) / 88.0);
					int rgb = java.awt.Color.HSBtoRGB(Math.max(0f, Math.min(1f, hue)), 0.72f, 1.0f);

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
		int y = bounds[1] + 18;
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

		openDropdown = null;
		return false;
	}

	/** Bildschirmposition eines Dropdowns: {x, y, breite}. */
	private int[] getDropdownBounds(Setting<?> target) {
		int y = (int) (panelY + 56 - scroll);
		int right = panelX + panelWidth - PADDING - 4;

		for (Setting<?> setting : module.getSettings()) {
			if (!setting.isVisible()) {
				continue;
			}

			if (setting == target) {
				return new int[] { right - 150, y, 150 };
			}

			y += ROW_HEIGHT;
		}

		return null;
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (draggingSlider != null) {
			int trackX = panelX + panelWidth - PADDING - 4 - 92;
			applySlider(draggingSlider, event.x(), trackX, 92);
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
		int viewHeight = panelHeight - 52 - 8;
		double maxScroll = Math.max(0, contentHeight - viewHeight);

		scroll = Math.max(0, Math.min(maxScroll, scroll - scrollY * 18));
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
}
