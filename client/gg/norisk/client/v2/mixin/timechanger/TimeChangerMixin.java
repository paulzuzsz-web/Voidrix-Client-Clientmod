package gg.norisk.client.v2.mixin.timechanger;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.norisk.client.v2.modules.timechanger.TimeChanger;
import net.minecraft.client.ClientClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientClockManager.class)
public abstract class TimeChangerMixin {
   @ModifyReturnValue(method = "getTotalTicks", at = @At("RETURN"))
   private long nrc$getDayTime(long original) {
      return TimeChanger.INSTANCE.isEnabled() ? TimeChanger.INSTANCE.getTime().longValue() : original;
   }
}
