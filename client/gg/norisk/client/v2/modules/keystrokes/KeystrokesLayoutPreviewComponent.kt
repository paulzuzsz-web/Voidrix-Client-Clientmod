package gg.norisk.client.v2.modules.keystrokes

import gg.norisk.owolib.owo.ui.base.BaseUIComponent
import gg.norisk.owolib.owo.ui.core.CursorStyle
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.input.MouseButtonEvent
import gg.norisk.owolib.owo.ui.util.UISounds
import gg.norisk.ui.modules.v3.V3Border
import gg.norisk.ui.modules.v3.V3Theme

public class KeystrokesLayoutPreviewComponent(widthPx: Int = 120,
      heightPx: Int = 64,
      gridCols: Int = 30,
      gridRows: Int = 16,
      layoutSource: () -> List<PlacedKey>,
      onClick: () -> Unit
   )
   : BaseUIComponent {
   private final val gridCols: Int
   private final val gridRows: Int
   private final val layoutSource: () -> List<PlacedKey>
   private final val onClick: () -> Unit
   private final var isHovered: Boolean

   init {
      this.gridCols = gridCols
      this.gridRows = gridRows
      this.layoutSource = layoutSource
      this.onClick = onClick
      this.horizontalSizing(Sizing.Companion.fixed(widthPx))
      this.verticalSizing(Sizing.Companion.fixed(heightPx))
      this.mouseEnter().subscribe({ 
         `this$0`.isHovered = true
      })
      this.mouseLeave().subscribe({ 
         `this$0`.isHovered = false
      })
      this.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         UISounds.playButtonSound()
         `this$0`.onClick()
         true
      })
      this.cursorStyle(CursorStyle.HAND)
   }

   public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      val x0: Double = this.x()
      val y0: Double = this.y()
      val w: Double = this.width()
      val h: Double = this.height()
      val scale: Double = Math.min(w / (double)this.gridCols, h / (double)this.gridRows)
      val renderW: Double = this.gridCols * scale
      val renderH: Double = this.gridRows * scale
      val ox: Double = x0 + (w - renderW) / 2
      val oy: Double = y0 + (h - renderH) / 2
      context.fill(x0, y0, x0 + w, y0 + h, V3Theme.INSTANCE.grayAlpha(2, 210))
      if (scale >= 1.8) {
         val keys: Int = V3Theme.INSTANCE.grayAlpha(6, 140)
         val keyFill: Int = 2

         // $VF: Unable to resugar Kotlin loop from Java for loop
         var keyBorder: Int = 2
         while (true) {
            if (keyBorder < this.gridRows) break
            // $VF: Unable to resugar Kotlin loop from Java for loop
            var c: Int = keyFill
            while (true) {
               if (c < this.gridCols) break
               context.fill(
                  ox + (double)c * scale - 0.5,
                  oy + (double)keyBorder * scale - 0.5,
                  ox + (double)c * scale - 0.5 + 1.0,
                  oy + (double)keyBorder * scale - 0.5 + 1.0,
                  keys
               )

               c += keyFill
            }

            keyBorder += keyFill
         }
      }

      val var37: java.util.List = this.layoutSource() as java.util.List
      val var38: Int = V3Theme.INSTANCE.grayAlpha(4, 230)
      val var39: Int = V3Theme.INSTANCE.gray(7)

      for (var41 in var37) {
         val kx: Double = ox + var41.cellX * scale
         val ky: Double = oy + var41.cellY * scale
         val kw: Double = var41.widthCells * scale
         val kh: Double = var41.heightCells * scale
         if (!(kw <= 0.0) && !(kh <= 0.0)) {
            context.fill(kx, ky, kx + kw, ky + kh, var38)
            V3Border.INSTANCE.draw(context, kx, ky, kw, kh, var39)
         }
      }

      V3Border.INSTANCE.draw(context, x0, y0, w, h, if (this.isHovered) V3Theme.INSTANCE.accent(8) else V3Theme.INSTANCE.gray(6))
   }
}
