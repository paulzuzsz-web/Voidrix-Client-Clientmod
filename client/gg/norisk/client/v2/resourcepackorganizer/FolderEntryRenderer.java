package gg.norisk.client.v2.resourcepackorganizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class FolderEntryRenderer {
   public static void renderFolderIcon(GuiGraphicsExtractor g, int x, int y, int color, int border, boolean isBack, boolean hovered) {
      g.fill(x + 2, y + 4, x + 14, y + 8, border);
      g.fill(x + 1, y + 8, x + 31, y + 28, color);
      g.fill(x, y + 7, x + 32, y + 8, border);
      g.fill(x, y + 28, x + 32, y + 29, border);
      g.fill(x, y + 8, x + 1, y + 28, border);
      g.fill(x + 31, y + 8, x + 32, y + 28, border);
      if (isBack) {
         g.centeredText(Minecraft.getInstance().font, "←", x + 16, y + 13, -1);
      }

      if (hovered) {
         g.fill(x, y, x + 32, y + 32, 1090519039);
      }
   }
}
