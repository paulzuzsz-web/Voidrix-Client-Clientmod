package dev.voidrix.module;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Registry and dispatcher for every {@link Module} in the client. */
public final class ModuleManager {
    private final Map<String, Module> byId = new LinkedHashMap<>();
    private final List<Module> ordered = new ArrayList<>();

    public void register(Module module) {
        if (byId.putIfAbsent(module.id(), module) != null) {
            throw new IllegalStateException("Duplicate Voidrix module id: " + module.id());
        }
        ordered.add(module);
    }

    public void registerAll(Module... modules) {
        for (Module module : modules) {
            register(module);
        }
    }

    public List<Module> all() {
        return Collections.unmodifiableList(ordered);
    }

    public Module byId(String id) {
        return byId.get(id);
    }

    /** Every module in a category, in registration order. */
    public List<Module> byCategory(Category category) {
        List<Module> out = new ArrayList<>();
        for (Module module : ordered) {
            if (module.category() == category) {
                out.add(module);
            }
        }
        return out;
    }

    /** Every HUD widget, enabled or not. */
    public List<HudModule> hudModules() {
        List<HudModule> out = new ArrayList<>();
        for (Module module : ordered) {
            if (module instanceof HudModule hud) {
                out.add(hud);
            }
        }
        return out;
    }

    public int enabledCount() {
        int n = 0;
        for (Module module : ordered) {
            if (module.isEnabled()) {
                n++;
            }
        }
        return n;
    }

    /** Applies the "enabled by default" flags. Called only when no config file exists yet. */
    public void applyDefaults() {
        for (Module module : ordered) {
            if (module.enabledByDefault()) {
                module.setEnabled(true);
            }
        }
    }

    public void onRegisterAll() {
        for (Module module : ordered) {
            module.onRegister();
        }
    }

    public void tick(Minecraft mc) {
        for (Module module : ordered) {
            if (module.isEnabled()) {
                module.onTick(mc);
            }
        }
    }
}
