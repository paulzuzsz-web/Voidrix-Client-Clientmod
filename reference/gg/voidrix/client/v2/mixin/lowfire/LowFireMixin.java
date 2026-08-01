package gg.voidrix.client.v2.mixin.lowfire;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.voidrix.client.v2.modules.lowfire.LowFireModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class LowFireMixin {
   @Inject(method = "submitFire", at = @At("HEAD"))
   private static void voidrix$lowFire(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite, CallbackInfo ci) {
      if (LowFireModule.INSTANCE.isEnabled()) {
         poseStack.translate(0.0, LowFireModule.INSTANCE.getLowFireHeight().doubleValue() / 10.0, 0.0);
      }
   }
}
