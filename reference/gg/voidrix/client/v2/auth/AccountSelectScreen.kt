package gg.voidrix.client.v2.auth

import gg.voidrix.compat.scale.IVoidrixScreen
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.base.BaseOwoScreen
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.OwoUIAdapter
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.ui.components.voidrix.VoidrixDialog
import gg.voidrix.ui.modules.v3.V3Theme
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nAccountSelectScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AccountSelectScreen.kt\ngg/voidrix/client/v2/auth/AccountSelectScreen\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,66:1\n67#2:67\n269#2:68\n355#3:69\n40#3:70\n356#3:71\n*S KotlinDebug\n*F\n+ 1 AccountSelectScreen.kt\ngg/voidrix/client/v2/auth/AccountSelectScreen\n*L\n49#1:67\n49#1:68\n57#1:69\n57#1:70\n57#1:71\n*E\n"])
public class AccountSelectScreen(parent: Screen?) : BaseOwoScreen(null, 1), IVoidrixScreen {
   private final val parent: Screen?
   protected open val renderPanoramaWhenNoWorld: Boolean
   protected open val useBlurredBackground: Boolean

   init {
      this.parent = parent
      this.renderPanoramaWhenNoWorld = true
      this.useBlurredBackground = true
   }

   protected open fun createAdapter(): OwoUIAdapter<FlowLayout> {
      return OwoUIAdapter.Companion.create(this as Screen, gg/voidrix/client/v2/auth/AccountSelectScreen##Lambda_0_61())
   }

   protected open fun build(rootComponent: FlowLayout) {
      rootComponent.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      val dialog: <unrepresentable> = object : VoidrixDialog {
         public open fun close() {
            AccountSelectScreen.this.onClose()
         }
      }
      dialog.horizontalSizing(Sizing.Companion.fixed(340))
      val var10000: LabelComponent = dialog.getTitle()
      val `$i$f$asString`: Array<Any> = arrayOfNulls(0)
      var var10001: MutableComponent = Component.translatable("voidrix.auth.select.title", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
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
