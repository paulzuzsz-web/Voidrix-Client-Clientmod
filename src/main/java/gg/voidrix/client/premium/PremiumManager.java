package gg.voidrix.client.premium;

import gg.voidrix.client.Voidrix;

/**
 * Verwaltet den Voidrix+ Status.
 *
 * <p>Der Status wird dauerhaft in {@code config/voidrix.json} gespeichert und
 * beim Start geladen. Ein einmal eingeloester Code muss also nicht erneut
 * eingegeben werden.</p>
 *
 * <h2>Ausbau auf ein echtes Backend</h2>
 * Aktuell wird der Code lokal gegen {@link PremiumCodes#VALID_CODES} geprueft -
 * das ist fuer einen reinen Client-Mod die einzige Moeglichkeit, laesst sich
 * aber auch umgehen. Fuer eine faelschungssichere Loesung wuerde man in
 * {@link #redeem(String)} stattdessen den Code an einen eigenen Server schicken
 * (z.B. {@code POST https://api.voidrix.gg/redeem}), dort gegen eine Datenbank
 * pruefen und ein signiertes Token zurueckgeben. Das Token wuerde man in der
 * Config ablegen und beim Start gegen den oeffentlichen Schluessel verifizieren.
 */
public final class PremiumManager {

	private static boolean premium;

	/** Zeitpunkt der letzten erfolgreichen Einloesung (nur informativ). */
	private static long redeemedAt;

	private PremiumManager() {
	}

	public static boolean isPremium() {
		return premium;
	}

	public static long getRedeemedAt() {
		return redeemedAt;
	}

	/**
	 * Loest einen Code ein.
	 *
	 * @param code Eingabe aus dem Textfeld
	 * @return true bei Erfolg, false wenn der Code ungueltig ist
	 */
	public static boolean redeem(String code) {
		if (!PremiumCodes.isValid(code)) {
			return false;
		}

		premium = true;
		redeemedAt = System.currentTimeMillis();

		Voidrix.LOGGER.info("Voidrix+ wurde freigeschaltet.");
		return true;
	}

	/** Nur fuer das Laden aus der Config. */
	public static void restore(boolean value, long timestamp) {
		premium = value;
		redeemedAt = timestamp;
	}

	/** Hebt Voidrix+ wieder auf (z.B. fuer Tests). */
	public static void revoke() {
		premium = false;
		redeemedAt = 0L;
	}
}
