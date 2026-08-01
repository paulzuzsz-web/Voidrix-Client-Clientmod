package gg.norisk.client.v2.modules.combocounter

import gg.norisk.compat.event.CombatEvents
import gg.norisk.compat.event.PlayerGotHitEvent
import gg.norisk.compat.event.PlayerHitEntityEvent
import gg.norisk.compat.text.TextKt
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.SingleTextHud
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.NotNull

public object ComboCounter : SingleTextHud(
      "Combo Counter", "{combo} Combo", null, TextKt.getLiteral("{combo} wird mit dem aktuellen Combo ersetzt") as Component, false, false, false, 116
   ) {
   public final var currentCombo: Int
      internal set

   public final var lastAttackId: Int = -1
      internal set

   public final var lastHitTime: Long
      internal set

   @Category(name = "Settings")
   @NotNull
   public final var comboActiveTime: Number by ValueApiKt.numeric$default(
         2.0, RangesKt.rangeTo(2.0, 6.0) as ClosedRange, 0.5F, "Combo Active Time (s)", null, null, 48, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return comboActiveTime$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }

      public final set(<set-?>) {
         comboActiveTime$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   private fun resetCombo() {
      currentCombo = 0
      lastAttackId = -1
      lastHitTime = 0L
   }

   public open fun shouldStopRenderExecution(): Boolean {
      if (currentCombo > 0 && System.currentTimeMillis() - lastHitTime >= (long)(this.comboActiveTime.doubleValue() * 1000)) {
         this.resetCombo()
      }

      return false
   }

   public open fun getParsedText(): String {
      return StringsKt.replace(this.getText(), "{combo}", java.lang.String.valueOf(currentCombo), true)
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @JvmStatic
   fun {
      CombatEvents.INSTANCE.getPlayerHitEntityEvent().listen(lambda_0@{ event: PlayerHitEntityEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            if (lastAttackId != -1 && lastAttackId == event.getAttackedEntityId()) {
               val var1: Int = currentCombo++
            } else {
               lastAttackId = event.getAttackedEntityId()
               currentCombo = 1
            }

            lastHitTime = System.currentTimeMillis()
            return@lambda_0 Unit.INSTANCE
         }
      })
      CombatEvents.INSTANCE.getPlayerGotHitEvent().listen({ it: PlayerGotHitEvent ->
         if (INSTANCE.isEnabled()) {
            INSTANCE.resetCombo()
         }

         Unit.INSTANCE
      })
   }
}
