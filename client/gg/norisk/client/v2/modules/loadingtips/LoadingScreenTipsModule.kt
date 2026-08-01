package gg.norisk.client.v2.modules.loadingtips

import gg.norisk.client.v2.tips.NrcLoadingTipsPresenter
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory

public object LoadingScreenTipsModule : Module("Tips", ModuleCategory.QUALITY_OF_LIFE, true, true, false, 16) {
   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }

   public open fun onDisable() {
      NrcLoadingTipsPresenter.clearSession()
   }

   public open fun onEnable() {
      NrcLoadingTipsPresenter.clearSession()
   }
}
