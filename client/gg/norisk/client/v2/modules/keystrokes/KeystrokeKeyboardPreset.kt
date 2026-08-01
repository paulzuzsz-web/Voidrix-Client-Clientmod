package gg.norisk.client.v2.modules.keystrokes

import gg.norisk.compat.input.MCKeyBindings
import gg.norisk.compat.resource.MCKey
import java.util.ArrayList
import java.util.LinkedHashSet
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

@SourceDebugExtension(["SMAP\nKeystrokeKeyboardPreset.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokeKeyboardPreset.kt\ngg/norisk/client/v2/modules/keystrokes/KeystrokeKeyboardPreset\n+ 2 MCKeyBindings.kt\ngg/norisk/compat/input/MCKeyBindings\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,132:1\n20#2:133\n21#2:135\n22#2:137\n23#2:139\n24#2:141\n27#2:143\n40#3:134\n40#3:136\n40#3:138\n40#3:140\n40#3:142\n40#3:144\n1878#4,3:145\n1878#4,3:148\n1878#4,3:151\n1878#4,3:154\n*S KotlinDebug\n*F\n+ 1 KeystrokeKeyboardPreset.kt\ngg/norisk/client/v2/modules/keystrokes/KeystrokeKeyboardPreset\n*L\n91#1:133\n92#1:135\n93#1:137\n94#1:139\n97#1:141\n98#1:143\n91#1:134\n92#1:136\n93#1:138\n94#1:140\n97#1:142\n98#1:144\n32#1:145,3\n36#1:148,3\n40#1:151,3\n44#1:154,3\n*E\n"])
public object KeystrokeKeyboardPreset {
   public const val ID: String = "keyboard"
   public const val DISPLAY_NAME: String = "Keyboard"
   private final val KEYBOARD_POSITIONS: Map<String, Pair<Int, Int>>
   private final val SPECIAL_WIDTHS: Map<String, Pair<Int, Int>>
   public const val FULL_ID: String = "full_keyboard"
   public const val FULL_DISPLAY_NAME: String = "Full Keyboard"

