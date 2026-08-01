package dev.voidrix.module.misc;

import dev.voidrix.VoidrixClient;
import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.DoubleSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.ui.Draw;
import dev.voidrix.ui.Theme;
import dev.voidrix.waypoint.Waypoint;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Draws your waypoints over the world.
 *
 * <p>Minecraft gives us no way to ask "where would this block appear on screen", so the marker
 * positions are worked out here: the offset from the camera is rotated into camera space using the
 * camera's own yaw and pitch, then divided by depth against the current field of view. Markers that
 * fall outside the viewport - including ones behind you - are pinned to the nearest edge with an
 * arrow, so a waypoint is never simply lost.
 */
public final class WaypointsModule extends Module {
    private final BoolSetting showName;
    private final BoolSetting showDistance;
    private final BoolSetting showOffscreen;
    private final IntSetting maxDistance;
    private final DoubleSetting markerScale;

    public WaypointsModule() {
        super("waypoints", "Waypoints", "Mark places and see them through the world", Category.MISC);
        this.showName = addBool("show_name", "Show name", "Print the waypoint's name", true);
        this.showDistance = addBool("show_distance", "Show distance", "Print how far away it is", true);
        this.showOffscreen = addBool("show_offscreen", "Edge arrows",
                "Pin markers that are off screen to the nearest edge", true);
        this.maxDistance = addInt("max_distance", "Render distance",
                "Hide markers further away than this, 0 for no limit", 0, 0, 5000, "m");
        this.markerScale = addDouble("scale", "Marker scale", "Size of the marker", 1.0, 0.5, 2.5, 2, "x");
    }

    @Override
    public boolean enabledByDefault() {
        return true;
    }

    /**
     * Projects a world position into GUI space.
     *
     * @return {@code {screenX, screenY, depth}}, where a depth at or below zero means the point is
     *         behind the camera; or null when there is no camera to project against
     */
    private static double[] project(double wx, double wy, double wz, Camera camera,
                                    int screenW, int screenH) {
        var cam = camera.position();
        double dx = wx - cam.x;
        double dy = wy - cam.y;
        double dz = wz - cam.z;

        double yaw = Math.toRadians(camera.yRot());
        double pitch = Math.toRadians(camera.xRot());
        double cosPitch = Math.cos(pitch);

        // Minecraft's look vector for a given yaw/pitch.
        double fx = -Math.sin(yaw) * cosPitch;
        double fy = -Math.sin(pitch);
        double fz = Math.cos(yaw) * cosPitch;

        // right = forward x worldUp, then up = right x forward.
        double rx = -fz;
        double rz = fx;
        double rLen = Math.sqrt(rx * rx + rz * rz);
        if (rLen < 1.0e-6) {
            // Looking straight up or down: the basis is degenerate, so fall back to the yaw alone.
            rx = Math.cos(yaw);
            rz = Math.sin(yaw);
            rLen = 1.0;
        }
        rx /= rLen;
        rz /= rLen;

        double ux = -rz * fy;
        double uy = rz * fx - rx * fz;
        double uz = rx * fy;

        double xc = dx * rx + dz * rz;
        double yc = dx * ux + dy * uy + dz * uz;
        double zc = dx * fx + dy * fy + dz * fz;

        double focal = (screenH / 2.0) / Math.tan(Math.toRadians(camera.getFov()) / 2.0);
        double sx = screenW / 2.0 + (xc / zc) * focal;
        double sy = screenH / 2.0 - (yc / zc) * focal;
        return new double[]{sx, sy, zc, xc};
    }

    /** Draws every visible waypoint. Called from the HUD pass. */
    public void render(GuiGraphicsExtractor g, Font font) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.gameRenderer == null) {
            return;
        }
        Camera camera = mc.gameRenderer.mainCamera();
        if (camera == null) {
            return;
        }

        int screenW = g.guiWidth();
        int screenH = g.guiHeight();
        float scale = markerScale.floatValue();
        int limit = maxDistance.value();

        for (Waypoint waypoint : VoidrixClient.waypoints().active()) {
            double wx = waypoint.x() + 0.5;
            double wy = waypoint.y() + 0.5;
            double wz = waypoint.z() + 0.5;

            double distance = mc.player.position().distanceTo(new net.minecraft.world.phys.Vec3(wx, wy, wz));
            if (limit > 0 && distance > limit) {
                continue;
            }

            double[] p = project(wx, wy, wz, camera, screenW, screenH);
            double sx = p[0];
            double sy = p[1];
            double depth = p[2];
            double sideways = p[3];

            boolean behind = depth <= 0.05;
            if (behind) {
                // Behind the camera the projection flips; place it on the correct side instead.
                sx = sideways >= 0 ? screenW * 2.0 : -screenW;
                sy = screenH / 2.0;
            }

            float margin = 10f;
            boolean offscreen = behind
                    || sx < margin || sx > screenW - margin
                    || sy < margin || sy > screenH - margin;

            if (offscreen && !showOffscreen.value()) {
                continue;
            }

            float x = (float) Math.clamp(sx, margin, screenW - margin);
            float y = (float) Math.clamp(sy, margin, screenH - margin);

            drawMarker(g, font, waypoint, x, y, distance, offscreen, scale);
        }
    }

    private void drawMarker(GuiGraphicsExtractor g, Font font, Waypoint waypoint,
                            float x, float y, double distance, boolean offscreen, float scale) {
        int color = waypoint.color();

        var pose = g.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(scale, scale);

        // The marker itself: a filled dot with a soft halo so it reads against any terrain.
        Draw.glow(g, -3.5f, -3.5f, 7, 7, 3.5f, 4f, color, 0.5f);
        Draw.circle(g, 0, 0, 3.5f, color);
        Draw.circle(g, 0, 0, 1.5f, 0xFFFFFFFF);

        if (offscreen) {
            // A ring marks "this one is not really here" without needing a separate arrow sprite.
            Draw.roundRectOutline(g, -6.5f, -6.5f, 13, 13, 6.5f, 1f, Theme.alpha(color, 0.85f));
        }

        String label = showName.value() ? waypoint.name() : "";
        String below = showDistance.value() ? Math.round(distance) + "m" : "";

        if (!label.isEmpty()) {
            float w = font.width(label) + 6;
            Draw.roundRect(g, -w / 2f, -18, w, 11, Theme.RADIUS_SM, 0xB80B0B12);
            Draw.textCentered(g, font, label, 0, -16, Theme.TEXT);
        }
        if (!below.isEmpty()) {
            float w = font.width(below) + 6;
            Draw.roundRect(g, -w / 2f, 7, w, 11, Theme.RADIUS_SM, 0xB80B0B12);
            Draw.textCentered(g, font, below, 0, 9, Theme.alpha(color, 1f));
        }

        pose.popMatrix();
    }
}
