package gg.voidrix.client.v2.modules.weatherchanger

import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder

public object WeatherChanger : Module("Weather Changer", ModuleCategory.VISUAL, false, false, false, 28) {
   public final val weather: WeatherState by ValueApiKt.enum$default(WeatherState.CLEAR, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return weather$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as WeatherState
      }


   public open val seoTags: Array<String>
}
