package dev.voidrix.module.hud;

import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Active potion effects with their remaining time, sorted so the ones about to lapse sit on top. */
public final class EffectsHud extends HudModule {
    private final BoolSetting showLevel;
    private final BoolSetting hideAmbient;
    private final BoolSetting colorByEffect;

    public EffectsHud() {
        super("effects", "Effects", "Active potion effects and how long they have left", 1.0, 0.0);
        this.showLevel = addBool("show_level", "Show level", "Append the effect tier as a roman numeral", true);
        this.hideAmbient = addBool("hide_ambient", "Hide beacon effects",
                "Skip ambient effects such as those from a beacon", false);
        this.colorByEffect = addBool("color_by_effect", "Tint by effect",
                "Use each effect's own colour instead of one flat colour", true);
    }

    private List<MobEffectInstance> effects() {
        List<MobEffectInstance> out = new ArrayList<>();
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return out;
        }
        for (MobEffectInstance effect : player.getActiveEffects()) {
            if (hideAmbient.value() && effect.isAmbient()) {
                continue;
            }
            out.add(effect);
        }
        // Infinite effects last, then soonest to expire first.
        out.sort(Comparator
                .comparingInt((MobEffectInstance e) -> e.isInfiniteDuration() ? 1 : 0)
                .thenComparingInt(MobEffectInstance::getDuration));
        return out;
    }

    private String describe(MobEffectInstance effect) {
        StringBuilder sb = new StringBuilder(effect.getEffect().value().getDisplayName().getString());
        if (showLevel.value() && effect.getAmplifier() > 0) {
            sb.append(' ').append(roman(effect.getAmplifier() + 1));
        }
        sb.append("  ").append(duration(effect));
        return sb.toString();
    }

    private static String duration(MobEffectInstance effect) {
        if (effect.isInfiniteDuration()) {
            return "∞";
        }
        int seconds = effect.getDuration() / 20;
        int minutes = seconds / 60;
        return String.format("%d:%02d", minutes, seconds % 60);
    }

    /** Roman numerals for effect tiers; beyond X the plain number is clearer anyway. */
    private static String roman(int value) {
        return switch (value) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> String.valueOf(value);
        };
    }

    private int colorOf(MobEffectInstance effect) {
        if (!colorByEffect.value()) {
            return Theme.TEXT;
        }
        var mobEffect = effect.getEffect().value();
        if (mobEffect.getCategory() == MobEffectCategory.HARMFUL) {
            return Theme.DANGER;
        }
        return 0xFF000000 | mobEffect.getColor();
    }

    @Override
    public boolean hasContent() {
        return !effects().isEmpty();
    }

    @Override
    public int contentWidth(Font font) {
        int width = 0;
        for (MobEffectInstance effect : effects()) {
            width = Math.max(width, font.width(describe(effect)));
        }
        return Math.max(width, font.width("No effects"));
    }

    @Override
    public int contentHeight(Font font) {
        return Math.max(1, effects().size()) * (font.lineHeight + 1);
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        List<MobEffectInstance> effects = effects();
        if (effects.isEmpty()) {
            // Only reachable in the HUD editor, where widgets are drawn regardless of content.
            Draw.text(g, font, "No effects", 0, 0, Theme.TEXT_FAINT);
            return;
        }
        int y = 0;
        for (MobEffectInstance effect : effects) {
            String text = describe(effect);
            if (useShadow()) {
                Draw.textShadow(g, font, text, 0, y, colorOf(effect));
            } else {
                Draw.text(g, font, text, 0, y, colorOf(effect));
            }
            y += font.lineHeight + 1;
        }
    }
}
