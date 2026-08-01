package gg.norisk.client.v2.mixin.blockoutline;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.norisk.client.v2.modules.blockoutline.BlockOutline;
import gg.norisk.client.v2.modules.blockoutline.BlockOverlayRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class BlockOutlineMixin {
   @WrapOperation(
      method = "submitHitOutline",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitShapeOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/client/renderer/rendertype/RenderType;IFZ)V"
      )
   )
   private void nrc$wrapOutline(
      SubmitNodeCollector collector,
      PoseStack poseStack,
      VoxelShape shape,
      RenderType renderType,
      int color,
      float width,
      boolean afterTerrain,
      Operation<Void> original
   ) {
      boolean on = BlockOutline.INSTANCE.isEnabled();
      BlockOverlayRenderer.submitFill(collector, poseStack, shape);
      if (!on || BlockOutline.INSTANCE.getOutlineEnabled()) {
         if (on && BlockOutline.INSTANCE.getOutlineEnabled()) {
            original.call(
               new Object[]{collector, poseStack, shape, renderType, BlockOutline.getComputedColorInt(), BlockOutline.getLineWidthFloat(), afterTerrain}
            );
         } else {
            original.call(new Object[]{collector, poseStack, shape, renderType, color, width, afterTerrain});
         }
      }
   }
}
