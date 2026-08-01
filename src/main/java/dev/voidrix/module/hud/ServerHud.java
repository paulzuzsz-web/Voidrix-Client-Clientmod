package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

/** Address of the server you are connected to. */
public final class ServerHud extends SimpleHudModule {
    private final BoolSetting preferName;

    public ServerHud() {
        super("server", "Server", "Address of the server you are playing on", 0.0, 0.0);
        this.preferName = addBool("prefer_name", "Use saved name",
                "Show the name from your server list instead of the raw address", false);
    }

    @Override
    public boolean hasContent() {
        Minecraft mc = Minecraft.getInstance();
        return mc.getCurrentServer() != null || mc.isLocalServer();
    }

    @Override
    protected String label() {
        return "Server";
    }

    @Override
    protected String value() {
        Minecraft mc = Minecraft.getInstance();
        ServerData server = mc.getCurrentServer();
        if (server == null) {
            return mc.isLocalServer() ? "Singleplayer" : "--";
        }
        if (preferName.value() && server.name != null && !server.name.isBlank()) {
            return server.name;
        }
        return server.ip;
    }
}
