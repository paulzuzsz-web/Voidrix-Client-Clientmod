package gg.norisk.client.v2.modules.jumpreset

import gg.norisk.compat.event.CombatEvents
import gg.norisk.compat.event.PlayerEvents
import gg.norisk.compat.event.PlayerGotHitEvent
import gg.norisk.compat.event.PlayerJumpEvent
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.SingleTextHud
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nJumpReset.kt\nKotlin\n*S Kotlin\n*F\n+ 1 JumpReset.kt\ngg/norisk/client/v2/modules/jumpreset/JumpReset\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,50:1\n185#2:51\n40#2:52\n*S KotlinDebug\n*F\n+ 1 JumpReset.kt\ngg/norisk/client/v2/modules/jumpreset/JumpReset\n*L\n31#1:51\n31#1:52\n*E\n"])
public object JumpReset : SingleTextHud("Jump Reset", "", null, null, false, false, false, 124) {
   private final var jumpTick: Int
   private final var hurtTick: Int
   private final var lastDisplayTime: Long

   @Category(name = "Settings")
   @NotNull
   public final var ticks: Number by ValueApiKt.numeric$default(
         10.0, RangesKt.rangeTo(1.0, 100.0) as ClosedRange, 1.0, "Threshold (Ticks)", null, null, 48, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return ticks$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }

      public final set(<set-?>) {
         ticks$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(false, 0, 0, 70, 0, 23, null)
   }

   public open fun getParsedText(): String {
      return if (lastDisplayTime + 2500 > System.currentTimeMillis() && Math.abs(jumpTick - hurtTick) < this.ticks.intValue())
         (
            if (jumpTick == hurtTick + 1)
               "Perfect!"
               else
               (
                  if (hurtTick + 1 < jumpTick)
                     "Late: ${jumpTick - hurtTick + 1} Tick"
                     else
                     (if (hurtTick + 1 > jumpTick) "Early: ${hurtTick + 1 - jumpTick} Tick" else "No Jump")
               )
         )
         else
         "No Jump"
      }

   public open fun createdAt(): Long {
      return 1754244274052L
   }

   @JvmStatic
   fun {
      PlayerEvents.INSTANCE.getPlayerJumpEvent().listen(lambda_0@{ event: PlayerJumpEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            jumpTick = event.getTickCount()
            lastDisplayTime = System.currentTimeMillis()
            return@lambda_0 Unit.INSTANCE
         }
      })
      CombatEvents.INSTANCE.getPlayerGotHitEvent().listen(lambda_1@{ it: PlayerGotHitEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.player != null) {
               hurtTick = var10000.player.tickCount
               lastDisplayTime = System.currentTimeMillis()
               return@lambda_1 Unit.INSTANCE
            } else {
               return@lambda_1 Unit.INSTANCE
            }
         }
      })
   }
}
