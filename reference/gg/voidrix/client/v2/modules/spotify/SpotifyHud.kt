package gg.voidrix.client.v2.modules.spotify

import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.AbstractHud
import gg.voidrix.ui.api.hud.AnchorPoint
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.IContentBackground
import gg.voidrix.ui.api.hud.IDynamicBackground
import gg.voidrix.ui.api.hud.IDynamicBackgroundKt
import gg.voidrix.ui.api.value.BooleanValue
import gg.voidrix.ui.api.value.ColorAttributeValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixBackgroundPicker
import gg.voidrix.ui.v2.hud.AnchorPointPosition
import gg.voidrix.ui.v2.hud.Background
import java.awt.Color
import java.util.Optional
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import org.endlesssource.mediainterface.api.MediaSession
import org.endlesssource.mediainterface.api.MediaTransportControls
import org.endlesssource.mediainterface.api.NowPlaying
import org.endlesssource.mediainterface.api.PlaybackState
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nSpotifyHud.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SpotifyHud.kt\ngg/voidrix/client/v2/modules/spotify/SpotifyHud\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,237:1\n1#2:238\n*E\n"])
public object SpotifyHud : AbstractHud("SpotifyOverlay", false, false, false, 10), IContentBackground {
   @Category(name = "Layout")
   @NotNull
   public final var layout: HudLayout by ValueApiKt.enum$default(HudLayout.DETAILED, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return layout$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as HudLayout
      }

