package gg.voidrix.client.v2.mixin.colorsaturation;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderPass;
import gg.voidrix.client.v2.modules.impl.colorsaturation.ColorSaturationUbo;
import java.util.Map;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostPass.class)
public class ColorSaturationPostPassMixin {
   @Inject(
      method = "lambda$addToFrame$1",
      remap = false,
      require = 0,
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;draw(IIII)V")
   )
   private void voidrix$colorSaturationUniform(ResourceHandle handle, GpuBufferSlice gpuBufferSlice, Map map, CallbackInfo ci, @Local RenderPass renderPass) {
      renderPass.setUniform("ColorSaturation", ColorSaturationUbo.INSTANCE.getBuffer());
   }
}
