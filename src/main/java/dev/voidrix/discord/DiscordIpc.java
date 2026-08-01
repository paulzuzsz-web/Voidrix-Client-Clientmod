package dev.voidrix.discord;

import com.google.gson.JsonObject;
import dev.voidrix.VoidrixClient;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * A minimal client for Discord's local IPC socket.
 *
 * <p>Discord exposes rich presence over a local socket - a Unix domain socket on Linux and macOS,
 * a named pipe on Windows - speaking length-prefixed JSON frames. That is little enough protocol to
 * implement directly, which is why Voidrix carries no Discord library: nothing to bundle, nothing
 * to keep up to date, and no third-party code in the jar.
 *
 * <p>This class only ever talks to a socket already on the machine. It opens no network connection
 * itself; whatever Discord then does with the presence is Discord's own doing, and the module above
 * this decides what it is allowed to see.
 */
public final class DiscordIpc implements AutoCloseable {
    private static final int OP_HANDSHAKE = 0;
    private static final int OP_FRAME = 1;
    private static final int OP_CLOSE = 2;

    /** Unix domain socket, on Linux and macOS. */
    private SocketChannel channel;
    /** Named pipe, on Windows. */
    private RandomAccessFile pipe;

    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    // -------------------------------------------------------------------------------------
    // Connection
    // -------------------------------------------------------------------------------------

    /** Attempts to attach to a running Discord client. Returns false when Discord is not there. */
    public boolean connect(String applicationId) {
        if (connected) {
            return true;
        }
        boolean windows = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
        for (int i = 0; i < 10; i++) {
            try {
                if (windows ? openPipe(i) : openSocket(i)) {
                    JsonObject handshake = new JsonObject();
                    handshake.addProperty("v", 1);
                    handshake.addProperty("client_id", applicationId);
                    write(OP_HANDSHAKE, handshake.toString());
                    // Discord answers with a READY frame; we only care that the read succeeds.
                    read();
                    connected = true;
                    return true;
                }
            } catch (IOException | RuntimeException e) {
                closeQuietly();
            }
        }
        return false;
    }

    private boolean openSocket(int index) throws IOException {
        for (Path dir : candidateDirectories()) {
            Path socket = dir.resolve("discord-ipc-" + index);
            if (!Files.exists(socket)) {
                continue;
            }
            SocketChannel c = SocketChannel.open(StandardProtocolFamily.UNIX);
            c.connect(UnixDomainSocketAddress.of(socket));
            channel = c;
            return true;
        }
        return false;
    }

    private boolean openPipe(int index) throws IOException {
        Path path = Path.of("\\\\.\\pipe\\discord-ipc-" + index);
        RandomAccessFile file = new RandomAccessFile(path.toString(), "rw");
        pipe = file;
        return true;
    }

    /**
     * Places Discord's socket can live. Beyond the plain runtime directory, the Flatpak and Snap
     * builds each nest theirs one level deeper, which is why a plain check misses them.
     */
    private static List<Path> candidateDirectories() {
        List<Path> roots = new ArrayList<>();
        for (String env : new String[]{"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"}) {
            String value = System.getenv(env);
            if (value != null && !value.isBlank()) {
                roots.add(Path.of(value));
            }
        }
        roots.add(Path.of("/tmp"));

        List<Path> all = new ArrayList<>();
        for (Path root : roots) {
            all.add(root);
            all.add(root.resolve("app/com.discordapp.Discord"));
            all.add(root.resolve("app/com.discordapp.DiscordCanary"));
            all.add(root.resolve("snap.discord"));
            all.add(root.resolve(".flatpak/com.discordapp.Discord/xdg-run"));
        }
        return all;
    }

    // -------------------------------------------------------------------------------------
    // Frames
    // -------------------------------------------------------------------------------------

    private void write(int opcode, String payload) throws IOException {
        byte[] body = payload.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(8 + body.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(opcode);
        buffer.putInt(body.length);
        buffer.put(body);
        buffer.flip();

        if (channel != null) {
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } else if (pipe != null) {
            pipe.write(buffer.array());
        } else {
            throw new IOException("not connected");
        }
    }

    /** Reads one frame and discards its contents; we never need to act on Discord's replies. */
    private void read() throws IOException {
        ByteBuffer header = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        if (channel != null) {
            while (header.hasRemaining()) {
                if (channel.read(header) < 0) {
                    throw new IOException("socket closed");
                }
            }
            header.flip();
            header.getInt();
            int length = header.getInt();
            ByteBuffer body = ByteBuffer.allocate(Math.max(0, length));
            while (body.hasRemaining()) {
                if (channel.read(body) < 0) {
                    throw new IOException("socket closed");
                }
            }
        } else if (pipe != null) {
            byte[] raw = new byte[8];
            pipe.readFully(raw);
            int length = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN).getInt(4);
            if (length > 0) {
                pipe.readFully(new byte[length]);
            }
        }
    }

    /** Pushes an activity payload. Returns false if the link dropped, so the caller can retry. */
    public boolean sendActivity(JsonObject activity) {
        if (!connected) {
            return false;
        }
        try {
            JsonObject args = new JsonObject();
            args.addProperty("pid", ProcessHandle.current().pid());
            if (activity == null) {
                args.add("activity", null);
            } else {
                args.add("activity", activity);
            }

            JsonObject frame = new JsonObject();
            frame.addProperty("cmd", "SET_ACTIVITY");
            frame.add("args", args);
            frame.addProperty("nonce", UUID.randomUUID().toString());

            write(OP_FRAME, frame.toString());
            read();
            return true;
        } catch (IOException | RuntimeException e) {
            VoidrixClient.LOGGER.debug("[Voidrix] Discord link dropped", e);
            closeQuietly();
            return false;
        }
    }

    @Override
    public void close() {
        if (connected) {
            try {
                write(OP_CLOSE, "{}");
            } catch (IOException ignored) {
                // Discord is gone already; nothing to tell it.
            }
        }
        closeQuietly();
    }

    private void closeQuietly() {
        connected = false;
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException ignored) {
            // Nothing useful to do with a failure to close.
        }
        try {
            if (pipe != null) {
                pipe.close();
            }
        } catch (IOException ignored) {
            // As above.
        }
        channel = null;
        pipe = null;
    }
}
