package dev.voidrix.module.hud;

import net.minecraft.client.Minecraft;

/** Current weather in the world you are in. */
public final class WeatherHud extends SimpleHudModule {
    public WeatherHud() {
        super("weather", "Weather", "Whether it is clear, raining or storming", 0.0, 0.30);
    }

    @Override
    public boolean hasContent() {
        return Minecraft.getInstance().level != null;
    }

    @Override
    protected String label() {
        return "Weather";
    }

    @Override
    protected String value() {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return "--";
        }
        if (level.isThundering()) {
            return "Thunder";
        }
        return level.isRaining() ? "Rain" : "Clear";
    }
}
