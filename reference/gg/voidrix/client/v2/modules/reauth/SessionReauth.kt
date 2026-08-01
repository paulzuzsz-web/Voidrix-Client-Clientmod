package gg.voidrix.client.v2.modules.reauth

import gg.voidrix.client.v2.modules.autoreconnect.AutoReconnect
import gg.voidrix.compat.auth.SessionData
import gg.voidrix.compat.auth.SessionSwapper
import gg.voidrix.compat.auth.launcher.Credentials
import gg.voidrix.compat.auth.launcher.MinecraftAuthStore
import gg.voidrix.compat.auth.launcher.TokenRefresh
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.ui.v2.toast.LoadingToastHandle
import gg.voidrix.ui.v2.toast.VoidrixLoadingToast
import java.util.Arrays
import java.util.Locale
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nSessionReauth.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SessionReauth.kt\ngg/voidrix/client/v2/modules/reauth/SessionReauth\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,83:1\n1761#2,3:84\n67#3:87\n269#3:88\n67#3:89\n269#3:90\n67#3:93\n269#3:94\n67#3:95\n269#3:96\n369#4:91\n40#4:92\n*S KotlinDebug\n*F\n+ 1 SessionReauth.kt\ngg/voidrix/client/v2/modules/reauth/SessionReauth\n*L\n35#1:84,3\n38#1:87\n38#1:88\n42#1:89\n42#1:90\n55#1:93\n55#1:94\n69#1:95\n69#1:96\n76#1:91\n76#1:92\n*E\n"])
public object SessionReauth {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-SessionReauth")
   private final val running: AtomicBoolean = AtomicBoolean(false)
   private final val AUTH_FAIL_MARKERS: List<String> =
      CollectionsKt.listOf(
         arrayOf("invalid session", "multiplayer.disconnect.unverified_username", "multiplayer.disconnect.invalid_session", "disconnect.loginfailedinfo")
      )

   public fun shouldShow(reasonText: String?): Boolean {
      if (reasonText == null || StringsKt.isBlank(reasonText)) {
         return false
      } else {
         val var10000: java.lang.String = reasonText.toLowerCase(Locale.ROOT)
         val var9: java.lang.String = var10000
         val `$this$any$iv`: java.lang.Iterable = AUTH_FAIL_MARKERS
         var var10: Boolean
         if (AUTH_FAIL_MARKERS is java.util.Collection && AUTH_FAIL_MARKERS.isEmpty()) {
            var10 = false
         } else {
            val var5: java.util.Iterator = `$this$any$iv`.iterator()

            while (true) {
               if (!var5.hasNext()) {
                  var10 = false
                  break
               }

               if (StringsKt.contains$default(var9, var5.next() as java.lang.String, false, 2, null)) {
                  var10 = true
                  break
               }
            }
         }

         return var10
      }
   }

   public fun buttonText(): String {
      val `$i$f$asString`: Array<Any> = arrayOfNulls(0)
      val var10000: MutableComponent = Component.translatable("voidrix.ui.reauth.button", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
      val var6: java.lang.String = (var10000 as Component).getString()
      return var6
   }

   public fun triggerReauth() {
      if (running.compareAndSet(false, true)) {
         val var10000: VoidrixLoadingToast = VoidrixLoadingToast.INSTANCE
         val `$i$f$asString`: Array<Any> = arrayOfNulls(0)
         val var10001: MutableComponent = Component.translatable("voidrix.ui.reauth.refreshing", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
         val var7: java.lang.String = (var10001 as Component).getString()
         CompletableFuture.runAsync(
            { 
               // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
               // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.modules.decompiler.exps.Exprent.toJava(int)" because the return value of "org.vineflower.kotlin.expr.KExitExprent.getValue()" is null
               //   at org.vineflower.kotlin.expr.KExitExprent.toJava(KExitExprent.java:41)
               //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:925)
               //   at org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:87)
            }
         )
      }
   }

   private fun onReauthed() {
      val `action$iv`: Runnable = { 
         if (AutoReconnect.INSTANCE.hasLastServer()) {
            AutoReconnect.INSTANCE.reconnectNow()
         }
      }
      val var10000: Minecraft = Minecraft.getInstance()
      var10000.execute(`action$iv`)
   }
}
