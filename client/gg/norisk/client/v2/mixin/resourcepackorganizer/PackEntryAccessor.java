package gg.norisk.client.v2.mixin.resourcepackorganizer;

import net.minecraft.client.gui.screens.packs.PackSelectionModel.Entry;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList.PackEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PackEntry.class)
public interface PackEntryAccessor {
   @Accessor("pack")
   Entry norisk$getPack();
}
