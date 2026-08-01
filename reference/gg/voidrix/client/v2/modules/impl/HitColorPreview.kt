package gg.voidrix.client.v2.modules.impl

import com.mojang.authlib.GameProfile
import gg.voidrix.ui.modules.IModuleScreen
import gg.voidrix.ui.modules.ModulesScreenManager
import gg.voidrix.ui.modules.api.ModuleEntry
import java.util.ArrayList
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.player.RemotePlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

@SourceDebugExtension(["SMAP\nHitColorPreview.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HitColorPreview.kt\ngg/voidrix/client/v2/modules/impl/HitColorPreview\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,282:1\n1761#2,3:283\n328#3:286\n40#3:287\n*S KotlinDebug\n*F\n+ 1 HitColorPreview.kt\ngg/voidrix/client/v2/modules/impl/HitColorPreview\n*L\n51#1:283,3\n150#1:286\n150#1:287\n*E\n"])
public object HitColorPreview {
   private const val COUNT: Int = 3
   private const val ID_BASE: Int = 1500100000
   private const val FORWARD_DISTANCE: Double = 2.8
   private const val SIDE_SPACING: Double = 1.4
   private final val entities: ArrayList<Entity> = ArrayList()
   private final var wasActive: Boolean

   public final val active: Boolean
      public final get() {
         return !entities.isEmpty()
      }


   public fun tick() {
      if (!this.isInHitColorScreen()) {
         if (!entities.isEmpty()) {
            this.despawn()
            if (wasActive) {
               wasActive = false
               HitColorModule.INSTANCE.reload()
            }
         }
      } else {
         val mc: Minecraft = Minecraft.getInstance()
         val world: ClientLevel = mc.level
         val player: LocalPlayer = mc.player
         if (mc.level != null && mc.player != null) {
            run label106@{
               if (!entities.isEmpty()) {
                  val `$this$any$iv`: java.lang.Iterable = entities
                  var var10000: Boolean
                  if (entities is java.util.Collection && entities.isEmpty()) {
                     var10000 = false
                  } else {
                     val var6: java.util.Iterator = `$this$any$iv`.iterator()

                     while (true) {
                        if (!var6.hasNext()) {
                           var10000 = false
                           break
                        }

                        if (INSTANCE.isGone(var6.next() as Entity)) {
                           var10000 = true
                           break
                        }
                     }
                  }

                  if (!var10000) {
                     return@label106
                  }
               }

               this.despawn()
               this.spawn(world)
            }

            var var12: Entity = entities.iterator()
            val var10: java.util.Iterator = var12

            while (var10.hasNext()) {
               var12 = (Entity)var10.next()
               val var11: Entity = var12
               if (var12 is LivingEntity) {
                  (var11 as LivingEntity).hurtTime = 10
               }
            }

            this.reposition(player as Entity)
            if (!wasActive && !entities.isEmpty()) {
               wasActive = true
               HitColorModule.INSTANCE.reload()
            }
         } else {
            if (!entities.isEmpty()) {
               this.despawn()
               wasActive = false
               HitColorModule.INSTANCE.reload()
            }
         }
      }
   }

   private fun spawn(world: ClientLevel) {
      repeat(2) { i ->
         val entity: RemotePlayer = RemotePlayer(world, GameProfile(UUID.randomUUID(), "HitColorPreview"))
         entity.noPhysics = true
         entity.setNoGravity(true)
         entity.setInvulnerable(true)
         entity.setSilent(true)
         entity.setId(1500100000 - i)
         world.addEntity(entity as Entity)
         entities.add(entity)
      }
   }

   private fun despawn() {
      var var10000: Entity = entities.iterator()
      val var1: java.util.Iterator = var10000

      while (var1.hasNext()) {
         var10000 = (Entity)var1.next()
         var10000.discard()
      }

      entities.clear()
   }

   private fun reposition(player: Entity) {
      if (!entities.isEmpty()) {
         val look: Vec3 = player.getLookAngle()
         val eye: Vec3 = player.getEyePosition(1.0F)
         val horizLen: Double = Math.sqrt(look.x * look.x + look.z * look.z)
         var rightX: Double = 0.0
         var rightZ: Double = 0.0
         if (horizLen > 1.0E-4) {
            rightX = -look.z / horizLen
            rightZ = look.x / horizLen
         } else {
            rightX = 1.0
            rightZ = 0.0
         }

         val baseX: Double = eye.x + look.x * 2.8
         val baseY: Double = eye.y + look.y * 2.8
         val baseZ: Double = eye.z + look.z * 2.8
         val var16: java.util.Iterator = entities.iterator()
         var var17: Int = 0

         while (var16.hasNext()) {
            val i: Int = var17++
            val entity: Entity = var16.next() as Entity
            val x: Double = baseX + rightX * ((i - 1) * 1.4)
            val z: Double = baseZ + rightZ * ((i - 1) * 1.4)
            val y: Double = baseY - entity.getBbHeight() / 2.0
            val yaw: Float = (float)(-Math.toDegrees(Math.atan2(player.getX() - x, player.getZ() - z)))
            entity.setPos(x, y, z)
            entity.xo = x
            entity.yo = y
            entity.zo = z
            entity.setYRot(yaw)
            if (entity is LivingEntity) {
               (entity as LivingEntity).yBodyRot = yaw
               (entity as LivingEntity).yBodyRotO = yaw
               (entity as LivingEntity).yHeadRot = yaw
               (entity as LivingEntity).yHeadRotO = yaw
            }
         }
      }
   }

   private fun isGone(entity: Entity): Boolean {
      return entity.isRemoved()
   }

   private fun isInHitColorScreen(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      val var4: Screen = var10000.gui.screen()
      if (var4 == null) {
         return false
      } else if (var4 !is IModuleScreen) {
         return false
      } else if (!(var4 as IModuleScreen).isInSettingsView()) {
         return false
      } else {
         val var5: ModuleEntry = ModulesScreenManager.INSTANCE.getLastOpenModule()
         return (if (var5 != null) var5.getName() else null) == HitColorModule.INSTANCE.getName()
      }
   }
}
