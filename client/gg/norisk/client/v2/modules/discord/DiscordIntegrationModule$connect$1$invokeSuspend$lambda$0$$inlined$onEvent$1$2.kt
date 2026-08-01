package gg.norisk.client.v2.modules.discord

import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlinx.coroutines.flow.FlowCollector

{ value: T ->
   var `$continuation`: Continuation
   run label34@{
      if (`$completion` is DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2$1) {
         `$continuation` = `$completion` as DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2$1
         if (((`$completion` as DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2$1).label and Integer.MIN_VALUE) != 0) {
            `$continuation`.label -= Integer.MIN_VALUE
            return@label34
         }
      }

      `$continuation` = DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2$1(this, `$completion`)
   }

   val `$result`: Any = `$continuation`.result
val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED()
   when (`$continuation`.label) {
      0 -> {
         ResultKt.throwOnFailure(`$result`)
         val `$this$filter_u24lambda_u2d0`: FlowCollector = this.$this_unsafeFlow
         if (value is ReadyEvent) {
            `$continuation`.label = 1
            if (`$this$filter_u24lambda_u2d0`.emit(value, `$continuation`) === var5) {
               return var5
            }
         }
      }
      1 -> ResultKt.throwOnFailure(`$result`)
      else -> throw IllegalStateException("call to 'resume' before 'invoke' with coroutine")
   }

   return Unit.INSTANCE
}