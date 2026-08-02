package gg.voidrix.client.module.hud;

import gg.voidrix.client.hud.HudModule;
import gg.voidrix.client.ui.Theme;
import gg.voidrix.client.util.TpsTracker;

import java.util.List;

/**
 * Zeigt die geschaetzten Server-TPS.
 * Die Messung erfolgt passiv ueber den Fortschritt der Weltzeit
 * (siehe {@link TpsTracker}).
 */
public class TpsModule extends HudModule {

	public TpsModule() {
		super("tps", "TPS", "Geschaetzte Server-Ticks pro Sekunde", 0.02f, 0.14f);
	}

	@Override
	public String getLeftText() {
		return "TPS";
	}

	@Override
	public String getRightText() {
		return String.format("%.1f", TpsTracker.getTps());
	}

	@Override
	public List<String> getPreviewLines() {
		return List.of(formatLine(getLeftText(), "20.0"));
	}

	/**
	 * Faerbt den Wert je nach Serverlast ein: gruen ab 19 TPS, sonst
	 * Akzentfarbe, unter 15 TPS rot.
	 */
	public int getStatusColor() {
		double tps = TpsTracker.getTps();

		if (tps >= 19.0) {
			return Theme.SUCCESS;
		}

		if (tps < 15.0) {
			return Theme.ERROR;
		}

		return Theme.ACCENT_2;
	}
}
