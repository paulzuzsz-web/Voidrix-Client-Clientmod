package gg.norisk.client.v2.serverstyling

import gg.norisk.ui.hints.HintManager

public object PromotedServerVisibility {
   public const val ADVERT_IP: String = "advert.norisk.space"
   private const val HIDDEN_ID: String = "advert.norisk.host.hidden"

   @JvmStatic
   public fun isHidden(): Boolean {
      return HintManager.INSTANCE.isDismissed("advert.norisk.host.hidden")
   }

   @JvmStatic
   public fun hide() {
      HintManager.INSTANCE.dismiss("advert.norisk.host.hidden")
   }
}
