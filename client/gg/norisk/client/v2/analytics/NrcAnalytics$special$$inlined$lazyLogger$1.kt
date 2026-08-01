package gg.norisk.client.v2.analytics

import gg.norisk.compat.client.MCLogger
import kotlin.jvm.functions.Function0
import org.slf4j.Logger

// $VF: Class flags could not be determined
internal class `NrcAnalytics$special$$inlined$lazyLogger$1` : Function0<Logger> {
   @JvmStatic
   public NrcAnalytics$special$$inlined$lazyLogger$1 INSTANCE = NrcAnalytics$special$$inlined$lazyLogger$1();

   fun invoke(): Logger {
      MCLogger.getLogger(NrcAnalytics.class)
   }
}
