package gg.voidrix.client.v2.mixin.noadvancement;

import gg.voidrix.client.v2.modules.impl.NoAdvancementModule;
import java.util.Deque;
import java.util.List;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.NowPlayingToast;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.components.toasts.ToastManager.ToastInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public abstract class NoAdvancementMixin {
   @Shadow
   @Final
   private Deque<Toast> queued;
   @Shadow
   @Final
   private List<ToastInstance<?>> visibleToasts;

   @Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
   private void voidrix$blockToast(Toast toast, CallbackInfo ci) {
      if (this.voidrix$shouldBlock(toast)) {
         ci.cancel();
      }
   }

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void voidrix$removeBlockedToasts(CallbackInfo ci) {
      if (NoAdvancementModule.isActive()) {
         this.queued.removeIf(this::voidrix$shouldBlock);
         this.visibleToasts.removeIf(instance -> this.voidrix$shouldBlock(instance.getToast()));
      }
   }

   private boolean voidrix$shouldBlock(Toast toast) {
      if (toast instanceof AdvancementToast && NoAdvancementModule.shouldBlockAdvancements()) {
         return true;
      } else if (toast instanceof RecipeToast && NoAdvancementModule.shouldBlockRecipes()) {
         return true;
      } else if (toast instanceof SystemToast && NoAdvancementModule.shouldBlockSystem()) {
         return true;
      } else {
         return toast instanceof TutorialToast && NoAdvancementModule.shouldBlockTutorial()
            ? true
            : toast instanceof NowPlayingToast && NoAdvancementModule.shouldBlockNowPlaying();
      }
   }
}
