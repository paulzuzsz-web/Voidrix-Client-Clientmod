package gg.voidrix.client.v2.modules.shinypots

import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nShinyPots.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ShinyPots.kt\ngg/voidrix/client/v2/modules/shinypots/ShinyPots\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,33:1\n1617#2,9:34\n1869#2:43\n1870#2:46\n1626#2:47\n1#3:44\n1#3:45\n*S KotlinDebug\n*F\n+ 1 ShinyPots.kt\ngg/voidrix/client/v2/modules/shinypots/ShinyPots\n*L\n27#1:34,9\n27#1:43\n27#1:46\n27#1:47\n27#1:45\n*E\n"])
public object ShinyPots : Module("Shiny Pots", ModuleCategory.QUALITY_OF_LIFE, false, false, false, 20) {
   public open val seoTags: Array<String>

   private final val potionIds: Set<Identifier> by LazyKt.lazy(
      { 
         val `$this$mapNotNullTo$iv$iv`: java.lang.Iterable = CollectionsKt.listOf(
            arrayOf("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion")
         )
         val `destination$iv$iv`: java.util.Collection = ArrayList()

         for (`element$iv$iv$iv` in `$this$mapNotNullTo$iv$iv`) {
            val it: java.lang.String = `element$iv$iv$iv` as java.lang.String
            val var13: ShinyPots = INSTANCE

            var `$this$potionIds_delegate_u24lambda_u242_u24lambda_u241_u24lambda_u240`: Any
            try {
               `$this$potionIds_delegate_u24lambda_u242_u24lambda_u241_u24lambda_u240` = Result.constructor_impl/* $VF was: constructor-impl */(
                  MCRegistryKt.registryId(MCRegistryKt.mcItem(it))
               )
            } catch (var18: java.lang.Throwable) {
               `$this$potionIds_delegate_u24lambda_u242_u24lambda_u241_u24lambda_u240` = Result.constructor_impl/* $VF was: constructor-impl */(
                  ResultKt.createFailure(var18)
               )
            }

            val var10000: Any = (
               if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$potionIds_delegate_u24lambda_u242_u24lambda_u241_u24lambda_u240`))
                  null
                  else
                  `$this$potionIds_delegate_u24lambda_u242_u24lambda_u241_u24lambda_u240`
            ) as Identifier
            if (var10000 != null) {
               `destination$iv$iv`.add(var10000)
            }
         }

         CollectionsKt.toSet(`destination$iv$iv` as java.util.List)
      }
   )
      private final get() {
         return potionIds$delegate.getValue() as MutableSet<Identifier>
      }


   public open fun createdAt(): Long {
      return 1776844800000L
   }

   public fun isPotion(id: Identifier): Boolean {
      return this.potionIds.contains(id)
   }
}
