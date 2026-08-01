package gg.norisk.client.v2.modules.discord

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler.Key

@SourceDebugExtension(["SMAP\nCoroutineExceptionHandler.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CoroutineExceptionHandler.kt\nkotlinx/coroutines/CoroutineExceptionHandlerKt$CoroutineExceptionHandler$1\n+ 2 DiscordIntegrationModule.kt\ngg/norisk/client/v2/modules/discord/DiscordIntegrationModule\n*L\n1#1,49:1\n43#2,2:50\n*E\n"])
// $VF: local visibility outside of methodSupplier
internal class `DiscordIntegrationModule$special$$inlined$CoroutineExceptionHandler$1` : AbstractCoroutineContextElement, CoroutineExceptionHandler {
   fun `DiscordIntegrationModule$special$$inlined$CoroutineExceptionHandler$1`(`$super_call_param$1`: Key) {
      super(`$super_call_param$1` as kotlin.coroutines.CoroutineContext.Key)
   }

   public open fun handleException(context: CoroutineContext, exception: Throwable) {
      DiscordIntegrationModule.access$getDiscordLogger$p().warn("Discord IPC error: ${exception.getMessage()}")
   }
}
