package gg.norisk.client.v2.modules.discord

import dev.cbyrne.kdiscordipc.core.socket.Socket
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLClassLoader
import java.util.zip.GZIPInputStream
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nKdiscordIpcSocketLoader.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KdiscordIpcSocketLoader.kt\ngg/norisk/client/v2/modules/discord/KdiscordIpcSocketLoader\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,84:1\n1#2:85\n*E\n"])
public object KdiscordIpcSocketLoader {
   private const val BUNDLE_RESOURCE: String = "/gg/norisk/lib/kdiscordipc/bundle.bin"

   private final val bundleFile: File by LazyKt.lazy({ 
      val var10000: InputStream = INSTANCE.getClass().getResourceAsStream("/gg/norisk/lib/kdiscordipc/bundle.bin")
      if (var10000 == null) {
         throw IllegalStateException("missing isolated junixsocket bundle at /gg/norisk/lib/kdiscordipc/bundle.bin".toString())
      } else {
         val tmp: File = File.createTempFile("nrc-kdiscordipc-", ".jar")
         tmp.deleteOnExit()
         val var2: Closeable = var10000
         var var3: java.lang.Throwable = null

         try {
            val var7: Closeable = GZIPInputStream(var2 as InputStream)
            var var8: java.lang.Throwable = null

            try {
               val gz: GZIPInputStream = var7 as GZIPInputStream
               val var12: Closeable = FileOutputStream(tmp)
               var var13: java.lang.Throwable = null

               try {
                  val var41: Long = ByteStreamsKt.copyTo$default(gz, var12 as FileOutputStream, 0, 2, null)
               } catch (var35: java.lang.Throwable) {
                  var13 = var35
                  throw var35
               } finally {
                  CloseableKt.closeFinally(var12, var13)
               }
            } catch (var37: java.lang.Throwable) {
               var8 = var37
               throw var37
            } finally {
               CloseableKt.closeFinally(var7, var8)
            }
         } catch (var39: java.lang.Throwable) {
            var3 = var39
            throw var39
         } finally {
            CloseableKt.closeFinally(var2, var3)
         }

         tmp
      }
   })
      private final get() {
         val var10000: Any = bundleFile$delegate.getValue()
         return var10000 as File
      }


   private final val isolatedLoader: ClassLoader by LazyKt.lazy({ 
      val var0: Array<URL> = arrayOf(INSTANCE.bundleFile.toURI().toURL())
      val var10003: ClassLoader = INSTANCE.getClass().getClassLoader()
      KdiscordIpcSocketLoader.IsolatedSocketLoader(var0, var10003)
   })
      private final get() {
         return isolatedLoader$delegate.getValue() as ClassLoader
      }


   public fun isAvailable(): Boolean {
      return this.getClass().getResource("/gg/norisk/lib/kdiscordipc/bundle.bin") != null
   }

   public fun openIsolatedSocket(): Socket {
      val var10000: Any = Class.forName("dev.cbyrne.kdiscordipc.core.socket.SocketProvider", true, this.isolatedLoader).getMethod("systemDefault").invoke(null)
      return var10000 as Socket
   }

   @SourceDebugExtension(["SMAP\nKdiscordIpcSocketLoader.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KdiscordIpcSocketLoader.kt\ngg/norisk/client/v2/modules/discord/KdiscordIpcSocketLoader$IsolatedSocketLoader\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,84:1\n1#2:85\n*E\n"])
   private class IsolatedSocketLoader(vararg urls: Any, parent: ClassLoader) : URLClassLoader(urls, parent) {
      protected override fun loadClass(name: String, resolve: Boolean): Class<*> {
         var var10000: Class = (Class)this.getClassLoadingLock(name)
         synchronized (var10000) {
            var10000 = this.findLoadedClass(name)
            if (var10000 != null) {
               return var10000
            } else {
               if (!this.parentFirst(name)) {
                  try {
                     val var7: Class = this.findClass(name)
                     if (resolve) {
                        this.resolveClass(var7)
                     }

                     return var7
                  } catch (var14: ClassNotFoundException) {
                  }
               }

               var10000 = super.loadClass(name, resolve)
               return var10000
            }
         }
      }

      private fun parentFirst(name: String): Boolean {
         return StringsKt.startsWith$default(name, "java.", false, 2, null)
            || StringsKt.startsWith$default(name, "javax.", false, 2, null)
            || StringsKt.startsWith$default(name, "sun.", false, 2, null)
            || StringsKt.startsWith$default(name, "kotlin.", false, 2, null)
            || StringsKt.startsWith$default(name, "kotlinx.", false, 2, null)
            || name == "dev.cbyrne.kdiscordipc.core.socket.Socket"
            || name == "dev.cbyrne.kdiscordipc.core.socket.RawPacket"
         }
   }
}
