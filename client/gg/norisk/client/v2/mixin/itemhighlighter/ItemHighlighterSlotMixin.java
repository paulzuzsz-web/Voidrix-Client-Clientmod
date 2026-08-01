package gg.norisk.client.v2.mixin.itemhighlighter;

import gg.norisk.client.v2.modules.itemhighlighter.ItemHighlighter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class ItemHighlighterSlotMixin {
   @Inject(
      method = "extractSlot",
      require = 0,
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;item(Lnet/minecraft/world/item/ItemStack;III)V")
   )
   private void nrc$highlightSlotBefore(GuiGraphicsExtractor guiGraphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
      ItemStack stack = slot.getItem();
      if (!stack.isEmpty()) {
         ItemHighlighter.INSTANCE.highlightItemBefore(stack, slot.x, slot.y, guiGraphics);
      }
   }

   @Inject(
      method = "extractFloatingItem",
      require = 0,
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;item(Lnet/minecraft/world/item/ItemStack;II)V")
   )
   private void nrc$highlightFloatingBefore(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
      if (!stack.isEmpty()) {
         ItemHighlighter.INSTANCE.highlightItemBefore(stack, x, y, guiGraphics);
      }
   }
}
