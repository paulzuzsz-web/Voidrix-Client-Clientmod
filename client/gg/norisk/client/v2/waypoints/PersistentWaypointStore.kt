package gg.norisk.client.v2.waypoints

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.kotlin.ExtensionsKt
import gg.norisk.compat.task.CoroutineScopesKt
import gg.norisk.compat.task.CoroutineTask
import gg.norisk.compat.task.NrcAsyncPool
import gg.norisk.compat.waypoint.persistent.PersistentWaypoint
import gg.norisk.compat.waypoint.persistent.WaypointDimensionData
import gg.norisk.compat.waypoint.persistent.WaypointPurpose
import gg.norisk.compat.waypoint.persistent.WaypointSet
import gg.norisk.compat.waypoint.persistent.WaypointVisibility
import java.io.File
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.time.Duration
import kotlinx.coroutines.BuildersKt
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonKt
import net.minecraft.client.Minecraft
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nPersistentWaypointStore.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PersistentWaypointStore.kt\ngg/norisk/client/v2/waypoints/PersistentWaypointStore\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n+ 4 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 5 Json.kt\nkotlinx/serialization/json/Json\n+ 6 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 7 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 8 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 9 CoroutineTask.kt\ngg/norisk/compat/task/CoroutineTaskKt\n*L\n1#1,437:1\n1#2:438\n68#3:439\n68#3:448\n68#3:461\n68#3:464\n384#4,7:440\n384#4,7:450\n222#5:447\n205#5:449\n222#5:460\n205#5:462\n205#5:463\n774#6:457\n865#6,2:458\n378#7:465\n40#7:466\n13472#8,2:467\n18#9,12:469\n*S KotlinDebug\n*F\n+ 1 PersistentWaypointStore.kt\ngg/norisk/client/v2/waypoints/PersistentWaypointStore\n*L\n65#1:439\n139#1:448\n349#1:461\n429#1:464\n94#1:440,7\n159#1:450,7\n137#1:447\n145#1:449\n347#1:460\n387#1:462\n403#1:463\n259#1:457\n259#1:458,2\n43#1:465\n43#1:466\n360#1:467,2\n365#1:469,12\n*E\n"])
public object PersistentWaypointStore {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-Waypoints")

   private final val json: Json = JsonKt.Json$default(null, { $this$Json: JsonBuilder ->
      `$this$Json`.setIgnoreUnknownKeys(true)
      `$this$Json`.setEncodeDefaults(true)
      Unit.INSTANCE
   }, 1, null)

   private final val waypointsRoot: File by LazyKt.lazy({ 
      val var10002: Minecraft = Minecraft.getInstance()
      val var5: File = var10002.gameDirectory
      val var3: File = File(var5, "VoidrixClient/waypoints")
      var3.mkdirs()
      var3
   })
      private final get() {
         return waypointsRoot$delegate.getValue() as File
      }


   private final var currentWorldId: String?
   private final var currentMultiworldId: String?
   private final var currentDimensionKey: String?
   private final val dimensions: MutableMap<String, WaypointDimensionData> = LinkedHashMap() as java.util.Map
   private final val changeListeners: MutableList<() -> Unit> = ArrayList() as java.util.List
   private final val transientWaypoints: MutableList<PersistentWaypoint> = ArrayList() as java.util.List

   public final val isLoaded: Boolean
      public final get() {
         return !dimensions.isEmpty()
      }


   public fun addChangeListener(listener: () -> Unit) {
      changeListeners.add(listener)
   }

   private fun fireChanged() {
      for (l in changeListeners) {
         val var3: PersistentWaypointStore = this

         var `$this$fireChanged_u24lambda_u243`: PersistentWaypointStore
         try {
            `$this$fireChanged_u24lambda_u243` = var3
            l()
            `$this$fireChanged_u24lambda_u243` = (PersistentWaypointStore)Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
         } catch (var10: java.lang.Throwable) {
            `$this$fireChanged_u24lambda_u243` = (PersistentWaypointStore)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var10))
         }

