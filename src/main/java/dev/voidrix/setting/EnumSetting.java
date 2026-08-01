package dev.voidrix.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;

/** A choice between the constants of an enum, shown as a segmented control. */
public final class EnumSetting<E extends Enum<E>> extends Setting<E> {
    private final E[] options;

    public EnumSetting(String id, String name, String description, E defaultValue) {
        super(id, name, description, defaultValue);
        this.options = defaultValue.getDeclaringClass().getEnumConstants();
    }

    public E value() {
        return get();
    }

    public E[] options() {
        return options;
    }

    /** Advances to the next constant, wrapping at the end. */
    public void cycle() {
        set(options[(get().ordinal() + 1) % options.length]);
    }

    /** Title-cased label for a constant, e.g. {@code TOP_LEFT} becomes {@code Top Left}. */
    public static String label(Enum<?> e) {
        String[] parts = e.name().toLowerCase(Locale.ROOT).split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }

    public String currentLabel() {
        return label(get());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get().name());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        String name = element.getAsString();
        for (E option : options) {
            if (option.name().equals(name)) {
                set(option);
                return;
            }
        }
    }
}
