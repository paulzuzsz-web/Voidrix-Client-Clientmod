package dev.voidrix.ui;

import dev.voidrix.VoidrixClient;
import dev.voidrix.module.Category;
import dev.voidrix.module.HudModule;
import dev.voidrix.module.Module;
import dev.voidrix.setting.BoolSetting;
import dev.voidrix.setting.ColorSetting;
import dev.voidrix.setting.DoubleSetting;
import dev.voidrix.setting.EnumSetting;
import dev.voidrix.setting.IntSetting;
import dev.voidrix.setting.Setting;
import dev.voidrix.util.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Voidrix control panel.
 *
 * <p>Three columns: categories, the modules in the selected category, and the settings of the
 * selected module. Everything is drawn by hand through {@link Draw} rather than assembled from
 * vanilla widgets, which is what lets the whole surface share one visual language.
 */
public final class MenuScreen extends Screen {
    private static final int HEADER_H = 44;
    private static final int SIDEBAR_W = 112;
    private static final int LIST_W = 186;
    private static final int ROW_H = 30;

    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    private Category selectedCategory = Category.HUD;
    private Module selectedModule;

    private float listScroll;
    private float settingsScroll;

    /** Setting whose slider is currently being dragged, if any. */
    private Setting<?> dragging;
    /** Which of a colour setting's three channels is being dragged. */
    private int dragChannel = -1;
    /** Module waiting for the next key press to become its hotkey. */
    private Module bindingModule;

    private final Map<String, Anim> hovers = new HashMap<>();
    private final Anim opening = new Anim(0f, 0.16f);
    private long lastFrame = System.nanoTime();

    public MenuScreen() {
        super(Component.literal("Voidrix"));
    }

    @Override
    protected void init() {
        panelW = Math.min(520, width - 40);
        panelH = Math.min(322, height - 40);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        opening.target(1f);

        if (selectedModule == null) {
            List<Module> inCategory = VoidrixClient.modules().byCategory(selectedCategory);
            if (!inCategory.isEmpty()) {
                selectedModule = inCategory.getFirst();
            }
        }
    }

    private Anim hover(String key) {
        return hovers.computeIfAbsent(key, k -> new Anim(0f));
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

        // Dim and blur the game behind the panel.
        g.fill(0, 0, width, height, Theme.alpha(0xFF05050A, 0.72f * open));
        g.nextStratum();
        g.blurBeforeThisStratum();

        // Slide the panel up slightly as it fades in.
        int lift = Math.round((1f - open) * 12f);
        int px = panelX;
        int py = panelY + lift;

        Draw.shadow(g, px, py, panelW, panelH, Theme.RADIUS_LG, 14f, 0xFF000000);
        Draw.roundRectGradient(g, px, py, panelW, panelH, Theme.RADIUS_LG,
                Theme.alpha(Theme.SURFACE, open), Theme.alpha(Theme.VOID, open));
        Draw.roundRectOutline(g, px, py, panelW, panelH, Theme.RADIUS_LG, 1f,
                Theme.alpha(Theme.BORDER, open));

        renderHeader(g, px, py, dt);

        int bodyY = py + HEADER_H;
        int bodyH = panelH - HEADER_H;

        renderSidebar(g, px, bodyY, bodyH, mouseX, mouseY, dt);
        renderModuleList(g, px + SIDEBAR_W, bodyY, LIST_W, bodyH, mouseX, mouseY, dt);
        renderSettings(g, px + SIDEBAR_W + LIST_W, bodyY,
                panelW - SIDEBAR_W - LIST_W, bodyH, mouseX, mouseY, dt);
    }

    private void renderHeader(GuiGraphicsExtractor g, int px, int py, float dt) {
        // Wordmark, with the accent ramp swept across the letters.
        String mark = "VOIDRIX";
        float x = px + Theme.PAD + 2;
        float sweep = (System.currentTimeMillis() % 3000L) / 3000f;
        for (int i = 0; i < mark.length(); i++) {
            String ch = String.valueOf(mark.charAt(i));
            int color = Theme.accentAt(sweep + i * 0.06f);
            Draw.text(g, font, ch, x, py + 13, color);
            x += font.width(ch) + 1.2f;
        }

        Draw.text(g, font, "client", x + 4, py + 13, Theme.TEXT_FAINT);

        int enabled = VoidrixClient.modules().enabledCount();
        int total = VoidrixClient.modules().all().size();
        String count = enabled + " / " + total + " active";
        float cw = font.width(count) + 14;
        float cx = px + panelW - Theme.PAD - cw;
        Draw.roundRect(g, cx, py + 9, cw, 16, 8f, Theme.alpha(Theme.ACCENT, 0.16f));
        Draw.text(g, font, count, cx + 7, py + 13, Theme.ACCENT);

        Draw.hLine(g, px + 1, py + HEADER_H - 1, panelW - 2, Theme.BORDER_SOFT);
    }

