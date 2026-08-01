package dev.voidrix.module.combat;

import dev.voidrix.module.Category;
import dev.voidrix.module.hud.SimpleHudModule;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;

/**
 * Your own food saturation - the hidden buffer that has to run out before the hunger bar moves.
 *
 * <p>Vanilla tracks this for you but never draws it, which is why a fight can turn without warning.
 * This is your own stat, read from your own client.
 */
public final class SaturationHud extends SimpleHudModule {
    public SaturationHud() {
        super("saturation", "Saturation", "The hidden buffer behind your hunger bar",
                Category.COMBAT, 0.5, 0.78, Theme.TEXT);
    }

    private static float saturation() {
        var player = Minecraft.getInstance().player;
        return player == null ? -1f : player.getFoodData().getSaturationLevel();
    }

    @Override
    public boolean hasContent() {
        return saturation() >= 0f;
    }

    @Override
    protected String label() {
        return "Sat";
    }

    @Override
    protected String value() {
        float saturation = saturation();
        return saturation < 0f ? "--" : String.format("%.1f", saturation);
    }

    @Override
    protected int valueColor() {
        float saturation = saturation();
        if (saturation <= 0f) {
            return Theme.DANGER;
        }
        if (saturation < 3f) {
            return Theme.WARN;
        }
        return super.valueColor();
    }
}
