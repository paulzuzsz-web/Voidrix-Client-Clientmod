package gg.voidrix.client.v2.mixin.potion;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Hud.class)
public interface InGameHudAccessor {
   @Invoker("extractEffects")
   void invokeRenderEffects(GuiGraphicsExtractor var1, DeltaTracker var2);
}
