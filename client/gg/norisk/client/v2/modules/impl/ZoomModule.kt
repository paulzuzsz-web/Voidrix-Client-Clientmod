package gg.norisk.client.v2.modules.impl

import gg.norisk.client.v2.modules.impl.ZoomAnimation.Easing
import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.event.KeyEventData
import gg.norisk.compat.event.KeyEvents
import gg.norisk.compat.event.MouseClickEventData
import gg.norisk.compat.event.MouseEvents
import gg.norisk.compat.event.MouseScrollEventData
import gg.norisk.compat.resource.MCKey
import gg.norisk.compat.resource.MCKeyType
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import java.time.Duration
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.DurationKt
import kotlin.time.DurationUnit
import net.minecraft.client.Minecraft
import org.jetbrains.annotations.NotNull
import org.joml.Vector2i
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@NrcMiniTag(tags = ["zoom"])
@SourceDebugExtension(["SMAP\nZoomModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ZoomModule.kt\ngg/norisk/client/v2/modules/impl/ZoomModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,187:1\n40#2:188\n328#2:189\n40#2:190\n*S KotlinDebug\n*F\n+ 1 ZoomModule.kt\ngg/norisk/client/v2/modules/impl/ZoomModule\n*L\n159#1:188\n61#1:189\n61#1:190\n*E\n"])
public object ZoomModule : Module("Zoom", ModuleCategory.VISUAL, false, true, false, 20) {
   @Category(name = "Keybind")
   @NotNull
   public final val zoomKey: MCKey by ValueApiKt.key$default(67, null, null, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return zoomKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MCKey
      }


   @Category(name = "Visual")
   @NotNull
   public final val enableScrolling: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return enableScrolling$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val smoothCamera: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return smoothCamera$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val smoothZoom: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return smoothZoom$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   public final val adjustMouseSensitivity: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return adjustMouseSensitivity$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   @Category(name = "Advanced")
   @NotNull
   public final val zoomInDuration: Number by ValueApiKt.numeric$default(0.1, RangesKt.rangeTo(0.0, 1.0) as ClosedRange, 0.1, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return zoomInDuration$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Number
      }


   public final val zoomOutDuration: Number by ValueApiKt.numeric$default(0.1, RangesKt.rangeTo(0.0, 1.0) as ClosedRange, 0.1, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return zoomOutDuration$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Number
      }


   public final val scrollStrength: Number by ValueApiKt.numeric$default(0.9, RangesKt.rangeTo(0.7, 0.9) as ClosedRange, 0.01, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return scrollStrength$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Number
      }


   public final val easing: Easing by ValueApiKt.enum$default(ZoomAnimation.Easing.LINEAR, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return easing$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as ZoomAnimation.Easing
      }


   public open val seoTags: Array<String>

   @JvmField
   public final var isZooming: Boolean
      private set

   @JvmField
   @NotNull
   public final var animation: ZoomAnimation
      private set

   private final var scrollMultiplier: Double = 1.0

   private fun onZoom(clicked: Boolean, released: Boolean) {
      if (this.isEnabled()) {
         if (clicked) {
            isZooming = true
            val var3: Long = DurationKt.toDuration(this.zoomInDuration.doubleValue(), DurationUnit.SECONDS)
            val var10004: Duration = Duration.ofSeconds(
               kotlin.time.Duration.getInWholeSeconds_impl/* $VF was: getInWholeSeconds-impl */(var3),
               (long)kotlin.time.Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(var3)
            )
            animation = ZoomAnimation(1.0F, 0.3F, var10004, this.easing)
            scrollMultiplier = 0.3
         } else if (released) {
            isZooming = false
            val var10002: Float = animation.get()
            val var8: Long = DurationKt.toDuration(this.zoomOutDuration.doubleValue(), DurationUnit.SECONDS)
            val var11: Duration = Duration.ofSeconds(
               kotlin.time.Duration.getInWholeSeconds_impl/* $VF was: getInWholeSeconds-impl */(var8),
               (long)kotlin.time.Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(var8)
            )
            animation = ZoomAnimation(var10002, 1.0F, var11, this.easing)
            scrollMultiplier = 1.0
         }
      }
   }

   @JvmStatic
   public fun modifyFov(fov: Double): Double {
      if (!INSTANCE.isEnabled()) {
         return fov
      } else {
         return if (INSTANCE.smoothZoom) fov * animation.get() else (if (isZooming) fov * scrollMultiplier else fov)
      }
   }

