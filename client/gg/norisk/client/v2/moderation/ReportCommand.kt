package gg.norisk.client.v2.moderation

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import gg.norisk.compat.command.ArgumentCommandBuilder
import gg.norisk.compat.command.CommandBuilder
import gg.norisk.compat.command.CommandBuilderKt
import gg.norisk.compat.command.LiteralCommandBuilder
import gg.norisk.compat.kotlin.ExtensionsKt
import gg.norisk.compat.task.CoroutineScopesKt
import gg.norisk.compat.task.CoroutineTask
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt

@SourceDebugExtension(["SMAP\nReportCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ReportCommand.kt\ngg/norisk/client/v2/moderation/ReportCommand\n+ 2 CoroutineTask.kt\ngg/norisk/compat/task/CoroutineTaskKt\n+ 3 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder\n*L\n1#1,47:1\n18#2,12:48\n147#3,7:60\n*S KotlinDebug\n*F\n+ 1 ReportCommand.kt\ngg/norisk/client/v2/moderation/ReportCommand\n*L\n38#1:48,12\n17#1:60,7\n*E\n"])
public object ReportCommand {
   public fun init() {
      CommandBuilderKt.clientCommand(
         "nrc",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "report",
               { $this$literal: LiteralCommandBuilder ->
                  // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
                  // java.lang.IllegalStateException: Anonymous class does not have Class Kotlin metadata
                  //   at org.vineflower.kotlin.KotlinWriter.writeClassDefinition(KotlinWriter.java:742)
                  //   at org.vineflower.kotlin.KotlinWriter.writeClass(KotlinWriter.java:309)
                  //   at org.vineflower.kotlin.expr.KNewExprent.toJava(KNewExprent.java:178)
                  //   at org.vineflower.kotlin.expr.KFunctionExprent.toJava(KFunctionExprent.java:196)
                  //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1054)
                  //   at org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:1151)
                  //   at org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.toJava(InvocationExprent.java:921)
               }
            )
            Unit.INSTANCE
         }
      )
   }

   public fun openReportScreen(uuid: UUID) {
      val `sync$iv`: Boolean = true
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getMcClientCoroutineScope(),
         null,
         null,
         ReportCommand$openReportScreen$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, uuid
         ),
         3,
         null
      )
   }
}
