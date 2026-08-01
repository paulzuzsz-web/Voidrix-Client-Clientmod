package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;

/** Your food level as a number. Pairs with the saturation widget under Combat. */
public final class HungerHud extends SimpleHudModule {
    private final BoolSetting grade;

    public HungerHud() {
        super("hunger", "Hunger", "Your food level as a number", 0.0, 0.55);
        this.grade = addBool("grade", "Colour by value", "Turn amber then red as you get hungry", true);
    }

    private static int food() {
        var player = Minecraft.getInstance().player;
        return player == null ? -1 : player.getFoodData().getFoodLevel();
    }

    @Override
    public boolean hasContent() {
        return food() >= 0;
    }

    @Override
    protected String label() {
        return "Food";
    }

    @Override
    protected String value() {
        int food = food();
        return food < 0 ? "--" : food + "/20";
    }

    @Override
    protected int valueColor() {
        if (!grade.value()) {
            return super.valueColor();
        }
        int food = food();
        if (food >= 14) {
            return Theme.SUCCESS;
        }
        // Below six you stop regenerating; below one you start starving.
        if (food >= 6) {
            return Theme.WARN;
        }
        return Theme.DANGER;
    }
}
