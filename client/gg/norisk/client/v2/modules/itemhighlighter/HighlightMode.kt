package gg.norisk.client.v2.modules.itemhighlighter

import kotlin.enums.EnumEntries
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
public enum class HighlightMode {
   SHINY,
   COLOR;

   @JvmStatic
   private Lazy<KSerializer<Object>> $cachedSerializer$delegate = LazyKt.lazy(
      LazyThreadSafetyMode.PUBLICATION, gg/norisk/client/v2/modules/itemhighlighter/HighlightMode##Lambda_0_112()
   );

   @JvmStatic
   fun getEntries(): EnumEntries<HighlightMode> {
      $ENTRIES
   }

   public companion object {
      public fun serializer(): KSerializer<HighlightMode> {
         return this.get$cachedSerializer()
      }
   }
}
