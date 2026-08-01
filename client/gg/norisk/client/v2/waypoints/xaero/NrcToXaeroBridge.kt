package gg.norisk.client.v2.waypoints.xaero

import gg.norisk.client.v2.waypoints.PersistentWaypointStore
import gg.norisk.client.v2.waypoints.WaypointEditScreen
import gg.norisk.client.v2.waypoints.WaypointModule
import gg.norisk.client.v2.waypoints.source.NrcWaypointSource
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.waypoint.source.SourceWaypoint
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nNrcToXaeroBridge.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcToXaeroBridge.kt\ngg/norisk/client/v2/waypoints/xaero/NrcToXaeroBridge\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,243:1\n328#2:244\n40#2:245\n355#2:246\n40#2:247\n356#2:248\n295#3,2:249\n1#4:251\n*S KotlinDebug\n*F\n+ 1 NrcToXaeroBridge.kt\ngg/norisk/client/v2/waypoints/xaero/NrcToXaeroBridge\n*L\n62#1:244\n62#1:245\n63#1:246\n63#1:247\n63#1:248\n112#1:249,2\n*E\n"])
public object NrcToXaeroBridge {
   private const val LOG_TAG: String = "nrc2xaero"
   private final val logger: Logger = MCLogger.getLogger("Voidrix-NrcToXaeroBridge")
   internal const val INJECTED_SET_NAME: String = "Voidrix"
   private final var inAppend: Boolean
   private final var lastMinimapCount: Int = -1
   private final var lastWorldmapCount: Int = -1

   @JvmStatic
   public fun handleOpenWaypoint(wmWaypoint: Any): Boolean {
      val var10000: SourceWaypoint = INSTANCE.resolveNrcFromWm(wmWaypoint)
      if (var10000 == null) {
         return false
      } else {
         val var10: java.lang.String = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
         if (var10 == null) {
            return false
         } else {
            MCLoggerKt.nrcDebugLog(logger, "nrc2xaero", "handleOpenWaypoint: routing xaero edit to Voidrix for '${var10000.getName()}'")
            val var11: Minecraft = Minecraft.getInstance()
            var `screen$iv`: Screen = var11.gui.screen()
            `screen$iv` = WaypointEditScreen(var10000, var10, NrcWaypointSource.INSTANCE, if (`screen$iv` is Screen) `screen$iv` else null) as Screen
            val var12: Minecraft = Minecraft.getInstance()
            var12.gui.setScreen(`screen$iv`)
            return true
         }
      }
   }

   @JvmStatic
   public fun handleDeleteWaypoint(wmWaypoint: Any): Boolean {
      val var10000: SourceWaypoint = INSTANCE.resolveNrcFromWm(wmWaypoint)
      if (var10000 == null) {
         return false
      } else {
         val var3: java.lang.String = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
         if (var3 == null) {
            return false
         } else {
            MCLoggerKt.nrcDebugLog(logger, "nrc2xaero", "handleDeleteWaypoint: deleting Voidrix waypoint '${var10000.getName()}'")
            NrcWaypointSource.INSTANCE.delete(var3, var10000.getId())
            return true
         }
      }
   }

   @JvmStatic
   public fun handleDisableWaypoint(wmWaypoint: Any): Boolean {
      val var10000: SourceWaypoint = INSTANCE.resolveNrcFromWm(wmWaypoint)
      if (var10000 == null) {
         return false
      } else {
         val var4: java.lang.String = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
         if (var4 == null) {
            return false
         } else {
            val toggled: SourceWaypoint = SourceWaypoint.copy$default(
               var10000, null, null, null, 0, 0, 0, 0, null, null, !var10000.getDisabled(), false, 0L, null, 7679, null
            )
            MCLoggerKt.nrcDebugLog(logger, "nrc2xaero", "handleDisableWaypoint: toggle disabled -> ${toggled.getDisabled()} for '${var10000.getName()}'")
            NrcWaypointSource.INSTANCE.update(var4, toggled)
            return true
         }
      }
   }

