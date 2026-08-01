package gg.norisk.client.v2.mixin.compat.healthindicators;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.adytech99.healthindicators.fabric.HealthIndicatorsFabric;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HealthIndicatorsFabric.class)
public abstract class HealthIndicatorsFabricMixin {
   @WrapOperation(
      method = "<clinit>",
      at = @At(
         value = "INVOKE",
         target = "Lnet/fabricmc/fabric/api/client/keybinding/v1/KeyBindingHelper;registerKeyBinding(Lnet/minecraft/client/KeyMapping;)Lnet/minecraft/client/KeyMapping;"
      ),
      require = 0
   )
   private static KeyMapping nrc$disableHealthIndicatorsKeyBinding(KeyMapping keyBinding, Operation<KeyMapping> original) {
      String id = keyBinding.getName();
      return (KeyMapping)original.call(new Object[]{new KeyMapping(id, InputConstants.UNKNOWN.getValue(), keyBinding.getCategory())});
   }
}
