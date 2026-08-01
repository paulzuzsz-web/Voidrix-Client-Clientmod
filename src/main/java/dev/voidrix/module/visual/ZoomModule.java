package dev.voidrix.module.visual;

import dev.voidrix.VoidrixKeys;
import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.DoubleSetting;
import net.minecraft.client.Minecraft;

/**
 * Hold a key to narrow the field of view.
 *
 * <p>Works by driving the vanilla FOV option and restoring it on release. The player's own FOV is
 * captured the moment the zoom starts, so changing it mid-session is picked up rather than
 * overwritten with a stale value.
 */
public final class ZoomModule extends Module {
    private final DoubleSetting factor;
    private final BoolSetting smooth;

    private Integer originalFov;
    private double current;

    public ZoomModule() {
        super("zoom", "Zoom", "Hold a key to zoom in, like a spyglass without the item", Category.VISUAL);
        this.factor = addDouble("factor", "Zoom factor", "How far in the zoom goes", 4.0, 1.5, 10.0, 1, "x");
        this.smooth = addBool("smooth", "Smooth", "Ease into the zoom instead of snapping", true);
    }

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    public void onDisable() {
        release();
    }

    @Override
    public void onTick(Minecraft mc) {
        boolean held = VoidrixKeys.zoom != null && VoidrixKeys.zoom.isDown() && mc.mouseHandler.isMouseGrabbed();

        if (held) {
            if (originalFov == null) {
                originalFov = mc.options.fov().get();
                current = originalFov;
            }
            double target = originalFov / factor.value();
            current = smooth.value() ? current + (target - current) * 0.45 : target;
            mc.options.fov().set((int) Math.round(current));
        } else {
            release();
        }
    }

    /** Restores the player's field of view if we are currently holding it hostage. */
    private void release() {
        if (originalFov == null) {
            return;
        }
        Minecraft.getInstance().options.fov().set(originalFov);
        originalFov = null;
    }
}
