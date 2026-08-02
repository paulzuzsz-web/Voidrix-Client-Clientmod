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
 * <pre>
 *  +----+-------------------------------------------------+
 *  | /  | MODS  PROFILES  WAYPOINTS      [*] [Suchen... ] |
 *  |VDX+|-------------------------------------------------|
 *  | T  |  +--------------+  +--------------+           | |
 *  |COSM|  | [i] TITEL ...|  | [i] TITEL ...|           | |
 *  | %  |  |     zwei Zei.|  |     zwei Zei.|           | |
 *  |FRND|  +--------------+  +--------------+           | |
 *  +----+-------------------------------------------------+
 *                                        [] 3 SPIELER ONLINE
 * </pre>
 *
 * <p>Helles Frosted Glass ueber dem geblurrten Spiel, runde Ecken,
 * Beschriftungen in Grossbuchstaben. Violett markiert ausschliesslich aktive
 * Zustaende.</p>
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

	/** Sidebar-Eintraege: Seite, Icon und Mini-Label darunter. */
	private static final Page[] SIDEBAR = { Page.PLUS, Page.COSMETICS, Page.FRIENDS, Page.EMOTES };

	private static final Icons.Icon[] SIDEBAR_ICONS = {
			Icons.Icon.LIGHTNING, Icons.Icon.SHIRT, Icons.Icon.FRIENDS, Icons.Icon.EMOTE
	};

	private static final String[] SIDEBAR_LABELS = { "VDX+", "COSM.", "FRIENDS", "EMOTES" };

	private static final int CARD_HEIGHT = 42;
	private static final int CARD_GAP = 6;
	private static final int PADDING = 12;
	private static final int SIDEBAR_ITEM_HEIGHT = 34;

	private Page page;

	private final TextField search = new TextField("Suchen ...", 32);
	private final TextField codeField = new TextField("Code eingeben", 64);

	/** Rueckmeldung im Voidrix+ Bereich. */
	private String codeMessage = "";
	private boolean codeSuccess;

	/** Vertikaler Scroll-Versatz des Inhaltsbereichs. */
	private double scroll;

	/** Gesamthoehe des aktuellen Inhalts - fuer den Scrollbalken. */
	private int contentHeight;

	// Panel-Geometrie, in init() berechnet
	private int panelX;
	private int panelY;
	private int panelWidth;
	private int panelHeight;

	public VoidrixMenuScreen() {
		this(Page.MODS);
	}

	public VoidrixMenuScreen(Page initialPage) {
		super(Component.literal(Voidrix.MOD_NAME));
		this.page = initialPage;
	}

	@Override
	protected void init() {
		panelWidth = Math.min(460, this.width - 30);
		panelHeight = Math.min(300, this.height - 30);
		panelX = (this.width - panelWidth) / 2;
		panelY = (this.height - panelHeight) / 2;

		search.setBounds(panelX + panelWidth - PADDING - 118, panelY + 8, 118, 16);
		search.onChange(() -> scroll = 0);
		codeField.onSubmit(this::redeemCode);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	// ---------------------------------------------------------------
	// Hintergrund
	// ---------------------------------------------------------------

	@Override
	public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		extractBlurredBackground(g);
		g.fill(0, 0, this.width, this.height, Theme.OVERLAY);
	}

	// ---------------------------------------------------------------
	// Zeichnen
	// ---------------------------------------------------------------

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
		super.extractRenderState(g, mouseX, mouseY, partialTick);

		Draw.panel(g, panelX, panelY, panelWidth, panelHeight);

		renderSidebar(g, mouseX, mouseY);
		renderHeader(g, mouseX, mouseY);

		int contentX = panelX + Theme.SIDEBAR_WIDTH;
		int contentY = panelY + Theme.TAB_BAR_HEIGHT;
		int contentWidth = panelWidth - Theme.SIDEBAR_WIDTH;
		int viewHeight = panelHeight - Theme.TAB_BAR_HEIGHT - 14;

		g.enableScissor(contentX, contentY, contentX + contentWidth, contentY + viewHeight);

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

		Draw.scrollbar(g, panelX + panelWidth - 5, contentY + 2, viewHeight - 4, contentHeight, scroll);
		renderStatusBar(g);
	}

	/** Schmale Leiste links: Icon mit Mini-Label darunter. */
	private void renderSidebar(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		Draw.roundedRect(g, panelX, panelY, Theme.SIDEBAR_WIDTH, panelHeight, Theme.RADIUS, Theme.SIDEBAR);
		Draw.rect(g, panelX + Theme.SIDEBAR_WIDTH - 1, panelY + 4, 1, panelHeight - 8, Theme.BORDER);

		// Logo oben - zurueck zur Modul-Uebersicht
		int logoY = panelY + 10;
		boolean logoHovered = Draw.isHovered(mouseX, mouseY, panelX + 8, logoY - 2, 38, 22);

		int logoColor = PremiumManager.isPremium()
				? Theme.pulse(System.currentTimeMillis())
				: (logoHovered ? Theme.ACCENT : Theme.TEXT);

		Icons.draw(g, Icons.Icon.LIGHTNING, panelX + Theme.SIDEBAR_WIDTH / 2 - 6, logoY, 13, logoColor);
		Draw.textCentered(g, "VOIDRIX", panelX + Theme.SIDEBAR_WIDTH / 2, logoY + 16, Theme.TEXT_MUTED);

		int y = panelY + 44;

		for (int i = 0; i < SIDEBAR.length; i++) {
			Page entry = SIDEBAR[i];
			boolean selected = page == entry;
			boolean hovered = Draw.isHovered(mouseX, mouseY, panelX + 5, y, Theme.SIDEBAR_WIDTH - 10,
					SIDEBAR_ITEM_HEIGHT);

			if (selected) {
				Draw.roundedRect(g, panelX + 5, y, Theme.SIDEBAR_WIDTH - 10, SIDEBAR_ITEM_HEIGHT,
						Theme.RADIUS_SMALL, Theme.withAlpha(Theme.ACCENT, 40));
			} else if (hovered) {
				Draw.roundedRect(g, panelX + 5, y, Theme.SIDEBAR_WIDTH - 10, SIDEBAR_ITEM_HEIGHT,
						Theme.RADIUS_SMALL, Theme.withAlpha(0xFFFFFFFF, 120));
			}

			int color = selected ? Theme.ACCENT : (hovered ? Theme.TEXT : Theme.TEXT_DIM);

			Icons.draw(g, SIDEBAR_ICONS[i], panelX + Theme.SIDEBAR_WIDTH / 2 - 6, y + 5, 13, color);
			Draw.textCentered(g, SIDEBAR_LABELS[i], panelX + Theme.SIDEBAR_WIDTH / 2, y + 22,
					selected ? Theme.ACCENT : Theme.TEXT_MUTED);

			y += SIDEBAR_ITEM_HEIGHT + 2;
		}

		Draw.textCentered(g, "v" + Voidrix.VERSION, panelX + Theme.SIDEBAR_WIDTH / 2,
				panelY + panelHeight - 13, Theme.TEXT_MUTED);
	}

	/** Tabs, Zahnrad und Suchleiste. */
	private void renderHeader(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + 11;

		if (isTabPage()) {
			for (Page tab : TABS) {
				String label = Draw.upper(tab.title);
				int textWidth = Draw.textWidth(label);
				boolean selected = page == tab;
				boolean hovered = Draw.isHovered(mouseX, mouseY, x - 5, y - 5, textWidth + 10, 18);

				if (selected) {
					Draw.roundedRect(g, x - 5, y - 5, textWidth + 10, 18, Theme.RADIUS_SMALL,
							Theme.withAlpha(0xFFFFFFFF, 190));
					Draw.roundedOutline(g, x - 5, y - 5, textWidth + 10, 18, Theme.RADIUS_SMALL, Theme.BORDER);
				}

				Draw.textFlat(g, label, x, y, selected ? Theme.TEXT : (hovered ? Theme.TEXT_DIM : Theme.TEXT_MUTED));
				x += textWidth + 20;
			}

			// Zahnrad vor der Suchleiste
			int gearX = search.x - 20;
			boolean gearHovered = Draw.isHovered(mouseX, mouseY, gearX, y - 3, 14, 14);
			Icons.draw(g, Icons.Icon.GEAR, gearX, y - 2, 12, gearHovered ? Theme.ACCENT : Theme.TEXT_DIM);

			search.render(g, mouseX, mouseY);
		} else {
			Draw.textFlat(g, Draw.upper(page.title), x, y, Theme.TEXT);

			if (page == Page.PLUS && PremiumManager.isPremium()) {
				Draw.badge(g, "aktiv", x + Draw.textWidth(Draw.upper(page.title)) + 8, y - 2,
						Theme.withAlpha(Theme.SUCCESS, 60), Theme.SUCCESS);
			}
		}

		Draw.separator(g, panelX + Theme.SIDEBAR_WIDTH + 6, panelY + Theme.TAB_BAR_HEIGHT - 4,
				panelWidth - Theme.SIDEBAR_WIDTH - 12, Theme.BORDER);
	}

	/** Statuszeile unten rechts, wie "5 FRIENDS ONLINE" im Vorbild. */
	private void renderStatusBar(GuiGraphicsExtractor g) {
		var connection = Minecraft.getInstance().getConnection();

		int count = connection == null ? 0 : connection.getListedOnlinePlayers().size();
		String label = count == 0 ? "OFFLINE" : count + " SPIELER ONLINE";

		int x = panelX + panelWidth - PADDING - Draw.textWidth(label);
		int y = panelY + panelHeight - 11;

		Draw.roundedRect(g, x - 10, y - 1, 5, 5, 1, count == 0 ? Theme.TEXT_MUTED : Theme.SUCCESS);
		Draw.textFlat(g, label, x, y - 2, Theme.TEXT_MUTED);
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
		int gridY = panelY + Theme.TAB_BAR_HEIGHT + 4;
		int gridWidth = panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2 - 4;
		int cardWidth = (gridWidth - CARD_GAP) / 2;

		int rows = (modules.size() + 1) / 2;
		contentHeight = rows * (CARD_HEIGHT + CARD_GAP);

		if (modules.isEmpty()) {
			Draw.textFlat(g, "Keine Module gefunden.", gridX, gridY + 10, Theme.TEXT_MUTED);
			return;
		}

		for (int i = 0; i < modules.size(); i++) {
			int cardX = gridX + (i % 2) * (cardWidth + CARD_GAP);
			int cardY = (int) (gridY + (i / 2) * (CARD_HEIGHT + CARD_GAP) - scroll);

			if (cardY + CARD_HEIGHT < panelY + Theme.TAB_BAR_HEIGHT || cardY > panelY + panelHeight) {
				continue;
			}

			renderCard(g, modules.get(i), cardX, cardY, cardWidth, mouseX, mouseY);
		}
	}

	/** Eine Modul-Karte: Icon links, Titel gross, zwei Zeilen Beschreibung, "..." rechts. */
	private void renderCard(GuiGraphicsExtractor g, Module module, int x, int y, int width,
			int mouseX, int mouseY) {
		boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, CARD_HEIGHT);
		boolean locked = module.isLocked();
		boolean active = module.isEnabled();

		Draw.card(g, x, y, width, CARD_HEIGHT, hovered, active);

		// Icon links
		int iconColor = locked ? Theme.LOCKED : (active ? Theme.ACCENT : Theme.TEXT_DIM);
		Icons.draw(g, Icons.forModule(module.getId()), x + 8, y + 8, 14, iconColor);

		int textX = x + 28;
		int textWidth = width - 28 - 16;

		// Schloss bzw. NEU-Badge rechts oben. Die belegte Breite wird vom
		// Titel abgezogen, damit sich beides nie ueberlappt.
		int reserved = 0;

		if (locked) {
			reserved = Draw.textWidth("LOCK") + 6;
			Draw.textRight(g, "LOCK", x + width - 8, y + 7, Theme.LOCKED);
		} else if (module.isMarkedNew()) {
			int badgeWidth = Draw.textWidth("NEU") + 8;
			reserved = badgeWidth + 6;
			Draw.badge(g, "neu", x + width - 8 - badgeWidth, y + 5,
					Theme.withAlpha(Theme.ACCENT, 55), Theme.ACCENT);
		}

		// Titel in Grossbuchstaben
		String title = Draw.upper(module.getDisplayName());
		Draw.textFlat(g, Draw.truncate(title, textWidth - reserved), textX, y + 7,
				locked ? Theme.TEXT_MUTED : Theme.TEXT);

		// zweizeilige Beschreibung in Grau
		String[] lines = Draw.wrapTwoLines(module.getDescription(), textWidth);
		Draw.textFlat(g, lines[0], textX, y + 19, Theme.TEXT_DIM);

		if (!lines[1].isEmpty()) {
			Draw.textFlat(g, lines[1], textX, y + 29, Theme.TEXT_DIM);
		}

		// "..." oeffnet die Detailseite
		boolean dotsHovered = Draw.isHovered(mouseX, mouseY, x + width - 20, y + CARD_HEIGHT - 16, 16, 14);
		Draw.textFlat(g, "...", x + width - 16, y + CARD_HEIGHT - 15,
				dotsHovered ? Theme.ACCENT : Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Seite: Profiles
	// ---------------------------------------------------------------

	private void renderProfiles(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 8;
		contentHeight = 0;

		Draw.textFlat(g, "AKTIVE KONFIGURATION", x, y, Theme.TEXT);
		Draw.textFlat(g, ConfigManager.getConfigPath().getFileName().toString(), x, y + 14, Theme.ACCENT);

		Draw.textFlat(g, "GESPEICHERT UNTER", x, y + 36, Theme.TEXT);
		Draw.textFlat(g, Draw.truncate(ConfigManager.getConfigPath().toString(),
				panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2), x, y + 50, Theme.TEXT_DIM);

		Draw.textFlat(g, "Aenderungen werden sofort gespeichert.", x, y + 72, Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Seite: Waypoints
	// ---------------------------------------------------------------

	private void renderWaypoints(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		int width = panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2 - 4;

		List<Waypoint> waypoints = WaypointManager.getAll();
		contentHeight = waypoints.size() * 22;

		if (waypoints.isEmpty()) {
			Draw.textFlat(g, "Noch keine Waypoints gesetzt.", x, y + 4, Theme.TEXT_DIM);
			Draw.textFlat(g, "Im Spiel mit der Waypoint-Taste (Standard: B) setzen.",
					x, y + 16, Theme.TEXT_MUTED);
			return;
		}

		for (int i = 0; i < waypoints.size(); i++) {
			Waypoint waypoint = waypoints.get(i);
			int rowY = (int) (y + i * 22 - scroll);

			if (rowY + 20 < panelY + Theme.TAB_BAR_HEIGHT || rowY > panelY + panelHeight) {
				continue;
			}

			Draw.roundedRect(g, x, rowY, width, 20, Theme.RADIUS_SMALL, Theme.CARD);

			Draw.roundedRect(g, x + 7, rowY + 7, 6, 6, 1, waypoint.getColor());
			Draw.textFlat(g, Draw.truncate(waypoint.getName(), 120), x + 19, rowY + 6, Theme.TEXT);

			String coords = String.format("%.0f  %.0f  %.0f",
					waypoint.getX(), waypoint.getY(), waypoint.getZ());
			Draw.textRight(g, coords, x + width - 8, rowY + 6, Theme.TEXT_DIM);
		}
	}

	// ---------------------------------------------------------------
	// Seite: Voidrix+
	// ---------------------------------------------------------------

	private void renderPlus(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		contentHeight = 0;

		if (PremiumManager.isPremium()) {
			Draw.textFlat(g, "Voidrix+ ist aktiv.", x, y, Theme.SUCCESS);
			Draw.textFlat(g, "Alle gesperrten Module sind freigeschaltet und dein",
					x, y + 14, Theme.TEXT_DIM);
			Draw.textFlat(g, "Namens-V erscheint in der Premium-Variante.", x, y + 24, Theme.TEXT_DIM);
		} else {
			Draw.textFlat(g, "VOIDRIX+ FREISCHALTEN", x, y, Theme.TEXT);
			Draw.textFlat(g, "Loese deinen Code ein, um Premium-Module und das",
					x, y + 14, Theme.TEXT_DIM);
			Draw.textFlat(g, "hervorgehobene Namens-V zu erhalten.", x, y + 24, Theme.TEXT_DIM);
		}

		Draw.textFlat(g, "CODE EINLOESEN", x, y + 46, Theme.TEXT);

		codeField.setBounds(x, y + 58, 150, 18);
		codeField.render(g, mouseX, mouseY);

		int buttonX = x + 158;
		int buttonY = y + 58;
		boolean hovered = Draw.isHovered(mouseX, mouseY, buttonX, buttonY, 74, 18);

		Draw.roundedRect(g, buttonX, buttonY, 74, 18, Theme.RADIUS_SMALL,
				hovered ? Theme.ACCENT : Theme.withAlpha(Theme.ACCENT, 210));
		Draw.textCentered(g, "EINLOESEN", buttonX + 37, buttonY + 5, Theme.TEXT_ON_ACCENT);

		if (!codeMessage.isEmpty()) {
			Draw.textFlat(g, codeMessage, x, y + 82, codeSuccess ? Theme.SUCCESS : Theme.ERROR);
		}

		int listY = y + 102;
		Draw.textFlat(g, "ENTHALTEN", x, listY, Theme.TEXT);

		String[] perks = {
				"Premium-Module ohne Schloss",
				"Leuchtendes V mit Farbverlauf",
				"Cosmetics & Emotes"
		};

		for (int i = 0; i < perks.length; i++) {
			Icons.draw(g, Icons.Icon.SPARKLE, x + 2, listY + 13 + i * 11, 7, Theme.ACCENT);
			Draw.textFlat(g, perks[i], x + 14, listY + 13 + i * 11, Theme.TEXT_DIM);
		}
	}

	/** Prueft den eingegebenen Code und schaltet ggf. Voidrix+ frei. */
	private void redeemCode() {
		if (PremiumManager.redeem(codeField.getValue())) {
			codeSuccess = true;
			codeMessage = "Voidrix+ wurde freigeschaltet.";
			codeField.clear();
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
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		contentHeight = 0;

		Draw.textFlat(g, "Die 3D-Skin-Vorschau findest du als Modul unter \"Optik\".",
				x, y, Theme.TEXT_DIM);

		Draw.textFlat(g, "ANGEMELDET ALS", x, y + 24, Theme.TEXT);
		Draw.textFlat(g, Minecraft.getInstance().getUser().getName(), x, y + 38, Theme.ACCENT);

		if (!PremiumManager.isPremium()) {
			Draw.textFlat(g, "Cosmetics benoetigen Voidrix+.", x, y + 62, Theme.LOCKED);
		}
	}

	private void renderFriends(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		int width = panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2 - 4;

		var connection = Minecraft.getInstance().getConnection();

		if (connection == null) {
			contentHeight = 0;
			Draw.textFlat(g, "Nicht mit einem Server verbunden.", x, y, Theme.TEXT_DIM);
			return;
		}

		List<String> names = new ArrayList<>();

		for (var info : connection.getListedOnlinePlayers()) {
			names.add(info.getProfile().name());
		}

		contentHeight = names.size() * 18;

		for (int i = 0; i < names.size(); i++) {
			int rowY = (int) (y + i * 18 - scroll);

			if (rowY + 16 < panelY + Theme.TAB_BAR_HEIGHT || rowY > panelY + panelHeight) {
				continue;
			}

			Draw.roundedRect(g, x, rowY, width, 16, Theme.RADIUS_SMALL, Theme.CARD);
			Draw.roundedRect(g, x + 7, rowY + 5, 5, 5, 1, Theme.SUCCESS);
			Draw.textFlat(g, names.get(i), x + 18, rowY + 4, Theme.TEXT);
		}
	}

	private void renderEmotes(GuiGraphicsExtractor g) {
		int x = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		contentHeight = 0;

		Draw.textFlat(g, "Emotes sind Voidrix+ Inhalte.", x, y, Theme.TEXT_DIM);

		if (!PremiumManager.isPremium()) {
			Draw.textFlat(g, "Mit einem Code im Voidrix+ Bereich freischalten.",
					x, y + 16, Theme.LOCKED);
		}
	}

	// ---------------------------------------------------------------
	// Eingabe
	// ---------------------------------------------------------------

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
		double mouseX = event.x();
		double mouseY = event.y();

		// Logo -> zurueck zur Modulliste
		if (Draw.isHovered(mouseX, mouseY, panelX + 8, panelY + 8, 38, 22)) {
			page = Page.MODS;
			scroll = 0;
			return true;
		}

		// Sidebar
		int sidebarY = panelY + 44;

		for (Page entry : SIDEBAR) {
			if (Draw.isHovered(mouseX, mouseY, panelX + 5, sidebarY, Theme.SIDEBAR_WIDTH - 10,
					SIDEBAR_ITEM_HEIGHT)) {
				page = entry;
				scroll = 0;
				codeMessage = "";
				return true;
			}

			sidebarY += SIDEBAR_ITEM_HEIGHT + 2;
		}

		// Tabs und Suche
		if (isTabPage()) {
			int tabX = panelX + Theme.SIDEBAR_WIDTH + PADDING;
			int tabY = panelY + 11;

			for (Page tab : TABS) {
				int textWidth = Draw.textWidth(Draw.upper(tab.title));

				if (Draw.isHovered(mouseX, mouseY, tabX - 5, tabY - 5, textWidth + 10, 18)) {
					page = tab;
					scroll = 0;
					return true;
				}

				tabX += textWidth + 20;
			}

			if (search.mouseClicked(mouseX, mouseY)) {
				return true;
			}
		}

		if (page == Page.PLUS) {
			if (codeField.mouseClicked(mouseX, mouseY)) {
				return true;
			}

			int buttonX = panelX + Theme.SIDEBAR_WIDTH + PADDING + 158;
			int buttonY = panelY + Theme.TAB_BAR_HEIGHT + 6 + 58;

			if (Draw.isHovered(mouseX, mouseY, buttonX, buttonY, 74, 18)) {
				redeemCode();
				return true;
			}
		}

		if (page == Page.MODS && handleCardClick(mouseX, mouseY)) {
			return true;
		}

		return super.mouseClicked(event, doubled);
	}

	/** Klick auf "..." oeffnet die Detailseite, Klick auf die Karte schaltet um. */
	private boolean handleCardClick(double mouseX, double mouseY) {
		List<Module> modules = ModuleManager.search(search.getValue());

		int gridX = panelX + Theme.SIDEBAR_WIDTH + PADDING;
		int gridY = panelY + Theme.TAB_BAR_HEIGHT + 4;
		int gridWidth = panelWidth - Theme.SIDEBAR_WIDTH - PADDING * 2 - 4;
		int cardWidth = (gridWidth - CARD_GAP) / 2;

		for (int i = 0; i < modules.size(); i++) {
			int cardX = gridX + (i % 2) * (cardWidth + CARD_GAP);
			int cardY = (int) (gridY + (i / 2) * (CARD_HEIGHT + CARD_GAP) - scroll);

			if (!Draw.isHovered(mouseX, mouseY, cardX, cardY, cardWidth, CARD_HEIGHT)) {
				continue;
			}

			Module module = modules.get(i);

			if (Draw.isHovered(mouseX, mouseY, cardX + cardWidth - 20, cardY + CARD_HEIGHT - 16, 16, 14)) {
				this.minecraft.gui.setScreen(new ModuleDetailScreen(this, module));
				return true;
			}

			module.toggle();
			ConfigManager.save();
			return true;
		}

		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		int viewHeight = panelHeight - Theme.TAB_BAR_HEIGHT - 14;
		double maxScroll = Math.max(0, contentHeight - viewHeight);

		scroll = Math.max(0, Math.min(maxScroll, scroll - scrollY * 18));
		return true;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
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
		ConfigManager.save();
		super.onClose();
	}
}
