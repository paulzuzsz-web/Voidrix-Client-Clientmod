package dev.voidrix.module.hud;

import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * A crosshair you can actually see.
 *
 * <p>Drawn as a widget rather than by replacing the vanilla one, which means it can be moved,
 * scaled and recoloured like everything else. Switch the vanilla crosshair off under Clean HUD to
 * avoid drawing both.
 */
public final class CrosshairHud extends HudModule {
    private final EnumSetting<Style> style;
    private final IntSetting size;
    private final IntSetting thickness;
    private final IntSetting gap;
    private final ColorSetting color;
    private final BoolSetting outline;
    private final BoolSetting centreDot;

    public CrosshairHud() {
        super("crosshair", "Crosshair", "A crosshair you can size and colour yourself", 0.5, 0.5);
        this.style = addEnum("style", "Style", "Shape of the crosshair", Style.CROSS);
        this.size = addInt("size", "Size", "How far each arm reaches", 5, 1, 16, "px");
        this.thickness = addInt("thickness", "Thickness", "Width of each arm", 1, 1, 5, "px");
        this.gap = addInt("gap", "Centre gap", "Space left open in the middle", 2, 0, 10, "px");
        this.color = addColor("color", "Colour", "Colour of the crosshair", 0xFFFFFFFF);
        this.outline = addBool("outline", "Outline", "Dark edge so it reads against any background", true);
        this.centreDot = addBool("dot", "Centre dot", "Add a dot in the middle", false);

        // The gap is meaningless without arms to leave a gap between.
        gap.visibleWhen(() -> style.value() != Style.DOT);
    }

    @Override
    public boolean enabledByDefault() {
        return false;
    }

    /** The widget is a square big enough for the whole shape. */
    private int extent() {
        return (size.value() + gap.value()) * 2 + thickness.value();
    }

    @Override
    public int contentWidth(Font font) {
        return extent();
    }

    @Override
    public int contentHeight(Font font) {
        return extent();
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        float c = extent() / 2f;
        int t = thickness.value();
        int len = size.value();
        int gapValue = style.value() == Style.DOT ? 0 : gap.value();
        int tint = color.resolve();

        switch (style.value()) {
            case CROSS -> {
                arm(g, c - t / 2f, c - gapValue - len, t, len, tint);
                arm(g, c - t / 2f, c + gapValue, t, len, tint);
                arm(g, c - gapValue - len, c - t / 2f, len, t, tint);
                arm(g, c + gapValue, c - t / 2f, len, t, tint);
            }
            case T_SHAPE -> {
                arm(g, c - t / 2f, c + gapValue, t, len, tint);
                arm(g, c - gapValue - len, c - t / 2f, len, t, tint);
                arm(g, c + gapValue, c - t / 2f, len, t, tint);
            }
            case CIRCLE -> {
                Draw.roundRectOutline(g, c - len, c - len, len * 2, len * 2, len, t, tint);
            }
            case DOT -> Draw.circle(g, c, c, Math.max(1f, t), tint);
        }

        if (centreDot.value() && style.value() != Style.DOT) {
            Draw.circle(g, c, c, Math.max(0.8f, t * 0.6f), tint);
        }
    }

    /** One arm, with an optional dark border drawn behind it. */
    private void arm(GuiGraphicsExtractor g, float x, float y, float w, float h, int tint) {
        if (outline.value()) {
            Draw.rect(g, x - 1, y - 1, w + 2, h + 2, Theme.alpha(0xFF000000, 0.65f));
        }
        Draw.rect(g, x, y, w, h, tint);
    }

    public enum Style {
        CROSS,
        T_SHAPE,
        CIRCLE,
        DOT
    }
}
