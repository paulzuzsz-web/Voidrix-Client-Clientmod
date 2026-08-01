package dev.voidrix.module.visual;

import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import net.minecraft.client.Minecraft;

/** Turns off the walking head-bob, which some players find nauseating. */
public final class NoBobbingModule extends Module {
    private Boolean previous;

    public NoBobbingModule() {
        super("no_bobbing", "No view bobbing", "Stop the camera swaying as you walk", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (previous == null) {
            previous = mc.options.bobView().get();
        }
        mc.options.bobView().set(false);
    }

    @Override
    public void onDisable() {
        restore();
    }

    @Override
    public void onTick(Minecraft mc) {
        // The video settings screen can flip this back; keep it pinned while we are on.
        if (mc.options.bobView().get()) {
            mc.options.bobView().set(false);
        }
    }

    /** Puts the player's original bobbing preference back. */
    public void restore() {
        if (previous == null) {
            return;
        }
        Minecraft.getInstance().options.bobView().set(previous);
        previous = null;
    }
}
