package gg.voidrix.client.v2.analytics

import gg.voidrix.compat.client.MCLogger
import kotlin.jvm.functions.Function0
import org.slf4j.Logger

// $VF: Class flags could not be determined
internal class `VoidrixAnalytics$special$$inlined$lazyLogger$1` : Function0<Logger> {
   @JvmStatic
   public VoidrixAnalytics$special$$inlined$lazyLogger$1 INSTANCE = VoidrixAnalytics$special$$inlined$lazyLogger$1();

   fun invoke(): Logger {
      MCLogger.getLogger(VoidrixAnalytics.class)
   }
}
