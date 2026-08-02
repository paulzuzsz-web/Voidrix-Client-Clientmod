package gg.voidrix.client.module.pvp;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.ColorSetting;
import gg.voidrix.client.module.setting.EnumSetting;
import gg.voidrix.client.module.setting.IntSetting;
import gg.voidrix.client.ui.Draw;
import gg.voidrix.client.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Ersetzt das Vanilla-Fadenkreuz durch ein eigenes.
 *
 * <p>Das Vanilla-Element wird ueber die Fabric-HUD-Registry ersetzt
 * (siehe {@code VoidrixClient#registerHud}), es wird also nichts
 * uebermalt - das spart Zeichenaufwand und bleibt kompatibel.</p>
 */
public class CustomCrosshairModule extends Module {

	/** Form des Fadenkreuzes. */
	public enum Style {
		CROSS,
		DOT,
		CIRCLE,
		T_SHAPE
	}

	private final EnumSetting<Style> style =
			add(new EnumSetting<>("style", "Form", "Aussehen des Fadenkreuzes", Style.CROSS));

	private final IntSetting size =
			add(new IntSetting("size", "Groesse", "Laenge der Striche", 5, 1, 12));

	private final IntSetting thickness =
			add(new IntSetting("thickness", "Staerke", "Dicke der Striche", 1, 1, 3));

	private final IntSetting gapSize =
			add(new IntSetting("gap", "Luecke", "Abstand zur Mitte", 2, 0, 8));

	private final ColorSetting color =
			add(new ColorSetting("color", "Farbe", "Grundfarbe", 0xFFFFFFFF));

	private final BoolSetting highlightTarget =
			add(new BoolSetting("highlight_target", "Ziel hervorheben",
					"Farbe wechseln, wenn ein Lebewesen anvisiert wird", true));

	private final ColorSetting targetColor =
			add(new ColorSetting("target_color", "Zielfarbe", "Farbe beim Anvisieren", Theme.ACCENT_2));

	public CustomCrosshairModule() {
		super("custom_crosshair", "Custom Crosshair", "Eigenes Fadenkreuz", Category.PVP);
	}

	/**
	 * Zeichnet das Fadenkreuz mittig auf dem Bildschirm.
	 * Wird vom HUD-Element aufgerufen, das Vanillas Fadenkreuz ersetzt.
	 */
	public void render(GuiGraphicsExtractor g) {
		int centerX = g.guiWidth() / 2;
		int centerY = g.guiHeight() / 2;

		int drawColor = highlightTarget.value() && isTargetingEntity() ? targetColor.value() : color.value();

		int length = size.value();
		int weight = thickness.value();
		int gap = gapSize.value();

		switch (style.value()) {
			case DOT -> Draw.rect(g, centerX - weight / 2, centerY - weight / 2,
					Math.max(1, weight), Math.max(1, weight), drawColor);

			case CIRCLE -> drawCircle(g, centerX, centerY, length, drawColor);

			case T_SHAPE -> {
				// waagerechter Balken plus nur der untere senkrechte Strich
				Draw.rect(g, centerX - gap - length, centerY - weight / 2, length, weight, drawColor);
				Draw.rect(g, centerX + gap, centerY - weight / 2, length, weight, drawColor);
				Draw.rect(g, centerX - weight / 2, centerY + gap, weight, length, drawColor);
			}

			default -> {
				// klassisches Kreuz aus vier Strichen
				Draw.rect(g, centerX - gap - length, centerY - weight / 2, length, weight, drawColor);
				Draw.rect(g, centerX + gap, centerY - weight / 2, length, weight, drawColor);
				Draw.rect(g, centerX - weight / 2, centerY - gap - length, weight, length, drawColor);
				Draw.rect(g, centerX - weight / 2, centerY + gap, weight, length, drawColor);
			}
		}
	}

	/** Kreis aus einzelnen Pixeln (Mittelpunktalgorithmus, grob genaehert). */
	private void drawCircle(GuiGraphicsExtractor g, int centerX, int centerY, int radius, int drawColor) {
		for (int angle = 0; angle < 360; angle += 6) {
			double radians = Math.toRadians(angle);
			int px = centerX + (int) Math.round(Math.cos(radians) * radius);
			int py = centerY + (int) Math.round(Math.sin(radians) * radius);

			Draw.rect(g, px, py, 1, 1, drawColor);
		}
	}

	/** true, wenn der Spieler gerade ein Lebewesen anvisiert. */
	private boolean isTargetingEntity() {
		Minecraft client = Minecraft.getInstance();
		HitResult hit = client.hitResult;

		if (hit == null || hit.getType() != HitResult.Type.ENTITY) {
			return false;
		}

		Entity target = ((EntityHitResult) hit).getEntity();
		return target.isAlive();
	}
}
