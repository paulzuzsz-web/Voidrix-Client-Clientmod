package gg.voidrix.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.voidrix.client.Voidrix;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Hebt die Gamma-Vorgabe an, aus der die Lightmap gebaut wird. Der Wert 10.0 liegt weit
 * ueber dem Vanilla-Maximum und macht die Szene gleichmaessig hell ("Fullbright").
 */
@Mixin(LightmapRenderStateExtractor.class)
public abstract class FullBrightMixin {

    @WrapOperation(
            method = "extract",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 0))
    private float voidrix$fullBright(Double instance, Operation<Float> original) {
        if (Voidrix.config().fullBright) {
            return 10.0F;
        }
        return original.call(instance);
    }
}
