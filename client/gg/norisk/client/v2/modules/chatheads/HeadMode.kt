package gg.norisk.client.v2.modules.chatheads

import kotlin.enums.EnumEntries

public enum class HeadMode {
   BEFORE_LINE,
   BEFORE_NAME,
   BEFORE_LINE_OFFSET;

   @JvmStatic
   fun getEntries(): EnumEntries<HeadMode> {
      $ENTRIES
   }
}
