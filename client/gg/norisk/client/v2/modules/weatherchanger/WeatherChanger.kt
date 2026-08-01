package gg.norisk.client.v2.modules.weatherchanger

import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder

public object WeatherChanger : Module("Weather Changer", ModuleCategory.VISUAL, false, false, false, 28) {
   public final val weather: WeatherState by ValueApiKt.enum$default(WeatherState.CLEAR, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return weather$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as WeatherState
      }


   public open val seoTags: Array<String>
}
