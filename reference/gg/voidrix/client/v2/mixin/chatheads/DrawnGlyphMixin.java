package gg.voidrix.client.v2.mixin.chatheads;

import gg.voidrix.client.v2.modules.chatheads.IDrawnGlyphExt;
import net.minecraft.client.gui.font.glyphs.BakedSheetGlyph.GlyphInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GlyphInstance.class)
public class DrawnGlyphMixin implements IDrawnGlyphExt {
   @Unique
   private Integer charCode;

   @Nullable
   @Override
   public Integer getVoidrix_charCode() {
      return this.charCode;
   }

   @Override
   public void setVoidrix_charCode(@Nullable Integer integer) {
      this.charCode = integer;
   }
}
