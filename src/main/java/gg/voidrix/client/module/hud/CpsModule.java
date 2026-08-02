package gg.voidrix.client.module.hud;

import gg.voidrix.client.hud.HudModule;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.util.ClickTracker;

import java.util.List;

/** Zeigt die Klicks pro Sekunde an. Gefuettert vom MouseHandler-Mixin. */
public class CpsModule extends HudModule {

	private final BoolSetting showRight =
			add(new BoolSetting("show_right", "Rechtsklicks", "Auch die rechte Maustaste zaehlen", false));

	public CpsModule() {
		super("cps", "CPS", "Klicks pro Sekunde", 0.02f, 0.30f);
		markNew();
	}

	@Override
	public String getLeftText() {
		return "CPS";
	}

	@Override
	public String getRightText() {
		if (showRight.value()) {
			return ClickTracker.getLeftCps() + " | " + ClickTracker.getRightCps();
		}

		return String.valueOf(ClickTracker.getLeftCps());
	}

	@Override
	public List<String> getPreviewLines() {
		// Im Editor klickt niemand - deshalb ein fester Beispielwert.
		return List.of(formatLine(getLeftText(), showRight.value() ? "8 | 3" : "8"));
	}
}
