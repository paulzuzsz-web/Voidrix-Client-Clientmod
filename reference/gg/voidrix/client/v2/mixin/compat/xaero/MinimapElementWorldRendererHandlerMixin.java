package gg.voidrix.client.v2.mixin.compat.xaero;

import gg.voidrix.client.v2.waypoints.xaero.InWorldRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.hud.minimap.element.render.world.MinimapElementWorldRendererHandler;

@Mixin(value = MinimapElementWorldRendererHandler.class, remap = false)
public abstract class MinimapElementWorldRendererHandlerMixin {
   @Inject(method = "render", at = @At("HEAD"), remap = false, require = 0)
   private void voidrix$markWorldRenderStart(CallbackInfo ci) {
      InWorldRenderState.push();
   }

   @Inject(method = "render", at = @At("RETURN"), remap = false, require = 0)
   private void voidrix$markWorldRenderEnd(CallbackInfo ci) {
      InWorldRenderState.pop();
   }
}
