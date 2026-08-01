package gg.voidrix.client.v2.mixin.itemhighlighter;

import gg.voidrix.client.v2.modules.itemhighlighter.ItemHighlighter;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class ItemHighlighterGuiMixin {
   @Inject(
      method = "extractSlot",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;III)V"
      )
   )
   private void voidrix$highlightHotbarBefore(
      GuiGraphicsExtractor guiGraphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack stack, int seed, CallbackInfo ci
   ) {
      ItemHighlighter.INSTANCE.highlightItemBefore(stack, x, y, guiGraphics);
   }
}
