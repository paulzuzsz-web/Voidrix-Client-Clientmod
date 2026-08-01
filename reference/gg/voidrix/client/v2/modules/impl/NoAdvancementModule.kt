package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import org.jetbrains.annotations.NotNull

@VoidrixMiniTag(tags = ["noadvancement"])
public object NoAdvancementModule : Module("NoAdvancement", ModuleCategory.VISUAL, false, false, false, 20) {
   @Category(name = "Toasts")
   @NotNull
   public final val advancements: Boolean by ValueApiKt.boolean$default(true, "Advancements", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return advancements$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   @Category(name = "Toasts")
   @NotNull
   public final val recipes: Boolean by ValueApiKt.boolean$default(true, "Recipes", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return recipes$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   @Category(name = "Toasts")
   @NotNull
   public final val system: Boolean by ValueApiKt.boolean$default(true, "System", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return system$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   @Category(name = "Toasts")
   @NotNull
   public final val tutorial: Boolean by ValueApiKt.boolean$default(true, "Tutorial", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return tutorial$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   @Category(name = "Toasts")
   @NotNull
   public final val nowPlaying: Boolean by ValueApiKt.boolean$default(true, "Now Playing", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return nowPlaying$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   public open val seoTags: Array<String>

   @JvmStatic
   public fun isActive(): Boolean {
      return INSTANCE.isEnabled()
   }

   @JvmStatic
   public fun shouldBlockAdvancements(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.advancements
   }

   @JvmStatic
   public fun shouldBlockRecipes(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.recipes
   }

   @JvmStatic
   public fun shouldBlockSystem(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.system
   }

   @JvmStatic
   public fun shouldBlockTutorial(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.tutorial
   }

   @JvmStatic
   public fun shouldBlockNowPlaying(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.nowPlaying
   }
}
