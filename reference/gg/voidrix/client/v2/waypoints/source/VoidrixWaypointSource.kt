package gg.voidrix.client.v2.waypoints.source

import gg.voidrix.client.v2.waypoints.PersistentWaypointStore
import gg.voidrix.compat.waypoint.persistent.PersistentWaypoint
import gg.voidrix.compat.waypoint.persistent.WaypointDimensionData
import gg.voidrix.compat.waypoint.persistent.WaypointSet
import gg.voidrix.compat.waypoint.source.SourceWaypoint
import gg.voidrix.compat.waypoint.source.WaypointSource
import gg.voidrix.compat.waypoint.source.WaypointSourceKt
import java.util.ArrayList
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nVoidrixWaypointSource.kt\nKotlin\n*S Kotlin\n*F\n+ 1 VoidrixWaypointSource.kt\ngg/voidrix/client/v2/waypoints/source/VoidrixWaypointSource\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,85:1\n1563#2:86\n1634#2,3:87\n1761#2,3:91\n295#2,2:94\n1#3:90\n*S KotlinDebug\n*F\n+ 1 VoidrixWaypointSource.kt\ngg/voidrix/client/v2/waypoints/source/VoidrixWaypointSource\n*L\n29#1:86\n29#1:87,3\n71#1:91,3\n78#1:94,2\n*E\n"])
public object VoidrixWaypointSource : WaypointSource {
   public open val id: String = "voidrix"
   public open val displayName: String = "VoidrixClient"
   public open val isWritable: Boolean = true

   public open val isAvailable: Boolean
      public open get() {
         return PersistentWaypointStore.INSTANCE.getCurrentWorldId() != null
      }


   public open fun listDimensions(): List<String> {
      val known: java.util.List = CollectionsKt.toList(PersistentWaypointStore.INSTANCE.getLoadedDimensionKeys())
      if (!known.isEmpty()) {
         return known
      } else {
         val current: java.lang.String = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
         return if (current != null) CollectionsKt.listOf(current) else CollectionsKt.emptyList()
      }
   }

