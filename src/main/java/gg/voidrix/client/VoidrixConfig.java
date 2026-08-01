package gg.voidrix.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Einstellungen des Clients. Wird als JSON unter {@code config/voidrix.json} abgelegt.
 *
 * <p>Bewusst ein flaches Feld-Objekt: Gson serialisiert es ohne weitere Adapter, und ein
 * fehlendes Feld in einer aelteren Datei behaelt einfach seinen Default.
 */
public final class VoidrixConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // --- Module ---
    public boolean fullBright = false;
    public boolean noHurtCam = false;

    // --- Zoom ---
    public boolean zoomEnabled = true;
    public double zoomDivisor = 4.0D;
    public boolean zoomSmooth = true;

    // --- HUD ---
    public boolean fpsHud = true;
    public boolean coordsHud = true;
    public boolean pingHud = false;
    public boolean clockHud = false;
    public boolean hudBackground = true;
    public int hudX = 4;
    public int hudY = 4;
    public int hudColor = 0xFFFFFF;

    // --- Branding ---
    public boolean customWindowTitle = true;

    public static VoidrixConfig load(Path path) {
        if (Files.isRegularFile(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                VoidrixConfig loaded = GSON.fromJson(reader, VoidrixConfig.class);
                if (loaded != null) {
                    return loaded.sanitized();
                }
            } catch (IOException | RuntimeException e) {
                Voidrix.LOGGER.warn("Konnte {} nicht lesen, verwende Defaults", path, e);
            }
        }
        return new VoidrixConfig();
    }

    public void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            Voidrix.LOGGER.warn("Konnte {} nicht schreiben", path, e);
        }
    }

    /** Faengt kaputte Werte aus handeditierten Dateien ab, bevor sie ins Rendering laufen. */
    private VoidrixConfig sanitized() {
        if (!Double.isFinite(zoomDivisor) || zoomDivisor < 1.0D) {
            zoomDivisor = 1.0D;
        } else if (zoomDivisor > 50.0D) {
            zoomDivisor = 50.0D;
        }
        hudColor &= 0xFFFFFF;
        return this;
    }
}
