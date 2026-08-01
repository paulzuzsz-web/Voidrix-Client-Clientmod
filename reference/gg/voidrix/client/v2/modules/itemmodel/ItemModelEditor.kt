package gg.voidrix.client.v2.modules.itemmodel

import gg.voidrix.compat.client.ItemSearch
import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.compat.client.ItemSearch.ItemSearchEntry
import gg.voidrix.compat.render.VoidrixItemDisplayContext
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.ItemComponent
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.ScrollContainer
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.ScrollContainer.Scrollbar
import gg.voidrix.owolib.owo.ui.core.CursorStyle
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixCheckbox
import gg.voidrix.ui.components.voidrix.VoidrixCollapsibleContainer
import gg.voidrix.ui.components.voidrix.VoidrixInputField
import gg.voidrix.ui.modules.v3.RightShiftMenuV3Screen
import gg.voidrix.ui.modules.v3.V3EnumSwitcher
import gg.voidrix.ui.modules.v3.V3Surfaces
import gg.voidrix.ui.modules.v3.V3Theme
import gg.voidrix.ui.modules.v3.V3ValueLine
import java.util.Arrays
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

@SourceDebugExtension(["SMAP\nItemModelEditor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemModelEditor.kt\ngg/voidrix/client/v2/modules/itemmodel/ItemModelEditor\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,294:1\n1869#2,2:295\n1869#2:299\n1870#2:301\n67#3:297\n67#3:298\n269#3:300\n*S KotlinDebug\n*F\n+ 1 ItemModelEditor.kt\ngg/voidrix/client/v2/modules/itemmodel/ItemModelEditor\n*L\n33#1:295,2\n91#1:299\n91#1:301\n49#1:297\n220#1:298\n95#1:300\n*E\n"])
public object ItemModelEditor {
   private fun styleCollapsibleV3(collapsible: VoidrixCollapsibleContainer) {
      collapsible.getTitleLabel().shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      collapsible.getTitleLabel().setAutoColorSupplier({ 
         V3Theme.INSTANCE.grayColor(12)
      })
      val var3: Any = CollectionsKt.firstOrNull(collapsible.getTitleLayout().children())
      val titleRow: FlowLayout = var3 as? FlowLayout
      if ((var3 as? FlowLayout) != null) {
         val var10000: java.util.List = titleRow.children()
         if (var10000 != null) {
            for (`element$iv` in var10000) {
               val child: UIComponent = `element$iv` as UIComponent
               if (`element$iv` as UIComponent is LabelComponent && `element$iv` as UIComponent != collapsible.getTitleLabel()) {
                  (child as LabelComponent).shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
                  (child as LabelComponent).setAutoColorSupplier({ 
                     V3Theme.INSTANCE.grayColor(11)
                  })
               }
            }
         }
      }
   }

   private fun buildResetButton(itemName: String, onReset: () -> Unit): LabelComponent {
      val btn: LabelComponent = LabelComponent(TextKt.getLiteral("↺") as Component, 0.9F)
      val hovered: BooleanRef = BooleanRef()
      btn.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      btn.setAutoColorSupplier({ 
         if (`$hovered`.element) V3Theme.INSTANCE.accentColor(11) else V3Theme.INSTANCE.grayColor(10)
      })
      btn.cursorStyle(CursorStyle.HAND)
      val var8: Array<Any> = arrayOf(itemName)
      val var10001: MutableComponent = Component.translatable("voidrix.itemmodel.reset.tooltip", Arrays.copyOf(var8, var8.length))
      btn.tooltip(var10001 as Component)
      btn.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      btn.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      btn.mouseDown().subscribe(lambda_6@{ event: MouseButtonEvent, var2: Boolean ->
         if (event.button() != 0) {
            return@lambda_6 false
         } else {
            UISounds.playButtonSound()
            `$onReset`()
            return@lambda_6 true
         }
      })
      btn.positioning(Positioning.Companion.relative(100, 50))
      btn.margins(Insets.Companion.right(6))
      return btn
   }

