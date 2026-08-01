package dev.voidrix.module.hud;

import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.EnumSetting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Real-world wall clock, for when you have lost track of the evening. */
public final class ClockHud extends SimpleHudModule {
    private static final DateTimeFormatter H24 = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter H24_SECONDS = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter H12 = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter H12_SECONDS = DateTimeFormatter.ofPattern("hh:mm:ss a");

    private final EnumSetting<Format> format;
    private final BoolSetting seconds;

    public ClockHud() {
        super("clock", "Clock", "Your computer's clock", 1.0, 0.20);
        this.format = addEnum("format", "Format", "12 or 24 hour", Format.H24);
        this.seconds = addBool("seconds", "Show seconds", "Include seconds in the time", false);
    }

    @Override
    protected String label() {
        return "Time";
    }

    @Override
    protected String value() {
        DateTimeFormatter fmt = format.value() == Format.H24
                ? (seconds.value() ? H24_SECONDS : H24)
                : (seconds.value() ? H12_SECONDS : H12);
        return LocalTime.now().format(fmt);
    }

    public enum Format {
        H24,
        H12
    }
}
