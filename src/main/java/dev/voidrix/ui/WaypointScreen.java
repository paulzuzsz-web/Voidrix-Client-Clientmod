package dev.voidrix.ui;

import dev.voidrix.VoidrixClient;
import dev.voidrix.waypoint.Waypoint;
import dev.voidrix.waypoint.WaypointManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Create and manage waypoints. Bound to <kbd>B</kbd>.
 *
 * <p>Opens with the name field already focused and your current position filled in, because the
 * overwhelmingly common case is "I am standing on the thing I want to remember" - that should be
 * two keys and a word, not a form.
 */
public final class WaypointScreen extends Screen {
    /** Preset colours, cycled by clicking the swatch. */
    private static final int[] PALETTE = {
            0xFF8B5CF6, 0xFFD946EF, 0xFF34D399, 0xFFFBBF24,
            0xFFF87171, 0xFF60A5FA, 0xFFFFFFFF, 0xFFFB923C
    };

    private static final int ROW_H = 26;

    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    private String nameInput = "";
    private boolean nameFocused = true;
    private int colorIndex;
    private int x;
    private int y;
    private int z;

    private float listScroll;
    private long lastFrame = System.nanoTime();
    private final Anim opening = new Anim(0f, 0.15f);

    public WaypointScreen() {
        super(Component.literal("Waypoints"));
    }

    @Override
    protected void init() {
        panelW = Math.min(420, width - 24);
        panelH = Math.min(300, height - 24);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        opening.target(1f);

        var player = Minecraft.getInstance().player;
        if (player != null) {
            x = (int) Math.floor(player.getX());
            y = (int) Math.floor(player.getY());
            z = (int) Math.floor(player.getZ());
        }
    }

    private WaypointManager manager() {
        return VoidrixClient.waypoints();
    }

    private int currentColor() {
        return PALETTE[Math.floorMod(colorIndex, PALETTE.length)];
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
        int py = panelY + Math.round((1f - open) * 10f);

        Draw.shadow(g, px, py, panelW, panelH, Theme.RADIUS_LG, 12f, 0xFF000000);
        Draw.roundRectGradient(g, px, py, panelW, panelH, Theme.RADIUS_LG,
                Theme.alpha(Theme.SURFACE, open), Theme.alpha(Theme.VOID, open));
        Draw.roundRectOutline(g, px, py, panelW, panelH, Theme.RADIUS_LG, 1f,
                Theme.alpha(Theme.BORDER, open));

        // Header.
        Draw.text(g, font, "WAYPOINTS", px + Theme.PAD, py + 11, Theme.ACCENT);
        String dim = WaypointManager.currentDimension();
        if (!dim.isEmpty()) {
            String shortDim = dim.contains(":") ? dim.substring(dim.indexOf(':') + 1) : dim;
            Draw.textRight(g, font, shortDim, px + panelW - Theme.PAD, py + 11, Theme.TEXT_FAINT);
        }
        Draw.hLine(g, px + 1, py + 28, panelW - 2, Theme.BORDER_SOFT);

        renderForm(g, px, py + 34, panelW, mouseX, mouseY);

        int listTop = py + 34 + 56;
        Draw.hLine(g, px + Theme.PAD, listTop - 6, panelW - Theme.PAD * 2, Theme.BORDER_SOFT);
        renderList(g, px, listTop, panelW, py + panelH - listTop - Theme.PAD, mouseX, mouseY);
    }

