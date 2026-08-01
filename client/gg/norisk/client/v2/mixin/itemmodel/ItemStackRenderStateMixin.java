package gg.norisk.client.v2.mixin.itemmodel;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.norisk.client.v2.modules.itemmodel.ItemModel;
import gg.norisk.client.v2.modules.itemmodel.NrcItemStackHolder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
public abstract class ItemStackRenderStateMixin implements NrcItemStackHolder {
   @Shadow
   ItemDisplayContext displayContext;
   @Unique
   private ItemStack nrc$itemStack = ItemStack.EMPTY;

   @Override
   public void nrc$setItemStack(ItemStack stack) {
      this.nrc$itemStack = stack;
   }

   @Override
   public ItemStack nrc$getItemStack() {
      return this.nrc$itemStack;
   }

   @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V", at = @At("HEAD"))
   private void nrc$applyTransformBeforeSubmit(PoseStack poseStack, SubmitNodeCollector snc, int light, int overlay, int outlineColor, CallbackInfo ci) {
      if (!this.nrc$itemStack.isEmpty()) {
         poseStack.pushPose();
         ItemModel.transformRenderState(poseStack, this.displayContext, this.nrc$itemStack);
      }
   }

   @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V", at = @At("TAIL"))
   private void nrc$popTransformAfterSubmit(PoseStack poseStack, SubmitNodeCollector snc, int light, int overlay, int outlineColor, CallbackInfo ci) {
      if (!this.nrc$itemStack.isEmpty()) {
         poseStack.popPose();
      }
   }

   @ModifyReturnValue(method = "getModelBoundingBox", at = @At("RETURN"))
   private AABB nrc$inflateGuiBoundingBox(AABB original) {
      if (this.displayContext == ItemDisplayContext.GUI && !this.nrc$itemStack.isEmpty()) {
         float[] factors = ItemModel.getGuiTransformFactors(this.nrc$itemStack);
         if (factors != null) {
            float sx = factors[0];
            float sy = factors[1];
            float sz = factors[2];
            float tx = factors[3];
            float ty = factors[4];
            float tz = factors[5];
            return original.inflate(sx, sy, sz).move(tx, ty, tz);
         }
      }

      return original;
   }

   @Inject(method = "clear", at = @At("HEAD"))
   private void nrc$clearItemStack(CallbackInfo ci) {
      this.nrc$itemStack = ItemStack.EMPTY;
   }
}