    private void renderSidebar(GuiGraphicsExtractor g, int x, int y, int h,
                               int mouseX, int mouseY, float dt) {
        Draw.vLine(g, x + SIDEBAR_W - 1, y, h, Theme.BORDER_SOFT);

        int cy = y + Theme.PAD;
        for (Category category : Category.values()) {
            boolean selected = category == selectedCategory;
            boolean hovered = inside(mouseX, mouseY, x + 8, cy, SIDEBAR_W - 16, 26);

            Anim anim = hover("cat:" + category.name());
            anim.target(selected ? 1f : (hovered ? 0.45f : 0f));
            float t = anim.update(dt);

            if (t > 0.001f) {
                Draw.roundRect(g, x + 8, cy, SIDEBAR_W - 16, 26, Theme.RADIUS_MD,
                        Theme.mix(0x00000000, Theme.SURFACE_HOVER, t));
            }
            if (selected) {
                // Accent rail marking the active category.
                Draw.roundRect(g, x + 8, cy + 6, 2.5f, 14, 1.25f, Theme.ACCENT);
            }

            int textColor = Theme.mix(Theme.TEXT_DIM, Theme.TEXT, t);
            Draw.text(g, font, category.displayName(), x + 18, cy + 9, textColor);

            int moduleCount = VoidrixClient.modules().byCategory(category).size();
            Draw.textRight(g, font, String.valueOf(moduleCount), x + SIDEBAR_W - 16, cy + 9,
                    Theme.TEXT_FAINT);

            cy += 30;
        }

        // Footer hint.
        Draw.text(g, font, "RShift menu", x + 10, y + h - 26, Theme.TEXT_FAINT);
        Draw.text(g, font, "RCtrl  HUD", x + 10, y + h - 15, Theme.TEXT_FAINT);
    }

    private void renderModuleList(GuiGraphicsExtractor g, int x, int y, int w, int h,
                                  int mouseX, int mouseY, float dt) {
        Draw.vLine(g, x + w - 1, y, h, Theme.BORDER_SOFT);

        List<Module> modules = VoidrixClient.modules().byCategory(selectedCategory);
        int contentH = modules.size() * ROW_H + Theme.PAD * 2;
        listScroll = clampScroll(listScroll, contentH, h);

        g.enableScissor(x, y, x + w - 1, y + h);
        int ry = (int) (y + Theme.PAD - listScroll);

        for (Module module : modules) {
            if (ry + ROW_H >= y && ry <= y + h) {
                renderModuleRow(g, module, x + Theme.PAD, ry, w - Theme.PAD * 2 - 1,
                        mouseX, mouseY, dt);
            }
            ry += ROW_H;
        }
        g.disableScissor();

        renderScrollbar(g, x + w - 3, y, h, contentH, listScroll);
    }

    private void renderModuleRow(GuiGraphicsExtractor g, Module module, int x, int y, int w,
                                 int mouseX, int mouseY, float dt) {
        boolean selected = module == selectedModule;
        boolean hovered = inside(mouseX, mouseY, x, y, w, ROW_H - 4);

        Anim anim = hover("mod:" + module.id());
        anim.target(selected ? 1f : (hovered ? 0.5f : 0f));
        float t = anim.update(dt);

        Anim onAnim = hover("on:" + module.id());
        onAnim.target(module.isEnabled() ? 1f : 0f);
        float on = onAnim.update(dt);

        int rowH = ROW_H - 4;
        Draw.roundRect(g, x, y, w, rowH, Theme.RADIUS_MD,
                Theme.mix(Theme.alpha(Theme.SURFACE_RAISED, 0.55f), Theme.SURFACE_HOVER, t));

        if (selected) {
            Draw.roundRectOutline(g, x, y, w, rowH, Theme.RADIUS_MD, 1f,
                    Theme.alpha(Theme.ACCENT, 0.55f));
        }

        int nameColor = Theme.mix(Theme.TEXT_DIM, Theme.TEXT, Math.max(t, on));
        String name = Draw.ellipsize(font, module.displayName(), w - 44);
        Draw.text(g, font, name, x + 8, y + (rowH - font.lineHeight) / 2f, nameColor);

        toggle(g, x + w - 30, y + (rowH - 12) / 2f, on);
    }

