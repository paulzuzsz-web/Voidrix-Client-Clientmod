package gg.norisk.client.v2.modules.autoreconnect

import gg.norisk.client.v2.modules.reauth.SessionReauth
import gg.norisk.compat.client.MCClient
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.event.ClientEvents
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.DisconnectedScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.ServerData.Type
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nAutoReconnect.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoReconnect.kt\ngg/norisk/client/v2/modules/autoreconnect/AutoReconnect\n+ 2 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 4 Text.kt\ngg/norisk/compat/text/TextKt\n*L\n1#1,289:1\n63#2:290\n63#2:291\n63#2:292\n63#2:293\n63#2:294\n63#2:296\n63#2:297\n63#2:300\n63#2:301\n63#2:302\n60#2:305\n40#3:295\n328#3:303\n40#3:304\n66#4:298\n66#4:299\n*S KotlinDebug\n*F\n+ 1 AutoReconnect.kt\ngg/norisk/client/v2/modules/autoreconnect/AutoReconnect\n*L\n113#1:290\n145#1:291\n150#1:292\n165#1:293\n177#1:294\n219#1:296\n246#1:297\n285#1:300\n53#1:301\n56#1:302\n22#1:305\n180#1:295\n70#1:303\n70#1:304\n257#1:298\n270#1:299\n*E\n"])
public object AutoReconnect : Module("Auto Reconnect", ModuleCategory.VISUAL, true, true, false, 16) {
   public open val seoTags: Array<String>

   @Category(name = "SETTINGS")
   @NotNull
   public final var autoStart: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return autoStart$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         autoStart$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   private final val log: Logger by LazyKt.lazy(AutoReconnect$special$$inlined$lazyLogger$1.INSTANCE)
      private final get() {
         return log$delegate.getValue() as Logger
      }


   public final var lastServerIp: String?
      private set

   public final var reconnectAttempts: Int
      private set

   public final var secondsRemaining: Int
      private set

   public final var isReconnecting: Boolean
      private set

   private final var tickCounter: Int

   public final var reconnectButton: Any?
      internal set

   public final var pauseButton: Any?
      internal set

   public final var isPaused: Boolean
      private set

   public final var needsReauth: Boolean
      private set

   public fun onDisconnectedScreenInit(reasonText: String? = null) {
      needsReauth = SessionReauth.INSTANCE.shouldShow(reasonText)
      val currentIp: java.lang.String = MCClient.getCurrentServerIp()
      if (currentIp != null) {
         lastServerIp = currentIp
      }

      if (lastServerIp != null) {
         val `$this$nrcDebug$iv`: Logger = this.log
         val `message$iv`: java.lang.String = "[AutoReconnect] DisconnectedScreen init, lastServerIp=${lastServerIp} needsReauth=${needsReauth}"
         if (MCLogger.IS_DEBUG) {
            `$this$nrcDebug$iv`.info(`message$iv`)
         }

         if (this.autoStart) {
            this.startReconnect()
         }
      }
   }

   public fun onReconnectButtonClick() {
      if (isReconnecting) {
         this.performReconnect()
      } else {
         this.startReconnect()
      }
   }

   public fun stop() {
      isReconnecting = false
      isPaused = false
      secondsRemaining = 0
      tickCounter = 0
   }

   private fun startReconnect() {
      isReconnecting = true
      secondsRemaining = this.getReconnectDelay(reconnectAttempts)
      tickCounter = 0
      val `$this$nrcDebug$iv`: Logger = this.log
      val `message$iv`: java.lang.String = "[AutoReconnect] Starting reconnect timer: ${secondsRemaining}s (attempt #${reconnectAttempts + 1})"
      if (MCLogger.IS_DEBUG) {
         `$this$nrcDebug$iv`.info(`message$iv`)
      }
   }

   private fun cancelReconnect() {
      if (isReconnecting) {
         val `$this$nrcDebug$iv`: Logger = this.log
         if (MCLogger.IS_DEBUG) {
            `$this$nrcDebug$iv`.info("[AutoReconnect] Reconnect cancelled")
         }
      }

      isReconnecting = false
      isPaused = false
      secondsRemaining = 0
      tickCounter = 0
   }

   private fun performReconnect() {
      if (needsReauth) {
         isReconnecting = false
         secondsRemaining = 0
         tickCounter = 0
         val `$this$nrcDebug$iv`: Logger = this.log
         if (MCLogger.IS_DEBUG) {
            `$this$nrcDebug$iv`.info("[AutoReconnect] needsReauth → SessionReauth.triggerReauth()")
         }

         SessionReauth.INSTANCE.triggerReauth()
      } else {
         this.reconnectNow()
      }
   }

