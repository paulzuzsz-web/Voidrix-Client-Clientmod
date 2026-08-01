package gg.norisk.client.v2.modules.hitbox

import com.mojang.authlib.GameProfile
import gg.norisk.client.v2.modules.hitbox.HitBox.Type
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.modules.ModulesScreenManager
import gg.norisk.ui.modules.api.ModuleEntry
import java.util.ArrayList
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.player.RemotePlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.arrow.Arrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

@SourceDebugExtension(["SMAP\nHitBoxPreview.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HitBoxPreview.kt\ngg/norisk/client/v2/modules/hitbox/HitBoxPreview\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,382:1\n1761#2,3:383\n328#3:386\n40#3:387\n*S KotlinDebug\n*F\n+ 1 HitBoxPreview.kt\ngg/norisk/client/v2/modules/hitbox/HitBoxPreview\n*L\n68#1:383,3\n215#1:386\n215#1:387\n*E\n"])
public object HitBoxPreview {
   private const val COUNT: Int = 3
   private const val ID_BASE: Int = 1500000000

   public final var previewType: Type?
      public final set(value) {
         if (previewType != value) {
            previewType = value
            this.despawn()
         }
      }


   private final val entities: ArrayList<Entity> = ArrayList()
   private const val FORWARD_DISTANCE: Double = 2.8
   private const val SIDE_SPACING: Double = 1.4

   public final val active: Boolean
      public final get() {
         return !entities.isEmpty()
      }


   public fun forcedType(entity: Any): Type? {
      return if (entity is Entity && this.isPreviewEntity(entity as Entity)) previewType else null
   }

   private fun isPreviewEntity(entity: Entity): Boolean {
      var var10000: Entity = entities.iterator()
      val var2: java.util.Iterator = var10000

      while (var2.hasNext()) {
         var10000 = (Entity)var2.next()
         if (var10000 === entity) {
            return true
         }
      }

      return false
   }

   public fun tick() {
      val type: HitBox.Type = previewType
      if (previewType != null && this.isInHitBoxScreen()) {
         val mc: Minecraft = Minecraft.getInstance()
         val world: ClientLevel = mc.level
         val player: LocalPlayer = mc.player
         if (mc.level != null && mc.player != null) {
            run label79@{
               if (!entities.isEmpty()) {
                  val `$this$any$iv`: java.lang.Iterable = entities
                  var var10000: Boolean
                  if (entities is java.util.Collection && entities.isEmpty()) {
                     var10000 = false
                  } else {
                     val var7: java.util.Iterator = `$this$any$iv`.iterator()

                     while (true) {
                        if (!var7.hasNext()) {
                           var10000 = false
                           break
                        }

                        if (INSTANCE.isGone(var7.next() as Entity)) {
                           var10000 = true
                           break
                        }
                     }
                  }

                  if (!var10000) {
                     return@label79
                  }
               }

               this.despawn()
               this.spawn(world, type)
            }

            this.reposition(player as Entity)
         } else {
            if (!entities.isEmpty()) {
               this.despawn()
            }
         }
      } else {
         if (!entities.isEmpty()) {
            this.despawn()
         }
      }
   }

   private fun despawn() {
      var var10000: Entity = entities.iterator()
      val var1: java.util.Iterator = var10000

      while (var1.hasNext()) {
         var10000 = (Entity)var1.next()
         this.discardEntity(var10000)
      }

      entities.clear()
   }

   private fun spawn(world: ClientLevel, type: Type) {
      repeat(2) { i ->
         val var10000: Entity = this.createEntity(world, type)
         if (var10000 != null) {
            this.freeze(var10000)
            this.addToWorld(world, var10000, 1500000000 - i)
            entities.add(var10000)
         }
      }
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
            this.place(
               entity,
               baseX + rightX * ((double)(i - 1) * 1.4),
               baseY - (double)entity.getBbHeight() / 2.0,
               baseZ + rightZ * ((double)(i - 1) * 1.4),
               (float)(
                  -Math.toDegrees(
                     Math.atan2(player.getX() - (baseX + rightX * ((double)(i - 1) * 1.4)), player.getZ() - (baseZ + rightZ * ((double)(i - 1) * 1.4)))
                  )
               )
            )
         }
      }
   }

   private fun place(entity: Entity, x: Double, y: Double, z: Double, yaw: Float) {
      entity.setPos(x, y, z)
      entity.xo = x
      entity.yo = y
      entity.zo = z
      this.setYaw(entity, yaw)
      if (entity is LivingEntity) {
         (entity as LivingEntity).yBodyRot = yaw
         (entity as LivingEntity).yBodyRotO = yaw
         (entity as LivingEntity).yHeadRot = yaw
         (entity as LivingEntity).yHeadRotO = yaw
      }
   }

   private fun freeze(entity: Entity) {
      entity.noPhysics = true
      entity.setNoGravity(true)
      entity.setInvulnerable(true)
      entity.setSilent(true)
      if (entity is Mob) {
         (entity as Mob).setNoAi(true)
      }
   }

   private fun createEntity(world: ClientLevel, type: Type): Entity? {
      var var10000: Entity
      when (HitBoxPreview.WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) {
         1, 2 -> var10000 = this.createPlayer(world) as Entity
         3 -> {
            val var10002: EntityType = EntityTypes.COW
            var10000 = this.createTyped(world, var10002)
         }
         4 -> var10000 = ItemEntity(world as Level, 0.0, 0.0, 0.0, ItemStack(Items.DIAMOND as ItemLike)) as Entity
         5 -> var10000 = this.createArrow(world) as Entity
         6 -> var10000 = ExperienceOrb(world as Level, 0.0, 0.0, 0.0, 1) as Entity
         7 -> var10000 = ArmorStand(world as Level, 0.0, 0.0, 0.0) as Entity
         else -> throw NoWhenBranchMatchedException()
      }

      return var10000
   }

   private fun <T : Entity> createTyped(world: ClientLevel, type: EntityType<T>): T? {
      return (T)type.create(world as Level, EntitySpawnReason.LOAD)
   }

   private fun createPlayer(world: ClientLevel): RemotePlayer {
      return RemotePlayer(world, GameProfile(UUID.randomUUID(), "HitboxPreview"))
   }

   private fun createArrow(world: ClientLevel): Arrow {
      return Arrow(world as Level, 0.0, 0.0, 0.0, ItemStack(Items.ARROW as ItemLike), null)
   }

   private fun addToWorld(world: ClientLevel, entity: Entity, id: Int) {
      entity.setId(id)
      world.addEntity(entity)
   }

   private fun discardEntity(entity: Entity) {
      entity.discard()
   }

   private fun isGone(entity: Entity): Boolean {
      return entity.isRemoved()
   }

   private fun setYaw(entity: Entity, yaw: Float) {
      entity.setYRot(yaw)
   }

   private fun isInHitBoxScreen(): Boolean {
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
         return (if (var5 != null) var5.getName() else null) == HitBox.INSTANCE.getName()
      }
   }
}
