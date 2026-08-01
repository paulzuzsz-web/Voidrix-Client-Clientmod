package gg.voidrix.client.v2.mixin.freelook;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.voidrix.client.v2.modules.impl.FreeLookModule;
import net.minecraft.client.CameraType;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Options.class)
public abstract class FreeLookOptionsMixin {
   @ModifyReturnValue(method = "getCameraType", at = @At("RETURN"))
   private CameraType owo$freelookPerspective(CameraType original) {
      return FreeLookModule.shouldOverridePerspective() ? CameraType.THIRD_PERSON_BACK : original;
   }
}
