package gg.voidrix.client.hud;

import com.google.gson.JsonObject;
import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.ColorSetting;
import gg.voidrix.client.module.setting.DoubleSetting;
import gg.voidrix.client.module.setting.EnumSetting;
import gg.voidrix.client.module.setting.IntSetting;
import gg.voidrix.client.module.setting.StringSetting;
import gg.voidrix.client.ui.Draw;
import gg.voidrix.client.ui.Theme;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;

import java.util.List;

/**
 * Basis fuer alle HUD-Module.
 *
 * <p>Ein HUD-Modul liefert nur seinen <b>Text</b>; Positionierung, Skalierung,
 * Hintergrund, Rahmen und Farben erledigt diese Klasse einheitlich fuer alle
 * Module. Dadurch verhaelt sich jedes HUD-Element gleich und ist im
 * HUD-Editor frei verschiebbar.</p>
 *
 * <h2>Eigenes HUD-Modul bauen</h2>
 * <pre>{@code
 * public class MyHud extends HudModule {
 *     public MyHud() { super("my_hud", "Mein HUD", "Zeigt etwas", 0.02f, 0.5f); }
 *
 *     @Override public String getLeftText()  { return "Label"; }
 *     @Override public String getRightText() { return "42"; }
 * }
 * }</pre>
 *
 * <p>Die Position wird als <b>Bruchteil des Bildschirms</b> (0..1) gespeichert.
 * So bleibt das Layout beim Wechsel der Aufloesung oder der GUI-Skalierung an
 * der gleichen relativen Stelle.</p>
 */
public abstract class HudModule extends Module {

	/** Innenabstand zwischen Rahmen und Text. */
	protected static final int PADDING_X = 5;
	protected static final int PADDING_Y = 4;

	/** Zeilenabstand bei mehrzeiligen Elementen. */
	protected static final int LINE_HEIGHT = 10;

	// --- Position (nicht als Setting, sondern per Drag & Drop) --------
	private float posX;
	private float posY;

	// --- Einheitliche Einstellungen aller HUD-Module ------------------

	protected final DoubleSetting scale =
			add(new DoubleSetting("scale", "HUD-Skalierung", "Groesse des Elements", 1.0, 0.5, 3.0, 0.05));

	protected final DoubleSetting extraWidth =
			add(new DoubleSetting("width", "Breite", "Zusaetzliche Breite des Hintergrunds", 0.0, 0.0, 60.0, 1.0));

	protected final DoubleSetting extraHeight =
			add(new DoubleSetting("height", "Hoehe", "Zusaetzliche Hoehe des Hintergrunds", 0.0, 0.0, 40.0, 1.0));

	protected final EnumSetting<BackgroundMode> background =
			add(new EnumSetting<>("background", "Hintergrund", "Blur, Farbe oder gar nichts", BackgroundMode.BLUR));

	protected final ColorSetting backgroundColor =
			add(new ColorSetting("bg_color", "Hintergrundfarbe", "Farbe der Flaeche", 0xB012101A));

	protected final ColorSetting textColor =
			add(new ColorSetting("text_color", "Textfarbe", "Farbe der Beschriftung", Theme.HUD_TEXT));

	protected final ColorSetting accentColor =
			add(new ColorSetting("accent_color", "Akzentfarbe", "Farbe des Wertes", Theme.ACCENT_2));

	protected final StringSetting format =
			add(new StringSetting("format", "Format",
					"Platzhalter: {left} = Beschriftung, {right} = Wert", "{left} {right}"));

	protected final BoolSetting rounded =
			add(new BoolSetting("rounded", "Ecken abrunden", "Runde statt gekappter Ecken", false));

	protected final IntSetting radius =
			add(new IntSetting("radius", "Radius", "Staerke der Rundung bzw. Kappung", 3, 0, 10));

	protected final BoolSetting outline =
			add(new BoolSetting("outline", "Glow-Kontur", "Feine leuchtende Umrandung", true));

	protected final BoolSetting shadow =
			add(new BoolSetting("shadow", "Textschatten", "Schatten hinter dem Text", false));

	protected HudModule(String id, String displayName, String description, float defaultX, float defaultY) {
		super(id, displayName, description, Category.HUD);
		this.posX = defaultX;
		this.posY = defaultY;
	}

	// ---------------------------------------------------------------
	// Inhalt - von Unterklassen geliefert
	// ---------------------------------------------------------------

	/** Beschriftung links (Platzhalter {left}). Standard: Anzeigename. */
	public String getLeftText() {
		return getDisplayName();
	}

	/** Wert rechts (Platzhalter {right}). */
	public abstract String getRightText();

	/**
	 * Die zu zeichnenden Zeilen. Standardmaessig genau eine Zeile aus dem
	 * Format-Textfeld. Mehrzeilige Module (z.B. Koordinaten) ueberschreiben das.
	 */
	public List<String> getLines() {
		return List.of(formatLine(getLeftText(), getRightText()));
	}