   private fun resolveNrcFromWm(wmWaypoint: Any): SourceWaypoint? {
      var var10000: java.lang.String = XaeroReflect.INSTANCE.wmWpSetName(wmWaypoint)
      if (var10000 == null) {
         return null
      } else if (!(var10000 == "Voidrix")) {
         return null
      } else {
         var10000 = XaeroReflect.INSTANCE.wmWpName(wmWaypoint)
         if (var10000 == null) {
            return null
         } else {
            val name: java.lang.String = var10000
            val x: Int = XaeroReflect.INSTANCE.wmWpX(wmWaypoint)
            val y: Int = XaeroReflect.INSTANCE.wmWpY(wmWaypoint)
            val z: Int = XaeroReflect.INSTANCE.wmWpZ(wmWaypoint)
            var10000 = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
            if (var10000 == null) {
               return null
            } else {
               val var10: java.util.Iterator = NrcWaypointSource.INSTANCE.list(var10000).iterator()

               while (true) {
                  if (!var10.hasNext()) {
                     var16 = null
                     break
                  }

                  val `element$iv`: Any = var10.next()
                  if ((`element$iv` as SourceWaypoint).getName() == name
                     && (`element$iv` as SourceWaypoint).getX() == x
                     && (`element$iv` as SourceWaypoint).getY() == y
                     && (`element$iv` as SourceWaypoint).getZ() == z) {
                     var16 = `element$iv`
                     break
                  }
               }

               return var16 as SourceWaypoint
            }
         }
      }
   }

   private fun isInWorldRenderPath(): Boolean {
      return InWorldRenderState.INSTANCE.isActive
   }

   @JvmStatic
   public fun appendMinimapWaypoints(list: MutableList<Any>) {
      if (!inAppend) {
         if (WaypointModule.INSTANCE.isEnabled()) {
            if (XaeroReflect.INSTANCE.isAvailable) {
               if (!INSTANCE.isInWorldRenderPath()) {
                  inAppend = true

                  try {
                     val var10000: XaeroWorld = XaeroReflect.INSTANCE.currentWorldCached()
                     if (var10000 == null) {
                        return
                     }

                     val var18: java.lang.String = var10000.dimKey
                     if (var18 == null) {
                        return
                     }

                     val nrcWaypoints: java.util.List = NrcWaypointSource.INSTANCE.list(var18)
                     if (!nrcWaypoints.isEmpty()) {
                        var added: Int = 0

                        for (wp in nrcWaypoints) {
                           if (!wp.getDisabled()) {
                              val var19: XaeroReflect = XaeroReflect.INSTANCE
                              val var10001: Int = wp.getX()
                              val var10002: Int = wp.getY()
                              val var10003: Int = wp.getZ()
                              val var10004: java.lang.String = wp.getName()
                              val var8: java.lang.CharSequence = wp.getInitials()
                              val var10005: java.lang.CharSequence
                              if (StringsKt.isBlank(var8)) {
                                 run label159@{
                                    val var20: Character = StringsKt.firstOrNull(wp.getName())
                                    if (var20 != null) {
                                       var21 = var20.toString()
                                       if (var21 != null) {
                                          return@label159
                                       }
                                    }

                                    var21 = "?"
                                 }

                                 var10005 = var21
                              } else {
                                 var10005 = var8
                              }

                              val var22: Any = var19.newWaypoint$nrc_client(
                                 var10001,
                                 var10002,
                                 var10003,
                                 var10004,
                                 var10005 as java.lang.String,
                                 wp.getColor(),
                                 XaeroReflect.INSTANCE.purposeFromName$nrc_client(wp.getPurpose().name()),
                                 wp.getYIncluded()
                              )
                              if (var22 != null) {
                                 list.add(var22)
                                 added++
                              }
                           }
                        }

                        if (added != lastMinimapCount) {
                           lastMinimapCount = added
                           MCLoggerKt.nrcDebugLog(logger, "nrc2xaero", "appendMinimap: now injecting $added Voidrix waypoint(s) for dim=$var18")
                        }

                        return
                     }
                  } finally {
                     inAppend = false
                  }
               }
            }
         }
      }
   }

