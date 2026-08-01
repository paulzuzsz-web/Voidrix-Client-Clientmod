package gg.voidrix.client.v2.modules.keystrokes

import gg.voidrix.compat.resource.MCKey
import gg.voidrix.owolib.owo.ui.base.BaseUIComponent
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent.FocusSource
import gg.voidrix.owolib.owo.ui.input.KeyEvent
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.input.MouseDragEvent
import gg.voidrix.ui.modules.v3.V3Border
import gg.voidrix.ui.modules.v3.V3Theme
import java.util.ArrayList
import java.util.NoSuchElementException
import kotlin.enums.EnumEntries
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nKeystrokeGridEditor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokeGridEditor.kt\ngg/voidrix/client/v2/modules/keystrokes/KeystrokeGridEditor\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,658:1\n1#2:659\n1563#3:660\n1634#3,3:661\n*S KotlinDebug\n*F\n+ 1 KeystrokeGridEditor.kt\ngg/voidrix/client/v2/modules/keystrokes/KeystrokeGridEditor\n*L\n318#1:660\n318#1:661,3\n*E\n"])
public class KeystrokeGridEditor(gridCols: Int = 30, gridRows: Int = 16, cellSizePx: Int = 8, dotEveryN: Int = 2) : BaseUIComponent {
   public final val gridCols: Int
   public final val gridRows: Int
   public final val cellSizePx: Int
   public final val dotEveryN: Int

   public final var onLayoutChange: ((List<PlacedKey>) -> Unit)?
      internal set

   public final var onSelectionChange: ((Int) -> Unit)?
      internal set

   public final var onKeyCreated: ((Int) -> Unit)?
      internal set

   public final var onRequestBind: ((Int) -> Unit)?
      internal set

   private final val keys: MutableList<PlacedKey>

   private final var selectedIndex: Int
      private final set(value) {
         if (this.selectedIndex != value) {
            this.selectedIndex = value
            if (this.onSelectionChange != null) {
               this.onSelectionChange(value)
            }
         }
      }


   private final var mode: gg.voidrix.client.v2.modules.keystrokes.KeystrokeGridEditor.Mode
   private final var drawStartCol: Int
   private final var drawStartRow: Int
   private final var drawCurCol: Int
   private final var drawCurRow: Int
   private final var origKey: PlacedKey?
   private final var moveAnchorCol: Int
   private final var moveAnchorRow: Int
   private final var resizeHandle: gg.voidrix.client.v2.modules.keystrokes.KeystrokeGridEditor.Handle
   private final val undoStack: MutableList<List<PlacedKey>>

   init {
      this.gridCols = gridCols
      this.gridRows = gridRows
      this.cellSizePx = cellSizePx
      this.dotEveryN = dotEveryN
      this.keys = ArrayList<>()
      this.selectedIndex = -1
      this.mode = KeystrokeGridEditor.Mode.IDLE
      this.resizeHandle = KeystrokeGridEditor.Handle.BR
      this.undoStack = ArrayList<>()
      this.horizontalSizing(Sizing.Companion.fixed(this.gridCols * this.cellSizePx + 1))
      this.verticalSizing(Sizing.Companion.fixed(this.gridRows * this.cellSizePx + 1))
   }

   public fun loadKeys(data: List<PlacedKey>) {
      this.keys.clear()
      this.keys.addAll(data)
      this.selectedIndex = -1
   }

   public fun replaceAll(newKeys: List<PlacedKey>) {
      this.pushUndo()
      this.keys.clear()
      this.keys.addAll(newKeys)
      this.selectedIndex = -1
      if (this.onLayoutChange != null) {
         this.onLayoutChange(this.getKeys())
      }
   }

