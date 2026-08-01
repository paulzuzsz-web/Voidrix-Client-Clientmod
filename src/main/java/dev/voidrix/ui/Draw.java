package dev.voidrix.ui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Drawing primitives for the Voidrix interface.
 *
 * <p>Minecraft only hands us axis-aligned {@code fill} calls, so rounded corners, rings and
 * shadows are all built here by emitting one horizontal span per pixel row. Corner rows get
 * fractional-coverage end pixels, which is what keeps the curves from looking like staircases.
 *
 * <p>The geometry is written against {@link Canvas} rather than Minecraft's graphics object, so
 * the same code that draws the menu in game can be pointed at an image buffer and checked.
 */
public final class Draw {
    private Draw() {
    }

    /** Anything that can fill an axis-aligned rectangle with a packed ARGB colour. */
    @FunctionalInterface
    public interface Canvas {
        void fill(int x1, int y1, int x2, int y2, int argb);
    }

    // -------------------------------------------------------------------------------------
    // Spans
    // -------------------------------------------------------------------------------------

    /**
     * Fills a single pixel row between two fractional x coordinates, feathering the two end
     * pixels by how much of them the span actually covers.
     */
    private static void span(Canvas c, float left, float right, int y, int color) {
        if (right - left <= 0.001f) {
            return;
        }
        if (((color >>> 24) & 0xFF) == 0) {
            return;
        }

        int solidL = (int) Math.ceil(left);
        int solidR = (int) Math.floor(right);

        if (solidR < solidL) {
            // The span sits entirely inside a single pixel: one blend, no edges, or the two edge
            // draws below would both hit that pixel and stack up too dark.
            int px = (int) Math.floor(left);
            c.fill(px, y, px + 1, y + 1, Theme.alpha(color, right - left));
            return;
        }

        if (solidR > solidL) {
            c.fill(solidL, y, solidR, y + 1, color);
        }

        float lCov = solidL - left;
        if (lCov > 0.004f) {
            c.fill(solidL - 1, y, solidL, y + 1, Theme.alpha(color, lCov));
        }
        float rCov = right - solidR;
        if (rCov > 0.004f) {
            c.fill(solidR, y, solidR + 1, y + 1, Theme.alpha(color, rCov));
        }
    }

    /** Horizontal inset of a rounded rectangle at a given distance into the corner zone. */
    private static float inset(float dy, float r) {
        if (dy <= 0f || r <= 0f) {
            return 0f;
        }
        if (dy >= r) {
            return r;
        }
        return r - (float) Math.sqrt(r * r - dy * dy);
    }

    /** Distance of a row centre into the nearer corner zone, or 0 when in the straight middle. */
    private static float cornerDy(float rowCentre, float y, float h, float r) {
        float top = (y + r) - rowCentre;
        float bottom = rowCentre - (y + h - r);
        return Math.max(0f, Math.max(top, bottom));
    }

    // -------------------------------------------------------------------------------------
    // Fills
    // -------------------------------------------------------------------------------------

    public static void rect(Canvas c, float x, float y, float w, float h, int color) {
        c.fill(Math.round(x), Math.round(y), Math.round(x + w), Math.round(y + h), color);
    }

    public static void rect(GuiGraphicsExtractor g, float x, float y, float w, float h, int color) {
        rect(g::fill, x, y, w, h, color);
    }

    public static void roundRect(Canvas c, float x, float y, float w, float h, float r, int color) {
        roundRectGradient(c, x, y, w, h, r, color, color);
    }

    public static void roundRect(GuiGraphicsExtractor g, float x, float y, float w, float h, float r, int color) {
        roundRectGradient(g::fill, x, y, w, h, r, color, color);
    }

    /**
     * Rounded rectangle with a vertical gradient. The colour is interpolated per row, which also
     * gives us flat fills for free when both stops match.
     */
    public static void roundRectGradient(Canvas c, float x, float y, float w, float h,
                                         float r, int top, int bottom) {
        if (w <= 0f || h <= 0f) {
            return;
        }
        float rad = Math.min(r, Math.min(w, h) * 0.5f);
        int y0 = Math.round(y);
        int y1 = Math.round(y + h);
        boolean flat = top == bottom;

        for (int iy = y0; iy < y1; iy++) {
            float centre = iy + 0.5f;
            float ins = inset(cornerDy(centre, y, h, rad), rad);
            int color = flat ? top : Theme.mix(top, bottom, (centre - y) / h);
            span(c, x + ins, x + w - ins, iy, color);
        }
    }

    public static void roundRectGradient(GuiGraphicsExtractor g, float x, float y, float w, float h,
                                         float r, int top, int bottom) {
        roundRectGradient(g::fill, x, y, w, h, r, top, bottom);
    }

    /** Rounded rectangle with a horizontal gradient, for accent bars and progress fills. */
    public static void roundRectGradientH(Canvas c, float x, float y, float w, float h,
                                          float r, int left, int right) {
        if (w <= 0f || h <= 0f) {
            return;
        }
        float rad = Math.min(r, Math.min(w, h) * 0.5f);
        int y0 = Math.round(y);
        int y1 = Math.round(y + h);

        for (int iy = y0; iy < y1; iy++) {
            float centre = iy + 0.5f;
            float ins = inset(cornerDy(centre, y, h, rad), rad);
            float sx = x + ins;
            float ex = x + w - ins;
            // Step across the row so the horizontal ramp stays smooth.
            int steps = Math.max(1, (int) Math.ceil(ex - sx));
            for (int s = 0; s < steps; s++) {
                float a = sx + s;
                float b = Math.min(ex, a + 1f);
                if (b <= a) {
                    break;
                }
                float t = ((a + b) * 0.5f - x) / w;
                span(c, a, b, iy, Theme.mix(left, right, t));
            }
        }
    }

