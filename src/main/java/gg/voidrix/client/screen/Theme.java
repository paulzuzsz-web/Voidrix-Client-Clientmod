package gg.voidrix.client.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Farbwerte und Zeichenhilfen fuer die Voidrix-Oberflaeche.
 *
 * <p>Die Palette ist bewusst kuehl und kontrastarm gehalten: fast schwarzes Blau als Flaeche,
 * ein einziger Akzent in Cyan-Magenta fuer alles Aktive. 26.2 kennt nur achsenparallele
 * Rechtecke, echte Rundungen gibt es nicht - die Ecken werden daher um einen Pixel eingezogen.
 */
public final class Theme {

    // Flaechen (ARGB)
    public static final int SCRIM = 0xC805060C;
    public static final int PANEL_TOP = 0xFF14182B;
    public static final int PANEL_BOTTOM = 0xFF0B0E1A;
    public static final int SIDEBAR_SOLID = 0xFF0E1120;
    public static final int DIVIDER = 0xFF232A44;
    public static final int PANEL_BORDER = 0xFF2E3550;

    // Akzent
    public static final int ACCENT = 0xFF3DDCC8;
    public static final int ACCENT_ALT = 0xFF7C6CFF;
    public static final int ACCENT_SOFT = 0xFF1F5F5C;

    // Text
    public static final int TEXT = 0xFFEAF0F6;
    public static final int TEXT_MUTED = 0xFF6E7893;
    public static final int TEXT_SECTION = 0xFF9AA6C4;

    // Zeilen
    public static final int ROW = 0xFF161B2E;
    public static final int ROW_HOVER = 0xFF1E2542;
    public static final int TRACK_OFF = 0xFF2A3149;
    public static final int KNOB_OFF = 0xFF5A6480;

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

    /** Senkrechter Verlauf mit eingezogenen Ecken. */
    public static void verticalGradient(
            GuiGraphicsExtractor g, int x, int y, int width, int height, int top, int bottom) {
        int right = x + width;
        int low = y + height;
        g.fillGradient(x + 1, y, right - 1, low, top, bottom);
        g.fillGradient(x, y + 1, x + 1, low - 1, top, bottom);
        g.fillGradient(right - 1, y + 1, right, low - 1, top, bottom);
    }

    /** Waagerechte Linie, die zu beiden Seiten ausblendet - als dezenter Trenner. */
    public static void fadingRule(GuiGraphicsExtractor g, int x, int y, int width, int color) {
        int half = width / 2;
        g.fillGradient(x, y, x + half, y + 1, color & 0x00FFFFFF, color);
        g.fillGradient(x + half, y, x + width, y + 1, color, color & 0x00FFFFFF);
    }
}
