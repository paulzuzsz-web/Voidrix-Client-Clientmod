package gg.norisk.client.v2.mixin.clearbackground;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import gg.norisk.compat.event.ClearBackgroundEvent;
import gg.norisk.compat.event.ClearBackgroundEvents;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ChatComponent.ChatGraphicsAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatComponent.class)
public abstract class ChatBackgroundMixin {
   @WrapWithCondition(
      method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V",
      require = 0,
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V")
   )
   private boolean nrc$cancelChatFill(ChatGraphicsAccess instance, int x1, int y1, int x2, int y2, int color) {
      ClearBackgroundEvent event = new ClearBackgroundEvent(false);
      ClearBackgroundEvents.INSTANCE.getChatBackgroundEvent().invoke(event);
      return !event.isCancelled();
   }

   @WrapWithCondition(
      method = "lambda$extractRenderState$1",
      require = 0,
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V")
   )
   private static boolean nrc$cancelChatFillLambda(ChatGraphicsAccess instance, int x1, int y1, int x2, int y2, int color) {
      ClearBackgroundEvent event = new ClearBackgroundEvent(false);
      ClearBackgroundEvents.INSTANCE.getChatBackgroundEvent().invoke(event);
      return !event.isCancelled();
   }
}
