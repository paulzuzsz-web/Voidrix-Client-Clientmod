package gg.norisk.client.v2.modules.armorstatus

import gg.norisk.compat.client.EquipmentSlotCompat
import java.util.Comparator
import kotlin.jvm.internal.SourceDebugExtension

// $VF: Class flags could not be determined
@SourceDebugExtension(["SMAP\nComparisons.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Comparisons.kt\nkotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2\n+ 2 ArmorStatus.kt\ngg/norisk/client/v2/modules/armorstatus/ArmorStatus$ArmorStatusContainer\n*L\n1#1,102:1\n160#2:103\n*E\n"])
internal class `ArmorStatus$ArmorStatusContainer$rebuildSlots$$inlined$compareBy$1`<T> : Comparator {
   override final fun compare(a: T, b: T): Int {
      ComparisonsKt.compareValues(
         ((a as Pair).getFirst() as EquipmentSlotCompat).getSortOrder(), ((b as Pair).getFirst() as EquipmentSlotCompat).getSortOrder()
      )
   }
}
