package gg.voidrix.client.module;

import com.google.gson.JsonObject;
import gg.voidrix.client.module.setting.Setting;
import gg.voidrix.client.premium.PremiumManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Basisklasse fuer jedes Voidrix-Modul.
 *
 * <h2>Eigenes Modul bauen</h2>
 * <pre>{@code
 * public class MyModule extends Module {
 *     private final BoolSetting fancy = add(new BoolSetting("fancy", "Schick", "Tut etwas", true));
 *
 *     public MyModule() {
 *         super("my_module", "Mein Modul", "Kurzbeschreibung", Category.MISC);
 *     }
 *
 *     @Override public void onEnable() { ... }
 *     @Override public void onTick()   { ... }
 * }
 * }</pre>
 * Danach in {@link ModuleManager#registerAll()} eintragen - fertig.
 */
public abstract class Module {

	private final String id;
	private final String displayName;
	private final String description;
	private final Category category;

	private final List<Setting<?>> settings = new ArrayList<>();

	private boolean enabled;

	/** Standardzustand, auf den "Zuruecksetzen" zurueckfaellt. */
	private boolean enabledByDefault;

	/** Nur mit Voidrix+ nutzbar - im Menue mit Schloss-Icon markiert. */
	private boolean premiumOnly;

	/** Zeigt im Menue das "NEU"-Badge. */
	private boolean markedNew;

	protected Module(String id, String displayName, String description, Category category) {
		this.id = id;
		this.displayName = displayName;
		this.description = description;
		this.category = category;
	}

	// ---------------------------------------------------------------
	// Metadaten
	// ---------------------------------------------------------------

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}

	public Category getCategory() {
		return category;
	}

	public List<Setting<?>> getSettings() {
		return settings;
	}

	public boolean isPremiumOnly() {
		return premiumOnly;
	}

	public boolean isMarkedNew() {
		return markedNew;
	}

	/** Markiert das Modul als Voidrix+ exklusiv (Schloss-Icon im Menue). */
	protected Module premium() {
		this.premiumOnly = true;
		return this;
	}

	/** Zeigt das "NEU"-Badge auf der Modul-Karte. */
	protected Module markNew() {
		this.markedNew = true;
		return this;
	}

	/** Modul ist ab Werk aktiv. */
	protected Module defaultOn() {
		this.enabled = true;
		this.enabledByDefault = true;
		return this;
	}

	/**
	 * Gesperrt, wenn es ein Voidrix+ Modul ist und der Nutzer kein Voidrix+ hat.
	 * Gesperrte Module lassen sich nicht einschalten.
	 */
	public boolean isLocked() {
		return premiumOnly && !PremiumManager.isPremium();
	}

	// ---------------------------------------------------------------
	// Zustand
	// ---------------------------------------------------------------

	public boolean isEnabled() {
		// Verliert der Nutzer Voidrix+, wird das Modul automatisch inaktiv.
		return enabled && !isLocked();
	}

	public void setEnabled(boolean value) {
		if (value && isLocked()) {
			return;
		}

		if (this.enabled == value) {
			return;
		}

		this.enabled = value;

		if (value) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	/** Setzt Modulzustand und alle Einstellungen auf die Standardwerte zurueck. */
	public void resetToDefaults() {
		for (Setting<?> setting : settings) {
			setting.reset();
		}

		setEnabled(enabledByDefault);
	}

	// ---------------------------------------------------------------
	// Einstellungen
	// ---------------------------------------------------------------

	/** Registriert eine Einstellung und gibt sie zurueck (fuer Feld-Initialisierung). */
	protected <T extends Setting<?>> T add(T setting) {
		settings.add(setting);
		return setting;
	}

	// ---------------------------------------------------------------
	// Hooks - koennen ueberschrieben werden
	// ---------------------------------------------------------------

	/** Wird beim Aktivieren aufgerufen. */
	public void onEnable() {
	}

	/** Wird beim Deaktivieren aufgerufen. */
	public void onDisable() {
	}

	/** Wird jeden Client-Tick aufgerufen - nur wenn das Modul aktiv ist. */
	public void onTick() {
	}

	// ---------------------------------------------------------------
	// Persistenz
	// ---------------------------------------------------------------

	public JsonObject save() {
		JsonObject root = new JsonObject();
		root.addProperty("enabled", enabled);

		JsonObject settingsJson = new JsonObject();

		for (Setting<?> setting : settings) {
			settingsJson.add(setting.getId(), setting.save());
		}

		root.add("settings", settingsJson);
		return root;
	}

	public void load(JsonObject root) {
		if (root == null) {
			return;
		}

		if (root.has("enabled")) {
			// direkt setzen, ohne onEnable/onDisable - die Welt existiert
			// beim Laden der Config noch gar nicht.
			this.enabled = root.get("enabled").getAsBoolean();
		}

		if (root.has("settings") && root.get("settings").isJsonObject()) {
			JsonObject settingsJson = root.getAsJsonObject("settings");

			for (Setting<?> setting : settings) {
				if (settingsJson.has(setting.getId())) {
					setting.load(settingsJson.get(setting.getId()));
				}
			}
		}
	}
}
