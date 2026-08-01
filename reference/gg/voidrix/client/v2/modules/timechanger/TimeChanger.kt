package gg.voidrix.client.v2.modules.timechanger

import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder

public object TimeChanger : Module("Time Changer", ModuleCategory.VISUAL, false, false, false, 28) {
   public final val time: Number by ValueApiKt.numeric$default(6000, IntRange(0, 24000) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return time$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }


   public open val seoTags: Array<String>
}
