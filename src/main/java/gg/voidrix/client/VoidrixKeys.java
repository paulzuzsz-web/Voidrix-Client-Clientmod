package gg.voidrix.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

/**
 * Alle Tastenbelegungen des Mods.
 *
 * <p>Die Tasten tauchen in den Minecraft-Optionen unter der eigenen Kategorie
 * "Voidrix" auf und lassen sich dort frei aendern.</p>
 */
public final class VoidrixKeys {

	/** Eigene Kategorie in den Steuerungs-Optionen. */
	public static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Voidrix.MOD_ID, "main"));

	/** Oeffnet das Voidrix-Menue - Standard: rechte Shift-Taste. */
	public static final KeyMapping OPEN_MENU = new KeyMapping(
			"key.voidrix.open_menu",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_RSHIFT,
			CATEGORY);

	/** Oeffnet den HUD-Editor - Standard: rechte Strg-Taste. */
	public static final KeyMapping OPEN_HUD_EDITOR = new KeyMapping(
			"key.voidrix.hud_editor",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_RCONTROL,
			CATEGORY);

	/** Zoom - Standard: C. */
	public static final KeyMapping ZOOM = new KeyMapping(
			"key.voidrix.zoom",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_C,
			CATEGORY);

	/** Setzt einen Waypoint an der aktuellen Position - Standard: B. */
	public static final KeyMapping ADD_WAYPOINT = new KeyMapping(
			"key.voidrix.add_waypoint",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_B,
			CATEGORY);

	private VoidrixKeys() {
	}

	/** Meldet alle Tasten bei Minecraft an. Wird beim Client-Start aufgerufen. */
	public static void register() {
		KeyMappingHelper.registerKeyMapping(OPEN_MENU);
		KeyMappingHelper.registerKeyMapping(OPEN_HUD_EDITOR);
		KeyMappingHelper.registerKeyMapping(ZOOM);
		KeyMappingHelper.registerKeyMapping(ADD_WAYPOINT);
	}
}
