package gg.norisk.client.v2.mixin.resourcepackorganizer;

import gg.norisk.client.v2.modules.impl.RecursivePackDiscovery;
import gg.norisk.client.v2.modules.impl.ResourcePackOrganizerModule;
import gg.norisk.client.v2.resourcepackorganizer.QuietPackMetaScope;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.FilePackResources.FileResourcesSupplier;
import net.minecraft.server.packs.PathPackResources.PathResourcesSupplier;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier;
import net.minecraft.world.level.validation.DirectoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FolderRepositorySource.class)
public abstract class RecursiveResourcePackMixin implements RepositorySource {
   @Unique
   private static final Logger NORISK_LOGGER = LoggerFactory.getLogger("RecursiveResourcePacks");
   @Shadow
   @Final
   private Path folder;
   @Shadow
   @Final
   private DirectoryValidator validator;
   @Shadow
   @Final
   private PackType packType;
   @Shadow
   @Final
   private PackSource packSource;

   @Inject(
      method = "loadPacks",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/server/packs/repository/FolderRepositorySource;discoverPacks(Ljava/nio/file/Path;Lnet/minecraft/world/level/validation/DirectoryValidator;Ljava/util/function/BiConsumer;)V",
         shift = Shift.AFTER
      )
   )
   private void norisk$injectRecursivePacks(Consumer<Pack> consumer, CallbackInfo ci) {
      if (ResourcePackOrganizerModule.INSTANCE.isEnabled()) {
         List<RecursivePackDiscovery.DiscoveredPack> nestedPacks = RecursivePackDiscovery.INSTANCE.discoverNestedPacks(this.folder, 10);
         QuietPackMetaScope.enter();

         try {
            for (RecursivePackDiscovery.DiscoveredPack discovered : nestedPacks) {
               Path packPath = discovered.getPath();
               String relativePath = this.folder.relativize(packPath).toString().replace('\\', '/');
               String id = "file/" + relativePath;
               PackLocationInfo locationInfo = new PackLocationInfo(id, Component.literal(relativePath), this.packSource, Optional.empty());
               ResourcesSupplier supplier;
               if (discovered.getType() == RecursivePackDiscovery.DiscoveredPackType.ZIP) {
                  supplier = new FileResourcesSupplier(packPath);
               } else {
                  supplier = new PathResourcesSupplier(packPath);
               }

               PackSelectionConfig selectionConfig = new PackSelectionConfig(false, Position.TOP, false);

               Pack pack;
               try {
                  pack = Pack.readMetaAndCreate(locationInfo, supplier, this.packType, selectionConfig);
               } catch (Exception e) {
                  NORISK_LOGGER.debug("Skipping nested pack with unreadable metadata: {}", id);
                  continue;
               }

               if (pack != null) {
                  NORISK_LOGGER.debug("Discovered nested pack: {}", id);
                  consumer.accept(pack);
               }
            }
         } finally {
            QuietPackMetaScope.exit();
         }
      }
   }
}
