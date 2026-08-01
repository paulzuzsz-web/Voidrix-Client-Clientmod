package gg.norisk.client.v2.utils

import java.awt.Color

public object ColorUtils {
   @JvmStatic
   public fun rainbowEffect(): Int {
      return Color.HSBtoRGB((float)(System.currentTimeMillis() % (long)3000) / 3000.0F, 1.0F, 1.0F)
   }
}
