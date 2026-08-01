package gg.norisk.client.v2.mixin.itemmodel;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.norisk.client.v2.modules.itemmodel.ItemModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ItemInHandRenderer.class, priority = 1100)
public abstract class ItemModelPreviewMixin {
   @WrapOperation(
      method = "submitHandsWithItems",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
      ),
      require = 0
   )
   private void nrc$previewItemInHand(
      ItemInHandRenderer self,
      AbstractClientPlayer player,
      float f1,
      float f2,
      InteractionHand hand,
      float f3,
      ItemStack item,
      float f4,
      PoseStack pose,
      SubmitNodeCollector collector,
      int packedLight,
      Operation<Void> original
   ) {
      ItemStack preview = ItemModel.getPreviewItemStack();
      original.call(new Object[]{self, player, f1, f2, hand, f3, preview != null ? preview : item, f4, pose, collector, packedLight});
   }
}
