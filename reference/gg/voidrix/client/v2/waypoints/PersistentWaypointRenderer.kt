package gg.voidrix.client.v2.waypoints

import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.compat.render.RendererUtils
import gg.voidrix.compat.ui.hud.BatchedHudManager
import gg.voidrix.compat.ui.hud.IBatchedHudRenderer
import gg.voidrix.compat.waypoint.persistent.PersistentWaypoint
import gg.voidrix.ui.v2.hud.BatchedHudRendererProvider
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft

@SourceDebugExtension(["SMAP\nPersistentWaypointRenderer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PersistentWaypointRenderer.kt\ngg/voidrix/client/v2/waypoints/PersistentWaypointRenderer\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,257:1\n211#2:258\n40#2:259\n225#2:260\n40#2:261\n384#3,7:262\n1021#4,2:269\n*S KotlinDebug\n*F\n+ 1 PersistentWaypointRenderer.kt\ngg/voidrix/client/v2/waypoints/PersistentWaypointRenderer\n*L\n82#1:258\n82#1:259\n83#1:260\n83#1:261\n125#1:262,7\n188#1:269,2\n*E\n"])
public object PersistentWaypointRenderer {
   public final var hidden: Boolean
      internal set

   private const val DIST_TEXT_REFRESH_TICKS: Int = 1
   private const val SORT_INTERVAL_TICKS: Int = 10
   private const val CLOSE_THRESHOLD_SQ: Double = 400.0
   private const val CLAMP_DISTANCE: Double = 64.0
   private const val CLAMP_DISTANCE_SQ: Double = 4096.0
   private const val HOVER_SCREEN_RADIUS: Double = 25.0
   private const val SCREEN_MARGIN: Double = 32.0
   private const val ROW_HEIGHT: Float = 12.0F
   private const val BG_PADDING: Float = 4.0F
   private const val DIST_GAP: Float = 2.0F
   private const val BG_DARK: Int = 1509949440
   private const val TEXT_WHITE: Int = -1
   private final val renderCache: HashMap<UUID, gg.voidrix.client.v2.waypoints.PersistentWaypointRenderer.WaypointRenderData> = HashMap()
   private final var visibleBuffer: ArrayList<gg.voidrix.client.v2.waypoints.PersistentWaypointRenderer.VisibleEntry> = ArrayList(64)
   private final var lastSortTick: Int = -1
   private final var tickCounter: Int

   public fun init() {
      BatchedHudRendererProvider.INSTANCE.addHudListener({ 
         INSTANCE.onRenderFrame()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         val var1: Int = tickCounter++
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         renderCache.clear()
         Unit.INSTANCE
      })
   }

