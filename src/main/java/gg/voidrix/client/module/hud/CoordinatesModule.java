package gg.voidrix.client.module.hud;

import gg.voidrix.client.hud.HudModule;
import gg.voidrix.client.module.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Zeigt die eigenen Koordinaten.
 *
 * <p>Beispiel fuer ein <b>mehrzeiliges</b> HUD-Modul: {@link #getLines()} wird
 * ueberschrieben und liefert je nach Einstellung eine oder drei Zeilen.</p>
 */
public class CoordinatesModule extends HudModule {

	private final BoolSetting singleLine =
			add(new BoolSetting("single_line", "Einzeilig", "X, Y und Z in einer Zeile", true));

	private final BoolSetting showDirection =
			add(new BoolSetting("show_direction", "Blickrichtung", "Himmelsrichtung anzeigen", true));

	public CoordinatesModule() {
		super("coordinates", "Koordinaten", "Aktuelle Position", 0.02f, 0.10f);
		defaultOn();
	}

	@Override
	public String getLeftText() {
		return "XYZ";
	}

	@Override
	public String getRightText() {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return "-";
		}

		return String.format("%.0f %.0f %.0f", player.getX(), player.getY(), player.getZ());
	}

	@Override
	public List<String> getLines() {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return List.of();
		}

		List<String> lines = new ArrayList<>();

		if (singleLine.value()) {
			lines.add(formatLine(getLeftText(), getRightText()));
		} else {
			lines.add(formatLine("X", String.format("%.1f", player.getX())));
			lines.add(formatLine("Y", String.format("%.1f", player.getY())));
			lines.add(formatLine("Z", String.format("%.1f", player.getZ())));
		}

		if (showDirection.value()) {
			lines.add(formatLine("Richtung", cardinalDirection(player.getYRot())));
		}

		return lines;
	}

	@Override
	public List<String> getPreviewLines() {
		List<String> lines = new ArrayList<>();

		if (singleLine.value()) {
			lines.add(formatLine(getLeftText(), "128 64 -512"));
		} else {
			lines.add(formatLine("X", "128.0"));
			lines.add(formatLine("Y", "64.0"));
			lines.add(formatLine("Z", "-512.0"));
		}

		if (showDirection.value()) {
			lines.add(formatLine("Richtung", "Nord"));
		}

		return lines;
	}

	/** Rechnet den Blickwinkel in eine Himmelsrichtung um. */
	private String cardinalDirection(float yaw) {
		// yaw normalisieren auf 0..360
		float normalised = (yaw % 360f + 360f) % 360f;

		// 8 Sektoren a 45 Grad, um 22.5 Grad versetzt
		int sector = (int) ((normalised + 22.5f) / 45f) % 8;

		return switch (sector) {
			case 0 -> "Sued";
			case 1 -> "Suedwest";
			case 2 -> "West";
			case 3 -> "Nordwest";
			case 4 -> "Nord";
			case 5 -> "Nordost";
			case 6 -> "Ost";
			default -> "Suedost";
		};
	}
}
