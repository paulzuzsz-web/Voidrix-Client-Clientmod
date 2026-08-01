package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.resource.MCKey
import gg.voidrix.owolib.owo.ui.component.ButtonComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.AnchorPoint
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.api.serializable.SoundAttribute
import gg.voidrix.ui.api.value.AnchorPositionValueKt
import gg.voidrix.ui.api.value.NumericValue
import gg.voidrix.ui.api.value.TextValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixBackgroundPicker
import gg.voidrix.ui.components.voidrix.VoidrixMultiColorPicker
import gg.voidrix.ui.v2.hud.AnchorPointPosition
import gg.voidrix.ui.v2.hud.Background
import java.awt.Color
import kotlin.enums.EnumEntries
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.StringCompanionObject
import kotlinx.serialization.builtins.BuiltinSerializersKt
import net.minecraft.resources.Identifier
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nDummyModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DummyModule.kt\ngg/voidrix/client/v2/modules/impl/DummyModule\n+ 2 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n*L\n1#1,178:1\n21#2:179\n*S KotlinDebug\n*F\n+ 1 DummyModule.kt\ngg/voidrix/client/v2/modules/impl/DummyModule\n*L\n129#1:179\n*E\n"])
public object DummyModule : Module("Dummy Module", ModuleCategory.VISUAL, true, false, false) {
   public open val seoTags: Array<String>

