package gg.voidrix.client.integration;

import com.google.gson.JsonObject;
import gg.voidrix.client.Voidrix;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Schlanke Discord-Rich-Presence-Anbindung <b>ohne externe Bibliothek</b>.
 *
 * <p>Discord stellt lokal einen IPC-Endpunkt bereit: unter Linux/macOS als
 * Unix-Socket ({@code $XDG_RUNTIME_DIR/discord-ipc-0}), unter Windows als
 * Named Pipe ({@code \\.\pipe\discord-ipc-0}). Das Protokoll besteht aus
 * einfachen Frames:</p>
 *
 * <pre>
 *   [ opcode : int32 little endian ][ laenge : int32 little endian ][ JSON ]
 * </pre>
 *
 * <p>Opcode 0 = Handshake, 1 = Nutzdaten (z.B. SET_ACTIVITY), 2 = Schliessen.
 * Java 21 bringt mit {@link UnixDomainSocketAddress} alles Noetige mit, daher
 * kommt Voidrix ohne JNA oder eine native Discord-Bibliothek aus.</p>
 *
 * <p>Alle Fehler werden bewusst verschluckt: Laeuft Discord nicht, soll das
 * Spiel davon nichts merken.</p>
 */
public final class DiscordRpc {

	private static final int OP_HANDSHAKE = 0;
	private static final int OP_FRAME = 1;
	private static final int OP_CLOSE = 2;

	/** Unix-Socket-Verbindung (Linux / macOS). */
	private static SocketChannel channel;

	/** Named-Pipe-Verbindung (Windows). */
	private static RandomAccessFile pipe;

	private static boolean connected;
	private static String applicationId;

	/** Startzeitpunkt fuer die "seit ..."-Anzeige in Discord. */
	private static long startTimestamp;

	private DiscordRpc() {
	}

	public static boolean isConnected() {
		return connected;
	}

	/**
	 * Baut die Verbindung zu Discord auf.
	 *
	 * @param appId Application-ID aus dem Discord-Developer-Portal
	 * @return true, wenn die Verbindung steht
	 */
	public static synchronized boolean connect(String appId) {
		if (connected) {
			return true;
		}

		applicationId = appId;
		startTimestamp = System.currentTimeMillis() / 1000L;

		// Discord probiert der Reihe nach discord-ipc-0 bis discord-ipc-9
		for (int i = 0; i < 10; i++) {
			try {
				if (openTransport(i)) {
					JsonObject handshake = new JsonObject();
					handshake.addProperty("v", 1);
					handshake.addProperty("client_id", applicationId);

					send(OP_HANDSHAKE, handshake.toString());
					connected = true;

					Voidrix.LOGGER.info("Discord Rich Presence verbunden (ipc-{}).", i);
					return true;
				}
			} catch (Exception e) {
				closeQuietly();
			}
		}

		Voidrix.LOGGER.debug("Kein laufender Discord-Client gefunden - Rich Presence bleibt aus.");
		return false;
	}

	/** Oeffnet Socket bzw. Pipe fuer den angegebenen Index. */
	private static boolean openTransport(int index) throws IOException {
		String os = System.getProperty("os.name", "").toLowerCase();

		if (os.contains("win")) {
			Path pipePath = Path.of("\\\\.\\pipe\\discord-ipc-" + index);

			if (!Files.exists(pipePath)) {
				return false;
			}

			pipe = new RandomAccessFile(pipePath.toString(), "rw");
			return true;
		}

		Path socketPath = findUnixSocket(index);

		if (socketPath == null) {
			return false;
		}

		channel = SocketChannel.open(StandardProtocolFamily.UNIX);
		channel.connect(UnixDomainSocketAddress.of(socketPath));
		return true;
	}

	/**
	 * Sucht den Socket in den ueblichen Verzeichnissen. Flatpak- und
	 * Snap-Installationen legen ihn in Unterordnern ab.
	 */
	private static Path findUnixSocket(int index) {
		String[] candidates = {
				System.getenv("XDG_RUNTIME_DIR"),
				System.getenv("TMPDIR"),
				"/tmp"
		};

		String[] subDirectories = { "", "app/com.discordapp.Discord/", "snap.discord/" };

		for (String base : candidates) {
			if (base == null || base.isBlank()) {
				continue;
			}

			for (String sub : subDirectories) {
				Path path = Path.of(base, sub, "discord-ipc-" + index);

				if (Files.exists(path)) {
					return path;
				}
			}
		}

		return null;
	}

	/**
	 * Aktualisiert die angezeigte Aktivitaet.
	 *
	 * @param details obere Zeile in Discord
	 * @param state   untere Zeile in Discord
	 * @param largeImageKey Schluessel des grossen Bildes (im Portal hinterlegt)
	 * @param largeImageText Tooltip des grossen Bildes
	 */
	public static synchronized void updatePresence(String details, String state,
			String largeImageKey, String largeImageText) {
		if (!connected) {
			return;
		}

		try {
			JsonObject activity = new JsonObject();
			activity.addProperty("details", details);
			activity.addProperty("state", state);

			JsonObject timestamps = new JsonObject();
			timestamps.addProperty("start", startTimestamp);
			activity.add("timestamps", timestamps);

			JsonObject assets = new JsonObject();
			assets.addProperty("large_image", largeImageKey);
			assets.addProperty("large_text", largeImageText);
			activity.add("assets", assets);

			JsonObject args = new JsonObject();
			args.addProperty("pid", ProcessHandle.current().pid());
			args.add("activity", activity);

			JsonObject frame = new JsonObject();
			frame.addProperty("cmd", "SET_ACTIVITY");
			frame.add("args", args);
			frame.addProperty("nonce", UUID.randomUUID().toString());

			send(OP_FRAME, frame.toString());
		} catch (Exception e) {
			// Discord wurde vermutlich beendet - Verbindung aufgeben
			Voidrix.LOGGER.debug("Discord-Presence konnte nicht aktualisiert werden.", e);
			disconnect();
		}
	}

	public static synchronized void disconnect() {
		if (connected) {
			try {
				send(OP_CLOSE, "{}");
			} catch (Exception ignored) {
				// beim Schliessen ist ein Fehler egal
			}
		}

		closeQuietly();
		connected = false;
	}

	// ---------------------------------------------------------------
	// Frame-Ein-/Ausgabe
	// ---------------------------------------------------------------

	private static void send(int opcode, String payload) throws IOException {
		byte[] data = payload.getBytes(StandardCharsets.UTF_8);

		ByteBuffer buffer = ByteBuffer.allocate(8 + data.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(opcode);
		buffer.putInt(data.length);
		buffer.put(data);
		buffer.flip();

		if (channel != null) {
			while (buffer.hasRemaining()) {
				channel.write(buffer);
			}
		} else if (pipe != null) {
			pipe.write(buffer.array());
		} else {
			throw new IOException("Keine offene Discord-Verbindung");
		}
	}

	private static void closeQuietly() {
		try {
			if (channel != null) {
				channel.close();
			}
		} catch (IOException ignored) {
			// egal
		}

		try {
			if (pipe != null) {
				pipe.close();
			}
		} catch (IOException ignored) {
			// egal
		}

		channel = null;
		pipe = null;
	}
}
