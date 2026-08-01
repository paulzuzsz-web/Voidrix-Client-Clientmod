@file:SourceDebugExtension(["SMAP\nCustomCrosshairPixelSection.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CustomCrosshairPixelSection.kt\ngg/norisk/client/v2/modules/crosshair/CustomCrosshairPixelSectionKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt\n+ 5 TextBuilder.kt\ngg/norisk/compat/text/LiteralTextBuilder\n*L\n1#1,178:1\n1563#2:179\n1634#2,3:180\n774#2:183\n865#2,2:184\n1#3:186\n8#4,4:187\n78#5,6:191\n72#5,4:197\n87#5:201\n*S KotlinDebug\n*F\n+ 1 CustomCrosshairPixelSection.kt\ngg/norisk/client/v2/modules/crosshair/CustomCrosshairPixelSectionKt\n*L\n42#1:179\n42#1:180,3\n42#1:183\n42#1:184,2\n92#1:187,4\n92#1:191,6\n92#1:197,4\n92#1:201\n*E\n"])

package gg.norisk.client.v2.modules.crosshair

import gg.norisk.compat.text.LiteralTextBuilder
import gg.norisk.ui.v2.toast.components.NrcToastComponent.Builder
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Reader
import java.util.ArrayList
import java.util.Arrays
import java.util.LinkedHashMap
import java.util.Locale
import java.util.Map.Entry
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component

private fun cellToChessCoord(col: Int, row: Int): String {
   return "${(char)(65 + col)}${row + 1}"
}

private fun chessCoordToCell(s: String): Pair<Int, Int>? {
   if (s.length() < 2) {
      return null
   } else {
      val col: Int = Character.toUpperCase(s.charAt(0)) - 'A'
      val var10000: java.lang.String = s.substring(1)
      val var3: Int = StringsKt.toIntOrNull(var10000)
      if (var3 != null) {
         val row: Int = var3 - 1
         return if (0 <= col && col < 15 && 0 <= row && row < 15) TuplesKt.to(col, row) else null
      } else {
         return null
      }
   }
}

public fun encodeToShareCode(map: Map<Pair<Int, Int>, Int>): String {
   return CollectionsKt.joinToString$default(CollectionsKt.sortedWith(map.entrySet(), ComparisonsKt.compareBy(arrayOf({ it: Entry ->
      (it.getKey() as Pair).getFirst() as java.lang.Comparable
   }, { it: Entry ->
      (it.getKey() as Pair).getSecond() as java.lang.Comparable
   }))), " ", null, null, 0, null, { var0: Entry ->
      val cell: Pair = var0.getKey() as Pair
      val rgb: Int = (var0.getValue() as java.lang.Number).intValue()
      val var10000: java.lang.String = cellToChessCoord((cell.getFirst() as java.lang.Number).intValue(), (cell.getSecond() as java.lang.Number).intValue())
      val var5: Array<Any> = arrayOf(rgb and 16777215)
      val var10001: java.lang.String = java.lang.String.format("%06X", Arrays.copyOf(var5, var5.length))
      ("$var10000 $var10001/") as java.lang.CharSequence
   }, 30, null)
}

public fun decodeFromShareCode(input: String): Map<Pair<Int, Int>, Int>? {
   val trimmed: java.lang.String = StringsKt.trim(input).toString()
   if (trimmed.length() != 0 && StringsKt.contains$default(trimmed, "/", false, 2, null)) {
      val result: java.util.Map = LinkedHashMap()
      val var16: java.lang.Iterable = StringsKt.split$default(trimmed, arrayOf("/"), false, 0, 6, null)
      var rgb: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(var16, 10))

      for (`element$iv$iv` in var16) {
         rgb.add(StringsKt.trim(`element$iv$iv` as java.lang.String).toString())
      }

      val var22: java.lang.Iterable = rgb as java.util.List
      rgb = ArrayList()

      for (var33 in var22) {
         if ((var33 as java.lang.String).length() > 0) {
            rgb.add(var33)
         }
      }

      for (var18 in rgb as java.util.List) {
         val var21: java.util.List = StringsKt.split$default(var18, arrayOf(" "), false, 2, 2, null)
         if (var21.size() == 2) {
            val var37: Pair = chessCoordToCell(var21.get(0) as java.lang.String)
            if (var37 != null) {
               var var29: Any
               try {
                  var29 = Result.constructor_impl/* $VF was: constructor-impl */(
                     (int)java.lang.Long.parseLong(var21.get(1) as java.lang.String, CharsKt.checkRadix(16))
                  )
               } catch (var14: java.lang.Throwable) {
                  var29 = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var14))
               }

               val var38: Int = (if (Result.isFailure_impl/* $VF was: isFailure-impl */(var29)) null else var29) as Int
               if (var38 != null) {
                  result.put(var37, var38)
               }
            }
         }
      }

      return if (result.isEmpty()) null else result
   } else {
      return null
   }
}

