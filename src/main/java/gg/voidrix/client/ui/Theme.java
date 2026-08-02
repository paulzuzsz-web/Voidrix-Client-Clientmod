package gg.voidrix.client.ui;

/**
 * Farbpalette und Layout-Konstanten des Voidrix-Designs.
 *
 * <p>Alle Farben sind ARGB-Integer. Wer das Design umfaerben moechte, aendert
 * nur diese Datei - Menue, HUD und Editor lesen ausschliesslich hier.</p>
 */
public final class Theme {

	// --- Basisfarben aus dem Design ---------------------------------
	/** Dunkles Violett-Grau - Grundflaeche. */
	public static final int BASE = 0xFF12101A;

	/** Akzent Violett. */
	public static final int ACCENT = 0xFF7C3AED;

	/** Zweitakzent Cyan. */
	public static final int ACCENT_2 = 0xFF22D3EE;

	// --- Abgeleitete Flaechen ---------------------------------------
	/** Karten-/Panelflaeche, minimal heller als BASE. */
	public static final int SURFACE = 0xFF17151F;

	/** Erhoehte Flaeche (Hover, Eingabefelder). */
	public static final int SURFACE_HIGH = 0xFF1E1B29;

	/** Halbtransparenter Overlay-Schleier ueber dem Spiel. */
	public static final int OVERLAY = 0xC012101A;

	/** Sidebar-Hintergrund. */
	public static final int SIDEBAR = 0xF00E0C15;

	// --- Text --------------------------------------------------------
	public static final int TEXT = 0xFFEDE9F5;
	public static final int TEXT_DIM = 0xFF9A94AD;
	public static final int TEXT_MUTED = 0xFF635D75;

	// --- Zustandsfarben ----------------------------------------------
	public static final int SUCCESS = 0xFF34D399;
	public static final int ERROR = 0xFFF87171;
	public static final int LOCKED = 0xFF4A4458;

	// --- Layout -------------------------------------------------------
	/** Groesse der gekappten Ecke oben rechts (Wiedererkennungsmerkmal). */
	public static final int CORNER_CUT = 10;

	/** Breite der Icon-Sidebar links. */
	public static final int SIDEBAR_WIDTH = 46;

	/** Hoehe der Tab-Leiste oben. */
	public static final int TAB_BAR_HEIGHT = 34;

	private Theme() {
	}

	// ---------------------------------------------------------------
	// Farb-Helfer
	// ---------------------------------------------------------------

	/** Ersetzt den Alpha-Kanal einer Farbe (alpha 0..255). */
	public static int withAlpha(int argb, int alpha) {
		return ((alpha & 0xFF) << 24) | (argb & 0x00FFFFFF);
	}

	/** Multipliziert den vorhandenen Alpha-Kanal (factor 0..1). */
	public static int fade(int argb, float factor) {
		int alpha = (int) (((argb >> 24) & 0xFF) * Math.max(0f, Math.min(1f, factor)));
		return withAlpha(argb, alpha);
	}

	/** Mischt zwei Farben linear (t = 0 -> a, t = 1 -> b). Auch fuer Farbverlaeufe. */
	public static int lerp(int a, int b, float t) {
		float clamped = Math.max(0f, Math.min(1f, t));

		int alpha = lerpChannel(a >>> 24, b >>> 24, clamped);
		int red = lerpChannel((a >> 16) & 0xFF, (b >> 16) & 0xFF, clamped);
		int green = lerpChannel((a >> 8) & 0xFF, (b >> 8) & 0xFF, clamped);
		int blue = lerpChannel(a & 0xFF, b & 0xFF, clamped);

		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	private static int lerpChannel(int a, int b, float t) {
		return (int) (a + (b - a) * t) & 0xFF;
	}

	/**
	 * Pulsierender Verlauf zwischen Violett und Cyan - fuer das Voidrix+ "V"
	 * und Premium-Hervorhebungen.
	 *
	 * @param phase fortlaufender Wert (z.B. Systemzeit in ms)
	 */
	public static int pulse(long phase) {
		float t = (float) ((Math.sin(phase / 500.0) + 1.0) / 2.0);
		return lerp(ACCENT, ACCENT_2, t);
	}
}
