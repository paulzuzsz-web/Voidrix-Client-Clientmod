package dev.voidrix.mixin;

import dev.voidrix.VoidrixClient;
import dev.voidrix.module.misc.DropStackModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Makes the drop key throw the whole stack.
 *
 * <p>Vanilla already has both behaviours - {@code drop(false)} for one item, {@code drop(true)} for
 * the stack - and simply binds the second to Control+drop. All this does is flip which one the bare
 * key reaches, by rewriting the argument on the way in. Nothing new is sent to the server and no
 * vanilla logic is skipped, which is why this is a one-line change rather than a reimplementation
 * of dropping.
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @ModifyVariable(method = "drop(Z)Z", at = @At("HEAD"), argsOnly = true, index = 1)
    private boolean voidrix$dropWholeStack(boolean dropStack) {
        DropStackModule module = VoidrixClient.dropStack();
        if (module == null || !module.isEnabled()) {
            return dropStack;
        }
        return module.shouldDropWholeStack();
    }
}
