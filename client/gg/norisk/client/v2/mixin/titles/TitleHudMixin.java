package gg.norisk.client.v2.mixin.titles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.titles.Titles;
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
public abstract class TitleHudMixin {
   @Inject(method = "extractTitle", at = @At("HEAD"), cancellable = true)
   private void nrc$cancelTitle(GuiGraphicsExtractor ctx, DeltaTracker dt, CallbackInfo ci) {
      if (Titles.INSTANCE.isEnabled() && Titles.INSTANCE.getTitlesX() == null) {
         ci.cancel();
      }
   }

   @WrapOperation(method = "extractTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;translate(FF)Lorg/joml/Matrix3x2f;", remap = false))
   private Matrix3x2f nrc$redirectTranslate(Matrix3x2fStack stack, float x, float y, Operation<Matrix3x2f> original) {
      Double cx = Titles.INSTANCE.getTitlesX();
      Double cy = Titles.INSTANCE.getTitlesY();
      return cx != null && cy != null
         ? (Matrix3x2f)original.call(new Object[]{stack, cx.floatValue(), cy.floatValue()})
         : (Matrix3x2f)original.call(new Object[]{stack, x, y});
   }

   @ModifyExpressionValue(
      method = "extractTitle",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;title:Lnet/minecraft/network/chat/Component;")
   )
   private Component nrc$wrapTitle(Component original) {
      return Titles.wrapTitle((Hud)this, original);
   }

   @ModifyExpressionValue(
      method = "extractTitle",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;subtitle:Lnet/minecraft/network/chat/Component;")
   )
   private Component nrc$wrapSubtitle(Component original) {
      return Titles.wrapSubtitle((Hud)this, original);
   }

   @ModifyExpressionValue(method = "extractTitle", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;titleTime:I"))
   private int nrc$wrapTitleTime(int original) {
      return Titles.wrapRemainTicks((Hud)this, original);
   }
}
