package gg.norisk.client.v2.modules.impl

import java.time.Duration
import kotlin.enums.EnumEntries

public class ZoomAnimation(start: Float, end: Float, dur: Duration) {
   public final var start: Float
      internal set

   public final var end: Float
      internal set

   public final var dur: Duration
      internal set

   public final var startTime: Long
      internal set

   public final var easing: gg.norisk.client.v2.modules.impl.ZoomAnimation.Easing
      internal set

   public final var forward: Boolean
      internal set

   init {
      this.start = start
      this.end = end
      this.dur = dur
      this.startTime = System.nanoTime()
      this.easing = ZoomAnimation.Easing.LINEAR
      this.forward = true
   }

   public constructor(start: Float, end: Float, duration: Duration, easing: gg.norisk.client.v2.modules.impl.ZoomAnimation.Easing) : this(start, end, duration) {
      this.easing = easing
   }

   public fun setDuration(dur: Duration) {
      this.dur = dur
   }

   public fun reset() {
      this.startTime = System.nanoTime()
   }

   public fun get(): Float {
      var var8: Float = (float)Math.max(0.0, Math.min(1.0, (double)((float)(System.nanoTime() - this.startTime) / (float)this.dur.toNanos())))
      if (!this.forward) {
         var8 = 1 - var8
      }

      return this.start + (this.end - this.start) * this.easing.apply((double)var8)
   }

   public final val isDone: Boolean
      public final get() {
         return System.nanoTime() - this.startTime >= this.dur.toNanos()
      }


