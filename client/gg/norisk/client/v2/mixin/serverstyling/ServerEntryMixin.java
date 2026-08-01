package gg.norisk.client.v2.mixin.serverstyling;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.serverstyling.ServerStylingColor;
import gg.norisk.client.v2.serverstyling.ServerStylingRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnlineServerEntry.class)
public abstract class ServerEntryMixin {
   @Shadow
   @Final
   private ServerData serverData;

   @Inject(method = "extractContent", at = @At("HEAD"))
   private void nrc$renderServerStyling(GuiGraphicsExtractor ctx, int mouseX, int mouseY, boolean hovered, float delta, CallbackInfo ci) {
      OnlineServerEntry e = (OnlineServerEntry)this;
      ServerStylingRenderer.onRenderHead(ctx, this.serverData, e.getX() + 1, e.getY() + 1, e.getWidth() - 2, e.getHeight() - 2);
   }

   @WrapOperation(
      method = "extractContent",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V",
         ordinal = 0
      )
   )
   private void nrc$colorCodes(GuiGraphicsExtractor ctx, Font font, String text, int x, int y, int color, Operation<Void> original) {
      if (text.equals(this.serverData.name) && text.contains("&")) {
         Component colored = ServerStylingColor.translateColorCodes(Component.literal(text));
         if (colored != null) {
            ctx.text(font, colored, x, y, color);
            return;
         }
      }

      original.call(new Object[]{ctx, font, text, x, y, color});
   }

   @WrapOperation(
      method = "extractContent",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList$OnlineServerEntry;extractIcon(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/resources/Identifier;)V",
         remap = true
      )
   )
   private void nrc$skipIcon(OnlineServerEntry instance, GuiGraphicsExtractor ctx, int x, int y, Identifier id, Operation<Void> op) {
      if (!ServerStylingRenderer.shouldSkipDefaultIcon(this.serverData.ip)) {
         op.call(new Object[]{instance, ctx, x, y, id});
      }
   }
}
