package gg.norisk.client.v2.mixin.resourcepackorganizer;

import gg.norisk.client.v2.modules.impl.ResourcePackOrganizerModule;
import gg.norisk.client.v2.resourcepackorganizer.ResourcePackFolderEntry;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import kotlin.Pair;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.client.gui.screens.packs.PackSelectionModel.EntryBase;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList.Entry;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList.PackEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackSelectionScreen.class)
public abstract class PackSelectionScreenMixin {
   @Shadow
   private TransferableSelectionList availablePackList;
   @Shadow
   private EditBox search;
   @Shadow
   private Path packDir;

   @Shadow
   private void updateFilteredEntries(String string) {
   }

   @Inject(method = "init", at = @At("TAIL"))
   private void norisk$preScanFolders(CallbackInfo ci) {
      if (ResourcePackOrganizerModule.INSTANCE.isEnabled()) {
         ResourcePackOrganizerModule.resetFolderCache();
         ResourcePackOrganizerModule.getFilesystemFolders(this.packDir, "");
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void norisk$checkFilterDirty(CallbackInfo ci) {
      if (ResourcePackOrganizerModule.filterDirty) {
         ResourcePackOrganizerModule.filterDirty = false;
         this.norisk$refresh();
      }
   }

   @Inject(method = "updateFilteredEntries(Ljava/lang/String;Lnet/minecraft/client/gui/screens/packs/PackSelectionModel$EntryBase;)V", at = @At("TAIL"))
   private void norisk$applyFolderFilter(String searchText, EntryBase transferredEntry, CallbackInfo ci) {
      if (this.availablePackList != null && ResourcePackOrganizerModule.INSTANCE.isEnabled()) {
         try {
            boolean showIncompatible = ResourcePackOrganizerModule.INSTANCE.getShowIncompatible();
            SelectionListAccessor accessor = (SelectionListAccessor)this.availablePackList;
            List<Entry> children = accessor.norisk$getChildren();
            List<Entry> snapshot = new ArrayList<>(children);
            Entry header = snapshot.isEmpty() ? null : snapshot.get(0);
            String currentFolder = ResourcePackOrganizerModule.currentFolder;
            String folderPrefix = currentFolder.isEmpty() ? "" : currentFolder + "/";
            Set<String> subfolders = new TreeSet<>();
            List<Entry> filteredPacks = new ArrayList<>();

            for (int i = 1; i < snapshot.size(); i++) {
               Entry entry = snapshot.get(i);
               if (!showIncompatible && entry instanceof PackEntry) {
                  PackEntryAccessor packAccess = (PackEntryAccessor)entry;
                  if (!packAccess.norisk$getPack().getCompatibility().isCompatible()) {
                     continue;
                  }
               }

               String id = entry.getPackId();
               if (!id.startsWith("file/")) {
                  if (currentFolder.isEmpty()) {
                     filteredPacks.add(entry);
                  }
               } else {
                  String relativePath = id.substring("file/".length());
                  if (currentFolder.isEmpty() || relativePath.startsWith(folderPrefix)) {
                     String remaining = currentFolder.isEmpty() ? relativePath : relativePath.substring(folderPrefix.length());
                     int sep = remaining.indexOf(47);
                     if (sep == -1) {
                        filteredPacks.add(entry);
                     } else {
                        subfolders.add(remaining.substring(0, sep));
                     }
                  }
               }
            }

            subfolders.addAll(ResourcePackOrganizerModule.getFilesystemFolders(this.packDir, currentFolder));
            children.clear();
            if (header != null) {
               header.setHeight(13);
               children.add(header);
            }

            if (!currentFolder.isEmpty()) {
               ResourcePackFolderEntry back = new ResourcePackFolderEntry(this.availablePackList, "..", true, () -> {
                  ResourcePackOrganizerModule.navigateBack();
                  this.norisk$refresh();
               });
               back.setHeight(36);
               children.add(back);
            }

            List<String> allNestedPaths = new ArrayList<>();

            for (Entry e : snapshot) {
               String id = e.getPackId();
               if (id.startsWith("file/")) {
                  String rel = id.substring("file/".length());
                  if (rel.contains("/")) {
                     allNestedPaths.add(rel);
                  }
               }
            }

            for (String subfolder : subfolders) {
               if (ResourcePackOrganizerModule.matchesSearch(subfolder, searchText)) {
                  String target = ResourcePackOrganizerModule.getTargetFolder(subfolder);
                  Pair<Integer, Integer> counts = ResourcePackOrganizerModule.countFolderContents(this.packDir, target, allNestedPaths);
                  String subtitle = ResourcePackOrganizerModule.formatFolderSubtitle((Integer)counts.getFirst(), (Integer)counts.getSecond());
                  ResourcePackFolderEntry folder = new ResourcePackFolderEntry(this.availablePackList, subfolder, false, () -> {
                     ResourcePackOrganizerModule.navigateToFolder(target);
                     this.norisk$refresh();
                  }, subtitle);
                  folder.setHeight(36);
                  children.add(folder);
               }
            }

            children.addAll(filteredPacks);
            int rowLeft = accessor.norisk$getRowLeft();
            int rowWidth = this.availablePackList.getRowWidth();
            int y = accessor.norisk$getFirstEntryY() - (int)this.availablePackList.scrollAmount();

            for (Entry child : children) {
               child.setX(rowLeft);
               child.setWidth(rowWidth);
               child.setY(y);
               y += child.getHeight();
            }
         } catch (Exception e) {
            System.out.println("[RPO] Error in folder filter: " + e.getMessage());
            e.printStackTrace();
         }
      }
   }

   @Unique
   private void norisk$refresh() {
      this.updateFilteredEntries(this.search != null ? this.search.getValue() : "");
   }
}
