package dev.voidrix.module.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

/** How many players are currently online. */
public final class PlayerCountHud extends SimpleHudModule {
    public PlayerCountHud() {
        super("player_count", "Players", "Number of players on the server", 1.0, 0.10);
    }

    private static int count() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        return connection == null ? -1 : connection.getOnlinePlayers().size();
    }

    @Override
    public boolean hasContent() {
        return count() >= 0;
    }

    @Override
    protected String label() {
        return "Players";
    }

    @Override
    protected String value() {
        int count = count();
        return count < 0 ? "--" : String.valueOf(count);
    }
}
