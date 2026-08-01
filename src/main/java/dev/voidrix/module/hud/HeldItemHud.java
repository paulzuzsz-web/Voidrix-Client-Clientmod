package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

/** Name of the item in your main hand, plus how many you are carrying in total. */
public final class HeldItemHud extends SimpleHudModule {
    private final BoolSetting showTotal;

    public HeldItemHud() {
        super("held_item", "Held item", "What you are holding, and how many you have", 0.0, 0.40);
        this.showTotal = addBool("show_total", "Show total",
                "Count every matching item across your inventory", true);
    }

    private static ItemStack held() {
        var player = Minecraft.getInstance().player;
        return player == null ? ItemStack.EMPTY : player.getMainHandItem();
    }

    /** Number of matching items across the whole inventory, hotbar included. */
    private static int totalHeld(ItemStack held) {
        var player = Minecraft.getInstance().player;
        if (player == null || held.isEmpty()) {
            return 0;
        }
        int total = 0;
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && ItemStack.isSameItem(stack, held)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    @Override
    public boolean hasContent() {
        return !held().isEmpty();
    }

    @Override
    protected String label() {
        return "Held";
    }

    @Override
    protected String value() {
        ItemStack stack = held();
        if (stack.isEmpty()) {
            return "--";
        }
        String name = stack.getHoverName().getString();
        if (!showTotal.value()) {
            return name;
        }
        return name + " x" + totalHeld(stack);
    }
}
