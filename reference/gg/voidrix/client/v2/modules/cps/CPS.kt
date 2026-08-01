package gg.voidrix.client.v2.modules.cps

import gg.voidrix.compat.event.MouseClickEventData
import gg.voidrix.compat.event.MouseEvents
import gg.voidrix.compat.text.TextKt
import gg.voidrix.ui.api.hud.SingleTextHud
import java.util.ArrayList
import net.minecraft.network.chat.Component

public object CPS : SingleTextHud(
      "CPS", "{left} | {right}", null, TextKt.getLiteral("{left} und {right} wird entsprechend replaced") as Component, false, false, false, 116
   ) {
   private final val clicksLeft: MutableList<Long> = ArrayList() as java.util.List
   private final val clicksRight: MutableList<Long> = ArrayList() as java.util.List

   private fun handleMouseButtonEvent(button: Int, action: Int) {
      var var10000: java.util.List
      when (button) {
         0 -> var10000 = clicksLeft
         1 -> var10000 = clicksRight
         else -> return
      }

      if (action == 1) {
         var10000.add(System.currentTimeMillis())
      } else {
         CollectionsKt.removeAll(var10000, { it: Long ->
            it + 1000L < System.currentTimeMillis()
         })
      }
   }

   private fun getCPS(clicks: MutableList<Long>): Int {
      CollectionsKt.removeAll(clicks, { it: Long ->
         it + 1000L < `$now`
      })
      return clicks.size()
   }

   public fun getLeftCps(): Int {
      return this.getCPS(clicksLeft)
   }

   public fun getRightCps(): Int {
      return this.getCPS(clicksRight)
   }

   public open fun getParsedText(): String {
      return StringsKt.replace(
         StringsKt.replace(this.getText(), "{left}", java.lang.String.valueOf(this.getLeftCps()), true),
         "{right}",
         java.lang.String.valueOf(this.getRightCps()),
         true
      )
   }

   @JvmStatic
   fun {
      MouseEvents.INSTANCE.getMouseClickEvent().listen({ event: MouseClickEventData ->
         INSTANCE.handleMouseButtonEvent(event.getButton(), event.getAction())
         Unit.INSTANCE
      })
   }
}
