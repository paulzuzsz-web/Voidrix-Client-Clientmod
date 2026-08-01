package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.loader.ModLoadingHelper
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.utils.ServerLockedModuleManager
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import org.jetbrains.annotations.Nullable
import org.lwjgl.glfw.GLFW

@SourceDebugExtension(["SMAP\nBorderlessFullscreenModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 BorderlessFullscreenModule.kt\ngg/norisk/client/v2/modules/impl/BorderlessFullscreenModule\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,282:1\n295#2,2:283\n*S KotlinDebug\n*F\n+ 1 BorderlessFullscreenModule.kt\ngg/norisk/client/v2/modules/impl/BorderlessFullscreenModule\n*L\n51#1:283,2\n*E\n"])
public object BorderlessFullscreenModule : Module("Borderless Fullscreen", ModuleCategory.VISUAL, false, true, false, 20) {
   public open val seoTags: Array<String>
   private final val competingMods: List<Pair<String, String>> =
      CollectionsKt.listOf(
         arrayOf(
            TuplesKt.to("cwb", "Cubes Without Borders"),
            TuplesKt.to("fullscreenfix", "Better-Fullscreen"),
            TuplesKt.to("borderless-mining", "Borderless Mining"),
            TuplesKt.to("fullscreenwindowed", "Fullscreen Windowed Mode"),
            TuplesKt.to("sk1er_fullscreen", "Windowed Fullscreen")
         )
      )

   @JvmField
   @Nullable
   public final val competingModName: String?

   @JvmField
   public final val isCompetingModLoaded: Boolean

   @JvmField
   public final var isBorderless: Boolean
      private set

   @JvmField
   public final var savedX: Int
      private set

   @JvmField
   public final var savedY: Int
      private set

   @JvmField
   public final var savedW: Int
      private set

   @JvmField
   public final var savedH: Int
      private set

   @JvmField
   public final var savedWasMaximized: Boolean
      private set

   public open fun onLeftClick(): () -> Unit {
      return { 
         INSTANCE.tryToggle()
         Unit.INSTANCE
      }
   }

   public open fun onRightClick(): () -> Unit {
      return { 
         INSTANCE.tryToggle()
         Unit.INSTANCE
      }
   }

   private fun tryToggle() {
      val conflict: java.lang.String = competingModName
      if (competingModName != null) {
         ServerLockedModuleManager.INSTANCE.showModuleConflictToast(this.getName(), conflict)
      } else {
         this.toggle()
      }
   }

   public open fun getConflictingModName(): String? {
      return competingModName
   }

   public open fun enableChangeCallback(enabled: Boolean) {
      if (!isCompetingModLoaded) {
         super.enableChangeCallback(enabled)
      }
   }

   public open fun onDisable() {
      if (isBorderless) {
         restoreWindowed()
      }
   }

   @JvmStatic
   public fun restoreWindowed() {
      if (!isCompetingModLoaded) {
         val handle: Long = Minecraft.getInstance().getWindow().handle
         GLFW.glfwSetWindowAttrib(handle, 131077, 1)
         GLFW.glfwSetWindowAttrib(handle, 131078, 1)
         GLFW.glfwRestoreWindow(handle)
         if (savedW > 0 && savedH > 0) {
            GLFW.glfwSetWindowPos(handle, savedX, savedY)
            GLFW.glfwSetWindowSize(handle, savedW, savedH)
         }

         if (savedWasMaximized) {
            GLFW.glfwMaximizeWindow(handle)
            savedWasMaximized = false
         }

         isBorderless = false
      }
   }

   @JvmStatic
   fun {
      val var3: java.util.Iterator = competingMods.iterator()

      var var10000: Any
      while (true) {
         if (var3.hasNext()) {
            val `element$iv`: Any = var3.next()
            val id: java.lang.String = (`element$iv` as Pair).component1() as java.lang.String

            var var8: Boolean
            try {
               var8 = ModLoadingHelper.INSTANCE.isModLoaded(id)
            } catch (var10: java.lang.Throwable) {
               var8 = false
            }

            if (!var8) {
               continue
            }

            var10000 = `element$iv`
            break
         }

         var10000 = null
         break
      }

      competingModName = if (var10000 as Pair != null) (var10000 as Pair).getSecond() as java.lang.String else null
      isCompetingModLoaded = competingModName != null
   }
}