   public fun build(settingsPanel: FlowLayout, module: ItemModel) {
      val searchField: VoidrixInputField = VoidrixInputField(120, "", 0.75F, true)
      searchField.horizontalSizing(Sizing.Companion.fill(100))
      searchField.setUseV3Colors(true)
      searchField.setPlaceholder(TextKt.getLiteral("Search items...") as Component)
      searchField.margins(Insets.Companion.bottom(3))
      settingsPanel.child(searchField as UIComponent)
      val listScroll: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      listScroll.gap(2)
      val var8: ScrollContainer = UIContainers.verticalScroll(Sizing.Companion.fill(), Sizing.Companion.fill(100), listScroll as UIComponent)
      var8.scrollbar(Scrollbar.Companion.flat(V3Surfaces.INSTANCE.scrollbarColor()))
      var8.scrollbarThiccness(2.0)
      settingsPanel.child(var8 as UIComponent)
      searchField.onChanged().subscribe({ it: java.lang.String ->
         `$listScroll`.scrollTo(0.0, true)
         build$rebuildItems(`$listScroll`, `$listChild`, `$searchField`, `$module`)
      })
      build$rebuildItems(var8, listScroll, searchField, module)
   }

   private fun buildItemEditor(container: VoidrixCollapsibleContainer, module: ItemModel, itemId: Identifier, rebuild: () -> Unit) {
      val itemTransformation: ItemTransformation = module.getTransformation(itemId)
      val content: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      content.gap(3)
      val previewLine: V3ValueLine = V3ValueLine(Sizing.Companion.fill(95), Sizing.Companion.content())
      previewLine.title("Model Preview")
      val previewCheckbox: VoidrixCheckbox = VoidrixCheckbox(0.75F)
      previewCheckbox.checked(module.modelPreview)
      previewCheckbox.onChanged().subscribe({ checked: Boolean ->
         `$module`.modelPreview = checked
      })
      V3ValueLine.Companion.styleV3(previewCheckbox as UIComponent)
      previewLine.right(previewCheckbox as UIComponent)
      content.child(previewLine as UIComponent)
      val groundTransform: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      groundTransform.gap(2)
      groundTransform.allowOverflow(true)
      groundTransform.child(
         TransformEditorComponent("Ground", itemTransformation.getOrCreate(VoidrixItemDisplayContext.GROUND).scale, 0.0, 4.0, 0.01, 2, 1.0F, true, { 
            `$module`.saveTransformations()
            Unit.INSTANCE
         }) as UIComponent
      )
      groundTransform.child(
         TransformEditorComponent(
            "1st Person", itemTransformation.getOrCreate(VoidrixItemDisplayContext.FIRST_PERSON_RIGHT_HAND).scale, 0.0, 4.0, 0.01, 2, 1.0F, true, { 
               `$module`.saveTransformations()
               Unit.INSTANCE
            }
         ) as UIComponent
      )
      groundTransform.child(TransformEditorComponent("GUI", itemTransformation.getOrCreate(VoidrixItemDisplayContext.GUI).scale, 0.0, 4.0, 0.01, 2, 1.0F, true, { 
         `$module`.saveTransformations()
         Unit.INSTANCE
      }) as UIComponent)
      content.child(groundTransform as UIComponent)
      val contextSwitch: Array<Any> = arrayOfNulls(0)
      val var10000: MutableComponent = Component.translatable("voidrix.itemmodel.label.advanced", Arrays.copyOf(contextSwitch, contextSwitch.length))
      val advancedCollapsible: <unrepresentable> = object : VoidrixCollapsibleContainer {
         public open fun toggleExpansion() {
            super.toggleExpansion()
            module.advancedExpanded = this.expanded()
         }
      }
      this.styleCollapsibleV3(advancedCollapsible)
      val var27: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      var27.gap(3)
      val var28: V3ValueLine = V3ValueLine(Sizing.Companion.fill(95), Sizing.Companion.content())
      var28.title("Context")
      val var31: V3EnumSwitcher = V3EnumSwitcher(VoidrixItemDisplayContext.Companion.validEntries(), module.currentMode, { it: VoidrixItemDisplayContext ->
         it.getDisplayName()
      }, 80)
      var31.onChanged().subscribe({ ctx: VoidrixItemDisplayContext ->
         `$module`.currentMode = ctx
         `$rebuild`()
      })
      var28.right(var31 as UIComponent)
      var27.child(var28 as UIComponent)
      val var34: ItemTransformation.Transformation = itemTransformation.getOrCreate(module.currentMode)
      val var19: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      var19.gap(2)
      var19.allowOverflow(true)
      var19.child(TransformEditorComponent("Scale", var34.scale, 0.0, 4.0, 0.01, 2, 1.0F, true, { 
         `$module`.saveTransformations()
         Unit.INSTANCE
      }) as UIComponent)
      var19.child(TransformEditorComponent("Rotation", var34.rotation, -180.0, 180.0, 1.0, 1, 0.0F, false, { 
         `$module`.saveTransformations()
         Unit.INSTANCE
      }, 128, null) as UIComponent)
      var19.child(TransformEditorComponent("Translation", var34.translation, -1.5, 1.5, 0.005, 3, 0.0F, false, { 
         `$module`.saveTransformations()
         Unit.INSTANCE
      }, 128, null) as UIComponent)
      var27.child(var19 as UIComponent)
      advancedCollapsible.child(var27 as UIComponent)
      content.child(advancedCollapsible as UIComponent)
      container.child(content as UIComponent)
   }

