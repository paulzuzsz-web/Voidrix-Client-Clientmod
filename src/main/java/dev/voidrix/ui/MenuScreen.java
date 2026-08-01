package dev.voidrix.ui;

import dev.voidrix.VoidrixClient;
import dev.voidrix.module.Category;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.setting.DoubleSetting;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.setting.Setting;
import dev.voidrix.setting.StringSetting;
import dev.voidrix.util.Keyboard;
import dev.voidrix.waypoint.Waypoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The Voidrix control panel.
 *
 * <p>An icon rail down the left picks the module category, tabs across the top switch between
 * modules, profiles and waypoints, and the body is a grid of cards. Clicking a card opens that
 * module's settings in place; the toggle on the card flips it without leaving the grid.
 *
 * <p>Everything is drawn by hand through {@link Draw} and {@link Icons} rather than assembled from
 * vanilla widgets or shipped textures, which is what lets the whole surface share one visual
 * language and re-colour itself instantly when the theme changes.
 */
public final class MenuScreen extends Screen {
    private static final int RAIL_W = 44;
    private static final int HEADER_H = 40;
    private static final int CARD_H = 46;

    private enum Tab {
        MODS, PROFILES, WAYPOINTS
    }

    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    private Tab tab = Tab.MODS;
    /** Category shown in the grid; null means "every category". */
    private Category filter = Category.HUD;
    /** When set, the grid is replaced by this module's settings. */
    private Module selected;

    private String search = "";
    private boolean searchFocused;
    private String profileInput = "";
    private boolean profileFocused;

    private float gridScroll;
    private float detailScroll;

    private Setting<?> dragging;
    private int dragChannel = -1;
    private Module bindingModule;
    private StringSetting focusedText;

    private final Map<String, Anim> anims = new HashMap<>();
    private final Anim opening = new Anim(0f, 0.16f);
    private long lastFrame = System.nanoTime();

    public MenuScreen() {
        super(Component.literal("Voidrix"));
    }

    @Override
    protected void init() {
        panelW = Math.min(600, width - 20);
        panelH = Math.min(350, height - 20);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        opening.target(1f);
    }

    private Anim anim(String key) {
        return anims.computeIfAbsent(key, k -> new Anim(0f));
    }

    private static List<Category> categories() {
        List<Category> out = new ArrayList<>();
        for (Category category : Category.values()) {
            if (!VoidrixClient.modules().byCategory(category).isEmpty()) {
                out.add(category);
            }
        }
        return out;
    }

    /** The modules the grid should show, honouring the category rail and the search box. */
    private List<Module> visibleModules() {
        String query = search.trim().toLowerCase(Locale.ROOT);
        List<Module> out = new ArrayList<>();
        for (Module module : VoidrixClient.modules().all()) {
            if (!query.isEmpty()) {
                // A search looks across every category - filtering twice only hides results.
                boolean hit = module.displayName().toLowerCase(Locale.ROOT).contains(query)
                        || module.description().toLowerCase(Locale.ROOT).contains(query);
                if (!hit) {
                    continue;
                }
            } else if (filter != null && module.category() != filter) {
                continue;
            }
            out.add(module);
        }
        return out;
    }

    // -------------------------------------------------------------------------------------
    // Render
    // -------------------------------------------------------------------------------------

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        long now = System.nanoTime();
        float dt = Math.clamp((now - lastFrame) / 1_000_000_000f, 0f, 0.2f);
        lastFrame = now;
        float open = Anim.easeOut(opening.update(dt));

        g.fill(0, 0, width, height, Theme.alpha(0xFF05050A, 0.66f * open));
        g.nextStratum();

        int px = panelX;
        int py = panelY + Math.round((1f - open) * 12f);

        Draw.shadow(g, px, py, panelW, panelH, Theme.RADIUS_LG, 14f, 0xFF000000);
        Draw.roundRectGradient(g, px, py, panelW, panelH, Theme.RADIUS_LG,
                Theme.alpha(Theme.SURFACE, open), Theme.alpha(Theme.VOID, open));
        Draw.roundRectOutline(g, px, py, panelW, panelH, Theme.RADIUS_LG, 1f,
                Theme.alpha(Theme.BORDER, open));

        renderRail(g, px, py, panelH, mouseX, mouseY, dt);
        renderHeader(g, px + RAIL_W, py, panelW - RAIL_W, mouseX, mouseY, dt);

        int bodyX = px + RAIL_W;
        int bodyY = py + HEADER_H;
        int bodyW = panelW - RAIL_W;
        int bodyH = panelH - HEADER_H;

