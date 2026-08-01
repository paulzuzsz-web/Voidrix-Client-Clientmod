package dev.voidrix.module.hud;

import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Your equipped armour and held item, with remaining durability. */
public final class ArmorHud extends HudModule {
    private static final int SLOT = 16;
    private static final int GAP = 3;

    private static final EquipmentSlot[] ARMOUR = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private final EnumSetting<Layout> layout;
    private final BoolSetting includeHand;
    private final BoolSetting showDurability;
    private final BoolSetting hideEmpty;

    public ArmorHud() {
        super("armor", "Armour", "Equipped armour and its durability", 0.5, 0.85);
        this.layout = addEnum("layout", "Layout", "Stack the pieces or lay them out in a row", Layout.HORIZONTAL);
        this.includeHand = addBool("include_hand", "Include held item", "Also show what is in your main hand", true);
        this.showDurability = addBool("durability", "Show durability", "Print remaining uses under each piece", true);
        this.hideEmpty = addBool("hide_empty", "Hide empty slots", "Skip slots with nothing in them", true);
    }

    /** The stacks to draw, in display order, after applying the empty-slot filter. */
    private List<ItemStack> stacks() {
        List<ItemStack> out = new ArrayList<>(5);
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return out;
        }
        for (EquipmentSlot slot : ARMOUR) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() || !hideEmpty.value()) {
                out.add(stack);
            }
        }
        if (includeHand.value()) {
            ItemStack hand = player.getMainHandItem();
            if (!hand.isEmpty() || !hideEmpty.value()) {
                out.add(hand);
            }
        }
        return out;
    }

    private boolean horizontal() {
        return layout.value() == Layout.HORIZONTAL;
    }

    /** Height of one cell, including the durability line when it is switched on. */
    private int cellHeight(Font font) {
        return SLOT + (showDurability.value() ? font.lineHeight + 1 : 0);
    }

    @Override
    public boolean hasContent() {
        return !stacks().isEmpty();
    }

    @Override
    public int contentWidth(Font font) {
        int count = Math.max(1, stacks().size());
        if (horizontal()) {
            return count * SLOT + (count - 1) * GAP;
        }
        return SLOT + (showDurability.value() ? GAP + font.width("100%") : 0);
    }

    @Override
    public int contentHeight(Font font) {
        int count = Math.max(1, stacks().size());
        if (horizontal()) {
            return cellHeight(font);
        }
        return count * SLOT + (count - 1) * GAP;
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        List<ItemStack> stacks = stacks();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            int x = horizontal() ? i * (SLOT + GAP) : 0;
            int y = horizontal() ? 0 : i * (SLOT + GAP);

            if (!stack.isEmpty()) {
                g.item(stack, x, y);
                g.itemDecorations(font, stack, x, y);
            } else {
                Draw.roundRect(g, x, y, SLOT, SLOT, Theme.RADIUS_SM, 0x40FFFFFF);
            }

            if (showDurability.value() && stack.isDamageableItem()) {
                drawDurability(g, font, stack, x, y);
            }
        }
    }

    private void drawDurability(GuiGraphicsExtractor g, Font font, ItemStack stack, int x, int y) {
        int max = stack.getMaxDamage();
        if (max <= 0) {
            return;
        }
        int left = max - stack.getDamageValue();
        float fraction = (float) left / max;
        String text = Math.round(fraction * 100f) + "%";

        int color = fraction > 0.5f ? Theme.SUCCESS : (fraction > 0.2f ? Theme.WARN : Theme.DANGER);
        float tx = horizontal() ? x + (SLOT - font.width(text)) / 2f : x + SLOT + GAP;
        float ty = horizontal() ? y + SLOT + 1f : y + (SLOT - font.lineHeight) / 2f;

        if (useShadow()) {
            Draw.textShadow(g, font, text, tx, ty, color);
        } else {
            Draw.text(g, font, text, tx, ty, color);
        }
    }

    public enum Layout {
        HORIZONTAL,
        VERTICAL
    }
}
