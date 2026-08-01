package gg.voidrix.client.v2.modules.spotify

import com.mojang.blaze3d.platform.NativeImage
import gg.voidrix.compat.resource.DynamicTextureCache
import gg.voidrix.compat.resource.NativeImagePixelsKt
import gg.voidrix.compat.resource.TextureInfo
import gg.voidrix.compat.task.CoroutineScopesKt
import java.awt.Color
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentHashMap.KeySetView
import kotlin.collections.MutableMap.MutableEntry
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.BuildersKt
import org.endlesssource.mediainterface.api.ArtworkDecoder

@SourceDebugExtension(["SMAP\nArtworkCache.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ArtworkCache.kt\ngg/voidrix/client/v2/modules/spotify/ArtworkCache\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,446:1\n1#2:447\n384#3,7:448\n1999#4,14:455\n1869#4,2:469\n1869#4,2:471\n*S KotlinDebug\n*F\n+ 1 ArtworkCache.kt\ngg/voidrix/client/v2/modules/spotify/ArtworkCache\n*L\n196#1:448,7\n209#1:455,14\n431#1:469,2\n438#1:471,2\n*E\n"])
public object ArtworkCache {
   private const val RESOLVED_MAX: Int = 64
   private const val DOMINANT_MAX: Int = 128
   private const val PENDING_MAX: Int = 8
   private final val resolved: MutableMap<gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key, TextureInfo>
   private final val readyPayload: ConcurrentHashMap<gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key, Any> = ConcurrentHashMap()
   private final val inFlight: KeySetView<gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key, Boolean> = ConcurrentHashMap.newKeySet()
   private final val dominantByUrl: MutableMap<String, Int>

   public fun get(url: String?, radius: Int, allCorners: Boolean = true, circular: Boolean = false): TextureInfo? {
      if (url != null) {
         var var10000: java.lang.String = if (!StringsKt.isBlank(url)) url else null
         if (var10000 != null) {
            val var14: ArtworkCache.Key = ArtworkCache.Key(var10000, radius, if (circular) 16 else (if (allCorners) 15 else 0))
            val var15: TextureInfo = resolved.get(var14)
            if (var15 != null) {
               return var15
            }

            var10000 = (java.lang.String)readyPayload.remove(var14)
            if (var10000 != null) {
               val info: TextureInfo = INSTANCE.uploadReady(var14, var10000)
               if (info != null) {
                  ArtworkCacheKt.access$getLogger$p()
                     .info(
                        "Artwork uploaded: url='{}' size={}x{} tex={}",
                        arrayOf(
                           StringsKt.take(var14.url, 60),
                           info.getTextureWidth(),
                           info.getTextureHeight(),
                           if (info.getUsesResourceLocation())
                              info.getTextureLocation() as java.lang.Comparable
                              else
                              ("gl#${info.getGlTextureId()}") as java.lang.Comparable
                        )
                     )
                     resolved.put(var14, info)
                  return info
               }

               ArtworkCacheKt.access$getLogger$p().warn("Artwork upload failed for url='{}'", StringsKt.take(var14.url, 60))
            }

            if (inFlight.add(var14)) {
               ArtworkCacheKt.access$getLogger$p()
                  .info("Artwork decode queued: url='{}' prefix='{}'", StringsKt.take(var14.url, 60), StringsKt.take(var14.url, 32))
                  BuildersKt.launch$default(CoroutineScopesKt.getBackgroundCoroutineScope(), null, null, {
                  // $VF: Could not decompile lambda - root function was not found. Is this a suspend lambda?
               } as Function2, 3, null)
            }

            return null
         }
      }

      return null
   }

   private fun decodeAndPrepare(key: gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key): Any? {
      val bytesOpt: Optional = ArtworkDecoder.decodeBytes(key.url)
      if (!bytesOpt.isPresent()) {
         return null
      } else {
         val isCircular: Closeable = ByteArrayInputStream(bytesOpt.get() as ByteArray)
         var var5: java.lang.Throwable = null

         var var16: NativeImage
         try {
            var16 = NativeImage.read(isCircular as ByteArrayInputStream)
         } catch (var12: java.lang.Throwable) {
            var5 = var12
            throw var12
         } finally {
            CloseableKt.closeFinally(isCircular, var5)
         }

         val image: NativeImage = var16

         try {
            val var10000: java.util.Map = dominantByUrl
            val var10001: java.lang.String = key.url
            var10000.put(var10001, this.extractDominantArgb(image))
            if ((key.cornerMask and 16) != 0) {
               this.applyCircularMask(image)
            } else if (key.radius > 0) {
               this.applyRoundedCorners(
                  image, key.radius, (key.cornerMask and 1) != 0, (key.cornerMask and 2) != 0, (key.cornerMask and 4) != 0, (key.cornerMask and 8) != 0
               )
            }

            var14 = image
         } catch (var11: java.lang.Throwable) {
            var14 = var16
         }

         return var14
      }
   }

