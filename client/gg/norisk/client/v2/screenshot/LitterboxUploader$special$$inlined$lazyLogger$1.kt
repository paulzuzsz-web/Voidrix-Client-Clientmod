package gg.norisk.client.v2.screenshot

import gg.norisk.compat.client.MCLogger
import kotlin.jvm.functions.Function0
import org.slf4j.Logger

// $VF: Class flags could not be determined
internal class `LitterboxUploader$special$$inlined$lazyLogger$1` : Function0<Logger> {
   @JvmStatic
   public LitterboxUploader$special$$inlined$lazyLogger$1 INSTANCE = LitterboxUploader$special$$inlined$lazyLogger$1();

   fun invoke(): Logger {
      MCLogger.getLogger(LitterboxUploader.class)
   }
}
