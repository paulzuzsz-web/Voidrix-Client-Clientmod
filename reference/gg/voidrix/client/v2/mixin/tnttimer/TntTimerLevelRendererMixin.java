package gg.voidrix.client.v2.mixin.tnttimer;

import gg.voidrix.client.v2.modules.tnttimer.TntTimer;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class TntTimerLevelRendererMixin {
   @Inject(method = "finalizeGizmoCollection", at = @At("HEAD"))
   private void voidrix$drainTntTimerDraws(CallbackInfo ci) {
      if (TntTimer.INSTANCE.isEnabled()) {
         TntTimer.drainPendingDraws();
      }
   }
}
