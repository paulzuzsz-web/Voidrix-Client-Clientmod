package dev.voidrix.module.hud;

import dev.voidrix.setting.EnumSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

/** Remaining durability of whatever is in your main hand. */
public final class DurabilityHud extends SimpleHudModule {
    private final EnumSetting<Display> display;

    public DurabilityHud() {
        super("durability", "Durability", "Remaining uses on your held item", 0.0, 0.45);
        this.display = addEnum("display", "Display", "How to write the remaining durability", Display.REMAINING);
    }

    private static ItemStack held() {
        var player = Minecraft.getInstance().player;
        return player == null ? ItemStack.EMPTY : player.getMainHandItem();
    }

    private static float fraction(ItemStack stack) {
        int max = stack.getMaxDamage();
        return max <= 0 ? 0f : (float) (max - stack.getDamageValue()) / max;
    }

    @Override
    public boolean hasContent() {
        ItemStack stack = held();
        return !stack.isEmpty() && stack.isDamageableItem();
    }

    @Override
    protected String label() {
        return "Durability";
    }

    @Override
    protected String value() {
        ItemStack stack = held();
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return "--";
        }
        int max = stack.getMaxDamage();
        int left = max - stack.getDamageValue();
        return switch (display.value()) {
            case REMAINING -> String.valueOf(left);
            case PERCENT -> Math.round(fraction(stack) * 100f) + "%";
            case FRACTION -> left + "/" + max;
        };
    }

    @Override
    protected int valueColor() {
        ItemStack stack = held();
        if (stack.isEmpty() || !stack.isDamageableItem()) {
            return Theme.TEXT_DIM;
        }
        float fraction = fraction(stack);
        if (fraction <= 0.2f) {
            return Theme.DANGER;
        }
        if (fraction <= 0.5f) {
            return Theme.WARN;
        }
        return super.valueColor();
    }

    public enum Display {
        REMAINING,
        PERCENT,
        FRACTION
    }
}
