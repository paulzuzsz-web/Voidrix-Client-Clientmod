package gg.voidrix.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.voidrix.client.Voidrix;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Unterdrueckt das Kamera-Wackeln beim Schadennehmen. */
@Mixin(GameRenderer.class)
public abstract class NoHurtCamMixin {

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void voidrix$noHurtCam(CameraRenderState cameraState, PoseStack poseStack, CallbackInfo ci) {
        if (Voidrix.config().noHurtCam) {
            ci.cancel();
        }
    }
}
