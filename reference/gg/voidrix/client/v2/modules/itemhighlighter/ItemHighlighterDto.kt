package gg.voidrix.client.v2.modules.itemhighlighter

import gg.voidrix.compat.serialization.IdentifierSerializer
import gg.voidrix.ui.api.serializable.MultiColor
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import net.minecraft.resources.Identifier

@Serializable
public data class ItemHighlighterDto(itemId: Identifier,
   isEnabled: Boolean = true,
   mode: HighlightMode = HighlightMode.SHINY,
   highlightColor: MultiColor = MultiColor(null, false, false, 7, null)
) {
   @Serializable(with = IdentifierSerializer.class)
   public final val itemId: Identifier

   public final var isEnabled: Boolean
      internal set

   public final var mode: HighlightMode
      internal set

   public final var highlightColor: MultiColor
      internal set

   @JvmField
   @JvmStatic
   private KSerializer<Object>[] $childSerializers = arrayOf(IdentifierSerializer(), null, HighlightMode.Companion.serializer(), null);

   init {
      this.itemId = itemId
      this.isEnabled = isEnabled
      this.mode = mode
      this.highlightColor = highlightColor
   }

   public operator fun component1(): Identifier {
      return this.itemId
   }

   public operator fun component2(): Boolean {
      return this.isEnabled
   }

   public operator fun component3(): HighlightMode {
      return this.mode
   }

   public operator fun component4(): MultiColor {
      return this.highlightColor
   }

   public fun copy(
      itemId: Identifier = this.itemId,
      isEnabled: Boolean = this.isEnabled,
      mode: HighlightMode = this.mode,
      highlightColor: MultiColor = this.highlightColor
   ): ItemHighlighterDto {
      return ItemHighlighterDto(itemId, isEnabled, mode, highlightColor)
   }

   public override fun toString(): String {
      return "ItemHighlighterDto(itemId=${this.itemId}, isEnabled=${this.isEnabled}, mode=${this.mode}, highlightColor=${this.highlightColor})"
   }

   public override fun hashCode(): Int {
      return ((this.itemId.hashCode() * 31 + java.lang.Boolean.hashCode(this.isEnabled)) * 31 + this.mode.hashCode()) * 31 + this.highlightColor.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label40@
      if (this === other) {
         return true
      } else {
         return other is ItemHighlighterDto
            && this.itemId == (other as ItemHighlighterDto).itemId
            && this.isEnabled == (other as ItemHighlighterDto).isEnabled
            && this.mode === (other as ItemHighlighterDto).mode
            && this.highlightColor == (other as ItemHighlighterDto).highlightColor
         }
   }

   public companion object {
      public fun serializer(): KSerializer<ItemHighlighterDto> {
         return ItemHighlighterDto.$serializer.INSTANCE as KSerializer<ItemHighlighterDto>
      }
   }
}
