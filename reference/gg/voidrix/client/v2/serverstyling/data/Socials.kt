package gg.voidrix.client.v2.serverstyling.data

public data class Socials(store: String? = null, discord: String? = null) {
   public final val store: String?
   public final val discord: String?

   init {
      this.store = store
      this.discord = discord
   }

   public operator fun component1(): String? {
      return this.store
   }

   public operator fun component2(): String? {
      return this.discord
   }

   public fun copy(store: String? = this.store, discord: String? = this.discord): Socials {
      return Socials(store, discord)
   }

   public override fun toString(): String {
      return "Socials(store=${this.store}, discord=${this.discord})"
   }

   public override fun hashCode(): Int {
      return (if (this.store == null) 0 else this.store.hashCode()) * 31 + (if (this.discord == null) 0 else this.discord.hashCode())
   }

   public override operator fun equals(other: Any?): Boolean {
      label28@
      if (this === other) {
         return true
      } else {
         return other is Socials && this.store == (other as Socials).store && this.discord == (other as Socials).discord
      }
   }

   fun Socials() {
      this(null, null, 3, null)
   }
}
