package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Kommazahl-Einstellung mit Grenzen - wird als Slider gezeichnet.
 * Wird u.a. fuer HUD-Skalierung, Breite und Hoehe verwendet.
 */
public class DoubleSetting extends Setting<Double> {

	private final double min;
	private final double max;
	private final double step;

	public DoubleSetting(String id, String displayName, String description,
			double defaultValue, double min, double max, double step) {
		super(id, displayName, description, defaultValue);
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public double value() {
		return get();
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	@Override
	public void set(Double newValue) {
		double clamped = Math.max(min, Math.min(max, newValue));

		// auf Schrittweite runden, damit der Slider saubere Werte liefert
		if (step > 0) {
			clamped = Math.round(clamped / step) * step;
		}

		super.set(clamped);
	}

	public double getFraction() {
		if (max == min) {
			return 0.0;
		}

		return (get() - min) / (max - min);
	}

	public void setFraction(double fraction) {
		set(min + fraction * (max - min));
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(get());
	}

	@Override
	public void load(JsonElement element) {
		if (element != null && element.isJsonPrimitive()) {
			try {
				set(element.getAsDouble());
			} catch (NumberFormatException ignored) {
				// fehlerhafter Wert -> Standard behalten
			}
		}
	}
}
