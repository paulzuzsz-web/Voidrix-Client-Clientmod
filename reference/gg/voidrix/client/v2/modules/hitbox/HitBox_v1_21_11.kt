package gg.voidrix.client.v2.modules.hitbox

import gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings
import java.awt.Color
import kotlin.jvm.internal.ArrayIteratorKt
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.debug.DebugRenderer.SimpleDebugRenderer
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.util.ARGB
import net.minecraft.util.debug.DebugValueAccess
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

public object HitBox_v1_21_11 : SimpleDebugRenderer {
   private final val client: Minecraft = Minecraft.getInstance()

   public open fun emitGizmos(cameraX: Double, cameraY: Double, cameraZ: Double, store: DebugValueAccess, frustum: Frustum, tickProgress: Float) {
      if (HitBox.INSTANCE.isEnabled()) {
         if (client.level != null) {
            for (entity in client.level.entitiesForRendering()) {
               if (!entity.isInvisible()
                  && frustum.isVisible(entity.getBoundingBox())
                  && (!(entity == client.getCameraEntity()) || client.options.getCameraType() != CameraType.FIRST_PERSON)) {
                  if (!HitBox.shouldSkipHitbox(entity)) {
                     val var10000: HitBox.Type = HitBox.Type.Companion.classify(entity)
                     if (var10000 != null) {
                        val settings: HitBox.HitBoxSettings = HitBox.INSTANCE.getSettings(var10000)
                        if (settings.visible) {
                           this.drawHitbox(entity, tickProgress, settings)
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private fun drawHitbox(entity: Entity, tickProgress: Float, settings: HitBoxSettings) {
      val entityPos: Vec3 = entity.position()
      val lerpedPos: Vec3 = entity.getPosition(tickProgress)
      val offset: Vec3 = lerpedPos.subtract(entityPos)
      val colorInt: Int = this.colorToArgb(HitBox.getEffectiveColor(entity, settings))
      Gizmos.cuboid(entity.getBoundingBox().move(offset), GizmoStyle.stroke(colorInt, settings.lineWidth))
      if (settings.showLookVector) {
         Gizmos.point(lerpedPos, colorInt, 2.0F)
      }

      val var10000: Entity = entity.getVehicle()
      if (var10000 != null) {
         val partOffset: Float = Math.min(var10000.getBbWidth(), entity.getBbWidth()) / 2.0F
         val attachmentPos: Vec3 = var10000.getPassengerRidingPosition(entity).add(offset)
         Gizmos.cuboid(
            AABB(
               attachmentPos.x - (double)partOffset,
               attachmentPos.y,
               attachmentPos.z - (double)partOffset,
               attachmentPos.x + (double)partOffset,
               attachmentPos.y + 0.0625,
               attachmentPos.z + (double)partOffset
            ),
            GizmoStyle.stroke(-256, settings.lineWidth)
         )
      }

      if (settings.showLookVector && entity is LivingEntity) {
         val eyePos: AABB = (entity as LivingEntity).getBoundingBox().move(offset)
         val lookVec: Float = (entity as LivingEntity).getEyeHeight()
         Gizmos.cuboid(
            AABB(eyePos.minX, eyePos.minY + (double)lookVec - 0.01, eyePos.minZ, eyePos.maxX, eyePos.minY + (double)lookVec + 0.01, eyePos.maxZ),
            GizmoStyle.stroke(-65536, settings.lineWidth)
         )
      }

      if (entity is EnderDragon) {
         val var15: java.util.Iterator = ArrayIteratorKt.iterator((entity as EnderDragon).getSubEntities())

         while (var15.hasNext()) {
            val var17: EnderDragonPart = var15.next() as EnderDragonPart
            Gizmos.cuboid(
               var17.getBoundingBox().move(var17.getPosition(tickProgress).subtract(var17.position())),
               GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F), settings.lineWidth)
            )
         }
      }

      if (settings.showLookVector) {
         val var16: Vec3 = lerpedPos.add(0.0, (double)entity.getEyeHeight(), 0.0)
         Gizmos.arrow(var16, var16.add(entity.getViewVector(tickProgress).scale(2.0)), this.colorToArgb(settings.facingColor.getChromaOrDefault()))
      }
   }

   private fun colorToArgb(color: Color): Int {
      return color.getAlpha() shl 24 or color.getRed() shl 16 or color.getGreen() shl 8 or color.getBlue()
   }
}
