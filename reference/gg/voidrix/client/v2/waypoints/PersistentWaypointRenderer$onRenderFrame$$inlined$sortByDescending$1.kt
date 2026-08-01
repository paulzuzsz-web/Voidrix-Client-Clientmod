package gg.voidrix.client.v2.waypoints

import java.util.Comparator
import kotlin.jvm.internal.SourceDebugExtension

// $VF: Class flags could not be determined
@SourceDebugExtension(["SMAP\nComparisons.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Comparisons.kt\nkotlin/comparisons/ComparisonsKt__ComparisonsKt$compareByDescending$1\n+ 2 PersistentWaypointRenderer.kt\ngg/voidrix/client/v2/waypoints/PersistentWaypointRenderer\n*L\n1#1,121:1\n188#2:122\n*E\n"])
internal class `PersistentWaypointRenderer$onRenderFrame$$inlined$sortByDescending$1`<T> : Comparator {
   override final fun compare(a: T, b: T): Int {
      ComparisonsKt.compareValues(
         (b as PersistentWaypointRenderer.VisibleEntry).cache.distanceSq, (a as PersistentWaypointRenderer.VisibleEntry).cache.distanceSq
      )
   }
}
