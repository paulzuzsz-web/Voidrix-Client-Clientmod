package gg.norisk.client.v2.mixin.fov;

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.OptionInstance.CaptionBasedToString;
import net.minecraft.client.OptionInstance.IntRange;
import net.minecraft.client.OptionInstance.TooltipSupplier;
import net.minecraft.client.OptionInstance.ValueSet;
import net.minecraft.client.OptionInstance.ValueUpdateListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionInstance.class)
public abstract class FovRangeMixin<T> {
   @Mutable
   @Shadow
   @Final
   private ValueSet<T> values;

   @Inject(
      method = "<init>(Ljava/lang/String;Lnet/minecraft/client/OptionInstance$TooltipSupplier;Lnet/minecraft/client/OptionInstance$CaptionBasedToString;Lnet/minecraft/client/OptionInstance$ValueSet;Lcom/mojang/serialization/Codec;Ljava/lang/Object;Lnet/minecraft/client/OptionInstance$ValueUpdateListener;)V",
      at = @At("TAIL")
   )
   private void nrc$extendFovRange(
      String captionId,
      TooltipSupplier<T> tooltip,
      CaptionBasedToString<T> toString,
      ValueSet<T> valuesParam,
      Codec<T> codec,
      Object initialValue,
      ValueUpdateListener<? super T> onValueUpdate,
      CallbackInfo ci
   ) {
      if ("options.fov".equals(captionId) && valuesParam instanceof IntRange intRange && intRange.minInclusive() == 30 && intRange.maxInclusive() == 110) {
         this.values = new IntRange(10, 130);
      }
   }
}
