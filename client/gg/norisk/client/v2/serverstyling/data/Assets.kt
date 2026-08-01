package gg.norisk.client.v2.serverstyling.data

public data class Assets(icon: String, background: String) {
   public final val icon: String
   public final val background: String

   init {
      this.icon = icon
      this.background = background
   }

   public operator fun component1(): String {
      return this.icon
   }

   public operator fun component2(): String {
      return this.background
   }

   public fun copy(icon: String = this.icon, background: String = this.background): Assets {
      return Assets(icon, background)
   }

   public override fun toString(): String {
      return "Assets(icon=${this.icon}, background=${this.background})"
   }

   public override fun hashCode(): Int {
      return this.icon.hashCode() * 31 + this.background.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label28@
      if (this === other) {
         return true
      } else {
         return other is Assets && this.icon == (other as Assets).icon && this.background == (other as Assets).background
      }
   }
}
