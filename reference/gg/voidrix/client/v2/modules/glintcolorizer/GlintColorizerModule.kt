package gg.voidrix.client.v2.modules.glintcolorizer

import gg.voidrix.owolib.owo.ui.core.Color
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixMultiColorPicker
import kotlin.jvm.functions.Function0
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import org.jetbrains.annotations.NotNull

public object GlintColorizerModule : Module("Glint Colorizer", ModuleCategory.VISUAL, false, false, false, 28) {
   @Category(name = "Color")
   @NotNull
   public final var multiColor: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(Color.WHITE, false, false, 6, null)
   }, MultiColor.Companion.serializer(), "Glint Color", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      VoidrixMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Color")
   @NotNull
   public final val strength: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.0, 1.0) as ClosedRange, 0.05, "Strength", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return strength$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Number
      }


   @Category(name = "Animation")
   @NotNull
   public final val speed: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.1, 5.0) as ClosedRange, 0.1, "Speed", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return speed$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Number
      }


   @Category(name = "Animation")
   @NotNull
   public final val rotation: Number by ValueApiKt.numeric$default(10.0, RangesKt.rangeTo(-180.0, 180.0) as ClosedRange, 1.0, "Rotation", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return rotation$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }


   @Category(name = "Animation")
   @NotNull
   public final val scale: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.1, 4.0) as ClosedRange, 0.001, "Scale", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return scale$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Number
      }


   public open val seoTags: Array<String>

   public open fun applyBlurInModuleScreen(): Boolean {
      return false
   }

   public fun init() {
      ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
         public open fun getFabricId(): Identifier {
            val var10000: Identifier = Identifier.fromNamespaceAndPath("voidrix", "glint_grayscale_loader")
            return var10000
         }

         public open fun onResourceManagerReload(resourceManager: ResourceManager) {
            VoidrixGlintRenderTypes.registerGrayscaleTextures()
         }
      } as IdentifiableResourceReloadListener)
   }

   @JvmStatic
   public fun isActive(): Boolean {
      return INSTANCE.isEnabled()
   }

   @JvmStatic
   public fun getComputedColor(): java.awt.Color {
      return INSTANCE.multiColor.getChromaOrDefault()
   }

   @JvmStatic
   public fun getRedF(): Float {
      return getComputedColor().getRed() / 255.0F
   }

   @JvmStatic
   public fun getGreenF(): Float {
      return getComputedColor().getGreen() / 255.0F
   }

   @JvmStatic
   public fun getBlueF(): Float {
      return getComputedColor().getBlue() / 255.0F
   }

   @JvmStatic
   public fun getStrengthF(): Float {
      return INSTANCE.strength.floatValue()
   }

   @JvmStatic
   public fun getSpeedMultiplier(): Float {
      return INSTANCE.speed.floatValue()
   }

   @JvmStatic
   public fun getRotationRadians(): Float {
      return (float)(INSTANCE.rotation.doubleValue() * Math.PI / 180.0)
   }

   @JvmStatic
   public fun getScaleValue(): Float {
      return INSTANCE.scale.floatValue()
   }
}
