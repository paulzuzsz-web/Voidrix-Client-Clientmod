package gg.norisk.client.v2.mixin.zoom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import gg.norisk.client.v2.modules.impl.ZoomModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public abstract class ZoomMouseMixin {
   @ModifyExpressionValue(method = "turnPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z"))
   private boolean nrc$smoothCamera(boolean original) {
      return ZoomModule.handleSmoothMouse(original);
   }

   @ModifyExpressionValue(
      method = "turnPlayer",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object nrc$adjustMouseSensitivity(Object original) {
      return (Double)original * ZoomModule.getMouseSensitivityMultiplier();
   }
}
