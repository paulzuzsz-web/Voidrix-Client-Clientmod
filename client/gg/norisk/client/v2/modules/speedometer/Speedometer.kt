package gg.norisk.client.v2.modules.speedometer

import gg.norisk.compat.text.TextKt
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.SingleTextHud
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import java.util.Arrays
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nSpeedometer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Speedometer.kt\ngg/norisk/client/v2/modules/speedometer/Speedometer\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n*L\n1#1,37:1\n185#2:38\n40#2:39\n915#3:40\n917#3:41\n916#3:42\n*S KotlinDebug\n*F\n+ 1 Speedometer.kt\ngg/norisk/client/v2/modules/speedometer/Speedometer\n*L\n23#1:38\n23#1:39\n25#1:40\n26#1:41\n27#1:42\n*E\n"])
public object Speedometer : SingleTextHud(
      "Speedometer", "{speed} m/s", null, TextKt.getLiteral("{speed} wird mit der aktuellen Geschwindigkeit replaced") as Component, false, false, false, 116
   ) {
   @Category(name = "Settings")
   @NotNull
   public final var includeY: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return includeY$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         includeY$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(false, 0, 0, 60, 0, 23, null)
   }

   public open fun getParsedText(): String {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player == null) {
         return StringsKt.replace(this.getText(), "{speed}", "0.00", true)
      } else {
         val player: LocalPlayer = var10000.player
         val dx: Double = var10000.player.getX() - player.xo
         val var15: Double = player.getZ() - player.zo
         val var23: Double = if (this.includeY) player.getY() - player.yo else 0.0
         val var17: Double = Math.sqrt(dx * dx + var15 * var15 + var23 * var23) / 0.05
         val var11: Locale = Locale.ENGLISH
         val var18: Array<Any> = arrayOf(var17)
         val var24: java.lang.String = java.lang.String.format(var11, "%.2f", Arrays.copyOf(var18, var18.length))
         return StringsKt.replace(this.getText(), "{speed}", var24, true)
      }
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }
}
