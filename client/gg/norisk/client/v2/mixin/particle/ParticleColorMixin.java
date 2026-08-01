package gg.norisk.client.v2.mixin.particle;

import gg.norisk.client.v2.api.ParticleColorAccessor;
import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SingleQuadParticle.class)
public abstract class ParticleColorMixin implements ParticleColorAccessor {
   @Shadow
   protected float rCol;
   @Shadow
   protected float gCol;
   @Shadow
   protected float bCol;
   @Shadow
   protected float alpha;

   @Unique
   @Override
   public float nrc$getRed() {
      return this.rCol;
   }

   @Unique
   @Override
   public float nrc$getGreen() {
      return this.gCol;
   }

   @Unique
   @Override
   public float nrc$getBlue() {
      return this.bCol;
   }

   @Unique
   @Override
   public float nrc$getAlpha() {
      return this.alpha;
   }

   @Unique
   @Override
   public void nrc$setRed(float r) {
      this.rCol = r;
   }

   @Unique
   @Override
   public void nrc$setGreen(float g) {
      this.gCol = g;
   }

   @Unique
   @Override
   public void nrc$setBlue(float b) {
      this.bCol = b;
   }

   @Unique
   @Override
   public void nrc$setAlpha(float a) {
      this.alpha = a;
   }
}
