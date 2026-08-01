package dev.voidrix.waypoint;

import com.google.gson.JsonObject;

/**
 * A place you marked.
 *
 * <p>Kept mutable because the edit screen writes straight into it, and stored per dimension so a
 * marker set in the Nether does not float over your overworld base.
 */
public final class Waypoint {
    private String name;
    private int x;
    private int y;
    private int z;
    /** Packed ARGB; alpha is ignored when drawing the marker. */
    private int color;
    /** Dimension id, e.g. {@code minecraft:overworld}. Empty means "show everywhere". */
    private String dimension;
    private boolean visible;

    public Waypoint(String name, int x, int y, int z, int color, String dimension) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.dimension = dimension == null ? "" : dimension;
        this.visible = true;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public void setPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int color() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String dimension() {
        return dimension;
    }

    public boolean visible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void toggleVisible() {
        this.visible = !visible;
    }

    /** Whether this marker belongs in the given dimension. */
    public boolean showsIn(String currentDimension) {
        return dimension.isEmpty() || dimension.equals(currentDimension);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("x", x);
        json.addProperty("y", y);
        json.addProperty("z", z);
        json.addProperty("color", String.format("#%06X", color & 0xFFFFFF));
        json.addProperty("dimension", dimension);
        json.addProperty("visible", visible);
        return json;
    }

    /** Reads one waypoint, or null when the entry is too broken to use. */
    public static Waypoint fromJson(JsonObject json) {
        try {
            String name = json.has("name") ? json.get("name").getAsString() : "Waypoint";
            int x = json.get("x").getAsInt();
            int y = json.get("y").getAsInt();
            int z = json.get("z").getAsInt();

            int color = 0xFF8B5CF6;
            if (json.has("color")) {
                String raw = json.get("color").getAsString().trim();
                if (raw.startsWith("#")) {
                    raw = raw.substring(1);
                }
                color = 0xFF000000 | (int) Long.parseLong(raw, 16);
            }
            String dimension = json.has("dimension") ? json.get("dimension").getAsString() : "";

            Waypoint waypoint = new Waypoint(name, x, y, z, color, dimension);
            if (json.has("visible")) {
                waypoint.setVisible(json.get("visible").getAsBoolean());
            }
            return waypoint;
        } catch (RuntimeException e) {
            return null;
        }
    }
}
