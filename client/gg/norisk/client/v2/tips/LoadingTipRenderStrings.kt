package gg.norisk.client.v2.tips

import org.jetbrains.annotations.NotNull

public class LoadingTipRenderStrings(title: String, tipLine: String) {
   @JvmField
   @NotNull
   public final val title: String

   @JvmField
   @NotNull
   public final val tipLine: String

   init {
      this.title = title
      this.tipLine = tipLine
   }
}