   @JvmStatic
   public fun appendWorldmapWaypoints(list: MutableList<Any>, support: Any) {
      if (!inAppend) {
         if (WaypointModule.INSTANCE.isEnabled()) {
            if (XaeroReflect.INSTANCE.isAvailable) {
               inAppend = true

               try {
                  val dimDiv: Double = XaeroReflect.INSTANCE.dimDivOf(support)
                  val var10000: XaeroWorld = XaeroReflect.INSTANCE.currentWorldCached()
                  if (var10000 == null) {
                     return
                  }

                  val var31: java.lang.String = var10000.dimKey
                  if (var31 == null) {
                     return
                  }

                  val nrcWaypoints: java.util.List = NrcWaypointSource.INSTANCE.list(var31)
                  if (!nrcWaypoints.isEmpty()) {
                     var added: Int = 0

                     for (wp in nrcWaypoints) {
                        if (!wp.getDisabled()) {
                           val var32: XaeroReflect = XaeroReflect.INSTANCE
                           val var10001: Int = wp.getX()
                           var var10002: Int = wp.getY()
                           var var10003: Int = wp.getZ()
                           val var10004: java.lang.String = wp.getName()
                           val raw: java.lang.CharSequence = wp.getInitials()
                           var var10005: java.lang.CharSequence
                           if (StringsKt.isBlank(raw)) {
                              run label172@{
                                 val var33: Character = StringsKt.firstOrNull(wp.getName())
                                 if (var33 != null) {
                                    var34 = var33.toString()
                                    if (var34 != null) {
                                       return@label172
                                    }
                                 }

                                 var34 = "?"
                              }

                              var10005 = var34
                           } else {
                              var10005 = raw
                           }

                           val minimapWp: Any = var32.newWaypoint$nrc_client(
                              var10001,
                              var10002,
                              var10003,
                              var10004,
                              var10005,
                              wp.getColor(),
                              XaeroReflect.INSTANCE.purposeFromName$nrc_client(wp.getPurpose().name()),
                              wp.getYIncluded()
                           )
                           val var35: XaeroReflect = XaeroReflect.INSTANCE
                           var var39: Any = minimapWp
                           var10002 = wp.getX()
                           var10003 = wp.getY()
                           val var42: Int = wp.getZ()
                           var10005 = wp.getName()
                           val var13: java.lang.CharSequence = wp.getInitials()
                           val var10006: java.lang.CharSequence
                           if (StringsKt.isBlank(var13)) {
                              run label177@{
                                 val var36: Character = StringsKt.firstOrNull(wp.getName())
                                 if (var36 != null) {
                                    var37 = var36.toString()
                                    if (var37 != null) {
                                       return@label177
                                    }
                                 }

                                 var37 = "?"
                              }

                              var39 = minimapWp
                              var10006 = var37
                           } else {
                              var10006 = var13
                           }

                           val var38: Any = var35.newWorldmapWaypoint(
                              var39,
                              var10002,
                              var10003,
                              var42,
                              var10005,
                              var10006 as java.lang.String,
                              wp.getColor(),
                              0,
                              wp.getDisabled(),
                              "Voidrix",
                              wp.getYIncluded(),
                              dimDiv
                           )
                           if (var38 != null) {
                              list.add(var38)
                              added++
                           }
                        }
                     }

                     if (added != lastWorldmapCount) {
                        lastWorldmapCount = added
                        MCLoggerKt.nrcDebugLog(logger, "nrc2xaero", "appendWorldmap: now injecting $added Voidrix waypoint(s) for dim=$var31 (dimDiv=$dimDiv)")
                     }

                     return
                  }
               } finally {
                  inAppend = false
               }
            }
         }
      }
   }
}
