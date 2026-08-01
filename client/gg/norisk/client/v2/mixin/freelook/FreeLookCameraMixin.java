package gg.norisk.client.v2.mixin.freelook;

import gg.norisk.client.v2.modules.impl.FreeLookModule;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class FreeLookCameraMixin {
   @Shadow
   private Entity entity;

   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Inject(
      method = "alignWithEntity",
      at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V"),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(Lnet/minecraft/world/phys/Vec3;)V")
      }
   )
   private void owo$freelookCamera(float partialTicks, CallbackInfo ci) {
      float[] result = FreeLookModule.getCameraOverride(this.entity.getViewYRot(partialTicks), this.entity.getViewXRot(partialTicks));
      if (result != null) {
         this.setRotation(result[0], result[1]);
      }
   }
}
