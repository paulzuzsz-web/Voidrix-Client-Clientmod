package gg.voidrix.client.v2.modules.reachdisplay

import gg.voidrix.compat.event.CombatEvents
import gg.voidrix.compat.event.PlayerAttackAttemptEvent
import gg.voidrix.compat.text.TextKt
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.SingleTextHud
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

@SourceDebugExtension(["SMAP\nReachDisplay.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ReachDisplay.kt\ngg/voidrix/client/v2/modules/reachdisplay/ReachDisplay\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,79:1\n185#2:80\n40#2:81\n40#2:82\n*S KotlinDebug\n*F\n+ 1 ReachDisplay.kt\ngg/voidrix/client/v2/modules/reachdisplay/ReachDisplay\n*L\n29#1:80\n29#1:81\n31#1:82\n*E\n"])
public object ReachDisplay : SingleTextHud(
      "Reach Display", "{reach} blocks", null, TextKt.getLiteral("{reach} wird mit der Reichweite replaced") as Component, false, false, false, 116
   ) {
   private final var distance: Double
   private final var lastAttackTime: Long
   private final val decimalFormat: DecimalFormat = DecimalFormat("0.00", DecimalFormatSymbols(Locale.ENGLISH))

   private fun computeReachDistance() {
      var var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player != null) {
         val player: LocalPlayer = var10000.player
         var10000 = Minecraft.getInstance()
         val hitResult: HitResult = var10000.hitResult
         if (var10000.hitResult is EntityHitResult) {
            val var6: Entity = (var10000.hitResult as EntityHitResult).getEntity()
            if (var6 !is LivingEntity || (var6 as LivingEntity).hurtTime <= 0) {
               distance = (hitResult as EntityHitResult).getLocation().distanceTo(player.getEyePosition())
               lastAttackTime = System.currentTimeMillis()
            }
         }
      }
   }

   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(false, 0, 0, 65, 0, 23, null)
   }

   public open fun getParsedText(): String {
      if (System.currentTimeMillis() - lastAttackTime > 5000L) {
         return StringsKt.replace(this.getText(), "{reach}", "0", true)
      } else {
         val var10000: java.lang.String = this.getText()
         val var10002: java.lang.String = decimalFormat.format(distance)
         return StringsKt.replace(var10000, "{reach}", var10002, true)
      }
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @JvmStatic
   fun {
      CombatEvents.INSTANCE.getPlayerAttackAttemptEvent().listen(lambda_0@{ event: PlayerAttackAttemptEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            INSTANCE.computeReachDistance()
            return@lambda_0 Unit.INSTANCE
         }
      })
   }
}
