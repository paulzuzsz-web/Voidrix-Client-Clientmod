package dev.voidrix.module.misc;

import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.util.Keyboard;

/**
 * Makes the drop key throw the whole stack instead of one item.
 *
 * <p>Vanilla puts the useful behaviour behind Control, which is the wrong way round for anyone
 * clearing an inventory. With {@code invert} on, Control still gets you back to dropping a single
 * item, so nothing is actually taken away.
 */
public final class DropStackModule extends Module {
    private final BoolSetting invert;

    public DropStackModule() {
        super("drop_stack", "Drop stack", "Drop the whole stack with one press of the drop key",
                Category.MISC);
        this.invert = addBool("invert", "Ctrl drops one",
                "Hold Control to drop a single item instead", true);
    }

    /** Whether the drop currently being handled should take the whole stack. */
    public boolean shouldDropWholeStack() {
        if (invert.value() && Keyboard.ctrlHeld()) {
            return false;
        }
        return true;
    }
}
