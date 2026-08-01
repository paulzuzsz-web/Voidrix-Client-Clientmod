package gg.norisk.client.v2.mixin.autoreconnect;

import gg.norisk.client.v2.modules.autoreconnect.AutoReconnect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin {
   @Inject(method = "startConnecting", at = @At("HEAD"))
   private static void nrc$cacheServerIp(
      Screen screen, Minecraft minecraft, ServerAddress serverAddress, ServerData serverData, boolean bl, TransferState transferState, CallbackInfo ci
   ) {
      if (serverData != null) {
         AutoReconnect.INSTANCE.cacheServerIp(serverData.ip);
      }
   }
}
