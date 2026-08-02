package gg.voidrix.client.mixin;

import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.module.pvp.ZoomModule;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Zoom-Modul: veraendert das Sichtfeld der Kamera.
 *
 * <p>{@code Camera#getFov} ist in 26.x die zentrale Stelle, an der der
 * Sichtwinkel fuer die Projektionsmatrix abgefragt wird. Ein Faktor darauf
 * wirkt daher wie eine kurzfristige FOV-Aenderung - genau das, was Zoom
 * ausmacht.</p>
 *
 * <p>Rein clientseitig: der Server erfaehrt vom Sichtfeld ohnehin nichts.</p>
 */
@Mixin(Camera.class)
public class CameraMixin {

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	private void voidrix$applyZoom(CallbackInfoReturnable<Float> info) {
		if (!(ModuleManager.get("zoom") instanceof ZoomModule zoom) || !zoom.isEnabled()) {
			return;
		}

		double multiplier = zoom.getFovMultiplier();

		if (multiplier == 1.0) {
			return;
		}

		info.setReturnValue((float) (info.getReturnValue() * multiplier));
	}
}
