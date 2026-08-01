package gg.norisk.client.v2.analytics

import java.util.LinkedHashMap

public class AnalyticsPropertiesBuilder {
   private final val values: LinkedHashMap<String, Any?> = LinkedHashMap()

   public fun put(key: String, value: Any?) {
      this.values.put(key, value)
   }

   public fun build(): Map<String, Any?> {
      return this.values
   }
}
