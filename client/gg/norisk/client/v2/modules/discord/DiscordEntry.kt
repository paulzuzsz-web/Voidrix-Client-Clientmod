package gg.norisk.client.v2.modules.discord

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
public data class DiscordEntry(source: String, state: String, details: String? = null, timestamp: Long) {
   public final val source: String
   public final val state: String
   public final val details: String?
   public final val timestamp: Long

   init {
      this.source = source
      this.state = state
      this.details = details
      this.timestamp = timestamp
   }

   public operator fun component1(): String {
      return this.source
   }

   public operator fun component2(): String {
      return this.state
   }

   public operator fun component3(): String? {
      return this.details
   }

   public operator fun component4(): Long {
      return this.timestamp
   }

   public fun copy(source: String = this.source, state: String = this.state, details: String? = this.details, timestamp: Long = this.timestamp): DiscordEntry {
      return DiscordEntry(source, state, details, timestamp)
   }

   public override fun toString(): String {
      return "DiscordEntry(source=${this.source}, state=${this.state}, details=${this.details}, timestamp=${this.timestamp})"
   }

   public override fun hashCode(): Int {
      return ((this.source.hashCode() * 31 + this.state.hashCode()) * 31 + (if (this.details == null) 0 else this.details.hashCode())) * 31
         + java.lang.Long.hashCode(this.timestamp)
      }

   public override operator fun equals(other: Any?): Boolean {
      label40@
      if (this === other) {
         return true
      } else {
         return other is DiscordEntry
            && this.source == (other as DiscordEntry).source
            && this.state == (other as DiscordEntry).state
            && this.details == (other as DiscordEntry).details
            && this.timestamp == (other as DiscordEntry).timestamp
         }
   }

   public companion object {
      public fun serializer(): KSerializer<DiscordEntry> {
         return DiscordEntry.$serializer.INSTANCE as KSerializer<DiscordEntry>
      }
   }
}