    /** The pill switch used for every on/off control. */
    private void toggle(GuiGraphicsExtractor g, float x, float y, float t) {
        float w = 22f;
        float h = 12f;
        int track = Theme.mix(0xFF2A2A38, Theme.ACCENT, t);
        Draw.roundRect(g, x, y, w, h, h / 2f, track);
        if (t > 0.02f) {
            Draw.glow(g, x, y, w, h, h / 2f, 3f, Theme.ACCENT, 0.20f * t);
        }
        float knobR = (h - 3f) / 2f;
        float knobCx = x + 1.5f + knobR + t * (w - h);
        Draw.circle(g, knobCx, y + h / 2f, knobR, Theme.mix(0xFF8A8A9C, 0xFFFFFFFF, t));
    }

    private void renderSettings(GuiGraphicsExtractor g, int x, int y, int w, int h,
                                int mouseX, int mouseY, float dt) {
        if (selectedModule == null) {
            Draw.textCentered(g, font, "Select a module", x + w / 2f, y + h / 2f - 4, Theme.TEXT_FAINT);
            return;
        }

        // Title block.
        Draw.text(g, font, selectedModule.displayName(), x + Theme.PAD, y + Theme.PAD, Theme.TEXT);
        String desc = Draw.ellipsize(font, selectedModule.description(), w - Theme.PAD * 2);
        Draw.text(g, font, desc, x + Theme.PAD, y + Theme.PAD + 12, Theme.TEXT_FAINT);

        int top = y + Theme.PAD + 28;
        Draw.hLine(g, x + Theme.PAD, top - 6, w - Theme.PAD * 2, Theme.BORDER_SOFT);

        int availH = h - (top - y) - Theme.PAD;
        List<Row> rows = layoutRows(x + Theme.PAD, w - Theme.PAD * 2);
        int contentH = 0;
        for (Row row : rows) {
            contentH += row.height;
        }
        settingsScroll = clampScroll(settingsScroll, contentH, availH);

        g.enableScissor(x, top, x + w, top + availH);
        int ry = (int) (top - settingsScroll);
        for (Row row : rows) {
            if (ry + row.height >= top && ry <= top + availH) {
                renderRow(g, row, ry, mouseX, mouseY, dt);
            }
            ry += row.height;
        }
        g.disableScissor();

        renderScrollbar(g, x + w - 3, top, availH, contentH, settingsScroll);
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

    // -------------------------------------------------------------------------------------
    // Setting rows
    // -------------------------------------------------------------------------------------

    /** One laid-out control in the settings pane. */
    private static final class Row {
        final Setting<?> setting;
        final int x;
        final int width;
        final int height;
        /** Marks the synthetic hotkey row that every module gets. */
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
            // Title, swatch, three channel sliders and the rainbow toggle, with room to breathe.
            return 74;
        }
        return 22;
    }

