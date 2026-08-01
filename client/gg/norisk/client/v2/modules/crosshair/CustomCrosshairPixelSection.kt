package gg.norisk.client.v2.modules.crosshair

import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.owolib.owo.ui.util.UISounds
import gg.norisk.ui.components.nrc.NrcLabelButton
import gg.norisk.ui.components.nrc.NrcPixelGridDrawComponent
import gg.norisk.ui.components.nrc.NrcPixelGridPreviewComponent
import gg.norisk.ui.modules.v3.V3Button
import gg.norisk.ui.modules.v3.V3ColorSwatch
import gg.norisk.ui.modules.v3.V3Theme
import java.awt.Color
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nCustomCrosshairPixelSection.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CustomCrosshairPixelSection.kt\ngg/norisk/client/v2/modules/crosshair/CustomCrosshairPixelSection\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,178:1\n1#2:179\n*E\n"])
public class CustomCrosshairPixelSection(initialCells: Map<Pair<Int, Int>, Int>,
   initialBrushColorRgb: Int,
   onApply: (Map<Pair<Int, Int>, Int>) -> Unit,
   onBrushColorChange: (Int) -> Unit
) : FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.VERTICAL) {
   public final val onApply: (Map<Pair<Int, Int>, Int>) -> Unit
   public final val onBrushColorChange: (Int) -> Unit
   public final val pixelGrid: NrcPixelGridDrawComponent

   init {
      this.onApply = onApply
      this.onBrushColorChange = onBrushColorChange
      this.pixelGrid = NrcPixelGridDrawComponent(15, 7)
      this.gap(6)
      this.padding(Insets.Companion.of(4))
      this.pixelGrid.setPixelColorRgb(initialBrushColorRgb)
      this.pixelGrid.loadCells(initialCells)
      this.pixelGrid.setOnChanged({ it: java.util.Map ->
         `this$0`.onApply(it)
         Unit.INSTANCE
      })
      this.pixelGrid.setOnInteract({ 
         CustomCrosshair.INSTANCE.activateCustom()
         Unit.INSTANCE
      })
      CustomCrosshair.INSTANCE.activeGrid = this.pixelGrid
      val swatch: V3ColorSwatch = V3ColorSwatch(Color(initialBrushColorRgb), false, 14)
      swatch.onChanged().subscribe({ color: Color ->
         val rgb: Int = color.getRGB() and 16777215
         `this$0`.pixelGrid.setPixelColorRgb(rgb)
         `this$0`.onBrushColorChange(rgb)
      })
      var var6: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var6.gap(4)
      var6.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER)
      var6.child(swatch as UIComponent)
      var6.child(this.toolButton("UNDO", { 
         `this$0`.pixelGrid.undo()
         Unit.INSTANCE
      }) as UIComponent)
      var6.child(this.toolButton("CLEAR", { 
         `this$0`.pixelGrid.clear()
         `this$0`.onApply(MapsKt.emptyMap())
         Unit.INSTANCE
      }) as UIComponent)
      var6.child(this.toolButton("COPY", { 
         val data: java.util.Map = `this$0`.pixelGrid.getFilledCellsWithColors()
         if (data.isEmpty()) {
            CustomCrosshairPixelSectionKt.access$showToast("Nothing to copy", false)
         } else {
            CustomCrosshairPixelSectionKt.access$copyToClipboard(CustomCrosshairPixelSectionKt.encodeToShareCode(data))
            CustomCrosshairPixelSectionKt.showToast$default("Copied!", false, 2, null)
         }

         Unit.INSTANCE
      }) as UIComponent)
      var6.child(this.toolButton("PASTE", { 
         val decoded: java.util.Map = CustomCrosshairPixelSectionKt.decodeFromShareCode(CustomCrosshairPixelSectionKt.access$getClipboard())
         if (decoded != null) {
            CustomCrosshair.INSTANCE.activateCustom()
            `this$0`.pixelGrid.loadCells(decoded)
            `this$0`.onApply(decoded)
            CustomCrosshairPixelSectionKt.showToast$default("Imported ${decoded.size()} px!", false, 2, null)
         } else {
            CustomCrosshairPixelSectionKt.access$showToast("Invalid data", false)
         }

         Unit.INSTANCE
      }) as UIComponent)
      this.child(var6 as UIComponent)
      this.child(this.pixelGrid as UIComponent)
      var6 = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      val var15: FlowLayout = var6
      var6.gap(2)
      var6.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER)
      val var9: LabelComponent = this.v3Label("PRESETS", 0.7F)
      var9.margins(Insets.Companion.right(2))
      var6.child(var9 as UIComponent)

      for (var18 in CustomCrosshairPresets.INSTANCE.list) {
         var15.child(NrcPixelGridPreviewComponent(var18, 0, 1, { 
            CustomCrosshair.INSTANCE.activateCustom()
            val cells: java.util.Map = NrcPixelGridDrawComponent.Companion.parsePresetPattern(`$preset`.getPattern(), `this$0`.pixelGrid.getPixelColorRgb())
            `this$0`.pixelGrid.loadCells(cells)
            `this$0`.onApply(cells)
            Unit.INSTANCE
         }, 2, null) as UIComponent)
      }

      this.child(var6 as UIComponent)
   }

   private fun toolButton(label: String, action: () -> Unit): V3Button {
      val var3: V3Button = V3Button(TextKt.toSmallCaps(label), { var1: NrcLabelButton, var2: Double, var4: Double, var6: Int ->
         UISounds.playButtonSound()
         `$action`()
         Unit.INSTANCE
      })
      var3.padding(Insets.Companion.of(3))
      return var3
   }

   private fun v3Label(text: String, scale: Float = 0.85F): LabelComponent {
      val var3: LabelComponent = LabelComponent(TextKt.toSmallCaps(text), scale)
      var3.setAutoColorSupplier({ 
         V3Theme.INSTANCE.grayColor(11)
      })
      return var3
   }
}
