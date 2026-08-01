package gg.norisk.client.v2.mixin.serverstyling;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnlineServerEntry.class)
public abstract class PromotedServerMotdMixin {
   @Shadow
   @Final
   private ServerData serverData;
   private static final String MOTD_KEY = "nrc.serverstyling.advert.motd";
   private static final String MOTD_FALLBACK = "Premium Minecraft Hosting\nOhne Abo • DDoS-Schutz • Sofort online";

   private boolean nrc$isAdvert() {
      return "advert.norisk.space".equalsIgnoreCase(this.serverData.ip);
   }

   @Inject(method = "extractContent", at = @At("HEAD"), require = 0)
   private void nrc$advertMotd(GuiGraphicsExtractor ctx, int mouseX, int mouseY, boolean hovered, float delta, CallbackInfo ci) {
      if (this.nrc$isAdvert()) {
         this.serverData.motd = Component.translatableWithFallback(
            "nrc.serverstyling.advert.motd", "Premium Minecraft Hosting\nOhne Abo • DDoS-Schutz • Sofort online"
         );
         this.serverData.status = CommonComponents.EMPTY;
         this.serverData.version = CommonComponents.EMPTY;
         this.serverData.protocol = SharedConstants.getProtocolVersion();
      }
   }

   @WrapOperation(
      method = "extractContent",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
      ),
      require = 0
   )
   private void nrc$skipBars(GuiGraphicsExtractor g, RenderPipeline p, Identifier s, int x, int y, int w, int h, Operation<Void> op) {
      if (!this.nrc$isAdvert() || w != 10 || h != 8) {
         op.call(new Object[]{g, p, s, x, y, w, h});
      }
   }
}
