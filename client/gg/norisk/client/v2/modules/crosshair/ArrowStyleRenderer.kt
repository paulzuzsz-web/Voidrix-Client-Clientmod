package gg.norisk.client.v2.modules.crosshair

import gg.norisk.owolib.owo.ui.core.Color
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics

public object ArrowStyleRenderer : CrosshairStyleRenderer {
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
      val width: Double = size
      val height: Double = size
      val thicknessOffset: Double = thickness / 4.0 * 1.4142135 / 4.0
      val fillColor: Color = Color.Companion.ofArgb(color)
      if (outline) {
         val oColor: Color = Color.Companion.ofArgb(outlineColor)
         graphics.drawLine(
            x - width - thicknessOffset,
            y + height + thicknessOffset,
            x + thicknessOffset + thicknessOffset,
            y - thicknessOffset - thicknessOffset,
            (double)thickness / 2.0,
            oColor
         )
         graphics.drawLine(
            x - thicknessOffset - thicknessOffset,
            y - thicknessOffset - thicknessOffset,
            x + width + thicknessOffset,
            y + height + thicknessOffset,
            (double)thickness / 2.0,
            oColor
         )
      }

      graphics.drawLine(x - width, y + height, x + thicknessOffset, y - thicknessOffset, (double)thickness / 4.0, fillColor)
      graphics.drawLine(x - thicknessOffset, y - thicknessOffset, x + width, y + height, (double)thickness / 4.0, fillColor)
   }
}
