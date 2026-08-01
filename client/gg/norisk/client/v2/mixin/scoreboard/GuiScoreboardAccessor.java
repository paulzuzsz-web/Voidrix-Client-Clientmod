package gg.norisk.client.v2.mixin.scoreboard;

import java.util.Comparator;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.scores.PlayerScoreEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Hud.class)
public interface GuiScoreboardAccessor {
   @Accessor("SCORE_DISPLAY_ORDER")
   static Comparator<PlayerScoreEntry> getScoreDisplayOrder() {
      throw new AssertionError();
   }
}