   @JvmStatic
   fun `build$rebuildItems`(listScroll: ScrollContainer<FlowLayout>, listChild: FlowLayout, searchField: VoidrixInputField, `$module`: final ItemModel) {
      val savedScroll: Double = listScroll.scrollOffset
      val savedScrollPos: Double = listScroll.currentScrollPosition
      listChild.clearChildren()

      for (`element$iv` in ItemSearch.search$default(ItemSearch.INSTANCE, ItemSearch.INSTANCE.buildIndex(), searchField.text(), 0, 4, null)) {
         val item: Item = (`element$iv` as ItemSearchEntry).component1()
         val var18: ItemModelEditor = INSTANCE

         try {
            val id: Identifier = MCRegistryKt.registryId(item)
            val stack: ItemStack = MCRegistryKt.defaultStack$default(item, 0, 1, null)
            val var10000: java.lang.String = MCRegistryKt.displayName(stack).getString()
            val var33: Boolean = `$module`.currentItemId == id
            val var34: Function0 = { 
               build$rebuildItems(`$listScroll`, `$listChild`, `$searchField`, `$module`)
               Unit.INSTANCE
            }
            val collapsible: <unrepresentable> = object : VoidrixCollapsibleContainer {
               public open fun toggleExpansion() {
                  super.toggleExpansion()
                  if (this.expanded()) {
                     $module.currentItemId = id
                     $module.modelPreview = true
                  } else {
                     $module.currentItemId = null
                  }

                  rebuildRef()
               }
            }
            var18.styleCollapsibleV3(collapsible)
            val var35: ItemComponent = ItemComponent(stack)
            var35.sizing(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
            var35.margins(Insets.Companion.right(4))
            val var37: Any = CollectionsKt.firstOrNull(collapsible.getTitleLayout().children())
            val var36: FlowLayout = var37 as? FlowLayout
            if ((var37 as? FlowLayout) != null) {
               var36.child(0, var35 as UIComponent)
            }

            collapsible.getTitleLayout().child(var18.buildResetButton(var10000, { 
               `$module`.transformations.remove(`$id`)
               `$module`.saveTransformations()
               `$rebuildRef`()
               Unit.INSTANCE
            }) as UIComponent)
            if (var33) {
               var18.buildItemEditor(collapsible, `$module`, id, var34)
            }

            val var32: Any = Result.constructor_impl/* $VF was: constructor-impl */(listChild.child(collapsible as UIComponent))
         } catch (var30: java.lang.Throwable) {
            val `$this$build_u24rebuildItems_u24lambda_u2411_u24lambda_u2410`: Any = Result.constructor_impl/* $VF was: constructor-impl */(
               ResultKt.createFailure(var30)
            )
         }
      }

      listScroll.scrollOffset = savedScroll
      listScroll.currentScrollPosition = savedScrollPos
   }
}
