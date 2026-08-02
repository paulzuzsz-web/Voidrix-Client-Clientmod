package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;

/**
 * Basisklasse fuer alle Einstellungen eines Moduls.
 *
 * <p>Jede Einstellung kennt ihren Anzeigenamen, kann sich selbst als JSON
 * speichern/laden und auf den Standardwert zuruecksetzen. Die GUI rendert
 * Einstellungen generisch: sie fragt nur den Typ ab und zeichnet das passende
 * Widget (Toggle, Slider, Dropdown, Farbwaehler, Textfeld).</p>
 *
 * <h2>Eigene Einstellung bauen</h2>
 * Von {@code Setting<T>} ableiten, {@link #save()} und {@link #load(JsonElement)}
 * implementieren und in {@code ui.detail.SettingWidgetFactory} einen Zweig fuer
 * den neuen Typ ergaenzen.
 *
 * @param <T> Werttyp der Einstellung
 */
public abstract class Setting<T> {

	private final String id;
	private final String displayName;
	private final String description;
	private final T defaultValue;

	protected T value;

	/** Optional: Einstellung nur sichtbar, wenn diese Bedingung wahr ist. */
	private java.util.function.BooleanSupplier visibleWhen = () -> true;

	protected Setting(String id, String displayName, String description, T defaultValue) {
		this.id = id;
		this.displayName = displayName;
		this.description = description;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public T get() {
		return value;
	}

	public void set(T newValue) {
		this.value = newValue;
	}

	/** Setzt die Einstellung auf ihren Standardwert zurueck ("Zuruecksetzen"-Button). */
	public void reset() {
		this.value = defaultValue;
	}

	public boolean isVisible() {
		return visibleWhen.getAsBoolean();
	}

	/** Blendet die Einstellung abhaengig von einer anderen ein/aus. */
	public Setting<T> visibleWhen(java.util.function.BooleanSupplier condition) {
		this.visibleWhen = condition;
		return this;
	}

	/** Serialisiert den aktuellen Wert. */
	public abstract JsonElement save();

	/** Laedt den Wert aus JSON. Fehlerhafte Werte werden ignoriert. */
	public abstract void load(JsonElement element);
}
