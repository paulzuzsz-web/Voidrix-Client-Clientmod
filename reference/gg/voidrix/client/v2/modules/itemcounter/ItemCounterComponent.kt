package gg.voidrix.client.v2.modules.itemcounter

import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.ui.components.voidrix.VoidrixItemComponent
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

public class ItemCounterComponent(dto: ItemCounterDto) : FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.VERTICAL) {
   public final val dto: ItemCounterDto
   public final val itemStack: ItemStack
   public final val itemComponent: gg.voidrix.client.v2.modules.itemcounter.ItemCounterComponent.ItemStackComponent
   private final var lastPaddingW: Int
   private final var lastPaddingH: Int

   init {
      this.dto = dto
      val var10001: java.lang.String = this.dto.itemId.toString()
      this.itemStack = MCRegistryKt.mcItemStack(var10001, 1)
      this.itemComponent = ItemCounterComponent.ItemStackComponent(this.itemStack, this.dto.itemId)
      this.lastPaddingW = this.dto.paddingWidth
      this.lastPaddingH = this.dto.paddingHeight
      this.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      this.child(this.itemComponent as UIComponent)
      this.allowOverflow(true)
      this.padding(Insets.Companion.vertical(this.dto.paddingHeight).withLeft(this.dto.paddingWidth).withRight(this.dto.paddingWidth))
   }

   public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      if (this.lastPaddingW != this.dto.paddingWidth || this.lastPaddingH != this.dto.paddingHeight) {
         this.lastPaddingW = this.dto.paddingWidth
         this.lastPaddingH = this.dto.paddingHeight
         this.padding(Insets.Companion.vertical(this.dto.paddingHeight).withLeft(this.dto.paddingWidth).withRight(this.dto.paddingWidth))
      }

      super.draw(context, mouseX, mouseY, partialTicks, delta)
   }

   @SourceDebugExtension(["SMAP\nItemCounterComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemCounterComponent.kt\ngg/voidrix/client/v2/modules/itemcounter/ItemCounterComponent$ItemStackComponent\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,98:1\n185#2:99\n40#2:100\n127#2:101\n40#2:102\n*S KotlinDebug\n*F\n+ 1 ItemCounterComponent.kt\ngg/voidrix/client/v2/modules/itemcounter/ItemCounterComponent$ItemStackComponent\n*L\n77#1:99\n77#1:100\n83#1:101\n83#1:102\n*E\n"])
   public inner class ItemStackComponent(stack: ItemStack, itemId: Identifier) : VoidrixItemComponent(stack) {
      private final val itemId: Identifier
      private final var cachedItemCount: String
      private final var cachedTextWidth: Int
      private final var lastUpdateTime: Long

      init {
         this.itemId = itemId
         this.cachedItemCount = "0"
         this.setShowOverlay(false)
         this.sizing(Sizing.Companion.fixed(16), Sizing.Companion.fixed(16))
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         super.draw(context, mouseX, mouseY, partialTicks, delta)
         val currentTime: Long = System.currentTimeMillis()
         if (currentTime - this.lastUpdateTime > 50L) {
            val var10001: ItemCounter = ItemCounter.INSTANCE
            val var10002: Minecraft = Minecraft.getInstance()
            this.cachedItemCount = java.lang.String.valueOf(var10001.countItems(var10002.player, this.itemId))
            this.cachedTextWidth = context.getTextWidth(this.cachedItemCount)
            this.lastUpdateTime = currentTime
         }

         val var12: Int = ItemCounterComponent.this.dto.multiColor.getChromaOrDefault().getRGB()
         val var10000: Minecraft = Minecraft.getInstance()
         val var14: Font = var10000.font
         context.push()
         context.translate(0.0F, 0.0F, 200.0F)
         context.drawString(
            var14, this.cachedItemCount, this.x + (double)19 - (double)2 - (double)this.cachedTextWidth, this.y + (double)6 + (double)3, var12, true
         )
         context.pop()
      }
   }
}
