package gg.voidrix.client.v2.waypoints.xaero

public class XaeroWorld internal constructor(raw: Any) {
   internal final val raw: Any

   init {
      this.raw = raw
   }

   public final val dimKey: String?
      public final get() {
         return XaeroReflect.INSTANCE.dimKeyOf$voidrix_client(XaeroReflect.INSTANCE.dimIdOf$voidrix_client(this.raw))
      }


   public final val waypointSets: List<XaeroWaypointSet>
      public final get() {
         return XaeroReflect.INSTANCE.waypointSetsOf$voidrix_client(this.raw)
      }


   public final val currentWaypointSet: XaeroWaypointSet?
      public final get() {
         return XaeroReflect.INSTANCE.currentWaypointSetOf$voidrix_client(this.raw)
      }


   public fun ensureSet(setName: String): XaeroWaypointSet? {
      return XaeroReflect.INSTANCE.ensureWaypointSet$voidrix_client(this.raw, setName)
   }

   public fun persist(): Boolean {
      return XaeroReflect.INSTANCE.persistWorld$voidrix_client(this.raw)
   }
}
