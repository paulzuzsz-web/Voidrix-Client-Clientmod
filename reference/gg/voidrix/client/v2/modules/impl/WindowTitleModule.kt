package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.util.McVersion

public object WindowTitleModule {
   private const val PROFILE_PROPERTY: String = "voidrix.profile.name"
   private const val DEFAULT_PROFILE: String = "Voidrix"

   @JvmStatic
   public fun getTitle(): String {
      return "${System.getProperty("voidrix.profile.name", "Voidrix")} | Minecraft ${McVersion.current()}"
   }
}
