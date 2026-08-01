package dev.voidrix.module.misc;

import com.google.gson.JsonObject;
import dev.voidrix.VoidrixClient;
import dev.voidrix.discord.DiscordIpc;
import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.StringSetting;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

/**
 * Shows what you are playing in your Discord profile.
 *
 * <p><strong>This is the one part of Voidrix that shares anything.</strong> Everything else in the
 * mod stays on your machine; rich presence exists to be seen by other people, so what it reveals is
 * spelled out option by option and all of it starts switched off. The server address in particular
 * is hidden behind its own toggle, because a presence line is visible to everyone on your friends
 * list and, depending on your settings, beyond it.
 *
 * <p>Discord state is gathered on the game thread and handed to a background worker, because
 * reading Minecraft's world and connection from another thread is not safe, and writing to a socket
 * on the render thread would stutter the game.
 */
public final class DiscordModule extends Module {
    /** How often the worker pushes an update. Discord rate-limits roughly one update per 15s. */
    private static final long UPDATE_INTERVAL_MS = 15_000L;

    private final StringSetting applicationId;
    private final StringSetting packName;
    private final BoolSetting showServer;
    private final BoolSetting hideAddress;
    private final BoolSetting showWorld;
    private final BoolSetting showModCount;
    private final BoolSetting showElapsed;

    /** Snapshot taken on the game thread for the worker to publish. */
    private volatile String details = "";
    private volatile String state = "";
    private volatile boolean wantConnection;

    private final long sessionStart = System.currentTimeMillis() / 1000L;
    private Thread worker;

    public DiscordModule() {
        super("discord", "Discord presence", "Show what you are playing on your Discord profile",
                Category.MISC);
        this.applicationId = add(new StringSetting("application_id", "Application ID",
                "The ID of your own Discord application - see the README", "", 32, "required"));
        this.packName = add(new StringSetting("pack_name", "Pack name",
                "Shown as the top line, e.g. the name of your modpack", "", 48, "Voidrix Client"));
        this.showServer = addBool("show_server", "Show server",
                "Reveal that you are on a multiplayer server", false);
        this.hideAddress = addBool("hide_address", "Hide the address",
                "Show only the server's name, never its IP", true);
        this.showWorld = addBool("show_world", "Show world name",
                "Reveal the name of your singleplayer world", false);
        this.showModCount = addBool("show_mods", "Show mod count",
                "Append how many mods are loaded", true);
        this.showElapsed = addBool("show_elapsed", "Show elapsed time",
                "Discord counts up from when you started playing", true);

        hideAddress.visibleWhen(showServer::value);
    }

    // -------------------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------------------

    @Override
    public void onEnable() {
        wantConnection = true;
        startWorker();
    }

    @Override
    public void onDisable() {
        wantConnection = false;
    }

    /** Stops the worker for good. Called when the game is shutting down. */
    public void shutdown() {
        wantConnection = false;
        Thread current = worker;
        worker = null;
        if (current != null) {
            current.interrupt();
        }
    }

    private synchronized void startWorker() {
        if (worker != null && worker.isAlive()) {
            return;
        }
        Thread thread = new Thread(this::runWorker, "Voidrix Discord");
        thread.setDaemon(true);
        worker = thread;
        thread.start();
    }

    private void runWorker() {
        DiscordIpc ipc = new DiscordIpc();
        try {
            while (!Thread.currentThread().isInterrupted() && worker == Thread.currentThread()) {
                if (!wantConnection || applicationId.isBlank()) {
                    if (ipc.isConnected()) {
                        ipc.close();
                    }
                    sleep(2000L);
                    continue;
                }

                if (!ipc.isConnected() && !ipc.connect(applicationId.value().trim())) {
                    // Discord is not running, or not running yet. Try again shortly.
                    sleep(10_000L);
                    continue;
                }

                if (!ipc.sendActivity(buildActivity())) {
                    sleep(5_000L);
                    continue;
                }
                sleep(UPDATE_INTERVAL_MS);
            }
        } finally {
            ipc.close();
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private JsonObject buildActivity() {
        JsonObject activity = new JsonObject();
        String top = details;
        String bottom = state;
        if (!top.isEmpty()) {
            activity.addProperty("details", top);
        }
        if (!bottom.isEmpty()) {
            activity.addProperty("state", bottom);
        }
        if (showElapsed.value()) {
            JsonObject timestamps = new JsonObject();
            timestamps.addProperty("start", sessionStart);
            activity.add("timestamps", timestamps);
        }

        JsonObject assets = new JsonObject();
        assets.addProperty("large_image", "voidrix");
        assets.addProperty("large_text", "Voidrix Client");
        activity.add("assets", assets);
        return activity;
    }

    // -------------------------------------------------------------------------------------
    // Snapshot, taken on the game thread
    // -------------------------------------------------------------------------------------

    @Override
    public void onTick(Minecraft mc) {
        details = buildDetails();
        state = buildState(mc);
        if (wantConnection) {
            startWorker();
        }
    }

    private String buildDetails() {
        String name = packName.isBlank() ? "Voidrix Client" : packName.value().trim();
        if (!showModCount.value()) {
            return name;
        }
        int mods = FabricLoader.getInstance().getAllMods().size();
        return name + "  -  " + mods + " mods";
    }

    private String buildState(Minecraft mc) {
        if (mc.level == null) {
            return "In the menus";
        }

        ServerData server = mc.getCurrentServer();
        if (server != null) {
            if (!showServer.value()) {
                return "In a world";
            }
            if (hideAddress.value()) {
                boolean named = server.name != null && !server.name.isBlank();
                return named ? "On " + server.name : "On a server";
            }
            return "On " + server.ip;
        }

        if (mc.isLocalServer()) {
            if (showWorld.value() && mc.getSingleplayerServer() != null) {
                String world = mc.getSingleplayerServer().getWorldData().getLevelName();
                return "Singleplayer - " + world;
            }
            return "Singleplayer";
        }
        return "In a world";
    }
}
