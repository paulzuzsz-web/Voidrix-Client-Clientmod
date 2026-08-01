package gg.norisk.client.v2.mixin.freelook;

import gg.norisk.client.v2.modules.impl.FreeLookModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class FreeLookEntityMixin {
   @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
   private void owo$freelookTurn(double yaw, double pitch, CallbackInfo ci) {
      if (FreeLookModule.handleTurn((float)(yaw * 0.15), (float)(pitch * 0.15))) {
         ci.cancel();
      }
   }
}
