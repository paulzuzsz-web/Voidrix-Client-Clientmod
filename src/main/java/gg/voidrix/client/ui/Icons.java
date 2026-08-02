package gg.voidrix.client.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Kleine Piktogramme, aus Rechtecken zusammengesetzt.
 *
 * <p>Bewusst ohne Texturen: So bleibt der Mod eine einzelne JAR ohne
 * Ressourcenpaket, und die Icons lassen sich frei einfaerben (z.B. violett,
 * sobald ein Modul aktiv ist).</p>
 *
 * <h2>Neues Icon ergaenzen</h2>
 * Eine Konstante zu {@link Icon} hinzufuegen und in {@link #draw} einen
 * {@code case} dafuer schreiben. Die Zeichenflaeche ist immer quadratisch;
 * alle Koordinaten sind relativ zur linken oberen Ecke.
 */
public final class Icons {

	/** Verfuegbare Piktogramme. */
	public enum Icon {
		LIGHTNING,
		CAMERA,
		SHIRT,
		FRIENDS,
		EMOTE,
		CURSOR,
		GAUGE,
		SIGNAL,
		COMPASS,
		HEART,
		CROSSHAIR,
		EYE,
		SUN,
		DROPLET,
		CUBE,
		MAP,
		SPARKLE,
		WINDOW,
		CHAT,
		GEAR
	}

	private Icons() {
	}

	/**
	 * Zeichnet ein Piktogramm.
	 *
	 * @param size Kantenlaenge der quadratischen Zeichenflaeche (10-16 sinnvoll)
	 */
	public static void draw(GuiGraphicsExtractor g, Icon icon, int x, int y, int size, int color) {
		int h = size / 2;          // Mitte
		int q = Math.max(1, size / 4);
		int t = Math.max(1, size / 8);   // Strichstaerke

		switch (icon) {
			case LIGHTNING -> {
				// Blitz: zwei versetzte Schraegen
				for (int i = 0; i < h; i++) {
					Draw.rect(g, x + h - i / 2, y + i, t + 1, 1, color);
				}
				for (int i = 0; i < h; i++) {
					Draw.rect(g, x + h - 1 - i / 2 + q, y + h + i, t + 1, 1, color);
				}
				Draw.rect(g, x + q, y + h - 1, h, 2, color);
			}

			case CAMERA -> {
				Draw.roundedOutline(g, x, y + q, size, size - q * 2, 2, color);
				Draw.rect(g, x + h - q, y + q - t, q * 2, t, color);
				Draw.roundedOutline(g, x + h - q + 1, y + h - q + 1, q * 2 - 2, q * 2 - 2, 2, color);
			}

			case SHIRT -> {
				// T-Shirt: Schultern plus Rumpf
				Draw.rect(g, x, y + q, size, t + 1, color);
				Draw.rect(g, x + q, y + q, t, size - q, color);
				Draw.rect(g, x + size - q - t, y + q, t, size - q, color);
				Draw.rect(g, x + q, y + size - t, size - q * 2, t, color);
			}

			case FRIENDS -> {
				// zwei Koepfe mit Schultern
				Draw.roundedRect(g, x + 1, y + 1, q + 1, q + 1, 1, color);
				Draw.roundedRect(g, x + size - q - 2, y + 2, q, q, 1, color);
				Draw.rect(g, x, y + h + 1, q + 3, t + 1, color);
				Draw.rect(g, x + size - q - 3, y + h + 2, q + 3, t + 1, color);
			}

            case EMOTE -> {
				// tanzende Figur
				Draw.roundedRect(g, x + h - q / 2, y, q, q, 1, color);
				Draw.rect(g, x + h - t / 2, y + q, t + 1, h - 1, color);
				Draw.rect(g, x + 1, y + q + 1, q + 2, t, color);
				Draw.rect(g, x + size - q - 2, y + q + 3, q + 2, t, color);
				Draw.rect(g, x + q, y + size - q, t, q, color);
				Draw.rect(g, x + size - q - 1, y + size - q, t, q, color);
			}

			case CURSOR -> {
				for (int i = 0; i < size - q; i++) {
					Draw.rect(g, x + q, y + i, Math.max(1, i / 2), 1, color);
				}
				Draw.rect(g, x + h, y + size - q - 1, t + 1, q, color);
			}

			case GAUGE -> {
				// Tacho: Bogen plus Zeiger
				Draw.roundedOutline(g, x, y + q, size, size - q, 3, color);
				Draw.rect(g, x + h, y + h - 1, t + 1, q, color);
				Draw.rect(g, x + h - q, y + h - q, q, t, color);
			}

			case SIGNAL -> {
				// aufsteigende Balken
				for (int i = 0; i < 4; i++) {
					int barHeight = q + i * t;
					Draw.rect(g, x + i * (size / 4), y + size - barHeight, t + 1, barHeight, color);
				}
			}

			case COMPASS -> {
				Draw.roundedOutline(g, x, y, size, size, size / 2, color);
				Draw.rect(g, x + h - 1, y + q, 2, h - q, color);
				Draw.rect(g, x + q, y + h - 1, h - q, 2, color);
			}

			case HEART -> {
				Draw.rect(g, x + q, y + q, size - q * 2, t + 1, color);
				for (int i = 0; i < h; i++) {
					Draw.rect(g, x + q + i / 2, y + q + t + i, size - q * 2 - i, 1, color);
				}
			}

			case CROSSHAIR -> {
				Draw.roundedOutline(g, x + t, y + t, size - t * 2, size - t * 2, (size - t * 2) / 2, color);
				Draw.rect(g, x + h, y, t, q, color);
				Draw.rect(g, x + h, y + size - q, t, q, color);
				Draw.rect(g, x, y + h, q, t, color);
				Draw.rect(g, x + size - q, y + h, q, t, color);
			}

			case EYE -> {
				Draw.roundedOutline(g, x, y + q, size, size - q * 2, (size - q * 2) / 2, color);
				Draw.roundedRect(g, x + h - t, y + h - t, t * 2, t * 2, t, color);
			}

			case SUN -> {
				Draw.roundedRect(g, x + q, y + q, size - q * 2, size - q * 2, (size - q * 2) / 2, color);
				Draw.rect(g, x + h, y, t, q - 1, color);
				Draw.rect(g, x + h, y + size - q + 1, t, q - 1, color);
				Draw.rect(g, x, y + h, q - 1, t, color);
				Draw.rect(g, x + size - q + 1, y + h, q - 1, t, color);
			}

			case DROPLET -> {
				for (int i = 0; i < size; i++) {
					int w = Math.min(i, size - i / 2);
					Draw.rect(g, x + h - w / 2, y + i, Math.max(1, w), 1, color);
				}
			}

			case CUBE -> {
				Draw.roundedOutline(g, x, y + q - 1, size, size - q + 1, 1, color);
				Draw.rect(g, x, y + h - 1, size, t, color);
				Draw.rect(g, x + h - 1, y + h, t, h - q + 1, color);
			}

			case MAP -> {
				Draw.roundedOutline(g, x, y, size, size, 1, color);
				Draw.rect(g, x + q, y, t, size, color);
				Draw.rect(g, x + size - q, y, t, size, color);
			}

			case SPARKLE -> {
				Draw.rect(g, x + h - 1, y, 2, size, color);
				Draw.rect(g, x, y + h - 1, size, 2, color);
				Draw.rect(g, x + q, y + q, t, t, color);
				Draw.rect(g, x + size - q, y + size - q, t, t, color);
			}

			case WINDOW -> {
				Draw.roundedOutline(g, x, y + 1, size, size - 2, 1, color);
				Draw.rect(g, x, y + q + 1, size, t, color);
			}

			case CHAT -> {
				Draw.roundedOutline(g, x, y + 1, size, size - q - 1, 2, color);
				Draw.rect(g, x + q, y + size - q, q, q - 1, color);
			}

			case GEAR -> {
				Draw.roundedOutline(g, x + t, y + t, size - t * 2, size - t * 2, (size - t * 2) / 2, color);
				Draw.rect(g, x + h, y, t, t + 1, color);
				Draw.rect(g, x + h, y + size - t - 1, t, t + 1, color);
				Draw.rect(g, x, y + h, t + 1, t, color);
				Draw.rect(g, x + size - t - 1, y + h, t + 1, t, color);
			}
		}
	}

	/**
	 * Ordnet jedem Modul ein Piktogramm zu.
	 * Unbekannte IDs bekommen das Voidrix-Blitzsymbol.
	 */
	public static Icon forModule(String moduleId) {
		return switch (moduleId) {
			case "cps" -> Icon.CURSOR;
			case "fps" -> Icon.GAUGE;
			case "ping" -> Icon.SIGNAL;
			case "coordinates" -> Icon.COMPASS;
			case "tps" -> Icon.GAUGE;
			case "health_indicator" -> Icon.HEART;
			case "chat_heads" -> Icon.CHAT;
			case "item_highlighter" -> Icon.SPARKLE;
			case "custom_crosshair" -> Icon.CROSSHAIR;
			case "block_outlines" -> Icon.CUBE;
			case "no_hurt_cam" -> Icon.EYE;
			case "drop_stack" -> Icon.CUBE;
			case "toggle_sprint" -> Icon.EMOTE;
			case "zoom" -> Icon.EYE;
			case "skin_preview" -> Icon.SHIRT;
			case "shulker_preview" -> Icon.CUBE;
			case "shiny_pots" -> Icon.DROPLET;
			case "wavey_capes" -> Icon.SHIRT;
			case "item_model" -> Icon.CUBE;
			case "particles_filter" -> Icon.SPARKLE;
			case "borderless" -> Icon.WINDOW;
			case "discord_rpc" -> Icon.FRIENDS;
			case "waypoints" -> Icon.MAP;
			default -> Icon.LIGHTNING;
		};
	}
}
