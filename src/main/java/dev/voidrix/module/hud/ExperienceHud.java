package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import net.minecraft.client.Minecraft;

/** Your experience level, and how far into the next one you are. */
public final class ExperienceHud extends SimpleHudModule {
    private final BoolSetting showProgress;

    public ExperienceHud() {
        super("experience", "Experience", "Your level and progress to the next", 0.0, 0.60);
        this.showProgress = addBool("progress", "Show progress",
                "Append how far through the level you are", true);
    }

    @Override
    public boolean hasContent() {
        return Minecraft.getInstance().player != null;
    }

    @Override
    protected String label() {
        return "XP";
    }

    @Override
    protected String value() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return "--";
        }
        if (!showProgress.value()) {
            return String.valueOf(player.experienceLevel);
        }
        return player.experienceLevel + "  " + Math.round(player.experienceProgress * 100f) + "%";
    }
}
