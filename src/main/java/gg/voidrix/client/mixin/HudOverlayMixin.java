package gg.voidrix.client.mixin;

import gg.voidrix.client.feature.HudOverlay;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Haengt das Voidrix-HUD an das Ende des Vanilla-HUD-Renderstates. Bei ausgeblendetem HUD
 * (F1) oder offenem Debug-Screen (F3) wird nichts gezeichnet, damit sich nichts ueberlagert.
 */
@Mixin(Hud.class)
public abstract class HudOverlayMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void voidrix$renderHud(GuiGraphicsExtractor graphics, DeltaTracker delta, CallbackInfo ci) {
        Hud self = (Hud) (Object) this;
        if (self.isHidden() || Minecraft.getInstance().getDebugOverlay().showDebugScreen()) {
            return;
        }
        HudOverlay.render(graphics);
    }
}
