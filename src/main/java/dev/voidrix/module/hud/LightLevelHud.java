package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;

/**
 * Light level where you are standing.
 *
 * <p>Reports the block light specifically, not the combined value, because that is the number that
 * decides whether something can spawn on the floor you are looking at.
 */
public final class LightLevelHud extends SimpleHudModule {
    private final BoolSetting warnOnSpawnable;

    public LightLevelHud() {
        super("light", "Light level", "Block light at your feet", 0.0, 0.25);
        this.warnOnSpawnable = addBool("warn", "Warn when spawnable",
                "Turn red when the light is low enough for mobs", true);
    }

    private static int blockLight() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return -1;
        }
        BlockPos pos = mc.player.blockPosition();
        return mc.level.getBrightness(LightLayer.BLOCK, pos);
    }

    @Override
    public boolean hasContent() {
        return blockLight() >= 0;
    }

    @Override
    protected String label() {
        return "Light";
    }

    @Override
    protected String value() {
        int light = blockLight();
        return light < 0 ? "--" : String.valueOf(light);
    }

    @Override
    protected int valueColor() {
        int light = blockLight();
        if (warnOnSpawnable.value() && light >= 0 && light == 0) {
            return Theme.DANGER;
        }
        return super.valueColor();
    }
}
