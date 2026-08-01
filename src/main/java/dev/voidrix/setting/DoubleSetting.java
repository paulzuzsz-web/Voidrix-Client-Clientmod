package dev.voidrix.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** A fractional number constrained to a range, shown as a slider. */
public final class DoubleSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final int decimals;
    private final String suffix;

    public DoubleSetting(String id, String name, String description, double defaultValue,
                         double min, double max) {
        this(id, name, description, defaultValue, min, max, 2, "");
    }

    public DoubleSetting(String id, String name, String description, double defaultValue,
                         double min, double max, int decimals, String suffix) {
        super(id, name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.decimals = decimals;
        this.suffix = suffix;
    }

    public double value() {
        return get();
    }

    public float floatValue() {
        return get().floatValue();
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public String suffix() {
        return suffix;
    }

    public float fraction() {
        return max == min ? 0f : (float) ((get() - min) / (max - min));
    }

    public void setFraction(float t) {
        double raw = min + Math.clamp(t, 0f, 1f) * (max - min);
        double step = Math.pow(10, decimals);
        set(Math.round(raw * step) / step);
    }

    public String format() {
        return String.format("%." + decimals + "f%s", get(), suffix);
    }

    @Override
    public void set(Double v) {
        super.set(Math.clamp(v, min, max));
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsDouble());
        }
    }
}
