package gg.voidrix.client.v2.modules.daycounter

import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.SingleTextHud
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import kotlin.enums.EnumEntries
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlin.time.DurationKt
import kotlin.time.DurationUnit
import net.minecraft.client.Minecraft
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nDayCounter.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DayCounter.kt\ngg/voidrix/client/v2/modules/daycounter/DayCounter\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 Duration.kt\nkotlin/time/Duration\n*L\n1#1,45:1\n138#2:46\n40#2:47\n501#3:48\n*S KotlinDebug\n*F\n+ 1 DayCounter.kt\ngg/voidrix/client/v2/modules/daycounter/DayCounter\n*L\n17#1:46\n17#1:47\n31#1:48\n*E\n"])
public object DayCounter : SingleTextHud("Day Counter", "DAY {time}", null, null, false, false, false, 124) {
   @Category(name = "Format")
   @NotNull
   public final val style: gg.voidrix.client.v2.modules.daycounter.DayCounter.Style by ValueApiKt.enum$default(
         DayCounter.Style.DAY_ONLY, null, null, null, null, 30, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return style$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as DayCounter.Style
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 30, null)
   }

   public open fun getParsedText(): String {
      val var10000: Minecraft = Minecraft.getInstance()
      val time: Long = if (var10000.level != null) var10000.level.getGameTime() else 0L
      return if (this.style === DayCounter.Style.TIMER)
         this.getTimeAsString(time)
         else
         StringsKt.replace(this.getText(), "{time}", this.getTimeAsString(time), true)
      }

   private fun getTimeAsString(time: Long): String {
      val var10000: java.lang.String
      if (this.style === DayCounter.Style.DAY_ONLY) {
         var10000 = java.lang.String.valueOf(Math.max(time / (long)24000, 1L))
      } else {
         val timeInSeconds: Int = (int)(time / 20)
         val builder: StringBuilder = StringBuilder()
         val `arg0$iv`: Long = DurationKt.toDuration(timeInSeconds, DurationUnit.SECONDS)
         val var15: Long = Duration.getInWholeDays_impl/* $VF was: getInWholeDays-impl */(`arg0$iv`)
         val var10001: Int = Duration.getHoursComponent_impl/* $VF was: getHoursComponent-impl */(`arg0$iv`)
         val var10002: Int = Duration.getMinutesComponent_impl/* $VF was: getMinutesComponent-impl */(`arg0$iv`)
         val var10003: Int = Duration.getSecondsComponent_impl/* $VF was: getSecondsComponent-impl */(`arg0$iv`)
         Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(`arg0$iv`)
         if (var15 > 0L) {
            builder.append(var15).append("d ")
         }

         if (var10001 > 0) {
            builder.append(var10001).append("h ")
         }

         if (var10002 > 0) {
            builder.append(var10002).append("m ")
         }

         builder.append(var10003).append("s")
         val var3: java.lang.String = builder.toString()
         var10000 = var3
      }

      return var10000
   }

   public enum class Style {
      TIMER,
      DAY_ONLY;

      @JvmStatic
      fun getEntries(): EnumEntries<DayCounter.Style> {
         $ENTRIES
      }
   }
}
