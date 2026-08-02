package gg.voidrix.client.waypoint;

import com.google.gson.JsonObject;
import gg.voidrix.client.ui.Theme;

/**
 * Ein benannter Punkt in der Welt.
 *
 * <p>Waypoints werden rein lokal in {@code config/voidrix.json} gespeichert -
 * es geht nichts an den Server.</p>
 */
public class Waypoint {

	private String name;
	private double x;
	private double y;
	private double z;
	private int color;

	/** Dimension, in der der Punkt gilt (z.B. {@code minecraft:overworld}). */
	private String dimension;

	private boolean visible;

	public Waypoint(String name, double x, double y, double z, String dimension) {
		this(name, x, y, z, dimension, Theme.ACCENT);
	}

	public Waypoint(String name, double x, double y, double z, String dimension, int color) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
		this.color = color;
		this.visible = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public JsonObject save() {
		JsonObject json = new JsonObject();
		json.addProperty("name", name);
		json.addProperty("x", x);
		json.addProperty("y", y);
		json.addProperty("z", z);
		json.addProperty("dimension", dimension);
		json.addProperty("color", String.format("#%08X", color));
		json.addProperty("visible", visible);
		return json;
	}

	public static Waypoint load(JsonObject json) {
		if (json == null || !json.has("name")) {
			return null;
		}

		int parsedColor = Theme.ACCENT;

		if (json.has("color")) {
			try {
				String raw = json.get("color").getAsString().replace("#", "");
				long value = Long.parseLong(raw, 16);

				if (raw.length() <= 6) {
					value |= 0xFF000000L;
				}

				parsedColor = (int) value;
			} catch (NumberFormatException ignored) {
				// unlesbare Farbe -> Standardfarbe
			}
		}

		Waypoint waypoint = new Waypoint(
				json.get("name").getAsString(),
				json.has("x") ? json.get("x").getAsDouble() : 0,
				json.has("y") ? json.get("y").getAsDouble() : 0,
				json.has("z") ? json.get("z").getAsDouble() : 0,
				json.has("dimension") ? json.get("dimension").getAsString() : "minecraft:overworld",
				parsedColor);

		if (json.has("visible")) {
			waypoint.setVisible(json.get("visible").getAsBoolean());
		}

		return waypoint;
	}
}
