package gg.voidrix.client.v2.modules.itemhighlighter

import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.ItemComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.components.voidrix.VoidrixCollapsibleContainer
import gg.voidrix.ui.components.voidrix.VoidrixColorPickerKt
import gg.voidrix.ui.modules.v3.V3Button
import gg.voidrix.ui.modules.v3.V3ColorSwatch
import gg.voidrix.ui.modules.v3.V3EnumSwitcher
import gg.voidrix.ui.modules.v3.V3RowActions
import gg.voidrix.ui.modules.v3.V3ValueLine
import java.awt.Color
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component

@SourceDebugExtension(["SMAP\nItemHighlighterListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemHighlighterListComponent.kt\ngg/voidrix/client/v2/modules/itemhighlighter/ItemHighlighterListComponent\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,152:1\n1563#2:153\n1634#2,3:154\n*S KotlinDebug\n*F\n+ 1 ItemHighlighterListComponent.kt\ngg/voidrix/client/v2/modules/itemhighlighter/ItemHighlighterListComponent\n*L\n36#1:153\n36#1:154,3\n*E\n"])
public class ItemHighlighterListComponent : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
   public fun build(items: List<ItemHighlighterDto>) {
      this.clearChildren()
      val `$this$map$iv`: java.lang.Iterable = items
      val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(items, 10))

      for (`item$iv$iv` in `$this$map$iv`) {
         `destination$iv$iv`.add(ItemHighlighterListComponent.ItemHighlighterListEntry(`item$iv$iv` as ItemHighlighterDto))
      }

      this.children(`destination$iv$iv` as java.util.List)
   }

   public fun add(dto: ItemHighlighterDto) {
      this.child(ItemHighlighterListComponent.ItemHighlighterListEntry(dto) as UIComponent)
   }

   public inner class ItemHighlighterListEntry(dto: ItemHighlighterDto) : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
      public final val dto: ItemHighlighterDto
      private final val collapsible: VoidrixCollapsibleContainer
      private final val collapsibleChild: FlowLayout
      private final val colorLine: V3ValueLine

      init {
         this.dto = dto
         val var10003: java.lang.String = this.dto.itemId.toString()
         this.collapsible = VoidrixCollapsibleContainer(
            TextKt.toSmallCapsText(StringsKt.substringAfter$default(var10003, ":", null, 2, null)) as Component, false, Sizing.Companion.fill(), null, 8, null
         )
         var onOffButton: ItemHighlighterListComponent.ItemHighlighterListEntry = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         onOffButton.gap(0)
         this.collapsibleChild = onOffButton
         this.colorLine = this.buildColorLine()
         ItemHighlighterListComponentKt.access$styleCollapsibleV3(this.collapsible)
         onOffButton = this

         try {
            var var16: ItemHighlighterListComponent.ItemHighlighterListEntry = onOffButton
            val var10000: java.lang.String = onOffButton.dto.itemId.toString()
            val switcher: ItemComponent = ItemComponent(MCRegistryKt.defaultStack$default(MCRegistryKt.mcItem(var10000), 0, 1, null))
            switcher.sizing(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
            switcher.margins(Insets.Companion.right(4))
            val var9: Any = CollectionsKt.firstOrNull(var16.collapsible.getTitleLayout().children())
            var16 = (ItemHighlighterListComponent.ItemHighlighterListEntry)Result.constructor_impl/* $VF was: constructor-impl */(
               if ((var9 as? FlowLayout) != null) (var9 as? FlowLayout).child(0, switcher as UIComponent) else null
            )
         } catch (var12: java.lang.Throwable) {
            val var15: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var12))
         }

         val var14: V3Button = V3RowActions.INSTANCE.toggleButton({ 
            `this$0`.dto.isEnabled
         }, { 
            `this$0`.dto.isEnabled = !`this$0`.dto.isEnabled
            Unit.INSTANCE
         })
         val var18: V3Button = V3RowActions.deleteButton$default(V3RowActions.INSTANCE, 0, { 
            val var10000: ItemHighlighter = ItemHighlighter.INSTANCE
            val var10001: ItemHighlighterDto = `this$0`.dto
            val var10002: ParentUIComponent = `this$0`.root()
            var10000.remove(var10001, var10002)
            Unit.INSTANCE
         }, 1, null)
         this.collapsible.getTitleLayout().horizontalSizing(Sizing.Companion.fill())
         this.collapsible.getTitleLayout().child(V3RowActions.INSTANCE.rightAlignedRow(arrayOf(var14, var18)) as UIComponent)
         this.id(this.dto.itemId.toString())
         this.child(this.collapsible as UIComponent)
         this.collapsible.child(this.collapsibleChild as UIComponent)
         val var27: FlowLayout = this.collapsibleChild
         val var21: V3ValueLine = V3ValueLine(null, null, 3, null)
         var21.margins(Insets.Companion.bottom(1))
         var21.title("Mode")
         val var24: V3EnumSwitcher = V3EnumSwitcher(
            CollectionsKt.toList(HighlightMode.getEntries() as java.lang.Iterable), this.dto.mode, { it: HighlightMode ->
               TextKt.toSmallCaps(it.name())
            }, 0, 8, null
         )
         var24.onChanged().subscribe({ newMode: HighlightMode ->
            `this$0`.dto.mode = newMode
            `this$0`.syncColorLineVisibility()
         })
         var21.right(var24 as UIComponent)
         var27.child(var21 as UIComponent)
         this.collapsibleChild.child(this.colorLine as UIComponent)
         this.syncColorLineVisibility()
      }

      private fun buildColorLine(): V3ValueLine {
         val mc: MultiColor = this.dto.highlightColor
         val var2: V3ValueLine = V3ValueLine(null, null, 3, null)
         var2.margins(Insets.Companion.bottom(1))
         var2.title("Color")
         val swatch: V3ColorSwatch = V3ColorSwatch(VoidrixColorPickerKt.toJavaColor(mc.getColor()), false, 0, 4, null)
         swatch.setShowRainbow(true)
         swatch.setRainbow(mc.isRainbow())
         swatch.setPositionalRainbow(mc.isPositionalRainbow())
         swatch.onChanged()
            .subscribe(
               { color: Color ->
                  `$mc`.setColor(
                     gg.voidrix.owolib.owo.ui.core.Color(
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

      private fun syncColorLineVisibility() {
         this.colorLine
            .positioning(if (this.dto.mode === HighlightMode.COLOR) Positioning.Companion.layout() else Positioning.Companion.absolute(-9999, -9999))
         }
   }
}
