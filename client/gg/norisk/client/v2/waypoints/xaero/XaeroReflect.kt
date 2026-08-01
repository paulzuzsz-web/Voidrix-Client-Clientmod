package gg.norisk.client.v2.waypoints.xaero

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.loader.ModLoadingHelper
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nXaeroReflect.kt\nKotlin\n*S Kotlin\n*F\n+ 1 XaeroReflect.kt\ngg/norisk/client/v2/waypoints/xaero/XaeroReflect\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 4 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n+ 5 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,774:1\n1#2:775\n1310#3,2:776\n1310#3,2:778\n1310#3,2:804\n1310#3,2:806\n1310#3,2:808\n1310#3,2:810\n1310#3,2:812\n1310#3,2:814\n1310#3,2:816\n1310#3,2:818\n1310#3,2:820\n1310#3,2:822\n1310#3,2:824\n1310#3,2:826\n68#4:780\n68#4:785\n68#4:788\n68#4:789\n68#4:800\n68#4:801\n68#4:802\n68#4:803\n1563#5:781\n1634#5,3:782\n295#5,2:786\n1563#5:790\n1634#5,3:791\n295#5,2:794\n1563#5:796\n1634#5,3:797\n*S KotlinDebug\n*F\n+ 1 XaeroReflect.kt\ngg/norisk/client/v2/waypoints/xaero/XaeroReflect\n*L\n227#1:776,2\n237#1:778,2\n83#1:804,2\n89#1:806,2\n96#1:808,2\n104#1:810,2\n112#1:812,2\n116#1:814,2\n150#1:816,2\n170#1:818,2\n186#1:820,2\n204#1:822,2\n205#1:824,2\n626#1:826,2\n349#1:780\n373#1:785\n420#1:788\n484#1:789\n543#1:800\n555#1:801\n561#1:802\n694#1:803\n370#1:781\n370#1:782,3\n387#1:786,2\n499#1:790\n499#1:791,3\n511#1:794,2\n531#1:796\n531#1:797,3\n*E\n"])
public object XaeroReflect {
   private const val LOG_TAG: String = "xaero-reflect"
   private final val logger: Logger = MCLogger.getLogger("Voidrix-XaeroReflect")
   private const val MINIMAP_MODULE_ID: String = "xaerominimap:minimap"