    private void renderForm(GuiGraphicsExtractor g, int px, int y0, int w, int mouseX, int mouseY) {
        int inner = w - Theme.PAD * 2;
        int fx = px + Theme.PAD;

        // Name field, with the colour swatch on its left and Create on its right.
        int swatch = 18;
        int createW = Math.max(52, font.width("Create") + 16);
        int fieldW = inner - swatch - createW - Theme.GAP * 2;

        Draw.roundRect(g, fx, y0, swatch, 18, Theme.RADIUS_SM, currentColor());
        Draw.roundRectOutline(g, fx, y0, swatch, 18, Theme.RADIUS_SM, 1f, Theme.BORDER);

        float nx = fx + swatch + Theme.GAP;
        Draw.roundRect(g, nx, y0, fieldW, 18, Theme.RADIUS_SM,
                nameFocused ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED);
        Draw.roundRectOutline(g, nx, y0, fieldW, 18, Theme.RADIUS_SM, 1f,
                Theme.alpha(Theme.ACCENT, nameFocused ? 0.8f : 0.22f));

        String shown = nameInput.isEmpty() ? "Name this place" : nameInput;
        int textColor = nameInput.isEmpty() ? Theme.TEXT_FAINT : Theme.TEXT;
        Draw.text(g, font, Draw.ellipsize(font, shown, fieldW - 10), nx + 5, y0 + 5, textColor);
        if (nameFocused && (System.currentTimeMillis() / 500L) % 2 == 0) {
            float caret = nx + 5 + font.width(Draw.ellipsize(font, nameInput, fieldW - 10)) + 1;
            Draw.rect(g, caret, y0 + 4, 1, 10, Theme.ACCENT);
        }

        float cx = fx + inner - createW;
        boolean canCreate = !nameInput.isBlank();
        boolean hovered = inside(mouseX, mouseY, cx, y0, createW, 18);
        Draw.roundRect(g, cx, y0, createW, 18, Theme.RADIUS_SM,
                canCreate ? Theme.alpha(Theme.ACCENT, hovered ? 0.9f : 0.7f) : Theme.SURFACE_RAISED);
        Draw.textCentered(g, font, "Create", cx + createW / 2f, y0 + 5,
                canCreate ? 0xFFFFFFFF : Theme.TEXT_FAINT);

        // Position line.
        String pos = "X " + x + "   Y " + y + "   Z " + z;
        Draw.text(g, font, pos, fx, y0 + 26, Theme.TEXT_DIM);
        Draw.textRight(g, font, "at your feet", fx + inner, y0 + 26, Theme.TEXT_FAINT);
    }

    private void renderList(GuiGraphicsExtractor g, int px, int y0, int w, int h,
                            int mouseX, int mouseY) {
        List<Waypoint> waypoints = manager().all();
        if (waypoints.isEmpty()) {
            Draw.textCentered(g, font, "No waypoints yet", px + w / 2f, y0 + h / 2f - 4, Theme.TEXT_FAINT);
            return;
        }

        int contentH = waypoints.size() * ROW_H;
        listScroll = Math.clamp(listScroll, 0f, Math.max(0f, contentH - h));

        g.enableScissor(px, y0, px + w, y0 + h);
        int ry = (int) (y0 - listScroll);
        var player = Minecraft.getInstance().player;

        for (Waypoint waypoint : waypoints) {
            if (ry + ROW_H >= y0 && ry <= y0 + h) {
                int rx = px + Theme.PAD;
                int rw = w - Theme.PAD * 2;
                boolean hovered = inside(mouseX, mouseY, rx, ry, rw, ROW_H - 3);

                Draw.roundRect(g, rx, ry, rw, ROW_H - 3, Theme.RADIUS_MD,
                        hovered ? Theme.SURFACE_HOVER : Theme.alpha(Theme.SURFACE_RAISED, 0.55f));

                float alpha = waypoint.visible() ? 1f : 0.35f;
                Draw.circle(g, rx + 11, ry + (ROW_H - 3) / 2f, 4f,
                        Theme.alpha(waypoint.color(), alpha));

                Draw.text(g, font, Draw.ellipsize(font, waypoint.name(), rw - 130),
                        rx + 21, ry + 3, Theme.alpha(Theme.TEXT, alpha));
                String coords = waypoint.x() + ", " + waypoint.y() + ", " + waypoint.z();
                Draw.text(g, font, coords, rx + 21, ry + 12, Theme.TEXT_FAINT);

                if (player != null) {
                    double d = player.position().distanceTo(
                            new net.minecraft.world.phys.Vec3(waypoint.x() + 0.5, waypoint.y() + 0.5, waypoint.z() + 0.5));
                    Draw.textRight(g, font, Math.round(d) + "m", rx + rw - 58, ry + 8, Theme.TEXT_DIM);
                }

                // Eye toggle and delete, as two small chips.
                drawChip(g, rx + rw - 50, ry + 4, 22, waypoint.visible() ? "on" : "off",
                        waypoint.visible() ? Theme.ACCENT : Theme.TEXT_FAINT,
                        inside(mouseX, mouseY, rx + rw - 50, ry + 4, 22, 15));
                drawChip(g, rx + rw - 25, ry + 4, 21, "del", Theme.DANGER,
                        inside(mouseX, mouseY, rx + rw - 25, ry + 4, 21, 15));
            }
            ry += ROW_H;
        }
        g.disableScissor();
    }