        switch (tab) {
            case MODS -> {
                if (selected == null) {
                    renderGrid(g, bodyX, bodyY, bodyW, bodyH, mouseX, mouseY, dt);
                } else {
                    renderDetail(g, bodyX, bodyY, bodyW, bodyH, mouseX, mouseY, dt);
                }
            }
            case PROFILES -> renderProfiles(g, bodyX, bodyY, bodyW, bodyH, mouseX, mouseY, dt);
            case WAYPOINTS -> renderWaypoints(g, bodyX, bodyY, bodyW, bodyH, mouseX, mouseY);
        }
    }

    private void renderRail(GuiGraphicsExtractor g, int x, int y, int h,
                            int mouseX, int mouseY, float dt) {
        Draw.vLine(g, x + RAIL_W - 1, y + 1, h - 2, Theme.BORDER_SOFT);

        // Brand mark at the top of the rail.
        Icons.brand(g, x + (RAIL_W - 20) / 2f, y + 10, 20);

        int cy = y + 44;
        for (Category category : categories()) {
            boolean active = tab == Tab.MODS && search.isBlank() && filter == category;
            boolean hovered = inside(mouseX, mouseY, x + 6, cy, RAIL_W - 12, 32);

            Anim a = anim("rail:" + category.name());
            a.target(active ? 1f : (hovered ? 0.5f : 0f));
            float t = a.update(dt);

            if (t > 0.001f) {
                Draw.roundRect(g, x + 6, cy, RAIL_W - 12, 32, Theme.RADIUS_MD,
                        Theme.mix(0x00000000, Theme.SURFACE_HOVER, t));
            }
            if (active) {
                Draw.roundRect(g, x + 1, cy + 8, 2.5f, 16, 1.25f, Theme.ACCENT);
            }

            int tint = Theme.mix(Theme.TEXT_FAINT, Theme.ACCENT, t);
            Icons.forCategory(g, category, x + (RAIL_W - 16) / 2f, cy + 5, 16, tint);
            Draw.textCentered(g, font, shortLabel(category), x + RAIL_W / 2f, cy + 23,
                    Theme.mix(Theme.TEXT_FAINT, Theme.TEXT_DIM, t));
            cy += 36;
        }
    }

    /** Three or four characters, so a category label fits the narrow rail. */
    private static String shortLabel(Category category) {
        return switch (category) {
            case HUD -> "HUD";
            case COMBAT -> "PvP";
            case VISUAL -> "View";
            case INTERFACE -> "UI";
            case MISC -> "More";
        };
    }

    private void renderHeader(GuiGraphicsExtractor g, int x, int y, int w,
                              int mouseX, int mouseY, float dt) {
        int tx = x + Theme.PAD;
        for (Tab value : Tab.values()) {
            String label = value.name();
            int tw = font.width(label) + 16;
            boolean active = tab == value;
            boolean hovered = inside(mouseX, mouseY, tx, y + 10, tw, 20);

            Anim a = anim("tab:" + value.name());
            a.target(active ? 1f : (hovered ? 0.4f : 0f));
            float t = a.update(dt);

            if (t > 0.001f) {
                Draw.roundRect(g, tx, y + 10, tw, 20, Theme.RADIUS_SM,
                        Theme.mix(0x00000000, Theme.SURFACE_HOVER, t));
            }
            Draw.textCentered(g, font, label, tx + tw / 2f, y + 16,
                    active ? Theme.TEXT : Theme.mix(Theme.TEXT_FAINT, Theme.TEXT_DIM, t));
            if (active) {
                Draw.roundRect(g, tx + 4, y + 31, tw - 8, 2, 1f, Theme.ACCENT);
            }
            tx += tw + 4;
        }

        // Search box, only meaningful on the modules tab.
        if (tab == Tab.MODS) {
            int sw = Math.min(150, Math.max(90, w - (tx - x) - Theme.PAD * 2));
            float sx = x + w - Theme.PAD - sw;
            Draw.roundRect(g, sx, y + 10, sw, 20, 10f,
                    searchFocused ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED);
            Draw.roundRectOutline(g, sx, y + 10, sw, 20, 10f, 1f,
                    Theme.alpha(Theme.ACCENT, searchFocused ? 0.7f : 0.18f));
            Icons.search(g, sx + 6, y + 15, 10, Theme.TEXT_FAINT);

            String shown = search.isEmpty() ? "Search" : search;
            int color = search.isEmpty() ? Theme.TEXT_FAINT : Theme.TEXT;
            Draw.text(g, font, Draw.ellipsize(font, shown, sw - 26), sx + 20, y + 16, color);
            if (searchFocused && (System.currentTimeMillis() / 500L) % 2 == 0) {
                Draw.rect(g, sx + 20 + font.width(search) + 1, y + 15, 1, 10, Theme.ACCENT);
            }
        }

        Draw.hLine(g, x, y + HEADER_H - 1, w - 1, Theme.BORDER_SOFT);
    }

    // ---- Card grid ------------------------------------------------------------------------

    private int cardWidth(int bodyW) {
        return (bodyW - Theme.PAD * 2 - Theme.GAP) / 2;
    }

    private void renderGrid(GuiGraphicsExtractor g, int x, int y, int w, int h,
                            int mouseX, int mouseY, float dt) {
        List<Module> modules = visibleModules();
        if (modules.isEmpty()) {
            Draw.textCentered(g, font, "Nothing matches that", x + w / 2f, y + h / 2f - 4, Theme.TEXT_FAINT);
            return;
        }

        int cardW = cardWidth(w);
        int rows = (modules.size() + 1) / 2;
        int contentH = rows * (CARD_H + Theme.GAP) + Theme.PAD;
        gridScroll = Math.clamp(gridScroll, 0f, Math.max(0f, contentH - h));

        g.enableScissor(x, y, x + w, y + h);
        for (int i = 0; i < modules.size(); i++) {
            int col = i % 2;
            int row = i / 2;
            float cx = x + Theme.PAD + col * (cardW + Theme.GAP);
            float cy = y + Theme.PAD + row * (CARD_H + Theme.GAP) - gridScroll;
            if (cy + CARD_H < y || cy > y + h) {
                continue;
            }
            renderCard(g, modules.get(i), cx, cy, cardW, mouseX, mouseY, dt);
        }
        g.disableScissor();

        renderScrollbar(g, x + w - 4, y, h, contentH, gridScroll);
    }

    private void renderCard(GuiGraphicsExtractor g, Module module, float x, float y, int w,
                            int mouseX, int mouseY, float dt) {
        boolean hovered = inside(mouseX, mouseY, x, y, w, CARD_H);

        Anim hoverAnim = anim("card:" + module.id());
        hoverAnim.target(hovered ? 1f : 0f);
        float hv = hoverAnim.update(dt);

        Anim onAnim = anim("on:" + module.id());
        onAnim.target(module.isEnabled() ? 1f : 0f);
        float on = onAnim.update(dt);

        Draw.roundRect(g, x, y, w, CARD_H, Theme.RADIUS_MD,
                Theme.mix(Theme.alpha(Theme.SURFACE_RAISED, 0.6f), Theme.SURFACE_HOVER, hv));
        Draw.roundRectOutline(g, x, y, w, CARD_H, Theme.RADIUS_MD, 1f,
                Theme.mix(Theme.alpha(Theme.BORDER, 0.7f), Theme.alpha(Theme.ACCENT, 0.55f),
                        Math.max(hv * 0.7f, on)));

        // Icon tile.
        float ix = x + 8;
        float iy = y + (CARD_H - 24) / 2f;
        Draw.roundRect(g, ix, iy, 24, 24, Theme.RADIUS_SM,
                Theme.mix(Theme.alpha(Theme.SURFACE, 0.8f), Theme.alpha(Theme.ACCENT, 0.2f), on));
        Icons.forCategory(g, module.category(), ix + 4, iy + 4, 16,
                Theme.mix(Theme.TEXT_FAINT, Theme.ACCENT, Math.max(on, hv * 0.5f)));

        float textX = ix + 32;
        float textW = w - (textX - x) - 34;

        String name = module.displayName().toUpperCase(Locale.ROOT);
        Draw.text(g, font, Draw.ellipsize(font, name, (int) textW), textX, y + 8,
                Theme.mix(Theme.TEXT_DIM, Theme.TEXT, Math.max(on, hv)));

        // Description over two lines.
        List<String> lines = wrap(module.description(), (int) textW, 2);
        float dy = y + 20;
        for (String line : lines) {
            Draw.text(g, font, line, textX, dy, Theme.TEXT_FAINT);
            dy += 10;
        }

        toggle(g, x + w - 28, y + (CARD_H - 12) / 2f, on);
    }

    /** Greedy word wrap, ellipsizing whatever will not fit in {@code maxLines}. */
    private List<String> wrap(String text, int maxWidth, int maxLines) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (font.width(candidate) <= maxWidth) {
                line.setLength(0);
                line.append(candidate);
                continue;
            }
            if (!line.isEmpty()) {
                lines.add(line.toString());
                line.setLength(0);
            }
            line.append(word);
            if (lines.size() == maxLines) {
                break;
            }
        }
        if (lines.size() < maxLines && !line.isEmpty()) {
            lines.add(line.toString());
        }
        if (lines.size() == maxLines) {
            lines.set(maxLines - 1, Draw.ellipsize(font, lines.get(maxLines - 1) + " ...", maxWidth));
        }
        return lines;
    }

    // ---- Detail view ----------------------------------------------------------------------

    private void renderDetail(GuiGraphicsExtractor g, int x, int y, int w, int h,
                              int mouseX, int mouseY, float dt) {
        boolean backHovered = inside(mouseX, mouseY, x + Theme.PAD, y + 8, 18, 18);
        Draw.roundRect(g, x + Theme.PAD, y + 8, 18, 18, Theme.RADIUS_SM,
                backHovered ? Theme.SURFACE_HOVER : Theme.alpha(Theme.SURFACE_RAISED, 0.6f));
        Icons.back(g, x + Theme.PAD + 2, y + 10, 14, Theme.TEXT_DIM);

        Draw.text(g, font, selected.displayName(), x + Theme.PAD + 26, y + 9, Theme.TEXT);
        Draw.text(g, font, Draw.ellipsize(font, selected.description(), w - 90),
                x + Theme.PAD + 26, y + 20, Theme.TEXT_FAINT);

        Anim onAnim = anim("on:" + selected.id());
        onAnim.target(selected.isEnabled() ? 1f : 0f);
        toggle(g, x + w - Theme.PAD - 24, y + 12, onAnim.update(dt));

        int top = y + 34;
        Draw.hLine(g, x + Theme.PAD, top - 4, w - Theme.PAD * 2, Theme.BORDER_SOFT);

        int availH = h - (top - y) - Theme.PAD;
        List<Row> rows = layoutRows(x + Theme.PAD, w - Theme.PAD * 2 - 4);
        int contentH = 0;
        for (Row row : rows) {
            contentH += row.height;
        }
        detailScroll = Math.clamp(detailScroll, 0f, Math.max(0f, contentH - availH));

        g.enableScissor(x, top, x + w, top + availH);
        int ry = (int) (top - detailScroll);
        for (Row row : rows) {
            if (ry + row.height >= top && ry <= top + availH) {
                renderRow(g, row, ry, mouseX, mouseY, dt);
            }
            ry += row.height;
        }
        g.disableScissor();

        renderScrollbar(g, x + w - 4, top, availH, contentH, detailScroll);
    }

    // ---- Profiles ---------------------------------------------------------------------------

    private void renderProfiles(GuiGraphicsExtractor g, int x, int y, int w, int h,
                                int mouseX, int mouseY, float dt) {
        var config = VoidrixClient.config();
        List<String> profiles = config.profiles();

        Draw.text(g, font, "Each profile is a complete, separate set of module settings.",
                x + Theme.PAD, y + 8, Theme.TEXT_FAINT);

        // New profile row.
        int inner = w - Theme.PAD * 2;
        int createW = Math.max(52, font.width("Create") + 16);
        int fieldW = inner - createW - Theme.GAP;
        float fy = y + 22;

        Draw.roundRect(g, x + Theme.PAD, fy, fieldW, 18, Theme.RADIUS_SM,
                profileFocused ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED);
        Draw.roundRectOutline(g, x + Theme.PAD, fy, fieldW, 18, Theme.RADIUS_SM, 1f,
                Theme.alpha(Theme.ACCENT, profileFocused ? 0.8f : 0.2f));
        String shown = profileInput.isEmpty() ? "New profile name" : profileInput;
        Draw.text(g, font, Draw.ellipsize(font, shown, fieldW - 10), x + Theme.PAD + 5, fy + 5,
                profileInput.isEmpty() ? Theme.TEXT_FAINT : Theme.TEXT);

        float cx = x + Theme.PAD + inner - createW;
        boolean canCreate = !profileInput.isBlank();
        Draw.roundRect(g, cx, fy, createW, 18, Theme.RADIUS_SM,
                canCreate ? Theme.alpha(Theme.ACCENT, 0.8f) : Theme.SURFACE_RAISED);
        Draw.textCentered(g, font, "Create", cx + createW / 2f, fy + 5,
                canCreate ? 0xFFFFFFFF : Theme.TEXT_FAINT);

        // Profile list.
        int ry = (int) fy + 26;
        for (String name : profiles) {
            boolean current = name.equals(config.currentProfile());
            boolean hovered = inside(mouseX, mouseY, x + Theme.PAD, ry, inner, 22);

            Draw.roundRect(g, x + Theme.PAD, ry, inner, 22, Theme.RADIUS_MD,
                    current ? Theme.alpha(Theme.ACCENT, 0.18f)
                            : (hovered ? Theme.SURFACE_HOVER : Theme.alpha(Theme.SURFACE_RAISED, 0.5f)));
            if (current) {
                Draw.roundRect(g, x + Theme.PAD, ry + 5, 2.5f, 12, 1.25f, Theme.ACCENT);
            }
            Icons.profile(g, x + Theme.PAD + 8, ry + 5, 12,
                    current ? Theme.ACCENT : Theme.TEXT_FAINT);
            Draw.text(g, font, name, x + Theme.PAD + 26, ry + 7,
                    current ? Theme.TEXT : Theme.TEXT_DIM);

            if (current) {
                Draw.textRight(g, font, "active", x + Theme.PAD + inner - 8, ry + 7, Theme.ACCENT);
            } else {
                Draw.roundRect(g, x + Theme.PAD + inner - 30, ry + 4, 24, 14, Theme.RADIUS_SM,
                        Theme.alpha(Theme.DANGER, 0.15f));
                Draw.textCentered(g, font, "del", x + Theme.PAD + inner - 18, ry + 7, Theme.DANGER);
            }
            ry += 25;
            if (ry > y + h - 20) {
                break;
            }
        }
    }

    // ---- Waypoints --------------------------------------------------------------------------

    private void renderWaypoints(GuiGraphicsExtractor g, int x, int y, int w, int h,
                                 int mouseX, int mouseY) {
        var manager = VoidrixClient.waypoints();
        List<Waypoint> waypoints = manager.all();

        Draw.text(g, font, "Press B in game to drop a waypoint where you stand.",
                x + Theme.PAD, y + 8, Theme.TEXT_FAINT);

        if (waypoints.isEmpty()) {
            Icons.waypoint(g, x + w / 2f - 10, y + h / 2f - 22, 20, Theme.TEXT_FAINT);
            Draw.textCentered(g, font, "No waypoints yet", x + w / 2f, y + h / 2f + 4, Theme.TEXT_FAINT);
            return;
        }

        int inner = w - Theme.PAD * 2;
        int ry = y + 24;
        var player = Minecraft.getInstance().player;

        for (Waypoint waypoint : waypoints) {
            if (ry > y + h - 22) {
                break;
            }
            boolean hovered = inside(mouseX, mouseY, x + Theme.PAD, ry, inner, 22);
            Draw.roundRect(g, x + Theme.PAD, ry, inner, 22, Theme.RADIUS_MD,
                    hovered ? Theme.SURFACE_HOVER : Theme.alpha(Theme.SURFACE_RAISED, 0.5f));

            float alpha = waypoint.visible() ? 1f : 0.35f;
            Draw.circle(g, x + Theme.PAD + 12, ry + 11, 4f, Theme.alpha(waypoint.color(), alpha));
            Draw.text(g, font, Draw.ellipsize(font, waypoint.name(), inner - 150),
                    x + Theme.PAD + 22, ry + 3, Theme.alpha(Theme.TEXT, alpha));
            Draw.text(g, font, waypoint.x() + ", " + waypoint.y() + ", " + waypoint.z(),
                    x + Theme.PAD + 22, ry + 12, Theme.TEXT_FAINT);

            if (player != null) {
                double d = player.position().distanceTo(new net.minecraft.world.phys.Vec3(
                        waypoint.x() + 0.5, waypoint.y() + 0.5, waypoint.z() + 0.5));
                Draw.textRight(g, font, Math.round(d) + "m", x + Theme.PAD + inner - 58, ry + 7,
                        Theme.TEXT_DIM);
            }

            Draw.roundRect(g, x + Theme.PAD + inner - 52, ry + 4, 24, 14, Theme.RADIUS_SM,
                    Theme.alpha(waypoint.visible() ? Theme.ACCENT : Theme.TEXT_FAINT, 0.18f));
            Draw.textCentered(g, font, waypoint.visible() ? "on" : "off",
                    x + Theme.PAD + inner - 40, ry + 7,
                    waypoint.visible() ? Theme.ACCENT : Theme.TEXT_FAINT);

            Draw.roundRect(g, x + Theme.PAD + inner - 26, ry + 4, 24, 14, Theme.RADIUS_SM,
                    Theme.alpha(Theme.DANGER, 0.15f));
            Draw.textCentered(g, font, "del", x + Theme.PAD + inner - 14, ry + 7, Theme.DANGER);

            ry += 25;
        }
    }

    // ---- Shared bits --------------------------------------------------------------------

    private void toggle(GuiGraphicsExtractor g, float x, float y, float t) {
        float w = 22f;
        float h = 12f;
        Draw.roundRect(g, x, y, w, h, h / 2f, Theme.mix(0xFF2A2A38, Theme.ACCENT, t));
        if (t > 0.02f) {
            Draw.glow(g, x, y, w, h, h / 2f, 3f, Theme.ACCENT, 0.20f * t);
        }
        float knobR = (h - 3f) / 2f;
        float knobCx = x + 1.5f + knobR + t * (w - h);
        Draw.circle(g, knobCx, y + h / 2f, knobR, Theme.mix(0xFF8A8A9C, 0xFFFFFFFF, t));
    }

    private void renderScrollbar(GuiGraphicsExtractor g, int x, int y, int h, int contentH, float scroll) {
        if (contentH <= h) {
            return;
        }
        float ratio = (float) h / contentH;
        float barH = Math.max(18f, h * ratio);
        float travel = h - barH;
        float t = scroll / (contentH - h);
        Draw.roundRect(g, x, y + t * travel, 2.5f, barH, 1.25f, Theme.alpha(Theme.ACCENT, 0.5f));
    }

    // ---- Setting rows ---------------------------------------------------------------------

    private static final class Row {
        final Setting<?> setting;
        final int x;
        final int width;
        final int height;
        final boolean hotkey;

        Row(Setting<?> setting, int x, int width, int height, boolean hotkey) {
            this.setting = setting;
            this.x = x;
            this.width = width;
            this.height = height;
            this.hotkey = hotkey;
        }
    }

    private static int heightOf(Setting<?> setting) {
        if (setting instanceof BoolSetting) {
            return 22;
        }
        if (setting instanceof IntSetting || setting instanceof DoubleSetting) {
            return 30;
        }
        if (setting instanceof EnumSetting<?>) {
            return 26;
        }
        if (setting instanceof ColorSetting) {
            return 74;
        }
        if (setting instanceof StringSetting) {
            return 34;
        }
        return 22;
    }

    private List<Row> layoutRows(int x, int w) {
        List<Row> rows = new ArrayList<>();
        if (selected == null) {
            return rows;
        }
        for (Setting<?> setting : selected.settings()) {
            if (!setting.visible()) {
                continue;
            }
            rows.add(new Row(setting, x, w, heightOf(setting), false));
        }
        rows.add(new Row(null, x, w, 24, true));
        return rows;
    }

    private void renderRow(GuiGraphicsExtractor g, Row row, int y, int mouseX, int mouseY, float dt) {
        if (row.hotkey) {
            renderHotkeyRow(g, row, y, mouseX, mouseY);
            return;
        }
        Setting<?> setting = row.setting;

        if (setting instanceof BoolSetting bool) {
            Draw.text(g, font, Draw.ellipsize(font, bool.displayName(), row.width - 30),
                    row.x, y + 5, Theme.TEXT_DIM);
            Anim a = anim("set:" + selected.id() + ":" + bool.id());
            a.target(bool.value() ? 1f : 0f);
            toggle(g, row.x + row.width - 24, y + 3, a.update(dt));
            return;
        }

        if (setting instanceof IntSetting slider) {
            renderSlider(g, row, y, slider.displayName(), slider.value() + slider.suffix(),
                    slider.fraction());
            return;
        }

        if (setting instanceof DoubleSetting slider) {
            renderSlider(g, row, y, slider.displayName(), slider.format(), slider.fraction());
            return;
        }

        if (setting instanceof EnumSetting<?> choice) {
            String label = choice.currentLabel();
            float cw = Math.min(font.width(label) + 16, row.width * 0.6f);
            Draw.text(g, font, Draw.ellipsize(font, choice.displayName(), row.width - (int) cw - 8),
                    row.x, y + 7, Theme.TEXT_DIM);
            float cx = row.x + row.width - cw;
            boolean hovered = inside(mouseX, mouseY, cx, y + 3, cw, 17);
            Draw.roundRect(g, cx, y + 3, cw, 17, Theme.RADIUS_SM,
                    hovered ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED);
            Draw.roundRectOutline(g, cx, y + 3, cw, 17, Theme.RADIUS_SM, 1f,
                    Theme.alpha(Theme.ACCENT, hovered ? 0.5f : 0.22f));
            Draw.textCentered(g, font, Draw.ellipsize(font, label, (int) cw - 10),
                    cx + cw / 2f, y + 7, Theme.TEXT);
            return;
        }

        if (setting instanceof StringSetting text) {
            Draw.text(g, font, Draw.ellipsize(font, text.displayName(), row.width),
                    row.x, y + 1, Theme.TEXT_DIM);
            boolean focused = focusedText == text;
            float fieldY = y + 13;
            Draw.roundRect(g, row.x, fieldY, row.width, 17, Theme.RADIUS_SM,
                    focused ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED);
            Draw.roundRectOutline(g, row.x, fieldY, row.width, 17, Theme.RADIUS_SM, 1f,
                    Theme.alpha(Theme.ACCENT, focused ? 0.8f : 0.22f));

            String shown = text.isBlank() ? text.placeholder() : text.value();
            int color = text.isBlank() ? Theme.TEXT_FAINT : Theme.TEXT;
            String fitted = tailFit(shown, row.width - 12);
            Draw.text(g, font, fitted, row.x + 5, fieldY + 5, color);
            if (focused && (System.currentTimeMillis() / 500L) % 2 == 0) {
                Draw.rect(g, row.x + 5 + font.width(fitted) + 1, fieldY + 4, 1, 9, Theme.ACCENT);
            }
            return;
        }

        if (setting instanceof ColorSetting color) {
            Draw.text(g, font, Draw.ellipsize(font, color.displayName(), row.width - 28),
                    row.x, y + 4, Theme.TEXT_DIM);
            Draw.roundRect(g, row.x + row.width - 22, y + 2, 22, 12, Theme.RADIUS_SM, color.resolve());
            Draw.roundRectOutline(g, row.x + row.width - 22, y + 2, 22, 12, Theme.RADIUS_SM, 1f, Theme.BORDER);

            String[] names = {"R", "G", "B"};
            int[] values = {color.red(), color.green(), color.blue()};
            for (int i = 0; i < 3; i++) {
                int cy = y + 18 + i * 13;
                Draw.text(g, font, names[i], row.x, cy + 1, Theme.TEXT_FAINT);
                float trackX = row.x + 12;
                float trackW = row.width - 12 - 26;
                Draw.roundRect(g, trackX, cy + 4, trackW, 3, 1.5f, 0xFF23232F);
                float t = values[i] / 255f;
                int channelColor = switch (i) {
                    case 0 -> 0xFFF87171;
                    case 1 -> 0xFF34D399;
                    default -> 0xFF60A5FA;
                };
                Draw.roundRect(g, trackX, cy + 4, trackW * t, 3, 1.5f, channelColor);
                Draw.circle(g, trackX + trackW * t, cy + 5.5f, 3.5f, 0xFFFFFFFF);
                Draw.textRight(g, font, String.valueOf(values[i]), row.x + row.width, cy + 1,
                        Theme.TEXT_FAINT);
            }

            Anim a = anim("rainbow:" + selected.id() + ":" + color.id());
            a.target(color.rainbow().value() ? 1f : 0f);
            Draw.text(g, font, "Rainbow", row.x, y + 57, Theme.TEXT_FAINT);
            toggle(g, row.x + row.width - 24, y + 55, a.update(dt));
        }
    }

    private void renderSlider(GuiGraphicsExtractor g, Row row, int y, String name, String value, float t) {
        int valueW = font.width(value);
        Draw.text(g, font, Draw.ellipsize(font, name, row.width - valueW - 8),
                row.x, y + 3, Theme.TEXT_DIM);
        Draw.textRight(g, font, value, row.x + row.width, y + 3, Theme.TEXT);

        float trackY = y + 19;
        Draw.roundRect(g, row.x, trackY, row.width, 3, 1.5f, 0xFF23232F);
        Draw.roundRectGradientH(g, row.x, trackY, row.width * t, 3, 1.5f, Theme.ACCENT, Theme.ACCENT_ALT);
        float knobX = row.x + row.width * t;
        Draw.glow(g, knobX - 4, trackY - 2.5f, 8, 8, 4f, 3f, Theme.ACCENT, 0.35f);
        Draw.circle(g, knobX, trackY + 1.5f, 4f, 0xFFFFFFFF);
    }

    private void renderHotkeyRow(GuiGraphicsExtractor g, Row row, int y, int mouseX, int mouseY) {
        boolean binding = bindingModule == selected;
        String label = binding ? "Press a key..." : Keyboard.nameOf(selected.keyCode());
        float cw = hotkeyButtonWidth(row);

        Draw.text(g, font, Draw.ellipsize(font, "Toggle key", row.width - (int) cw - 8),
                row.x, y + 5, Theme.TEXT_DIM);
        float cx = row.x + row.width - cw;
        boolean hovered = inside(mouseX, mouseY, cx, y + 1, cw, 17);

        Draw.roundRect(g, cx, y + 1, cw, 17, Theme.RADIUS_SM,
                binding ? Theme.alpha(Theme.ACCENT, 0.22f)
                        : (hovered ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED));
        Draw.roundRectOutline(g, cx, y + 1, cw, 17, Theme.RADIUS_SM, 1f,
                Theme.alpha(Theme.ACCENT, binding ? 0.8f : 0.22f));
        Draw.textCentered(g, font, Draw.ellipsize(font, label, (int) cw - 8), cx + cw / 2f, y + 5,
                binding ? Theme.ACCENT : Theme.TEXT);
    }

    private float hotkeyButtonWidth(Row row) {
        String label = bindingModule == selected ? "Press a key..." : Keyboard.nameOf(selected.keyCode());
        return Math.min(Math.max(52f, font.width(label) + 16), row.width * 0.65f);
    }

    // -------------------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------------------

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }
        int mx = (int) event.x();
        int my = (int) event.y();

        boolean hadTextFocus = focusedText != null;
        focusedText = null;
        if (hadTextFocus) {
            VoidrixClient.config().save();
        }
        searchFocused = false;
        profileFocused = false;

        int bodyX = panelX + RAIL_W;
        int bodyY = panelY + HEADER_H;
        int bodyW = panelW - RAIL_W;
        int bodyH = panelH - HEADER_H;

        // Rail.
        int cy = panelY + 44;
        for (Category category : categories()) {
            if (inside(mx, my, panelX + 6, cy, RAIL_W - 12, 32)) {
                tab = Tab.MODS;
                filter = category;
                selected = null;
                search = "";
                gridScroll = 0f;
                return true;
            }
            cy += 36;
        }

        // Tabs.
        int tx = bodyX + Theme.PAD;
        for (Tab value : Tab.values()) {
            int tw = font.width(value.name()) + 16;
            if (inside(mx, my, tx, panelY + 10, tw, 20)) {
                tab = value;
                selected = null;
                return true;
            }
            tx += tw + 4;
        }

        // Search field.
        if (tab == Tab.MODS) {
            int sw = Math.min(150, Math.max(90, bodyW - (tx - bodyX) - Theme.PAD * 2));
            float sx = bodyX + bodyW - Theme.PAD - sw;
            if (inside(mx, my, sx, panelY + 10, sw, 20)) {
                searchFocused = true;
                return true;
            }
        }

        return switch (tab) {
            case MODS -> selected == null
                    ? clickGrid(mx, my, bodyX, bodyY, bodyW, bodyH)
                    : clickDetail(mx, my, bodyX, bodyY, bodyW);
            case PROFILES -> clickProfiles(mx, my, bodyX, bodyY, bodyW);
            case WAYPOINTS -> clickWaypoints(mx, my, bodyX, bodyY, bodyW, bodyH);
        };
    }

    private boolean clickGrid(int mx, int my, int x, int y, int w, int h) {
        if (my < y || my > y + h) {
            return false;
        }
        List<Module> modules = visibleModules();
        int cardW = cardWidth(w);
        for (int i = 0; i < modules.size(); i++) {
            float cx = x + Theme.PAD + (i % 2) * (cardW + Theme.GAP);
            float cardY = y + Theme.PAD + (i / 2) * (CARD_H + Theme.GAP) - gridScroll;
            if (!inside(mx, my, cx, cardY, cardW, CARD_H)) {
                continue;
            }
            Module module = modules.get(i);
            if (mx >= cx + cardW - 32) {
                module.toggle();
                VoidrixClient.config().save();
            } else {
                selected = module;
                detailScroll = 0f;
            }
            return true;
        }
        return false;
    }

    private boolean clickDetail(int mx, int my, int x, int y, int w) {
        if (inside(mx, my, x + Theme.PAD, y + 8, 18, 18)) {
            selected = null;
            return true;
        }
        if (inside(mx, my, x + w - Theme.PAD - 26, y + 10, 28, 16)) {
            selected.toggle();
            VoidrixClient.config().save();
            return true;
        }

        int top = y + 34;
        List<Row> rows = layoutRows(x + Theme.PAD, w - Theme.PAD * 2 - 4);
        int ry = (int) (top - detailScroll);
        for (Row row : rows) {
            if (handleRowClick(row, ry, mx, my)) {
                return true;
            }
            ry += row.height;
        }
        return false;
    }

    private boolean clickProfiles(int mx, int my, int x, int y, int w) {
        var config = VoidrixClient.config();
        int inner = w - Theme.PAD * 2;
        int createW = Math.max(52, font.width("Create") + 16);
        int fieldW = inner - createW - Theme.GAP;
        float fy = y + 22;

        if (inside(mx, my, x + Theme.PAD, fy, fieldW, 18)) {
            profileFocused = true;
            return true;
        }
        if (inside(mx, my, x + Theme.PAD + inner - createW, fy, createW, 18)) {
            if (!profileInput.isBlank()) {
                config.createProfile(profileInput);
                profileInput = "";
            }
            return true;
        }

        int ry = (int) fy + 26;
        for (String name : config.profiles()) {
            if (inside(mx, my, x + Theme.PAD, ry, inner, 22)) {
                if (!name.equals(config.currentProfile())
                        && inside(mx, my, x + Theme.PAD + inner - 30, ry + 4, 24, 14)) {
                    config.deleteProfile(name);
                } else {
                    config.switchTo(name);
                }
                return true;
            }
            ry += 25;
        }
        return false;
    }

    private boolean clickWaypoints(int mx, int my, int x, int y, int w, int h) {
        var manager = VoidrixClient.waypoints();
        int inner = w - Theme.PAD * 2;
        int ry = y + 24;
        for (Waypoint waypoint : List.copyOf(manager.all())) {
            if (ry > y + h - 22) {
                break;
            }
            if (inside(mx, my, x + Theme.PAD, ry, inner, 22)) {
                if (inside(mx, my, x + Theme.PAD + inner - 26, ry + 4, 24, 14)) {
                    manager.remove(waypoint);
                } else if (inside(mx, my, x + Theme.PAD + inner - 52, ry + 4, 24, 14)) {
                    waypoint.toggleVisible();
                    manager.save();
                }
                return true;
            }
            ry += 25;
        }
        return false;
    }

    private boolean handleRowClick(Row row, int y, int mx, int my) {
        if (row.hotkey) {
            float cw = hotkeyButtonWidth(row);
            if (inside(mx, my, row.x + row.width - cw, y + 1, cw, 17)) {
                bindingModule = selected;
                return true;
            }
            return false;
        }

        Setting<?> setting = row.setting;

        if (setting instanceof BoolSetting bool && inside(mx, my, row.x, y, row.width, row.height)) {
            bool.toggle();
            VoidrixClient.config().save();
            return true;
        }

        if (setting instanceof IntSetting || setting instanceof DoubleSetting) {
            if (inside(mx, my, row.x - 4, y + 12, row.width + 8, 16)) {
                dragging = setting;
                dragChannel = -1;
                applySlider(setting, row, mx);
                return true;
            }
            return false;
        }

        if (setting instanceof EnumSetting<?> choice && inside(mx, my, row.x, y, row.width, row.height)) {
            choice.cycle();
            VoidrixClient.config().save();
            return true;
        }

        if (setting instanceof StringSetting text) {
            if (inside(mx, my, row.x, y + 13, row.width, 17)) {
                focusedText = text;
                return true;
            }
            return false;
        }

        if (setting instanceof ColorSetting color) {
            for (int i = 0; i < 3; i++) {
                int cy = y + 18 + i * 13;
                if (inside(mx, my, row.x + 8, cy, row.width - 12 - 22, 14)) {
                    dragging = color;
                    dragChannel = i;
                    applyColor(color, row, i, mx);
                    return true;
                }
            }
            if (inside(mx, my, row.x, y + 54, row.width, 16)) {
                color.rainbow().toggle();
                VoidrixClient.config().save();
                return true;
            }
        }
        return false;
    }

    private void applySlider(Setting<?> setting, Row row, int mx) {
        float t = Math.clamp((mx - row.x) / (float) row.width, 0f, 1f);
        if (setting instanceof IntSetting slider) {
            slider.setFraction(t);
        } else if (setting instanceof DoubleSetting slider) {
            slider.setFraction(t);
        }
    }

    private void applyColor(ColorSetting color, Row row, int channel, int mx) {
        float trackX = row.x + 12;
        float trackW = row.width - 12 - 26;
        int v = Math.round(Math.clamp((mx - trackX) / trackW, 0f, 1f) * 255f);
        color.setRgb(channel == 0 ? v : color.red(),
                channel == 1 ? v : color.green(),
                channel == 2 ? v : color.blue());
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (dragging != null && selected != null) {
            int bodyX = panelX + RAIL_W;
            int bodyW = panelW - RAIL_W;
            for (Row row : layoutRows(bodyX + Theme.PAD, bodyW - Theme.PAD * 2 - 4)) {
                if (row.setting != dragging) {
                    continue;
                }
                if (dragChannel >= 0 && dragging instanceof ColorSetting color) {
                    applyColor(color, row, dragChannel, (int) event.x());
                } else {
                    applySlider(dragging, row, (int) event.x());
                }
                return true;
            }
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (dragging != null) {
            dragging = null;
            dragChannel = -1;
            VoidrixClient.config().save();
            return true;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        float amount = (float) scrollY * 18f;
        if (tab == Tab.MODS && selected != null) {
            detailScroll -= amount;
        } else {
            gridScroll -= amount;
        }
        return true;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        String typed = event.codepointAsString();
        if (searchFocused) {
            for (int i = 0; i < typed.length(); i++) {
                char c = typed.charAt(i);
                if (c >= ' ' && c != 127 && search.length() < 32) {
                    search += c;
                }
            }
            gridScroll = 0f;
            return true;
        }
        if (profileFocused) {
            for (int i = 0; i < typed.length(); i++) {
                char c = typed.charAt(i);
                if (c >= ' ' && c != 127 && profileInput.length() < 24) {
                    profileInput += c;
                }
            }
            return true;
        }
        if (focusedText != null) {
            for (int i = 0; i < typed.length(); i++) {
                char c = typed.charAt(i);
                if (c >= ' ' && c != 127) {
                    focusedText.append(c);
                }
            }
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (bindingModule != null) {
            int key = event.key();
            bindingModule.setKeyCode(key == GLFW.GLFW_KEY_ESCAPE ? -1 : key);
            Keyboard.forget(bindingModule.id());
            bindingModule = null;
            VoidrixClient.config().save();
            return true;
        }

        if (searchFocused) {
            switch (event.key()) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    if (!search.isEmpty()) {
                        search = search.substring(0, search.length() - 1);
                    }
                    return true;
                }
                case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                    searchFocused = false;
                    return true;
                }
                default -> {
                    return true;
                }
            }
        }

        if (profileFocused) {
            switch (event.key()) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    if (!profileInput.isEmpty()) {
                        profileInput = profileInput.substring(0, profileInput.length() - 1);
                    }
                    return true;
                }
                case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                    VoidrixClient.config().createProfile(profileInput);
                    profileInput = "";
                    return true;
                }
                case GLFW.GLFW_KEY_ESCAPE -> {
                    profileFocused = false;
                    return true;
                }
                default -> {
                    return true;
                }
            }
        }

        if (focusedText != null) {
            switch (event.key()) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    focusedText.backspace();
                    return true;
                }
                case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER, GLFW.GLFW_KEY_TAB -> {
                    focusedText = null;
                    VoidrixClient.config().save();
                    return true;
                }
                default -> {
                    return true;
                }
            }
        }

        // Escape backs out of a module before it closes the whole panel.
        if (event.key() == GLFW.GLFW_KEY_ESCAPE && selected != null) {
            selected = null;
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        VoidrixClient.config().save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ---- Helpers ---------------------------------------------------------------------------

    private static boolean inside(int mx, int my, float x, float y, float w, float h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private String tailFit(String s, int maxWidth) {
        if (font.width(s) <= maxWidth) {
            return s;
        }
        int start = 0;
        while (start < s.length() && font.width(s.substring(start)) > maxWidth) {
            start++;
        }
        return s.substring(start);
    }

    public static void open() {
        Minecraft.getInstance().setScreenAndShow(new MenuScreen());
    }
}
