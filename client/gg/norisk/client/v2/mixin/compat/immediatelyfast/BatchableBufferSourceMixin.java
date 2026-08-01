package gg.norisk.client.v2.mixin.compat.immediatelyfast;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.raphimc.immediatelyfast.feature.core.BatchableBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BatchableBufferSource.class)
public abstract class BatchableBufferSourceMixin {
   @Inject(
      method = "getRenderTypeOrder",
      at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", shift = Shift.AFTER),
      cancellable = true,
      require = 0
   )
   private void nrc$fixLayerOrder(RenderType renderType, CallbackInfoReturnable<Integer> cir, @Local Identifier original) {
      if ("noriskclient".equals(original.getNamespace())) {
         cir.setReturnValue(3);
      }
   }
}
