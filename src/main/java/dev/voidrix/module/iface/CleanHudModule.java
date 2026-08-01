package dev.voidrix.module.iface;

import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

/**
 * Hides parts of the vanilla HUD.
 *
 * <p>Rather than cancelling the draw with a mixin, each vanilla element is wrapped once at startup
 * in a delegate that simply declines to forward the call while the matching option is on. That
 * keeps every other mod's HUD additions untouched and makes the whole thing toggleable live.
 */
public final class CleanHudModule extends Module {
    private final BoolSetting crosshair;
    private final BoolSetting hotbar;
    private final BoolSetting experience;
    private final BoolSetting health;
    private final BoolSetting food;
    private final BoolSetting armour;
    private final BoolSetting air;
    private final BoolSetting effects;
    private final BoolSetting scoreboard;
    private final BoolSetting bossBar;
    private final BoolSetting actionBar;

    public CleanHudModule() {
        super("clean_hud", "Clean HUD", "Hide parts of the vanilla interface", Category.INTERFACE);
        this.crosshair = addBool("crosshair", "Hide crosshair", "", false);
        this.hotbar = addBool("hotbar", "Hide hotbar", "", false);
        this.experience = addBool("experience", "Hide experience bar", "", false);
        this.health = addBool("health", "Hide health", "", false);
        this.food = addBool("food", "Hide hunger", "", false);
        this.armour = addBool("armour", "Hide armour bar", "", false);
        this.air = addBool("air", "Hide air bubbles", "", false);
        this.effects = addBool("effects", "Hide effect icons", "", false);
        this.scoreboard = addBool("scoreboard", "Hide scoreboard", "", false);
        this.bossBar = addBool("boss_bar", "Hide boss bar", "", false);
        this.actionBar = addBool("action_bar", "Hide action bar text", "", false);
    }

    @Override
    public void onRegister() {
        hide(VanillaHudElements.CROSSHAIR, crosshair);
        hide(VanillaHudElements.HOTBAR, hotbar);
        hide(VanillaHudElements.EXPERIENCE_LEVEL, experience);
        hide(VanillaHudElements.HEALTH_BAR, health);
        hide(VanillaHudElements.FOOD_BAR, food);
        hide(VanillaHudElements.ARMOR_BAR, armour);
        hide(VanillaHudElements.AIR_BAR, air);
        hide(VanillaHudElements.MOB_EFFECTS, effects);
        hide(VanillaHudElements.SCOREBOARD, scoreboard);
        hide(VanillaHudElements.BOSS_BAR, bossBar);
        hide(VanillaHudElements.OVERLAY_MESSAGE, actionBar);
    }

    private void hide(Identifier element, BoolSetting setting) {
        HudElementRegistry.replaceElement(element, original -> (graphics, delta) -> {
            if (isEnabled() && setting.value()) {
                return;
            }
            original.extractRenderState(graphics, delta);
        });
    }
}
