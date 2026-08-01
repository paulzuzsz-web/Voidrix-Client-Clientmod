package gg.norisk.client.v2.modules.spotify

import gg.norisk.compat.resource.TextureInfo
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.ui.api.hud.DynamicBackground
import java.time.Duration
import java.util.ArrayList
import java.util.Arrays
import java.util.Optional
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.gui.Font
import net.minecraft.resources.Identifier
import org.endlesssource.mediainterface.api.NowPlaying
import org.endlesssource.mediainterface.api.PlaybackState

@SourceDebugExtension(["SMAP\nSpotifyRenderer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SpotifyRenderer.kt\ngg/norisk/client/v2/modules/spotify/SpotifyRenderer\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,530:1\n1#2:531\n774#3:532\n865#3,2:533\n774#3:535\n865#3,2:536\n*S KotlinDebug\n*F\n+ 1 SpotifyRenderer.kt\ngg/norisk/client/v2/modules/spotify/SpotifyRenderer\n*L\n126#1:532\n126#1:533,2\n187#1:535\n187#1:536,2\n*E\n"])
public class SpotifyRenderer : FlowLayout(Sizing.Companion.fixed(200), Sizing.Companion.fixed(32), Algorithm.VERTICAL) {
   private final var lastLayout: HudLayout?
   private final var lastScaleTimes100: Int
   private final var lastPadW: Int = -1
   private final var lastPadH: Int = -1
   private final var lastShowLabels: Boolean?
   private final var lastTrackKey: String = ""
   private final var trackChangedAt: Long
   private final var hiddenSinceMs: Long
   private final var lastCover: TextureInfo?

   public open fun draw(graphics: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
      this.applySizingForCurrentLayout()
this.surface(SpotifyHud.INSTANCE.background.toSurface())
super.draw(graphics, mouseX, mouseY, partialTicks, delta)
val layout: HudLayout = SpotifyHud.INSTANCE.layout
val scale: Double = SpotifyHud.INSTANCE.hudScale.floatValue() + 0.5F
val var9: Pair = this.paddingOffset()
val originX: Double = this.x + (var9.component1() as java.lang.Number).doubleValue()
val originY: Double = this.y + (var9.component2() as java.lang.Number).doubleValue()
val np: NowPlaying = SpotifyMediaState.INSTANCE.currentNowPlaying
val state: PlaybackState = SpotifyHud.INSTANCE.currentPlaybackState()
val isPaused: Boolean = state === PlaybackState.PAUSED || state === PlaybackState.STOPPED
      when (SpotifyRenderer.WhenMappings.$EnumSwitchMapping$0[layout.ordinal()]) {
         1 -> this.drawCompact(graphics, originX, originY, scale, np, isPaused)
         2 -> this.drawDetailed(graphics, originX, originY, scale, np, isPaused)
         3 -> this.drawMiniPlayer(graphics, originX, originY, scale, np, isPaused)
         else -> throw NoWhenBranchMatchedException()
      }
   }

