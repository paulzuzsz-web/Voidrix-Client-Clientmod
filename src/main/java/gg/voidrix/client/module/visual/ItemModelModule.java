package gg.voidrix.client.module.visual;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.DoubleSetting;

/**
 * Passt die Darstellung des Items in der Hand an (Position, Groesse, Winkel).
 *
 * <p>Der Mixin auf das Rendern der Hand liest diese Werte und verschiebt bzw.
 * skaliert das Modell entsprechend. Rein optisch - Reichweite und Trefferbereich
 * bleiben unveraendert.</p>
 */
public class ItemModelModule extends Module {

	private final DoubleSetting scale =
			add(new DoubleSetting("scale", "Groesse", "Skalierung des Handmodells", 1.0, 0.5, 2.0, 0.05));

	private final DoubleSetting offsetX =
			add(new DoubleSetting("offset_x", "Versatz X", "Verschiebung nach links/rechts", 0.0, -0.5, 0.5, 0.01));

	private final DoubleSetting offsetY =
			add(new DoubleSetting("offset_y", "Versatz Y", "Verschiebung nach oben/unten", 0.0, -0.5, 0.5, 0.01));

	private final DoubleSetting offsetZ =
			add(new DoubleSetting("offset_z", "Versatz Z", "Verschiebung nach vorn/hinten", 0.0, -0.5, 0.5, 0.01));

	private final BoolSetting hideOffhand =
			add(new BoolSetting("hide_offhand", "Zweithand ausblenden", "Item in der linken Hand verbergen", false));

	public ItemModelModule() {
		super("item_model", "Item Model", "Handmodell verschieben und skalieren", Category.VISUAL);
	}

	public float getScale() {
		return (float) scale.value();
	}

	public float getOffsetX() {
		return (float) offsetX.value();
	}

	public float getOffsetY() {
		return (float) offsetY.value();
	}

	public float getOffsetZ() {
		return (float) offsetZ.value();
	}

	public boolean isHideOffhand() {
		return hideOffhand.value();
	}
}
