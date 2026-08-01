package gg.voidrix.client.v2.modules.itemmodel

import gg.voidrix.compat.render.VoidrixVector3f
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.Color
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixSliderWithInput
import gg.voidrix.ui.modules.v3.RightShiftMenuV3Screen
import gg.voidrix.ui.modules.v3.V3Surfaces
import gg.voidrix.ui.modules.v3.V3Theme
import gg.voidrix.ui.modules.v3.V3ValueLine
import kotlin.jvm.internal.Intrinsics
import net.minecraft.network.chat.Component

public class TransformEditorComponent(title: String,
   vector: VoidrixVector3f,
   min: Double,
   max: Double,
   stepSize: Double,
   decimalPlaces: Int,
   defaultValue: Float,
   linked: Boolean = false,
   onChanged: () -> Unit
) : FlowLayout(Sizing.Companion.fill(33), Sizing.Companion.content(), Algorithm.VERTICAL) {
   private final val vector: VoidrixVector3f
   private final val min: Double
   private final val max: Double
   private final val stepSize: Double
   private final val decimalPlaces: Int
   private final val defaultValue: Float
   private final val linked: Boolean
   private final val onChanged: () -> Unit
   private final var expanded: Boolean
   private final val uniformRow: FlowLayout
   private final val uniformSlider: VoidrixSliderWithInput
   private final val axisContainer: FlowLayout
   private final val sliderContainer: FlowLayout
   private final lateinit var sliderX: VoidrixSliderWithInput
   private final lateinit var sliderY: VoidrixSliderWithInput
   private final lateinit var sliderZ: VoidrixSliderWithInput
   private final var updatingLinked: Boolean
   private final var expandIcon: LabelComponent?

   init {
      this.vector = vector
      this.min = min
      this.max = max
      this.stepSize = stepSize
      this.decimalPlaces = decimalPlaces
      this.defaultValue = defaultValue
      this.linked = linked
      this.onChanged = onChanged
      this.gap(1)
      this.alignment(HorizontalAlignment.CENTER, VerticalAlignment.TOP)
      this.allowOverflow(true)
      val titleLabel: LabelComponent = LabelComponent(TextKt.toSmallCapsText(title) as Component)
      titleLabel.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      titleLabel.setAutoColorSupplier({ 
         V3Theme.INSTANCE.accentColor(11)
      })
      this.child(titleLabel as UIComponent)
      this.sliderContainer = FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL)
      this.sliderContainer.gap(1)
      this.uniformRow = FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.HORIZONTAL)
      this.uniformRow.gap(1)
      this.uniformRow.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER)
      this.uniformRow.allowOverflow(true)
      val var19: Double = this.vector.getX()
      val var21: Double = this.defaultValue
      val var23: Double = this.stepSize
      val var25: Int = this.decimalPlaces
      this.uniformSlider = VoidrixSliderWithInput(this.min, this.max, var19, var21, var23, true, 25, 20, var25, 0.55F, 0.5F)
      this.uniformSlider.horizontalSizing(Sizing.Companion.content())
      this.uniformSlider.allowOverflow(true)
      this.uniformSlider.getSlider().horizontalSizing(Sizing.Companion.fixed(30))
      this.uniformSlider.onChanged().subscribe({ value: Double ->
         if (!`this$0`.updatingLinked) {
            val v: Float = (float)value
            `this$0`.vector.setX((float)value)
            `this$0`.vector.setY(v)
            `this$0`.vector.setZ(v)
            `this$0`.onChanged()
         }
      })
      V3ValueLine.Companion.styleV3(this.uniformSlider as UIComponent)
      this.uniformRow.child(this.uniformSlider as UIComponent)
      this.axisContainer = FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL)
      this.axisContainer.gap(1)
      this.sliderX = this.createAxisRow("X", COLOR_X, this.vector.getX(), { it: Float ->
         `this$0`.vector.setX(it)
         `this$0`.onChanged()
         Unit.INSTANCE
      })
      this.sliderY = this.createAxisRow("Y", COLOR_Y, this.vector.getY(), { it: Float ->
         `this$0`.vector.setY(it)
         `this$0`.onChanged()
         Unit.INSTANCE
      })
      this.sliderZ = this.createAxisRow("Z", COLOR_Z, this.vector.getZ(), { it: Float ->
         `this$0`.vector.setZ(it)
         `this$0`.onChanged()
         Unit.INSTANCE
      })
      if (this.linked) {
         val var27: LabelComponent = LabelComponent(TextKt.getLiteral("▶") as Component)
         var27.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
         var27.setAutoColorSupplier({ 
            V3Surfaces.INSTANCE.fontColor(128)
         })
         var27.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
            UISounds.playButtonSound()
            `this$0`.expanded = !`this$0`.expanded
            `this$0`.updateVisibility()
            true
         })
         this.expandIcon = var27
         val var28: FlowLayout = FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.HORIZONTAL)
         var28.gap(1)
         var28.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER)
         var28.allowOverflow(true)
         val var10001: LabelComponent = this.expandIcon
         var28.child(var10001 as UIComponent)
         this.sliderContainer.child(this.uniformRow as UIComponent)
         var28.child(this.sliderContainer as UIComponent)
         this.child(var28 as UIComponent)
      } else {
         this.sliderContainer.child(this.axisContainer as UIComponent)
         this.child(this.sliderContainer as UIComponent)
      }
   }

   private fun updateVisibility() {
      this.sliderContainer.clearChildren()
      if (this.expanded) {
         if (this.expandIcon != null) {
            this.expandIcon.text(TextKt.getLiteral("▼") as Component)
         }

         this.updatingLinked = true
         var var10000: VoidrixSliderWithInput = this.sliderX
         if (this.sliderX == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderX")
            var10000 = null
         }

         var10000.value((double)this.vector.getX())
         var10000 = this.sliderY
         if (this.sliderY == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderY")
            var10000 = null
         }

         var10000.value((double)this.vector.getY())
         var10000 = this.sliderZ
         if (this.sliderZ == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sliderZ")
            var10000 = null
         }

         var10000.value((double)this.vector.getZ())
         this.updatingLinked = false
         this.sliderContainer.child(this.axisContainer as UIComponent)
      } else {
         if (this.expandIcon != null) {
            this.expandIcon.text(TextKt.getLiteral("▶") as Component)
         }

         this.updatingLinked = true
         this.uniformSlider.value((double)this.vector.getX())
         this.updatingLinked = false
         this.sliderContainer.child(this.uniformRow as UIComponent)
      }
   }

   private fun createAxisRow(label: String, color: Color, initialValue: Float, setter: (Float) -> Unit): VoidrixSliderWithInput {
      val row: FlowLayout = FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.HORIZONTAL)
      row.gap(1)
      row.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER)
      row.allowOverflow(true)
      val axisLabel: LabelComponent = LabelComponent(TextKt.getLiteral(label) as Component)
      axisLabel.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      axisLabel.color(color)
      row.child(axisLabel as UIComponent)
      val var12: Double = initialValue
      val var14: Double = this.defaultValue
      val var16: Double = this.stepSize
      val var18: Int = this.decimalPlaces
      val slider: VoidrixSliderWithInput = VoidrixSliderWithInput(this.min, this.max, var12, var14, var16, true, 25, 20, var18, 0.55F, 0.5F)
      slider.horizontalSizing(Sizing.Companion.content())
      slider.allowOverflow(true)
      slider.getSlider().horizontalSizing(Sizing.Companion.fixed(30))
      slider.onChanged().subscribe({ value: Double ->
         `$setter`((float)value)
      })
      V3ValueLine.Companion.styleV3(slider as UIComponent)
      row.child(slider as UIComponent)
      this.axisContainer.child(row as UIComponent)
      return slider
   }

   public fun updateFromVector() {
      var var10000: VoidrixSliderWithInput = this.sliderX
      if (this.sliderX == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sliderX")
         var10000 = null
      }

      var10000.value((double)this.vector.getX())
      var10000 = this.sliderY
      if (this.sliderY == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sliderY")
         var10000 = null
      }

      var10000.value((double)this.vector.getY())
      var10000 = this.sliderZ
      if (this.sliderZ == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sliderZ")
         var10000 = null
      }

      var10000.value((double)this.vector.getZ())
      this.uniformSlider.value((double)this.vector.getX())
   }

   public companion object {
      private final val COLOR_X: Color
      private final val COLOR_Y: Color
      private final val COLOR_Z: Color
   }
}