         val var10000: java.lang.Throwable = Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(`$this$fireChanged_u24lambda_u243`)
         if (var10000 != null) {
            if (MCLogger.IS_DEBUG) {
               logger.error("Change listener threw", var10000)
            }
         }
      }
   }

   public fun init() {
      ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
         INSTANCE.onJoinWorld()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         INSTANCE.onDisconnect()
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getRespawnEvent().listen({ it: Unit ->
         INSTANCE.onDimensionChange()
         Unit.INSTANCE
      })
   }

   private fun onJoinWorld() {
      val worldId: java.lang.String = WorldIdentifier.INSTANCE.getCurrentWorldId()
      val dimKey: java.lang.String = WorldIdentifier.INSTANCE.getCurrentDimensionKey()
      if (worldId != null && dimKey != null) {
         currentWorldId = worldId
         currentMultiworldId = this.resolveMultiworldId(worldId)
         currentDimensionKey = dimKey
         this.loadAllAsync(worldId, currentMultiworldId)
      } else {
         MCLoggerKt.nrcDebugLog(logger, "waypoints", "Could not determine world/dimension, skipping waypoint load")
      }
   }

   private fun onDimensionChange() {
      val var10000: java.lang.String = WorldIdentifier.INSTANCE.getCurrentDimensionKey()
      if (var10000 != null) {
         if (!(var10000 == currentDimensionKey)) {
            currentDimensionKey = var10000
            val `$this$getOrPut$iv`: java.util.Map = dimensions
            if (dimensions.get(var10000) == null) {
               `$this$getOrPut$iv`.put(var10000, WaypointDimensionData(null, null, 3, null))
            }

            MCLoggerKt.nrcDebugLog(logger, "waypoints", "Dimension changed to $var10000 (${this.getAllWaypoints().size()} waypoints, no IO)")
            this.fireChanged()
         }
      }
   }

   private fun onDisconnect() {
      this.saveAllSync()
      MCLoggerKt.nrcDebugLog(logger, "waypoints", "Disconnected, saved ${dimensions.size()} dimensions and cleared state")
      currentWorldId = null
      currentMultiworldId = null
      currentDimensionKey = null
      dimensions.clear()
   }

   private fun resolveMultiworldId(worldId: String): String? {
      if (!WorldIdentifier.INSTANCE.isMultiplayer()) {
         return null
      } else {
         val var10000: java.lang.String = WorldIdentifier.INSTANCE.getMultiworldId()
         if (var10000 == null) {
            return null
         } else {
            val config: WorldConfig = this.loadWorldConfig(worldId)
            if (config.defaultMultiworldId == null) {
               config.defaultMultiworldId = var10000
               this.saveWorldConfig(worldId, config)
               MCLoggerKt.nrcDebugLog(logger, "waypoints", "Persisted default multiworld ID: $var10000 for $worldId")
            }

            val var5: java.lang.String = config.defaultMultiworldId
            MCLoggerKt.nrcDebugLog(logger, "waypoints", "Resolved multiworld ID: $var5 (detected: $var10000)")
            return var5
         }
      }
   }

   private fun getWorldConfigFile(worldId: String): File {
      val worldDir: File = File(this.waypointsRoot, worldId)
      worldDir.mkdirs()
      return File(worldDir, "world_config.json")
   }

   private fun loadWorldConfig(worldId: String): WorldConfig {
      val file: File = this.getWorldConfigFile(worldId)
      if (!file.exists()) {
         return WorldConfig(null, 1, null)
      } else {
         var `this_$iv`: WorldConfig
         try {
            val var9: Json = json
            val e: java.lang.String = FilesKt.readText$default(file, null, 1, null)
            var9.getSerializersModule()
            `this_$iv` = var9.decodeFromString(WorldConfig.Companion.serializer() as DeserializationStrategy, e) as WorldConfig
         } catch (var8: Exception) {
            val `message$iv`: java.lang.String = "Failed to load world config for $worldId"
            if (MCLogger.IS_DEBUG) {
               logger.error(`message$iv`, var8)
            }

            `this_$iv` = WorldConfig(null, 1, null)
         }

         return `this_$iv`
      }
   }

   private fun saveWorldConfig(worldId: String, config: WorldConfig) {
      val file: Json = json
      json.getSerializersModule()
      NrcAsyncPool.INSTANCE.getExecutor().execute({ 
         INSTANCE.safeWrite(`$file`, `$content`)
      })
   }

   public fun getCurrentDimensionData(): WaypointDimensionData? {
      return dimensions.get(currentDimensionKey)
   }

   public fun getDimensionData(dimKey: String): WaypointDimensionData? {
      return dimensions.get(dimKey)
   }

   public fun getOrCreateDimensionData(dimKey: String): WaypointDimensionData {
      val `$this$getOrPut$iv`: java.util.Map = dimensions
      val `value$iv`: Any = dimensions.get(dimKey)
      val var10000: Any
      if (`value$iv` == null) {
         val var6: Any = WaypointDimensionData(null, null, 3, null)
         `$this$getOrPut$iv`.put(dimKey, var6)
         var10000 = var6
      } else {
         var10000 = `value$iv`
      }

      return var10000 as WaypointDimensionData
   }

   public fun getLoadedDimensionKeys(): Set<String> {
      return dimensions.keySet()
   }

   public fun createWaypoint(
      name: String,
      x: Int,
      y: Int,
      z: Int,
      color: Int,
      purpose: WaypointPurpose = WaypointPurpose.NORMAL,
      visibility: WaypointVisibility = WaypointVisibility.LOCAL,
      dimensionKey: String? = null
   ): PersistentWaypoint? {
      var var10000: java.lang.String = dimensionKey
      if (dimensionKey == null) {
         var10000 = currentDimensionKey
         if (currentDimensionKey == null) {
            return null
         }
      }

      val set: WaypointSet = this.getOrCreateDimensionData(var10000).getOrCreateActiveSet()
      val waypoint: PersistentWaypoint = PersistentWaypoint(
         null, name, StringsKt.take(name, 2), x, y, z, color, purpose, visibility, false, false, 0L, 3585, null
      )
      set.getWaypoints().add(waypoint)
      this.saveDimension(var10000)
      MCLoggerKt.nrcDebugLog(
         logger, "waypoints", "Created waypoint '${waypoint.getName()}' at ${waypoint.getX()},${waypoint.getY()},${waypoint.getZ()} in $var10000"
      )
      this.fireChanged()
      return waypoint
   }

   public fun deleteWaypoint(id: UUID): Boolean {
      for (var3 in dimensions.entrySet()) {
         val dimKey: java.lang.String = var3.getKey() as java.lang.String

         for (set in (var3.getValue() as WaypointDimensionData).getSets().values()) {
            if (set.getWaypoints().removeIf({ p0: Any ->
               `$tmp0`(p0)
            })) {
               this.saveDimension(dimKey)
               MCLoggerKt.nrcDebugLog(logger, "waypoints", "Deleted waypoint $id from $dimKey")
               this.fireChanged()
               return true
            }
         }
      }

      return false
   }

   public fun deleteWaypointByName(name: String): Boolean {
      if (currentDimensionKey == null) {
         return false
      } else {
         val dimKey: java.lang.String = currentDimensionKey
         val var10000: WaypointDimensionData = this.getCurrentDimensionData()
         if (var10000 == null) {
            return false
         } else {
            val var6: WaypointSet = var10000.getActiveSet()
            if (var6 == null) {
               return false
            } else {
               val removed: Boolean = var6.getWaypoints().removeIf({ p0: Any ->
                  `$tmp0`(p0)
               })
               if (removed) {
                  this.saveDimension(dimKey)
                  MCLoggerKt.nrcDebugLog(logger, "waypoints", "Deleted waypoint '$name' from $dimKey")
               }

               return removed
            }
         }
      }
   }

   public fun modifyWaypoint(id: UUID, block: (PersistentWaypoint) -> Unit): Boolean {
      for (var4 in dimensions.entrySet()) {
         for (set in (var4.getValue() as WaypointDimensionData).getSets().values()) {
            val var11: java.util.Iterator = set.getWaypoints().iterator()

            var var10000: Any
            while (true) {
               if (!var11.hasNext()) {
                  var10000 = null
                  break
               }

               val var12: Any = var11.next()
               if ((var12 as PersistentWaypoint).getId() == id) {
                  var10000 = var12
                  break
               }
            }

            val found: PersistentWaypoint = var10000 as PersistentWaypoint
            if (var10000 as PersistentWaypoint != null) {
               block(found)
               this.saveDimension(var4.getKey() as java.lang.String)
               this.fireChanged()
               return true
            }
         }
      }

      return false
   }

   public fun getWaypoint(id: UUID): PersistentWaypoint? {
      for (data in dimensions.values()) {
         for (set in data.getSets().values()) {
            val var8: java.util.Iterator = set.getWaypoints().iterator()

            var var10000: Any
            while (true) {
               if (!var8.hasNext()) {
                  var10000 = null
                  break
               }

               val var9: Any = var8.next()
               if ((var9 as PersistentWaypoint).getId() == id) {
                  var10000 = var9
                  break
               }
            }

            val found: PersistentWaypoint = var10000 as PersistentWaypoint
            if (var10000 as PersistentWaypoint != null) {
               return found
            }
         }
      }

      return null
   }

   public fun getWaypointByName(name: String): PersistentWaypoint? {
      val var10000: WaypointDimensionData = this.getCurrentDimensionData()
      if (var10000 != null) {
         val var8: WaypointSet = var10000.getActiveSet()
         if (var8 != null) {
            val var4: java.util.Iterator = var8.getWaypoints().iterator()

            while (true) {
               if (var4.hasNext()) {
                  val var5: Any = var4.next()
                  if (!StringsKt.equals((var5 as PersistentWaypoint).getName(), name, true)) {
                     continue
                  }

                  var9 = var5
                  break
               }

               var9 = null
               break
            }

            return var9 as PersistentWaypoint
         }
      }

      return null
   }

   public fun getAllWaypoints(): List<PersistentWaypoint> {
      var var3: java.util.List
      run label29@{
         val var10000: WaypointDimensionData = this.getCurrentDimensionData()
         if (var10000 != null) {
            val var2: WaypointSet = var10000.getActiveSet()
            if (var2 != null) {
               var3 = var2.getWaypoints()
               if (var3 != null) {
                  return@label29
               }
            }
         }

         var3 = CollectionsKt.emptyList()
      }

      if (transientWaypoints.isEmpty()) {
         return var3
      } else {
         return if (var3.isEmpty()) CollectionsKt.toList(transientWaypoints) else CollectionsKt.plus(var3, transientWaypoints)
      }
   }

   public fun getWaypointsByPurpose(purpose: WaypointPurpose): List<PersistentWaypoint> {
      val `$this$filterTo$iv$iv`: java.lang.Iterable = this.getAllWaypoints()
      val `destination$iv$iv`: java.util.Collection = ArrayList()

      for (`element$iv$iv` in `$this$filterTo$iv$iv`) {
         if ((`element$iv$iv` as PersistentWaypoint).getPurpose() === purpose) {
            `destination$iv$iv`.add(`element$iv$iv`)
         }
      }

      return `destination$iv$iv` as MutableList<PersistentWaypoint>
   }

   public fun addTransient(specs: List<gg.norisk.client.v2.waypoints.PersistentWaypointStore.WaypointSpec>): Int {
      for (spec in specs) {
         transientWaypoints.add(
            PersistentWaypoint(null, spec.name, StringsKt.take(spec.name, 2), spec.x, spec.y, spec.z, spec.color, null, null, false, false, 0L, 3969, null)
         )
      }

      MCLoggerKt.nrcDebugLog(logger, "waypoints", "Added ${specs.size()} transient waypoints (total transient: ${transientWaypoints.size()})")
      this.fireChanged()
      return specs.size()
   }

   public fun clearTransient(): Int {
      val n: Int = transientWaypoints.size()
      if (n == 0) {
         return 0
      } else {
         transientWaypoints.clear()
         MCLoggerKt.nrcDebugLog(logger, "waypoints", "Cleared $n transient waypoints")
         this.fireChanged()
         return n
      }
   }

   public fun createSet(name: String): Boolean {
      val var10000: WaypointDimensionData = this.getCurrentDimensionData()
      if (var10000 == null) {
         return false
      } else if (var10000.getSets().containsKey(name)) {
         return false
      } else {
         var10000.getSets().put(name, WaypointSet(null, 1, null))
         val var10001: java.lang.String = currentDimensionKey
         this.saveDimension(var10001)
         return true
      }
   }

   public fun deleteSet(name: String): Boolean {
      val var10000: WaypointDimensionData = this.getCurrentDimensionData()
      if (var10000 == null) {
         return false
      } else if (name == "default") {
         return false
      } else {
         val removed: Boolean = var10000.getSets().remove(name) != null
         if (removed) {
            if (var10000.getCurrentSet() == name) {
               var10000.setCurrentSet("default")
            }

            val var10001: java.lang.String = currentDimensionKey
            this.saveDimension(var10001)
         }

         return removed
      }
   }

   public fun switchSet(name: String): Boolean {
      val var10000: WaypointDimensionData = this.getCurrentDimensionData()
      if (var10000 == null) {
         return false
      } else if (!var10000.getSets().containsKey(name)) {
         return false
      } else {
         var10000.setCurrentSet(name)
         val var10001: java.lang.String = currentDimensionKey
         this.saveDimension(var10001)
         return true
      }
   }

   private fun getSubworldDir(worldId: String, multiworldId: String?): File {
      val worldDir: File = File(this.waypointsRoot, worldId)
      return if (multiworldId != null) File(worldDir, multiworldId) else worldDir
   }

   private fun getDimensionFile(worldId: String, multiworldId: String?, dimKey: String): File {
      val dir: File = this.getSubworldDir(worldId, multiworldId)
      dir.mkdirs()
      return File(dir, "$dimKey.json")
   }

   private fun loadFromFile(file: File): WaypointDimensionData {
      var `this_$iv`: WaypointDimensionData
      try {
         val var8: Json = json
         val e: java.lang.String = FilesKt.readText$default(file, null, 1, null)
         var8.getSerializersModule()
         `this_$iv` = var8.decodeFromString(WaypointDimensionData.Companion.serializer() as DeserializationStrategy, e) as WaypointDimensionData
      } catch (var7: Exception) {
         val `message$iv`: java.lang.String = "Failed to load waypoints from $file"
         if (MCLogger.IS_DEBUG) {
            logger.error(`message$iv`, var7)
         }

         `this_$iv` = WaypointDimensionData(null, null, 3, null)
      }

      return `this_$iv`
   }

   private fun loadAllAsync(worldId: String, multiworldId: String?) {
      NrcAsyncPool.INSTANCE
         .getExecutor()
         .execute(
            { 
               val dir: File = INSTANCE.getSubworldDir(`$worldId`, `$multiworldId`)
               val loaded: java.util.Map = LinkedHashMap()
               if (dir.exists()) {
                  val `sync$iv`: Array<File> = dir.listFiles({ f: File ->
                     FilesKt.getExtension(f) == "json" && !(f.getName() == "world_config.json")
                  })
                  if (`sync$iv` != null) {
                     for (`period$iv` in `sync$iv`) {
                        loaded.put(FilesKt.getNameWithoutExtension((File)`period$iv`), INSTANCE.loadFromFile((File)`period$iv`))
                     }
                  }
               }

               val var15: Boolean = true
               val var16: Boolean = true
               BuildersKt.launch$default(
                  CoroutineScopesKt.getMcClientCoroutineScope(),
                  null,
                  null,
                  PersistentWaypointStore$loadAllAsync$lambda$25$$inlined$mcCoroutineTask-ML416i8$default$1(
                     Duration.Companion.getZERO_UwyO8pc/* $VF was: getZERO-UwyO8pc */(),
                     1L,
                     CoroutineTask(1L),
                     ExtensionsKt.getTicks(1),
                     null,
                     loaded,
                     `$worldId`,
                     `$multiworldId`
                  ),
                  3,
                  null
               )
            }
         )
      }

   public fun saveDimension(dimKey: String) {
      if (currentWorldId != null) {
         val worldId: java.lang.String = currentWorldId
         val var10000: WaypointDimensionData = dimensions.get(dimKey)
         if (var10000 != null) {
            val file: File = this.getDimensionFile(worldId, currentMultiworldId, dimKey)
            val `this_$iv`: Json = json
            json.getSerializersModule()
            NrcAsyncPool.INSTANCE.getExecutor().execute({ 
               INSTANCE.safeWrite(`$file`, `$content`)
            })
         }
      }
   }

   private fun saveAllSync() {
      if (currentWorldId != null) {
         val worldId: java.lang.String = currentWorldId

         for (var3 in dimensions.entrySet()) {
            val dimKey: java.lang.String = var3.getKey() as java.lang.String
            val data: WaypointDimensionData = var3.getValue() as WaypointDimensionData
            val file: File = this.getDimensionFile(worldId, currentMultiworldId, dimKey)
            val `this_$iv`: Json = json
            json.getSerializersModule()
            this.safeWrite(file, `this_$iv`.encodeToString(WaypointDimensionData.Companion.serializer() as SerializationStrategy, data))
         }
      }
   }

   private fun safeWrite(file: File, content: String) {
      try {
         val tmp: File = File(file.getParent(), "${file.getName()}.tmp")
         FilesKt.writeText$default(tmp, content, null, 2, null)

         try {
            val var11: Path = Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
         } catch (var8: AtomicMoveNotSupportedException) {
            val e: Path = Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
         }
      } catch (var9: Exception) {
         val `message$iv`: java.lang.String = "Failed to save waypoints to $file"
         if (MCLogger.IS_DEBUG) {
            logger.error(`message$iv`, var9)
         }
      }
   }

   public fun getCurrentWorldId(): String? {
      return currentWorldId
   }

   public fun getCurrentMultiworldId(): String? {
      return currentMultiworldId
   }

   public fun getCurrentDimensionKey(): String? {
      return currentDimensionKey
   }

   public data class WaypointSpec(name: String, x: Int, y: Int, z: Int, color: Int) {
      public final val name: String
      public final val x: Int
      public final val y: Int
      public final val z: Int
      public final val color: Int

      init {
         this.name = name
         this.x = x
         this.y = y
         this.z = z
         this.color = color
      }

      public operator fun component1(): String {
         return this.name
      }

      public operator fun component2(): Int {
         return this.x
      }

      public operator fun component3(): Int {
         return this.y
      }

      public operator fun component4(): Int {
         return this.z
      }

      public operator fun component5(): Int {
         return this.color
      }

      public fun copy(name: String = this.name, x: Int = this.x, y: Int = this.y, z: Int = this.z, color: Int = this.color): gg.norisk.client.v2.waypoints.PersistentWaypointStore.WaypointSpec {
         return PersistentWaypointStore.WaypointSpec(name, x, y, z, color)
      }

      public override fun toString(): String {
         return "WaypointSpec(name=${this.name}, x=${this.x}, y=${this.y}, z=${this.z}, color=${this.color})"
      }

      public override fun hashCode(): Int {
         return (((this.name.hashCode() * 31 + Integer.hashCode(this.x)) * 31 + Integer.hashCode(this.y)) * 31 + Integer.hashCode(this.z)) * 31
            + Integer.hashCode(this.color)
         }

      public override operator fun equals(other: Any?): Boolean {
         label46@
         if (this === other) {
            return true
         } else {
            return other is PersistentWaypointStore.WaypointSpec
               && this.name == (other as PersistentWaypointStore.WaypointSpec).name
               && this.x == (other as PersistentWaypointStore.WaypointSpec).x
               && this.y == (other as PersistentWaypointStore.WaypointSpec).y
               && this.z == (other as PersistentWaypointStore.WaypointSpec).z
               && this.color == (other as PersistentWaypointStore.WaypointSpec).color
            }
      }
   }
}
