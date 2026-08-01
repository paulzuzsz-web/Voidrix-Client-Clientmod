package gg.norisk.client.v2.modules.itemcounter

import gg.norisk.compat.serialization.IdentifierSerializer
import gg.norisk.owolib.owo.ui.core.Color
import gg.norisk.ui.api.hud.AnchorPoint
import gg.norisk.ui.api.serializable.MultiColor
import gg.norisk.ui.v2.hud.AnchorPointPosition
import gg.norisk.ui.v2.hud.Background
import gg.norisk.ui.v2.hud.Background.Type
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import net.minecraft.resources.Identifier

@Serializable
public data class ItemCounterDto(itemId: Identifier,
   scale: Float = 1.0F,
   background: Background = Background(null, null, 0.0F, 0.0F, 0.0F, null, 63, null),
   anchorPointPosition: AnchorPointPosition = AnchorPointPosition(AnchorPoint.MIDDLE_LEFT, 0.0, 0.5, null, 8, null),
   multiColor: MultiColor = MultiColor(null, false, false, 7, null),
   isEnabled: Boolean = true,
   paddingWidth: Int = 4,
   paddingHeight: Int = 4
) {
   @Serializable(with = IdentifierSerializer.class)
   public final val itemId: Identifier

   public final var scale: Float
      internal set

   public final var background: Background
      internal set

   public final var anchorPointPosition: AnchorPointPosition
      internal set

   public final var multiColor: MultiColor
      internal set

   public final var isEnabled: Boolean
      internal set

   public final var paddingWidth: Int
      internal set

   public final var paddingHeight: Int
      internal set

   @JvmField
   @JvmStatic
   private KSerializer<Object>[] $childSerializers = arrayOf(IdentifierSerializer(), null, null, null, null, null, null, null);

   init {
      this.itemId = itemId
      this.scale = scale
      this.background = background
      this.anchorPointPosition = anchorPointPosition
      this.multiColor = multiColor
      this.isEnabled = isEnabled
      this.paddingWidth = paddingWidth
      this.paddingHeight = paddingHeight
   }

   public fun reset() {
      this.scale = 1.0F
      this.background.setType(Type.VANILLA)
      this.background.setColor(Background(null, null, 0.0F, 0.0F, 0.0F, null, 63, null).getColor())
      this.multiColor.setColor(Color.WHITE)
      this.multiColor.setRainbow(false)
      this.multiColor.setPositionalRainbow(false)
      this.isEnabled = true
      this.paddingWidth = 4
      this.paddingHeight = 4
   }

   public operator fun component1(): Identifier {
      return this.itemId
   }

   public operator fun component2(): Float {
      return this.scale
   }

   public operator fun component3(): Background {
      return this.background
   }

   public operator fun component4(): AnchorPointPosition {
      return this.anchorPointPosition
   }

   public operator fun component5(): MultiColor {
      return this.multiColor
   }

   public operator fun component6(): Boolean {
      return this.isEnabled
   }

   public operator fun component7(): Int {
      return this.paddingWidth
   }

   public operator fun component8(): Int {
      return this.paddingHeight
   }

   public fun copy(
      itemId: Identifier = this.itemId,
      scale: Float = this.scale,
      background: Background = this.background,
      anchorPointPosition: AnchorPointPosition = this.anchorPointPosition,
      multiColor: MultiColor = this.multiColor,
      isEnabled: Boolean = this.isEnabled,
      paddingWidth: Int = this.paddingWidth,
      paddingHeight: Int = this.paddingHeight
   ): ItemCounterDto {
      return ItemCounterDto(itemId, scale, background, anchorPointPosition, multiColor, isEnabled, paddingWidth, paddingHeight)
   }

   public override fun toString(): String {
      return "ItemCounterDto(itemId=${this.itemId}, scale=${this.scale}, background=${this.background}, anchorPointPosition=${this.anchorPointPosition}, multiColor=${this.multiColor}, isEnabled=${this.isEnabled}, paddingWidth=${this.paddingWidth}, paddingHeight=${this.paddingHeight})"
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          ((this.itemId.hashCode() * 31 + java.lang.Float.hashCode(this.scale)) * 31 + this.background.hashCode()) * 31
                                             + this.anchorPointPosition.hashCode()
                                       )
                                       * 31
                                    + this.multiColor.hashCode()
                              )
                              * 31
                           + java.lang.Boolean.hashCode(this.isEnabled)
                     )
                     * 31
                  + Integer.hashCode(this.paddingWidth)
            )
            * 31
         + Integer.hashCode(this.paddingHeight)
      }

   public override operator fun equals(other: Any?): Boolean {
      label64@
      if (this === other) {
         return true
      } else {
         return other is ItemCounterDto
            && this.itemId == (other as ItemCounterDto).itemId
            && java.lang.Float.compare(this.scale, (other as ItemCounterDto).scale) == 0
            && this.background == (other as ItemCounterDto).background
            && this.anchorPointPosition == (other as ItemCounterDto).anchorPointPosition
            && this.multiColor == (other as ItemCounterDto).multiColor
            && this.isEnabled == (other as ItemCounterDto).isEnabled
            && this.paddingWidth == (other as ItemCounterDto).paddingWidth
            && this.paddingHeight == (other as ItemCounterDto).paddingHeight
         }
   }

   public companion object {
      public fun serializer(): KSerializer<ItemCounterDto> {
         return ItemCounterDto.$serializer.INSTANCE as KSerializer<ItemCounterDto>
      }
   }
}
