package gg.voidrix.client;

import gg.voidrix.client.screen.VoidrixSettingsScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

/**
 * TEMPORAER - nur fuer die Abnahme der Oberflaeche, nicht Teil des Mods.
 * Aktivierung ueber -Dvoidrix.autoshot=true
 */
public final class ScreenshotHarness {

    private static final int SETTLE_TICKS = 120;
    private static final int STEP_TICKS = 25;

    private static int ticks;
    private static int step;

    private ScreenshotHarness() {
    }

    public static void installIfRequested() {
        if (!Boolean.getBoolean("voidrix.autoshot")) {
            return;
        }
        Voidrix.LOGGER.info("[autoshot] aktiv");
        ClientTickEvents.END_CLIENT_TICK.register(ScreenshotHarness::onTick);
    }

    private static void onTick(Minecraft client) {
        ticks++;
        if (ticks < SETTLE_TICKS || (ticks - SETTLE_TICKS) % STEP_TICKS != 0) {
            return;
        }

        VoidrixConfig config = Voidrix.config();
        switch (step) {
            case 0 -> {
                config.fullBright = true;
                config.zoomEnabled = true;
                config.zoomSmooth = true;
                config.noHurtCam = false;
                client.gui.setScreen(new VoidrixSettingsScreen(null));
            }
            case 1 -> grab(client, "01-render");
            case 2 -> selectTab(client, 1);
            case 3 -> grab(client, "02-hud");
            case 4 -> selectTab(client, 2);
            case 5 -> grab(client, "03-steuerung");
            case 6 -> selectTab(client, 0);
            case 7 -> grab(client, "04-render-wieder");
            default -> {
                Voidrix.LOGGER.info("[autoshot] fertig");
                client.stop();
            }
        }
        step++;
    }

    private static void selectTab(Minecraft client, int index) {
        if (client.gui.screen() instanceof VoidrixSettingsScreen screen) {
            screen.selectTab(index);
        }
    }

    private static void grab(Minecraft client, String name) {
        Voidrix.LOGGER.info("[autoshot] {}", name);
        Screenshot.grab(client, false);
    }
}
