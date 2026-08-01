package gg.voidrix.client.screen;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.VoidrixConfig;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * Einstellungsmenue, erreichbar ueber die Rechte Umschalttaste.
 *
 * <p>Ein zentriertes Panel mit zwei Spalten. Alle Optionen sind Umschalter; gespeichert wird
 * beim Schliessen.
 */
public final class VoidrixSettingsScreen extends Screen {

    private static final int PANEL_WIDTH = 400;
    private static final int PANEL_PADDING = 18;
    private static final int HEADER_HEIGHT = 42;

    private static final int COLUMN_WIDTH = 176;
    private static final int COLUMN_GAP = 10;
    private static final int ROW_HEIGHT = 20;
    private static final int ROW_GAP = 4;
    private static final int ROWS_PER_COLUMN = 5;

    private static final int SECTION_LABEL_HEIGHT = 14;
    private static final int FOOTER_HEIGHT = 44;

    private static final String SUBTITLE = "Client-Einstellungen";
    private static final String FOOTER_HINT = "C  Zoom     G  Fullbright     Rechts-Umschalt  Menue";

    private final Screen parent;

    private int panelX;
    private int panelY;
    private int panelHeight;

    public VoidrixSettingsScreen(Screen parent) {
        super(Component.literal(Voidrix.NAME));
        this.parent = parent;
    }

    @Override
    protected void init() {
        VoidrixConfig config = Voidrix.config();

        int rowsHeight = ROWS_PER_COLUMN * ROW_HEIGHT + (ROWS_PER_COLUMN - 1) * ROW_GAP;
        panelHeight = HEADER_HEIGHT + PANEL_PADDING + SECTION_LABEL_HEIGHT + rowsHeight + FOOTER_HEIGHT;
        panelX = (this.width - PANEL_WIDTH) / 2;
        panelY = Math.max(10, (this.height - panelHeight) / 2);

        int leftColumn = panelX + PANEL_PADDING;
        int rightColumn = leftColumn + COLUMN_WIDTH + COLUMN_GAP;
        int firstRowY = panelY + HEADER_HEIGHT + PANEL_PADDING + SECTION_LABEL_HEIGHT;

        addToggle(leftColumn, rowY(firstRowY, 0), "Fullbright",
                () -> config.fullBright, v -> config.fullBright = v);
        addToggle(leftColumn, rowY(firstRowY, 1), "No Hurt Cam",
                () -> config.noHurtCam, v -> config.noHurtCam = v);
        addToggle(leftColumn, rowY(firstRowY, 2), "Zoom",
                () -> config.zoomEnabled, v -> config.zoomEnabled = v);
        addToggle(leftColumn, rowY(firstRowY, 3), "Zoom weich",
                () -> config.zoomSmooth, v -> config.zoomSmooth = v);
        addToggle(leftColumn, rowY(firstRowY, 4), "Fenstertitel",
                () -> config.customWindowTitle, v -> config.customWindowTitle = v);

        addToggle(rightColumn, rowY(firstRowY, 0), "FPS-Anzeige",
                () -> config.fpsHud, v -> config.fpsHud = v);
        addToggle(rightColumn, rowY(firstRowY, 1), "Koordinaten",
                () -> config.coordsHud, v -> config.coordsHud = v);
        addToggle(rightColumn, rowY(firstRowY, 2), "Ping",
                () -> config.pingHud, v -> config.pingHud = v);
        addToggle(rightColumn, rowY(firstRowY, 3), "Uhr",
                () -> config.clockHud, v -> config.clockHud = v);
        addToggle(rightColumn, rowY(firstRowY, 4), "HUD-Hintergrund",
                () -> config.hudBackground, v -> config.hudBackground = v);

        int doneWidth = 120;
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                .bounds(panelX + (PANEL_WIDTH - doneWidth) / 2,
                        firstRowY + rowsHeight + PANEL_PADDING,
                        doneWidth, ROW_HEIGHT)
                .build());
    }

    private static int rowY(int firstRowY, int index) {
        return firstRowY + index * (ROW_HEIGHT + ROW_GAP);
    }

    private void addToggle(int x, int y, String label, BooleanSupplier getter, Consumer<Boolean> setter) {
        this.addRenderableWidget(new ToggleButton(
                x, y, COLUMN_WIDTH, ROW_HEIGHT, Component.literal(label), getter, setter));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(g, mouseX, mouseY, partialTick);

        Theme.roundedRect(g, panelX, panelY, PANEL_WIDTH, panelHeight, Theme.PANEL_BG);
        Theme.roundedRect(g, panelX, panelY, PANEL_WIDTH, HEADER_HEIGHT, Theme.HEADER_BG);
        // Akzentlinie als Trenner zwischen Kopf und Inhalt.
        g.fill(panelX + 1, panelY + HEADER_HEIGHT - 1, panelX + PANEL_WIDTH - 1,
                panelY + HEADER_HEIGHT, Theme.ACCENT);
        Theme.border(g, panelX, panelY, PANEL_WIDTH, panelHeight, Theme.PANEL_BORDER);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(g, mouseX, mouseY, partialTick);

        int centerX = panelX + PANEL_WIDTH / 2;
        g.centeredText(this.font, Voidrix.NAME.toUpperCase(), centerX, panelY + 12, Theme.ACCENT);
        g.centeredText(this.font, SUBTITLE, centerX, panelY + 25, Theme.TEXT_MUTED);

        int sectionY = panelY + HEADER_HEIGHT + PANEL_PADDING;
        int leftColumn = panelX + PANEL_PADDING;
        g.text(this.font, "RENDER", leftColumn, sectionY, Theme.TEXT_SECTION);
        g.text(this.font, "HUD", leftColumn + COLUMN_WIDTH + COLUMN_GAP, sectionY, Theme.TEXT_SECTION);

        g.centeredText(this.font, FOOTER_HINT, centerX, panelY + panelHeight - 14, Theme.TEXT_MUTED);
    }

    @Override
    public void onClose() {
        Voidrix.saveConfig();
        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(this.parent);
        }
    }
}
