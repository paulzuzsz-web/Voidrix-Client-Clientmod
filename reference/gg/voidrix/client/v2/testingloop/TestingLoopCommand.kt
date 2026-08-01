package gg.voidrix.client.v2.testingloop

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.command.ArgumentCommandBuilder
import gg.voidrix.compat.command.CommandBuilder
import gg.voidrix.compat.command.CommandBuilderKt
import gg.voidrix.compat.command.CommandExecutionContext
import gg.voidrix.compat.command.LiteralCommandBuilder
import gg.voidrix.compat.testingloop.SyncTestRegistry
import gg.voidrix.compat.testingloop.TestingLoopController
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.VoidrixBranding
import gg.voidrix.compat.text.VoidrixBranding.StatusRow
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import net.minecraft.network.chat.Component

@SourceDebugExtension(["SMAP\nTestingLoopCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TestingLoopCommand.kt\ngg/voidrix/client/v2/testingloop/TestingLoopCommand\n+ 2 VoidrixBranding.kt\ngg/voidrix/compat/text/VoidrixBranding\n+ 3 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 4 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n+ 5 CommandBuilder.kt\ngg/voidrix/compat/command/CommandBuilder\n*L\n1#1,169:1\n83#2,2:170\n75#2:172\n76#2:177\n77#2:189\n78#2,8:201\n83#2,2:235\n75#2:237\n76#2:242\n77#2:254\n78#2,8:266\n83#2,2:322\n75#2:324\n76#2:329\n77#2:341\n78#2,8:364\n83#2,2:372\n75#2:374\n76#2:379\n77#2:391\n78#2,8:403\n83#2,2:411\n75#2:413\n76#2:418\n77#2:430\n78#2,8:442\n83#2,2:450\n75#2:452\n76#2:457\n77#2:469\n78#2,8:481\n8#3,4:173\n8#3,4:209\n8#3,4:238\n8#3,4:274\n8#3,4:325\n8#3,4:375\n8#3,4:414\n8#3,4:453\n78#4,6:178\n72#4,4:184\n87#4:188\n78#4,6:190\n72#4,4:196\n87#4:200\n78#4,6:213\n72#4,4:219\n87#4:223\n78#4,6:224\n72#4,4:230\n87#4:234\n78#4,6:243\n72#4,4:249\n87#4:253\n78#4,6:255\n72#4,4:261\n87#4:265\n78#4,6:278\n72#4,4:284\n87#4:288\n78#4,6:289\n72#4,4:295\n87#4:299\n78#4,6:300\n72#4,4:306\n87#4:310\n78#4,6:311\n72#4,4:317\n87#4:321\n78#4,6:330\n72#4,4:336\n87#4:340\n78#4,6:342\n72#4,4:348\n87#4:352\n78#4,6:353\n72#4,4:359\n87#4:363\n78#4,6:380\n72#4,4:386\n87#4:390\n78#4,6:392\n72#4,4:398\n87#4:402\n78#4,6:419\n72#4,4:425\n87#4:429\n78#4,6:431\n72#4,4:437\n87#4:441\n78#4,6:458\n72#4,4:464\n87#4:468\n78#4,6:470\n72#4,4:476\n87#4:480\n147#5,7:489\n147#5,7:496\n*S KotlinDebug\n*F\n+ 1 TestingLoopCommand.kt\ngg/voidrix/client/v2/testingloop/TestingLoopCommand\n*L\n108#1:170,2\n108#1:172\n108#1:177\n108#1:189\n108#1:201,8\n152#1:235,2\n152#1:237\n152#1:242\n152#1:254\n152#1:266,8\n26#1:322,2\n26#1:324\n26#1:329\n26#1:341\n26#1:364,8\n33#1:372,2\n33#1:374\n33#1:379\n33#1:391\n33#1:403,8\n43#1:411,2\n43#1:413\n43#1:418\n43#1:430\n43#1:442,8\n47#1:450,2\n47#1:452\n47#1:457\n47#1:469\n47#1:481,8\n108#1:173,4\n121#1:209,4\n152#1:238,4\n159#1:274,4\n26#1:325,4\n33#1:375,4\n43#1:414,4\n47#1:453,4\n108#1:178,6\n108#1:184,4\n108#1:188\n109#1:190,6\n109#1:196,4\n109#1:200\n122#1:213,6\n122#1:219,4\n122#1:223\n123#1:224,6\n123#1:230,4\n123#1:234\n152#1:243,6\n152#1:249,4\n152#1:253\n153#1:255,6\n153#1:261,4\n153#1:265\n160#1:278,6\n160#1:284,4\n160#1:288\n161#1:289,6\n161#1:295,4\n161#1:299\n162#1:300,6\n162#1:306,4\n162#1:310\n163#1:311,6\n163#1:317,4\n163#1:321\n26#1:330,6\n26#1:336,4\n26#1:340\n27#1:342,6\n27#1:348,4\n27#1:352\n28#1:353,6\n28#1:359,4\n28#1:363\n33#1:380,6\n33#1:386,4\n33#1:390\n34#1:392,6\n34#1:398,4\n34#1:402\n43#1:419,6\n43#1:425,4\n43#1:429\n44#1:431,6\n44#1:437,4\n44#1:441\n47#1:458,6\n47#1:464,4\n47#1:468\n48#1:470,6\n48#1:476,4\n48#1:480\n70#1:489,7\n86#1:496,7\n*E\n"])
public object TestingLoopCommand {
   private const val PREFIX: String = "TestingLoop"

