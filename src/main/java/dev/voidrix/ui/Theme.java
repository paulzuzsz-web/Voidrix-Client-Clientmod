package dev.voidrix.ui;

/**
 * The Voidrix design system.
 *
 * <p>Everything visual in the mod pulls its colours and metrics from here, so the whole client
 * reads as one surface: a near-black "void" base, a violet-to-magenta accent that only ever marks
 * the active thing, and generous rounding.
 *
 * <p>The colour fields are deliberately mutable statics rather than constants, because the palette
 * is swappable at runtime - see {@link Variant}. Every screen reads them fresh each frame, so a
 * theme change takes effect immediately with nothing to invalidate.
 */
public final class Theme {
    private Theme() {
    }

    /** The available palettes. */
    public enum Variant {
        /** The default: near-black, high contrast, violet accent. */
        DARK,
        /** A lighter surface for bright rooms, same accent ramp. */
        LIGHT,
        /** Pure black surfaces, for OLED panels. */
        MIDNIGHT
    }

    private static Variant current = Variant.DARK;

    // ---- Surfaces -------------------------------------------------------------------------
    /** Page backdrop, behind everything. */
    public static int VOID = 0xFF07070B;
    /** Default panel. */
    public static int SURFACE = 0xFF101018;
    /** A panel sitting on top of another panel. */
    public static int SURFACE_RAISED = 0xFF16161F;
    /** Hover state for an interactive row or card. */
    public static int SURFACE_HOVER = 0xFF1E1E2A;
    /** Pressed / selected state. */
    public static int SURFACE_ACTIVE = 0xFF262635;

    // ---- Lines ----------------------------------------------------------------------------
    public static int BORDER = 0xFF23232F;
    public static int BORDER_SOFT = 0x8023232F;

    // ---- Accent ---------------------------------------------------------------------------
    /** Primary accent - the violet end of the ramp. */
    public static int ACCENT = 0xFF8B5CF6;
    /** Secondary accent - the magenta end, used for gradients. */
    public static int ACCENT_ALT = 0xFFD946EF;
    /** Dim accent for rails and inactive tracks. */
    public static int ACCENT_MUTED = 0x668B5CF6;

    // ---- Text -----------------------------------------------------------------------------
    public static int TEXT = 0xFFF3F3F8;
    public static int TEXT_DIM = 0xFF9A9AAE;
    public static int TEXT_FAINT = 0xFF62626F;

    // ---- Status ---------------------------------------------------------------------------
    public static int SUCCESS = 0xFF34D399;
    public static int WARN = 0xFFFBBF24;
    public static int DANGER = 0xFFF87171;

    // ---- Metrics --------------------------------------------------------------------------
    /** Corner radius for large panels. */
    public static final float RADIUS_LG = 10f;
    /** Corner radius for cards and rows. */
    public static final float RADIUS_MD = 6f;
    /** Corner radius for chips, toggles and small controls. */
    public static final float RADIUS_SM = 4f;

    public static final int PAD = 10;
    public static final int GAP = 6;

    /** Standard transition length, in seconds. */
    public static final float ANIM = 0.18f;

    public static Variant variant() {
        return current;
    }

    /** Switches the palette. Cheap enough to call from a click handler. */
    public static void apply(Variant variant) {
        current = variant;
        switch (variant) {
            case DARK -> {
                VOID = 0xFF07070B;
                SURFACE = 0xFF101018;
                SURFACE_RAISED = 0xFF16161F;
                SURFACE_HOVER = 0xFF1E1E2A;
                SURFACE_ACTIVE = 0xFF262635;
                BORDER = 0xFF23232F;
                BORDER_SOFT = 0x8023232F;
                TEXT = 0xFFF3F3F8;
                TEXT_DIM = 0xFF9A9AAE;
                TEXT_FAINT = 0xFF62626F;
            }
            case MIDNIGHT -> {
                VOID = 0xFF000000;
                SURFACE = 0xFF07070A;
                SURFACE_RAISED = 0xFF0D0D12;
                SURFACE_HOVER = 0xFF16161D;
                SURFACE_ACTIVE = 0xFF1F1F28;
                BORDER = 0xFF1B1B24;
                BORDER_SOFT = 0x801B1B24;
                TEXT = 0xFFF5F5FA;
                TEXT_DIM = 0xFF8E8EA2;
                TEXT_FAINT = 0xFF55555F;
            }
            case LIGHT -> {
                VOID = 0xFFE8E8EE;
                SURFACE = 0xFFF6F6FA;
                SURFACE_RAISED = 0xFFFFFFFF;
                SURFACE_HOVER = 0xFFEBEBF2;
                SURFACE_ACTIVE = 0xFFDEDEE8;
                BORDER = 0xFFD2D2DC;
                BORDER_SOFT = 0x80D2D2DC;
                TEXT = 0xFF15151C;
                TEXT_DIM = 0xFF5A5A6B;
                TEXT_FAINT = 0xFF8E8E9E;
            }
        }
        ACCENT_MUTED = alpha(ACCENT, 0.4f);
    }

    /** True when the current palette is a light one, for choosing contrasting overlays. */
    public static boolean isLight() {
        return current == Variant.LIGHT;
    }

    /**
     * Blends {@code b} over {@code a} by {@code t} in the range 0..1, channel by channel,
     * including alpha.
     */
    public static int mix(int a, int b, float t) {
        float k = t < 0f ? 0f : (t > 1f ? 1f : t);
        int aa = (a >>> 24) & 0xFF, ar = (a >>> 16) & 0xFF, ag = (a >>> 8) & 0xFF, ab = a & 0xFF;
        int ba = (b >>> 24) & 0xFF, br = (b >>> 16) & 0xFF, bg = (b >>> 8) & 0xFF, bb = b & 0xFF;
        int ra = Math.round(aa + (ba - aa) * k);
        int rr = Math.round(ar + (br - ar) * k);
        int rg = Math.round(ag + (bg - ag) * k);
        int rb = Math.round(ab + (bb - ab) * k);
        return (ra << 24) | (rr << 16) | (rg << 8) | rb;
    }

    /** Returns {@code color} scaled to the given alpha multiplier (0..1). */
    public static int alpha(int color, float mul) {
        int a = (int) (((color >>> 24) & 0xFF) * Math.clamp(mul, 0f, 1f));
        return (a << 24) | (color & 0x00FFFFFF);
    }

    /** Replaces the alpha channel outright with {@code a} in the range 0..255. */
    public static int withAlpha(int color, int a) {
        return ((a & 0xFF) << 24) | (color & 0x00FFFFFF);
    }

    /**
     * Samples the accent gradient at {@code t}, wrapping so that animated values sweep back and
     * forth instead of jumping at the seam.
     */
    public static int accentAt(float t) {
        float k = t - (float) Math.floor(t);
        float tri = k < 0.5f ? k * 2f : (1f - k) * 2f;
        return mix(ACCENT, ACCENT_ALT, tri);
    }
}
