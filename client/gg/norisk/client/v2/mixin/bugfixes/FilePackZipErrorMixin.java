package gg.norisk.client.v2.mixin.bugfixes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.resourcepackorganizer.QuietPackMetaScope;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.server.packs.FilePackResources$SharedZipFileAccess")
public abstract class FilePackZipErrorMixin {
   @WrapOperation(
      method = "getOrCreateZipFile",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false)
   )
   private void nrc$silenceZipError(Logger logger, String msg, Object file, Object throwable, Operation<Void> original) {
      if (!QuietPackMetaScope.isActive()) {
         String detail = throwable instanceof Throwable t ? t.getClass().getSimpleName() + ": " + t.getMessage() : String.valueOf(throwable);
         logger.warn("[Voidrix] Skipping unreadable pack {} ({})", file, detail);
      }
   }
}
