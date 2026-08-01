package gg.voidrix.client.v2.serverstyling

import java.util.LinkedHashSet
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextColor

@SourceDebugExtension(["SMAP\nServerStylingColor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ServerStylingColor.kt\ngg/voidrix/client/v2/serverstyling/ServerStylingColor\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 _Strings.kt\nkotlin/text/StringsKt___StringsKt\n*L\n1#1,144:1\n68#2:145\n66#2:146\n1#3:147\n1869#4,2:148\n1069#5,2:150\n*S KotlinDebug\n*F\n+ 1 ServerStylingColor.kt\ngg/voidrix/client/v2/serverstyling/ServerStylingColor\n*L\n23#1:145\n77#1:146\n83#1:148,2\n90#1:150,2\n*E\n"])
public object ServerStylingColor {
   @JvmStatic
   public fun translateColorCodes(text: Component?): Component? {
      if (text == null) {
         return null
      } else {
         val input: java.lang.String = text.getString()
         return if (!StringsKt.contains$default(input, '&', false, 2, null)) text else INSTANCE.parseColoredText(input)
      }
   }

   private fun parseColoredText(input: String): Component {
      val var10000: MutableComponent = Component.empty()
      val result: MutableComponent = var10000
      var var9: Int = 0
      val currentText: StringBuilder = StringBuilder()
      var currentColor: TextColor = null
      val currentFormatting: java.util.Set = LinkedHashSet()

      while (var9 < input.length()) {
         if (var9 < input.length() - 1 && input.charAt(var9) == '&') {
            if (currentText.length() > 0) {
               val var10002: java.lang.String = currentText.toString()
               result.append(this.createStyledText(var10002, currentColor, currentFormatting))
               StringsKt.clear(currentText)
            }

            if (var9 + 7 <= input.length()) {
               val var10001: java.lang.String = input.substring(var9 + 1, var9 + 7)
               if (this.isValidHexColor(var10001)) {
                  val var11: java.lang.String = input.substring(var9 + 1, var9 + 7)
                  currentColor = this.parseHexColor(var11)
                  currentFormatting.clear()
                  var9 += 7
                  continue
               }
            }

            val formatting: ChatFormatting = this.getFormattingFromCode(Character.toLowerCase(input.charAt(var9 + 1)))
            if (formatting != null) {
               if (formatting.ordinal() < 16) {
                  currentColor = TextColor.fromLegacyFormat(formatting)
                  currentFormatting.clear()
               } else {
                  currentFormatting.add(formatting)
               }

               var9 += 2
            } else {
               currentText.append(input.charAt(var9))
               var9++
            }
         } else {
            currentText.append(input.charAt(var9))
            var9++
         }
      }

      if (currentText.length() > 0) {
         val var12: java.lang.String = currentText.toString()
         result.append(this.createStyledText(var12, currentColor, currentFormatting))
      }

      return result as Component
   }

   private fun createStyledText(text: String, color: TextColor?, formatting: Set<ChatFormatting>): Component {
      val var10000: MutableComponent = Component.literal(text)
      var var12: MutableComponent = var10000
      if (color != null) {
         var12 = var10000.setStyle(var10000.getStyle().withColor(color))
      }

      for (`element$iv` in formatting) {
         var12 = var12.setStyle(var12.getStyle().applyFormats(arrayOf(`element$iv` as ChatFormatting)))
      }

      return var12 as Component
   }

   private fun isValidHexColor(hex: String): Boolean {
      if (hex.length() != 6) {
         return false
      } else {
         val `$this$all$iv`: java.lang.CharSequence = hex
         var var4: Int = 0

         var var9: Boolean
         while (true) {
            if (var4 >= `$this$all$iv`.length()) {
               var9 = true
               break
            }

            run label48@{
               val it: Char = `$this$all$iv`.charAt(var4)
               if (!Character.isDigit(it)) {
                  val var8: Char = Character.toLowerCase(it)
                  if ('a' > var8 || var8 >= 'g') {
                     var9 = false
                     return@label48
                  }
               }

               var9 = true
            }

            if (!var9) {
               var9 = false
               break
            }

            var4++
         }

         return var9
      }
   }

   private fun parseHexColor(hex: String): TextColor? {
      var var2: TextColor
      try {
         var2 = TextColor.fromRgb(Integer.parseInt(hex, 16))
      } catch (var4: NumberFormatException) {
         var2 = null
      }

      return var2
   }

   private fun getFormattingFromCode(code: Char): ChatFormatting? {
      var var10000: ChatFormatting
      when (code) {
         48 -> var10000 = ChatFormatting.BLACK
         49 -> var10000 = ChatFormatting.DARK_BLUE
         50 -> var10000 = ChatFormatting.DARK_GREEN
         51 -> var10000 = ChatFormatting.DARK_AQUA
         52 -> var10000 = ChatFormatting.DARK_RED
         53 -> var10000 = ChatFormatting.DARK_PURPLE
         54 -> var10000 = ChatFormatting.GOLD
         55 -> var10000 = ChatFormatting.GRAY
         56 -> var10000 = ChatFormatting.DARK_GRAY
         57 -> var10000 = ChatFormatting.BLUE
         97 -> var10000 = ChatFormatting.GREEN
         98 -> var10000 = ChatFormatting.AQUA
         99 -> var10000 = ChatFormatting.RED
         100 -> var10000 = ChatFormatting.LIGHT_PURPLE
         101 -> var10000 = ChatFormatting.YELLOW
         102 -> var10000 = ChatFormatting.WHITE
         107 -> var10000 = ChatFormatting.OBFUSCATED
         108 -> var10000 = ChatFormatting.BOLD
         109 -> var10000 = ChatFormatting.STRIKETHROUGH
         110 -> var10000 = ChatFormatting.UNDERLINE
         111 -> var10000 = ChatFormatting.ITALIC
         114 -> var10000 = ChatFormatting.RESET
         else -> var10000 = null
      }

      return var10000
   }
}
