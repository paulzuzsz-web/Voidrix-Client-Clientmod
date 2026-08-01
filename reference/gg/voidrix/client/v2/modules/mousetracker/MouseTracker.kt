package gg.voidrix.client.v2.modules.mousetracker

import gg.voidrix.client.v2.modules.cps.CPS
import gg.voidrix.compat.event.MouseEvents
import gg.voidrix.compat.event.MouseMoveEventData
import gg.voidrix.compat.framebuffer.FramebufferManager
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.AbstractHud
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.IContentBackground
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.api.value.NumericValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixMultiColorPicker
import gg.voidrix.ui.v2.toast.components.VoidrixToastComponent.Builder
import java.util.Arrays
import kotlin.enums.EnumEntries
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.DurationKt
import kotlin.time.DurationUnit
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nMouseTracker.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MouseTracker.kt\ngg/voidrix/client/v2/modules/mousetracker/MouseTracker\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 4 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 5 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n*L\n1#1,317:1\n185#2:318\n40#2:319\n8#3,4:320\n67#4:324\n67#4:336\n90#5,6:325\n72#5,4:331\n99#5:335\n*S KotlinDebug\n*F\n+ 1 MouseTracker.kt\ngg/voidrix/client/v2/modules/mousetracker/MouseTracker\n*L\n106#1:318\n106#1:319\n108#1:320,4\n108#1:324\n109#1:336\n108#1:325,6\n108#1:331,4\n108#1:335\n*E\n"])
public object MouseTracker : AbstractHud("MouseTracker", false, false, false, 14), IContentBackground {
   public open val seoTags: Array<String>

   public open var dynamicBackground: DynamicBackground = DynamicBackground(false, 0, 0, 0, 0, 31, null)
      internal final set

   private final var dotX: Float
   private final var dotY: Float
   private final val trail: ArrayDeque<Pair<Float, Float>> = ArrayDeque()
   private final var pendingDx: Double
   private final var pendingDy: Double
   private final var lastForceUpdateMs: Long
   private final var currentDotColor: Int = -1

