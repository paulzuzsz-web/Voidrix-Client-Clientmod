package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.EnumSetting;
import net.minecraft.client.Minecraft;

/**
 * How fast you are actually moving.
 *
 * <p>Measured from the change in position between ticks rather than from the velocity vector, so
 * it reflects what the world does to you - being slowed by cobwebs, carried by a boat - instead of
 * what your input asked for.
 */
public final class SpeedHud extends SimpleHudModule {
    private final EnumSetting<Unit> unit;
    private final BoolSetting horizontalOnly;

    private double lastX;
    private double lastY;
    private double lastZ;
    private double speed;
    private boolean primed;

    public SpeedHud() {
        super("speed", "Speed", "Your current movement speed", 0.0, 0.0);
        this.unit = addEnum("unit", "Unit", "Blocks per second or kilometres per hour", Unit.BLOCKS_PER_SECOND);
        this.horizontalOnly = addBool("horizontal", "Ignore vertical", "Measure only across the ground", true);
    }

    @Override
    public void onEnable() {
        primed = false;
    }

    @Override
    public void onTick(Minecraft mc) {
        var player = mc.player;
        if (player == null) {
            primed = false;
            return;
        }
        if (!primed) {
            lastX = player.getX();
            lastY = player.getY();
            lastZ = player.getZ();
            primed = true;
            return;
        }
        double dx = player.getX() - lastX;
        double dy = player.getY() - lastY;
        double dz = player.getZ() - lastZ;
        lastX = player.getX();
        lastY = player.getY();
        lastZ = player.getZ();

        double perTick = horizontalOnly.value()
                ? Math.sqrt(dx * dx + dz * dz)
                : Math.sqrt(dx * dx + dy * dy + dz * dz);
        double perSecond = perTick * 20.0;
        // Light smoothing: raw per-tick deltas jitter enough to make the readout unreadable.
        speed = speed * 0.6 + perSecond * 0.4;
    }

    @Override
    protected String label() {
        return "Speed";
    }

    @Override
    protected String value() {
        return switch (unit.value()) {
            case BLOCKS_PER_SECOND -> String.format("%.2f b/s", speed);
            case KILOMETRES_PER_HOUR -> String.format("%.1f km/h", speed * 3.6);
        };
    }

    public enum Unit {
        BLOCKS_PER_SECOND,
        KILOMETRES_PER_HOUR
    }
}
