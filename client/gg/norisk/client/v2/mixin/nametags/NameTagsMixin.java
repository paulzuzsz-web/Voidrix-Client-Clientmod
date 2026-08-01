package gg.norisk.client.v2.mixin.nametags;

import gg.norisk.client.v2.modules.impl.NameTagsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class NameTagsMixin {
   @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("RETURN"), cancellable = true)
   private void owo$showOwnNametag(LivingEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue() && entity == Minecraft.getInstance().player && NameTagsModule.shouldShowOwnNametag()) {
         cir.setReturnValue(true);
      }
   }
}
