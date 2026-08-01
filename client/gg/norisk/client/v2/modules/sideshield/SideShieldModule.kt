package gg.norisk.client.v2.modules.sideshield

import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory

@NrcMiniTag(tags = ["sideshield"])
public object SideShieldModule : Module("Side Shield", ModuleCategory.VISUAL, false, false, false, 20) {
   public open val seoTags: Array<String>
}
