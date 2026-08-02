package gg.voidrix.client.mixin;

import gg.voidrix.client.module.ModuleManager;
import gg.voidrix.client.module.pvp.ItemHighlighterModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Item Highlighter: laesst am Boden liegende Items leuchten.
 *
 * <p>Statt eigene Umrisse zu zeichnen, meldet Voidrix fuer passende
 * Item-Entities einfach "diese Entity leuchtet". Vanilla uebernimmt dann den
 * kompletten Umriss-Effekt (derselbe wie beim Leuchten-Effekt).</p>
 *
 * <p>Wichtig: Es wird nur die <b>Anzeige</b> beeinflusst - der Spielzustand der
 * Entity bleibt unangetastet, und es geht nichts an den Server.</p>
 */
@Mixin(Entity.class)
public class EntityMixin {

	@Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
	private void voidrix$highlightItems(CallbackInfoReturnable<Boolean> info) {
		// bereits leuchtende Entities nicht doppelt behandeln
		if (info.getReturnValue()) {
			return;
		}

		if (!(ModuleManager.get("item_highlighter") instanceof ItemHighlighterModule module)
				|| !module.isEnabled()) {
			return;
		}

		Entity self = (Entity) (Object) this;

		if (module.shouldHighlight(self)) {
			info.setReturnValue(true);
		}
	}
}
