package dev.voidrix.module.visual;

import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.IntSetting;
import net.minecraft.client.Minecraft;

/**
 * Overrides your field of view beyond what the vanilla slider offers.
 *
 * <p>Like the other modules that drive a game option, the value you had is captured on enable and
 * handed back on disable, so switching this off does not silently rewrite your own setting.
 */
public final class FovModule extends Module {
    private final IntSetting fov;

    private Integer previousFov;

    public FovModule() {
        super("fov", "FOV changer", "Set a field of view outside the vanilla range", Category.VISUAL);
        this.fov = addInt("value", "Field of view", "Vertical field of view in degrees", 90, 30, 150, "°");
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (previousFov == null) {
            previousFov = mc.options.fov().get();
        }
    }

    @Override
    public void onDisable() {
        restore();
    }

    @Override
    public void onTick(Minecraft mc) {
        // The zoom module drives FOV too; leave it alone while it is holding the value.
        if (!mc.options.fov().get().equals(fov.value())) {
            mc.options.fov().set(fov.value());
        }
    }

    /** Puts the player's original field of view back. */
    public void restore() {
        if (previousFov == null) {
            return;
        }
        Minecraft.getInstance().options.fov().set(previousFov);
        previousFov = null;
    }
}
