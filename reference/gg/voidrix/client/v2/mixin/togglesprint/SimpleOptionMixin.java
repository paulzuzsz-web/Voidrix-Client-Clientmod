package gg.voidrix.client.v2.mixin.togglesprint;

import gg.voidrix.client.v2.modules.togglesprint.ToggleSprintModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionInstance.class)
public abstract class SimpleOptionMixin<T> {
   @Inject(
      method = "set",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance$ValueUpdateListener;valueChanged(Ljava/lang/Object;)V", shift = Shift.AFTER)
   )
   private void voidrix$afterToggleSprintChanged(T value, CallbackInfo ci) {
      if (this == Minecraft.getInstance().options.toggleSprint()) {
         ToggleSprintModule.onToggleSprintOptionChanged((Boolean)value);
      }
   }
}