   private fun onRenderFrame() {
      if (!hidden) {
         if (PersistentWaypointStore.INSTANCE.isLoaded) {
            val var10000: IBatchedHudRenderer = BatchedHudManager.INSTANCE.getRenderer()
            if (var10000 != null) {
               val hud: IBatchedHudRenderer = var10000
               val waypoints: java.util.List = PersistentWaypointStore.INSTANCE.getAllWaypoints()
               if (!waypoints.isEmpty()) {
                  val var57: Minecraft = Minecraft.getInstance()
                  val screenW: Double = var57.getWindow().getGuiScaledWidth()
                  val var58: Minecraft = Minecraft.getInstance()
                  val var46: Double = var58.getWindow().getGuiScaledHeight()
                  val var47: Double = screenW / 2.0
                  val screenCenterY: Double = var46 / 2.0
                  visibleBuffer.clear()
                  var closestHoverDist: Double = java.lang.Double.MAX_VALUE
                  var closestHoverIndex: Int = -1

                  for (`$i$f$sortByDescending` in waypoints) {
                     if (!`$i$f$sortByDescending`.getDisabled()) {
                        val wx: Double = `$i$f$sortByDescending`.getX() + 0.5
                        val wy: Double = `$i$f$sortByDescending`.getY() + 1.0
                        val wz: Double = `$i$f$sortByDescending`.getZ() + 0.5
                        val dx: Double = wx - RendererUtils.cameraX
                        val dy: Double = wy - RendererUtils.cameraY
                        val dz: Double = wz - RendererUtils.cameraZ
                        val realDistSq: Double = dx * dx + dy * dy + (wz - RendererUtils.cameraZ) * (wz - RendererUtils.cameraZ)
                        val var59: Triple
                        if (dx * dx + dy * dy + (wz - RendererUtils.cameraZ) * (wz - RendererUtils.cameraZ) > 4096.0) {
                           val px: Double = 64.0 / Math.sqrt(realDistSq)
                           var59 = Triple(RendererUtils.cameraX + dx * px, RendererUtils.cameraY + dy * px, RendererUtils.cameraZ + dz * px)
                        } else {
                           var59 = Triple(wx, wy, wz)
                        }

                        val screen: Triple = RendererUtils.INSTANCE
                           .worldSpaceToScreenSpace(
                              (var59.component1() as java.lang.Number).doubleValue(),
                              (var59.component2() as java.lang.Number).doubleValue(),
                              (var59.component3() as java.lang.Number).doubleValue()
                           )
                           if (RendererUtils.INSTANCE.screenSpaceCoordinateIsInFront((screen.getThird() as java.lang.Number).doubleValue())
                           && !((screen.getFirst() as java.lang.Number).doubleValue() < -32.0)
                           && !((screen.getFirst() as java.lang.Number).doubleValue() > screenW + 32.0)
                           && !((screen.getSecond() as java.lang.Number).doubleValue() < -32.0)
                           && !((screen.getSecond() as java.lang.Number).doubleValue() > var46 + 32.0)) {
                           val isClose: java.util.Map = renderCache
                           var expectedInitials: Any = `$i$f$sortByDescending`.getId()
                           val `value$iv`: Any = isClose.get(expectedInitials)
                           val var60: Any
                           if (`value$iv` == null) {
                              val var55: Any = PersistentWaypointRenderer.WaypointRenderData()
                              isClose.put(expectedInitials, var55)
                              var60 = var55
                           } else {
                              var60 = `value$iv`
                           }

                           val cache: PersistentWaypointRenderer.WaypointRenderData = var60 as PersistentWaypointRenderer.WaypointRenderData
                           (var60 as PersistentWaypointRenderer.WaypointRenderData).screenX = (screen.getFirst() as java.lang.Number).doubleValue()
                           cache.screenY = (screen.getSecond() as java.lang.Number).doubleValue()
                           cache.distanceSq = realDistSq
                           cache.expanded = cache.distanceSq < 400.0
                           cache.showDistance = false
                           expectedInitials = if (`$i$f$sortByDescending`.getPurpose().isDeath())
                              "☠"
                              else
                              (if (`$i$f$sortByDescending`.getPurpose().isDestination()) "→" else `$i$f$sortByDescending`.getInitials())
                              if (!(cache.initials == expectedInitials) || !(cache.cachedName == `$i$f$sortByDescending`.getName())) {
                              cache.initials = expectedInitials
                              cache.cachedName = `$i$f$sortByDescending`.getName()
                              val var10002: java.lang.String = cache.initials
                              cache.initialsWidth = hud.getTextWidth(var10002)
                              cache.nameWidth = hud.getTextWidth(`$i$f$sortByDescending`.getName())
                           }

                           val var54: Double = Math.sqrt(cache.distanceSq)
                           val var56: Int = (int)(var54 * 10.0)
                           if (cache.distanceTenths != (int)(var54 * 10.0) && (tickCounter % 1 == 0 || cache.distLabel == null)) {
                              cache.distanceTenths = var56
                              cache.distLabel = this.formatDistance(var54)
                              val var61: java.lang.String = cache.distLabel
                              cache.distLabelWidth = hud.getTextWidth(var61)
                           }

                           if (cache.distLabel == null) {
                              cache.distanceTenths = var56
                              cache.distLabel = this.formatDistance(var54)
                              val var62: java.lang.String = cache.distLabel
                              cache.distLabelWidth = hud.getTextWidth(var62)
                           }

                           val screenDist: Double = Math.hypot(cache.screenX - var47, cache.screenY - screenCenterY)
                           if (screenDist <= 25.0 && screenDist < closestHoverDist) {
                              closestHoverDist = screenDist
                              closestHoverIndex = visibleBuffer.size()
                           }

                           visibleBuffer.add(PersistentWaypointRenderer.VisibleEntry(`$i$f$sortByDescending`, cache))
                        }
                     }
                  }

                  if (!visibleBuffer.isEmpty()) {
                     if (closestHoverIndex >= 0) {
                        val var48: PersistentWaypointRenderer.WaypointRenderData = (visibleBuffer.get(closestHoverIndex) as PersistentWaypointRenderer.VisibleEntry).cache
                        var48.expanded = true
                        var48.showDistance = true
                     }

                     if (tickCounter - lastSortTick >= 10) {
                        val var49: java.util.List = visibleBuffer
                        if (visibleBuffer.size() > 1) {
                           CollectionsKt.sortWith(var49, PersistentWaypointRenderer$onRenderFrame$$inlined$sortByDescending$1())
                        }

                        lastSortTick = tickCounter
                     }

                     hud.beginFrame()
                     this.renderBackgrounds(hud)
                     hud.flushBackgrounds()
                     this.renderText(hud)
                     hud.flushText()
                     hud.endFrame()
                  }
               }
            }
         }
      }
   }

