package gg.norisk.client.v2.serverstyling.data

public data class Gamemode(name: String, versions: List<String>) {
   public final val name: String
   public final val versions: List<String>

   init {
      this.name = name
      this.versions = versions
   }

   public operator fun component1(): String {
      return this.name
   }

   public operator fun component2(): List<String> {
      return this.versions
   }

   public fun copy(name: String = this.name, versions: List<String> = this.versions): Gamemode {
      return Gamemode(name, versions)
   }

   public override fun toString(): String {
      return "Gamemode(name=${this.name}, versions=${this.versions})"
   }

   public override fun hashCode(): Int {
      return this.name.hashCode() * 31 + this.versions.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label28@
      if (this === other) {
         return true
      } else {
         return other is Gamemode && this.name == (other as Gamemode).name && this.versions == (other as Gamemode).versions
      }
   }
}
