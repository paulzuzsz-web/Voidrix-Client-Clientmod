package gg.norisk.client.v2.modules.coordinates

import gg.norisk.compat.client.Direction8
import gg.norisk.compat.client.MCClient
import gg.norisk.compat.client.MCClientKt
import gg.norisk.compat.event.KeyEventData
import gg.norisk.compat.event.KeyEvents
import gg.norisk.compat.event.MouseClickEventData
import gg.norisk.compat.event.MouseEvents
import gg.norisk.compat.input.ClipboardHelper
import gg.norisk.compat.resource.MCKey
import gg.norisk.compat.text.LiteralTextBuilder
import gg.norisk.compat.text.RainbowTextUtilsKt
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.component.LabelComponent.Companion
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.AbstractHud
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.IContentBackground
import gg.norisk.ui.api.hud.IDynamicBackground
import gg.norisk.ui.api.hud.IDynamicBackgroundKt
import gg.norisk.ui.api.serializable.MultiColor
import gg.norisk.ui.api.value.BooleanValue
import gg.norisk.ui.api.value.TextValue
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.components.nrc.NrcMultiColorPicker
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.v2.hud.AnchorPointPosition
import gg.norisk.ui.v2.toast.components.NrcToastComponent.Builder
import java.awt.geom.Point2D
import java.util.function.Supplier
import kotlin.enums.EnumEntries
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.DurationKt
import kotlin.time.DurationUnit
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nCoordinates.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Coordinates.kt\ngg/norisk/client/v2/modules/coordinates/Coordinates\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt\n+ 4 TextBuilder.kt\ngg/norisk/compat/text/LiteralTextBuilder\n*L\n1#1,269:1\n328#2:270\n40#2:271\n185#2:272\n40#2:273\n8#3,4:274\n78#4,6:278\n72#4,4:284\n87#4:288\n*S KotlinDebug\n*F\n+ 1 Coordinates.kt\ngg/norisk/client/v2/modules/coordinates/Coordinates\n*L\n96#1:270\n96#1:271\n97#1:272\n97#1:273\n110#1:274,4\n111#1:278,6\n111#1:284,4\n111#1:288\n*E\n"])
public object Coordinates : AbstractHud("Coordinates", false, false, false, 14), IContentBackground {
   public open val seoTags: Array<String>

