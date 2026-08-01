package gg.norisk.client.v2.mixin.arrowtrail;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.arrowtrail.ArrowTrail;
import gg.norisk.compat.client.MCClient;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public class ArrowTrailMixin {
   @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;isCritArrow()Z"))
   private boolean nrc$modifyCritCheck(boolean original) {
      AbstractArrow arrow = (AbstractArrow)this;
      return original || ArrowTrail.INSTANCE.isEnabled() && MCClient.getPlayer() == arrow.getOwner();
   }

   @WrapOperation(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V")
   )
   private void nrc$wrapParticle(Level level, ParticleOptions particle, double x, double y, double z, double dx, double dy, double dz, Operation<Void> original) {
      if (ArrowTrail.INSTANCE.isEnabled()) {
         ParticleOptions custom = (ParticleOptions)ArrowTrail.INSTANCE.getEffect().getEffect().invoke();
         level.addParticle(custom, true, false, x, y, z, dx, dy, dz);
      } else {
         original.call(new Object[]{level, particle, x, y, z, dx, dy, dz});
      }
   }
}
