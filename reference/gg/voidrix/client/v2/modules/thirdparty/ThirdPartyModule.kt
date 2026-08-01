package gg.voidrix.client.v2.modules.thirdparty

import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.loader.ModLoadingHelper
import gg.voidrix.compat.loader.ReflectionHelper
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import kotlin.jvm.internal.SourceDebugExtension
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nThirdPartyModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ThirdPartyModule.kt\ngg/voidrix/client/v2/modules/thirdparty/ThirdPartyModule\n+ 2 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n*L\n1#1,63:1\n63#2:64\n63#2:65\n*S KotlinDebug\n*F\n+ 1 ThirdPartyModule.kt\ngg/voidrix/client/v2/modules/thirdparty/ThirdPartyModule\n*L\n57#1:64\n59#1:65\n*E\n"])
public abstract class ThirdPartyModule : Module {
   public final val modId: String
   private final val authorName: String
   private final val requiredMod: String
   private final val fabricListenerInterface: String?

   open fun ThirdPartyModule(
      name: java.lang.String,
      category: ModuleCategory,
      enabled: Boolean,
      modId: java.lang.String,
      authorName: java.lang.String,
      requiredMod: java.lang.String,
      fabricListenerInterface: java.lang.String?
   ) {
      super(name, category, true, enabled, false, 16, null)
      this.modId = modId
      this.authorName = authorName
      this.requiredMod = requiredMod
      this.fabricListenerInterface = fabricListenerInterface
   }

   public open fun getOriginalModAuthor(): String {
      return this.authorName
   }

   public open fun isThirdPartyModInstalled(): Boolean {
      return ModLoadingHelper.INSTANCE.isModLoaded(this.modId)
   }

   public open fun getRequiredModName(): String {
      return this.requiredMod
   }

   public abstract fun setupReflectionHooks() {
   }

   public fun initHooks() {
      if (this.isThirdPartyModInstalled()) {
         this.setupReflectionHooks()
      }
   }

   protected fun registerModEvent(eventClassName: String, label: String, handler: (Any) -> Unit) {
      if (this.fabricListenerInterface == null) {
         throw IllegalStateException("fabricListenerInterface must be set for Fabric".toString())
      } else {
         if (ReflectionHelper.registerFabricEvent$default(ReflectionHelper.INSTANCE, eventClassName, this.fabricListenerInterface, null, handler, 4, null)) {
            val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
            val `message$iv`: java.lang.String = "[${this.getName()}] Registered $label event handler"
            if (MCLogger.IS_DEBUG) {
               `$this$voidrixDebug$iv`.info(`message$iv`)
            }
         } else {
            val var8: Logger = this.getLogger().getValue() as Logger
            val var9: java.lang.String = "[${this.getName()}] FAILED to register $label event handler"
            if (MCLogger.IS_DEBUG) {
               var8.info(var9)
            }
         }
      }
   }
}
