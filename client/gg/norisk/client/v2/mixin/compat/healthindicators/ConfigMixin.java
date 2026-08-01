package gg.norisk.client.v2.mixin.compat.healthindicators;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.norisk.client.v2.modules.thirdparty.HealthIndicatorsModule;
import io.github.adytech99.healthindicators.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Config.class)
public class ConfigMixin {
   @ModifyReturnValue(method = "getRenderingEnabled", at = @At("RETURN"), remap = false, require = 0)
   private static boolean nrc$disableHeartRendering(boolean original) {
      return HealthIndicatorsModule.INSTANCE.isDisabledByServer() ? false : original;
   }
}
