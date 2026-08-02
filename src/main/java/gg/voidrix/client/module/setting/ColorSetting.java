package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Farb-Einstellung als ARGB-Integer - wird als Farbwaehler gezeichnet.
 *
 * <p>Gespeichert wird die Farbe als Hex-String (z.B. {@code "#FF7C3AED"}),
 * damit man sie in der voidrix.json auch von Hand bearbeiten kann.</p>
 */
public class ColorSetting extends Setting<Integer> {

	public ColorSetting(String id, String displayName, String description, int defaultArgb) {
		super(id, displayName, description, defaultArgb);
	}

	public int value() {
		return get();
	}

	public int getAlpha() {
		return (get() >> 24) & 0xFF;
	}

	public int getRed() {
		return (get() >> 16) & 0xFF;
	}

	public int getGreen() {
		return (get() >> 8) & 0xFF;
	}

	public int getBlue() {
		return get() & 0xFF;
	}

	public void setRgb(int red, int green, int blue) {
		set((getAlpha() << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF));
	}

	public void setAlpha(int alpha) {
		set(((alpha & 0xFF) << 24) | (get() & 0x00FFFFFF));
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(String.format("#%08X", get()));
	}

	@Override
	public void load(JsonElement element) {
		if (element == null || !element.isJsonPrimitive()) {
			return;
		}

		try {
			String raw = element.getAsString().trim();

			if (raw.startsWith("#")) {
				raw = raw.substring(1);
			}

			// 6-stellige Angaben werden als voll deckend interpretiert
			long parsed = Long.parseLong(raw, 16);

			if (raw.length() <= 6) {
				parsed |= 0xFF000000L;
			}

			set((int) parsed);
		} catch (NumberFormatException ignored) {
			// fehlerhafter Wert -> Standard behalten
		}
	}
}
