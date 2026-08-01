package gg.norisk.client.v2.mixin.serverstyling;

import gg.norisk.client.v2.serverstyling.PromotedServerVisibility;
import gg.norisk.compat.client.MCClient;
import gg.norisk.compat.text.TextKt;
import java.net.URI;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.Entry;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public abstract class PromotedServerScreenMixin {
   private static final String NRC_HOST_URL = "https://norisk.host";
   @Shadow
   private ServerSelectionList serverSelectionList;
   @Shadow
   private Button selectButton;
   @Shadow
   private Button editButton;

   private boolean nrc$isAdvert(ServerData data) {
      return data != null && "advert.norisk.space".equalsIgnoreCase(data.ip);
   }

   private boolean nrc$advertSelected() {
      Entry entry = (Entry)this.serverSelectionList.getSelected();
      return entry instanceof OnlineServerEntry ? this.nrc$isAdvert(((OnlineServerEntry)entry).getServerData()) : false;
   }

   @Inject(method = "join", at = @At("HEAD"), require = 0, cancellable = true)
   private void nrc$blockAdvertJoin(ServerData serverData, CallbackInfo ci) {
      if (this.nrc$isAdvert(serverData)) {
         MCClient.openUri(URI.create("https://norisk.host"));
         ci.cancel();
      }
   }

   @Inject(method = "onSelectedChange", at = @At("TAIL"), require = 0)
   private void nrc$greyJoinButton(CallbackInfo ci) {
      if (this.nrc$advertSelected()) {
         this.selectButton.active = true;
         this.selectButton.setMessage(TextKt.translatableText("nrc.serverstyling.advert.openWebsite", new Object[0]));
         this.editButton.active = false;
      } else {
         this.selectButton.setMessage(TextKt.translatableText("selectServer.select", new Object[0]));
      }
   }

   @Inject(method = "deleteCallback", at = @At("HEAD"), require = 0)
   private void nrc$persistHide(boolean confirmed, CallbackInfo ci) {
      if (confirmed && this.nrc$advertSelected()) {
         PromotedServerVisibility.hide();
      }
   }

   @Inject(method = "init", at = @At("TAIL"), require = 0)
   private void nrc$selectFirstRealServer(CallbackInfo ci) {
      List<? extends Entry> entries = this.serverSelectionList.children();
      if (entries.size() >= 2) {
         Entry first = entries.get(0);
         if (first instanceof OnlineServerEntry && this.nrc$isAdvert(((OnlineServerEntry)first).getServerData())) {
            Entry second = entries.get(1);
            if (second instanceof OnlineServerEntry) {
               this.serverSelectionList.setSelected(second);
            }
         }
      }
   }
}
