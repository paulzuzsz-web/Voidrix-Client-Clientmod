package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.event.KeyEventData
import gg.voidrix.compat.event.KeyEvents
import gg.voidrix.compat.event.MouseClickEventData
import gg.voidrix.compat.event.MouseEvents
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.resource.MCKeyType
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen

@VoidrixMiniTag(tags = ["freelook"])
@SourceDebugExtension(["SMAP\nFreeLookModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FreeLookModule.kt\ngg/voidrix/client/v2/modules/impl/FreeLookModule\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,164:1\n328#2:165\n40#2:166\n*S KotlinDebug\n*F\n+ 1 FreeLookModule.kt\ngg/voidrix/client/v2/modules/impl/FreeLookModule\n*L\n49#1:165\n49#1:166\n*E\n"])
public object FreeLookModule : Module("FreeLook", ModuleCategory.VISUAL, false, false, false, 20) {
   public final val key: MCKey by ValueApiKt.key$default(82, null, null, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return key$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MCKey
      }


   public final val toggleFreelook: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return toggleFreelook$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   @JvmField
   public final var active: Boolean
      private set

   @JvmField
   public final var cameraYaw: Float
      private set

   @JvmField
   public final var cameraPitch: Float
      private set

   private final var needsInit: Boolean = true
   private final var lastActivationTime: Long

   private fun onFreelook(clicked: Boolean, released: Boolean) {
      if (this.isEnabled()) {
         if (!this.key.isUnknown()) {
            val var10000: Minecraft = Minecraft.getInstance()
            val screen: Screen = var10000.gui.screen()
            if (MCClient.isChatScreenOpen()) {
               lastActivationTime = System.currentTimeMillis()
            } else if (screen != null) {
               if (active) {
                  active = false
                  needsInit = true
               }
            } else {
               if (clicked && System.currentTimeMillis() - lastActivationTime > 150L) {
                  if (this.toggleFreelook) {
                     active = !active
                     if (!active) {
                        needsInit = true
                     }
                  } else {
                     active = true
                  }
               }

               if (released && !this.toggleFreelook) {
                  active = false
                  needsInit = true
               }
            }
         }
      }
   }

   public open fun onDisable() {
      active = false
      needsInit = true
   }

   @JvmStatic
   public fun handleTurn(yawDelta: Float, pitchDelta: Float): Boolean {
      if (INSTANCE.isEnabled() && active) {
         cameraYaw += yawDelta
         cameraPitch = RangesKt.coerceIn(cameraPitch + pitchDelta, -90.0F, 90.0F)
         return true
      } else {
         return false
      }
   }

   @JvmStatic
   public fun getCameraOverride(entityYaw: Float, entityPitch: Float): FloatArray? {
      if (!active) {
         needsInit = true
         return null
      } else {
         if (needsInit) {
            cameraYaw = entityYaw
            cameraPitch = entityPitch
            needsInit = false
         }

         return floatArrayOf(cameraYaw, cameraPitch)
      }
   }

   @JvmStatic
   public fun shouldOverridePerspective(): Boolean {
      return active && INSTANCE.isEnabled()
   }

   @JvmStatic
   fun {
      KeyEvents.INSTANCE.getKeyEvent().listen({ event: KeyEventData ->
         val bound: MCKey = INSTANCE.key
         if (bound.getType() === MCKeyType.KEYBOARD && bound.getCode() == event.getKey()) {
            INSTANCE.onFreelook(event.isClicked(), event.isReleased())
         }

         Unit.INSTANCE
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen({ event: MouseClickEventData ->
         val bound: MCKey = INSTANCE.key
         if (bound.getType() === MCKeyType.MOUSE && bound.getCode() == event.getButton()) {
            INSTANCE.onFreelook(event.getAction() == 1, event.getAction() == 0)
         }

         Unit.INSTANCE
      })
   }
}