   @Category(name = "Display")
   @NotNull
   public final var multiColor: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(null, false, false, 7, null)
   }, MultiColor.Companion.serializer(), "Dot Color", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      VoidrixMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public final val shape: gg.voidrix.client.v2.modules.mousetracker.MouseTracker.Shape by ValueApiKt.enum$default(
         MouseTracker.Shape.CIRCLE, "Shape", null, null, { it: MouseTracker.Shape ->
            INSTANCE.resetDot()
            Unit.INSTANCE
         }, 12, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return shape$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MouseTracker.Shape
      }


   @Category(name = "Display")
   @NotNull
   public final val dotSize: Number by ValueApiKt.numeric$default(4.0, RangesKt.rangeTo(1.0, 12.0) as ClosedRange, 0.5, "Dot Size", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return dotSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Number
      }


   @Category(name = "Display")
   @NotNull
   public final val showCrosshair: Boolean by ValueApiKt.boolean$default(true, "Show Center Cross", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return showCrosshair$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   @Category(name = "Display")
   @NotNull
   public final val showTrail: Boolean by ValueApiKt.boolean$default(true, "Show Trail", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return showTrail$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   @Category(name = "Display")
   @NotNull
   public final val showCps: Boolean by ValueApiKt.boolean$default(false, "Show CPS", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return showCps$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }


   @Category(name = "Display")
   @NotNull
   public final val cpsGap: Number
      public final get() {
         return cpsGap$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val sensitivity: Number by ValueApiKt.numeric$default(
         0.3, RangesKt.rangeTo(0.01, 3.0) as ClosedRange, 0.01, "Sensitivity", null, null, 48, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return sensitivity$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val smoothing: Number by ValueApiKt.numeric$default(15, IntRange(0, 99) as ClosedRange, 1, "Decay Speed (% / s)", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return smoothing$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val trailLength: Number by ValueApiKt.numeric$default(25, IntRange(2, 80) as ClosedRange, 1, "Trail Length", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
         public final get() {
         return trailLength$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val updateIntervalMs: Number by ValueApiKt.numeric$default(10, IntRange(0, 25) as ClosedRange, 1, "Update Interval (ms)", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
         public final get() {
         return updateIntervalMs$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as java.lang.Number
      }


   private const val BASE_SIZE: Int = 60
   private const val CPS_BOX_HEIGHT: Int = 14
   private final var lastPhysicsFrameNanos: Long

   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   private fun resetDot() {
      dotX = 0.0F
      dotY = 0.0F
      pendingDx = 0.0
      pendingDy = 0.0
      trail.clear()
   }

   public open fun onEnable() {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player != null) {
         val var20: Builder = Builder(null, null, 3, null)
         val `$i$f$translatableText`: LiteralTextBuilder = LiteralTextBuilder(null, true)
         val `inheritStyle$iv`: Array<Any> = arrayOfNulls(0)
         val var21: MutableComponent = Component.translatable(
            "voidrix.ui.modules.mousetracker.toast.title", Arrays.copyOf(`inheritStyle$iv`, `inheritStyle$iv`.length)
         )
         `$i$f$translatableText`.getAppendTasks().add(MouseTracker$onEnable$lambda$7$$inlined$text$default$1(`$i$f$translatableText`, var21, true))
         val var22: Builder = var20.title(`$i$f$translatableText`.build() as Component)
         val `args$ivx`: Array<Any> = arrayOfNulls(0)
         val var10001: MutableComponent = Component.translatable("voidrix.ui.modules.mousetracker.toast.warning", Arrays.copyOf(`args$ivx`, `args$ivx`.length))
         var22.description(var10001 as Component)
            .duration_LRDsOJo/* $VF was: duration-LRDsOJo */(DurationKt.toDuration(5, DurationUnit.SECONDS))
            .build()
            .show()
         }
   }

   public open fun hudComponent(): UIComponent {
      return MouseTracker.TrackerComponent() as UIComponent
   }

   public open fun createdAt(): Long {
      return 1745452800000L
   }

   private fun stepPhysicsIfNeeded() {
      val now: Long = System.nanoTime()
      if (now != lastPhysicsFrameNanos) {
         val dtSec: Float = if (lastPhysicsFrameNanos == 0L) 0.0F else RangesKt.coerceIn((float)((double)(now - lastPhysicsFrameNanos) / 1.0E9), 0.0F, 0.25F)
         lastPhysicsFrameNanos = now
         val sens: Float = this.sensitivity.floatValue()
         dotX = dotX + (float)(pendingDx * sens)
         dotY = dotY + (float)(pendingDy * sens)
         pendingDx = 0.0
         pendingDy = 0.0
         val maxOffset: Float = RangesKt.coerceAtLeast(30.0F - this.dotSize.floatValue() / 2.0F - 1.0F, 0.0F)
         if (this.shape === MouseTracker.Shape.CIRCLE) {
            val max: Float = (float)Math.sqrt((double)(dotX * dotX + dotY * dotY))
            if (max > maxOffset && max > 0.0F) {
               dotX = dotX / max * maxOffset
               dotY = dotY / max * maxOffset
            }
         } else {
            dotX = RangesKt.coerceIn(dotX, -maxOffset, maxOffset)
            dotY = RangesKt.coerceIn(dotY, -maxOffset, maxOffset)
         }

         if (dtSec > 0.0F) {
            val frameKeep: Float = (float)Math.pow((double)RangesKt.coerceIn(this.smoothing.floatValue() / 100.0F, 0.0F, 0.99F), (double)dtSec)
            dotX *= frameKeep
            dotY *= frameKeep
         }

         if (this.showTrail) {
            trail.addLast(TuplesKt.to(dotX, dotY))
            val var10: Int = this.trailLength.intValue()

            while (trail.size() > var10) {
               trail.removeFirst()
            }
         } else if (!(trail as java.util.Collection).isEmpty()) {
            trail.clear()
         }

         currentDotColor = this.multiColor.getChromaOrDefault().getRGB()
      }
   }

   @JvmStatic
   fun {
      val var4: NumericValue = ValueApiKt.numeric$default(2, IntRange(0, 15) as ClosedRange, 1, "CPS Box Gap", null, null, 48, null)
      var4.setUiCondition({ 
         INSTANCE.showCps
      })
      cpsGap$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
      MouseEvents.INSTANCE.getMouseMoveEvent().listen(lambda_5@{ event: MouseMoveEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_5 Unit.INSTANCE
         } else {
            pendingDx = pendingDx + event.getDx()
            pendingDy = pendingDy + event.getDy()
            return@lambda_5 Unit.INSTANCE
         }
      })
   }

   @SourceDebugExtension(["SMAP\nMouseTracker.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MouseTracker.kt\ngg/voidrix/client/v2/modules/mousetracker/MouseTracker$ArenaComponent\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,317:1\n1740#2,3:318\n*S KotlinDebug\n*F\n+ 1 MouseTracker.kt\ngg/voidrix/client/v2/modules/mousetracker/MouseTracker$ArenaComponent\n*L\n233#1:318,3\n*E\n"])
   internal class ArenaComponent : FlowLayout(Sizing.Companion.fixed(60), Sizing.Companion.fixed(60), Algorithm.VERTICAL) {
      init {
         this.allowOverflow(true)
         this.surface(MouseTracker.INSTANCE.getBackground().toSurface())
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         var eps: Float
         var var45: Boolean
         run label112@{
            this.surface(MouseTracker.INSTANCE.getBackground().toSurface())
            super.draw(context, mouseX, mouseY, partialTicks, delta)
            eps = 0.1F
            if (MouseTracker.INSTANCE.showTrail) {
               val atRest: java.lang.Iterable = MouseTracker.trail as java.lang.Iterable
               if (atRest is java.util.Collection && (atRest as java.util.Collection).isEmpty()) {
                  var45 = true
               } else {
                  val half: java.util.Iterator = atRest.iterator()

                  while (true) {
                     if (!half.hasNext()) {
                        var45 = true
                        break
                     }

                     val it: Pair = half.next() as Pair
                     if (!(Math.abs((it.getFirst() as java.lang.Number).floatValue()) < eps)
                        || !(Math.abs((it.getSecond() as java.lang.Number).floatValue()) < eps)) {
                        var45 = false
                        break
                     }
                  }
               }

               if (!var45) {
                  var45 = false
                  return@label112
               }
            }

            var45 = true
         }

         if (!(Math.abs(MouseTracker.dotX) < eps)
            || !(Math.abs(MouseTracker.dotY) < eps)
            || MouseTracker.pendingDx != 0.0
            || MouseTracker.pendingDy != 0.0
            || !var45
            || MouseTracker.INSTANCE.multiColor.isRainbow()) {
            val var35: Long = System.currentTimeMillis()
            if (var35 - MouseTracker.lastForceUpdateMs >= MouseTracker.INSTANCE.updateIntervalMs.longValue()) {
               FramebufferManager.INSTANCE.forceUpdate()
               MouseTracker.lastForceUpdateMs = var35
            }
         }

         MouseTracker.INSTANCE.stepPhysicsIfNeeded()
         val cx: Double = this.x() + 60 / 2.0F
         val cy: Double = this.y() + 60 / 2.0F
         val dotColor: Int = MouseTracker.currentDotColor
         val rgb: Int = dotColor and 16777215
         if (MouseTracker.INSTANCE.showCrosshair) {
            val dx: Int = rgb or Integer.MIN_VALUE
            context.fill(cx - 2.0, cy - 0.5, cx + 2.0, cy + 0.5, rgb or Integer.MIN_VALUE)
            context.fill(cx - 0.5, cy - 2.0, cx + 0.5, cy + 2.0, dx)
         }

         if (MouseTracker.INSTANCE.showTrail && MouseTracker.trail.size() > 1) {
            var var40: Int = 0
            val count: Int = MouseTracker.trail.size()
            val var42: Float = MouseTracker.INSTANCE.dotSize.floatValue() / 2.0F

            for (dy in MouseTracker.trail) {
               val color: Int = RangesKt.coerceIn((int)(((float)var40 + 1.0F) / (float)count * 200.0F), 0, 255) shl 24 or rgb
               val d: Float = var42 * ((var40 + 1.0F) / count)
               val tx: Double = cx + (dy.getFirst() as java.lang.Number).doubleValue()
               val ty: Double = cy + (dy.getSecond() as java.lang.Number).doubleValue()
               context.fill(tx - (double)d, ty - (double)d, tx + (double)d, ty + (double)d, color)
               var40++
            }
         }

         val var41: Double = MouseTracker.INSTANCE.dotSize.doubleValue() / 2.0
         val var43: Double = cx + MouseTracker.dotX
         val var44: Double = cy + MouseTracker.dotY
         context.fill(var43 - var41, var44 - var41, var43 + var41, var44 + var41, dotColor)
      }
   }

   @SourceDebugExtension(["SMAP\nMouseTracker.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MouseTracker.kt\ngg/voidrix/client/v2/modules/mousetracker/MouseTracker$CpsLabelComponent\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClientKt\n*L\n1#1,317:1\n127#2:318\n40#2:319\n941#3:320\n940#3:321\n*S KotlinDebug\n*F\n+ 1 MouseTracker.kt\ngg/voidrix/client/v2/modules/mousetracker/MouseTracker$CpsLabelComponent\n*L\n306#1:318\n306#1:319\n308#1:320\n310#1:321\n*E\n"])
   internal class CpsLabelComponent : FlowLayout(Sizing.Companion.fixed(60), Sizing.Companion.fixed(14), Algorithm.VERTICAL) {
      init {
         this.allowOverflow(true)
         this.surface(MouseTracker.INSTANCE.getBackground().toSurface())
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         this.surface(MouseTracker.INSTANCE.getBackground().toSurface())
         super.draw(context, mouseX, mouseY, partialTicks, delta)
         val var10000: Minecraft = Minecraft.getInstance()
         val var19: Font = var10000.font
         val var14: java.lang.String = "${CPS.INSTANCE.getLeftCps()} | ${CPS.INSTANCE.getRightCps()}"
         val var15: Int = var19.width(var14)
         val var16: Int = var19.lineHeight - 1
         context.drawString(var19, var14, this.x() + (double)((60 - var15) / 2), this.y() + (double)((14 - var16) / 2), MouseTracker.currentDotColor, true)
      }
   }

   public enum class Shape {
      CIRCLE,
      SQUARE;

      @JvmStatic
      fun getEntries(): EnumEntries<MouseTracker.Shape> {
         $ENTRIES
      }
   }

   internal class TrackerComponent : FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.VERTICAL) {
      private final val arena: gg.voidrix.client.v2.modules.mousetracker.MouseTracker.ArenaComponent = MouseTracker.ArenaComponent()
      private final val cpsLabel: gg.voidrix.client.v2.modules.mousetracker.MouseTracker.CpsLabelComponent = MouseTracker.CpsLabelComponent()
      private final var cpsAttached: Boolean
      private final var lastGap: Int = -1

      init {
         this.allowOverflow(true)
         this.horizontalAlignment(HorizontalAlignment.CENTER)
         this.child(this.arena as UIComponent)
         this.applyCpsVisibility()
      }

      private fun applyCpsVisibility() {
         val want: Boolean = MouseTracker.INSTANCE.showCps
         if (want != this.cpsAttached) {
            if (want) {
               this.child(this.cpsLabel as UIComponent)
            } else {
               this.removeChild(this.cpsLabel as UIComponent)
            }

            this.cpsAttached = want
         }

         val wantGap: Int = if (want) MouseTracker.INSTANCE.cpsGap.intValue() else 0
         if (wantGap != this.lastGap) {
            this.gap(wantGap)
            this.lastGap = wantGap
         }
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         this.applyCpsVisibility()
         super.draw(context, mouseX, mouseY, partialTicks, delta)
      }
   }
}
