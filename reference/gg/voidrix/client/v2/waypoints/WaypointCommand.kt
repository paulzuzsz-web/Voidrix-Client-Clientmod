package gg.voidrix.client.v2.waypoints

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.command.ArgumentCommandBuilder
import gg.voidrix.compat.command.ClientCommandSource
import gg.voidrix.compat.command.CommandBuilder
import gg.voidrix.compat.command.CommandBuilderKt
import gg.voidrix.compat.command.CommandExecutionContext
import gg.voidrix.compat.command.LiteralCommandBuilder
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.VoidrixBranding
import gg.voidrix.compat.waypoint.persistent.PersistentWaypoint
import gg.voidrix.compat.waypoint.persistent.WaypointPurpose
import java.util.ArrayList
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.random.Random
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component

@SourceDebugExtension(["SMAP\nWaypointCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointCommand.kt\ngg/voidrix/client/v2/waypoints/WaypointCommand\n+ 2 VoidrixBranding.kt\ngg/voidrix/compat/text/VoidrixBranding\n+ 3 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 4 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n+ 5 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 6 CommandBuilder.kt\ngg/voidrix/compat/command/CommandBuilder\n*L\n1#1,259:1\n83#2,2:260\n75#2:262\n76#2:267\n77#2:279\n78#2,8:291\n83#2,2:327\n75#2:329\n76#2:334\n77#2:346\n78#2,8:380\n83#2,2:388\n75#2:390\n76#2:395\n77#2:407\n78#2,8:419\n83#2,2:427\n75#2:429\n76#2:434\n77#2:446\n78#2,8:458\n83#2,2:494\n75#2:496\n76#2:501\n77#2:513\n78#2,8:525\n83#2,2:533\n75#2:535\n76#2:540\n77#2:552\n78#2,8:586\n83#2,2:594\n75#2:596\n76#2:601\n77#2:613\n78#2,8:647\n83#2,2:683\n75#2:685\n76#2:690\n77#2:702\n78#2,8:725\n8#3,4:263\n8#3,4:299\n8#3,4:330\n8#3,4:391\n8#3,4:430\n8#3,4:466\n8#3,4:497\n8#3,4:536\n8#3,4:597\n8#3,4:686\n8#3,4:733\n78#4,6:268\n72#4,4:274\n87#4:278\n78#4,6:280\n72#4,4:286\n87#4:290\n78#4,6:303\n72#4,4:309\n87#4:313\n78#4,6:314\n72#4,4:320\n87#4:324\n78#4,6:335\n72#4,4:341\n87#4:345\n78#4,6:347\n72#4,4:353\n87#4:357\n78#4,6:358\n72#4,4:364\n87#4:368\n78#4,6:369\n72#4,4:375\n87#4:379\n78#4,6:396\n72#4,4:402\n87#4:406\n78#4,6:408\n72#4,4:414\n87#4:418\n78#4,6:435\n72#4,4:441\n87#4:445\n78#4,6:447\n72#4,4:453\n87#4:457\n78#4,6:470\n72#4,4:476\n87#4:480\n78#4,6:481\n72#4,4:487\n87#4:491\n78#4,6:502\n72#4,4:508\n87#4:512\n78#4,6:514\n72#4,4:520\n87#4:524\n78#4,6:541\n72#4,4:547\n87#4:551\n78#4,6:553\n72#4,4:559\n87#4:563\n78#4,6:564\n72#4,4:570\n87#4:574\n78#4,6:575\n72#4,4:581\n87#4:585\n78#4,6:602\n72#4,4:608\n87#4:612\n78#4,6:614\n72#4,4:620\n87#4:624\n78#4,6:625\n72#4,4:631\n87#4:635\n78#4,6:636\n72#4,4:642\n87#4:646\n78#4,6:691\n72#4,4:697\n87#4:701\n78#4,6:703\n72#4,4:709\n87#4:713\n78#4,6:714\n72#4,4:720\n87#4:724\n78#4,6:737\n72#4,4:743\n87#4:747\n78#4,6:748\n72#4,4:754\n87#4:758\n78#4,6:759\n72#4,4:765\n87#4:769\n78#4,6:770\n72#4,4:776\n87#4:780\n185#5:325\n40#5:326\n185#5:492\n40#5:493\n147#6,7:655\n147#6,5:662\n147#6,7:667\n152#6,2:674\n147#6,7:676\n147#6,7:781\n147#6,7:788\n147#6,7:795\n*S KotlinDebug\n*F\n+ 1 WaypointCommand.kt\ngg/voidrix/client/v2/waypoints/WaypointCommand\n*L\n142#1:260,2\n142#1:262\n142#1:267\n142#1:279\n142#1:291,8\n175#1:327,2\n175#1:329\n175#1:334\n175#1:346\n175#1:380,8\n181#1:388,2\n181#1:390\n181#1:395\n181#1:407\n181#1:419,8\n188#1:427,2\n188#1:429\n188#1:434\n188#1:446\n188#1:458,8\n206#1:494,2\n206#1:496\n206#1:501\n206#1:513\n206#1:525,8\n243#1:533,2\n243#1:535\n243#1:540\n243#1:552\n243#1:586,8\n252#1:594,2\n252#1:596\n252#1:601\n252#1:613\n252#1:647,8\n65#1:683,2\n65#1:685\n65#1:690\n65#1:702\n65#1:725,8\n142#1:263,4\n154#1:299,4\n175#1:330,4\n181#1:391,4\n188#1:430,4\n197#1:466,4\n206#1:497,4\n243#1:536,4\n252#1:597,4\n65#1:686,4\n73#1:733,4\n142#1:268,6\n142#1:274,4\n142#1:278\n143#1:280,6\n143#1:286,4\n143#1:290\n155#1:303,6\n155#1:309,4\n155#1:313\n156#1:314,6\n156#1:320,4\n156#1:324\n175#1:335,6\n175#1:341,4\n175#1:345\n176#1:347,6\n176#1:353,4\n176#1:357\n177#1:358,6\n177#1:364,4\n177#1:368\n178#1:369,6\n178#1:375,4\n178#1:379\n181#1:396,6\n181#1:402,4\n181#1:406\n182#1:408,6\n182#1:414,4\n182#1:418\n188#1:435,6\n188#1:441,4\n188#1:445\n189#1:447,6\n189#1:453,4\n189#1:457\n198#1:470,6\n198#1:476,4\n198#1:480\n199#1:481,6\n199#1:487,4\n199#1:491\n206#1:502,6\n206#1:508,4\n206#1:512\n207#1:514,6\n207#1:520,4\n207#1:524\n243#1:541,6\n243#1:547,4\n243#1:551\n244#1:553,6\n244#1:559,4\n244#1:563\n245#1:564,6\n245#1:570,4\n245#1:574\n246#1:575,6\n246#1:581,4\n246#1:585\n252#1:602,6\n252#1:608,4\n252#1:612\n253#1:614,6\n253#1:620,4\n253#1:624\n254#1:625,6\n254#1:631,4\n254#1:635\n255#1:636,6\n255#1:642,4\n255#1:646\n65#1:691,6\n65#1:697,4\n65#1:701\n66#1:703,6\n66#1:709,4\n66#1:713\n67#1:714,6\n67#1:720,4\n67#1:724\n74#1:737,6\n74#1:743,4\n74#1:747\n75#1:748,6\n75#1:754,4\n75#1:758\n76#1:759,6\n76#1:765,4\n76#1:769\n78#1:770,6\n78#1:776,4\n78#1:780\n162#1:325\n162#1:326\n205#1:492\n205#1:493\n23#1:655,7\n31#1:662,5\n32#1:667,7\n31#1:674,2\n41#1:676,7\n87#1:781,7\n122#1:788,7\n128#1:795,7\n*E\n"])
public object WaypointCommand {
   private const val PREFIX: String = "Waypoints"

