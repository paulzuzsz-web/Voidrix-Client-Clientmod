package gg.voidrix.client.util;

import net.minecraft.client.Minecraft;

/**
 * Schaetzt die Server-TPS rein clientseitig.
 *
 * <p>Der Vanilla-Server verraet dem Client seine TPS nicht. Voidrix misst
 * deshalb, wie schnell die <b>Weltzeit</b> voranschreitet: Der Server erhoeht
 * {@code level.getGameTime()} genau einmal pro Server-Tick. Vergleicht man den
 * Zuwachs mit der echt vergangenen Zeit, ergibt sich die TPS-Schaetzung.</p>
 *
 * <p>Das ist bewusst passiv - es wird kein Paket an den Server geschickt, der
 * Mod bleibt damit auf jedem Server unauffaellig.</p>
 */
public final class TpsTracker {

	/** Glaettung, damit der Wert nicht zappelt (0..1, hoeher = traeger). */
	private static final double SMOOTHING = 0.85;

	private static long lastGameTime = -1L;
	private static long lastWallClock = 0L;

	private static double smoothedTps = 20.0;

	private TpsTracker() {
	}

	/** Muss einmal pro Client-Tick aufgerufen werden. */
	public static void tick() {
		Minecraft client = Minecraft.getInstance();

		if (client.level == null) {
			reset();
			return;
		}

		long gameTime = client.level.getGameTime();
		long now = System.currentTimeMillis();

		if (lastGameTime < 0) {
			lastGameTime = gameTime;
			lastWallClock = now;
			return;
		}

		long tickDelta = gameTime - lastGameTime;
		long timeDelta = now - lastWallClock;

		// Nur ueber ein halbwegs langes Fenster messen, sonst rauscht es stark.
		if (timeDelta < 500L) {
			return;
		}

		lastGameTime = gameTime;
		lastWallClock = now;

		if (tickDelta <= 0) {
			// Weltzeit steht still (z.B. Singleplayer-Pause) - nicht werten.
			return;
		}

		double measured = tickDelta * 1000.0 / timeDelta;

		// unrealistische Ausreisser abfangen
		measured = Math.max(0.0, Math.min(20.0, measured));

		smoothedTps = smoothedTps * SMOOTHING + measured * (1.0 - SMOOTHING);
	}

	public static double getTps() {
		return smoothedTps;
	}

	public static void reset() {
		lastGameTime = -1L;
		lastWallClock = 0L;
		smoothedTps = 20.0;
	}
}
