package dev.voidrix.ui;

import dev.voidrix.module.Category;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * The interface's glyphs, drawn rather than shipped.
 *
 * <p>Each icon is a handful of rectangles and circles built from {@link Draw}, which keeps the jar
 * free of texture atlases, lets every glyph inherit the current accent colour, and means they stay
 * crisp at any GUI scale instead of blurring like a bitmap would.
 *
 * <p>All of them draw inside a {@code size} x {@code size} box with its top-left at (x, y).
 */
public final class Icons {
    private Icons() {
    }

    /** A readout panel: a frame with two bars inside. */
    public static void hud(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        Draw.roundRectOutline(g, x, y + s * 0.15f, s, s * 0.7f, s * 0.14f, 1f, color);
        Draw.roundRect(g, x + s * 0.16f, y + s * 0.34f, s * 0.5f, s * 0.1f, s * 0.05f, color);
        Draw.roundRect(g, x + s * 0.16f, y + s * 0.54f, s * 0.32f, s * 0.1f, s * 0.05f, color);
    }

    /** Two crossed blades. */
    public static void combat(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        diagonal(g, x + s * 0.18f, y + s * 0.82f, x + s * 0.82f, y + s * 0.18f, color, s * 0.11f);
        diagonal(g, x + s * 0.18f, y + s * 0.18f, x + s * 0.82f, y + s * 0.82f, color, s * 0.11f);
    }

    /** A sun: filled centre with rays. */
    public static void visual(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        float cx = x + s / 2f;
        float cy = y + s / 2f;
        Draw.circle(g, cx, cy, s * 0.2f, color);
        for (int i = 0; i < 8; i++) {
            double a = Math.PI * 2 * i / 8.0;
            float rx = cx + (float) Math.cos(a) * s * 0.36f;
            float ry = cy + (float) Math.sin(a) * s * 0.36f;
            Draw.circle(g, rx, ry, s * 0.055f, color);
        }
    }

    /** A window with a title bar. */
    public static void iface(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        Draw.roundRectOutline(g, x + s * 0.08f, y + s * 0.16f, s * 0.84f, s * 0.68f, s * 0.12f, 1f, color);
        Draw.roundRect(g, x + s * 0.08f, y + s * 0.16f, s * 0.84f, s * 0.18f, s * 0.06f, color);
    }

    /** Three dots. */
    public static void misc(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        float cy = y + s / 2f;
        Draw.circle(g, x + s * 0.24f, cy, s * 0.08f, color);
        Draw.circle(g, x + s * 0.5f, cy, s * 0.08f, color);
        Draw.circle(g, x + s * 0.76f, cy, s * 0.08f, color);
    }

    /** A map pin. */
    public static void waypoint(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        float cx = x + s / 2f;
        Draw.circle(g, cx, y + s * 0.38f, s * 0.26f, color);
        Draw.circle(g, cx, y + s * 0.38f, s * 0.11f, Theme.SURFACE);
        // Tapering stem down to the point.
        for (int i = 0; i < 6; i++) {
            float t = i / 5f;
            float w = s * (0.2f - 0.16f * t);
            Draw.rect(g, cx - w / 2f, y + s * (0.58f + 0.06f * i), w, s * 0.07f, color);
        }
    }

    /** A person, for the profiles tab. */
    public static void profile(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        float cx = x + s / 2f;
        Draw.circle(g, cx, y + s * 0.32f, s * 0.18f, color);
        Draw.roundRect(g, cx - s * 0.3f, y + s * 0.58f, s * 0.6f, s * 0.28f, s * 0.14f, color);
    }

    /** A magnifying glass. */
    public static void search(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        float cx = x + s * 0.42f;
        float cy = y + s * 0.42f;
        Draw.roundRectOutline(g, cx - s * 0.26f, cy - s * 0.26f, s * 0.52f, s * 0.52f, s * 0.26f, 1f, color);
        diagonal(g, cx + s * 0.2f, cy + s * 0.2f, x + s * 0.82f, y + s * 0.82f, color, s * 0.1f);
    }

    /** A left-pointing chevron, for going back. */
    public static void back(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        diagonal(g, x + s * 0.62f, y + s * 0.2f, x + s * 0.34f, y + s * 0.5f, color, s * 0.11f);
        diagonal(g, x + s * 0.34f, y + s * 0.5f, x + s * 0.62f, y + s * 0.8f, color, s * 0.11f);
    }

    /** A plus sign. */
    public static void plus(GuiGraphicsExtractor g, float x, float y, float s, int color) {
        Draw.roundRect(g, x + s * 0.5f - s * 0.05f, y + s * 0.22f, s * 0.1f, s * 0.56f, s * 0.05f, color);
        Draw.roundRect(g, x + s * 0.22f, y + s * 0.5f - s * 0.05f, s * 0.56f, s * 0.1f, s * 0.05f, color);
    }

    /** The Voidrix mark: a gradient V. */
    public static void brand(GuiGraphicsExtractor g, float x, float y, float s) {
        diagonal(g, x + s * 0.22f, y + s * 0.2f, x + s * 0.5f, y + s * 0.8f, Theme.ACCENT, s * 0.13f);
        diagonal(g, x + s * 0.78f, y + s * 0.2f, x + s * 0.5f, y + s * 0.8f, Theme.ACCENT_ALT, s * 0.13f);
    }

    /** Picks the glyph that belongs to a module category. */
    public static void forCategory(GuiGraphicsExtractor g, Category category, float x, float y,
                                   float s, int color) {
        switch (category) {
            case HUD -> hud(g, x, y, s, color);
            case COMBAT -> combat(g, x, y, s, color);
            case VISUAL -> visual(g, x, y, s, color);
            case INTERFACE -> iface(g, x, y, s, color);
            case MISC -> misc(g, x, y, s, color);
        }
    }

    /**
     * Draws a thick line between two points by stepping little squares along it. Crude, but with
     * only axis-aligned fills available it is the honest way to get a diagonal, and at icon sizes
     * the overlap reads as a solid stroke.
     */
    private static void diagonal(GuiGraphicsExtractor g, float x1, float y1, float x2, float y2,
                                 int color, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        int steps = Math.max(2, (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)) * 2f));
        float r = Math.max(0.5f, thickness / 2f);
        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            Draw.circle(g, x1 + dx * t, y1 + dy * t, r, color);
        }
    }
}
