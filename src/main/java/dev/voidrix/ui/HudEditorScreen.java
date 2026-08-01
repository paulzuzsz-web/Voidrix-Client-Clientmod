package dev.voidrix.ui;

import dev.voidrix.VoidrixClient;
import dev.voidrix.module.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Drag-and-drop placement for HUD widgets.
 *
 * <p>Widgets snap to the screen edges, the centre lines and to each other's edges once the pointer
 * comes within a few pixels, with a guide drawn for whichever snap took effect - the same
 * behaviour as any layout tool, which is what makes lining widgets up feel effortless.
 */
public final class HudEditorScreen extends Screen {
    private static final int SNAP = 5;

    private static boolean open;

    private HudModule dragged;
    private int grabX;
    private int grabY;

    /** Guides to draw this frame, rebuilt on every drag. */
    private Integer guideX;
    private Integer guideY;

    private long lastFrame = System.nanoTime();
    private final Anim fade = new Anim(0f, 0.14f);

    public HudEditorScreen() {
        super(Component.literal("Voidrix HUD"));
    }

    /** True while the editor is on screen, so the normal HUD pass can stand down. */
    public static boolean isOpen() {
        return open;
    }

    @Override
    protected void init() {
        open = true;
        fade.target(1f);
    }

    @Override
    public void removed() {
        open = false;
        VoidrixClient.config().save();
        super.removed();
    }

    private List<HudModule> widgets() {
        return VoidrixClient.modules().hudModules();
    }

    // -------------------------------------------------------------------------------------
    // Render
    // -------------------------------------------------------------------------------------

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        long now = System.nanoTime();
        float dt = Math.clamp((now - lastFrame) / 1_000_000_000f, 0f, 0.2f);
        lastFrame = now;
        float t = fade.update(dt);

        g.fill(0, 0, width, height, Theme.alpha(0xFF05050A, 0.55f * t));

        // Faint centre lines to aim at.
        Draw.vLine(g, width / 2f, 0, height, Theme.alpha(Theme.ACCENT, 0.10f * t));
        Draw.hLine(g, 0, height / 2f, width, Theme.alpha(Theme.ACCENT, 0.10f * t));

        for (HudModule widget : widgets()) {
            if (!widget.isEnabled()) {
                continue;
            }
            widget.onFrame();

            int x = widget.screenX(font, width);
            int y = widget.screenY(font, height);
            int w = widget.screenWidth(font);
            int h = widget.screenHeight(font);

            boolean hovered = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
            boolean active = widget == dragged;

            // Selection frame around the widget's real bounds.
            int frame = active ? Theme.ACCENT
                    : (hovered ? Theme.alpha(Theme.ACCENT, 0.6f) : Theme.alpha(Theme.BORDER, 0.85f));
            Draw.roundRect(g, x - 2, y - 2, w + 4, h + 4, Theme.RADIUS_SM,
                    Theme.alpha(active ? Theme.ACCENT : 0xFF000000, active ? 0.12f : 0.35f));
            Draw.roundRectOutline(g, x - 2, y - 2, w + 4, h + 4, Theme.RADIUS_SM, 1f, frame);

            try {
                widget.renderWidget(g, font, width, height);
            } catch (RuntimeException e) {
                VoidrixClient.LOGGER.error("[Voidrix] widget '{}' failed to render in the editor",
                        widget.id(), e);
            }

            if (hovered || active) {
                String label = widget.displayName();
                float lw = font.width(label) + 10;
                float lx = Math.clamp(x + w / 2f - lw / 2f, 2f, width - lw - 2f);
                float ly = y - 14f < 2f ? y + h + 4f : y - 14f;
                Draw.roundRect(g, lx, ly, lw, 12, Theme.RADIUS_SM, 0xE60F0F16);
                Draw.textCentered(g, font, label, lx + lw / 2f, ly + 2, Theme.TEXT);
            }
        }

        if (guideX != null) {
            Draw.vLine(g, guideX, 0, height, Theme.ACCENT_ALT);
        }
        if (guideY != null) {
            Draw.hLine(g, 0, guideY, width, Theme.ACCENT_ALT);
        }

