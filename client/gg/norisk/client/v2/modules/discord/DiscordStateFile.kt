package gg.norisk.client.v2.modules.discord

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.path.LauncherPaths
import java.io.File
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.random.Random
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nDiscordStateFile.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DiscordStateFile.kt\ngg/norisk/client/v2/modules/discord/DiscordStateFile\n+ 2 Json.kt\nkotlinx/serialization/json/Json\n+ 3 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,71:1\n205#2:72\n222#2:74\n63#3:73\n1#4:75\n*S KotlinDebug\n*F\n+ 1 DiscordStateFile.kt\ngg/norisk/client/v2/modules/discord/DiscordStateFile\n*L\n44#1:72\n57#1:74\n46#1:73\n*E\n"])
public object DiscordStateFile {
   private final val instanceId: Long = RangesKt.random(IntRange(1, 999999), Random.Default as Random)

   private final val discordDir: File by LazyKt.lazy({ 
      val var10000: java.lang.String = System.getProperty("norisk.meta.dir")
      val itx: File = File(if (var10000 != null) File(var10000) else INSTANCE.defaultMetaDir(), "discord")
      itx.mkdirs()
      itx
   })
      private final get() {
         return discordDir$delegate.getValue() as File
      }


   private final val ownFile: File
      private final get() {
         return File(this.discordDir, "client.${instanceId}.json")
      }


   public fun writeState(state: String, details: String? = null) {
      val var3: DiscordStateFile = this

      var `$this$writeState_u24lambda_u243`: DiscordStateFile
      try {
         `$this$writeState_u24lambda_u243` = var3
         val var6: DiscordEntry = DiscordEntry("client", state, details, System.currentTimeMillis())
         val var10000: File = `$this$writeState_u24lambda_u243`.ownFile
         val `$this$nrcDebug$iv`: Json = DiscordStateFileKt.access$getJson$p()
         `$this$nrcDebug$iv`.getSerializersModule()
         FilesKt.writeText$default(
            var10000, `$this$nrcDebug$iv`.encodeToString(DiscordEntry.Companion.serializer() as SerializationStrategy, var6), null, 2, null
         )
         `$this$writeState_u24lambda_u243` = (DiscordStateFile)Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
      } catch (var10: java.lang.Throwable) {
         `$this$writeState_u24lambda_u243` = (DiscordStateFile)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var10))
      }

      val var18: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$writeState_u24lambda_u243`)
      if (var18 != null) {
         val var16: Logger = DiscordStateFileKt.access$getLogger$p()
         val var17: java.lang.String = "Failed to write Discord state file: ${var18.getMessage()}"
         if (MCLogger.IS_DEBUG) {
            var16.info(var17)
         }
      }
   }

   public fun readLauncherState(): DiscordEntry? {
      val file: File = File(this.discordDir, "launcher.json")
      if (!file.exists()) {
         return null
      } else {
         val var2: DiscordStateFile = this

         var `$this$readLauncherState_u24lambda_u245`: DiscordStateFile
         try {
            `$this$readLauncherState_u24lambda_u245` = var2
            val `this_$iv`: Json = DiscordStateFileKt.access$getJson$p()
            val `string$iv`: java.lang.String = FilesKt.readText$default(file, null, 1, null)
            `this_$iv`.getSerializersModule()
            val entry: DiscordEntry = `this_$iv`.decodeFromString(DiscordEntry.Companion.serializer() as DeserializationStrategy, `string$iv`) as DiscordEntry
            `$this$readLauncherState_u24lambda_u245` = (DiscordStateFile)Result.constructor_impl/* $VF was: constructor-impl */(
               if (System.currentTimeMillis() - entry.timestamp < 30000L) entry else null
            )
         } catch (var9: java.lang.Throwable) {
            `$this$readLauncherState_u24lambda_u245` = (DiscordStateFile)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var9))
         }

         return (
            if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$readLauncherState_u24lambda_u245`))
               null
               else
               `$this$readLauncherState_u24lambda_u245`
         ) as DiscordEntry
      }
   }

   public fun cleanup() {
      val var1: DiscordStateFile = this

      try {
         val var6: Any = Result.constructor_impl/* $VF was: constructor-impl */((var1 as DiscordStateFile).ownFile.delete())
      } catch (var4: java.lang.Throwable) {
         val `$this$cleanup_u24lambda_u246`: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
      }
   }

   private fun defaultMetaDir(): File {
      return LauncherPaths.INSTANCE.getMetaDir()
   }
}
