package gg.norisk.client.bootstrap

import gg.norisk.client.v2.analytics.NrcAnalytics
import gg.norisk.client.v2.debug.LegacyTweaksDebugCommand
import gg.norisk.client.v2.debug.NrcTextureDebugCommand
import gg.norisk.client.v2.moderation.PunishCommand
import gg.norisk.client.v2.moderation.ReportCommand
import gg.norisk.compat.auth.DevAuth
import gg.norisk.compat.auth.NoriskTokenManager
import gg.norisk.compat.bootstrap.NrcBootstrap
import gg.norisk.compat.client.MCClient
import gg.norisk.compat.debug.DebugUtil
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.event.MixinBridge
import gg.norisk.compat.reflection.NrcReflectionUtil
import gg.norisk.compat.resource.ExternalResourcePackRegistry
import gg.norisk.compat.text.LiteralTextBuilder
import gg.norisk.compat.util.DirectoryLinkManager
import gg.norisk.compat.websocket.NrcWebSocketClient
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.keybinds.NrcKeybindsLayer
import gg.norisk.ui.modules.api.ModuleProvider
import gg.norisk.ui.modules.v3.V3Preload
import gg.norisk.ui.utils.DevUtilsKt
import gg.norisk.ui.utils.ServerLockedModuleManager
import gg.norisk.ui.v2.toast.components.NrcToastComponent.Builder
import java.util.ArrayList
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

public class ClientBootstrap : NrcBootstrap {
   private final val log: Logger = LogManager.getLogger("Voidrix-Bootstrap-Client")

   public open fun priority(): Int {
      return 100
   }

   public open fun onEarlyInit() {
      this.log.info("client bootstrap onEarlyInit → initBootstrap")
      Companion.initBootstrap()
   }

