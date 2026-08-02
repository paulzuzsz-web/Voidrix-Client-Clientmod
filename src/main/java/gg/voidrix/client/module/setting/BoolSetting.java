package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** An/Aus-Einstellung - wird in der GUI als Toggle-Switch gezeichnet. */
public class BoolSetting extends Setting<Boolean> {

	public BoolSetting(String id, String displayName, String description, boolean defaultValue) {
		super(id, displayName, description, defaultValue);
	}

	public boolean value() {
		return get();
	}

	public void toggle() {
		set(!get());
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(get());
	}

	@Override
	public void load(JsonElement element) {
		if (element != null && element.isJsonPrimitive()) {
			set(element.getAsBoolean());
		}
	}
}
