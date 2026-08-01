package gg.voidrix.client.feature;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.VoidrixConfig;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;

/**
 * Zeichnet die aktivierten HUD-Zeilen als einen zusammenhaengenden Block.
 *
 * <p>Wird vom Hud-Mixin am Ende von {@code extractRenderState} aufgerufen, also innerhalb
 * des normalen HUD-Renderpfads von 26.2.
 */
public final class HudOverlay {

    private static final DateTimeFormatter CLOCK_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final int LINE_HEIGHT = 10;
    private static final int PADDING = 3;
    private static final int BACKGROUND_COLOR = 0x60000000;

    private HudOverlay() {
    }

    public static void render(GuiGraphicsExtractor graphics) {
        Minecraft client = Minecraft.getInstance();
        VoidrixConfig config = Voidrix.config();

        List<String> lines = collectLines(client, config);
        if (lines.isEmpty()) {
            return;
        }

        Font font = client.font;
        int x = config.hudX;
        int y = config.hudY;
        int color = 0xFF000000 | (config.hudColor & 0xFFFFFF);

        if (config.hudBackground) {
            int widest = 0;
            for (String line : lines) {
                widest = Math.max(widest, font.width(line));
            }
            graphics.fill(
                    x - PADDING,
                    y - PADDING,
                    x + widest + PADDING,
                    y + lines.size() * LINE_HEIGHT - (LINE_HEIGHT - font.lineHeight) + PADDING,
                    BACKGROUND_COLOR);
        }

        for (int i = 0; i < lines.size(); i++) {
            graphics.text(font, lines.get(i), x, y + i * LINE_HEIGHT, color);
        }
    }

    private static List<String> collectLines(Minecraft client, VoidrixConfig config) {
        List<String> lines = new ArrayList<>(4);

        if (config.fpsHud) {
            lines.add(client.getFps() + " FPS");
        }
        if (config.coordsHud) {
            LocalPlayer player = client.player;
            if (player != null) {
                lines.add(String.format(
                        "XYZ %.1f %.1f %.1f", player.getX(), player.getY(), player.getZ()));
            }
        }
        if (config.pingHud) {
            int ping = currentPing(client);
            if (ping >= 0) {
                lines.add(ping + " ms");
            }
        }
        if (config.clockHud) {
            lines.add(LocalTime.now().format(CLOCK_FORMAT));
        }
        return lines;
    }

    /** @return Latenz in ms, oder -1 wenn kein Server verbunden ist. */
    private static int currentPing(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null || client.getConnection() == null) {
            return -1;
        }
        PlayerInfo info = client.getConnection().getPlayerInfo(player.getUUID());
        return info == null ? -1 : info.getLatency();
    }
}
