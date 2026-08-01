package gg.voidrix.client.v2.serverstyling.data

public data class ServerStyleManifest(servers: Map<String, StyledServer>) {
   public final val servers: Map<String, StyledServer>

   init {
      this.servers = servers
   }

   public operator fun component1(): Map<String, StyledServer> {
      return this.servers
   }

   public fun copy(servers: Map<String, StyledServer> = this.servers): ServerStyleManifest {
      return ServerStyleManifest(servers)
   }

   public override fun toString(): String {
      return "ServerStyleManifest(servers=${this.servers})"
   }

   public override fun hashCode(): Int {
      return this.servers.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label22@
      if (this === other) {
         return true
      } else {
         return other is ServerStyleManifest && this.servers == (other as ServerStyleManifest).servers
      }
   }
}
