package gg.norisk.client.v2.modules.particle

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable
public data class ParticleTypeSettings(enabled: Boolean = true,
   multiplier: Double = 1.0,
   scale: Double = 1.0,
   color: Int = 0,
   overlayColor: Int = 0,
   spawnOnHit: Boolean = false
) {
   public final var enabled: Boolean
      internal set

   public final var multiplier: Double
      internal set

   public final var scale: Double
      internal set

   public final var color: Int
      internal set

   public final var overlayColor: Int
      internal set

   public final var spawnOnHit: Boolean
      internal set

   init {
      this.enabled = enabled
      this.multiplier = multiplier
      this.scale = scale
      this.color = color
      this.overlayColor = overlayColor
      this.spawnOnHit = spawnOnHit
   }

   public fun isDefault(): Boolean {
      return this.enabled && this.multiplier == 1.0 && this.scale == 1.0 && this.color == 0 && this.overlayColor == 0 && !this.spawnOnHit
   }

   public operator fun component1(): Boolean {
      return this.enabled
   }

   public operator fun component2(): Double {
      return this.multiplier
   }

   public operator fun component3(): Double {
      return this.scale
   }

   public operator fun component4(): Int {
      return this.color
   }

   public operator fun component5(): Int {
      return this.overlayColor
   }

   public operator fun component6(): Boolean {
      return this.spawnOnHit
   }

   public fun copy(
      enabled: Boolean = this.enabled,
      multiplier: Double = this.multiplier,
      scale: Double = this.scale,
      color: Int = this.color,
      overlayColor: Int = this.overlayColor,
      spawnOnHit: Boolean = this.spawnOnHit
   ): ParticleTypeSettings {
      return ParticleTypeSettings(enabled, multiplier, scale, color, overlayColor, spawnOnHit)
   }

   public override fun toString(): String {
      return "ParticleTypeSettings(enabled=${this.enabled}, multiplier=${this.multiplier}, scale=${this.scale}, color=${this.color}, overlayColor=${this.overlayColor}, spawnOnHit=${this.spawnOnHit})"
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (java.lang.Boolean.hashCode(this.enabled) * 31 + java.lang.Double.hashCode(this.multiplier)) * 31
                                    + java.lang.Double.hashCode(this.scale)
                              )
                              * 31
                           + Integer.hashCode(this.color)
                     )
                     * 31
                  + Integer.hashCode(this.overlayColor)
            )
            * 31
         + java.lang.Boolean.hashCode(this.spawnOnHit)
      }

   public override operator fun equals(other: Any?): Boolean {
      label52@
      if (this === other) {
         return true
      } else {
         return other is ParticleTypeSettings
            && this.enabled == (other as ParticleTypeSettings).enabled
            && java.lang.Double.compare(this.multiplier, (other as ParticleTypeSettings).multiplier) == 0
            && java.lang.Double.compare(this.scale, (other as ParticleTypeSettings).scale) == 0
            && this.color == (other as ParticleTypeSettings).color
            && this.overlayColor == (other as ParticleTypeSettings).overlayColor
            && this.spawnOnHit == (other as ParticleTypeSettings).spawnOnHit
         }
   }

   fun ParticleTypeSettings() {
      this(false, 0.0, 0.0, 0, 0, false, 63, null)
   }

   public companion object {
      public fun serializer(): KSerializer<ParticleTypeSettings> {
         return ParticleTypeSettings.$serializer.INSTANCE as KSerializer<ParticleTypeSettings>
      }
   }
}
