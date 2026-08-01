package gg.voidrix.client.v2.modules.keystrokes

import gg.voidrix.compat.client.MCSounds
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent.FocusSource
import gg.voidrix.owolib.owo.ui.inject.GreedyInputComponent
import gg.voidrix.owolib.owo.ui.input.KeyEvent
import gg.voidrix.owolib.owo.ui.util.FocusHandler
import gg.voidrix.ui.components.GreedyMouseInputComponent
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component

private class PressToBindButton(initialKey: MCKey, onBind: (MCKey) -> Unit, autoStartListening: Boolean = false) : VoidrixLabelButton(
         Companion.labelFor(initialKey, autoStartListening), null, 2
      ),
   GreedyInputComponent,
   GreedyMouseInputComponent {
   public final val onBind: (MCKey) -> Unit
   private final var currentKey: MCKey
   private final var isListening: Boolean
   private final var autoStartPending: Boolean

   init {
      this.onBind = onBind
      this.currentKey = initialKey
      this.autoStartPending = autoStartListening
      this.setSelected({ 
         `this$0`.isListening
      })
      this.focusLost().subscribe({ 
         if (`this$0`.isListening) {
            `this$0`.isListening = false
            `this$0`.refreshLabel()
         }
      })
      this.onPress({ var1: VoidrixLabelButton, var2: Double, var4: Double, button: Int ->
         MCSounds.INSTANCE.playButtonSound()
         if (!`this$0`.isListening) {
            `this$0`.startListening()
         } else {
            `this$0`.accept(MCKey.Companion.ofMouse(button))
         }

         Unit.INSTANCE
      })
   }

   public open fun canFocus(source: FocusSource): Boolean {
      return true
   }

   public open fun mount(parent: ParentUIComponent?, x: Double, y: Double) {
      super.mount(parent, x, y)
      if (this.autoStartPending) {
         this.autoStartPending = false
         this.startListening()
      }
   }

   private fun startListening() {
      if (!this.isListening) {
         this.isListening = true
         val var10000: FocusHandler = this.focusHandler()
         if (var10000 != null) {
            var10000.focus(this as UIComponent, FocusSource.MOUSE_CLICK)
         }

         this.refreshLabel()
      }
   }

   private fun accept(key: MCKey) {
      this.currentKey = key
      this.onBind(key)
      this.isListening = false
      this.refreshLabel()
      val var10000: FocusHandler = this.focusHandler()
      if (var10000 != null) {
         var10000.focus(null, FocusSource.MOUSE_CLICK)
      }
   }

   public open fun onKeyPress(input: KeyEvent): Boolean {
      if (!this.isListening) {
         return super.onKeyPress(input)
      } else {
         MCSounds.INSTANCE.playButtonSound()
         this.accept(if (input.getKey() == 256) MCKey.Companion.getUNKNOWN() else MCKey.Companion.ofKeyboard(input.getKey()))
         return true
      }
   }

   public open fun clickedOutOf(mouseX: Double, mouseY: Double, button: Int) {
      if (this.isListening && button > 2) {
         this.accept(MCKey.Companion.ofMouse(button))
      }
   }

   private fun refreshLabel() {
      this.getLabel().text(TextKt.getLiteral(Companion.labelFor(this.currentKey, this.isListening)) as Component)
   }

   @SourceDebugExtension(["SMAP\nKeystrokeLayoutEditorScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokeLayoutEditorScreen.kt\ngg/voidrix/client/v2/modules/keystrokes/PressToBindButton$Companion\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,1011:1\n269#2:1012\n*S KotlinDebug\n*F\n+ 1 KeystrokeLayoutEditorScreen.kt\ngg/voidrix/client/v2/modules/keystrokes/PressToBindButton$Companion\n*L\n652#1:1012\n*E\n"])
   public companion object {
      private fun labelFor(key: MCKey, listening: Boolean): String {
         val var10000: java.lang.String
         if (listening) {
            var10000 = "press any key…"
         } else if (key.isUnknown()) {
            var10000 = "click to bind"
         } else {
            var10000 = key.getDisplayName().getString()
         }

         return var10000
      }
   }
}
