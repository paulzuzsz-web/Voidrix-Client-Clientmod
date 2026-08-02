package gg.voidrix.client.ui;

import gg.voidrix.client.Voidrix;
import gg.voidrix.client.config.ConfigManager;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.premium.PremiumCodes;
import gg.voidrix.client.premium.PremiumManager;
import gg.voidrix.client.ui.widget.TextField;
import gg.voidrix.client.waypoint.Waypoint;
import gg.voidrix.client.waypoint.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Das Voidrix-Hauptmenue.
 *
 * <p>Aufbau:</p>
 * <pre>
 *  +--+--------------------------------------------+
 *  |V | [Mods] [Profiles] [Waypoints]   [Suche...] |
 *  |C |--------------------------------------------|
 *  |F |  +-----------+  +-----------+              |
 *  |E |  | Modulkarte|  | Modulkarte|              |
 *  +--+--------------------------------------------+
 * </pre>
 *
 * <p>Links die schmale Icon-Sidebar (Voidrix+, Cosmetics, Friends, Emotes),
 * oben die Tabs samt Suchleiste, darunter das zweispaltige Karten-Grid.</p>
 */
public class VoidrixMenuScreen extends Screen {

	/** Welcher Bereich gerade angezeigt wird. */
	public enum Page {
		MODS("Mods"),
		PROFILES("Profiles"),
		WAYPOINTS("Waypoints"),
		PLUS("Voidrix+"),
		COSMETICS("Cosmetics"),
		FRIENDS("Friends"),
		EMOTES("Emotes");

		public final String title;

		Page(String title) {
			this.title = title;
		}
	}

	/** Die drei Tabs oben. */
	private static final Page[] TABS = { Page.MODS, Page.PROFILES, Page.WAYPOINTS };

	/** Die vier Sidebar-Eintraege samt Icon-Buchstabe. */
	private static final Page[] SIDEBAR = { Page.PLUS, Page.COSMETICS, Page.FRIENDS, Page.EMOTES };

	private static final int CARD_HEIGHT = 54;
	private static final int CARD_GAP = 8;
	private static final int PADDING = 12;

	private Page page = Page.MODS;

	private final TextField search = new TextField("Suchen ...", 32);
	private final TextField codeField = new TextField("Code eingeben", 64);

	/** Rueckmeldung im Voidrix+ Bereich. */
	private String codeMessage = "";
	private boolean codeSuccess;

	/** Vertikaler Scroll-Versatz des Karten-Grids. */
	private double scroll;

	// Panel-Geometrie, in init() berechnet
	private int panelX;
	private int panelY;
	private int panelWidth;
	private int panelHeight;

	public VoidrixMenuScreen() {
		super(Component.literal(Voidrix.MOD_NAME));
	}

	@Override
	protected void init() {
		panelWidth = Math.min(440, this.width - 40);
		panelHeight = Math.min(280, this.height - 40);
		panelX = (this.width - panelWidth) / 2;
		panelY = (this.height - panelHeight) / 2;

		int contentX = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int contentRight = panelX + panelWidth - PADDING;

		search.setBounds(contentRight - 110, panelY + 9, 110, 16);
		search.onChange(() -> scroll = 0);

		codeField.setBounds(contentX, panelY + 100, 160, 18);
		codeField.onSubmit(this::redeemCode);
	}

	/** Das Menue pausiert das Spiel nicht - es bleibt ein Overlay. */
	@Override
	public boolean isPauseScreen() {
		return false;
	}

	// ---------------------------------------------------------------
	// Hintergrund
	// ---------------------------------------------------------------

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		// Echter Blur des Spielgeschehens (Vanilla-Effekt) ...
		extractBlurredBackground(g);

