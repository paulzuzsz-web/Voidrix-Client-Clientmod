package gg.voidrix.client.screen;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.VoidrixConfig;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Einstellungsmenue, erreichbar ueber die Rechte Umschalttaste.
 *
 * <p>Aufbau: Kopfzeile mit Wortmarke, links eine Navigationsspalte, rechts der Inhalt der
 * gewaehlten Kategorie. Beim Wechsel der Kategorie werden die Widgets neu aufgebaut, damit
 * jede Seite ihr eigenes Layout bekommen kann.
 */
public final class VoidrixSettingsScreen extends Screen {

    private static final String[] TABS = {"Render", "HUD", "Steuerung"};

    private static final int PANEL_WIDTH = 424;
    private static final int PANEL_HEIGHT = 244;
    private static final int HEADER_HEIGHT = 40;
    private static final int SIDEBAR_WIDTH = 116;

    private static final int TAB_HEIGHT = 22;
    private static final int TAB_GAP = 2;
    private static final int CONTENT_PADDING = 14;
    private static final int ROW_HEIGHT = 20;
    private static final int ROW_GAP = 4;
    private static final int SLIDER_HEIGHT = 28;

    private final Screen parent;
    private int activeTab = 0;

    private int panelX;
    private int panelY;

    public VoidrixSettingsScreen(Screen parent) {
        super(Component.literal(Voidrix.NAME));
        this.parent = parent;
    }

    /** Nur fuer die Abnahme der Oberflaeche: erlaubt das Vorwaehlen einer Kategorie. */
    public void selectTab(int index) {
        if (index >= 0 && index < TABS.length) {
            activeTab = index;
            rebuildWidgets();
        }
    }

    @Override
    protected void init() {
        panelX = (this.width - PANEL_WIDTH) / 2;
        panelY = Math.max(10, (this.height - PANEL_HEIGHT) / 2);

        int tabX = panelX + 8;
        int tabY = panelY + HEADER_HEIGHT + 10;
        for (int i = 0; i < TABS.length; i++) {
            this.addRenderableWidget(new CategoryTab(
                    tabX, tabY + i * (TAB_HEIGHT + TAB_GAP), SIDEBAR_WIDTH - 16, TAB_HEIGHT,
                    Component.literal(TABS[i]), i, () -> activeTab, this::selectTab));
        }

        switch (activeTab) {
            case 0 -> buildRenderTab();
            case 1 -> buildHudTab();
            default -> buildControlsTab();
        }
    }

    private int contentX() {
        return panelX + SIDEBAR_WIDTH + CONTENT_PADDING;
    }

    private int contentWidth() {
        return PANEL_WIDTH - SIDEBAR_WIDTH - 2 * CONTENT_PADDING;
    }

    private int contentTop() {
        return panelY + HEADER_HEIGHT + CONTENT_PADDING + 12;
    }

    private int rowY(int index) {
        return contentTop() + index * (ROW_HEIGHT + ROW_GAP);
    }

    private void buildRenderTab() {
        VoidrixConfig config = Voidrix.config();
        addToggle(rowY(0), "Fullbright", () -> config.fullBright, v -> config.fullBright = v);
        addToggle(rowY(1), "No Hurt Cam", () -> config.noHurtCam, v -> config.noHurtCam = v);
        addToggle(rowY(2), "Zoom", () -> config.zoomEnabled, v -> config.zoomEnabled = v);
        addToggle(rowY(3), "Zoom weich", () -> config.zoomSmooth, v -> config.zoomSmooth = v);

        this.addRenderableWidget(new ValueSlider(
                contentX(), rowY(4), contentWidth(), SLIDER_HEIGHT,
                "Zoomfaktor", 1.5D, 20.0D, config.zoomDivisor,
                v -> config.zoomDivisor = v));
    }

    private void buildHudTab() {
        VoidrixConfig config = Voidrix.config();
        addToggle(rowY(0), "FPS-Anzeige", () -> config.fpsHud, v -> config.fpsHud = v);
        addToggle(rowY(1), "Koordinaten", () -> config.coordsHud, v -> config.coordsHud = v);
        addToggle(rowY(2), "Ping", () -> config.pingHud, v -> config.pingHud = v);
        addToggle(rowY(3), "Uhr", () -> config.clockHud, v -> config.clockHud = v);
        addToggle(rowY(4), "Hintergrund", () -> config.hudBackground, v -> config.hudBackground = v);
    }

    private void buildControlsTab() {
        VoidrixConfig config = Voidrix.config();
        addToggle(rowY(0), "Fenstertitel", () -> config.customWindowTitle,
                v -> config.customWindowTitle = v);
    }