	/**
	 * Ersetzt die Platzhalter <code>{left}</code> und <code>{right}</code> im
	 * Format-String durch die uebergebenen Werte.
	 */
	public String formatLine(String left, String right) {
		return format.value()
				.replace("{left}", left == null ? "" : left)
				.replace("{right}", right == null ? "" : right);
	}

	/**
	 * Vorschautext fuer den HUD-Editor, falls das Modul gerade keine echten
	 * Daten liefern kann (z.B. kein Server verbunden).
	 */
	public List<String> getPreviewLines() {
		return getLines();
	}

	// ---------------------------------------------------------------
	// Position
	// ---------------------------------------------------------------

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosition(float x, float y) {
		this.posX = Math.max(0f, Math.min(1f, x));
		this.posY = Math.max(0f, Math.min(1f, y));
	}

	public double getScale() {
		return scale.value();
	}

	/** Breite des Elements in Pixeln (vor Skalierung). */
	public int getWidth(List<String> lines) {
		int widest = 0;

		for (String line : lines) {
			widest = Math.max(widest, Draw.textWidth(line));
		}

		return widest + PADDING_X * 2 + (int) extraWidth.value();
	}

	/** Hoehe des Elements in Pixeln (vor Skalierung). */
	public int getHeight(List<String> lines) {
		return lines.size() * LINE_HEIGHT + PADDING_Y * 2 - 2 + (int) extraHeight.value();
	}

	/** Linke obere Ecke in Bildschirmkoordinaten (vor Skalierung). */
	public int getScreenX(int screenWidth, int elementWidth) {
		int scaled = (int) (elementWidth * getScale());
		return Math.round(Math.min(posX * screenWidth, screenWidth - scaled));
	}

	public int getScreenY(int screenHeight, int elementHeight) {
		int scaled = (int) (elementHeight * getScale());
		return Math.round(Math.min(posY * screenHeight, screenHeight - scaled));
	}

	// ---------------------------------------------------------------
	// Rendering
	// ---------------------------------------------------------------

	/**
	 * Zeichnet das Element inklusive Hintergrund an seiner gespeicherten
	 * Position. Wird sowohl vom {@link HudManager} im Spiel als auch vom
	 * HUD-Editor benutzt.
	 */
	public void render(GuiGraphicsExtractor g, List<String> lines) {
		if (lines.isEmpty()) {
			return;
		}

		int width = getWidth(lines);
		int height = getHeight(lines);

		int x = getScreenX(g.guiWidth(), width);
		int y = getScreenY(g.guiHeight(), height);

		Matrix3x2fStack pose = g.pose();
		pose.pushMatrix();

		// Erst an die Zielposition, dann skalieren: so waechst das Element
		// von seiner linken oberen Ecke aus.
		pose.translate(x, y);
		pose.scale((float) getScale(), (float) getScale());

		renderBackground(g, width, height);
		renderLines(g, lines);

		pose.popMatrix();
	}

	protected void renderBackground(GuiGraphicsExtractor g, int width, int height) {
		BackgroundMode mode = background.value();

		if (mode == BackgroundMode.BLANK) {
			return;
		}

		// BLUR wird als weiche, dunkle Flaeche angenaehert. Ein echter
		// Gauss-Blur ist im HUD-Stratum nicht ohne Weiteres verfuegbar - im
		// Menue dagegen schon (siehe VoidrixMenuScreen#extractBackground).
		int color = mode == BackgroundMode.BLUR
				? Theme.withAlpha(Theme.BASE, 150)
				: backgroundColor.value();

		if (rounded.value()) {
			Draw.roundedRect(g, 0, 0, width, height, radius.value(), color);
		} else {
			Draw.cutCornerRect(g, 0, 0, width, height, radius.value(), color);
		}

		if (outline.value()) {
			// feine glühende Kontur statt hartem Schatten
			Draw.cutCornerOutline(g, 0, 0, width, height, rounded.value() ? 0 : radius.value(),
					Theme.withAlpha(accentColor.value(), 90));
		}
	}

	protected void renderLines(GuiGraphicsExtractor g, List<String> lines) {
		int y = PADDING_Y;

		for (String line : lines) {
			g.text(Draw.font(), line, PADDING_X, y, textColor.value(), shadow.value());
			y += LINE_HEIGHT;
		}
	}

	// ---------------------------------------------------------------
	// Persistenz - Position zusaetzlich zu den Settings
	// ---------------------------------------------------------------

	@Override
	public JsonObject save() {
		JsonObject root = super.save();

		JsonObject position = new JsonObject();
		position.addProperty("x", posX);
		position.addProperty("y", posY);
		root.add("position", position);

		return root;
	}

	@Override
	public void load(JsonObject root) {
		super.load(root);

		if (root != null && root.has("position") && root.get("position").isJsonObject()) {
			JsonObject position = root.getAsJsonObject("position");

			if (position.has("x") && position.has("y")) {
				setPosition(position.get("x").getAsFloat(), position.get("y").getAsFloat());
			}
		}
	}
}
