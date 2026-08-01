package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.compat.event.FogEvents
import gg.norisk.compat.event.FogModifyEvent
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.NumericValue
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import org.jetbrains.annotations.NotNull

@NrcMiniTag(tags = ["nofog"])
public object NoFogModule : Module("NoFog", ModuleCategory.VISUAL, false, true, false, 20) {
   @Category(name = "Fog Types")
   @NotNull
   public final val disableLavaFog: Boolean by ValueApiKt.boolean$default(true, "Lava", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return disableLavaFog$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val disableWaterFog: Boolean by ValueApiKt.boolean$default(true, "Water", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return disableWaterFog$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val disablePowderSnow: Boolean by ValueApiKt.boolean$default(true, "Powder Snow", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return disablePowderSnow$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val disableTerrainFog: Boolean by ValueApiKt.boolean$default(true, "Terrain", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return disableTerrainFog$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   public final val disableDimensionOrBoss: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return disableDimensionOrBoss$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   @Category(name = "Distance")
   @NotNull
   public final val fogEndDistance: Number
      public final get() {
         return fogEndDistance$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Number
      }


   public final val fogMultiplier: Number
      public final get() {
         return fogMultiplier$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Number
      }


   @JvmStatic
   fun {
      var var3: NumericValue = ValueApiKt.numeric$default(360.0, RangesKt.rangeTo(0.0, 512.0) as ClosedRange, 4.0, "Fog End", null, null, 48, null)
      var3.setUiCondition({ 
         false
      })
      fogEndDistance$delegate = var3.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
      var3 = ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.01, 5.0) as ClosedRange, 0.01, null, null, null, 56, null)
      var3.setUiCondition({ 
         true
      })
      fogMultiplier$delegate = var3.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
      FogEvents.INSTANCE.getFogModifyEvent().listen(lambda_4@{ event: FogModifyEvent ->
         if (INSTANCE.isEnabled() && !event.isMobEffect()) {
            var var10000: Boolean
            when (NoFogModule.WhenMappings.$EnumSwitchMapping$0[event.getFogType().ordinal()]) {
               1 -> var10000 = INSTANCE.disableLavaFog
               2 -> var10000 = INSTANCE.disableWaterFog
               3 -> var10000 = INSTANCE.disablePowderSnow
               4, 5, 6 -> var10000 = INSTANCE.disableTerrainFog
               7 -> var10000 = INSTANCE.disableDimensionOrBoss
               else -> var10000 = false
            }

            if (!var10000) {
               return@lambda_4 Unit.INSTANCE
            } else {
               val target: Float = INSTANCE.fogEndDistance.floatValue()
               if (event.getFogEnd() < target) {
                  event.setFogEnd(target)
               }

               var var4: Float
               when (NoFogModule.WhenMappings.$EnumSwitchMapping$0[event.getFogType().ordinal()]) {
                  1 -> var4 = 100.0F
                  2 -> var4 = 5.0F
                  3 -> var4 = 3.0F
                  4, 5, 6 -> var4 = 10.0F
                  7 -> var4 = 10.0F
                  else -> var4 = 1.0F
               }

               event.setMultiplier(var4 * INSTANCE.fogMultiplier.floatValue())
               return@lambda_4 Unit.INSTANCE
            }
         } else {
            return@lambda_4 Unit.INSTANCE
         }
      })
   }
}
