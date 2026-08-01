package gg.norisk.client.v2.modules.keystrokes

import gg.norisk.client.v2.modules.keystrokes.Keystrokes.GridKeyWrapper
import gg.norisk.compat.client.MCLogger
import gg.norisk.owolib.owo.ui.base.BaseUIComponent
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.Size
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent.FocusSource
import gg.norisk.owolib.owo.ui.input.MouseButtonEvent
import gg.norisk.owolib.owo.ui.input.MouseDragEvent
import gg.norisk.ui.modules.v3.V3Border
import gg.norisk.ui.modules.v3.V3Theme
import gg.norisk.ui.v2.hud.AnchorPointPosition
import java.awt.geom.Rectangle2D
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font

@SourceDebugExtension(["SMAP\nKeystrokeLayoutEditorScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokeLayoutEditorScreen.kt\ngg/norisk/client/v2/modules/keystrokes/LayoutPreview\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n*L\n1#1,1011:1\n127#2:1012\n40#2:1013\n127#2:1015\n40#2:1016\n941#3:1014\n941#3:1017\n*S KotlinDebug\n*F\n+ 1 KeystrokeLayoutEditorScreen.kt\ngg/norisk/client/v2/modules/keystrokes/LayoutPreview\n*L\n738#1:1012\n738#1:1013\n825#1:1015\n825#1:1016\n740#1:1014\n826#1:1017\n*E\n"])
private class LayoutPreview(viewportWidth: Int, viewportHeight: Int) : BaseUIComponent {
   private final val viewportWidth: Int
   private final val viewportHeight: Int
   private final val wrapper: GridKeyWrapper
   private final var lastHudRectX: Float
   private final var lastHudRectY: Float
   private final var lastHudRectW: Float
   private final var lastHudRectH: Float
   private final var lastLayoutWGui: Float
   private final var lastLayoutHGui: Float
   private final var lastScreenW: Int
   private final var lastScreenH: Int
   private final var isDragging: Boolean
   private final var isResizing: Boolean
   private final val resizeHandleSizePx: Float

   init {
      this.viewportWidth = viewportWidth
      this.viewportHeight = viewportHeight
      val var3: Keystrokes.GridKeyWrapper = Keystrokes.GridKeyWrapper()
      Keystrokes.INSTANCE.designerPreviewWrapper = var3
      this.wrapper = var3
      this.lastLayoutWGui = 1.0F
      this.lastLayoutHGui = 1.0F
      this.resizeHandleSizePx = 5.0F
      this.horizontalSizing(Sizing.Companion.fixed(this.viewportWidth))
      this.verticalSizing(Sizing.Companion.fixed(this.viewportHeight))
   }

   public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      val x0: Double = this.x()
      val y0: Double = this.y()
      val screenMaxH: Int = this.viewportHeight - 12
      val var46: Int
      val var47: Int
      if (this.viewportWidth / 1.7777777777777777 <= this.viewportHeight - 12) {
         var46 = this.viewportWidth
         var47 = (int)(this.viewportWidth / 1.7777777777777777)
      } else {
         var47 = screenMaxH
         var46 = (int)(screenMaxH * 1.7777777777777777)
      }

