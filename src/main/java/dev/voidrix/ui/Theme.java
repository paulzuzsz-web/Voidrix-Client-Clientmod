package dev.voidrix.ui;

/**
 * The Voidrix design system.
 *
 * <p>Everything visual in the mod pulls its colours and metrics from here, so the whole client
 * reads as one surface: a near-black "void" base, a violet-to-magenta accent that only ever marks
 * the active thing, and generous rounding.
 */
public final class Theme {
    private Theme() {
    }

    // ---- Surfaces -------------------------------------------------------------------------
    /** Page backdrop, behind everything. */
    public static final int VOID = 0xFF07070B;
    /** Default panel. */
    public static final int SURFACE = 0xFF101018;
    /** A panel sitting on top of another panel. */
    public static final int SURFACE_RAISED = 0xFF16161F;
    /** Hover state for an interactive row or card. */
    public static final int SURFACE_HOVER = 0xFF1E1E2A;
    /** Pressed / selected state. */
    public static final int SURFACE_ACTIVE = 0xFF262635;

    // ---- Lines ----------------------------------------------------------------------------
    public static final int BORDER = 0xFF23232F;
    public static final int BORDER_SOFT = 0x8023232F;

    // ---- Accent ---------------------------------------------------------------------------
    /** Primary accent - the violet end of the ramp. */
    public static final int ACCENT = 0xFF8B5CF6;
    /** Secondary accent - the magenta end, used for gradients. */
    public static final int ACCENT_ALT = 0xFFD946EF;
    /** Dim accent for rails and inactive tracks. */
    public static final int ACCENT_MUTED = 0x668B5CF6;

    // ---- Text -----------------------------------------------------------------------------
    public static final int TEXT = 0xFFF3F3F8;
    public static final int TEXT_DIM = 0xFF9A9AAE;
    public static final int TEXT_FAINT = 0xFF62626F;

    // ---- Status ---------------------------------------------------------------------------
    public static final int SUCCESS = 0xFF34D399;
    public static final int WARN = 0xFFFBBF24;
    public static final int DANGER = 0xFFF87171;

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
