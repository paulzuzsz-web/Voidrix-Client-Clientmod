package gg.voidrix.client.v2.serverswitcher

import gg.voidrix.owolib.owo.ui.component.TextureComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.CursorStyle
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.util.UISounds
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nServerSwitcherComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ServerSwitcherComponent.kt\ngg/voidrix/client/v2/serverswitcher/ServerSwitcherComponent\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 4 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n*L\n1#1,68:1\n67#2:69\n355#3:70\n40#3:71\n356#3:72\n21#4:73\n*S KotlinDebug\n*F\n+ 1 ServerSwitcherComponent.kt\ngg/voidrix/client/v2/serverswitcher/ServerSwitcherComponent\n*L\n55#1:69\n60#1:70\n60#1:71\n60#1:72\n38#1:73\n*E\n"])
public class ServerSwitcherComponent(parentScreen: Screen) : FlowLayout(Sizing.Companion.fixed(20), Sizing.Companion.fixed(20), Algorithm.HORIZONTAL) {
   private final val parentScreen: Screen

   init {
      this.parentScreen = parentScreen
      this.horizontalAlignment(HorizontalAlignment.CENTER)
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
      this.child(TextureComponent(GLOBE, 0, 0, 20, 20, 20, 20).blend(true).sizing(Sizing.Companion.fixed(16)))
      val `args$iv`: Array<Any> = arrayOfNulls(0)
      val var10001: MutableComponent = Component.translatable("voidrix.ui.pause.serverSwitcher", Arrays.copyOf(`args$iv`, `args$iv`.length))
      this.tooltip(var10001 as Component)
      this.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         UISounds.playButtonSound()
         val `screen$iv`: Screen = JoinMultiplayerScreen(`this$0`.parentScreen) as Screen
         val var10000: Minecraft = Minecraft.getInstance()
         var10000.gui.setScreen(`screen$iv`)
         true
      })
   }

   @JvmStatic
   fun {
      val var10000: Identifier = Identifier.fromNamespaceAndPath("voidrix-ui", "textures/icon/globe.png")
      GLOBE = var10000
   }

   public companion object {
      private final val GLOBE: Identifier
      private const val TEX: Int = 20
      private const val ICON: Int = 16
   }
}
