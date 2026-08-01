package gg.voidrix.client.v2.analytics

import dev.jakub.nrc.analytics.AnalyticsClient
import dev.jakub.nrc.analytics.utils.AnalyticsUtils
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.compat.event.EventPriority
import gg.voidrix.compat.event.LocalCosmeticEquippedEvent
import gg.voidrix.compat.event.LocalEmoteSelectedEvent
import gg.voidrix.compat.event.ThemeModeChangedEvent
import gg.voidrix.compat.kotlin.ExtensionsKt
import gg.voidrix.compat.task.CoroutineScopesKt
import gg.voidrix.compat.task.CoroutineTask
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.config.profile.ProfileManager
import gg.voidrix.ui.events.ModuleEvents
import gg.voidrix.ui.events.ModuleEvents.ModuleToggleEvent
import gg.voidrix.ui.modules.api.ModuleProvider
import gg.voidrix.ui.theme.ThemeModule
import java.util.LinkedHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlin.time.DurationKt
import kotlin.time.DurationUnit
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.Job
import kotlinx.coroutines.Job.DefaultImpls
import net.minecraft.client.Minecraft
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nVoidrixAnalytics.kt\nKotlin\n*S Kotlin\n*F\n+ 1 VoidrixAnalytics.kt\ngg/voidrix/client/v2/analytics/VoidrixAnalytics\n+ 2 CoroutineTask.kt\ngg/voidrix/compat/task/CoroutineTaskKt\n+ 3 MCServer.kt\ngg/voidrix/compat/host/MCServer\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 6 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 7 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n*L\n1#1,269:1\n18#2,12:270\n18#2,12:283\n18#2,12:297\n18#2,12:309\n18#2,12:321\n95#3:282\n1869#4,2:295\n1869#4,2:336\n1869#4,2:338\n1869#4,2:340\n1#5:333\n216#6,2:334\n60#7:342\n*S KotlinDebug\n*F\n+ 1 VoidrixAnalytics.kt\ngg/voidrix/client/v2/analytics/VoidrixAnalytics\n*L\n109#1:270,12\n143#1:283,12\n183#1:297,12\n202#1:309,12\n227#1:321,12\n133#1:282\n164#1:295,2\n87#1:336,2\n89#1:338,2\n91#1:340,2\n253#1:334,2\n24#1:342\n*E\n"])
public object VoidrixAnalytics {
   private final val logger: Logger by LazyKt.lazy(VoidrixAnalytics$special$$inlined$lazyLogger$1.INSTANCE)
      private final get() {
         return logger$delegate.getValue() as Logger
      }


   private final var client: AnalyticsClient?
   private final val initialized: AtomicBoolean = AtomicBoolean(false)
   private final val usageTrackingEnabled: AtomicBoolean = AtomicBoolean(false)
   private const val usageDebounceMs: Long = 1000L
   private final var themeChangeDebounceJob: Job?
   private final val moduleToggleDebounceJobs: LinkedHashMap<String, Job> = LinkedHashMap()
   private final val emotePlayDebounceJobs: LinkedHashMap<String, Job> = LinkedHashMap()
   private final val cosmeticEquipDebounceJobs: LinkedHashMap<String, Job> = LinkedHashMap()
   private final var lastTrackedThemeMode: String?
   private final val moduleEnabledBaseline: LinkedHashMap<String, Boolean> = LinkedHashMap()

