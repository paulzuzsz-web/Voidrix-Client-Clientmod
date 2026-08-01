package gg.voidrix.client.v2.mixin.fullbright;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.voidrix.client.v2.modules.impl.FullBrightModule;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class LightTextureMixin {
   @WrapOperation(method = "extract", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 0))
   private float owo$fullBright(Double instance, Operation<Float> original) {
      return FullBrightModule.INSTANCE.isEnabled() ? 10.0F : (Float)original.call(new Object[]{instance});
   }
}
