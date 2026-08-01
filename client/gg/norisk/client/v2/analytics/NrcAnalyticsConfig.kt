package gg.norisk.client.v2.analytics

import gg.norisk.compat.auth.DevAuth
import gg.norisk.compat.auth.NoriskAuth
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nNrcAnalyticsConfig.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcAnalyticsConfig.kt\ngg/norisk/client/v2/analytics/NrcAnalyticsConfig\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,35:1\n1#2:36\n*E\n"])
public object NrcAnalyticsConfig {
   public const val DEFAULT_BASE_URL: String = "https://analytics-api-staging.norisk.gg"
   public const val LOCAL_BASE_URL: String = "http://127.0.0.1:8080"
   private const val URL_PROPERTY: String = "norisk.analytics.url"
   private const val ENABLED_PROPERTY: String = "norisk.analytics.enabled"

   public fun baseUrl(): String {
      val var1: java.lang.String = System.getProperty("norisk.analytics.url")
      if (var1 != null) {
         val var2: java.lang.String = StringsKt.trim(var1).toString()
         if (var2 != null) {
            val var3: java.lang.String = if (!StringsKt.isBlank(var2)) var2 else null
            if (var3 != null) {
               return var3
            }
         }
      }

      return if (NoriskAuth.INSTANCE.isLocal()) "http://127.0.0.1:8080" else "https://analytics-api-staging.norisk.gg"
   }

   public fun trackUrl(): String {
      return "${StringsKt.trimEnd(this.baseUrl(), charArrayOf('/'))}/api/track"
   }

   public fun isEnabled(): Boolean {
      val var10000: java.lang.String = System.getProperty("norisk.analytics.enabled")
      if (var10000 != null) {
         val var3: java.lang.Boolean = StringsKt.toBooleanStrictOrNull(var10000)
         if (var3 != null) {
            return var3
         }
      }

      return DevAuth.isEnabled
   }

   public fun serverModeLabel(): String {
      return if (NoriskAuth.INSTANCE.isLocal()) "local" else (if (NoriskAuth.INSTANCE.isExperimental()) "staging" else "default")
   }
}
