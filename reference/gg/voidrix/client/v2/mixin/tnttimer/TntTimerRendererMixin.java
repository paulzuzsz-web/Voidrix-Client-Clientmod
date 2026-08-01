package gg.voidrix.client.v2.mixin.tnttimer;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.voidrix.client.v2.modules.tnttimer.TntTimer;
import gg.voidrix.compat.render.VoidrixEntityRenderStateExt;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntRenderer.class)
public class TntTimerRendererMixin {
   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/TntRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At("HEAD")
   )
   private void voidrix$renderTntCountdown(
      TntRenderState tntRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci
   ) {
      if (TntTimer.INSTANCE.isEnabled()) {
         if (((VoidrixEntityRenderStateExt)tntRenderState).getVoidrixEntity() instanceof PrimedTnt tntEntity) {
            TntTimer.submitEntityText(tntEntity.getFuse(), poseStack, tntEntity.getBbHeight(), submitNodeCollector);
         }
      }
   }
}
