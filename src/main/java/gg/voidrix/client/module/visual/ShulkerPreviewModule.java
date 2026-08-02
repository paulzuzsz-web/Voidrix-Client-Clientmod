package gg.voidrix.client.module.visual;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.IntSetting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Zeigt den Inhalt einer Shulkerbox direkt im Tooltip.
 *
 * <p>Der Inhalt steckt in der Datenkomponente
 * {@code minecraft:container}, die der Server mit dem Item mitschickt - es
 * wird also nichts nachgefragt, nur bereits vorhandene Daten dargestellt.</p>
 */
public class ShulkerPreviewModule extends Module {

	private final IntSetting maxRows =
			add(new IntSetting("max_rows", "Max. Zeilen", "Wie viele Eintraege maximal gezeigt werden", 9, 1, 27));

	private final BoolSetting showCounts =
			add(new BoolSetting("show_counts", "Anzahl anzeigen", "Stapelgroesse hinter dem Namen", true));

	private final BoolSetting compact =
			add(new BoolSetting("compact", "Gleiche zusammenfassen", "Gleiche Items zu einer Zeile buendeln", true));

	public ShulkerPreviewModule() {
		super("shulker_preview", "Shulker Preview", "Inhalt der Shulkerbox im Tooltip", Category.VISUAL);
		markNew();
	}

	/**
	 * Baut die zusaetzlichen Tooltip-Zeilen fuer einen Stack.
	 *
	 * @return Zeilen, oder eine leere Liste wenn das Item keinen Inhalt hat
	 */
	public List<Component> buildTooltipLines(ItemStack stack) {
		ItemContainerContents contents = stack.get(DataComponents.CONTAINER);

		if (contents == null) {
			return List.of();
		}

		List<ItemStack> items = new ArrayList<>();

		for (ItemStack entry : contents.nonEmptyItemCopyStream().toList()) {
			if (compact.value()) {
				// gleiche Items zu einem Eintrag zusammenfassen
				ItemStack existing = findMatching(items, entry);

				if (existing != null) {
					existing.grow(entry.getCount());
					continue;
				}

				items.add(entry.copy());
			} else {
				items.add(entry);
			}
		}

		if (items.isEmpty()) {
			return List.of();
		}

		List<Component> lines = new ArrayList<>();
		int limit = Math.min(items.size(), maxRows.value());

		for (int i = 0; i < limit; i++) {
			ItemStack entry = items.get(i);

			String label = showCounts.value()
					? entry.getCount() + "x " + entry.getHoverName().getString()
					: entry.getHoverName().getString();

			lines.add(Component.literal("  " + label));
		}

		int remaining = items.size() - limit;

		if (remaining > 0) {
			lines.add(Component.literal("  ... und " + remaining + " weitere"));
		}

		return lines;
	}

	private ItemStack findMatching(List<ItemStack> items, ItemStack candidate) {
		for (ItemStack existing : items) {
			if (ItemStack.isSameItemSameComponents(existing, candidate)) {
				return existing;
			}
		}

		return null;
	}
}
