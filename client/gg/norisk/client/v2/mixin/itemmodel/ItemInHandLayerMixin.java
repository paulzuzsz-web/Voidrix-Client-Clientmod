package gg.norisk.client.v2.mixin.itemmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.norisk.client.v2.modules.itemmodel.ItemModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {
   @Inject(method = "submitArmWithItem", at = @At("HEAD"))
   private void nrc$pushThirdPersonTransform(
      ArmedEntityRenderState state,
      ItemStackRenderState renderState,
      ItemStack itemStack,
      HumanoidArm arm,
      PoseStack poseStack,
      SubmitNodeCollector snc,
      int light,
      CallbackInfo ci
   ) {
      poseStack.pushPose();
      if (!itemStack.isEmpty()) {
         ItemDisplayContext ctx = arm == HumanoidArm.LEFT ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
         ItemModel.transform(poseStack, ctx, itemStack, arm == HumanoidArm.LEFT);
      }
   }

   @Inject(method = "submitArmWithItem", at = @At("RETURN"))
   private void nrc$popThirdPersonTransform(
      ArmedEntityRenderState state,
      ItemStackRenderState renderState,
      ItemStack itemStack,
      HumanoidArm arm,
      PoseStack poseStack,
      SubmitNodeCollector snc,
      int light,
      CallbackInfo ci
   ) {
      poseStack.popPose();
   }
}
