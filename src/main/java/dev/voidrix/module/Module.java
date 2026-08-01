package dev.voidrix.module;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.setting.DoubleSetting;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * A single toggleable feature.
 *
 * <p>Subclasses register their options in the constructor via the {@code add*} helpers and
 * override whichever lifecycle hooks they need. Nothing here talks to the network or to disk -
 * persistence is entirely {@link dev.voidrix.config.ConfigManager}'s job.
 */
public abstract class Module {
    private final String id;
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();

    private boolean enabled;
    /** GLFW key code that toggles this module, or -1 for none. */
    private int keyCode = -1;

    protected Module(String id, String name, String description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // ---- Identity -------------------------------------------------------------------------

    public String id() {
        return id;
    }

    public String displayName() {
        return name;
    }

    public String description() {
        return description;
    }

    public Category category() {
        return category;
    }

    public List<Setting<?>> settings() {
        return settings;
    }

    // ---- State ----------------------------------------------------------------------------

    public boolean isEnabled() {
        return enabled;
    }

    /** Whether this module should default to on for a fresh install. */
    public boolean enabledByDefault() {
        return false;
    }

    public void setEnabled(boolean value) {
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

    public int keyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    // ---- Lifecycle ------------------------------------------------------------------------

    /** Called once during client startup, after every module has been constructed. */
    public void onRegister() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    /** Called every client tick while enabled. */
    public void onTick(Minecraft mc) {
    }

    // ---- Setting helpers ------------------------------------------------------------------

    protected BoolSetting addBool(String id, String name, String description, boolean def) {
        return add(new BoolSetting(id, name, description, def));
    }

    protected IntSetting addInt(String id, String name, String description, int def, int min, int max) {
        return add(new IntSetting(id, name, description, def, min, max));
    }

    protected IntSetting addInt(String id, String name, String description, int def, int min, int max, String suffix) {
        return add(new IntSetting(id, name, description, def, min, max, suffix));
    }

    protected DoubleSetting addDouble(String id, String name, String description, double def, double min, double max) {
        return add(new DoubleSetting(id, name, description, def, min, max));
    }

    protected DoubleSetting addDouble(String id, String name, String description, double def,
                                      double min, double max, int decimals, String suffix) {
        return add(new DoubleSetting(id, name, description, def, min, max, decimals, suffix));
    }

    protected <E extends Enum<E>> EnumSetting<E> addEnum(String id, String name, String description, E def) {
        return add(new EnumSetting<>(id, name, description, def));
    }

    protected ColorSetting addColor(String id, String name, String description, int def) {
        return add(new ColorSetting(id, name, description, def));
    }

    protected <S extends Setting<?>> S add(S setting) {
        settings.add(setting);
        return setting;
    }
}