   public fun replaceAllCentered(newKeys: List<PlacedKey>) {
      if (newKeys.isEmpty()) {
         this.replaceAll(CollectionsKt.emptyList())
      } else {
         val bboxW: java.util.Iterator = newKeys.iterator()
         if (!bboxW.hasNext()) {
            throw NoSuchElementException()
         } else {
            var var20: Int = (bboxW.next() as PlacedKey).cellX

            while (bboxW.hasNext()) {
               val var24: Int = (bboxW.next() as PlacedKey).cellX
               if (var20 > var24) {
                  var20 = var24
               }
            }

            val var21: java.util.Iterator = newKeys.iterator()
            if (!var21.hasNext()) {
               throw NoSuchElementException()
            } else {
               var var26: Int = (var21.next() as PlacedKey).cellY

               while (var21.hasNext()) {
                  val var31: Int = (var21.next() as PlacedKey).cellY
                  if (var26 > var31) {
                     var26 = var31
                  }
               }

               val var27: java.util.Iterator = newKeys.iterator()
               if (!var27.hasNext()) {
                  throw NoSuchElementException()
               } else {
                  var var33: Int = (var27.next() as PlacedKey).right

                  while (var27.hasNext()) {
                     val var38: Int = (var27.next() as PlacedKey).right
                     if (var33 < var38) {
                        var33 = var38
                     }
                  }

                  val var19: Int = var33 - var20
                  val var34: java.util.Iterator = newKeys.iterator()
                  if (!var34.hasNext()) {
                     throw NoSuchElementException()
                  } else {
                     var var40: Int = (var34.next() as PlacedKey).bottom

                     while (var34.hasNext()) {
                        val var44: Int = (var34.next() as PlacedKey).bottom
                        if (var40 < var44) {
                           var40 = var44
                        }
                     }

                     val var22: Int = var40 - var26
                     val var28: Int = (this.gridCols - var19) / 2 - var20
                     var33 = (this.gridRows - var22) / 2 - var26
                     val var41: java.lang.Iterable = newKeys
                     val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(newKeys, 10))

                     for (`item$iv$iv` in var41) {
                        `destination$iv$iv`.add(
                           PlacedKey.copy$default(
                              `item$iv$iv` as PlacedKey,
                              (`item$iv$iv` as PlacedKey).cellX + var28,
                              (`item$iv$iv` as PlacedKey).cellY + var33,
                              0,
                              0,
                              null,
                              28,
                              null
                           )
                        )
                     }

                     this.replaceAll(`destination$iv$iv` as MutableList<PlacedKey>)
                  }
               }
            }
         }
      }
   }

   public fun getKeys(): List<PlacedKey> {
      return CollectionsKt.toList(this.keys)
   }

   public fun getSelectedIndex(): Int {
      return this.selectedIndex
   }

   public fun getSelectedKey(): PlacedKey? {
      return CollectionsKt.getOrNull(this.keys, this.selectedIndex) as PlacedKey
   }

   public fun setSelectedKey(key: MCKey) {
      val idx: Int = this.selectedIndex
      if (0 <= this.selectedIndex && this.selectedIndex < this.keys.size()) {
         val existing: PlacedKey = this.keys.get(idx)
         if (!(existing.key == key)) {
            this.pushUndo()
            this.keys.set(idx, PlacedKey.copy$default(existing, 0, 0, 0, 0, key, 15, null))
            if (this.onLayoutChange != null) {
               this.onLayoutChange(this.getKeys())
            }
         }
      }
   }

   public fun undo(): Boolean {
      if (this.undoStack.isEmpty()) {
         return false
      } else {
         val prev: java.util.List = this.undoStack.removeLast()
         this.keys.clear()
         val var10000: java.util.List = this.keys
         var10000.addAll(prev)
         this.selectedIndex = -1
         if (this.onLayoutChange != null) {
            this.onLayoutChange(this.getKeys())
         }

         return true
      }
   }

   public fun clear() {
      if (!this.keys.isEmpty()) {
         this.pushUndo()
         this.keys.clear()
         this.selectedIndex = -1
         if (this.onLayoutChange != null) {
            this.onLayoutChange(this.getKeys())
         }
      }
   }

   public fun deleteSelected() {
      if (0 <= this.selectedIndex && this.selectedIndex < this.keys.size()) {
         this.pushUndo()
         this.keys.remove(this.selectedIndex)
         this.selectedIndex = -1
         if (this.onLayoutChange != null) {
            this.onLayoutChange(this.getKeys())
         }
      }
   }

   public fun spawnKey(widthCells: Int = 2, heightCells: Int = 2): Boolean {
      if (widthCells <= this.gridCols && heightCells <= this.gridRows) {
         val brX: Int = this.gridCols - widthCells
         val brY: Int = this.gridRows - heightCells
         if (spawnKey$tryPlace(widthCells, heightCells, this, brX, this.gridRows - heightCells)) {
            return true
         } else {
            for (y in brY downTo 0) {
               for (x in brX downTo 0) {
                  if (spawnKey$tryPlace(widthCells, heightCells, this, x, y)) {
                     return true
                  }
               }
            }

            return false
         }
      } else {
         return false
      }
   }

   private fun pushUndo() {
      this.undoStack.add(CollectionsKt.toList(this.keys))
      if (this.undoStack.size() > 50) {
         this.undoStack.removeFirst()
      }
   }

   public open fun canFocus(source: FocusSource): Boolean {
      return true
   }

   private fun cellFromRel(relX: Double, relY: Double): Pair<Int, Int> {
      return TuplesKt.to(
         RangesKt.coerceIn((int)(relX / (double)this.cellSizePx), 0, this.gridCols - 1),
         RangesKt.coerceIn((int)(relY / (double)this.cellSizePx), 0, this.gridRows - 1)
      )
   }

   private fun keyIndexAt(col: Int, row: Int): Int {
      var var3: Int = this.keys.size() + -1
      if (0 <= var3) {
         do {
            val i: Int = var3--
            if (this.keys.get(i).contains(col, row)) {
               return i
            }
         } while (0 <= var3)
      }

      return -1
   }

   private fun hitHandle(relX: Double, relY: Double, key: PlacedKey): gg.voidrix.client.v2.modules.keystrokes.KeystrokeGridEditor.Handle? {
      val reach: Double = this.cellSizePx / 2.0

      for (var11 in arrayOf(
         TuplesKt.to(KeystrokeGridEditor.Handle.TL, TuplesKt.to((double)key.cellX * (double)this.cellSizePx, (double)key.cellY * (double)this.cellSizePx)),
         TuplesKt.to(KeystrokeGridEditor.Handle.TR, TuplesKt.to((double)key.right * (double)this.cellSizePx, (double)key.cellY * (double)this.cellSizePx)),
         TuplesKt.to(KeystrokeGridEditor.Handle.BL, TuplesKt.to((double)key.cellX * (double)this.cellSizePx, (double)key.bottom * (double)this.cellSizePx)),
         TuplesKt.to(KeystrokeGridEditor.Handle.BR, TuplesKt.to((double)key.right * (double)this.cellSizePx, (double)key.bottom * (double)this.cellSizePx))
      )) {
         val h: KeystrokeGridEditor.Handle = var11.component1() as KeystrokeGridEditor.Handle
         val pos: Pair = var11.component2() as Pair
         if (Math.abs(relX - (pos.getFirst() as java.lang.Number).doubleValue()) <= reach
            && Math.abs(relY - (pos.getSecond() as java.lang.Number).doubleValue()) <= reach) {
            return h
         }
      }

      return null
   }

   public open fun onMouseDown(click: MouseButtonEvent, doubled: Boolean): Boolean {
      if (click.button() != 0) {
         return false
      } else {
         val relX: Double = click.x()
         val relY: Double = click.y()
         val var7: Pair = this.cellFromRel(relX, relY)
         val col: Int = (var7.component1() as java.lang.Number).intValue()
         val row: Int = (var7.component2() as java.lang.Number).intValue()
         if (0 <= this.selectedIndex && this.selectedIndex < this.keys.size()) {
            val var12: KeystrokeGridEditor.Handle = this.hitHandle(relX, relY, this.keys.get(this.selectedIndex))
            if (var12 != null) {
               this.pushUndo()
               this.mode = KeystrokeGridEditor.Mode.RESIZE
               this.resizeHandle = var12
               this.origKey = this.keys.get(this.selectedIndex)
               return true
            }
         }

         val var13: Int = this.keyIndexAt(col, row)
         if (var13 >= 0) {
            this.selectedIndex = var13
            this.pushUndo()
            this.mode = KeystrokeGridEditor.Mode.MOVE
            this.origKey = this.keys.get(var13)
            this.moveAnchorCol = col
            this.moveAnchorRow = row
            return true
         } else {
            this.selectedIndex = -1
            this.mode = KeystrokeGridEditor.Mode.IDLE
            if (this.onSelectionChange != null) {
               this.onSelectionChange(-1)
            }

            return true
         }
      }
   }

   public open fun onMouseDrag(drag: MouseDragEvent): Boolean {
      val var2: Pair = this.cellFromRel(drag.x(), drag.y())
val col: Int = (var2.component1() as java.lang.Number).intValue()
val row: Int = (var2.component2() as java.lang.Number).intValue()
      when (KeystrokeGridEditor.WhenMappings.$EnumSwitchMapping$1[this.mode.ordinal()]) {
         1 -> {
            this.drawCurCol = col
            this.drawCurRow = row
         }
         2 -> {
            if (0 > this.selectedIndex || this.selectedIndex >= this.keys.size()) {
               return true
            }

            if (this.origKey == null) {
               return true
            }

            val var11: PlacedKey = this.origKey
            val moved: PlacedKey = PlacedKey.copy$default(
               this.origKey,
               RangesKt.coerceIn(this.origKey.cellX + (col - this.moveAnchorCol), 0, this.gridCols - var11.widthCells),
               RangesKt.coerceIn(var11.cellY + (row - this.moveAnchorRow), 0, this.gridRows - var11.heightCells),
               0,
               0,
               null,
               28,
               null
            )
            if (!this.collides(moved, this.selectedIndex)) {
               this.keys.set(this.selectedIndex, moved)
            }
            break
         }
         3 -> {
            if (0 > this.selectedIndex || this.selectedIndex >= this.keys.size()) {
               return true
            }

            if (this.origKey == null) {
               return true
            }

            val var9: PlacedKey = this.origKey
var var10000: PlacedKey
            when (KeystrokeGridEditor.WhenMappings.$EnumSwitchMapping$0[this.resizeHandle.ordinal()]) {
               1 -> var10000 = this.rectFromCorners(var9.cellX, var9.cellY, col + 1, row + 1, var9.key)
               2 -> var10000 = this.rectFromCorners(var9.cellX, row, col + 1, var9.bottom, var9.key)
               3 -> var10000 = this.rectFromCorners(col, var9.cellY, var9.right, row + 1, var9.key)
               4 -> var10000 = this.rectFromCorners(col, row, var9.right, var9.bottom, var9.key)
               else -> throw NoWhenBranchMatchedException()
            }

            if (var10000 != null && !this.collides(var10000, this.selectedIndex)) {
               this.keys.set(this.selectedIndex, var10000)
            }
         }
         4 -> {}
         else -> throw NoWhenBranchMatchedException()
      }

      return true
   }

   public open fun onMouseUp(click: MouseButtonEvent): Boolean {
      when (KeystrokeGridEditor.WhenMappings.$EnumSwitchMapping$1[this.mode.ordinal()]) {
         1 -> {
            val var4: PlacedKey = rectFromCorners$default(this, this.drawStartCol, this.drawStartRow, this.drawCurCol + 1, this.drawCurRow + 1, null, 16, null)
            if (var4 != null && !this.collides(var4, -1)) {
               this.pushUndo()
               this.keys.add(var4)
               val var5: Int = CollectionsKt.getLastIndex(this.keys)
               this.selectedIndex = var5
               if (this.onLayoutChange != null) {
                  this.onLayoutChange(this.getKeys())
               }

               if (this.onKeyCreated != null) {
                  this.onKeyCreated(var5)
               }
            }
         }
         2 -> {
            val orig: PlacedKey = this.origKey
            val idx: Int = this.selectedIndex
            if (this.origKey != null && 0 <= this.selectedIndex && this.selectedIndex < this.keys.size() && this.keys.get(idx) == orig) {
               if (this.onRequestBind != null) {
                  this.onRequestBind(idx)
               }
            } else if (this.onLayoutChange != null) {
               this.onLayoutChange(this.getKeys())
            }
         }
         3 -> {
            if (this.onLayoutChange != null) {
               this.onLayoutChange(this.getKeys())
            }
         }
         4 -> {}
         else -> throw NoWhenBranchMatchedException()
      }

      this.mode = KeystrokeGridEditor.Mode.IDLE
      this.origKey = null
      return true
   }

   private fun rectFromCorners(x1: Int, y1: Int, x2: Int, y2: Int, key: MCKey = MCKey.Companion.getUNKNOWN()): PlacedKey? {
      val left: Int = RangesKt.coerceIn(Math.min(x1, x2), 0, this.gridCols)
      val top: Int = RangesKt.coerceIn(Math.min(y1, y2), 0, this.gridRows)
      val right: Int = RangesKt.coerceIn(Math.max(x1, x2), 0, this.gridCols)
      val bottom: Int = RangesKt.coerceIn(Math.max(y1, y2), 0, this.gridRows)
      return if (right - left >= 1 && bottom - top >= 1) PlacedKey(left, top, right - left, bottom - top, key) else null
   }

   private fun collides(candidate: PlacedKey, excludeIndex: Int): Boolean {
      var i: Int = 0

      for (var4 in this.keys.size()..i) {
         if (i != excludeIndex && candidate.overlaps(this.keys.get(i))) {
            return true
         }
      }

      return false
   }

   public open fun onKeyPress(input: KeyEvent): Boolean {
      if (0 > this.selectedIndex || this.selectedIndex >= this.keys.size()) {
         return super.onKeyPress(input)
      } else {
         val var4: Int = input.getKey()
         if (var4 == KeyEvent.Companion.getKEY_DELETE() || var4 == KeyEvent.Companion.getKEY_BACKSPACE()) {
            this.deleteSelected()
            return true
         } else if (var4 == 256) {
            return super.onKeyPress(input)
         } else {
            this.setSelectedKey(MCKey.Companion.ofKeyboard(input.getKey()))
            return true
         }
      }
   }

   public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      val x0: Double = this.x()
      val y0: Double = this.y()
      val totalW: Double = this.gridCols * this.cellSizePx
      val totalH: Double = this.gridRows * this.cellSizePx
      context.fill(x0, y0, x0 + totalW, y0 + (double)(this.gridRows * this.cellSizePx), V3Theme.INSTANCE.grayAlpha(2, 210))
      val dotColor: Int = V3Theme.INSTANCE.grayAlpha(6, 140)
      val preview: IntProgression = RangesKt.step(RangesKt.until(this.dotEveryN, this.gridRows) as IntProgression, this.dotEveryN)
      var bad: Int = preview.getFirst()
      var fill: Int = preview.getLast()
      var border: Int = preview.getStep()
      if (border > 0 && bad <= fill || border < 0 && fill <= bad) {
         while (true) {
            val px: IntProgression = RangesKt.step(RangesKt.until(this.dotEveryN, this.gridCols) as IntProgression, this.dotEveryN)
            var hy: Int = px.getFirst()
            val py: Int = px.getLast()
            val var22: Int = px.getStep()
            if (var22 > 0 && hy <= py || var22 < 0 && py <= hy) {
               while (true) {
                  context.fill(
                     x0 + (double)(hy * this.cellSizePx) - 0.5,
                     y0 + (double)(bad * this.cellSizePx) - 0.5,
                     x0 + (double)(hy * this.cellSizePx) - 0.5 + 1.0,
                     y0 + (double)(bad * this.cellSizePx) - 0.5 + 1.0,
                     dotColor
                  )
                  if (hy == py) {
                     break
                  }

                  hy += var22
               }
            }

            if (bad == fill) {
               break
            }

            bad += border
         }
      }

      if (this.hovered && this.mode === KeystrokeGridEditor.Mode.IDLE) {
         val var31: Pair = this.cellFromRel((double)mouseX - x0, (double)mouseY - y0)
         bad = (var31.component1() as java.lang.Number).intValue()
         fill = (var31.component2() as java.lang.Number).intValue()
         context.fill(
            x0 + (double)(bad * this.cellSizePx),
            y0 + (double)(fill * this.cellSizePx),
            x0 + (double)(bad * this.cellSizePx) + (double)this.cellSizePx,
            y0 + (double)(fill * this.cellSizePx) + (double)this.cellSizePx,
            V3Theme.INSTANCE.grayAlpha(8, 50)
         )
      }

      val var32: java.util.Iterator = this.keys.iterator()
      bad = 0

      while (var32.hasNext()) {
         fill = bad++
         val var41: PlacedKey = var32.next() as PlacedKey
         val var43: Double = x0 + var41.cellX * this.cellSizePx
         val var46: Double = y0 + var41.cellY * this.cellSizePx
         val var48: Double = (double)var41.widthCells * this.cellSizePx
         val var50: Double = (double)var41.heightCells * this.cellSizePx
         val selected: Boolean = fill == this.selectedIndex
         val fillx: Int = if (fill == this.selectedIndex) V3Theme.INSTANCE.accentAlpha(5, 200) else V3Theme.INSTANCE.grayAlpha(4, 230)
         val borderx: Int = if (selected) V3Theme.INSTANCE.accent(9) else V3Theme.INSTANCE.gray(7)
         context.fill(var43, var46, var43 + var48, var46 + var50, fillx)
         V3Border.INSTANCE.draw(context, var43, var46, var48, var50, borderx)
         KeystrokeGridEditorKt.drawFittedKeyLabel$default(
            context,
            var41.key,
            var43,
            var46,
            var48,
            var50,
            if (selected) V3Theme.INSTANCE.accent(12) else V3Theme.INSTANCE.gray(12),
            false,
            0.0,
            0.0F,
            0.0F,
            null,
            3968,
            null
         )
         if (selected) {
            this.drawHandle(context, var43, var46)
            this.drawHandle(context, var43 + var48, var46)
            this.drawHandle(context, var43, var46 + var50)
            this.drawHandle(context, var43 + var48, var46 + var50)
         }
      }

      if (this.mode === KeystrokeGridEditor.Mode.DRAW) {
         val var33: PlacedKey = rectFromCorners$default(this, this.drawStartCol, this.drawStartRow, this.drawCurCol + 1, this.drawCurRow + 1, null, 16, null)
         if (var33 != null) {
            val var36: Boolean = this.collides(var33, -1)
            fill = if (var36) 1090475349 else V3Theme.INSTANCE.accentAlpha(8, 120)
            border = if (var36) -43691 else V3Theme.INSTANCE.accent(10)
            val var44: Double = x0 + var33.cellX * this.cellSizePx
            val var47: Double = y0 + var33.cellY * this.cellSizePx
            val var49: Double = (double)var33.widthCells * this.cellSizePx
            val var51: Double = (double)var33.heightCells * this.cellSizePx
            context.fill(var44, var47, var44 + var49, var47 + var51, fill)
            V3Border.INSTANCE.draw(context, var44, var47, var49, var51, border)
         }
      }

      V3Border.INSTANCE.draw(context, x0, y0, totalW, totalH, V3Theme.INSTANCE.gray(6))
   }

   private fun drawHandle(context: OwoUIGraphics, cx: Double, cy: Double) {
      val h: Double = 3.5 / 2.0
      context.fill(cx - 3.5 / 2.0, cy - 3.5 / 2.0, cx + 3.5 / 2.0, cy + 3.5 / 2.0, V3Theme.INSTANCE.accent(10))
      V3Border.INSTANCE.draw(context, cx - h, cy - h, 3.5, 3.5, V3Theme.INSTANCE.gray(12))
   }

   @JvmStatic
   fun `spawnKey$tryPlace`(`$widthCells`: Int, `$heightCells`: Int, `this$0`: KeystrokeGridEditor, x: Int, y: Int): Boolean {
      val candidate: PlacedKey = PlacedKey(x, y, `$widthCells`, `$heightCells`, MCKey.Companion.getUNKNOWN())
      if (`this$0`.collides(candidate, -1)) {
         false
      } else {
         `this$0`.pushUndo()
         `this$0`.keys.add(candidate)
         `this$0`.selectedIndex = CollectionsKt.getLastIndex(`this$0`.keys)
         if (`this$0`.onLayoutChange != null) {
            `this$0`.onLayoutChange(`this$0`.getKeys())
         }

         if (`this$0`.onKeyCreated != null) {
            `this$0`.onKeyCreated(`this$0`.selectedIndex)
         }

         true
      }
   }

   fun KeystrokeGridEditor() {
      this(0, 0, 0, 0, 15, null)
   }

   private enum class Handle {
      TL,
      TR,
      BL,
      BR;

      @JvmStatic
      fun getEntries(): EnumEntries<KeystrokeGridEditor.Handle> {
         $ENTRIES
      }
   }

   private enum class Mode {
      IDLE,
      DRAW,
      MOVE,
      RESIZE;

      @JvmStatic
      fun getEntries(): EnumEntries<KeystrokeGridEditor.Mode> {
         $ENTRIES
      }
   }
}
