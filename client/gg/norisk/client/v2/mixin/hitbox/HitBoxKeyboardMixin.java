package gg.norisk.client.v2.mixin.hitbox;

import gg.norisk.client.v2.modules.hitbox.HitBox;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class HitBoxKeyboardMixin {
   @Shadow
   protected abstract void debugFeedbackTranslated(String var1);

   @Inject(
      method = "handleDebugKeys",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/debug/DebugScreenEntries;ENTITY_HITBOXES:Lnet/minecraft/resources/Identifier;"),
      cancellable = true
   )
   private void nrc$onF3B(KeyEvent keyInput, CallbackInfoReturnable<Boolean> cir) {
      HitBox.INSTANCE.setEnabled(!HitBox.INSTANCE.isEnabled());
      this.debugFeedbackTranslated(HitBox.INSTANCE.isEnabled() ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
      cir.setReturnValue(true);
   }
}
