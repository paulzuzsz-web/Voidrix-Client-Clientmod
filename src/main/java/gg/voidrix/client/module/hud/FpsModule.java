package gg.voidrix.client.module.hud;

import gg.voidrix.client.hud.HudModule;
import net.minecraft.client.Minecraft;

import java.util.List;

/** Zeigt die aktuellen Bilder pro Sekunde. */
public class FpsModule extends HudModule {

	public FpsModule() {
		super("fps", "FPS", "Bilder pro Sekunde", 0.02f, 0.02f);
		defaultOn();
	}

	@Override
	public String getLeftText() {
		return "FPS";
	}

	@Override
	public String getRightText() {
		return String.valueOf(Minecraft.getInstance().getFps());
	}

	@Override
	public List<String> getPreviewLines() {
		return List.of(formatLine(getLeftText(), "144"));
	}
}
