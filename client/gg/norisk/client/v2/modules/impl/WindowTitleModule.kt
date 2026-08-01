package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.util.McVersion

public object WindowTitleModule {
   private const val PROFILE_PROPERTY: String = "norisk.profile.name"
   private const val DEFAULT_PROFILE: String = "Voidrix"

   @JvmStatic
   public fun getTitle(): String {
      return "${System.getProperty("norisk.profile.name", "Voidrix")} | Minecraft ${McVersion.current()}"
   }
}
