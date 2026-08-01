package gg.voidrix.client.v2.mixin.itemmodel;

import gg.voidrix.client.v2.modules.itemmodel.ItemModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public abstract class ArmedEntityRenderStateMixin {
   @Inject(method = "extractArmedEntityRenderState", at = @At("TAIL"))
   private static void voidrix$previewItemThirdPerson(
      LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver resolver, float partialTick, CallbackInfo ci
   ) {
      if (entity == Minecraft.getInstance().player) {
         ItemStack preview = ItemModel.getPreviewItemStack();
         if (preview != null) {
            resolver.updateForLiving(state.rightHandItemState, preview, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
            state.rightHandItemStack = preview.copy();
            resolver.updateForLiving(state.leftHandItemState, preview, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
            state.leftHandItemStack = preview.copy();
         }
      }
   }
}
