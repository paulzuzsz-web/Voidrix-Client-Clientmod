package gg.norisk.client.v2.debug

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import gg.norisk.client.v2.perf.BatchModelRendering
import gg.norisk.compat.bugfix.CameraThroughGrass
import gg.norisk.compat.command.ArgumentCommandBuilder
import gg.norisk.compat.command.ClientCommandSource
import gg.norisk.compat.command.CommandBuilder
import gg.norisk.compat.command.CommandBuilderKt
import gg.norisk.compat.command.CommandExecutionContext
import gg.norisk.compat.command.LiteralCommandBuilder
import gg.norisk.compat.resource.NrcTextureUploadQueue
import gg.norisk.compat.skin.BodyPartTextureRange
import gg.norisk.compat.skin.SkinDebugPainter
import java.util.ArrayList
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nLegacyTweaksDebugCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LegacyTweaksDebugCommand.kt\ngg/norisk/client/v2/debug/LegacyTweaksDebugCommand\n+ 2 Text.kt\ngg/norisk/compat/text/TextKt\n+ 3 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder\n+ 4 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,187:1\n66#2:188\n66#2:189\n66#2:190\n66#2:191\n66#2:192\n66#2:193\n66#2:194\n66#2:195\n66#2:196\n66#2:218\n66#2:219\n66#2:220\n66#2:221\n131#3,7:197\n131#3,7:204\n131#3,7:211\n147#3,5:222\n147#3,7:227\n152#3,2:234\n147#3,5:236\n147#3,7:245\n152#3,2:252\n11228#4:241\n11563#4,3:242\n*S KotlinDebug\n*F\n+ 1 LegacyTweaksDebugCommand.kt\ngg/norisk/client/v2/debug/LegacyTweaksDebugCommand\n*L\n182#1:188\n183#1:189\n184#1:190\n20#1:191\n26#1:192\n33#1:193\n39#1:194\n51#1:195\n58#1:196\n92#1:218\n102#1:219\n113#1:220\n119#1:221\n62#1:197,7\n71#1:204,7\n80#1:211,7\n123#1:222,5\n124#1:227,7\n123#1:234,2\n140#1:236,5\n142#1:245,7\n140#1:252,2\n141#1:241\n141#1:242,3\n*E\n"])
public object LegacyTweaksDebugCommand {
   public fun init() {
      CommandBuilderKt.clientCommand(
         "nrc",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "debug",
               { $this$literal: LiteralCommandBuilder ->
                  `$this$literal`.literal("togglebatch", { $this$literal: LiteralCommandBuilder ->
                     `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                        BatchModelRendering.enabled = !BatchModelRendering.enabled
                        val state: java.lang.String = if (BatchModelRendering.enabled) "§2ON" else "§cOFF"
                        val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                        val var10001: MutableComponent = Component.literal("§7[Voidrix] Batch Model Rendering: $state §8(rejoin world for full rebuild)")
                        var10000.sendSuccess(var10001 as Component)
                        Unit.INSTANCE
                     })
                     Unit.INSTANCE
                  })
                  `$this$literal`.literal("batchstatus", { $this$literal: LiteralCommandBuilder ->
                     `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                        val state: java.lang.String = if (BatchModelRendering.enabled) "§2ON" else "§cOFF"
                        val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                        val var10001: MutableComponent = Component.literal("§7[Voidrix] Batch Model Rendering: $state")
                        var10000.sendSuccess(var10001 as Component)
                        Unit.INSTANCE
                     })
                     Unit.INSTANCE
                  })
                  `$this$literal`.literal("togglegrasscam", { $this$literal: LiteralCommandBuilder ->
                     `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                        CameraThroughGrass.enabled = !CameraThroughGrass.enabled
                        val state: java.lang.String = if (CameraThroughGrass.enabled) "§2ON" else "§cOFF"
                        val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                        val var10001: MutableComponent = Component.literal("§7[Voidrix] Camera through grass: $state")
                        var10000.sendSuccess(var10001 as Component)
                        Unit.INSTANCE
                     })
                     Unit.INSTANCE
                  })
                  `$this$literal`.literal("grasscamstatus", { $this$literal: LiteralCommandBuilder ->
                     `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                        val state: java.lang.String = if (CameraThroughGrass.enabled) "§2ON" else "§cOFF"
                        val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                        val var10001: MutableComponent = Component.literal("§7[Voidrix] Camera through grass: $state")
                        var10000.sendSuccess(var10001 as Component)
                        Unit.INSTANCE
                     })
                     Unit.INSTANCE
                  })
                  `$this$literal`.literal(
                     "uploadqueue",
                     { $this$literal: LiteralCommandBuilder ->
                        `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                           INSTANCE.sendUploadQueueStatus(`$this$runsSuccess`.getSource() as ClientCommandSource)
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal("status", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              INSTANCE.sendUploadQueueStatus(`$this$runsSuccess`.getSource() as ClientCommandSource)
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal("toggle", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              NrcTextureUploadQueue.INSTANCE.setEnabled(!NrcTextureUploadQueue.INSTANCE.getEnabled())
                              val state: java.lang.String = if (NrcTextureUploadQueue.INSTANCE.getEnabled()) "§2ON" else "§cOFF"
                              val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                              val var10001: MutableComponent = Component.literal("§7[Voidrix] UploadQueue: $state")
                              var10000.sendSuccess(var10001 as Component)
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal("inline", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              NrcTextureUploadQueue.INSTANCE.setInlineIfOnRenderThread(!NrcTextureUploadQueue.INSTANCE.getInlineIfOnRenderThread())
                              val state: java.lang.String = if (NrcTextureUploadQueue.INSTANCE.getInlineIfOnRenderThread()) "§2ON" else "§cOFF"
                              val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                              val var10001: MutableComponent = Component.literal("§7[Voidrix] UploadQueue inline-on-render-thread: $state")
                              var10000.sendSuccess(var10001 as Component)
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal(
                           "maxperframe",
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
                           "maxms",
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
                           "delay",
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
                              val before: Int = NrcTextureUploadQueue.INSTANCE.getPending()
                              NrcTextureUploadQueue.INSTANCE.clear()
                              val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                              val var10001: MutableComponent = Component.literal("§7[Voidrix] UploadQueue cleared §8($before pending dropped)")
                              var10000.sendSuccess(var10001 as Component)
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        `$this$literal`.literal("reset", { $this$literal: LiteralCommandBuilder ->
                           `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                              NrcTextureUploadQueue.INSTANCE.setMaxUploadsPerFrame(3)
                              NrcTextureUploadQueue.INSTANCE.setMaxUploadMillisPerFrame(4L)
                              NrcTextureUploadQueue.INSTANCE.setMinDelayBetweenDrainsMs(0L)
                              NrcTextureUploadQueue.INSTANCE.setEnabled(true)
                              NrcTextureUploadQueue.INSTANCE.setInlineIfOnRenderThread(true)
                              val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                              val var10001: MutableComponent = Component.literal("§7[Voidrix] UploadQueue reset to defaults §8(3 / 4ms / 0ms delay)")
                              var10000.sendSuccess(var10001 as Component)
                              INSTANCE.sendUploadQueueStatus(`$this$runsSuccess`.getSource() as ClientCommandSource)
                              Unit.INSTANCE
                           })
                           Unit.INSTANCE
                        })
                        Unit.INSTANCE
                     }
                  )
                  `$this$literal`.literal(
                     "colorbodypart",
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

   private fun parseHexColor(input: String): Int? {
      val s: java.lang.String = StringsKt.removePrefix(StringsKt.removePrefix(StringsKt.removePrefix(input, "#"), "0x"), "0X")
      if (s.length() != 6 && s.length() != 8) {
         return null
      } else {
         var var3: Int
         try {
            val var6: Long = java.lang.Long.parseLong(s, 16)
            var3 = if (s.length() == 6) -16777216 or (int)var6 else (int)var6
         } catch (var5: NumberFormatException) {
            var3 = null
         }

         return var3
      }
   }

   private fun sendUploadQueueStatus(source: ClientCommandSource) {
      val q: NrcTextureUploadQueue = NrcTextureUploadQueue.INSTANCE
      var var10001: MutableComponent = Component.literal(
         "§7[Voidrix] UploadQueue: ${if (NrcTextureUploadQueue.INSTANCE.getEnabled()) "§2ON" else "§cOFF"} §8| §7pending=§e${q.getPending()}"
      )
      source.sendSuccess(var10001 as Component)
      var10001 = Component.literal(
         "§7  maxPerFrame=§e${q.getMaxUploadsPerFrame()} §7maxMs=§e${q.getMaxUploadMillisPerFrame()}ms §7delay=§e${q.getMinDelayBetweenDrainsMs()}ms"
      )
      source.sendSuccess(var10001 as Component)
      var10001 = Component.literal(
         "§7  lastFrame=§e${q.getLastFrameUploads()} uploads§7/§e${q.getLastFrameMicros()}µs §7inline=§e${q.getInlineIfOnRenderThread()}"
      )
      source.sendSuccess(var10001 as Component)
   }
}
