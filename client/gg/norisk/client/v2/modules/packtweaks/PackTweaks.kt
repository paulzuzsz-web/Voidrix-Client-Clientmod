package gg.norisk.client.v2.modules.packtweaks

import gg.norisk.compat.event.PumpkinOverlayRenderEvent
import gg.norisk.compat.event.RenderEvents
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder

public object PackTweaks : Module("Pack Tweaks", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   private final val hidePumpkinOverlay: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         private final get() {
         return hidePumpkinOverlay$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val noVignette: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return noVignette$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public open val seoTags: Array<String>

   @JvmStatic
   fun {
      INSTANCE.setEnabled(true)
      RenderEvents.INSTANCE.getPumpkinOverlayRenderEvent().subscribe({ it: PumpkinOverlayRenderEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.hidePumpkinOverlay) {
            it.setCancelled(true)
         }

         Unit.INSTANCE
      })
   }
}
