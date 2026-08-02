package gg.voidrix.client.module.pvp;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.ColorSetting;
import gg.voidrix.client.module.setting.IntSetting;
import gg.voidrix.client.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * Hebt am Boden liegende Items durch einen Leuchtrahmen hervor.
 *
 * <p>Umgesetzt per Mixin auf {@code Entity#isCurrentlyGlowing}: Voidrix
 * meldet fuer passende Item-Entities einfach "leuchtet", woraufhin Vanilla
 * seinen normalen Umriss-Effekt zeichnet. Es wird nichts am Spielzustand
 * geaendert - der Server sieht keinerlei Unterschied.</p>
 */
public class ItemHighlighterModule extends Module {

	private final IntSetting range =
			add(new IntSetting("range", "Reichweite", "Nur Items in diesem Umkreis (Bloecke)", 24, 4, 64));

	private final BoolSetting onlyRare =
			add(new BoolSetting("only_rare", "Nur seltene Items", "Gewoehnliche Items ignorieren", false));

	private final BoolSetting rarityColors =
			add(new BoolSetting("rarity_colors", "Seltenheitsfarben", "Farbe nach Item-Seltenheit", true));

	private final ColorSetting color =
			add(new ColorSetting("color", "Farbe", "Farbe des Leuchtrahmens", Theme.ACCENT));

	public ItemHighlighterModule() {
		super("item_highlighter", "Item Highlighter", "Leuchtrahmen um liegende Items", Category.PVP);
		markNew();
	}

	/** Entscheidet, ob dieses Item hervorgehoben wird. Vom Mixin aufgerufen. */
	public boolean shouldHighlight(Entity entity) {
		if (!(entity instanceof ItemEntity item)) {
			return false;
		}

		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return false;
		}

		// Reichweite quadratisch vergleichen - spart die Wurzel
		double maxDistance = range.value();

		if (entity.distanceToSqr(player) > maxDistance * maxDistance) {
			return false;
		}

		if (onlyRare.value()) {
			return item.getItem().getRarity() != Rarity.COMMON;
		}

		return true;
	}

	/** Farbe des Umrisses fuer ein bestimmtes Item. */
	public int getHighlightColor(ItemStack stack) {
		if (!rarityColors.value()) {
			return color.value();
		}

		// Vanilla-Seltenheiten auf das Voidrix-Farbschema abbilden
		return switch (stack.getRarity()) {
			case UNCOMMON -> 0xFFFFFF55;
			case RARE -> Theme.ACCENT_2;
			case EPIC -> Theme.ACCENT;
			default -> 0xFFFFFFFF;
		};
	}
}
