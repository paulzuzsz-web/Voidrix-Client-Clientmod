package gg.norisk.client.v2.waypoints

import gg.norisk.client.v2.waypoints.source.NrcWaypointSource
import gg.norisk.client.v2.waypoints.xaero.XaeroReflect
import gg.norisk.client.v2.waypoints.xaero.XaeroWaypointSource
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.event.KeyEventData
import gg.norisk.compat.event.KeyEvents
import gg.norisk.compat.event.MouseClickEventData
import gg.norisk.compat.event.MouseEvents
import gg.norisk.compat.kotlin.ExtensionsKt
import gg.norisk.compat.resource.MCKey
import gg.norisk.compat.resource.MCKeyType
import gg.norisk.compat.task.CoroutineScopesKt
import gg.norisk.compat.task.CoroutineTask
import gg.norisk.compat.waypoint.source.SourceWaypoint
import gg.norisk.compat.waypoint.source.WaypointSource
import gg.norisk.compat.waypoint.source.WaypointSourceRegistry
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nWaypointModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointModule.kt\ngg/norisk/client/v2/waypoints/WaypointModule\n+ 2 CoroutineTask.kt\ngg/norisk/compat/task/CoroutineTaskKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,115:1\n18#2,12:116\n18#2,12:128\n355#3:140\n40#3:141\n356#3:142\n328#3:143\n40#3:144\n328#3:146\n40#3:147\n1#4:145\n*S KotlinDebug\n*F\n+ 1 WaypointModule.kt\ngg/norisk/client/v2/waypoints/WaypointModule\n*L\n74#1:116,12\n83#1:128,12\n94#1:140\n94#1:141\n94#1:142\n53#1:143\n53#1:144\n59#1:146\n59#1:147\n*E\n"])
public object WaypointModule : Module("Waypoints", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   public open val seoTags: Array<String>

   public final var createWaypointKey: MCKey by ValueApiKt.key$default(MCKey.Companion.ofKeyboard(85), null, null, 6, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return createWaypointKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MCKey
      }

      public final set(<set-?>) {
         createWaypointKey$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public final var openWaypoints: MCKey by ValueApiKt.key$default(MCKey.Companion.getUNKNOWN(), null, null, 6, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return openWaypoints$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MCKey
      }

      public final set(<set-?>) {
         openWaypoints$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var switchToAutoOnDeath: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return switchToAutoOnDeath$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         switchToAutoOnDeath$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   public final var deathpoints: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return deathpoints$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         deathpoints$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   public final var oldDeathpoints: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return oldDeathpoints$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         oldDeathpoints$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   public final var deleteReachedDeathpoints: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return deleteReachedDeathpoints$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         deleteReachedDeathpoints$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   private fun matchKeyboard(bound: MCKey, key: Int): Unit? {
      return if (!bound.isUnknown() && bound.getType() === MCKeyType.KEYBOARD && bound.getCode() == key) Unit.INSTANCE else null
   }

   private fun matchMouse(bound: MCKey, button: Int): Unit? {
      return if (!bound.isUnknown() && bound.getType() === MCKeyType.MOUSE && bound.getCode() == button) Unit.INSTANCE else null
   }

   public fun openWaypointsScreen() {
      val moduleId: java.lang.String = this.getInternalKey()
      val `sync$iv`: Boolean = true
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getMcClientCoroutineScope(),
         null,
         null,
         WaypointModule$openWaypointsScreen$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, moduleId
         ),
         3,
         null
      )
   }

   public fun openWaypointAddScreen() {
      val `sync$iv`: Boolean = true
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getMcClientCoroutineScope(),
         null,
         null,
         WaypointModule$openWaypointAddScreen$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null
         ),
         3,
         null
      )
   }

   public fun openEditScreen(waypoint: SourceWaypoint?, dimensionKey: String?, targetSource: WaypointSource = NrcWaypointSource.INSTANCE as WaypointSource) {
      val `screen$iv`: Screen = WaypointEditScreen(waypoint, dimensionKey, targetSource, null, 8, null) as Screen
      val var10000: Minecraft = Minecraft.getInstance()
      var10000.gui.setScreen(`screen$iv`)
   }

   public open fun onEnable() {
      PersistentWaypointRenderer.INSTANCE.hidden = false
   }

   public open fun onDisable() {
      PersistentWaypointRenderer.INSTANCE.hidden = true
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         settingsPanel.child(WaypointListComponent() as UIComponent)
         Unit.INSTANCE
      }
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @JvmStatic
   fun {
      val var3: Logger = MCLogger.getLogger("Voidrix-WaypointModule")
      WaypointSourceRegistry.INSTANCE.register(NrcWaypointSource.INSTANCE)
      WaypointSourceRegistry.INSTANCE.register(XaeroWaypointSource.INSTANCE)
      MCLoggerKt.nrcDebugLog(
         var3,
         "waypoints",
         "Registered waypoint sources: ${CollectionsKt.joinToString$default(
            WaypointSourceRegistry.INSTANCE.allSources(), ", ", null, null, 0, null, { it: WaypointSource ->
               ("${it.getId()}(${it.getDisplayName()})") as java.lang.CharSequence
            }, 30, null
         )}"
      )
      ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
         XaeroReflect.invalidateCachedWorld()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         XaeroReflect.invalidateCachedWorld()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getRespawnEvent().listen({ it: Unit ->
         XaeroReflect.invalidateCachedWorld()
         Unit.INSTANCE
      })
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_6@{ event: KeyEventData ->
         if (INSTANCE.isEnabled()) {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() == null) {
               if (!event.isClicked()) {
                  return@lambda_6 Unit.INSTANCE
               }

               if (INSTANCE.matchKeyboard(INSTANCE.createWaypointKey, event.getKey()) != null) {
                  INSTANCE.openWaypointAddScreen()
               }

               if (INSTANCE.matchKeyboard(INSTANCE.openWaypoints, event.getKey()) != null) {
                  INSTANCE.openWaypointsScreen()
               }

               return@lambda_6 Unit.INSTANCE
            }
         }

         return@lambda_6 Unit.INSTANCE
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_9@{ event: MouseClickEventData ->
         if (INSTANCE.isEnabled()) {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() == null) {
               if (event.getAction() != 1) {
                  return@lambda_9 Unit.INSTANCE
               }

               if (INSTANCE.matchMouse(INSTANCE.createWaypointKey, event.getButton()) != null) {
                  INSTANCE.openWaypointAddScreen()
               }

               if (INSTANCE.matchMouse(INSTANCE.openWaypoints, event.getButton()) != null) {
                  INSTANCE.openWaypointsScreen()
               }

               return@lambda_9 Unit.INSTANCE
            }
         }

         return@lambda_9 Unit.INSTANCE
      })
   }
}