   private fun drawCompact(graphics: OwoUIGraphics, x: Double, y: Double, scale: Double, np: NowPlaying?, isPaused: Boolean) {
      var coverSize: Double
      var gap: Double
      var totalWidth: Double
      var totalHeight: Double
      var var67: java.lang.String
      run label141@{
         coverSize = 28.0 * scale
         gap = 4.0 * scale
         totalWidth = 200.0 * scale
         totalHeight = 32.0 * scale
         if (np != null) {
            val var10001: Optional = np.getArtwork()
            if (var10001 != null) {
               var67 = var10001.orElse(null) as java.lang.String
               return@label141
            }
         }

         var67 = null
      }

      val info: TextureInfo = this.cover(var67, 18)
      if (info != null) {
         this.drawTexture(graphics, info, x, y, coverSize, coverSize)
      }

      if (isPaused) {
         this.drawPauseOverlay(graphics, x, y, coverSize, false)
      }

      var textX: Double
      var textWidth: Double
      var titleY: Double
      var artistY: Double
      var var62: java.lang.String
      run label148@{
         textX = x + coverSize + gap
         textWidth = RangesKt.coerceAtLeast(x + totalWidth - (x + coverSize + gap), 1.0)
         titleY = y + 4.0 * scale
         artistY = y + 14.0 * scale
         if (np != null) {
            val var10000: Optional = np.getTitle()
            if (var10000 != null) {
               var62 = var10000.orElse("")
               if (var62 != null) {
                  var62 = if (!StringsKt.isBlank(var62)) var62 else null
                  if (var62 != null) {
                     return@label148
                  }
               }
            }
         }

         var62 = "Not Playing"
      }

      run label153@{
         if (np != null) {
            val var63: Optional = np.getArtist()
            if (var63 != null) {
               var62 = var63.orElse("")
               if (var62 != null) {
                  var62 = if (!StringsKt.isBlank(var62)) var62 else null
                  if (var62 != null) {
                     return@label153
                  }
               }
            }
         }

         var62 = ""
      }

      run label158@{
         if (np != null) {
            val secondary: Optional = np.getAlbum()
            if (secondary != null) {
               val var47: java.lang.String = secondary.orElse("")
               if (var47 != null) {
                  val var52: java.lang.String = if (!StringsKt.isBlank(var47) && !StringsKt.equals(var47, var62, true)) var47 else null
                  if (var52 != null) {
                     var62 = var52
                     return@label158
                  }
               }
            }
         }

         var62 = ""
      }

      val var56: java.lang.Iterable = CollectionsKt.listOf(arrayOf(var62, var62))
      val var59: java.util.Collection = ArrayList()

      for (`element$iv$iv` in var56) {
         if ((`element$iv$iv` as java.lang.String).length() > 0) {
            var59.add(`element$iv$iv`)
         }
      }

      val var46: java.lang.String = CollectionsKt.joinToString$default(var59 as java.util.List, " · ", null, null, 0, null, null, 62, null)
      this.drawMarqueeString(graphics, "spotify.title", var62, textX, titleY, textWidth, scale, -1)
      if (var46.length() > 0) {
         this.drawMarqueeString(graphics, "spotify.secondary", var46, textX, artistY, textWidth, scale, -4144960)
      }

      val var50: Double = Math.max(2.0, 2.0 * scale)
      this.drawProgressBar(graphics, x, y + totalHeight - var50, totalWidth, var50, scale, np, false, false)
   }

