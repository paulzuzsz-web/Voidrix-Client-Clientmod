package gg.voidrix.client.v2.modules.tps

import gg.voidrix.compat.event.EntityUpdatePacketEvent
import gg.voidrix.compat.event.PacketEvents
import gg.voidrix.compat.event.TimePacketEvent
import gg.voidrix.compat.text.TextKt
import gg.voidrix.ui.api.hud.SingleTextHud
import java.util.Arrays
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@SourceDebugExtension(["SMAP\nTPS.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TPS.kt\ngg/voidrix/client/v2/modules/tps/TPS\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,115:1\n185#2:116\n40#2:117\n*S KotlinDebug\n*F\n+ 1 TPS.kt\ngg/voidrix/client/v2/modules/tps/TPS\n*L\n84#1:116\n84#1:117\n*E\n"])
public object TPS : SingleTextHud(
      "TPS", "{tps} TPS", null, TextKt.getLiteral("{tps} wird mit den aktuellen TPS ersetzt") as Component, false, false, false, 116
   ) {
   private final val tickTimes: LongArray = LongArray(20)
   private final var nextIndex: Int
   private final var timeGameJoined: Long
   private final var lastTickTime: Long
   private final var tickCount: Long
   private final var lastEntityUpdate: Long
   private final var entityUpdateCount: Int

   private fun onWorldTimeUpdate() {
      val currentTime: Long = System.nanoTime()
      if (timeGameJoined == 0L) {
         this.reset()
      }

      if (lastTickTime > 0L && tickCount > 0L) {
         val timeDiff: Long = currentTime - lastTickTime
         if (currentTime - lastTickTime > 0L && currentTime - lastTickTime < 5000000000L) {
            val msptEstimate: Float = (float)timeDiff / 1000000.0F / 20.0F
            if ((float)timeDiff / 1000000.0F / 20.0F > 10.0F && (float)timeDiff / 1000000.0F / 20.0F < 200.0F) {
               tickTimes[nextIndex] = (long)(msptEstimate * 1000000)
               nextIndex = (nextIndex + 1) % tickTimes.length
            }
         }
      }

      lastTickTime = currentTime
      val var6: Int = tickCount++
   }

   private fun onEntityUpdate() {
      val currentTime: Long = System.nanoTime()
      val timeDiff: Int = entityUpdateCount++
      if (lastEntityUpdate > 0L && entityUpdateCount % 20 == 0) {
         val var6: Long = currentTime - lastEntityUpdate
         if (currentTime - lastEntityUpdate > 0L && currentTime - lastEntityUpdate < 2000000000L) {
            val msptEstimate: Float = (float)var6 / 1000000.0F / 20.0F
            if ((float)var6 / 1000000.0F / 20.0F > 25.0F && (float)var6 / 1000000.0F / 20.0F < 150.0F) {
               tickTimes[nextIndex] = (long)(msptEstimate * 1000000)
               nextIndex = (nextIndex + 1) % tickTimes.length
            }
         }

         lastEntityUpdate = currentTime
      } else if (lastEntityUpdate == 0L) {
         lastEntityUpdate = currentTime
      }
   }

   private fun reset() {
      ArraysKt.fill$default(tickTimes, 0L, 0, 0, 6, null)
      nextIndex = 0
      timeGameJoined = System.currentTimeMillis()
      lastTickTime = 0L
      tickCount = 0L
      lastEntityUpdate = 0L
      entityUpdateCount = 0
   }

   private fun getTickTime(): Float {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player == null) {
         return 50.0F
      } else if (System.currentTimeMillis() - timeGameJoined < 5000L) {
         return 50.0F
      } else {
         var var9: Int = 0
         var var10: Long = 0L

         for (tickTime in tickTimes) {
            if (tickTime > 0L) {
               var10 += tickTime
               var9++
            }
         }

         return if (var9 >= 3) (float)var10 / var9 / 1000000.0F else 50.0F
      }
   }

   private fun getTPS(): Float {
      val mspt: Float = this.getTickTime()
      return if (mspt > 0.0F) Math.min(1000.0F / mspt, 20.0F) else 20.0F
   }

   public open fun getParsedText(): String {
      val tps: Float = this.getTPS()
      val var4: Locale = Locale.ENGLISH
      val var6: Array<Any> = arrayOf(tps)
      val var10000: java.lang.String = java.lang.String.format(var4, "%4.1f", Arrays.copyOf(var6, var6.length))
      return StringsKt.replace(this.getText(), "{tps}", var10000, true)
   }

   public open fun createdAt(): Long {
      return 1744532286000L
   }

   @JvmStatic
   fun {
      PacketEvents.INSTANCE.getTimePacketEvent().listen({ it: TimePacketEvent ->
         INSTANCE.onWorldTimeUpdate()
         Unit.INSTANCE
      })
      PacketEvents.INSTANCE.getEntityUpdatePacketEvent().listen({ it: EntityUpdatePacketEvent ->
         INSTANCE.onEntityUpdate()
         Unit.INSTANCE
      })
   }
}