   @SourceDebugExtension(["SMAP\nClientBootstrap.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ClientBootstrap.kt\ngg/norisk/client/bootstrap/ClientBootstrap$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 5 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt\n+ 6 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt$buildText$1\n+ 7 MCGameOptions.kt\ngg/norisk/compat/client/MCGameOptions\n+ 8 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,239:1\n1617#2,9:240\n1869#2:249\n1870#2:251\n1626#2:252\n1869#2,2:258\n1#3:250\n1#3:257\n37#4:253\n36#4,3:254\n11#5:260\n8#5,4:261\n10#6:265\n51#7:266\n63#7:268\n72#7:270\n81#7:272\n40#8:267\n40#8:269\n40#8:271\n40#8:273\n*S KotlinDebug\n*F\n+ 1 ClientBootstrap.kt\ngg/norisk/client/bootstrap/ClientBootstrap$Companion\n*L\n130#1:240,9\n130#1:249\n130#1:251\n130#1:252\n150#1:258,2\n130#1:250\n132#1:253\n132#1:254,3\n189#1:260\n190#1:261,4\n190#1:265\n229#1:266\n230#1:268\n231#1:270\n232#1:272\n229#1:267\n230#1:269\n231#1:271\n232#1:273\n*E\n"])
   public companion object {
      private final var initialized: Boolean

      public fun initBootstrap() {
         if (!ClientBootstrap.initialized) {
            ClientBootstrap.initialized = true
            NoriskTokenManager.INSTANCE.ensureStarted()
            NrcWebSocketClient.INSTANCE.ensureAutoConnect()
            NrcAnalytics.init()
            V3Preload.INSTANCE.register()
            val var5: java.lang.Iterable = CollectionsKt.listOf(
               arrayOf(
                  "gg.norisk.ui.modules.profiles.ProfilesModule",
                  "gg.norisk.client.v2.modules.impl.FullBrightModule",
                  "gg.norisk.client.v2.modules.impl.FreeLookModule",
                  "gg.norisk.client.v2.modules.impl.NameTagsModule",
                  "gg.norisk.client.v2.modules.impl.FovChanger",
                  "gg.norisk.client.v2.modules.impl.ColorSaturationModule",
                  "gg.norisk.client.v2.modules.impl.MotionBlurModule",
                  "gg.norisk.client.v2.modules.impl.NoFogModule",
                  "gg.norisk.client.v2.modules.impl.IconModule",
                  "gg.norisk.client.v2.modules.itemcounter.ItemCounter",
                  "gg.norisk.client.v2.modules.itemhighlighter.ItemHighlighter",
                  "gg.norisk.client.v2.modules.shinypots.ShinyPots",
                  "gg.norisk.client.v2.modules.armorstatus.ArmorStatus",
                  "gg.norisk.client.v2.modules.combocounter.ComboCounter",
                  "gg.norisk.client.v2.modules.cps.CPS",
                  "gg.norisk.client.v2.modules.clock.Clock",
                  "gg.norisk.client.v2.modules.speedometer.Speedometer",
                  "gg.norisk.client.v2.modules.reachdisplay.ReachDisplay",
                  "gg.norisk.client.v2.modules.uptime.Uptime",
                  "gg.norisk.client.v2.modules.daycounter.DayCounter",
                  "gg.norisk.client.v2.modules.jumpreset.JumpReset",
                  "gg.norisk.client.v2.modules.tps.TPS",
                  "gg.norisk.client.v2.modules.coordinates.Coordinates",
                  "gg.norisk.client.v2.modules.chatheads.ChatHeads",
                  "gg.norisk.client.v2.modules.impl.ZoomModule",
                  "gg.norisk.client.v2.modules.impl.BorderlessFullscreenModule",
                  "gg.norisk.client.v2.modules.fps.FPS",
                  "gg.norisk.client.v2.modules.ping.PingHud",
                  "gg.norisk.client.v2.modules.crosshair.CustomCrosshair",
                  "gg.norisk.cosmetics.v2.NRCPlusModule",
                  "gg.norisk.client.v2.modules.togglesprint.ToggleSprintModule",
                  "gg.norisk.client.v2.modules.dropstack.DropStackModule",
                  "gg.norisk.client.v2.modules.oldanimations.OldAnimationsModule",
                  "gg.norisk.client.v2.modules.impl.ClearBackgroundModule",
                  "gg.norisk.client.v2.modules.potion.PotionStatus",
                  "gg.norisk.client.v2.modules.weatherchanger.WeatherChanger",
                  "gg.norisk.client.v2.modules.timechanger.TimeChanger",
                  "gg.norisk.client.v2.modules.scoreboard.ScoreboardModule",
                  "gg.norisk.client.v2.modules.titles.Titles",
                  "gg.norisk.client.v2.modules.actionbar.ActionBar",
                  "gg.norisk.client.v2.modules.impl.HitColorModule",
                  "gg.norisk.client.v2.modules.hitbox.HitBox",
                  "gg.norisk.client.v2.modules.autotext.AutoText",
                  "gg.norisk.client.v2.modules.resourcepackdisplay.ResourcePackDisplay",
                  "gg.norisk.client.v2.modules.keystrokes.Keystrokes",
                  "gg.norisk.client.v2.modules.mousetracker.MouseTracker",
                  "gg.norisk.client.v2.screenshot.ScreenshotModule",
                  "gg.norisk.client.v2.modules.arrowtrail.ArrowTrail",
                  "gg.norisk.client.v2.modules.blockoutline.BlockOutline",
                  "gg.norisk.client.v2.modules.particle.ParticleModule",
                  "gg.norisk.client.v2.modules.spotify.SpotifyHud",
                  "gg.norisk.client.v2.modules.glintcolorizer.GlintColorizerModule",
                  "gg.norisk.client.v2.modules.itemmodel.ItemModel",
                  "gg.norisk.client.v2.modules.streamermode.StreamerMode",
                  "gg.norisk.client.v2.modules.tnttimer.TntTimer",
                  "gg.norisk.client.v2.modules.packtweaks.PackTweaks",
                  "gg.norisk.client.v2.modules.lowfire.LowFireModule",
                  "gg.norisk.client.v2.modules.sideshield.SideShieldModule",
                  "gg.norisk.client.v2.modules.impl.NoAdvancementModule",
                  "gg.norisk.client.v2.modules.serverhud.ServerHud",
                  "gg.norisk.client.v2.modules.autoreconnect.AutoReconnect",
                  "gg.norisk.client.v2.modules.loadingtips.LoadingScreenTipsModule",
                  "gg.norisk.client.v2.modules.thirdparty.SaturationModule",
                  "gg.norisk.client.v2.modules.thirdparty.ShulkerPreviewModule",
                  "gg.norisk.client.v2.modules.thirdparty.ThreeDSkinModule",
                  "gg.norisk.client.v2.modules.thirdparty.TiersModule",
                  "gg.norisk.client.v2.modules.thirdparty.HealthIndicatorsModule",
                  "gg.norisk.client.v2.modules.thirdparty.WaveyCapesModule",
                  "gg.norisk.client.v2.modules.nohurtcam.NoHurtCam",
                  "gg.norisk.client.v2.modules.discord.DiscordIntegrationModule",
                  "gg.norisk.client.v2.waypoints.WaypointModule",
                  "gg.norisk.client.v2.modules.impl.ResourcePackOrganizerModule",
                  "gg.norisk.client.v2.modules.impl.BadOptimizationsModule"
               )
            )
            val cls: java.util.Collection = ArrayList()

            for (`element$iv$iv$iv` in var5) {
               val var10000: Any = NrcReflectionUtil.tryLoadObject(`element$iv$iv$iv` as java.lang.String) as Module
               if (var10000 != null) {
                  cls.add(var10000)
               }
            }

            val var19: Array<Module> = (cls as java.util.List).toArray(arrayOfNulls(0))
            ModuleProvider.Companion.registerAll(Arrays.copyOf(var19, var19.length))
            if (DevAuth.isEnabled) {
               val var21: Module = NrcReflectionUtil.tryLoadObject("gg.norisk.client.v2.modules.impl.DummyModule") as Module
               if (var21 != null) {
                  ModuleProvider.Companion.registerAll(arrayOf(var21))
               }
            }

            for (var36 in CollectionsKt.listOf(
               arrayOf(
                  "gg.norisk.client.v2.modules.thirdparty.SaturationModule",
                  "gg.norisk.client.v2.modules.thirdparty.ShulkerPreviewModule",
                  "gg.norisk.client.v2.modules.thirdparty.ThreeDSkinModule",
                  "gg.norisk.client.v2.modules.thirdparty.TiersModule",
                  "gg.norisk.client.v2.modules.thirdparty.HealthIndicatorsModule",
                  "gg.norisk.client.v2.modules.thirdparty.WaveyCapesModule"
               )
            )) {
               NrcReflectionUtil.tryInvokeNoArg(var36 as java.lang.String, "initHooks")
            }

            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.screenshot.ScreenshotModule", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.resourcepackorganizer.RpoToggleLayer", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.tips.LoadingTipsLayer", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.auth.AccountSwitcherLayer", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.serverswitcher.ServerSwitcherLayer", "init")
            NrcKeybindsLayer.init()
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.cosmetics.v2.command.CosmeticsCommand", "register")
            ExternalResourcePackRegistry.INSTANCE.register("VoidrixClient/designer")
            DirectoryLinkManager.INSTANCE.setupResourcePackLink()
            DirectoryLinkManager.INSTANCE.setupScreenshotsLink()
            DirectoryLinkManager.INSTANCE.setupShaderPacksLink()
            NrcTextureDebugCommand.INSTANCE.init()
            LegacyTweaksDebugCommand.INSTANCE.init()
            val var24: ClientBootstrap.Companion = this

            try {
               var var29: ClientBootstrap.Companion = var24
               val var37: Class = Class.forName("gg.norisk.client.v2.serverstyling.ServerStylingManager")
               ServerLockedModuleManager.INSTANCE.setDisabledModulesProvider({ serverIp: java.lang.String ->
                  val var10000: Any = `$method`.invoke(`$instance`, serverIp)
                  var10000 as java.util.List
               })
               var29 = (ClientBootstrap.Companion)Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
            } catch (var18: java.lang.Throwable) {
               val var28: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var18))
            }

            ServerLockedModuleManager.INSTANCE.setNotificationHandler({ title: java.lang.String, description: java.lang.String ->
               val var10000: Builder = Builder.info$default(Builder(null, null, 3, null), false, 1, null)
               val `$i$f$buildText`: LiteralTextBuilder = LiteralTextBuilder(title, true)
               `$i$f$buildText`.setBold(true)
               var10000.title(`$i$f$buildText`.build() as Component).description(LiteralTextBuilder(description, true).build() as Component).build().show()
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
               val ip: java.lang.String = MCClient.getCurrentServerIp()
               DevUtilsKt.nrcDebug(ClientBootstrap.Companion, "[NrcClient] joinEvent fired! ip=$ip")
               ServerLockedModuleManager.INSTANCE.onServerJoin(ip)
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
               ServerLockedModuleManager.INSTANCE.onServerDisconnect()
               Unit.INSTANCE
            })
            PunishCommand.INSTANCE.init()
            ReportCommand.INSTANCE.init()
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.waypoints.PersistentWaypointStore", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.waypoints.DeathWaypointHandler", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.waypoints.DestinationWaypointHandler", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.waypoints.WaypointCommand", "register")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.waypoints.PersistentWaypointRenderer", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.testingloop.TestingLoopBootstrap", "init")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.cosmetics.v2.testingloop.CosmeticSyncTests", "register")
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.friends.testingloop.FriendsSyncTests", "register")
            DebugUtil.INSTANCE.init()
            NrcReflectionUtil.tryInvokeNoArg("gg.norisk.client.v2.modules.glintcolorizer.GlintColorizerModule", "init")
            ClientEvents.INSTANCE.getClientStartedEvent().listen({ it: Unit ->
               val isFirst: Boolean = MixinBridge.isFirstOptionsTxtCreate
               `$defaultsLogger`.info("clientStartedEvent: isFirstOptionsTxtCreate={}", MixinBridge.isFirstOptionsTxtCreate)
               if (isFirst) {
                  `$defaultsLogger`.info("Applying first-launch defaults: vsync=false, maxFps=260, gamma=1.0")
                  var var10000: Minecraft = Minecraft.getInstance()
                  var10000.options.enableVsync().set(false)
                  var10000 = Minecraft.getInstance()
                  var10000.options.framerateLimit().set(260)
                  var10000 = Minecraft.getInstance()
                  var10000.options.gamma().set(1.0)
                  var10000 = Minecraft.getInstance()
                  var10000.options.save()
               }

               Unit.INSTANCE
            })
         }
      }
   }
}
