package gg.voidrix.client.v2.resourcepackorganizer;

import gg.voidrix.client.v2.modules.impl.ResourcePackOrganizerModule;
import gg.voidrix.compat.client.MCLoggerKt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public final class PackLogReroute {
   private static final Logger LOG = LoggerFactory.getLogger("PackDiscovery");
   private static final String CATEGORY = "packs";

   private PackLogReroute() {
   }

   public static boolean shouldDrop() {
      return ResourcePackOrganizerModule.INSTANCE.isEnabled();
   }

   public static void debug(String format, Object... args) {
      String msg = MessageFormatter.arrayFormat(format, args).getMessage();
      MCLoggerKt.voidrixDebugLog(LOG, "packs", msg);
   }
}
