package gg.voidrix.client.v2.modules.actionbar

import gg.voidrix.client.v2.mixin.actionbar.ActionBarHudAccessor
import gg.voidrix.compat.framebuffer.FramebufferManager
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.core.Sizing.Companion
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.AbstractHud
import gg.voidrix.ui.api.hud.AnchorPoint
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.IContentBackground
import gg.voidrix.ui.api.hud.IDynamicBackground
import gg.voidrix.ui.api.hud.IDynamicBackgroundKt
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixBackgroundPicker
import gg.voidrix.ui.modules.IModuleScreen
import gg.voidrix.ui.v2.hud.AnchorPointPosition
import gg.voidrix.ui.v2.hud.Background
import gg.voidrix.ui.v2.hud.Background.Type
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nActionBar.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ActionBar.kt\ngg/voidrix/client/v2/modules/actionbar/ActionBar\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,408:1\n40#2:409\n328#2:410\n40#2:411\n40#2:412\n*S KotlinDebug\n*F\n+ 1 ActionBar.kt\ngg/voidrix/client/v2/modules/actionbar/ActionBar\n*L\n80#1:409\n107#1:410\n107#1:411\n113#1:412\n*E\n"])
public object ActionBar : AbstractHud("Action Bar", false, false, false, 10), IContentBackground {
   public open var background: Background by ValueApiKt.attribute$default({ 
      Background(Type.BLANK, null, 0.0F, 0.0F, 0.0F, null, 62, null)
   }, null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      VoidrixBackgroundPicker(INSTANCE.background, null) as UIComponent
   }, 14, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public open get() {
         return background$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as Background
      }

      public open set(<set-?>) {
         background$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var actionX: Double?
      internal set

   public final var actionY: Double?
      internal set

   public final val dummyMessage: Component = TextKt.getLiteral("Hello World!") as Component

   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.BOTTOM_CENTER, 0.5, 0.475, null, 8, null)
         }
      }


   public open fun useGuiScale(): Boolean {
      return true
   }

   @JvmStatic
   public fun wrapOverlayMessage(gui: Any, original: Component?): Component? {
      if (!INSTANCE.isInModuleScreen()) {
         return original
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val var4: Hud = var10000.gui.hud
         return if ((var4 as ActionBarHudAccessor).getOverlayRemaining() > 0 && original != null) original else dummyMessage
      }
   }

   @JvmStatic
   public fun wrapRemainTicks(gui: Any, original: Int): Int {
      if (!INSTANCE.isInModuleScreen()) {
         return original
      } else {
         return if (original > 0) original else 20
      }
   }

   public fun isInModuleScreen(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      return var10000.gui.screen() is IModuleScreen && this.isEnabled()
   }

   public open fun shouldStopRenderExecution(): Boolean {
      if (this.isInModuleScreen()) {
         return false
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val var3: Hud = var10000.gui.hud
         return (var3 as ActionBarHudAccessor).getOverlayMessage() == null || (var3 as ActionBarHudAccessor).getOverlayRemaining() <= 0
      }
   }

   public open fun hudComponent(): UIComponent {
      val wrapper: FlowLayout = UIContainers.horizontalFlow(
         Sizing.Companion.content(this.dynamicBackground.getDynamicWidth()), Sizing.Companion.content(this.dynamicBackground.getDynamicHeight())
      )
      wrapper.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      wrapper.surface(this.background.toSurface())
      wrapper.allowOverflow(true)
      wrapper.child(ActionBar.ActionBarComponent(wrapper, null, null, 6, null) as UIComponent)
      return wrapper as UIComponent
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @SourceDebugExtension(["SMAP\nActionBar.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ActionBar.kt\ngg/voidrix/client/v2/modules/actionbar/ActionBar$ActionBarComponent\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,408:1\n40#2:409\n40#2:410\n40#2:411\n40#2:412\n40#2:413\n*S KotlinDebug\n*F\n+ 1 ActionBar.kt\ngg/voidrix/client/v2/modules/actionbar/ActionBar$ActionBarComponent\n*L\n156#1:409\n159#1:410\n163#1:411\n221#1:412\n238#1:413\n*E\n"])
   public class ActionBarComponent(wrapper: FlowLayout,
      horizontalSizing: Sizing = Sizing.Companion.fixed(250),
      verticalSizing: Sizing = Sizing.Companion.fixed(20)
   ) : FlowLayout(horizontalSizing, verticalSizing, Algorithm.VERTICAL) {
      public final val wrapper: FlowLayout
      private final var lastTextWidth: Int
      private final var lastVisible: Boolean

      init {
         this.wrapper = wrapper
         this.lastTextWidth = -1
         this.allowOverflow(true)
         this.child(UIContainers.verticalFlow(Sizing.Companion.fixed(5), Sizing.Companion.fixed(5)) as UIComponent)
         this.setWidthAndHeight()
      }

      public fun setWidthAndHeight() {
         var var10000: Minecraft = Minecraft.getInstance()
         val var9: Hud = var10000.gui.hud
         val var7: Component = if (ActionBar.wrapRemainTicks(var9 as ActionBarHudAccessor, (var9 as ActionBarHudAccessor).getOverlayRemaining()) > 0)
            ActionBar.wrapOverlayMessage(var9 as ActionBarHudAccessor, (var9 as ActionBarHudAccessor).getOverlayMessage())
            else
            null
            val var11: Int
         if (var7 != null) {
            var10000 = Minecraft.getInstance()
            var11 = var10000.font.width(var7 as FormattedText)
         } else {
            var11 = 0
         }

         val var8: Boolean = var7 != null
         if (var7 != null) {
            this.horizontalSizing(Sizing.Companion.fixed(var11))
            val var10001: Companion = Sizing.Companion
            val var10002: Minecraft = Minecraft.getInstance()
            this.verticalSizing(var10001.fixed(var10002.font.lineHeight))
         }

         if (var11 != this.lastTextWidth || var8 != this.lastVisible) {
            this.lastTextWidth = var11
            this.lastVisible = var8
            FramebufferManager.INSTANCE.forceUpdate()
         }
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         this.setWidthAndHeight()
         val var10000: Minecraft = Minecraft.getInstance()
         val var11: Hud = var10000.gui.hud
         val accessor: ActionBarHudAccessor = var11 as ActionBarHudAccessor
         if (ActionBar.wrapOverlayMessage(var11 as ActionBarHudAccessor, (var11 as ActionBarHudAccessor).getOverlayMessage()) != null
            && ActionBar.wrapRemainTicks(var11 as ActionBarHudAccessor, (var11 as ActionBarHudAccessor).getOverlayRemaining()) > 0) {
            this.wrapper.surface(ActionBar.INSTANCE.background.toSurface())
            ActionBar.INSTANCE.actionX = this.x() + this.width() / (double)2
            ActionBar.INSTANCE.actionY = this.y() + this.height() / (double)2
            val var10001: GuiGraphicsExtractor = context as GuiGraphicsExtractor
            val var10002: Minecraft = Minecraft.getInstance()
            accessor.invokeRenderOverlayMessage(var10001, var10002.getDeltaTracker())
            ActionBar.INSTANCE.actionX = null
            ActionBar.INSTANCE.actionY = null
            super.draw(context, mouseX, mouseY, partialTicks, delta)
         } else {
            this.wrapper.surface(Surface.BLANK)
            ActionBar.INSTANCE.actionX = null
            ActionBar.INSTANCE.actionY = null
            super.draw(context, mouseX, mouseY, partialTicks, delta)
         }
      }
   }
}
