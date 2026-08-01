package dev.voidrix;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

/** The key bindings Voidrix owns, all rebindable through the vanilla controls screen. */
public final class VoidrixKeys {
    /** Our own section in the controls list, labelled by {@code key.category.voidrix.general}. */
    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(VoidrixClient.MOD_ID, "general"));

    public static KeyMapping openMenu;
    public static KeyMapping openHudEditor;
    public static KeyMapping openWaypoints;
    public static KeyMapping zoom;

    private VoidrixKeys() {
    }

    public static void register() {
        openMenu = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.voidrix.menu", GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY));
        openHudEditor = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.voidrix.hud_editor", GLFW.GLFW_KEY_RIGHT_CONTROL, CATEGORY));
        openWaypoints = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.voidrix.waypoints", GLFW.GLFW_KEY_B, CATEGORY));
        zoom = KeyMappingHelper.registerKeyMapping(
                new KeyMapping("key.voidrix.zoom", GLFW.GLFW_KEY_C, CATEGORY));
    }
}
