package gg.norisk.client.v2.mixin.autoreconnect;

import gg.norisk.client.v2.modules.autoreconnect.AutoReconnect;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin {
   @Shadow
   @Final
   private LinearLayout layout;
   @Shadow
   @Final
   private DisconnectionDetails details;
   @Unique
   private Button nrc$reconnectButton;
   @Unique
   private Button nrc$pauseButton;

   @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;arrangeElements()V"))
   private void nrc$addReconnectButton(CallbackInfo ci) {
      if (AutoReconnect.INSTANCE.isEnabled()) {
         AutoReconnect.INSTANCE.onDisconnectedScreenInit(this.details.reason().getString());
         if (AutoReconnect.INSTANCE.hasLastServer()) {
            LinearLayout row = LinearLayout.horizontal().spacing(4);
            this.nrc$reconnectButton = Button.builder(
                  Component.literal(AutoReconnect.INSTANCE.getButtonText()), btn -> AutoReconnect.INSTANCE.onReconnectButtonClick()
               )
               .width(176)
               .build();
            row.addChild(this.nrc$reconnectButton);
            this.nrc$pauseButton = Button.builder(
                  Component.literal(AutoReconnect.INSTANCE.getPauseButtonText()), btn -> AutoReconnect.INSTANCE.onPauseButtonClick()
               )
               .width(20)
               .build();
            row.addChild(this.nrc$pauseButton);
            this.layout.addChild(row);
            AutoReconnect.INSTANCE.setReconnectButton(this.nrc$reconnectButton);
            AutoReconnect.INSTANCE.setPauseButton(this.nrc$pauseButton);
         }
      }
   }
}
