package gg.norisk.client.v2.mixin.titles;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Hud.class)
public interface TitleHudAccessor {
   @Invoker("extractTitle")
   void invokeRenderTitle(GuiGraphicsExtractor var1, DeltaTracker var2);

   @Accessor("title")
   @Nullable
   Component getTitle();

   @Accessor("subtitle")
   @Nullable
   Component getSubtitle();

   @Accessor("titleTime")
   int getTitleTime();
}
