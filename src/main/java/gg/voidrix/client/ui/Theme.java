package gg.voidrix.client.ui;

/**
 * Farbpalette und Layout-Konstanten des Voidrix-Designs.
 *
 * <p><b>Bildsprache:</b> helles "Frosted Glass" - weiss-transluzente Flaechen
 * ueber dem geblurrten Spiel, runde Ecken, Beschriftungen in Grossbuchstaben.
 * Violett dient ausschliesslich als <i>Zustandsfarbe</i>: aktiver Tab, aktiver
 * Schalter, gefuellter Slider, ausgewaehlter Sidebar-Eintrag.</p>
 *
 * <p>Wer umfaerben moechte, aendert nur diese Datei - Menue, HUD und Editor
 * lesen ausschliesslich hier.</p>
 */
public final class Theme {

	// ---------------------------------------------------------------
	// Akzente (Voidrix-Identitaet)
	// ---------------------------------------------------------------

	/** Akzent Violett - markiert aktive Zustaende. */
	public static final int ACCENT = 0xFF7C3AED;

	/** Zweitakzent Cyan - sparsam fuer Hervorhebungen. */
	public static final int ACCENT_2 = 0xFF22D3EE;

	/** Dunkles Violett-Grau - Logo, HUD-Hintergrund, dunkle Flaechen. */
	public static final int BASE = 0xFF12101A;

	// ---------------------------------------------------------------
	// Frosted-Glass-Flaechen
	// ---------------------------------------------------------------

	/** Heller Schleier ueber dem geblurrten Spiel. */
	public static final int OVERLAY = 0x33101018;

	/** Hauptflaeche des Menues. */
	public static final int PANEL = 0xD9EDEDF2;

	/** Sidebar - eine Spur kraeftiger als das Panel. */
	public static final int SIDEBAR = 0xE6E4E4EB;

	/** Kartenflaeche auf dem Panel. */
	public static final int CARD = 0x8CFFFFFF;

	/** Karte unter dem Mauszeiger. */
	public static final int CARD_HOVER = 0xCCFFFFFF;

	/** Eingabefelder, Dropdowns, Slider-Spur. */
	public static final int INPUT = 0xA6FFFFFF;

	/** Feine Trenn- und Umrandungslinien. */
	public static final int BORDER = 0x24000000;

	/** Etwas kraeftigere Umrandung (Fokus, Hover). */
	public static final int BORDER_STRONG = 0x4D000000;

	// ---------------------------------------------------------------
	// Text (dunkel auf hellem Grund)
	// ---------------------------------------------------------------

	public static final int TEXT = 0xFF23202B;
	public static final int TEXT_DIM = 0xFF6B6676;
	public static final int TEXT_MUTED = 0xFF9C97A6;

	/** Text auf violetter Flaeche. */
	public static final int TEXT_ON_ACCENT = 0xFFFFFFFF;

	/** Text im HUD - dort liegt der Grund dunkel ueber der Welt. */
	public static final int HUD_TEXT = 0xFFFFFFFF;

	// ---------------------------------------------------------------
	// Zustandsfarben
	// ---------------------------------------------------------------

	public static final int SUCCESS = 0xFF22C55E;
	public static final int ERROR = 0xFFDC2626;
	public static final int LOCKED = 0xFFA8A2B3;

	// ---------------------------------------------------------------
	// Layout
	// ---------------------------------------------------------------

	/** Eckenradius von Panels und Karten. */
	public static final int RADIUS = 5;

	/** Kleinerer Radius fuer Knoepfe, Felder und Badges. */
	public static final int RADIUS_SMALL = 3;

	/** Breite der Icon-Sidebar (Icon plus Mini-Label darunter). */
	public static final int SIDEBAR_WIDTH = 44;

	/**
	 * Abstand zwischen Sidebar und Inhaltsflaeche. Die Sidebar ist ein
	 * eigenstaendiges Panel und "schwebt" neben dem Inhalt - genau dieser
	 * Spalt macht den Look aus.
	 */
	public static final int SIDEBAR_GAP = 6;

	/** Hoehe der Tab-Leiste oben. */
	public static final int TAB_BAR_HEIGHT = 32;

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

	/** Mischt zwei Farben linear (t = 0 -> a, t = 1 -> b). */
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
