package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/** Ganzzahlige Einstellung mit Grenzen - wird als Slider gezeichnet. */
public class IntSetting extends Setting<Integer> {

	private final int min;
	private final int max;

	public IntSetting(String id, String displayName, String description, int defaultValue, int min, int max) {
		super(id, displayName, description, defaultValue);
		this.min = min;
		this.max = max;
	}

	public int value() {
		return get();
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	@Override
	public void set(Integer newValue) {
		super.set(Math.max(min, Math.min(max, newValue)));
	}

	/** Position auf dem Slider (0..1). */
	public double getFraction() {
		if (max == min) {
			return 0.0;
		}

		return (double) (get() - min) / (double) (max - min);
	}

	/** Setzt den Wert anhand einer Slider-Position (0..1). */
	public void setFraction(double fraction) {
		set((int) Math.round(min + fraction * (max - min)));
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(get());
	}

	@Override
	public void load(JsonElement element) {
		if (element != null && element.isJsonPrimitive()) {
			try {
				set(element.getAsInt());
			} catch (NumberFormatException ignored) {
				// fehlerhafter Wert -> Standard behalten
			}
		}
	}
}
