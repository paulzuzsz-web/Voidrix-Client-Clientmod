package dev.voidrix.ui;

import dev.voidrix.VoidrixClient;
import dev.voidrix.module.HudModule;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

/**
 * Draws every enabled HUD widget, on top of the vanilla HUD.
 *
 * <p>Registered as a single Fabric HUD element rather than one per module, so widget order stays
 * under our control and other mods only ever see one Voidrix layer to sort against.
 */
public final class HudRenderer {
    public static final Identifier ELEMENT_ID =
            Identifier.fromNamespaceAndPath(VoidrixClient.MOD_ID, "widgets");

    private HudRenderer() {
    }

    public static void register() {
        HudElementRegistry.addLast(ELEMENT_ID, HudRenderer::render);
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }
        // The HUD editor draws its own copy of every widget, so stay out of its way.
        if (HudEditorScreen.isOpen()) {
            return;
        }

        Font font = mc.font;
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // Waypoints go under the widgets, so a marker never covers a readout.
        var waypoints = VoidrixClient.waypointsModule();
        if (waypoints != null && waypoints.isEnabled()) {
            try {
                waypoints.render(graphics, font);
            } catch (RuntimeException e) {
                VoidrixClient.LOGGER.error("[Voidrix] waypoint rendering failed, disabling it", e);
                waypoints.setEnabled(false);
            }
        }

        for (HudModule widget : VoidrixClient.modules().hudModules()) {
            if (!widget.isEnabled()) {
                continue;
            }
            widget.onFrame();
            if (!widget.hasContent()) {
                continue;
            }
            try {
                widget.renderWidget(graphics, font, width, height);
            } catch (RuntimeException e) {
                // One misbehaving widget must not take the whole HUD - and with it the game - down.
                VoidrixClient.LOGGER.error("[Voidrix] widget '{}' failed to render, disabling it",
                        widget.id(), e);
                widget.setEnabled(false);
            }
        }
    }
}
