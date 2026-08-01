package gg.norisk.client.v2.mixin.chatheads;

import gg.norisk.client.v2.modules.chatheads.ChatHeads;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$1")
public class ChatHud1Mixin {
   @Inject(method = "accept(Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;IF)V", at = @At("HEAD"))
   private void nrc$setCurrentSkinTextures(Line visible, int y, float opacity, CallbackInfo ci) {
      ChatHeads.INSTANCE.setCurrentSkinTextures(visible);
   }

   @Inject(method = "accept(Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;IF)V", at = @At("TAIL"))
   private void nrc$removeCurrentSkinTextures(Line visible, int y, float opacity, CallbackInfo ci) {
      ChatHeads.INSTANCE.removeCurrentSkinTextures();
   }
}
