package gg.voidrix.client.v2.mixin.fullscreen;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import gg.voidrix.client.v2.modules.impl.BorderlessFullscreenModule;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class BorderlessFullscreenMixin {
   @Shadow
   private boolean fullscreen;
   @Shadow
   @Final
   public long handle;
   @Shadow
   private int windowedX;
   @Shadow
   private int windowedY;
   @Shadow
   private int windowedWidth;
   @Shadow
   private int windowedHeight;
   @Shadow
   private int x;
   @Shadow
   private int y;
   @Shadow
   private int width;
   @Shadow
   private int height;
   @Unique
   private boolean voidrix$wasFullscreen = false;
   @Unique
   private boolean voidrix$wasMaximized = false;

   @Inject(method = "onMove", at = @At("TAIL"))
   private void voidrix$onMoveTail(long l, int i, int j, CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         if (!this.fullscreen && GLFW.glfwGetWindowAttrib(this.handle, 131080) != 1) {
            this.windowedX = this.x;
            this.windowedY = this.y;
         }
      }
   }

   @Inject(method = "onResize", at = @At("TAIL"))
   private void voidrix$onResizeTail(long l, int i, int j, CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         if (!this.fullscreen && GLFW.glfwGetWindowAttrib(this.handle, 131080) != 1) {
            this.windowedWidth = this.width;
            this.windowedHeight = this.height;
         }
      }
   }

   @Inject(method = "setMode", at = @At("HEAD"), cancellable = true)
   private void voidrix$onSetMode(CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         long handle = this.handle;
         if (this.fullscreen && !this.voidrix$wasFullscreen) {
            this.voidrix$wasMaximized = GLFW.glfwGetWindowAttrib(handle, 131080) == 1;
            if (this.voidrix$wasMaximized) {
               this.x = this.windowedX;
               this.y = this.windowedY;
               this.width = this.windowedWidth;
               this.height = this.windowedHeight;
            }
         }

         if (BorderlessFullscreenModule.INSTANCE.isEnabled()) {
            ci.cancel();
            this.voidrix$updateWindowState();
            if (!this.fullscreen && this.voidrix$wasMaximized) {
               GLFW.glfwMaximizeWindow(handle);
               this.voidrix$wasMaximized = false;
            }
         }

         this.voidrix$wasFullscreen = this.fullscreen;
      }
   }

   @Inject(method = "setMode", at = @At("TAIL"))
   private void voidrix$onSetModeTail(CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         if (!this.fullscreen && this.voidrix$wasMaximized) {
            GLFW.glfwMaximizeWindow(this.handle);
            this.voidrix$wasMaximized = false;
         }
      }
   }

   @Unique
   private void voidrix$updateWindowState() {
      Window window = (Window)this;
      long handle = this.handle;
      boolean wasExclusive = GLFW.glfwGetWindowMonitor(handle) != 0L;
      if (this.fullscreen) {
         if (wasExclusive) {
            GLFW.glfwSetWindowMonitor(handle, 0L, this.windowedX, this.windowedY, this.windowedWidth, this.windowedHeight, -1);
         } else if (!this.voidrix$wasFullscreen) {
            this.windowedX = this.x;
            this.windowedY = this.y;
            this.windowedWidth = this.width;
            this.windowedHeight = this.height;
         }

         Monitor monitor = window.findBestMonitor();
         if (monitor != null) {
            VideoMode videoMode = monitor.currentMode();
            GLFW.glfwSetWindowAttrib(handle, 131077, 0);
            GLFW.glfwSetWindowAttrib(handle, 131078, 0);
            this.x = monitor.x();
            this.y = monitor.y();
            this.width = videoMode.getWidth();
            this.height = videoMode.getHeight();
            GLFW.glfwSetWindowPos(handle, this.x, this.y);
            GLFW.glfwSetWindowSize(handle, this.width, this.height);
            BorderlessFullscreenModule.savedX = this.windowedX;
            BorderlessFullscreenModule.savedY = this.windowedY;
            BorderlessFullscreenModule.savedW = this.windowedWidth;
            BorderlessFullscreenModule.savedH = this.windowedHeight;
            BorderlessFullscreenModule.savedWasMaximized = this.voidrix$wasMaximized;
            BorderlessFullscreenModule.isBorderless = true;
         }
      } else {
         if (wasExclusive) {
            GLFW.glfwSetWindowMonitor(handle, 0L, this.windowedX, this.windowedY, this.windowedWidth, this.windowedHeight, -1);
         }

         GLFW.glfwSetWindowAttrib(handle, 131077, 1);
         GLFW.glfwSetWindowAttrib(handle, 131078, 1);
         GLFW.glfwRestoreWindow(handle);
         this.x = this.windowedX;
         this.y = this.windowedY;
         this.width = this.windowedWidth;
         this.height = this.windowedHeight;
         GLFW.glfwSetWindowPos(handle, this.x, this.y);
         GLFW.glfwSetWindowSize(handle, this.width, this.height);
         BorderlessFullscreenModule.isBorderless = false;
      }

      GLFW.glfwFocusWindow(handle);
   }
}
