package dev.voidrix.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** An on/off switch. */
public final class BoolSetting extends Setting<Boolean> {
    public BoolSetting(String id, String name, String description, boolean defaultValue) {
        super(id, name, description, defaultValue);
    }

    public boolean value() {
        return get();
    }

    public void toggle() {
        set(!get());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsBoolean());
        }
    }
}
