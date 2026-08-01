package gg.voidrix.client.v2.auth

import gg.voidrix.compat.auth.MinecraftSessionAccessor
import gg.voidrix.compat.auth.SessionData
import gg.voidrix.compat.auth.SessionStatus
import gg.voidrix.compat.auth.SessionSwapper
import gg.voidrix.compat.auth.launcher.Credentials
import gg.voidrix.compat.auth.launcher.LoginOrchestrator
import gg.voidrix.compat.client.MCLogger
import java.util.concurrent.CompletableFuture
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nAccountSwitcherButton.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AccountSwitcherButton.kt\ngg/voidrix/client/v2/auth/AccountSwitcherButton\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,87:1\n40#2:88\n*S KotlinDebug\n*F\n+ 1 AccountSwitcherButton.kt\ngg/voidrix/client/v2/auth/AccountSwitcherButton\n*L\n62#1:88\n*E\n"])
public object AccountSwitcherButton {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-AccountSwitcher")
   private const val CACHE_DURATION_MS: Long = 60000L
   private final var cachedStatus: SessionStatus?
   private final var lastCheckTime: Long
   private final var isChecking: Boolean

   public fun getOrRefreshStatus(): SessionStatus? {
      if (cachedStatus != null && System.currentTimeMillis() - lastCheckTime < 60000L) {
         return cachedStatus
      } else {
         if (!isChecking) {
            isChecking = true
            CompletableFuture.runAsync({ 
               try {
                  cachedStatus = SessionSwapper.INSTANCE.getSessionStatus()
                  lastCheckTime = System.currentTimeMillis()
               } catch (var3: Exception) {
                  logger.warn("Failed to check session status: ${var3.getMessage()}")
                  cachedStatus = SessionStatus.OFFLINE
                  lastCheckTime = System.currentTimeMillis()
               } finally {
                  isChecking = false
               }
            })
         }

         return cachedStatus
      }
   }

   public fun invalidateCache() {
      cachedStatus = null
      lastCheckTime = 0L
   }

   public fun getCurrentUsername(): String {
      val var10000: Minecraft = Minecraft.getInstance()
      val var3: java.lang.String = (var10000 as MinecraftSessionAccessor).compatGetUser().getName()
      return var3
   }

   public fun startDirectAddFlow(onComplete: ((Boolean) -> Unit)? = null) {
      CompletableFuture.runAsync({ 
         var success: Boolean = false

         try {
            val e: Credentials = LoginOrchestrator.INSTANCE.loginDirect()
            SessionSwapper.INSTANCE.swap(SessionData(e.getAccessToken(), e.getId(), e.getUsername()), e.getId())
            INSTANCE.invalidateCache()
            success = true
         } catch (var5: Exception) {
            logger.warn("Direct add-account flow failed: ${var5.getMessage()}")
         } finally {
            if (`$onComplete` != null) {
               `$onComplete`(success)
            }
         }
      })
   }
}