   private fun uploadReady(key: gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key, payload: Any): TextureInfo? {
      val var10000: java.lang.String = key.cacheId()
      return DynamicTextureCache.getOrLoadRawTexture(var10000, payload as NativeImage)
   }

   private fun extractDominantArgb(img: NativeImage): Int {
      val w: Int = img.getWidth()
      val h: Int = img.getHeight()
      val step: Int = Math.max(1, Math.max(w, h) / 64)
      val buckets: HashMap = HashMap(64)
      val hsb: FloatArray = FloatArray(3)

      // $VF: Unable to resugar Kotlin loop from Java for loop
      var y: Int = 0
      while (true) {
         if (y < h) break
         // $VF: Unable to resugar Kotlin loop from Java for loop
         var winner: Int = 0
         while (true) {
            if (winner < w) break
            val wgt: Int = NativeImagePixelsKt.getArgbPixel(img, winner, y)
            if ((wgt ushr 24 and 255) >= 200) {
               val r: Int = wgt ushr 16 and 255
               val g: Int = wgt ushr 8 and 255
               val b: Int = wgt and 255
               Color.RGBtoHSB(r, g, wgt and 255, hsb)
               val `e$iv`: Float = hsb[0]
               val `v$iv`: Float = hsb[1]
               val var18: Float = hsb[2]
               if (hsb[2] >= 0.2F && hsb[2] <= 0.95F && `v$iv` >= 0.2F) {
                  val bucket: Int = (int)(`e$iv` * 12)
                  val wgtx: java.util.Map = buckets
                  val `key$iv`: Any = bucket
                  val `value$iv`: Any = wgtx.get(`key$iv`)
                  val var10000: Any
                  if (`value$iv` == null) {
                     val var43: Any = DoubleArray(4)
                     wgtx.put(`key$iv`, var43)
                     var10000 = var43
                  } else {
                     var10000 = `value$iv`
                  }

                  val acc: DoubleArray = var10000 as DoubleArray
                  val var42: Double = `v$iv` * var18
                  acc[0] += `v$iv` * var18
                  acc[1] += (double)r * (`v$iv` * var18)
                  acc[2] += g * var42
                  acc[3] += b * var42
               }
            }

            winner += step
         }

         y += step
      }

      if (buckets.isEmpty()) {
         return -16729344
      } else {
         val var44: java.util.Collection = buckets.values()
         val var30: java.util.Iterator = var44.iterator()
         val var45: Any
         if (!var30.hasNext()) {
            var45 = null
         } else {
            var var32: Any = var30.next()
            if (!var30.hasNext()) {
               var45 = var32
            } else {
               var var35: Double = (var32 as DoubleArray)[0]

               do {
                  val var38: Any = var30.next()
                  val var40: Double = (var38 as DoubleArray)[0]
                  if (java.lang.Double.compare(var35, (var38 as DoubleArray)[0]) < 0) {
                     var32 = var38
                     var35 = var40
                  }
               } while (var30.hasNext())

               var45 = var32
            }
         }

         val var28: Double = RangesKt.coerceAtLeast((var45 as DoubleArray)[0], 1.0)
         return -16777216 or RangesKt.coerceIn((int)((var45 as DoubleArray)[1] / var28), 0, 255) shl 16 or RangesKt.coerceIn(
            (int)((var45 as DoubleArray)[2] / var28), 0, 255
         ) shl 8 or RangesKt.coerceIn((int)((var45 as DoubleArray)[3] / var28), 0, 255)
      }
   }

   private fun applyCircularMask(img: NativeImage) {
      val w: Int = img.getWidth()
      val h: Int = img.getHeight()
      val cx: Double = w / 2.0
      val cy: Double = h / 2.0
      val r: Double = Math.min(w, h) / 2.0
      val r2: Double = r * r

      repeat(h) { y ->
         repeat(w) { x ->
            if ((x + 0.5 - cx) * (x + 0.5 - cx) + (y + 0.5 - cy) * (y + 0.5 - cy) > r2) {
               NativeImagePixelsKt.setArgbPixel(img, x, y, 0)
            }
         }
      }
   }