   public fun reconnectNow() {
      if (lastServerIp != null) {
         val ip: java.lang.String = lastServerIp
         isReconnecting = false
         val e: Int = reconnectAttempts++
         val var7: Logger = this.log
         val `$this$nrcDebug$iv`: java.lang.String = "[AutoReconnect] Reconnecting to $ip (attempt #${reconnectAttempts})"
         if (MCLogger.IS_DEBUG) {
            var7.info(`$this$nrcDebug$iv`)
         }

         try {
            val var10000: Minecraft = Minecraft.getInstance()
            ConnectScreen.startConnecting(
               TitleScreen() as Screen, var10000, ServerAddress.parseString(ip), ServerData("reconnect", ip, Type.OTHER), false, null
            )
         } catch (var6: Exception) {
            val var9: Logger = this.log
            val `message$ivx`: java.lang.String = "[AutoReconnect] Failed to reconnect: ${var6.getMessage()}"
            if (MCLogger.IS_DEBUG) {
               var9.info(`message$ivx`)
            }

            this.startReconnect()
         }
      }
   }

   public fun getButtonText(): String {
      val base: java.lang.String = if (needsReauth) SessionReauth.INSTANCE.buttonText() else "Reconnect"
      return if (isReconnecting && secondsRemaining > 0) "$base (${secondsRemaining}s)" else base
   }

   private fun getReconnectDelay(attempts: Int): Int {
      return if (attempts <= 0) 5 else (if (attempts == 1) 10 else (if (attempts == 2) 15 else (if (attempts == 3) 30 else 60)))
   }

   public fun onPauseButtonClick() {
      isPaused = !isPaused
      val `$this$nrcDebug$iv`: Logger = this.log
      val `message$iv`: java.lang.String = "[AutoReconnect] ${if (isPaused) "Paused" else "Resumed"}"
      if (MCLogger.IS_DEBUG) {
         `$this$nrcDebug$iv`.info(`message$iv`)
      }

      this.updatePauseButtonText()
   }

   public fun getPauseButtonText(): String {
      return if (isPaused) "▶" else "⏸"
   }

   private fun updatePauseButtonText() {
      if (pauseButton != null) {
         val btn: Any = pauseButton

         try {
            val var10000: Button = btn as? Button
            if ((btn as? Button) == null) {
               return
            }

            val var10001: MutableComponent = Component.literal(this.getPauseButtonText())
            var10000.setMessage(var10001 as Component)
         } catch (var5: Exception) {
         }
      }
   }

   private fun updateButtonText() {
      if (reconnectButton != null) {
         val btn: Any = reconnectButton

         try {
            val var10000: Button = btn as? Button
            if ((btn as? Button) == null) {
               return
            }

            val var10001: MutableComponent = Component.literal(this.getButtonText())
            var10000.setMessage(var10001 as Component)
         } catch (var5: Exception) {
         }
      }
   }

   public fun hasLastServer(): Boolean {
      return lastServerIp != null
   }

   public fun cacheServerIp(ip: String) {
      lastServerIp = ip
      val `$this$nrcDebug$iv`: Logger = this.log
      val `message$iv`: java.lang.String = "[AutoReconnect] Cached server IP from connect: $ip"
      if (MCLogger.IS_DEBUG) {
         `$this$nrcDebug$iv`.info(`message$iv`)
      }
   }

   @JvmStatic
   fun {
      val var3: Any = INSTANCE
      ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
         val ip: java.lang.String = MCClient.getCurrentServerIp()
         if (ip != null) {
            lastServerIp = ip
            val `$this$nrcDebug$iv`: Logger = INSTANCE.log
            val `message$iv`: java.lang.String = "[AutoReconnect] Cached server IP: $ip"
            if (MCLogger.IS_DEBUG) {
               `$this$nrcDebug$iv`.info(`message$iv`)
            }
         }

         if (isReconnecting) {
            val var5: Logger = INSTANCE.log
            if (MCLogger.IS_DEBUG) {
               var5.info("[AutoReconnect] Successfully reconnected!")
            }
         }

         isReconnecting = false
         reconnectAttempts = 0
         secondsRemaining = 0
         tickCounter = 0
         needsReauth = false
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getClientTickEvent().listen(lambda_1@{ it: Unit ->
         if (INSTANCE.isEnabled() && isReconnecting && !isPaused) {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() !is DisconnectedScreen) {
               INSTANCE.cancelReconnect()
               return@lambda_1 Unit.INSTANCE
            } else {
               val var4: Int = tickCounter++
               if (tickCounter >= 20) {
                  tickCounter = 0
                  secondsRemaining += -1
                  INSTANCE.updateButtonText()
                  if (secondsRemaining <= 0) {
                     INSTANCE.performReconnect()
                  }
               }

               return@lambda_1 Unit.INSTANCE
            }
         } else {
            return@lambda_1 Unit.INSTANCE
         }
      })
   }
}
