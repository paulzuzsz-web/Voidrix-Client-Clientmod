package dev.voidrix.module.combat;

import dev.voidrix.module.Category;
import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Attack cooldown, as a bar that fills back to full.
 *
 * <p>Reads the same vanilla value the fading crosshair indicator uses - this only makes the number
 * legible, it does not change how fast the cooldown recovers.
 */
public final class CooldownHud extends HudModule {
    private final IntSetting barWidth;
    private final BoolSetting showPercent;
    private final BoolSetting hideWhenReady;

    public CooldownHud() {
        super("cooldown", "Attack cooldown", "How far your attack has recharged",
                Category.COMBAT, 0.5, 0.7);
        this.barWidth = addInt("width", "Bar width", "Width of the bar in pixels", 80, 30, 200, "px");
        this.showPercent = addBool("percent", "Show percent", "Print the value next to the bar", true);
        this.hideWhenReady = addBool("hide_ready", "Hide when full", "Only show while recharging", false);
    }

    /** 0..1, where 1 means a fully charged attack. */
    private static float charge() {
        var player = Minecraft.getInstance().player;
        return player == null ? 1f : Math.clamp(player.getAttackStrengthScale(0f), 0f, 1f);
    }

    @Override
    public boolean hasContent() {
        return !hideWhenReady.value() || charge() < 0.999f;
    }

    @Override
    public int contentWidth(Font font) {
        int width = barWidth.value();
        if (showPercent.value()) {
            width += 4 + font.width("100%");
        }
        return width;
    }

    @Override
    public int contentHeight(Font font) {
        return Math.max(font.lineHeight, 6);
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        float charge = charge();
        int w = barWidth.value();
        float y = (contentHeight(font) - 5) / 2f;

        Draw.roundRect(g, 0, y, w, 5, 2.5f, 0xFF23232F);

        // Amber while recharging, accent gradient once the hit would land at full strength.
        if (charge >= 0.999f) {
            Draw.roundRectGradientH(g, 0, y, w, 5, 2.5f, Theme.ACCENT, Theme.ACCENT_ALT);
        } else {
            Draw.roundRect(g, 0, y, w * charge, 5, 2.5f, Theme.WARN);
        }

        if (showPercent.value()) {
            String text = Math.round(charge * 100f) + "%";
            int color = charge >= 0.999f ? Theme.SUCCESS : Theme.TEXT_DIM;
            float tx = w + 4;
            float ty = (contentHeight(font) - font.lineHeight) / 2f;
            if (useShadow()) {
                Draw.textShadow(g, font, text, tx, ty, color);
            } else {
                Draw.text(g, font, text, tx, ty, color);
            }
        }
    }
}
