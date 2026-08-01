package gg.norisk.client.v2.mixin.itemhighlighter;

import gg.norisk.client.v2.modules.itemhighlighter.ItemHighlighter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class ItemHighlighterRenderItemMixin {
   @Inject(method = "item(Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"))
   private void nrc$highlightBefore(ItemStack stack, int x, int y, CallbackInfo ci) {
      if (!stack.isEmpty()) {
         ItemHighlighter.INSTANCE.highlightItemBefore(stack, x, y, (GuiGraphicsExtractor)this);
      }
   }

   @Inject(method = "item(Lnet/minecraft/world/item/ItemStack;II)V", at = @At("TAIL"))
   private void nrc$highlightCleanup(ItemStack stack, int x, int y, CallbackInfo ci) {
      ItemHighlighter.INSTANCE.highlightItemEndRender(x, y);
   }
}
