package gg.voidrix.client.v2.mixin.itemhighlighter;

import gg.voidrix.client.v2.modules.itemhighlighter.ItemHighlighter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class ItemHighlighterDecorationsMixin {
   @Inject(
      method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;popMatrix()Lorg/joml/Matrix3x2fStack;", remap = false)
   )
   private void voidrix$highlightItem(Font font, ItemStack stack, int x, int y, String string, CallbackInfo ci) {
      ItemHighlighter.INSTANCE.highlightItemAfter(stack, x, y, (GuiGraphicsExtractor)this);
   }
}
