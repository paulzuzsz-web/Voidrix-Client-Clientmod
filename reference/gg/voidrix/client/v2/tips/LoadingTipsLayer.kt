package gg.voidrix.client.v2.tips

import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.component.UIComponents
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.core.Color
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.layers.Layers
import gg.voidrix.owolib.owo.ui.layers.Layer.Instance
import java.io.File
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.LevelLoadingScreen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nLoadingTipsLayer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LoadingTipsLayer.kt\ngg/voidrix/client/v2/tips/LoadingTipsLayer\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,98:1\n378#2:99\n40#2:100\n225#2:105\n40#2:106\n66#3:101\n66#3:102\n66#3:103\n66#3:104\n*S KotlinDebug\n*F\n+ 1 LoadingTipsLayer.kt\ngg/voidrix/client/v2/tips/LoadingTipsLayer\n*L\n90#1:99\n90#1:100\n79#1:105\n79#1:106\n93#1:101\n94#1:102\n76#1:103\n77#1:104\n*E\n"])
public object LoadingTipsLayer {
   private final var registered: Boolean

   @JvmStatic
   public fun init() {
      if (!registered) {
         Layers.add({ h: Sizing, v: Sizing ->
            UIContainers.verticalFlow(h, v)
         }, { inst: Instance ->
            INSTANCE.installTipsContent(inst)
         }, arrayOf(LevelLoadingScreen::class.java))
         registered = true
      }
   }

   private fun installTipsContent(inst: Instance) {
      val root: FlowLayout = inst.getAdapter().rootComponent as FlowLayout
      root.padding(Insets.Companion.of(0))
      val titleLabel: LabelComponent = UIComponents.label("").shadow(true).color(Color.Companion.ofRgb(16769132))
      val tipLabel: LabelComponent = UIComponents.label("").shadow(true).color(Color.Companion.ofRgb(16777215))
      val var6: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var6.gap(2)
      var6.allowOverflow(true)
      var6.child(titleLabel as UIComponent)
      var6.child(tipLabel as UIComponent)
      root.child(var6 as UIComponent)
      inst.setAggressivePositioning(true)
      inst.alignComponentDynamic(
         var6 as UIComponent,
         { 
            if (!INSTANCE.tryApplyTipsToLabels(`$titleLabel`, `$tipLabel`)) {
               var var10001: MutableComponent = Component.literal("")
               `$titleLabel`.text(var10001 as Component)
               var10001 = Component.literal("")
               `$tipLabel`.text(var10001 as Component)
            }

            val var10000: Minecraft = Minecraft.getInstance()
            Positioning.Companion
               .absolute(6.0, RangesKt.coerceAtLeast((double)(var10000.getWindow().getGuiScaledHeight() - 15) - `$col`.fullSize().height() + (double)8, 0.0))
            }
      )
   }

   private fun tryApplyTipsToLabels(titleLabel: LabelComponent, tipLabel: LabelComponent): Boolean {
      if (!VoidrixTipsTranslationLoader.isTipsFeatureActive()) {
         return false
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val var10: File = var10000.gameDirectory
         val var11: LoadingTipRenderStrings = VoidrixLoadingTipsPresenter.updateAndGetStringsForRender(var10, System.currentTimeMillis())
         if (var11 == null) {
            return false
         } else {
            var var10001: MutableComponent = Component.literal(var11.title)
            titleLabel.text(var10001 as Component)
            var10001 = Component.literal(var11.tipLine)
            tipLabel.text(var10001 as Component)
            return true
         }
      }
   }
}
