package gg.norisk.client.v2.debug

import gg.norisk.compat.command.CommandBuilderKt
import gg.norisk.compat.command.CommandExecutionContext
import gg.norisk.compat.command.LiteralCommandBuilder
import gg.norisk.compat.kotlin.ExtensionsKt
import gg.norisk.compat.task.CoroutineScopesKt
import gg.norisk.compat.task.CoroutineTask
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.BuildersKt

@SourceDebugExtension(["SMAP\nNrcTextureDebugCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcTextureDebugCommand.kt\ngg/norisk/client/v2/debug/NrcTextureDebugCommand\n+ 2 CoroutineTask.kt\ngg/norisk/compat/task/CoroutineTaskKt\n*L\n1#1,19:1\n18#2,12:20\n*S KotlinDebug\n*F\n+ 1 NrcTextureDebugCommand.kt\ngg/norisk/client/v2/debug/NrcTextureDebugCommand\n*L\n12#1:20,12\n*E\n"])
public object NrcTextureDebugCommand {
   public fun init() {
      CommandBuilderKt.clientCommand(
         "nrctexturedebug",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.runsSuccess(
               { $this$runsSuccess: CommandExecutionContext ->
                  val `client$iv`: Boolean = true
                  val `delay$iv`: Long = ExtensionsKt.getTicks(1)
                  val `sync$iv`: Boolean = true
                  BuildersKt.launch$default(
                     CoroutineScopesKt.getMcClientCoroutineScope(),
                     null,
                     null,
                     NrcTextureDebugCommand$init$lambda$2$lambda$1$$inlined$mcCoroutineTask-ML416i8$default$1(
                        `delay$iv`, 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null
                     ),
                     3,
                     null
                  )
                  Unit.INSTANCE
               }
            )
            Unit.INSTANCE
         }
      )
   }
}
