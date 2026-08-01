package dev.voidrix.module.iface;

import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;

/**
 * Chooses the palette the Voidrix interface is drawn in.
 *
 * <p>The setting is applied whenever it differs from what is live, rather than through a change
 * callback, so it also takes effect the moment the config is loaded at startup.
 */
public final class ThemeModule extends Module {
    private final EnumSetting<Theme.Variant> variant;

    public ThemeModule() {
        super("theme", "Theme", "Switch between the dark, midnight and light palettes",
                Category.INTERFACE);
        this.variant = addEnum("variant", "Palette", "Which colour scheme to use", Theme.Variant.DARK);
    }

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    public void onEnable() {
        apply();
    }

    @Override
    public void onTick(Minecraft mc) {
        apply();
    }

    /** Also called straight after the config loads, before the first frame is drawn. */
    public void apply() {
        if (Theme.variant() != variant.value()) {
            Theme.apply(variant.value());
        }
    }
}
