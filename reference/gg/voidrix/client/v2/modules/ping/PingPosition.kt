package gg.voidrix.client.v2.modules.ping

import kotlin.enums.EnumEntries

public enum class PingPosition {
   ABOVE,
   BELOW,
   LEFT,
   RIGHT,
   NONE;

   @JvmStatic
   fun getEntries(): EnumEntries<PingPosition> {
      $ENTRIES
   }
}
