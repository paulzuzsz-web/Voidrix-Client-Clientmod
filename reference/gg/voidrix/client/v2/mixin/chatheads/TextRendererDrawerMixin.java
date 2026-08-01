package gg.voidrix.client.v2.mixin.chatheads;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.voidrix.client.v2.modules.chatheads.IDrawnGlyphExt;
import net.minecraft.client.gui.Font.PreparedTextBuilder;
import net.minecraft.client.gui.font.TextRenderable.Styled;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PreparedTextBuilder.class)
public abstract class TextRendererDrawerMixin {
   @Unique
   private Integer codePoint;

   @Inject(method = "accept(ILnet/minecraft/network/chat/Style;I)Z", at = @At("HEAD"))
   private void voidrix$addCodePointHead(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir) {
      this.codePoint = j;
   }

   @WrapOperation(
      method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Font$PreparedTextBuilder;addGlyph(Lnet/minecraft/client/gui/font/TextRenderable$Styled;)V"
      )
   )
   private void voidrix$addCodePoint(PreparedTextBuilder instance, Styled glyph, Operation<Void> original, int index, Style style, BakedGlyph bakedGlyph) {
      if (glyph instanceof IDrawnGlyphExt ext && this.codePoint != null) {
         ext.setVoidrix_charCode(this.codePoint);
      }

      this.codePoint = null;
      original.call(new Object[]{instance, glyph});
   }
}
