package gg.norisk.client.v2.modules.titles

import gg.norisk.client.v2.mixin.titles.TitleHudAccessor
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.AbstractHud
import gg.norisk.ui.api.hud.AnchorPoint
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.components.nrc.NrcBackgroundPicker
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.v2.hud.AnchorPointPosition
import gg.norisk.ui.v2.hud.Background
import gg.norisk.ui.v2.hud.Background.Type
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nTitles.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Titles.kt\ngg/norisk/client/v2/modules/titles/Titles\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,300:1\n328#2:301\n40#2:302\n40#2:303\n*S KotlinDebug\n*F\n+ 1 Titles.kt\ngg/norisk/client/v2/modules/titles/Titles\n*L\n75#1:301\n75#1:302\n81#1:303\n*E\n"])
public object Titles : AbstractHud("Titles", false, false, false, 10) {
   @Category(name = "Display")
   @NotNull
   public final val showInHudEditor: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return showInHudEditor$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public open var background: Background by ValueApiKt.attribute$default({ 
      Background(Type.BLANK, null, 0.0F, 0.0F, 0.0F, null, 62, null)
   }, null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      NrcBackgroundPicker(INSTANCE.background, null) as UIComponent
   }, 14, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public open get() {
         return background$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as Background
      }

      public open set(<set-?>) {
         background$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var titlesX: Double?
      internal set

   public final var titlesY: Double?
      internal set

   public final val dummyTitle: Component = TextKt.getLiteral("Hello World!") as Component
   public final val dummySubtitle: Component = TextKt.getLiteral("Move Me Around") as Component

   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.MIDDLE_CENTER, 0.5, 0.5, null, 8, null)
         }
      }


   public open fun useGuiScale(): Boolean {
      return true
   }

   @JvmStatic
   public fun wrapTitle(gui: Any, original: Component?): Component? {
      return if (INSTANCE.isInModuleScreen() && original == null) dummyTitle else original
   }

   @JvmStatic
   public fun wrapSubtitle(gui: Any, original: Component?): Component? {
      return if (INSTANCE.isInModuleScreen() && original == null) dummySubtitle else original
   }

   @JvmStatic
   public fun wrapRemainTicks(gui: Any, original: Int): Int {
      return if (INSTANCE.isInModuleScreen() && original == 0) 20 else original
   }

   public fun isInModuleScreen(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      return var10000.gui.screen() is IModuleScreen && this.isEnabled()
   }

   public open fun shouldStopRenderExecution(): Boolean {
      if (this.isInModuleScreen()) {
         return !this.showInHudEditor
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val var4: Hud = var10000.gui.hud
         return (var4 as TitleHudAccessor).getTitle() == null || (var4 as TitleHudAccessor).getTitleTime() <= 0
      }
   }

   public open fun hudComponent(): UIComponent? {
      val wrapper: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      wrapper.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      wrapper.allowOverflow(true)
      if (this.isInModuleScreen() && !this.showInHudEditor) {
         return null
      } else {
         wrapper.child(Titles.TitlesComponent(wrapper, null, null, 6, null) as UIComponent)
         return wrapper as UIComponent
      }
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @SourceDebugExtension(["SMAP\nTitles.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Titles.kt\ngg/norisk/client/v2/modules/titles/Titles$TitlesComponent\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,300:1\n40#2:301\n40#2:302\n*S KotlinDebug\n*F\n+ 1 Titles.kt\ngg/norisk/client/v2/modules/titles/Titles$TitlesComponent\n*L\n125#1:301\n126#1:302\n*E\n"])
   public class TitlesComponent(wrapper: FlowLayout,
      horizontalSizing: Sizing = Sizing.Companion.fixed(250),
      verticalSizing: Sizing = Sizing.Companion.fixed(100)
   ) : FlowLayout(horizontalSizing, verticalSizing, Algorithm.VERTICAL) {
      public final val wrapper: FlowLayout

      init {
         this.wrapper = wrapper
         this.allowOverflow(true)
         this.child(UIContainers.verticalFlow(Sizing.Companion.fixed(5), Sizing.Companion.fixed(5)) as UIComponent)
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         Titles.INSTANCE.titlesX = this.x() + this.width() / (double)2
         Titles.INSTANCE.titlesY = this.y() + this.height() / (double)2
         val var10000: Minecraft = Minecraft.getInstance()
         val var8: Hud = var10000.gui.hud
         val var9: TitleHudAccessor = var8 as TitleHudAccessor
         val var10001: GuiGraphicsExtractor = context as GuiGraphicsExtractor
         val var10002: Minecraft = Minecraft.getInstance()
         var9.invokeRenderTitle(var10001, var10002.getDeltaTracker())
         Titles.INSTANCE.titlesX = null
         Titles.INSTANCE.titlesY = null
         super.draw(context, mouseX, mouseY, partialTicks, delta)
      }
   }
}
