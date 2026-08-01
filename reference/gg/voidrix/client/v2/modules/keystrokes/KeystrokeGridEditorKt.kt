@file:SourceDebugExtension(["SMAP\nKeystrokeGridEditor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokeGridEditor.kt\ngg/voidrix/client/v2/modules/keystrokes/KeystrokeGridEditorKt\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 3 MCKeyBindings.kt\ngg/voidrix/compat/input/MCKeyBindings\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 5 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 6 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n+ 7 MCClient.kt\ngg/voidrix/compat/client/MCClientKt\n*L\n1#1,658:1\n269#2:659\n27#3:660\n40#4:661\n127#4:677\n40#4:678\n239#4:680\n40#4:681\n127#4:682\n40#4:683\n239#4:685\n40#4:686\n8#5,4:662\n78#6,6:666\n72#6,4:672\n87#6:676\n941#7:679\n941#7:684\n*S KotlinDebug\n*F\n+ 1 KeystrokeGridEditor.kt\ngg/voidrix/client/v2/modules/keystrokes/KeystrokeGridEditorKt\n*L\n69#1:659\n73#1:660\n73#1:661\n108#1:677\n108#1:678\n129#1:680\n129#1:681\n183#1:682\n183#1:683\n204#1:685\n204#1:686\n83#1:662,4\n84#1:666,6\n84#1:672,4\n84#1:676\n109#1:679\n184#1:684\n*E\n"])

package gg.voidrix.client.v2.modules.keystrokes

import gg.voidrix.compat.input.MCKeyBindings
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.resource.MCKeyType
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component

internal fun shortKeyLabel(key: MCKey): String {
   if (key.isUnknown()) {
      return "?"
   } else if (key.getName() == "key.keyboard.space") {
      return "      "
   } else if (!(key.getName() == "key.keyboard.left.shift") && !isSneakBound(key)) {
      val `$this$asString$iv`: java.lang.String = key.getName()
      when (`$this$asString$iv`.hashCode()) {
         -1117194457 -> {
            if (`$this$asString$iv`.equals("key.keyboard.backspace")) {
               return "⌫"
            }
         }
         -699195688 -> {
            if (`$this$asString$iv`.equals("key.keyboard.right.shift")) {
               return "RSHIFT"
            }
         }
         -302080102 -> {
            if (`$this$asString$iv`.equals("key.keyboard.left.alt")) {
               return "ALT"
            }
         }
         -153212210 -> {
            if (`$this$asString$iv`.equals("key.keyboard.left.control")) {
               return "CTRL"
            }
         }
         188168896 -> {
            if (`$this$asString$iv`.equals("key.keyboard.caps.lock")) {
               return "CAPS"
            }
         }
         254003583 -> {
            if (`$this$asString$iv`.equals("key.keyboard.right.alt")) {
               return "ALT GR"
            }
         }
         1031974272 -> {
            if (`$this$asString$iv`.equals("key.keyboard.enter")) {
               return "↵"
            }
         }
         1273092659 -> {
            if (`$this$asString$iv`.equals("key.keyboard.right.control")) {
               return "FN"
            }
         }
         else -> {}
      }

      if (key.getType() === MCKeyType.MOUSE) {
         var var5: java.lang.String
         when (key.getCode()) {
            0 -> var5 = "LMB"
            1 -> var5 = "RMB"
            2 -> var5 = "MMB"
            else -> var5 = "M${key.getCode() + 1}"
         }

         return var5
      } else {
         var var10000: java.lang.String = key.getDisplayName().getString()
         var10000 = var10000.toUpperCase(Locale.ROOT)
         return var10000
      }
   } else {
      return "SHIFT"
   }
}

private fun isSneakBound(key: MCKey): Boolean {
   var var1: Any
   try {
      val var10001: Minecraft = Minecraft.getInstance()
      val var8: KeyMapping = var10001.options.keyShift
      var1 = Result.constructor_impl/* $VF was: constructor-impl */(key == MCKeyBindings.getBoundKey(var8))
   } catch (var4: java.lang.Throwable) {
      var1 = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
   }

   return (if (Result.isFailure_impl/* $VF was: isFailure-impl */(var1)) false else var1) as java.lang.Boolean
}