    private void drawChip(GuiGraphicsExtractor g, float x, float y, float w, String label,
                          int color, boolean hovered) {
        Draw.roundRect(g, x, y, w, 15, Theme.RADIUS_SM,
                hovered ? Theme.SURFACE_ACTIVE : Theme.alpha(Theme.SURFACE, 0.8f));
        Draw.roundRectOutline(g, x, y, w, 15, Theme.RADIUS_SM, 1f, Theme.alpha(color, hovered ? 0.7f : 0.3f));
        Draw.textCentered(g, font, label, x + w / 2f, y + 4, color);
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

        int formY = panelY + 34;
        int inner = panelW - Theme.PAD * 2;
        int fx = panelX + Theme.PAD;
        int swatch = 18;
        int createW = Math.max(52, font.width("Create") + 16);
        int fieldW = inner - swatch - createW - Theme.GAP * 2;

        if (inside(mx, my, fx, formY, swatch, 18)) {
            colorIndex++;
            return true;
        }
        if (inside(mx, my, fx + swatch + Theme.GAP, formY, fieldW, 18)) {
            nameFocused = true;
            return true;
        }
        if (inside(mx, my, fx + inner - createW, formY, createW, 18)) {
            create();
            return true;
        }
        nameFocused = false;

        // List rows.
        int listTop = formY + 56;
        int listH = panelY + panelH - listTop - Theme.PAD;
        int ry = (int) (listTop - listScroll);
        int rx = panelX + Theme.PAD;
        int rw = panelW - Theme.PAD * 2;

        for (Waypoint waypoint : List.copyOf(manager().all())) {
            if (my >= listTop && my <= listTop + listH && inside(mx, my, rx, ry, rw, ROW_H - 3)) {
                if (inside(mx, my, rx + rw - 25, ry + 4, 21, 15)) {
                    manager().remove(waypoint);
                } else if (inside(mx, my, rx + rw - 50, ry + 4, 22, 15)) {
                    waypoint.toggleVisible();
                    manager().save();
                }
                return true;
            }
            ry += ROW_H;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void create() {
        if (nameInput.isBlank()) {
            return;
        }
        manager().add(new Waypoint(nameInput.trim(), x, y, z, currentColor(),
                WaypointManager.currentDimension()));
        nameInput = "";
        colorIndex++;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        listScroll -= (float) scrollY * 16f;
        return true;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (!nameFocused) {
            return super.charTyped(event);
        }
        String typed = event.codepointAsString();
        for (int i = 0; i < typed.length(); i++) {
            char c = typed.charAt(i);
            if (c >= ' ' && c != 127 && nameInput.length() < 32) {
                nameInput += c;
            }
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (nameFocused) {
            switch (event.key()) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    if (!nameInput.isEmpty()) {
                        nameInput = nameInput.substring(0, nameInput.length() - 1);
                    }
                    return true;
                }
                case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                    create();
                    return true;
                }
                case GLFW.GLFW_KEY_ESCAPE -> {
                    // The field is focused the moment the screen opens, so swallowing Escape here
                    // would mean pressing it twice every single time just to leave.
                    nameFocused = false;
                    onClose();
                    return true;
                }
                default -> {
                    return true;
                }
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static boolean inside(int mx, int my, float x, float y, float w, float h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    public static void open() {
        Minecraft.getInstance().setScreenAndShow(new WaypointScreen());
    }
}
