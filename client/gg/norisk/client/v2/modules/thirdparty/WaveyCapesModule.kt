package gg.norisk.client.v2.modules.thirdparty

import gg.norisk.compat.client.MCLogger
import gg.norisk.cosmetics.v2.compat.waveycapes.WaveyCapesState
import gg.norisk.ui.api.module.ModuleCategory
import java.lang.reflect.Method
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nWaveyCapesModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaveyCapesModule.kt\ngg/norisk/client/v2/modules/thirdparty/WaveyCapesModule\n+ 2 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,59:1\n63#2:60\n63#2:66\n328#3:61\n40#3:62\n355#3:63\n40#3:64\n356#3:65\n*S KotlinDebug\n*F\n+ 1 WaveyCapesModule.kt\ngg/norisk/client/v2/modules/thirdparty/WaveyCapesModule\n*L\n27#1:60\n51#1:66\n40#1:61\n40#1:62\n46#1:63\n46#1:64\n46#1:65\n*E\n"])
public object WaveyCapesModule : ThirdPartyModule("Wavey Capes", ModuleCategory.VISUAL, true, "waveycapes", "tr7zw", "WaveyCapes", null, 64) {
   public open val seoTags: Array<String>

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   public open fun onLeftClick(): () -> Unit {
      return { 
         INSTANCE.toggle()
         Unit.INSTANCE
      }
   }

   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.openWaveyCapesConfigScreen()
         Unit.INSTANCE
      }
   }

   public override fun setupReflectionHooks() {
      WaveyCapesState.setRenderGate({ 
         INSTANCE.renderWaveyCape()
      })
      val `$this$nrcDebug$iv`: Logger = this.getLogger().getValue() as Logger
      val `message$iv`: java.lang.String = "[WaveyCapes] Module initialized, mod installed: ${this.isThirdPartyModInstalled()}"
      if (MCLogger.IS_DEBUG) {
         `$this$nrcDebug$iv`.info(`message$iv`)
      }
   }

   private fun openWaveyCapesConfigScreen() {
      if (this.isThirdPartyModInstalled()) {
         try {
            val e: Class = Class.forName("dev.tr7zw.waveycapes.WaveyCapesConfigScreen")
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
               if ((`$i$f$setScreen`[`$i$f$getInstance`] as Method).getName() == "createConfigScreen"
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
            val `message$iv`: java.lang.String = "[WaveyCapes] Failed to open config screen: ${var12.getMessage()}"
            if (MCLogger.IS_DEBUG) {
               `$this$nrcDebug$iv`.info(`message$iv`)
            }
         }
      }
   }

   public fun renderWaveyCape(): Boolean {
      return this.isThirdPartyModInstalled() && this.isEnabled()
   }
}
