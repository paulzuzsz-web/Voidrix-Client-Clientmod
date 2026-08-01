package dev.voidrix.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** A whole number constrained to a range, shown as a slider. */
public final class IntSetting extends Setting<Integer> {
    private final int min;
    private final int max;
    private final String suffix;

    public IntSetting(String id, String name, String description, int defaultValue, int min, int max) {
        this(id, name, description, defaultValue, min, max, "");
    }

    public IntSetting(String id, String name, String description, int defaultValue, int min, int max, String suffix) {
        super(id, name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.suffix = suffix;
    }

    public int value() {
        return get();
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    public String suffix() {
        return suffix;
    }

    /** Position of the current value inside the range, 0..1. */
    public float fraction() {
        return max == min ? 0f : (float) (get() - min) / (max - min);
    }

    /** Sets the value from a 0..1 slider position. */
    public void setFraction(float t) {
        set(Math.round(min + Math.clamp(t, 0f, 1f) * (max - min)));
    }

    @Override
    public void set(Integer v) {
        super.set(Math.clamp(v, min, max));
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsInt());
        }
    }
}
