package gg.norisk.client.v2.mixin.compat.xaero;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.norisk.client.v2.waypoints.xaero.NrcToXaeroBridge;
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
   private ArrayList<Waypoint> nrc$appendNrcWmWaypoints(ArrayList<Waypoint> original) {
      NrcToXaeroBridge.appendWorldmapWaypoints(original, this);
      return original;
   }

   @Inject(method = "openWaypoint", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private void nrc$interceptOpenWaypoint(GuiMap gui, Waypoint waypoint, CallbackInfo ci) {
      if (NrcToXaeroBridge.handleOpenWaypoint(waypoint)) {
         ci.cancel();
      }
   }

   @Inject(method = "deleteWaypoint", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private void nrc$interceptDeleteWaypoint(Waypoint waypoint, CallbackInfo ci) {
      if (NrcToXaeroBridge.handleDeleteWaypoint(waypoint)) {
         ci.cancel();
      }
   }

   @Inject(method = "disableWaypoint", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private void nrc$interceptDisableWaypoint(Waypoint waypoint, CallbackInfo ci) {
      if (NrcToXaeroBridge.handleDisableWaypoint(waypoint)) {
         ci.cancel();
      }
   }
}
