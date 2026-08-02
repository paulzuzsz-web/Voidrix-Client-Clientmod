package gg.voidrix.client.module.hud;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.IntSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Locale;

/**
 * Zeigt den Kopf des Absenders vor jeder Chatnachricht.
 *
 * <p>Anders als die uebrigen HUD-Module hat Chat Heads keine freie Position -
 * es haengt fest am Chat. Deshalb erbt es von {@link Module} statt von
 * {@code HudModule} und taucht nicht im HUD-Editor auf.</p>
 *
 * <p><b>Wie der Absender ermittelt wird:</b> Vanilla speichert zur fertig
 * gerenderten Chatzeile keinen Absender mehr. Voidrix sucht deshalb im Text
 * nach einem Namen aus der Tab-Liste (siehe {@link #resolveSkin(Component)}).
 * Das funktioniert fuer das uebliche Format {@code <Name> Nachricht} sowie fuer
 * die meisten Server-Chatformate mit Praefix.</p>
 */
public class ChatHeadsModule extends Module {

	private final IntSetting size =
			add(new IntSetting("size", "Kopfgroesse", "Kantenlaenge in Pixeln", 8, 6, 12));

	private final IntSetting gap =
			add(new IntSetting("gap", "Abstand", "Abstand zwischen Kopf und Text", 2, 0, 6));

	private final BoolSetting hatLayer =
			add(new BoolSetting("hat_layer", "Hut-Ebene", "Zweite Skin-Ebene mitzeichnen", true));

	public ChatHeadsModule() {
		super("chat_heads", "Chat Heads", "Spielerkopf vor jeder Chatnachricht", Category.HUD);
		markNew();
	}

	public int getSize() {
		return size.value();
	}

	public int getGap() {
		return gap.value();
	}

	public boolean isHatLayer() {
		return hatLayer.value();
	}

	/** Gesamter Platz, um den die Chatzeile nach rechts rueckt. */
	public int getIndent() {
		return size.value() + gap.value();
	}

	/**
	 * Versucht, zu einer Chatnachricht die Skin-Textur des Absenders zu finden.
	 *
	 * @return Textur des Absenders oder {@code null}, wenn kein Spieler passt
	 */
	public Identifier resolveSkin(Component message) {
		Minecraft client = Minecraft.getInstance();
		ClientPacketListener connection = client.getConnection();

		if (connection == null || message == null) {
			return null;
		}

		String raw = message.getString();

		if (raw.isEmpty()) {
			return null;
		}

		String haystack = raw.toLowerCase(Locale.ROOT);

		PlayerInfo best = null;
		int bestIndex = Integer.MAX_VALUE;

		for (PlayerInfo info : connection.getListedOnlinePlayers()) {
			String name = info.getProfile().name();

			if (name == null || name.isEmpty()) {
				continue;
			}

			int index = haystack.indexOf(name.toLowerCase(Locale.ROOT));

			// Der Absender steht immer am weitesten links - ein Name, der
			// spaeter im Text vorkommt, ist nur erwaehnt.
			if (index >= 0 && index < bestIndex) {
				bestIndex = index;
				best = info;
			}
		}

		return best == null ? null : best.getSkin().body().texturePath();
	}
}