   public enum class Easing(floatFunction: (Double) -> Double) {
      LINEAR({ x: Double ->
         x
      }),
      SINE_IN({ x: Double ->
         1 - Math.cos(x * Math.PI / (double)2)
      }),
      SINE_OUT({ x: Double ->
         Math.sin(x * Math.PI / (double)2)
      }),
      SINE_IN_OUT({ x: Double ->
         -(Math.cos(Math.PI * x) - 1) / 2
      }),
      CUBIC_IN({ x: Double ->
         Math.pow(x, 3.0)
      }),
      CUBIC_OUT({ x: Double ->
         1 - Math.pow((double)1 - x, 3.0)
      }),
      CUBIC_IN_OUT({ x: Double ->
         if (x < 0.5) 4 * x * x * x else 1 - Math.pow((double)-2 * x + (double)2, 3.0) / 2
      }),
      QUINT_IN({ x: Double ->
         Math.pow(x, 5.0)
      }),
      QUINT_OUT({ x: Double ->
         1 - Math.pow((double)1 - x, 5.0)
      }),
      QUINT_IN_OUT({ x: Double ->
         if (x < 0.5) 16 * x * x * x * x * x else 1 - Math.pow((double)-2 * x + (double)2, 5.0) / 2
      }),
      CIRC_IN({ x: Double ->
         1 - Math.sqrt((double)1 - Math.pow(x, 2.0))
      }),
      CIRC_OUT({ x: Double ->
         Math.sqrt((double)1 - Math.pow(x - (double)1, 2.0))
      }),
      CIRC_IN_OUT(
         { x: Double ->
            if (x < 0.5)
               (1 - Math.sqrt((double)1 - Math.pow((double)2 * x, 2.0))) / 2
               else
               (Math.sqrt((double)1 - Math.pow((double)-2 * x + (double)2, 2.0)) + 1) / 2
            }
      ),
      ELASTIC_IN(
         { x: Double ->
            if (x == 0.0)
               0.0
               else
               (if (x == 1.0) 1.0 else -Math.pow(2.0, (double)10 * x - (double)10) * Math.sin((x * (double)10 - 10.75) * (Math.PI * 2.0 / 3.0)))
            }
      ),
      ELASTIC_OUT({ x: Double ->
         if (x == 0.0) 0.0 else (if (x == 1.0) 1.0 else Math.pow(2.0, (double)-10 * x) * Math.sin((x * (double)10 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1)
      }),
      ELASTIC_IN_OUT(
         { x: Double ->
            val sin: Double = Math.sin(((double)20 * x - 11.125) * (Math.PI * 4.0 / 9.0))
            if (x == 0.0)
               0.0
               else
               (
                  if (x == 1.0)
                     1.0
                     else
                     (if (x < 0.5) -(Math.pow(2.0, (double)20 * x - (double)10) * sin) / 2 else Math.pow(2.0, (double)-20 * x + (double)10) * sin / 2 + 1)
               )
            }
      ),
      QUAD_IN({ x: Double ->
         x * x
      }),
      QUAD_OUT({ x: Double ->
         1 - (1 - x) * (1 - x)
      }),
      QUAD_IN_OUT({ x: Double ->
         if (x < 0.5) 2 * x * x else 1 - Math.pow((double)-2 * x + (double)2, 2.0) / 2
      }),
      QUART_IN({ x: Double ->
         x * x * x * x
      }),
      QUART_OUT({ x: Double ->
         1 - Math.pow((double)1 - x, 4.0)
      }),
      QUART_IN_OUT({ x: Double ->
         if (x < 0.5) 8 * x * x * x * x else 1 - Math.pow((double)-2 * x + (double)2, 4.0) / 2
      }),
      EXPO_IN({ x: Double ->
         if (x == 0.0) 0.0 else Math.pow(2.0, (double)10 * x - (double)10)
      }),
      EXPO_OUT({ x: Double ->
         if (x == 1.0) 1.0 else 1 - Math.pow(2.0, (double)-10 * x)
      }),
      EXPO_IN_OUT(
         { x: Double ->
            if (x == 0.0)
               0.0
               else
               (if (x == 1.0) 1.0 else (if (x < 0.5) Math.pow(2.0, (double)20 * x - (double)10) / 2 else (2 - Math.pow(2.0, (double)-20 * x + (double)10)) / 2))
            }
      ),
      BACK_IN({ x: Double ->
         (1.70158 + 1) * x * x * x - 1.70158 * x * x
      }),
      BACK_OUT({ x: Double ->
         1 + (1.70158 + 1) * Math.pow(x - (double)1, 3.0) + 1.70158 * Math.pow(x - (double)1, 2.0)
      }),
      BACK_IN_OUT(
         { x: Double ->
            if (x < 0.5)
               Math.pow((double)2 * x, 2.0) * ((1.70158 * 1.525 + 1) * 2 * x - 1.70158 * 1.525) / 2
               else
               (Math.pow((double)2 * x - (double)2, 2.0) * ((1.70158 * 1.525 + 1) * (x * 2 - 2) + 1.70158 * 1.525) + 2) / 2
            }
      ),
      BOUNCE_OUT(
         { x: Double ->
            if (x < 1 / 2.75)
               7.5625 * x * x
               else
               (
                  if (x < 2 / 2.75)
                     7.5625 * (x - 1.5 / 2.75) * (x - 1.5 / 2.75) + 0.75
                     else
                     (
                        if (x < 2.5 / 2.75)
                           7.5625 * (x - 2.25 / 2.75) * (x - 2.25 / 2.75) + 0.9375
                           else
                           7.5625 * (x - 2.625 / 2.75) * (x - 2.625 / 2.75) + 0.984375
                     )
               )
            }
      ),
      BOUNCE_IN({ x: Double ->
         1 - BOUNCE_OUT.apply(x)
      }),
      BOUNCE_IN_OUT({ x: Double ->
         if (x < 0.5) (1 - BOUNCE_OUT.apply((double)1 - (double)2 * x)) / 2 else (1 + BOUNCE_OUT.apply((double)2 * x - (double)1)) / 2
      });

      public final val floatFunction: (Double) -> Double

      init {
         this.floatFunction = floatFunction
      }

      public fun apply(f: Double): Float {
         return (float)(this.floatFunction(f) as java.lang.Number).doubleValue()
      }

      @JvmStatic
      fun getEntries(): EnumEntries<ZoomAnimation.Easing> {
         $ENTRIES
      }
   }
}
