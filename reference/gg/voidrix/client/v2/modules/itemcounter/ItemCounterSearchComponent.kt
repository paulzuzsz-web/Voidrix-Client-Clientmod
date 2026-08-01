package gg.voidrix.client.v2.modules.itemcounter

import gg.voidrix.compat.client.ItemSearch
import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.compat.client.ItemSearch.ItemSearchEntry
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.ItemComponent
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.ScrollContainer
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.container.ScrollContainer.Scrollbar
import gg.voidrix.owolib.owo.ui.core.CursorStyle
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.NonCenteredOverlayContainer
import gg.voidrix.ui.components.voidrix.VoidrixInputField
import gg.voidrix.ui.modules.v3.RightShiftMenuV3Screen
import gg.voidrix.ui.modules.v3.V3Surfaces
import gg.voidrix.ui.modules.v3.V3Theme
import gg.voidrix.ui.versionless.DrawContextVersionlessKt
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

@SourceDebugExtension(["SMAP\nItemCounterSearchComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemCounterSearchComponent.kt\ngg/voidrix/client/v2/modules/itemcounter/ItemCounterSearchComponent\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,171:1\n1869#2:172\n1870#2:174\n269#3:173\n*S KotlinDebug\n*F\n+ 1 ItemCounterSearchComponent.kt\ngg/voidrix/client/v2/modules/itemcounter/ItemCounterSearchComponent\n*L\n72#1:172\n72#1:174\n76#1:173\n*E\n"])
public class ItemCounterSearchComponent(horizontalSizing: Sizing = Sizing.Companion.fill(), verticalSizing: Sizing = Sizing.Companion.content()) : FlowLayout(
      horizontalSizing, verticalSizing, Algorithm.VERTICAL
   ) {
   private final var overlay: NonCenteredOverlayContainer<*>?
   private final var dropdown: UIComponent?

   init {
      val var4: VoidrixInputField = VoidrixInputField(120, "", 0.75F, true)
      var4.horizontalSizing(Sizing.Companion.fill())
      var4.setUseV3Colors(true)
      var4.margins(Insets.Companion.bottom(2))
      var4.setPlaceholder(TextKt.getLiteral("Search items...") as Component)
      this.child(var4 as UIComponent)
      var4.onChanged().subscribe({ it: java.lang.String ->
         _init_$openDropdown(`this$0`, `$searchField`)
      })
   }

   private fun closeDropdown() {
      if (this.overlay != null) {
         this.overlay.remove()
      }

      this.overlay = null
      this.dropdown = null
   }

   private fun isDropdownOpen(): Boolean {
      return this.overlay != null && this.dropdown != null
   }

   public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      if (this.dropdown != null) {
         this.dropdown.positioning(Positioning.Companion.absolute(this.x(), this.y() + this.fullSize().getHeight()))
      }

      if (this.overlay != null && (this.overlay == null || !this.overlay.hasParent())) {
         this.overlay = null
         this.dropdown = null
      }

      super.draw(context, mouseX, mouseY, partialTicks, delta)
   }

   @JvmStatic
   fun `_init_$addItem`(`this$0`: ItemCounterSearchComponent, searchField: VoidrixInputField, id: Identifier, root: ParentUIComponent) {
      ItemCounter.INSTANCE.add(id, root)
      `this$0`.closeDropdown()
      searchField.text("")
   }

   @JvmStatic
   fun `_init_$openDropdown`(`this$0`: ItemCounterSearchComponent, searchField: VoidrixInputField) {
      `this$0`.closeDropdown()
      val searchText: java.lang.String = searchField.text()
      if (searchText.length() != 0) {
         val results: java.util.List = CollectionsKt.take(
            ItemSearch.search$default(ItemSearch.INSTANCE, ItemSearch.INSTANCE.buildIndex(), searchText, 0, 4, null), 30
         )
         if (!results.isEmpty()) {
            val scroll: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
            scroll.padding(Insets.Companion.right(3))
            val listChild: FlowLayout = scroll

            for (`$this$openDropdown_u24lambda_u2416` in results) {
               val item: Item = (`$this$openDropdown_u24lambda_u2416` as ItemSearchEntry).component1()

               try {
                  val id: Identifier = MCRegistryKt.registryId(item)
                  val stack: ItemStack = MCRegistryKt.defaultStack$default(item, 0, 1, null)
                  val var10000: java.lang.String = MCRegistryKt.displayName(stack).getString()
                  val var48: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
                  var48.padding(Insets.Companion.of(2))
                  var48.verticalAlignment(VerticalAlignment.CENTER)
                  var48.gap(3)
                  var48.cursorStyle(CursorStyle.HAND)
                  val var49: BooleanRef = BooleanRef()
                  var48.mouseEnter().subscribe({ 
                     `$isHovered`.element = true
                  })
                  var48.mouseLeave().subscribe({ 
                     `$isHovered`.element = false
                  })
                  var48.surface(
                     { context: OwoUIGraphics, component: ParentUIComponent ->
                        if (`$isHovered`.element) {
                           context.fill(
                              component.x(),
                              component.y(),
                              component.x() + component.width(),
                              component.y() + component.height(),
                              V3Theme.INSTANCE.grayAlpha(5, 60)
                           )
                        }
                     }
                  )
                  var48.mouseDown().subscribe(lambda_13_lambda_12_lambda_6@{ var4: MouseButtonEvent, var5: Boolean ->
                     UISounds.playButtonSound()
                     if (ItemCounter.INSTANCE.items.containsKey(`$id`)) {
                        val var10000: ItemCounterDto = ItemCounter.INSTANCE.items.get(`$id`)
                        if (var10000 == null) {
                           return@lambda_13_lambda_12_lambda_6 true
                        }

                        val var7: ItemCounter = ItemCounter.INSTANCE
                        val var10002: ParentUIComponent = `$wrapper`.root()
                        var7.remove(var10000, var10002)
                        _init_$openDropdown(`this$0`, `$searchField`)
                     } else {
                        val var10003: ParentUIComponent = `$wrapper`.root()
                        _init_$addItem(`this$0`, `$searchField`, `$id`, var10003)
                     }

                     return@lambda_13_lambda_12_lambda_6 true
                  })
                  val var50: ItemComponent = ItemComponent(stack)
                  var50.sizing(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
                  val isAdded: LabelComponent = LabelComponent(TextKt.getLiteral(var10000) as Component)
                  isAdded.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
                  isAdded.scale(0.75F)
                  isAdded.setAutoColorSupplier({ 
                     V3Theme.INSTANCE.grayColor(11)
                  })
                  val var52: Boolean = ItemCounter.INSTANCE.items.containsKey(id)
                  val var54: LabelComponent = LabelComponent(TextKt.getLiteral(if (var52) "✕" else "+") as Component)
                  var54.scale(0.75F)
                  var54.setAutoColorSupplier({ 
                     if (`$isAdded`) V3Theme.INSTANCE.grayColor(9) else V3Theme.INSTANCE.grayColor(11)
                  })
                  var48.child(var50 as UIComponent)
                  val var55: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.expand(100), Sizing.Companion.content())
                  var55.child(isAdded as UIComponent)
                  var55.verticalAlignment(VerticalAlignment.CENTER)
                  var48.child(var55 as UIComponent)
                  var48.child(var54 as UIComponent)
                  val var46: Any = Result.constructor_impl/* $VF was: constructor-impl */(listChild.child(var48 as UIComponent))
               } catch (var30: java.lang.Throwable) {
                  val `$this$openDropdown_u24lambda_u2413_u24lambda_u2412`: Any = Result.constructor_impl/* $VF was: constructor-impl */(
                     ResultKt.createFailure(var30)
                  )
               }
            }

            val var34: ScrollContainer = UIContainers.verticalScroll(Sizing.Companion.fill(), Sizing.Companion.fixed(80), listChild as UIComponent)
            var34.scrollbar(Scrollbar.Companion.flat(V3Surfaces.INSTANCE.scrollbarColor()))
            var34.scrollbarThiccness(2.0)
            val var38: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed((int)`this$0`.width()), Sizing.Companion.fixed(85))
            var38.surface(V3Surfaces.INSTANCE.dialogSurface(2))
            var38.child(var34 as UIComponent)
            var38.padding(Insets.Companion.of(2))
            `this$0`.dropdown = var38 as UIComponent
            val var39: NonCenteredOverlayContainer = NonCenteredOverlayContainer(var38 as UIComponent)
            DrawContextVersionlessKt.voidrixZIndex(var39 as UIComponent, 8000)
            var39.surface(Surface.BLANK)
            `this$0`.overlay = var39
            val var43: ParentUIComponent = `this$0`.root()
            val var58: FlowLayout = var43 as? FlowLayout
            if ((var43 as? FlowLayout) != null) {
               val var10001: NonCenteredOverlayContainer = `this$0`.overlay
               var58.child(var10001 as UIComponent)
            }
         }
      }
   }

   fun ItemCounterSearchComponent() {
      this(null, null, 3, null)
   }
}
