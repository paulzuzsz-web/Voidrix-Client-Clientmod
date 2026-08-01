package gg.norisk.client.v2.waypoints

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
public data class WorldConfig(defaultMultiworldId: String? = null) {
   public final var defaultMultiworldId: String?
      internal set

   init {
      this.defaultMultiworldId = defaultMultiworldId
   }

   public operator fun component1(): String? {
      return this.defaultMultiworldId
   }

   public fun copy(defaultMultiworldId: String? = this.defaultMultiworldId): WorldConfig {
      return WorldConfig(defaultMultiworldId)
   }

   public override fun toString(): String {
      return "WorldConfig(defaultMultiworldId=${this.defaultMultiworldId})"
   }

   public override fun hashCode(): Int {
      return if (this.defaultMultiworldId == null) 0 else this.defaultMultiworldId.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label22@
      if (this === other) {
         return true
      } else {
         return other is WorldConfig && this.defaultMultiworldId == (other as WorldConfig).defaultMultiworldId
      }
   }

   fun WorldConfig() {
      this(null, 1, null)
   }

   public companion object {
      public fun serializer(): KSerializer<WorldConfig> {
         return WorldConfig.$serializer.INSTANCE as KSerializer<WorldConfig>
      }
   }
}
