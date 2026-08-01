package gg.norisk.client.v2.debug

import gg.norisk.compat.command.ArgumentCommandBuilder
import gg.norisk.compat.command.ClientCommandSource
import gg.norisk.compat.command.CommandBuilder
import gg.norisk.compat.command.CommandBuilderKt
import gg.norisk.compat.command.CommandExecutionContext
import gg.norisk.compat.command.LiteralCommandBuilder
import gg.norisk.compat.framebuffer.FramebufferManager
import gg.norisk.compat.framebuffer.ICachedFramebufferRenderer
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nFramebufferDebugCommand.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FramebufferDebugCommand.kt\ngg/norisk/client/v2/debug/FramebufferDebugCommand\n+ 2 Text.kt\ngg/norisk/compat/text/TextKt\n+ 3 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder\n*L\n1#1,56:1\n66#2:57\n66#2:58\n66#2:66\n66#2:67\n66#2:68\n66#2:69\n66#2:70\n66#2:71\n66#2:72\n66#2:73\n131#3,7:59\n*S KotlinDebug\n*F\n+ 1 FramebufferDebugCommand.kt\ngg/norisk/client/v2/debug/FramebufferDebugCommand\n*L\n12#1:57\n19#1:58\n36#1:66\n44#1:67\n45#1:68\n46#1:69\n47#1:70\n48#1:71\n49#1:72\n50#1:73\n24#1:59,7\n*E\n"])
public object FramebufferDebugCommand {
   public fun init() {
      CommandBuilderKt.clientCommand(
         "nrcframebuffer",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "toggle",
               { $this$literal: LiteralCommandBuilder ->
                  `$this$literal`.runsSuccess(
                     { $this$runsSuccess: CommandExecutionContext ->
                        val result: java.lang.Boolean = FramebufferManager.INSTANCE.toggle()
                        val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                        val var10001: MutableComponent = Component.literal(
                           "§aCachedFramebuffer: ${if (FramebufferManager.INSTANCE.isEnabled()) "§2ENABLED" else "§cDISABLED"}"
                        )
                        var10000.sendSuccess(var10001 as Component)
                        Unit.INSTANCE
                     }
                  )
                  Unit.INSTANCE
               }
            )
            `$this$clientCommand`.literal("debug", { $this$literal: LiteralCommandBuilder ->
               `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                  val enabled: Boolean = FramebufferManager.INSTANCE.toggleDebug()
                  val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                  val var10001: MutableComponent = Component.literal("§aDebug overlay: ${if (enabled) "§2ON" else "§cOFF"}")
                  var10000.sendSuccess(var10001 as Component)
                  Unit.INSTANCE
               })
               Unit.INSTANCE
            })
            `$this$clientCommand`.literal(
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
            `$this$clientCommand`.literal("forceupdate", { $this$literal: LiteralCommandBuilder ->
               `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                  FramebufferManager.INSTANCE.forceUpdate()
                  val var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                  val var10001: MutableComponent = Component.literal("§aForced framebuffer update")
                  var10000.sendSuccess(var10001 as Component)
                  Unit.INSTANCE
               })
               Unit.INSTANCE
            })
            `$this$clientCommand`.literal("status", { $this$literal: LiteralCommandBuilder ->
               `$this$literal`.runsSuccess({ $this$runsSuccess: CommandExecutionContext ->
                  val status: java.lang.String = FramebufferManager.INSTANCE.getStatus()
                  val renderer: ICachedFramebufferRenderer = FramebufferManager.INSTANCE.getRenderer()
                  var var10000: ClientCommandSource = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var var10001: MutableComponent = Component.literal("§aCachedFramebuffer Status:")
                  var10000.sendSuccess(var10001 as Component)
                  var10000 = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var10001 = Component.literal("§7  Supported: §f${FramebufferManager.INSTANCE.isSupported()}")
                  var10000.sendSuccess(var10001 as Component)
                  var10000 = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var10001 = Component.literal("§7  Enabled: §f${FramebufferManager.INSTANCE.isEnabled()}")
                  var10000.sendSuccess(var10001 as Component)
                  var10000 = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var10001 = Component.literal("§7  Interval: §f${if (renderer != null) renderer.getUpdateIntervalMs() else "N/A"}ms")
                  var10000.sendSuccess(var10001 as Component)
                  var10000 = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var10001 = Component.literal("§7  Tick: §f${if (renderer != null) renderer.getTickCounter() else "N/A"}")
                  var10000.sendSuccess(var10001 as Component)
                  var10000 = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var10001 = Component.literal("§7  Debug: §f${renderer != null && renderer.getShowDebugOverlay()}")
                  var10000.sendSuccess(var10001 as Component)
                  var10000 = `$this$runsSuccess`.getSource() as ClientCommandSource
                  var10001 = Component.literal("§7  Status: §f$status")
                  var10000.sendSuccess(var10001 as Component)
                  Unit.INSTANCE
               })
               Unit.INSTANCE
            })
            Unit.INSTANCE
         }
      )
   }
}
