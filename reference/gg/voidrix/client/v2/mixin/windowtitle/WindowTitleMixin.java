package gg.voidrix.client.v2.mixin.windowtitle;

import com.mojang.blaze3d.platform.Window;
import gg.voidrix.client.v2.modules.impl.WindowTitleModule;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class WindowTitleMixin {
   @Shadow
   @Final
   private long handle;

   @Inject(method = "setTitle", at = @At("HEAD"), cancellable = true)
   private void voidrix$overrideTitle(String title, CallbackInfo ci) {
      GLFW.glfwSetWindowTitle(this.handle, WindowTitleModule.getTitle());
      ci.cancel();
   }
}
