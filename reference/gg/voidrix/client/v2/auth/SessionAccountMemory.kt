package gg.voidrix.client.v2.auth

import gg.voidrix.compat.auth.CurrentSessionInfo
import gg.voidrix.compat.auth.SessionHelper
import gg.voidrix.compat.auth.launcher.Credentials
import java.util.LinkedHashMap

public object SessionAccountMemory {
   private const val SYNTHETIC_EXPIRES: String = "9999-01-01T00:00:00Z"
   private final val seen: MutableMap<String, Credentials> = LinkedHashMap() as java.util.Map

   public fun rememberCurrent() {
      var id: CurrentSessionInfo
      try {
         id = SessionHelper.INSTANCE.getCurrentSessionInfo()
      } catch (var4: Exception) {
         return
      }

      val var10000: java.lang.String = id.getProfileId().toString()
      val existing: Credentials = seen.get(var10000)
      seen.put(
         var10000,
         if (existing == null)
            Credentials(var10000, id.getUsername(), id.getAccessToken(), "", "9999-01-01T00:00:00Z", null, true, false, null, 416, null)
            else
            Credentials.copy$default(existing, null, id.getUsername(), id.getAccessToken(), null, null, null, false, false, null, 505, null)
      )
   }

   public fun all(): Map<String, Credentials> {
      return MapsKt.toMap(seen)
   }
}
