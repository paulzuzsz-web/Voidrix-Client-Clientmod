package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

/** Round-trip latency to the current server, as reported in the player list. */
public final class PingHud extends SimpleHudModule {
    private final BoolSetting grade;

    public PingHud() {
        super("ping", "Ping", "Latency to the server you are on", 0.0, 0.0);
        this.grade = addBool("grade", "Colour by value", "Green when low, red when high", true);
    }

    /** Latency in milliseconds, or -1 when there is nothing to report. */
    private static int latency() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return -1;
        }
        ClientPacketListener connection = mc.getConnection();
        if (connection == null) {
            return -1;
        }
        PlayerInfo info = connection.getPlayerInfo(mc.player.getUUID());
        return info == null ? -1 : info.getLatency();
    }

    @Override
    public boolean hasContent() {
        return latency() >= 0;
    }

    @Override
    protected String label() {
        return "Ping";
    }

    @Override
    protected String value() {
        int ms = latency();
        return ms < 0 ? "--" : ms + "ms";
    }

    @Override
    protected int valueColor() {
        if (!grade.value()) {
            return super.valueColor();
        }
        int ms = latency();
        if (ms < 0) {
            return Theme.TEXT_DIM;
        }
        if (ms <= 80) {
            return Theme.SUCCESS;
        }
        if (ms <= 180) {
            return Theme.WARN;
        }
        return Theme.DANGER;
    }
}
