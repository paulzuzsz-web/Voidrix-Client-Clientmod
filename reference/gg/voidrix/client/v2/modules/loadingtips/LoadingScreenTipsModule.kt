package gg.voidrix.client.v2.modules.loadingtips

import gg.voidrix.client.v2.tips.VoidrixLoadingTipsPresenter
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory

public object LoadingScreenTipsModule : Module("Tips", ModuleCategory.QUALITY_OF_LIFE, true, true, false, 16) {
   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }

   public open fun onDisable() {
      VoidrixLoadingTipsPresenter.clearSession()
   }

   public open fun onEnable() {
      VoidrixLoadingTipsPresenter.clearSession()
   }
}
