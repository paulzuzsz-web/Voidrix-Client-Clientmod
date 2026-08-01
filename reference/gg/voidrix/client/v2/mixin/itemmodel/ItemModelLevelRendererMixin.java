package gg.voidrix.client.v2.mixin.itemmodel;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.voidrix.client.v2.modules.itemmodel.ItemModel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class ItemModelLevelRendererMixin {
   @Inject(method = "submitEntities", at = @At("TAIL"))
   private void voidrix$renderGroundPreviewItems(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
      Vec3 camPos = levelRenderState.cameraRenderState.pos;
      ItemModel.renderGroundPreviewItems(poseStack, submitNodeCollector, camPos.x(), camPos.y(), camPos.z());
   }
}
