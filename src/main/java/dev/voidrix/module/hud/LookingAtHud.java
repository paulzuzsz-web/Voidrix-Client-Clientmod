package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** What is under your crosshair - a block, an entity, or nothing. */
public final class LookingAtHud extends SimpleHudModule {
    private final BoolSetting includeEntities;

    public LookingAtHud() {
        super("looking_at", "Looking at", "The block or entity under your crosshair", 0.0, 0.0);
        this.includeEntities = addBool("entities", "Include entities", "Also name entities, not just blocks", true);
    }

    private String target() {
        Minecraft mc = Minecraft.getInstance();
        HitResult hit = mc.hitResult;
        if (hit == null || mc.level == null) {
            return null;
        }
        if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult block) {
            return mc.level.getBlockState(block.getBlockPos()).getBlock().getName().getString();
        }
        if (includeEntities.value() && hit.getType() == HitResult.Type.ENTITY
                && hit instanceof EntityHitResult entity) {
            return entity.getEntity().getDisplayName().getString();
        }
        return null;
    }

    @Override
    public boolean hasContent() {
        return target() != null;
    }

    @Override
    protected String label() {
        return "Looking at";
    }

    @Override
    protected String value() {
        String target = target();
        return target == null ? "--" : target;
    }
}