   @JvmStatic
   public fun init() {
      if (initialized.compareAndSet(false, true)) {
         if (!VoidrixAnalyticsConfig.INSTANCE.isEnabled()) {
            INSTANCE.logger.info("[Analytics] Disabled (${VoidrixAnalyticsConfig.INSTANCE.serverModeLabel()}). Enable with -Dvoidrix.analytics.enabled=true")
         } else {
            val var0: VoidrixAnalytics = INSTANCE

            var `$this$init_u24lambda_u240`: Any
            try {
               val var3: AnalyticsClient = AnalyticsClient(VoidrixAnalyticsConfig.INSTANCE.baseUrl())
               var3.setUserContext(AnalyticsUtils.generatePersistentUserId(), AnalyticsUtils.systemProperties())
               client = var3
               var0.logger.info("[Analytics] Ready (${VoidrixAnalyticsConfig.INSTANCE.serverModeLabel()}) → ${VoidrixAnalyticsConfig.INSTANCE.trackUrl()}")
               `$this$init_u24lambda_u240` = Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
            } catch (var5: java.lang.Throwable) {
               `$this$init_u24lambda_u240` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var5))
            }

            val var10000: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$init_u24lambda_u240`)
            if (var10000 != null) {
               INSTANCE.logger.warn("[Analytics] Init failed: ${var10000.getMessage()}")
               initialized.set(false)
            }

            ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
               INSTANCE.onServerJoin()
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
               INSTANCE.onServerDisconnect()
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getClientStoppingEvent().listen({ it: Unit ->
               val var1: VoidrixAnalytics = INSTANCE

               try {
                  val var10000: Unit
                  if (client != null) {
                     client.close()
                     var10000 = Unit.INSTANCE
                  } else {
                     var10000 = null
                  }

                  val var5: Any = Result.constructor_impl/* $VF was: constructor-impl */(var10000)
               } catch (var4: java.lang.Throwable) {
                  val `$this$init_u24lambda_u245_u24lambda_u244`: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
               }

               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getThemeModeChangedEvent().listen(lambda_6@{ it: ThemeModeChangedEvent ->
               if (!usageTrackingEnabled.get()) {
                  return@lambda_6 Unit.INSTANCE
               } else {
                  INSTANCE.scheduleThemeChangedTrack()
                  return@lambda_6 Unit.INSTANCE
               }
            })
            ModuleEvents.INSTANCE.getModuleToggleEvent().listen({ event: ModuleToggleEvent ->
               INSTANCE.onModuleToggle(event)
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getLocalEmoteSelectedEvent().listen({ event: LocalEmoteSelectedEvent ->
               INSTANCE.onLocalEmoteSelected(event)
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getLocalCosmeticEquippedEvent().listen({ event: LocalCosmeticEquippedEvent ->
               INSTANCE.onLocalCosmeticEquipped(event)
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getClientStartedEvent().listen(EventPriority.LAST, { it: Unit ->
               ProfileManager.INSTANCE.getCurrentProfile()
               val nightMode: Boolean = ThemeModule.INSTANCE.getNightMode()
               val themeMode: java.lang.String = INSTANCE.themeModeLabel(nightMode)
               lastTrackedThemeMode = themeMode
               if (themeChangeDebounceJob != null) {
                  DefaultImpls.cancel$default(themeChangeDebounceJob, null, 1, null)
               }

               var var10000: java.util.Collection = moduleToggleDebounceJobs.values()

               for (`element$iv` in var10000) {
                  DefaultImpls.cancel$default(`element$iv` as Job, null, 1, null)
               }

               moduleToggleDebounceJobs.clear()
               var10000 = emotePlayDebounceJobs.values()

               for (var15 in var10000) {
                  DefaultImpls.cancel$default(var15 as Job, null, 1, null)
               }

               emotePlayDebounceJobs.clear()
               var10000 = cosmeticEquipDebounceJobs.values()

               for (var16 in var10000) {
                  DefaultImpls.cancel$default(var16 as Job, null, 1, null)
               }

               cosmeticEquipDebounceJobs.clear()
               INSTANCE.recordModuleBaseline()
               INSTANCE.logger.info("[Analytics] client_started (theme baseline=$themeMode, night_mode=$nightMode)")
               track$default("client_started", null, 2, null)
               usageTrackingEnabled.set(true)
               Unit.INSTANCE
            })
         }
      }
   }

   @JvmStatic
   public fun isReady(): Boolean {
      return client != null && VoidrixAnalyticsConfig.INSTANCE.isEnabled()
   }

   @JvmOverloads
   @JvmStatic
   public fun track(eventType: String, properties: Map<String, Any?>? = null) {
      if (VoidrixAnalyticsConfig.INSTANCE.isEnabled()) {
         if (client != null) {
            val analytics: AnalyticsClient = client
            BuildersKt.launch$default(
               CoroutineScopesKt.getBackgroundCoroutineScope(),
               null,
               null,
               VoidrixAnalytics$track$$inlined$mcCoroutineTask-ML416i8$default$1(
                  Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(),
                  1L,
                  CoroutineTask(1L),
                  ExtensionsKt.getTicks(1),
                  null,
                  analytics,
                  eventType,
                  INSTANCE.enrich(properties)
               ),
               3,
               null
            )
         }
      }
   }

   @JvmStatic
   public fun track(eventType: String, vararg properties: Pair<String, Any?>) {
      track(eventType, MapsKt.toMap(properties))
   }

   @JvmStatic
   public fun track(eventType: String, builder: (AnalyticsPropertiesBuilder) -> Unit) {
      val var2: AnalyticsPropertiesBuilder = AnalyticsPropertiesBuilder()
      builder(var2)
      track(eventType, var2.build())
   }

   private fun onServerJoin() {
      if (client != null) {
         client.startSession()
      }

      track("server_joined", TuplesKt.to("server", MCClient.getCurrentServerIp()), TuplesKt.to("singleplayer", Minecraft.getInstance().isLocalServer()))
   }

   private fun onServerDisconnect() {
      track$default("server_left", null, 2, null)
   }

   private fun scheduleThemeChangedTrack() {
      if (themeChangeDebounceJob != null) {
         DefaultImpls.cancel$default(themeChangeDebounceJob, null, 1, null)
      }

      themeChangeDebounceJob = BuildersKt.launch$default(
         CoroutineScopesKt.getBackgroundCoroutineScope(),
         null,
         null,
         VoidrixAnalytics$scheduleThemeChangedTrack$$inlined$mcCoroutineTask-ML416i8$default$1(
            DurationKt.toDuration(1000L, DurationUnit.MILLISECONDS), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null
         ),
         3,
         null
      )
   }

   private fun trackThemeChanged(themeMode: String, nightMode: Boolean) {
      track("theme_changed", TuplesKt.to("theme_mode", themeMode), TuplesKt.to("night_mode", nightMode))
   }

   private fun recordModuleBaseline() {
      moduleEnabledBaseline.clear()

      for (`element$iv` in ModuleProvider.Companion.getEntries()) {
         moduleEnabledBaseline.put((`element$iv` as Module).getName(), (`element$iv` as Module).isEnabled())
      }
   }

   private fun onModuleToggle(event: ModuleToggleEvent) {
      if (usageTrackingEnabled.get()) {
         val module: Module = event.getModule()
         if (module.isToggleable()) {
            val name: java.lang.String = module.getName()
            val var10000: java.lang.Boolean = moduleEnabledBaseline.get(name)
            val wasEnabled: Boolean = var10000 != null && var10000
            moduleEnabledBaseline.put(name, event.getEnabled())
            val var18: Job = moduleToggleDebounceJobs.remove(name)
            if (var18 != null) {
               DefaultImpls.cancel$default(var18, null, 1, null)
            }

            if (event.getEnabled() && !wasEnabled) {
               moduleToggleDebounceJobs.put(
                  name,
                  BuildersKt.launch$default(
                     CoroutineScopesKt.getBackgroundCoroutineScope(),
                     null,
                     null,
                     VoidrixAnalytics$onModuleToggle$$inlined$mcCoroutineTask-ML416i8$default$1(
                        DurationKt.toDuration(1000L, DurationUnit.MILLISECONDS), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, name, module
                     ),
                     3,
                     null
                  )
               )
            }
         }
      }
   }

   private fun onLocalEmoteSelected(event: LocalEmoteSelectedEvent) {
      if (usageTrackingEnabled.get()) {
         val emoteId: java.lang.String = event.getEmoteId()
         val var10000: Job = emotePlayDebounceJobs.remove(emoteId)
         if (var10000 != null) {
            DefaultImpls.cancel$default(var10000, null, 1, null)
         }

         emotePlayDebounceJobs.put(
            emoteId,
            BuildersKt.launch$default(
               CoroutineScopesKt.getBackgroundCoroutineScope(),
               null,
               null,
               VoidrixAnalytics$onLocalEmoteSelected$$inlined$mcCoroutineTask-ML416i8$default$1(
                  DurationKt.toDuration(1000L, DurationUnit.MILLISECONDS), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, emoteId, event
               ),
               3,
               null
            )
         )
      }
   }

   private fun onLocalCosmeticEquipped(event: LocalCosmeticEquippedEvent) {
      if (usageTrackingEnabled.get()) {
         val cosmeticId: java.lang.String = event.getCosmeticId()
         val var10000: Job = cosmeticEquipDebounceJobs.remove(cosmeticId)
         if (var10000 != null) {
            DefaultImpls.cancel$default(var10000, null, 1, null)
         }

         cosmeticEquipDebounceJobs.put(
            cosmeticId,
            BuildersKt.launch$default(
               CoroutineScopesKt.getBackgroundCoroutineScope(),
               null,
               null,
               VoidrixAnalytics$onLocalCosmeticEquipped$$inlined$mcCoroutineTask-ML416i8$default$1(
                  DurationKt.toDuration(1000L, DurationUnit.MILLISECONDS), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, cosmeticId, event
               ),
               3,
               null
            )
         )
      }
   }

   private fun themeModeLabel(nightMode: Boolean = ThemeModule.INSTANCE.getNightMode()): String {
      return if (nightMode) "dark" else "light"
   }

   private fun enrich(properties: Map<String, Any?>?): Map<String, Any> {
      val merged: LinkedHashMap = LinkedHashMap()
      merged.put("platform", "minecraft_client")
      var var10000: java.lang.String = MCClient.getCurrentServerIp()
      if (var10000 != null) {
         var10000 = if (!StringsKt.isBlank(var10000)) var10000 else null
         if (var10000 != null) {
            merged.put("server", var10000)
         }
      }

      if (properties != null) {
         for (var13 in properties.entrySet()) {
            val key: java.lang.String = var13.getKey() as java.lang.String
            val value: Any = var13.getValue()
            if (value != null) {
               merged.put(key, value)
            }
         }
      }

      return merged
   }

   @JvmOverloads
   @JvmStatic
   fun track(eventType: java.lang.String) {
      track$default(eventType, null, 2, null)
   }

   @JvmStatic
   fun {
      val `$this$lazyLogger$iv`: Any = INSTANCE
   }
}
