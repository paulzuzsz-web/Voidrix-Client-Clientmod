package gg.norisk.client.v2.modules.keystrokes

import gg.norisk.compat.resource.MCKey
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nKeystrokePresets.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokePresets.kt\ngg/norisk/client/v2/modules/keystrokes/KeystrokePreset\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,99:1\n1563#2:100\n1634#2,3:101\n*S KotlinDebug\n*F\n+ 1 KeystrokePresets.kt\ngg/norisk/client/v2/modules/keystrokes/KeystrokePreset\n*L\n27#1:100\n27#1:101,3\n*E\n"])
public data class KeystrokePreset(id: String, displayName: String, keys: List<gg.norisk.client.v2.modules.keystrokes.KeystrokePreset.PresetEntry>) {
   public final val id: String
   public final val displayName: String
   public final val keys: List<gg.norisk.client.v2.modules.keystrokes.KeystrokePreset.PresetEntry>

   init {
      this.id = id
      this.displayName = displayName
      this.keys = keys
   }

   public fun materialize(offsetX: Int = 0, offsetY: Int = 0): List<PlacedKey> {
      val `$this$mapTo$iv$iv`: java.lang.Iterable = this.keys
      val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(this.keys, 10))

      for (`item$iv$iv` in `$this$mapTo$iv$iv`) {
         `destination$iv$iv`.add(
            PlacedKey(
               (`item$iv$iv` as KeystrokePreset.PresetEntry).cellX + offsetX,
               (`item$iv$iv` as KeystrokePreset.PresetEntry).cellY + offsetY,
               (`item$iv$iv` as KeystrokePreset.PresetEntry).widthCells,
               (`item$iv$iv` as KeystrokePreset.PresetEntry).heightCells,
               MCKey.Companion.fromName((`item$iv$iv` as KeystrokePreset.PresetEntry).keyName)
            )
         )
      }

      return `destination$iv$iv` as MutableList<PlacedKey>
   }

   public operator fun component1(): String {
      return this.id
   }

   public operator fun component2(): String {
      return this.displayName
   }

   public operator fun component3(): List<gg.norisk.client.v2.modules.keystrokes.KeystrokePreset.PresetEntry> {
      return this.keys
   }

   public fun copy(
      id: String = this.id,
      displayName: String = this.displayName,
      keys: List<gg.norisk.client.v2.modules.keystrokes.KeystrokePreset.PresetEntry> = this.keys
   ): KeystrokePreset {
      return KeystrokePreset(id, displayName, keys)
   }

   public override fun toString(): String {
      return "KeystrokePreset(id=${this.id}, displayName=${this.displayName}, keys=${this.keys})"
   }

   public override fun hashCode(): Int {
      return (this.id.hashCode() * 31 + this.displayName.hashCode()) * 31 + this.keys.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label34@
      if (this === other) {
         return true
      } else {
         return other is KeystrokePreset
            && this.id == (other as KeystrokePreset).id
            && this.displayName == (other as KeystrokePreset).displayName
            && this.keys == (other as KeystrokePreset).keys
         }
   }

   public data class PresetEntry(cellX: Int, cellY: Int, widthCells: Int, heightCells: Int, keyName: String) {
      public final val cellX: Int
      public final val cellY: Int
      public final val widthCells: Int
      public final val heightCells: Int
      public final val keyName: String

      init {
         this.cellX = cellX
         this.cellY = cellY
         this.widthCells = widthCells
         this.heightCells = heightCells
         this.keyName = keyName
      }

      public operator fun component1(): Int {
         return this.cellX
      }

      public operator fun component2(): Int {
         return this.cellY
      }

      public operator fun component3(): Int {
         return this.widthCells
      }

      public operator fun component4(): Int {
         return this.heightCells
      }

      public operator fun component5(): String {
         return this.keyName
      }

      public fun copy(
         cellX: Int = this.cellX,
         cellY: Int = this.cellY,
         widthCells: Int = this.widthCells,
         heightCells: Int = this.heightCells,
         keyName: String = this.keyName
      ): gg.norisk.client.v2.modules.keystrokes.KeystrokePreset.PresetEntry {
         return KeystrokePreset.PresetEntry(cellX, cellY, widthCells, heightCells, keyName)
      }

      public override fun toString(): String {
         return "PresetEntry(cellX=${this.cellX}, cellY=${this.cellY}, widthCells=${this.widthCells}, heightCells=${this.heightCells}, keyName=${this.keyName})"
      }

      public override fun hashCode(): Int {
         return (
                  ((Integer.hashCode(this.cellX) * 31 + Integer.hashCode(this.cellY)) * 31 + Integer.hashCode(this.widthCells)) * 31
                     + Integer.hashCode(this.heightCells)
               )
               * 31
            + this.keyName.hashCode()
         }

      public override operator fun equals(other: Any?): Boolean {
         label46@
         if (this === other) {
            return true
         } else {
            return other is KeystrokePreset.PresetEntry
               && this.cellX == (other as KeystrokePreset.PresetEntry).cellX
               && this.cellY == (other as KeystrokePreset.PresetEntry).cellY
               && this.widthCells == (other as KeystrokePreset.PresetEntry).widthCells
               && this.heightCells == (other as KeystrokePreset.PresetEntry).heightCells
               && this.keyName == (other as KeystrokePreset.PresetEntry).keyName
            }
      }
   }
}
