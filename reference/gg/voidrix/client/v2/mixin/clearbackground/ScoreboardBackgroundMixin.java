package gg.voidrix.client.v2.mixin.clearbackground;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.voidrix.compat.event.ClearBackgroundEvent;
import gg.voidrix.compat.event.ClearBackgroundEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class ScoreboardBackgroundMixin {
   @WrapWithCondition(method = "displayScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
   private boolean voidrix$cancelScoreboardFill(GuiGraphicsExtractor instance, int x1, int y1, int x2, int y2, int color) {
      ClearBackgroundEvent event = new ClearBackgroundEvent(false);
      ClearBackgroundEvents.INSTANCE.getScoreboardBackgroundEvent().invoke(event);
      return !event.isCancelled();
   }
}
