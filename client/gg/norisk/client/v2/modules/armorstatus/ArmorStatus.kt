package gg.norisk.client.v2.modules.armorstatus

import gg.norisk.compat.client.EquipmentSlotCompat
import gg.norisk.compat.client.MCInventory
import gg.norisk.compat.client.MCRegistryKt
import gg.norisk.compat.text.RainbowTextUtilsKt
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.Color
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.AbstractHud
import gg.norisk.ui.api.hud.AnchorPoint
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.IContentBackground
import gg.norisk.ui.api.hud.IDynamicBackground
import gg.norisk.ui.api.hud.IDynamicBackgroundKt
import gg.norisk.ui.api.serializable.MultiColor
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.components.nrc.NrcBackgroundPicker
import gg.norisk.ui.components.nrc.NrcItemComponent
import gg.norisk.ui.components.nrc.NrcMultiColorPicker
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.v2.hud.AnchorPointPosition
import gg.norisk.ui.v2.hud.Background
import gg.norisk.ui.v2.hud.Background.Type
import java.awt.geom.Point2D
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Map.Entry
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nArmorStatus.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,324:1\n185#2:325\n40#2:326\n2746#3,3:327\n*S KotlinDebug\n*F\n+ 1 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus\n*L\n116#1:325\n116#1:326\n117#1:327,3\n*E\n"])
public object ArmorStatus : AbstractHud("ArmorStatus", false, false, false, 14), IContentBackground {
   @Category(name = "Display")
   @NotNull
   public open var background: Background by ValueApiKt.attribute$default({ 
      Background(Type.BLANK, null, 0.0F, 0.0F, 0.0F, null, 62, null)
   }, null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      NrcBackgroundPicker(INSTANCE.background, null) as UIComponent
   }, 14, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public open get() {
         return background$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as Background
      }

