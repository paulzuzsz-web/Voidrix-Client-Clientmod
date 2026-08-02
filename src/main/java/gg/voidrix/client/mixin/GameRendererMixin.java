package gg.voidrix.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.module.pvp.NoHurtCamModule;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * NoHurtCam: unterdrueckt das Kamera-Wackeln beim Schadennehmen.
 *
 * <p>{@code GameRenderer#bobHurt} kippt die Kamera nach einem Treffer. Steht
 * die Staerke im Modul auf 0, wird der Aufruf komplett uebersprungen.</p>
 *
 * <p>Reine Darstellungssache - Schaden, Knockback und alles Spielrelevante
 * bleiben voellig unberuehrt.</p>
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
	private void voidrix$cancelHurtCam(CameraRenderState cameraState, PoseStack poseStack, CallbackInfo info) {
		if (ModuleManager.get("no_hurt_cam") instanceof NoHurtCamModule module
				&& module.isEnabled()
				&& module.shouldCancel()) {
			info.cancel();
		}
	}
}
