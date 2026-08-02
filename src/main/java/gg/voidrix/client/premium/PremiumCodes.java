package gg.voidrix.client.premium;

import java.util.List;
import java.util.Locale;

/**
 * Liste aller gueltigen Voidrix+ Freischalt-Codes.
 *
 * <h2>Neue Codes ergaenzen</h2>
 * Einfach einen weiteren String in {@link #VALID_CODES} eintragen - mehr ist
 * nicht noetig. Der Vergleich ist gross-/kleinschreibungsunabhaengig und
 * ignoriert fuehrende/abschliessende Leerzeichen.
 *
 * <p><b>Hinweis zur Sicherheit:</b> Da der Mod rein clientseitig ist, stehen
 * die Codes im Klartext in der JAR und koennen ausgelesen werden. Fuer eine
 * faelschungssichere Loesung muesste die Einloesung serverseitig gegen ein
 * Backend geprueft werden (siehe Kommentar in {@link PremiumManager}).</p>
 */
public final class PremiumCodes {

	/** Fehlermeldung bei ungueltiger Eingabe (Vorgabe aus dem Design). */
	public static final String ERROR_INVALID = "Code ungueltig - pruefe deine Eingabe";

	/**
	 * Alle gueltigen Codes. Hier weitere Codes einfach anhaengen.
	 */
	public static final List<String> VALID_CODES = List.of(
			"kwhfiejyguso+"   // Hauptcode
			// , "voidrix-beta-2026"   <-- Beispiel fuer einen weiteren Code
	);

	/**
	 * Prueft, ob der eingegebene Code gueltig ist.
	 *
	 * @param input Roheingabe aus dem Textfeld (darf {@code null} sein)
	 * @return true, wenn der Code in {@link #VALID_CODES} steht
	 */
	public static boolean isValid(String input) {
		if (input == null) {
			return false;
		}

		String normalised = input.trim().toLowerCase(Locale.ROOT);

		if (normalised.isEmpty()) {
			return false;
		}

		for (String code : VALID_CODES) {
			if (code.toLowerCase(Locale.ROOT).equals(normalised)) {
				return true;
			}
		}

		return false;
	}

	private PremiumCodes() {
	}
}
