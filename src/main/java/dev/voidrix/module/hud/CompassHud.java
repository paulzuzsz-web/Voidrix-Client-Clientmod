package dev.voidrix.module.hud;

import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * A sliding compass strip, the kind first-person games put across the top of the screen.
 *
 * <p>Marks slide past a fixed centre needle as you turn. Everything is positioned from the shortest
 * signed angle to each mark, so the strip wraps seamlessly through north instead of snapping when
 * the yaw crosses 180 degrees.
 */
public final class CompassHud extends HudModule {
    private static final String[] CARDINALS = {"N", "E", "S", "W"};
    private static final int[] CARDINAL_ANGLES = {180, 270, 0, 90};

    private final IntSetting width;
    private final IntSetting span;
    private final BoolSetting showDegrees;
    private final BoolSetting showTicks;

    public CompassHud() {
        super("compass", "Compass", "A sliding compass strip with the direction you face", 0.5, 0.02);
        this.width = addInt("width", "Width", "Width of the strip in pixels", 140, 60, 320, "px");
        this.span = addInt("span", "Field", "How many degrees the strip covers", 120, 60, 260, "°");
        this.showDegrees = addBool("degrees", "Show degrees", "Print the exact bearing under the needle", true);
        this.showTicks = addBool("ticks", "Minor ticks", "Mark the halfway points between cardinals", true);
    }

    /** Bearing in 0..360, where 0 is north. */
    private static float bearing() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return 0f;
        }
        // Minecraft's yaw has 0 pointing south, and grows clockwise.
        float yaw = player.getYRot() % 360f;
        float compass = (yaw + 180f) % 360f;
        return compass < 0 ? compass + 360f : compass;
    }

    /** Shortest signed difference from the current bearing to {@code target}, in -180..180. */
    private static float delta(float target) {
        float d = (target - bearing() + 540f) % 360f - 180f;
        return d;
    }

    @Override
    public int contentWidth(Font font) {
        return width.value();
    }

    @Override
    public int contentHeight(Font font) {
        return font.lineHeight + (showDegrees.value() ? font.lineHeight + 2 : 0) + 6;
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        int w = width.value();
        float halfSpan = span.value() / 2f;
        float pixelsPerDegree = w / (float) span.value();
        float centre = w / 2f;
        float rowY = 4f;

        // Base line.
        Draw.roundRect(g, 0, rowY + font.lineHeight + 1, w, 1, 0.5f, Theme.alpha(Theme.BORDER, 0.9f));

        // Minor ticks every 15 degrees.
        if (showTicks.value()) {
            for (int angle = 0; angle < 360; angle += 15) {
                float d = delta(angle);
                if (Math.abs(d) > halfSpan) {
                    continue;
                }
                float x = centre + d * pixelsPerDegree;
                boolean major = angle % 45 == 0;
                float h = major ? 4f : 2.5f;
                Draw.rect(g, x, rowY + font.lineHeight + 1 - h, 1, h,
                        Theme.alpha(Theme.TEXT_FAINT, major ? 1f : 0.6f));
            }
        }

        // Cardinal labels, fading out towards the edges of the strip.
        for (int i = 0; i < CARDINALS.length; i++) {
            float d = delta(CARDINAL_ANGLES[i]);
            if (Math.abs(d) > halfSpan) {
                continue;
            }
            float x = centre + d * pixelsPerDegree;
            float fade = 1f - Math.min(1f, Math.abs(d) / halfSpan);
            int color = Theme.mix(Theme.TEXT_FAINT, Theme.TEXT, fade);
            String label = CARDINALS[i];
            Draw.textCentered(g, font, label, x, rowY, color);
        }

        // Fixed needle.
        Draw.rect(g, centre - 0.5f, rowY - 2, 1, font.lineHeight + 5, Theme.ACCENT);
        Draw.circle(g, centre, rowY - 2.5f, 1.8f, Theme.ACCENT);

        if (showDegrees.value()) {
            String text = Math.round(bearing()) + "°";
            float ty = rowY + font.lineHeight + 4;
            if (useShadow()) {
                Draw.textShadow(g, font, text, centre - font.width(text) / 2f, ty, Theme.TEXT_DIM);
            } else {
                Draw.text(g, font, text, centre - font.width(text) / 2f, ty, Theme.TEXT_DIM);
            }
        }
    }
}