    private List<Row> layoutRows(int x, int w) {
        List<Row> rows = new ArrayList<>();
        if (selectedModule == null) {
            return rows;
        }
        for (Setting<?> setting : selectedModule.settings()) {
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
            renderHotkeyRow(g, row, y, mouseX, mouseY, dt);
            return;
        }
        Setting<?> setting = row.setting;

        if (setting instanceof BoolSetting bool) {
            Draw.text(g, font, bool.displayName(), row.x, y + 5, Theme.TEXT_DIM);
            Anim anim = hover("set:" + selectedModule.id() + ":" + bool.id());
            anim.target(bool.value() ? 1f : 0f);
            toggle(g, row.x + row.width - 24, y + 3, anim.update(dt));
            return;
        }

        if (setting instanceof IntSetting slider) {
            String value = slider.value() + slider.suffix();
            renderSlider(g, row, y, slider.displayName(), value, slider.fraction());
            return;
        }

        if (setting instanceof DoubleSetting slider) {
            renderSlider(g, row, y, slider.displayName(), slider.format(), slider.fraction());
            return;
        }

        if (setting instanceof EnumSetting<?> choice) {
            Draw.text(g, font, choice.displayName(), row.x, y + 7, Theme.TEXT_DIM);
            String label = choice.currentLabel();
            float cw = font.width(label) + 16;
            float cx = row.x + row.width - cw;
            boolean hovered = inside(mouseX, mouseY, (int) cx, y + 3, (int) cw, 17);
            Draw.roundRect(g, cx, y + 3, cw, 17, Theme.RADIUS_SM,
                    hovered ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED);
            Draw.roundRectOutline(g, cx, y + 3, cw, 17, Theme.RADIUS_SM, 1f,
                    Theme.alpha(Theme.ACCENT, hovered ? 0.5f : 0.22f));
            Draw.text(g, font, label, cx + 8, y + 7, Theme.TEXT);
            return;
        }

        if (setting instanceof ColorSetting color) {
            Draw.text(g, font, color.displayName(), row.x, y + 4, Theme.TEXT_DIM);

            // Swatch showing the resolved colour, so rainbow mode is visible at a glance.
            Draw.roundRect(g, row.x + row.width - 22, y + 2, 22, 12, Theme.RADIUS_SM, color.resolve());
            Draw.roundRectOutline(g, row.x + row.width - 22, y + 2, 22, 12, Theme.RADIUS_SM, 1f,
                    Theme.BORDER);

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

            // Rainbow toggle.
            Anim anim = hover("rainbow:" + selectedModule.id() + ":" + color.id());
            anim.target(color.rainbow().value() ? 1f : 0f);
            Draw.text(g, font, "Rainbow", row.x, y + 57, Theme.TEXT_FAINT);
            toggle(g, row.x + row.width - 24, y + 55, anim.update(dt));
        }
    }

    private void renderSlider(GuiGraphicsExtractor g, Row row, int y, String name, String value, float t) {
        Draw.text(g, font, name, row.x, y + 3, Theme.TEXT_DIM);
        Draw.textRight(g, font, value, row.x + row.width, y + 3, Theme.TEXT);

        float trackY = y + 19;
        Draw.roundRect(g, row.x, trackY, row.width, 3, 1.5f, 0xFF23232F);
        Draw.roundRectGradientH(g, row.x, trackY, row.width * t, 3, 1.5f, Theme.ACCENT, Theme.ACCENT_ALT);
        float knobX = row.x + row.width * t;
        Draw.glow(g, knobX - 4, trackY - 2.5f, 8, 8, 4f, 3f, Theme.ACCENT, 0.35f);
        Draw.circle(g, knobX, trackY + 1.5f, 4f, 0xFFFFFFFF);
    }

    private void renderHotkeyRow(GuiGraphicsExtractor g, Row row, int y, int mouseX, int mouseY, float dt) {
        Draw.text(g, font, "Toggle key", row.x, y + 5, Theme.TEXT_DIM);

        boolean binding = bindingModule == selectedModule;
        String label = binding ? "Press a key..." : Keyboard.nameOf(selectedModule.keyCode());
        float cw = Math.max(52f, font.width(label) + 16);
        float cx = row.x + row.width - cw;
        boolean hovered = inside(mouseX, mouseY, (int) cx, y + 1, (int) cw, 17);

        Draw.roundRect(g, cx, y + 1, cw, 17, Theme.RADIUS_SM,
                binding ? Theme.alpha(Theme.ACCENT, 0.22f)
                        : (hovered ? Theme.SURFACE_ACTIVE : Theme.SURFACE_RAISED));
        Draw.roundRectOutline(g, cx, y + 1, cw, 17, Theme.RADIUS_SM, 1f,
                Theme.alpha(Theme.ACCENT, binding ? 0.8f : 0.22f));
        Draw.textCentered(g, font, label, cx + cw / 2f, y + 5,
                binding ? Theme.ACCENT : Theme.TEXT);
    }

