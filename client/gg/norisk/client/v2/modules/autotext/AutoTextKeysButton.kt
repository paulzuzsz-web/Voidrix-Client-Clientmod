package gg.norisk.client.v2.modules.autotext

import gg.norisk.compat.client.MCSounds
import gg.norisk.compat.resource.MCKey
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.base.BaseUIComponent
import gg.norisk.owolib.owo.ui.core.CursorStyle
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.UIComponent.FocusSource
import gg.norisk.owolib.owo.ui.inject.GreedyInputUIComponent
import gg.norisk.owolib.owo.ui.input.KeyEvent
import gg.norisk.owolib.owo.ui.input.MouseButtonEvent
import gg.norisk.owolib.owo.ui.util.FocusHandler
import gg.norisk.ui.api.value.KeyValue.KeyWrapper
import gg.norisk.ui.components.GreedyMouseInputComponent
import gg.norisk.ui.modules.v3.RightShiftMenuV3Screen
import gg.norisk.ui.modules.v3.V3Border
import gg.norisk.ui.modules.v3.V3Theme
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText

@SourceDebugExtension(["SMAP\nAutoTextKeysButton.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoTextKeysButton.kt\ngg/norisk/client/v2/modules/autotext/AutoTextKeysButton\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,168:1\n127#2:169\n40#2:170\n942#3:171\n940#3:172\n2746#4,3:173\n*S KotlinDebug\n*F\n+ 1 AutoTextKeysButton.kt\ngg/norisk/client/v2/modules/autotext/AutoTextKeysButton\n*L\n72#1:169\n72#1:170\n75#1:171\n77#1:172\n158#1:173,3\n*E\n"])
public class AutoTextKeysButton(getter: () -> MutableList<KeyWrapper>, setter: (MutableList<KeyWrapper>) -> Unit, fixedWidth: Int = 100)
   : BaseUIComponent,
   GreedyInputUIComponent,
   GreedyMouseInputComponent {
   private final val getter: () -> MutableList<KeyWrapper>
   private final val setter: (MutableList<KeyWrapper>) -> Unit
   private final val fixedWidth: Int
   private final var isListening: Boolean
   private final var isHovered: Boolean
   private final var capturedSinceListenStart: Int

   init {
      this.getter = getter
      this.setter = setter
      this.fixedWidth = fixedWidth
      this.sizing(Sizing.Companion.fixed(this.fixedWidth), Sizing.Companion.fixed(12))
      this.cursorStyle(CursorStyle.HAND)
      this.mouseEnter().subscribe({ 
         `this$0`.isHovered = true
      })
      this.mouseLeave().subscribe({ 
         `this$0`.isHovered = false
      })
      this.focusLost().subscribe({ 
         `this$0`.isListening = false
         `this$0`.capturedSinceListenStart = 0
      })
   }

   public open fun canFocus(source: FocusSource): Boolean {
      return true
   }

   public open fun isCapturingKey(): Boolean {
      return this.isListening
   }

   public open fun draw(graphics: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      val x1: Double = this.x
      val y1: Double = this.y
      val w: Double = this.width
      val h: Double = this.height
      val bw: Double = V3Border.INSTANCE.getWidth()
      graphics.fill(
         x1 + bw,
         y1 + bw,
         x1 + w - bw,
         y1 + h - bw,
         if (this.isListening)
            V3Theme.INSTANCE.accentAlpha(5, 80)
            else
            (if (this.isHovered) V3Theme.INSTANCE.accentAlpha(4, 60) else V3Theme.INSTANCE.accentAlpha(3, 40))
      )
      V3Border.INSTANCE
         .draw(
            graphics,
            x1,
            y1,
            w,
            h,
            if (this.isListening) V3Theme.INSTANCE.accent(9) else (if (this.isHovered) V3Theme.INSTANCE.accent(8) else V3Theme.INSTANCE.accent(6))
         )
         val var10000: Minecraft = Minecraft.getInstance()
      val var32: Font = var10000.font
      val var27: java.lang.String = this.displayText()
      val var28: Float = (float)(x1 + (w - var32.width((TextKt.getLiteral(var27) as Component) as FormattedText) * 0.75F) / 2)
      val var29: Float = (float)(y1 + (h - var32.lineHeight * 0.75F) / 2)
      val var31: Int = if (this.isListening) V3Theme.INSTANCE.accent(11) else (if (this.isHovered) V3Theme.INSTANCE.gray(12) else V3Theme.INSTANCE.gray(11))
      graphics.push()
      graphics.translate(var28, var29)
      graphics.scale(0.75F, 0.75F)
      graphics.drawString(var32, TextKt.getLiteral(var27) as Component, 0.0, 0.0, var31, RightShiftMenuV3Screen.Companion.getUseTextShadow())
      graphics.pop()
   }

   private fun displayText(): String {
      if (this.isListening && this.capturedSinceListenStart == 0) {
         return "..."
      } else {
         val keys: java.util.List = this.getter() as java.util.List
         return if (keys.isEmpty()) "—" else CollectionsKt.joinToString$default(keys, " + ", null, null, 0, null, { wrapper: KeyWrapper ->
            val var10000: java.lang.String = MCKey.Companion.fromName(wrapper.getTranslationKey()).getDisplayName().getString()
            var10000 as java.lang.CharSequence
         }, 30, null)
      }
   }

   public open fun onMouseDown(click: MouseButtonEvent, doubled: Boolean): Boolean {
      MCSounds.INSTANCE.playButtonSound()
      if (!this.isListening) {
         this.startListening()
      } else {
         this.addKey(MCKey.Companion.ofMouse(click.button()))
      }

      return true
   }

   public open fun onKeyPress(input: KeyEvent): Boolean {
      if (!this.isListening) {
         return super.onKeyPress(input)
      } else {
         MCSounds.INSTANCE.playButtonSound()
         val keyCode: Int = input.getKey()
         if (keyCode == 256) {
            this.setter(ArrayList())
            this.stopListening()
            return true
         } else {
            this.addKey(MCKey.Companion.ofKeyboard(keyCode))
            return true
         }
      }
   }

   public open fun clickedOutOf(mouseX: Double, mouseY: Double, button: Int) {
      if (this.isListening && button > 2) {
         this.addKey(MCKey.Companion.ofMouse(button))
      }
   }

   private fun startListening() {
      this.isListening = true
      this.capturedSinceListenStart = 0
      val var10000: FocusHandler = this.focusHandler()
      if (var10000 != null) {
         var10000.focus(this as UIComponent, FocusSource.MOUSE_CLICK)
      }
   }

   private fun stopListening() {
      if (this.isListening) {
         this.isListening = false
         this.capturedSinceListenStart = 0
         val var10000: FocusHandler = this.focusHandler()
         if (var10000 != null) {
            var10000.focus(null, FocusSource.MOUSE_CLICK)
         }
      }
   }

   private fun addKey(key: MCKey) {
      if (!key.isUnknown()) {
         val keys: java.util.List = CollectionsKt.toMutableList(this.getter() as java.util.Collection)
         if (this.capturedSinceListenStart == 0) {
            keys.clear()
         }

         val wrapper: KeyWrapper = KeyWrapper(key.getCode(), key.getType(), key.getName())
         val `$this$none$iv`: java.lang.Iterable = keys
         var var10000: Boolean
         if (keys is java.util.Collection && (keys as java.util.Collection).isEmpty()) {
            var10000 = true
         } else {
            val var6: java.util.Iterator = `$this$none$iv`.iterator()

            while (true) {
               if (!var6.hasNext()) {
                  var10000 = true
                  break
               }

               val it: KeyWrapper = var6.next() as KeyWrapper
               if (it.getCode() == wrapper.getCode() && it.getType() === wrapper.getType()) {
                  var10000 = false
                  break
               }
            }
         }

         if (var10000) {
            keys.add(wrapper)
         }

         val var10: Int = this.capturedSinceListenStart++
         this.setter(keys)
      }
   }

   protected open fun determineHorizontalContentSize(sizing: Sizing?): Double {
      return this.fixedWidth
   }

   protected open fun determineVerticalContentSize(sizing: Sizing?): Double {
      return 12.0
   }
}
