package gg.norisk.client.v2.modules.spotify

import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nMarqueeState.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MarqueeState.kt\ngg/norisk/client/v2/modules/spotify/MarqueeState\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,60:1\n1#2:61\n*E\n"])
public object MarqueeState {
   private const val PIXELS_PER_SECOND: Double = 15.0
   private const val PAUSE_MS: Long = 1500L
   private final val entries: ConcurrentHashMap<String, gg.norisk.client.v2.modules.spotify.MarqueeState.Entry> = ConcurrentHashMap()

   public fun pixelOffset(id: String, text: String, textWidthPx: Int, availableWidthPx: Int, gapPx: Int = 20): Double {
      if (textWidthPx <= availableWidthPx) {
         entries.remove(id)
         return 0.0
      } else {
         run label27@{
            val now: Long = System.currentTimeMillis()
            val current: MarqueeState.Entry = entries.get(id)
            val var10000: Long
            if (current != null && current.text == text) {
               var10000 = current.startAt
            } else {
               val elapsed: MarqueeState.Entry = MarqueeState.Entry(text, now)
               entries.put(id, elapsed)
               var10000 = elapsed.startAt
            }

            val var17: Long = RangesKt.coerceAtLeast(now - var10000, 0L)
            return if (var17 < 1500L) 0.0 else (var17 - 1500L) / 1000.0 * 15.0 % (textWidthPx + gapPx)
         }
      }
   }

   public fun reset(id: String) {
      entries.remove(id)
   }

   private data class Entry(text: String, startAt: Long) {
      public final val text: String
      public final val startAt: Long

      init {
         this.text = text
         this.startAt = startAt
      }

      public operator fun component1(): String {
         return this.text
      }

      public operator fun component2(): Long {
         return this.startAt
      }

      public fun copy(text: String = this.text, startAt: Long = this.startAt): gg.norisk.client.v2.modules.spotify.MarqueeState.Entry {
         return MarqueeState.Entry(text, startAt)
      }

      public override fun toString(): String {
         return "Entry(text=${this.text}, startAt=${this.startAt})"
      }

      public override fun hashCode(): Int {
         return this.text.hashCode() * 31 + java.lang.Long.hashCode(this.startAt)
      }

      public override operator fun equals(other: Any?): Boolean {
         label28@
         if (this === other) {
            return true
         } else {
            return other is MarqueeState.Entry && this.text == (other as MarqueeState.Entry).text && this.startAt == (other as MarqueeState.Entry).startAt
         }
      }
   }
}
