package gg.voidrix.client.module.hud;

import gg.voidrix.client.hud.HudModule;
import gg.voidrix.client.module.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

/** Zeigt Leben, Ruestung und Hunger als kompakte Zahlenanzeige. */
public class HealthIndicatorModule extends HudModule {

	private final BoolSetting showArmor =
			add(new BoolSetting("show_armor", "Ruestung", "Ruestungspunkte anzeigen", true));

	private final BoolSetting showFood =
			add(new BoolSetting("show_food", "Hunger", "Hungerpunkte anzeigen", false));

	public HealthIndicatorModule() {
		super("health_indicator", "Health Indicator", "Leben, Ruestung und Hunger", 0.02f, 0.22f);
	}

	@Override
	public String getLeftText() {
		return "HP";
	}

	@Override
	public String getRightText() {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return "-";
		}

		// Absorption (Goldenes Herz) mit anzeigen, sonst wirkt der Wert falsch
		float total = player.getHealth() + player.getAbsorptionAmount();
		return String.format("%.1f", total);
	}

	@Override
	public List<String> getLines() {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return List.of();
		}

		List<String> lines = new ArrayList<>();
		lines.add(formatLine(getLeftText(), getRightText()));

		if (showArmor.value()) {
			lines.add(formatLine("Ruestung", String.valueOf(player.getArmorValue())));
		}

		if (showFood.value()) {
			lines.add(formatLine("Hunger", String.valueOf(player.getFoodData().getFoodLevel())));
		}

		return lines;
	}

	@Override
	public List<String> getPreviewLines() {
		List<String> lines = new ArrayList<>();
		lines.add(formatLine(getLeftText(), "20.0"));

		if (showArmor.value()) {
			lines.add(formatLine("Ruestung", "16"));
		}

		if (showFood.value()) {
			lines.add(formatLine("Hunger", "18"));
		}

		return lines;
	}
}
