package gg.voidrix.client.v2.mixin.resourcepackorganizer;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.DataResult;
import gg.voidrix.client.v2.resourcepackorganizer.PackLogReroute;
import java.util.function.Consumer;
import net.minecraft.server.packs.AbstractPackResources;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractPackResources.class)
public abstract class QuietPackMetadataMixin {
   @WrapWithCondition(
      method = "getMetadataFromStream",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false),
      require = 0
   )
   private static boolean voidrix$dropError2(Logger logger, String format, Object a, Object b) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug(format, a, b);
         return false;
      } else {
         return true;
      }
   }

   @WrapWithCondition(
      method = "getMetadataFromStream",
      at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;[Ljava/lang/Object;)V", remap = false),
      require = 0
   )
   private static boolean voidrix$dropErrorVar(Logger logger, String format, Object[] args) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug(format, args);
         return false;
      } else {
         return true;
      }
   }

   @WrapOperation(
      method = "getMetadataFromStream",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/serialization/DataResult;ifError(Ljava/util/function/Consumer;)Lcom/mojang/serialization/DataResult;",
         remap = false
      ),
      require = 0
   )
   private static DataResult<?> voidrix$skipIfError(DataResult<?> result, Consumer<?> consumer, Operation<DataResult<?>> original) {
      if (PackLogReroute.shouldDrop()) {
         PackLogReroute.debug("Couldn't load pack metadata: codec error");
         return result;
      } else {
         return (DataResult<?>)original.call(new Object[]{result, consumer});
      }
   }
}
