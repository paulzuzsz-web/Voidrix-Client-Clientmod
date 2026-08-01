package gg.voidrix.client.v2.serverswitcher

import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.layers.Layers
import gg.voidrix.owolib.owo.ui.layers.Layer.Instance
import net.minecraft.client.gui.screens.PauseScreen

public object ServerSwitcherLayer {
   @JvmStatic
   public fun init() {
      Layers.add({ hSizing: Sizing, vSizing: Sizing ->
         UIContainers.verticalFlow(hSizing, vSizing)
      }, { instance: Instance ->
         val globe: ServerSwitcherComponent = ServerSwitcherComponent(instance.getScreen())
         globe.positioning(Positioning.Companion.relative(100, 0))
         globe.margins(Insets.Companion.of(5, 0, 0, 5))
         (instance.getAdapter().rootComponent as FlowLayout).child(globe as UIComponent)
      }, arrayOf(PauseScreen::class.java))
   }
}
