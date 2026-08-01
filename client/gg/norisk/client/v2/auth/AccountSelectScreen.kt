package gg.norisk.client.v2.auth

import gg.norisk.compat.scale.INRCScreen
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.base.BaseOwoScreen
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.OwoUIAdapter
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.components.nrc.NrcDialog
import gg.norisk.ui.modules.v3.V3Theme
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nAccountSelectScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AccountSelectScreen.kt\ngg/norisk/client/v2/auth/AccountSelectScreen\n+ 2 Text.kt\ngg/norisk/compat/text/TextKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,66:1\n67#2:67\n269#2:68\n355#3:69\n40#3:70\n356#3:71\n*S KotlinDebug\n*F\n+ 1 AccountSelectScreen.kt\ngg/norisk/client/v2/auth/AccountSelectScreen\n*L\n49#1:67\n49#1:68\n57#1:69\n57#1:70\n57#1:71\n*E\n"])
public class AccountSelectScreen(parent: Screen?) : BaseOwoScreen(null, 1), INRCScreen {
   private final val parent: Screen?
   protected open val renderPanoramaWhenNoWorld: Boolean
   protected open val useBlurredBackground: Boolean

   init {
      this.parent = parent
      this.renderPanoramaWhenNoWorld = true
      this.useBlurredBackground = true
   }

   protected open fun createAdapter(): OwoUIAdapter<FlowLayout> {
      return OwoUIAdapter.Companion.create(this as Screen, gg/norisk/client/v2/auth/AccountSelectScreen##Lambda_0_61())
   }

   protected open fun build(rootComponent: FlowLayout) {
      rootComponent.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      val dialog: <unrepresentable> = object : NrcDialog {
         public open fun close() {
            AccountSelectScreen.this.onClose()
         }
      }
      dialog.horizontalSizing(Sizing.Companion.fixed(340))
      val var10000: LabelComponent = dialog.getTitle()
      val `$i$f$asString`: Array<Any> = arrayOfNulls(0)
      var var10001: MutableComponent = Component.translatable("nrc.auth.select.title", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
      val var8: java.lang.String = (var10001 as Component).getString()
      var10001 = TextKt.getLiteral(TextKt.toSmallCaps(var8)).withColor(V3Theme.INSTANCE.gray(12))
      var10000.text(var10001 as Component)
      dialog.content(AccountSelectContent({ 
         `this$0`.onClose()
         Unit.INSTANCE
      }) as UIComponent)
      dialog.getButtonActionWrapper().verticalSizing(Sizing.Companion.fixed(0))
      rootComponent.child(dialog as UIComponent)
   }

   public open fun onClose() {
      val `screen$iv`: Screen = this.parent
      val var10000: Minecraft = Minecraft.getInstance()
      var10000.gui.setScreen(`screen$iv`)
   }

   public open fun isPauseScreen(): Boolean {
      return false
   }
}
