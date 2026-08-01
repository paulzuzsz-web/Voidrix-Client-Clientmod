package gg.voidrix.client.bootstrap

import gg.voidrix.client.v2.analytics.VoidrixAnalytics
import gg.voidrix.client.v2.debug.LegacyTweaksDebugCommand
import gg.voidrix.client.v2.debug.VoidrixTextureDebugCommand
import gg.voidrix.client.v2.moderation.PunishCommand
import gg.voidrix.client.v2.moderation.ReportCommand
import gg.voidrix.compat.auth.DevAuth
import gg.voidrix.compat.auth.VoidrixTokenManager
import gg.voidrix.compat.bootstrap.VoidrixBootstrap
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.debug.DebugUtil
import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.compat.event.MixinBridge
import gg.voidrix.compat.reflection.VoidrixReflectionUtil
import gg.voidrix.compat.resource.ExternalResourcePackRegistry
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.util.DirectoryLinkManager
import gg.voidrix.compat.websocket.VoidrixWebSocketClient
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.keybinds.VoidrixKeybindsLayer
import gg.voidrix.ui.modules.api.ModuleProvider
import gg.voidrix.ui.modules.v3.V3Preload
import gg.voidrix.ui.utils.DevUtilsKt
import gg.voidrix.ui.utils.ServerLockedModuleManager
import gg.voidrix.ui.v2.toast.components.VoidrixToastComponent.Builder
import java.util.ArrayList
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

public class ClientBootstrap : VoidrixBootstrap {
   private final val log: Logger = LogManager.getLogger("Voidrix-Bootstrap-Client")

   public open fun priority(): Int {
      return 100
   }

   public open fun onEarlyInit() {
      this.log.info("client bootstrap onEarlyInit → initBootstrap")
      Companion.initBootstrap()
   }