internal fun buildKeyLabel(key: MCKey): Component {
   val raw: java.lang.String = shortKeyLabel(key)
   if (key.getName() == "key.keyboard.space") {
      val var4: LiteralTextBuilder = LiteralTextBuilder(null, true)
      var4.getAppendTasks().add(KeystrokeGridEditorKt$buildKeyLabel$lambda$2$$inlined$text$default$1(var4, raw, true))
      return var4.build() as Component
   } else {
      return TextKt.getLiteral(raw) as Component
   }
}

internal fun drawFittedTextComponent(
   context: OwoUIGraphics,
   text: Component,
   textForWidth: String,
   x: Double,
   y: Double,
   w: Double,
   h: Double,
   color: Int,
   shadow: Boolean = true,
   padding: Double = 1.5,
   maxScale: Float = 3.0F,
   minScale: Float = 0.35F,
   fitHeight: Double? = null
) {
   if (textForWidth.length() != 0 && !(w <= 0.0) && !(h <= 0.0)) {
      var var10000: Minecraft = Minecraft.getInstance()
      val var35: Font = var10000.font
      val var31: Float = var35.width(textForWidth)
      if (!(var31 <= 0.0F)) {
         val scale: Float = RangesKt.coerceAtLeast(
            Math.min(
               (float)RangesKt.coerceAtLeast(w - padding * (double)2, 1.0) / var31,
               Math.min((float)RangesKt.coerceAtLeast((fitHeight ?: h) - padding * (double)2, 1.0) / 8.0F, maxScale)
            ),
            minScale
         )
         val scaledW: Float = var31 * scale
         val scaledH: Float = 8.0F * scale
         val px: Float = (float)Math.floor(x + (w - (double)scaledW) / 2.0)
         val py: Float = (float)Math.floor(y + (h - (double)scaledH) / 2.0)
         var10000 = Minecraft.getInstance()
         val guiScale: Float = RangesKt.coerceAtLeast((float)((double)var10000.getWindow().getGuiScale()), 1.0F)
         context.push()
         context.translate(0.0F, 1.0F / guiScale)
         context.translate(px, py)
         context.scale(scale, scale)
         context.drawString(var35, text, 0.0, 0.0, color, shadow)
         context.pop()
      }
   }
}

internal fun drawFittedKeyLabel(
   context: OwoUIGraphics,
   key: MCKey,
   x: Double,
   y: Double,
   w: Double,
   h: Double,
   color: Int,
   shadow: Boolean = true,
   padding: Double = 1.5,
   maxScale: Float = 3.0F,
   minScale: Float = 0.35F,
   fitHeight: Double? = null
) {
   drawFittedTextComponent(context, buildKeyLabel(key), shortKeyLabel(key), x, y, w, h, color, shadow, padding, maxScale, minScale, fitHeight)
}

internal fun drawFittedText(
   context: OwoUIGraphics,
   text: String,
   x: Double,
   y: Double,
   w: Double,
   h: Double,
   color: Int,
   shadow: Boolean = true,
   padding: Double = 1.5,
   maxScale: Float = 3.0F,
   minScale: Float = 0.35F,
   fitHeight: Double? = null
) {
   if (text.length() != 0 && !(w <= 0.0) && !(h <= 0.0)) {
      var var10000: Minecraft = Minecraft.getInstance()
      val var34: Font = var10000.font
      val var30: Float = var34.width(text)
      if (!(var30 <= 0.0F)) {
         val scale: Float = RangesKt.coerceAtLeast(
            Math.min(
               (float)RangesKt.coerceAtLeast(w - padding * (double)2, 1.0) / var30,
               Math.min((float)RangesKt.coerceAtLeast((fitHeight ?: h) - padding * (double)2, 1.0) / 8.0F, maxScale)
            ),
            minScale
         )
         val scaledW: Float = var30 * scale
         val scaledH: Float = 8.0F * scale
         val px: Float = (float)Math.floor(x + (w - (double)scaledW) / 2.0)
         val py: Float = (float)Math.floor(y + (h - (double)scaledH) / 2.0)
         var10000 = Minecraft.getInstance()
         val guiScale: Float = RangesKt.coerceAtLeast((float)((double)var10000.getWindow().getGuiScale()), 1.0F)
         context.push()
         context.translate(0.0F, 1.0F / guiScale)
         context.translate(px, py)
         context.scale(scale, scale)
         context.drawString(var34, text, 0.0, 0.0, color, shadow)
         context.pop()
      }
   }
}
