package gg.voidrix.client.module.visual;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Gibt Traenken den Verzauberungsglanz, damit man sie im Inventar sofort
 * erkennt - beliebt in PvP-Situationen.
 *
 * <p>Umgesetzt per Mixin auf die Glanz-Abfrage von {@code ItemStack}.
 * Es wird nur die Darstellung veraendert, nicht das Item selbst.</p>
 */
public class ShinyPotsModule extends Module {

	private final BoolSetting splashOnly =
			add(new BoolSetting("splash_only", "Nur Wurftraenke", "Normale Traenke auslassen", false));

	private final BoolSetting includeArrows =
			add(new BoolSetting("include_arrows", "Auch Spektralpfeile", "Verzauberte Pfeile mitglaenzen lassen", false));

	public ShinyPotsModule() {
		super("shiny_pots", "Shiny Pots", "Traenke mit Verzauberungsglanz", Category.VISUAL);
	}

	/** true, wenn dieser Stack zusaetzlich glaenzen soll. */
	public boolean shouldGlint(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return false;
		}

		if (stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
			return true;
		}

		if (!splashOnly.value() && stack.is(Items.POTION)) {
			return true;
		}

		return includeArrows.value() && stack.is(Items.TIPPED_ARROW);
	}
}
