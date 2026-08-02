package gg.voidrix.client.module.pvp;

import gg.voidrix.client.VoidrixKeys;
import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.DoubleSetting;

/**
 * Zoom auf Tastendruck (Standard: C).
 *
 * <p>Umgesetzt per Mixin auf {@code Camera#getFov} - der Sichtwinkel wird beim
 * Zoomen geteilt. Es wird nichts an den Server gemeldet; fuer den Server sieht
 * es aus, als haette der Spieler nur seine FOV-Einstellung geaendert.</p>
 */
public class ZoomModule extends Module {

	private final DoubleSetting factor =
			add(new DoubleSetting("factor", "Zoomstufe", "Wie stark herangezoomt wird", 4.0, 1.5, 10.0, 0.5));

	private final BoolSetting smooth =
			add(new BoolSetting("smooth", "Weicher Uebergang", "Sanft ein- und auszoomen", true));

	private final BoolSetting toggleMode =
			add(new BoolSetting("toggle", "Umschalten", "Taste schaltet um statt gedrueckt zu halten", false));

	/** Fortschritt des Zooms (0 = normal, 1 = voll herangezoomt). */
	private double progress;

	/** Im Umschaltmodus: ist der Zoom gerade an? */
	private boolean toggled;

	/** Merkt sich den vorherigen Tastenzustand fuer die Flankenerkennung. */
	private boolean wasDown;

	public ZoomModule() {
		super("zoom", "Zoom", "Heranzoomen auf Tastendruck", Category.PVP);
		defaultOn();
	}

	@Override
	public void onTick() {
		boolean down = VoidrixKeys.ZOOM.isDown();

		if (toggleMode.value()) {
			// nur bei der steigenden Flanke umschalten
			if (down && !wasDown) {
				toggled = !toggled;
			}
		} else {
			toggled = down;
		}

		wasDown = down;

		double target = toggled ? 1.0 : 0.0;

		if (smooth.value()) {
			// exponentielle Annaeherung - fuehlt sich weicher an als linear
			progress += (target - progress) * 0.35;

			if (Math.abs(target - progress) < 0.001) {
				progress = target;
			}
		} else {
			progress = target;
		}
	}

	@Override
	public void onDisable() {
		progress = 0.0;
		toggled = false;
	}

	/**
	 * Multiplikator fuer den Sichtwinkel. 1.0 = unveraendert.
	 * Wird vom Camera-Mixin abgefragt.
	 */
	public double getFovMultiplier() {
		if (progress <= 0.0) {
			return 1.0;
		}

		// zwischen 1.0 und 1/factor interpolieren
		double zoomed = 1.0 / factor.value();
		return 1.0 + (zoomed - 1.0) * progress;
	}

	public boolean isZooming() {
		return progress > 0.0;
	}
}
