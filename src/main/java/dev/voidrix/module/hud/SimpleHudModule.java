package dev.voidrix.module.hud;

import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Base for the many widgets that are just "a label and a value on one line".
 *
 * <p>Keeping them on a shared implementation is what makes the HUD look like one system: every
 * one of these picks up the same label styling, the same colour options and the same panel.
 */
public abstract class SimpleHudModule extends HudModule {
    private final BoolSetting showLabel;
    protected final ColorSetting valueColor;

    protected SimpleHudModule(String id, String name, String description,
                              double defaultX, double defaultY) {
        this(id, name, description, defaultX, defaultY, Theme.TEXT);
    }

    protected SimpleHudModule(String id, String name, String description,
                              double defaultX, double defaultY, int defaultColor) {
        this(id, name, description, dev.voidrix.module.Category.HUD, defaultX, defaultY, defaultColor);
    }

    protected SimpleHudModule(String id, String name, String description,
                              dev.voidrix.module.Category category,
                              double defaultX, double defaultY, int defaultColor) {
        super(id, name, description, category, defaultX, defaultY);
        this.showLabel = addBool("show_label", "Show label", "Prefix the value with its name", true);
        this.valueColor = addColor("color", "Value colour", "Colour of the value text", defaultColor);
    }

    /** Short prefix shown before the value, e.g. {@code FPS}. */
    protected abstract String label();

    /** The value itself, recomputed every frame. */
    protected abstract String value();

    /** Colour of the value; override for widgets that grade their value. */
    protected int valueColor() {
        return valueColor.resolve();
    }

    private String labelText() {
        return label() + " ";
    }

    @Override
    public int contentWidth(Font font) {
        int width = font.width(value());
        if (showLabel.value()) {
            width += font.width(labelText());
        }
        return width;
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        float x = 0f;
        if (showLabel.value()) {
            String text = labelText();
            if (useShadow()) {
                Draw.textShadow(g, font, text, x, 0, Theme.TEXT_DIM);
            } else {
                Draw.text(g, font, text, x, 0, Theme.TEXT_DIM);
            }
            x += font.width(text);
        }
        String value = value();
        if (useShadow()) {
            Draw.textShadow(g, font, value, x, 0, valueColor());
        } else {
            Draw.text(g, font, value, x, 0, valueColor());
        }
    }
}
