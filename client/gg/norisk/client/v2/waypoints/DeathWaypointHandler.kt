package gg.norisk.client.v2.waypoints

import gg.norisk.client.v2.waypoints.xaero.XaeroReflect
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.waypoint.persistent.PersistentWaypoint
import gg.norisk.compat.waypoint.persistent.WaypointDimensionData
import gg.norisk.compat.waypoint.persistent.WaypointPurpose
import gg.norisk.compat.waypoint.persistent.WaypointVisibility
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nDeathWaypointHandler.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DeathWaypointHandler.kt\ngg/norisk/client/v2/waypoints/DeathWaypointHandler\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,102:1\n185#2:103\n40#2:104\n774#3:105\n865#3,2:106\n*S KotlinDebug\n*F\n+ 1 DeathWaypointHandler.kt\ngg/norisk/client/v2/waypoints/DeathWaypointHandler\n*L\n48#1:103\n48#1:104\n68#1:105\n68#1:106,2\n*E\n"])
public object DeathWaypointHandler {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-DeathWaypoint")
   private final var deathX: Int
   private final var deathY: Int
   private final var deathZ: Int
   private final var deathDimensionKey: String?
   private final var deathRecorded: Boolean

   public fun init() {
      ClientEvents.INSTANCE.getDeathEvent().listen({ it: Unit ->
         INSTANCE.onPlayerDeath()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getRespawnRequestEvent().listen({ it: Unit ->
         if (deathRecorded) {
            if (WaypointModule.INSTANCE.deathpoints) {
               INSTANCE.cleanUpDeathWaypoints()
               if (XaeroReflect.INSTANCE.isXaeroAutoDeathpointsEnabled() == true) {
                  MCLoggerKt.nrcDebugLog(logger, "waypoints", "Skipping Voidrix deathpoint — xaero handles it (AUTO_WAYPOINTS_ON_DEATH=true)")
               } else {
                  INSTANCE.createDeathWaypoint()
               }
            }

            deathRecorded = false
         }

         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         deathRecorded = false
         Unit.INSTANCE
      })
   }

   private fun onPlayerDeath() {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player != null) {
         val player: LocalPlayer = var10000.player
         deathX = var10000.player.blockPosition().getX()
         deathY = player.blockPosition().getY()
         deathZ = player.blockPosition().getZ()
         deathDimensionKey = WorldIdentifier.INSTANCE.getCurrentDimensionKey()
         deathRecorded = true
         MCLoggerKt.nrcDebugLog(logger, "waypoints", "Death recorded at ${deathX}, ${deathY}, ${deathZ} in ${deathDimensionKey}")
      }
   }

   private fun cleanUpDeathWaypoints() {
      for (dimKey in PersistentWaypointStore.INSTANCE.getLoadedDimensionKeys()) {
         val var10000: WaypointDimensionData = PersistentWaypointStore.INSTANCE.getDimensionData(dimKey)
         if (var10000 != null) {
            var changed: Boolean = false

            for (set in var10000.getSets().values()) {
               val `$this$filterTo$iv$iv`: java.lang.Iterable = set.getWaypoints()
               val `destination$iv$iv`: java.util.Collection = ArrayList()

               for (`element$iv$iv` in `$this$filterTo$iv$iv`) {
                  if ((`element$iv$iv` as PersistentWaypoint).getPurpose() === WaypointPurpose.DEATH) {
                     `destination$iv$iv`.add(`element$iv$iv`)
                  }
               }

               val deathWaypoints: java.util.List = `destination$iv$iv` as java.util.List
               if (!(`destination$iv$iv` as java.util.List).isEmpty()) {
                  if (WaypointModule.INSTANCE.oldDeathpoints) {
                     for (var18 in deathWaypoints) {
                        var18.setPurpose(WaypointPurpose.OLD_DEATH)
                        var18.setName("Old Death")
                        var18.setInitials("☠")
                     }
                  } else {
                     set.getWaypoints().removeAll(CollectionsKt.toSet(deathWaypoints))
                  }

                  changed = true
               }
            }

            if (changed) {
               PersistentWaypointStore.INSTANCE.saveDimension(dimKey)
               MCLoggerKt.nrcDebugLog(logger, "waypoints", "Cleaned up death waypoints in $dimKey (oldDeathpoints=${WaypointModule.INSTANCE.oldDeathpoints})")
            }
         }
      }
   }

   private fun createDeathWaypoint() {
      if (deathDimensionKey != null) {
         val dimKey: java.lang.String = deathDimensionKey
         PersistentWaypointStore.INSTANCE
            .createWaypoint("Death", deathX, deathY, deathZ, 16733525, WaypointPurpose.DEATH, WaypointVisibility.GLOBAL, deathDimensionKey)
            MCLoggerKt.nrcDebugLog(logger, "waypoints", "Death waypoint created at ${deathX}, ${deathY}, ${deathZ} in $dimKey")
      }
   }
}