private fun copyToClipboard(text: String) {
   try {
      var var10000: java.lang.String = System.getProperty("os.name")
      var10000 = var10000.toLowerCase(Locale.ROOT)
      val var36: Int
      if (StringsKt.contains$default(var10000, "win", false, 2, null)) {
         var36 = ProcessBuilder("powershell", "-NoProfile", "-Command", "Set-Clipboard -Value '${StringsKt.replace$default(text, "'", "''", false, 4, null)}'")
            .start()
            .waitFor()
         } else if (StringsKt.contains$default(var10000, "mac", false, 2, null)) {
         val var25: Process = ProcessBuilder("pbcopy").start()
         val var27: Closeable = var25.getOutputStream()
         var var5: java.lang.Throwable = null

         try {
            val it: OutputStream = var27 as OutputStream
            val var10001: ByteArray = text.getBytes(Charsets.UTF_8)
            it.write(var10001)
            it.flush()
         } catch (var18: java.lang.Throwable) {
            var5 = var18
            throw var18
         } finally {
            CloseableKt.closeFinally(var27, var5)
         }

         var36 = var25.waitFor()
      } else {
         val var26: Process = ProcessBuilder("xclip", "-selection", "clipboard").start()
         val var29: Closeable = var26.getOutputStream()
         var var30: java.lang.Throwable = null

         try {
            val var32: OutputStream = var29 as OutputStream
            val var37: ByteArray = text.getBytes(Charsets.UTF_8)
            var32.write(var37)
            var32.flush()
         } catch (var20: java.lang.Throwable) {
            var30 = var20
            throw var20
         } finally {
            CloseableKt.closeFinally(var29, var30)
         }

         var36 = var26.waitFor()
      }

      val var24: Any = Result.constructor_impl/* $VF was: constructor-impl */(var36)
   } catch (var22: java.lang.Throwable) {
      val var1: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var22))
   }
}

private fun getClipboard(): String {
   var var0: Any
   try {
      var var10000: java.lang.String = System.getProperty("os.name")
      var10000 = var10000.toLowerCase(Locale.ROOT)
      val var17: Process = if (StringsKt.contains$default(var10000, "win", false, 2, null))
         ProcessBuilder("powershell", "-NoProfile", "-Command", "Get-Clipboard").start()
         else
         (
            if (StringsKt.contains$default(var10000, "mac", false, 2, null))
               ProcessBuilder("pbpaste").start()
               else
               ProcessBuilder("xclip", "-selection", "clipboard", "-o").start()
         )
         val var18: InputStream = var17.getInputStream()
      val var5: Reader = InputStreamReader(var18, Charsets.UTF_8)
      val var14: java.lang.String = StringsKt.trim(TextStreamsKt.readText(if (var5 is BufferedReader) var5 as BufferedReader else BufferedReader(var5, 8192)))
         .toString()
         var17.waitFor()
      var0 = Result.constructor_impl/* $VF was: constructor-impl */(var14)
   } catch (var7: java.lang.Throwable) {
      var0 = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var7))
   }

   return (if (Result.isFailure_impl/* $VF was: isFailure-impl */(var0)) "" else var0) as java.lang.String
}

private fun showToast(text: String, success: Boolean = true) {
   val `baseText$iv`: Builder = Builder(null, null, 3, null)
   if (success) {
      Builder.success$default(`baseText$iv`, false, 1, null)
   } else {
      Builder.error$default(`baseText$iv`, false, 1, null)
   }

   val var14: LiteralTextBuilder = LiteralTextBuilder(null, true)
   var14.getAppendTasks().add(CustomCrosshairPixelSectionKt$showToast$lambda$13$$inlined$text$default$1(var14, text, true))
   var14.setBold(true)
   `baseText$iv`.title(var14.build() as Component).build().show()
}
