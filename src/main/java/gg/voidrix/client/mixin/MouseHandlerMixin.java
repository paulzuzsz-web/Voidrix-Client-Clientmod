package gg.voidrix.client.mixin;

import gg.voidrix.client.util.ClickTracker;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fuettert den CPS-Zaehler.
 *
 * <p>Hier wird nur <b>mitgezaehlt</b> - es wird kein Klick erzeugt, verschluckt
 * oder veraendert. Der Hook sitzt an der Stelle, an der GLFW den Tastendruck
 * meldet, wodurch auch sehr schnelles Klicken exakt erfasst wird.</p>
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Inject(method = "onButton", at = @At("HEAD"))
	private void voidrix$countClick(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo info) {
		// nur das Druecken zaehlen, nicht das Loslassen
		if (action != GLFW.GLFW_PRESS) {
			return;
		}

		if (buttonInfo.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			ClickTracker.onLeftClick();
		} else if (buttonInfo.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			ClickTracker.onRightClick();
		}
	}
}
