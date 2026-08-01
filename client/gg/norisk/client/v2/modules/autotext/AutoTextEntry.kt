package gg.norisk.client.v2.modules.autotext

import gg.norisk.compat.resource.MCKey
import gg.norisk.ui.api.value.KeyValue.KeyWrapper
import java.util.ArrayList
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayListSerializer

@Serializable
@SourceDebugExtension(["SMAP\nAutoTextEntry.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoTextEntry.kt\ngg/norisk/client/v2/modules/autotext/AutoTextEntry\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,23:1\n1563#2:24\n1634#2,3:25\n1563#2:28\n1634#2,3:29\n2746#2,3:32\n*S KotlinDebug\n*F\n+ 1 AutoTextEntry.kt\ngg/norisk/client/v2/modules/autotext/AutoTextEntry\n*L\n16#1:24\n16#1:25,3\n19#1:28\n19#1:29,3\n21#1:32,3\n*E\n"])
public data class AutoTextEntry(id: String = UUID.randomUUID().toString(),
   text: String = "",
   keys: MutableList<KeyWrapper> = ArrayList() as java.util.List,
   isEnabled: Boolean = true
) {
   public final val id: String

   public final var text: String
      internal set

   public final var keys: MutableList<KeyWrapper>
      internal set

   public final var isEnabled: Boolean
      internal set

   @JvmField
   @JvmStatic
   private KSerializer<Object>[] $childSerializers = arrayOf(
      null,
      null,
      ArrayListSerializer(gg.norisk.ui.api.value.KeyValue.KeyWrapper..serializer.INSTANCE as KSerializer),
      null
   );

   init {
      this.id = id
      this.text = text
      this.keys = keys
      this.isEnabled = isEnabled
   }

   public fun toMCKeys(): Set<MCKey> {
      val `$this$mapTo$iv$iv`: java.lang.Iterable = this.keys
      val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(this.keys, 10))

      for (`item$iv$iv` in `$this$mapTo$iv$iv`) {
         `destination$iv$iv`.add(MCKey.Companion.fromName((`item$iv$iv` as KeyWrapper).getTranslationKey()))
      }

      return CollectionsKt.toSet(`destination$iv$iv` as java.util.List)
   }

   public fun toMCKeyList(): List<MCKey> {
      val `$this$mapTo$iv$iv`: java.lang.Iterable = this.keys
      val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(this.keys, 10))

      for (`item$iv$iv` in `$this$mapTo$iv$iv`) {
         `destination$iv$iv`.add(MCKey.Companion.fromName((`item$iv$iv` as KeyWrapper).getTranslationKey()))
      }

      return `destination$iv$iv` as MutableList<MCKey>
   }

   public fun hasValidKeys(): Boolean {
      if (!this.keys.isEmpty()) {
         val `$this$none$iv`: java.lang.Iterable = this.keys
         var var10000: Boolean
         if (this.keys is java.util.Collection && this.keys.isEmpty()) {
            var10000 = true
         } else {
            val var3: java.util.Iterator = `$this$none$iv`.iterator()

            while (true) {
               if (!var3.hasNext()) {
                  var10000 = true
                  break
               }

               if (MCKey.Companion.fromName((var3.next() as KeyWrapper).getTranslationKey()).isUnknown()) {
                  var10000 = false
                  break
               }
            }
         }

         if (var10000) {
            return true
         }
      }

      return false
   }

   public operator fun component1(): String {
      return this.id
   }

   public operator fun component2(): String {
      return this.text
   }

   public operator fun component3(): MutableList<KeyWrapper> {
      return this.keys
   }

   public operator fun component4(): Boolean {
      return this.isEnabled
   }

   public fun copy(id: String = this.id, text: String = this.text, keys: MutableList<KeyWrapper> = this.keys, isEnabled: Boolean = this.isEnabled): AutoTextEntry {
      return AutoTextEntry(id, text, keys, isEnabled)
   }

   public override fun toString(): String {
      return "AutoTextEntry(id=${this.id}, text=${this.text}, keys=${this.keys}, isEnabled=${this.isEnabled})"
   }

   public override fun hashCode(): Int {
      return ((this.id.hashCode() * 31 + this.text.hashCode()) * 31 + this.keys.hashCode()) * 31 + java.lang.Boolean.hashCode(this.isEnabled)
   }

   public override operator fun equals(other: Any?): Boolean {
      label40@
      if (this === other) {
         return true
      } else {
         return other is AutoTextEntry
            && this.id == (other as AutoTextEntry).id
            && this.text == (other as AutoTextEntry).text
            && this.keys == (other as AutoTextEntry).keys
            && this.isEnabled == (other as AutoTextEntry).isEnabled
         }
   }

   fun AutoTextEntry() {
      this(null, null, null, false, 15, null)
   }

   public companion object {
      public fun serializer(): KSerializer<AutoTextEntry> {
         return AutoTextEntry.$serializer.INSTANCE as KSerializer<AutoTextEntry>
      }
   }
}
