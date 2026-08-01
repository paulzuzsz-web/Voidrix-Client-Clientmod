package gg.voidrix.client.v2.modules.blockoutline

import gg.voidrix.client.v2.utils.ColorUtils
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.awt.Color
import org.jetbrains.annotations.NotNull

public object BlockOutline : Module("Block Outlines", ModuleCategory.VISUAL, false, false, false, 28) {
   @Category(name = "Outline")
   @NotNull
   public final val outlineEnabled: Boolean by ValueApiKt.boolean$default(true, "Enable Outline", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return outlineEnabled$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val color: Color
      public final get() {
         return color$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as Color
      }


   public final val rgb: Boolean by ValueApiKt.boolean$default(false, "Rainbow", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return rgb$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val width: Number by ValueApiKt.numeric$default(2.0F, RangesKt.rangeTo(0.5F, 25.0F) as ClosedRange, 0.5F, "Line Width", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return width$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }


   @Category(name = "Overlay")
   @NotNull
   public final val overlayEnabled: Boolean by ValueApiKt.boolean$default(false, "Enable Overlay", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return overlayEnabled$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   public final val overlayColor: Color
      public final get() {
         return overlayColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as Color
      }


   public final val overlayRgb: Boolean by ValueApiKt.boolean$default(false, "Rainbow", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return overlayRgb$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }


   public final val overlayAlpha: Number by ValueApiKt.numeric$default(0.3, RangesKt.rangeTo(0.05, 1.0) as ClosedRange, 0.05, "Alpha", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return overlayAlpha$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Number
      }


   public open val seoTags: Array<String>

   @JvmStatic
   public fun getComputedColor(): Color {
      return if (INSTANCE.rgb) Color(ColorUtils.rainbowEffect()) else INSTANCE.color
   }

   @JvmStatic
   public fun getComputedColorInt(): Int {
      return getComputedColor().getRGB()
   }

   @JvmStatic
   public fun getLineWidthFloat(): Float {
      return INSTANCE.width.floatValue()
   }

   public open fun createdAt(): Long {
      return 1776412800000L
   }

   @JvmStatic
   fun {
      var var10000: Color = Color.WHITE
      color$delegate = ValueApiKt.attribute$default(var10000, false, null, null, null, 30, null)
         .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         var10000 = Color.WHITE
      overlayColor$delegate = ValueApiKt.attribute$default(var10000, false, null, null, null, 30, null)
         .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
      }
}