   private fun drawDetailed(graphics: OwoUIGraphics, x: Double, y: Double, scale: Double, np: NowPlaying?, isPaused: Boolean) {
      var coverSize: Double
      var gap: Double
      var rightPad: Double
      var totalWidth: Double
      var totalHeight: Double
      var coverY: Double
      var var70: java.lang.String
      run label147@{
         coverSize = 32.0 * scale
         gap = 4.0 * scale
         rightPad = 5.0 * scale
         totalWidth = 200.0 * scale
         totalHeight = (if (SpotifyHud.INSTANCE.showProgressLabels) 40.0 else 33.0) * scale
         coverY = y + (totalHeight - coverSize) / 2.0
         if (np != null) {
            val var10001: Optional = np.getArtwork()
            if (var10001 != null) {
               var70 = var10001.orElse(null) as java.lang.String
               return@label147
            }
         }

         var70 = null
      }

      val info: TextureInfo = this.cover(var70, 22)
      if (info != null) {
         this.drawTexture(graphics, info, x, coverY, coverSize, coverSize)
      }

      if (isPaused) {
         this.drawPauseOverlay(graphics, x, coverY, coverSize, false)
      }

      var textX: Double
      var textWidth: Double
      var titleY: Double
      var artistY: Double
      var var65: java.lang.String
      run label154@{
         textX = x + coverSize + gap
         textWidth = RangesKt.coerceAtLeast(x + totalWidth - rightPad - (x + coverSize + gap), 1.0)
         titleY = y + 2.0 * scale
         artistY = y + 12.0 * scale
         if (np != null) {
            val var10000: Optional = np.getTitle()
            if (var10000 != null) {
               var65 = var10000.orElse("")
               if (var65 != null) {
                  var65 = if (!StringsKt.isBlank(var65)) var65 else null
                  if (var65 != null) {
                     return@label154
                  }
               }
            }
         }

         var65 = "Not Playing"
      }

      run label159@{
         if (np != null) {
            val var66: Optional = np.getArtist()
            if (var66 != null) {
               var65 = var66.orElse("")
               if (var65 != null) {
                  var65 = if (!StringsKt.isBlank(var65)) var65 else null
                  if (var65 != null) {
                     return@label159
                  }
               }
            }
         }

         var65 = ""
      }

      run label164@{
         if (np != null) {
            val secondary: Optional = np.getAlbum()
            if (secondary != null) {
               val var49: java.lang.String = secondary.orElse("")
               if (var49 != null) {
                  val var54: java.lang.String = if (!StringsKt.isBlank(var49) && !StringsKt.equals(var49, var65, true)) var49 else null
                  if (var54 != null) {
                     var65 = var54
                     return@label164
                  }
               }
            }
         }

         var65 = ""
      }

      val var58: java.lang.Iterable = CollectionsKt.listOf(arrayOf(var65, var65))
      val var61: java.util.Collection = ArrayList()

      for (`element$iv$iv` in var58) {
         if ((`element$iv$iv` as java.lang.String).length() > 0) {
            var61.add(`element$iv$iv`)
         }
      }

      val var48: java.lang.String = CollectionsKt.joinToString$default(var61 as java.util.List, " · ", null, null, 0, null, null, 62, null)
      this.drawMarqueeString(graphics, "spotify.title", var65, textX, titleY, textWidth, scale, -1)
      if (var48.length() > 0) {
         this.drawMarqueeString(graphics, "spotify.secondary", var48, textX, artistY, textWidth, scale, -4144960)
      }

      val var52: Double = Math.max(2.0, 1.5 * scale)
      this.drawProgressBar(graphics, textX, y + totalHeight - var52 - 3.0 * scale, textWidth, var52, scale, np, SpotifyHud.INSTANCE.showProgressLabels, true)
   }

   private fun drawMiniPlayer(graphics: OwoUIGraphics, x: Double, y: Double, scale: Double, np: NowPlaying?, isPaused: Boolean) {
      var coverSize: Double
      var totalWidth: Double
      var totalHeight: Double
      var coverPad: Double
      var coverX: Double
      var coverY: Double
      var var58: java.lang.String
      run label138@{
         coverSize = 90.0 * scale
         totalWidth = 96.0 * scale
         totalHeight = (if (SpotifyHud.INSTANCE.showProgressLabels) 152.0 else 145.0) * scale
         coverPad = 3.0 * scale
         coverX = x + 3.0 * scale
         coverY = y + 3.0 * scale
         if (np != null) {
            val var10001: Optional = np.getArtwork()
            if (var10001 != null) {
               var58 = var10001.orElse(null) as java.lang.String
               return@label138
            }
         }

         var58 = null
      }

      val info: TextureInfo = this.cover(var58, 28)
      if (info != null) {
         this.drawTexture(graphics, info, coverX, coverY, coverSize, coverSize)
      }

      if (isPaused) {
         this.drawPauseOverlay(graphics, coverX, coverY, coverSize, true)
      }

      var textX: Double
      var textWidth: Double
      var baseTextY: Double
      var lineH: Double
      var var53: java.lang.String
      run label145@{
         textX = x + coverPad
         textWidth = RangesKt.coerceAtLeast(x + totalWidth - coverPad - (x + coverPad), 1.0)
         baseTextY = coverY + coverSize + 4.0 * scale
         lineH = (graphics.getTextHeight() + 1) * scale
         if (np != null) {
            val var10000: Optional = np.getTitle()
            if (var10000 != null) {
               var53 = var10000.orElse("")
               if (var53 != null) {
                  var53 = if (!StringsKt.isBlank(var53)) var53 else null
                  if (var53 != null) {
                     return@label145
                  }
               }
            }
         }

         var53 = "Not Playing"
      }

      run label150@{
         if (np != null) {
            val var54: Optional = np.getArtist()
            if (var54 != null) {
               var53 = var54.orElse("")
               if (var53 != null) {
                  var53 = if (!StringsKt.isBlank(var53)) var53 else null
                  if (var53 != null) {
                     return@label150
                  }
               }
            }
         }

         var53 = ""
      }

      run label155@{
         if (np != null) {
            val ny: Optional = np.getAlbum()
            if (ny != null) {
               val var44: java.lang.String = ny.orElse("")
               if (var44 != null) {
                  val var46: java.lang.String = if (!StringsKt.isBlank(var44) && !StringsKt.equals(var44, var53, true)) var44 else null
                  if (var46 != null) {
                     var53 = var46
                     return@label155
                  }
               }
            }
         }

         var53 = ""
      }

      this.drawMarqueeString(graphics, "spotify.title", var53, textX, baseTextY, textWidth, scale, -1)
      var var42: Double = baseTextY + lineH
      if (var53.length() > 0) {
         this.drawMarqueeString(graphics, "spotify.artist", var53, textX, var42, textWidth, scale, -4144960)
         var42 += lineH
      }

      if (var53.length() > 0) {
         this.drawMarqueeString(graphics, "spotify.album", var53, textX, var42, textWidth, scale, -7303024)
         var42 = var42 + lineH
      }

      val var47: Double = Math.max(3.0, 3.0 * scale)
      this.drawProgressBar(graphics, textX, y + totalHeight - var47 - coverPad, textWidth, var47, scale, np, SpotifyHud.INSTANCE.showProgressLabels, true)
   }

