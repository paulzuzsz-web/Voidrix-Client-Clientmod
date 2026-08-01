package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import net.minecraft.client.Minecraft;

/** Player position, either exact or rounded to the block. */
public final class CoordinatesHud extends SimpleHudModule {
    private final BoolSetting precise;

    public CoordinatesHud() {
        super("coordinates", "Coordinates", "Your X, Y and Z position", 0.0, 0.0);
        this.precise = addBool("precise", "Decimals", "Show one decimal place instead of whole blocks", false);
    }

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    @Override
    protected String label() {
        return "XYZ";
    }

    @Override
    protected String value() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return "--";
        }
        if (precise.value()) {
            return String.format("%.1f %.1f %.1f", player.getX(), player.getY(), player.getZ());
        }
        return String.format("%d %d %d",
                (int) Math.floor(player.getX()),
                (int) Math.floor(player.getY()),
                (int) Math.floor(player.getZ()));
    }
}
