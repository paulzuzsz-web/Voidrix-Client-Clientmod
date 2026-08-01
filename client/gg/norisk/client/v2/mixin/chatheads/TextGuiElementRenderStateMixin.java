package gg.norisk.client.v2.mixin.chatheads;

import gg.norisk.client.v2.modules.chatheads.ChatHeads_v1_21_6;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiTextRenderState.class)
public class TextGuiElementRenderStateMixin implements ChatHeads_v1_21_6.ITextGuiElementRenderStateExt {
   @Unique
   private PlayerSkin skintextures;

   @Nullable
   @Override
   public PlayerSkin getNrc_currentSkinTextures() {
      return this.skintextures;
   }

   @Override
   public void setNrc_currentSkinTextures(@Nullable PlayerSkin skinTextures) {
      this.skintextures = skinTextures;
   }
}
