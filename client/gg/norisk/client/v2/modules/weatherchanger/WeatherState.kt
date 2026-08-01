package gg.norisk.client.v2.modules.weatherchanger

import kotlin.enums.EnumEntries

public enum class WeatherState(rainGradient: Float, thunderGradient: Float) {
   CLEAR(0.0F, 0.0F),
   RAIN(1.0F, 0.0F),
   THUNDER(1.0F, 1.0F);

   public final val rainGradient: Float
   public final val thunderGradient: Float

   init {
      this.rainGradient = rainGradient
      this.thunderGradient = thunderGradient
   }

   @JvmStatic
   fun getEntries(): EnumEntries<WeatherState> {
      $ENTRIES
   }
}
