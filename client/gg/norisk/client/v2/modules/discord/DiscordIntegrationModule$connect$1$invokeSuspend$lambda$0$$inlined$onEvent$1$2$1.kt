package gg.norisk.client.v2.modules.discord

import kotlin.coroutines.Continuation
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.coroutines.jvm.internal.DebugMetadata

// $VF: Class flags could not be determined
@DebugMetadata(f = "DiscordIntegrationModule.kt", l = [224], i = [], s = [], n = [], m = "emit", c = "gg.norisk.client.v2.modules.discord.DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2")
internal class `DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2$1` : ContinuationImpl {
   open int label;
   open Object L$0;
   open Object L$1;

   fun `DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2$1`(
      `this$0`: DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2, `$completion`: Continuation
   ) {
      super(`$completion`)
      this.this$0 = `this$0`
   }

   fun invokeSuspend(`$result`: Any) {
      this.result = `$result`
      this.label |= Integer.MIN_VALUE
      this.this$0.emit(null, this as Continuation)
   }
}
