package gg.voidrix.client.v2.modules.dropstack

import gg.voidrix.compat.event.KeyEventData
import gg.voidrix.compat.event.KeyEvents
import gg.voidrix.compat.event.MouseClickEventData
import gg.voidrix.compat.event.MouseEvents
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.resource.MCKeyType
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nDropStackModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 DropStackModule.kt\ngg/voidrix/client/v2/modules/dropstack/DropStackModule\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,64:1\n185#2:65\n40#2:66\n328#2:67\n40#2:68\n328#2:69\n40#2:70\n*S KotlinDebug\n*F\n+ 1 DropStackModule.kt\ngg/voidrix/client/v2/modules/dropstack/DropStackModule\n*L\n47#1:65\n47#1:66\n30#1:67\n30#1:68\n39#1:69\n39#1:70\n*E\n"])
public object DropStackModule : Module("Drop Stack", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   @Category(name = "Keybind")
   @NotNull
   public final val dropKey: MCKey by ValueApiKt.key$default(MCKey.Companion.getUNKNOWN(), null, null, 6, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return dropKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MCKey
      }


   @Category(name = "Settings")
   @NotNull
   public final var dropInInventory: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return dropInInventory$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         dropInInventory$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public open val seoTags: Array<String>

   public open fun createdAt(): Long {
      return 1778112000000L
   }

   private fun dropWholeStack() {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player != null) {
         val player: LocalPlayer = var10000.player
         if (!var10000.player.isSpectator()) {
            if (player.drop(true)) {
               player.swing(InteractionHand.MAIN_HAND)
            }
         }
      }
   }

   @JvmStatic
   fun {
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_0@{ event: KeyEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            val bound: MCKey = INSTANCE.dropKey
            if (bound.isUnknown()) {
               return@lambda_0 Unit.INSTANCE
            } else if (bound.getType() != MCKeyType.KEYBOARD || bound.getCode() != event.getKey()) {
               return@lambda_0 Unit.INSTANCE
            } else if (!event.isClicked()) {
               return@lambda_0 Unit.INSTANCE
            } else {
               val var10000: Minecraft = Minecraft.getInstance()
               if (var10000.gui.screen() != null) {
                  return@lambda_0 Unit.INSTANCE
               } else {
                  INSTANCE.dropWholeStack()
                  return@lambda_0 Unit.INSTANCE
               }
            }
         }
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_1@{ event: MouseClickEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            val bound: MCKey = INSTANCE.dropKey
            if (bound.isUnknown()) {
               return@lambda_1 Unit.INSTANCE
            } else if (bound.getType() != MCKeyType.MOUSE || bound.getCode() != event.getButton()) {
               return@lambda_1 Unit.INSTANCE
            } else if (event.getAction() != 1) {
               return@lambda_1 Unit.INSTANCE
            } else {
               val var10000: Minecraft = Minecraft.getInstance()
               if (var10000.gui.screen() != null) {
                  return@lambda_1 Unit.INSTANCE
               } else {
                  INSTANCE.dropWholeStack()
                  return@lambda_1 Unit.INSTANCE
               }
            }
         }
      })
   }
}
