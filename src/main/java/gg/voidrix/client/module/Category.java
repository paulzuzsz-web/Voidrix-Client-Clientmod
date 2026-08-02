package gg.voidrix.client.module;

/**
 * Kategorie eines Moduls. Bestimmt die Gruppierung im Menue und die
 * Filterung ueber die Tab-Leiste.
 */
public enum Category {

	HUD("HUD"),
	PVP("Gameplay & PvP"),
	VISUAL("Optik"),
	MISC("Sonstiges");

	private final String displayName;

	Category(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
