package gg.voidrix.client.v2.mixin.serverstyling;

import gg.voidrix.client.v2.serverstyling.PromotedServerVisibility;
import gg.voidrix.compat.client.MCClient;
import gg.voidrix.compat.text.TextKt;
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
   private static final String Voidrix_HOST_URL = "https://norisk.host";
   @Shadow
   private ServerSelectionList serverSelectionList;
   @Shadow
   private Button selectButton;
   @Shadow
   private Button editButton;

   private boolean voidrix$isAdvert(ServerData data) {
      return data != null && "advert.norisk.space".equalsIgnoreCase(data.ip);
   }

   private boolean voidrix$advertSelected() {
      Entry entry = (Entry)this.serverSelectionList.getSelected();
      return entry instanceof OnlineServerEntry ? this.voidrix$isAdvert(((OnlineServerEntry)entry).getServerData()) : false;
   }

   @Inject(method = "join", at = @At("HEAD"), require = 0, cancellable = true)
   private void voidrix$blockAdvertJoin(ServerData serverData, CallbackInfo ci) {
      if (this.voidrix$isAdvert(serverData)) {
         MCClient.openUri(URI.create("https://norisk.host"));
         ci.cancel();
      }
   }

   @Inject(method = "onSelectedChange", at = @At("TAIL"), require = 0)
   private void voidrix$greyJoinButton(CallbackInfo ci) {
      if (this.voidrix$advertSelected()) {
         this.selectButton.active = true;
         this.selectButton.setMessage(TextKt.translatableText("voidrix.serverstyling.advert.openWebsite", new Object[0]));
         this.editButton.active = false;
      } else {
         this.selectButton.setMessage(TextKt.translatableText("selectServer.select", new Object[0]));
      }
   }

   @Inject(method = "deleteCallback", at = @At("HEAD"), require = 0)
   private void voidrix$persistHide(boolean confirmed, CallbackInfo ci) {
      if (confirmed && this.voidrix$advertSelected()) {
         PromotedServerVisibility.hide();
      }
   }

   @Inject(method = "init", at = @At("TAIL"), require = 0)
   private void voidrix$selectFirstRealServer(CallbackInfo ci) {
      List<? extends Entry> entries = this.serverSelectionList.children();
      if (entries.size() >= 2) {
         Entry first = entries.get(0);
         if (first instanceof OnlineServerEntry && this.voidrix$isAdvert(((OnlineServerEntry)first).getServerData())) {
            Entry second = entries.get(1);
            if (second instanceof OnlineServerEntry) {
               this.serverSelectionList.setSelected(second);
            }
         }
      }
   }
}
