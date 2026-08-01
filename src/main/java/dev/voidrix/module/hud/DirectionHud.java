package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;

/** Which way you are facing, with the matching axis hint. */
public final class DirectionHud extends SimpleHudModule {
    private final BoolSetting showAxis;

    public DirectionHud() {
        super("direction", "Direction", "The compass direction you are facing", 0.0, 0.0);
        this.showAxis = addBool("show_axis", "Show axis", "Append the axis the direction runs along", true);
    }

    @Override
    protected String label() {
        return "Facing";
    }

    @Override
    protected String value() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return "--";
        }
        Direction facing = player.getDirection();
        String name = switch (facing) {
            case NORTH -> "North";
            case SOUTH -> "South";
            case WEST -> "West";
            case EAST -> "East";
            default -> "--";
        };
        if (!showAxis.value()) {
            return name;
        }
        String axis = switch (facing) {
            case NORTH -> "-Z";
            case SOUTH -> "+Z";
            case WEST -> "-X";
            case EAST -> "+X";
            default -> "";
        };
        return name + " (" + axis + ")";
    }
}