   @SourceDebugExtension(["SMAP\nClientBootstrap.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ClientBootstrap.kt\ngg/voidrix/client/bootstrap/ClientBootstrap$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n+ 5 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 6 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt$buildText$1\n+ 7 MCGameOptions.kt\ngg/voidrix/compat/client/MCGameOptions\n+ 8 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,239:1\n1617#2,9:240\n1869#2:249\n1870#2:251\n1626#2:252\n1869#2,2:258\n1#3:250\n1#3:257\n37#4:253\n36#4,3:254\n11#5:260\n8#5,4:261\n10#6:265\n51#7:266\n63#7:268\n72#7:270\n81#7:272\n40#8:267\n40#8:269\n40#8:271\n40#8:273\n*S KotlinDebug\n*F\n+ 1 ClientBootstrap.kt\ngg/voidrix/client/bootstrap/ClientBootstrap$Companion\n*L\n130#1:240,9\n130#1:249\n130#1:251\n130#1:252\n150#1:258,2\n130#1:250\n132#1:253\n132#1:254,3\n189#1:260\n190#1:261,4\n190#1:265\n229#1:266\n230#1:268\n231#1:270\n232#1:272\n229#1:267\n230#1:269\n231#1:271\n232#1:273\n*E\n"])
   public companion object {
      private final var initialized: Boolean

      public fun initBootstrap() {
         if (!ClientBootstrap.initialized) {
            ClientBootstrap.initialized = true
            VoidrixTokenManager.INSTANCE.ensureStarted()
            VoidrixWebSocketClient.INSTANCE.ensureAutoConnect()
            VoidrixAnalytics.init()
            V3Preload.INSTANCE.register()
            val var5: java.lang.Iterable = CollectionsKt.listOf(
               arrayOf(
                  "gg.voidrix.ui.modules.profiles.ProfilesModule",
                  "gg.voidrix.client.v2.modules.impl.FullBrightModule",
                  "gg.voidrix.client.v2.modules.impl.FreeLookModule",
                  "gg.voidrix.client.v2.modules.impl.NameTagsModule",
                  "gg.voidrix.client.v2.modules.impl.FovChanger",
                  "gg.voidrix.client.v2.modules.impl.ColorSaturationModule",
                  "gg.voidrix.client.v2.modules.impl.MotionBlurModule",
                  "gg.voidrix.client.v2.modules.impl.NoFogModule",
                  "gg.voidrix.client.v2.modules.impl.IconModule",
                  "gg.voidrix.client.v2.modules.itemcounter.ItemCounter",
                  "gg.voidrix.client.v2.modules.itemhighlighter.ItemHighlighter",
                  "gg.voidrix.client.v2.modules.shinypots.ShinyPots",
                  "gg.voidrix.client.v2.modules.armorstatus.ArmorStatus",
                  "gg.voidrix.client.v2.modules.combocounter.ComboCounter",
                  "gg.voidrix.client.v2.modules.cps.CPS",
                  "gg.voidrix.client.v2.modules.clock.Clock",
                  "gg.voidrix.client.v2.modules.speedometer.Speedometer",
                  "gg.voidrix.client.v2.modules.reachdisplay.ReachDisplay",
                  "gg.voidrix.client.v2.modules.uptime.Uptime",
                  "gg.voidrix.client.v2.modules.daycounter.DayCounter",
                  "gg.voidrix.client.v2.modules.jumpreset.JumpReset",
                  "gg.voidrix.client.v2.modules.tps.TPS",
                  "gg.voidrix.client.v2.modules.coordinates.Coordinates",
                  "gg.voidrix.client.v2.modules.chatheads.ChatHeads",
                  "gg.voidrix.client.v2.modules.impl.ZoomModule",
                  "gg.voidrix.client.v2.modules.impl.BorderlessFullscreenModule",
                  "gg.voidrix.client.v2.modules.fps.FPS",
                  "gg.voidrix.client.v2.modules.ping.PingHud",
                  "gg.voidrix.client.v2.modules.crosshair.CustomCrosshair",
                  "gg.voidrix.cosmetics.v2.VoidrixPlusModule",
                  "gg.voidrix.client.v2.modules.togglesprint.ToggleSprintModule",
                  "gg.voidrix.client.v2.modules.dropstack.DropStackModule",
                  "gg.voidrix.client.v2.modules.oldanimations.OldAnimationsModule",
                  "gg.voidrix.client.v2.modules.impl.ClearBackgroundModule",
                  "gg.voidrix.client.v2.modules.potion.PotionStatus",
                  "gg.voidrix.client.v2.modules.weatherchanger.WeatherChanger",
                  "gg.voidrix.client.v2.modules.timechanger.TimeChanger",
                  "gg.voidrix.client.v2.modules.scoreboard.ScoreboardModule",
                  "gg.voidrix.client.v2.modules.titles.Titles",
                  "gg.voidrix.client.v2.modules.actionbar.ActionBar",
                  "gg.voidrix.client.v2.modules.impl.HitColorModule",
                  "gg.voidrix.client.v2.modules.hitbox.HitBox",
                  "gg.voidrix.client.v2.modules.autotext.AutoText",
                  "gg.voidrix.client.v2.modules.resourcepackdisplay.ResourcePackDisplay",
                  "gg.voidrix.client.v2.modules.keystrokes.Keystrokes",
                  "gg.voidrix.client.v2.modules.mousetracker.MouseTracker",
                  "gg.voidrix.client.v2.screenshot.ScreenshotModule",
                  "gg.voidrix.client.v2.modules.arrowtrail.ArrowTrail",
                  "gg.voidrix.client.v2.modules.blockoutline.BlockOutline",
                  "gg.voidrix.client.v2.modules.particle.ParticleModule",
                  "gg.voidrix.client.v2.modules.spotify.SpotifyHud",
                  "gg.voidrix.client.v2.modules.glintcolorizer.GlintColorizerModule",
                  "gg.voidrix.client.v2.modules.itemmodel.ItemModel",
                  "gg.voidrix.client.v2.modules.streamermode.StreamerMode",
                  "gg.voidrix.client.v2.modules.tnttimer.TntTimer",
                  "gg.voidrix.client.v2.modules.packtweaks.PackTweaks",
                  "gg.voidrix.client.v2.modules.lowfire.LowFireModule",
                  "gg.voidrix.client.v2.modules.sideshield.SideShieldModule",
                  "gg.voidrix.client.v2.modules.impl.NoAdvancementModule",
                  "gg.voidrix.client.v2.modules.serverhud.ServerHud",
                  "gg.voidrix.client.v2.modules.autoreconnect.AutoReconnect",
                  "gg.voidrix.client.v2.modules.loadingtips.LoadingScreenTipsModule",
                  "gg.voidrix.client.v2.modules.thirdparty.SaturationModule",
                  "gg.voidrix.client.v2.modules.thirdparty.ShulkerPreviewModule",
                  "gg.voidrix.client.v2.modules.thirdparty.ThreeDSkinModule",
                  "gg.voidrix.client.v2.modules.thirdparty.TiersModule",
                  "gg.voidrix.client.v2.modules.thirdparty.HealthIndicatorsModule",
                  "gg.voidrix.client.v2.modules.thirdparty.WaveyCapesModule",
                  "gg.voidrix.client.v2.modules.nohurtcam.NoHurtCam",
                  "gg.voidrix.client.v2.modules.discord.DiscordIntegrationModule",
                  "gg.voidrix.client.v2.waypoints.WaypointModule",
                  "gg.voidrix.client.v2.modules.impl.ResourcePackOrganizerModule",
                  "gg.voidrix.client.v2.modules.impl.BadOptimizationsModule"
               )
            )
            val cls: java.util.Collection = ArrayList()

            for (`element$iv$iv$iv` in var5) {
               val var10000: Any = VoidrixReflectionUtil.tryLoadObject(`element$iv$iv$iv` as java.lang.String) as Module
               if (var10000 != null) {
                  cls.add(var10000)
               }
            }

            val var19: Array<Module> = (cls as java.util.List).toArray(arrayOfNulls(0))
            ModuleProvider.Companion.registerAll(Arrays.copyOf(var19, var19.length))
            if (DevAuth.isEnabled) {
               val var21: Module = VoidrixReflectionUtil.tryLoadObject("gg.voidrix.client.v2.modules.impl.DummyModule") as Module
               if (var21 != null) {
                  ModuleProvider.Companion.registerAll(arrayOf(var21))
               }
            }

            for (var36 in CollectionsKt.listOf(
               arrayOf(
                  "gg.voidrix.client.v2.modules.thirdparty.SaturationModule",
                  "gg.voidrix.client.v2.modules.thirdparty.ShulkerPreviewModule",
                  "gg.voidrix.client.v2.modules.thirdparty.ThreeDSkinModule",
                  "gg.voidrix.client.v2.modules.thirdparty.TiersModule",
                  "gg.voidrix.client.v2.modules.thirdparty.HealthIndicatorsModule",
                  "gg.voidrix.client.v2.modules.thirdparty.WaveyCapesModule"
               )
            )) {
               VoidrixReflectionUtil.tryInvokeNoArg(var36 as java.lang.String, "initHooks")
            }

            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.screenshot.ScreenshotModule", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.resourcepackorganizer.RpoToggleLayer", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.tips.LoadingTipsLayer", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.auth.AccountSwitcherLayer", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.serverswitcher.ServerSwitcherLayer", "init")
            VoidrixKeybindsLayer.init()
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.cosmetics.v2.command.CosmeticsCommand", "register")
            ExternalResourcePackRegistry.INSTANCE.register("VoidrixClient/designer")
            DirectoryLinkManager.INSTANCE.setupResourcePackLink()
            DirectoryLinkManager.INSTANCE.setupScreenshotsLink()
            DirectoryLinkManager.INSTANCE.setupShaderPacksLink()
            VoidrixTextureDebugCommand.INSTANCE.init()
            LegacyTweaksDebugCommand.INSTANCE.init()
            val var24: ClientBootstrap.Companion = this

            try {
               var var29: ClientBootstrap.Companion = var24
               val var37: Class = Class.forName("gg.voidrix.client.v2.serverstyling.ServerStylingManager")
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
               DevUtilsKt.voidrixDebug(ClientBootstrap.Companion, "[VoidrixClient] joinEvent fired! ip=$ip")
               ServerLockedModuleManager.INSTANCE.onServerJoin(ip)
               Unit.INSTANCE
            })
            ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
               ServerLockedModuleManager.INSTANCE.onServerDisconnect()
               Unit.INSTANCE
            })
            PunishCommand.INSTANCE.init()
            ReportCommand.INSTANCE.init()
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.waypoints.PersistentWaypointStore", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.waypoints.DeathWaypointHandler", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.waypoints.DestinationWaypointHandler", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.waypoints.WaypointCommand", "register")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.waypoints.PersistentWaypointRenderer", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.testingloop.TestingLoopBootstrap", "init")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.cosmetics.v2.testingloop.CosmeticSyncTests", "register")
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.friends.testingloop.FriendsSyncTests", "register")
            DebugUtil.INSTANCE.init()
            VoidrixReflectionUtil.tryInvokeNoArg("gg.voidrix.client.v2.modules.glintcolorizer.GlintColorizerModule", "init")
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
