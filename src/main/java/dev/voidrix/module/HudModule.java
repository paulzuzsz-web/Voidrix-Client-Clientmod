package dev.voidrix.module;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.DoubleSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * A module that paints a widget over the game.
 *
 * <p>Position is stored as a pair of 0..1 fractions of the <em>free</em> space on each axis, so a
 * widget dragged flush to an edge stays flush at any window size or GUI scale, and one left in the
 * middle stays centred. Subclasses only describe their content; the frame, padding, scaling and
 * drag handling all live here.
 */
public abstract class HudModule extends Module {
    /** Inner padding used when the backing panel is switched on. */
    public static final int PADDING = 4;

    private double posX;
    private double posY;

    protected final DoubleSetting scale;
    protected final BoolSetting background;
    protected final BoolSetting shadow;

    protected HudModule(String id, String name, String description, double defaultX, double defaultY) {
        super(id, name, description, Category.HUD);
        this.posX = defaultX;
        this.posY = defaultY;
        this.scale = addDouble("scale", "Scale", "Size of the widget", 1.0, 0.5, 3.0, 2, "x");
        this.background = addBool("background", "Panel", "Draw a rounded panel behind the widget", true);
        this.shadow = addBool("shadow", "Text shadow", "Drop shadow under the text", true);
    }

    // ---- Content contract -----------------------------------------------------------------

    /** Width of the content, before padding and scaling. */
    public abstract int contentWidth(Font font);

    /** Height of the content, before padding and scaling. */
    public int contentHeight(Font font) {
        return font.lineHeight;
    }

    /** Draws the content with its top-left corner at the origin. */
    public abstract void renderContent(GuiGraphicsExtractor g, Font font);

    /**
     * Whether the widget currently has anything to show. A widget that returns false is skipped
     * in game but still drawn in the HUD editor so it can be positioned.
     */
    public boolean hasContent() {
        return true;
    }

    /**
     * Called once per rendered frame, before the widget is measured.
     *
     * <p>Widgets that need to observe input at frame rate rather than tick rate - counting clicks,
     * tracking key state - sample here, because 20 ticks a second is too coarse to catch them.
     */
    public void onFrame() {
    }

    // ---- Geometry -------------------------------------------------------------------------

    public double posX() {
        return posX;
    }

    public double posY() {
        return posY;
    }

    public void setPos(double x, double y) {
        this.posX = Math.clamp(x, 0.0, 1.0);
        this.posY = Math.clamp(y, 0.0, 1.0);
    }

    public float scaleValue() {
        return scale.floatValue();
    }

    private int pad() {
        return background.value() ? PADDING : 0;
    }

    /** Unscaled width of the whole widget including padding. */
    public int boxWidth(Font font) {
        return contentWidth(font) + pad() * 2;
    }

    /** Unscaled height of the whole widget including padding. */
    public int boxHeight(Font font) {
        return contentHeight(font) + pad() * 2;
    }

    /** Scaled on-screen width. */
    public int screenWidth(Font font) {
        return Math.round(boxWidth(font) * scaleValue());
    }

    /** Scaled on-screen height. */
    public int screenHeight(Font font) {
        return Math.round(boxHeight(font) * scaleValue());
    }

    /** Resolved top-left x for the given screen width. */
    public int screenX(Font font, int screenW) {
        return (int) Math.round(posX * Math.max(0, screenW - screenWidth(font)));
    }

    /** Resolved top-left y for the given screen height. */
    public int screenY(Font font, int screenH) {
        return (int) Math.round(posY * Math.max(0, screenH - screenHeight(font)));
    }

    /** Converts an absolute pixel position back into the stored 0..1 fractions. */
    public void setPosFromScreen(Font font, int screenW, int screenH, int x, int y) {
        int freeX = Math.max(1, screenW - screenWidth(font));
        int freeY = Math.max(1, screenH - screenHeight(font));
        setPos((double) x / freeX, (double) y / freeY);
    }

    // ---- Rendering ------------------------------------------------------------------------

    /** Draws the widget at its configured position, panel and all. */
    public void renderWidget(GuiGraphicsExtractor g, Font font, int screenW, int screenH) {
        int x = screenX(font, screenW);
        int y = screenY(font, screenH);
        float s = scaleValue();

        var pose = g.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(s, s);

        if (background.value()) {
            int w = boxWidth(font);
            int h = boxHeight(font);
            Draw.roundRect(g, 0, 0, w, h, Theme.RADIUS_SM, 0x9E0B0B12);
            Draw.roundRectOutline(g, 0, 0, w, h, Theme.RADIUS_SM, 1f, Theme.alpha(Theme.BORDER, 0.65f));
        }

        pose.translate(pad(), pad());
        renderContent(g, font);

        pose.popMatrix();
    }

    /** Whether text drawn by this widget should carry a shadow. */
    protected boolean useShadow() {
        return shadow.value();
    }

    /** Convenience for the common "one line of text at the origin" case. */
    protected void line(GuiGraphicsExtractor g, Font font, String text, int color) {
        if (useShadow()) {
            Draw.textShadow(g, font, text, 0, 0, color);
        } else {
            Draw.text(g, font, text, 0, 0, color);
        }
    }
}
