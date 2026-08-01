package gg.voidrix.client.v2.mixin.dropstack;

import gg.voidrix.client.v2.modules.dropstack.DropStackModule;
import gg.voidrix.compat.resource.MCKey;
import gg.voidrix.compat.resource.MCKeyType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class DropStackInventoryMixin {
   @Shadow
   @Nullable
   protected Slot hoveredSlot;

   @Shadow
   protected abstract void slotClicked(Slot var1, int var2, int var3, ContainerInput var4);

   @Inject(method = "keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z", at = @At("HEAD"), cancellable = true)
   private void voidrix$dropStackInInventory(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
      if (DropStackModule.INSTANCE.isEnabled()) {
         if (DropStackModule.INSTANCE.getDropInInventory()) {
            MCKey bound = DropStackModule.INSTANCE.getDropKey();
            if (!bound.isUnknown() && bound.getType() == MCKeyType.KEYBOARD && bound.getCode() == event.key()) {
               Slot slot = this.hoveredSlot;
               if (slot != null && slot.hasItem()) {
                  this.slotClicked(slot, slot.index, 1, ContainerInput.THROW);
                  cir.setReturnValue(true);
               }
            }
         }
      }
   }
}
