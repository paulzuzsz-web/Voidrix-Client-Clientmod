package gg.voidrix.client.screen;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

/**
 * Eintrag in der linken Navigationsspalte.
 *
 * <p>Der aktive Eintrag traegt links einen Akzentbalken und hellen Text, inaktive bleiben
 * gedaempft - so ist die Auswahl auch ohne Farbwahrnehmung an Position und Helligkeit erkennbar.
 */
public final class CategoryTab extends AbstractButton {

    private static final int MARKER_WIDTH = 2;
    private static final int TEXT_INSET = 10;

    private final int index;
    private final java.util.function.IntSupplier active;
    private final Consumer<Integer> onSelect;

    public CategoryTab(
            int x, int y, int width, int height,
            Component label,
            int index,
            java.util.function.IntSupplier active,
            Consumer<Integer> onSelect) {
        super(x, y, width, height, label);
        this.index = index;
        this.active = active;
        this.onSelect = onSelect;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        onSelect.accept(index);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        boolean selected = active.getAsInt() == index;
        int x = getX();
        int y = getY();

        if (selected) {
            Theme.roundedRect(g, x, y, getWidth(), getHeight(), Theme.ROW_HOVER);
            g.fill(x, y + 2, x + MARKER_WIDTH, y + getHeight() - 2, Theme.ACCENT);
        } else if (isHoveredOrFocused()) {
            Theme.roundedRect(g, x, y, getWidth(), getHeight(), Theme.ROW);
        }

        g.text(Minecraft.getInstance().font, getMessage().getString(),
                x + TEXT_INSET, y + (getHeight() - 8) / 2,
                selected ? Theme.TEXT : Theme.TEXT_MUTED);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
