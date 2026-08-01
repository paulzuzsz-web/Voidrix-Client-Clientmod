package dev.voidrix.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** A short line of free text, edited in place in the settings pane. */
public final class StringSetting extends Setting<String> {
    private final int maxLength;
    private final String placeholder;

    public StringSetting(String id, String name, String description, String defaultValue,
                         int maxLength, String placeholder) {
        super(id, name, description, defaultValue);
        this.maxLength = maxLength;
        this.placeholder = placeholder;
    }

    public String value() {
        return get();
    }

    public int maxLength() {
        return maxLength;
    }

    /** Greyed-out hint shown while the field is empty. */
    public String placeholder() {
        return placeholder;
    }

    public boolean isBlank() {
        return get() == null || get().isBlank();
    }

    @Override
    public void set(String v) {
        if (v == null) {
            super.set("");
            return;
        }
        super.set(v.length() > maxLength ? v.substring(0, maxLength) : v);
    }

    /** Appends a typed character, respecting the length limit. */
    public void append(char c) {
        if (get().length() < maxLength) {
            set(get() + c);
        }
    }

    /** Removes the last character. */
    public void backspace() {
        String current = get();
        if (!current.isEmpty()) {
            set(current.substring(0, current.length() - 1));
        }
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsString());
        }
    }
}
