package gg.voidrix.client.v2.mixin.branding;

import gg.voidrix.client.v2.modules.impl.BrandingOverlay;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.PauseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class BrandingPauseScreenMixin {
   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void voidrix$renderBranding(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
      PauseScreen screen = (PauseScreen)this;
      BrandingOverlay.handleScreenRendering(screen.width, screen.height, graphics, mouseX, mouseY);
   }
}
