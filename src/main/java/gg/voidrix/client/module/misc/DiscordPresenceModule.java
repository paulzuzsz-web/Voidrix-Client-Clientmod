package gg.voidrix.client.module.misc;

import gg.voidrix.client.integration.DiscordRpc;
import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.StringSetting;
import gg.voidrix.client.premium.PremiumManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

/**
 * Zeigt den Spielstatus in Discord an.
 *
 * <p>Die Verbindung laeuft ueber {@link DiscordRpc} - ohne externe
 * Bibliothek. Laeuft kein Discord, passiert schlicht nichts.</p>
 *
 * <p><b>Eigene Application-ID:</b> Im Discord-Developer-Portal eine Anwendung
 * anlegen, die Bilder unter "Rich Presence -&gt; Art Assets" hochladen und die
 * ID unten in {@link #appId} eintragen (oder im Menue aendern).</p>
 */
public class DiscordPresenceModule extends Module {

	/** Aktualisierung nur alle 5 Sekunden - Discord drosselt haeufigere Updates. */
	private static final int UPDATE_INTERVAL_TICKS = 100;

	private final StringSetting appId =
			add(new StringSetting("app_id", "Application-ID", "ID aus dem Discord-Developer-Portal",
					"000000000000000000"));

	private final BoolSetting showServer =
			add(new BoolSetting("show_server", "Server anzeigen", "Serveradresse in Discord zeigen", false));

	private final BoolSetting showDimension =
			add(new BoolSetting("show_dimension", "Dimension anzeigen", "Aktuelle Welt anzeigen", true));

	private int tickCounter;

	public DiscordPresenceModule() {
		super("discord_rpc", "Discord Rich Presence", "Spielstatus in Discord anzeigen", Category.MISC);
	}

	@Override
	public void onEnable() {
		// Verbindungsaufbau kann kurz blockieren -> in einen eigenen Thread
		Thread thread = new Thread(() -> DiscordRpc.connect(appId.value()), "Voidrix-DiscordRPC");
		thread.setDaemon(true);
		thread.start();

		tickCounter = 0;
	}

	@Override
	public void onDisable() {
		DiscordRpc.disconnect();
	}

	@Override
	public void onTick() {
		if (!DiscordRpc.isConnected()) {
			return;
		}

		if (tickCounter++ % UPDATE_INTERVAL_TICKS != 0) {
			return;
		}

		DiscordRpc.updatePresence(
				buildDetails(),
				buildState(),
				"voidrix_logo",
				PremiumManager.isPremium() ? "Voidrix+ " : "Voidrix Client");
	}

	/** Obere Zeile: was der Spieler gerade macht. */
	private String buildDetails() {
		Minecraft client = Minecraft.getInstance();

		if (client.level == null) {
			return "Im Hauptmenue";
		}

		if (showServer.value()) {
			ServerData server = client.getCurrentServer();

			if (server != null) {
				return "Spielt auf " + server.ip;
			}
		}

		return client.hasSingleplayerServer() ? "Einzelspieler" : "Mehrspieler";
	}

	/** Untere Zeile: Dimension bzw. Kennzeichnung. */
	private String buildState() {
		Minecraft client = Minecraft.getInstance();

		if (showDimension.value() && client.level != null) {
			String dimension = client.level.dimension().identifier().getPath();

			return switch (dimension) {
				case "overworld" -> "Oberwelt";
				case "the_nether" -> "Nether";
				case "the_end" -> "Das Ende";
				default -> dimension;
			};
		}

		return PremiumManager.isPremium() ? "Voidrix+" : "Voidrix";
	}
}
