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

/**
 * Armour status: what you are wearing, what you are holding, and how much life each piece has left.
 *
 * <p>The durability bar is drawn here rather than left to the vanilla item decoration, because the
 * vanilla one is three pixels tall, sits under the icon and is the same shade of green until the
 * item is nearly gone. This one is full width, colour-graded the whole way down, and can show the
 * exact figure next to it - which is the difference between noticing a helmet is about to break and
 * finding out when it does.
 */
public final class ArmorHud extends HudModule {
    private static final int SLOT = 18;

    private static final EquipmentSlot[] ARMOUR = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private final EnumSetting<Layout> layout;
    private final EnumSetting<Readout> readout;
    private final BoolSetting includeMainHand;
    private final BoolSetting includeOffHand;
    private final BoolSetting durabilityBar;
    private final BoolSetting hideEmpty;
    private final BoolSetting warnLow;
    private final BoolSetting showCount;

    public ArmorHud() {
        super("armor", "Armour status", "Your armour and held items, with durability", 0.5, 0.88);
        this.layout = addEnum("layout", "Layout", "Lay the pieces out in a row or a column", Layout.HORIZONTAL);
        this.readout = addEnum("readout", "Durability text", "How to print the remaining durability",
                Readout.PERCENT);
        this.durabilityBar = addBool("bar", "Durability bar", "Draw a graded bar under each piece", true);
        this.includeMainHand = addBool("main_hand", "Include main hand", "Also show your held item", true);
        this.includeOffHand = addBool("off_hand", "Include off hand", "Also show your off-hand item", false);
        this.hideEmpty = addBool("hide_empty", "Hide empty slots", "Skip slots with nothing in them", true);
        this.warnLow = addBool("warn_low", "Flash when nearly broken",
                "Pulse a piece once it drops below a tenth of its durability", true);
        this.showCount = addBool("show_count", "Show stack size", "Print the count for stacked items", true);
    }

    /** The stacks to draw, in display order, after applying the empty-slot filter. */
    private List<ItemStack> stacks() {
        List<ItemStack> out = new ArrayList<>(6);
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return out;
        }
        for (EquipmentSlot slot : ARMOUR) {
            add(out, player.getItemBySlot(slot));
        }
        if (includeMainHand.value()) {
            add(out, player.getMainHandItem());
        }
        if (includeOffHand.value()) {
            add(out, player.getItemBySlot(EquipmentSlot.OFFHAND));
        }
        return out;
    }

    private void add(List<ItemStack> out, ItemStack stack) {
        if (!stack.isEmpty() || !hideEmpty.value()) {
            out.add(stack);
        }
    }

    private boolean horizontal() {
        return layout.value() == Layout.HORIZONTAL;
    }

    private boolean anyText() {
        return readout.value() != Readout.NONE;
    }

    /** Width of one cell: the icon, plus room for the figure when it sits beside it. */
    private int cellWidth(Font font) {
        if (horizontal()) {
            return Math.max(SLOT, anyText() ? font.width("100%") : 0);
        }
        return SLOT + (anyText() ? 4 + font.width("1000/1000") : 0);
    }

    private int cellHeight(Font font) {
        int h = SLOT;
        if (durabilityBar.value()) {
            h += 4;
        }
        if (horizontal() && anyText()) {
            h += font.lineHeight;
        }
        return h;
    }

    @Override
    public boolean hasContent() {
        return !stacks().isEmpty();
    }

    @Override
    public int contentWidth(Font font) {
        int count = Math.max(1, stacks().size());
        int cell = cellWidth(font);
        return horizontal() ? count * cell + (count - 1) * 4 : cell;
    }

    @Override
    public int contentHeight(Font font) {
        int count = Math.max(1, stacks().size());
        int cell = cellHeight(font);
        return horizontal() ? cell : count * cell + (count - 1) * 3;
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        List<ItemStack> stacks = stacks();
        int cellW = cellWidth(font);
        int cellH = cellHeight(font);

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            int x = horizontal() ? i * (cellW + 4) : 0;
            int y = horizontal() ? 0 : i * (cellH + 3);
            drawCell(g, font, stack, x, y, cellW);
        }
    }

    private void drawCell(GuiGraphicsExtractor g, Font font, ItemStack stack, int x, int y, int cellW) {
        int iconX = horizontal() ? x + (cellW - SLOT) / 2 : x;

        if (stack.isEmpty()) {
            Draw.roundRectOutline(g, iconX + 1, y + 1, SLOT - 2, SLOT - 2, Theme.RADIUS_SM, 1f,
                    Theme.alpha(Theme.BORDER, 0.8f));
            return;
        }

        boolean damageable = stack.isDamageableItem();
        float fraction = damageable ? remaining(stack) : 1f;
        int gradeColor = grade(fraction);

        // Pulse the frame of a piece that is about to go.
        if (warnLow.value() && damageable && fraction <= 0.1f) {
            float pulse = 0.35f + 0.35f * (float) Math.sin(System.currentTimeMillis() / 180.0);
            Draw.roundRect(g, iconX - 1, y - 1, SLOT + 2, SLOT + 2, Theme.RADIUS_SM,
                    Theme.alpha(Theme.DANGER, pulse));
        }

        g.item(stack, iconX + 1, y + 1);
        if (showCount.value() && stack.getCount() > 1) {
            g.itemDecorations(font, stack, iconX + 1, y + 1);
        }

        if (!damageable) {
            return;
        }

        int textY = y + SLOT;
        if (durabilityBar.value()) {
            float barW = SLOT - 2;
            Draw.roundRect(g, iconX + 1, y + SLOT, barW, 2.5f, 1.25f, 0xC023232F);
            Draw.roundRect(g, iconX + 1, y + SLOT, barW * fraction, 2.5f, 1.25f, gradeColor);
            textY += 4;
        }

        String text = text(stack, fraction);
        if (text.isEmpty()) {
            return;
        }
        if (horizontal()) {
            float tx = x + (cellW - font.width(text)) / 2f;
            drawText(g, font, text, tx, textY, gradeColor);
        } else {
            float tx = iconX + SLOT + 4;
            drawText(g, font, text, tx, y + (SLOT - font.lineHeight) / 2f, gradeColor);
        }
    }

    private void drawText(GuiGraphicsExtractor g, Font font, String text, float x, float y, int color) {
        if (useShadow()) {
            Draw.textShadow(g, font, text, x, y, color);
        } else {
            Draw.text(g, font, text, x, y, color);
        }
    }

    private String text(ItemStack stack, float fraction) {
        int max = stack.getMaxDamage();
        int left = max - stack.getDamageValue();
        return switch (readout.value()) {
            case NONE -> "";
            case PERCENT -> Math.round(fraction * 100f) + "%";
            case REMAINING -> String.valueOf(left);
            case FRACTION -> left + "/" + max;
        };
    }

    private static float remaining(ItemStack stack) {
        int max = stack.getMaxDamage();
        return max <= 0 ? 1f : (float) (max - stack.getDamageValue()) / max;
    }

    /** Green through amber to red, so the colour alone tells you how worried to be. */
    private static int grade(float fraction) {
        if (fraction > 0.5f) {
            return Theme.mix(Theme.WARN, Theme.SUCCESS, (fraction - 0.5f) * 2f);
        }
        return Theme.mix(Theme.DANGER, Theme.WARN, fraction * 2f);
    }

    public enum Layout {
        HORIZONTAL,
        VERTICAL
    }

    public enum Readout {
        PERCENT,
        REMAINING,
        FRACTION,
        NONE
    }
}
