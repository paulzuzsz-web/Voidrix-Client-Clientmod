package dev.voidrix.module.combat;

import dev.voidrix.module.Category;
import dev.voidrix.module.HudModule;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import dev.voidrix.util.CombatTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;

/**
 * A card for whoever you are currently fighting: name, health and distance.
 *
 * <p>Every number here is already on your screen somewhere - the name above their head, the hearts
 * in their nameplate. This only gathers it into one place; it reveals nothing the client was not
 * already told.
 */
public final class TargetHud extends HudModule {
    private final IntSetting barWidth;
    private final BoolSetting showDistance;
    private final BoolSetting showHealthNumber;

    /** Smoothly trailing health, so a burst of damage reads as a sweep rather than a jump. */
    private float displayedHealth = -1f;
    private long lastFrameNanos = System.nanoTime();

    public TargetHud() {
        super("target", "Target", "Who you are fighting, with their health",
                Category.COMBAT, 0.5, 0.12);
        this.barWidth = addInt("width", "Card width", "Width of the card in pixels", 110, 70, 220, "px");
        this.showDistance = addBool("distance", "Show distance", "How far away they are", true);
        this.showHealthNumber = addBool("health_number", "Show health value", "Print hearts as a number", true);
    }

    @Override
    public void onFrame() {
        CombatTracker.update();

        LivingEntity target = CombatTracker.target();
        long now = System.nanoTime();
        float dt = Math.clamp((now - lastFrameNanos) / 1_000_000_000f, 0f, 0.2f);
        lastFrameNanos = now;

        if (target == null) {
            displayedHealth = -1f;
            return;
        }
        float actual = target.getHealth();
        if (displayedHealth < 0f) {
            displayedHealth = actual;
        } else {
            displayedHealth += (actual - displayedHealth) * Math.min(1f, dt / 0.12f);
        }
    }

    @Override
    public boolean hasContent() {
        return CombatTracker.target() != null;
    }

    @Override
    public int contentWidth(Font font) {
        return barWidth.value();
    }

    @Override
    public int contentHeight(Font font) {
        return font.lineHeight * 2 + 8;
    }

    @Override
    public void renderContent(GuiGraphicsExtractor g, Font font) {
        LivingEntity target = CombatTracker.target();
        if (target == null) {
            Draw.text(g, font, "No target", 0, 0, Theme.TEXT_FAINT);
            return;
        }

        int w = barWidth.value();
        String name = Draw.ellipsize(font, target.getDisplayName().getString(), w - 34);
        if (useShadow()) {
            Draw.textShadow(g, font, name, 0, 0, Theme.TEXT);
        } else {
            Draw.text(g, font, name, 0, 0, Theme.TEXT);
        }

        if (showDistance.value()) {
            double distance = CombatTracker.distanceTo(target);
            if (distance >= 0) {
                Draw.textRight(g, font, String.format("%.1fm", distance), w, 0, Theme.TEXT_DIM);
            }
        }

        // Health bar.
        float max = Math.max(1f, target.getMaxHealth());
        float health = Math.clamp(displayedHealth < 0f ? target.getHealth() : displayedHealth, 0f, max);
        float fraction = health / max;
        float barY = font.lineHeight + 3;

        Draw.roundRect(g, 0, barY, w, 4, 2f, 0xFF23232F);
        int color = fraction > 0.5f ? Theme.SUCCESS : (fraction > 0.25f ? Theme.WARN : Theme.DANGER);
        Draw.roundRect(g, 0, barY, w * fraction, 4, 2f, color);

        if (showHealthNumber.value()) {
            String text = String.format("%.1f / %.0f", health, max);
            float ty = barY + 6;
            if (useShadow()) {
                Draw.textShadow(g, font, text, 0, ty, Theme.TEXT_DIM);
            } else {
                Draw.text(g, font, text, 0, ty, Theme.TEXT_DIM);
            }
        }
    }
}
