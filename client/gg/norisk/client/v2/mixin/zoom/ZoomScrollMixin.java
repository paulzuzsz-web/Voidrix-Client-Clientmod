package gg.norisk.client.v2.mixin.zoom;

import gg.norisk.client.v2.modules.impl.ZoomModule;
import net.minecraft.client.ScrollWheelHandler;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScrollWheelHandler.class)
public abstract class ZoomScrollMixin {
   @Inject(
      method = "onMouseScroll",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/ScrollWheelHandler;accumulatedScrollX:D", ordinal = 6),
      cancellable = true
   )
   private void nrc$blockHotbarScroll(double horizontal, double vertical, CallbackInfoReturnable<Vector2i> cir) {
      ZoomModule.handleHotBarScrolling(cir);
   }
}
