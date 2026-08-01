package gg.voidrix.client.v2.mixin.chatheads;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.voidrix.client.v2.modules.chatheads.ChatHeads_v1_21_6;
import net.minecraft.client.gui.font.glyphs.BakedSheetGlyph.GlyphInstance;
import net.minecraft.client.renderer.state.gui.GlyphRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.render.GuiRenderer$1")
public class GuiRenderer1Mixin {
   @WrapOperation(
      method = "acceptRenderable",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addGlyphToCurrentLayer(Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;)V"
      )
   )
   private void voidrix$applyStyleToRenderState(GuiRenderState instance, GuiElementRenderState state, Operation<Void> original) {
      if (state instanceof GlyphRenderState glyphState && glyphState.renderable() instanceof GlyphInstance glyph) {
         ChatHeads_v1_21_6.INSTANCE.renderHead_v1_21_6(instance, state, original, glyph, instance);
      } else {
         original.call(new Object[]{instance, state});
      }
   }
}
