package gg.voidrix.client.waypoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

/** Haelt alle gesetzten Waypoints und speichert sie in der Config. */
public final class WaypointManager {

	private static final List<Waypoint> WAYPOINTS = new ArrayList<>();

	private WaypointManager() {
	}

	public static List<Waypoint> getAll() {
		return WAYPOINTS;
	}

	public static void add(Waypoint waypoint) {
		WAYPOINTS.add(waypoint);
	}

	public static void remove(Waypoint waypoint) {
		WAYPOINTS.remove(waypoint);
	}

	public static void clear() {
		WAYPOINTS.clear();
	}

	/**
	 * Setzt einen Waypoint an der aktuellen Spielerposition.
	 *
	 * @return der neue Waypoint oder {@code null}, wenn kein Spieler existiert
	 */
	public static Waypoint createAtPlayer(String name) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null) {
			return null;
		}

		Waypoint waypoint = new Waypoint(
				name,
				Math.floor(player.getX()) + 0.5,
				Math.floor(player.getY()),
				Math.floor(player.getZ()) + 0.5,
				getCurrentDimension());

		WAYPOINTS.add(waypoint);
		return waypoint;
	}

	/** Kennung der aktuellen Dimension, z.B. {@code minecraft:overworld}. */
	public static String getCurrentDimension() {
		Minecraft client = Minecraft.getInstance();

		if (client.level == null) {
			return "minecraft:overworld";
		}

		return client.level.dimension().identifier().toString();
	}

	/** Alle Waypoints, die in der aktuellen Dimension sichtbar sind. */
	public static List<Waypoint> getVisibleHere() {
		String dimension = getCurrentDimension();
		List<Waypoint> result = new ArrayList<>();

		for (Waypoint waypoint : WAYPOINTS) {
			if (waypoint.isVisible() && waypoint.getDimension().equals(dimension)) {
				result.add(waypoint);
			}
		}

		return result;
	}

	// ---------------------------------------------------------------
	// Persistenz
	// ---------------------------------------------------------------

	public static JsonArray save() {
		JsonArray array = new JsonArray();

		for (Waypoint waypoint : WAYPOINTS) {
			array.add(waypoint.save());
		}

		return array;
	}

	public static void load(JsonArray array) {
		WAYPOINTS.clear();

		if (array == null) {
			return;
		}

		for (JsonElement element : array) {
			if (!element.isJsonObject()) {
				continue;
			}

			Waypoint waypoint = Waypoint.load((JsonObject) element);

			if (waypoint != null) {
				WAYPOINTS.add(waypoint);
			}
		}
	}
}
