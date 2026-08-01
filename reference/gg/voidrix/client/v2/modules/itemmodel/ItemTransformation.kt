package gg.voidrix.client.v2.modules.itemmodel

import gg.voidrix.compat.render.VoidrixItemDisplayContext
import gg.voidrix.compat.render.VoidrixVector3f
import java.util.LinkedHashMap
import java.util.Map.Entry
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.LinkedHashMapSerializer

@Serializable
@SourceDebugExtension(["SMAP\nItemTransformation.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemTransformation.kt\ngg/voidrix/client/v2/modules/itemmodel/ItemTransformation\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,46:1\n384#2,7:47\n465#2:57\n415#2:58\n1740#3,3:54\n1252#3,4:59\n*S KotlinDebug\n*F\n+ 1 ItemTransformation.kt\ngg/voidrix/client/v2/modules/itemmodel/ItemTransformation\n*L\n37#1:47,7\n43#1:57\n43#1:58\n40#1:54,3\n43#1:59,4\n*E\n"])
public data class ItemTransformation(transformations: MutableMap<
            VoidrixItemDisplayContext,
            gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation
         > = LinkedHashMap() as java.util.Map
) {
   public final var transformations: MutableMap<VoidrixItemDisplayContext, gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation>
      internal set

   @JvmField
   @JvmStatic
   private KSerializer<Object>[] $childSerializers = arrayOf(
      LinkedHashMapSerializer(VoidrixItemDisplayContext.Companion.serializer(), ItemTransformation.Transformation.$serializer.INSTANCE as KSerializer)
   );

   init {
      this.transformations = transformations
   }

   public fun getOrCreate(context: VoidrixItemDisplayContext): gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation {
      val `$this$getOrPut$iv`: java.util.Map = this.transformations
      val `value$iv`: Any = this.transformations.get(context)
      val var10000: Any
      if (`value$iv` == null) {
         val var6: Any = ItemTransformation.Transformation(null, null, null, 7, null)
         `$this$getOrPut$iv`.put(context, var6)
         var10000 = var6
      } else {
         var10000 = `value$iv`
      }

      return var10000 as ItemTransformation.Transformation
   }

   public fun isEmpty(): Boolean {
      if (!this.transformations.isEmpty()) {
         val `$this$all$iv`: java.lang.Iterable = this.transformations.values()
         var var10000: Boolean
         if (`$this$all$iv` is java.util.Collection && (`$this$all$iv` as java.util.Collection).isEmpty()) {
            var10000 = true
         } else {
            val var3: java.util.Iterator = `$this$all$iv`.iterator()

            while (true) {
               if (!var3.hasNext()) {
                  var10000 = true
                  break
               }

               if (!(var3.next() as ItemTransformation.Transformation).isDefault()) {
                  var10000 = false
                  break
               }
            }
         }

         if (!var10000) {
            return false
         }
      }

      return true
   }

   public fun copy(): ItemTransformation {
      val `$this$mapValuesTo$iv$iv`: java.util.Map = this.transformations
      val `destination$iv$iv`: java.util.Map = LinkedHashMap(MapsKt.mapCapacity(this.transformations.size()))

      for (`element$iv$iv$iv` in `$this$mapValuesTo$iv$iv`.entrySet()) {
         `destination$iv$iv`.put((`element$iv$iv$iv` as Entry).getKey(), ((`element$iv$iv$iv` as Entry).getValue() as ItemTransformation.Transformation).copy())
      }

      return ItemTransformation(MapsKt.toMutableMap(`destination$iv$iv`))
   }

   public operator fun component1(): MutableMap<VoidrixItemDisplayContext, gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation> {
      return this.transformations
   }

   public fun copy(
      transformations: MutableMap<VoidrixItemDisplayContext, gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation> = this.transformations
   ): ItemTransformation {
      return ItemTransformation(transformations)
   }

   public override fun toString(): String {
      return "ItemTransformation(transformations=${this.transformations})"
   }

   public override fun hashCode(): Int {
      return this.transformations.hashCode()
   }

   public override operator fun equals(other: Any?): Boolean {
      label22@
      if (this === other) {
         return true
      } else {
         return other is ItemTransformation && this.transformations == (other as ItemTransformation).transformations
      }
   }

   fun ItemTransformation() {
      this(null, 1, null)
   }

   public companion object {
      public fun serializer(): KSerializer<ItemTransformation> {
         return ItemTransformation.$serializer.INSTANCE as KSerializer<ItemTransformation>
      }
   }

   @Serializable
   public data class Transformation(rotation: VoidrixVector3f = VoidrixVector3f(0.0F, 0.0F, 0.0F, 7, null),
      translation: VoidrixVector3f = VoidrixVector3f(0.0F, 0.0F, 0.0F, 7, null),
      scale: VoidrixVector3f = VoidrixVector3f(1.0F, 1.0F, 1.0F)
   ) {
      public final var rotation: VoidrixVector3f
         internal set

      public final var translation: VoidrixVector3f
         internal set

      public final var scale: VoidrixVector3f
         internal set

      init {
         this.rotation = rotation
         this.translation = translation
         this.scale = scale
      }

      public fun isDefault(): Boolean {
         return this.rotation.isZero() && this.translation.isZero() && this.scale.getX() == 1.0F && this.scale.getY() == 1.0F && this.scale.getZ() == 1.0F
      }

      public fun copyFrom(other: gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation) {
         this.rotation.copyFrom(other.rotation)
         this.translation.copyFrom(other.translation)
         this.scale.copyFrom(other.scale)
      }

      public fun copy(): gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation {
         return ItemTransformation.Transformation(this.rotation.copy(), this.translation.copy(), this.scale.copy())
      }

      public operator fun component1(): VoidrixVector3f {
         return this.rotation
      }

      public operator fun component2(): VoidrixVector3f {
         return this.translation
      }

      public operator fun component3(): VoidrixVector3f {
         return this.scale
      }

      public fun copy(rotation: VoidrixVector3f = this.rotation, translation: VoidrixVector3f = this.translation, scale: VoidrixVector3f = this.scale): gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation {
         return ItemTransformation.Transformation(rotation, translation, scale)
      }

      public override fun toString(): String {
         return "Transformation(rotation=${this.rotation}, translation=${this.translation}, scale=${this.scale})"
      }

      public override fun hashCode(): Int {
         return (this.rotation.hashCode() * 31 + this.translation.hashCode()) * 31 + this.scale.hashCode()
      }

      public override operator fun equals(other: Any?): Boolean {
         label34@
         if (this === other) {
            return true
         } else {
            return other is ItemTransformation.Transformation
               && this.rotation == (other as ItemTransformation.Transformation).rotation
               && this.translation == (other as ItemTransformation.Transformation).translation
               && this.scale == (other as ItemTransformation.Transformation).scale
            }
      }

      fun Transformation() {
         this(null, null, null, 7, null)
      }

      public companion object {
         public fun serializer(): KSerializer<gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation> {
            return ItemTransformation.Transformation.$serializer.INSTANCE as KSerializer<ItemTransformation.Transformation>
         }
      }
   }
}
