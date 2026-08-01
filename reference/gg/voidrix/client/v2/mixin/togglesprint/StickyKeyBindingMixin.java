package gg.voidrix.client.v2.mixin.togglesprint;

import com.mojang.blaze3d.platform.InputConstants.Type;
import gg.voidrix.client.v2.modules.togglesprint.ToggleSprintModule;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.KeyMapping.Category;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToggleKeyMapping.class)
public abstract class StickyKeyBindingMixin extends KeyMapping implements ToggleSprintModule.StickKeyBindingExt {
   @Unique
   private boolean voidrix$isVoidrixPressed;

   public StickyKeyBindingMixin(String id, Type type, int code, Category category) {
      super(id, type, code, category);
   }

   @Inject(method = "setDown", at = @At("HEAD"), cancellable = true)
   private void voidrix$handleStickyPress(boolean pressed, CallbackInfo ci) {
      ToggleSprintModule.handleStickyPress((ToggleKeyMapping)this, pressed, ci);
   }

   @Inject(method = "release", at = @At("HEAD"), cancellable = true)
   private void voidrix$preventStickyRelease(CallbackInfo ci) {
      ToggleSprintModule.handleStickyRelease((ToggleKeyMapping)this, ci);
   }

   @Override
   public boolean isVoidrixPressed() {
      return this.voidrix$isVoidrixPressed;
   }

   @Override
   public void setVoidrixPressed(boolean value) {
      this.voidrix$isVoidrixPressed = value;
   }
}
