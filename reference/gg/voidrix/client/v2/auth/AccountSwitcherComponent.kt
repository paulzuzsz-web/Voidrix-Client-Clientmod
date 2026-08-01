package gg.voidrix.client.v2.auth

import com.mojang.authlib.GameProfile
import gg.voidrix.compat.auth.CurrentSessionInfo
import gg.voidrix.compat.auth.SessionHelper
import gg.voidrix.compat.auth.SessionStatus
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.CursorStyle
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.v2.toast.components.PlayerHeadComponent
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nAccountSwitcherComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AccountSwitcherComponent.kt\ngg/voidrix/client/v2/auth/AccountSwitcherComponent\n+ 2 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 3 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 5 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n*L\n1#1,112:1\n8#2,4:113\n78#3,6:117\n72#3,4:123\n87#3:127\n78#3,6:128\n72#3,4:134\n87#3:138\n78#3,6:139\n72#3,4:145\n87#3:149\n355#4:150\n40#4:151\n356#4:152\n21#5:153\n*S KotlinDebug\n*F\n+ 1 AccountSwitcherComponent.kt\ngg/voidrix/client/v2/auth/AccountSwitcherComponent\n*L\n69#1:113,4\n70#1:117,6\n70#1:123,4\n70#1:127\n71#1:128,6\n71#1:134,4\n71#1:138\n72#1:139,6\n72#1:145,4\n72#1:149\n77#1:150\n77#1:151\n77#1:152\n42#1:153\n*E\n"])
public class AccountSwitcherComponent(parentScreen: Screen?) : FlowLayout(Sizing.Companion.content(), Sizing.Companion.fixed(20), Algorithm.HORIZONTAL) {
   private final val parentScreen: Screen?
   private final val head: PlayerHeadComponent
   private final val username: String

   init {
      this.parentScreen = parentScreen
      val info: CurrentSessionInfo = SessionHelper.INSTANCE.getCurrentSessionInfo()
      this.username = info.getUsername()
      this.head = PlayerHeadComponent(GameProfile(info.getProfileId(), info.getUsername()), 8, null, null, 12, null)
      this.gap(4)
      this.padding(Insets.Companion.of(0, 0, 4, 6))
      this.verticalAlignment(VerticalAlignment.CENTER)
      this.cursorStyle(CursorStyle.HAND)
      val hovered: BooleanRef = BooleanRef()
      this.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      this.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      this.surface(gg.voidrix.owolib.owo.ui.core.Surface.Companion.vanillaButton$default(Surface.Companion, null, { 
         `$hovered`.element
      }, 1, null))
      this.child(this.head as UIComponent)
      val `baseText$iv`: LabelComponent = LabelComponent(TextKt.getLiteral("Accounts") as Component)
      `baseText$iv`.shadow(true)
      this.child(`baseText$iv` as UIComponent)
      val var18: LiteralTextBuilder = LiteralTextBuilder(null, true)
      var18.getAppendTasks().add(AccountSwitcherComponent$_init_$lambda$5$$inlined$text$default$1(var18, "Hii, ", true))
      val var19: java.lang.String = this.username
      var18.getAppendTasks().add(AccountSwitcherComponent$_init_$lambda$5$$inlined$text$default$2(var18, var19, true))
      var18.getAppendTasks().add(AccountSwitcherComponent$_init_$lambda$5$$inlined$text$default$3(var18, "!", true))
      this.tooltip(var18.build() as Component)
      this.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         UISounds.playButtonSound()
         val `screen$iv`: Screen = AccountSelectScreen(`this$0`.parentScreen) as Screen
         val var10000: Minecraft = Minecraft.getInstance()
         var10000.gui.setScreen(`screen$iv`)
         true
      })
   }

   public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      super.draw(context, mouseX, mouseY, partialTicks, delta)
val hx: Double = this.head.x()
val hy: Double = this.head.y()
context.fill(hx + (double)8, hy + (double)1, hx + (double)8 + (double)1, hy + (double)8 + (double)1, -16777216)
context.fill(hx + (double)1, hy + (double)8, hx + (double)8 + (double)1, hy + (double)8 + (double)1, -16777216)
val var10000: SessionStatus = AccountSwitcherButton.INSTANCE.getOrRefreshStatus()
var var13: Double
      when (if (var10000 == null) -1 else AccountSwitcherComponent.WhenMappings.$EnumSwitchMapping$0[var10000.ordinal()]) {
         -1 -> var13 = 16.0
         0 -> throw NoWhenBranchMatchedException()
         1 -> var13 = 0.0
         2 -> var13 = 8.0
         3 -> var13 = 16.0
         else -> throw NoWhenBranchMatchedException()
      }

      context.drawTextureScaled(
         SESSION_STATUS_TEXTURE, this.x() + this.width() - (double)6, this.y() - (double)1, 8.0, 8.0, var13, 0.0, 8.0, 8.0, 24.0, 8.0, true
      )
   }

   @JvmStatic
   fun {
      val var10000: Identifier = Identifier.fromNamespaceAndPath("owo-compat", "textures/gui/session_status.png")
      SESSION_STATUS_TEXTURE = var10000
   }

   public companion object {
      private final val SESSION_STATUS_TEXTURE: Identifier
      private const val HEAD_SIZE: Int = 8
   }
}
