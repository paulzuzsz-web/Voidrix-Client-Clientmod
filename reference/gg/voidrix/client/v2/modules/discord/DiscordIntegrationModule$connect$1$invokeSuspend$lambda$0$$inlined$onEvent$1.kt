package gg.voidrix.client.v2.modules.discord

import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@SourceDebugExtension(["SMAP\nSafeCollector.common.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SafeCollector.common.kt\nkotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1\n+ 2 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt\n*L\n1#1,112:1\n51#2,5:113\n*E\n"])
// $VF: local visibility outside of methodSupplier
internal class `DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1` : Flow<Object> {
   fun `DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1`(var1: Flow) {
      this.$this_unsafeTransform$inlined = var1
   }

   public open suspend fun collect(collector: FlowCollector<Any>) {
      val var10000: Any = this.$this_unsafeTransform$inlined
         .collect(DiscordIntegrationModule$connect$1$invokeSuspend$lambda$0$$inlined$onEvent$1$2(collector), `$completion`)
         return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE
   }
}