   private fun drawPauseOverlay(graphics: OwoUIGraphics, coverX: Double, coverY: Double, coverSize: Double, big: Boolean) {
      graphics.fill(coverX, coverY, coverX + coverSize, coverY + coverSize, -1728053248)
      if (graphics.getFont() != null) {
         this.drawScaledString(
            graphics,
            "❚❚",
            coverX + (coverSize - (double)graphics.getTextWidth("❚❚") * (if (big) coverSize / 28.0 else coverSize / 32.0)) / 2.0,
            coverY + (coverSize - (double)graphics.getTextHeight() * (if (big) coverSize / 28.0 else coverSize / 32.0)) / 2.0,
            if (big) coverSize / 28.0 else coverSize / 32.0,
            -1
         )
      }
   }

   private fun drawProgressBar(
      graphics: OwoUIGraphics,
      x: Double,
      y: Double,
      w: Double,
      h: Double,
      scale: Double,
      np: NowPlaying?,
      showLabels: Boolean = true,
      drawHandle: Boolean = true
   ) {
      var var38: Duration
      run label54@{
         if (np != null) {
            val var10000: Optional = np.getPosition()
            if (var10000 != null) {
               var38 = var10000.orElse(null) as Duration
               return@label54
            }
         }

         var38 = null
      }

      run label57@{
         if (np != null) {
            val var39: Optional = np.getDuration()
            if (var39 != null) {
               var38 = var39.orElse(null) as Duration
               return@label57
            }
         }

         var38 = null
      }

      val posSec: Long = if (var38 != null) RangesKt.coerceAtLeast(var38.getSeconds(), 0L) else 0L
      val durSec: Long = if (var38 != null) RangesKt.coerceAtLeast(var38.getSeconds(), 0L) else 0L
      val progress: Double = if (durSec > 0L) RangesKt.coerceIn((double)posSec / (double)durSec, 0.0, 1.0) else 0.0
      val color: Int = SpotifyHud.INSTANCE.currentBarColorArgb()
      if (showLabels && durSec > 0L) {
         val fillX: java.lang.String = this.formatTime(posSec)
         val total: java.lang.String = this.formatTime(durSec)
         val handleSize: Double = scale * 0.65
         val handleY: Double = y - graphics.getTextHeight() * (scale * 0.65) - 1.0 * scale
         this.drawScaledString(graphics, fillX, x, handleY, scale * 0.65, -5592406)
         this.drawScaledString(graphics, total, x + w - (double)graphics.getTextWidth(total) * handleSize, handleY, handleSize, -5592406)
      }

      graphics.fill(x, y, x + w, y + h, -935313344)
      val var34: Double = x + w * progress
      graphics.fill(x, y, x + w * progress, y + h, color)
      if (drawHandle) {
         val var35: Double = Math.max(3.0, 3.0 * scale)
         graphics.fill(var34 - var35 / 2.0, y + h / 2.0 - var35 / 2.0, var34 - var35 / 2.0 + var35, y + h / 2.0 - var35 / 2.0 + var35, color)
      }
   }