		// ... darueber der halbtransparente Voidrix-Schleier.
		g.fill(0, 0, this.width, this.height, Theme.OVERLAY);
	}

	// ---------------------------------------------------------------
	// Zeichnen
	// ---------------------------------------------------------------

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(g, mouseX, mouseY, partialTick);

		// Grundflaeche der Karte mit gekappter Ecke oben rechts
		Draw.glow(g, panelX, panelY, panelWidth, panelHeight, Theme.CORNER_CUT, Theme.ACCENT, 3);
		Draw.cutCornerRect(g, panelX, panelY, panelWidth, panelHeight, Theme.CORNER_CUT, Theme.BASE);
		Draw.cutCornerOutline(g, panelX, panelY, panelWidth, panelHeight, Theme.CORNER_CUT,
				Theme.withAlpha(Theme.ACCENT, 130));

		renderSidebar(g, mouseX, mouseY);
		renderHeader(g, mouseX, mouseY);

		// Inhaltsbereich auf das Panel begrenzen, damit nichts herauslaeuft
		int contentX = panelX + Theme.SIDEBAR_WIDTH;
		int contentY = panelY + Theme.TAB_BAR_HEIGHT;
		int contentWidth = panelWidth - Theme.SIDEBAR_WIDTH;
		int contentHeight = panelHeight - Theme.TAB_BAR_HEIGHT;

		g.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);

		switch (page) {
			case MODS -> renderModuleGrid(g, mouseX, mouseY);
			case PROFILES -> renderProfiles(g);
			case WAYPOINTS -> renderWaypoints(g);
			case PLUS -> renderPlus(g, mouseX, mouseY);
			case COSMETICS -> renderCosmetics(g);
			case FRIENDS -> renderFriends(g);
			case EMOTES -> renderEmotes(g);
		}

		g.disableScissor();
	}

	/** Schmale Icon-Leiste links. */
	private void renderSidebar(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		Draw.rect(g, panelX, panelY, Theme.SIDEBAR_WIDTH, panelHeight, Theme.SIDEBAR);

		// Trennlinie zum Inhalt
		Draw.rect(g, panelX + Theme.SIDEBAR_WIDTH - 1, panelY, 1, panelHeight,
				Theme.withAlpha(Theme.ACCENT, 60));

		// Logo oben - fuehrt zurueck zur Modul-Uebersicht
		int logoY = panelY + 10;
		boolean logoHovered = Draw.isHovered(mouseX, mouseY, panelX + 13, logoY, 20, 20);

		int logoColor = PremiumManager.isPremium()
				? Theme.pulse(System.currentTimeMillis())
				: Theme.ACCENT;

		Draw.textCentered(g, "V", panelX + Theme.SIDEBAR_WIDTH / 2, logoY + 6,
				logoHovered ? Theme.ACCENT_2 : logoColor);

		int y = panelY + 44;

		for (Page entry : SIDEBAR) {
			boolean selected = page == entry;
			boolean hovered = Draw.isHovered(mouseX, mouseY, panelX + 8, y, 30, 26);

			if (selected) {
				// aktiver Eintrag: leuchtender Balken links
				Draw.rect(g, panelX + 1, y + 3, 2, 20, Theme.ACCENT);
				Draw.cutCornerRect(g, panelX + 8, y, 30, 26, 4, Theme.withAlpha(Theme.ACCENT, 40));
			} else if (hovered) {
				Draw.cutCornerRect(g, panelX + 8, y, 30, 26, 4, Theme.withAlpha(Theme.TEXT, 15));
			}

			int color = selected ? Theme.ACCENT_2 : (hovered ? Theme.TEXT : Theme.TEXT_DIM);
			Draw.textCentered(g, iconFor(entry), panelX + Theme.SIDEBAR_WIDTH / 2, y + 9, color);

			y += 30;
		}

		// Version unten links
		Draw.textCentered(g, "v" + Voidrix.VERSION, panelX + Theme.SIDEBAR_WIDTH / 2,
				panelY + panelHeight - 14, Theme.TEXT_MUTED);
	}

	/** Ein Buchstabe als Icon - reicht fuer die schmale Leiste. */
	private String iconFor(Page entry) {
		return switch (entry) {
			case PLUS -> "+";
			case COSMETICS -> "C";
			case FRIENDS -> "F";
			case EMOTES -> "E";
			default -> "*";
		};
	}

	/** Tabs und Suchleiste. */
	private void renderHeader(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + 12;

		if (isTabPage()) {
			for (Page tab : TABS) {
				int textWidth = Draw.textWidth(tab.title);
				boolean selected = page == tab;
				boolean hovered = Draw.isHovered(mouseX, mouseY, x - 4, y - 4, textWidth + 8, 16);

				Draw.textFlat(g, tab.title, x, y,
						selected ? Theme.TEXT : (hovered ? Theme.TEXT_DIM : Theme.TEXT_MUTED));

				if (selected) {
					// feine leuchtende Linie unter dem aktiven Tab
					Draw.rect(g, x, y + 11, textWidth, 1, Theme.ACCENT);
					Draw.rect(g, x, y + 12, textWidth, 1, Theme.withAlpha(Theme.ACCENT, 60));
				}

				x += textWidth + 18;
			}

			search.render(g, mouseX, mouseY);
		} else {
			// Sidebar-Bereiche zeigen statt der Tabs ihren Titel
			Draw.textFlat(g, page.title, x, y, Theme.TEXT);

			if (page == Page.PLUS && PremiumManager.isPremium()) {
				Draw.badge(g, "AKTIV", x + Draw.textWidth(page.title) + 8, y - 2,
						Theme.withAlpha(Theme.SUCCESS, 60), Theme.SUCCESS);
			}
		}

		Draw.separator(g, panelX + Theme.SIDEBAR_WIDTH + 6, panelY + Theme.TAB_BAR_HEIGHT - 4,
				panelWidth - Theme.SIDEBAR_WIDTH - 12, Theme.withAlpha(Theme.ACCENT, 110));
	}

	private boolean isTabPage() {
		return page == Page.MODS || page == Page.PROFILES || page == Page.WAYPOINTS;
	}

	// ---------------------------------------------------------------
	// Seite: Mods (Karten-Grid)
	// ---------------------------------------------------------------

	private void renderModuleGrid(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		List<Module> modules = ModuleManager.search(search.getValue());

		int gridX = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int gridY = panelY + Theme.TAB_BAR_HEIGHT + 6;
		int gridWidth = panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2;

		int cardWidth = (gridWidth - CARD_GAP) / 2;

		if (modules.isEmpty()) {
			Draw.textFlat(g, "Keine Module gefunden.", gridX, gridY + 10, Theme.TEXT_MUTED);
			return;
		}

		for (int i = 0; i < modules.size(); i++) {
			int column = i % 2;
			int row = i / 2;

			int cardX = gridX + column * (cardWidth + CARD_GAP);
			int cardY = (int) (gridY + row * (CARD_HEIGHT + CARD_GAP) - scroll);

			// nur zeichnen, was sichtbar ist
			if (cardY + CARD_HEIGHT < panelY + Theme.TAB_BAR_HEIGHT || cardY > panelY + panelHeight) {
				continue;
			}

			renderCard(g, modules.get(i), cardX, cardY, cardWidth, mouseX, mouseY);
		}
	}

	/** Eine einzelne Modul-Karte. */
	private void renderCard(GuiGraphicsExtractor g, Module module, int x, int y, int width,
			int mouseX, int mouseY) {
		boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, CARD_HEIGHT);
		boolean locked = module.isLocked();
		boolean active = module.isEnabled();

		Draw.cutCornerRect(g, x, y, width, CARD_HEIGHT, Theme.CORNER_CUT,
				hovered ? Theme.SURFACE_HIGH : Theme.SURFACE);

		// aktive Module bekommen eine leuchtende Kontur
		int outlineColor = locked
				? Theme.withAlpha(Theme.LOCKED, 120)
				: (active ? Theme.withAlpha(Theme.ACCENT, 190) : Theme.withAlpha(Theme.TEXT_MUTED, 70));

		Draw.cutCornerOutline(g, x, y, width, CARD_HEIGHT, Theme.CORNER_CUT, outlineColor);

		// Icon-Kachel links: erster Buchstabe des Modulnamens
		int iconSize = 22;
		int iconX = x + 8;
		int iconY = y + 8;

		Draw.cutCornerRect(g, iconX, iconY, iconSize, iconSize, 4,
				active ? Theme.withAlpha(Theme.ACCENT, 70) : Theme.withAlpha(Theme.TEXT_MUTED, 30));

		Draw.textCentered(g, module.getDisplayName().substring(0, 1).toUpperCase(),
				iconX + iconSize / 2, iconY + 7, active ? Theme.ACCENT_2 : Theme.TEXT_DIM);

		int textX = iconX + iconSize + 8;
		int textWidth = width - (textX - x) - 10;

		// Titel (bei gesperrten Modulen mit Schloss)
		String title = locked ? "⚿ " + module.getDisplayName() : module.getDisplayName();
		Draw.textFlat(g, Draw.truncate(title, textWidth - 26), textX, y + 10,
				locked ? Theme.TEXT_MUTED : Theme.TEXT);

		// Kurzbeschreibung
		Draw.textFlat(g, Draw.truncate(module.getDescription(), textWidth),
				textX, y + 22, Theme.TEXT_DIM);

		// "NEU"-Badge
		if (module.isMarkedNew()) {
			Draw.badge(g, "NEU", x + width - 34, y + 6,
					Theme.withAlpha(Theme.ACCENT_2, 55), Theme.ACCENT_2);
		}

		// Zustandszeile unten
		String state = locked ? "Voidrix+ noetig" : (active ? "AN" : "AUS");
		int stateColor = locked ? Theme.LOCKED : (active ? Theme.SUCCESS : Theme.TEXT_MUTED);
		Draw.textFlat(g, state, textX, y + 36, stateColor);

		// Konfigurations-Button unten rechts
		int gearX = x + width - 30;
		int gearY = y + CARD_HEIGHT - 16;
		boolean gearHovered = Draw.isHovered(mouseX, mouseY, gearX, gearY, 24, 12);

		Draw.textFlat(g, "Config", gearX - 8, gearY + 2,
				gearHovered ? Theme.ACCENT_2 : Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Seite: Profiles
	// ---------------------------------------------------------------

	private void renderProfiles(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 10;

		Draw.textFlat(g, "Profile", x, y, Theme.TEXT);
		Draw.textFlat(g, "Konfigurationen als Datei sichern und laden.", x, y + 14, Theme.TEXT_DIM);

		Draw.textFlat(g, "Aktive Konfiguration:", x, y + 36, Theme.TEXT_DIM);
		Draw.textFlat(g, ConfigManager.getConfigPath().getFileName().toString(), x, y + 48, Theme.ACCENT_2);

		Draw.textFlat(g, "Gespeichert unter:", x, y + 68, Theme.TEXT_DIM);
		Draw.textFlat(g, Draw.truncate(ConfigManager.getConfigPath().toString(),
				panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2), x, y + 80, Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Seite: Waypoints
	// ---------------------------------------------------------------

	private void renderWaypoints(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 10;

		List<Waypoint> waypoints = WaypointManager.getAll();

		if (waypoints.isEmpty()) {
			Draw.textFlat(g, "Noch keine Waypoints gesetzt.", x, y, Theme.TEXT_DIM);
			Draw.textFlat(g, "Im Spiel mit der Waypoint-Taste (Standard: B) setzen.",
					x, y + 14, Theme.TEXT_MUTED);
			return;
		}

		int row = 0;

		for (Waypoint waypoint : waypoints) {
			int rowY = (int) (y + row * 16 - scroll);

			if (rowY > panelY + panelHeight - 16) {
				break;
			}

			// Farbpunkt des Waypoints
			Draw.rect(g, x, rowY + 2, 5, 5, waypoint.getColor());

			Draw.textFlat(g, Draw.truncate(waypoint.getName(), 120), x + 10, rowY, Theme.TEXT);

			String coords = String.format("%.0f %.0f %.0f",
					waypoint.getX(), waypoint.getY(), waypoint.getZ());

			Draw.textRight(g, coords, panelX + panelWidth - PADDING, rowY, Theme.TEXT_DIM);

			row++;
		}
	}

	// ---------------------------------------------------------------
	// Seite: Voidrix+
	// ---------------------------------------------------------------

	private void renderPlus(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 8;

		if (PremiumManager.isPremium()) {
			Draw.textFlat(g, "Voidrix+ ist aktiv.", x, y, Theme.SUCCESS);
			Draw.textFlat(g, "Alle gesperrten Module sind freigeschaltet und dein",
					x, y + 16, Theme.TEXT_DIM);
			Draw.textFlat(g, "Namens-V erscheint in der Premium-Variante.",
					x, y + 27, Theme.TEXT_DIM);
		} else {
			Draw.textFlat(g, "Voidrix+ freischalten", x, y, Theme.TEXT);
			Draw.textFlat(g, "Loese deinen Code ein, um Premium-Module und das",
					x, y + 16, Theme.TEXT_DIM);
			Draw.textFlat(g, "hervorgehobene Namens-V zu erhalten.", x, y + 27, Theme.TEXT_DIM);
		}

		Draw.textFlat(g, "Code einloesen", x, y + 50, Theme.TEXT_DIM);

		codeField.setBounds(x, y + 62, 160, 18);
		codeField.render(g, mouseX, mouseY);

		// Einloesen-Button
		int buttonX = x + 168;
		int buttonY = y + 62;
		boolean hovered = Draw.isHovered(mouseX, mouseY, buttonX, buttonY, 70, 18);

		Draw.cutCornerRect(g, buttonX, buttonY, 70, 18, 5,
				hovered ? Theme.ACCENT : Theme.withAlpha(Theme.ACCENT, 160));

		Draw.cutCornerOutline(g, buttonX, buttonY, 70, 18, 5, Theme.withAlpha(Theme.ACCENT_2, 150));
		Draw.textCentered(g, "Einloesen", buttonX + 35, buttonY + 5, Theme.TEXT);

		// Rueckmeldung
		if (!codeMessage.isEmpty()) {
			Draw.textFlat(g, codeMessage, x, y + 88, codeSuccess ? Theme.SUCCESS : Theme.ERROR);
		}

		// Vorteile
		int listY = y + 108;
		Draw.textFlat(g, "Enthalten:", x, listY, Theme.TEXT_DIM);

		String[] perks = {
				"Premium-Module ohne Schloss",
				"Leuchtendes V mit Farbverlauf",
				"Cosmetics & Emotes"
		};

		for (int i = 0; i < perks.length; i++) {
			Draw.textFlat(g, "+ " + perks[i], x + 4, listY + 12 + i * 11, Theme.TEXT_MUTED);
		}
	}

	/** Prueft den eingegebenen Code und schaltet ggf. Voidrix+ frei. */
	private void redeemCode() {
		String input = codeField.getValue();

		if (PremiumManager.redeem(input)) {
			codeSuccess = true;
			codeMessage = "Voidrix+ wurde freigeschaltet.";
			codeField.clear();

			// dauerhaft sichern, damit der Code nur einmal noetig ist
			ConfigManager.save();
		} else {
			codeSuccess = false;
			codeMessage = PremiumCodes.ERROR_INVALID;
		}
	}

	// ---------------------------------------------------------------
	// Seiten: Cosmetics / Friends / Emotes
	// ---------------------------------------------------------------

	private void renderCosmetics(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 10;

		Draw.textFlat(g, "Cosmetics", x, y, Theme.TEXT);
		Draw.textFlat(g, "Die 3D-Skin-Vorschau findest du als Modul unter \"Optik\".",
				x, y + 16, Theme.TEXT_DIM);

		String name = Minecraft.getInstance().getUser().getName();
		Draw.textFlat(g, "Angemeldet als: " + name, x, y + 36, Theme.TEXT_MUTED);

		if (!PremiumManager.isPremium()) {
			Draw.textFlat(g, "Cosmetics benoetigen Voidrix+.", x, y + 56, Theme.LOCKED);
		}
	}

	private void renderFriends(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 10;

		Draw.textFlat(g, "Friends", x, y, Theme.TEXT);

		// Ohne Backend kann der Client nur zeigen, wer gerade auf dem
		// gleichen Server ist - siehe VoidrixUsers fuer die Erweiterung.
		var connection = Minecraft.getInstance().getConnection();

		if (connection == null) {
			Draw.textFlat(g, "Nicht mit einem Server verbunden.", x, y + 16, Theme.TEXT_DIM);
			return;
		}

		Draw.textFlat(g, "Spieler auf diesem Server:", x, y + 16, Theme.TEXT_DIM);

		List<String> names = new ArrayList<>();

		for (var info : connection.getListedOnlinePlayers()) {
			names.add(info.getProfile().name());
		}

		for (int i = 0; i < Math.min(names.size(), 8); i++) {
			Draw.textFlat(g, names.get(i), x + 4, y + 32 + i * 11, Theme.TEXT_MUTED);
		}
	}

	private void renderEmotes(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 10;

		Draw.textFlat(g, "Emotes", x, y, Theme.TEXT);
		Draw.textFlat(g, "Emotes sind Voidrix+ Inhalte.", x, y + 16, Theme.TEXT_DIM);

		if (!PremiumManager.isPremium()) {
			Draw.textFlat(g, "Mit einem Code im Voidrix+ Bereich freischalten.",
					x, y + 32, Theme.LOCKED);
		}
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		double mouseX = event.x();
		double mouseY = event.y();

		// Sidebar-Logo -> zurueck zur Modulliste
		if (Draw.isHovered(mouseX, mouseY, panelX + 8, panelY + 8, 30, 24)) {
			page = Page.MODS;
			scroll = 0;
			return true;
		}

		// Sidebar-Eintraege
		int sidebarY = panelY + 44;

		for (Page entry : SIDEBAR) {
			if (Draw.isHovered(mouseX, mouseY, panelX + 8, sidebarY, 30, 26)) {
				page = entry;
				scroll = 0;
				codeMessage = "";
				return true;
			}

			sidebarY += 30;
		}

		// Tabs
		if (isTabPage()) {
			int tabX = panelX + Theme.SIDEBAR_WIDTH + PADDING;
			int tabY = panelY + 12;

			for (Page tab : TABS) {
				int textWidth = Draw.textWidth(tab.title);

				if (Draw.isHovered(mouseX, mouseY, tabX - 4, tabY - 4, textWidth + 8, 16)) {
					page = tab;
					scroll = 0;
					return true;
				}

				tabX += textWidth + 18;
			}

			if (search.mouseClicked(mouseX, mouseY)) {
				return true;
			}
		}

		if (page == Page.PLUS) {
			if (codeField.mouseClicked(mouseX, mouseY)) {
				return true;
			}

			// Einloesen-Button
			int buttonX = panelX + Theme.SIDEBAR_WIDTH + PADDING + 168;
			int buttonY = panelY + Theme.TAB_BAR_HEIGHT + 8 + 62;

			if (Draw.isHovered(mouseX, mouseY, buttonX, buttonY, 70, 18)) {
				redeemCode();
				return true;
			}
		}

		if (page == Page.MODS && handleCardClick(mouseX, mouseY)) {
			return true;
		}

		return super.mouseClicked(event, doubled);
	}

	/** Klick auf eine Modul-Karte: Config-Button oeffnet Details, sonst umschalten. */
	private boolean handleCardClick(double mouseX, double mouseY) {
		List<Module> modules = ModuleManager.search(search.getValue());

		int gridX = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int gridY = panelY + Theme.TAB_BAR_HEIGHT + 6;
		int gridWidth = panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2;
		int cardWidth = (gridWidth - CARD_GAP) / 2;

		for (int i = 0; i < modules.size(); i++) {
			int column = i % 2;
			int row = i / 2;

			int cardX = gridX + column * (cardWidth + CARD_GAP);
			int cardY = (int) (gridY + row * (CARD_HEIGHT + CARD_GAP) - scroll);

			if (!Draw.isHovered(mouseX, mouseY, cardX, cardY, cardWidth, CARD_HEIGHT)) {
				continue;
			}

			Module module = modules.get(i);

			// Klick auf "Config" oeffnet die Detailseite ...
			int gearX = cardX + cardWidth - 38;
			int gearY = cardY + CARD_HEIGHT - 16;

			if (Draw.isHovered(mouseX, mouseY, gearX, gearY, 34, 14)) {
				this.minecraft.gui.setScreen(new ModuleDetailScreen(this, module));
				return true;
			}

			// ... ein Klick auf die Karte schaltet das Modul um.
			module.toggle();
			ConfigManager.save();
			return true;
		}

		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		scroll = Math.max(0, scroll - scrollY * 18);
		return true;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		// Textfelder haben Vorrang, damit z.B. "E" nicht das Inventar oeffnet
		if (search.keyPressed(event.key()) || codeField.keyPressed(event.key())) {
			return true;
		}

		if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
			this.onClose();
			return true;
		}

		return super.keyPressed(event);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		char typed = (char) event.codepoint();

		if (search.charTyped(typed) || codeField.charTyped(typed)) {
			return true;
		}

		return super.charTyped(event);
	}

	@Override
	public void onClose() {
		// Beim Schliessen alles sichern - so geht keine Aenderung verloren.
		ConfigManager.save();
		super.onClose();
	}
}
