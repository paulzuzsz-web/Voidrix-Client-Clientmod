package gg.voidrix.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Farbwerte und Zeichenhilfen fuer die Voidrix-Oberflaeche.
 *
 * <p>26.2 kennt nur achsenparallele Rechtecke, echte Rundungen gibt es nicht. Die Ecken werden
 * daher um einen Pixel eingezogen - das reicht optisch und kostet nur drei Fills.
 */
public final class Theme {

    // Flaechen
    public static final int PANEL_BG = 0xF01A1626;
    public static final int PANEL_BORDER = 0xFF3D3357;
    public static final int HEADER_BG = 0xFF221C33;

    // Akzent
    public static final int ACCENT = 0xFF9A6BFF;
    public static final int ACCENT_SOFT = 0xFF5B4A8C;

    // Text
    public static final int TEXT = 0xFFE8E4F2;
    public static final int TEXT_MUTED = 0xFF8A82A0;
    public static final int TEXT_SECTION = 0xFFB9A8E8;

    // Schalter
    public static final int ROW_BG = 0xFF241F35;
    public static final int ROW_BG_HOVER = 0xFF322A47;
    public static final int TRACK_OFF = 0xFF3A3448;
    public static final int KNOB_OFF = 0xFF6E6785;

    private Theme() {
    }

    /** Rechteck mit einem Pixel eingezogenen Ecken. */
    public static void roundedRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int color) {
        int right = x + width;
        int bottom = y + height;
        g.fill(x + 1, y, right - 1, bottom, color);
        g.fill(x, y + 1, x + 1, bottom - 1, color);
        g.fill(right - 1, y + 1, right, bottom - 1, color);
    }

    /** Ein Pixel breiter Rahmen im selben Eckstil. */
    public static void border(GuiGraphicsExtractor g, int x, int y, int width, int height, int color) {
        int right = x + width;
        int bottom = y + height;
        g.fill(x + 1, y, right - 1, y + 1, color);
        g.fill(x + 1, bottom - 1, right - 1, bottom, color);
        g.fill(x, y + 1, x + 1, bottom - 1, color);
        g.fill(right - 1, y + 1, right, bottom - 1, color);
    }
}
