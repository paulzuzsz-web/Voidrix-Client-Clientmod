package gg.voidrix.client.v2.modules.discord

import dev.cbyrne.kdiscordipc.KDiscordIPC
import gg.voidrix.client.v2.serverstyling.ServerStylingManager
import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.compat.event.ScreenChangeEvent
import gg.voidrix.compat.host.HostedWorldService
import gg.voidrix.compat.manager.ManagerProvider
import gg.voidrix.compat.server.ServerInfoExt
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.modules.IModuleScreen
import java.lang.invoke.StringConcatFactory
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineScopeKt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorKt
import net.minecraft.SharedConstants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
import net.minecraft.client.multiplayer.ServerData
import org.slf4j.Logger

@VoidrixMiniTag(tags = ["discord"])
@SourceDebugExtension(["SMAP\nDiscordIntegrationModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DiscordIntegrationModule.kt\ngg/voidrix/client/v2/modules/discord/DiscordIntegrationModule\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 4 MCServer.kt\ngg/voidrix/compat/host/MCServer\n+ 5 CoroutineExceptionHandler.kt\nkotlinx/coroutines/CoroutineExceptionHandlerKt\n*L\n1#1,244:1\n1#2:245\n328#3:246\n40#3:247\n40#3:250\n95#4:248\n95#4:249\n47#5,4:251\n*S KotlinDebug\n*F\n+ 1 DiscordIntegrationModule.kt\ngg/voidrix/client/v2/modules/discord/DiscordIntegrationModule\n*L\n136#1:246\n136#1:247\n194#1:250\n144#1:248\n149#1:249\n42#1:251,4\n*E\n"])
public object DiscordIntegrationModule : Module("Discord Integration", ModuleCategory.VISUAL, true, true, false, 16) {
   public open val seoTags: Array<String>
   private const val CLIENT_ID: String = "1237087999104122981"
   private final val discordLogger: Logger = MCLogger.getLogger("DiscordRPC")
   private final val scope: CoroutineScope =
      CoroutineScopeKt.CoroutineScope(
         Dispatchers.getIO()
            .plus(SupervisorKt.SupervisorJob$default(null, 1, null) as CoroutineContext)
            .plus(DiscordIntegrationModule$special$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key) as CoroutineContext)
      )
      private final var ipc: KDiscordIPC?
   private final val launchTime: Long = System.currentTimeMillis()
   private final var lastState: String = ""
   private final var connected: Boolean
   private final var tickCounter: Long
   private final val gameVersion: String

   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }

   public open fun onEnable() {
      this.connect()
   }

   public open fun onDisable() {
      DiscordStateFile.INSTANCE.cleanup()
      this.disconnect()
   }

   private fun connect() {
      val newIpc: KDiscordIPC = if (KdiscordIpcSocketLoader.INSTANCE.isAvailable()) KDiscordIPC("1237087999104122981", { 
         KdiscordIpcSocketLoader.INSTANCE.openIsolatedSocket()
      }, null, 4, null) else KDiscordIPC("1237087999104122981", null, null, 6, null)
      ipc = newIpc
      BuildersKt.launch$default(scope, null, null, {
         // $VF: Could not decompile lambda - root function was not found. Is this a suspend lambda?
      } as Function2, 3, null)
   }

   private fun disconnect() {
      connected = false
      val var1: DiscordIntegrationModule = this

      try {
         var var5: DiscordIntegrationModule = var1
         val var10000: Unit
         if (ipc != null) {
            ipc.disconnect()
            var10000 = Unit.INSTANCE
         } else {
            var10000 = null
         }

         var5 = (DiscordIntegrationModule)Result.constructor_impl/* $VF was: constructor-impl */(var10000)
      } catch (var4: java.lang.Throwable) {
         val `$this$disconnect_u24lambda_u248`: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
      }

      ipc = null
   }

   private fun updateState() {
      val state: java.lang.String = this.resolveState()
      if (!(state == lastState)) {
         discordLogger.info("[DiscordRPC] State changed: '${lastState}' -> '$state' (connected=${connected})")
         this.updatePresence(state)
      }
   }

   private fun resolveState(): String {
      val var10001: Minecraft = Minecraft.getInstance()
      val screenState: java.lang.String = this.resolveScreenState(var10001.gui.screen())
      if (screenState != null) {
         return screenState
      } else if (HostedWorldService.INSTANCE.isHosting()) {
         return "Playing Private Multiplayer"
      } else {
         val var4: java.lang.String = MCClient.getCurrentServerIp()
         if (var4 != null && !Minecraft.getInstance().isLocalServer()) {
            return this.formatServerAddress(var4)
         } else {
            return if (Minecraft.getInstance().isLocalServer()) "Playing Singleplayer" else "Idling"
         }
      }
   }

   private fun resolveScreenState(screen: Any?): String? {
      if (screen == null) {
         return null
      } else {
         return if (screen.getClass().getName() == "gg.voidrix.minigames.base.MinigameScreen")
            "Playing a Minigame"
            else
            (
               if (screen.getClass().getName() == "gg.voidrix.cosmetics.v2.ui.newwardrobe.NewWardrobeScreen")
                  "Browsing Wardrobe"
                  else
                  (
                     if (screen.getClass().getName() == "gg.voidrix.mcreal.ui.McRealScreen")
                        "Browsing McReal"
                        else
                        (
                           if (screen is IModuleScreen)
                              "Configuring Mods"
                              else
                              (
                                 if (screen is TitleScreen)
                                    "In Main Menu"
                                    else
                                    (
                                       if (screen is JoinMultiplayerScreen)
                                          "Browsing Servers"
                                          else
                                          (if (screen is SelectWorldScreen) "Selecting a World" else null)
                                    )
                              )
                        )
                  )
            )
         }
   }

   private fun formatServerAddress(ip: String): String {
      val host: java.lang.String = StringsKt.split$default(ip, arrayOf(":"), false, 0, 6, null).get(0) as java.lang.String
      val var8: Boolean = ServerStylingManager.getStyledServer(host) != null
      val var11: Minecraft = Minecraft.getInstance()
      val serverData: ServerData = var11.getCurrentServer()
      if (!var8 && ((serverData as? ServerInfoExt) == null || !(serverData as? ServerInfoExt).getVoidrix_friendsVisible())) {
         return "Playing Multiplayer"
      } else if (!var8 && !ManagerProvider.INSTANCE.getShowServerToFriends()) {
         return "Playing Multiplayer"
      } else {
         val port: java.lang.String = CollectionsKt.getOrNull(StringsKt.split$default(ip, arrayOf(":"), false, 0, 6, null), 1) as java.lang.String
         return "Playing $host${if (port != null && !(port == "25565")) ":$port" else ""}"
      }
   }

   private fun updatePresence(state: String) {
      lastState = state
      DiscordStateFile.INSTANCE.writeState(state, "Minecraft ${gameVersion}")
      if (ipc != null) {
         val currentIpc: KDiscordIPC = ipc
         if (connected) {
            val launcherState: DiscordEntry = DiscordStateFile.INSTANCE.readLauncherState()
            if (launcherState == null || launcherState.details == null) {
               StringConcatFactory.makeConcatWithConstants<"makeConcatWithConstants","Minecraft \u0001">(gameVersion)
            }

            BuildersKt.launch$default(scope, null, null, {
               // $VF: Could not decompile lambda - root function was not found. Is this a suspend lambda?
            } as Function2, 3, null)
         }
      }
   }

   @JvmStatic
   fun {
      val var3: java.lang.String = SharedConstants.getCurrentVersion().name()
      gameVersion = var3
      ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
         INSTANCE.updateState()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         INSTANCE.updatePresence("Idling")
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         val var1: Int = tickCounter++
         if (connected && tickCounter % 100 == 0L) {
            INSTANCE.updateState()
         }

         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getScreenChangeEvent().listen({ event: ScreenChangeEvent ->
         val screenState: java.lang.String = INSTANCE.resolveScreenState(event.getScreen())
         if (screenState != null) {
            INSTANCE.updatePresence(screenState)
         }

         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getClientStoppingEvent().listen({ it: Unit ->
         INSTANCE.disconnect()
         Unit.INSTANCE
      })
   }
}