   @Category(name = "Booleans")
   @NotNull
   public final val simpleBool: Boolean by ValueApiKt.boolean$default(false, "Simple Boolean", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return simpleBool$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val boolWithCallback: Boolean by ValueApiKt.boolean$default(true, "Bool With Callback", null, { newValue: Boolean ->
      System.out.println((Object)("Boolean changed to: $newValue"))
      Unit.INSTANCE
   }, 4, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return boolWithCallback$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val computedBool: Boolean by ValueApiKt.booleanWithGetter$default(false, "Computed Boolean", null, null, { currentValue: Boolean ->
      currentValue && INSTANCE.simpleBool
   }, 12, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      public final get() {
         return computedBool$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   @Category(name = "Numerics")
   @NotNull
   public final val intSlider: Number by ValueApiKt.numeric$default(50, IntRange(0, 100) as ClosedRange, 1, "Int Slider", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return intSlider$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }


   public final val floatSlider: Number by ValueApiKt.numeric$default(
         0.5F, RangesKt.rangeTo(0.0F, 1.0F) as ClosedRange, 0.01F, "Float Slider", null, null, 48, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return floatSlider$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Number
      }


   public final val doubleSlider: Number by ValueApiKt.numeric$default(
         5.0, RangesKt.rangeTo(0.0, 10.0) as ClosedRange, 0.1, "Double Slider", null, null, 48, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return doubleSlider$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Number
      }


   public final val unrangedNumber: Number by ValueApiKt.numeric$default(42, null, null, "Unranged Number", null, null, 54, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return unrangedNumber$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Number
      }


   @Category(name = "Numeric Range")
   @NotNull
   public final val rangeSlider: ClosedFloatingPointRange<Double> by ValueApiKt.numericRange$default(
         RangesKt.rangeTo(2.0, 8.0), RangesKt.rangeTo(0.0, 10.0), 0.5, "Range Slider", null, null, 48, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return rangeSlider$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as ClosedFloatingPointRange<java.lang.Double>
      }


   @Category(name = "Text")
   @NotNull
   public final val singleLineText: String by ValueApiKt.text$default("Hello World", false, "Single Line Text", null, null, 26, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return singleLineText$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.String
      }


   public final val multiLineText: String by ValueApiKt.text$default("Line 1\nLine 2\nLine 3", true, "Multi Line Text", null, null, 24, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
         public final get() {
         return multiLineText$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.String
      }


   @Category(name = "Enums")
   @NotNull
   public final val testEnum: gg.voidrix.client.v2.modules.impl.DummyModule.TestMode by ValueApiKt.enum$default(
         DummyModule.TestMode.NORMAL, "Test Mode", null, null, null, 28, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
         public final get() {
         return testEnum$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as DummyModule.TestMode
      }


   public final val filteredEnum: gg.voidrix.client.v2.modules.impl.DummyModule.TestMode by ValueApiKt.enum$default(
         DummyModule.TestMode.NORMAL, "Filtered Enum", null, { it: DummyModule.TestMode ->
            it != DummyModule.TestMode.HIDDEN
         }, null, 20, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[11])
         public final get() {
         return filteredEnum$delegate.getValue(this as ValueHolder, $$delegatedProperties[11]) as DummyModule.TestMode
      }


   @Category(name = "Keybinds")
   @NotNull
   public final val toggleKey: MCKey by ValueApiKt.key$default(72, null, null, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[12])
      public final get() {
         return toggleKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[12]) as MCKey
      }


   public final val unknownKey: MCKey by ValueApiKt.key$default(MCKey.Companion.getUNKNOWN(), null, null, 6, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[13])
         public final get() {
         return unknownKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[13]) as MCKey
      }


   @Category(name = "Colors")
   @NotNull
   public final val colorWithAlpha: Color by ValueApiKt.attribute$default(Color(255, 0, 0, 128), true, "Color With Alpha", null, null, 24, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[14])
         public final get() {
         return colorWithAlpha$delegate.getValue(this as ValueHolder, $$delegatedProperties[14]) as Color
      }


   public final val colorNoAlpha: Color
      public final get() {
         return colorNoAlpha$delegate.getValue(this as ValueHolder, $$delegatedProperties[15]) as Color
      }


   @Category(name = "Conditional")
   @NotNull
   public final val enableConditional: Boolean by ValueApiKt.boolean$default(false, "Enable Conditional Settings", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[16])
         public final get() {
         return enableConditional$delegate.getValue(this as ValueHolder, $$delegatedProperties[16]) as java.lang.Boolean
      }


   public final val conditionalSlider: Number
      public final get() {
         return conditionalSlider$delegate.getValue(this as ValueHolder, $$delegatedProperties[17]) as java.lang.Number
      }


   public final val conditionalText: String
      public final get() {
         return conditionalText$delegate.getValue(this as ValueHolder, $$delegatedProperties[18]) as java.lang.String
      }


   @Category(name = "HUD Position")
   @NotNull
   public final val hudPosition: AnchorPointPosition by AnchorPositionValueKt.anchorPosition$default({ 
      AnchorPointPosition(AnchorPoint.TOP_LEFT, 0.1, 0.1, null, 8, null)
   }, "HUD Position", null, null, 12, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[19])
      public final get() {
         return hudPosition$delegate.getValue(this as ValueHolder, $$delegatedProperties[19]) as AnchorPointPosition
      }


   @Category(name = "Actions")
   @NotNull
   public final val resetButton: (ButtonComponent) -> Unit by ValueApiKt.button$default("Reset Action", null, { it: ButtonComponent ->
      System.out.println((Object)"Button pressed!")
      Unit.INSTANCE
   }, 2, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[20])
      public final get() {
         return resetButton$delegate.getValue(this as ValueHolder, $$delegatedProperties[20]) as (ButtonComponent?) -> Unit
      }


   @Category(name = "Attributes")
   @NotNull
   public final val backgroundAttr: Background by ValueApiKt.generic$default({ 
      Background(null, null, 0.0F, 0.0F, 0.0F, null, 63, null)
   }, Background.Companion.serializer(), "Background", null, null, { parent: FlowLayout ->
      parent.child(VoidrixBackgroundPicker(INSTANCE.backgroundAttr, null) as UIComponent)
      true
   }, null, 88, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[21])
      public final get() {
         return backgroundAttr$delegate.getValue(this as ValueHolder, $$delegatedProperties[21]) as Background
      }


   public final val multiColorAttr: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(null, false, false, 7, null)
   }, MultiColor.Companion.serializer(), "Multi Color", null, null, { parent: FlowLayout ->
      parent.child(VoidrixMultiColorPicker(INSTANCE.multiColorAttr, null, null, null, 12, null) as UIComponent)
      true
   }, null, 88, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[22])
      public final get() {
         return multiColorAttr$delegate.getValue(this as ValueHolder, $$delegatedProperties[22]) as MultiColor
      }


   public final val soundAttr: SoundAttribute by ValueApiKt.attribute$default({ 
      val var10002: Identifier = Identifier.fromNamespaceAndPath("minecraft", "ui.button.click")
      SoundAttribute(var10002, 0.0F, 0.0F, 6, null)
   }, "Sound", null, null, null, 28, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[23])
      public final get() {
         return soundAttr$delegate.getValue(this as ValueHolder, $$delegatedProperties[23]) as SoundAttribute
      }


   @Category(name = "Generic")
   @NotNull
   public final val genericString: String by ValueApiKt.generic$default({ 
         "generic value"
      }, BuiltinSerializersKt.serializer(StringCompanionObject.INSTANCE), "Generic String", null, null, null, null, 120, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[24])
         public final get() {
         return genericString$delegate.getValue(this as ValueHolder, $$delegatedProperties[24]) as java.lang.String
      }


   @Category(name = "Collections")
   @NotNull
   public final val stringList: MutableList<String> by ValueApiKt.list$default({ 
         CollectionsKt.mutableListOf(arrayOf("item1", "item2", "item3"))
      }, BuiltinSerializersKt.serializer(StringCompanionObject.INSTANCE), "String List", null, null, 24, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[25])
         public final get() {
         return stringList$delegate.getValue(this as ValueHolder, $$delegatedProperties[25]) as MutableList<java.lang.String>
      }


   public final val stringMap: MutableMap<String, String> by ValueApiKt.map$default(
         { 
            MapsKt.mutableMapOf(arrayOf(TuplesKt.to("key1", "value1"), TuplesKt.to("key2", "value2")))
         },
         BuiltinSerializersKt.serializer(StringCompanionObject.INSTANCE),
         BuiltinSerializersKt.serializer(StringCompanionObject.INSTANCE),
         "String Map",
         null,
         null,
         48,
         null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[26])
         public final get() {
         return stringMap$delegate.getValue(this as ValueHolder, $$delegatedProperties[26]) as MutableMap<java.lang.String, java.lang.String>
      }


   public open fun onEnable() {
      System.out.println((Object)"DummyModule enabled")
   }

   public open fun onDisable() {
      System.out.println((Object)"DummyModule disabled")
   }

   public open fun isBannable(): Boolean {
      return false
   }

   public open fun getOriginalModAuthor(): String {
      return "Voidrix Team"
   }

   public open fun createdAt(): Long {
      return System.currentTimeMillis()
   }

   @JvmStatic
   fun {
      val var10000: Color = Color.WHITE
      colorNoAlpha$delegate = ValueApiKt.attribute$default(var10000, false, "Color No Alpha", null, null, 24, null)
         .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[15])
         val var4: NumericValue = ValueApiKt.numeric$default(5, IntRange(0, 10) as ClosedRange, 1, "Conditional Slider", null, null, 48, null)
      var4.setUiCondition({ 
         INSTANCE.enableConditional
      })
      conditionalSlider$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[17])
      val var5: TextValue = ValueApiKt.text$default("Only visible when enabled", false, "Conditional Text", null, null, 26, null)
      var5.setUiCondition({ 
         INSTANCE.enableConditional
      })
      conditionalText$delegate = var5.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[18])
   }

   public enum class TestMode {
      NORMAL,
      ADVANCED,
      EXPERT,
      HIDDEN;

      @JvmStatic
      fun getEntries(): EnumEntries<DummyModule.TestMode> {
         $ENTRIES
      }
   }
}
