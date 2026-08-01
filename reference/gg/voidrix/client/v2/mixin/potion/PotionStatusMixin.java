package gg.voidrix.client.v2.mixin.potion;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import gg.voidrix.client.v2.modules.potion.PotionStatus;
import java.util.Collection;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class PotionStatusMixin {
   @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
   private void voidrix$cancelVanillaRenderEffects(GuiGraphicsExtractor context, DeltaTracker deltaTracker, CallbackInfo ci) {
      if ((PotionStatus.INSTANCE.isEnabled() || PotionStatus.INSTANCE.getHideVanillaPotionStatus()) && PotionStatus.INSTANCE.getPotionX() == null) {
         ci.cancel();
      }
   }

   @WrapOperation(
      method = "extractEffects",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getActiveEffects()Ljava/util/Collection;")
   )
   private Collection<MobEffectInstance> voidrix$redirectPotionEffects(LocalPlayer instance, Operation<Collection<MobEffectInstance>> original) {
      Collection<MobEffectInstance> vanilla = (Collection<MobEffectInstance>)original.call(new Object[]{instance});
      return PotionStatus.INSTANCE.getPotionEffects(vanilla);
   }

   @WrapOperation(method = "extractEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;guiWidth()I"))
   private int voidrix$redirectPotionX(GuiGraphicsExtractor instance, Operation<Integer> original) {
      Double value = PotionStatus.INSTANCE.getPotionX();
      return value != null ? value.intValue() : (Integer)original.call(new Object[]{instance});
   }

   @ModifyVariable(method = "extractEffects", at = @At("STORE"), ordinal = 3)
   private int voidrix$redirectPotionY(int y) {
      Double value = PotionStatus.INSTANCE.getPotionY();
      return value != null ? value.intValue() : y;
   }

   @ModifyConstant(method = "extractEffects", constant = @Constant(intValue = 25))
   private int voidrix$modifyPotionXOffset(int constant) {
      Double value = PotionStatus.INSTANCE.getPotionX();
      return value != null ? PotionStatus.INSTANCE.getXOffset(constant, true) : constant;
   }

   @WrapWithCondition(
      method = "extractEffects",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
      )
   )
   private boolean voidrix$conditionalBackgroundSprite(
      GuiGraphicsExtractor instance, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height
   ) {
      return PotionStatus.INSTANCE.getPotionX() != null ? PotionStatus.INSTANCE.getDrawSprite() : true;
   }

   @WrapOperation(
      method = "extractEffects",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V"
      )
   )
   private void voidrix$addStatusEffectOverlay(
      GuiGraphicsExtractor instance,
      RenderPipeline pipeline,
      Identifier sprite,
      int x,
      int y,
      int width,
      int height,
      int color,
      Operation<Void> original,
      @Local MobEffectInstance mobEffectInstance,
      @Local(ordinal = 2) int effectX,
      @Local(ordinal = 3) int effectY
   ) {
      original.call(new Object[]{instance, pipeline, sprite, x, y, width, height, color});
      if (PotionStatus.INSTANCE.getShowTime() && PotionStatus.INSTANCE.getPotionX() != null) {
         PotionStatus.INSTANCE.drawStatusEffectOverlay(instance, mobEffectInstance, effectX, effectY);
      }
   }
}
