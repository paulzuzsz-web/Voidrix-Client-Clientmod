package gg.voidrix.client.module.misc;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.IntSetting;
import gg.voidrix.client.ui.Draw;
import gg.voidrix.client.ui.Theme;
import gg.voidrix.client.waypoint.Waypoint;
import gg.voidrix.client.waypoint.WaypointManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.phys.Vec3;

/**
 * Zeichnet gesetzte Waypoints im Spiel.
 *
 * <p>Die Marker werden nicht in die 3D-Szene gezeichnet, sondern ihre
 * Weltposition wird selbst auf den Bildschirm projiziert und als HUD-Element
 * dargestellt. Das ist deutlich einfacher als ein eigener Render-Layer und
 * kommt ohne Eingriff in die Weltdarstellung aus.</p>
 */
public class WaypointsModule extends Module {

	private final BoolSetting showDistance =
			add(new BoolSetting("show_distance", "Entfernung", "Entfernung unter dem Namen anzeigen", true));

	private final IntSetting maxDistance =
			add(new IntSetting("max_distance", "Sichtweite", "Nur Waypoints naeher als (Bloecke)", 512, 32, 4096));

	private final BoolSetting clampToEdge =
			add(new BoolSetting("clamp", "Am Rand halten", "Marker ausserhalb des Bildes am Rand anzeigen", true));

	private final IntSetting markerSize =
			add(new IntSetting("marker_size", "Markergroesse", "Groesse der Raute", 5, 3, 12));

	public WaypointsModule() {
		super("waypoints", "Waypoints", "Punkte setzen, benennen und anzeigen", Category.MISC);
		defaultOn();
		markNew();
	}

	/** Zeichnet alle sichtbaren Waypoints. Vom HUD-Element aufgerufen. */
	public void render(GuiGraphicsExtractor g) {
		Minecraft client = Minecraft.getInstance();

		if (client.player == null || client.level == null) {
			return;
		}

		Camera camera = client.gameRenderer.mainCamera();
		Vec3 cameraPos = camera.position();

		int screenWidth = g.guiWidth();
		int screenHeight = g.guiHeight();

		for (Waypoint waypoint : WaypointManager.getVisibleHere()) {
			Vec3 target = new Vec3(waypoint.getX(), waypoint.getY(), waypoint.getZ());
			double distance = cameraPos.distanceTo(target);

			if (distance > maxDistance.value()) {
				continue;
			}

			float[] projected = project(camera, cameraPos, target, screenWidth, screenHeight);

			if (projected == null) {
				continue;
			}

			float x = projected[0];
			float y = projected[1];
			boolean behind = projected[2] < 0f;

			if (behind || x < 0 || x > screenWidth || y < 0 || y > screenHeight) {
				if (!clampToEdge.value()) {
					continue;
				}

				// Marker am Bildschirmrand festhalten
				x = Math.max(8, Math.min(screenWidth - 8, x));
				y = Math.max(8, Math.min(screenHeight - 8, y));

				if (behind) {
					// hinter uns: an den unteren Rand spiegeln
					y = screenHeight - 12;
					x = screenWidth - x;
				}
			}

			drawMarker(g, waypoint, Math.round(x), Math.round(y), distance);
		}
	}

	private void drawMarker(GuiGraphicsExtractor g, Waypoint waypoint, int x, int y, double distance) {
		int size = markerSize.value();

		// Raute aus zeilenweise schmaler werdenden Strichen
		for (int i = 0; i < size; i++) {
			int halfWidth = size - i;
			Draw.rect(g, x - halfWidth, y - size + i, halfWidth * 2, 1, waypoint.getColor());
			Draw.rect(g, x - halfWidth, y + size - i - 1, halfWidth * 2, 1, waypoint.getColor());
		}

		String name = waypoint.getName();
		Draw.textCentered(g, name, x, y + size + 2, Theme.HUD_TEXT);

		if (showDistance.value()) {
			String label = Math.round(distance) + "m";
			Draw.textCentered(g, label, x, y + size + 12, Theme.withAlpha(Theme.HUD_TEXT, 180));
		}
	}

	/**
	 * Projiziert eine Weltposition auf Bildschirmkoordinaten.
	 *
	 * <p>Statt die Projektionsmatrix aus dem Renderer zu holen, wird hier
	 * direkt gerechnet: Die Zielrichtung wird in das Kamerasystem gedreht
	 * (Yaw/Pitch) und dann perspektivisch geteilt.</p>
	 *
	 * @return {@code [screenX, screenY, tiefe]} - Tiefe < 0 heisst "hinter der
	 *         Kamera" - oder {@code null}, wenn die Projektion entartet
	 */
	private float[] project(Camera camera, Vec3 cameraPos, Vec3 target, int screenWidth, int screenHeight) {
		double dx = target.x - cameraPos.x;
		double dy = target.y - cameraPos.y;
		double dz = target.z - cameraPos.z;

		double yaw = Math.toRadians(camera.yRot());
		double pitch = Math.toRadians(camera.xRot());

		// In Minecraft zeigt Yaw 0 nach +Z, positive Drehung nach links.
		double cosYaw = Math.cos(yaw);
		double sinYaw = Math.sin(yaw);

		// um die Y-Achse zurueckdrehen
		double rx = dx * cosYaw - dz * sinYaw;
		double rz = dx * sinYaw + dz * cosYaw;

		// um die X-Achse zurueckdrehen (Pitch)
		double cosPitch = Math.cos(pitch);
		double sinPitch = Math.sin(pitch);

		double ry = dy * cosPitch + rz * sinPitch;
		double depth = rz * cosPitch - dy * sinPitch;

		if (Math.abs(depth) < 1.0E-4) {
			return null;
		}

		// Perspektivische Teilung mit dem aktuellen Sichtfeld
		double fov = Math.toRadians(camera.getFov());
		double focal = (screenHeight / 2.0) / Math.tan(fov / 2.0);

		float screenX = (float) (screenWidth / 2.0 - rx / depth * focal);
		float screenY = (float) (screenHeight / 2.0 - ry / depth * focal);

		return new float[] { screenX, screenY, (float) depth };
	}
}
