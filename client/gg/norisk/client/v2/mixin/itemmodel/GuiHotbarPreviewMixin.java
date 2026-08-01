package gg.norisk.client.v2.mixin.itemmodel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.itemmodel.ItemModel;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Hud.class, priority = 1100)
public abstract class GuiHotbarPreviewMixin {
   @WrapOperation(
      method = "extractItemHotbar",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Hud;extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"
      ),
      require = 0
   )
   private void nrc$previewItemInHotbar(
      Hud self, GuiGraphicsExtractor gui, int x, int y, DeltaTracker delta, Player player, ItemStack item, int seed, Operation<Void> original
   ) {
      ItemStack preview = ItemModel.getPreviewItemStack();
      original.call(new Object[]{self, gui, x, y, delta, player, preview != null ? preview : item, seed});
   }
}
