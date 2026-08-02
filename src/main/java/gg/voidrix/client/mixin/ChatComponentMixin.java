package gg.voidrix.client.mixin;

import gg.voidrix.client.user.VoidrixUsers;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Locale;

/**
 * Setzt das "V" vor den Absender einer <b>Chatnachricht</b>.
 *
 * <p>Vanilla uebergibt an {@code ChatComponent#addMessage} nur den fertigen
 * Text, nicht den Absender. Voidrix sucht deshalb im Text nach einem Namen aus
 * der Tab-Liste: der am weitesten links stehende Treffer ist der Absender
 * (ein spaeter erwaehnter Name ist nur zitiert).</p>
 *
 * <p>Nur wenn dieser Spieler als Voidrix-Nutzer bekannt ist, wird die Nachricht
 * mit dem Praefix neu zusammengesetzt. Alle anderen Nachrichten bleiben
 * unveraendert - insbesondere werden keine Server-Formatierungen zerstoert,
 * weil das Original als Ganzes angehaengt wird.</p>
 */
@Mixin(ChatComponent.class)
public class ChatComponentMixin {

	@ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, index = 1)
	private Component voidrix$prefixChatMessage(Component message) {
		if (message == null) {
			return null;
		}

		Minecraft client = Minecraft.getInstance();
		ClientPacketListener connection = client.getConnection();

		if (connection == null) {
			return message;
		}

		String raw = message.getString();

		if (raw.isEmpty()) {
			return message;
		}

		String haystack = raw.toLowerCase(Locale.ROOT);

		PlayerInfo sender = null;
		int bestIndex = Integer.MAX_VALUE;

		for (PlayerInfo info : connection.getListedOnlinePlayers()) {
			String name = info.getProfile().name();

			if (name == null || name.isEmpty()) {
				continue;
			}

			int index = haystack.indexOf(name.toLowerCase(Locale.ROOT));

			if (index >= 0 && index < bestIndex) {
				bestIndex = index;
				sender = info;
			}
		}

		if (sender == null) {
			return message;
		}

		var uuid = sender.getProfile().id();

		if (!VoidrixUsers.isVoidrixUser(uuid)) {
			return message;
		}

		return VoidrixUsers.decorate(uuid, message, false);
	}
}
