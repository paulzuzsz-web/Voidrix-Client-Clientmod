package gg.voidrix.client.v2.mixin.weatherchanger;

import gg.voidrix.client.v2.modules.weatherchanger.WeatherChanger;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class WeatherChangerLevelMixin {
   @Shadow
   protected float oRainLevel;
   @Shadow
   protected float rainLevel;
   @Shadow
   protected float oThunderLevel;
   @Shadow
   protected float thunderLevel;

   @Shadow
   public abstract DimensionType dimensionType();

   @Inject(method = "getRainLevel", at = @At("RETURN"), cancellable = true)
   private void voidrix$getRainLevel(float delta, CallbackInfoReturnable<Float> cir) {
      if (WeatherChanger.INSTANCE.isEnabled()) {
         cir.setReturnValue(WeatherChanger.INSTANCE.getWeather().getRainGradient());
      }
   }

   @Inject(method = "getThunderLevel", at = @At("RETURN"), cancellable = true)
   private void voidrix$getThunderLevel(float delta, CallbackInfoReturnable<Float> cir) {
      if (WeatherChanger.INSTANCE.isEnabled()) {
         cir.setReturnValue(WeatherChanger.INSTANCE.getWeather().getThunderGradient());
      }
   }

   @Inject(method = "isRaining", at = @At("RETURN"), cancellable = true)
   private void voidrix$isRaining(CallbackInfoReturnable<Boolean> cir) {
      if (WeatherChanger.INSTANCE.isEnabled()) {
         if (!this.voidrix$canHaveWeather()) {
            cir.setReturnValue(false);
            return;
         }

         cir.setReturnValue(this.voidrix$getOriginalRainGradient(1.0F) > 0.2);
      }
   }

   @Inject(method = "isThundering", at = @At("RETURN"), cancellable = true)
   private void voidrix$isThundering(CallbackInfoReturnable<Boolean> cir) {
      if (WeatherChanger.INSTANCE.isEnabled()) {
         if (!this.voidrix$canHaveWeather()) {
            cir.setReturnValue(false);
            return;
         }

         cir.setReturnValue(this.voidrix$getOriginalThunderGradient(1.0F) > 0.9);
      }
   }

   @Unique
   private float voidrix$getOriginalRainGradient(float delta) {
      return this.oRainLevel + delta * (this.rainLevel - this.oRainLevel);
   }

   @Unique
   private float voidrix$getOriginalThunderGradient(float delta) {
      return (this.oThunderLevel + delta * (this.thunderLevel - this.oThunderLevel)) * this.voidrix$getOriginalRainGradient(delta);
   }

   @Unique
   private boolean voidrix$canHaveWeather() {
      return this.dimensionType().hasSkyLight() && !this.dimensionType().hasCeiling();
   }
}
