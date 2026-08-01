package gg.norisk.client.v2.plugin

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.StringSerializer

@Serializable
public data class MixinCancellerConfig(cancelledMixinPackages: List<String> = CollectionsKt.emptyList(),
   cancelledMixinClassNames: List<String> = CollectionsKt.emptyList()
) {
   public final val cancelledMixinPackages: List<String>
   public final val cancelledMixinClassNames: List<String>
   @JvmField
   @JvmStatic
   private KSerializer<Object>[] $childSerializers = arrayOf(
      ArrayListSerializer(StringSerializer.INSTANCE as KSerializer),
      ArrayListSerializer(StringSerializer.INSTANCE as KSerializer)
   );

   init {
      this.cancelledMixinPackages = cancelledMixinPackages
      this.cancelledMixinClassNames = cancelledMixinClassNames
   }

   public operator fun component1(): List<String> {
      return this.cancelledMixinPackages
   }

   public operator fun component2(): List<String> {
      return this.cancelledMixinClassNames
   }

   public fun copy(cancelledMixinPackages: List<String> = this.cancelledMixinPackages, cancelledMixinClassNames: List<String> = this.cancelledMixinClassNames): MixinCancellerConfig {
      return MixinCancellerConfig(cancelledMixinPackages, cancelledMixinClassNames)
   }

   public override fun toString(): String {
      return "MixinCancellerConfig(cancelledMixinPackages=${this.cancelledMixinPackages}, cancelledMixinClassNames=${this.cancelledMixinClassNames})"
   }

   public override fun hashCode(): Int {
      return this.cancelledMixinPackages.hashCode() * 31 + this.cancelledMixinClassNames.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label28@
      if (this === other) {
         return true
      } else {
         return other is MixinCancellerConfig
            && this.cancelledMixinPackages == (other as MixinCancellerConfig).cancelledMixinPackages
            && this.cancelledMixinClassNames == (other as MixinCancellerConfig).cancelledMixinClassNames
         }
   }

   fun MixinCancellerConfig() {
      this(null, null, 3, null)
   }

   public companion object {
      public fun serializer(): KSerializer<MixinCancellerConfig> {
         return MixinCancellerConfig.$serializer.INSTANCE as KSerializer<MixinCancellerConfig>
      }
   }
}
