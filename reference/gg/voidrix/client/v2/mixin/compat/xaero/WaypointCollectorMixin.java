package gg.voidrix.client.v2.mixin.compat.xaero;

import gg.voidrix.client.v2.waypoints.xaero.VoidrixToXaeroBridge;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.waypoint.WaypointCollector;

@Mixin(value = WaypointCollector.class, remap = false)
public abstract class WaypointCollectorMixin {
   @Inject(method = "collect", at = @At("RETURN"), remap = false, require = 0)
   private void voidrix$appendVoidrixWaypoints(List<Waypoint> waypoints, CallbackInfo ci) {
      VoidrixToXaeroBridge.appendMinimapWaypoints(waypoints);
   }
}
