package gg.voidrix.client.user;

import gg.voidrix.client.premium.PremiumManager;
import gg.voidrix.client.ui.Theme;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Wer traegt das leuchtende "V" vor dem Namen?
 *
 * <h2>Warum sieht man erstmal nur sich selbst?</h2>
 * Voidrix ist ein reiner Client-Mod. Der Client weiss deshalb nur von sich
 * selbst sicher, dass Voidrix laeuft - ueber andere Spieler bekommt er vom
 * Server keinerlei Mod-Informationen. Standardmaessig traegt also nur der
 * eigene Spieler das V.
 *
 * <h2>Ausweitung auf andere Spieler</h2>
 * Es gibt zwei uebliche Wege, das zu erweitern - beide lassen sich hier
 * andocken, ohne dass Rendering oder Mixins angefasst werden muessen:
 *
 * <ol>
 *   <li><b>Backend/REST:</b> Beim Betreten eines Servers schickt der Client
 *       die UUIDs aus der Tab-Liste an einen eigenen Dienst, z.B.
 *       {@code POST https://api.voidrix.gg/v1/users/lookup}. Der Dienst
 *       antwortet mit der Teilmenge, die Voidrix nutzt, plus Voidrix+ Status.
 *       Das Ergebnis wandert per {@link #markVoidrixUser(UUID, boolean)}
 *       hier hinein - fertig. Vorteil: funktioniert auf jedem Server, auch
 *       ohne dass dieser den Mod kennt.</li>
 *
 *   <li><b>Custom Payload:</b> Laeuft auf dem Server ein Voidrix-Plugin, kann
 *       es ueber einen eigenen Kanal (z.B. {@code voidrix:users}) die Liste
 *       aktiv an alle Clients schicken. Dafuer wuerde man mit
 *       {@code ClientPlayNetworking.registerGlobalReceiver(...)} aus der
 *       Fabric-Networking-API einen Empfaenger registrieren und die Payload
 *       ebenfalls in {@link #markVoidrixUser(UUID, boolean)} kippen.
 *       Vorteil: kein eigener Webdienst noetig, dafuer serverseitige
 *       Installation.</li>
 * </ol>
 *
 * <p>Wichtig: Solange nichts davon eingerichtet ist, verhaelt sich der Mod
 * vollstaendig passiv - es wird nichts gesendet und nichts abgefragt.</p>
 */
public final class VoidrixUsers {

	/** Alle bekannten Voidrix-Nutzer. */
	private static final Set<UUID> USERS = Collections.synchronizedSet(new HashSet<>());

	/** Teilmenge davon mit Voidrix+. */
	private static final Set<UUID> PREMIUM_USERS = Collections.synchronizedSet(new HashSet<>());

	private VoidrixUsers() {
	}

	// ---------------------------------------------------------------
	// Registrierung
	// ---------------------------------------------------------------

	/**
	 * Traegt einen Spieler als Voidrix-Nutzer ein.
	 * Genau hier wuerde die Antwort eines Backends bzw. einer Custom Payload
	 * einfliessen (siehe Klassenkommentar).
	 */
	public static void markVoidrixUser(UUID uuid, boolean premium) {
		if (uuid == null) {
			return;
		}

		USERS.add(uuid);

		if (premium) {
			PREMIUM_USERS.add(uuid);
		} else {
			PREMIUM_USERS.remove(uuid);
		}
	}

	public static void forget(UUID uuid) {
		USERS.remove(uuid);
		PREMIUM_USERS.remove(uuid);
	}

	public static void clear() {
		USERS.clear();
		PREMIUM_USERS.clear();
	}

	// ---------------------------------------------------------------
	// Abfragen
	// ---------------------------------------------------------------

	/** true, wenn dieser Spieler das V bekommen soll. */
	public static boolean isVoidrixUser(UUID uuid) {
		if (uuid == null) {
			return false;
		}

		// Der eigene Spieler zaehlt immer - der Mod laeuft ja nachweislich.
		if (isSelf(uuid)) {
			return true;
		}

		return USERS.contains(uuid);
	}

	/** true, wenn dieser Spieler die Premium-Variante des V bekommt. */
	public static boolean isPremiumUser(UUID uuid) {
		if (uuid == null) {
			return false;
		}

		if (isSelf(uuid)) {
			return PremiumManager.isPremium();
		}

		return PREMIUM_USERS.contains(uuid);
	}

	private static boolean isSelf(UUID uuid) {
		Minecraft client = Minecraft.getInstance();
		return client.player != null && client.player.getUUID().equals(uuid);
	}

	// ---------------------------------------------------------------
	// Darstellung des Praefixes
	// ---------------------------------------------------------------

	/**
	 * Baut das "V"-Praefix fuer einen Spieler.
	 *
	 * <ul>
	 *   <li>Voidrix+ : Violett und fett - wirkt durch die kraeftige Akzentfarbe
	 *       wie ein Leuchten. Ein echter Farbverlauf pro Buchstabe ist im Chat
	 *       und in der Tab-Liste nicht moeglich, weil Minecraft dort nur eine
	 *       Farbe pro Textbaustein zulaesst; im Nametag wird der Verlauf
	 *       zusaetzlich ueber die Zeit animiert (siehe unten).</li>
	 *   <li>Normal : schlichtes graues V.</li>
	 * </ul>
	 *
	 * @return Praefix inklusive abschliessendem Leerzeichen
	 */
	public static MutableComponent buildPrefix(UUID uuid) {
		boolean premium = isPremiumUser(uuid);

		Style style = premium
				? Style.EMPTY.withColor(TextColor.fromRgb(Theme.ACCENT & 0xFFFFFF)).withBold(true)
				: Style.EMPTY.withColor(ChatFormatting.GRAY);

		return Component.literal("V").setStyle(style).append(Component.literal(" "));
	}

	/**
	 * Variante fuer das Nametag ueber dem Kopf: Bei Voidrix+ pulsiert die Farbe
	 * zwischen Violett und Cyan, wodurch das V sichtbar "glueht".
	 */
	public static MutableComponent buildAnimatedPrefix(UUID uuid) {
		if (!isPremiumUser(uuid)) {
			return buildPrefix(uuid);
		}

		int pulsing = Theme.pulse(System.currentTimeMillis()) & 0xFFFFFF;

		Style style = Style.EMPTY
				.withColor(TextColor.fromRgb(pulsing))
				.withBold(true);

		return Component.literal("V").setStyle(style).append(Component.literal(" "));
	}

	/**
	 * Haengt das Praefix vor einen bestehenden Namen - die zentrale Stelle,
	 * die alle Mixins benutzen.
	 */
	public static Component decorate(UUID uuid, Component name, boolean animated) {
		if (!isVoidrixUser(uuid)) {
			return name;
		}

		MutableComponent prefix = animated ? buildAnimatedPrefix(uuid) : buildPrefix(uuid);

		// Wichtig: als Kinder an eine *leere* Wurzel haengen. Wuerde man
		// stattdessen prefix.append(name) benutzen, erbt der Name den Stil des
		// Praefixes - die komplette Nachricht waere dann violett und fett.
		return Component.empty().append(prefix).append(name);
	}
}
