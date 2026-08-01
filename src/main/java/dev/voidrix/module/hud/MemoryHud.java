package dev.voidrix.module.hud;

import dev.voidrix.setting.EnumSetting;
import dev.voidrix.ui.Theme;

/** Java heap usage, so you can see a memory leak coming before it stutters. */
public final class MemoryHud extends SimpleHudModule {
    private final EnumSetting<Display> display;

    public MemoryHud() {
        super("memory", "Memory", "How much of the allocated heap is in use", 1.0, 0.30);
        this.display = addEnum("display", "Display", "What to show", Display.PERCENT);
    }

    private static long usedBytes() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static long maxBytes() {
        return Runtime.getRuntime().maxMemory();
    }

    private static float usedFraction() {
        long max = maxBytes();
        return max <= 0L ? 0f : (float) usedBytes() / max;
    }

    @Override
    protected String label() {
        return "Mem";
    }

    @Override
    protected String value() {
        long usedMb = usedBytes() / 1_048_576L;
        long maxMb = maxBytes() / 1_048_576L;
        return switch (display.value()) {
            case PERCENT -> Math.round(usedFraction() * 100f) + "%";
            case MEGABYTES -> usedMb + "MB";
            case BOTH -> Math.round(usedFraction() * 100f) + "% (" + usedMb + "/" + maxMb + "MB)";
        };
    }

    @Override
    protected int valueColor() {
        float used = usedFraction();
        if (used >= 0.9f) {
            return Theme.DANGER;
        }
        if (used >= 0.75f) {
            return Theme.WARN;
        }
        return super.valueColor();
    }

    public enum Display {
        PERCENT,
        MEGABYTES,
        BOTH
    }
}
