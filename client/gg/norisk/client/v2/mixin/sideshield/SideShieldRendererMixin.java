package gg.norisk.client.v2.mixin.sideshield;

import com.llamalad7.mixinextras.sugar.Local;
import gg.norisk.client.v2.modules.sideshield.SideShieldModule;
import gg.norisk.client.v2.modules.sideshield.SideShieldTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState.LayerRenderState;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelWrapper.class)
public abstract class SideShieldRendererMixin {
   @Shadow
   @Final
   private SpecialModelRenderer<?> specialRenderer;

   @Inject(method = "update", at = @At("TAIL"))
   private void norisk$applySideShield(
      ItemStackRenderState renderState,
      ItemStack stack,
      ItemModelResolver resolver,
      ItemDisplayContext ctx,
      ClientLevel level,
      @Coerce Object owner,
      int seed,
      CallbackInfo ci,
      @Local LayerRenderState layerRenderState
   ) {
      if (SideShieldModule.INSTANCE.isEnabled()) {
         if (this.specialRenderer instanceof ShieldSpecialRenderer) {
            boolean blocking = norisk$isHolderBlocking(owner);
            ItemTransform custom = SideShieldTransforms.lookup(blocking, ctx);
            if (custom != null) {
               layerRenderState.setItemTransform(custom);
            }
         }
      }
   }

   @Unique
   private static boolean norisk$isHolderBlocking(Object owner) {
      LivingEntity entity;
      if (owner instanceof LivingEntity le) {
         entity = le;
      } else {
         entity = Minecraft.getInstance().player;
      }

      return entity == null ? false : entity.isUsingItem() && entity.getUseItem().is(Items.SHIELD);
   }
}
