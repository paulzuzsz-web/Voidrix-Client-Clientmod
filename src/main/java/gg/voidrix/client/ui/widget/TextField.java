package gg.voidrix.client.ui.widget;

import gg.voidrix.client.ui.Draw;
import gg.voidrix.client.ui.Theme;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.lwjgl.glfw.GLFW;

/**
 * Schlankes Textfeld im Voidrix-Design.
 *
 * <p>Bewusst eine Eigenentwicklung statt Vanillas {@code EditBox}: So passt
 * die Optik (gekappte Ecke, Glow-Kontur) zum restlichen Menue und der Zustand
 * laesst sich einfacher von aussen steuern.</p>
 */
public class TextField {

	private final String placeholder;
	private final int maxLength;

	private String value = "";
	private boolean focused;

	/** Position des Schreibcursors innerhalb des Textes. */
	private int caret;

	public int x;
	public int y;
	public int width;
	public int height;

	/** Wird bei jeder Aenderung aufgerufen (z.B. um die Suche zu aktualisieren). */
	private Runnable changeListener = () -> {
	};

	/** Wird bei Enter aufgerufen. */
	private Runnable submitListener = () -> {
	};

	public TextField(String placeholder, int maxLength) {
		this.placeholder = placeholder;
		this.maxLength = maxLength;
	}

	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String newValue) {
		this.value = newValue == null ? "" : newValue;

		if (this.value.length() > maxLength) {
			this.value = this.value.substring(0, maxLength);
		}

		this.caret = this.value.length();
		changeListener.run();
	}

	public void clear() {
		value = "";
		caret = 0;
		changeListener.run();
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean value) {
		this.focused = value;
	}

	public TextField onChange(Runnable listener) {
		this.changeListener = listener;
		return this;
	}

	public TextField onSubmit(Runnable listener) {
		this.submitListener = listener;
		return this;
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	/** @return true, wenn der Klick das Feld getroffen hat */
	public boolean mouseClicked(double mouseX, double mouseY) {
		boolean hit = Draw.isHovered(mouseX, mouseY, x, y, width, height);
		focused = hit;
		return hit;
	}

	/** @return true, wenn die Taste verarbeitet wurde */
	public boolean keyPressed(int keyCode) {
		if (!focused) {
			return false;
		}

		switch (keyCode) {
			case GLFW.GLFW_KEY_BACKSPACE -> {
				if (caret > 0) {
					value = value.substring(0, caret - 1) + value.substring(caret);
					caret--;
					changeListener.run();
				}

				return true;
			}

			case GLFW.GLFW_KEY_DELETE -> {
				if (caret < value.length()) {
					value = value.substring(0, caret) + value.substring(caret + 1);
					changeListener.run();
				}

				return true;
			}

			case GLFW.GLFW_KEY_LEFT -> {
				caret = Math.max(0, caret - 1);
				return true;
			}

			case GLFW.GLFW_KEY_RIGHT -> {
				caret = Math.min(value.length(), caret + 1);
				return true;
			}

			case GLFW.GLFW_KEY_HOME -> {
				caret = 0;
				return true;
			}

			case GLFW.GLFW_KEY_END -> {
				caret = value.length();
				return true;
			}

			case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
				submitListener.run();
				return true;
			}

			default -> {
				return false;
			}
		}
	}

	/** Nimmt ein eingetipptes Zeichen entgegen. */
	public boolean charTyped(char character) {
		if (!focused || value.length() >= maxLength) {
			return false;
		}

		// Steuerzeichen ignorieren
		if (character < ' ' || character == 127) {
			return false;
		}

		value = value.substring(0, caret) + character + value.substring(caret);
		caret++;
		changeListener.run();
		return true;
	}

	// ---------------------------------------------------------------
	// Darstellung
	// ---------------------------------------------------------------

	public void render(GuiGraphicsExtractor g, double mouseX, double mouseY) {
		boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, height);

		Draw.cutCornerRect(g, x, y, width, height, 4, Theme.SURFACE_HIGH);

		// Kontur: im Fokus in der Akzentfarbe, sonst dezent
		int borderColor = focused
				? Theme.withAlpha(Theme.ACCENT, 200)
				: Theme.withAlpha(hovered ? Theme.ACCENT : Theme.TEXT_MUTED, 90);

		Draw.cutCornerOutline(g, x, y, width, height, 4, borderColor);

		int textY = y + (height - 8) / 2;
		boolean empty = value.isEmpty();

		String shown = empty && !focused ? placeholder : value;
		int color = empty && !focused ? Theme.TEXT_MUTED : Theme.TEXT;

		// Text ggf. kuerzen, damit er nicht aus dem Feld laeuft
		String visible = Draw.truncate(shown, width - 12);
		Draw.textFlat(g, visible, x + 6, textY, color);

		// blinkender Cursor
		if (focused && (System.currentTimeMillis() / 500) % 2 == 0) {
			int caretX = x + 6 + Draw.textWidth(value.substring(0, Math.min(caret, value.length())));
			Draw.rect(g, Math.min(caretX, x + width - 4), textY - 1, 1, 10, Theme.ACCENT_2);
		}
	}
}
