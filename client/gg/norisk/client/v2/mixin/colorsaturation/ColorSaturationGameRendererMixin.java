package gg.norisk.client.v2.mixin.colorsaturation;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import gg.norisk.client.v2.modules.impl.ColorSaturationModule;
import gg.norisk.client.v2.modules.impl.colorsaturation.ColorSaturationUbo;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class ColorSaturationGameRendererMixin {
   @Shadow
   @Final
   private CrossFrameResourcePool resourcePool;

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = Shift.AFTER))
   private void nrc$renderColorSaturation(CallbackInfo ci) {
      ColorSaturationModule.INSTANCE.renderColorSaturation(this.resourcePool);
   }

   @Inject(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GlobalSettingsUniform;update(IIDJLnet/minecraft/client/DeltaTracker;ILnet/minecraft/world/phys/Vec3;Z)V"
      )
   )
   private void nrc$setUniforms(CallbackInfo ci) {
      ColorSaturationUbo.INSTANCE.set();
   }
}
