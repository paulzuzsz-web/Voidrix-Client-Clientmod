package gg.voidrix.client.v2.mixin.nofog;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.voidrix.compat.event.FogEvents;
import gg.voidrix.compat.event.FogModifyEvent;
import gg.voidrix.compat.event.VoidrixFogType;
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

   private static boolean voidrix$isMobEffect(FogType fogType, Entity entity) {
      for (FogEnvironment env : FOG_ENVIRONMENTS) {
         if (env instanceof MobEffectFogEnvironment && env.isApplicable(fogType, entity)) {
            return true;
         }
      }

      return false;
   }

   private static VoidrixFogType voidrix$mapFogType(FogType fogType) {
      if (fogType == FogType.LAVA) {
         return VoidrixFogType.LAVA;
      } else if (fogType == FogType.WATER) {
         return VoidrixFogType.WATER;
      } else if (fogType == FogType.POWDER_SNOW) {
         return VoidrixFogType.POWDER_SNOW;
      } else if (fogType == FogType.ATMOSPHERIC) {
         return VoidrixFogType.ATMOSPHERIC;
      } else {
         return fogType == FogType.NONE ? VoidrixFogType.TERRAIN : VoidrixFogType.UNKNOWN;
      }
   }

   @ModifyReturnValue(method = "setupFog", at = @At("RETURN"))
   private FogData voidrix$modifyFogData(
      FogData fog, Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level
   ) {
      FogType fogType = camera.getFluidInCamera();
      Entity entity = camera.entity();
      boolean isMobEffect = voidrix$isMobEffect(fogType, entity);
      VoidrixFogType voidrixFogType = isMobEffect ? VoidrixFogType.BLINDNESS : voidrix$mapFogType(fogType);
      FogModifyEvent event = new FogModifyEvent(voidrixFogType, fog.environmentalStart, fog.environmentalEnd, isMobEffect, 1.0F);
      FogEvents.INSTANCE.getFogModifyEvent().invoke(event);
      float m = event.getMultiplier();
      fog.environmentalStart *= m;
      fog.environmentalEnd *= m;
      fog.renderDistanceStart *= m;
      fog.renderDistanceEnd *= m;
      return fog;
   }
}
