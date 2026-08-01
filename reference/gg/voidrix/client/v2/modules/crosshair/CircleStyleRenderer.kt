package gg.voidrix.client.v2.modules.crosshair

import gg.voidrix.owolib.owo.ui.core.Color
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics

public object CircleStyleRenderer : CrosshairStyleRenderer {
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
      val innerRadius: Double = gap
      val outerRadius: Double = (double)gap + thickness
      val fillColor: Color = Color.Companion.ofArgb(color)
      if (outline) {
         val oColor: Color = Color.Companion.ofArgb(outlineColor)
         graphics.drawRing(x, y, 64, innerRadius, outerRadius + 1.0, oColor, oColor)
      }

      graphics.drawRing(x, y, 64, innerRadius, outerRadius, fillColor, fillColor)
   }
}
