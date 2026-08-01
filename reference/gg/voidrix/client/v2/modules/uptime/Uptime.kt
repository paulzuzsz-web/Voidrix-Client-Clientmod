package gg.voidrix.client.v2.modules.uptime

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
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nUptime.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Uptime.kt\ngg/voidrix/client/v2/modules/uptime/Uptime\n+ 2 Duration.kt\nkotlin/time/Duration\n*L\n1#1,55:1\n501#2:56\n501#2:57\n*S KotlinDebug\n*F\n+ 1 Uptime.kt\ngg/voidrix/client/v2/modules/uptime/Uptime\n*L\n28#1:56\n39#1:57\n*E\n"])
public object Uptime : SingleTextHud("Uptime", "{time}", null, null, false, false, false, 124) {
   private final val startTime: Long = System.currentTimeMillis()

   @Category(name = "Format")
   @NotNull
   private final val format: gg.voidrix.client.v2.modules.uptime.Uptime.Format by ValueApiKt.enum$default(
         Uptime.Format.TEXT, "Format", null, null, null, 28, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         private final get() {
         return format$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as Uptime.Format
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 30, null)
   }

   public open fun getParsedText(): String {
      val var10000: java.lang.String = this.getText()
var var10002: java.lang.String
      when (Uptime.WhenMappings.$EnumSwitchMapping$0[this.format.ordinal()]) {
         1 -> var10002 = this.textUptime()
         2 -> var10002 = this.digitalUptime()
         else -> throw NoWhenBranchMatchedException()
      }

      return StringsKt.replace(var10000, "{time}", StringsKt.removeSuffix(var10002, " "), true)
   }

   private fun textUptime(): String {
      val `arg0$iv`: Long = DurationKt.toDuration(System.currentTimeMillis() - startTime, DurationUnit.MILLISECONDS)
      val var10000: Long = Duration.getInWholeDays_impl/* $VF was: getInWholeDays-impl */(`arg0$iv`)
      val var10001: Int = Duration.getHoursComponent_impl/* $VF was: getHoursComponent-impl */(`arg0$iv`)
      val var10002: Int = Duration.getMinutesComponent_impl/* $VF was: getMinutesComponent-impl */(`arg0$iv`)
      val var10003: Int = Duration.getSecondsComponent_impl/* $VF was: getSecondsComponent-impl */(`arg0$iv`)
      Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(`arg0$iv`)
      val var10: StringBuilder = StringBuilder()
      if (var10000 > 0L) {
         var10.append("$var10000d ")
      }

      if (var10001 > 0) {
         var10.append("$var10001h ")
      }

      if (var10002 > 0) {
         var10.append("$var10002m ")
      }

      var10.append("$var10003s")
      return var10.toString()
   }

   private fun digitalUptime(): String {
      val `arg0$iv`: Long = DurationKt.toDuration(System.currentTimeMillis() - startTime, DurationUnit.MILLISECONDS)
      val var10000: Long = Duration.getInWholeDays_impl/* $VF was: getInWholeDays-impl */(`arg0$iv`)
      val var10001: Int = Duration.getHoursComponent_impl/* $VF was: getHoursComponent-impl */(`arg0$iv`)
      val var10002: Int = Duration.getMinutesComponent_impl/* $VF was: getMinutesComponent-impl */(`arg0$iv`)
      val var10003: Int = Duration.getSecondsComponent_impl/* $VF was: getSecondsComponent-impl */(`arg0$iv`)
      Duration.getNanosecondsComponent_impl/* $VF was: getNanosecondsComponent-impl */(`arg0$iv`)
      val var10: java.util.List = CollectionsKt.createListBuilder()
      if (var10000 > 0L) {
         var10.add(var10000)
      }

      var10.add(var10001)
      var10.add(var10002)
      var10.add(var10003)
      return CollectionsKt.joinToString$default(CollectionsKt.build(var10), ":", null, null, 0, null, <unrepresentable>.INSTANCE, 30, null)
   }

   private enum class Format {
      TEXT,
      DIGITAL;

      @JvmStatic
      fun getEntries(): EnumEntries<Uptime.Format> {
         $ENTRIES
      }
   }
}
