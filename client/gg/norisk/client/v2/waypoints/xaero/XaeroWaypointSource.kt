package gg.norisk.client.v2.waypoints.xaero

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.waypoint.persistent.WaypointPurpose
import gg.norisk.compat.waypoint.persistent.WaypointVisibility
import gg.norisk.compat.waypoint.source.SourceWaypoint
import gg.norisk.compat.waypoint.source.WaypointSource
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.internal.SourceDebugExtension
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nXaeroWaypointSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 XaeroWaypointSource.kt\ngg/norisk/client/v2/waypoints/xaero/XaeroWaypointSource\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,228:1\n1617#2,9:229\n1869#2:238\n1870#2:240\n1626#2:241\n1374#2:242\n1460#2,2:243\n1563#2:245\n1634#2,3:246\n1462#2,3:249\n295#2,2:253\n295#2,2:255\n1#3:239\n1#3:252\n*S KotlinDebug\n*F\n+ 1 XaeroWaypointSource.kt\ngg/norisk/client/v2/waypoints/xaero/XaeroWaypointSource\n*L\n61#1:229,9\n61#1:238\n61#1:240\n61#1:241\n75#1:242\n75#1:243,2\n78#1:245\n78#1:246,3\n75#1:249,3\n180#1:253,2\n189#1:255,2\n61#1:239\n*E\n"])
public object XaeroWaypointSource : WaypointSource {
   private const val LOG_TAG: String = "xaero-source"
   private final val logger: Logger = MCLogger.getLogger("Voidrix-XaeroSource")
   private const val DEFAULT_SET_NAME: String = "gui.xaero_default"
   private final val dumpedOnce: AtomicBoolean = AtomicBoolean(false)
   public open val id: String = "xaero_minimap"
   public open val displayName: String = "Xaero's Minimap"
   public open val isWritable: Boolean = true

