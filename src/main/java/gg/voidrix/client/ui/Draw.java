package gg.voidrix.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

import java.util.Locale;

/**
 * Zeichen-Helfer fuer das Voidrix-Design.
 *
 * <p>Minecraft kennt nur achsenparallele Rechtecke ({@code fill}). Runde Ecken
 * werden deshalb aus einzelnen Pixelzeilen zusammengesetzt - bei den hier
 * verwendeten Radien (3-5 px) sind das nur wenige Zeilen, der Rest ist ein
 * einziges grosses Rechteck.</p>
 *
 * <p>In 26.x laeuft das Zeichnen ueber {@link GuiGraphicsExtractor}; die
 * frueher uebliche {@code DrawContext}/{@code GuiGraphics}-Klasse gibt es in
 * dieser Form nicht mehr.</p>
 */
public final class Draw {

	private Draw() {
	}

	public static Font font() {
		return Minecraft.getInstance().font;
	}

	/** Beschriftungen im Menue stehen durchgaengig in Grossbuchstaben. */
	public static String upper(String value) {
		return value == null ? "" : value.toUpperCase(Locale.ROOT);
	}

	// ---------------------------------------------------------------
	// Grundformen
	// ---------------------------------------------------------------

	public static void rect(GuiGraphicsExtractor g, int x, int y, int width, int height, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		g.fill(x, y, x + width, y + height, color);
	}

