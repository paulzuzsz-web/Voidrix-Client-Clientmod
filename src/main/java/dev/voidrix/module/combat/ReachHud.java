package dev.voidrix.module.combat;

import dev.voidrix.module.Category;
import dev.voidrix.module.hud.SimpleHudModule;
import dev.voidrix.ui.Theme;
import dev.voidrix.util.CombatTracker;

/**
 * How far away your last hit landed.
 *
 * <p>A readout only. It measures the distance of a swing that already happened and does not extend
 * how far you can reach by a single block.
 */
public final class ReachHud extends SimpleHudModule {
    public ReachHud() {
        super("reach", "Reach", "Distance of your last landed hit",
                Category.COMBAT, 0.5, 0.2, Theme.TEXT);
    }

    @Override
    public void onFrame() {
        CombatTracker.update();
    }

    @Override
    public boolean hasContent() {
        return CombatTracker.lastReach() >= 0;
    }

    @Override
    protected String label() {
        return "Reach";
    }

    @Override
    protected String value() {
        double reach = CombatTracker.lastReach();
        return reach < 0 ? "--" : String.format("%.2fm", reach);
    }
}