   @JvmStatic
   public fun handleHotBarScrolling(info: CallbackInfoReturnable<Vector2i>) {
      if (INSTANCE.isEnabled() && isZooming && INSTANCE.enableScrolling) {
         info.setReturnValue(Vector2i(0, 0))
         info.cancel()
      }
   }

   private fun handleMouseScrollEvent(event: MouseScrollEventData) {
      if (this.isEnabled() && isZooming && this.enableScrolling) {
         scrollMultiplier = if (event.getVertical() > 0.0)
            Math.max(0.01, scrollMultiplier * this.scrollStrength.doubleValue())
            else
            Math.min(1.0, scrollMultiplier / this.scrollStrength.doubleValue())
            animation.end = (float)scrollMultiplier
      }
   }

   @JvmStatic
   public fun handleSmoothMouse(original: Boolean): Boolean {
      return if (!INSTANCE.isEnabled()) original else original || INSTANCE.smoothCamera && isZooming
   }

   @JvmStatic
   public fun getMouseSensitivityMultiplier(): Double {
      if (INSTANCE.isEnabled() && INSTANCE.adjustMouseSensitivity) {
         val var10000: Minecraft = Minecraft.getInstance()
         if (!var10000.options.getCameraType().isFirstPerson()) {
            return 1.0
         } else {
            val var2: Double = if (INSTANCE.smoothZoom) animation.get() else scrollMultiplier
            return if (var2 >= 0.3) 1.0 else var2 / 0.3
         }
      } else {
         return 1.0
      }
   }

   public open fun onDisable() {
      isZooming = false
      val var10002: Float = animation.get()
      val var1: Long = DurationKt.toDuration(this.zoomOutDuration.doubleValue(), DurationUnit.SECONDS)
      val var10004: Duration = Duration.ofSeconds(
         kotlin.time.Duration.getInWholeSeconds_impl/* $VF was: getInWholeSeconds-impl */(var1),
         (long)kotlin.time.Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(var1)
      )
      animation = ZoomAnimation(var10002, 1.0F, var10004, this.easing)
      scrollMultiplier = 1.0
   }

   @JvmStatic
   fun {
      val var6: Long = DurationKt.toDuration(5, DurationUnit.SECONDS)
      val var10004: Duration = Duration.ofSeconds(
         kotlin.time.Duration.getInWholeSeconds_impl/* $VF was: getInWholeSeconds-impl */(var6),
         (long)kotlin.time.Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(var6)
      )
      animation = ZoomAnimation(1.0F, 1.0F, var10004)
      INSTANCE.setEnabled(true)
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_0@{ event: KeyEventData ->
         val bound: MCKey = INSTANCE.zoomKey
         if (bound.isUnknown()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            if (bound.getType() === MCKeyType.KEYBOARD && bound.getCode() == event.getKey()) {
               val var10000: Minecraft = Minecraft.getInstance()
               if (var10000.gui.screen() == null) {
                  INSTANCE.onZoom(event.isClicked(), event.isReleased())
               } else if (event.isReleased()) {
                  INSTANCE.onZoom(false, true)
               }
            }

            return@lambda_0 Unit.INSTANCE
         }
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_1@{ event: MouseClickEventData ->
         val bound: MCKey = INSTANCE.zoomKey
         if (bound.isUnknown()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            if (bound.getType() === MCKeyType.MOUSE && bound.getCode() == event.getButton()) {
               INSTANCE.onZoom(event.getAction() == 1, event.getAction() == 0)
            }

            return@lambda_1 Unit.INSTANCE
         }
      })
      MouseEvents.INSTANCE.getMouseScrollEvent().listen({ it: MouseScrollEventData ->
         INSTANCE.handleMouseScrollEvent(it)
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE
         .getDisconnectEvent()
         .listen(
            { it: Unit ->
               if (INSTANCE.isEnabled()) {
                  isZooming = false
                  val var10002: Float = animation.get()
                  val var1: Long = DurationKt.toDuration(INSTANCE.zoomOutDuration.doubleValue(), DurationUnit.SECONDS)
                  val var10004: Duration = Duration.ofSeconds(
                     kotlin.time.Duration.getInWholeSeconds_impl/* $VF was: getInWholeSeconds-impl */(var1),
                     (long)kotlin.time.Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(var1)
                  )
                  animation = ZoomAnimation(var10002, 1.0F, var10004, INSTANCE.easing)
                  scrollMultiplier = 1.0
               }

               Unit.INSTANCE
            }
         )
      }
}
