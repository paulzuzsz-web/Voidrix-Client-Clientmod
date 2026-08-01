package gg.norisk.client.v2.serverswitcher

import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.Positioning
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.layers.Layers
import gg.norisk.owolib.owo.ui.layers.Layer.Instance
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
