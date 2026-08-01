package gg.norisk.client.v2.mixin.chatheads;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.chatheads.ChatHeads_v1_21_6;
import net.minecraft.client.gui.Font.PreparedText;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
   @WrapOperation(
      method = "lambda$prepareText$0",
      require = 0,
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiTextRenderState;ensurePrepared()Lnet/minecraft/client/gui/Font$PreparedText;"
      )
   )
   private PreparedText nrc$setTextGuiElementRenderState(GuiTextRenderState instance, Operation<PreparedText> original) {
      ChatHeads_v1_21_6.INSTANCE.setTextGuiElementRenderState(instance);
      return (PreparedText)original.call(new Object[]{instance});
   }
}