	public static void gradientRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int top, int bottom) {
		if (width <= 0 || height <= 0) {
			return;
		}

		g.fillGradient(x, y, x + width, y + height, top, bottom);
	}

	/** Rechteck mit runden Ecken - die Grundform des gesamten Menues. */
	public static void roundedRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int radius, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int r = Math.max(0, Math.min(radius, Math.min(width, height) / 2));

		if (r == 0) {
			rect(g, x, y, width, height, color);
			return;
		}

		// Mittelblock in voller Breite
		g.fill(x, y + r, x + width, y + height - r, color);

		// obere und untere Kappe, Zeile fuer Zeile eingerueckt
		for (int i = 0; i < r; i++) {
			int inset = r - (int) Math.round(Math.sqrt(r * r - (r - i - 1) * (r - i - 1)));
			g.fill(x + inset, y + i, x + width - inset, y + i + 1, color);
			g.fill(x + inset, y + height - i - 1, x + width - inset, y + height - i, color);
		}
	}

	/** Feine Kontur entlang eines abgerundeten Rechtecks. */
	public static void roundedOutline(GuiGraphicsExtractor g, int x, int y, int width, int height,
			int radius, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int r = Math.max(0, Math.min(radius, Math.min(width, height) / 2));

		// waagerechte Kanten (ohne die Ecken)
		g.fill(x + r, y, x + width - r, y + 1, color);
		g.fill(x + r, y + height - 1, x + width - r, y + height, color);

		// senkrechte Kanten (ohne die Ecken)
		g.fill(x, y + r, x + 1, y + height - r, color);
		g.fill(x + width - 1, y + r, x + width, y + height - r, color);

		// die vier Ecken
		for (int i = 0; i < r; i++) {
			int inset = r - (int) Math.round(Math.sqrt(r * r - (r - i - 1) * (r - i - 1)));

			g.fill(x + inset, y + i, x + inset + 1, y + i + 1, color);
			g.fill(x + width - inset - 1, y + i, x + width - inset, y + i + 1, color);
			g.fill(x + inset, y + height - i - 1, x + inset + 1, y + height - i, color);
			g.fill(x + width - inset - 1, y + height - i - 1, x + width - inset, y + height - i, color);
		}
	}

	// ---------------------------------------------------------------
	// Zusammengesetzte Flaechen
	// ---------------------------------------------------------------

	/** Die grosse Frosted-Glass-Flaeche des Menues. */
	public static void panel(GuiGraphicsExtractor g, int x, int y, int width, int height) {
		roundedRect(g, x, y, width, height, Theme.RADIUS, Theme.PANEL);
		roundedOutline(g, x, y, width, height, Theme.RADIUS, Theme.BORDER);
	}

	/** Eine Karte im Grid bzw. eine Zeile im Inhaltsbereich. */
	public static void card(GuiGraphicsExtractor g, int x, int y, int width, int height,
			boolean hovered, boolean active) {
		roundedRect(g, x, y, width, height, Theme.RADIUS, hovered ? Theme.CARD_HOVER : Theme.CARD);

		if (active) {
			// aktive Module bekommen eine violette Kontur
			roundedOutline(g, x, y, width, height, Theme.RADIUS, Theme.withAlpha(Theme.ACCENT, 160));
		} else {
			roundedOutline(g, x, y, width, height, Theme.RADIUS, Theme.BORDER);
		}
	}

	/** Eingabefeld-, Dropdown- und Slider-Untergrund. */
	public static void input(GuiGraphicsExtractor g, int x, int y, int width, int height, boolean focused) {
		roundedRect(g, x, y, width, height, Theme.RADIUS_SMALL, Theme.INPUT);
		roundedOutline(g, x, y, width, height, Theme.RADIUS_SMALL,
				focused ? Theme.withAlpha(Theme.ACCENT, 200) : Theme.BORDER);
	}

	/** Kleines Badge, z.B. "NEU". */
	public static void badge(GuiGraphicsExtractor g, String label, int x, int y, int background, int textColor) {
		String text = upper(label);
		int width = textWidth(text) + 8;
		int height = 11;

		roundedRect(g, x, y, width, height, Theme.RADIUS_SMALL, background);
		textFlat(g, text, x + 4, y + 2, textColor);
	}

	/** Waagerechte Trennlinie. */
	public static void separator(GuiGraphicsExtractor g, int x, int y, int width, int color) {
		rect(g, x, y, width, 1, color);
	}

	/**
	 * Schlanker Scrollbalken am rechten Rand eines Inhaltsbereichs.
	 *
	 * @param contentHeight Gesamthoehe des Inhalts
	 * @param viewHeight    sichtbare Hoehe
	 * @param scroll        aktueller Versatz
	 */
	public static void scrollbar(GuiGraphicsExtractor g, int x, int y, int viewHeight,
			int contentHeight, double scroll) {
		if (contentHeight <= viewHeight) {
			return;
		}

		rect(g, x, y, 2, viewHeight, Theme.withAlpha(0xFF000000, 20));

		int thumbHeight = Math.max(16, (int) (viewHeight * (viewHeight / (float) contentHeight)));
		int maxScroll = contentHeight - viewHeight;
		int thumbY = y + (int) ((viewHeight - thumbHeight) * (scroll / (double) maxScroll));

		roundedRect(g, x, thumbY, 2, thumbHeight, 1, Theme.withAlpha(Theme.ACCENT, 170));
	}

	/** Der wiederverwendete ON/OFF-Schalter im NoRisk-Stil (Textknopf). */
	public static void toggle(GuiGraphicsExtractor g, int x, int y, int width, int height,
			boolean on, boolean locked, boolean hovered) {
		int background = locked
				? Theme.withAlpha(Theme.LOCKED, 90)
				: (on ? Theme.ACCENT : Theme.INPUT);

		roundedRect(g, x, y, width, height, Theme.RADIUS_SMALL, background);
		roundedOutline(g, x, y, width, height, Theme.RADIUS_SMALL,
				hovered ? Theme.BORDER_STRONG : Theme.BORDER);

		String label = locked ? "LOCK" : (on ? "ON" : "OFF");
		int color = on && !locked ? Theme.TEXT_ON_ACCENT : Theme.TEXT_DIM;

		textCentered(g, label, x + width / 2, y + (height - 8) / 2, color);
	}

	/** Kleines Kaestchen zum Ankreuzen (z.B. "Dynamic Padding"). */
	public static void checkbox(GuiGraphicsExtractor g, int x, int y, int size, boolean checked, boolean hovered) {
		roundedRect(g, x, y, size, size, 2, checked ? Theme.ACCENT : Theme.INPUT);
		roundedOutline(g, x, y, size, size, 2, hovered ? Theme.BORDER_STRONG : Theme.BORDER);

		if (checked) {
			// schlichter Haken aus zwei Strichen
			rect(g, x + 3, y + size / 2, 2, 2, Theme.TEXT_ON_ACCENT);
			rect(g, x + 5, y + size / 2 + 2, 2, 2, Theme.TEXT_ON_ACCENT);
			rect(g, x + 7, y + size / 2, 2, 2, Theme.TEXT_ON_ACCENT);
			rect(g, x + 9, y + 3, 2, 2, Theme.TEXT_ON_ACCENT);
		}
	}

	// ---------------------------------------------------------------
	// Formen mit gekappter Ecke - im HUD weiterhin als Merkmal genutzt
	// ---------------------------------------------------------------

	/** Flaeche mit gekappter Ecke oben rechts. */
	public static void cutCornerRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int cut, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int clampedCut = Math.max(0, Math.min(cut, Math.min(width, height)));

		for (int i = 0; i < clampedCut; i++) {
			int rowRight = x + width - clampedCut + i;
			g.fill(x, y + i, rowRight, y + i + 1, color);
		}

		g.fill(x, y + clampedCut, x + width, y + height, color);
	}

	/** Kontur einer Flaeche mit gekappter Ecke. */
	public static void cutCornerOutline(GuiGraphicsExtractor g, int x, int y, int width, int height,
			int cut, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int clampedCut = Math.max(0, Math.min(cut, Math.min(width, height)));

		g.fill(x, y, x + width - clampedCut, y + 1, color);
		g.fill(x, y + height - 1, x + width, y + height, color);
		g.fill(x, y, x + 1, y + height, color);
		g.fill(x + width - 1, y + clampedCut, x + width, y + height, color);

		for (int i = 0; i < clampedCut; i++) {
			int px = x + width - clampedCut + i;
			g.fill(px, y + i, px + 1, y + i + 1, color);
		}
	}

	// ---------------------------------------------------------------
	// Text
	// ---------------------------------------------------------------

	public static void text(GuiGraphicsExtractor g, String value, int x, int y, int color) {
		g.text(font(), value, x, y, color, true);
	}

	/** Text ohne Schlagschatten - Standard auf hellem Grund. */
	public static void textFlat(GuiGraphicsExtractor g, String value, int x, int y, int color) {
		g.text(font(), value, x, y, color, false);
	}

	public static void textCentered(GuiGraphicsExtractor g, String value, int centerX, int y, int color) {
		g.text(font(), value, centerX - font().width(value) / 2, y, color, false);
	}

	public static void textRight(GuiGraphicsExtractor g, String value, int rightX, int y, int color) {
		g.text(font(), value, rightX - font().width(value), y, color, false);
	}

	public static int textWidth(String value) {
		return font().width(value);
	}

	/**
	 * Text in beliebiger Groesse.
	 *
	 * <p>Minecrafts Schrift kennt nur eine feste Groesse; groessere
	 * Beschriftungen entstehen deshalb ueber die Transformationsmatrix. Das
	 * wird u.a. fuer den Schriftzug im Schnellzugriff und die Kartentitel
	 * benutzt.</p>
	 */
	public static void textScaled(GuiGraphicsExtractor g, String value, int x, int y,
			int color, float scale, boolean shadow) {
		Matrix3x2fStack pose = g.pose();
		pose.pushMatrix();
		pose.translate(x, y);
		pose.scale(scale, scale);

		g.text(font(), value, 0, 0, color, shadow);

		pose.popMatrix();
	}

	public static void textScaledCentered(GuiGraphicsExtractor g, String value, int centerX, int y,
			int color, float scale, boolean shadow) {
		int x = centerX - Math.round(font().width(value) * scale / 2f);
		textScaled(g, value, x, y, color, scale, shadow);
	}

	/** Breite eines skalierten Textes. */
	public static int textWidthScaled(String value, float scale) {
		return Math.round(font().width(value) * scale);
	}

	/** Kuerzt Text auf eine Maximalbreite und haengt "..." an. */
	public static String truncate(String value, int maxWidth) {
		if (font().width(value) <= maxWidth) {
			return value;
		}

		StringBuilder builder = new StringBuilder();
		int ellipsis = font().width("...");

		for (char c : value.toCharArray()) {
			if (font().width(builder.toString() + c) + ellipsis > maxWidth) {
				break;
			}

			builder.append(c);
		}

		return builder + "...";
	}

	/**
	 * Bricht Text auf zwei Zeilen um, wie in den Modul-Karten.
	 *
	 * @return Array mit genau zwei Eintraegen (die zweite Zeile kann leer sein)
	 */
	public static String[] wrapTwoLines(String value, int maxWidth) {
		if (font().width(value) <= maxWidth) {
			return new String[] { value, "" };
		}

		StringBuilder first = new StringBuilder();
		String[] words = value.split(" ");
		int index = 0;

		while (index < words.length) {
			String candidate = first.isEmpty() ? words[index] : first + " " + words[index];

			if (font().width(candidate) > maxWidth) {
				break;
			}

			first = new StringBuilder(candidate);
			index++;
		}

		StringBuilder second = new StringBuilder();

		while (index < words.length) {
			second.append(second.isEmpty() ? "" : " ").append(words[index]);
			index++;
		}

		return new String[] { first.toString(), truncate(second.toString(), maxWidth) };
	}

	public static boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}
}