   private fun renderBackgrounds(hud: IBatchedHudRenderer) {
      var var10000: PersistentWaypointRenderer.VisibleEntry = visibleBuffer.iterator()
      val var2: java.util.Iterator = var10000

      while (var2.hasNext()) {
         var10000 = (PersistentWaypointRenderer.VisibleEntry)var2.next()
         val wp: PersistentWaypoint = (var10000 as PersistentWaypointRenderer.VisibleEntry).waypoint
         val c: PersistentWaypointRenderer.WaypointRenderData = (var10000 as PersistentWaypointRenderer.VisibleEntry).cache
         val cx: Float = (float)c.screenX
         var y: Float = (float)c.screenY
         if (c.expanded) {
            wp.getName()
         }

         val topW: Float = (if (c.expanded) c.nameWidth else c.initialsWidth) + 4.0F
         hud.drawBackground(cx - topW / 2.0F, y - 2.0F, topW, 12.0F, -1879048192 or wp.getColor() and 16777215)
         if (c.showDistance) {
            y = y + 14.0F
            val distW: Float = c.distLabelWidth + 4.0F
            hud.drawBackground(cx - distW / 2.0F, y - 2.0F, distW, 12.0F, 1509949440)
         }
      }
   }

   private fun renderText(hud: IBatchedHudRenderer) {
      var var10000: PersistentWaypointRenderer.VisibleEntry = visibleBuffer.iterator()
      val var2: java.util.Iterator = var10000

      while (var2.hasNext()) {
         var10000 = (PersistentWaypointRenderer.VisibleEntry)var2.next()
         val wp: PersistentWaypoint = (var10000 as PersistentWaypointRenderer.VisibleEntry).waypoint
         val c: PersistentWaypointRenderer.WaypointRenderData = (var10000 as PersistentWaypointRenderer.VisibleEntry).cache
         val cx: Float = (float)c.screenX
         var y: Float = (float)c.screenY
         val var13: java.lang.String
         if (c.expanded) {
            var13 = wp.getName()
         } else {
            var13 = c.initials
         }

         hud.drawText(var13, cx - (float)(if (c.expanded) c.nameWidth else c.initialsWidth) / 2.0F, y, -1, true)
         if (c.showDistance) {
            y = y + 14.0F
            val var14: java.lang.String = c.distLabel
            if (var14 != null) {
               hud.drawText(var14, cx - (float)c.distLabelWidth / 2.0F, y, -1, true)
            }
         }
      }
   }

   private fun formatDistance(distanceM: Double): String {
      var var9: java.lang.String
      if (distanceM >= 1000.0) {
         val var6: Array<Any> = arrayOf(distanceM / 1000.0)
         var9 = java.lang.String.format("%.1f", Arrays.copyOf(var6, var6.length))
         var9 = "${StringsKt.replace$default(var9, '.', ',', false, 4, null)}km"
      } else {
         val var8: Array<Any> = arrayOf(distanceM)
         var9 = java.lang.String.format("%.1f", Arrays.copyOf(var8, var8.length))
         var9 = "${StringsKt.replace$default(var9, '.', ',', false, 4, null)}m"
      }

      return var9
   }

   private class VisibleEntry(waypoint: PersistentWaypoint, cache: gg.voidrix.client.v2.waypoints.PersistentWaypointRenderer.WaypointRenderData) {
      public final val waypoint: PersistentWaypoint
      public final val cache: gg.voidrix.client.v2.waypoints.PersistentWaypointRenderer.WaypointRenderData

      init {
         this.waypoint = waypoint
         this.cache = cache
      }
   }

   private class WaypointRenderData {
      public final var screenX: Double
         internal set

      public final var screenY: Double
         internal set

      public final var distanceSq: Double
         internal set

      public final var distanceTenths: Int = -1
         internal set

      public final var initials: String?
         internal set

      public final var cachedName: String?
         internal set

      public final var initialsWidth: Int = -1
         internal set

      public final var nameWidth: Int = -1
         internal set

      public final var distLabel: String?
         internal set

      public final var distLabelWidth: Int = -1
         internal set

      public final var expanded: Boolean
         internal set

      public final var showDistance: Boolean
         internal set
   }
}
