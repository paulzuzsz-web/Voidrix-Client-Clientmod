package gg.norisk.client.v2.modules.streamermode

import gg.norisk.compat.chat.ChatEvents
import gg.norisk.compat.chat.VisitFormattedData
import gg.norisk.compat.client.MCClient
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.skin.SkinOverrideData
import gg.norisk.compat.skin.SkinOverrideEventKt
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import java.util.concurrent.ConcurrentHashMap
import org.jetbrains.annotations.NotNull

public object StreamerMode : Module("Streamer Mode", ModuleCategory.QUALITY_OF_LIFE, false, false, false, 20) {
   @Category(name = "Name Options")
   @NotNull
   public final val nickname: String by ValueApiKt.text$default("???", false, null, null, null, 28, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return nickname$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.String
      }


   public final val hideOtherNames: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return hideOtherNames$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   @Category(name = "Skin Options")
   @NotNull
   public final val hideYourSkin: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return hideYourSkin$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public final val hideOtherSkins: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return hideOtherSkins$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   private final val randomNameCache: ConcurrentHashMap<String, String> = ConcurrentHashMap()

   public fun apply(text: String): String {
      var result: java.lang.String = text
      val sessionName: java.lang.String = MCClient.getSessionName()
      if (!StringsKt.isBlank(sessionName)) {
         result = StringsKt.replace(text, sessionName, this.nickname, true)
      }

      if (this.hideOtherNames) {
         for (playerName in MCClient.getOnlinePlayerNames()) {
            if (!StringsKt.isBlank(playerName)) {
               val var10000: Any = randomNameCache.computeIfAbsent(playerName, { p0: Any ->
                  `$tmp0`(p0) as java.lang.String
               })
               result = StringsKt.replace(result, playerName, var10000 as java.lang.String, true)
            }
         }
      }

      return result
   }

   @JvmStatic
   fun {
      ChatEvents.INSTANCE.getVisitFormattedEvent().listen(lambda_0@{ data: VisitFormattedData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            data.setString(INSTANCE.apply(data.getString()))
            return@lambda_0 Unit.INSTANCE
         }
      })
      SkinOverrideEventKt.getSkinOverrideEvent().listen(lambda_1@{ event: SkinOverrideData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            if (INSTANCE.hideYourSkin && event.isLocalPlayer()) {
               event.setUseDefaultSkin(true)
            }

            if (INSTANCE.hideOtherSkins && !event.isLocalPlayer()) {
               event.setUseDefaultSkin(true)
            }

            return@lambda_1 Unit.INSTANCE
         }
      })
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         randomNameCache.clear()
         Unit.INSTANCE
      })
   }
}
