package gg.norisk.client.v2.moderation

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import gg.norisk.compat.client.PlayerResolver
import gg.norisk.compat.command.ArgumentCommandBuilder
import gg.norisk.compat.command.CommandBuilder
import gg.norisk.compat.command.CommandBuilderKt
import gg.norisk.compat.command.LiteralCommandBuilder
import gg.norisk.compat.kotlin.ExtensionsKt
import gg.norisk.compat.task.CoroutineScopesKt
import gg.norisk.compat.task.CoroutineTask
import java.util.UUID
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nPunishCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PunishCommand.kt\ngg/norisk/client/v2/moderation/PunishCommand\n+ 2 CoroutineTask.kt\ngg/norisk/compat/task/CoroutineTaskKt\n+ 3 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder\n+ 4 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n*L\n1#1,151:1\n18#2,12:152\n18#2,12:164\n18#2,12:176\n18#2,12:188\n147#3,7:200\n147#3,5:207\n147#3,5:212\n147#3,7:217\n152#3,2:224\n152#3,2:226\n147#3,5:228\n147#3,5:233\n147#3,7:238\n152#3,2:245\n152#3,2:247\n60#4:249\n*S KotlinDebug\n*F\n+ 1 PunishCommand.kt\ngg/norisk/client/v2/moderation/PunishCommand\n*L\n89#1:152,12\n112#1:164,12\n130#1:176,12\n141#1:188,12\n29#1:200,7\n37#1:207,5\n42#1:212,5\n47#1:217,7\n42#1:224,2\n37#1:226,2\n57#1:228,5\n62#1:233,5\n67#1:238,7\n62#1:245,2\n57#1:247,2\n23#1:249\n*E\n"])
public object PunishCommand {
   private final val logger: Logger by LazyKt.lazy(PunishCommand$special$$inlined$lazyLogger$1.INSTANCE)
      private final get() {
         return logger$delegate.getValue() as Logger
      }


   public fun init() {
      CommandBuilderKt.clientCommand(
         "nrc",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "staff",
               { $this$literal: LiteralCommandBuilder ->
                  `$this$literal`.literal(
                     "vc-pardon",
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
                  `$this$literal`.literal(
                     "vc-ban",
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
                  `$this$literal`.literal(
                     "vc-mute",
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
            Unit.INSTANCE
         }
      )
   }

   private fun resolveAndExecute(input: String, action: (String) -> Unit) {
      PlayerResolver.INSTANCE.resolve(input, { uuid: UUID ->
         val var10001: java.lang.String = uuid.toString()
         `$action`(var10001)
         Unit.INSTANCE
      }, { var0: java.lang.String, msg: java.lang.String ->
         INSTANCE.showErrorToast("Invalid player: $msg")
         Unit.INSTANCE
      })
   }

   private fun vcPardon(uuid: String) {
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getBackgroundCoroutineScope(),
         null,
         null,
         PunishCommand$vcPardon$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, uuid
         ),
         3,
         null
      )
   }

   private fun vcBan(uuid: String, duration: Long, reason: String) {
      this.executePunish(uuid, duration, reason, "BAN")
   }

   private fun vcMute(uuid: String, duration: Long, reason: String) {
      this.executePunish(uuid, duration, reason, "MUTE")
   }

   private fun executePunish(uuid: String, duration: Long, reason: String, type: String) {
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getBackgroundCoroutineScope(),
         null,
         null,
         PunishCommand$executePunish$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(),
            1L,
            CoroutineTask(1L),
            ExtensionsKt.getTicks(1),
            null,
            type,
            uuid,
            duration,
            reason
         ),
         3,
         null
      )
   }

   private fun showSuccessToast() {
      val `sync$iv`: Boolean = true
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getMcClientCoroutineScope(),
         null,
         null,
         PunishCommand$showSuccessToast$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null
         ),
         3,
         null
      )
   }

   private fun showErrorToast(message: String) {
      val `sync$iv`: Boolean = true
      val `client$iv`: Boolean = true
      BuildersKt.launch$default(
         CoroutineScopesKt.getMcClientCoroutineScope(),
         null,
         null,
         PunishCommand$showErrorToast$$inlined$mcCoroutineTask-ML416i8$default$1(
            Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(), 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, message
         ),
         3,
         null
      )
   }

   @JvmStatic
   fun {
      val `$this$lazyLogger$iv`: Any = INSTANCE
   }
}
