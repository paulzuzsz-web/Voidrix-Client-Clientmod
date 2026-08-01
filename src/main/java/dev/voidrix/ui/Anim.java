package dev.voidrix.ui;

/**
 * A float that chases a target instead of jumping to it.
 *
 * <p>Uses frame-rate independent exponential smoothing, so a hover fades over the same wall-clock
 * time at 30fps and at 240fps. Everything that moves in the Voidrix interface goes through one of
 * these.
 */
public final class Anim {
    private float value;
    private float target;
    private final float tau;

    /** @param seconds roughly how long the value takes to close most of the gap */
    public Anim(float initial, float seconds) {
        this.value = initial;
        this.target = initial;
        this.tau = Math.max(0.0001f, seconds);
    }

    public Anim(float initial) {
        this(initial, Theme.ANIM);
    }

    public void target(float t) {
        this.target = t;
    }

    public float target() {
        return target;
    }

    public float value() {
        return value;
    }

    /** Jumps straight to a value, skipping the transition. */
    public void snap(float v) {
        this.value = v;
        this.target = v;
    }

    /** Advances by {@code dt} seconds and returns the new value. */
    public float update(float dt) {
        float step = 1f - (float) Math.exp(-Math.clamp(dt, 0f, 0.25f) / tau);
        value += (target - value) * step;
        if (Math.abs(target - value) < 0.0005f) {
            value = target;
        }
        return value;
    }

    /** Cubic ease-out, for one-shot transitions like a panel opening. */
    public static float easeOut(float t) {
        float k = 1f - Math.clamp(t, 0f, 1f);
        return 1f - k * k * k;
    }
}
