package gg.norisk.client.v2.modules.nohurtcam

import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory

@NrcMiniTag(tags = ["nohurtcam"])
public object NoHurtCam : Module("NoHurtCam", ModuleCategory.VISUAL, true, false, false, 16) {
   public open val seoTags: Array<String>

   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }
}
