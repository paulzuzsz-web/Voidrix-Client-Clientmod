package gg.voidrix.client.feature;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.VoidrixConfig;

/**
 * Zoom auf Tastendruck. Der Faktor wird pro Tick auf den Zielwert zubewegt, damit der
 * Uebergang nicht springt; {@link #modifyFov(float)} wird vom Camera-Mixin aufgerufen.
 */
public final class Zoom {

    /** Anteil der Reststrecke, die pro Tick zurueckgelegt wird. */
    private static final double SMOOTHING = 0.35D;
    private static final double SNAP_EPSILON = 1.0E-4D;

    private static boolean active;
    private static double progress;

    private Zoom() {
    }

    public static void tick(boolean keyDown) {
        VoidrixConfig config = Voidrix.config();
        active = config.zoomEnabled && keyDown;

        double target = active ? 1.0D : 0.0D;
        if (!config.zoomSmooth) {
            progress = target;
            return;
        }

        progress += (target - progress) * SMOOTHING;
        if (Math.abs(target - progress) < SNAP_EPSILON) {
            progress = target;
        }
    }

    /** Wird aus dem Mixin heraus auf den von Vanilla berechneten FOV angewendet. */
    public static float modifyFov(float fov) {
        if (progress <= 0.0D) {
            return fov;
        }
        double divisor = Voidrix.config().zoomDivisor;
        // Zwischen 1.0 (kein Zoom) und 1/divisor (voller Zoom) interpolieren.
        double factor = 1.0D + progress * (1.0D / divisor - 1.0D);
        return (float) (fov * factor);
    }

    public static boolean isZooming() {
        return active || progress > 0.0D;
    }
}
