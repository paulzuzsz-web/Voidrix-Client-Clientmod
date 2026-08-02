package gg.voidrix.client.module.pvp;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.ColorSetting;
import gg.voidrix.client.module.setting.DoubleSetting;
import gg.voidrix.client.ui.Theme;

/**
 * Faerbt den Auswahlrahmen des anvisierten Blocks um.
 *
 * <p>Umgesetzt per Mixin auf das Zeichnen des Block-Umrisses. Die Farbe wird
 * dort aus diesem Modul gelesen; abgeschaltet blendet den Rahmen ganz aus.</p>
 */
public class BlockOutlinesModule extends Module {

	private final ColorSetting color =
			add(new ColorSetting("color", "Farbe", "Farbe des Auswahlrahmens", Theme.ACCENT));

	private final DoubleSetting thickness =
			add(new DoubleSetting("thickness", "Linienstaerke", "Dicke der Linien", 2.0, 0.5, 6.0, 0.5));

	private final BoolSetting hideOutline =
			add(new BoolSetting("hide", "Rahmen ausblenden", "Gar keinen Rahmen zeichnen", false));

	public BlockOutlinesModule() {
		super("block_outlines", "Block Outlines", "Eingefaerbter Block-Auswahlrahmen", Category.PVP);
	}

	public int getColor() {
		return color.value();
	}

	public float getThickness() {
		return (float) thickness.value();
	}

	public boolean isHidden() {
		return hideOutline.value();
	}
}
