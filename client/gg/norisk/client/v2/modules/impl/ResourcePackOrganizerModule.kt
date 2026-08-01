package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.client.MCLogger
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import java.io.Closeable
import java.io.IOException
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.Locale
import java.util.TreeSet
import java.util.stream.Stream
import kotlin.jdk7.AutoCloseableKt
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.IntRef
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nResourcePackOrganizerModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ResourcePackOrganizerModule.kt\ngg/norisk/client/v2/modules/impl/ResourcePackOrganizerModule\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,166:1\n384#2,7:167\n1788#3,4:174\n*S KotlinDebug\n*F\n+ 1 ResourcePackOrganizerModule.kt\ngg/norisk/client/v2/modules/impl/ResourcePackOrganizerModule\n*L\n47#1:167,7\n136#1:174,4\n*E\n"])
public object ResourcePackOrganizerModule : Module("ResourcePack Organizer", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   public open val seoTags: Array<String>

   public final var showIncompatible: Boolean by ValueApiKt.boolean$default(true, "Show Incompatible Packs", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return showIncompatible$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showIncompatible$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public final var transparentBackground: Boolean by ValueApiKt.boolean$default(false, "Transparent Background", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return transparentBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         transparentBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var packPreviews: Boolean by ValueApiKt.boolean$default(false, "Pack Previews", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return packPreviews$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         packPreviews$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   private final val packLogger: Logger = MCLogger.getLogger("ResourcePackOrganizer")

   @JvmField
   @NotNull
   public final var currentFolder: String = ""
      private set

   @JvmField
   public final var filterDirty: Boolean
      private set

   private final val folderCache: MutableMap<String, Set<String>> = LinkedHashMap() as java.util.Map

   @JvmStatic
   public fun resetFolderCache() {
      folderCache.clear()
   }

   @JvmStatic
   public fun getFilesystemFolders(packDir: Path, relativePath: String): Set<String> {
      val `$this$getOrPut$iv`: java.util.Map = folderCache
      val `value$iv`: Any = folderCache.get(relativePath)
      var var24: Any
      if (`value$iv` == null) {
         val folders: TreeSet = SetsKt.sortedSetOf(arrayOfNulls(0))
         val dir: Path = if (relativePath.length() == 0) packDir else packDir.resolve(relativePath)
         if (!Files.isDirectory(dir)) {
            var24 = folders
         } else {
            try {
               val e: Closeable = Files.newDirectoryStream(dir, { p0: Any ->
                  `$tmp0`(p0)
               })
               var var9: java.lang.Throwable = null

               try {
                  val var10000: java.util.Iterator = (e as DirectoryStream).iterator()
                  val var12: java.util.Iterator = var10000

                  while (var12.hasNext()) {
                     val entry: Path = var12.next() as Path
                     val name: java.lang.String = entry.getFileName().toString()
                     if (!Files.isRegularFile(entry.resolve("pack.mcmeta"))) {
                        folders.add(name)
                     }
                  }
               } catch (var19: java.lang.Throwable) {
                  var9 = var19
                  throw var19
               } finally {
                  CloseableKt.closeFinally(e, var9)
               }
            } catch (var21: IOException) {
               packLogger.warn("Failed to scan folder: {}", dir, var21)
            }

            var24 = folders
         }

         `$this$getOrPut$iv`.put(relativePath, var24)
         var24 = var24
      } else {
         var24 = `value$iv`
      }

      return var24 as MutableSet<java.lang.String>
   }

   @JvmStatic
   public fun filterByFolder(packIds: List<String>): Pair<List<Int>, Set<String>> {
      val subfolders: TreeSet = SetsKt.sortedSetOf(arrayOfNulls(0))
      val keepIndices: java.util.List = ArrayList()
      val folderPrefix: java.lang.String = if (currentFolder.length() == 0) "" else "${currentFolder}/"
      var i: Int = 0

      for (var5 in packIds.size()..i) {
         val id: java.lang.String = packIds.get(i) as java.lang.String
         if (!StringsKt.startsWith$default(id, "file/", false, 2, null)) {
            if (currentFolder.length() == 0) {
               keepIndices.add(i)
            }
         } else {
            val relativePath: java.lang.String = StringsKt.removePrefix(id, "file/")
            if (currentFolder.length() <= 0 || StringsKt.startsWith$default(relativePath, folderPrefix, false, 2, null)) {
               val remaining: java.lang.String = if (currentFolder.length() == 0) relativePath else StringsKt.removePrefix(relativePath, folderPrefix)
               val separatorIdx: Int = StringsKt.indexOf$default(remaining, '/', 0, false, 6, null)
               if (separatorIdx == -1) {
                  keepIndices.add(i)
               } else {
                  val var10001: java.lang.String = remaining.substring(0, separatorIdx)
                  subfolders.add(var10001)
               }
            }
         }
      }

      return TuplesKt.to(keepIndices, subfolders)
   }

   @JvmStatic
   public fun navigateToFolder(folder: String) {
      currentFolder = folder
   }

   @JvmStatic
   public fun navigateBack() {
      val lastSlash: Int = StringsKt.lastIndexOf$default(currentFolder, '/', 0, false, 6, null)
      val var10000: java.lang.String
      if (lastSlash == -1) {
         var10000 = ""
      } else {
         var10000 = currentFolder.substring(0, lastSlash)
      }

      currentFolder = var10000
   }

   @JvmStatic
   public fun getTargetFolder(subfolder: String): String {
      return if (currentFolder.length() == 0) subfolder else "${currentFolder}/$subfolder"
   }

   @JvmStatic
   public fun matchesSearch(folderName: String, searchText: String): Boolean {
      if (!StringsKt.isBlank(searchText)) {
         var var10000: java.lang.CharSequence = folderName.toLowerCase(Locale.ROOT)
         var10000 = var10000
         val var10001: java.lang.String = searchText.toLowerCase(Locale.ROOT)
         if (!StringsKt.contains$default(var10000, var10001, false, 2, null)) {
            return false
         }
      }

      return true
   }

   @JvmStatic
   public fun countFolderContents(packDir: Path, folderPath: String, nestedPackPaths: List<String>): Pair<Int, Int> {
      val prefix: java.lang.String = "$folderPath/"
      val folderCount: java.lang.Iterable = nestedPackPaths
      val var27: Int
      if (nestedPackPaths is java.util.Collection && (nestedPackPaths as java.util.Collection).isEmpty()) {
         var27 = 0
      } else {
         val `count$iv`: Int = 0

         for (stream in folderCount) {
            val var10: java.lang.String = stream as java.lang.String
            val var10000: Boolean
            if (!StringsKt.startsWith$default(stream as java.lang.String, prefix, false, 2, null)) {
               var10000 = false
            } else {
               val var26: java.lang.String = var10.substring(prefix.length())
               var10000 = !StringsKt.contains$default(var26, '/', false, 2, null)
            }

            if (var10000) {
               if (++`count$iv` < 0) {
                  CollectionsKt.throwCountOverflow()
               }
            }
         }

         var27 = `count$iv`
      }

      val var19: IntRef = IntRef()
      val var20: Path = packDir.resolve(folderPath)
      if (Files.isDirectory(var20)) {
         try {
            val var21: AutoCloseable = Files.walk(var20)
            var var22: java.lang.Throwable = null

            try {
               (var21 as Stream).filter({ p0: Any ->
                  `$tmp0`(p0)
               }).filter({ p0: Any ->
                  `$tmp0`(p0)
               }).forEach({ p0: Any ->
                  `$tmp0`(p0)
               })
            } catch (var16: java.lang.Throwable) {
               var22 = var16
               throw var16
            } finally {
               AutoCloseableKt.closeFinally(var21, var22)
            }
         } catch (var18: Exception) {
         }
      }

      return TuplesKt.to(var19.element, var27)
   }

   @JvmStatic
   public fun formatFolderSubtitle(folderCount: Int, packCount: Int): String {
      val parts: java.util.List = ArrayList()
      if (folderCount > 0) {
         parts.add("$folderCount Folder${if (folderCount != 1) "s" else ""}")
      }

      if (packCount > 0) {
         parts.add("$packCount Pack${if (packCount != 1) "s" else ""}")
      }

      return if (parts.isEmpty()) "Empty" else CollectionsKt.joinToString$default(parts, " / ", null, null, 0, null, null, 62, null)
   }
}
