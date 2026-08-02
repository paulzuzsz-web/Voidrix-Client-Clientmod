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
 *  +--+  +---------------------------------------------+
 *  | /|  | [MODS] PROFILES WAYPOINTS     * [Suchen... ] |
 *  |  |  |---------------------------------------------|
 *  | +|  |  +---------------+  +---------------+     | |
 *  |  |  |  |[i] TITEL   ...|  |[i] TITEL   ...|     | |
 *  | T|  |  |    zwei Zeilen|  |    zwei Zeilen|     | |
 *  +--+  +---------------------------------------------+
 *                                      [] 3 SPIELER ONLINE
 * </pre>
 *
 * <p>Sidebar und Inhalt sind <b>zwei getrennte Panels</b> mit einem Spalt
 * dazwischen - das praegt die Bildsprache. Helles Frosted Glass ueber dem
 * geblurrten Spiel, runde Ecken, Beschriftungen in Grossbuchstaben; Violett
 * markiert ausschliesslich aktive Zustaende.</p>
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

	private static final Page[] TABS = { Page.MODS, Page.PROFILES, Page.WAYPOINTS };

	private static final Page[] SIDEBAR = { Page.PLUS, Page.COSMETICS, Page.FRIENDS, Page.EMOTES };

	private static final Icons.Icon[] SIDEBAR_ICONS = {
			Icons.Icon.LIGHTNING, Icons.Icon.SHIRT, Icons.Icon.FRIENDS, Icons.Icon.EMOTE
	};

	private static final String[] SIDEBAR_LABELS = { "VDX+", "COSM", "FRND", "EMOT" };

	/** Groessere, luftigere Karten - naeher am Vorbild. */
	private static final int CARD_HEIGHT = 48;
	private static final int CARD_GAP = 7;
	private static final int PADDING = 12;
	private static final int SIDEBAR_ITEM_HEIGHT = 36;

	private Page page;

	private final TextField search = new TextField("Suchen ...", 32);
	private final TextField codeField = new TextField("Code eingeben", 64);

	private String codeMessage = "";
	private boolean codeSuccess;

	private double scroll;
	private int contentHeight;

	// Gesamtflaeche
	private int panelX;
	private int panelY;
	private int panelWidth;
	private int panelHeight;

	// Inhaltspanel (rechts neben der Sidebar)
	private int contentX;
	private int contentWidth;

	public VoidrixMenuScreen() {
		this(Page.MODS);
	}

	public VoidrixMenuScreen(Page initialPage) {
		super(Component.literal(Voidrix.MOD_NAME));
		this.page = initialPage;
	}

	@Override
	protected void init() {
		panelWidth = Math.min(470, this.width - 24);
		panelHeight = Math.min(310, this.height - 24);
		panelX = (this.width - panelWidth) / 2;
		panelY = (this.height - panelHeight) / 2;

		contentX = panelX + Theme.SIDEBAR_WIDTH + Theme.SIDEBAR_GAP;
		contentWidth = panelWidth - Theme.SIDEBAR_WIDTH - Theme.SIDEBAR_GAP;

		search.setBounds(contentX + contentWidth - PADDING - 116, panelY + 8, 116, 16);
		search.onChange(() -> scroll = 0);
		codeField.onSubmit(this::redeemCode);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

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

		// Zwei getrennte Panels mit Spalt dazwischen
		Draw.roundedRect(g, panelX, panelY, Theme.SIDEBAR_WIDTH, panelHeight, Theme.RADIUS, Theme.SIDEBAR);
		Draw.roundedOutline(g, panelX, panelY, Theme.SIDEBAR_WIDTH, panelHeight, Theme.RADIUS, Theme.BORDER);

		Draw.panel(g, contentX, panelY, contentWidth, panelHeight);

		renderSidebar(g, mouseX, mouseY);
		renderHeader(g, mouseX, mouseY);

		int viewTop = panelY + Theme.TAB_BAR_HEIGHT;
		int viewHeight = panelHeight - Theme.TAB_BAR_HEIGHT - 14;

		g.enableScissor(contentX, viewTop, contentX + contentWidth, viewTop + viewHeight);

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

		Draw.scrollbar(g, contentX + contentWidth - 5, viewTop + 2, viewHeight - 4, contentHeight, scroll);
		renderStatusBar(g);
	}

	/** Eigenstaendiges Panel links: Icon mit Mini-Label darunter. */
	private void renderSidebar(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int centerX = panelX + Theme.SIDEBAR_WIDTH / 2;

		// Logo oben - zurueck zur Modul-Uebersicht
		int logoY = panelY + 12;
		boolean logoHovered = Draw.isHovered(mouseX, mouseY, panelX + 4, logoY - 4, Theme.SIDEBAR_WIDTH - 8, 26);

		int logoColor = PremiumManager.isPremium()
				? Theme.pulse(System.currentTimeMillis())
				: (logoHovered ? Theme.ACCENT : Theme.TEXT);

		Icons.draw(g, Icons.Icon.LIGHTNING, centerX - 7, logoY, 15, logoColor);

		Draw.separator(g, panelX + 8, panelY + 38, Theme.SIDEBAR_WIDTH - 16, Theme.BORDER);

		int y = panelY + 48;

		for (int i = 0; i < SIDEBAR.length; i++) {
			Page entry = SIDEBAR[i];
			boolean selected = page == entry;
			boolean hovered = Draw.isHovered(mouseX, mouseY, panelX + 4, y, Theme.SIDEBAR_WIDTH - 8,
					SIDEBAR_ITEM_HEIGHT);

			if (selected) {
				Draw.roundedRect(g, panelX + 4, y, Theme.SIDEBAR_WIDTH - 8, SIDEBAR_ITEM_HEIGHT,
						Theme.RADIUS_SMALL, Theme.withAlpha(Theme.ACCENT, 45));
				Draw.roundedOutline(g, panelX + 4, y, Theme.SIDEBAR_WIDTH - 8, SIDEBAR_ITEM_HEIGHT,
						Theme.RADIUS_SMALL, Theme.withAlpha(Theme.ACCENT, 120));
			} else if (hovered) {
				Draw.roundedRect(g, panelX + 4, y, Theme.SIDEBAR_WIDTH - 8, SIDEBAR_ITEM_HEIGHT,
						Theme.RADIUS_SMALL, Theme.withAlpha(0xFFFFFFFF, 130));
			}

			int color = selected ? Theme.ACCENT : (hovered ? Theme.TEXT : Theme.TEXT_DIM);

			Icons.draw(g, SIDEBAR_ICONS[i], centerX - 7, y + 6, 15, color);

			// Mini-Label unter dem Icon, bewusst sehr klein
			Draw.textScaledCentered(g, SIDEBAR_LABELS[i], centerX, y + 24,
					selected ? Theme.ACCENT : Theme.TEXT_MUTED, 0.75f, false);

			y += SIDEBAR_ITEM_HEIGHT + 3;
		}

		Draw.textScaledCentered(g, "v" + Voidrix.VERSION, centerX, panelY + panelHeight - 12,
				Theme.TEXT_MUTED, 0.75f, false);
	}

	/** Tabs, Icon und Suchleiste. */
	private void renderHeader(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = contentX + PADDING;
		int y = panelY + 11;

		if (isTabPage()) {
			for (Page tab : TABS) {
				String label = Draw.upper(tab.title);
				int textWidth = Draw.textWidth(label);
				boolean selected = page == tab;
				boolean hovered = Draw.isHovered(mouseX, mouseY, x - 6, y - 5, textWidth + 12, 18);

				if (selected) {
					Draw.roundedRect(g, x - 6, y - 5, textWidth + 12, 18, Theme.RADIUS_SMALL, 0xF2FFFFFF);
					Draw.roundedOutline(g, x - 6, y - 5, textWidth + 12, 18, Theme.RADIUS_SMALL,
							Theme.BORDER_STRONG);
				}

				Draw.textFlat(g, label, x, y, selected ? Theme.TEXT : (hovered ? Theme.TEXT_DIM : Theme.TEXT_MUTED));
				x += textWidth + 22;
			}

			// kleines Symbol vor der Suchleiste
			int markX = search.x - 18;
			boolean markHovered = Draw.isHovered(mouseX, mouseY, markX, y - 3, 14, 14);
			Icons.draw(g, Icons.Icon.SPARKLE, markX, y - 1, 11, markHovered ? Theme.ACCENT : Theme.TEXT_MUTED);

			search.render(g, mouseX, mouseY);
		} else {
			Draw.textFlat(g, Draw.upper(page.title), x, y, Theme.TEXT);

			if (page == Page.PLUS && PremiumManager.isPremium()) {
				Draw.badge(g, "aktiv", x + Draw.textWidth(Draw.upper(page.title)) + 8, y - 2,
						Theme.withAlpha(Theme.SUCCESS, 60), Theme.SUCCESS);
			}
		}

		Draw.separator(g, contentX + 8, panelY + Theme.TAB_BAR_HEIGHT - 4, contentWidth - 16, Theme.BORDER);
	}

	/** Statuszeile unten rechts im Inhaltspanel. */
	private void renderStatusBar(GuiGraphicsExtractor g) {
		var connection = Minecraft.getInstance().getConnection();

		int count = connection == null ? 0 : connection.getListedOnlinePlayers().size();
		String label = count == 0 ? "OFFLINE" : count + " SPIELER ONLINE";

		int scaledWidth = Draw.textWidthScaled(label, 0.8f);
		int x = contentX + contentWidth - PADDING - scaledWidth;
		int y = panelY + panelHeight - 12;

		Draw.roundedRect(g, x - 9, y + 1, 5, 5, 1, count == 0 ? Theme.TEXT_MUTED : Theme.SUCCESS);
		Draw.textScaled(g, label, x, y, Theme.TEXT_MUTED, 0.8f, false);
	}

	private boolean isTabPage() {
		return page == Page.MODS || page == Page.PROFILES || page == Page.WAYPOINTS;
	}

	// ---------------------------------------------------------------
	// Seite: Mods (Karten-Grid)
	// ---------------------------------------------------------------

	private void renderModuleGrid(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		List<Module> modules = ModuleManager.search(search.getValue());

		int gridX = contentX + PADDING;
		int gridY = panelY + Theme.TAB_BAR_HEIGHT + 4;
		int gridWidth = contentWidth - PADDING * 2 - 4;
		int cardWidth = (gridWidth - CARD_GAP) / 2;

		contentHeight = ((modules.size() + 1) / 2) * (CARD_HEIGHT + CARD_GAP);

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

	/** Eine Modul-Karte: grosses Icon links, Titel, zwei Zeilen Text, "..." rechts mittig. */
	private void renderCard(GuiGraphicsExtractor g, Module module, int x, int y, int width,
			int mouseX, int mouseY) {
		boolean hovered = Draw.isHovered(mouseX, mouseY, x, y, width, CARD_HEIGHT);
		boolean locked = module.isLocked();
		boolean active = module.isEnabled();

		Draw.card(g, x, y, width, CARD_HEIGHT, hovered, active);

		// Icon links, senkrecht mittig
		int iconColor = locked ? Theme.LOCKED : (active ? Theme.ACCENT : Theme.TEXT_DIM);
		Icons.draw(g, Icons.forModule(module.getId()), x + 9, y + CARD_HEIGHT / 2 - 8, 17, iconColor);

		int textX = x + 32;
		int dotsWidth = 14;
		int textWidth = width - (textX - x) - dotsWidth - 8;

		// Schloss bzw. NEU-Badge rechts oben; belegte Breite vom Titel abziehen
		int reserved = 0;

		if (locked) {
			reserved = Draw.textWidthScaled("LOCK", 0.8f) + 8;
			Draw.textScaled(g, "LOCK", x + width - dotsWidth - reserved, y + 8, Theme.LOCKED, 0.8f, false);
		} else if (module.isMarkedNew()) {
			int badgeWidth = Draw.textWidth("NEU") + 8;
			reserved = badgeWidth + 6;
			Draw.badge(g, "neu", x + width - dotsWidth - badgeWidth - 4, y + 6,
					Theme.withAlpha(Theme.ACCENT, 55), Theme.ACCENT);
		}

		// Titel etwas groesser als der Fliesstext
		String title = Draw.upper(module.getDisplayName());
		Draw.textScaled(g, Draw.truncate(title, Math.round((textWidth - reserved) / 1.1f)),
				textX, y + 8, locked ? Theme.TEXT_MUTED : Theme.TEXT, 1.1f, false);

		// zweizeilige Beschreibung, kleiner und grau
		String[] lines = Draw.wrapTwoLines(module.getDescription(), Math.round(textWidth / 0.85f));
		Draw.textScaled(g, lines[0], textX, y + 24, Theme.TEXT_DIM, 0.85f, false);

		if (!lines[1].isEmpty()) {
			Draw.textScaled(g, lines[1], textX, y + 33, Theme.TEXT_DIM, 0.85f, false);
		}

		// "..." rechts, senkrecht mittig - oeffnet die Detailseite
		boolean dotsHovered = Draw.isHovered(mouseX, mouseY, x + width - 20, y + CARD_HEIGHT / 2 - 8, 18, 16);
		Draw.textFlat(g, "...", x + width - 16, y + CARD_HEIGHT / 2 - 5,
				dotsHovered ? Theme.ACCENT : Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Seite: Profiles
	// ---------------------------------------------------------------

	private void renderProfiles(GuiGraphicsExtractor g) {
		int x = contentX + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 8;
		contentHeight = 0;

		Draw.textFlat(g, "AKTIVE KONFIGURATION", x, y, Theme.TEXT);
		Draw.textFlat(g, ConfigManager.getConfigPath().getFileName().toString(), x, y + 14, Theme.ACCENT);

		Draw.textFlat(g, "GESPEICHERT UNTER", x, y + 36, Theme.TEXT);
		Draw.textScaled(g, Draw.truncate(ConfigManager.getConfigPath().toString(),
				Math.round((contentWidth - PADDING * 2) / 0.85f)), x, y + 50, Theme.TEXT_DIM, 0.85f, false);

		Draw.textFlat(g, "Aenderungen werden sofort gespeichert.", x, y + 72, Theme.TEXT_MUTED);
	}

	// ---------------------------------------------------------------
	// Seite: Waypoints
	// ---------------------------------------------------------------

	private void renderWaypoints(GuiGraphicsExtractor g) {
		int x = contentX + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		int width = contentWidth - PADDING * 2 - 4;

		List<Waypoint> waypoints = WaypointManager.getAll();
		contentHeight = waypoints.size() * 24;

		if (waypoints.isEmpty()) {
			Draw.textFlat(g, "Noch keine Waypoints gesetzt.", x, y + 4, Theme.TEXT_DIM);
			Draw.textScaled(g, "Im Spiel mit der Waypoint-Taste (Standard: B) setzen.",
					x, y + 18, Theme.TEXT_MUTED, 0.85f, false);
			return;
		}

		for (int i = 0; i < waypoints.size(); i++) {
			Waypoint waypoint = waypoints.get(i);
			int rowY = (int) (y + i * 24 - scroll);

			if (rowY + 22 < panelY + Theme.TAB_BAR_HEIGHT || rowY > panelY + panelHeight) {
				continue;
			}

			Draw.roundedRect(g, x, rowY, width, 22, Theme.RADIUS_SMALL, Theme.CARD);
			Draw.roundedOutline(g, x, rowY, width, 22, Theme.RADIUS_SMALL, Theme.BORDER);

			Draw.roundedRect(g, x + 8, rowY + 8, 6, 6, 1, waypoint.getColor());
			Draw.textFlat(g, Draw.truncate(waypoint.getName(), 120), x + 20, rowY + 7, Theme.TEXT);

			String coords = String.format("%.0f  %.0f  %.0f",
					waypoint.getX(), waypoint.getY(), waypoint.getZ());
			Draw.textScaled(g, coords, x + width - 8 - Draw.textWidthScaled(coords, 0.85f), rowY + 8,
					Theme.TEXT_DIM, 0.85f, false);
		}
	}

	// ---------------------------------------------------------------
	// Seite: Voidrix+
	// ---------------------------------------------------------------

	private void renderPlus(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = contentX + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		contentHeight = 0;

		if (PremiumManager.isPremium()) {
			Draw.textFlat(g, "Voidrix+ ist aktiv.", x, y, Theme.SUCCESS);
			Draw.textScaled(g, "Alle gesperrten Module sind freigeschaltet und dein",
					x, y + 15, Theme.TEXT_DIM, 0.85f, false);
			Draw.textScaled(g, "Namens-V erscheint in der Premium-Variante.",
					x, y + 25, Theme.TEXT_DIM, 0.85f, false);
		} else {
			Draw.textFlat(g, "VOIDRIX+ FREISCHALTEN", x, y, Theme.TEXT);
			Draw.textScaled(g, "Loese deinen Code ein, um Premium-Module und das",
					x, y + 15, Theme.TEXT_DIM, 0.85f, false);
			Draw.textScaled(g, "hervorgehobene Namens-V zu erhalten.",
					x, y + 25, Theme.TEXT_DIM, 0.85f, false);
		}

		Draw.textFlat(g, "CODE EINLOESEN", x, y + 48, Theme.TEXT);

		codeField.setBounds(x, y + 62, 150, 18);
		codeField.render(g, mouseX, mouseY);

		int buttonX = x + 158;
		int buttonY = y + 62;
		boolean hovered = Draw.isHovered(mouseX, mouseY, buttonX, buttonY, 74, 18);

		Draw.roundedRect(g, buttonX, buttonY, 74, 18, Theme.RADIUS_SMALL,
				hovered ? Theme.ACCENT : Theme.withAlpha(Theme.ACCENT, 210));
		Draw.textCentered(g, "EINLOESEN", buttonX + 37, buttonY + 5, Theme.TEXT_ON_ACCENT);

		if (!codeMessage.isEmpty()) {
			Draw.textFlat(g, codeMessage, x, y + 88, codeSuccess ? Theme.SUCCESS : Theme.ERROR);
		}

		int listY = y + 110;
		Draw.textFlat(g, "ENTHALTEN", x, listY, Theme.TEXT);

		String[] perks = {
				"Premium-Module ohne Schloss",
				"Leuchtendes V mit Farbverlauf",
				"Cosmetics & Emotes"
		};

		for (int i = 0; i < perks.length; i++) {
			Icons.draw(g, Icons.Icon.SPARKLE, x + 2, listY + 14 + i * 12, 8, Theme.ACCENT);
			Draw.textScaled(g, perks[i], x + 15, listY + 15 + i * 12, Theme.TEXT_DIM, 0.85f, false);
		}
	}

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
		int x = contentX + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		contentHeight = 0;

		Draw.textScaled(g, "Die 3D-Skin-Vorschau findest du als Modul unter \"Optik\".",
				x, y, Theme.TEXT_DIM, 0.85f, false);

		Draw.textFlat(g, "ANGEMELDET ALS", x, y + 24, Theme.TEXT);
		Draw.textFlat(g, Minecraft.getInstance().getUser().getName(), x, y + 38, Theme.ACCENT);

		if (!PremiumManager.isPremium()) {
			Draw.textFlat(g, "Cosmetics benoetigen Voidrix+.", x, y + 62, Theme.LOCKED);
		}
	}

	private void renderFriends(GuiGraphicsExtractor g) {
		int x = contentX + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		int width = contentWidth - PADDING * 2 - 4;

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

		contentHeight = names.size() * 20;

		for (int i = 0; i < names.size(); i++) {
			int rowY = (int) (y + i * 20 - scroll);

			if (rowY + 18 < panelY + Theme.TAB_BAR_HEIGHT || rowY > panelY + panelHeight) {
				continue;
			}

			Draw.roundedRect(g, x, rowY, width, 18, Theme.RADIUS_SMALL, Theme.CARD);
			Draw.roundedOutline(g, x, rowY, width, 18, Theme.RADIUS_SMALL, Theme.BORDER);
			Draw.roundedRect(g, x + 8, rowY + 6, 5, 5, 1, Theme.SUCCESS);
			Draw.textFlat(g, names.get(i), x + 20, rowY + 5, Theme.TEXT);
		}
	}

	private void renderEmotes(GuiGraphicsExtractor g) {
		int x = contentX + PADDING;
		int y = panelY + Theme.TAB_BAR_HEIGHT + 6;
		contentHeight = 0;

		Draw.textFlat(g, "Emotes sind Voidrix+ Inhalte.", x, y, Theme.TEXT_DIM);

		if (!PremiumManager.isPremium()) {
			Draw.textScaled(g, "Mit einem Code im Voidrix+ Bereich freischalten.",
					x, y + 16, Theme.LOCKED, 0.85f, false);
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
		if (Draw.isHovered(mouseX, mouseY, panelX + 4, panelY + 8, Theme.SIDEBAR_WIDTH - 8, 26)) {
			page = Page.MODS;
			scroll = 0;
			return true;
		}

		int sidebarY = panelY + 48;

		for (Page entry : SIDEBAR) {
			if (Draw.isHovered(mouseX, mouseY, panelX + 4, sidebarY, Theme.SIDEBAR_WIDTH - 8,
					SIDEBAR_ITEM_HEIGHT)) {
				page = entry;
				scroll = 0;
				codeMessage = "";
				return true;
			}

			sidebarY += SIDEBAR_ITEM_HEIGHT + 3;
		}

		if (isTabPage()) {
			int tabX = contentX + PADDING;
			int tabY = panelY + 11;

			for (Page tab : TABS) {
				int textWidth = Draw.textWidth(Draw.upper(tab.title));

				if (Draw.isHovered(mouseX, mouseY, tabX - 6, tabY - 5, textWidth + 12, 18)) {
					page = tab;
					scroll = 0;
					return true;
				}

				tabX += textWidth + 22;
			}

			if (search.mouseClicked(mouseX, mouseY)) {
				return true;
			}
		}

		if (page == Page.PLUS) {
			if (codeField.mouseClicked(mouseX, mouseY)) {
				return true;
			}

			int buttonX = contentX + PADDING + 158;
			int buttonY = panelY + Theme.TAB_BAR_HEIGHT + 6 + 62;

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

	private boolean handleCardClick(double mouseX, double mouseY) {
		List<Module> modules = ModuleManager.search(search.getValue());

		int gridX = contentX + PADDING;
		int gridY = panelY + Theme.TAB_BAR_HEIGHT + 4;
		int gridWidth = contentWidth - PADDING * 2 - 4;
		int cardWidth = (gridWidth - CARD_GAP) / 2;

		for (int i = 0; i < modules.size(); i++) {
			int cardX = gridX + (i % 2) * (cardWidth + CARD_GAP);
			int cardY = (int) (gridY + (i / 2) * (CARD_HEIGHT + CARD_GAP) - scroll);

			if (!Draw.isHovered(mouseX, mouseY, cardX, cardY, cardWidth, CARD_HEIGHT)) {
				continue;
			}

			Module module = modules.get(i);

			if (Draw.isHovered(mouseX, mouseY, cardX + cardWidth - 20, cardY + CARD_HEIGHT / 2 - 8, 18, 16)) {
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
