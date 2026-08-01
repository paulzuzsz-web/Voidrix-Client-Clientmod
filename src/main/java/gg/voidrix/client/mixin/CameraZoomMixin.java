package gg.voidrix.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.voidrix.client.feature.Zoom;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Skaliert das von Vanilla berechnete Sichtfeld. {@code calculateHudFov} wird mitgenommen,
 * damit die Hand nicht ausserhalb des gezoomten Bildes haengt.
 */
@Mixin(Camera.class)
public abstract class CameraZoomMixin {

    @ModifyReturnValue(method = "calculateFov", at = @At("RETURN"))
    private float voidrix$zoomFov(float original) {
        return Zoom.modifyFov(original);
    }

    @ModifyReturnValue(method = "calculateHudFov", at = @At("RETURN"))
    private float voidrix$zoomHudFov(float original) {
        return Zoom.modifyFov(original);
    }
}
