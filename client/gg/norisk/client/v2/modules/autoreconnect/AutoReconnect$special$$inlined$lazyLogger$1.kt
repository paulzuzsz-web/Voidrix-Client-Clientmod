package gg.norisk.client.v2.modules.autoreconnect

import gg.norisk.compat.client.MCLogger
import kotlin.jvm.functions.Function0
import org.slf4j.Logger

// $VF: Class flags could not be determined
internal class `AutoReconnect$special$$inlined$lazyLogger$1` : Function0<Logger> {
   @JvmStatic
   public AutoReconnect$special$$inlined$lazyLogger$1 INSTANCE = AutoReconnect$special$$inlined$lazyLogger$1();

   fun invoke(): Logger {
      MCLogger.getLogger(AutoReconnect.class)
   }
}
