package dev.voidrix.module.hud;

import dev.voidrix.setting.EnumSetting;
import net.minecraft.client.Minecraft;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Clicks per second.
 *
 * <p>Sampled every frame rather than every tick: at 20 ticks a second a fast click can begin and
 * end inside one tick and never be seen. Presses are timestamped into a rolling one-second window,
 * which is what "per second" actually means - no averaging or decay.
 */
public final class CpsHud extends SimpleHudModule {
    private static final long WINDOW_MS = 1000L;

    private final EnumSetting<Button> button;

    private final Deque<Long> leftClicks = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();
    private boolean leftWasDown;
    private boolean rightWasDown;

    public CpsHud() {
        super("cps", "CPS", "How many times per second you are clicking", 0.0, 0.0);
        this.button = addEnum("button", "Button", "Which mouse button to count", Button.LEFT);
    }

    @Override
    public void onFrame() {
        Minecraft mc = Minecraft.getInstance();
        long now = System.currentTimeMillis();

        // Ignore clicks made while a menu is open - those are UI interactions, not game clicks.
        boolean inGame = mc.mouseHandler.isMouseGrabbed();

        boolean leftDown = inGame && mc.options.keyAttack.isDown();
        if (leftDown && !leftWasDown) {
            leftClicks.addLast(now);
        }
        leftWasDown = leftDown;

        boolean rightDown = inGame && mc.options.keyUse.isDown();
        if (rightDown && !rightWasDown) {
            rightClicks.addLast(now);
        }
        rightWasDown = rightDown;

        prune(leftClicks, now);
        prune(rightClicks, now);
    }

    private static void prune(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() > WINDOW_MS) {
            clicks.removeFirst();
        }
    }

    @Override
    protected String label() {
        return "CPS";
    }

    @Override
    protected String value() {
        return switch (button.value()) {
            case LEFT -> String.valueOf(leftClicks.size());
            case RIGHT -> String.valueOf(rightClicks.size());
            case BOTH -> leftClicks.size() + " | " + rightClicks.size();
        };
    }

    public enum Button {
        LEFT,
        RIGHT,
        BOTH
    }
}
