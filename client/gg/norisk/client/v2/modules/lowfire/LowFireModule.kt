package gg.norisk.client.v2.modules.lowfire

import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder

@NrcMiniTag(tags = ["lowfire"])
public object LowFireModule : Module("Low Fire", ModuleCategory.VISUAL, false, true, false, 20) {
   public final val lowFireHeight: Number by ValueApiKt.numeric$default(-3, IntRange(-5, 0) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return lowFireHeight$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }


   public open val seoTags: Array<String>
}
