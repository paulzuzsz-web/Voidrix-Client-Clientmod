package gg.voidrix.client.v2.mixin.fov;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import gg.voidrix.compat.event.FovEvents;
import gg.voidrix.compat.event.FovModifyEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractClientPlayer.class, priority = 1001)
public abstract class AbstractClientPlayerFovMixin {
   @ModifyConstant(method = "getFieldOfViewModifier", constant = @Constant(floatValue = 1.1F), require = 0)
   private float voidrix$flyingFovMultiplier(float constant) {
      FovModifyEvent event = new FovModifyEvent(constant);
      FovEvents.INSTANCE.getFlyingFovMultiplierEvent().invoke(event);
      return (float)event.getFov();
   }

   @ModifyConstant(method = "getFieldOfViewModifier", constant = @Constant(floatValue = 0.15F), require = 0)
   private float voidrix$aimingFovMultiplier(float constant) {
      FovModifyEvent event = new FovModifyEvent(constant);
      FovEvents.INSTANCE.getAimingFovMultiplierEvent().invoke(event);
      return (float)event.getFov();
   }

   @Inject(
      method = "getFieldOfViewModifier",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem()Z"),
      require = 0
   )
   private void voidrix$afterWalkingFovMultiplier(CallbackInfoReturnable<Float> cir, @Local(ordinal = 1) LocalFloatRef fovMultiplier) {
      FovModifyEvent event = new FovModifyEvent(fovMultiplier.get());
      FovEvents.INSTANCE.getAfterWalkingFovMultiplierEvent().invoke(event);
      fovMultiplier.set((float)event.getFov());
   }
}
