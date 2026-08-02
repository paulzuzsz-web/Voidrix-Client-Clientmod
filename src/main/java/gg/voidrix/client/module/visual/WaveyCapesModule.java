package gg.voidrix.client.module.visual;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.DoubleSetting;
import gg.voidrix.client.module.setting.IntSetting;

/**
 * Laesst Umhaenge weich im Wind wehen statt starr zu haengen.
 *
 * <p>Vanilla berechnet den Umhangwinkel nur aus der Bewegung des Spielers.
 * Dieses Modul legt eine Wellenbewegung darueber - rein optisch und nur auf
 * dem eigenen Client sichtbar.</p>
 *
 * <p>Der berechnete Zusatzwinkel wird vom Umhang-Mixin abgefragt.</p>
 */
public class WaveyCapesModule extends Module {

	private final DoubleSetting waveStrength =
			add(new DoubleSetting("strength", "Staerke", "Wie weit der Umhang ausschlaegt", 6.0, 0.0, 25.0, 0.5));

	private final DoubleSetting waveSpeed =
			add(new DoubleSetting("speed", "Geschwindigkeit", "Tempo der Wellenbewegung", 1.0, 0.1, 4.0, 0.1));

	private final IntSetting sway =
			add(new IntSetting("sway", "Seitliches Schwingen", "Zusaetzliches Pendeln zur Seite", 3, 0, 15));

	private final BoolSetting onlyWhenMoving =
			add(new BoolSetting("only_moving", "Nur in Bewegung", "Im Stehen ruhig haengen lassen", false));

	public WaveyCapesModule() {
		super("wavey_capes", "Wavey Capes", "Weich wehende Umhaenge", Category.VISUAL);
		markNew();
	}

	/**
	 * Zusaetzlicher Neigungswinkel des Umhangs in Grad.
	 *
	 * @param timeMillis fortlaufende Zeit (System.currentTimeMillis)
	 * @param moving     ob der Spieler sich gerade bewegt
	 */
	public double getWaveAngle(long timeMillis, boolean moving) {
		if (onlyWhenMoving.value() && !moving) {
			return 0.0;
		}

		double phase = timeMillis / 1000.0 * waveSpeed.value();
		return Math.sin(phase * Math.PI * 2.0) * waveStrength.value();
	}

	/** Seitliches Pendeln in Grad. */
	public double getSwayAngle(long timeMillis) {
		if (sway.value() == 0) {
			return 0.0;
		}

		// leicht andere Frequenz als die Hauptwelle, damit es organisch wirkt
		double phase = timeMillis / 1000.0 * waveSpeed.value() * 0.7;
		return Math.sin(phase * Math.PI * 2.0) * sway.value();
	}
}