    public static void roundRectGradientH(GuiGraphicsExtractor g, float x, float y, float w, float h,
                                          float r, int left, int right) {
        roundRectGradientH(g::fill, x, y, w, h, r, left, right);
    }

    /** Ring following a rounded rectangle, drawn inside the given bounds. */
    public static void roundRectOutline(Canvas c, float x, float y, float w, float h,
                                        float r, float thickness, int color) {
        if (w <= 0f || h <= 0f || thickness <= 0f) {
            return;
        }
        float rad = Math.min(r, Math.min(w, h) * 0.5f);
        float iw = w - thickness * 2f;
        float ih = h - thickness * 2f;
        float irad = Math.max(0f, Math.min(rad - thickness, Math.min(iw, ih) * 0.5f));

        int y0 = Math.round(y);
        int y1 = Math.round(y + h);
        float iy0 = y + thickness;
        float iy1 = y + h - thickness;

        for (int iy = y0; iy < y1; iy++) {
            float centre = iy + 0.5f;
            float outIns = inset(cornerDy(centre, y, h, rad), rad);
            float oL = x + outIns;
            float oR = x + w - outIns;

            if (centre < iy0 || centre > iy1 || iw <= 0f || ih <= 0f) {
                // Cap row: the ring is solid all the way across.
                span(c, oL, oR, iy, color);
                continue;
            }
            float inIns = inset(cornerDy(centre, iy0, ih, irad), irad);
            float iL = x + thickness + inIns;
            float iR = x + w - thickness - inIns;
            span(c, oL, iL, iy, color);
            span(c, iR, oR, iy, color);
        }
    }

    public static void roundRectOutline(GuiGraphicsExtractor g, float x, float y, float w, float h,
                                        float r, float thickness, int color) {
        roundRectOutline(g::fill, x, y, w, h, r, thickness, color);
    }

    /**
     * Soft shadow cast behind a rounded rectangle. Built from concentric low-alpha rings whose
     * opacity falls off quadratically, which reads as a blur without needing a shader.
     */
    public static void shadow(Canvas c, float x, float y, float w, float h,
                              float r, float spread, int color) {
        int steps = Math.max(1, Math.round(spread));
        for (int i = steps; i >= 1; i--) {
            float t = (float) i / steps;
            float grow = t * spread;
            float a = (1f - t) * (1f - t) * 0.5f;
            roundRect(c, x - grow, y - grow + spread * 0.35f, w + grow * 2f, h + grow * 2f,
                    r + grow, Theme.alpha(color, a));
        }
    }

    public static void shadow(GuiGraphicsExtractor g, float x, float y, float w, float h,
                              float r, float spread, int color) {
        shadow(g::fill, x, y, w, h, r, spread, color);
    }

    /** Accent glow, used behind the active element. */
    public static void glow(Canvas c, float x, float y, float w, float h,
                            float r, float spread, int color, float strength) {
        int steps = Math.max(1, Math.round(spread));
        for (int i = steps; i >= 1; i--) {
            float t = (float) i / steps;
            float grow = t * spread;
            float a = (1f - t) * (1f - t) * strength;
            roundRect(c, x - grow, y - grow, w + grow * 2f, h + grow * 2f, r + grow,
                    Theme.alpha(color, a));
        }
    }

    public static void glow(GuiGraphicsExtractor g, float x, float y, float w, float h,
                            float r, float spread, int color, float strength) {
        glow(g::fill, x, y, w, h, r, spread, color, strength);
    }

    public static void circle(Canvas c, float cx, float cy, float radius, int color) {
        roundRect(c, cx - radius, cy - radius, radius * 2f, radius * 2f, radius, color);
    }

    public static void circle(GuiGraphicsExtractor g, float cx, float cy, float radius, int color) {
        circle(g::fill, cx, cy, radius, color);
    }

    public static void hLine(Canvas c, float x, float y, float w, int color) {
        rect(c, x, y, w, 1f, color);
    }

    public static void hLine(GuiGraphicsExtractor g, float x, float y, float w, int color) {
        rect(g::fill, x, y, w, 1f, color);
    }

    public static void vLine(Canvas c, float x, float y, float h, int color) {
        rect(c, x, y, 1f, h, color);
    }

    public static void vLine(GuiGraphicsExtractor g, float x, float y, float h, int color) {
        rect(g::fill, x, y, 1f, h, color);
    }

    // -------------------------------------------------------------------------------------
    // Text
    // -------------------------------------------------------------------------------------

    public static void text(GuiGraphicsExtractor g, Font font, String s, float x, float y, int color) {
        g.text(font, s, Math.round(x), Math.round(y), color, false);
    }

    public static void textShadow(GuiGraphicsExtractor g, Font font, String s, float x, float y, int color) {
        g.text(font, s, Math.round(x), Math.round(y), color, true);
    }

    public static void textCentered(GuiGraphicsExtractor g, Font font, String s, float cx, float y, int color) {
        text(g, font, s, cx - font.width(s) / 2f, y, color);
    }

    public static void textRight(GuiGraphicsExtractor g, Font font, String s, float right, float y, int color) {
        text(g, font, s, right - font.width(s), y, color);
    }

    /** Truncates {@code s} with an ellipsis so that it fits inside {@code maxWidth}. */
    public static String ellipsize(Font font, String s, int maxWidth) {
        if (font.width(s) <= maxWidth) {
            return s;
        }
        String ell = "...";
        int budget = maxWidth - font.width(ell);
        if (budget <= 0) {
            return ell;
        }
        return font.plainSubstrByWidth(s, budget) + ell;
    }
}
