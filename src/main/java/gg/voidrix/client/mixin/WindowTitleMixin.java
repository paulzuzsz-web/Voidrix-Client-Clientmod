package gg.voidrix.client.mixin;

import com.mojang.blaze3d.platform.Window;
import gg.voidrix.client.Voidrix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Ersetzt den Fenstertitel durch das Client-Branding. Als {@code @ModifyArg} umgesetzt, damit
 * Vanilla den Titel weiterhin selbst setzt - so bleibt das Verhalten beim Fensterwechsel intakt.
 */
@Mixin(Window.class)
public abstract class WindowTitleMixin {

    @ModifyArg(
            method = "setTitle",
            at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowTitle(JLjava/lang/CharSequence;)V"),
            index = 1)
    private CharSequence voidrix$brandTitle(CharSequence original) {
        if (!Voidrix.config().customWindowTitle) {
            return original;
        }
        return Voidrix.NAME + " | " + original;
    }
}