   public open val isAvailable: Boolean
      public open get() {
         if (dumpedOnce.compareAndSet(false, true)) {
            XaeroReflect.INSTANCE.dumpResolution()
         }

         if (!XaeroReflect.INSTANCE.isAvailable) {
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "isAvailable=false (XaeroReflect not ready)")
            return false
         } else {
            val world: XaeroWorld = XaeroReflect.INSTANCE.currentWorld()
            val ok: Boolean = world != null
            MCLoggerKt.nrcDebugLog(
               logger,
               "xaero-source",
               "isAvailable=${world != null} (world=${if (world != null) "present (dim=${if (world != null) world.dimKey else null})" else "null"})"
            )
            return ok
         }
      }


   public open fun listDimensions(): List<String> {
      MCLoggerKt.nrcDebugLog(logger, "xaero-source", "listDimensions() called")
      val worlds: java.util.List = XaeroReflect.INSTANCE.worldsInCurrentRoot()
      val var20: java.util.List
      if (worlds.isEmpty()) {
         val var10000: XaeroWorld = XaeroReflect.INSTANCE.currentWorld()
         val dims: java.lang.String = if (var10000 != null) var10000.dimKey else null
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "listDimensions: no root worlds, falling back to currentWorld.dimKey=$dims")
         var20 = if (dims != null) CollectionsKt.listOf(dims) else CollectionsKt.emptyList()
      } else {
         val `$this$mapNotNullTo$iv$iv`: java.lang.Iterable = worlds
         val `destination$iv$iv`: java.util.Collection = ArrayList()

         for (`element$iv$iv$iv` in `$this$mapNotNullTo$iv$iv`) {
            val var21: Any = (`element$iv$iv$iv` as XaeroWorld).dimKey
            if (var21 != null) {
               `destination$iv$iv`.add(var21)
            }
         }

         val var19: java.util.List = CollectionsKt.distinct(`destination$iv$iv` as java.util.List)
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "listDimensions: ${worlds.size()} worlds, ${var19.size()} distinct dims: $var19")
         var20 = var19
      }

      return var20
   }

   public open fun list(dimensionKey: String): List<SourceWaypoint> {
      MCLoggerKt.nrcDebugLog(logger, "xaero-source", "list(dim='$dimensionKey') called")
      val var10000: XaeroWorld = this.worldForDim(dimensionKey)
      if (var10000 == null) {
         val var25: XaeroWaypointSource = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "list: no xaero world for dim '$dimensionKey'")
         return CollectionsKt.emptyList()
      } else {
         val sets: java.util.List = var10000.waypointSets
         val `$this$flatMapTo$iv$iv`: java.lang.Iterable = sets
         val `destination$iv$iv`: java.util.Collection = ArrayList()

         for (`element$iv$iv` in `$this$flatMapTo$iv$iv`) {
            val `list$iv$iv`: XaeroWaypointSet = `element$iv$iv` as XaeroWaypointSet
            val wps: java.util.List = (`element$iv$iv` as XaeroWaypointSet).waypoints
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "list: set '${(`element$iv$iv` as XaeroWaypointSet).name}' has ${wps.size()} waypoint(s)")
            val `$this$mapTo$iv$iv`: java.lang.Iterable = wps
            val `destination$iv$ivx`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(wps, 10))

            for (`item$iv$iv` in `$this$mapTo$iv$iv`) {
               `destination$iv$ivx`.add(INSTANCE.toSourceWaypoint(`item$iv$iv` as XaeroWaypoint, `list$iv$iv`.name))
            }

            CollectionsKt.addAll(`destination$iv$iv`, `destination$iv$ivx` as java.util.List)
         }

         val flat: java.util.List = `destination$iv$iv` as java.util.List
         MCLoggerKt.nrcDebugLog(
            logger, "xaero-source", "list(dim='$dimensionKey') -> ${(`destination$iv$iv` as java.util.List).size()} waypoint(s) across ${sets.size()} set(s)"
         )
         return flat
      }
   }

   public open fun add(dimensionKey: String, waypoint: SourceWaypoint): Boolean {
      MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add(dim='$dimensionKey', name='${waypoint.getName()}') called")
      val var10000: XaeroWorld = this.worldForDim(dimensionKey)
      if (var10000 == null) {
         val var17: XaeroWaypointSource = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add: no xaero world for dim '$dimensionKey'")
         return false
      } else {
         var var27: XaeroWaypointSet = var10000.currentWaypointSet
         if (var27 == null) {
            var27 = var10000.ensureSet("gui.xaero_default")
            if (var27 == null) {
               val var22: XaeroWaypointSource = this
               MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add: could not resolve a waypoint set")
               return false
            }
         }

         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add: using set '${var27.name}'")
         val var28: XaeroWaypoint.Companion = XaeroWaypoint.Companion
         val var10001: Int = waypoint.getX()
         val var10002: Int = waypoint.getY()
         val var10003: Int = waypoint.getZ()
         val var10004: java.lang.String = waypoint.getName()
         val persisted: java.lang.CharSequence = waypoint.getInitials()
         val var10005: java.lang.CharSequence
         if (StringsKt.isBlank(persisted)) {
            run label56@{
               val var29: Character = StringsKt.firstOrNull(waypoint.getName())
               if (var29 != null) {
                  var30 = var29.toString()
                  if (var30 != null) {
                     return@label56
                  }
               }

               var30 = "?"
            }

            var10005 = var30
         } else {
            var10005 = persisted
         }

         val var31: XaeroWaypoint = var28.create(
            var10001, var10002, var10003, var10004, var10005 as java.lang.String, waypoint.getColor(), waypoint.getPurpose().name(), waypoint.getYIncluded()
         )
         if (var31 == null) {
            val var21: XaeroWaypointSource = this
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add: Waypoint ctor failed")
            return false
         } else {
            var var32: XaeroWaypoint = var31
            val var35: java.lang.String = waypoint.getName()
            val ok: java.lang.CharSequence = waypoint.getInitials()
            val var36: java.lang.CharSequence
            if (StringsKt.isBlank(ok)) {
               run label61@{
                  val var33: Character = StringsKt.firstOrNull(waypoint.getName())
                  if (var33 != null) {
                     var34 = var33.toString()
                     if (var34 != null) {
                        return@label61
                     }
                  }

                  var34 = "?"
               }

               var32 = var31
               var36 = var34
            } else {
               var36 = ok
            }

            var32.applyUpdate(
               var35,
               var36 as java.lang.String,
               waypoint.getX(),
               waypoint.getY(),
               waypoint.getZ(),
               waypoint.getColor(),
               waypoint.getDisabled(),
               waypoint.getYIncluded(),
               waypoint.getPurpose().name(),
               waypoint.getVisibility().name()
            )
            val var16: Boolean = var27.add(var31)
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add: set.add returned $var16")
            if (var16) {
               MCLoggerKt.nrcDebugLog(logger, "xaero-source", "add: persisted=${var10000.persist()}")
            }

            return var16
         }
      }
   }

   public open fun update(dimensionKey: String, waypoint: SourceWaypoint): Boolean {
      MCLoggerKt.nrcDebugLog(logger, "xaero-source", "update(dim='$dimensionKey', id='${waypoint.getId()}', name='${waypoint.getName()}') called")
      val var10000: XaeroWorld = this.worldForDim(dimensionKey)
      if (var10000 == null) {
         val var13: XaeroWaypointSource = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "update: no xaero world for dim '$dimensionKey'")
         return false
      } else {
         val var15: XaeroWaypointSource.FoundWaypoint = this.findWaypoint(var10000, waypoint.getId())
         if (var15 == null) {
            val `$this$update_u24lambda_u2410`: XaeroWaypointSource = this
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "update: waypoint not found for id=${waypoint.getId()}")
            return false
         } else {
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "update: found in set '${var15.set.name}', applying change")
            val var16: XaeroWaypoint = var15.waypoint
            val var10001: java.lang.String = waypoint.getName()
            val persisted: java.lang.CharSequence = waypoint.getInitials()
            val var10002: java.lang.CharSequence
            if (StringsKt.isBlank(persisted)) {
               run label31@{
                  val var17: Character = StringsKt.firstOrNull(waypoint.getName())
                  if (var17 != null) {
                     var18 = var17.toString()
                     if (var18 != null) {
                        return@label31
                     }
                  }

                  var18 = "?"
               }

               var10002 = var18
            } else {
               var10002 = persisted
            }

            var16.applyUpdate(
               var10001,
               var10002 as java.lang.String,
               waypoint.getX(),
               waypoint.getY(),
               waypoint.getZ(),
               waypoint.getColor(),
               waypoint.getDisabled(),
               waypoint.getYIncluded(),
               waypoint.getPurpose().name(),
               waypoint.getVisibility().name()
            )
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "update: persisted=${var10000.persist()}")
            return true
         }
      }
   }

   public open fun delete(dimensionKey: String, id: String): Boolean {
      MCLoggerKt.nrcDebugLog(logger, "xaero-source", "delete(dim='$dimensionKey', id='$id') called")
      val var10000: XaeroWorld = this.worldForDim(dimensionKey)
      if (var10000 == null) {
         val var9: XaeroWaypointSource = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "delete: no xaero world for dim '$dimensionKey'")
         return false
      } else {
         val var11: XaeroWaypointSource.FoundWaypoint = this.findWaypoint(var10000, id)
         if (var11 == null) {
            val `$this$delete_u24lambda_u2413`: XaeroWaypointSource = this
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "delete: waypoint not found for id=$id")
            return false
         } else {
            val ok: Boolean = var11.set.remove(var11.waypoint)
            MCLoggerKt.nrcDebugLog(logger, "xaero-source", "delete: set.remove returned $ok")
            if (ok) {
               MCLoggerKt.nrcDebugLog(logger, "xaero-source", "delete: persisted=${var10000.persist()}")
            }

            return ok
         }
      }
   }

   private fun worldForDim(dimensionKey: String): XaeroWorld? {
      val current: XaeroWorld = XaeroReflect.INSTANCE.currentWorld()
      if ((if (current != null) current.dimKey else null) == dimensionKey) {
         MCLoggerKt.nrcDebugLog(logger, "xaero-source", "worldForDim('$dimensionKey'): using currentWorld")
         return current
      } else {
         val var6: java.util.Iterator = XaeroReflect.INSTANCE.worldsInCurrentRoot().iterator()

         var var10000: Any
         while (true) {
            if (var6.hasNext()) {
               val `element$iv`: Any = var6.next()
               if (!((`element$iv` as XaeroWorld).dimKey == dimensionKey)) {
                  continue
               }

               var10000 = `element$iv`
               break
            }

            var10000 = null
            break
         }

         val match: XaeroWorld = var10000 as XaeroWorld
         MCLoggerKt.nrcDebugLog(
            logger,
            "xaero-source",
            "worldForDim('$dimensionKey'): currentWorld dim='${if (current != null) current.dimKey else null}', scanned root -> ${if (var10000 as XaeroWorld
                  != null)
               "match"
               else
               "no match"}"
         )
         return match
      }
   }

   private fun findWaypoint(world: XaeroWorld, id: String): gg.norisk.client.v2.waypoints.xaero.XaeroWaypointSource.FoundWaypoint? {
      for (set in world.waypointSets) {
         val var8: java.util.Iterator = set.waypoints.iterator()

         var var10000: Any
         while (true) {
            if (!var8.hasNext()) {
               var10000 = null
               break
            }

            val `element$iv`: Any = var8.next()
            if (INSTANCE.syntheticId(`element$iv` as XaeroWaypoint, set.name) == id) {
               var10000 = `element$iv`
               break
            }
         }

         val match: XaeroWaypoint = var10000 as XaeroWaypoint
         if (var10000 as XaeroWaypoint != null) {
            return XaeroWaypointSource.FoundWaypoint(set, match)
         }
      }

      return null
   }

   private fun toSourceWaypoint(wp: XaeroWaypoint, setName: String): SourceWaypoint {
      var visibility: WaypointPurpose
      try {
         visibility = WaypointPurpose.valueOf(wp.purposeName)
      } catch (var6: Exception) {
         visibility = WaypointPurpose.NORMAL
      }

      val var5: java.lang.String = wp.visibilityName
      val var7: WaypointVisibility = if (!(var5 == "GLOBAL") && !(var5 == "WORLD_MAP_GLOBAL")) WaypointVisibility.LOCAL else WaypointVisibility.GLOBAL
      return SourceWaypoint(
         this.syntheticId(wp, setName), wp.name, wp.initials, wp.x, wp.y, wp.z, wp.color, visibility, var7, wp.disabled, wp.yIncluded, wp.createdAt, setName
      )
   }

   private fun syntheticId(wp: XaeroWaypoint, setName: String): String {
      return "xaero:$setName:${wp.createdAt}:${wp.x},${wp.y},${wp.z}:${wp.name}"
   }

   private data class FoundWaypoint(set: XaeroWaypointSet, waypoint: XaeroWaypoint) {
      public final val set: XaeroWaypointSet
      public final val waypoint: XaeroWaypoint

      init {
         this.set = set
         this.waypoint = waypoint
      }

      public operator fun component1(): XaeroWaypointSet {
         return this.set
      }

      public operator fun component2(): XaeroWaypoint {
         return this.waypoint
      }

      public fun copy(set: XaeroWaypointSet = this.set, waypoint: XaeroWaypoint = this.waypoint): gg.norisk.client.v2.waypoints.xaero.XaeroWaypointSource.FoundWaypoint {
         return XaeroWaypointSource.FoundWaypoint(set, waypoint)
      }

      public override fun toString(): String {
         return "FoundWaypoint(set=${this.set}, waypoint=${this.waypoint})"
      }

      public override fun hashCode(): Int {
         return this.set.hashCode() * 31 + this.waypoint.hashCode()
      }

      public override operator fun equals(other: Any?): Boolean {
         label28@
         if (this === other) {
            return true
         } else {
            return other is XaeroWaypointSource.FoundWaypoint
               && this.set == (other as XaeroWaypointSource.FoundWaypoint).set
               && this.waypoint == (other as XaeroWaypointSource.FoundWaypoint).waypoint
            }
      }
   }
}
