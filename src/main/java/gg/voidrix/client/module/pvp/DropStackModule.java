package gg.voidrix.client.module.pvp;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;

/**
 * Laesst die Drop-Taste immer den kompletten Stapel fallen.
 *
 * <p>Umgesetzt per Mixin auf die Tastenauswertung: Vanilla ruft
 * {@code LocalPlayer#drop(boolean fullStack)} auf: {@code false} fuer ein
 * einzelnes Item, {@code true} fuer den ganzen Stapel (Strg+Q). Das Modul
 * dreht dieses Argument einfach auf {@code true}.</p>
 */
public class DropStackModule extends Module {

	private final BoolSetting invertWithSneak =
			add(new BoolSetting("invert_sneak", "Mit Schleichen umkehren",
					"Beim Schleichen wieder nur ein einzelnes Item fallen lassen", true));

	public DropStackModule() {
		super("drop_stack", "Drop Stack", "Drop-Taste wirft den ganzen Stapel", Category.PVP);
	}

	public boolean isInvertWithSneak() {
		return invertWithSneak.value();
	}
}
