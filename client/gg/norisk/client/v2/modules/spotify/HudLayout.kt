package gg.norisk.client.v2.modules.spotify

import kotlin.enums.EnumEntries

public enum class HudLayout {
   COMPACT,
   DETAILED,
   MINI_PLAYER;

   @JvmStatic
   fun getEntries(): EnumEntries<HudLayout> {
      $ENTRIES
   }
}