        renderHint(g, t);
    }

    private void renderHint(GuiGraphicsExtractor g, float t) {
        String title = "HUD Editor";
        String hint = "Drag to move  -  R resets the widget under the cursor  -  Esc to finish";
        int w = Math.max(font.width(title), font.width(hint)) + 24;
        int h = 34;
        float x = (width - w) / 2f;
        float y = height - h - 12;

        Draw.shadow(g, x, y, w, h, Theme.RADIUS_MD, 8f, 0xFF000000);
        Draw.roundRect(g, x, y, w, h, Theme.RADIUS_MD, Theme.alpha(Theme.SURFACE, t));
        Draw.roundRectOutline(g, x, y, w, h, Theme.RADIUS_MD, 1f, Theme.alpha(Theme.BORDER, t));
        Draw.textCentered(g, font, title, x + w / 2f, y + 7, Theme.ACCENT);
        Draw.textCentered(g, font, hint, x + w / 2f, y + 19, Theme.TEXT_FAINT);
    }

    // -------------------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------------------

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }
        int mx = (int) event.x();
        int my = (int) event.y();

        // Topmost widget wins, matching the draw order.
        List<HudModule> widgets = widgets();
        for (int i = widgets.size() - 1; i >= 0; i--) {
            HudModule widget = widgets.get(i);
            if (!widget.isEnabled()) {
                continue;
            }
            int x = widget.screenX(font, width);
            int y = widget.screenY(font, height);
            if (mx >= x && mx < x + widget.screenWidth(font)
                    && my >= y && my < y + widget.screenHeight(font)) {
                dragged = widget;
                grabX = mx - x;
                grabY = my - y;
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (dragged == null) {
            return super.mouseDragged(event, dragX, dragY);
        }

        int w = dragged.screenWidth(font);
        int h = dragged.screenHeight(font);
        int x = (int) event.x() - grabX;
        int y = (int) event.y() - grabY;

        guideX = null;
        guideY = null;

        // Snap against the screen, then against every other widget.
        x = snapAxis(x, w, width, true);
        y = snapAxis(y, h, height, false);

        x = Math.clamp(x, 0, Math.max(0, width - w));
        y = Math.clamp(y, 0, Math.max(0, height - h));

        dragged.setPosFromScreen(font, width, height, x, y);
        return true;
    }

    /**
     * Snaps one axis to the screen edges, the centre and neighbouring widget edges, recording the
     * guide to draw. Returns the adjusted coordinate.
     */
    private int snapAxis(int pos, int size, int screen, boolean horizontal) {
        int best = pos;
        int bestDelta = SNAP + 1;
        Integer guide = null;

        int[] targets = {0, (screen - size) / 2, screen - size};
        int[] guides = {0, screen / 2, screen};
        for (int i = 0; i < targets.length; i++) {
            int delta = Math.abs(pos - targets[i]);
            if (delta < bestDelta) {
                bestDelta = delta;
                best = targets[i];
                guide = guides[i];
            }
        }

        for (HudModule other : widgets()) {
            if (other == dragged || !other.isEnabled()) {
                continue;
            }
            int otherPos = horizontal ? other.screenX(font, width) : other.screenY(font, height);
            int otherSize = horizontal ? other.screenWidth(font) : other.screenHeight(font);

            int[] candidates = {otherPos, otherPos + otherSize - size, otherPos + otherSize, otherPos - size};
            int[] candidateGuides = {otherPos, otherPos + otherSize, otherPos + otherSize, otherPos};
            for (int i = 0; i < candidates.length; i++) {
                int delta = Math.abs(pos - candidates[i]);
                if (delta < bestDelta) {
                    bestDelta = delta;
                    best = candidates[i];
                    guide = candidateGuides[i];
                }
            }
        }

        if (bestDelta <= SNAP) {
            if (horizontal) {
                guideX = guide;
            } else {
                guideY = guide;
            }
            return best;
        }
        return pos;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (dragged != null) {
            dragged = null;
            guideX = null;
            guideY = null;
            VoidrixClient.config().save();
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_R) {
            // Reset whatever the pointer is over back to the top-left.
            if (minecraft == null) {
                return true;
            }
            double mx = minecraft.mouseHandler.getScaledXPos(minecraft.getWindow());
            double my = minecraft.mouseHandler.getScaledYPos(minecraft.getWindow());
            for (HudModule widget : widgets()) {
                if (!widget.isEnabled()) {
                    continue;
                }
                int x = widget.screenX(font, width);
                int y = widget.screenY(font, height);
                if (mx >= x && mx < x + widget.screenWidth(font)
                        && my >= y && my < y + widget.screenHeight(font)) {
                    widget.setPos(0.0, 0.0);
                    VoidrixClient.config().save();
                    return true;
                }
            }
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /** Opens the editor. */
    public static void open() {
        Minecraft.getInstance().setScreenAndShow(new HudEditorScreen());
    }
}
