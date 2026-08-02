package gg.voidrix.client.module.pvp;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Dauersprint: einmal Sprint druecken, statt die Taste zu halten.
 *
 * <p>Setzt lediglich das Sprint-Flag des eigenen Spielers - genau das, was
 * Vanilla beim Halten der Taste auch tut. Es werden keine zusaetzlichen Pakete
 * erzeugt und keine Bewegungsdaten manipuliert, deshalb ist das Modul auf
 * Servern unproblematisch.</p>
 */
public class ToggleSprintModule extends Module {

	private final BoolSetting alwaysSprint =
			add(new BoolSetting("always", "Immer sprinten", "Dauerhaft sprinten, sobald man laeuft", true));

	private final BoolSetting keepOnHunger =
			add(new BoolSetting("keep_on_hunger", "Bei Hunger halten",
					"Auch weiter versuchen, wenn der Hunger zu niedrig ist", false));

	public ToggleSprintModule() {
		super("toggle_sprint", "Toggle Sprint", "Sprinten ohne Taste zu halten", Category.PVP);
	}

	@Override
	public void onTick() {
		if (!alwaysSprint.value()) {
			return;
		}

		Minecraft client = Minecraft.getInstance();
		LocalPlayer player = client.player;

		if (player == null || client.gui.screen() != null) {
			return;
		}

		// Vanilla verlangt mindestens 6 Hungerpunkte zum Sprinten.
		if (!keepOnHunger.value() && player.getFoodData().getFoodLevel() <= 6) {
			return;
		}

		// Nur sprinten, wenn der Spieler sich ueberhaupt vorwaerts bewegt
		// und nicht schleicht - sonst wuerde man z.B. im Wasser zappeln.
		boolean movingForward = player.input.getMoveVector().y > 0;

		if (movingForward && !player.isCrouching() && !player.isSprinting()) {
			player.setSprinting(true);
		}
	}

	@Override
	public void onDisable() {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player != null) {
			player.setSprinting(false);
		}
	}
}
