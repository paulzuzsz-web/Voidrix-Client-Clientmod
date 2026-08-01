package gg.norisk.client.v2.serverstyling.data

public data class StyledServer(`server-address`: List<String>,
   `pretty-name`: String,
   categorys: List<String>,
   gamemodes: Map<String, Gamemode>,
   socials: Socials,
   assets: Assets,
   `disabled-modules`: List<String> = CollectionsKt.emptyList()
) {
   public final val `server-address`: List<String>
   public final val `pretty-name`: String
   public final val categorys: List<String>
   public final val gamemodes: Map<String, Gamemode>
   public final val socials: Socials
   public final val assets: Assets
   public final val `disabled-modules`: List<String>

   init {
      this.server-address = var1
      this.pretty-name = var2
      this.categorys = categorys
      this.gamemodes = gamemodes
      this.socials = socials
      this.assets = assets
      this.disabled-modules = var7
   }

   public operator fun component1(): List<String> {
      return this.server-address
   }

   public operator fun component2(): String {
      return this.pretty-name
   }

   public operator fun component3(): List<String> {
      return this.categorys
   }

   public operator fun component4(): Map<String, Gamemode> {
      return this.gamemodes
   }

   public operator fun component5(): Socials {
      return this.socials
   }

   public operator fun component6(): Assets {
      return this.assets
   }

   public operator fun component7(): List<String> {
      return this.disabled-modules
   }

   public fun copy(
      `server-address`: List<String> = this.server-address,
      `pretty-name`: String = this.pretty-name,
      categorys: List<String> = this.categorys,
      gamemodes: Map<String, Gamemode> = this.gamemodes,
      socials: Socials = this.socials,
      assets: Assets = this.assets,
      `disabled-modules`: List<String> = this.disabled-modules
   ): StyledServer {
      return StyledServer(var1, var2, categorys, gamemodes, socials, assets, var7)
   }

   public override fun toString(): String {
      return "StyledServer(server-address=${this.server-address}, pretty-name=${this.pretty-name}, categorys=${this.categorys}, gamemodes=${this.gamemodes}, socials=${this.socials}, assets=${this.assets}, disabled-modules=${this.disabled-modules})"
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 ((this.server-address.hashCode() * 31 + this.pretty-name.hashCode()) * 31 + this.categorys.hashCode()) * 31
                                    + this.gamemodes.hashCode()
                              )
                              * 31
                           + this.socials.hashCode()
                     )
                     * 31
                  + this.assets.hashCode()
            )
            * 31
         + this.disabled-modules.hashCode()
      }

   public override operator fun equals(other: Any?): Boolean {
      label58@
      if (this === other) {
         return true
      } else {
         return other is StyledServer
            && this.server-address == (other as StyledServer).server-address
            && this.pretty-name == (other as StyledServer).pretty-name
            && this.categorys == (other as StyledServer).categorys
            && this.gamemodes == (other as StyledServer).gamemodes
            && this.socials == (other as StyledServer).socials
            && this.assets == (other as StyledServer).assets
            && this.disabled-modules == (other as StyledServer).disabled-modules
         }
   }
}