   private fun formatTime(seconds: Long): String {
      val var5: Array<Any> = arrayOf(seconds / (long)60, seconds % (long)60)
      val var10000: java.lang.String = java.lang.String.format("%d:%02d", Arrays.copyOf(var5, var5.length))
      return var10000
   }

   private fun cover(url: String?, radius: Int): TextureInfo? {
      var var9: TextureInfo
      run label38@{
         if (url != null) {
            val var10000: java.lang.String = if (!StringsKt.isBlank(url)) url else null
            if (var10000 != null) {
               var9 = ArtworkCache.get$default(ArtworkCache.INSTANCE, var10000, radius, false, false, 12, null)
               return@label38
            }
         }

         var9 = null
      }

      if (var9 != null) {
         this.lastCover = var9
      }

      var9 = var9
      if (var9 == null) {
         var9 = this.lastCover
      }

      return var9
   }

   private fun drawTexture(graphics: OwoUIGraphics, info: TextureInfo, x: Double, y: Double, w: Double, h: Double) {
      val loc: Identifier = info.getTextureLocation()
      if (info.getUsesResourceLocation() && loc != null) {
         graphics.drawTextureScaled(
            loc,
            x,
            y,
            w,
            h,
            0.0,
            0.0,
            (double)info.getTextureWidth(),
            (double)info.getTextureHeight(),
            (double)info.getTextureWidth(),
            (double)info.getTextureHeight(),
            true
         )
      } else if (info.getGlTextureId() > 0) {
         graphics.drawDynamicTexture(
            info.getGlTextureId(),
            x,
            y,
            w,
            h,
            0.0,
            0.0,
            (double)info.getTextureWidth(),
            (double)info.getTextureHeight(),
            (double)info.getTextureWidth(),
            (double)info.getTextureHeight()
         )
      }
   }

   private fun drawScaledString(graphics: OwoUIGraphics, text: String, x: Double, y: Double, scale: Double, color: Int) {
      graphics.push()
      graphics.translate((float)x, (float)y)
      graphics.scale((float)scale, (float)scale)
      val var10001: Font = graphics.getFont()
      graphics.drawString(var10001, text, 0.0, 0.0, color, true)
      graphics.pop()
   }

   private fun drawMarqueeString(graphics: OwoUIGraphics, id: String, text: String, x: Double, y: Double, maxWidthPx: Double, scale: Double, color: Int) {
      val var10000: Font = graphics.getFont()
      val textWidthFont: Double = graphics.getTextWidth(text)
      if (textWidthFont * scale <= maxWidthPx) {
         this.drawScaledString(graphics, text, x, y, scale, color)
      } else if (SpotifyHud.INSTANCE.enableMarquee) {
         val offsetFont: Double = MarqueeState.INSTANCE.pixelOffset(id, text, (int)textWidthFont, (int)(maxWidthPx / scale), 20)
         graphics.enableScissor(x, y, x + maxWidthPx, y + (double)graphics.getTextHeight() * scale)
         graphics.push()
         graphics.translate((float)x, (float)y)
         graphics.scale((float)scale, (float)scale)
         graphics.drawString(var10000, text, -offsetFont, 0.0, color, true)
         graphics.drawString(var10000, text, -offsetFont + textWidthFont + (double)20, 0.0, color, true)
         graphics.pop()
         graphics.disableScissor()
      } else {
         var gapFont: java.lang.String = text

         while (gapFont.length() > 0 && graphics.getTextWidth(gapFont + "…") * scale > maxWidthPx) {
            gapFont = StringsKt.dropLast(gapFont, 1)
         }

         this.drawScaledString(graphics, "$gapFont…", x, y, scale, color)
      }
   }