      public open set(<set-?>) {
         background$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Layout")
   @NotNull
   public final var vertical: Boolean by ValueApiKt.boolean$default(true, "Vertical", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return vertical$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         vertical$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   @Category(name = "Layout")
   @NotNull
   public final var leftText: Boolean by ValueApiKt.boolean$default(true, "Left Text", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return leftText$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         leftText$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   @Category(name = "Layout")
   @NotNull
   public final var showInPercentage: Boolean by ValueApiKt.boolean$default(false, "Show in Percentage", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return showInPercentage$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showInPercentage$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   @Category(name = "Armor")
   @NotNull
   public final var showHelmet: Boolean by ValueApiKt.boolean$default(true, "Show Helmet", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return showHelmet$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showHelmet$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   @Category(name = "Armor")
   @NotNull
   public final var showChestplate: Boolean by ValueApiKt.boolean$default(true, "Show Chestplate", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return showChestplate$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showChestplate$delegate.setValue(this as ValueHolder, $$delegatedProperties[6], var1)
      }


   @Category(name = "Armor")
   @NotNull
   public final var showLeggings: Boolean by ValueApiKt.boolean$default(true, "Show Leggings", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return showLeggings$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showLeggings$delegate.setValue(this as ValueHolder, $$delegatedProperties[7], var1)
      }


   @Category(name = "Armor")
   @NotNull
   public final var showBoots: Boolean by ValueApiKt.boolean$default(true, "Show Boots", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return showBoots$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showBoots$delegate.setValue(this as ValueHolder, $$delegatedProperties[8], var1)
      }


   @Category(name = "Armor")
   @NotNull
   public final var showHeldItem: Boolean by ValueApiKt.boolean$default(false, "Show Held Item", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
         public final get() {
         return showHeldItem$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showHeldItem$delegate.setValue(this as ValueHolder, $$delegatedProperties[9], var1)
      }


   @Category(name = "Armor")
   @NotNull
   public final var showOffHandItem: Boolean by ValueApiKt.boolean$default(false, "Show Off Hand Item", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
         public final get() {
         return showOffHandItem$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showOffHandItem$delegate.setValue(this as ValueHolder, $$delegatedProperties[10], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var showItemDamage: Boolean by ValueApiKt.boolean$default(true, "Show Item Damage", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[11])
         public final get() {
         return showItemDamage$delegate.getValue(this as ValueHolder, $$delegatedProperties[11]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showItemDamage$delegate.setValue(this as ValueHolder, $$delegatedProperties[11], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var showDamageOverlay: Boolean by ValueApiKt.boolean$default(true, "Damage Overlay", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[12])
         public final get() {
         return showDamageOverlay$delegate.getValue(this as ValueHolder, $$delegatedProperties[12]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showDamageOverlay$delegate.setValue(this as ValueHolder, $$delegatedProperties[12], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var showDamageColor: Boolean by ValueApiKt.boolean$default(true, "Damage Color", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[13])
         public final get() {
         return showDamageColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[13]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showDamageColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[13], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var multiColor: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(null, false, false, 7, null)
   }, MultiColor.Companion.serializer(), "Color", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      NrcMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[14])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[14]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[14], var1)
      }


   private final val slotMap: Map<EquipmentSlotCompat, () -> Boolean> = MapsKt.linkedMapOf(arrayOf(TuplesKt.to(EquipmentSlotCompat.MAINHAND, { 
      INSTANCE.showHeldItem
   }), TuplesKt.to(EquipmentSlotCompat.HEAD, { 
      INSTANCE.showHelmet
   }), TuplesKt.to(EquipmentSlotCompat.CHEST, { 
      INSTANCE.showChestplate
   }), TuplesKt.to(EquipmentSlotCompat.LEGS, { 
      INSTANCE.showLeggings
   }), TuplesKt.to(EquipmentSlotCompat.FEET, { 
      INSTANCE.showBoots
   }), TuplesKt.to(EquipmentSlotCompat.OFFHAND, { 
      INSTANCE.showOffHandItem
   }))) as java.util.Map

   private final val exampleSlots: Map<EquipmentSlotCompat, String> =
      MapsKt.mapOf(
         arrayOf(
            TuplesKt.to(EquipmentSlotCompat.MAINHAND, "iron_sword"),
            TuplesKt.to(EquipmentSlotCompat.HEAD, "iron_helmet"),
            TuplesKt.to(EquipmentSlotCompat.CHEST, "iron_chestplate"),
            TuplesKt.to(EquipmentSlotCompat.LEGS, "iron_leggings"),
            TuplesKt.to(EquipmentSlotCompat.FEET, "iron_boots"),
            TuplesKt.to(EquipmentSlotCompat.OFFHAND, "shield")
         )
      )

   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.BOTTOM_RIGHT, 1.0, 1.0, null, 8, null)
         }
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 4, 4, 0, 0, 24, null)
   }

   public open fun shouldStopRenderExecution(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player == null) {
         return true
      } else {
         val player: LocalPlayer = var10000.player
         val `$this$none$iv`: java.lang.Iterable = slotMap.entrySet()
         var var13: Boolean
         if (`$this$none$iv` is java.util.Collection && (`$this$none$iv` as java.util.Collection).isEmpty()) {
            var13 = true
         } else {
            val var11: java.util.Iterator = `$this$none$iv`.iterator()

            while (true) {
               if (!var11.hasNext()) {
                  var13 = true
                  break
               }

               val var6: Entry = var11.next() as Entry
               if ((var6.getValue() as Function0)() as java.lang.Boolean
                  && MCInventory.INSTANCE.getEquipmentStack(player, var6.getKey() as EquipmentSlotCompat) != null) {
                  var13 = false
                  break
               }
            }
         }

         return var13
      }
   }

   public open fun hudComponent(): UIComponent {
      return ArmorStatus.ArmorStatusContainer() as UIComponent
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @SourceDebugExtension(["SMAP\nArmorStatus.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus$ArmorStatusContainer\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,324:1\n185#2:325\n40#2:326\n328#2:327\n40#2:328\n185#2:330\n40#2:331\n1#3:329\n*S KotlinDebug\n*F\n+ 1 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus$ArmorStatusContainer\n*L\n142#1:325\n142#1:326\n143#1:327\n143#1:328\n190#1:330\n190#1:331\n*E\n"])
   private class ArmorStatusContainer : FlowLayout(
         Sizing.Companion.content(), Sizing.Companion.content(), if (ArmorStatus.INSTANCE.vertical) Algorithm.VERTICAL else Algorithm.HORIZONTAL
      ) {
      private final val currentItems: MutableMap<EquipmentSlotCompat, ItemStack?> = LinkedHashMap() as java.util.Map

      init {
         this.allowOverflow(true)
         this.rebuildSlots()
      }

      private fun rebuildSlots() {
         this.clearChildren()
         this.currentItems.clear()
         var var10000: Minecraft = Minecraft.getInstance()
         val player: LocalPlayer = var10000.player
         var10000 = Minecraft.getInstance()
         val var16: Boolean = var10000.gui.screen() is IModuleScreen
         val var18: java.util.List = ArrayList()

         for (bg in ArmorStatus.slotMap.entrySet()) {
            val hSizing: EquipmentSlotCompat = bg.getKey() as EquipmentSlotCompat
            if ((bg.getValue() as Function0)() as java.lang.Boolean) {
               val innerWrapper: ItemStack = if (player != null) MCInventory.INSTANCE.getEquipmentStack(player, hSizing) else null
               this.currentItems.put(hSizing, if (innerWrapper != null) MCRegistryKt.copyStack(innerWrapper) else null)
               if (innerWrapper != null) {
                  var18.add(TuplesKt.to(hSizing, innerWrapper))
               } else if (var16) {
                  val var32: java.lang.String = ArmorStatus.exampleSlots.get(hSizing)
                  if (var32 != null) {
                     val exampleId: java.lang.String = var32
                     val var12: ArmorStatus.ArmorStatusContainer = this

                     var stack: ArmorStatus.ArmorStatusContainer
                     try {
                        stack = var12
                        stack = (ArmorStatus.ArmorStatusContainer)Result.constructor_impl/* $VF was: constructor-impl */(
                           MCRegistryKt.mcItemStack$default(exampleId, 0, 2, null)
                        )
                     } catch (var15: java.lang.Throwable) {
                        stack = (ArmorStatus.ArmorStatusContainer)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var15))
                     }

                     val var33: ItemStack = (if (Result.isFailure_impl/* $VF was: isFailure-impl */(stack)) null else stack) as ItemStack
                     if (var33 != null) {
                        var18.add(TuplesKt.to(hSizing, var33))
                     }
                  }
               }
            }
         }

         CollectionsKt.sortWith(var18, ArmorStatus$ArmorStatusContainer$rebuildSlots$$inlined$compareBy$1())
         CollectionsKt.reverse(var18)
         val var20: Boolean = !var18.isEmpty()
         if (var20 || var16) {
            val var21: DynamicBackground = ArmorStatus.INSTANCE.dynamicBackground
            val var25: FlowLayout = FlowLayout(
               if (var21.isDynamic()) Sizing.Companion.content(var21.getDynamicWidth()) else Sizing.Companion.fixed(var21.getStaticWidth()),
               if (var21.isDynamic()) Sizing.Companion.content(var21.getDynamicHeight()) else Sizing.Companion.fixed(var21.getStaticHeight()),
               if (ArmorStatus.INSTANCE.vertical) Algorithm.VERTICAL else Algorithm.HORIZONTAL
            )
            val var26: FlowLayout = var25
            var25.gap(2)
            var25.allowOverflow(true)
            var25.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            val var28: java.util.Iterator = var18.iterator()

            while (var28.hasNext()) {
               var26.child(ArmorStatus.ItemShowcaseComponent((var28.next() as Pair).component2() as ItemStack) as UIComponent)
            }

            var26.surface(if (!var20 && !var16) Surface.BLANK else ArmorStatus.INSTANCE.background.toSurface())
            this.child(var25 as UIComponent)
         }
      }

      protected open fun parentUpdate(delta: Float, mouseX: Int, mouseY: Int) {
         super.parentUpdate(delta, mouseX, mouseY)
         val var10000: Minecraft = Minecraft.getInstance()
         if (var10000.player != null) {
            val player: LocalPlayer = var10000.player
            var changed: Boolean = false

            for (var13 in ArmorStatus.slotMap.entrySet()) {
               val slot: EquipmentSlotCompat = var13.getKey() as EquipmentSlotCompat
               if ((var13.getValue() as Function0)() as java.lang.Boolean
                  && !MCRegistryKt.stacksMatch(MCInventory.INSTANCE.getEquipmentStack(player, slot), this.currentItems.get(slot))) {
                  changed = true
                  break
               }
            }

            if (changed) {
               this.rebuildSlots()
            }
         }
      }
   }

   @SourceDebugExtension(["SMAP\nArmorStatus.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus$ItemShowcaseComponent\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 Text.kt\ngg/norisk/compat/text/TextKt\n+ 4 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n+ 5 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,324:1\n127#2:325\n40#2:326\n66#3:327\n66#3:329\n66#3:330\n942#4:328\n808#5,11:331\n*S KotlinDebug\n*F\n+ 1 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus$ItemShowcaseComponent\n*L\n222#1:325\n222#1:326\n223#1:327\n243#1:329\n269#1:330\n223#1:328\n296#1:331,11\n*E\n"])
   private class ItemShowcaseComponent(itemStack: ItemStack) : FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.HORIZONTAL) {
      private final val itemStack: ItemStack
      private final val itemComponent: NrcItemComponent
      private final val durabilityLabel: LabelComponent

      init {
         this.itemStack = itemStack
         val var2: NrcItemComponent = NrcItemComponent(this.itemStack)
         var2.showOverlay(ArmorStatus.INSTANCE.showDamageOverlay)
         var2.sizing(Sizing.Companion.fixed(16), Sizing.Companion.fixed(16))
         this.itemComponent = var2
         val var13: LabelComponent = LabelComponent(this.getDurabilityText())
         var13.shadow(true)
         var13.setAutoTextSupplier({ 
            `this$0`.getDurabilityText()
         })
         if (ArmorStatus.INSTANCE.vertical) {
            val var10000: Minecraft = Minecraft.getInstance()
            val var20: Font = var10000.font
            val var21: MutableComponent = Component.literal("100%")
            var13.horizontalSizing(Sizing.Companion.fixed(var20.width((var21 as Component) as FormattedText)))
            if (ArmorStatus.INSTANCE.leftText) {
               var13.horizontalTextAlignment(HorizontalAlignment.RIGHT)
            } else {
               var13.horizontalTextAlignment(HorizontalAlignment.LEFT)
            }
         }

         this.durabilityLabel = var13
         this.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
         this.handleLayout()
         this.gap(2)
         this.allowOverflow(true)
      }

      private fun getDurabilityText(): Component {
         if (ArmorStatus.INSTANCE.showItemDamage && MCRegistryKt.isDamageable(this.itemStack)) {
            val var6: Int = MCRegistryKt.maxDamage(this.itemStack)
            val remaining: Int = var6 - MCRegistryKt.damageValue(this.itemStack)
            val var10: java.lang.String = if (ArmorStatus.INSTANCE.showInPercentage)
               "${if (var6 > 0) (int)((double)remaining / var6 * 100) else 100}%"
               else
               java.lang.String.valueOf(remaining)
               val var11: Component
            if (ArmorStatus.INSTANCE.showDamageColor && !ArmorStatus.INSTANCE.multiColor.isRainbow()) {
               val var12: MutableComponent = Component.literal(var10)
               var11 = var12 as Component
            } else {
               val var8: Point2D = AnchorPointPosition.toGlobalPos$default(ArmorStatus.INSTANCE.getAnchorPosition(), 0, 0, 3, null)
               var11 = RainbowTextUtilsKt.rainbowText(
                  var10,
                  ArmorStatus.INSTANCE.multiColor.isRainbow(),
                  ArmorStatus.INSTANCE.multiColor.isPositionalRainbow(),
                  ArmorStatus.INSTANCE.multiColor.getChromaOrDefault().getRGB(),
                  (int)var8.getX(),
                  (int)var8.getY()
               ) as Component
            }

            return var11
         } else {
            val var10000: MutableComponent = Component.literal("")
            return var10000 as Component
         }
      }

      private fun applyLabel() {
         if (ArmorStatus.INSTANCE.showItemDamage) {
            this.child(this.durabilityLabel as UIComponent)
            if (MCRegistryKt.isDamageable(this.itemStack)) {
               this.durabilityLabel.text(this.getDurabilityText())
            }
         }
      }

      private fun handleLayout() {
         this.clearChildren()
         if (ArmorStatus.INSTANCE.leftText) {
            this.applyLabel()
         }

         this.child(this.itemComponent as UIComponent)
         if (!ArmorStatus.INSTANCE.leftText) {
            this.applyLabel()
         }
      }

      protected open fun parentUpdate(delta: Float, mouseX: Int, mouseY: Int) {
         super.parentUpdate(delta, mouseX, mouseY)
         this.updateLabelColor()
      }

      private fun updateLabelColor() {
         val remaining: java.lang.Iterable = this.children()
         val percent: java.util.Collection = ArrayList()

         for (`element$iv$iv` in remaining) {
            if (`element$iv$iv` is LabelComponent) {
               percent.add(`element$iv$iv`)
            }
         }

         val var10000: LabelComponent = CollectionsKt.firstOrNull(percent as java.util.List) as LabelComponent
         if (var10000 != null) {
            val var17: Color
            if (ArmorStatus.INSTANCE.showDamageColor && !ArmorStatus.INSTANCE.multiColor.isRainbow() && MCRegistryKt.isDamageable(this.itemStack)) {
               val var12: Int = MCRegistryKt.maxDamage(this.itemStack)
               var17 = if ((if (var12 > 0) (double)(var12 - MCRegistryKt.damageValue(this.itemStack)) / var12 else 1.0) < 0.8)
                  Color.Companion.ofArgb(MCRegistryKt.durabilityBarColor(this.itemStack) or -16777216)
                  else
                  Color.WHITE
               } else {
               var17 = if (ArmorStatus.INSTANCE.multiColor.isRainbow())
                  Color.Companion.ofArgb(ArmorStatus.INSTANCE.multiColor.getChromaOrDefault().getRGB())
                  else
                  Color.Companion.ofArgb(ArmorStatus.INSTANCE.multiColor.getChromaOrDefault().getRGB())
               }

            var10000.color(var17)
         }
      }
   }
}
