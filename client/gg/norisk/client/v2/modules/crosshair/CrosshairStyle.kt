package gg.norisk.client.v2.modules.crosshair

import kotlin.enums.EnumEntries

public enum class CrosshairStyle(renderer: CrosshairStyleRenderer?) {
   CROSS(CrossStyleRenderer.INSTANCE),
   CIRCLE(CircleStyleRenderer.INSTANCE),
   ARROW(ArrowStyleRenderer.INSTANCE),
   CUSTOM(null);

   public final val renderer: CrosshairStyleRenderer?

   init {
      this.renderer = renderer
   }

   @JvmStatic
   fun getEntries(): EnumEntries<CrosshairStyle> {
      $ENTRIES
   }
}
