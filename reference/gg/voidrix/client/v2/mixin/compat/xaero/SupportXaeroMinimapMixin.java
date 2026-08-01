package gg.voidrix.client.v2.mixin.compat.xaero;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.voidrix.client.v2.waypoints.xaero.VoidrixToXaeroBridge;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.gui.GuiMap;
import xaero.map.mods.SupportXaeroMinimap;
import xaero.map.mods.gui.Waypoint;

@Mixin(value = SupportXaeroMinimap.class, remap = false)
public abstract class SupportXaeroMinimapMixin {
   @ModifyReturnValue(method = "convertWaypoints", at = @At("RETURN"), remap = false, require = 0)
   private ArrayList<Waypoint> voidrix$appendVoidrixWmWaypoints(ArrayList<Waypoint> original) {
      VoidrixToXaeroBridge.appendWorldmapWaypoints(original, this);
      return original;
   }

   @Inject(method = "openWaypoint", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private void voidrix$interceptOpenWaypoint(GuiMap gui, Waypoint waypoint, CallbackInfo ci) {
      if (VoidrixToXaeroBridge.handleOpenWaypoint(waypoint)) {
         ci.cancel();
      }
   }

   @Inject(method = "deleteWaypoint", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private void voidrix$interceptDeleteWaypoint(Waypoint waypoint, CallbackInfo ci) {
      if (VoidrixToXaeroBridge.handleDeleteWaypoint(waypoint)) {
         ci.cancel();
      }
   }

   @Inject(method = "disableWaypoint", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private void voidrix$interceptDisableWaypoint(Waypoint waypoint, CallbackInfo ci) {
      if (VoidrixToXaeroBridge.handleDisableWaypoint(waypoint)) {
         ci.cancel();
      }
   }
}