   public fun register() {
      CommandBuilderKt.clientCommand(
         "voidrix",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "waypoint",
               { $this$literal: LiteralCommandBuilder ->
                  `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                     INSTANCE.showHelp()
                     Unit.INSTANCE
                  })
                  `$this$literal`.literal(
                     "create",
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
                     "createcolor",
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
                     "delete",
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
                     "list",
                     { $this$literal: LiteralCommandBuilder ->
                        `$this$literal`.runsSuccess(
                           lambda_29_lambda_28_lambda_17_lambda_16@{ $this$runsSuccess: CommandExecutionContext ->
                              val waypoints: java.util.List = PersistentWaypointStore.INSTANCE.getAllWaypoints()
                              var var10000: java.lang.String = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
                              if (var10000 == null) {
                                 var10000 = "unknown"
                              }

                              if (waypoints.isEmpty()) {
                                 val var27: VoidrixBranding = VoidrixBranding.INSTANCE
                                 val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
                                 `this_$iv$ivx`.getAppendTasks()
                                    .add(
                                       WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$$inlined$sendChat$default$1(
                                          `this_$iv$ivx`, "[Waypoints] ", true, 5625087
                                       )
                                    )
                                    `this_$iv$ivx`.getAppendTasks()
                                    .add(
                                       WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$lambda$10$$inlined$text$default$1(
                                          `this_$iv$ivx`, "No waypoints in ", true
                                       )
                                    )
                                    `this_$iv$ivx`.getAppendTasks()
                                    .add(
                                       WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$lambda$10$$inlined$text$default$2(
                                          `this_$iv$ivx`, var10000, true
                                       )
                                    )
                                    MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
                                 return@lambda_29_lambda_28_lambda_17_lambda_16 Unit.INSTANCE
                              } else {
                                 (`$this$runsSuccess`.getSource() as ClientCommandSource)
                                    .sendSuccess(VoidrixBranding.header$default(VoidrixBranding.INSTANCE, "Waypoints ($var10000)", 0, 2, null) as Component)

                                 for (wp in waypoints) {
                                    val var57: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                                    val `this_$iv$iv`: LiteralTextBuilder = LiteralTextBuilder(null, true)
                                    `this_$iv$iv`.getAppendTasks()
                                       .add(
                                          WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$lambda$15$$inlined$text$default$1(
                                             `this_$iv$iv`, " ● ", true, wp
                                          )
                                       )
                                       `this_$iv$iv`.getAppendTasks()
                                       .add(
                                          WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$lambda$15$$inlined$text$default$2(
                                             `this_$iv$iv`, wp.getName(), true
                                          )
                                       )
                                       `this_$iv$iv`.getAppendTasks()
                                       .add(
                                          WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$lambda$15$$inlined$text$default$3(
                                             `this_$iv$iv`, " [${wp.getX()}, ${wp.getY()}, ${wp.getZ()}]", true
                                          )
                                       )
                                       if (wp.getPurpose() != WaypointPurpose.NORMAL) {
                                       `this_$iv$iv`.getAppendTasks()
                                          .add(
                                             WaypointCommand$register$lambda$29$lambda$28$lambda$17$lambda$16$lambda$15$$inlined$text$default$4(
                                                `this_$iv$iv`, " ${wp.getPurpose().name()}", true
                                             )
                                          )
                                       }

                                    var57.sendSuccess(`this_$iv$iv`.build() as Component)
                                 }

                                 (`$this$runsSuccess`.getSource() as ClientCommandSource)
                                    .sendSuccess(VoidrixBranding.footer$default(VoidrixBranding.INSTANCE, 0, 1, null) as Component)
                                    return@lambda_29_lambda_28_lambda_17_lambda_16 Unit.INSTANCE
                              }
                           }
                        )
                        Unit.INSTANCE
                     }
                  )
                  `$this$literal`.literal(
                     "navigate",
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
                     "debug",
                     { $this$literal: LiteralCommandBuilder ->
                        `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                           INSTANCE.showDebugHelp()
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal(
                           "stress",
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
                           "stress-dense",
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
                        `$this$literal`.literal("clear", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              INSTANCE.clearAll()
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
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
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$showHelp$$inlined$sendChat$default$1(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$showHelp$lambda$31$$inlined$text$default$1(`this_$iv$ivx`, "Commands:", true))
      MCClient.sendMessage(`this_$iv$ivx`.build() as Component)

      for (var27 in CollectionsKt.listOf(
         arrayOf(
            TuplesKt.to("/voidrix waypoint create <name>", "Create at your position"),
            TuplesKt.to("/voidrix waypoint createcolor <name> <color>", "Create with specific color"),
            TuplesKt.to("/voidrix waypoint delete <name>", "Delete a waypoint"),
            TuplesKt.to("/voidrix waypoint list", "List all waypoints"),
            TuplesKt.to("/voidrix waypoint navigate <name>", "Navigate to a waypoint"),
            TuplesKt.to("/voidrix waypoint debug", "Stress-test commands (stress, stress-dense, clear)")
         )
      )) {
         val var28: java.lang.String = var27.component1() as java.lang.String
         val desc: java.lang.String = var27.component2() as java.lang.String
         val `this_$iv$ivxx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         `this_$iv$ivxx`.getAppendTasks().add(WaypointCommand$showHelp$lambda$34$$inlined$text$default$1(`this_$iv$ivxx`, " $var28", true))
         `this_$iv$ivxx`.getAppendTasks().add(WaypointCommand$showHelp$lambda$34$$inlined$text$default$2(`this_$iv$ivxx`, " - $desc", true))
         MCClient.sendMessage(`this_$iv$ivxx`.build() as Component)
      }
   }

   private fun createAtPlayerPos(name: String, colorOverride: Int?) {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player != null) {
         val player: LocalPlayer = var10000.player
         val wp: PersistentWaypoint = PersistentWaypointStore.createWaypoint$default(
            PersistentWaypointStore.INSTANCE,
            name,
            var10000.player.blockPosition().getX(),
            player.blockPosition().getY(),
            player.blockPosition().getZ(),
            colorOverride ?: WaypointColors.INSTANCE.random(),
            null,
            null,
            null,
            224,
            null
         )
         if (wp != null) {
            val `$this$iv`: VoidrixBranding = VoidrixBranding.INSTANCE
            val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
            `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$createAtPlayerPos$$inlined$sendChat$default$1(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
            `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$createAtPlayerPos$lambda$38$$inlined$text$default$1(`this_$iv$ivx`, "Created ", true))
            `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$createAtPlayerPos$lambda$38$$inlined$text$default$2(`this_$iv$ivx`, wp.getName(), true))
            `this_$iv$ivx`.getAppendTasks()
               .add(WaypointCommand$createAtPlayerPos$lambda$38$$inlined$text$default$3(`this_$iv$ivx`, " [${wp.getX()}, ${wp.getY()}, ${wp.getZ()}]", true))
               MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
         } else {
            val var34: VoidrixBranding = VoidrixBranding.INSTANCE
            val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
            `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$createAtPlayerPos$$inlined$sendChat$default$2(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
            `this_$iv$ivx`.getAppendTasks()
               .add(WaypointCommand$createAtPlayerPos$lambda$40$$inlined$text$default$1(`this_$iv$ivx`, "Failed to create waypoint (not in a world?)", true))
               MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
         }
      }
   }

   private fun showDebugHelp() {
      val commands: VoidrixBranding = VoidrixBranding.INSTANCE
      val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$showDebugHelp$$inlined$sendChat$default$1(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
      `this_$iv$ivx`.getAppendTasks()
         .add(WaypointCommand$showDebugHelp$lambda$42$$inlined$text$default$1(`this_$iv$ivx`, "Debug commands (transient, never persisted):", true))
         MCClient.sendMessage(`this_$iv$ivx`.build() as Component)

      for (var27 in CollectionsKt.listOf(
         arrayOf(
            TuplesKt.to("/voidrix waypoint debug stress <count>", "Spawn N waypoints in 4-block grid"),
            TuplesKt.to("/voidrix waypoint debug stress-dense <count>", "Spawn N waypoints in 2-block grid"),
            TuplesKt.to("/voidrix waypoint debug clear", "Clear all transient debug waypoints")
         )
      )) {
         val var28: java.lang.String = var27.component1() as java.lang.String
         val desc: java.lang.String = var27.component2() as java.lang.String
         val `this_$iv$ivxx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         `this_$iv$ivxx`.getAppendTasks().add(WaypointCommand$showDebugHelp$lambda$45$$inlined$text$default$1(`this_$iv$ivxx`, " $var28", true))
         `this_$iv$ivxx`.getAppendTasks().add(WaypointCommand$showDebugHelp$lambda$45$$inlined$text$default$2(`this_$iv$ivxx`, " - $desc", true))
         MCClient.sendMessage(`this_$iv$ivxx`.build() as Component)
      }
   }

   private fun stressGrid(count: Int, spacing: Int) {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player == null) {
         val var38: WaypointCommand = this
         val var40: VoidrixBranding = VoidrixBranding.INSTANCE
         val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$stressGrid$lambda$48$$inlined$sendChat$default$1(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
         `this_$iv$ivx`.getAppendTasks()
            .add(WaypointCommand$stressGrid$lambda$48$lambda$47$$inlined$text$default$1(`this_$iv$ivx`, "No player (not in a world?)", true))
            MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
      } else {
         val player: LocalPlayer = var10000.player
         val px: Int = var10000.player.blockPosition().getX()
         val var36: Int = player.blockPosition().getY()
         val var37: Int = player.blockPosition().getZ()
         val side: Int = (int)Math.ceil(Math.sqrt((double)count))
         val offsetX: Int = (side - 1) * spacing / 2
         val offsetZ: Int = (side - 1) * spacing / 2
         val specs: ArrayList = ArrayList(count)
         var placed: Int = 0

         repeat(side) label37@{ added ->
            repeat(side) { `$this$iv` ->
               if (placed >= count) {
                  break@label37
               }

               specs.add(
                  PersistentWaypointStore.WaypointSpec(
                     "WP$placed", px + `$this$iv` * spacing - offsetX, var36, var37 + added * spacing - offsetZ, Random.Default.nextInt(16777215)
                  )
               )
               placed++
            }
         }

         val var44: Int = PersistentWaypointStore.INSTANCE.addTransient(specs)
         val var45: VoidrixBranding = VoidrixBranding.INSTANCE
         val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$stressGrid$$inlined$sendChat$default$1(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
         `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$stressGrid$lambda$52$$inlined$text$default$1(`this_$iv$ivx`, "Spawned ", true))
         `this_$iv$ivx`.getAppendTasks()
            .add(WaypointCommand$stressGrid$lambda$52$$inlined$text$default$2(`this_$iv$ivx`, java.lang.String.valueOf(var44), true))
            `this_$iv$ivx`.getAppendTasks()
            .add(WaypointCommand$stressGrid$lambda$52$$inlined$text$default$3(`this_$iv$ivx`, " transient waypoints ($spacingb grid)", true))
            MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
      }
   }

   private fun clearAll() {
      val n: Int = PersistentWaypointStore.INSTANCE.clearTransient()
      val `$this$iv`: VoidrixBranding = VoidrixBranding.INSTANCE
      val `this_$iv$ivx`: LiteralTextBuilder = LiteralTextBuilder(null, true)
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$clearAll$$inlined$sendChat$default$1(`this_$iv$ivx`, "[Waypoints] ", true, 5625087))
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$clearAll$lambda$56$$inlined$text$default$1(`this_$iv$ivx`, "Cleared ", true))
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$clearAll$lambda$56$$inlined$text$default$2(`this_$iv$ivx`, java.lang.String.valueOf(n), true))
      `this_$iv$ivx`.getAppendTasks().add(WaypointCommand$clearAll$lambda$56$$inlined$text$default$3(`this_$iv$ivx`, " transient waypoints", true))
      MCClient.sendMessage(`this_$iv$ivx`.build() as Component)
   }
}
