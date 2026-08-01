package gg.voidrix.client.v2.mixin.serverstyling;

import gg.voidrix.client.v2.serverstyling.PromotedServerVisibility;
import java.util.List;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.ServerData.Type;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerList.class, priority = 2500)
public abstract class PromotedServerMixin {
   private static final String ADVERT_IP = "advert.norisk.space";
   private static final String ADVERT_NAME = "§f§lNoRisk§b§l.Host";
   @Shadow
   @Final
   private List<ServerData> serverList;

   @Inject(method = "load", at = @At("TAIL"), require = 0)
   private void voidrix$injectPromotedServer(CallbackInfo ci) {
      if (!PromotedServerVisibility.isHidden()) {
         this.serverList.removeIf(server -> "advert.norisk.space".equalsIgnoreCase(server.ip));
         this.serverList.add(0, new ServerData("§f§lNoRisk§b§l.Host", "advert.norisk.space", Type.OTHER));
      }
   }
}
