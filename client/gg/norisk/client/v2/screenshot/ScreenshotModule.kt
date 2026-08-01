package gg.norisk.client.v2.screenshot

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.compat.command.ArgumentCommandBuilder
import gg.norisk.compat.command.CommandBuilder
import gg.norisk.compat.command.CommandBuilderKt
import gg.norisk.compat.command.LiteralCommandBuilder
import gg.norisk.compat.event.EventPriority
import gg.norisk.compat.event.ScreenshotEventData
import gg.norisk.compat.event.ScreenshotEvents
import gg.norisk.compat.event.ScreenshotMessageBuilder
import gg.norisk.compat.event.ScreenshotUtils
import gg.norisk.compat.kotlin.ExtensionsKt
import gg.norisk.compat.task.CoroutineScopesKt
import gg.norisk.compat.task.CoroutineTask
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt
import net.minecraft.client.Minecraft
import net.minecraft.client.Screenshot
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.jetbrains.annotations.NotNull

@NrcMiniTag(tags = ["screenshots"])
@SourceDebugExtension(["SMAP\nScreenshotModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ScreenshotModule.kt\ngg/norisk/client/v2/screenshot/ScreenshotModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 CommandBuilder.kt\ngg/norisk/compat/command/CommandBuilder\n+ 4 CoroutineTask.kt\ngg/norisk/compat/task/CoroutineTaskKt\n*L\n1#1,228:1\n378#2:229\n40#2:230\n378#2:231\n40#2:232\n378#2:278\n40#2:279\n147#3,7:233\n147#3,7:240\n147#3,7:247\n18#4,12:254\n18#4,12:266\n*S KotlinDebug\n*F\n+ 1 ScreenshotModule.kt\ngg/norisk/client/v2/screenshot/ScreenshotModule\n*L\n168#1:229\n168#1:230\n224#1:231\n224#1:232\n133#1:278\n133#1:279\n50#1:233,7\n67#1:240,7\n83#1:247,7\n137#1:254,12\n144#1:266,12\n*E\n"])
public object ScreenshotModule : Module("Screenshot", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   public open val seoTags: Array<String>

   public final val async: Boolean by ValueApiKt.boolean$default(true, "Async Screenshot", null, { it: Boolean ->
      ScreenshotUtils.INSTANCE.setAsyncEnabled(it)
      Unit.INSTANCE
   }, 4, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return async$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   @Category(name = "Sharing")
   @NotNull
   public final val copyToClipboard: Boolean by ValueApiKt.boolean$default(false, "Clipboard", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return copyToClipboard$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val openAfterUpload: Boolean by ValueApiKt.boolean$default(true, "Open After Upload", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return openAfterUpload$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   private final val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")

   public fun init() {
      ScreenshotUtils.INSTANCE.setAsyncEnabled(this.async)
      this.registerCommands()
      this.registerScreenshotListener()
   }

   private fun registerCommands() {
      CommandBuilderKt.clientCommand(
         "norisk-screenshot",
         { $this$clientCommand: LiteralCommandBuilder ->
            `$this$clientCommand`.literal(
               "copy",
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
            `$this$clientCommand`.literal(
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
            `$this$clientCommand`.literal(
               "upload",
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

   private fun registerScreenshotListener() {
      ScreenshotEvents.INSTANCE
         .getScreenshotEvent()
         .listen(
            EventPriority.LAST,
            lambda_11@{ event: ScreenshotEventData ->
               if (!INSTANCE.isEnabled()) {
                  return@lambda_11 Unit.INSTANCE
               } else if (event.isCancelled().get()) {
                  return@lambda_11 Unit.INSTANCE
               } else {
                  event.isCancelled().set(true)
                  val fileName: java.lang.String = "${dateFormat.format(Date())}.png"
                  val var10002: Minecraft = Minecraft.getInstance()
                  val var5: File = var10002.gameDirectory
                  val screenshotFile: File = File(var5, "screenshots/$fileName")
                  INSTANCE.takeScreenshot(
                     screenshotFile,
                     fileName,
                     { 
                        if (INSTANCE.copyToClipboard) {
                           BuildersKt.launch$default(
                              CoroutineScopesKt.getBackgroundCoroutineScope(),
                              null,
                              null,
                              ScreenshotModule$registerScreenshotListener$lambda$11$lambda$10$$inlined$mcCoroutineTask-ML416i8$default$1(
                                 Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(),
                                 1L,
                                 CoroutineTask(1L),
                                 ExtensionsKt.getTicks(1),
                                 null,
                                 `$screenshotFile`
                              ),
                              3,
                              null
                           )
                        }

                        val var13: MutableComponent = INSTANCE.createScreenshotMessage(`$screenshotFile`)
                        val var14: Long = ExtensionsKt.getTicks(1)
                        val `client$iv`: Boolean = true
                        val var15: Boolean = true
                        BuildersKt.launch$default(
                           CoroutineScopesKt.getMcClientCoroutineScope(),
                           null,
                           null,
                           ScreenshotModule$registerScreenshotListener$lambda$11$lambda$10$$inlined$mcCoroutineTask-ML416i8$default$2(
                              var14, 1L, CoroutineTask(1L), ExtensionsKt.getTicks(1), null, var13
                           ),
                           3,
                           null
                        )
                        Unit.INSTANCE
                     }
                  )
                  return@lambda_11 Unit.INSTANCE
               }
            }
         )
      }

   private fun takeScreenshot(file: File, fileName: String, callback: () -> Unit) {
      if (this.async) {
         ScreenshotUtils.INSTANCE.takeScreenshotAsync(file, callback)
      } else {
         val mc: Minecraft = Minecraft.getInstance()
         val var10000: Minecraft = Minecraft.getInstance()
         val var7: File = var10000.gameDirectory
         Screenshot.grab(var7, fileName, mc.gameRenderer.mainRenderTarget(), 1, { it: Component ->
            `$callback`()
         })
      }
   }

   public fun createScreenshotMessage(file: File): MutableComponent {
      return ScreenshotMessageBuilder.INSTANCE.createMessage(file)
   }

   private fun copyImageToClipboard(file: File) {
      try {
         var var10000: java.lang.String = System.getProperty("os.name")
         var10000 = var10000.toLowerCase(Locale.ROOT)
         if (StringsKt.contains$default(var10000, "win", false, 2, null)) {
            val e: Array<java.lang.String> = arrayOf("powershell", "-NoProfile", "-Command", null)
            val var10004: java.lang.String = file.getAbsolutePath()
            e[3] = "Add-Type -AssemblyName System.Windows.Forms; [System.Windows.Forms.Clipboard]::SetImage([System.Drawing.Image]::FromFile('${StringsKt.replace$default(
               var10004, "'", "''", false, 4, null
            )}'))"
            ProcessBuilder(e).start().waitFor()
         } else if (StringsKt.contains$default(var10000, "mac", false, 2, null)) {
            ProcessBuilder("osascript", "-e", "set the clipboard to (read (POSIX file \"${file.getAbsolutePath()}\") as «class PNGf»)").start().waitFor()
         } else {
            ProcessBuilder("xclip", "-selection", "clipboard", "-t", "image/png", "-i", file.getAbsolutePath()).start().waitFor()
         }
      } catch (var4: Exception) {
         var4.printStackTrace()
      }
   }

   private fun getScreenshotFile(filename: String): File {
      val var10002: Minecraft = Minecraft.getInstance()
      val var4: File = var10002.gameDirectory
      return File(var4, "screenshots/$filename")
   }
}
