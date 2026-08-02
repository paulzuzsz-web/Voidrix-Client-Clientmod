package gg.voidrix.client.util;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Zaehlt Mausklicks pro Sekunde.
 *
 * <p>Gefuettert wird der Tracker vom Mixin auf
 * {@code MouseHandler#onButton} - dadurch werden auch sehr schnelle Klicks
 * exakt erfasst (ein Abfragen im Client-Tick waere auf 20 Hz begrenzt und
 * wuerde bei hohen CPS-Werten Klicks verschlucken).</p>
 */
public final class ClickTracker {

	/** Zeitfenster fuer die CPS-Messung. */
	private static final long WINDOW_MS = 1000L;

	private static final Deque<Long> LEFT_CLICKS = new ArrayDeque<>();
	private static final Deque<Long> RIGHT_CLICKS = new ArrayDeque<>();

	private ClickTracker() {
	}

	public static void onLeftClick() {
		LEFT_CLICKS.addLast(System.currentTimeMillis());
	}

	public static void onRightClick() {
		RIGHT_CLICKS.addLast(System.currentTimeMillis());
	}

	public static int getLeftCps() {
		return count(LEFT_CLICKS);
	}

	public static int getRightCps() {
		return count(RIGHT_CLICKS);
	}

	/** Entfernt alle Klicks ausserhalb des Zeitfensters und zaehlt den Rest. */
	private static int count(Deque<Long> clicks) {
		long threshold = System.currentTimeMillis() - WINDOW_MS;

		while (!clicks.isEmpty() && clicks.peekFirst() < threshold) {
			clicks.removeFirst();
		}

		return clicks.size();
	}
}
