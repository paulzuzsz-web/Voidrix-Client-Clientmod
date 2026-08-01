package dev.voidrix.module.combat;

import dev.voidrix.module.Category;
import dev.voidrix.module.hud.SimpleHudModule;
import dev.voidrix.ui.Theme;
import dev.voidrix.util.CombatTracker;

/** Consecutive hits landed on the same target without a long gap. */
public final class ComboHud extends SimpleHudModule {
    public ComboHud() {
        super("combo", "Combo", "Consecutive hits on your current target",
                Category.COMBAT, 0.5, 0.60, Theme.TEXT);
    }

    @Override
    public void onFrame() {
        CombatTracker.update();
    }

    @Override
    public boolean hasContent() {
        return CombatTracker.combo() > 0;
    }

    @Override
    protected String label() {
        return "Combo";
    }

    @Override
    protected String value() {
        int combo = CombatTracker.combo();
        return combo <= 0 ? "-" : combo + "x";
    }

    @Override
    protected int valueColor() {
        int combo = CombatTracker.combo();
        if (combo >= 8) {
            return Theme.ACCENT_ALT;
        }
        if (combo >= 4) {
            return Theme.ACCENT;
        }
        return super.valueColor();
    }
}
