package gg.voidrix.client.module.visual;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

/**
 * Randloses Vollbild: das Fenster wird auf Bildschirmgroesse gezogen und die
 * Fensterdekoration entfernt.
 *
 * <p>Vorteil gegenueber dem echten Vollbild: Alt-Tab ist sofort, ohne dass der
 * Bildschirmmodus umgeschaltet wird.</p>
 *
 * <p>Arbeitet direkt mit GLFW auf dem Fenster-Handle - beruehrt also nichts am
 * Spielzustand.</p>
 */
public class BorderlessFullscreenModule extends Module {

	private final BoolSetting alwaysOnTop =
			add(new BoolSetting("always_on_top", "Immer im Vordergrund", "Fenster ueber anderen halten", false));

	/** Fenstergeometrie vor dem Umschalten, um sie wiederherstellen zu koennen. */
	private int savedX;
	private int savedY;
	private int savedWidth;
	private int savedHeight;
	private boolean hasSavedState;

	public BorderlessFullscreenModule() {
		super("borderless", "Borderless Fullscreen", "Randloses Fenster in Bildschirmgroesse", Category.VISUAL);
	}

	@Override
	public void onEnable() {
		long handle = Minecraft.getInstance().getWindow().handle();

		if (handle == 0L) {
			return;
		}

		try {
			saveWindowState(handle);

			long monitor = GLFW.glfwGetPrimaryMonitor();
			GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);

			if (mode == null) {
				return;
			}

			GLFW.glfwSetWindowAttrib(handle, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
			GLFW.glfwSetWindowAttrib(handle, GLFW.GLFW_FLOATING,
					alwaysOnTop.value() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

			GLFW.glfwSetWindowPos(handle, 0, 0);
			GLFW.glfwSetWindowSize(handle, mode.width(), mode.height());
		} catch (Exception e) {
			Voidrix.LOGGER.error("Randloses Vollbild konnte nicht aktiviert werden", e);
		}
	}

	@Override
	public void onDisable() {
		long handle = Minecraft.getInstance().getWindow().handle();

		if (handle == 0L || !hasSavedState) {
			return;
		}

		try {
			GLFW.glfwSetWindowAttrib(handle, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
			GLFW.glfwSetWindowAttrib(handle, GLFW.GLFW_FLOATING, GLFW.GLFW_FALSE);

			GLFW.glfwSetWindowPos(handle, savedX, savedY);
			GLFW.glfwSetWindowSize(handle, savedWidth, savedHeight);
		} catch (Exception e) {
			Voidrix.LOGGER.error("Fenstermodus konnte nicht wiederhergestellt werden", e);
		}
	}

	/** Merkt sich Position und Groesse des Fensters vor dem Umschalten. */
	private void saveWindowState(long handle) {
		int[] x = new int[1];
		int[] y = new int[1];
		int[] width = new int[1];
		int[] height = new int[1];

		GLFW.glfwGetWindowPos(handle, x, y);
		GLFW.glfwGetWindowSize(handle, width, height);

		savedX = x[0];
		savedY = y[0];
		savedWidth = width[0];
		savedHeight = height[0];
		hasSavedState = true;
	}
}
