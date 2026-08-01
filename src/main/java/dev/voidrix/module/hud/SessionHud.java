package dev.voidrix.module.hud;

/** How long the game has been running this session. */
public final class SessionHud extends SimpleHudModule {
    private final long startedAt = System.currentTimeMillis();

    public SessionHud() {
        super("session", "Session", "Time since you launched the game", 1.0, 0.25);
    }

    @Override
    protected String label() {
        return "Session";
    }

    @Override
    protected String value() {
        long seconds = (System.currentTimeMillis() - startedAt) / 1000L;
        long hours = seconds / 3600L;
        long minutes = (seconds % 3600L) / 60L;
        long secs = seconds % 60L;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        }
        return String.format("%d:%02d", minutes, secs);
    }
}
