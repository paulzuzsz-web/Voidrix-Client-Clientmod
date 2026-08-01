package gg.voidrix.client.v2.mixin.resourcepackorganizer;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.voidrix.client.v2.resourcepackorganizer.PackLogReroute;
import net.minecraft.server.packs.repository.Pack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Pack.class)
public abstract class QuietPackRepoMixin {
   @WrapWithCondition(
      method = "readPackMetadata",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
      require = 0
   )
   private static boolean voidrix$dropWarn1(Logger logger, String format, Object a) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug(format, a);
         return false;
      } else {
         return true;
      }
   }

   @WrapWithCondition(
      method = "readPackMetadata",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
      require = 0
   )
   private static boolean voidrix$dropWarn2(Logger logger, String format, Object a, Object b) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug(format, a, b);
         return false;
      } else {
         return true;
      }
   }

   @WrapWithCondition(
      method = "readPackMetadata",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false),
      require = 0
   )
   private static boolean voidrix$dropWarnVar(Logger logger, String format, Object[] args) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug(format, args);
         return false;
      } else {
         return true;
      }
   }
}