   private fun applySizingForCurrentLayout() {
      val layout: HudLayout = SpotifyHud.INSTANCE.layout
      val scaleF: Float = SpotifyHud.INSTANCE.hudScale.floatValue() + 0.5F
      val scaleKey: Int = (int)(scaleF * 100)
      val bg: DynamicBackground = SpotifyHud.INSTANCE.dynamicBackground
      val padW: Int = if (bg.isDynamic()) bg.getDynamicWidth() else 0
      val padH: Int = if (bg.isDynamic()) bg.getDynamicHeight() else 0
      val showLabels: Boolean = SpotifyHud.INSTANCE.showProgressLabels
      if (layout != this.lastLayout
         || scaleKey != this.lastScaleTimes100
         || padW != this.lastPadW
         || padH != this.lastPadH
         || !(showLabels == this.lastShowLabels)) {
         this.lastLayout = layout
         this.lastScaleTimes100 = scaleKey
         this.lastPadW = padW
         this.lastPadH = padH
         this.lastShowLabels = showLabels
         val var8: Pair = this.layoutDimensions(layout, showLabels)
         this.sizing(
            Sizing.Companion.fixed(RangesKt.coerceAtLeast((int)((var8.component1() as java.lang.Number).doubleValue() * (double)scaleF), 1) + padW * 2),
            Sizing.Companion.fixed(RangesKt.coerceAtLeast((int)((var8.component2() as java.lang.Number).doubleValue() * (double)scaleF), 1) + padH * 2)
         )
      }
   }

   private fun layoutDimensions(layout: HudLayout, showLabels: Boolean): Pair<Double, Double> {
      var var10000: Pair
      when (SpotifyRenderer.WhenMappings.$EnumSwitchMapping$0[layout.ordinal()]) {
         1 -> var10000 = TuplesKt.to(200.0, 32.0)
         2 -> var10000 = TuplesKt.to(200.0, if (showLabels) 40.0 else 33.0)
         3 -> var10000 = TuplesKt.to(96.0, if (showLabels) 152.0 else 145.0)
         else -> throw NoWhenBranchMatchedException()
      }

      return var10000
   }

   private fun paddingOffset(): Pair<Double, Double> {
      val bg: DynamicBackground = SpotifyHud.INSTANCE.dynamicBackground
      return if (bg.isDynamic()) TuplesKt.to((double)bg.getDynamicWidth(), (double)bg.getDynamicHeight()) else TuplesKt.to(0.0, 0.0)
   }

   public companion object {
      private const val COMPACT_WIDTH: Double = 200.0
      private const val COMPACT_HEIGHT: Double = 32.0
      private const val DETAILED_WIDTH: Double = 200.0
      private const val DETAILED_HEIGHT_WITH_LABELS: Double = 40.0
      private const val DETAILED_HEIGHT_NO_LABELS: Double = 33.0
      private const val LABEL_SCALE_RATIO: Double = 0.65
      private const val MINI_WIDTH: Double = 96.0
      private const val MINI_HEIGHT_WITH_LABELS: Double = 152.0
      private const val MINI_HEIGHT_NO_LABELS: Double = 145.0
      private const val MINI_COVER: Double = 90.0
      private const val COLOR_TITLE: Int = -1
      private const val COLOR_SECONDARY: Int = -4144960
      private const val COLOR_TERTIARY: Int = -7303024
      private const val COLOR_TIME: Int = -5592406
      private const val COLOR_BAR_TRACK: Int = -935313344
   }
}
