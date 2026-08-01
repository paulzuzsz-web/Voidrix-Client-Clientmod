package gg.norisk.client.v2.mixin.branding;

import gg.norisk.client.v2.modules.impl.BrandingOverlay;
import gg.norisk.client.v2.modules.impl.IconModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class BrandingContainerScreenMixin {
   @Shadow
   protected int leftPos;
   @Shadow
   protected int topPos;
   @Shadow
   protected int imageWidth;

   @Inject(method = "extractContents", at = @At("RETURN"))
   private void nrc$renderBranding(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
      AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)this;
      if (IconModule.INSTANCE.getTiktokMode()) {
         BrandingOverlay.handleAboveInventoryRendering(
            graphics, mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, screen instanceof CreativeModeInventoryScreen
         );
      } else {
         BrandingOverlay.handleScreenRendering(screen.width, screen.height, graphics, mouseX, mouseY);
      }
   }
}
