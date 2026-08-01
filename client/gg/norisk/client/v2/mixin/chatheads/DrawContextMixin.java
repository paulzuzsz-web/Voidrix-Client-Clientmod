package gg.norisk.client.v2.mixin.chatheads;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.chatheads.ChatHeads;
import gg.norisk.client.v2.modules.chatheads.ChatHeads_v1_21_6;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphicsExtractor.class)
public abstract class DrawContextMixin {
   @WrapOperation(
      method = "text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addText(Lnet/minecraft/client/renderer/state/gui/GuiTextRenderState;)V"
      )
   )
   private void nrc$applyStyleToRenderState(GuiRenderState instance, GuiTextRenderState guiTextRenderState, Operation<Void> original) {
      ((ChatHeads_v1_21_6.ITextGuiElementRenderStateExt)guiTextRenderState).setNrc_currentSkinTextures(ChatHeads.INSTANCE.getCurrentSkinTextures());
      original.call(new Object[]{instance, guiTextRenderState});
   }
}
