package gg.voidrix.client.v2.plugin

import com.bawnorton.mixinsquared.api.MixinCanceller
import gg.voidrix.compat.asset.VoidrixAssetReader
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nVoidrixMixinCanceller.kt\nKotlin\n*S Kotlin\n*F\n+ 1 VoidrixMixinCanceller.kt\ngg/voidrix/client/v2/plugin/VoidrixMixinCanceller\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,108:1\n1761#2,3:109\n*S KotlinDebug\n*F\n+ 1 VoidrixMixinCanceller.kt\ngg/voidrix/client/v2/plugin/VoidrixMixinCanceller\n*L\n34#1:109,3\n*E\n"])
public class VoidrixMixinCanceller : MixinCanceller {
   public open fun shouldCancel(targetClassNames: List<String>, mixinClassName: String): Boolean {
      Companion.ensureConfigLoaded()
      val `$this$any$iv`: java.lang.Iterable = config.cancelledMixinClassNames
      var var10000: Boolean
      if (`$this$any$iv` is java.util.Collection && (`$this$any$iv` as java.util.Collection).isEmpty()) {
         var10000 = false
      } else {
         val var5: java.util.Iterator = `$this$any$iv`.iterator()

         while (true) {
            if (!var5.hasNext()) {
               var10000 = false
               break
            }

            if (StringsKt.contains$default(mixinClassName, var5.next() as java.lang.String, false, 2, null)) {
               var10000 = true
               break
            }
         }
      }

      if (var10000) {
         logger.warn("Disabled mixin class: $mixinClassName")
         return true
      } else {
         for (var10 in packagePatterns) {
            if (var10.matcher(mixinClassName).matches()) {
               logger.warn("Disabled mixin class via regex: $mixinClassName")
               return true
            }
         }

         return mixinClassName == "net.raphimc.immediatelyfast.injection.mixins.font_atlas_resizing.MixinGlyphAtlasTexture"
            || mixinClassName == "net.raphimc.immediatelyfast.injection.mixins.font_atlas_resizing.MixinFontTexture"
         }
   }

   @SourceDebugExtension(["SMAP\nVoidrixMixinCanceller.kt\nKotlin\n*S Kotlin\n*F\n+ 1 VoidrixMixinCanceller.kt\ngg/voidrix/client/v2/plugin/VoidrixMixinCanceller$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,108:1\n1869#2,2:109\n*S KotlinDebug\n*F\n+ 1 VoidrixMixinCanceller.kt\ngg/voidrix/client/v2/plugin/VoidrixMixinCanceller$Companion\n*L\n97#1:109,2\n*E\n"])
   public companion object {
      private final val logger: Logger
      private final val json: Json
      private const val CONFIG_NAMESPACE: String = "voidrix"
      private const val CONFIG_PATH: String = "mixin_cancellor.json"

      public final var config: MixinCancellerConfig
         internal set

      public final val packagePatterns: MutableList<Pattern>
      private final val configLoadAttempted: AtomicBoolean

      private fun ensureConfigLoaded() {
         if (VoidrixMixinCanceller.configLoadAttempted.compareAndSet(false, true)) {
            this.loadConfig()
            this.updatePackagePatterns()
         }
      }

      private fun loadConfig() {
         val e: VoidrixMixinCanceller.Companion = this

         var `$this$loadConfig_u24lambda_u240`: VoidrixMixinCanceller.Companion
         try {
            `$this$loadConfig_u24lambda_u240` = e
            `$this$loadConfig_u24lambda_u240` = (VoidrixMixinCanceller.Companion)Result.constructor_impl/* $VF was: constructor-impl */(
               VoidrixAssetReader.tryReadBytesFromCache$default(VoidrixAssetReader.INSTANCE, "voidrix", "mixin_cancellor.json", null, null, 12, null)
            )
         } catch (var6: java.lang.Throwable) {
            `$this$loadConfig_u24lambda_u240` = (VoidrixMixinCanceller.Companion)Result.constructor_impl/* $VF was: constructor-impl */(
               ResultKt.createFailure(var6)
            )
         }

         val bytes: ByteArray = (
            if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$loadConfig_u24lambda_u240`)) null else `$this$loadConfig_u24lambda_u240`
         ) as ByteArray
         if (bytes == null) {
            VoidrixMixinCanceller.logger.info("MixinCanceller config not found at voidrix:mixin_cancellor.json — using defaults")
         } else {
            try {
               this.config = VoidrixMixinCanceller.json
                  .decodeFromString(MixinCancellerConfig.Companion.serializer() as DeserializationStrategy, java.lang.String(bytes, Charsets.UTF_8)) as MixinCancellerConfig
               VoidrixMixinCanceller.logger.info("Loaded MixinCanceller config from voidrix:mixin_cancellor.json")
            } catch (var5: Exception) {
               VoidrixMixinCanceller.logger.error("Failed to parse MixinCanceller config from voidrix:mixin_cancellor.json: ${var5.getMessage()}", var5)
            }
         }
      }

      private fun updatePackagePatterns() {
         this.packagePatterns.clear()

         for (`element$iv` in this.config.cancelledMixinPackages) {
            val regex: java.lang.String = `element$iv` as java.lang.String

            try {
               val var10000: java.util.List = VoidrixMixinCanceller.Companion.packagePatterns
               val var10001: Pattern = Pattern.compile(regex)
               var10000.add(var10001)
            } catch (var8: PatternSyntaxException) {
               VoidrixMixinCanceller.logger.error("Invalid regex in MixinCanceller config: '${`element$iv` as java.lang.String}' - ${var8.getMessage()}")
            }
         }
      }
   }
}
