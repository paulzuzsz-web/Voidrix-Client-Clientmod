package gg.voidrix.client.v2.modules.sideshield

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory

@VoidrixMiniTag(tags = ["sideshield"])
public object SideShieldModule : Module("Side Shield", ModuleCategory.VISUAL, false, false, false, 20) {
   public open val seoTags: Array<String>
}
