package gg.voidrix.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zentrale Konstanten des Mods.
 *
 * <p>Voidrix ist ein <b>reiner Client-Mod</b>: Es wird niemals ein eigenes Paket
 * an den Server geschickt und kein Spielverhalten veraendert, das der Server
 * nicht ohnehin erlaubt. Dadurch laeuft der Mod auf jedem Server, ohne dass er
 * dort installiert sein muss, und loest keine Anti-Cheat-Probleme aus.</p>
 */
public final class Voidrix {

	public static final String MOD_ID = "voidrix";
	public static final String MOD_NAME = "Voidrix";

	/** Wird im Menue unten links angezeigt. */
	public static final String VERSION = "1.0.0";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	private Voidrix() {
	}
}
