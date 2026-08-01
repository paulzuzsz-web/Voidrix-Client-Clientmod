package gg.voidrix.client.v2.modules.itemcounter

import gg.voidrix.compat.client.MCInventory
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.serialization.IdentifierSerializer
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.api.hud.AbstractHud
import gg.voidrix.ui.api.hud.IMultiDraggableElements
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.modules.DraggableElement
import gg.voidrix.ui.components.modules.DraggableModuleEditWrapper
import gg.voidrix.ui.modules.IModuleScreen
import gg.voidrix.ui.utils.OwoLibExtensionsKt
import gg.voidrix.ui.v2.hud.AnchorPointPosition
import java.util.ArrayList
import java.util.LinkedHashMap
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.resources.Identifier
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nItemCounter.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemCounter.kt\ngg/voidrix/client/v2/modules/itemcounter/ItemCounter\n+ 2 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 3 OwoLibExtensions.kt\ngg/voidrix/ui/utils/OwoLibExtensionsKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 6 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,111:1\n126#2:112\n153#2,3:113\n153#3,3:116\n165#3,3:130\n808#4,11:119\n808#4,11:133\n295#4,2:144\n328#5:146\n40#5:147\n328#5:149\n40#5:150\n1#6:148\n*S KotlinDebug\n*F\n+ 1 ItemCounter.kt\ngg/voidrix/client/v2/modules/itemcounter/ItemCounter\n*L\n37#1:112\n37#1:113,3\n89#1:116,3\n97#1:130,3\n89#1:119,11\n97#1:133,11\n97#1:144,2\n103#1:146\n103#1:147\n53#1:149\n53#1:150\n*E\n"])
public object ItemCounter : AbstractHud("Item Counter", false, false, false, 14), IMultiDraggableElements {
   private final val icLogger: Logger = MCLogger.getLogger("ItemCounter")

   public final var pendingExpandItemId: Identifier?
      internal set

   public final var items: MutableMap<Identifier, ItemCounterDto> by ValueApiKt.map$default({ 
         LinkedHashMap() as java.util.Map
      }, IdentifierSerializer() as KSerializer, ItemCounterDto.Companion.serializer(), null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return items$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MutableMap<Identifier, ItemCounterDto>
      }

