package gg.norisk.client.v2.mixin.glintcolorizer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import gg.norisk.client.v2.modules.glintcolorizer.GlintColorizerModule;
import gg.norisk.client.v2.modules.glintcolorizer.NrcGlintRenderTypes;
import net.minecraft.client.renderer.DynamicUniforms;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderType.class)
public abstract class GlintColorMixin {
   @WrapOperation(
      method = "writeDynamicTransforms",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
      )
   )
   private GpuBufferSlice nrc$modifyGlintColor(DynamicUniforms instance, Matrix4f modelView, Matrix4f textureMatrix, Operation<GpuBufferSlice> original) {
      if (NrcGlintRenderTypes.isNrcGlint((RenderType)this)) {
         float strength = GlintColorizerModule.getStrengthF();
         Vector4f customColor = new Vector4f(
            GlintColorizerModule.getRedF() * strength, GlintColorizerModule.getGreenF() * strength, GlintColorizerModule.getBlueF() * strength, 1.0F
         );
         return instance.writeTransform(modelView, customColor, new Vector3f(0.0F, 0.0F, 0.0F), textureMatrix);
      } else {
         return (GpuBufferSlice)original.call(new Object[]{instance, modelView, textureMatrix});
      }
   }
}
