package gg.norisk.client.v2.mixin.actionbar;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.actionbar.ActionBar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class ActionBarHudMixin {
   @Inject(method = "extractOverlayMessage", at = @At("HEAD"), cancellable = true)
   private void nrc$cancelOverlayMessage(GuiGraphicsExtractor ctx, DeltaTracker dt, CallbackInfo ci) {
      if (ActionBar.INSTANCE.isEnabled() && ActionBar.INSTANCE.getActionX() == null) {
         ci.cancel();
      }
   }

   @WrapOperation(
      method = "extractOverlayMessage",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;translate(FF)Lorg/joml/Matrix3x2f;", remap = false)
   )
   private Matrix3x2f nrc$redirectTranslate(Matrix3x2fStack stack, float x, float y, Operation<Matrix3x2f> original) {
      Double cx = ActionBar.INSTANCE.getActionX();
      Double cy = ActionBar.INSTANCE.getActionY();
      return cx != null && cy != null
         ? (Matrix3x2f)original.call(new Object[]{stack, cx.floatValue(), cy.floatValue()})
         : (Matrix3x2f)original.call(new Object[]{stack, x, y});
   }

   @ModifyExpressionValue(
      method = "extractOverlayMessage",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;overlayMessageString:Lnet/minecraft/network/chat/Component;")
   )
   private Component nrc$wrapOverlayMessage(Component original) {
      return ActionBar.wrapOverlayMessage((Hud)this, original);
   }

   @ModifyExpressionValue(method = "extractOverlayMessage", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;overlayMessageTime:I"))
   private int nrc$wrapOverlayTime(int original) {
      return ActionBar.wrapRemainTicks((Hud)this, original);
   }
}
