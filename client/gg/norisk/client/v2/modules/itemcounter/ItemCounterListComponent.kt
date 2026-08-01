package gg.norisk.client.v2.modules.itemcounter

import gg.norisk.compat.client.MCRegistryKt
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.ItemComponent
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.CursorStyle
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.owolib.owo.ui.input.MouseButtonEvent
import gg.norisk.owolib.owo.ui.util.UISounds
import gg.norisk.ui.api.serializable.MultiColor
import gg.norisk.ui.components.nrc.NrcCollapsibleContainer
import gg.norisk.ui.components.nrc.NrcColorPickerKt
import gg.norisk.ui.components.nrc.NrcSliderWithInput
import gg.norisk.ui.modules.v3.RightShiftMenuV3Screen
import gg.norisk.ui.modules.v3.V3Button
import gg.norisk.ui.modules.v3.V3ColorSwatch
import gg.norisk.ui.modules.v3.V3EnumSwitcher
import gg.norisk.ui.modules.v3.V3RowActions
import gg.norisk.ui.modules.v3.V3Theme
import gg.norisk.ui.modules.v3.V3ValueLine
import gg.norisk.ui.v2.hud.Background
import gg.norisk.ui.v2.hud.Background.Type
import java.awt.Color
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component

@SourceDebugExtension(["SMAP\nItemCounterListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemCounterListComponent.kt\ngg/norisk/client/v2/modules/itemcounter/ItemCounterListComponent\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,251:1\n1563#2:252\n1634#2,3:253\n*S KotlinDebug\n*F\n+ 1 ItemCounterListComponent.kt\ngg/norisk/client/v2/modules/itemcounter/ItemCounterListComponent\n*L\n31#1:252\n31#1:253,3\n*E\n"])
public class ItemCounterListComponent : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
   public fun build(items: List<ItemCounterDto>) {
      this.clearChildren()
      val `$this$map$iv`: java.lang.Iterable = items
      val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(items, 10))

      for (`item$iv$iv` in `$this$map$iv`) {
         `destination$iv$iv`.add(ItemCounterListComponent.ItemCounterListEntry(`item$iv$iv` as ItemCounterDto))
      }

      this.children(`destination$iv$iv` as java.util.List)
   }

   public fun add(dto: ItemCounterDto) {
      this.child(ItemCounterListComponent.ItemCounterListEntry(dto) as UIComponent)
   }

   @SourceDebugExtension(["SMAP\nItemCounterListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemCounterListComponent.kt\ngg/norisk/client/v2/modules/itemcounter/ItemCounterListComponent$ItemCounterListEntry\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,251:1\n1#2:252\n*E\n"])
   public inner class ItemCounterListEntry(itemCounterDto: ItemCounterDto) : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
      public final val itemCounterDto: ItemCounterDto
      public final val shouldExpand: Boolean
      public final val collapsible: NrcCollapsibleContainer
      public final val collapsibleChild: FlowLayout

      public final var scaleSlider: NrcSliderWithInput
         internal set

      public final val scaleLine: V3ValueLine

      public final var backgroundLine: V3ValueLine
         internal set

      public final var colorLine: V3ValueLine
         internal set

      public final val dynamicBackgroundLine: FlowLayout

      init {
         this.itemCounterDto = itemCounterDto
         this.shouldExpand = ItemCounter.INSTANCE.pendingExpandItemId == this.itemCounterDto.itemId
         val var10003: java.lang.String = this.itemCounterDto.itemId.toString()
         this.collapsible = NrcCollapsibleContainer(
            TextKt.toSmallCapsText(StringsKt.substringAfter$default(var10003, ":", null, 2, null)) as Component,
            this.shouldExpand,
            Sizing.Companion.fill(),
            null,
            8,
            null
         )
         var onOffButton: ItemCounterListComponent.ItemCounterListEntry = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         onOffButton.gap(0)
         this.collapsibleChild = onOffButton
         val var14: NrcSliderWithInput = NrcSliderWithInput(0.5, 3.0, this.itemCounterDto.scale, 1.0, 0.01, false, 0, 0, 0, 0.0F, 0.0F, 2016, null)
         var14.onChanged().subscribe({ it: Double ->
            `this$0`.itemCounterDto.scale = (float)it
         })
         this.scaleSlider = var14
         val var15: V3ValueLine = V3ValueLine(null, null, 3, null)
         var15.title("Scale")
         var15.right(this.scaleSlider as UIComponent)
         this.scaleLine = var15
         this.backgroundLine = this.buildBackgroundLine()
         this.colorLine = this.buildMultiColorLine()
         onOffButton = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         onOffButton.child(this.buildPaddingSliders() as UIComponent)
         this.dynamicBackgroundLine = onOffButton
         this.resetComponents()
         ItemCounterListComponentKt.access$styleCollapsibleV3(this.collapsible)
         onOffButton = this

         try {
            var var23: ItemCounterListComponent.ItemCounterListEntry = onOffButton
            val var10000: java.lang.String = onOffButton.itemCounterDto.itemId.toString()
            val iconComp: ItemComponent = ItemComponent(MCRegistryKt.defaultStack$default(MCRegistryKt.mcItem(var10000), 0, 1, null))
            iconComp.sizing(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
            iconComp.margins(Insets.Companion.right(4))
            val `$this$lambda_u2412_u24lambda_u2411`: Any = CollectionsKt.firstOrNull(var23.collapsible.getTitleLayout().children())
            var23 = (ItemCounterListComponent.ItemCounterListEntry)Result.constructor_impl/* $VF was: constructor-impl */(
               if ((`$this$lambda_u2412_u24lambda_u2411` as? FlowLayout) != null)
                  (`$this$lambda_u2412_u24lambda_u2411` as? FlowLayout).child(0, iconComp as UIComponent)
                  else
                  null
            )
         } catch (var13: java.lang.Throwable) {
            val var22: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var13))
         }

         val var18: V3Button = V3RowActions.INSTANCE.toggleButton({ 
            `this$0`.itemCounterDto.isEnabled
         }, { 
            `this$0`.itemCounterDto.isEnabled = !`this$0`.itemCounterDto.isEnabled
            Unit.INSTANCE
         })
         val var25: V3Button = V3RowActions.deleteButton$default(V3RowActions.INSTANCE, 0, { 
            val var10000: ItemCounter = ItemCounter.INSTANCE
            val var10001: ItemCounterDto = `this$0`.itemCounterDto
            val var10002: ParentUIComponent = `this$0`.root()
            var10000.remove(var10001, var10002)
            Unit.INSTANCE
         }, 1, null)
         this.collapsible.getTitleLayout().horizontalSizing(Sizing.Companion.fill())
         this.collapsible.getTitleLayout().child(V3RowActions.INSTANCE.rightAlignedRow(arrayOf(var18, var25)) as UIComponent)
         this.id(this.itemCounterDto.itemId.toString())
         this.child(this.collapsible as UIComponent)
         this.collapsible.child(this.collapsibleChild as UIComponent)
         val var42: FlowLayout = this.collapsibleChild
         val var31: V3ValueLine = V3ValueLine(null, null, 3, null)
         var31.title("Reset")
         val var43: FlowLayout = var31.getRightWrapper()
         val var34: LabelComponent = LabelComponent(TextKt.getLiteral("↺") as Component)
         var34.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
         var34.margins(Insets.Companion.bottom(2))
         var34.setAutoColorSupplier({ 
            V3Theme.INSTANCE.grayColor(10)
         })
         var34.cursorStyle(CursorStyle.HAND)
         var34.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
            UISounds.playButtonSound()
            `this$0`.itemCounterDto.reset()
            `this$0`.resetComponents()
            true
         })
         var43.child(var34 as UIComponent)
         var42.child(var31 as UIComponent)
         this.collapsibleChild.child(this.backgroundLine as UIComponent)
         this.collapsibleChild.child(this.scaleLine as UIComponent)
         this.collapsibleChild.child(this.dynamicBackgroundLine as UIComponent)
         this.collapsibleChild.child(this.colorLine as UIComponent)
      }

      private fun buildBackgroundLine(): V3ValueLine {
         val bg: Background = this.itemCounterDto.background
         val var2: V3ValueLine = V3ValueLine(null, null, 3, null)
         var2.margins(Insets.Companion.bottom(1))
         var2.padding(Insets.Companion.of(1, 1, 4, 4))
         var2.title("Background")
         val rightRow: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
         rightRow.alignment(HorizontalAlignment.RIGHT, VerticalAlignment.CENTER)
         rightRow.gap(4)
         val switcher: V3EnumSwitcher = V3EnumSwitcher(CollectionsKt.toList(Type.getEntries() as java.lang.Iterable), bg.getType(), { it: Type ->
            it.name()
         }, 0, 8, null)
         switcher.onChanged().subscribe({ newType: Type ->
            `$bg`.setType(newType)
         })
         val swatchPlaceholder: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
         swatchPlaceholder.allowOverflow(true)
         if (bg.getType().getHasColor()) {
            val swatch: V3ColorSwatch = V3ColorSwatch(NrcColorPickerKt.toJavaColor(bg.getColor().getColor()), false, 0, 6, null)
            swatch.onChanged().subscribe({ color: Color ->
               `$bg`.getColor().setColor(gg.norisk.owolib.owo.ui.core.Color.Companion.ofArgb(color.getRGB()))
            })
            swatchPlaceholder.child(swatch as UIComponent)
         }

         rightRow.child(swatchPlaceholder as UIComponent)
         rightRow.child(switcher as UIComponent)
         var2.right(rightRow as UIComponent)
         return var2
      }

      private fun buildMultiColorLine(): V3ValueLine {
         val mc: MultiColor = this.itemCounterDto.multiColor
         val var2: V3ValueLine = V3ValueLine(null, null, 3, null)
         var2.margins(Insets.Companion.bottom(1))
         var2.title("Color")
         val swatch: V3ColorSwatch = V3ColorSwatch(NrcColorPickerKt.toJavaColor(mc.getColor()), false, 0, 4, null)
         swatch.setShowRainbow(true)
         swatch.setRainbow(mc.isRainbow())
         swatch.setPositionalRainbow(mc.isPositionalRainbow())
         swatch.onChanged()
            .subscribe(
               { color: Color ->
                  `$mc`.setColor(
                     gg.norisk.owolib.owo.ui.core.Color(
                        (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
                     )
                  )
               }
            )
            swatch.setOnRainbowChanged({ it: Boolean ->
            `$mc`.setRainbow(it)
            Unit.INSTANCE
         })
         swatch.setOnPositionalChanged({ it: Boolean ->
            `$mc`.setPositionalRainbow(it)
            Unit.INSTANCE
         })
         var2.right(swatch as UIComponent)
         return var2
      }

      private fun buildPaddingSliders(): FlowLayout {
         val var2: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         var2.gap(0)
         val var10: V3ValueLine = V3ValueLine(null, null, 3, null)
         var10.title("Padding W")
         var var5: NrcSliderWithInput = NrcSliderWithInput(0.0, 20.0, this.itemCounterDto.paddingWidth, 4.0, 1.0, false, 0, 0, 0, 0.0F, 0.0F, 1760, null)
         var5.onChanged().subscribe({ it: Double ->
            `this$0`.itemCounterDto.paddingWidth = (int)it
         })
         var10.right(var5 as UIComponent)
         var2.child(var10 as UIComponent)
         val var11: V3ValueLine = V3ValueLine(null, null, 3, null)
         var11.title("Padding H")
         var5 = NrcSliderWithInput(0.0, 20.0, this.itemCounterDto.paddingHeight, 4.0, 1.0, false, 0, 0, 0, 0.0F, 0.0F, 1760, null)
         var5.onChanged().subscribe({ it: Double ->
            `this$0`.itemCounterDto.paddingHeight = (int)it
         })
         var11.right(var5 as UIComponent)
         var2.child(var11 as UIComponent)
         return var2
      }

      private fun resetComponents() {
         val colorIndex: FlowLayout = this.scaleLine.getRightWrapper()
         colorIndex.clearChildren()
         val var4: NrcSliderWithInput = NrcSliderWithInput(0.5, 3.0, this.itemCounterDto.scale, 1.0, 0.01, false, 0, 0, 0, 0.0F, 0.0F, 2016, null)
         var4.onChanged().subscribe({ it: Double ->
            `this$0`.itemCounterDto.scale = (float)it
         })
         this.scaleSlider = var4
         colorIndex.child(this.scaleSlider as UIComponent)
         V3ValueLine.Companion.styleV3(this.scaleSlider as UIComponent)
         this.dynamicBackgroundLine.clearChildren()
         this.dynamicBackgroundLine.child(this.buildPaddingSliders() as UIComponent)
         val var8: Int = this.collapsibleChild.children().indexOf(this.backgroundLine)
         if (var8 >= 0) {
            this.collapsibleChild.removeChild(this.backgroundLine as UIComponent)
            this.backgroundLine = this.buildBackgroundLine()
            this.collapsibleChild.child(var8, this.backgroundLine as UIComponent)
         }

         val var9: Int = this.collapsibleChild.children().indexOf(this.colorLine)
         if (var9 >= 0) {
            this.collapsibleChild.removeChild(this.colorLine as UIComponent)
            this.colorLine = this.buildMultiColorLine()
            this.collapsibleChild.child(var9, this.colorLine as UIComponent)
         }
      }
   }
}
