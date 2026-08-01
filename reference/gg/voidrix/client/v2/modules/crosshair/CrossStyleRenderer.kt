package gg.voidrix.client.v2.modules.crosshair

import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics

public object CrossStyleRenderer : CrosshairStyleRenderer {
   public override fun draw(
      graphics: OwoUIGraphics,
      x: Double,
      y: Double,
      color: Int,
      thickness: Int,
      size: Int,
      gap: Int,
      dot: Boolean,
      outline: Boolean,
      outlineColor: Int,
      outlineThickness: Double,
      inverted: Boolean
   ) {
      val t: Double = thickness
      val halfT: Double = thickness / 2.0
      val g: Double = gap
      val bt: Double = if (outline) outlineThickness else 0.0
      val centerX: Double = x - halfT
      val centerY: Double = y - halfT
      if (dot) {
         OwoUIGraphics.drawBorderedRect$default(
            graphics, centerX, centerY, centerX + t, centerY + t, color, if (g > 0.0) bt else 0.0, outlineColor, 0, inverted, 128, null
         )
      }

      graphics.drawBorderedRect(centerX, centerY - g - (double)size, centerX + t, centerY - g, color, bt, outlineColor, if (g == 0.0) 1 else -1, inverted)
      graphics.drawBorderedRect(
         centerX, centerY + t + g, centerX + t, centerY + t + g + (double)size, color, bt, outlineColor, if (g == 0.0) 0 else -1, inverted
      )
      graphics.drawBorderedRect(centerX - g - (double)size, centerY, centerX - g, centerY + t, color, bt, outlineColor, if (g == 0.0) 3 else -1, inverted)
      graphics.drawBorderedRect(
         centerX + t + g, centerY, centerX + t + g + (double)size, centerY + t, color, bt, outlineColor, if (g == 0.0) 2 else -1, inverted
      )
   }
}
