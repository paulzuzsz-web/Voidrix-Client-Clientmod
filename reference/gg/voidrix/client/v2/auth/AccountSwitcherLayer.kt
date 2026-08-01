package gg.voidrix.client.v2.auth

import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.layers.Layers
import gg.voidrix.owolib.owo.ui.layers.Layer.Instance
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen

public object AccountSwitcherLayer {
   @JvmStatic
   public fun init() {
      Layers.add({ hSizing: Sizing, vSizing: Sizing ->
         UIContainers.verticalFlow(hSizing, vSizing)
      }, { instance: Instance ->
         val component: AccountSwitcherComponent = AccountSwitcherComponent(instance.getScreen())
         component.positioning(Positioning.Companion.absolute(5, 5))
         (instance.getAdapter().rootComponent as FlowLayout).child(component as UIComponent)
      }, arrayOf(JoinMultiplayerScreen::class.java))
   }
}
