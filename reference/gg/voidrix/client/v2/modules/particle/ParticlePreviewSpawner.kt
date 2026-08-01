package gg.voidrix.client.v2.modules.particle

import gg.voidrix.compat.client.MCParticles
import gg.voidrix.ui.modules.IModuleScreen
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.phys.Vec3

@SourceDebugExtension(["SMAP\nParticlePreviewSpawner.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ParticlePreviewSpawner.kt\ngg/voidrix/client/v2/modules/particle/ParticlePreviewSpawner\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,143:1\n328#2:144\n40#2:145\n*S KotlinDebug\n*F\n+ 1 ParticlePreviewSpawner.kt\ngg/voidrix/client/v2/modules/particle/ParticlePreviewSpawner\n*L\n138#1:144\n138#1:145\n*E\n"])
public object ParticlePreviewSpawner {
   private final var tickCounter: Int

   public fun tick() {
      if (ParticleModule.INSTANCE.settingsOpen) {
         if (ParticleModule.INSTANCE.previewEnabled) {
            val var10000: java.lang.String = ParticleModule.INSTANCE.previewTypeId
            if (var10000 != null) {
               if (!this.isInParticleSettingsScreen()) {
                  ParticleModule.INSTANCE.settingsOpen = false
                  ParticleModule.INSTANCE.previewTypeId = null
               } else {
                  val var2: Int = tickCounter++
                  if (tickCounter % 2 == 0) {
                     this.spawnPattern(var10000)
                  }
               }
            }
         }
      }
   }

   private fun spawnPattern(typeId: String) {
      val mc: Minecraft = Minecraft.getInstance()
      if (mc.player != null) {
         val player: LocalPlayer = mc.player
         val eye: Vec3 = mc.player.getEyePosition(1.0F)
         val lookVec: Vec3 = player.getLookAngle()
         val lx: Double = lookVec.x
         val ly: Double = lookVec.y
         val lz: Double = lookVec.z
         val rightX: Double = -lookVec.z
         val rightZ: Double = lookVec.x
         val rightLen: Double = Math.sqrt(rightX * rightX + lookVec.x * lookVec.x)
         val rx: Double = if (rightLen > 0.001) rightX / rightLen else 1.0
         val rz: Double = if (rightLen > 0.001) rightZ / rightLen else 0.0
         val upX: Double = (if (rightLen > 0.001) rightZ / rightLen else 0.0) * ly - 0.0 * lz
         val upY: Double = 0.0 * lx - rx * lz
         val upZ: Double = rx * 0.0 - rz * lx
         val fixUpX: Double = -rz * ly * -1.0
         val cupX: Double = rz * ly - 0.0 * lz
         val cupY: Double = 0.0 * lx - rx * lz
         val cupZ: Double = rx * 0.0 - rz * lx
         val upLen: Double = Math.sqrt(cupX * cupX + cupY * cupY + (rx * 0.0 - rz * lx) * (rx * 0.0 - rz * lx))
         var uX: Double = 0.0
         var uY: Double = 0.0
         var uZ: Double = 0.0
         if (upLen > 0.001) {
            uX = cupX / upLen
            uY = cupY / upLen
            uZ = cupZ / upLen
         } else {
            uX = 0.0
            uY = 1.0
            uZ = 0.0
         }

         val distance: Double = 3.0
         val spacing: Double = 0.7
         ParticleModule.INSTANCE.previewSpawnActive = true

         try {
            for (row in -1..1) {
               for (col in -1..1) {
                  MCParticles.INSTANCE
                     .spawnPreviewParticle(
                        typeId,
                        eye.x + lx * distance + rx * (double)col * spacing + uX * (double)row * spacing,
                        eye.y + ly * distance + uY * (double)row * spacing,
                        eye.z + lz * distance + rz * (double)col * spacing + uZ * (double)row * spacing
                     )
                  }
            }
         } finally {
            ParticleModule.INSTANCE.previewSpawnActive = false
         }
      }
   }

   private fun isInParticleSettingsScreen(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      val var4: Screen = var10000.gui.screen()
      return var4 != null && var4 is IModuleScreen && (var4 as IModuleScreen).isInSettingsView()
   }
}
