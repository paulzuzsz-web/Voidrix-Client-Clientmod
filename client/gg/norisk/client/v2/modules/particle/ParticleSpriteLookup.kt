package gg.norisk.client.v2.modules.particle

import gg.norisk.client.v2.mixin.particle.ParticleEngineAccessor
import gg.norisk.client.v2.mixin.particle.ParticleResourcesAccessor
import gg.norisk.compat.client.ParticleSpriteInfo
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleResources
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nParticleSpriteLookup.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ParticleSpriteLookup.kt\ngg/norisk/client/v2/modules/particle/ParticleSpriteLookup\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,205:1\n1#2:206\n*E\n"])
public object ParticleSpriteLookup {
   public fun get(typeId: String): ParticleSpriteInfo? {
      val parts: java.util.List = StringsKt.split$default(typeId, arrayOf(":"), false, 0, 6, null)
      if (parts.size() != 2) {
         return null
      } else {
         val var19: Identifier = Identifier.fromNamespaceAndPath(parts.get(0) as java.lang.String, parts.get(1) as java.lang.String)
         val var25: ParticleEngine = Minecraft.getInstance().particleEngine
         if (var25 == null) {
            return null
         } else {
            val var26: ParticleEngineAccessor = var25 as? ParticleEngineAccessor
            if ((var25 as? ParticleEngineAccessor) == null) {
               return null
            } else {
               val var27: ParticleResources = var26.nrc$getResourceManager()
               if (var27 == null) {
                  return null
               } else {
                  val var28: ParticleResourcesAccessor = var27 as? ParticleResourcesAccessor
                  if ((var27 as? ParticleResourcesAccessor) == null) {
                     return null
                  } else {
                     val var29: SpriteSet = var28.nrc$getSpriteSets().get(var19)
                     if (var29 == null) {
                        return null
                     } else {
                        val spriteSet: SpriteSet = var29
                        val v0: ParticleSpriteLookup = this

                        var u1: ParticleSpriteLookup
                        try {
                           u1 = v0
                           u1 = (ParticleSpriteLookup)Result.constructor_impl/* $VF was: constructor-impl */(spriteSet.first())
                        } catch (var18: java.lang.Throwable) {
                           u1 = (ParticleSpriteLookup)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var18))
                        }

                        val var30: TextureAtlasSprite = (if (Result.isFailure_impl/* $VF was: isFailure-impl */(u1)) null else u1) as TextureAtlasSprite
                        if (var30 == null) {
                           return null
                        } else {
                           val u0: Float = var30.getU0()
                           val var21: Float = var30.getV0()
                           u1 = var30.getU1()
                           val var24: Float = var30.getV1()
                           val regionW: Int = var30.contents().width()
                           val regionH: Int = var30.contents().height()
                           val atlasW: Int = if (u1 > u0) (int)(regionW / (u1 - u0)) else regionW
                           val atlasH: Int = if (var24 > var21) (int)(regionH / (var24 - var21)) else regionH
                           val var10002: Identifier = var30.atlasLocation()
                           return ParticleSpriteInfo(var10002, (int)(u0 * atlasW), (int)(var21 * atlasH), regionW, regionH, atlasW, atlasH)
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
