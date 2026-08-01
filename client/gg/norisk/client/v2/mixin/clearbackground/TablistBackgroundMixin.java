package gg.norisk.client.v2.mixin.clearbackground;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.norisk.compat.event.ClearBackgroundEvent;
import gg.norisk.compat.event.ClearBackgroundEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PlayerTabOverlay.class, priority = 999)
public abstract class TablistBackgroundMixin {
   @WrapWithCondition(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
   private boolean nrc$cancelTablistFill(GuiGraphicsExtractor instance, int x1, int y1, int x2, int y2, int color) {
      ClearBackgroundEvent event = new ClearBackgroundEvent(false);
      ClearBackgroundEvents.INSTANCE.getTablistBackgroundEvent().invoke(event);
      return !event.isCancelled();
   }
}
