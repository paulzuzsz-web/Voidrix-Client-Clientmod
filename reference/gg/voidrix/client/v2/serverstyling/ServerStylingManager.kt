package gg.voidrix.client.v2.serverstyling

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import gg.voidrix.client.v2.serverstyling.data.Gamemode
import gg.voidrix.client.v2.serverstyling.data.GamemodeDeserializer
import gg.voidrix.client.v2.serverstyling.data.ServerStyleManifest
import gg.voidrix.client.v2.serverstyling.data.StyledServer
import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.asset.VoidrixAssetReader
import gg.voidrix.compat.task.CoroutineScopesKt
import gg.voidrix.ui.utils.DevUtilsKt
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.coroutines.BuildersKt

@VoidrixMiniTag(tags = ["server-styling"])
@SourceDebugExtension(["SMAP\nServerStylingManager.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ServerStylingManager.kt\ngg/voidrix/client/v2/serverstyling/ServerStylingManager\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,98:1\n1#2:99\n1761#3,3:100\n1761#3,3:103\n*S KotlinDebug\n*F\n+ 1 ServerStylingManager.kt\ngg/voidrix/client/v2/serverstyling/ServerStylingManager\n*L\n88#1:100,3\n94#1:103,3\n*E\n"])
public object ServerStylingManager {
   private final val gson: Gson
   private final var styledServers: Map<String, StyledServer> = MapsKt.emptyMap()
   private final var loaded: Boolean
   private final val loadInFlight: AtomicBoolean = AtomicBoolean(false)
   private const val NAMESPACE: String = "voidrix"
   private const val PATH: String = "merged/merged-manifest.json"

   private fun loadManifest() {
      if (!loaded) {
         if (!this.tryLoadInline()) {
            if (loadInFlight.compareAndSet(false, true)) {
               this.startAsyncLoad()
            }
         }
      }
   }

   private fun tryLoadInline(): Boolean {
      val var10000: ByteArray = VoidrixAssetReader.tryReadBytesFromCache$default(
         VoidrixAssetReader.INSTANCE, "voidrix", "merged/merged-manifest.json", null, null, 12, null
      )
      return var10000 != null && this.parseAndCommit(var10000)
   }

   private fun startAsyncLoad() {
      BuildersKt.launch$default(CoroutineScopesKt.getIoCoroutineScope(), null, null, {
         // $VF: Could not decompile lambda - root function was not found. Is this a suspend lambda?
      } as Function2, 3, null)
   }

   private fun parseAndCommit(bytes: ByteArray): Boolean {
      val var2: ServerStylingManager = this

      var it: ServerStylingManager
      try {
         it = var2
         styledServers = (gson.fromJson(java.lang.String(bytes, Charsets.UTF_8), ServerStyleManifest.class) as ServerStyleManifest).servers
         loaded = true
         DevUtilsKt.voidrixDebug(it, "Loaded server styling manifest (${styledServers.size()} servers)")
         it = (ServerStylingManager)Result.constructor_impl/* $VF was: constructor-impl */(true)
      } catch (var6: java.lang.Throwable) {
         it = (ServerStylingManager)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var6))
      }

      val var10000: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it)
      val var11: Any
      if (var10000 == null) {
         var11 = it
      } else {
         DevUtilsKt.voidrixError(INSTANCE, "Failed to parse voidrix:merged/merged-manifest.json", var10000)
         var11 = false
      }

      return var11 as java.lang.Boolean
   }

   @JvmStatic
   public fun getStyledServer(serverAddress: String): StyledServer? {
      INSTANCE.loadManifest()
      var var10000: StyledServer = serverAddress.toLowerCase(Locale.ROOT)
      val lowerAddress: java.lang.String = var10000
      val var3: java.util.Iterator = styledServers.values().iterator()

      while (true) {
         if (!var3.hasNext()) {
            var10000 = null
            break
         }

         val var4: Any = var3.next()
         val `$this$any$iv`: java.lang.Iterable = (var4 as StyledServer).server-address
         var var14: Boolean
         if (`$this$any$iv` is java.util.Collection && (`$this$any$iv` as java.util.Collection).isEmpty()) {
            var14 = false
         } else {
            val var9: java.util.Iterator = `$this$any$iv`.iterator()

            while (true) {
               if (!var9.hasNext()) {
                  var14 = false
                  break
               }

               val var13: java.lang.String = (var9.next() as java.lang.String).toLowerCase(Locale.ROOT)
               if (var13 == lowerAddress) {
                  var14 = true
                  break
               }
            }
         }

         if (var14) {
            var10000 = (StyledServer)var4
            break
         }
      }

      return var10000
   }

   public fun getDisabledModulesForServer(serverAddress: String): List<String> {
      this.loadManifest()
      var var10000: StyledServer = serverAddress.toLowerCase(Locale.ROOT)
      val lowerAddress: java.lang.String = var10000
      val var5: java.util.Iterator = styledServers.values().iterator()

      while (true) {
         if (!var5.hasNext()) {
            var10000 = null
            break
         }

         val var6: Any = var5.next()
         val `$this$any$iv`: java.lang.Iterable = (var6 as StyledServer).server-address
         var var16: Boolean
         if (`$this$any$iv` is java.util.Collection && (`$this$any$iv` as java.util.Collection).isEmpty()) {
            var16 = false
         } else {
            val var11: java.util.Iterator = `$this$any$iv`.iterator()

            while (true) {
               if (!var11.hasNext()) {
                  var16 = false
                  break
               }

               val var15: java.lang.String = (var11.next() as java.lang.String).toLowerCase(Locale.ROOT)
               if (var15 == lowerAddress) {
                  var16 = true
                  break
               }
            }
         }

         if (var16) {
            var10000 = (StyledServer)var6
            break
         }
      }

      val server: StyledServer = var10000
      if (var10000 != null) {
         val var18: java.util.List = server.disabled-modules
         if (var18 != null) {
            return var18
         }
      }

      return CollectionsKt.emptyList()
   }

   @JvmStatic
   fun {
      val var10000: Gson = GsonBuilder().registerTypeAdapter(Gamemode::class.java, GamemodeDeserializer()).create()
      gson = var10000
   }
}