   public open fun list(dimensionKey: String): List<SourceWaypoint> {
      val var10000: WaypointDimensionData = PersistentWaypointStore.INSTANCE.getDimensionData(dimensionKey)
      if (var10000 == null) {
         return CollectionsKt.emptyList()
      } else {
         val var13: WaypointSet = var10000.getActiveSet()
         if (var13 != null) {
            val var14: java.util.List = var13.getWaypoints()
            if (var14 != null) {
               val `$this$mapTo$iv$iv`: java.lang.Iterable = var14
               val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(var14, 10))

               for (`item$iv$iv` in `$this$mapTo$iv$iv`) {
                  `destination$iv$iv`.add(WaypointSourceKt.toSource$default(`item$iv$iv` as PersistentWaypoint, null, 1, null))
               }

               return `destination$iv$iv` as MutableList<SourceWaypoint>
            }
         }

         return CollectionsKt.emptyList()
      }
   }

   public open fun add(dimensionKey: String, waypoint: SourceWaypoint): Boolean {
      return PersistentWaypointStore.INSTANCE
            .createWaypoint(
               waypoint.getName(),
               waypoint.getX(),
               waypoint.getY(),
               waypoint.getZ(),
               waypoint.getColor(),
               waypoint.getPurpose(),
               waypoint.getVisibility(),
               dimensionKey
            )
         != null
      }

   public open fun update(dimensionKey: String, waypoint: SourceWaypoint): Boolean {
      val modified: VoidrixWaypointSource = this

      var `$this$update_u24lambda_u241`: VoidrixWaypointSource
      try {
         `$this$update_u24lambda_u241` = modified
         `$this$update_u24lambda_u241` = (VoidrixWaypointSource)Result.constructor_impl/* $VF was: constructor-impl */(UUID.fromString(waypoint.getId()))
      } catch (var8: java.lang.Throwable) {
         `$this$update_u24lambda_u241` = (VoidrixWaypointSource)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var8))
      }

      val var10000: UUID = (if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$update_u24lambda_u241`)) null else `$this$update_u24lambda_u241`) as UUID
      if (var10000 == null) {
         return false
      } else {
         val currentDim: java.lang.String = this.findWaypointDimension(var10000)
         if (!PersistentWaypointStore.INSTANCE.modifyWaypoint(var10000, { $this$modifyWaypoint: PersistentWaypoint ->
            `$this$modifyWaypoint`.setName(`$waypoint`.getName())
            var var10000: PersistentWaypoint = `$this$modifyWaypoint`
            val var2: java.lang.CharSequence = `$waypoint`.getInitials()
            val var10001: java.lang.CharSequence
            if (StringsKt.isBlank(var2)) {
               run label20@{
                  val var5: Character = StringsKt.firstOrNull(`$waypoint`.getName())
                  if (var5 != null) {
                     var6 = var5.toString()
                     if (var6 != null) {
                        return@label20
                     }
                  }

                  var6 = ""
               }

               var10001 = var6
               var10000 = `$this$modifyWaypoint`
            } else {
               var10001 = var2
            }

            var10000.setInitials(var10001 as java.lang.String)
            `$this$modifyWaypoint`.setX(`$waypoint`.getX())
            `$this$modifyWaypoint`.setY(`$waypoint`.getY())
            `$this$modifyWaypoint`.setZ(`$waypoint`.getZ())
            `$this$modifyWaypoint`.setColor(`$waypoint`.getColor())
            `$this$modifyWaypoint`.setDisabled(`$waypoint`.getDisabled())
            `$this$modifyWaypoint`.setYIncluded(`$waypoint`.getYIncluded())
            `$this$modifyWaypoint`.setPurpose(`$waypoint`.getPurpose())
            `$this$modifyWaypoint`.setVisibility(`$waypoint`.getVisibility())
            Unit.INSTANCE
         })) {
            return false
         } else {
            if (currentDim != null && !(currentDim == dimensionKey)) {
               this.moveWaypoint(var10000, currentDim, dimensionKey)
            }

            return true
         }
      }
   }

   public open fun delete(dimensionKey: String, id: String): Boolean {
      run label27@{
         val var4: VoidrixWaypointSource = this

         var `$this$delete_u24lambda_u244`: VoidrixWaypointSource
         try {
            `$this$delete_u24lambda_u244` = var4
            `$this$delete_u24lambda_u244` = (VoidrixWaypointSource)Result.constructor_impl/* $VF was: constructor-impl */(UUID.fromString(id))
         } catch (var7: java.lang.Throwable) {
            `$this$delete_u24lambda_u244` = (VoidrixWaypointSource)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var7))
         }

         val var10000: UUID = (if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$delete_u24lambda_u244`)) null else `$this$delete_u24lambda_u244`) as UUID
         return var10000 != null && PersistentWaypointStore.INSTANCE.deleteWaypoint(var10000)
      }
   }

   private fun findWaypointDimension(id: UUID): String? {
      for (dimKey in PersistentWaypointStore.INSTANCE.getLoadedDimensionKeys()) {
         val var10000: WaypointDimensionData = PersistentWaypointStore.INSTANCE.getDimensionData(dimKey)
         if (var10000 != null) {
            run label68@{
               val var11: WaypointSet = var10000.getActiveSet()
               if (var11 != null) {
                  val var12: java.util.List = var11.getWaypoints()
                  if (var12 != null) {
                     val `$this$any$iv`: java.lang.Iterable = var12
                     var var14: Boolean
                     if (var12 is java.util.Collection && (var12 as java.util.Collection).isEmpty()) {
                        var14 = false
                     } else {
                        val var7: java.util.Iterator = `$this$any$iv`.iterator()

                        while (true) {
                           if (!var7.hasNext()) {
                              var14 = false
                              break
                           }

                           if ((var7.next() as PersistentWaypoint).getId() == id) {
                              var14 = true
                              break
                           }
                        }
                     }

                     var13 = var14
                     return@label68
                  }
               }

               var13 = false
            }

            if (var13) {
               return dimKey
            }
         }
      }

      return null
   }

   private fun moveWaypoint(id: UUID, fromDim: String, toDim: String) {
      val var10000: WaypointDimensionData = PersistentWaypointStore.INSTANCE.getDimensionData(fromDim)
      if (var10000 != null) {
         val var12: WaypointSet = var10000.getActiveSet()
         if (var12 != null) {
            val var13: java.util.List = var12.getWaypoints()
            if (var13 != null) {
               val var8: java.util.Iterator = var13.iterator()

               while (true) {
                  if (!var8.hasNext()) {
                     var14 = null
                     break
                  }

                  val `element$iv`: Any = var8.next()
                  if ((`element$iv` as PersistentWaypoint).getId() == id) {
                     var14 = `element$iv`
                     break
                  }
               }

               val var15: PersistentWaypoint = var14 as PersistentWaypoint
               if (var14 as PersistentWaypoint != null) {
                  val var16: WaypointSet = var10000.getActiveSet()
                  if (var16 != null) {
                     val var17: java.util.List = var16.getWaypoints()
                     if (var17 != null) {
                        var17.removeIf({ p0: Any ->
                           `$tmp0`(p0)
                        })
                     }
                  }

                  PersistentWaypointStore.INSTANCE.saveDimension(fromDim)
                  PersistentWaypointStore.INSTANCE.getOrCreateDimensionData(toDim).getOrCreateActiveSet().getWaypoints().add(var15)
                  PersistentWaypointStore.INSTANCE.saveDimension(toDim)
                  return
               }
            }
         }
      }
   }
}