      public final set(<set-?>) {
         layout$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Layout")
   @NotNull
   public open var hudScale: Number by ValueApiKt.numeric$default(0.7F, RangesKt.rangeTo(0.5F, 3.0F) as ClosedRange, 0.1F, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public open get() {
         return hudScale$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Number
      }

      public open set(<set-?>) {
         hudScale$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Layout")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   @Category(name = "Appearance")
   @NotNull
   public open var background: Background by ValueApiKt.attribute$default({ 
      Background(null, null, 0.0F, 0.0F, 0.0F, null, 63, null)
   }, null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      VoidrixBackgroundPicker(INSTANCE.background, null) as UIComponent
   }, 14, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
      public open get() {
         return background$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as Background
      }

      public open set(<set-?>) {
         background$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   @Category(name = "Appearance")
   @NotNull
   public final var useAlbumColor: Boolean by ValueApiKt.boolean$default(true, "Album Color", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return useAlbumColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         useAlbumColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   @Category(name = "Appearance")
   @NotNull
   public final var artworkResolution: Number by ValueApiKt.numeric$default(
         128,
         IntRange(16, 512) as ClosedRange,
         16,
         "Cover Resolution",
         TextKt.getLiteral("Native album-art downscale target in pixels. Lower = lighter; 128 is sharp for the HUD.") as Component,
         null,
         32,
         null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return artworkResolution$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Number
      }

      public final set(<set-?>) {
         artworkResolution$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   @Category(name = "Appearance")
   @NotNull
   public final var barColor: Color
      public final get() {
         return barColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as Color
      }

      public final set(<set-?>) {
         barColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[6], var1)
      }


   @Category(name = "Behavior")
   @NotNull
   public final var autoHide: Boolean by ValueApiKt.boolean$default(true, "Auto Hide", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return autoHide$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         autoHide$delegate.setValue(this as ValueHolder, $$delegatedProperties[7], var1)
      }


   @Category(name = "Behavior")
   @NotNull
   public final var enableMarquee: Boolean by ValueApiKt.boolean$default(true, "Marquee", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return enableMarquee$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         enableMarquee$delegate.setValue(this as ValueHolder, $$delegatedProperties[8], var1)
      }


   @Category(name = "Behavior")
   @NotNull
   public final val showProgressLabels: Boolean
      public final get() {
         return showProgressLabels$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.Boolean
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceSpotify: Boolean by ValueApiKt.boolean$default(true, "Spotify", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
         public final get() {
         return sourceSpotify$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceSpotify$delegate.setValue(this as ValueHolder, $$delegatedProperties[10], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceAppleMusic: Boolean by ValueApiKt.boolean$default(true, "Apple Music", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[11])
         public final get() {
         return sourceAppleMusic$delegate.getValue(this as ValueHolder, $$delegatedProperties[11]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceAppleMusic$delegate.setValue(this as ValueHolder, $$delegatedProperties[11], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceYouTube: Boolean by ValueApiKt.boolean$default(true, "YouTube", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[12])
         public final get() {
         return sourceYouTube$delegate.getValue(this as ValueHolder, $$delegatedProperties[12]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceYouTube$delegate.setValue(this as ValueHolder, $$delegatedProperties[12], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceAmazon: Boolean by ValueApiKt.boolean$default(true, "Amazon Music", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[13])
         public final get() {
         return sourceAmazon$delegate.getValue(this as ValueHolder, $$delegatedProperties[13]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceAmazon$delegate.setValue(this as ValueHolder, $$delegatedProperties[13], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceSoundCloud: Boolean by ValueApiKt.boolean$default(true, "SoundCloud", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[14])
         public final get() {
         return sourceSoundCloud$delegate.getValue(this as ValueHolder, $$delegatedProperties[14]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceSoundCloud$delegate.setValue(this as ValueHolder, $$delegatedProperties[14], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceTidal: Boolean by ValueApiKt.boolean$default(true, "Tidal", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[15])
         public final get() {
         return sourceTidal$delegate.getValue(this as ValueHolder, $$delegatedProperties[15]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceTidal$delegate.setValue(this as ValueHolder, $$delegatedProperties[15], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceCider: Boolean by ValueApiKt.boolean$default(true, "Cider", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[16])
         public final get() {
         return sourceCider$delegate.getValue(this as ValueHolder, $$delegatedProperties[16]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceCider$delegate.setValue(this as ValueHolder, $$delegatedProperties[16], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceDeezer: Boolean by ValueApiKt.boolean$default(true, "Deezer", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[17])
         public final get() {
         return sourceDeezer$delegate.getValue(this as ValueHolder, $$delegatedProperties[17]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceDeezer$delegate.setValue(this as ValueHolder, $$delegatedProperties[17], var1)
      }


   @Category(name = "Sources")
   @NotNull
   public final var sourceOther: Boolean by ValueApiKt.boolean$default(
         false,
         "Allow Other",
         TextKt.getLiteral("Match any music app not covered by the toggles above (VLC, foobar2000, Pretzel, ...).") as Component,
         null,
         8,
         null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[18])
         public final get() {
         return sourceOther$delegate.getValue(this as ValueHolder, $$delegatedProperties[18]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         sourceOther$delegate.setValue(this as ValueHolder, $$delegatedProperties[18], var1)
      }


   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.TOP_CENTER, 0.5, 0.05, null, 8, null)
         }
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   public open fun onEnable() {
      super.onEnable()
      SpotifyMediaState.INSTANCE.initialize(this.artworkResolution.intValue())
   }

   public open fun onDisable() {
      super.onDisable()
      SpotifyMediaState.INSTANCE.shutdown()
      ArtworkCache.INSTANCE.evictAll()
   }

   public fun tickMediaState() {
      SpotifyMediaState.INSTANCE.refresh(this.enabledSourceMatches(), this.sourceOther, this.artworkResolution.intValue())
   }

   private fun enabledSourceMatches(): List<String> {
      val var1: java.util.List = CollectionsKt.createListBuilder()
      if (INSTANCE.sourceSpotify) {
         var1.add("Spotify")
      }

      if (INSTANCE.sourceAppleMusic) {
         var1.add("AppleMusic")
         var1.add("Apple")
         var1.add("Music")
      }

      if (INSTANCE.sourceYouTube) {
         var1.add("YouTube")
         var1.add("Youtube")
      }

      if (INSTANCE.sourceAmazon) {
         var1.add("Amazon")
      }

      if (INSTANCE.sourceSoundCloud) {
         var1.add("SoundCloud")
      }

      if (INSTANCE.sourceTidal) {
         var1.add("Tidal")
      }

      if (INSTANCE.sourceCider) {
         var1.add("Cider")
      }

      if (INSTANCE.sourceDeezer) {
         var1.add("Deezer")
      }

      return CollectionsKt.build(var1)
   }

   public fun currentBarColorArgb(): Int {
      if (this.useAlbumColor) {
         var var6: java.lang.String
         run label22@{
            val var10000: NowPlaying = SpotifyMediaState.INSTANCE.currentNowPlaying
            if (var10000 != null) {
               val var5: Optional = var10000.getArtwork()
               if (var5 != null) {
                  var6 = var5.orElse(null) as java.lang.String
                  return@label22
               }
            }

            var6 = null
         }

         val var2: Int = ArtworkCache.INSTANCE.dominantColorFor(var6)
         if (var2 != null) {
            return var2.intValue()
         }
      }

      return -16777216 or this.barColor.getRGB() and 16777215
   }

   public fun currentPlaybackState(): PlaybackState {
      val var10000: MediaSession = SpotifyMediaState.INSTANCE.currentSession
      if (var10000 != null) {
         val var1: MediaTransportControls = var10000.getControls()
         if (var1 != null) {
            val var2: PlaybackState = var1.getPlaybackState()
            if (var2 != null) {
               return var2
            }
         }
      }

      return PlaybackState.UNKNOWN
   }

   public open fun shouldStopRenderExecution(): Boolean {
      if (!this.autoHide) {
         return false
      } else {
         val s: PlaybackState = this.currentPlaybackState()
         return s === PlaybackState.PAUSED || s === PlaybackState.STOPPED || s === PlaybackState.UNKNOWN
      }
   }

   public open fun hudComponent(): UIComponent {
      return SpotifyRenderer().id(this.getName())
   }

   @JvmStatic
   fun {
      val var3: ColorAttributeValue = ValueApiKt.attribute$default(Color(0, 187, 0), false, "Bar Color", null, null, 24, null)
      var3.setUiCondition({ 
         !INSTANCE.useAlbumColor
      })
      barColor$delegate = var3.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
      val var4: BooleanValue = ValueApiKt.boolean$default(true, "Show Time Labels", null, null, 12, null)
      var4.setUiCondition({ 
         INSTANCE.layout != HudLayout.COMPACT
      })
      showProgressLabels$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         if (INSTANCE.isEnabled()) {
            INSTANCE.tickMediaState()
         }

         Unit.INSTANCE
      })
   }
}
