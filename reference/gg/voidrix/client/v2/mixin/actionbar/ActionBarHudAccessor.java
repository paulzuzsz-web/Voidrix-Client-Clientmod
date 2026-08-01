package gg.voidrix.client.v2.mixin.actionbar;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Hud.class)
public interface ActionBarHudAccessor {
   @Invoker("extractOverlayMessage")
   void invokeRenderOverlayMessage(GuiGraphicsExtractor var1, DeltaTracker var2);

   @Accessor("overlayMessageString")
   @Nullable
   Component getOverlayMessage();

   @Accessor("overlayMessageTime")
   int getOverlayRemaining();

   @Accessor("animateOverlayMessageColor")
   boolean getAnimateOverlayMessageColor();
}
