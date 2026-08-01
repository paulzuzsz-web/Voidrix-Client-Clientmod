package gg.norisk.client.v2.mixin.zoom;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.norisk.client.v2.modules.impl.ZoomModule;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class ZoomFovMixin {
   @ModifyReturnValue(method = "calculateFov", at = @At("RETURN"))
   private float nrc$zoomFov(float original) {
      return (float)ZoomModule.modifyFov(original);
   }

   @ModifyReturnValue(method = "calculateHudFov", at = @At("RETURN"))
   private float nrc$zoomHudFov(float original) {
      return (float)ZoomModule.modifyFov(original);
   }
}
