package dev.voidrix.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * An ARGB colour.
 *
 * <p>Stored as a hex string so a hand-edited config stays readable. Setting {@link #rainbow} makes
 * the colour sweep the accent ramp instead of using the fixed value.
 */
public final class ColorSetting extends Setting<Integer> {
    private final BoolSetting rainbow;

    public ColorSetting(String id, String name, String description, int defaultValue) {
        super(id, name, description, defaultValue);
        this.rainbow = new BoolSetting(id + "_rainbow", "Rainbow", "Cycle this colour through the spectrum", false);
    }

    public int value() {
        return get();
    }

    public BoolSetting rainbow() {
        return rainbow;
    }

    /** The colour to actually draw with at this moment, honouring rainbow mode. */
    public int resolve() {
        if (!rainbow.value()) {
            return get();
        }
        float t = (System.currentTimeMillis() % 4000L) / 4000f;
        return (get() & 0xFF000000) | (hsbToRgb(t, 0.72f, 1f) & 0x00FFFFFF);
    }

    /**
     * Hue/saturation/brightness to packed RGB. Hand-rolled so the mod does not need the
     * {@code java.desktop} module just for a colour conversion.
     */
    public static int hsbToRgb(float hue, float saturation, float brightness) {
        float h = (hue - (float) Math.floor(hue)) * 6f;
        float f = h - (float) Math.floor(h);
        float p = brightness * (1f - saturation);
        float q = brightness * (1f - saturation * f);
        float t = brightness * (1f - saturation * (1f - f));

        float r;
        float g;
        float b;
        switch ((int) h) {
            case 0 -> { r = brightness; g = t; b = p; }
            case 1 -> { r = q; g = brightness; b = p; }
            case 2 -> { r = p; g = brightness; b = t; }
            case 3 -> { r = p; g = q; b = brightness; }
            case 4 -> { r = t; g = p; b = brightness; }
            default -> { r = brightness; g = p; b = q; }
        }
        return (Math.round(r * 255f) << 16) | (Math.round(g * 255f) << 8) | Math.round(b * 255f);
    }

    public int red() {
        return (get() >>> 16) & 0xFF;
    }

    public int green() {
        return (get() >>> 8) & 0xFF;
    }

    public int blue() {
        return get() & 0xFF;
    }

    public int alphaChannel() {
        return (get() >>> 24) & 0xFF;
    }

    public void setRgb(int r, int g, int b) {
        set((alphaChannel() << 24) | (r << 16) | (g << 8) | b);
    }

    public void setAlphaChannel(int a) {
        set((a << 24) | (get() & 0x00FFFFFF));
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(String.format("#%08X", get()));
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        String raw = element.getAsString().trim();
        if (raw.startsWith("#")) {
            raw = raw.substring(1);
        }
        try {
            set((int) Long.parseLong(raw, 16));
        } catch (NumberFormatException ignored) {
            // Keep the default rather than blowing up on a malformed hand edit.
        }
    }
}
