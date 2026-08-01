package gg.norisk.client.v2.mixin.itemmodel;

import gg.norisk.client.v2.modules.itemmodel.ItemModel;
import gg.norisk.client.v2.modules.itemmodel.NrcItemStackHolder;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin {
   @Inject(method = "updateForTopItem", at = @At("TAIL"))
   private void nrc$storeItemStack(
      ItemStackRenderState state, ItemStack itemStack, ItemDisplayContext ctx, @Nullable Level level, @Nullable ItemOwner itemOwner, int seed, CallbackInfo ci
   ) {
      ((NrcItemStackHolder)state).nrc$setItemStack(itemStack);
      if (ctx == ItemDisplayContext.GUI && ItemModel.hasNonDefaultGuiTransform(itemStack)) {
         state.setOversizedInGui(true);
      }
   }
}
