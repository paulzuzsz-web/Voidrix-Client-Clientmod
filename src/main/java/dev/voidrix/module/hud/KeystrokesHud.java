package dev.voidrix.module.hud;

import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * The classic movement key display.
 *
 * <p>Each key fades between its idle and pressed colour rather than snapping, which is what stops
 * a fast tap from being invisible at high frame rates.
 */
public final class KeystrokesHud extends HudModule {
    private static final int KEY = 16;
    private static final int GAP = 2;
    private static final int ROW = KEY + GAP;

    private final BoolSetting showMouse;
    private final BoolSetting showSpace;
    private final ColorSetting pressedColor;

    // Per-key press animation, 0 = idle, 1 = fully pressed.
    private final float[] anim = new float[7];
    private long lastFrame = System.nanoTime();

    private static final int W = 0;
    private static final int A = 1;
    private static final int S = 2;
    private static final int D = 3;
    private static final int LMB = 4;
    private static final int RMB = 5;
    private static final int SPACE = 6;

    public KeystrokesHud() {
        super("keystrokes", "Keystrokes", "Shows which movement keys you are pressing", 0.0, 0.86);
        this.showMouse = addBool("show_mouse", "Show mouse", "Include the two mouse buttons", true);
        this.showSpace = addBool("show_space", "Show jump", "Include a jump bar", true);
        this.pressedColor = addColor("pressed_color", "Pressed colour", "Fill colour while a key is held", Theme.ACCENT);
    }

    private static boolean down(KeyMapping mapping) {
        return mapping.isDown();
    }

    @Override
    public void onFrame() {
        Minecraft mc = Minecraft.getInstance();
        long now = System.nanoTime();
        float dt = (now - lastFrame) / 1_000_000_000f;
        lastFrame = now;
        // Clamp so a stutter or a paused game does not make every key snap at once.
        dt = Math.clamp(dt, 0f, 0.1f);

        boolean inGame = mc.mouseHandler.isMouseGrabbed();
        var options = mc.options;
        boolean[] state = {
                inGame && down(options.keyUp),
                inGame && down(options.keyLeft),
                inGame && down(options.keyDown),
                inGame && down(options.keyRight),
                inGame && down(options.keyAttack),
                inGame && down(options.keyUse),
                inGame && down(options.keyJump),
        };

        float rate = dt / Theme.ANIM;
        for (int i = 0; i < anim.length; i++) {
            float target = state[i] ? 1f : 0f;
            anim[i] += Math.clamp(target - anim[i], -rate, rate);
            anim[i] = Math.clamp(anim[i], 0f, 1f);
        }
    }

    @Override
    public int contentWidth(Font font) {
        return KEY * 3 + GAP * 2;
    }

    @Override
    public int contentHeight(Font font) {
        int rows = 2;
        if (showMouse.value()) {
            rows++;
        }
        if (showSpace.value()) {
            rows++;
        }
        return rows * KEY + (rows - 1) * GAP;
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        int full = contentWidth(font);
        int half = (full - GAP) / 2;

        key(g, font, KEY + GAP, 0, KEY, KEY, "W", anim[W]);
        key(g, font, 0, ROW, KEY, KEY, "A", anim[A]);
        key(g, font, KEY + GAP, ROW, KEY, KEY, "S", anim[S]);
        key(g, font, (KEY + GAP) * 2, ROW, KEY, KEY, "D", anim[D]);

        int y = ROW * 2;
        if (showMouse.value()) {
            key(g, font, 0, y, half, KEY, "LMB", anim[LMB]);
            key(g, font, half + GAP, y, full - half - GAP, KEY, "RMB", anim[RMB]);
            y += ROW;
        }
        if (showSpace.value()) {
            key(g, font, 0, y, full, KEY, "___", anim[SPACE]);
        }
    }

    /** Draws one key cap, blended between idle and pressed by {@code t}. */
    private void key(GuiGraphicsExtractor g, Font font, int x, int y, int w, int h, String label, float t) {
        int idleFill = 0x8C14141C;
        int pressFill = Theme.alpha(pressedColor.resolve(), 0.9f);
        int fill = Theme.mix(idleFill, pressFill, t);

        Draw.roundRect(g, x, y, w, h, Theme.RADIUS_SM, fill);
        Draw.roundRectOutline(g, x, y, w, h, Theme.RADIUS_SM, 1f,
                Theme.mix(Theme.alpha(Theme.BORDER, 0.8f), Theme.alpha(pressedColor.resolve(), 1f), t));

        int textColor = Theme.mix(Theme.TEXT_DIM, Theme.TEXT, t);
        float tx = x + (w - font.width(label)) / 2f;
        float ty = y + (h - font.lineHeight) / 2f + 1f;
        if (useShadow()) {
            Draw.textShadow(g, font, label, tx, ty, textColor);
        } else {
            Draw.text(g, font, label, tx, ty, textColor);
        }
    }
}
