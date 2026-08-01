package gg.norisk.client.v2.mixin.resourcepackorganizer;

import java.util.List;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSelectionList.class)
public interface SelectionListAccessor {
   @Accessor("children")
   List norisk$getChildren();

   @Invoker("getRowLeft")
   int norisk$getRowLeft();

   @Invoker("getFirstEntryY")
   int norisk$getFirstEntryY();
}
