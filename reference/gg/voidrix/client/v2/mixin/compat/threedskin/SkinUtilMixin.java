package gg.voidrix.client.v2.mixin.compat.threedskin;

import dev.tr7zw.skinlayers.SkinUtil;
import gg.voidrix.client.v2.modules.thirdparty.ThreeDSkinModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkinUtil.class)
public abstract class SkinUtilMixin {
   @Inject(
      method = "setup3dLayers(Lcom/mojang/authlib/GameProfile;Ldev/tr7zw/skinlayers/accessor/SkullSettings;)Z",
      at = @At("HEAD"),
      cancellable = true,
      remap = false,
      require = 0
   )
   private static void voidrix$cancelSkull3DSkins(CallbackInfoReturnable<Boolean> cir) {
      if (!ThreeDSkinModule.INSTANCE.isEnabled()) {
         cir.setReturnValue(false);
      }
   }

   @Inject(
      method = "setup3dLayers(Lnet/minecraft/world/entity/Avatar;Ldev/tr7zw/skinlayers/accessor/PlayerSettings;Z)Z",
      at = @At("HEAD"),
      cancellable = true,
      require = 0
   )
   private static void voidrix$cancelPlayer3DSkins(CallbackInfoReturnable<Boolean> cir) {
      if (!ThreeDSkinModule.INSTANCE.isEnabled()) {
         cir.setReturnValue(false);
      }
   }
}
