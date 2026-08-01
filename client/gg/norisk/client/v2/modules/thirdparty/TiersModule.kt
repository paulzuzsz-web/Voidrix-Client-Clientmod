package gg.norisk.client.v2.modules.thirdparty

import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.loader.ReflectionHelper
import gg.norisk.ui.api.module.ModuleCategory
import java.lang.reflect.Method
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nTiersModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TiersModule.kt\ngg/norisk/client/v2/modules/thirdparty/TiersModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n*L\n1#1,79:1\n328#2:80\n40#2:81\n355#2:82\n40#2:83\n356#2:84\n63#3:85\n63#3:86\n*S KotlinDebug\n*F\n+ 1 TiersModule.kt\ngg/norisk/client/v2/modules/thirdparty/TiersModule\n*L\n54#1:80\n54#1:81\n60#1:82\n60#1:83\n60#1:84\n65#1:85\n72#1:86\n*E\n"])
public object TiersModule : ThirdPartyModule("Tiers", ModuleCategory.PVP, true, "tiers", "Flavio6561", "Tiers", null, 64) {
   private const val TIERS_CLIENT_CLASS: String = "com.tiers.TiersClient"
   private const val CONFIG_MANAGER_CLASS: String = "com.tiers.ConfigManager"
   private const val CONFIG_SCREEN_CLASS: String = "com.tiers.screens.ConfigScreen"
   public open val seoTags: Array<String>

   public open fun createdAt(): Long {
      return 1754306341496L
   }

   public open fun onLeftClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }

   public open fun onRightClick(): (() -> Unit)? {
      return if (!this.isThirdPartyModInstalled()) null else { 
         INSTANCE.openTiersConfigScreen()
         Unit.INSTANCE
      }
   }

   public override fun setupReflectionHooks() {
      this.loadExternalValues()
   }

   public open fun enableChangeCallback(enabled: Boolean) {
      super.enableChangeCallback(enabled)
      if (this.isThirdPartyModInstalled()) {
         ReflectionHelper.setFieldValue$default(ReflectionHelper.INSTANCE, "com.tiers.TiersClient", "toggleMod", enabled, null, 8, null)
         this.saveConfig()
      }
   }

   public open fun resetToDefault() {
      if (this.isThirdPartyModInstalled()) {
         ReflectionHelper.setFieldValue$default(ReflectionHelper.INSTANCE, "com.tiers.TiersClient", "toggleMod", true, null, 8, null)
         this.saveConfig()
         this.setEnabled(true)
      }

      super.resetToDefault()
   }

   private fun openTiersConfigScreen() {
      try {
         val e: Class = Class.forName("com.tiers.screens.ConfigScreen")
         var var10000: Method = Minecraft.getInstance()
         val var13: Screen = var10000.gui.screen()
         var10000 = e.getMethods()
         val `$i$f$setScreen`: Array<Any> = var10000 as Array<Any>
         var `$i$f$getInstance`: Int = 0
         val var8: Int = `$i$f$setScreen`.length

         while (true) {
            if (`$i$f$getInstance` >= var8) {
               var10000 = null
               break
            }

            val var9: Any = `$i$f$setScreen`[`$i$f$getInstance`]
            if ((`$i$f$setScreen`[`$i$f$getInstance`] as Method).getName() == "getConfigScreen"
               && (`$i$f$setScreen`[`$i$f$getInstance`] as Method).getParameterCount() == 1) {
               var10000 = (Method)var9
               break
            }

            `$i$f$getInstance`++
         }

         var10000 = var10000
         if (var10000 == null) {
            return
         }

         var10000 = (Method)var10000.invoke(null, var13)
         if (var10000 == null) {
            return
         }

         val `screen$iv`: Screen = var10000 as? Screen
         val var25: Minecraft = Minecraft.getInstance()
         var25.gui.setScreen(`screen$iv`)
      } catch (var12: Exception) {
         val `$this$nrcDebug$iv`: Logger = this.getLogger().getValue() as Logger
         val `message$iv`: java.lang.String = "[Tiers] Failed to open config screen: ${var12.getMessage()}"
         if (MCLogger.IS_DEBUG) {
            `$this$nrcDebug$iv`.info(`message$iv`)
         }
      }
   }

   private fun loadExternalValues() {
      if (this.isThirdPartyModInstalled()) {
         ReflectionHelper.setFieldValue$default(ReflectionHelper.INSTANCE, "com.tiers.TiersClient", "toggleMod", this.isEnabled(), null, 8, null)
         val `$this$nrcDebug$iv`: Logger = this.getLogger().getValue() as Logger
         val `message$iv`: java.lang.String = "[Tiers] Loaded external values: toggleMod=${this.isEnabled()}"
         if (MCLogger.IS_DEBUG) {
            `$this$nrcDebug$iv`.info(`message$iv`)
         }
      }
   }

   private fun saveConfig() {
      ReflectionHelper.invokeMethod$default(ReflectionHelper.INSTANCE, "com.tiers.ConfigManager", "saveConfig", null, arrayOfNulls(0), 4, null)
   }
}
