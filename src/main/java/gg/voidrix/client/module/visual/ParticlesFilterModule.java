package gg.voidrix.client.module.visual;

import gg.voidrix.client.module.Category;
import gg.voidrix.client.module.Module;
import gg.voidrix.client.module.setting.BoolSetting;
import gg.voidrix.client.module.setting.IntSetting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

/**
 * Filtert stoerende Partikel heraus.
 *
 * <p>Umgesetzt per Mixin auf {@code ParticleEngine}: passende Partikel werden
 * gar nicht erst erzeugt. Das spart nebenbei Leistung bei vielen Effekten.</p>
 */
public class ParticlesFilterModule extends Module {

	private final BoolSetting hideExplosions =
			add(new BoolSetting("hide_explosions", "Explosionen", "Explosionspartikel ausblenden", false));

	private final BoolSetting hidePotions =
			add(new BoolSetting("hide_potions", "Trankeffekte", "Wirbelnde Trankpartikel ausblenden", true));

	private final BoolSetting hideEnchant =
			add(new BoolSetting("hide_enchant", "Verzauberungstisch", "Schwebende Glyphen ausblenden", false));

	private final BoolSetting hideBlockBreak =
			add(new BoolSetting("hide_block_break", "Blockbruch", "Bruchstuecke beim Abbauen ausblenden", false));

	private final BoolSetting hideFire =
			add(new BoolSetting("hide_fire", "Feuer & Rauch", "Flammen- und Rauchpartikel ausblenden", false));

	private final IntSetting maxParticles =
			add(new IntSetting("max_particles", "Obergrenze", "Maximale Anzahl gleichzeitiger Partikel", 4000, 200, 16000));

	public ParticlesFilterModule() {
		super("particles_filter", "Particles Filter", "Stoerende Partikel ausblenden", Category.VISUAL);
	}

	public int getMaxParticles() {
		return maxParticles.value();
	}

	/**
	 * Entscheidet, ob ein Partikel unterdrueckt wird.
	 * Vom {@code ParticleEngineMixin} aufgerufen.
	 *
	 * <h2>Weitere Partikel filtern</h2>
	 * Einfach eine neue {@link BoolSetting} anlegen und hier einen Vergleich
	 * gegen den passenden {@link ParticleTypes}-Eintrag ergaenzen.
	 */
	public boolean shouldSuppress(ParticleOptions options) {
		if (options == null) {
			return false;
		}

		var type = options.getType();

		if (hideExplosions.value()
				&& (type == ParticleTypes.EXPLOSION || type == ParticleTypes.EXPLOSION_EMITTER)) {
			return true;
		}

		if (hidePotions.value()
				&& (type == ParticleTypes.EFFECT || type == ParticleTypes.INSTANT_EFFECT
				|| type == ParticleTypes.ENTITY_EFFECT)) {
			return true;
		}

		if (hideEnchant.value() && type == ParticleTypes.ENCHANT) {
			return true;
		}

		if (hideBlockBreak.value() && type == ParticleTypes.BLOCK) {
			return true;
		}

		if (hideFire.value()
				&& (type == ParticleTypes.FLAME || type == ParticleTypes.SMOKE
				|| type == ParticleTypes.LARGE_SMOKE)) {
			return true;
		}

		return false;
	}
}
