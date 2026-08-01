package dev.voidrix.module.hud;

import dev.voidrix.setting.EnumSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

/** The biome you are standing in. */
public final class BiomeHud extends SimpleHudModule {
    private final EnumSetting<NameStyle> style;

    public BiomeHud() {
        super("biome", "Biome", "The biome at your feet", 0.0, 0.0);
        this.style = addEnum("style", "Name style", "How the biome id is written out", NameStyle.PRETTY);
    }

    @Override
    protected String label() {
        return "Biome";
    }

    @Override
    protected String value() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return "--";
        }
        BlockPos pos = mc.player.blockPosition();
        var key = mc.level.getBiome(pos).unwrapKey();
        if (key.isEmpty()) {
            return "--";
        }
        Identifier id = key.get().identifier();
        return switch (style.value()) {
            case PRETTY -> prettify(id.getPath());
            case RAW -> id.getPath();
            case FULL -> id.toString();
        };
    }

    /** {@code dark_forest} becomes {@code Dark Forest}. */
    private static String prettify(String path) {
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }

    public enum NameStyle {
        /** Dark Forest */
        PRETTY,
        /** dark_forest */
        RAW,
        /** minecraft:dark_forest */
        FULL
    }
}
