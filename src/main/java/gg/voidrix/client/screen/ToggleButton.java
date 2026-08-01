package gg.voidrix.client.screen;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

/**
 * Zeile mit Beschriftung links und Schiebeschalter rechts.
 *
 * <p>Erbt von {@link AbstractButton}, damit Klick-, Tastatur- und Fokusbehandlung von Vanilla
 * kommen; gezeichnet wird ausschliesslich in {@link #extractContents}, die Vanilla-Textur
 * bleibt also aussen vor.
 */
public final class ToggleButton extends AbstractButton {

    private static final int TRACK_WIDTH = 22;
    private static final int TRACK_HEIGHT = 10;
    private static final int KNOB_SIZE = 6;
    private static final int EDGE_PADDING = 8;
    private static final int ACCENT_BAR_WIDTH = 2;

    private final BooleanSupplier getter;
    private final Consumer<Boolean> setter;

    public ToggleButton(
            int x, int y, int width, int height,
            Component label,
            BooleanSupplier getter,
            Consumer<Boolean> setter) {
        super(x, y, width, height, label);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        setter.accept(!getter.getAsBoolean());
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        boolean on = getter.getAsBoolean();
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        Theme.roundedRect(g, x, y, width, height,
                isHoveredOrFocused() ? Theme.ROW_BG_HOVER : Theme.ROW_BG);

        // Akzentkante links markiert aktive Optionen auch ohne Blick auf den Schalter.
        if (on) {
            g.fill(x, y + 1, x + ACCENT_BAR_WIDTH, y + height - 1, Theme.ACCENT);
        }

        int textY = y + (height - 8) / 2;
        g.text(Minecraft.getInstance().font, getMessage().getString(),
                x + EDGE_PADDING, textY, on ? Theme.TEXT : Theme.TEXT_MUTED);

        int trackX = x + width - EDGE_PADDING - TRACK_WIDTH;
        int trackY = y + (height - TRACK_HEIGHT) / 2;
        Theme.roundedRect(g, trackX, trackY, TRACK_WIDTH, TRACK_HEIGHT,
                on ? Theme.ACCENT_SOFT : Theme.TRACK_OFF);

        int knobInset = (TRACK_HEIGHT - KNOB_SIZE) / 2;
        int knobX = on
                ? trackX + TRACK_WIDTH - KNOB_SIZE - knobInset
                : trackX + knobInset;
        Theme.roundedRect(g, knobX, trackY + knobInset, KNOB_SIZE, KNOB_SIZE,
                on ? Theme.ACCENT : Theme.KNOB_OFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
