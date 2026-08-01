package gg.voidrix.client.v2.mixin.resourcepackorganizer;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.voidrix.client.v2.resourcepackorganizer.PackLogReroute;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FolderRepositorySource.class)
public abstract class FolderRepositoryQuietMixin {
   @WrapWithCondition(
      method = "discoverPacks",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
      require = 0
   )
   private static boolean voidrix$dropInfo1(Logger logger, String format, Object a) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug(format, a);
         return false;
      } else {
         return true;
      }
   }
}