   @Category(name = "Display")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public final var multiColor: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(null, false, false, 7, null)
   }, MultiColor.Companion.serializer(), "Color", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      NrcMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Layout")
   @NotNull
   public final val showX: Boolean by ValueApiKt.boolean$default(true, "X", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return showX$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val showY: Boolean by ValueApiKt.boolean$default(true, "Y", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return showY$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   public final val showZ: Boolean by ValueApiKt.boolean$default(true, "Z", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return showZ$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   public final val showBiome: Boolean by ValueApiKt.boolean$default(true, "Biome", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return showBiome$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }


   public final val showDirection: Boolean by ValueApiKt.boolean$default(true, "Direction", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return showDirection$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }


   public final val alignment: gg.norisk.client.v2.modules.coordinates.Coordinates.Alignment by ValueApiKt.enum$default(
         Coordinates.Alignment.VERTICAL, null, null, null, null, 30, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return alignment$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as Coordinates.Alignment
      }


   @Category(name = "Util")
   @NotNull
   public final val copyCoordsToClipboard: MCKey by ValueApiKt.key$default(MCKey.Companion.getUNKNOWN(), null, null, 6, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return copyCoordsToClipboard$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as MCKey
      }


   public final val sendCoordsAfterCopying: Boolean
      public final get() {
         return sendCoordsAfterCopying$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.Boolean
      }


   public final val coordsFormat: String
      public final get() {
         return coordsFormat$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as java.lang.String
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 4, 4, 0, 0, 24, null)
   }

   private fun copyCoordsToClipboard() {
      var var10000: Minecraft = Minecraft.getInstance()
      if (var10000.gui.screen() == null) {
         var10000 = Minecraft.getInstance()
         if (var10000.player != null) {
            val var17: LocalPlayer = var10000.player
            val text: java.lang.String = StringsKt.replace$default(
               StringsKt.replace$default(
                  StringsKt.replace$default(this.coordsFormat, "{x}", java.lang.String.valueOf(var10000.player.getBlockX()), false, 4, null),
                  "{y}",
                  java.lang.String.valueOf(var17.getBlockY()),
                  false,
                  4,
                  null
               ),
               "{z}",
               java.lang.String.valueOf(var17.getBlockZ()),
               false,
               4,
               null
            )
            val var23: Builder = Builder.soundEvent$default(Builder.info$default(Builder(null, null, 3, null), false, 1, null), null, 1, null)
            val var8: LiteralTextBuilder = LiteralTextBuilder(null, true)
            var8.getAppendTasks().add(Coordinates$copyCoordsToClipboard$lambda$10$$inlined$text$default$1(var8, "Coordinates", true))
            var8.setBold(true)
            var23.title(var8.build() as Component)
               .description(TextKt.getLiteral(text) as Component)
               .duration_LRDsOJo/* $VF was: duration-LRDsOJo */(DurationKt.toDuration(2, DurationUnit.SECONDS))
               .build()
               .show()
               ClipboardHelper.INSTANCE.setString(text)
            if (this.sendCoordsAfterCopying) {
               MCClient.sendChatToServer(text)
            }
         }
      }
   }

   public open fun hudComponent(): UIComponent {
      return Coordinates.CoordinatesComponent(null, null, 3, null) as UIComponent
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @JvmStatic
   fun {
      val var4: BooleanValue = ValueApiKt.boolean$default(false, null, null, null, 14, null)
      var4.setUiCondition({ 
         !INSTANCE.copyCoordsToClipboard.isUnknown()
      })
      sendCoordsAfterCopying$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
      val var5: TextValue = ValueApiKt.text$default("X: {x} Y: {y} Z: {z}", false, null, null, null, 30, null)
      var5.setUiCondition({ 
         !INSTANCE.copyCoordsToClipboard.isUnknown()
      })
      coordsFormat$delegate = var5.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_8@{ it: MouseClickEventData ->
         if (INSTANCE.copyCoordsToClipboard.isUnknown()) {
            return@lambda_8 Unit.INSTANCE
         } else {
            if (MCKey.Companion.ofMouse(it.getButton()) == INSTANCE.copyCoordsToClipboard && it.getAction() == 1) {
               INSTANCE.copyCoordsToClipboard()
            }

            return@lambda_8 Unit.INSTANCE
         }
      })
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_9@{ it: KeyEventData ->
         if (INSTANCE.copyCoordsToClipboard.isUnknown()) {
            return@lambda_9 Unit.INSTANCE
         } else {
            if (MCKey.Companion.ofKeyboard(it.getKey()) == INSTANCE.copyCoordsToClipboard && it.isClicked()) {
               INSTANCE.copyCoordsToClipboard()
            }

            return@lambda_9 Unit.INSTANCE
         }
      })
   }

   public enum class Alignment {
      HORIZONTAL,
      VERTICAL;

      @JvmStatic
      fun getEntries(): EnumEntries<Coordinates.Alignment> {
         $ENTRIES
      }
   }

   private class AutoUpdateLabelComponent(supplier: Supplier<String>) : LabelComponent {
      public final val supplier: Supplier<String>

      init {
         val `$this$_init__u24lambda_u240`: Companion = LabelComponent.Companion
         val pos: Point2D = AnchorPointPosition.toGlobalPos$default(Coordinates.INSTANCE.getAnchorPosition(), 0, 0, 3, null)
         val var10000: Any = supplier.get()
         super(
            RainbowTextUtilsKt.rainbowText(
               var10000 as java.lang.String,
               Coordinates.INSTANCE.multiColor.isRainbow(),
               Coordinates.INSTANCE.multiColor.isPositionalRainbow(),
               Coordinates.INSTANCE.multiColor.getChromaOrDefault().getRGB(),
               (int)pos.getX(),
               (int)pos.getY()
            ) as Component
         )
         this.supplier = supplier
         this.shadow(true)
         this.setAutoTextSupplier(
            { 
               val pos: Point2D = AnchorPointPosition.toGlobalPos$default(Coordinates.INSTANCE.getAnchorPosition(), 0, 0, 3, null)
               val var10000: Any = `this$0`.supplier.get()
               RainbowTextUtilsKt.rainbowText(
                  var10000 as java.lang.String,
                  Coordinates.INSTANCE.multiColor.isRainbow(),
                  Coordinates.INSTANCE.multiColor.isPositionalRainbow(),
                  Coordinates.INSTANCE.multiColor.getChromaOrDefault().getRGB(),
                  (int)pos.getX(),
                  (int)pos.getY()
               ) as Component
            }
         )
      }
   }

   @SourceDebugExtension(["SMAP\nCoordinates.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Coordinates.kt\ngg/norisk/client/v2/modules/coordinates/Coordinates$CoordinatesComponent\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,269:1\n328#2:270\n40#2:271\n185#2:272\n40#2:273\n185#2:274\n40#2:275\n185#2:276\n40#2:277\n185#2:278\n40#2:279\n*S KotlinDebug\n*F\n+ 1 Coordinates.kt\ngg/norisk/client/v2/modules/coordinates/Coordinates$CoordinatesComponent\n*L\n260#1:270\n260#1:271\n211#1:272\n211#1:273\n214#1:274\n214#1:275\n217#1:276\n217#1:277\n220#1:278\n220#1:279\n*E\n"])
   private class CoordinatesComponent(horizontalSizing: Sizing = Sizing.Companion.content(Coordinates.INSTANCE.dynamicBackground.getDynamicWidth()),
      verticalSizing: Sizing = Sizing.Companion.content(Coordinates.INSTANCE.dynamicBackground.getDynamicHeight())
   ) : FlowLayout(horizontalSizing, verticalSizing, Algorithm.HORIZONTAL) {
      init {
         this.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
         this.handleLayout()
         this.allowOverflow(true)
      }

      public fun handleLayout() {
         this.surface(Coordinates.INSTANCE.getBackground().toSurface())
         val xLabel: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent({ 
            val var10000: Minecraft = Minecraft.getInstance()
            "X: ${if (var10000.player != null) var10000.player.getBlockX() else null}"
         })
         val yLabel: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent({ 
            val var10000: Minecraft = Minecraft.getInstance()
            "Y: ${if (var10000.player != null) var10000.player.getBlockY() else null}"
         })
         val zLabel: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent({ 
            val var10000: Minecraft = Minecraft.getInstance()
            "Z: ${if (var10000.player != null) var10000.player.getBlockZ() else null}"
         })
         val biomeLabel: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent({ 
            val var10000: Minecraft = Minecraft.getInstance()
            "Biome: ${if (var10000.player != null) MCClientKt.getBiomeName(var10000.player) else null}"
         })
         this.gap(5)
         this.clearChildren()
         if (Coordinates.INSTANCE.alignment === Coordinates.Alignment.VERTICAL) {
            val verticalWrapper: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
            verticalWrapper.allowOverflow(true)
            if (Coordinates.INSTANCE.showX) {
               verticalWrapper.child(xLabel as UIComponent)
            }

            if (Coordinates.INSTANCE.showY) {
               verticalWrapper.child(yLabel as UIComponent)
            }

            if (Coordinates.INSTANCE.showZ) {
               verticalWrapper.child(zLabel as UIComponent)
            }

            if (Coordinates.INSTANCE.showBiome) {
               verticalWrapper.child(biomeLabel as UIComponent)
            }

            this.child(verticalWrapper as UIComponent)
            if (Coordinates.INSTANCE.showDirection) {
               this.child(Coordinates.DirectionComponent(null, null, 3, null) as UIComponent)
            }
         } else {
            val var7: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
            var7.gap(2)
            var7.allowOverflow(true)
            val coordsWrapper: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
            coordsWrapper.gap(5)
            coordsWrapper.allowOverflow(true)
            if (Coordinates.INSTANCE.showX) {
               coordsWrapper.child(xLabel as UIComponent)
            }

            if (Coordinates.INSTANCE.showY) {
               coordsWrapper.child(yLabel as UIComponent)
            }

            if (Coordinates.INSTANCE.showZ) {
               coordsWrapper.child(zLabel as UIComponent)
            }

            if (Coordinates.INSTANCE.showDirection) {
               coordsWrapper.child(Coordinates.DirectionComponent(null, null, 3, null) as UIComponent)
            }

            var7.child(coordsWrapper as UIComponent)
            if (Coordinates.INSTANCE.showBiome) {
               var7.child(biomeLabel as UIComponent)
            }

            this.child(var7 as UIComponent)
         }
      }

      protected open fun parentUpdate(delta: Float, mouseX: Int, mouseY: Int) {
         super.parentUpdate(delta, mouseX, mouseY)
         val var10000: Minecraft = Minecraft.getInstance()
         if (var10000.gui.screen() is IModuleScreen) {
            this.handleLayout()
         }
      }

      fun CoordinatesComponent() {
         this(null, null, 3, null)
      }
   }

   @SourceDebugExtension(["SMAP\nCoordinates.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Coordinates.kt\ngg/norisk/client/v2/modules/coordinates/Coordinates$DirectionComponent\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCEntity.kt\ngg/norisk/compat/client/MCEntityKt\n*L\n1#1,269:1\n185#2:270\n40#2:271\n185#2:273\n40#2:274\n185#2:276\n40#2:277\n122#3:272\n122#3:275\n122#3:278\n*S KotlinDebug\n*F\n+ 1 Coordinates.kt\ngg/norisk/client/v2/modules/coordinates/Coordinates$DirectionComponent\n*L\n165#1:270\n165#1:271\n172#1:273\n172#1:274\n179#1:276\n179#1:277\n166#1:272\n173#1:275\n180#1:278\n*E\n"])
   private class DirectionComponent(horizontalSizing: Sizing = Sizing.Companion.content(), verticalSizing: Sizing = Sizing.Companion.content()) : FlowLayout(
         horizontalSizing, verticalSizing, Algorithm.VERTICAL
      ) {
      init {
         this.horizontalAlignment(HorizontalAlignment.CENTER)
         this.handleLayout()
         this.allowOverflow(true)
      }

      public fun getSign(value: Int): String {
         var var10000: java.lang.String
         when (value) {
            -1 -> var10000 = "-"
            0 -> var10000 = " "
            1 -> var10000 = "+"
            else -> var10000 = " "
         }

         return var10000
      }

      public fun handleLayout() {
         val firstValue: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent({ 
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.player == null) "" else Direction8.Companion.fromYaw((var10000.player as LivingEntity).yHeadRot).getAbbreviation()
         })
         firstValue.horizontalSizing(Sizing.Companion.fixed(13))
         firstValue.horizontalTextAlignment(HorizontalAlignment.CENTER)
         val var9: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent(
            { 
               val var10000: Minecraft = Minecraft.getInstance()
               if (var10000.player == null)
                  ""
                  else
                  `this$0`.getSign(
                     (Direction8.Companion.fromYaw((var10000.player as LivingEntity).yHeadRot).getTowards().getFirst() as java.lang.Number).intValue()
                  )
               }
         )
         var9.horizontalSizing(Sizing.Companion.fixed(13))
         var9.horizontalTextAlignment(HorizontalAlignment.CENTER)
         val var12: Coordinates.AutoUpdateLabelComponent = Coordinates.AutoUpdateLabelComponent(
            { 
               val var10000: Minecraft = Minecraft.getInstance()
               if (var10000.player == null)
                  ""
                  else
                  `this$0`.getSign(
                     (Direction8.Companion.fromYaw((var10000.player as LivingEntity).yHeadRot).getTowards().getSecond() as java.lang.Number).intValue()
                  )
               }
         )
         var12.horizontalSizing(Sizing.Companion.fixed(13))
         var12.horizontalTextAlignment(HorizontalAlignment.CENTER)
         if (Coordinates.INSTANCE.alignment === Coordinates.Alignment.VERTICAL) {
            this.child(var9 as UIComponent)
            this.child(firstValue as UIComponent)
            this.child(var12 as UIComponent)
         } else {
            this.child(firstValue as UIComponent)
         }
      }

      fun DirectionComponent() {
         this(null, null, 3, null)
      }
   }
}
