package gg.voidrix.client.v2.mixin.scoreboard;

import gg.voidrix.client.v2.modules.scoreboard.ScoreboardModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class ScoreboardHudMixin {
   @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void voidrix$onScoreboard(GuiGraphicsExtractor graphics, Objective objective, CallbackInfo ci) {
      ScoreboardModule.setLastObjective(objective);
      if (ScoreboardModule.INSTANCE.isEnabled()) {
         ci.cancel();
      }
   }
}
