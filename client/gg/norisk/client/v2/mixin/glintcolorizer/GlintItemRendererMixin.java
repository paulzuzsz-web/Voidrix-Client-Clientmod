package gg.norisk.client.v2.mixin.glintcolorizer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.glintcolorizer.GlintColorizerModule;
import gg.norisk.client.v2.modules.glintcolorizer.NrcGlintRenderTypes;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFeatureRenderer.class)
public abstract class GlintItemRendererMixin {
   @WrapOperation(
      method = "getFoilBuffer",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;glintTranslucent()Lnet/minecraft/client/renderer/rendertype/RenderType;"
      )
   )
   private RenderType nrc$glintTranslucent(Operation<RenderType> original) {
      return GlintColorizerModule.isActive() ? NrcGlintRenderTypes.glintTranslucent() : (RenderType)original.call(new Object[0]);
   }

   @WrapOperation(
      method = "getFoilBuffer",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;glint()Lnet/minecraft/client/renderer/rendertype/RenderType;")
   )
   private RenderType nrc$glint(Operation<RenderType> original) {
      return GlintColorizerModule.isActive() ? NrcGlintRenderTypes.glint() : (RenderType)original.call(new Object[0]);
   }
}