      val screenX: Double = x0 + (this.viewportWidth - var46) / 2.0
      this.lastScreenW = var46
      this.lastScreenH = var47
      context.fill(screenX, y0, screenX + (double)var46, y0 + (double)var47, 1426063360)
      V3Border.INSTANCE.draw(context, screenX, y0, (double)var46, (double)var47, V3Theme.INSTANCE.grayAlpha(8, 130))
      val previewScale: Float = (float)(var46 / 640.0)
      this.drawCrosshairReference(context, screenX + (double)var46 / 2.0, y0 + (double)var47 / 2.0)
      this.drawHotbarReference(context, screenX, y0, var46, var47, previewScale)
      if (Keystrokes.INSTANCE.currentLayout.isEmpty()) {
         val var60: Minecraft = Minecraft.getInstance()
         val var61: Font = var60.font
         context.drawString(
            var61,
            "no layout yet",
            screenX + (double)(var46 - var61.width("no layout yet")) / 2.0,
            y0 + (double)(var47 - 8) / 2.0,
            V3Theme.INSTANCE.gray(9),
            false
         )
         this.lastHudRectW = 0.0F
      } else {
         this.wrapper.rebuildLayout()
         this.wrapper.inflate(Size.Companion.of(this.viewportWidth * 4, this.viewportHeight * 4))
         val layoutWGui: Float = RangesKt.coerceAtLeast((float)this.wrapper.width(), 1.0F)
         val layoutHGui: Float = RangesKt.coerceAtLeast((float)this.wrapper.height(), 1.0F)
         val hudScale: Float = Keystrokes.INSTANCE.getHudScale().floatValue()
         val effectiveScale: Float = previewScale * hudScale
         val scaledW: Float = layoutWGui * (previewScale * hudScale)
         val scaledH: Float = layoutHGui * (previewScale * hudScale)
         val aligned: Rectangle2D = Keystrokes.INSTANCE
            .getAnchorPosition()
            .getAlignedPos(java.awt.geom.Rectangle2D.Double(0.0, 0.0, (double)(layoutWGui * hudScale), (double)(layoutHGui * hudScale)), (int)640.0, (int)360.0)
            val targetX: Float = (float)(screenX + aligned.getX() * previewScale)
         val targetY: Float = (float)(y0 + aligned.getY() * previewScale)
         this.lastHudRectX = targetX
         this.lastHudRectY = targetY
         this.lastHudRectW = scaledW
         this.lastHudRectH = scaledH
         this.lastLayoutWGui = layoutWGui
         this.lastLayoutHGui = layoutHGui
         this.wrapper.mount(null, 0.0, 0.0)
         context.push()
         context.translate(targetX, targetY)
         context.scale(effectiveScale, effectiveScale)
         this.wrapper.draw(context, mouseX, mouseY, partialTicks, delta)
         context.pop()
         if (mouseX >= targetX && mouseX <= targetX + scaledW && mouseY >= targetY && mouseY <= targetY + scaledH || this.isDragging || this.isResizing) {
            V3Border.INSTANCE.draw(context, (double)targetX, (double)targetY, (double)scaledW, (double)scaledH, V3Theme.INSTANCE.accent(9))
            val realGuiW: Float = targetX + scaledW - this.resizeHandleSizePx
            val realGuiH: Float = targetY + scaledH - this.resizeHandleSizePx
            context.fill(
               (double)realGuiW,
               (double)(targetY + scaledH - this.resizeHandleSizePx),
               (double)(realGuiW + this.resizeHandleSizePx),
               (double)(targetY + scaledH - this.resizeHandleSizePx + this.resizeHandleSizePx),
               V3Theme.INSTANCE.accent(10)
            )
            V3Border.INSTANCE
               .draw(context, (double)realGuiW, (double)realGuiH, (double)this.resizeHandleSizePx, (double)this.resizeHandleSizePx, V3Theme.INSTANCE.gray(12))
            }

         val readout: java.lang.String = "${(int)(layoutWGui * hudScale)}×${(int)(layoutHGui * hudScale)} px · ${(int)(
            (int)(layoutWGui * hudScale) / 640.0 * 100
         )}% w · ${(int)((int)(layoutHGui * hudScale) / 360.0 * 100)}% h"
         val var10000: Minecraft = Minecraft.getInstance()
         val var59: Font = var10000.font
         context.drawString(
            var59, readout, x0 + (double)(this.viewportWidth - var59.width(readout)) / 2.0, y0 + (double)var47 + 2.0, V3Theme.INSTANCE.gray(10), false
         )
      }
   }

   private fun drawCrosshairReference(context: OwoUIGraphics, cx: Double, cy: Double) {
      val color: Int = V3Theme.INSTANCE.grayAlpha(11, 140)
      context.fill(cx - 3.0 / (double)2, cy - 0.5, cx + 3.0 / (double)2, cy + 0.5, color)
      context.fill(cx - 0.5, cy - 3.0 / (double)2, cx + 0.5, cy + 3.0 / (double)2, color)
   }

   private fun drawHotbarReference(context: OwoUIGraphics, screenX: Double, screenY: Double, screenW: Int, screenH: Int, previewScale: Float) {
      val w: Float = 182.0F * previewScale
      val h: Float = 22.0F * previewScale
      val hx: Double = screenX + (screenW - w) / 2.0
      val hy: Double = screenY + screenH - h - 2.0F * previewScale
      context.fill(
         hx,
         screenY + (double)screenH - (double)h - (double)(2.0F * previewScale),
         hx + (double)w,
         screenY + (double)screenH - (double)h - (double)(2.0F * previewScale) + (double)h,
         -872415232
      )
      V3Border.INSTANCE.draw(context, hx, hy, (double)w, (double)h, -7039852)
      val slotW: Double = w / 9.0
      val divColor: Int = -12895429

      for (selSlot in 1..8) {
         context.fill(hx + slotW * (double)selSlot, hy + (double)1, hx + slotW * (double)selSlot + 0.5, hy + (double)h - (double)1, divColor)
      }

      V3Border.INSTANCE
         .draw(
            context,
            hx + slotW * (double)4 - 1.0 * (double)previewScale,
            hy - 1.0 * (double)previewScale,
            slotW + 1.0 * (double)previewScale * (double)2,
            (double)h + 1.0 * (double)previewScale * (double)2,
            -1
         )
      }

   public open fun canFocus(source: FocusSource): Boolean {
      return source === FocusSource.MOUSE_CLICK
   }

   public open fun onMouseDown(click: MouseButtonEvent, doubled: Boolean): Boolean {
      if (click.button() != 0) {
         return false
      } else {
         val cx: Double = click.x()
         val cy: Double = click.y()
         val localHudX: Double = this.lastHudRectX - this.x()
         val localHudY: Double = this.lastHudRectY - this.y()
         if (cx >= localHudX + this.lastHudRectW - this.resizeHandleSizePx - 2.0F
            && cx <= localHudX + this.lastHudRectW - this.resizeHandleSizePx - 2.0F + (this.resizeHandleSizePx + 2.0F * 2)
            && cy >= localHudY + this.lastHudRectH - this.resizeHandleSizePx - 2.0F
            && cy <= localHudY + this.lastHudRectH - this.resizeHandleSizePx - 2.0F + (this.resizeHandleSizePx + 2.0F * 2)) {
            this.isResizing = true
            return true
         } else if (cx >= localHudX && cx <= localHudX + this.lastHudRectW && cy >= localHudY && cy <= localHudY + this.lastHudRectH) {
            this.isDragging = true
            return true
         } else {
            return false
         }
      }
   }

   public open fun onMouseUp(click: MouseButtonEvent): Boolean {
      this.isDragging = false
      this.isResizing = false
      return super.onMouseUp(click)
   }

   public open fun onMouseDrag(drag: MouseDragEvent): Boolean {
      if (this.lastScreenW <= 0) {
         return false
      } else {
         val previewScale: Double = this.lastScreenW / 640.0
         if (this.isResizing) {
            val var30: Double = drag.x()
            val var31: Double = drag.y()
            val var32: Double = this.lastHudRectX - this.x()
            val var33: Double = this.lastHudRectY - this.y()
            val var34: Float = RangesKt.coerceAtLeast((float)(var30 - var32), 1.0F)
            val dyPreview: Float = RangesKt.coerceAtLeast((float)(var31 - var33), 1.0F)
            val var35: Double = var34 / previewScale
            val var36: Double = dyPreview / previewScale
            val var37: Float = (float)(var35 / this.lastLayoutWGui)
            val var38: Float = (float)(var36 / this.lastLayoutHGui)
            val newScale: Float = RangesKt.coerceIn(Math.min(var37, (float)(var36 / (double)this.lastLayoutHGui)), 0.5F, 3.0F)
            MCLogger.getLogger("KeystrokeResize")
               .info(
                  "cursor=({}, {})  hud=({}, {})  dPrev=({}, {})  dGui=({}, {})  layoutGui=({}, {})  ratio=({}, {})  newScale={}",
                  arrayOf(var30, var31, var32, var33, var34, dyPreview, var35, var36, this.lastLayoutWGui, this.lastLayoutHGui, var37, var38, newScale)
               )
               Keystrokes.INSTANCE.setHudScale$nrc_client(newScale)
            return true
         } else if (this.isDragging) {
            val dxGui: Double = drag.deltaX() / previewScale
            val dyGui: Double = drag.deltaY() / previewScale
            val pos: AnchorPointPosition = Keystrokes.INSTANCE.getAnchorPosition()
            val rect: java.awt.geom.Rectangle2D.Double = pos.getAnchor().getRectangle(640, 360)
            val regionW: Double = RangesKt.coerceAtLeast(rect.width, 1.0)
            val regionH: Double = RangesKt.coerceAtLeast(rect.height, 1.0)
            var newRelX: Double = pos.getRelativX() + dxGui / regionW
            var newRelY: Double = pos.getRelativY() + dyGui / regionH
            val hudScale: Float = Keystrokes.INSTANCE.getHudScale().floatValue()
            val hudW: Double = this.lastLayoutWGui * hudScale
            val hudH: Double = this.lastLayoutHGui * hudScale
            if (hudW < 640.0 && this.lastLayoutHGui * hudScale < 360.0) {
               val aligned: Rectangle2D = AnchorPointPosition.copy$default(pos, null, newRelX, newRelY, null, 9, null)
                  .getAlignedPos(java.awt.geom.Rectangle2D.Double(0.0, 0.0, hudW, hudH), 640, 360)
                  val clampedX: Double = RangesKt.coerceIn(aligned.getX(), 0.0, 640.0 - hudW)
               val clampedY: Double = RangesKt.coerceIn(aligned.getY(), 0.0, 360.0 - hudH)
               newRelX += (clampedX - aligned.getX()) / regionW
               newRelY += (clampedY - aligned.getY()) / regionH
            }

            Keystrokes.INSTANCE.setAnchorPosition(AnchorPointPosition.copy$default(pos, null, newRelX, newRelY, null, 9, null))
            return true
         } else {
            return false
         }
      }
   }
}
