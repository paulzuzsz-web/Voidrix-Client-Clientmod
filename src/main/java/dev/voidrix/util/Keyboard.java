package dev.voidrix.util;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

/**
 * Raw keyboard polling for per-module toggle keys.
 *
 * <p>Module hotkeys deliberately do not go through {@code KeyMapping}: registering one vanilla
 * binding per module would flood the controls screen with entries the player never asked for.
 * Polling GLFW directly keeps them out of the way, at the cost of having to track the edges here.
 */
public final class Keyboard {
    private static final Map<String, Boolean> previous = new HashMap<>();

    private Keyboard() {
    }

    /** Whether a GLFW key code is held right now. */
    public static boolean isDown(int keyCode) {
        if (keyCode <= 0) {
            return false;
        }
        long handle = Minecraft.getInstance().getWindow().handle();
        return GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;
    }

    /**
     * True on the frame the key goes down, false while it is held. The {@code id} separates one
     * caller's edge tracking from another's.
     */
    public static boolean justPressed(String id, int keyCode) {
        boolean down = isDown(keyCode);
        boolean was = previous.getOrDefault(id, Boolean.FALSE);
        previous.put(id, down);
        return down && !was;
    }

    /** Drops remembered state, so a rebind cannot fire an edge left over from the old key. */
    public static void forget(String id) {
        previous.remove(id);
    }

    /** Whether either Control key is held, for chord shortcuts. */
    public static boolean ctrlHeld() {
        return isDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isDown(GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    /**
     * True on the frame {@code key} goes down while Control is held.
     *
     * <p>Polled rather than registered as a key binding because Minecraft's bindings are single
     * keys - there is no way to express a chord through them.
     */
    public static boolean ctrlChord(String id, int key) {
        boolean pressed = justPressed(id, key);
        return pressed && ctrlHeld();
    }

    /** Human readable name for a key, for showing the current binding in the menu. */
    public static String nameOf(int keyCode) {
        if (keyCode <= 0) {
            return "None";
        }
        return switch (keyCode) {
            case GLFW.GLFW_KEY_SPACE -> "Space";
            case GLFW.GLFW_KEY_ENTER -> "Enter";
            case GLFW.GLFW_KEY_TAB -> "Tab";
            case GLFW.GLFW_KEY_BACKSPACE -> "Backspace";
            case GLFW.GLFW_KEY_ESCAPE -> "Escape";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "L Shift";
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "R Shift";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "L Ctrl";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "R Ctrl";
            case GLFW.GLFW_KEY_LEFT_ALT -> "L Alt";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "R Alt";
            case GLFW.GLFW_KEY_UP -> "Up";
            case GLFW.GLFW_KEY_DOWN -> "Down";
            case GLFW.GLFW_KEY_LEFT -> "Left";
            case GLFW.GLFW_KEY_RIGHT -> "Right";
            default -> {
                String name = GLFW.glfwGetKeyName(keyCode, 0);
                if (name != null && !name.isBlank()) {
                    yield name.toUpperCase(java.util.Locale.ROOT);
                }
                if (keyCode >= GLFW.GLFW_KEY_F1 && keyCode <= GLFW.GLFW_KEY_F25) {
                    yield "F" + (keyCode - GLFW.GLFW_KEY_F1 + 1);
                }
                yield "Key " + keyCode;
            }
        };
    }
}