      public final set(<set-?>) {
         items$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public open fun hudComponent(): UIComponent? {
      return null
   }

   public open fun getDraggableElements(): List<DraggableElement> {
      icLogger.info("getDraggableElements called, items.size=${this.items.size()}, keys=${this.items.keySet()}")
      val `$this$map$iv`: java.util.Map = this.items
      val `destination$iv$iv`: java.util.Collection = ArrayList(`$this$map$iv`.size())

      for (`item$iv$iv` in `$this$map$iv`.entrySet()) {
         `destination$iv$iv`.add(INSTANCE.toDraggableElement(`item$iv$iv`.getValue() as ItemCounterDto))
      }

      return `destination$iv$iv` as MutableList<DraggableElement>
   }

   private fun ItemCounterDto.toDraggableElement(): DraggableElement {
      val var10000: java.lang.String = `$this$toDraggableElement`.itemId.toString()
      return DraggableElement(
         var10000,
         { 
            `$this_toDraggableElement`.scale
         },
         { it: Float ->
            `$this_toDraggableElement`.scale = RangesKt.coerceIn(it, 0.5F, 3.0F)
            Unit.INSTANCE
         },
         { 
            `$this_toDraggableElement`.anchorPointPosition
         },
         { it: AnchorPointPosition ->
            `$this_toDraggableElement`.anchorPointPosition = it
            Unit.INSTANCE
         },
         { 
            val var10000: UIComponent
            if (`$this_toDraggableElement`.isEnabled) {
               var `$this$toDraggableElement_u24lambda_u247_u24lambda_u246`: Any
               try {
                  `$this$toDraggableElement_u24lambda_u247_u24lambda_u246` = Result.constructor_impl/* $VF was: constructor-impl */(
                     ItemCounterComponent(`$this_toDraggableElement`)
                  )
               } catch (var4: java.lang.Throwable) {
                  `$this$toDraggableElement_u24lambda_u247_u24lambda_u246` = Result.constructor_impl/* $VF was: constructor-impl */(
                     ResultKt.createFailure(var4)
                  )
               }

               var10000 = (
                  if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$toDraggableElement_u24lambda_u247_u24lambda_u246`))
                     null
                     else
                     `$this$toDraggableElement_u24lambda_u247_u24lambda_u246`
               ) as UIComponent
            } else {
               var10000 = null
            }

            var10000
         },
         { var1: DraggableModuleEditWrapper ->
            pendingExpandItemId = `$this_toDraggableElement`.itemId
            val var10000: Minecraft = Minecraft.getInstance()
            val screen: Screen = var10000.gui.screen()
            if (screen is IModuleScreen) {
               (screen as IModuleScreen).openSettingsForModule(INSTANCE.getInternalKey())
            }

            Unit.INSTANCE
         },
         { it: DraggableModuleEditWrapper ->
            UISounds.playButtonSound()
            `$this_toDraggableElement`.isEnabled = !`$this_toDraggableElement`.isEnabled
            Unit.INSTANCE
         },
         null,
         null,
         { 
            `$this_toDraggableElement`.background.toSurface()
         },
         768,
         null
      )
   }

   public fun countItems(player: LocalPlayer?, itemId: Identifier): Int {
      return if (player == null) 0 else MCInventory.INSTANCE.countItem(player, itemId)
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         settingsPanel.clearChildren()
         settingsPanel.child(ItemCounterSearchComponent(null, null, 3, null) as UIComponent)
         val var1: ItemCounterListComponent = ItemCounterListComponent()
         var1.build(CollectionsKt.toList(INSTANCE.items.values()))
         settingsPanel.child(var1 as UIComponent)
         pendingExpandItemId = null
         Unit.INSTANCE
      }
   }

   public fun add(identifier: Identifier, root: ParentUIComponent) {
      UISounds.playButtonSound()
      this.items.put(identifier, ItemCounterDto(identifier, 0.0F, null, null, null, false, 0, 0, 254, null))
      val `array$iv`: ArrayList = ArrayList()
      root.collectDescendants(`array$iv`)
      val `$this$filterIsInstanceTo$iv$iv$iv`: java.lang.Iterable = `array$iv`
      val `destination$iv$iv$iv`: java.util.Collection = ArrayList()

      for (`element$iv$iv$iv` in `$this$filterIsInstanceTo$iv$iv$iv`) {
         if (`element$iv$iv$iv` is ItemCounterListComponent) {
            `destination$iv$iv$iv`.add(`element$iv$iv$iv`)
         }
      }

      val var10000: ItemCounterListComponent = CollectionsKt.firstOrNull(`destination$iv$iv$iv` as java.util.List) as ItemCounterListComponent
      if (var10000 != null) {
         val var10001: ItemCounterDto = this.items.get(identifier)
         if (var10001 == null) {
            return
         }

         var10000.add(var10001)
      }

      this.rebuildScreenDraggables()
   }

   public fun remove(dto: ItemCounterDto, root: ParentUIComponent) {
      UISounds.playButtonSound()
      dto.isEnabled = false
      this.items.remove(dto.itemId)
      var var10000: UIComponent = dto.itemId.toString()
      val `id$iv`: java.lang.String = var10000
      val `array$iv`: ArrayList = ArrayList()
      root.collectDescendants(`array$iv`)
      val `$this$filterIsInstanceTo$iv$iv$iv`: java.lang.Iterable = `array$iv`
      val `element$iv$iv`: java.util.Collection = ArrayList()

      for (`element$iv$iv$iv` in `$this$filterIsInstanceTo$iv$iv$iv`) {
         if (`element$iv$iv$iv` is ItemCounterListComponent.ItemCounterListEntry) {
            `element$iv$iv`.add(`element$iv$iv$iv`)
         }
      }

      val var17: java.util.Iterator = (`element$iv$iv` as java.util.List).iterator()

      while (true) {
         if (var17.hasNext()) {
            val var18: Any = var17.next()
            if (!((var18 as UIComponent).id() == `id$iv`)) {
               continue
            }

            var10000 = (UIComponent)var18
            break
         }

         var10000 = null
         break
      }

      val var3: ItemCounterListComponent.ItemCounterListEntry = var10000 as ItemCounterListComponent.ItemCounterListEntry
      if (var10000 as ItemCounterListComponent.ItemCounterListEntry != null) {
         OwoLibExtensionsKt.removeFromParent(var3 as UIComponent)
      }

      this.rebuildScreenDraggables()
   }

   private fun rebuildScreenDraggables() {
      val var10000: Minecraft = Minecraft.getInstance()
      val screen: Screen = var10000.gui.screen()
      if (screen is IModuleScreen) {
         (screen as IModuleScreen).rebuildDraggableHuds()
      }
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }
}
