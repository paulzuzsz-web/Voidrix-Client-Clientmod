package gg.voidrix.client.screen;

import java.util.function.DoubleConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

/**
 * Schieberegler im Stil der Umschalter: flache Bahn, Fuellung bis zum Griff.
 *
 * <p>{@link AbstractSliderButton} rechnet intern mit 0..1; hier wird auf das fachliche
 * Intervall [min, max] umgerechnet, damit die Konfiguration echte Werte speichert.
 */
public final class ValueSlider extends AbstractSliderButton {

    private static final int EDGE_PADDING = 8;
    private static final int TRACK_HEIGHT = 4;
    private static final int HANDLE_WIDTH = 4;

    private final String label;
    private final double min;
    private final double max;
    private final DoubleConsumer setter;

    public ValueSlider(
            int x, int y, int width, int height,
            String label, double min, double max, double initial,
            DoubleConsumer setter) {
        super(x, y, width, height, Component.empty(), (initial - min) / (max - min));
        this.label = label;
        this.min = min;
        this.max = max;
        this.setter = setter;
        updateMessage();
    }

    private double actualValue() {
        return min + value * (max - min);
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(String.format("%s  %.1fx", label, actualValue())));
    }

    @Override
    protected void applyValue() {
        setter.accept(actualValue());
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        Theme.roundedRect(g, x, y, width, height,
                isHoveredOrFocused() ? Theme.ROW_HOVER : Theme.ROW);

        g.text(Minecraft.getInstance().font, getMessage().getString(),
                x + EDGE_PADDING, y + 4, Theme.TEXT);

        int trackX = x + EDGE_PADDING;
        int trackWidth = width - 2 * EDGE_PADDING;
        int trackY = y + height - EDGE_PADDING;
        g.fill(trackX, trackY, trackX + trackWidth, trackY + TRACK_HEIGHT, Theme.TRACK_OFF);

        int filled = (int) (trackWidth * value);
        g.fill(trackX, trackY, trackX + filled, trackY + TRACK_HEIGHT, Theme.ACCENT_SOFT);

        int handleX = Math.min(trackX + filled, trackX + trackWidth - HANDLE_WIDTH);
        g.fill(handleX, trackY - 2, handleX + HANDLE_WIDTH, trackY + TRACK_HEIGHT + 2, Theme.ACCENT);
    }
}
