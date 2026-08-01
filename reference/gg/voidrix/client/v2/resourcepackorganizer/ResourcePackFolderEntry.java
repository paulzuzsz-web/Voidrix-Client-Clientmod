package gg.voidrix.client.v2.resourcepackorganizer;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList.Entry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ResourcePackFolderEntry extends Entry {
   private static final int FOLDER_COLOR = -2842601;
   private static final int FOLDER_DARK_COLOR = -7640812;
   private static final int BACK_COLOR = -9803158;
   private static final int BACK_DARK_COLOR = -11908534;
   private final String folderName;
   private final boolean isBackEntry;
   private final Runnable onClick;
   private final String subtitle;

   public ResourcePackFolderEntry(TransferableSelectionList list, String folderName, boolean isBackEntry, Runnable onClick) {
      this(list, folderName, isBackEntry, onClick, null);
   }

   public ResourcePackFolderEntry(TransferableSelectionList list, String folderName, boolean isBackEntry, Runnable onClick, String subtitle) {
      Objects.requireNonNull(list);
      super(list);
      this.folderName = folderName;
      this.isBackEntry = isBackEntry;
      this.onClick = onClick;
      this.subtitle = subtitle != null ? subtitle : (isBackEntry ? "Back" : "Folder");
   }

   public void extractContent(GuiGraphicsExtractor g, int mouseX, int mouseY, boolean hovered, float partialTick) {
      int x = this.getContentX();
      int y = this.getContentY();
      Font font = Minecraft.getInstance().font;
      int ic = this.isBackEntry ? -9803158 : -2842601;
      int bc = this.isBackEntry ? -11908534 : -7640812;
      FolderEntryRenderer.renderFolderIcon(g, x, y, ic, bc, this.isBackEntry, hovered);
      g.text(font, this.isBackEntry ? ".." : this.folderName, x + 34, y + 1, -1);
      g.text(font, this.subtitle, x + 34, y + 12, -7829368);
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
      AbstractWidget.playButtonClickSound(Minecraft.getInstance().getSoundManager());
      this.onClick.run();
      return true;
   }

   public Component getNarration() {
      return Component.literal(this.isBackEntry ? "Back" : this.folderName);
   }

   public String getPackId() {
      return "folder/" + this.folderName;
   }
}
