package gg.voidrix.client.v2.mixin.packtweaks;

import gg.voidrix.client.v2.modules.packtweaks.PackTweaks;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class VignetteMixin {
   @Inject(method = "extractVignette", at = @At("HEAD"), cancellable = true)
   private void voidrix$noVignette(GuiGraphicsExtractor graphics, Entity camera, CallbackInfo ci) {
      if (PackTweaks.INSTANCE.isEnabled() && PackTweaks.INSTANCE.getNoVignette()) {
         ci.cancel();
      }
   }
}
