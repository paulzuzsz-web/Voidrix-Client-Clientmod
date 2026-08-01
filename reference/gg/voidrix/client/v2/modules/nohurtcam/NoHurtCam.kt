package gg.voidrix.client.v2.modules.nohurtcam

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory

@VoidrixMiniTag(tags = ["nohurtcam"])
public object NoHurtCam : Module("NoHurtCam", ModuleCategory.VISUAL, true, false, false, 16) {
   public open val seoTags: Array<String>

   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }
}
