package gg.norisk.client.v2.waypoints.xaero

public class XaeroWorld internal constructor(raw: Any) {
   internal final val raw: Any

   init {
      this.raw = raw
   }

   public final val dimKey: String?
      public final get() {
         return XaeroReflect.INSTANCE.dimKeyOf$nrc_client(XaeroReflect.INSTANCE.dimIdOf$nrc_client(this.raw))
      }


   public final val waypointSets: List<XaeroWaypointSet>
      public final get() {
         return XaeroReflect.INSTANCE.waypointSetsOf$nrc_client(this.raw)
      }


   public final val currentWaypointSet: XaeroWaypointSet?
      public final get() {
         return XaeroReflect.INSTANCE.currentWaypointSetOf$nrc_client(this.raw)
      }


   public fun ensureSet(setName: String): XaeroWaypointSet? {
      return XaeroReflect.INSTANCE.ensureWaypointSet$nrc_client(this.raw, setName)
   }

   public fun persist(): Boolean {
      return XaeroReflect.INSTANCE.persistWorld$nrc_client(this.raw)
   }
}
