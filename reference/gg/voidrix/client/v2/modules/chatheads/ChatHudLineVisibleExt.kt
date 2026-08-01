package gg.voidrix.client.v2.modules.chatheads

import java.util.UUID
import java.util.function.Supplier
import net.minecraft.world.entity.player.PlayerSkin

public interface ChatHudLineVisibleExt {
   public var voidrix_messageSender: UUID?
      internal final set

   public var voidrix_skinTextures: Supplier<PlayerSkin>?
      internal final set

   public var voidrix_tellReceiver: String?
      internal final set
}
