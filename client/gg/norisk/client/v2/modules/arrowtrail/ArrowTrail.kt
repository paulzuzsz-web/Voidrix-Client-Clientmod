package gg.norisk.client.v2.modules.arrowtrail

import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.enums.EnumEntries
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType

public object ArrowTrail : Module("ArrowTrail", ModuleCategory.COSMETIC, false, false, false, 28) {
   public final val effect: gg.norisk.client.v2.modules.arrowtrail.ArrowTrail.TrailType by ValueApiKt.enum$default(
         ArrowTrail.TrailType.CLOUD, "Trail Type", null, null, null, 28, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return effect$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as ArrowTrail.TrailType
      }


   public open val seoTags: Array<String>

   @JvmStatic
   public final var forceSpawn: Boolean

   public enum class TrailType(effect: () -> ParticleOptions) {
      LAVA,
      HEART,
      CLOUD,
      FLAME,
      NOTE,
      PORTAL;

      public final val effect: () -> ParticleOptions

      init {
         this.effect = effect
      }

      private constructor(defaultType: ParticleOptions) : this({ 
            `$defaultType`
         })
      @JvmStatic
      fun getEntries(): EnumEntries<ArrowTrail.TrailType> {
         $ENTRIES
      }

      // $VF: Failed to inline enum fields
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      @JvmStatic
      fun {
         var var10004: SimpleParticleType = ParticleTypes.LAVA
         LAVA = ArrowTrail.TrailType(var10004 as ParticleOptions)
         var10004 = ParticleTypes.HEART
         HEART = ArrowTrail.TrailType(var10004 as ParticleOptions)
         var10004 = ParticleTypes.CLOUD
         CLOUD = ArrowTrail.TrailType(var10004 as ParticleOptions)
         var10004 = ParticleTypes.FLAME
         FLAME = ArrowTrail.TrailType(var10004 as ParticleOptions)
         var10004 = ParticleTypes.NOTE
         NOTE = ArrowTrail.TrailType(var10004 as ParticleOptions)
         var10004 = ParticleTypes.PORTAL
         PORTAL = ArrowTrail.TrailType(var10004 as ParticleOptions)
      }
   }
}
