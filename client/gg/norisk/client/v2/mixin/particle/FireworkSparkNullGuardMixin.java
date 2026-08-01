package gg.norisk.client.v2.mixin.particle;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.particle.FireworkParticles.Starter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Starter.class)
public abstract class FireworkSparkNullGuardMixin {
   @WrapMethod(method = "createParticle")
   private void nrc$guardNullSpark(
      double x,
      double y,
      double z,
      double xa,
      double ya,
      double za,
      IntList rgbColors,
      IntList fadeColors,
      boolean trail,
      boolean twinkle,
      Operation<Void> original
   ) {
      try {
         original.call(new Object[]{x, y, z, xa, ya, za, rgbColors, fadeColors, trail, twinkle});
      } catch (NullPointerException var19) {
      }
   }
}
