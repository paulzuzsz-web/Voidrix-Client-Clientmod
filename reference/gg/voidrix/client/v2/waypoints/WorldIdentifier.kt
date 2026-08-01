package gg.voidrix.client.v2.waypoints

import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.client.MCLoggerKt
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.server.IntegratedServer
import net.minecraft.core.BlockPos
import net.minecraft.world.level.storage.LevelData.RespawnData
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nWorldIdentifier.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WorldIdentifier.kt\ngg/voidrix/client/v2/waypoints/WorldIdentifier\n+ 2 MCServer.kt\ngg/voidrix/compat/host/MCServer\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,116:1\n95#2:117\n27#2:118\n95#2:121\n95#2:124\n138#3:119\n40#3:120\n138#3:122\n40#3:123\n*S KotlinDebug\n*F\n+ 1 WorldIdentifier.kt\ngg/voidrix/client/v2/waypoints/WorldIdentifier\n*L\n32#1:117\n33#1:118\n89#1:121\n111#1:124\n55#1:119\n55#1:120\n90#1:122\n90#1:123\n*E\n"])
public object WorldIdentifier {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-WorldIdentifier")

   public fun getCurrentWorldId(): String? {
      label35@
      if (Minecraft.getInstance().isLocalServer()) {
         val var9: IntegratedServer = Minecraft.getInstance().getSingleplayerServer()
         val var11: IntegratedServer = if (var9 is IntegratedServer) var9 else null
         if ((if (var9 is IntegratedServer) var9 else null) == null) {
            return null
         } else {
            val var8: java.lang.String = var11.getWorldData().getLevelName()
            return if (var8 != null) this.sanitizeForFilesystem(var8) else null
         }
      } else {
         val var10000: java.lang.String = MCClient.getCurrentServerIp()
         return if (var10000 == null) null else "Multiplayer_${this.sanitizeForFilesystem(Regex(":\\d+$").replace(var10000, ""))}"
      }
   }

   public fun getCurrentDimensionKey(): String? {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.level == null) {
         return null
      } else {
         val path: java.lang.String = var10000.level.dimension().identifier().getPath()
         return StringsKt.replace$default(path, '/', '_', false, 4, null)
      }
   }

   public fun getMultiworldId(): String? {
      if (Minecraft.getInstance().isLocalServer()) {
         return null
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         if (var10000.level == null) {
            return null
         } else {
            val var9: RespawnData = var10000.level.getRespawnData()
            if (var9 == null) {
               return null
            } else {
               val var6: BlockPos = var9.pos()
               val var7: java.lang.String = "mw${var6.getX() shr 6},${var6.getY() shr 6},${var6.getZ() shr 6}"
               MCLoggerKt.voidrixDebugLog(logger, "waypoints", "Multiworld ID: $var7")
               return var7
            }
         }
      }
   }

   public fun isMultiplayer(): Boolean {
      return !Minecraft.getInstance().isLocalServer()
   }

   private fun String.sanitizeForFilesystem(): String {
      return StringsKt.trim(Regex("[\\\\/:*?\"<>|]").replace(`$this$sanitizeForFilesystem`, "_")).toString()
   }
}
