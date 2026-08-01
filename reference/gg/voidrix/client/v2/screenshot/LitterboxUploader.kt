package gg.voidrix.client.v2.screenshot

import gg.voidrix.compat.client.MCLogger
import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import kotlin.jvm.internal.SourceDebugExtension
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nLitterboxUploader.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LitterboxUploader.kt\ngg/voidrix/client/v2/screenshot/LitterboxUploader\n+ 2 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n*L\n1#1,89:1\n63#2:90\n63#2:91\n63#2:92\n63#2:93\n63#2:94\n63#2:95\n63#2:96\n60#2:97\n*S KotlinDebug\n*F\n+ 1 LitterboxUploader.kt\ngg/voidrix/client/v2/screenshot/LitterboxUploader\n*L\n29#1:90\n36#1:91\n59#1:92\n69#1:93\n72#1:94\n85#1:95\n19#1:96\n12#1:97\n*E\n"])
public object LitterboxUploader {
   private final val logger: Lazy<Logger> = LazyKt.lazy(LitterboxUploader$special$$inlined$lazyLogger$1.INSTANCE)
   private const val UPLOAD_URL: String = "https://litterbox.catbox.moe/resources/internals/api.php"

   private final val sslSocketFactory: SSLSocketFactory? by LazyKt.lazy({ 
      var ctx: SSLSocketFactory
      try {
         val var5: SSLContext = SSLContext.getInstance("TLSv1.2")
         var5.init(null, null, null)
         val e: Logger = logger.getValue() as Logger
         if (MCLogger.IS_DEBUG) {
            e.info("Initialized TLSv1.2 SSLContext")
         }

         ctx = var5.getSocketFactory()
      } catch (var4: Exception) {
         (logger.getValue() as Logger).error("Failed to create TLSv1.2 SSLContext", var4)
         ctx = null
      }

      ctx
   })
      private final get() {
         return sslSocketFactory$delegate.getValue() as SSLSocketFactory
      }


   public fun upload(imageBytes: ByteArray, fileName: String, time: String = "24h"): String? {
      var boundary: java.lang.String
      try {
         val var20: Logger = logger.getValue() as Logger
         val e: java.lang.String = "Uploading ${imageBytes.length} bytes to Litterbox (time=$time)..."
         if (MCLogger.IS_DEBUG) {
            var20.info(e)
         }

         boundary = "----Voidrix${System.currentTimeMillis()}"
         val var10000: URLConnection = URL("https://litterbox.catbox.moe/resources/internals/api.php").openConnection()
         val var22: HttpURLConnection = var10000 as HttpURLConnection
         if (var10000 as HttpURLConnection is HttpsURLConnection) {
            val var43: SSLSocketFactory = this.sslSocketFactory
            if (var43 != null) {
               (var22 as HttpsURLConnection).setSSLSocketFactory(var43)
               val `$i$f$voidrixDebug`: Logger = logger.getValue() as Logger
               if (MCLogger.IS_DEBUG) {
                  `$i$f$voidrixDebug`.info("Applied TLSv1.2 SSLSocketFactory")
               }
            }
         }

         var22.setRequestMethod("POST")
         var22.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
         var22.setDoOutput(true)
         var22.setConnectTimeout(15000)
         var22.setReadTimeout(60000)
         val var23: Closeable = var22.getOutputStream()
         var response: java.lang.Throwable = null

         try {
            val var27: OutputStream = var23 as OutputStream
            upload$lambda$2$writeField(var23 as OutputStream, boundary, "reqtype", "fileupload")
            upload$lambda$2$writeField(var27, boundary, "time", time)
            var var10001: ByteArray = ("--$boundary\r\nContent-Disposition: form-data; name=\"fileToUpload\"; filename=\"$fileName\"\r\nContent-Type: application/octet-stream\r\n\r\n")
               .getBytes(Charsets.UTF_8)
               var27.write(var10001)
            var27.write(imageBytes)
            var10001 = ("\r\n--$boundary--\r\n").getBytes(Charsets.UTF_8)
            var27.write(var10001)
         } catch (var17: java.lang.Throwable) {
            response = var17
            throw var17
         } finally {
            CloseableKt.closeFinally(var23, response)
         }

         val var24: Int = var22.getResponseCode()
         val var25: Logger = logger.getValue() as Logger
         val var29: java.lang.String = "Litterbox response code: $var24"
         if (MCLogger.IS_DEBUG) {
            var25.info(var29)
         }

         run label166@{
            val var30: InputStream = if (200 <= var24 && var24 < 300) var22.getInputStream() else var22.getErrorStream()
            if (var30 != null) {
               val var42: Reader = InputStreamReader(var30, Charsets.UTF_8)
               val var38: java.lang.String = TextStreamsKt.readText(if (var42 is BufferedReader) var42 as BufferedReader else BufferedReader(var42, 8192))
               if (var38 != null) {
                  var44 = StringsKt.trim(var38).toString()
                  return@label166
               }
            }

            var44 = null
         }

         if (var44 == null) {
            (logger.getValue() as Logger).error("Litterbox upload: no response body (HTTP $var24)")
            return null
         }

         val var31: Logger = logger.getValue() as Logger
         var `message$ivx`: java.lang.String = "Litterbox response: $var44"
         if (MCLogger.IS_DEBUG) {
            var31.info(`message$ivx`)
         }

         val var45: java.lang.String
         if (200 <= var24 && var24 < 300 && StringsKt.startsWith$default(var44, "https://", false, 2, null)) {
            val var32: Logger = logger.getValue() as Logger
            `message$ivx` = "Litterbox upload success: $var44"
            if (MCLogger.IS_DEBUG) {
               var32.info(`message$ivx`)
            }

            var45 = var44
         } else {
            (logger.getValue() as Logger).error("Litterbox upload failed (HTTP $var24): $var44")
            var45 = null
         }

         boundary = var45
      } catch (var19: Exception) {
         (logger.getValue() as Logger).error("Litterbox upload error", var19)
         boundary = null
      }

      return boundary
   }

   public fun uploadFile(file: File, time: String = "24h"): String? {
      val `$this$voidrixDebug$iv`: Logger = logger.getValue() as Logger
      val `message$iv`: java.lang.String = "Uploading file: ${file.getAbsolutePath()} (${file.length()} bytes)"
      if (MCLogger.IS_DEBUG) {
         `$this$voidrixDebug$iv`.info(`message$iv`)
      }

      val var10001: ByteArray = FilesKt.readBytes(file)
      val var10002: java.lang.String = file.getName()
      return this.upload(var10001, var10002, time)
   }

   @JvmStatic
   fun {
      val `$this$lazyLogger$iv`: Any = INSTANCE
   }
}
