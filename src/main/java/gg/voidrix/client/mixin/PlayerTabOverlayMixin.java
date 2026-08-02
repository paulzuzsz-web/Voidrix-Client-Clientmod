package gg.voidrix.client.mixin;

import gg.voidrix.client.user.VoidrixUsers;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Setzt das "V" vor den Namen in der <b>Tab-Liste</b>.
 *
 * <p>{@code PlayerTabOverlay#getNameForDisplay} baut den Anzeigenamen eines
 * Eintrags - inklusive Team-Faerbung und eventuellem Server-Praefix. Genau
 * davor haengt Voidrix sein Zeichen.</p>
 *
 * <p>Der Eintrag selbst ({@link PlayerInfo}) bleibt unveraendert - es wird nur
 * der dargestellte Text erweitert.</p>
 */
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

	@Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
	private void voidrix$prefixTabName(PlayerInfo playerInfo, CallbackInfoReturnable<Component> info) {
		if (playerInfo == null) {
			return;
		}

		Component original = info.getReturnValue();

		if (original == null) {
			return;
		}

		var uuid = playerInfo.getProfile().id();

		if (!VoidrixUsers.isVoidrixUser(uuid)) {
			return;
		}

		// nicht animiert: die Tab-Liste wird nicht jeden Frame neu gebaut
		info.setReturnValue(VoidrixUsers.decorate(uuid, original, false));
	}
}
