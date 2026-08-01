package gg.voidrix.client.v2.mixin.particle;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.voidrix.client.v2.api.ParticleColorAccessor;
import gg.voidrix.client.v2.modules.particle.ParticleModule;
import gg.voidrix.client.v2.modules.particle.ParticleTypeSettings;
import gg.voidrix.compat.client.MCParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
   @Unique
   private static final ThreadLocal<Boolean> voidrix$extraSpawn = ThreadLocal.withInitial(() -> false);

   @WrapWithCondition(
      method = "createParticle",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V")
   )
   private boolean voidrix$conditionalAdd(ParticleEngine engine, Particle particle, ParticleOptions particleOptions, double x, double y, double z) {
      if (!ParticleModule.INSTANCE.isEnabled()) {
         return true;
      }

      if (voidrix$extraSpawn.get()) {
         return true;
      }

      if (!ParticleModule.INSTANCE.getShowOnSelf()) {
         Player player = Minecraft.getInstance().player;
         if (player != null && player.getBoundingBox().inflate(0.5).contains(x, y, z)) {
            return false;
         }
      }

      String typeId = MCParticles.INSTANCE.particleIdFromOptions(particleOptions);
      if (typeId == null) {
         return true;
      }

      ParticleTypeSettings settings = ParticleModule.getSettings(typeId);
      if (settings == null) {
         return true;
      }

      if (!settings.getEnabled()) {
         return false;
      }

      double mult = settings.getMultiplier();
      return mult <= 0.0 ? false : !(mult < 1.0) || !(Math.random() >= mult);
   }

   @Inject(method = "createParticle", at = @At("RETURN"))
   private void voidrix$applyEffects(
      ParticleOptions particleOptions, double x, double y, double z, double vx, double vy, double vz, CallbackInfoReturnable<Particle> cir
   ) {
      if (ParticleModule.INSTANCE.isEnabled() || ParticleModule.INSTANCE.getPreviewSpawnActive()) {
         if (!voidrix$extraSpawn.get()) {
            Particle particle = (Particle)cir.getReturnValue();
            if (particle != null) {
               String typeId = MCParticles.INSTANCE.particleIdFromOptions(particleOptions);
               if (typeId != null) {
                  ParticleTypeSettings settings = ParticleModule.getSettings(typeId);
                  if (settings != null) {
                     double s = settings.getScale();
                     if (s != 1.0) {
                        particle.scale((float)s);
                     }

                     if (particle instanceof ParticleColorAccessor accessor) {
                        int colorArgb = settings.getColor();
                        int overlayArgb = settings.getOverlayColor();
                        float colorA = (colorArgb >> 24 & 0xFF) / 255.0F;
                        float overlayA = (overlayArgb >> 24 & 0xFF) / 255.0F;
                        if (colorA > 0.0F || overlayA > 0.0F) {
                           float pr = accessor.voidrix$getRed();
                           float pg = accessor.voidrix$getGreen();
                           float pb = accessor.voidrix$getBlue();
                           if (colorA > 0.0F) {
                              float cr = (colorArgb >> 16 & 0xFF) / 255.0F;
                              float cg = (colorArgb >> 8 & 0xFF) / 255.0F;
                              float cb = (colorArgb & 0xFF) / 255.0F;
                              pr = pr * (1.0F - colorA) + cr * colorA;
                              pg = pg * (1.0F - colorA) + cg * colorA;
                              pb = pb * (1.0F - colorA) + cb * colorA;
                           }

                           if (overlayA > 0.0F) {
                              float or = (overlayArgb >> 16 & 0xFF) / 255.0F;
                              float og = (overlayArgb >> 8 & 0xFF) / 255.0F;
                              float ob = (overlayArgb & 0xFF) / 255.0F;
                              pr = pr * (1.0F - overlayA) + or * overlayA;
                              pg = pg * (1.0F - overlayA) + og * overlayA;
                              pb = pb * (1.0F - overlayA) + ob * overlayA;
                           }

                           accessor.voidrix$setRed(pr);
                           accessor.voidrix$setGreen(pg);
                           accessor.voidrix$setBlue(pb);
                        }
                     }

                     double mult = settings.getMultiplier();
                     if (mult > 1.0) {
                        double overOne = mult - 1.0;
                        int extras = (int)(overOne * 2.0);
                        double frac = overOne * 2.0 - extras;
                        if (Math.random() < frac) {
                           extras++;
                        }

                        ParticleEngine engine = (ParticleEngine)this;
                        voidrix$extraSpawn.set(true);

                        try {
                           for (int i = 0; i < extras; i++) {
                              double ox = x + (Math.random() - 0.5) * 0.25;
                              double oy = y + (Math.random() - 0.5) * 0.25;
                              double oz = z + (Math.random() - 0.5) * 0.25;
                              engine.createParticle(particleOptions, ox, oy, oz, vx, vy, vz);
                           }
                        } finally {
                           voidrix$extraSpawn.set(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
