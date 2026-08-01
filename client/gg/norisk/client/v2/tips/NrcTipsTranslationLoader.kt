package gg.norisk.client.v2.tips

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import gg.norisk.compat.asset.NrcAssetReader
import gg.norisk.compat.reflection.NrcReflectionUtil
import gg.norisk.ui.api.module.Module
import java.io.File
import java.util.ArrayList
import java.util.Collections
import java.util.LinkedHashMap
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nNrcTipsTranslationLoader.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcTipsTranslationLoader.kt\ngg/norisk/client/v2/tips/NrcTipsTranslationLoader\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,101:1\n774#2:102\n865#2,2:103\n*S KotlinDebug\n*F\n+ 1 NrcTipsTranslationLoader.kt\ngg/norisk/client/v2/tips/NrcTipsTranslationLoader\n*L\n66#1:102\n66#1:103,2\n*E\n"])
public object NrcTipsTranslationLoader {
   public const val TITLE_KEY: String = "nrc.ui.tips.title"
   public const val TIP_KEY_PREFIX: String = "nrc.ui.tips.value."
   private const val NAMESPACE: String = "noriskclient-cosmetics"
   private const val LANG_PREFIX: String = "lang/"
   private const val MODULE_FQN: String = "gg.norisk.client.v2.modules.loadingtips.LoadingScreenTipsModule"

   @JvmStatic
   public fun isJvmTipsOverrideEnabled(): Boolean {
      return java.lang.Boolean.parseBoolean(System.getProperty("nrc.tips.enabled", "true"))
   }

   @JvmStatic
   public fun isTipsFeatureActive(): Boolean {
      label16@
      if (!isJvmTipsOverrideEnabled()) {
         return false
      } else {
         val var10000: Module = NrcReflectionUtil.tryLoadObject("gg.norisk.client.v2.modules.loadingtips.LoadingScreenTipsModule") as Module
         return var10000 != null && var10000.isEnabled()
      }
   }

   @JvmStatic
   public fun translationsDir(gameDir: File): File {
      return File(gameDir, "VoidrixClient/designer/nrc-cosmetics/assets/noriskclient-cosmetics/lang")
   }

   @JvmStatic
   public fun loadAllStrings(gameDir: File): Map<String, String> {
      if (!isTipsFeatureActive()) {
         return MapsKt.emptyMap()
      } else {
         val out: LinkedHashMap = LinkedHashMap()
         val parser: JsonParser = JsonParser()
         val rel: java.lang.Iterable = NrcAssetReader.listKnownInCache$default(NrcAssetReader.INSTANCE, "noriskclient-cosmetics/lang/", null, 2, null)
         val bytes: java.util.Collection = ArrayList()

         for (`element$iv$iv` in rel) {
            if (StringsKt.endsWith$default(`element$iv$iv` as java.lang.String, ".json", false, 2, null)) {
               bytes.add(`element$iv$iv`)
            }
         }

         for (var15 in bytes as java.util.List) {
            val var10000: ByteArray = NrcAssetReader.tryReadBytesFromCache$default(
               NrcAssetReader.INSTANCE, "noriskclient-cosmetics", StringsKt.removePrefix(var15, "noriskclient-cosmetics/"), null, null, 12, null
            )
            if (var10000 != null) {
               val var17: ByteArray = var10000

               try {
                  for (var20 in parser.parse(java.lang.String(var17, Charsets.UTF_8)).getAsJsonObject().entrySet()) {
                     val var21: java.lang.String = var20.getKey() as java.lang.String
                     val var22: JsonElement = var20.getValue() as JsonElement
                     if (var22 != null && var22.isJsonPrimitive()) {
                        out.put(var21, var22.getAsString())
                     }
                  }
               } catch (var13: Exception) {
               }
            }
         }

         val var23: java.util.Map = Collections.unmodifiableMap(out)
         return var23
      }
   }

   @JvmStatic
   public fun tipValues(translations: Map<String, String>): List<String> {
      val values: ArrayList = ArrayList()

      for (var3 in translations.entrySet()) {
         val k: java.lang.String = var3.getKey() as java.lang.String
         val v: java.lang.String = var3.getValue() as java.lang.String
         if (StringsKt.startsWith$default(k, "nrc.ui.tips.value.", false, 2, null) && !StringsKt.isBlank(v)) {
            values.add(v)
         }
      }

      return values
   }

   @JvmStatic
   public fun titleOrDefault(translations: Map<String, String>): String {
      val var10000: java.lang.String = translations.get("nrc.ui.tips.title") as java.lang.String
      val t: java.lang.String = if (var10000 != null) StringsKt.trim(var10000).toString() else null
      return if (t as java.lang.CharSequence == null || t.length() == 0) "Tips" else t
   }
}
