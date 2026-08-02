package gg.voidrix.client.mixin;

import gg.voidrix.client.user.VoidrixUsers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Setzt das leuchtende "V" vor den <b>Nametag ueber dem Kopf</b>.
 *
 * <p>{@code EntityRenderer#getNameTag} liefert den Text, der ueber der Entity
 * schwebt. Handelt es sich um einen Spieler, der als Voidrix-Nutzer bekannt
 * ist, wird das Praefix vorangestellt.</p>
 *
 * <p>Hier kommt die <i>animierte</i> Variante zum Einsatz: Voidrix+ Nutzer
 * bekommen ein V, dessen Farbe zwischen Violett und Cyan pulsiert. Im Chat und
 * in der Tab-Liste ist das nicht moeglich, weil diese Texte nur einmal gebaut
 * und dann zwischengespeichert werden.</p>
 *
 * <p>Welche Spieler ueberhaupt ein V bekommen, entscheidet
 * {@link VoidrixUsers} - dort ist auch beschrieben, wie sich das ueber ein
 * Backend oder eine Custom Payload auf andere Spieler ausweiten laesst.</p>
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

	@Inject(method = "getNameTag", at = @At("RETURN"), cancellable = true)
	private void voidrix$prefixNameTag(Entity entity, CallbackInfoReturnable<Component> info) {
		if (!(entity instanceof Player player)) {
			return;
		}

		Component original = info.getReturnValue();

		if (original == null) {
			return;
		}

		if (!VoidrixUsers.isVoidrixUser(player.getUUID())) {
			return;
		}

		info.setReturnValue(VoidrixUsers.decorate(player.getUUID(), original, true));
	}
}
