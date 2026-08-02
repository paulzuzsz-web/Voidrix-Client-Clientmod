package gg.voidrix.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Zeichen-Helfer fuer das Voidrix-Design.
 *
 * <p>Minecraft kennt nur achsenparallele Rechtecke ({@code fill}). Alle
 * Rundungen und die gekappte Ecke werden deshalb aus Zeilen zusammengesetzt.
 * Das ist bei den hier verwendeten Groessen guenstig genug, weil nur die
 * wenigen Pixelzeilen der Ecke einzeln gezeichnet werden - der Rest ist ein
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

	// ---------------------------------------------------------------
	// Grundformen
	// ---------------------------------------------------------------

	public static void rect(GuiGraphicsExtractor g, int x, int y, int width, int height, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		g.fill(x, y, x + width, y + height, color);
	}

	/** Rechteck mit vertikalem Farbverlauf. */
	public static void gradientRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int top, int bottom) {
		if (width <= 0 || height <= 0) {
			return;
		}

		g.fillGradient(x, y, x + width, y + height, top, bottom);
	}

	/**
	 * Karte mit gekappter Ecke oben rechts - das Wiedererkennungsmerkmal des
	 * Voidrix-Designs.
	 *
	 * @param cut Groesse der Kappung in Pixeln
	 */
	public static void cutCornerRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int cut, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int clampedCut = Math.max(0, Math.min(cut, Math.min(width, height)));

		// Die Schraege oben rechts: pro Pixelzeile wird die rechte Kante
		// um eine Spalte weiter nach rechts gezogen.
		for (int i = 0; i < clampedCut; i++) {
			int rowRight = x + width - clampedCut + i;
			g.fill(x, y + i, rowRight, y + i + 1, color);
		}

		// Restflaeche als ein einzelnes Rechteck
		g.fill(x, y + clampedCut, x + width, y + height, color);
	}

	/**
	 * Feine, leuchtende Kontur entlang einer Karte mit gekappter Ecke.
	 * Statt harter Schatten setzt das Design auf genau diese Linien.
	 */
	public static void cutCornerOutline(GuiGraphicsExtractor g, int x, int y, int width, int height, int cut, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int clampedCut = Math.max(0, Math.min(cut, Math.min(width, height)));

		// oben (bis zum Beginn der Kappung)
		g.fill(x, y, x + width - clampedCut, y + 1, color);
		// unten
		g.fill(x, y + height - 1, x + width, y + height, color);
		// links
		g.fill(x, y, x + 1, y + height, color);
		// rechts (erst unterhalb der Kappung)
		g.fill(x + width - 1, y + clampedCut, x + width, y + height, color);

		// die Diagonale der gekappten Ecke
		for (int i = 0; i < clampedCut; i++) {
			int px = x + width - clampedCut + i;
			g.fill(px, y + i, px + 1, y + i + 1, color);
		}
	}

	/**
	 * Glueh-Effekt: mehrere Konturen mit abnehmender Deckkraft nach aussen.
	 * Ersetzt bewusst harte Schlagschatten.
	 */
	public static void glow(GuiGraphicsExtractor g, int x, int y, int width, int height, int cut, int color, int layers) {
		for (int i = layers; i >= 1; i--) {
			int alpha = (int) (60f / i);
			cutCornerOutline(g, x - i, y - i, width + i * 2, height + i * 2, cut, Theme.withAlpha(color, alpha));
		}
	}

	/** Rechteck mit abgerundeten Ecken (aus Zeilen zusammengesetzt). */
	public static void roundedRect(GuiGraphicsExtractor g, int x, int y, int width, int height, int radius, int color) {
		if (width <= 0 || height <= 0) {
			return;
		}

		int r = Math.max(0, Math.min(radius, Math.min(width, height) / 2));

		if (r == 0) {
			rect(g, x, y, width, height, color);
			return;
		}

		// Mittelblock
		g.fill(x, y + r, x + width, y + height - r, color);

		// obere und untere Kappen, Zeile fuer Zeile eingerueckt
		for (int i = 0; i < r; i++) {
			int inset = r - (int) Math.sqrt(r * r - (r - i - 1) * (r - i - 1));
			g.fill(x + inset, y + i, x + width - inset, y + i + 1, color);
			g.fill(x + inset, y + height - i - 1, x + width - inset, y + height - i, color);
		}
	}

	// ---------------------------------------------------------------
	// Text
	// ---------------------------------------------------------------

	public static void text(GuiGraphicsExtractor g, String value, int x, int y, int color) {
		g.text(font(), value, x, y, color, true);
	}

	/** Text ohne Schlagschatten - fuer das flache, luftige Layout. */
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

	// ---------------------------------------------------------------
	// Zusammengesetzte Elemente
	// ---------------------------------------------------------------

	/** Kleines Badge (z.B. "NEU") mit gekappter Ecke. */
	public static void badge(GuiGraphicsExtractor g, String label, int x, int y, int background, int textColor) {
		int width = textWidth(label) + 8;
		int height = 11;

		cutCornerRect(g, x, y, width, height, 3, background);
		textFlat(g, label, x + 4, y + 2, textColor);
	}

	/** Waagerechte Trennlinie im Akzentfarbton, nach aussen ausblendend. */
	public static void separator(GuiGraphicsExtractor g, int x, int y, int width, int color) {
		int half = width / 2;

		for (int i = 0; i < half; i++) {
			// zur Mitte hin kraeftiger
			float t = i / (float) Math.max(1, half);
			int faded = Theme.fade(color, t);

			g.fill(x + i, y, x + i + 1, y + 1, faded);
			g.fill(x + width - i - 1, y, x + width - i, y + 1, faded);
		}
	}

	public static boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
	}
}
