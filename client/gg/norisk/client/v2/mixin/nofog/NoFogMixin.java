package gg.norisk.client.v2.mixin.nofog;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.norisk.compat.event.FogEvents;
import gg.norisk.compat.event.FogModifyEvent;
import gg.norisk.compat.event.NrcFogType;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.renderer.fog.environment.MobEffectFogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FogRenderer.class, priority = 999)
public abstract class NoFogMixin {
   @Shadow
   @Final
   private static List<FogEnvironment> FOG_ENVIRONMENTS;

   private static boolean nrc$isMobEffect(FogType fogType, Entity entity) {
      for (FogEnvironment env : FOG_ENVIRONMENTS) {
         if (env instanceof MobEffectFogEnvironment && env.isApplicable(fogType, entity)) {
            return true;
         }
      }

      return false;
   }

   private static NrcFogType nrc$mapFogType(FogType fogType) {
      if (fogType == FogType.LAVA) {
         return NrcFogType.LAVA;
      } else if (fogType == FogType.WATER) {
         return NrcFogType.WATER;
      } else if (fogType == FogType.POWDER_SNOW) {
         return NrcFogType.POWDER_SNOW;
      } else if (fogType == FogType.ATMOSPHERIC) {
         return NrcFogType.ATMOSPHERIC;
      } else {
         return fogType == FogType.NONE ? NrcFogType.TERRAIN : NrcFogType.UNKNOWN;
      }
   }

   @ModifyReturnValue(method = "setupFog", at = @At("RETURN"))
   private FogData nrc$modifyFogData(
      FogData fog, Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level
   ) {
      FogType fogType = camera.getFluidInCamera();
      Entity entity = camera.entity();
      boolean isMobEffect = nrc$isMobEffect(fogType, entity);
      NrcFogType nrcFogType = isMobEffect ? NrcFogType.BLINDNESS : nrc$mapFogType(fogType);
      FogModifyEvent event = new FogModifyEvent(nrcFogType, fog.environmentalStart, fog.environmentalEnd, isMobEffect, 1.0F);
      FogEvents.INSTANCE.getFogModifyEvent().invoke(event);
      float m = event.getMultiplier();
      fog.environmentalStart *= m;
      fog.environmentalEnd *= m;
      fog.renderDistanceStart *= m;
      fog.renderDistanceEnd *= m;
      return fog;
   }
}