    /**
     * Tastenbelegungen als reine Anzeige. Bewusst keine Widgets: geaendert wird in den
     * Vanilla-Steuerungseinstellungen, hier steht nur, was aktuell gebunden ist.
     */
    private void renderKeyBindings(GuiGraphicsExtractor g) {
        record Entry(String label, KeyMapping mapping) {
        }
        Entry[] entries = {
                new Entry("Zoom", Voidrix.zoomKey),
                new Entry("Fullbright umschalten", Voidrix.fullBrightKey),
                new Entry("Dieses Menue", Voidrix.settingsKey),
        };

        int x = contentX();
        int width = contentWidth();
        int y = rowY(1) + 8;

        g.text(this.font, "TASTEN", x, y, Theme.TEXT_SECTION);
        Theme.fadingRule(g, x, y + 10, width, Theme.DIVIDER);
        y += 18;

        for (Entry entry : entries) {
            if (entry.mapping() == null) {
                continue;
            }
            g.text(this.font, entry.label(), x + 2, y, Theme.TEXT_MUTED);

            String key = entry.mapping().getTranslatedKeyMessage().getString();
            int keyWidth = this.font.width(key);
            int boxX = x + width - keyWidth - 10;
            Theme.roundedRect(g, boxX, y - 4, keyWidth + 8, 15, Theme.ROW);
            g.text(this.font, key, boxX + 4, y, Theme.ACCENT);

            y += 20;
        }
    }

    private void addToggle(int y, String label, BooleanSupplier getter, Consumer<Boolean> setter) {
        this.addRenderableWidget(new ToggleButton(
                contentX(), y, contentWidth(), ROW_HEIGHT,
                Component.literal(label), getter, setter));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, this.width, this.height, Theme.SCRIM);

        Theme.verticalGradient(g, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT,
                Theme.PANEL_TOP, Theme.PANEL_BOTTOM);
        Theme.roundedRect(g, panelX, panelY + HEADER_HEIGHT, SIDEBAR_WIDTH,
                PANEL_HEIGHT - HEADER_HEIGHT, Theme.SIDEBAR_SOLID);

        // Trenner: waagerecht unter dem Kopf, senkrecht neben der Navigation.
        g.fill(panelX + 1, panelY + HEADER_HEIGHT, panelX + PANEL_WIDTH - 1,
                panelY + HEADER_HEIGHT + 1, Theme.DIVIDER);
        g.fill(panelX + SIDEBAR_WIDTH, panelY + HEADER_HEIGHT + 1,
                panelX + SIDEBAR_WIDTH + 1, panelY + PANEL_HEIGHT - 1, Theme.DIVIDER);

        Theme.border(g, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, Theme.PANEL_BORDER);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(g, mouseX, mouseY, partialTick);

        // Wortmarke: Akzentblock als Logo-Ersatz, danach der Name.
        int markX = panelX + 16;
        int markY = panelY + 15;
        g.fill(markX, markY, markX + 3, markY + 10, Theme.ACCENT);
        g.fill(markX + 5, markY + 3, markX + 8, markY + 10, Theme.ACCENT_ALT);
        g.text(this.font, Voidrix.NAME.toUpperCase(), markX + 14, markY + 1, Theme.TEXT);

        String version = "v" + Voidrix.VERSION;
        g.text(this.font, version,
                panelX + PANEL_WIDTH - 16 - this.font.width(version), markY + 1, Theme.TEXT_MUTED);

        // Ueberschrift des aktiven Bereichs.
        g.text(this.font, TABS[activeTab].toUpperCase(),
                contentX(), panelY + HEADER_HEIGHT + CONTENT_PADDING - 2, Theme.TEXT_SECTION);
        Theme.fadingRule(g, contentX(), panelY + HEADER_HEIGHT + CONTENT_PADDING + 8,
                contentWidth(), Theme.DIVIDER);

        if (activeTab == 2) {
            renderKeyBindings(g);
        }

        String hint = switch (activeTab) {
            case 0 -> "C halten zum Zoomen  ·  G schaltet Fullbright";
            case 1 -> "Anzeige oben links  ·  Position in config/voidrix.json";
            default -> "Rechte Umschalttaste oeffnet und schliesst dieses Menue";
        };
        g.centeredText(this.font, hint, panelX + PANEL_WIDTH / 2,
                panelY + PANEL_HEIGHT - 16, Theme.TEXT_MUTED);
    }

    @Override
    public void onClose() {
        Voidrix.saveConfig();
        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(this.parent);
        }
    }
}
