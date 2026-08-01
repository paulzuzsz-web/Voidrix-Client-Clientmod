package dev.voidrix.setting;

import com.google.gson.JsonElement;

import java.util.function.BooleanSupplier;

/**
 * One configurable value belonging to a module.
 *
 * <p>The {@code id} is the stable key written to disk and never shown to the user, so display
 * names can be reworded freely without invalidating anyone's config.
 */
public abstract class Setting<T> {
    private final String id;
    private final String name;
    private final String description;
    protected final T defaultValue;
    protected T value;
    private BooleanSupplier visible = () -> true;

    protected Setting(String id, String name, String description, T defaultValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return name;
    }

    public String description() {
        return description;
    }

    public T get() {
        return value;
    }

    public void set(T v) {
        this.value = v;
    }

    public void reset() {
        this.value = defaultValue;
    }

    public boolean isDefault() {
        return value.equals(defaultValue);
    }

    /**
     * Hides this setting in the UI while the supplied condition is false - used for options that
     * only make sense once another option is switched on.
     */
    public Setting<T> visibleWhen(BooleanSupplier condition) {
        this.visible = condition;
        return this;
    }

    public boolean visible() {
        return visible.getAsBoolean();
    }

    public abstract JsonElement toJson();

    public abstract void fromJson(JsonElement element);
}
