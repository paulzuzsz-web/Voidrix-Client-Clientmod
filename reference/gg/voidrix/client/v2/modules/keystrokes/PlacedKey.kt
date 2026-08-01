package gg.voidrix.client.v2.modules.keystrokes

import gg.voidrix.compat.resource.MCKey

public data class PlacedKey(cellX: Int, cellY: Int, widthCells: Int, heightCells: Int, key: MCKey = MCKey.Companion.getUNKNOWN()) {
   public final val cellX: Int
   public final val cellY: Int
   public final val widthCells: Int
   public final val heightCells: Int
   public final val key: MCKey

   init {
      this.cellX = cellX
      this.cellY = cellY
      this.widthCells = widthCells
      this.heightCells = heightCells
      this.key = key
   }

   public final val right: Int
      public final get() {
         return this.cellX + this.widthCells
      }


   public final val bottom: Int
      public final get() {
         return this.cellY + this.heightCells
      }


   public fun contains(col: Int, row: Int): Boolean {
      var var3: Int = this.cellX
      if (col < this.right && var3 <= col) {
         var3 = this.cellY
         if (row < this.bottom && var3 <= row) {
            return true
         }
      }

      return false
   }

   public fun overlaps(other: PlacedKey): Boolean {
      return this.right > other.cellX && other.right > this.cellX && this.bottom > other.cellY && other.bottom > this.cellY
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

   public operator fun component5(): MCKey {
      return this.key
   }

   public fun copy(
      cellX: Int = this.cellX,
      cellY: Int = this.cellY,
      widthCells: Int = this.widthCells,
      heightCells: Int = this.heightCells,
      key: MCKey = this.key
   ): PlacedKey {
      return PlacedKey(cellX, cellY, widthCells, heightCells, key)
   }

   public override fun toString(): String {
      return "PlacedKey(cellX=${this.cellX}, cellY=${this.cellY}, widthCells=${this.widthCells}, heightCells=${this.heightCells}, key=${this.key})"
   }

   public override fun hashCode(): Int {
      return (
               ((Integer.hashCode(this.cellX) * 31 + Integer.hashCode(this.cellY)) * 31 + Integer.hashCode(this.widthCells)) * 31
                  + Integer.hashCode(this.heightCells)
            )
            * 31
         + this.key.hashCode()
      }

   public override operator fun equals(other: Any?): Boolean {
      label46@
      if (this === other) {
         return true
      } else {
         return other is PlacedKey
            && this.cellX == (other as PlacedKey).cellX
            && this.cellY == (other as PlacedKey).cellY
            && this.widthCells == (other as PlacedKey).widthCells
            && this.heightCells == (other as PlacedKey).heightCells
            && this.key == (other as PlacedKey).key
         }
   }
}
