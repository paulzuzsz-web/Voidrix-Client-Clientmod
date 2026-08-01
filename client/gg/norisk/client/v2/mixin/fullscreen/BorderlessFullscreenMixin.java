package gg.norisk.client.v2.mixin.fullscreen;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import gg.norisk.client.v2.modules.impl.BorderlessFullscreenModule;
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
   private boolean nrc$wasFullscreen = false;
   @Unique
   private boolean nrc$wasMaximized = false;

   @Inject(method = "onMove", at = @At("TAIL"))
   private void nrc$onMoveTail(long l, int i, int j, CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         if (!this.fullscreen && GLFW.glfwGetWindowAttrib(this.handle, 131080) != 1) {
            this.windowedX = this.x;
            this.windowedY = this.y;
         }
      }
   }

   @Inject(method = "onResize", at = @At("TAIL"))
   private void nrc$onResizeTail(long l, int i, int j, CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         if (!this.fullscreen && GLFW.glfwGetWindowAttrib(this.handle, 131080) != 1) {
            this.windowedWidth = this.width;
            this.windowedHeight = this.height;
         }
      }
   }

   @Inject(method = "setMode", at = @At("HEAD"), cancellable = true)
   private void nrc$onSetMode(CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         long handle = this.handle;
         if (this.fullscreen && !this.nrc$wasFullscreen) {
            this.nrc$wasMaximized = GLFW.glfwGetWindowAttrib(handle, 131080) == 1;
            if (this.nrc$wasMaximized) {
               this.x = this.windowedX;
               this.y = this.windowedY;
               this.width = this.windowedWidth;
               this.height = this.windowedHeight;
            }
         }

         if (BorderlessFullscreenModule.INSTANCE.isEnabled()) {
            ci.cancel();
            this.nrc$updateWindowState();
            if (!this.fullscreen && this.nrc$wasMaximized) {
               GLFW.glfwMaximizeWindow(handle);
               this.nrc$wasMaximized = false;
            }
         }

         this.nrc$wasFullscreen = this.fullscreen;
      }
   }

   @Inject(method = "setMode", at = @At("TAIL"))
   private void nrc$onSetModeTail(CallbackInfo ci) {
      if (!BorderlessFullscreenModule.isCompetingModLoaded) {
         if (!this.fullscreen && this.nrc$wasMaximized) {
            GLFW.glfwMaximizeWindow(this.handle);
            this.nrc$wasMaximized = false;
         }
      }
   }

   @Unique
   private void nrc$updateWindowState() {
      Window window = (Window)this;
      long handle = this.handle;
      boolean wasExclusive = GLFW.glfwGetWindowMonitor(handle) != 0L;
      if (this.fullscreen) {
         if (wasExclusive) {
            GLFW.glfwSetWindowMonitor(handle, 0L, this.windowedX, this.windowedY, this.windowedWidth, this.windowedHeight, -1);
         } else if (!this.nrc$wasFullscreen) {
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
            BorderlessFullscreenModule.savedWasMaximized = this.nrc$wasMaximized;
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
