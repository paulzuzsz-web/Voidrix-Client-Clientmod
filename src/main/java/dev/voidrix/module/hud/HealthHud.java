package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;

/** Your health as a number, which is easier to read at a glance than counting hearts. */
public final class HealthHud extends SimpleHudModule {
    private final EnumSetting<Display> display;
    private final BoolSetting includeAbsorption;
    private final BoolSetting grade;

    public HealthHud() {
        super("health", "Health", "Your health as a number", 0.0, 0.50);
        this.display = addEnum("display", "Display", "How to write it", Display.CURRENT);
        this.includeAbsorption = addBool("absorption", "Add absorption",
                "Count golden hearts towards the total", true);
        this.grade = addBool("grade", "Colour by value", "Green when healthy, red when not", true);
    }

    private float health() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return -1f;
        }
        return player.getHealth() + (includeAbsorption.value() ? player.getAbsorptionAmount() : 0f);
    }

    private static float maxHealth() {
        var player = Minecraft.getInstance().player;
        return player == null ? 20f : player.getMaxHealth();
    }

    @Override
    public boolean hasContent() {
        return health() >= 0f;
    }

    @Override
    protected String label() {
        return "HP";
    }

    @Override
    protected String value() {
        float health = health();
        if (health < 0f) {
            return "--";
        }
        return switch (display.value()) {
            case CURRENT -> trim(health);
            case FRACTION -> trim(health) + "/" + trim(maxHealth());
            case PERCENT -> Math.round(health / Math.max(1f, maxHealth()) * 100f) + "%";
        };
    }

    /** Drops a trailing {@code .0}, since whole hearts are the common case. */
    private static String trim(float v) {
        return v == Math.rint(v) ? String.valueOf((int) v) : String.format("%.1f", v);
    }

    @Override
    protected int valueColor() {
        if (!grade.value()) {
            return super.valueColor();
        }
        float fraction = health() / Math.max(1f, maxHealth());
        if (fraction > 0.6f) {
            return Theme.SUCCESS;
        }
        if (fraction > 0.3f) {
            return Theme.WARN;
        }
        return Theme.DANGER;
    }

    public enum Display {
        CURRENT,
        FRACTION,
        PERCENT
    }
}
