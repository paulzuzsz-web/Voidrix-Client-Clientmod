package gg.norisk.client.v2.mixin.compat.tiers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import com.tiers.TiersClient;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TiersClient.class)
public abstract class TiersClientMixin {
   @WrapOperation(
      method = "onInitializeClient",
      at = @At(
         value = "INVOKE",
         target = "Lnet/fabricmc/fabric/api/client/keybinding/v1/KeyBindingHelper;registerKeyBinding(Lnet/minecraft/client/KeyMapping;)Lnet/minecraft/client/KeyMapping;"
      ),
      remap = false,
      require = 0
   )
   private static KeyMapping nrc$disableTiersKeyBinding(KeyMapping keyBinding, Operation<KeyMapping> original) {
      String id = keyBinding.getName();
      return (KeyMapping)original.call(new Object[]{new KeyMapping(id, InputConstants.UNKNOWN.getValue(), keyBinding.getCategory())});
   }
}
