package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.event.ClearBackgroundEvent
import gg.norisk.compat.event.ClearBackgroundEvents
import gg.norisk.compat.nametag.NameTagBackgroundEvent
import gg.norisk.compat.nametag.NameTagBackgroundEventKt
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder

public object ClearBackgroundModule : Module("Clear Background", ModuleCategory.VISUAL, false, false, false, 20) {
   public final val chat: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return chat$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val inventory: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return inventory$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val scoreboard: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return scoreboard$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val nameTags: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return nameTags$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   public final val tablist: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return tablist$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   @JvmStatic
   fun {
      ClearBackgroundEvents.INSTANCE.getChatBackgroundEvent().listen(lambda_0@{ it: ClearBackgroundEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.chat) {
            it.setCancelled(true)
            return@lambda_0 Unit.INSTANCE
         } else {
            return@lambda_0 Unit.INSTANCE
         }
      })
      ClearBackgroundEvents.INSTANCE.getScreenBackgroundEvent().listen(lambda_1@{ it: ClearBackgroundEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.inventory) {
            it.setCancelled(true)
            return@lambda_1 Unit.INSTANCE
         } else {
            return@lambda_1 Unit.INSTANCE
         }
      })
      ClearBackgroundEvents.INSTANCE.getScoreboardBackgroundEvent().listen(lambda_2@{ it: ClearBackgroundEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.scoreboard) {
            it.setCancelled(true)
            return@lambda_2 Unit.INSTANCE
         } else {
            return@lambda_2 Unit.INSTANCE
         }
      })
      ClearBackgroundEvents.INSTANCE.getTablistBackgroundEvent().listen(lambda_3@{ it: ClearBackgroundEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.tablist) {
            it.setCancelled(true)
            return@lambda_3 Unit.INSTANCE
         } else {
            return@lambda_3 Unit.INSTANCE
         }
      })
      NameTagBackgroundEventKt.getNameTagBackgroundEvent().listen(lambda_4@{ it: NameTagBackgroundEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.nameTags) {
            it.setBackgroundColor(1)
            return@lambda_4 Unit.INSTANCE
         } else {
            return@lambda_4 Unit.INSTANCE
         }
      })
   }
}
