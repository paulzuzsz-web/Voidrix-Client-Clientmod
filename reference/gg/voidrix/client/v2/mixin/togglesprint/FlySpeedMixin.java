package gg.voidrix.client.v2.mixin.togglesprint;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.voidrix.client.v2.modules.togglesprint.ToggleSprintModule;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Abilities.class)
public class FlySpeedMixin {
   @ModifyReturnValue(method = "getFlyingSpeed", at = @At("RETURN"))
   private float voidrix$modifyFlySpeed(float original) {
      return ToggleSprintModule.getFlySpeed(original);
   }
}
