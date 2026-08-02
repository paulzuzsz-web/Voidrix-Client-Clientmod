package gg.voidrix.client.module.pvp;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.DoubleSetting;

/**
 * Entfernt das Kamera-Wackeln beim Schadennehmen.
 *
 * <p>Umgesetzt per Mixin auf {@code GameRenderer#bobHurt} - die Methode, die
 * die Kamera bei Treffern kippt. Bei Staerke 0 wird sie komplett
 * uebersprungen, sonst wird der Effekt abgeschwaecht.</p>
 *
 * <p>Rein optisch und rein clientseitig: der Server merkt davon nichts.</p>
 */
public class NoHurtCamModule extends Module {

	private final DoubleSetting strength =
			add(new DoubleSetting("strength", "Staerke", "0 = ganz aus, 1 = unveraendert", 0.0, 0.0, 1.0, 0.05));

	public NoHurtCamModule() {
		super("no_hurt_cam", "NoHurtCam", "Kein Kamera-Wackeln bei Treffern", Category.PVP);
		defaultOn();
	}

	/** true, wenn der Effekt komplett unterdrueckt werden soll. */
	public boolean shouldCancel() {
		return strength.value() <= 0.0;
	}

	/** Abschwaechung des Effekts, wenn er nicht ganz abgeschaltet ist. */
	public double getStrength() {
		return strength.value();
	}
}
