package gg.norisk.client.v2.mixin.chatheads;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.chatheads.ChatHeads;
import gg.norisk.client.v2.modules.chatheads.ChatHeads_v1_21_6;
import net.minecraft.client.gui.GuiGraphicsExtractor.RenderingTextCollector;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderingTextCollector.class)
public class DrawContextTextConsumerImplMixin {
   @WrapOperation(
      method = "accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/client/gui/ActiveTextCollector$Parameters;Lnet/minecraft/util/FormattedCharSequence;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addText(Lnet/minecraft/client/renderer/state/gui/GuiTextRenderState;)V"
      )
   )
   private void nrc$setCurrentSkinTextures(GuiRenderState instance, GuiTextRenderState textGuiElementRenderState, Operation<Void> source) {
      PlayerSkin currentSkinTextures = ChatHeads.INSTANCE.getCurrentSkinTextures();
      if (currentSkinTextures != null) {
         ((ChatHeads_v1_21_6.ITextGuiElementRenderStateExt)textGuiElementRenderState).setNrc_currentSkinTextures(currentSkinTextures);
      }

      ChatHeads_v1_21_6.INSTANCE.setTextGuiElementRenderState(textGuiElementRenderState);
      source.call(new Object[]{instance, textGuiElementRenderState});
   }
}
