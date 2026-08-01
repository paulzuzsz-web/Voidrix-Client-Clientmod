package gg.voidrix.client.v2.mixin.fov;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import gg.voidrix.compat.event.FovEvents;
import gg.voidrix.compat.event.FovModifyEvent;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class GameRendererFovMixin {
   @ModifyExpressionValue(method = "calculateFov", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;intValue()I"))
   private int voidrix$defaultFovEvent(int original) {
      FovModifyEvent event = new FovModifyEvent(original);
      FovEvents.INSTANCE.getDefaultFovEvent().invoke(event);
      return (int)event.getFov();
   }
}
