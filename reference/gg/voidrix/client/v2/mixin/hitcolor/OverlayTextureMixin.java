package gg.voidrix.client.v2.mixin.hitcolor;

import gg.voidrix.client.v2.modules.impl.HitColorModule;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public abstract class OverlayTextureMixin implements HitColorModule.IOverlayTextureExt {
   @Shadow
   @Final
   private DynamicTexture texture;

   @Inject(method = "<init>", at = @At("TAIL"))
   public void modifyHitColor(CallbackInfo ci) {
      this.voidrix_reload();
   }

   @Override
   public void voidrix_reload() {
      HitColorModule.INSTANCE.reloadOverlay(this.texture);
   }
}
