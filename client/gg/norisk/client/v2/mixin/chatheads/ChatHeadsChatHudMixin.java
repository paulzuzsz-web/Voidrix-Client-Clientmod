package gg.norisk.client.v2.mixin.chatheads;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.chatheads.ChatHeads;
import gg.norisk.client.v2.modules.chatheads.ChatHudLineVisibleExt;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import kotlin.Pair;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public abstract class ChatHeadsChatHudMixin {
   @Inject(method = "addMessageToDisplayQueue", at = @At("HEAD"))
   private void nrc$addVisibleMessageHead(GuiMessage message, CallbackInfo ci) {
      String string = message.content().getString();
      ChatHeads.INSTANCE.setCurrentMessageOwner(ChatHeads.INSTANCE.getMessagesOwner(string, message.content()));
      if (ChatHeads.INSTANCE.getCurrentMessageOwner() == null) {
         ChatHeads.INSTANCE.getTellReceiver(message.content()).ifPresent(ChatHeads.INSTANCE::setCurrentMessageTellReceiver);
      }
   }

   @Inject(method = "addMessageToDisplayQueue", at = @At("TAIL"))
   private void nrc$addVisibleMessageTail(GuiMessage message, CallbackInfo ci) {
      ChatHeads.INSTANCE.setCurrentMessageOwner(null);
      ChatHeads.INSTANCE.setCurrentMessageTellReceiver(null);
   }

   @WrapOperation(method = "addMessageToDisplayQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;addFirst(Ljava/lang/Object;)V"))
   private void nrc$addVisibleMessage(List instance, Object element, Operation<Void> original) {
      Line line = (Line)element;
      Pair<UUID, Supplier<PlayerSkin>> messageOwner = ChatHeads.INSTANCE.getCurrentMessageOwner();
      String tellReceiver = ChatHeads.INSTANCE.getCurrentMessageTellReceiver();
      if (messageOwner != null) {
         ((ChatHudLineVisibleExt)line).setNrc_messageSender((UUID)messageOwner.component1());
         ((ChatHudLineVisibleExt)line).setNrc_skinTextures((Supplier<PlayerSkin>)messageOwner.component2());
      } else if (tellReceiver != null) {
         ((ChatHudLineVisibleExt)line).setNrc_tellReceiver(tellReceiver);
      }

      original.call(new Object[]{instance, element});
   }
}
