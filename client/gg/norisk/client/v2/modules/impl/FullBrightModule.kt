package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.compat.event.KeyEventData
import gg.norisk.compat.event.KeyEvents
import gg.norisk.compat.event.MouseClickEventData
import gg.norisk.compat.event.MouseEvents
import gg.norisk.compat.resource.MCKey
import gg.norisk.compat.resource.MCKeyType
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft

@NrcMiniTag(tags = ["fullbright"])
@SourceDebugExtension(["SMAP\nFullBrightModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FullBrightModule.kt\ngg/norisk/client/v2/modules/impl/FullBrightModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,44:1\n328#2:45\n40#2:46\n328#2:47\n40#2:48\n*S KotlinDebug\n*F\n+ 1 FullBrightModule.kt\ngg/norisk/client/v2/modules/impl/FullBrightModule\n*L\n31#1:45\n31#1:46\n39#1:47\n39#1:48\n*E\n"])
public object FullBrightModule : Module("FullBright", ModuleCategory.VISUAL, false, true, false, 20) {
   public open val seoTags: Array<String>

   public final val toggleKey: MCKey by ValueApiKt.key$default(MCKey.Companion.getUNKNOWN(), null, null, 6, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return toggleKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MCKey
      }


   @JvmStatic
   fun {
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_0@{ event: KeyEventData ->
         val bound: MCKey = INSTANCE.toggleKey
         if (bound.isUnknown()) {
            return@lambda_0 Unit.INSTANCE
         } else if (bound.getType() != MCKeyType.KEYBOARD || bound.getCode() != event.getKey()) {
            return@lambda_0 Unit.INSTANCE
         } else if (!event.isClicked()) {
            return@lambda_0 Unit.INSTANCE
         } else {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() != null) {
               return@lambda_0 Unit.INSTANCE
            } else {
               INSTANCE.toggle()
               return@lambda_0 Unit.INSTANCE
            }
         }
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_1@{ event: MouseClickEventData ->
         val bound: MCKey = INSTANCE.toggleKey
         if (bound.isUnknown()) {
            return@lambda_1 Unit.INSTANCE
         } else if (bound.getType() != MCKeyType.MOUSE || bound.getCode() != event.getButton()) {
            return@lambda_1 Unit.INSTANCE
         } else if (event.getAction() != 1) {
            return@lambda_1 Unit.INSTANCE
         } else {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() != null) {
               return@lambda_1 Unit.INSTANCE
            } else {
               INSTANCE.toggle()
               return@lambda_1 Unit.INSTANCE
            }
         }
      })
   }
}