   public fun materialize(): List<PlacedKey> {
      val result: java.util.List = ArrayList()
      val usedPositions: java.util.Set = LinkedHashSet()
      var var10002: Minecraft = Minecraft.getInstance()
      val var17: KeyMapping = var10002.options.keyUp
      materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var17))
      var10002 = Minecraft.getInstance()
      val var19: KeyMapping = var10002.options.keyDown
      materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var19))
      var10002 = Minecraft.getInstance()
      val var21: KeyMapping = var10002.options.keyLeft
      materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var21))
      var10002 = Minecraft.getInstance()
      val var23: KeyMapping = var10002.options.keyRight
      materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var23))
      var10002 = Minecraft.getInstance()
      val var25: KeyMapping = var10002.options.keyJump
      materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var25))
      var10002 = Minecraft.getInstance()
      val var27: KeyMapping = var10002.options.keyShift
      materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var27))

      repeat(8) { var10 ->
         val var10000: KeyMapping = MCKeyBindings.hotbarSlot(var10)
         if (var10000 != null) {
            materialize$add(usedPositions, result, MCKeyBindings.getBoundKey(var10000))
         }
      }

      return result
   }

   public fun materializeFull(): List<PlacedKey> {
      val result: java.util.List = ArrayList()

      for (var3 in KEYBOARD_POSITIONS.entrySet()) {
         val keyName: java.lang.String = var3.getKey() as java.lang.String
         val pos: Pair = var3.getValue() as Pair
         val key: MCKey = MCKey.Companion.fromName(keyName)
         if (!key.isUnknown()) {
            var var10000: Pair = SPECIAL_WIDTHS.get(keyName)
            if (var10000 == null) {
               var10000 = TuplesKt.to(2, 2)
            }

            result.add(
               PlacedKey(
                  (pos.getFirst() as java.lang.Number).intValue(),
                  (pos.getSecond() as java.lang.Number).intValue(),
                  (var10000.component1() as java.lang.Number).intValue(),
                  (var10000.component2() as java.lang.Number).intValue(),
                  key
               )
            )
         }
      }

      return result
   }

   @JvmStatic
   fun `materialize$add`(usedPositions: MutableSet<Pair<Integer, Integer>>, result: MutableList<PlacedKey>, key: MCKey) {
      if (!key.isUnknown()) {
         var var10000: Pair = KEYBOARD_POSITIONS.get(key.getName())
         if (var10000 != null) {
            if (!usedPositions.contains(var10000)) {
               var10000 = SPECIAL_WIDTHS.get(key.getName())
               if (var10000 == null) {
                  var10000 = TuplesKt.to(2, 2)
               }

               result.add(
                  PlacedKey(
                     (var10000.getFirst() as java.lang.Number).intValue(),
                     (var10000.getSecond() as java.lang.Number).intValue(),
                     (var10000.component1() as java.lang.Number).intValue(),
                     (var10000.component2() as java.lang.Number).intValue(),
                     key
                  )
               )
               usedPositions.add(var10000)
            }
         }
      }
   }

   @JvmStatic
   fun {
      val var0: java.util.Map = MapsKt.createMapBuilder()
      val `$this$KEYBOARD_POSITIONS_u24lambda_u244`: java.util.Map = var0
      var var13: java.lang.Iterable = CollectionsKt.listOf(arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"))
      var `index$iv`: Int = 0

      for (`item$iv` in var13) {
         val var8: Int = `index$iv`++
         if (var8 < 0) {
            CollectionsKt.throwIndexOverflow()
         }

         `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.${`item$iv` as java.lang.String}", TuplesKt.to(2 + var8 * 2, 0))
      }

      var13 = CollectionsKt.listOf(arrayOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"))
      `index$iv` = 0

      for (var29 in var13) {
         val var32: Int = `index$iv`++
         if (var32 < 0) {
            CollectionsKt.throwIndexOverflow()
         }

         `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.${var29 as java.lang.String}", TuplesKt.to(3 + var32 * 2, 2))
      }

      var13 = CollectionsKt.listOf(arrayOf("a", "s", "d", "f", "g", "h", "j", "k", "l"))
      `index$iv` = 0

      for (var30 in var13) {
         val var33: Int = `index$iv`++
         if (var33 < 0) {
            CollectionsKt.throwIndexOverflow()
         }

         `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.${var30 as java.lang.String}", TuplesKt.to(4 + var33 * 2, 4))
      }

      var13 = CollectionsKt.listOf(arrayOf("z", "x", "c", "v", "b", "n", "m"))
      `index$iv` = 0

      for (var31 in var13) {
         val var34: Int = `index$iv`++
         if (var34 < 0) {
            CollectionsKt.throwIndexOverflow()
         }

         `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.${var31 as java.lang.String}", TuplesKt.to(5 + var34 * 2, 6))
      }

      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.left.shift", TuplesKt.to(0, 6))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.right.shift", TuplesKt.to(19, 6))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.left.control", TuplesKt.to(0, 8))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.right.control", TuplesKt.to(22, 8))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.left.alt", TuplesKt.to(4, 8))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.right.alt", TuplesKt.to(18, 8))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.space", TuplesKt.to(8, 8))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.tab", TuplesKt.to(0, 2))
      `$this$KEYBOARD_POSITIONS_u24lambda_u244`.put("key.keyboard.caps.lock", TuplesKt.to(0, 4))
      KEYBOARD_POSITIONS = MapsKt.build(var0)
      SPECIAL_WIDTHS = MapsKt.mapOf(
         arrayOf(
            TuplesKt.to("key.keyboard.space", TuplesKt.to(10, 2)),
            TuplesKt.to("key.keyboard.left.shift", TuplesKt.to(5, 2)),
            TuplesKt.to("key.keyboard.right.shift", TuplesKt.to(4, 2)),
            TuplesKt.to("key.keyboard.left.control", TuplesKt.to(4, 2)),
            TuplesKt.to("key.keyboard.right.control", TuplesKt.to(4, 2)),
            TuplesKt.to("key.keyboard.left.alt", TuplesKt.to(4, 2)),
            TuplesKt.to("key.keyboard.right.alt", TuplesKt.to(4, 2)),
            TuplesKt.to("key.keyboard.tab", TuplesKt.to(3, 2)),
            TuplesKt.to("key.keyboard.caps.lock", TuplesKt.to(4, 2))
         )
      )
   }
}
