package gg.norisk.client.v2.waypoints.xaero

public class XaeroWaypointSet internal constructor(raw: Any) {
   internal final val raw: Any

   init {
      this.raw = raw
   }

   public final val name: String
      public final get() {
         return XaeroReflect.INSTANCE.setNameOf$nrc_client(this.raw)
      }


   public final val waypoints: List<XaeroWaypoint>
      public final get() {
         return XaeroReflect.INSTANCE.waypointsOf$nrc_client(this.raw)
      }


   public fun add(waypoint: XaeroWaypoint): Boolean {
      return XaeroReflect.INSTANCE.addToSet$nrc_client(this.raw, waypoint.raw)
   }

   public fun remove(waypoint: XaeroWaypoint): Boolean {
      return XaeroReflect.INSTANCE.removeFromSet$nrc_client(this.raw, waypoint.raw)
   }

   public fun clear(): Boolean {
      return XaeroReflect.INSTANCE.clearSet$nrc_client(this.raw)
   }
}
