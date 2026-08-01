package dev.voidrix;

import dev.voidrix.config.ConfigManager;
import dev.voidrix.module.Module;
import dev.voidrix.module.ModuleManager;
import dev.voidrix.module.hud.ArmorHud;
import dev.voidrix.module.hud.BiomeHud;
import dev.voidrix.module.hud.ClockHud;
import dev.voidrix.module.hud.CoordinatesHud;
import dev.voidrix.module.hud.CpsHud;
import dev.voidrix.module.hud.DirectionHud;
import dev.voidrix.module.hud.DurabilityHud;
import dev.voidrix.module.hud.EffectsHud;
import dev.voidrix.module.hud.FpsHud;
import dev.voidrix.module.hud.HeldItemHud;
import dev.voidrix.module.hud.KeystrokesHud;
import dev.voidrix.module.hud.LightLevelHud;
import dev.voidrix.module.hud.LookingAtHud;
import dev.voidrix.module.hud.MemoryHud;
import dev.voidrix.module.hud.PingHud;
import dev.voidrix.module.hud.PlayerCountHud;
import dev.voidrix.module.hud.ServerHud;
import dev.voidrix.module.hud.SessionHud;
import dev.voidrix.module.hud.SpeedHud;
import dev.voidrix.module.hud.WeatherHud;
import dev.voidrix.module.iface.CleanHudModule;
import dev.voidrix.module.visual.FullbrightModule;
import dev.voidrix.module.visual.NoBobbingModule;
import dev.voidrix.module.visual.ZoomModule;
import dev.voidrix.ui.HudEditorScreen;
import dev.voidrix.ui.HudRenderer;
import dev.voidrix.ui.MenuScreen;
import dev.voidrix.util.Keyboard;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point for Voidrix Client.
 *
 * <p>Voidrix is entirely client side and entirely local: it renders widgets, tweaks a handful of
 * vanilla options and stores its settings in one JSON file. There is no account system, no
 * backend, no database and no telemetry - nothing it does reaches the network.
 */
public final class VoidrixClient implements ClientModInitializer {
    public static final String MOD_ID = "voidrix";
    public static final String MOD_NAME = "Voidrix Client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private static ModuleManager modules;
    private static ConfigManager config;

    private FullbrightModule fullbright;
    private NoBobbingModule noBobbing;

    public static ModuleManager modules() {
        return modules;
    }

    public static ConfigManager config() {
        return config;
    }

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();
        registerModules();

        config = new ConfigManager(modules);

        VoidrixKeys.register();
        HudRenderer.register();

        // Wrapping vanilla HUD elements has to happen during init, before anything renders.
        modules.onRegisterAll();

        // Settings are read once the client is up, because several modules touch game options
        // that do not exist yet while mods are still initialising.
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            config.load();
            LOGGER.info("[Voidrix] {} modules registered, {} enabled",
                    modules.all().size(), modules.enabledCount());
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            // Hand back any vanilla option we were holding, then persist.
            fullbright.restore();
            noBobbing.restore();
            config.save();
        });

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void registerModules() {
        fullbright = new FullbrightModule();
        noBobbing = new NoBobbingModule();

        modules.registerAll(
                // HUD widgets
                new FpsHud(),
                new PingHud(),
                new CoordinatesHud(),
                new DirectionHud(),
                new BiomeHud(),
                new SpeedHud(),
                new ClockHud(),
                new SessionHud(),
                new MemoryHud(),
                new CpsHud(),
                new KeystrokesHud(),
                new ArmorHud(),
                new EffectsHud(),
                new DurabilityHud(),
                new HeldItemHud(),
                new LookingAtHud(),
                new LightLevelHud(),
                new WeatherHud(),
                new ServerHud(),
                new PlayerCountHud(),

                // Visual
                fullbright,
                noBobbing,
                new ZoomModule(),

                // Interface
                new CleanHudModule()
        );
    }

    private void onTick(Minecraft mc) {
        if (VoidrixKeys.openMenu.consumeClick()) {
            mc.setScreenAndShow(new MenuScreen());
        }
        if (VoidrixKeys.openHudEditor.consumeClick()) {
            mc.setScreenAndShow(new HudEditorScreen());
        }

        handleModuleHotkeys(mc);
        modules.tick(mc);
    }

    /** Per-module toggle keys, polled here so they need no extra vanilla key binding each. */
    private void handleModuleHotkeys(Minecraft mc) {
        // Only while actually playing, so typing in chat or a menu cannot trip a hotkey.
        if (!mc.mouseHandler.isMouseGrabbed()) {
            return;
        }
        boolean changed = false;
        for (Module module : modules.all()) {
            if (module.keyCode() > 0 && Keyboard.justPressed(module.id(), module.keyCode())) {
                module.toggle();
                changed = true;
            }
        }
        if (changed) {
            config.save();
        }
    }
}
