package gg.norisk.client.v2.waypoints

import kotlin.random.Random

public object WaypointColors {
   public final val COLORS: IntArray =
      intArrayOf(0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925, 5636095, 16733525, 16733695, 16777045, 16777215)

   public fun random(): Int {
      return ArraysKt.random(COLORS, Random.Default as Random)
   }

   public fun fromIndex(index: Int): Int {
      return COLORS[RangesKt.coerceIn(index, 0, ArraysKt.getLastIndex(COLORS))]
   }
}
