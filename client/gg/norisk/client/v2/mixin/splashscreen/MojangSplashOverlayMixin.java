package gg.norisk.client.v2.mixin.splashscreen;

import java.util.function.IntSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class MojangSplashOverlayMixin {
   @Mutable
   @Shadow
   @Final
   private static IntSupplier BRAND_BACKGROUND;
   @Shadow
   @Final
   private static int LOGO_BACKGROUND_COLOR_DARK;

   @Inject(method = "registerTextures", at = @At("HEAD"))
   private static void nrc$darkSplash(TextureManager textureManager, CallbackInfo ci) {
      BRAND_BACKGROUND = () -> Minecraft.getInstance().options.darkMojangStudiosBackground().get() ? LOGO_BACKGROUND_COLOR_DARK : ARGB.color(255, 27, 30, 43);
   }
}
