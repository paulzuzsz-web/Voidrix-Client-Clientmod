package gg.voidrix.client.module.hud;

import gg.voidrix.client.hud.HudModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.List;

/**
 * Zeigt die eigene Latenz zum Server.
 *
 * <p>Der Wert stammt aus der Tab-Liste, die der Server ohnehin an jeden Client
 * schickt - es wird also nichts zusaetzlich angefragt.</p>
 */
public class PingModule extends HudModule {

	public PingModule() {
		super("ping", "Ping", "Latenz zum Server", 0.02f, 0.06f);
	}

	@Override
	public String getLeftText() {
		return "Ping";
	}

	@Override
	public String getRightText() {
		Minecraft client = Minecraft.getInstance();
		ClientPacketListener connection = client.getConnection();

		if (client.player == null || connection == null) {
			return "-";
		}

		PlayerInfo info = connection.getPlayerInfo(client.player.getUUID());

		if (info == null) {
			return "-";
		}

		return info.getLatency() + "ms";
	}

	@Override
	public List<String> getPreviewLines() {
		return List.of(formatLine(getLeftText(), "24ms"));
	}
}