    // -------------------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------------------

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int mx = (int) event.x();
        int my = (int) event.y();

        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }

        int bodyY = panelY + HEADER_H;
        int bodyH = panelH - HEADER_H;

        // Categories.
        int cy = bodyY + Theme.PAD;
        for (Category category : Category.values()) {
            if (inside(mx, my, panelX + 8, cy, SIDEBAR_W - 16, 26)) {
                if (selectedCategory != category) {
                    selectedCategory = category;
                    listScroll = 0f;
                    settingsScroll = 0f;
                    List<Module> inCategory = VoidrixClient.modules().byCategory(category);
                    selectedModule = inCategory.isEmpty() ? null : inCategory.getFirst();
                }
                return true;
            }
            cy += 30;
        }

        // Module rows.
        int listX = panelX + SIDEBAR_W;
        List<Module> modules = VoidrixClient.modules().byCategory(selectedCategory);
        int ry = (int) (bodyY + Theme.PAD - listScroll);
        int rowW = LIST_W - Theme.PAD * 2 - 1;
        for (Module module : modules) {
            if (inside(mx, my, listX + Theme.PAD, ry, rowW, ROW_H - 4)) {
                if (mx >= listX + Theme.PAD + rowW - 30) {
                    module.toggle();
                    VoidrixClient.config().save();
                } else {
                    selectedModule = module;
                    settingsScroll = 0f;
                }
                return true;
            }
            ry += ROW_H;
        }

        // Settings pane.
        if (selectedModule != null) {
            int sx = panelX + SIDEBAR_W + LIST_W;
            int sw = panelW - SIDEBAR_W - LIST_W;
            int top = bodyY + Theme.PAD + 28;
            List<Row> rows = layoutRows(sx + Theme.PAD, sw - Theme.PAD * 2);
            int y = (int) (top - settingsScroll);
            for (Row row : rows) {
                if (handleRowClick(row, y, mx, my)) {
                    return true;
                }
                y += row.height;
            }
        }

        // Clicking outside the panel closes it, like any other overlay.
        if (!inside(mx, my, panelX, panelY, panelW, panelH)) {
            onClose();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private boolean handleRowClick(Row row, int y, int mx, int my) {
        if (row.hotkey) {
            float cw = Math.max(52f, font.width(Keyboard.nameOf(selectedModule.keyCode())) + 16);
            if (inside(mx, my, (int) (row.x + row.width - cw), y + 1, (int) cw, 17)) {
                bindingModule = selectedModule;
                return true;
            }
            return false;
        }

        Setting<?> setting = row.setting;

        if (setting instanceof BoolSetting bool
                && inside(mx, my, row.x, y, row.width, row.height)) {
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

        if (setting instanceof EnumSetting<?> choice
                && inside(mx, my, row.x, y, row.width, row.height)) {
            choice.cycle();
            VoidrixClient.config().save();
            return true;
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
        int r = channel == 0 ? v : color.red();
        int g = channel == 1 ? v : color.green();
        int b = channel == 2 ? v : color.blue();
        color.setRgb(r, g, b);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (dragging != null && selectedModule != null) {
            int sx = panelX + SIDEBAR_W + LIST_W;
            int sw = panelW - SIDEBAR_W - LIST_W;
            List<Row> rows = layoutRows(sx + Theme.PAD, sw - Theme.PAD * 2);
            for (Row row : rows) {
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
        int listX = panelX + SIDEBAR_W;
        float amount = (float) scrollY * 18f;
        if (mouseX >= listX && mouseX < listX + LIST_W) {
            listScroll -= amount;
        } else if (mouseX >= listX + LIST_W) {
            settingsScroll -= amount;
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (bindingModule != null) {
            int key = event.key();
            // Escape clears the binding rather than assigning Escape to it.
            bindingModule.setKeyCode(key == GLFW.GLFW_KEY_ESCAPE ? -1 : key);
            Keyboard.forget(bindingModule.id());
            bindingModule = null;
            VoidrixClient.config().save();
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

    // -------------------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------------------

    private static boolean inside(int mx, int my, float x, float y, float w, float h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static float clampScroll(float scroll, int contentH, int viewH) {
        float max = Math.max(0f, contentH - viewH);
        return Math.clamp(scroll, 0f, max);
    }

    /** Opens the menu, used by the key binding and by other screens. */
    public static void open() {
        Minecraft.getInstance().setScreenAndShow(new MenuScreen());
    }
}
