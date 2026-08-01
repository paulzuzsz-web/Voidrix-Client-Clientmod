package gg.voidrix.client.v2.mixin.clearbackground;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.voidrix.compat.event.ClearBackgroundEvent;
import gg.voidrix.compat.event.ClearBackgroundEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Screen.class)
public abstract class ScreenBackgroundMixin {
   @WrapWithCondition(
      require = 0,
      method = "extractTransparentBackground",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fillGradient(IIIIII)V")
   )
   private boolean voidrix$cancelScreenGradient(GuiGraphicsExtractor instance, int x1, int y1, int x2, int y2, int color1, int color2) {
      ClearBackgroundEvent event = new ClearBackgroundEvent(false);
      ClearBackgroundEvents.INSTANCE.getScreenBackgroundEvent().invoke(event);
      return !event.isCancelled();
   }
}
