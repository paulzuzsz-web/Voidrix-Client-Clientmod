package gg.voidrix.client.v2.mixin.itemmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.voidrix.client.v2.modules.itemmodel.ItemModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
   @Inject(method = "renderItem", at = @At("HEAD"))
   private void voidrix$pushItemModelTransform(
      LivingEntity entity, ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, SubmitNodeCollector snc, int light, CallbackInfo ci
   ) {
      poseStack.pushPose();
      if (!stack.isEmpty()) {
         ItemModel.transform(poseStack, ctx, stack, ctx.leftHand());
      }
   }

   @Inject(method = "renderItem", at = @At("RETURN"))
   private void voidrix$popItemModelTransform(
      LivingEntity entity, ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, SubmitNodeCollector snc, int light, CallbackInfo ci
   ) {
      poseStack.popPose();
   }
}
