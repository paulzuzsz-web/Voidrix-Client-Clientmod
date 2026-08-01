package gg.norisk.client.v2.modules.crosshair

import gg.norisk.owolib.owo.ui.core.OwoUIGraphics

public interface CrosshairStyleRenderer {
   public abstract fun draw(
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
      inverted: Boolean = false
   ) {
   }
}
