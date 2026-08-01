package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;

/** Current frame rate, optionally tinted by how healthy that frame rate is. */
public final class FpsHud extends SimpleHudModule {
    private final BoolSetting grade;

    public FpsHud() {
        super("fps", "FPS", "Frames per second", 0.0, 0.0);
        this.grade = addBool("grade", "Colour by value", "Green when smooth, red when struggling", true);
    }

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    protected String label() {
        return "FPS";
    }

    @Override
    protected String value() {
        return String.valueOf(Minecraft.getInstance().getFps());
    }

    @Override
    protected int valueColor() {
        if (!grade.value()) {
            return super.valueColor();
        }
        int fps = Minecraft.getInstance().getFps();
        if (fps >= 90) {
            return Theme.SUCCESS;
        }
        if (fps >= 45) {
            return Theme.WARN;
        }
        return Theme.DANGER;
    }
}
