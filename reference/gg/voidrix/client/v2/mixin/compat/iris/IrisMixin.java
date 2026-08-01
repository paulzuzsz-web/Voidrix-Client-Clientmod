package gg.voidrix.client.v2.mixin.compat.iris;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.irisshaders.iris.Iris;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Iris.class)
public abstract class IrisMixin {
   @ModifyArg(
      method = "onEarlyInitialize",
      at = @At(
         value = "INVOKE",
         target = "Lnet/irisshaders/iris/platform/IrisPlatformHelpers;registerKeyBinding(Lnet/minecraft/client/KeyMapping;)Lnet/minecraft/client/KeyMapping;",
         ordinal = 0
      ),
      index = 0,
      require = 0
   )
   private static KeyMapping voidrix$fixIrisKeybind(KeyMapping keyBinding) {
      return new KeyMapping("iris.keybind.reload", Type.KEYSYM, -1, keyBinding.getCategory());
   }
}
