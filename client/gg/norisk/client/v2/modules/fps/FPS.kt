package gg.norisk.client.v2.modules.fps

import gg.norisk.compat.text.TextKt
import gg.norisk.ui.api.hud.SingleTextHud
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@SourceDebugExtension(["SMAP\nFPS.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FPS.kt\ngg/norisk/client/v2/modules/fps/FPS\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,16:1\n271#2:17\n40#2:18\n*S KotlinDebug\n*F\n+ 1 FPS.kt\ngg/norisk/client/v2/modules/fps/FPS\n*L\n13#1:17\n13#1:18\n*E\n"])
public object FPS : SingleTextHud(
      "FPS", "{fps} FPS", null, TextKt.getLiteral("{fps} wird mit den aktuellen FPS ersetzt") as Component, false, false, false, 116
   ) {
   public open fun getParsedText(): String {
      val var10000: java.lang.String = this.getText()
      val var10002: Minecraft = Minecraft.getInstance()
      return StringsKt.replace(var10000, "{fps}", java.lang.String.valueOf(var10002.getFps()), true)
   }
}