   private final val c_hudMod: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.common.HudMod")
   })
      private final get() {
         return c_hudMod$delegate.getValue() as Class<*>
      }


   private final val c_hudModule: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.module.HudModule")
   })
      private final get() {
         return c_hudModule$delegate.getValue() as Class<*>
      }


   private final val c_minimapSession: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.module.MinimapSession")
   })
      private final get() {
         return c_minimapSession$delegate.getValue() as Class<*>
      }


   private final val c_minimapWorldManager: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.world.MinimapWorldManager")
   })
      private final get() {
         return c_minimapWorldManager$delegate.getValue() as Class<*>
      }


   private final val c_minimapWorld: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.world.MinimapWorld")
   })
      private final get() {
         return c_minimapWorld$delegate.getValue() as Class<*>
      }


   private final val c_waypointSet: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.waypoint.set.WaypointSet")
   })
      private final get() {
         return c_waypointSet$delegate.getValue() as Class<*>
      }


   private final val c_waypoint: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.common.minimap.waypoints.Waypoint")
   })
      private final get() {
         return c_waypoint$delegate.getValue() as Class<*>
      }


   private final val c_waypointColor: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.waypoint.WaypointColor")
   })
      private final get() {
         return c_waypointColor$delegate.getValue() as Class<*>
      }


   private final val c_waypointPurpose: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.waypoint.WaypointPurpose")
   })
      private final get() {
         return c_waypointPurpose$delegate.getValue() as Class<*>
      }


   private final val c_waypointVisibilityType: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.waypoint.WaypointVisibilityType")
   })
      private final get() {
         return c_waypointVisibilityType$delegate.getValue() as Class<*>
      }


   private final val c_wmWaypoint: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.map.mods.gui.Waypoint")
   })
      private final get() {
         return c_wmWaypoint$delegate.getValue() as Class<*>
      }


   private final val c_supportXaeroMinimap: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.map.mods.SupportXaeroMinimap")
   })
      private final get() {
         return c_supportXaeroMinimap$delegate.getValue() as Class<*>
      }


   private final val m_support_getDimDiv: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_supportXaeroMinimap, "getDimDiv")
   })
      private final get() {
         return m_support_getDimDiv$delegate.getValue() as Method
      }


   private final val c_resourceKey: Class<out Any>? by LazyKt.lazy(lambda_13@{ 
      val var10000: Method = INSTANCE.m_minimapWorld_getDimId
      if (var10000 != null) {
         val var0: Class = var10000.getReturnType()
         if (var0 != null) {
            return@lambda_13 var0
         }
      }

      var var1: Class = INSTANCE.tryClass("net.minecraft.resources.ResourceKey")
      if (var1 == null) {
         var1 = INSTANCE.tryClass("net.minecraft.util.registry.RegistryKey")
         if (var1 == null) {
            var1 = INSTANCE.tryClass("net.minecraft.class_5321")
         }
      }

      return@lambda_13 var1
   })
      private final get() {
         return c_resourceKey$delegate.getValue() as Class<out Object>
      }


   private final val c_resourceLocation: Class<out Any>? by LazyKt.lazy(lambda_14@{ 
      val var10000: Method = INSTANCE.m_hudModule_getId
      if (var10000 != null) {
         val var0: Class = var10000.getReturnType()
         if (var0 != null) {
            return@lambda_14 var0
         }
      }

      var var1: Class = INSTANCE.tryClass("net.minecraft.resources.ResourceLocation")
      if (var1 == null) {
         var1 = INSTANCE.tryClass("net.minecraft.util.Identifier")
         if (var1 == null) {
            var1 = INSTANCE.tryClass("net.minecraft.class_2960")
         }
      }

      return@lambda_14 var1
   })
      private final get() {
         return c_resourceLocation$delegate.getValue() as Class<out Object>
      }


   private final val m_hudMod_getHud: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_hudMod, "getHud")
   })
      private final get() {
         return m_hudMod_getHud$delegate.getValue() as Method
      }


   private final val m_hud_getModuleManager: Method? by LazyKt.lazy({ 
      val var10000: XaeroReflect = INSTANCE
      val var10001: Method = INSTANCE.m_hudMod_getHud
      var10000.tryMethod0(if (var10001 != null) var10001.getReturnType() else null, "getModuleManager")
   })
      private final get() {
         return m_hud_getModuleManager$delegate.getValue() as Method
      }


   private final val m_moduleManager_getModules: Method? by LazyKt.lazy({ 
      val var10000: XaeroReflect = INSTANCE
      val var10001: Method = INSTANCE.m_hud_getModuleManager
      var10000.tryMethod0(if (var10001 != null) var10001.getReturnType() else null, "getModules")
   })
      private final get() {
         return m_moduleManager_getModules$delegate.getValue() as Method
      }


   private final val m_hudModule_getId: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_hudModule, "getId")
   })
      private final get() {
         return m_hudModule_getId$delegate.getValue() as Method
      }


   private final val m_hudModule_getCurrentSession: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_hudModule, "getCurrentSession")
   })
      private final get() {
         return m_hudModule_getCurrentSession$delegate.getValue() as Method
      }


   private final val m_minimapSession_getWorldManager: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapSession, "getWorldManager")
   })
      private final get() {
         return m_minimapSession_getWorldManager$delegate.getValue() as Method
      }


   private final val m_minimapSession_getWorldManagerIO: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapSession, "getWorldManagerIO")
   })
      private final get() {
         return m_minimapSession_getWorldManagerIO$delegate.getValue() as Method
      }


   private final val m_worldManagerIO_saveWorld: Method? by LazyKt.lazy(
      { 
         var var12: Method
         run label61@{
            var12 = INSTANCE.m_minimapSession_getWorldManagerIO
            val ioClass: Class = if (var12 != null) var12.getReturnType() else null
            if (ioClass != null) {
               val var11: Array<Any> = ioClass.getMethods()
               if (var11 != null) {
                  for (`element$iv` in var11) {
                     if (`element$iv`.getName() == "saveWorld"
                        && `element$iv`.getParameterCount() == 1
                        && `element$iv`.getParameterTypes()[0] == INSTANCE.c_minimapWorld) {
                        var12 = (Method)`element$iv`
                        return@label61
                     }
                  }

                  var12 = null
                  return@label61
               }
            }

            var12 = null
         }

         logLookup$default(INSTANCE, "MinimapWorldManagerIO.saveWorld(MinimapWorld)", var12 != null, null, 4, null)
         var12
      }
   )
      private final get() {
         return m_worldManagerIO_saveWorld$delegate.getValue() as Method
      }


   private final val m_worldManager_getCurrentWorld: Method? by LazyKt.lazy({ 
      var var11: Method
      run label53@{
         val var10000: Class = INSTANCE.c_minimapWorldManager
         if (var10000 != null) {
            val var10: Array<Any> = var10000.getMethods()
            if (var10 != null) {
               for (`element$iv` in var10) {
                  if (`element$iv`.getName() == "getCurrentWorld" && `element$iv`.getParameterCount() == 0) {
                     var11 = (Method)`element$iv`
                     return@label53
                  }
               }

               var11 = null
               return@label53
            }
         }

         var11 = null
      }

      logLookup$default(INSTANCE, "MinimapWorldManager.getCurrentWorld()", var11 != null, null, 4, null)
      var11
   })
      private final get() {
         return m_worldManager_getCurrentWorld$delegate.getValue() as Method
      }


   private final val m_worldManager_getRootContainers: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapWorldManager, "getRootContainers")
   })
      private final get() {
         return m_worldManager_getRootContainers$delegate.getValue() as Method
      }


   private final val m_worldManager_getAutoRootContainer: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapWorldManager, "getAutoRootContainer")
   })
      private final get() {
         return m_worldManager_getAutoRootContainer$delegate.getValue() as Method
      }


   private final val m_container_getAllWorldsIterable: Method? by LazyKt.lazy({ 
      var var12: Method
      run label44@{
         var12 = INSTANCE.m_worldManager_getAutoRootContainer
         if (var12 != null) {
            val var10: Class = var12.getReturnType()
            if (var10 != null) {
               val var11: Array<Any> = var10.getMethods()
               if (var11 != null) {
                  for (`element$iv` in var11) {
                     if (`element$iv`.getName() == "getAllWorldsIterable") {
                        var12 = (Method)`element$iv`
                        return@label44
                     }
                  }

                  var12 = null
                  return@label44
               }
            }
         }

         var12 = null
      }

      logLookup$default(INSTANCE, "MinimapWorldContainer.getAllWorldsIterable()", var12 != null, null, 4, null)
      var12
   })
      private final get() {
         return m_container_getAllWorldsIterable$delegate.getValue() as Method
      }


   private final val m_minimapWorld_getDimId: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapWorld, "getDimId")
   })
      private final get() {
         return m_minimapWorld_getDimId$delegate.getValue() as Method
      }


   private final val m_minimapWorld_getIterableWaypointSets: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapWorld, "getIterableWaypointSets")
   })
      private final get() {
         return m_minimapWorld_getIterableWaypointSets$delegate.getValue() as Method
      }


   private final val m_minimapWorld_getCurrentWaypointSet: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_minimapWorld, "getCurrentWaypointSet")
   })
      private final get() {
         return m_minimapWorld_getCurrentWaypointSet$delegate.getValue() as Method
      }


   private final val m_minimapWorld_addWaypointSet_String: Method? by LazyKt.lazy(
      { 
         var var11: Method
         run label55@{
            val var10000: Class = INSTANCE.c_minimapWorld
            if (var10000 != null) {
               val var10: Array<Any> = var10000.getMethods()
               if (var10 != null) {
                  for (`element$iv` in var10) {
                     if (`element$iv`.getName() == "addWaypointSet"
                        && `element$iv`.getParameterCount() == 1
                        && `element$iv`.getParameterTypes()[0] == java.lang.String::class.java) {
                        var11 = (Method)`element$iv`
                        return@label55
                     }
                  }

                  var11 = null
                  return@label55
               }
            }

            var11 = null
         }

         logLookup$default(INSTANCE, "MinimapWorld.addWaypointSet(String)", var11 != null, null, 4, null)
         var11
      }
   )
      private final get() {
         return m_minimapWorld_addWaypointSet_String$delegate.getValue() as Method
      }


   private final val m_waypointSet_getName: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypointSet, "getName")
   })
      private final get() {
         return m_waypointSet_getName$delegate.getValue() as Method
      }


   private final val m_waypointSet_getWaypoints: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypointSet, "getWaypoints")
   })
      private final get() {
         return m_waypointSet_getWaypoints$delegate.getValue() as Method
      }


   private final val m_waypointSet_clear: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypointSet, "clear")
   })
      private final get() {
         return m_waypointSet_clear$delegate.getValue() as Method
      }


   private final val m_waypointSet_add: Method? by LazyKt.lazy({ 
      var var11: Method
      run label55@{
         val var10000: Class = INSTANCE.c_waypointSet
         if (var10000 != null) {
            val var10: Array<Any> = var10000.getMethods()
            if (var10 != null) {
               for (`element$iv` in var10) {
                  if (`element$iv`.getName() == "add" && `element$iv`.getParameterCount() == 1 && `element$iv`.getParameterTypes()[0] == INSTANCE.c_waypoint) {
                     var11 = (Method)`element$iv`
                     return@label55
                  }
               }

               var11 = null
               return@label55
            }
         }

         var11 = null
      }

      logLookup$default(INSTANCE, "WaypointSet.add(Waypoint)", var11 != null, null, 4, null)
      var11
   })
      private final get() {
         return m_waypointSet_add$delegate.getValue() as Method
      }


   private final val m_waypointSet_remove: Method? by LazyKt.lazy(
      { 
         var var11: Method
         run label55@{
            val var10000: Class = INSTANCE.c_waypointSet
            if (var10000 != null) {
               val var10: Array<Any> = var10000.getMethods()
               if (var10 != null) {
                  for (`element$iv` in var10) {
                     if (`element$iv`.getName() == "remove"
                        && `element$iv`.getParameterCount() == 1
                        && `element$iv`.getParameterTypes()[0] == INSTANCE.c_waypoint) {
                        var11 = (Method)`element$iv`
                        return@label55
                     }
                  }

                  var11 = null
                  return@label55
               }
            }

            var11 = null
         }

         logLookup$default(INSTANCE, "WaypointSet.remove(Waypoint)", var11 != null, null, 4, null)
         var11
      }
   )
      private final get() {
         return m_waypointSet_remove$delegate.getValue() as Method
      }


   private final val m_wp_getX: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getX")
   })
      private final get() {
         return m_wp_getX$delegate.getValue() as Method
      }


   private final val m_wp_getY: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getY")
   })
      private final get() {
         return m_wp_getY$delegate.getValue() as Method
      }


   private final val m_wp_getZ: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getZ")
   })
      private final get() {
         return m_wp_getZ$delegate.getValue() as Method
      }


   private final val m_wp_getName: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getName")
   })
      private final get() {
         return m_wp_getName$delegate.getValue() as Method
      }


   private final val m_wp_getInitials: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getInitials")
   })
      private final get() {
         return m_wp_getInitials$delegate.getValue() as Method
      }


   private final val m_wp_getColor: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getColor")
   })
      private final get() {
         return m_wp_getColor$delegate.getValue() as Method
      }


   private final val m_wp_getActualColor: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getActualColor")
   })
      private final get() {
         return m_wp_getActualColor$delegate.getValue() as Method
      }


   private final val m_wp_getWaypointColor: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getWaypointColor")
   })
      private final get() {
         return m_wp_getWaypointColor$delegate.getValue() as Method
      }


   private final val m_wp_getPurpose: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getPurpose")
   })
      private final get() {
         return m_wp_getPurpose$delegate.getValue() as Method
      }


   private final val m_wp_getVisibility: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getVisibility")
   })
      private final get() {
         return m_wp_getVisibility$delegate.getValue() as Method
      }


   private final val m_wp_isDisabled: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "isDisabled")
   })
      private final get() {
         return m_wp_isDisabled$delegate.getValue() as Method
      }


   private final val m_wp_isYIncluded: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "isYIncluded")
   })
      private final get() {
         return m_wp_isYIncluded$delegate.getValue() as Method
      }


   private final val m_wp_getCreatedAt: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypoint, "getCreatedAt")
   })
      private final get() {
         return m_wp_getCreatedAt$delegate.getValue() as Method
      }


   private final val m_wp_setX: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setX", int.class)
   })
      private final get() {
         return m_wp_setX$delegate.getValue() as Method
      }


   private final val m_wp_setY: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setY", int.class)
   })
      private final get() {
         return m_wp_setY$delegate.getValue() as Method
      }


   private final val m_wp_setZ: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setZ", int.class)
   })
      private final get() {
         return m_wp_setZ$delegate.getValue() as Method
      }


   private final val m_wp_setName: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setName", java.lang.String.class)
   })
      private final get() {
         return m_wp_setName$delegate.getValue() as Method
      }


   private final val m_wp_setInitials: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setInitials", java.lang.String.class)
   })
      private final get() {
         return m_wp_setInitials$delegate.getValue() as Method
      }


   private final val m_wp_setColor: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setColor", int.class)
   })
      private final get() {
         return m_wp_setColor$delegate.getValue() as Method
      }


   private final val m_wp_setWaypointColor: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setWaypointColor", INSTANCE.c_waypointColor)
   })
      private final get() {
         return m_wp_setWaypointColor$delegate.getValue() as Method
      }


   private final val m_wp_setPurpose: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setPurpose", INSTANCE.c_waypointPurpose)
   })
      private final get() {
         return m_wp_setPurpose$delegate.getValue() as Method
      }


   private final val m_wp_setVisibility: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setVisibility", INSTANCE.c_waypointVisibilityType)
   })
      private final get() {
         return m_wp_setVisibility$delegate.getValue() as Method
      }


   private final val m_wp_setDisabled: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setDisabled", boolean.class)
   })
      private final get() {
         return m_wp_setDisabled$delegate.getValue() as Method
      }


   private final val m_wp_setYIncluded: Method? by LazyKt.lazy({ 
      INSTANCE.trySetter(INSTANCE.c_waypoint, "setYIncluded", boolean.class)
   })
      private final get() {
         return m_wp_setYIncluded$delegate.getValue() as Method
      }


   private final val ctor_waypoint: Constructor<*>? by LazyKt.lazy(
      { 
         var var10: Constructor
         run label71@{
            val var10000: Class = INSTANCE.c_waypoint
            if (var10000 != null) {
               val var9: Array<Any> = var10000.getConstructors()
               if (var9 != null) {
                  for (`element$iv` in var9) {
                     if (`element$iv`.getParameterCount() == 9
                        && `element$iv`.getParameterTypes()[0] == Int::class.javaPrimitiveType
                        && `element$iv`.getParameterTypes()[5] == INSTANCE.c_waypointColor
                        && `element$iv`.getParameterTypes()[6] == INSTANCE.c_waypointPurpose) {
                        var10 = (Constructor)`element$iv`
                        return@label71
                     }
                  }

                  var10 = null
                  return@label71
               }
            }

            var10 = null
         }

         logLookup$default(INSTANCE, "Waypoint.<init>(int*3,String*2,WaypointColor,WaypointPurpose,boolean*2)", var10 != null, null, 4, null)
         if (var10 == null) {
            run label75@{
               val var11: Class = INSTANCE.c_waypoint
               if (var11 != null) {
                  val var12: Array<Constructor> = var11.getConstructors()
                  if (var12 != null) {
                     var13 = ArraysKt.joinToString$default(var12, "\n    ", null, null, 0, null, { it: Constructor ->
                        val var10000: java.lang.String = it.toString()
                        var10000 as java.lang.CharSequence
                     }, 30, null)
                     if (var13 != null) {
                        return@label75
                     }
                  }
               }

               var13 = "(class not loaded)"
            }

            MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Waypoint ctors available:\n    $var13")
         }

         var10
      }
   )
      private final get() {
         return ctor_waypoint$delegate.getValue() as Constructor<*>
      }


   private final val m_color_getHex: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_waypointColor, "getHex")
   })
      private final get() {
         return m_color_getHex$delegate.getValue() as Method
      }


   private final val ctor_wmWaypoint: Constructor<*>? by LazyKt.lazy(
      { 
         var var9: Constructor
         run label55@{
            val var10000: Class = INSTANCE.c_wmWaypoint
            if (var10000 != null) {
               val var8: Array<Any> = var10000.getConstructors()
               if (var8 != null) {
                  for (`element$iv` in var8) {
                     if (`element$iv`.getParameterCount() == 12
                        && `element$iv`.getParameterTypes()[1] == Int::class.javaPrimitiveType
                        && `element$iv`.getParameterTypes()[11] == java.lang.Double::class.javaPrimitiveType) {
                        var9 = (Constructor)`element$iv`
                        return@label55
                     }
                  }

                  var9 = null
                  return@label55
               }
            }

            var9 = null
         }

         logLookup$default(INSTANCE, "WM-Waypoint.<init>(12-arg)", var9 != null, null, 4, null)
         var9
      }
   )
      private final get() {
         return ctor_wmWaypoint$delegate.getValue() as Constructor<*>
      }


   private final val m_wmWp_getSetName: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_wmWaypoint, "getSetName")
   })
      private final get() {
         return m_wmWp_getSetName$delegate.getValue() as Method
      }


   private final val m_wmWp_getName: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_wmWaypoint, "getName")
   })
      private final get() {
         return m_wmWp_getName$delegate.getValue() as Method
      }


   private final val m_wmWp_getX: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_wmWaypoint, "getX")
   })
      private final get() {
         return m_wmWp_getX$delegate.getValue() as Method
      }


   private final val m_wmWp_getY: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_wmWaypoint, "getY")
   })
      private final get() {
         return m_wmWp_getY$delegate.getValue() as Method
      }


   private final val m_wmWp_getZ: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_wmWaypoint, "getZ")
   })
      private final get() {
         return m_wmWp_getZ$delegate.getValue() as Method
      }


   private final val m_color_values: Method? by LazyKt.lazy({ 
      var var11: Method
      run label53@{
         val var10000: Class = INSTANCE.c_waypointColor
         if (var10000 != null) {
            val var10: Array<Any> = var10000.getMethods()
            if (var10 != null) {
               for (`element$iv` in var10) {
                  if (`element$iv`.getName() == "values" && `element$iv`.getParameterCount() == 0) {
                     var11 = (Method)`element$iv`
                     return@label53
                  }
               }

               var11 = null
               return@label53
            }
         }

         var11 = null
      }

      logLookup$default(INSTANCE, "WaypointColor.values()", var11 != null, null, 4, null)
      var11
   })
      private final get() {
         return m_color_values$delegate.getValue() as Method
      }


   private final val m_resourceKey_location: Method? by LazyKt.lazy(
      lambda_88@{ 
         val rl: Class = INSTANCE.c_resourceLocation
         val var10000: Class = INSTANCE.c_resourceKey
         if (var10000 != null) {
            val var20: Array<Method> = var10000.getMethods()
            if (var20 != null) {
               val knownNames: java.util.Set = SetsKt.setOf(arrayOf("identifier", "location", "getValue", "method_29177"))
               val chosen: Array<Any> = var20
               var `$i$f$firstOrNull`: Int = 0
               var var7: Int = var20.length

               while (true) {
                  if (`$i$f$firstOrNull` >= var7) {
                     var21 = null
                     break
                  }

                  val `element$iv`: Any = chosen[`$i$f$firstOrNull`]
                  if (chosen[`$i$f$firstOrNull`].getParameterCount() == 0 && knownNames.contains(chosen[`$i$f$firstOrNull`].getName())) {
                     var21 = (Method)`element$iv`
                     break
                  }

                  `$i$f$firstOrNull`++
               }

               var var22: Method = var21
               if (var21 == null) {
                  val `$this$firstOrNull$ivx`: Array<Any> = var20
                  var7 = 0
                  val var17: Int = var20.length

                  while (true) {
                     if (var7 >= var17) {
                        var22 = null
                        break
                     }

                     val var18: Any = `$this$firstOrNull$ivx`[var7]
                     if (`$this$firstOrNull$ivx`[var7].getParameterCount() == 0
                        && rl != null
                        && `$this$firstOrNull$ivx`[var7].getReturnType() == rl
                        && !(`$this$firstOrNull$ivx`[var7].getName() == "registry")) {
                        var22 = (Method)var18
                        break
                     }

                     var7++
                  }
               }

               INSTANCE.logLookup("ResourceKey.<value-location>()", var22 != null, "chosen=${if (var22 != null) var22.getName() else null}")
               return@lambda_88 var22
            }
         }

         return@lambda_88 null
      }
   )
      private final get() {
         return m_resourceKey_location$delegate.getValue() as Method
      }


   private final var availableCache: Boolean?
   private final var cachedWorld: XaeroWorld?
   private final var cachedWorldDirty: Boolean = true

   private final val m_hudMod_getHudConfigs: Method? by LazyKt.lazy({ 
      INSTANCE.tryMethod0(INSTANCE.c_hudMod, "getHudConfigs")
   })
      private final get() {
         return m_hudMod_getHudConfigs$delegate.getValue() as Method
      }


   private final val m_configChannel_getClientConfigManager: Method? by LazyKt.lazy({ 
      val var10000: XaeroReflect = INSTANCE
      val var10001: Method = INSTANCE.m_hudMod_getHudConfigs
      var10000.tryMethod0(if (var10001 != null) var10001.getReturnType() else null, "getClientConfigManager")
   })
      private final get() {
         return m_configChannel_getClientConfigManager$delegate.getValue() as Method
      }


   private final val m_clientConfigManager_getEffective: Method? by LazyKt.lazy({ 
      var var12: Method
      run label56@{
         var12 = INSTANCE.m_configChannel_getClientConfigManager
         if (var12 != null) {
            val var10: Class = var12.getReturnType()
            if (var10 != null) {
               val var11: Array<Any> = var10.getMethods()
               if (var11 != null) {
                  for (`element$iv` in var11) {
                     if (`element$iv`.getName() == "getEffective" && `element$iv`.getParameterCount() == 1) {
                        var12 = (Method)`element$iv`
                        return@label56
                     }
                  }

                  var12 = null
                  return@label56
               }
            }
         }

         var12 = null
      }

      logLookup$default(INSTANCE, "ClientConfigManager.getEffective(ConfigOption)", var12 != null, null, 4, null)
      var12
   })
      private final get() {
         return m_clientConfigManager_getEffective$delegate.getValue() as Method
      }


   private final val c_minimapProfiledOptions: Class<*>? by LazyKt.lazy({ 
      INSTANCE.tryClass("xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions")
   })
      private final get() {
         return c_minimapProfiledOptions$delegate.getValue() as Class<*>
      }


   private final val f_autoWaypointsOnDeath: Field? by LazyKt.lazy({ 
      val var0: XaeroReflect = INSTANCE

      var it: Any
      try {
         val var10000: Class = var0.c_minimapProfiledOptions
         it = Result.constructor_impl/* $VF was: constructor-impl */(if (var10000 != null) var10000.getField("AUTO_WAYPOINTS_ON_DEATH") else null)
      } catch (var3: java.lang.Throwable) {
         it = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var3))
      }

      val var5: Any = if (Result.isFailure_impl/* $VF was: isFailure-impl */(it)) null else it
      logLookup$default(INSTANCE, "MinimapProfiledConfigOptions.AUTO_WAYPOINTS_ON_DEATH", var5 as Field != null, null, 4, null)
      var5 as Field
   })
      private final get() {
         return f_autoWaypointsOnDeath$delegate.getValue() as Field
      }


   private fun tryClass(name: String): Class<*>? {
      val var3: XaeroReflect = this

      var `$this$tryClass_u24lambda_u2489`: XaeroReflect
      try {
         `$this$tryClass_u24lambda_u2489` = var3
         `$this$tryClass_u24lambda_u2489` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(Class.forName(name))
      } catch (var6: java.lang.Throwable) {
         `$this$tryClass_u24lambda_u2489` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var6))
      }

      val found: Class = (if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$tryClass_u24lambda_u2489`)) null else `$this$tryClass_u24lambda_u2489`) as Class
      MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Class.forName('$name') -> ${if (found != null) "OK" else "MISSING"}")
      return found
   }

   private fun tryMethod0(clazz: Class<*>?, name: String): Method? {
      if (clazz == null) {
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method lookup '$name' skipped — owning class not resolved")
         return null
      } else {
         var var10000: Method = clazz.getMethods()
         val `$this$firstOrNull$iv`: Array<Any> = var10000 as Array<Any>
         var var6: Int = 0
         val var7: Int = `$this$firstOrNull$iv`.length

         while (true) {
            if (var6 >= var7) {
               var10000 = null
               break
            }

            val `element$iv`: Any = `$this$firstOrNull$iv`[var6]
            if ((`$this$firstOrNull$iv`[var6] as Method).getName() == name && (`$this$firstOrNull$iv`[var6] as Method).getParameterCount() == 0) {
               var10000 = (Method)`element$iv`
               break
            }

            var6++
         }

         val m: Method = var10000
         MCLoggerKt.nrcDebugLog(
            logger,
            "xaero-reflect",
            "Method ${clazz.getSimpleName()}.$name() -> ${if (var10000 as Method != null)
               "OK (returns ${var10000.getReturnType().getSimpleName()})"
               else
               "MISSING"}"
         )
         return m
      }
   }

   private fun trySetter(clazz: Class<*>?, name: String, paramType: Class<*>?): Method? {
      if (clazz != null && paramType != null) {
         var var10000: Method = clazz.getMethods()
         val `$this$firstOrNull$iv`: Array<Any> = var10000 as Array<Any>
         var var7: Int = 0
         val var8: Int = `$this$firstOrNull$iv`.length

         while (true) {
            if (var7 >= var8) {
               var10000 = null
               break
            }

            val `element$iv`: Any = `$this$firstOrNull$iv`[var7]
            if ((`$this$firstOrNull$iv`[var7] as Method).getName() == name
               && (`$this$firstOrNull$iv`[var7] as Method).getParameterCount() == 1
               && (`$this$firstOrNull$iv`[var7] as Method).getParameterTypes()[0] == paramType) {
               var10000 = (Method)`element$iv`
               break
            }

            var7++
         }

         val m: Method = var10000
         MCLoggerKt.nrcDebugLog(
            logger,
            "xaero-reflect",
            "Setter ${clazz.getSimpleName()}.$name(${paramType.getSimpleName()}) -> ${if (var10000 as Method != null) "OK" else "MISSING"}"
         )
         return m
      } else {
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Setter lookup '$name' skipped — owning class or param type not resolved")
         return null
      }
   }

   private fun logLookup(label: String, ok: Boolean, detail: String = "") {
      MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Lookup $label -> ${if (ok) "OK" else "MISSING"}${if (detail.length() > 0) " ($detail)" else ""}")
   }

   public final val isAvailable: Boolean
      public final get() {
         if (availableCache != null) {
            return availableCache
         } else {
            val var5: Boolean = ModLoadingHelper.INSTANCE.isModLoaded("xaerominimap")
            val ok: Boolean = var5 && this.c_hudMod != null && this.c_waypoint != null
            availableCache = ok
            MCLoggerKt.nrcDebugLog(
               logger, "xaero-reflect", "isAvailable=$ok (modLoaded=$var5, hudMod=${this.c_hudMod != null}, waypoint=${this.c_waypoint != null}) [cached]"
            )
            return ok
         }
      }


   @JvmStatic
   public fun invalidateCachedWorld() {
      cachedWorldDirty = true
      cachedWorld = null
   }

   public fun currentWorldCached(): XaeroWorld? {
      if (!cachedWorldDirty) {
         return cachedWorld
      } else {
         val fresh: XaeroWorld = this.currentWorld()
         cachedWorld = fresh
         cachedWorldDirty = false
         return fresh
      }
   }

   public fun dumpResolution() {
      var var13: java.lang.String
      var var10000: Logger
      run label244@{
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "=== XaeroReflect resolution dump ===")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "xaerominimap loaded: ${ModLoadingHelper.INSTANCE.isModLoaded("xaerominimap")}")
         var10000 = logger
         val var10002: Class = this.c_hudMod
         if (var10002 != null) {
            var13 = var10002.getName()
            if (var13 != null) {
               return@label244
            }
         }

         var13 = "MISSING"
      }

      run label247@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class HudMod: $var13")
         var10000 = logger
         val var14: Class = this.c_hudModule
         if (var14 != null) {
            var13 = var14.getName()
            if (var13 != null) {
               return@label247
            }
         }

         var13 = "MISSING"
      }

      run label250@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class HudModule: $var13")
         var10000 = logger
         val var16: Class = this.c_minimapSession
         if (var16 != null) {
            var13 = var16.getName()
            if (var13 != null) {
               return@label250
            }
         }

         var13 = "MISSING"
      }

      run label253@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class MinimapSession: $var13")
         var10000 = logger
         val var18: Class = this.c_minimapWorldManager
         if (var18 != null) {
            var13 = var18.getName()
            if (var13 != null) {
               return@label253
            }
         }

         var13 = "MISSING"
      }

      run label256@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class MinimapWorldManager: $var13")
         var10000 = logger
         val var20: Class = this.c_minimapWorld
         if (var20 != null) {
            var13 = var20.getName()
            if (var13 != null) {
               return@label256
            }
         }

         var13 = "MISSING"
      }

      run label259@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class MinimapWorld: $var13")
         var10000 = logger
         val var22: Class = this.c_waypointSet
         if (var22 != null) {
            var13 = var22.getName()
            if (var13 != null) {
               return@label259
            }
         }

         var13 = "MISSING"
      }

      run label262@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class WaypointSet: $var13")
         var10000 = logger
         val var24: Class = this.c_waypoint
         if (var24 != null) {
            var13 = var24.getName()
            if (var13 != null) {
               return@label262
            }
         }

         var13 = "MISSING"
      }

      run label265@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class Waypoint: $var13")
         var10000 = logger
         val var26: Class = this.c_waypointColor
         if (var26 != null) {
            var13 = var26.getName()
            if (var13 != null) {
               return@label265
            }
         }

         var13 = "MISSING"
      }

      run label268@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class WaypointColor: $var13")
         var10000 = logger
         val var28: Class = this.c_waypointPurpose
         if (var28 != null) {
            var13 = var28.getName()
            if (var13 != null) {
               return@label268
            }
         }

         var13 = "MISSING"
      }

      run label271@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class WaypointPurpose: $var13")
         var10000 = logger
         val var30: Class = this.c_waypointVisibilityType
         if (var30 != null) {
            var13 = var30.getName()
            if (var13 != null) {
               return@label271
            }
         }

         var13 = "MISSING"
      }

      run label274@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class WaypointVisibilityType: $var13")
         var10000 = logger
         val var32: Class = this.c_resourceKey
         if (var32 != null) {
            var13 = var32.getName()
            if (var13 != null) {
               return@label274
            }
         }

         var13 = "MISSING"
      }

      run label277@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class ResourceKey: $var13")
         var10000 = logger
         val var34: Class = this.c_resourceLocation
         if (var34 != null) {
            var13 = var34.getName()
            if (var13 != null) {
               return@label277
            }
         }

         var13 = "MISSING"
      }

      run label280@{
         MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Class ResourceLocation: $var13")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method HudMod.getHud: ${this.m_hudMod_getHud != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method Hud.getModuleManager: ${this.m_hud_getModuleManager != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method ModuleManager.getModules: ${this.m_moduleManager_getModules != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method HudModule.getId: ${this.m_hudModule_getId != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method HudModule.getCurrentSession: ${this.m_hudModule_getCurrentSession != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapSession.getWorldManager: ${this.m_minimapSession_getWorldManager != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapSession.getWorldManagerIO: ${this.m_minimapSession_getWorldManagerIO != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorldManagerIO.saveWorld(MinimapWorld): ${this.m_worldManagerIO_saveWorld != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorldManager.getCurrentWorld: ${this.m_worldManager_getCurrentWorld != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorldManager.getAutoRootContainer: ${this.m_worldManager_getAutoRootContainer != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorldContainer.getAllWorldsIterable: ${this.m_container_getAllWorldsIterable != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorld.getDimId: ${this.m_minimapWorld_getDimId != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorld.getIterableWaypointSets: ${this.m_minimapWorld_getIterableWaypointSets != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorld.getCurrentWaypointSet: ${this.m_minimapWorld_getCurrentWaypointSet != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method MinimapWorld.addWaypointSet(String): ${this.m_minimapWorld_addWaypointSet_String != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method WaypointSet.getName: ${this.m_waypointSet_getName != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method WaypointSet.getWaypoints: ${this.m_waypointSet_getWaypoints != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method WaypointSet.add(Waypoint): ${this.m_waypointSet_add != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method WaypointSet.remove(Waypoint): ${this.m_waypointSet_remove != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Ctor Waypoint(9-arg): ${this.ctor_waypoint != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method WaypointColor.getHex: ${this.m_color_getHex != null}")
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "Method WaypointColor.values(): ${this.m_color_values != null}")
         var10000 = logger
         val var36: Method = this.m_resourceKey_location
         if (var36 != null) {
            var13 = var36.getName()
            if (var13 != null) {
               return@label280
            }
         }

         var13 = "MISSING"
      }

      MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "Method ResourceKey.<location-like>: $var13")
      MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "=== end dump ===")
   }

   public fun currentWorld(): XaeroWorld? {
      val var1: XaeroReflect = this

      var `$this$currentWorld_u24lambda_u2493`: Any
      try {
         val var10000: Any = var1.currentWorldRaw()
         `$this$currentWorld_u24lambda_u2493` = Result.constructor_impl/* $VF was: constructor-impl */(if (var10000 == null) null else XaeroWorld(var10000))
      } catch (var8: java.lang.Throwable) {
         `$this$currentWorld_u24lambda_u2493` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var8))
      }

      val var16: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$currentWorld_u24lambda_u2493`)
      if (var16 != null) {
         if (MCLogger.IS_DEBUG) {
            logger.error("currentWorld() threw", var16)
         }
      }

      return (if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$currentWorld_u24lambda_u2493`)) null else `$this$currentWorld_u24lambda_u2493`) as XaeroWorld
   }

   public fun worldsInCurrentRoot(): List<XaeroWorld> {
      MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "worldsInCurrentRoot() called")
      val var1: XaeroReflect = this

      var it: XaeroReflect
      try {
         it = var1
         var var10000: Any = var1.currentWorldManager()
         if (var10000 == null) {
            MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "worldsInCurrentRoot() -> empty (manager was null)")
            var10000 = CollectionsKt.emptyList()
         } else {
            run label77@{
               var10000 = it.m_worldManager_getAutoRootContainer
               if (var10000 != null) {
                  var10000 = var10000.invoke(var10000)
                  if (var10000 != null) {
                     val var36: Method = it.m_container_getAllWorldsIterable
                     var `$this$nrcError$iv`: java.lang.Iterable = (java.lang.Iterable)(if (var36 != null) var36.invoke(var10000) else null)
                     val var37: java.lang.Iterable = `$this$nrcError$iv` as? java.lang.Iterable
                     if ((`$this$nrcError$iv` as? java.lang.Iterable) == null) {
                        MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "worldsInCurrentRoot() -> empty (getAllWorldsIterable returned non-iterable or null)")
                        var10000 = CollectionsKt.emptyList()
                        return@label77
                     }

                     `$this$nrcError$iv` = CollectionsKt.filterNotNull(var37)
                     val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(`$this$nrcError$iv`, 10))

                     for (`item$iv$iv` in `$this$nrcError$iv`) {
                        `destination$iv$iv`.add(XaeroWorld(`item$iv$iv`))
                     }

                     val var4: java.util.List = `destination$iv$iv` as java.util.List
                     MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "worldsInCurrentRoot() -> ${(`destination$iv$iv` as java.util.List).size()} worlds")
                     var10000 = var4
                     return@label77
                  }
               }

               MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "worldsInCurrentRoot() -> empty (autoRootContainer was null)")
               var10000 = CollectionsKt.emptyList()
            }
         }

         it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(var10000)
      } catch (var18: java.lang.Throwable) {
         it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var18))
      }

      val var38: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it)
      if (var38 != null) {
         if (MCLogger.IS_DEBUG) {
            logger.error("worldsInCurrentRoot() threw", var38)
         }
      }

      return (if (Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it) == null) it else CollectionsKt.emptyList()) as MutableList<XaeroWorld>
   }

   private fun currentWorldRaw(): Any? {
      val var10000: Any = this.currentWorldManager()
      if (var10000 == null) {
         return null
      } else {
         val var2: Method = this.m_worldManager_getCurrentWorld
         return if (var2 != null) var2.invoke(var10000) else null
      }
   }

   private fun currentSessionRaw(): Any? {
      var var10000: Any = this.c_hudMod
      if (var10000 == null) {
         return null
      } else {
         val hudModClass: Class = (Class)var10000
         val manager: XaeroReflect = this

         var modules: XaeroReflect
         try {
            modules = manager
            modules = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(hudModClass.getField("INSTANCE").get(null))
         } catch (var14: java.lang.Throwable) {
            modules = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var14))
         }

         var10000 = if (Result.isFailure_impl/* $VF was: isFailure-impl */(modules)) null else modules
         if (var10000 == null) {
            return null
         } else {
            val var21: Method = this.m_hudMod_getHud
            if (var21 != null) {
               var10000 = var21.invoke(var10000)
               if (var10000 != null) {
                  val var23: Method = this.m_hud_getModuleManager
                  if (var23 != null) {
                     var10000 = var23.invoke(var10000)
                     if (var10000 != null) {
                        val var25: Method = this.m_moduleManager_getModules
                        val var7: Any = if (var25 != null) var25.invoke(var10000) else null
                        val var26: java.lang.Iterable = var7 as? java.lang.Iterable
                        if ((var7 as? java.lang.Iterable) == null) {
                           return null
                        }

                        val var10: java.util.Iterator = var26.iterator()

                        while (true) {
                           if (!var10.hasNext()) {
                              var10000 = null
                              break
                           }

                           var `element$iv`: Any
                           run label111@{
                              `element$iv` = var10.next()
                              if (`element$iv` != null) {
                                 run label107@{
                                    val var27: Method = INSTANCE.m_hudModule_getId
                                    if (var27 != null) {
                                       val var28: Any = var27.invoke(`element$iv`)
                                       if (var28 != null) {
                                          var29 = var28.toString()
                                          return@label107
                                       }
                                    }

                                    var29 = null
                                 }

                                 if (var29 == "xaerominimap:minimap") {
                                    var30 = true
                                    return@label111
                                 }
                              }

                              var30 = false
                           }

                           if (var30) {
                              var10000 = `element$iv`
                              break
                           }
                        }

                        if (var10000 == null) {
                           return null
                        }

                        val var32: Method = this.m_hudModule_getCurrentSession
                        return if (var32 != null) var32.invoke(var10000) else null
                     }
                  }

                  return null
               }
            }

            return null
         }
      }
   }

   private fun currentWorldManager(): Any? {
      val var10000: Any = this.currentSessionRaw()
      if (var10000 == null) {
         return null
      } else {
         val var2: Method = this.m_minimapSession_getWorldManager
         return if (var2 != null) var2.invoke(var10000) else null
      }
   }

   internal fun persistWorld(worldRaw: Any): Boolean {
      var var10000: Any = this.currentSessionRaw()
      if (var10000 == null) {
         val var15: XaeroReflect = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "persistWorld: no session")
         return false
      } else {
         var10000 = this.m_minimapSession_getWorldManagerIO
         if (var10000 != null) {
            var10000 = var10000.invoke(var10000)
            if (var10000 != null) {
               val io: Any = var10000
               var10000 = this.m_worldManagerIO_saveWorld
               if (var10000 == null) {
                  val var22: XaeroReflect = this
                  MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "persistWorld: saveWorld method not resolved")
                  return false
               }

               val save: Method = (Method)var10000
               val `$this$persistWorld_u24lambda_u24104`: XaeroReflect = this

               var it: XaeroReflect
               try {
                  it = `$this$persistWorld_u24lambda_u24104`
                  save.invoke(io, worldRaw)
                  MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "persistWorld: saved ${worldRaw.getClass().getSimpleName()}")
                  it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(true)
               } catch (var12: java.lang.Throwable) {
                  it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var12))
               }

               val var29: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it)
               if (var29 != null) {
                  if (MCLogger.IS_DEBUG) {
                     logger.error("persistWorld threw", var29)
                  }
               }

               return (if (Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it) == null) it else false) as java.lang.Boolean
            }
         }

         val var7: XaeroReflect = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "persistWorld: getWorldManagerIO returned null")
         return false
      }
   }

   internal fun dimKeyOf(dimIdRaw: Any?): String? {
      if (dimIdRaw == null) {
         return null
      } else {
         val var10000: Method = this.m_resourceKey_location
         if (var10000 != null) {
            var var7: Any = var10000.invoke(dimIdRaw)
            if (var7 != null) {
               val str: java.lang.String = var7.toString()
               val colonIdx: Int = StringsKt.indexOf$default(str, ':', 0, false, 6, null)
               if (colonIdx >= 0) {
                  var7 = str.substring(colonIdx + 1)
               } else {
                  var7 = str
               }

               return StringsKt.replace$default((java.lang.String)var7, '/', '_', false, 4, null)
            }
         }

         return null
      }
   }

   internal fun nearestColorEnum(rgb: Int): Any? {
      if (this.c_waypointColor == null) {
         return null
      } else {
         var var10000: Method = this.m_color_values
         var g: Int = (int)(if (var10000 != null) var10000.invoke(null) else null)
         val var21: Array<Any> = g as? Array<Any>
         if ((g as? Array<Any>) == null) {
            return null
         } else {
            val values: Array<Any> = var21
            val r: Int = rgb shr 16 and 255
            g = rgb shr 8 and 255
            val b: Int = rgb and 255
            var bestIdx: Int = 0
            var bestDist: Int = Integer.MAX_VALUE
            var chosen: Int = 0

            for (var10 in var21.length..chosen) {
               val v: Any = values[chosen]
               if (values[chosen] != null) {
                  var10000 = this.m_color_getHex
                  val dg: Any = if (var10000 != null) var10000.invoke(v) else null
                  val var23: Int = dg as? Int
                  if ((dg as? Int) != null) {
                     val hex: Int = var23
                     val dr: Int = (hex shr 16 and 255) - r
                     val var20: Int = (hex shr 8 and 255) - g
                     val d: Int = dr * dr + ((hex shr 8 and 255) - g) * ((hex shr 8 and 255) - g) + ((hex and 255) - b) * ((hex and 255) - b)
                     if (dr * dr + var20 * var20 + ((hex and 255) - b) * ((hex and 255) - b) < bestDist) {
                        bestDist = d
                        bestIdx = chosen
                     }
                  }
               }
            }

            val var19: Any = values[bestIdx]
            val var24: Logger = logger
            val var10002: java.lang.String = Integer.toString(rgb, CharsKt.checkRadix(16))
            MCLoggerKt.nrcDebugLog(
               var24, "xaero-reflect", "nearestColorEnum(#$var10002) -> ${if ((var19 as? java.lang.Enum) != null) (var19 as? java.lang.Enum).name() else null}"
            )
            return var19
         }
      }
   }

   internal fun newWaypoint(x: Int, y: Int, z: Int, name: String, initials: String, color: Int, purpose: Any?, yIncluded: Boolean): Any? {
      var var10000: Constructor = this.ctor_waypoint
      if (var10000 == null) {
         val var22: XaeroReflect = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "newWaypoint: ctor not resolved")
         return null
      } else {
         val ctor: Constructor = var10000
         var10000 = (Constructor)this.nearestColorEnum$nrc_client(color)
         if (var10000 == null) {
            val var25: XaeroReflect = this
            val var36: Logger = logger
            val var10002: java.lang.String = Integer.toString(color, CharsKt.checkRadix(16))
            MCLoggerKt.nrcDebugLog(var36, "xaero-reflect", "newWaypoint: could not derive WaypointColor for rgb=#$var10002")
            return null
         } else {
            val colorEnum: Any = var10000
            var10000 = (Constructor)purpose
            if (purpose == null) {
               var10000 = (Constructor)this.enumValue$nrc_client(this.c_waypointPurpose, "NORMAL")
               if (var10000 == null) {
                  val var28: XaeroReflect = this
                  MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "newWaypoint: could not resolve WaypointPurpose")
                  return null
               }
            }

            val purposeEnum: Any = var10000
            val `$this$newWaypoint_u24lambda_u24110`: XaeroReflect = this

            var `$this$newWaypoint_u24lambda_u24113`: XaeroReflect
            try {
               `$this$newWaypoint_u24lambda_u24113` = `$this$newWaypoint_u24lambda_u24110`
               val `$this$nrcError$iv`: Any = ctor.newInstance(x, y, z, name, initials, colorEnum, purposeEnum, false, yIncluded)
               MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "newWaypoint: constructed Waypoint name='$name' at ($x,$y,$z)")
               `$this$newWaypoint_u24lambda_u24113` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(`$this$nrcError$iv`)
            } catch (var19: java.lang.Throwable) {
               `$this$newWaypoint_u24lambda_u24113` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var19))
            }

            val var35: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$newWaypoint_u24lambda_u24113`)
            if (var35 != null) {
               if (MCLogger.IS_DEBUG) {
                  logger.error("Failed to construct xaero Waypoint", var35)
               }
            }

            return if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$newWaypoint_u24lambda_u24113`)) null else `$this$newWaypoint_u24lambda_u24113`
         }
      }
   }

   internal fun enumValue(clazz: Class<*>?, name: String): Any? {
      if (clazz == null) {
         return null
      } else {
         val var3: XaeroReflect = this

         var `$this$enumValue_u24lambda_u24115`: XaeroReflect
         try {
            `$this$enumValue_u24lambda_u24115` = var3
            `$this$enumValue_u24lambda_u24115` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(java.lang.Enum.valueOf(clazz, name))
         } catch (var7: java.lang.Throwable) {
            `$this$enumValue_u24lambda_u24115` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var7))
         }

         val var10000: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$enumValue_u24lambda_u24115`)
         if (var10000 != null) {
            MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "enumValue(${clazz.getSimpleName()}, '$name') threw: ${var10000.getMessage()}")
         }

         return if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$enumValue_u24lambda_u24115`)) null else `$this$enumValue_u24lambda_u24115`
      }
   }

   internal fun waypointSetsOf(worldRaw: Any): List<XaeroWaypointSet> {
      val var10000: Method = this.m_minimapWorld_getIterableWaypointSets
      val `$i$f$map`: Any = if (var10000 != null) var10000.invoke(worldRaw) else null
      val var14: java.lang.Iterable = `$i$f$map` as? java.lang.Iterable
      if ((`$i$f$map` as? java.lang.Iterable) == null) {
         return CollectionsKt.emptyList()
      } else {
         val `$this$map$iv`: java.lang.Iterable = CollectionsKt.filterNotNull(var14)
         val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(`$this$map$iv`, 10))

         for (`item$iv$iv` in `$this$map$iv`) {
            `destination$iv$iv`.add(XaeroWaypointSet(`item$iv$iv`))
         }

         return `destination$iv$iv` as MutableList<XaeroWaypointSet>
      }
   }

   internal fun currentWaypointSetOf(worldRaw: Any): XaeroWaypointSet? {
      val var10000: Method = this.m_minimapWorld_getCurrentWaypointSet
      if (var10000 != null) {
         val var5: Any = var10000.invoke(worldRaw)
         if (var5 != null) {
            return XaeroWaypointSet(var5)
         }
      }

      val `$this$currentWaypointSetOf_u24lambda_u24118`: XaeroReflect = this
      MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "currentWaypointSetOf: getCurrentWaypointSet returned null")
      return null
   }

   internal fun ensureWaypointSet(worldRaw: Any, setName: String): XaeroWaypointSet? {
      val var6: java.util.Iterator = this.waypointSetsOf$nrc_client(worldRaw).iterator()

      var var10000: Any
      while (true) {
         if (var6.hasNext()) {
            val `element$iv`: Any = var6.next()
            if (!((`element$iv` as XaeroWaypointSet).name == setName)) {
               continue
            }

            var10000 = `element$iv`
            break
         }

         var10000 = null
         break
      }

      val existing: XaeroWaypointSet = var10000 as XaeroWaypointSet
      if (var10000 as XaeroWaypointSet != null) {
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "ensureWaypointSet: '$setName' already exists")
         return existing
      } else {
         val var14: Method = this.m_minimapWorld_addWaypointSet_String
         if (var14 != null) {
            val var15: Any = var14.invoke(worldRaw, setName)
            if (var15 != null) {
               MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "ensureWaypointSet: created '$setName'")
               return XaeroWaypointSet(var15)
            }
         }

         val var12: XaeroReflect = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "ensureWaypointSet: addWaypointSet('$setName') returned null")
         return null
      }
   }

   internal fun dimIdOf(worldRaw: Any): Any? {
      val var10000: Method = this.m_minimapWorld_getDimId
      return if (var10000 != null) var10000.invoke(worldRaw) else null
   }

   internal fun setNameOf(setRaw: Any): String {
      val var10000: Method = this.m_waypointSet_getName
      val var2: Any = if (var10000 != null) var10000.invoke(setRaw) else null
      var var3: java.lang.String = var2 as? java.lang.String
      if ((var2 as? java.lang.String) == null) {
         var3 = "default"
      }

      return var3
   }

   internal fun waypointsOf(setRaw: Any): List<XaeroWaypoint> {
      val var10000: Method = this.m_waypointSet_getWaypoints
      val `$i$f$map`: Any = if (var10000 != null) var10000.invoke(setRaw) else null
      val var14: java.lang.Iterable = `$i$f$map` as? java.lang.Iterable
      if ((`$i$f$map` as? java.lang.Iterable) == null) {
         return CollectionsKt.emptyList()
      } else {
         val `$this$map$iv`: java.lang.Iterable = CollectionsKt.filterNotNull(var14)
         val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(`$this$map$iv`, 10))

         for (`item$iv$iv` in `$this$map$iv`) {
            `destination$iv$iv`.add(XaeroWaypoint(`item$iv$iv`))
         }

         return `destination$iv$iv` as MutableList<XaeroWaypoint>
      }
   }

   internal fun addToSet(setRaw: Any, waypointRaw: Any): Boolean {
      val var10000: Method = this.m_waypointSet_add
      if (var10000 == null) {
         val var18: XaeroReflect = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "addToSet: add method handle null")
         return false
      } else {
         val m: Method = var10000
         val var4: XaeroReflect = this

         var it: XaeroReflect
         try {
            it = var4
            m.invoke(setRaw, waypointRaw)
            MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "addToSet: succeeded")
            it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(true)
         } catch (var11: java.lang.Throwable) {
            it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var11))
         }

         val var21: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it)
         if (var21 != null) {
            if (MCLogger.IS_DEBUG) {
               logger.error("addToSet threw", var21)
            }
         }

         return (if (Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it) == null) it else false) as java.lang.Boolean
      }
   }

   internal fun removeFromSet(setRaw: Any, waypointRaw: Any): Boolean {
      val var10000: Method = this.m_waypointSet_remove
      if (var10000 == null) {
         val var18: XaeroReflect = this
         MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "removeFromSet: remove method handle null")
         return false
      } else {
         val m: Method = var10000
         val var4: XaeroReflect = this

         var it: XaeroReflect
         try {
            it = var4
            m.invoke(setRaw, waypointRaw)
            MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "removeFromSet: succeeded")
            it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(true)
         } catch (var11: java.lang.Throwable) {
            it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var11))
         }

         val var21: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it)
         if (var21 != null) {
            if (MCLogger.IS_DEBUG) {
               logger.error("removeFromSet threw", var21)
            }
         }

         return (if (Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it) == null) it else false) as java.lang.Boolean
      }
   }

   internal fun clearSet(setRaw: Any): Boolean {
      val var10000: Method = this.m_waypointSet_clear
      if (var10000 == null) {
         return false
      } else {
         val m: Method = var10000
         val var3: XaeroReflect = this

         var it: XaeroReflect
         try {
            it = var3
            m.invoke(setRaw)
            it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(true)
         } catch (var10: java.lang.Throwable) {
            it = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var10))
         }

         val var17: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it)
         if (var17 != null) {
            if (MCLogger.IS_DEBUG) {
               logger.error("clearSet threw", var17)
            }
         }

         return (if (Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it) == null) it else false) as java.lang.Boolean
      }
   }

   internal fun wpX(raw: Any): Int {
      val var10000: Method = this.m_wp_getX
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? Int) != null) var2 as? Int else 0
   }

   internal fun wpY(raw: Any): Int {
      val var10000: Method = this.m_wp_getY
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? Int) != null) var2 as? Int else 0
   }

   internal fun wpZ(raw: Any): Int {
      val var10000: Method = this.m_wp_getZ
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? Int) != null) var2 as? Int else 0
   }

   internal fun wpName(raw: Any): String {
      val var10000: Method = this.m_wp_getName
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      var var3: java.lang.String = var2 as? java.lang.String
      if ((var2 as? java.lang.String) == null) {
         var3 = ""
      }

      return var3
   }

   internal fun wpInitials(raw: Any): String {
      val var10000: Method = this.m_wp_getInitials
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      var var3: java.lang.String = var2 as? java.lang.String
      if ((var2 as? java.lang.String) == null) {
         var3 = ""
      }

      return var3
   }

   internal fun wpColor(raw: Any): Int {
      var var10000: Method = this.m_wp_getWaypointColor
      val colorEnum: Any = if (var10000 != null) var10000.invoke(raw) else null
      if (colorEnum != null) {
         var10000 = this.m_color_getHex
         val values: Any = if (var10000 != null) var10000.invoke(colorEnum) else null
         val idx: Int = values as? Int
         if ((values as? Int) != null) {
            return idx.intValue() and 16777215
         }
      }

      var10000 = this.m_wp_getColor
      val clamped: Any = if (var10000 != null) var10000.invoke(raw) else null
      val var16: Int = clamped as? Int
      if ((clamped as? Int) != null) {
         val var8: Int = var16
         var10000 = this.m_color_values
         val chosen: Any = if (var10000 != null) var10000.invoke(null) else null
         val var18: Array<Any> = chosen as? Array<Any>
         if ((chosen as? Array<Any>) == null) {
            return 16777215
         } else {
            val var10: Int = RangesKt.coerceIn(var8, 0, var18.length - 1)
            val var19: Any = var18[var10]
            if (var18[var10] == null) {
               return 16777215
            } else {
               val var20: Method = this.m_color_getHex
               val var7: Any = if (var20 != null) var20.invoke(var19) else null
               return if ((var7 as? Int) != null) var7 as? Int and 16777215 else 16777215
            }
         }
      } else {
         return 16777215
      }
   }

   internal fun wpDisabled(raw: Any): Boolean {
      val var10000: Method = this.m_wp_isDisabled
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return (var2 as? java.lang.Boolean) != null && var2 as? java.lang.Boolean
   }

   internal fun wpYIncluded(raw: Any): Boolean {
      val var10000: Method = this.m_wp_isYIncluded
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return (var2 as? java.lang.Boolean) == null || var2 as? java.lang.Boolean
   }

   internal fun wpCreatedAt(raw: Any): Long {
      val var10000: Method = this.m_wp_getCreatedAt
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? java.lang.Long) != null) var2 as? java.lang.Long else 0L
   }

   internal fun wpPurposeName(raw: Any): String {
      val var10000: Method = this.m_wp_getPurpose
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      val var3: java.lang.Enum = var2 as? java.lang.Enum
      if ((var2 as? java.lang.Enum) != null) {
         val var4: java.lang.String = var3.name()
         if (var4 != null) {
            return var4
         }
      }

      return "NORMAL"
   }

   internal fun wpVisibilityName(raw: Any): String {
      val var10000: Method = this.m_wp_getVisibility
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      val var3: java.lang.Enum = var2 as? java.lang.Enum
      if ((var2 as? java.lang.Enum) != null) {
         val var4: java.lang.String = var3.name()
         if (var4 != null) {
            return var4
         }
      }

      return "LOCAL"
   }

   internal fun applyUpdate(
      raw: Any,
      name: String,
      initials: String,
      x: Int,
      y: Int,
      z: Int,
      color: Int,
      disabled: Boolean,
      yIncluded: Boolean,
      purpose: Any? = ...,
      visibility: Any? = ...
   ) {
      val var10000: Logger = logger
      val var10006: java.lang.String = Integer.toString(color, CharsKt.checkRadix(16))
      MCLoggerKt.nrcDebugLog(var10000, "xaero-reflect", "applyUpdate: name='$name' pos=($x,$y,$z) color=#$var10006 disabled=$disabled yIncluded=$yIncluded")
      val var28: Method = this.m_wp_setName
      if (var28 != null) {
         var28.invoke(raw, name)
      }

      val var29: Method = this.m_wp_setInitials
      if (var29 != null) {
         var29.invoke(raw, initials)
      }

      val var30: Method = this.m_wp_setX
      if (var30 != null) {
         var30.invoke(raw, x)
      }

      val var31: Method = this.m_wp_setY
      if (var31 != null) {
         var31.invoke(raw, y)
      }

      val var32: Method = this.m_wp_setZ
      if (var32 != null) {
         var32.invoke(raw, z)
      }

      val colorEnum: Any = this.nearestColorEnum$nrc_client(color)
      if (colorEnum != null) {
         val var33: Method = this.m_wp_setWaypointColor
         if (var33 != null) {
            var33.invoke(raw, colorEnum)
         }
      } else {
         val var34: Method = this.m_wp_setColor
         if (var34 != null) {
            var34.invoke(raw, color)
         }
      }

      val var35: Method = this.m_wp_setDisabled
      if (var35 != null) {
         var35.invoke(raw, disabled)
      }

      val var36: Method = this.m_wp_setYIncluded
      if (var36 != null) {
         var36.invoke(raw, yIncluded)
      }

      if (purpose != null) {
         val var37: Method = INSTANCE.m_wp_setPurpose
         if (var37 != null) {
            var37.invoke(raw, purpose)
         }
      }

      if (visibility != null) {
         val var38: Method = INSTANCE.m_wp_setVisibility
         if (var38 != null) {
            var38.invoke(raw, visibility)
         }
      }
   }

   internal fun purposeFromName(name: String): Any? {
      return this.enumValue$nrc_client(this.c_waypointPurpose, name)
   }

   internal fun visibilityFromName(name: String): Any? {
      return this.enumValue$nrc_client(this.c_waypointVisibilityType, name)
   }

   public fun isXaeroAutoDeathpointsEnabled(): Boolean? {
      if (!this.isAvailable) {
         return null
      } else {
         val var1: XaeroReflect = this

         var `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`: XaeroReflect
         try {
            `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145` = var1
            var var10000: Class = (var1 as XaeroReflect).c_hudMod
            var var21: java.lang.Boolean
            if (var10000 == null) {
               var21 = null
            } else {
               var10000 = (Class)var10000.getField("INSTANCE").get(null)
               if (var10000 == null) {
                  var21 = null
               } else {
                  run label82@{
                     val var19: Method = `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`.m_hudMod_getHudConfigs
                     if (var19 != null) {
                        var10000 = (Class)var19.invoke(var10000)
                        if (var10000 != null) {
                           val var22: Method = `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`.m_configChannel_getClientConfigManager
                           if (var22 != null) {
                              var10000 = (Class)var22.invoke(var10000)
                              if (var10000 != null) {
                                 val var24: Field = `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`.f_autoWaypointsOnDeath
                                 if (var24 != null) {
                                    var10000 = (Class)var24.get(null)
                                    if (var10000 != null) {
                                       val var26: Method = `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`.m_clientConfigManager_getEffective
                                       var10000 = (Class)(if (var26 != null) var26.invoke(var10000, var10000) else null)
                                       var21 = var10000 as? java.lang.Boolean
                                       return@label82
                                    }
                                 }

                                 var21 = null
                                 return@label82
                              }
                           }

                           var21 = null
                           return@label82
                        }
                     }

                     var21 = null
                  }
               }
            }

            `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(var21)
         } catch (var11: java.lang.Throwable) {
            `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(
               ResultKt.createFailure(var11)
            )
         }

         val var28: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`)
         if (var28 != null) {
            MCLoggerKt.nrcDebugLog(logger, "xaero-reflect", "isXaeroAutoDeathpointsEnabled threw: ${var28.getMessage()}")
         }

         return (
            if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`))
               null
               else
               `$this$isXaeroAutoDeathpointsEnabled_u24lambda_u24145`
         ) as java.lang.Boolean
      }
   }

   public fun wmWpSetName(raw: Any): String? {
      val var10000: Method = this.m_wmWp_getSetName
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return var2 as? java.lang.String
   }

   public fun wmWpName(raw: Any): String? {
      val var10000: Method = this.m_wmWp_getName
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return var2 as? java.lang.String
   }

   public fun wmWpX(raw: Any): Int {
      val var10000: Method = this.m_wmWp_getX
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? Int) != null) var2 as? Int else 0
   }

   public fun wmWpY(raw: Any): Int {
      val var10000: Method = this.m_wmWp_getY
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? Int) != null) var2 as? Int else 0
   }

   public fun wmWpZ(raw: Any): Int {
      val var10000: Method = this.m_wmWp_getZ
      val var2: Any = if (var10000 != null) var10000.invoke(raw) else null
      return if ((var2 as? Int) != null) var2 as? Int else 0
   }

   public fun newWorldmapWaypoint(
      original: Any?,
      x: Int,
      y: Int,
      z: Int,
      text: String,
      symbol: String,
      color: Int,
      type: Int,
      disabled: Boolean,
      setName: String,
      yIncluded: Boolean,
      dimDiv: Double
   ): Any? {
      val var10000: Constructor = this.ctor_wmWaypoint
      if (var10000 == null) {
         return null
      } else {
         val ctor: Constructor = var10000
         val var15: XaeroReflect = this

         var `$this$newWorldmapWaypoint_u24lambda_u24147`: XaeroReflect
         try {
            `$this$newWorldmapWaypoint_u24lambda_u24147` = var15
            `$this$newWorldmapWaypoint_u24lambda_u24147` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(
               ctor.newInstance(original, x, y, z, text, symbol, color, type, disabled, setName, yIncluded, dimDiv)
            )
         } catch (var22: java.lang.Throwable) {
            `$this$newWorldmapWaypoint_u24lambda_u24147` = (XaeroReflect)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var22))
         }

         val var29: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$newWorldmapWaypoint_u24lambda_u24147`)
         if (var29 != null) {
            if (MCLogger.IS_DEBUG) {
               logger.error("newWorldmapWaypoint threw", var29)
            }
         }

         return if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$newWorldmapWaypoint_u24lambda_u24147`))
            null
            else
            `$this$newWorldmapWaypoint_u24lambda_u24147`
         }
   }

   public fun dimDivOf(support: Any?): Double {
      val var10000: Method = this.m_support_getDimDiv
      val var2: Any = if (var10000 != null) var10000.invoke(support) else null
      return if ((var2 as? java.lang.Double) != null) var2 as? java.lang.Double else 1.0
   }
}
