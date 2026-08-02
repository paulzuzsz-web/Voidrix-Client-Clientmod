package gg.voidrix.client.module.visual;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.DoubleSetting;

/**
 * 3D-Vorschau des eigenen Skins im Cosmetics-Bereich des Menues.
 *
 * <p>Die Figur wird von {@code CosmeticsPage} gezeichnet; dieses Modul haelt
 * nur die Einstellungen (Drehung, Groesse, Beleuchtung) und den aktuellen
 * Drehwinkel.</p>
 */
public class SkinPreviewModule extends Module {

	private final BoolSetting autoRotate =
			add(new BoolSetting("auto_rotate", "Automatisch drehen", "Figur langsam rotieren lassen", true));

	private final DoubleSetting rotateSpeed =
			add(new DoubleSetting("rotate_speed", "Drehgeschwindigkeit", "Grad pro Sekunde", 25.0, 5.0, 90.0, 5.0));

	private final DoubleSetting previewScale =
			add(new DoubleSetting("preview_scale", "Groesse", "Groesse der Vorschau", 1.0, 0.5, 2.0, 0.05));

	private final BoolSetting followMouse =
			add(new BoolSetting("follow_mouse", "Blick folgt Maus", "Kopf schaut zum Mauszeiger", true));

	/** Aktueller Drehwinkel der Vorschau in Grad. */
	private float rotation;

	public SkinPreviewModule() {
		super("skin_preview", "3D Skin Vorschau", "Eigener Skin als drehbare Figur", Category.VISUAL);
		defaultOn();
	}

	@Override
	public void onTick() {
		if (autoRotate.value()) {
			// 20 Ticks pro Sekunde -> Grad pro Tick
			rotation = (rotation + (float) (rotateSpeed.value() / 20.0)) % 360f;
		}
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float value) {
		this.rotation = value % 360f;
	}

	public float getPreviewScale() {
		return (float) previewScale.value();
	}

	public boolean isFollowMouse() {
		return followMouse.value();
	}

	public boolean isAutoRotate() {
		return autoRotate.value();
	}
}
