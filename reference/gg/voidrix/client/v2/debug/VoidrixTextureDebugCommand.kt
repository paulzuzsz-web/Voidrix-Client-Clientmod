package gg.voidrix.client.v2.debug

import gg.voidrix.compat.command.CommandBuilderKt
import gg.voidrix.compat.command.CommandExecutionContext
import gg.voidrix.compat.command.LiteralCommandBuilder
import gg.voidrix.compat.kotlin.ExtensionsKt
import gg.voidrix.compat.task.CoroutineScopesKt
import gg.voidrix.compat.task.CoroutineTask
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.BuildersKt

@SourceDebugExtension(["SMAP\nVoidrixTextureDebugCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 VoidrixTextureDebugCommand.kt\ngg/voidrix/client/v2/debug/VoidrixTextureDebugCommand\n+ 2 CoroutineTask.kt\ngg/voidrix/compat/task/CoroutineTaskKt\n*L\n1#1,19:1\n18#2,12:20\n*S KotlinDebug\n*F\n+ 1 VoidrixTextureDebugCommand.kt\ngg/voidrix/client/v2/debug/VoidrixTextureDebugCommand\n*L\n12#1:20,12\n*E\n"])
public object VoidrixTextureDebugCommand {
   public fun init() {
      CommandBuilderKt.clientCommand(
         "voidrixtexturedebug",
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
                     VoidrixTextureDebugCommand$init$lambda$2$lambda$1$$inlined$mcCoroutineTask-ML416i8$default$1(
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
