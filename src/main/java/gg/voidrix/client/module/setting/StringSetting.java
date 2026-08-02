package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Text-Einstellung - wird als Textfeld gezeichnet.
 *
 * <p>Wird u.a. fuer das Format-Textfeld der HUD-Module benutzt. Dort stehen
 * die Platzhalter <code>{left}</code> und <code>{right}</code> zur Verfuegung,
 * die beim Rendern durch Beschriftung bzw. Wert ersetzt werden
 * (siehe {@link gg.voidrix.client.hud.HudModule#formatLine(String, String)}).</p>
 */
public class StringSetting extends Setting<String> {

	private final int maxLength;

	public StringSetting(String id, String displayName, String description, String defaultValue) {
		this(id, displayName, description, defaultValue, 128);
	}

	public StringSetting(String id, String displayName, String description, String defaultValue, int maxLength) {
		super(id, displayName, description, defaultValue);
		this.maxLength = maxLength;
	}

	public String value() {
		return get();
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public void set(String newValue) {
		if (newValue == null) {
			super.set("");
			return;
		}

		super.set(newValue.length() > maxLength ? newValue.substring(0, maxLength) : newValue);
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(get());
	}

	@Override
	public void load(JsonElement element) {
		if (element != null && element.isJsonPrimitive()) {
			set(element.getAsString());
		}
	}
}
