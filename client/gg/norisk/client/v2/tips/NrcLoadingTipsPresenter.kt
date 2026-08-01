package gg.norisk.client.v2.tips

import java.io.File
import java.util.concurrent.ThreadLocalRandom
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nNrcLoadingTipsPresenter.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcLoadingTipsPresenter.kt\ngg/norisk/client/v2/tips/NrcLoadingTipsPresenter\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,71:1\n1#2:72\n*E\n"])
public object NrcLoadingTipsPresenter {
   public const val LEFT_MARGIN: Int = 6
   public const val BOTTOM_MARGIN: Int = 15
   public const val NUDGE_DOWN_PX: Int = 8
   public const val LINE_GAP: Int = 10
   public const val ROTATION_MS: Long = 8000L
   private final var tipUntilMs: Long
   private final var currentTipLine: String?
   private final var tipList: List<String>?
   private final var titleCache: String?

   @JvmStatic
   public fun pickRandomTip(lines: List<String>): String {
      if (lines.isEmpty()) {
         throw IllegalArgumentException("lines must not be empty".toString())
      } else {
         return lines.get(ThreadLocalRandom.current().nextInt(lines.size())) as java.lang.String
      }
   }

   @JvmStatic
   public fun clearSession() {
      tipUntilMs = 0L
      currentTipLine = null
      tipList = null
      titleCache = null
   }

   @JvmStatic
   public fun updateAndGetStringsForRender(gameDir: File, nowMs: Long): LoadingTipRenderStrings? {
      if (!NrcTipsTranslationLoader.isTipsFeatureActive()) {
         clearSession()
         return null
      } else {
         val tips: java.util.List = INSTANCE.resolveTipList(gameDir)
         if (tips.isEmpty()) {
            currentTipLine = null
            return null
         } else {
            if (nowMs >= tipUntilMs) {
               currentTipLine = pickRandomTip(tips)
               tipUntilMs = nowMs + 8000L
            }

            if (currentTipLine == null) {
               return null
            } else {
               var line: java.lang.String
               var var10000: java.lang.String
               run label49@{
                  line = currentTipLine
                  if (titleCache != null) {
                     val var6: java.lang.String = titleCache
                     var10000 = if (!StringsKt.isBlank(titleCache)) var6 else null
                     if (var10000 != null) {
                        return@label49
                     }
                  }

                  var10000 = NrcTipsTranslationLoader.titleOrDefault(MapsKt.emptyMap())
               }

               return LoadingTipRenderStrings(var10000, line)
            }
         }
      }
   }

   private fun resolveTipList(gameDir: File): List<String> {
      if (tipList != null) {
         return tipList
      } else {
         val translations: java.util.Map = NrcTipsTranslationLoader.loadAllStrings(gameDir)
         val values: java.util.List = NrcTipsTranslationLoader.tipValues(translations)
         tipList = values
         titleCache = NrcTipsTranslationLoader.titleOrDefault(translations)
         return values
      }
   }
}