   public fun register() {
      CommandBuilderKt.clientCommand(
         "voidrix",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "debug",
               { $this$literal: LiteralCommandBuilder ->
                  `$this$literal`.literal(
                     "testingloop",
                     { $this$literal: LiteralCommandBuilder ->
                        `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                           INSTANCE.showHelp()
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal(
                           "start",
                           { $this$literal: LiteralCommandBuilder ->
                              `$this$literal`.runsSuccess(
                                 { $this$runsSuccess: CommandExecutionContext ->
                                    if (TestingLoopController.INSTANCE.start()) {
                                       val `$this$iv`: VoidrixBranding = VoidrixBranding.INSTANCE
                                       val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
                                       `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$7$lambda$6$$inlined$sendChat$default$1(
                                                `this_$iv$ivx`, "[TestingLoop] ", true, 5625087
                                             )
                                          )
                                          `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$7$lambda$6$lambda$3$$inlined$text$default$1(
                                                `this_$iv$ivx`, "Started ", true
                                             )
                                          )
                                          `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$7$lambda$6$lambda$3$$inlined$text$default$2(
                                                `this_$iv$ivx`,
                                                "(${SyncTestRegistry.INSTANCE.all().size()} tests, period=${Duration.toString_impl/* $VF was: toString-impl */(
                                                   TestingLoopController.INSTANCE.getPeriod_UwyO8pc/* $VF was: getPeriod-UwyO8pc */()
                                                )})",
                                                true
                                             )
                                          )
                                          MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
                                    } else {
                                       val var24: VoidrixBranding = VoidrixBranding.INSTANCE
                                       val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
                                       `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$7$lambda$6$$inlined$sendChat$default$2(
                                                `this_$iv$ivx`, "[TestingLoop] ", true, 5625087
                                             )
                                          )
                                          `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$7$lambda$6$lambda$5$$inlined$text$default$1(
                                                `this_$iv$ivx`, "Already running or no tests registered", true
                                             )
                                          )
                                          MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
                                    }

                                    Unit.INSTANCE
                                 }
                              )
                              Unit.INSTANCE
                           }
                        )
                        `$this$literal`.literal(
                           "stop",
                           { $this$literal: LiteralCommandBuilder ->
                              `$this$literal`.runsSuccess(
                                 { $this$runsSuccess: CommandExecutionContext ->
                                    if (TestingLoopController.INSTANCE.stop()) {
                                       val `$this$iv`: VoidrixBranding = VoidrixBranding.INSTANCE
                                       val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
                                       `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$13$lambda$12$$inlined$sendChat$default$1(
                                                `this_$iv$ivx`, "[TestingLoop] ", true, 5625087
                                             )
                                          )
                                          `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$13$lambda$12$lambda$9$$inlined$text$default$1(
                                                `this_$iv$ivx`, "Stopped", true
                                             )
                                          )
                                          MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
                                    } else {
                                       val var24: VoidrixBranding = VoidrixBranding.INSTANCE
                                       val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
                                       `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$13$lambda$12$$inlined$sendChat$default$2(
                                                `this_$iv$ivx`, "[TestingLoop] ", true, 5625087
                                             )
                                          )
                                          `this_$iv$ivx`.getAppendTasks()
                                          .add(
                                             TestingLoopCommand$register$lambda$26$lambda$25$lambda$24$lambda$13$lambda$12$lambda$11$$inlined$text$default$1(
                                                `this_$iv$ivx`, "Not running", true
                                             )
                                          )
                                          MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
                                    }

                                    Unit.INSTANCE
                                 }
                              )
                              Unit.INSTANCE
                           }
                        )
                        `$this$literal`.literal("status", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              INSTANCE.showStatus()
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal("list", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              INSTANCE.showList()
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal(
                           "announce",
                           { $this$literal: LiteralCommandBuilder ->
                              `$this$literal`.runsSuccess(
                                 { $this$runsSuccess: CommandExecutionContext ->
                                    TestingLoopController.INSTANCE.setAnnounceInChat(!TestingLoopController.INSTANCE.getAnnounceInChat())
                                    VoidrixBranding.sendToggle$default(
                                       VoidrixBranding.INSTANCE, "TestingLoop", "announce", TestingLoopController.INSTANCE.getAnnounceInChat(), 0, null, 24, null
                                    )
                                    Unit.INSTANCE
                                 }
                              )
                              Unit.INSTANCE
                           }
                        )
                        `$this$literal`.literal(
                           "period",
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
                           "single",
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
            Unit.INSTANCE
         }
      )
   }

   private fun showHelp() {
      val commands: VoidrixBranding = VoidrixBranding.INSTANCE
      val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
      `this_$iv$ivx`.getAppendTasks().add(TestingLoopCommand$showHelp$$inlined$sendChat$default$1(`this_$iv$ivx`, "[TestingLoop] ", true, 5625087))
      `this_$iv$ivx`.getAppendTasks().add(TestingLoopCommand$showHelp$lambda$28$$inlined$text$default$1(`this_$iv$ivx`, "Commands:", true))
      MCClient.sendMessage(`this_$iv$ivx`.build() as Component)

      for (var27 in CollectionsKt.listOf(
         arrayOf(
            TuplesKt.to("/voidrix debug testingloop start", "Start the loop"),
            TuplesKt.to("/voidrix debug testingloop stop", "Stop the loop"),
            TuplesKt.to("/voidrix debug testingloop status", "Show running state, period, announce flag"),
            TuplesKt.to("/voidrix debug testingloop list", "List registered tests"),
            TuplesKt.to("/voidrix debug testingloop announce", "Toggle chat announce (other accounts can see)"),
            TuplesKt.to("/voidrix debug testingloop period <seconds>", "Set delay between tests"),
            TuplesKt.to("/voidrix debug testingloop single <id>", "Run one test once")
         )
      )) {
         val var28: java.lang.String = var27.component1() as java.lang.String
         val desc: java.lang.String = var27.component2() as java.lang.String
         val `this_$iv$ivxx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         `this_$iv$ivxx`.getAppendTasks().add(TestingLoopCommand$showHelp$lambda$31$$inlined$text$default$1(`this_$iv$ivxx`, " $var28", true))
         `this_$iv$ivxx`.getAppendTasks().add(TestingLoopCommand$showHelp$lambda$31$$inlined$text$default$2(`this_$iv$ivxx`, " - $desc", true))
         MCClient.sendMessage(`this_$iv$ivxx`.build() as Component)
      }
   }

   private fun showStatus() {
      VoidrixBranding.sendStatus$default(
         VoidrixBranding.INSTANCE,
         "TestingLoop",
         0,
         CollectionsKt.listOf(
            arrayOf(
               StatusRow(
                  "running",
                  if (TestingLoopController.INSTANCE.isRunning()) "YES" else "NO",
                  if (TestingLoopController.INSTANCE.isRunning()) 5635925 else 16733525,
                  true,
                  null,
                  16,
                  null
               ),
               StatusRow(
                  "period",
                  Duration.toString_impl/* $VF was: toString-impl */(TestingLoopController.INSTANCE.getPeriod_UwyO8pc/* $VF was: getPeriod-UwyO8pc */()),
                  8251647,
                  false,
                  null,
                  24,
                  null
               ),
               StatusRow(
                  "announce",
                  if (TestingLoopController.INSTANCE.getAnnounceInChat()) "ON" else "OFF",
                  if (TestingLoopController.INSTANCE.getAnnounceInChat()) 5635925 else 12303291,
                  false,
                  null,
                  24,
                  null
               ),
               StatusRow("tests", java.lang.String.valueOf(SyncTestRegistry.INSTANCE.all().size()), 16777215, false, null, 24, null)
            )
         ),
         2,
         null
      )
   }

   private fun showList() {
      val tests: java.util.Collection = SyncTestRegistry.INSTANCE.all()
      if (tests.isEmpty()) {
         val var25: VoidrixBranding = VoidrixBranding.INSTANCE
         val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         `this_$iv$ivx`.getAppendTasks().add(TestingLoopCommand$showList$$inlined$sendChat$default$1(`this_$iv$ivx`, "[TestingLoop] ", true, 5625087))
         `this_$iv$ivx`.getAppendTasks().add(TestingLoopCommand$showList$lambda$33$$inlined$text$default$1(`this_$iv$ivx`, "No tests registered", true))
         MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
      } else {
         MCClient.sendMessage(VoidrixBranding.header$default(VoidrixBranding.INSTANCE, "TestingLoop (${tests.size()})", 0, 2, null) as Component)

         for (test in tests) {
            val `this_$iv$iv`: LiteralTextBuilder = LiteralTextBuilder(null, true)
            `this_$iv$iv`.getAppendTasks().add(TestingLoopCommand$showList$lambda$38$$inlined$text$default$1(`this_$iv$iv`, " ● ", true))
            `this_$iv$iv`.getAppendTasks().add(TestingLoopCommand$showList$lambda$38$$inlined$text$default$2(`this_$iv$iv`, test.getId(), true))
            `this_$iv$iv`.getAppendTasks().add(TestingLoopCommand$showList$lambda$38$$inlined$text$default$3(`this_$iv$iv`, " — ", true))
            `this_$iv$iv`.getAppendTasks().add(TestingLoopCommand$showList$lambda$38$$inlined$text$default$4(`this_$iv$iv`, test.getDescription(), true))
            MCClient.sendMessage(`this_$iv$iv`.build() as Component)
         }

         MCClient.sendMessage(VoidrixBranding.footer$default(VoidrixBranding.INSTANCE, 0, 1, null) as Component)
      }
   }
}
