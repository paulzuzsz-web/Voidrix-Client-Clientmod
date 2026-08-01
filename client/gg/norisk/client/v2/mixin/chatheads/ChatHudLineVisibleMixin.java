package gg.norisk.client.v2.mixin.chatheads;

import gg.norisk.client.v2.modules.chatheads.ChatHudLineVisibleExt;
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
   public void setNrc_messageSender(@Nullable UUID uuid) {
      this.messageSender = uuid;
   }

   @Nullable
   @Override
   public UUID getNrc_messageSender() {
      return this.messageSender;
   }

   @Override
   public void setNrc_skinTextures(@Nullable Supplier<PlayerSkin> skinTexturesSupplier) {
      this.getSkinTextures = skinTexturesSupplier;
   }

   @Nullable
   @Override
   public Supplier<PlayerSkin> getNrc_skinTextures() {
      return this.getSkinTextures;
   }

   @Override
   public void setNrc_tellReceiver(@Nullable String s) {
      this.tellReceiver = s;
   }

   @Nullable
   @Override
   public String getNrc_tellReceiver() {
      return this.tellReceiver;
   }
}
