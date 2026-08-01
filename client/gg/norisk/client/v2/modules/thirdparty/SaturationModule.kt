package gg.norisk.client.v2.modules.thirdparty

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.loader.ReflectionHelper
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nSaturationModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SaturationModule.kt\ngg/norisk/client/v2/modules/thirdparty/SaturationModule\n+ 2 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n*L\n1#1,55:1\n63#2:56\n63#2:57\n*S KotlinDebug\n*F\n+ 1 SaturationModule.kt\ngg/norisk/client/v2/modules/thirdparty/SaturationModule\n*L\n30#1:56\n52#1:57\n*E\n"])
public object SaturationModule : ThirdPartyModule(
      "Saturation", ModuleCategory.HUD, false, "appleskin", "squeek502", "AppleSkin", "squeek.appleskin.api.handler.EventHandler"
   ) {
   public open val seoTags: Array<String>

   @Category(name = "APPLE SKIN")
   @NotNull
   public final val showTooltipInfo: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return showTooltipInfo$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val showSaturation: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return showSaturation$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val showExhaustion: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return showExhaustion$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val showHealRestored: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return showHealRestored$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   public final val showHungerRestored: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return showHungerRestored$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   public open fun createdAt(): Long {
      return 1744532285000L
   }

   public override fun setupReflectionHooks() {
      var `$this$nrcDebug$iv`: Logger = this.getLogger().getValue() as Logger
      if (MCLogger.IS_DEBUG) {
         `$this$nrcDebug$iv`.info("[SaturationModule] Setting up AppleSkin reflection hooks...")
      }

      this.registerModEvent("squeek.appleskin.api.event.TooltipOverlayEvent$Pre", "TooltipPre", { event: Any ->
         if (!INSTANCE.isEnabled() || !INSTANCE.showTooltipInfo) {
            ReflectionHelper.INSTANCE.cancelEvent(event)
         }

         Unit.INSTANCE
      })
      this.registerModEvent("squeek.appleskin.api.event.HUDOverlayEvent$Saturation", "Saturation", { event: Any ->
         if (!INSTANCE.isEnabled() || !INSTANCE.showSaturation) {
            ReflectionHelper.INSTANCE.cancelEvent(event)
         }

         Unit.INSTANCE
      })
      this.registerModEvent("squeek.appleskin.api.event.HUDOverlayEvent$Exhaustion", "Exhaustion", { event: Any ->
         if (!INSTANCE.isEnabled() || !INSTANCE.showExhaustion) {
            ReflectionHelper.INSTANCE.cancelEvent(event)
         }

         Unit.INSTANCE
      })
      this.registerModEvent("squeek.appleskin.api.event.HUDOverlayEvent$HealthRestored", "HealthRestored", { event: Any ->
         if (!INSTANCE.isEnabled() || !INSTANCE.showHealRestored) {
            ReflectionHelper.INSTANCE.cancelEvent(event)
         }

         Unit.INSTANCE
      })
      this.registerModEvent("squeek.appleskin.api.event.HUDOverlayEvent$HungerRestored", "HungerRestored", { event: Any ->
         if (!INSTANCE.isEnabled() || !INSTANCE.showHungerRestored) {
            ReflectionHelper.INSTANCE.cancelEvent(event)
         }

         Unit.INSTANCE
      })
      `$this$nrcDebug$iv` = this.getLogger().getValue() as Logger
      if (MCLogger.IS_DEBUG) {
         `$this$nrcDebug$iv`.info("[SaturationModule] AppleSkin reflection hooks setup complete.")
      }
   }
}
