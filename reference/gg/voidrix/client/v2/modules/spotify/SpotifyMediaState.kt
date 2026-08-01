package gg.voidrix.client.v2.modules.spotify

import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.event.ClientEvents
import java.time.Duration
import java.util.Optional
import kotlin.jvm.internal.SourceDebugExtension
import org.endlesssource.mediainterface.SystemMediaFactory
import org.endlesssource.mediainterface.api.MediaSession
import org.endlesssource.mediainterface.api.NowPlaying
import org.endlesssource.mediainterface.api.SystemMediaInterface
import org.endlesssource.mediainterface.api.SystemMediaOptions
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nSpotifyMediaState.kt\nKotlin\n*S Kotlin\n*F\n+ 1 SpotifyMediaState.kt\ngg/voidrix/client/v2/modules/spotify/SpotifyMediaState\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,182:1\n295#2:183\n1761#2,3:184\n296#2:187\n1#3:188\n*S KotlinDebug\n*F\n+ 1 SpotifyMediaState.kt\ngg/voidrix/client/v2/modules/spotify/SpotifyMediaState\n*L\n165#1:183\n167#1:184,3\n165#1:187\n*E\n"])
public object SpotifyMediaState {
   private final val logger: Logger = MCLogger.getLogger("SpotifyMediaState")
   private final var media: SystemMediaInterface?

   public final var currentSession: MediaSession?
      private set

   public final var currentNowPlaying: NowPlaying?
      private set

   public final var lastSnapshotAt: Long
      private set

   private final var artworkMaxSize: Int = 128
   private final var appliedArtworkMaxSize: Int = -1
   private final var reinitInFlight: Boolean

   public final val isRunning: Boolean
      public final get() {
         return media != null
      }


   public fun initialize(artworkMaxSize: Int) {
      artworkMaxSize = artworkMaxSize
      if (media == null) {
         val `$this$initialize_u24lambda_u243`: Thread = Thread({ 
            INSTANCE.createInterface()
         }, "SpotifyMediaState-init")
         `$this$initialize_u24lambda_u243`.setDaemon(true)
         `$this$initialize_u24lambda_u243`.start()
      }
   }

   private fun createInterface() {
      try {
         val e: Int = artworkMaxSize
         val created: SystemMediaInterface = SystemMediaFactory.createSystemInterface(
            SystemMediaOptions.defaults().withArtworkMaxSize(e).withSessionUpdateInterval(Duration.ofMillis(1000L))
         )
         media = created
         appliedArtworkMaxSize = e
         logger.info("SpotifyMediaState: media interface created (eventDriven={}, artworkMaxSize={})", created.isEventDrivenEnabled(), e)
      } catch (var4: UnsupportedOperationException) {
         logger.warn("SpotifyMediaState: platform not supported — {}", var4.getMessage())
      } catch (var5: java.lang.Throwable) {
         logger.error("SpotifyMediaState: failed to initialize media interface", var5)
      }
   }

   public fun shutdown() {
      if (media != null) {
         val m: SystemMediaInterface = media
         media = null
         currentSession = null
         currentNowPlaying = null
         val `$this$shutdown_u24lambda_u245`: Thread = Thread({ 
            try {
               `$m`.close()
            } catch (var2: java.lang.Throwable) {
               logger.warn("SpotifyMediaState: error while closing media interface: {}", var2.getMessage())
            }
         }, "SpotifyMediaState-shutdown")
         `$this$shutdown_u24lambda_u245`.setDaemon(true)
         `$this$shutdown_u24lambda_u245`.start()
      }
   }

   public fun refresh(sourceMatches: List<String>, allowOther: Boolean, artworkMaxSize: Int) {
      artworkMaxSize = artworkMaxSize
      this.reconcileArtworkSize()
      if (media != null) {
         var var6: NowPlaying
         run label22@{
            val session: MediaSession = this.pickSession(media, sourceMatches, allowOther)
            currentSession = session
            if (session != null) {
               val var10000: Optional = session.getNowPlaying()
               if (var10000 != null) {
                  var6 = var10000.orElse(null) as NowPlaying
                  return@label22
               }
            }

            var6 = null
         }

         currentNowPlaying = var6
         lastSnapshotAt = System.currentTimeMillis()
      }
   }

   private fun reconcileArtworkSize() {
      if (media != null && !reinitInFlight && artworkMaxSize != appliedArtworkMaxSize) {
         reinitInFlight = true
         val old: SystemMediaInterface = media
         media = null
         val `$this$reconcileArtworkSize_u24lambda_u247`: Thread = Thread({ 
            try {
               if (`$old` != null) {
                  `$old`.close()
               }
            } catch (var2: java.lang.Throwable) {
               logger.warn("SpotifyMediaState: error closing interface during resize: {}", var2.getMessage())
            }

            INSTANCE.createInterface()
            reinitInFlight = false
         }, "SpotifyMediaState-resize")
         `$this$reconcileArtworkSize_u24lambda_u247`.setDaemon(true)
         `$this$reconcileArtworkSize_u24lambda_u247`.start()
      }
   }

   private fun pickSession(m: SystemMediaInterface, sourceMatches: List<String>, allowOther: Boolean): MediaSession? {
      val sessions: java.util.List = m.getAllSessions()
      if (sessions.isEmpty()) {
         return null
      } else {
         if (!sourceMatches.isEmpty()) {
            var var20: Any
            run label80@{
               for (`element$iv` in sessions) {
                  var var10000: java.lang.String = (`element$iv` as MediaSession).getApplicationName()
                  if (var10000 == null) {
                     var10000 = ""
                  }

                  val name: java.lang.String = var10000
                  val `$this$any$iv`: java.lang.Iterable = sourceMatches
                  var var19: Boolean
                  if (sourceMatches is java.util.Collection && (sourceMatches as java.util.Collection).isEmpty()) {
                     var19 = false
                  } else {
                     val var15: java.util.Iterator = `$this$any$iv`.iterator()

                     while (true) {
                        if (!var15.hasNext()) {
                           var19 = false
                           break
                        }

                        if (StringsKt.contains(name, var15.next() as java.lang.String, true)) {
                           var19 = true
                           break
                        }
                     }
                  }

                  if (var19) {
                     var20 = `element$iv`
                     return@label80
                  }
               }

               var20 = null
            }

            val matched: MediaSession = var20 as MediaSession
            if (var20 as MediaSession != null) {
               return matched
            }
         }

         return if (!allowOther) null else m.getActiveSession().orElseGet({ 
            CollectionsKt.firstOrNull(`$sessions`) as MediaSession
         })
      }
   }

   public fun debugCurrentTrack() {
      if (currentNowPlaying != null) {
         val np: NowPlaying = currentNowPlaying
         logger.info("[Spotify] {} - {}", currentNowPlaying.getTitle().orElse("?"), np.getArtist().orElse("?"))
      }
   }

   @JvmStatic
   fun {
      ClientEvents.INSTANCE.getClientStoppingEvent().listen({ it: Unit ->
         val var1: SpotifyMediaState = INSTANCE

         try {
            var1.shutdown()
            val var6: Any = Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
         } catch (var4: java.lang.Throwable) {
            val `$this$lambda_u241_u24lambda_u240`: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
         }

         Unit.INSTANCE
      })
   }
}
