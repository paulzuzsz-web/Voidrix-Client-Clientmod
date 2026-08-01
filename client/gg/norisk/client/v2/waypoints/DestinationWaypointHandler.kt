package gg.norisk.client.v2.waypoints

import gg.norisk.compat.client.MCClient
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.text.LiteralTextBuilder
import gg.norisk.compat.waypoint.persistent.PersistentWaypoint
import gg.norisk.compat.waypoint.persistent.WaypointPurpose
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nDestinationWaypointHandler.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DestinationWaypointHandler.kt\ngg/norisk/client/v2/waypoints/DestinationWaypointHandler\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt\n+ 4 TextBuilder.kt\ngg/norisk/compat/text/LiteralTextBuilder\n+ 5 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,70:1\n185#2:71\n40#2:72\n8#3,4:73\n78#4,6:77\n72#4,4:83\n87#4:87\n78#4,6:88\n72#4,4:94\n87#4:98\n774#5:99\n865#5,2:100\n*S KotlinDebug\n*F\n+ 1 DestinationWaypointHandler.kt\ngg/norisk/client/v2/waypoints/DestinationWaypointHandler\n*L\n26#1:71\n26#1:72\n43#1:73,4\n44#1:77,6\n44#1:83,4\n44#1:87\n45#1:88,6\n45#1:94,4\n45#1:98\n58#1:99\n58#1:100,2\n*E\n"])
public object DestinationWaypointHandler {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-Destination")
   private const val ARRIVAL_RADIUS_SQ: Int = 25
   private const val MIN_AGE_MS: Long = 5000L
   private final var tickCounter: Int

   public fun init() {
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         val var1: Int = tickCounter++
         if (tickCounter % 20 == 0) {
            INSTANCE.checkArrival()
         }

         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         tickCounter = 0
         Unit.INSTANCE
      })
   }

   private fun checkArrival() {
      if (PersistentWaypointStore.INSTANCE.isLoaded) {
         val var10000: Minecraft = Minecraft.getInstance()
         if (var10000.player != null) {
            val player: LocalPlayer = var10000.player
            val px: Int = var10000.player.blockPosition().getX()
            val var21: Int = player.blockPosition().getZ()
            val var22: Long = System.currentTimeMillis()

            for (wp in PersistentWaypointStore.INSTANCE.getWaypointsByPurpose(WaypointPurpose.DESTINATION)) {
               if (var22 - wp.getCreatedAt() >= 5000L) {
                  val dx: Int = wp.getX() - px
                  val dz: Int = wp.getZ() - var21
                  if (dx * dx + dz * dz <= 25) {
                     PersistentWaypointStore.INSTANCE.deleteWaypoint(wp.getId())
                     val var13: LiteralTextBuilder = LiteralTextBuilder(null, true)
                     var13.getAppendTasks().add(DestinationWaypointHandler$checkArrival$lambda$4$$inlined$text$default$1(var13, "Reached destination: ", true))
                     var13.getAppendTasks().add(DestinationWaypointHandler$checkArrival$lambda$4$$inlined$text$default$2(var13, wp.getName(), true))
                     MCClient.sendMessage(var13.build() as Component)
                     MCLoggerKt.nrcDebugLog(logger, "waypoints", "Destination '${wp.getName()}' reached and removed")
                  }
               }
            }

            if (WaypointModule.INSTANCE.deleteReachedDeathpoints) {
               this.checkDeathWaypointArrival(px, var21, var22)
            }
         }
      }
   }

   private fun checkDeathWaypointArrival(px: Int, pz: Int, now: Long) {
      val dx: java.lang.Iterable = PersistentWaypointStore.INSTANCE.getAllWaypoints()
      val dz: java.util.Collection = ArrayList()

      for (`element$iv$iv` in dx) {
         if ((`element$iv$iv` as PersistentWaypoint).getPurpose().isDeath() && now - (`element$iv$iv` as PersistentWaypoint).getCreatedAt() >= 5000L) {
            dz.add(`element$iv$iv`)
         }
      }

      for (var16 in dz as java.util.List) {
         val var17: Int = var16.getX() - px
         val var18: Int = var16.getZ() - pz
         if (var17 * var17 + var18 * var18 <= 25) {
            PersistentWaypointStore.INSTANCE.deleteWaypoint(var16.getId())
            MCLoggerKt.nrcDebugLog(logger, "waypoints", "Death waypoint '${var16.getName()}' reached and removed")
         }
      }
   }
}
