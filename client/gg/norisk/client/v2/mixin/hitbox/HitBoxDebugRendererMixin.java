package gg.norisk.client.v2.mixin.hitbox;

import gg.norisk.client.v2.modules.hitbox.HitBox;
import gg.norisk.client.v2.modules.hitbox.HitBox_v1_21_11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.util.debug.DebugValueAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public abstract class HitBoxDebugRendererMixin {
   @Inject(method = "emitGizmos", at = @At("TAIL"))
   private void nrc$renderHitboxes(Frustum frustum, double cameraX, double cameraY, double cameraZ, float tickProgress, CallbackInfo ci) {
      if (HitBox.INSTANCE.isRenderActive()) {
         DebugValueAccess store = Minecraft.getInstance().getConnection().createDebugValueAccess();
         HitBox_v1_21_11.INSTANCE.emitGizmos(cameraX, cameraY, cameraZ, store, frustum, tickProgress);
      }
   }
}
