package gg.voidrix.client.v2.mixin.resourcepackorganizer;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.voidrix.client.v2.modules.impl.ResourcePackOrganizerModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Screen.class)
public abstract class RpoModernScreenBackgroundMixin {
   @WrapWithCondition(
      method = "extractRenderStateWithTooltipAndSubtitles",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V")
   )
   private boolean voidrix$skipRpoBackground(Screen instance, GuiGraphicsExtractor gg, int mx, int my, float pt) {
      if (!(instance instanceof PackSelectionScreen)) {
         return true;
      } else {
         return !ResourcePackOrganizerModule.INSTANCE.isEnabled() ? true : !ResourcePackOrganizerModule.INSTANCE.getTransparentBackground();
      }
   }
}
