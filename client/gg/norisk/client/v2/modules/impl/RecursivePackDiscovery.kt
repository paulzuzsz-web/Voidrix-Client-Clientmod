package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import java.io.IOException
import java.nio.file.FileVisitOption
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.ArrayList
import kotlin.enums.EnumEntries
import org.slf4j.Logger

public object RecursivePackDiscovery {
   private final val logger: Logger = MCLogger.getLogger("RecursivePackDiscovery")
   private const val DEBUG_CATEGORY: String = "packs"

   public fun discoverNestedPacks(root: Path, maxDepth: Int = 10): List<gg.norisk.client.v2.modules.impl.RecursivePackDiscovery.DiscoveredPack> {
      if (!Files.isDirectory(root)) {
         return CollectionsKt.emptyList()
      } else {
         val packs: java.util.List = ArrayList()
         Files.walkFileTree(root, SetsKt.setOf(FileVisitOption.FOLLOW_LINKS), maxDepth, object : SimpleFileVisitor<Path> {
            public open fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
               if (dir == root) {
                  return FileVisitResult.CONTINUE
               } else if (dir.getParent() == root) {
                  return if (Files.isRegularFile(dir.resolve("pack.mcmeta"))) FileVisitResult.SKIP_SUBTREE else FileVisitResult.CONTINUE
               } else if (Files.isRegularFile(dir.resolve("pack.mcmeta"))) {
                  packs.add(RecursivePackDiscovery.DiscoveredPack(dir, RecursivePackDiscovery.DiscoveredPackType.DIRECTORY))
                  return FileVisitResult.SKIP_SUBTREE
               } else {
                  return FileVisitResult.CONTINUE
               }
            }

            public open fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
               if (file.getParent() == root) {
                  return FileVisitResult.CONTINUE
               } else {
                  if (attrs.isRegularFile() && StringsKt.endsWith$default(file.getFileName().toString(), ".zip", false, 2, null)) {
                     packs.add(RecursivePackDiscovery.DiscoveredPack(file, RecursivePackDiscovery.DiscoveredPackType.ZIP))
                  }

                  return FileVisitResult.CONTINUE
               }
            }

            public open fun visitFileFailed(file: Path, exc: IOException): FileVisitResult {
               MCLoggerKt.nrcDebugLog(RecursivePackDiscovery.logger, "packs", "Failed to access: $file")
               return FileVisitResult.CONTINUE
            }
         })
         if (!packs.isEmpty()) {
            MCLoggerKt.nrcDebugLog(logger, "packs", "Discovered ${packs.size()} nested resource pack(s) in $root")
         }

         return packs
      }
   }

   public data class DiscoveredPack(path: Path, type: gg.norisk.client.v2.modules.impl.RecursivePackDiscovery.DiscoveredPackType) {
      public final val path: Path
      public final val type: gg.norisk.client.v2.modules.impl.RecursivePackDiscovery.DiscoveredPackType

      init {
         this.path = path
         this.type = type
      }

      public operator fun component1(): Path {
         return this.path
      }

      public operator fun component2(): gg.norisk.client.v2.modules.impl.RecursivePackDiscovery.DiscoveredPackType {
         return this.type
      }

      public fun copy(path: Path = this.path, type: gg.norisk.client.v2.modules.impl.RecursivePackDiscovery.DiscoveredPackType = this.type): gg.norisk.client.v2.modules.impl.RecursivePackDiscovery.DiscoveredPack {
         return RecursivePackDiscovery.DiscoveredPack(path, type)
      }

      public override fun toString(): String {
         return "DiscoveredPack(path=${this.path}, type=${this.type})"
      }

      public override fun hashCode(): Int {
         return this.path.hashCode() * 31 + this.type.hashCode()
      }

      public override operator fun equals(other: Any?): Boolean {
         label28@
         if (this === other) {
            return true
         } else {
            return other is RecursivePackDiscovery.DiscoveredPack
               && this.path == (other as RecursivePackDiscovery.DiscoveredPack).path
               && this.type === (other as RecursivePackDiscovery.DiscoveredPack).type
            }
      }
   }

   public enum class DiscoveredPackType {
      ZIP,
      DIRECTORY;

      @JvmStatic
      fun getEntries(): EnumEntries<RecursivePackDiscovery.DiscoveredPackType> {
         $ENTRIES
      }
   }
}