   private fun applyRoundedCorners(img: NativeImage, radius: Int, topLeft: Boolean, topRight: Boolean, bottomLeft: Boolean, bottomRight: Boolean) {
      val w: Int = img.getWidth()
      val h: Int = img.getHeight()
      val y: Int = Math.min(Math.min(radius, w / 2), h / 2)
      val effective: Int = if (w < 250) y / 3 else y
      if ((if (w < 250) y / 3 else y) > 0) {
         val r2: Int = effective * effective

         repeat(h) { var16 ->
            repeat(w) { var17 ->
               if (if (topLeft && var17 < effective && var16 < effective)
                  (effective - var17) * (effective - var17) + (effective - var16) * (effective - var16) > r2
                  else
                  (
                     if (topRight && var17 >= w - effective && var16 < effective)
                        (var17 - (w - effective)) * (var17 - (w - effective)) + (effective - var16) * (effective - var16) > r2
                        else
                        (
                           if (bottomLeft && var17 < effective && var16 >= h - effective)
                              (effective - var17) * (effective - var17) + (var16 - (h - effective)) * (var16 - (h - effective)) > r2
                              else
                              bottomRight
                                 && var17 >= w - effective
                                 && var16 >= h - effective
                                 && (var17 - (w - effective)) * (var17 - (w - effective)) + (var16 - (h - effective)) * (var16 - (h - effective)) > r2
                        )
                  )) {
                  NativeImagePixelsKt.setArgbPixel(img, var17, var16, 0)
               }
            }
         }
      }
   }

   public fun dominantColorFor(url: String?): Int? {
      return if (url as java.lang.CharSequence == null || StringsKt.isBlank(url)) null else dominantByUrl.get(url)
   }

   private fun trimPendingPayloads(keep: gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key) {
      if (readyPayload.size() > 8) {
         var var10000: Any = ArrayList(readyPayload.keySet()).iterator()
         val var2: java.util.Iterator = (java.util.Iterator)var10000

         while (var2.hasNext()) {
            val key: ArtworkCache.Key = var2.next() as ArtworkCache.Key
            if (readyPayload.size() <= 8) {
               break
            }

            if (!(key == keep)) {
               var10000 = readyPayload.remove(key)
               if (var10000 != null) {
                  var10000 = var10000 as? NativeImage
                  if ((var10000 as? NativeImage) != null) {
                     var10000.close()
                  }
               }
            }
         }
      }
   }

   public fun evictAll() {
      synchronized (resolved) {
         for (`element$iv` in resolved.keySet()) {
            DynamicTextureCache.invalidateByKey((`element$iv` as ArtworkCache.Key).cacheId())
         }

         resolved.clear()
      }

      val var10000: java.util.Collection = readyPayload.values()

      for (var18 in var10000) {
         val var21: NativeImage = var18 as? NativeImage
         if ((var18 as? NativeImage) != null) {
            var21.close()
         }
      }

      readyPayload.clear()
      inFlight.clear()
      synchronized (dominantByUrl) {
         dominantByUrl.clear()
      }
   }

   @JvmStatic
   fun {
      var var10000: java.util.Map = Collections.synchronizedMap(object : LinkedHashMap<gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key, TextureInfo> {
         protected override fun removeEldestEntry(eldest: MutableEntry<gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key, TextureInfo>): Boolean {
            if (this.size() <= 64) {
               false
            } else {
               DynamicTextureCache.invalidateByKey((eldest.getKey() as ArtworkCache.Key).cacheId())
               true
            }
         }
      })
      resolved = var10000
      var10000 = Collections.synchronizedMap(object : LinkedHashMap<String, Int> {
         protected override fun removeEldestEntry(eldest: MutableEntry<String, Int>): Boolean {
            this.size() > 128
         }
      })
      dominantByUrl = var10000
   }

   private data class Key(url: String, radius: Int, cornerMask: Int) {
      public final val url: String
      public final val radius: Int
      public final val cornerMask: Int

      init {
         this.url = url
         this.radius = radius
         this.cornerMask = cornerMask
      }

      public fun cacheId(): String {
         val var10000: java.lang.String = java.lang.Long.toString((long)this.url.hashCode() and 4294967295L, CharsKt.checkRadix(16))
         return "spotify/$var10000_r${this.radius}_m${this.cornerMask}"
      }

      public operator fun component1(): String {
         return this.url
      }

      public operator fun component2(): Int {
         return this.radius
      }

      public operator fun component3(): Int {
         return this.cornerMask
      }

      public fun copy(url: String = this.url, radius: Int = this.radius, cornerMask: Int = this.cornerMask): gg.voidrix.client.v2.modules.spotify.ArtworkCache.Key {
         return ArtworkCache.Key(url, radius, cornerMask)
      }

      public override fun toString(): String {
         return "Key(url=${this.url}, radius=${this.radius}, cornerMask=${this.cornerMask})"
      }

      public override fun hashCode(): Int {
         return (this.url.hashCode() * 31 + Integer.hashCode(this.radius)) * 31 + Integer.hashCode(this.cornerMask)
      }

      public override operator fun equals(other: Any?): Boolean {
         label34@
         if (this === other) {
            return true
         } else {
            return other is ArtworkCache.Key
               && this.url == (other as ArtworkCache.Key).url
               && this.radius == (other as ArtworkCache.Key).radius
               && this.cornerMask == (other as ArtworkCache.Key).cornerMask
            }
      }
   }
}
