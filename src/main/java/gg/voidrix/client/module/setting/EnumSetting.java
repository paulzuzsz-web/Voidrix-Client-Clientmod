package gg.voidrix.client.module.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;

/**
 * Auswahl aus einer Enum-Konstante - wird als Dropdown gezeichnet.
 * Wird z.B. fuer den Hintergrund-Modus (Blur / Color / Blank) verwendet.
 *
 * @param <E> Enum-Typ
 */
public class EnumSetting<E extends Enum<E>> extends Setting<E> {

	private final E[] options;

	public EnumSetting(String id, String displayName, String description, E defaultValue) {
		super(id, displayName, description, defaultValue);
		this.options = defaultValue.getDeclaringClass().getEnumConstants();
	}

	public E value() {
		return get();
	}

	public E[] getOptions() {
		return options;
	}

	/** Alle Optionen als lesbare Namen - fuer das Dropdown-Widget. */
	public String[] getOptionNames() {
		String[] names = new String[options.length];

		for (int i = 0; i < options.length; i++) {
			names[i] = prettify(options[i]);
		}

		return names;
	}

	public String getSelectedName() {
		return prettify(get());
	}

	public int getSelectedIndex() {
		for (int i = 0; i < options.length; i++) {
			if (options[i] == get()) {
				return i;
			}
		}

		return 0;
	}

	public void selectIndex(int index) {
		if (index >= 0 && index < options.length) {
			set(options[index]);
		}
	}

	/** Schaltet zur naechsten Option weiter (Klick auf das Dropdown). */
	public void cycle() {
		selectIndex((getSelectedIndex() + 1) % options.length);
	}

	/** BLUR -> "Blur", NO_HURT_CAM -> "No Hurt Cam" */
	private String prettify(E constant) {
		String[] parts = constant.name().toLowerCase(Locale.ROOT).split("_");
		StringBuilder builder = new StringBuilder();

		for (String part : parts) {
			if (part.isEmpty()) {
				continue;
			}

			if (!builder.isEmpty()) {
				builder.append(' ');
			}

			builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}

		return builder.toString();
	}

	@Override
	public JsonElement save() {
		return new JsonPrimitive(get().name());
	}

	@Override
	public void load(JsonElement element) {
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
