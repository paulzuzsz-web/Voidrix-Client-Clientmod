package gg.voidrix.client.v2.analytics

import gg.voidrix.compat.auth.DevAuth
import gg.voidrix.compat.auth.VoidrixAuth
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nVoidrixAnalyticsConfig.kt\nKotlin\n*S Kotlin\n*F\n+ 1 VoidrixAnalyticsConfig.kt\ngg/voidrix/client/v2/analytics/VoidrixAnalyticsConfig\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,35:1\n1#2:36\n*E\n"])
public object VoidrixAnalyticsConfig {
   public const val DEFAULT_BASE_URL: String = "https://analytics-api-staging.norisk.gg"
   public const val LOCAL_BASE_URL: String = "http://127.0.0.1:8080"
   private const val URL_PROPERTY: String = "voidrix.analytics.url"
   private const val ENABLED_PROPERTY: String = "voidrix.analytics.enabled"

   public fun baseUrl(): String {
      val var1: java.lang.String = System.getProperty("voidrix.analytics.url")
      if (var1 != null) {
         val var2: java.lang.String = StringsKt.trim(var1).toString()
         if (var2 != null) {
            val var3: java.lang.String = if (!StringsKt.isBlank(var2)) var2 else null
            if (var3 != null) {
               return var3
            }
         }
      }

      return if (VoidrixAuth.INSTANCE.isLocal()) "http://127.0.0.1:8080" else "https://analytics-api-staging.norisk.gg"
   }

   public fun trackUrl(): String {
      return "${StringsKt.trimEnd(this.baseUrl(), charArrayOf('/'))}/api/track"
   }

   public fun isEnabled(): Boolean {
      val var10000: java.lang.String = System.getProperty("voidrix.analytics.enabled")
      if (var10000 != null) {
         val var3: java.lang.Boolean = StringsKt.toBooleanStrictOrNull(var10000)
         if (var3 != null) {
            return var3
         }
      }

      return DevAuth.isEnabled
   }

   public fun serverModeLabel(): String {
      return if (VoidrixAuth.INSTANCE.isLocal()) "local" else (if (VoidrixAuth.INSTANCE.isExperimental()) "staging" else "default")
   }
}
