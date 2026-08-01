package gg.voidrix.client.v2.mixin.chatheads;

import gg.voidrix.client.v2.modules.chatheads.ChatHudLineVisibleExt;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.world.entity.player.PlayerSkin;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Line.class)
public abstract class ChatHudLineVisibleMixin implements ChatHudLineVisibleExt {
   @Unique
   private UUID messageSender;
   @Unique
   private Supplier<PlayerSkin> getSkinTextures;
   @Unique
   private String tellReceiver;

   @Override
   public void setVoidrix_messageSender(@Nullable UUID uuid) {
      this.messageSender = uuid;
   }

   @Nullable
   @Override
   public UUID getVoidrix_messageSender() {
      return this.messageSender;
   }

   @Override
   public void setVoidrix_skinTextures(@Nullable Supplier<PlayerSkin> skinTexturesSupplier) {
      this.getSkinTextures = skinTexturesSupplier;
   }

   @Nullable
   @Override
   public Supplier<PlayerSkin> getVoidrix_skinTextures() {
      return this.getSkinTextures;
   }

   @Override
   public void setVoidrix_tellReceiver(@Nullable String s) {
      this.tellReceiver = s;
   }

   @Nullable
   @Override
   public String getVoidrix_tellReceiver() {
      return this.tellReceiver;
   }
}
