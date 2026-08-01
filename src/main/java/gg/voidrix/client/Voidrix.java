package gg.voidrix.client;

import gg.voidrix.client.feature.Zoom;
import gg.voidrix.client.screen.VoidrixSettingsScreen;
import java.nio.file.Path;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Einstiegspunkt des Voidrix Client.
 *
 * <p>Haelt die Konfiguration und die Tastenbelegungen. Die eigentlichen Features sitzen in
 * {@code gg.voidrix.client.feature} und werden von den Mixins in {@code gg.voidrix.client.mixin}
 * abgefragt - die Mixins enthalten selbst keine Logik.
 */
public final class Voidrix implements ClientModInitializer {

    public static final String MOD_ID = "voidrix";
    public static final String NAME = "Voidrix";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    /** Aus den Mod-Metadaten gelesen, damit Anzeige und Build nicht auseinanderlaufen. */
    public static final String VERSION = FabricLoader.getInstance()
            .getModContainer(MOD_ID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("dev");

    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("voidrix.json");

    private static VoidrixConfig config = new VoidrixConfig();

    public static KeyMapping zoomKey;
    public static KeyMapping fullBrightKey;
    public static KeyMapping settingsKey;

    public static VoidrixConfig config() {
        return config;
    }

    public static void saveConfig() {
        config.save(CONFIG_PATH);
    }

    @Override
    public void onInitializeClient() {
        config = VoidrixConfig.load(CONFIG_PATH);

        zoomKey = register("key.voidrix.zoom", GLFW.GLFW_KEY_C);
        fullBrightKey = register("key.voidrix.fullbright", GLFW.GLFW_KEY_G);
        settingsKey = register("key.voidrix.settings", GLFW.GLFW_KEY_RIGHT_SHIFT);

        ClientTickEvents.END_CLIENT_TICK.register(Voidrix::onTick);
        ScreenshotHarness.installIfRequested();

        LOGGER.info("{} bereit - Konfiguration: {}", NAME, CONFIG_PATH);
    }

    private static KeyMapping register(String translationKey, int defaultKey) {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping(translationKey, defaultKey, KeyMapping.Category.MISC));
    }

    private static void onTick(Minecraft client) {
        Zoom.tick(zoomKey.isDown());

        while (fullBrightKey.consumeClick()) {
            config.fullBright = !config.fullBright;
            saveConfig();
        }

        while (settingsKey.consumeClick()) {
            if (client.gui.screen() == null) {
                client.gui.setScreen(new VoidrixSettingsScreen(null));
            }
        }
    }
}
