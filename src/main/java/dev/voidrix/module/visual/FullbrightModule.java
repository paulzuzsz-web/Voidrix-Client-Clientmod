package dev.voidrix.module.visual;

import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.DoubleSetting;
import net.minecraft.client.Minecraft;

/**
 * Lifts the brightness slider past its usual ceiling so caves are readable.
 *
 * <p>The value the player had before is captured on enable and put back on disable, so switching
 * this off never leaves their own brightness setting changed behind their back.
 */
public final class FullbrightModule extends Module {
    private final DoubleSetting brightness;

    private Double previousGamma;

    public FullbrightModule() {
        super("fullbright", "Fullbright", "See in the dark without a torch", Category.VISUAL);
        this.brightness = addDouble("brightness", "Brightness", "How far past the vanilla maximum to go",
                4.0, 1.0, 15.0, 1, "x");
    }

    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (previousGamma == null) {
            previousGamma = mc.options.gamma().get();
        }
        apply();
    }

    @Override
    public void onDisable() {
        restore();
    }

    @Override
    public void onTick(Minecraft mc) {
        // Reapply every tick: opening the video settings screen writes the slider's own value back.
        apply();
    }

    private void apply() {
        Minecraft mc = Minecraft.getInstance();
        // Only write when it actually differs - setting an option every tick churns for nothing.
        if (!mc.options.gamma().get().equals(brightness.value())) {
            mc.options.gamma().set(brightness.value());
        }
    }

    /** Puts the player's original brightness back. Safe to call when never enabled. */
    public void restore() {
        if (previousGamma == null) {
            return;
        }
        Minecraft.getInstance().options.gamma().set(previousGamma);
        previousGamma = null;
    }
}
