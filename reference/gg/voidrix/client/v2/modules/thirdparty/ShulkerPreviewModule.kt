package gg.voidrix.client.v2.modules.thirdparty

import com.mojang.blaze3d.platform.InputConstants.Key
import com.mojang.blaze3d.platform.InputConstants.Type
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.loader.ReflectionHelper
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.lang.reflect.Field
import kotlin.jvm.internal.SourceDebugExtension
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nShulkerPreviewModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ShulkerPreviewModule.kt\ngg/voidrix/client/v2/modules/thirdparty/ShulkerPreviewModule\n+ 2 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n*L\n1#1,140:1\n63#2:141\n63#2:142\n63#2:143\n63#2:144\n63#2:145\n*S KotlinDebug\n*F\n+ 1 ShulkerPreviewModule.kt\ngg/voidrix/client/v2/modules/thirdparty/ShulkerPreviewModule\n*L\n66#1:141\n92#1:142\n102#1:143\n105#1:144\n131#1:145\n*E\n"])
public object ShulkerPreviewModule : ThirdPartyModule(
      "Shulker Preview", ModuleCategory.VISUAL, true, "shulkerboxtooltip", "MisterPeModder", "ShulkerBoxTooltip", null, 64
   ) {
   private const val SHULKER_CLASS: String = "com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip"
   private const val CONFIG_HANDLER_CLASS: String = "com.misterpemodder.shulkerboxtooltip.impl.config.ConfigurationHandler"
   private const val KEY_CLASS: String = "com.misterpemodder.shulkerboxtooltip.impl.util.Key"
   public open val seoTags: Array<String>

   @Category(name = "SETTINGS")
   @NotNull
   public final val alwaysPreview: Boolean by ValueApiKt.boolean$default(false, null, null, { it: Boolean ->
      INSTANCE.setConfigField("preview", "alwaysOn", it)
      INSTANCE.saveConfig()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return alwaysPreview$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val previewKey: MCKey by ValueApiKt.key$default(340, null, { it: MCKey ->
      INSTANCE.setPreviewKey(it)
      INSTANCE.saveConfig()
      Unit.INSTANCE
   }, 2, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return previewKey$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MCKey
      }


   public open fun createdAt(): Long {
      return 1744532285000L
   }

   public override fun setupReflectionHooks() {
      this.loadExternalValues()
   }

   public open fun enableChangeCallback(enabled: Boolean) {
      super.enableChangeCallback(enabled)
      if (this.isThirdPartyModInstalled()) {
         this.setConfigField("preview", "enable", enabled)
         this.saveConfig()
      }
   }

   public open fun resetToDefault() {
      super.resetToDefault()
      this.loadExternalValues()
      this.saveConfig()
   }

   public open fun updateProfile() {
      super.updateProfile()
      this.saveConfig()
   }

   private fun loadExternalValues() {
      if (this.isThirdPartyModInstalled()) {
         this.setConfigField("preview", "enable", this.isEnabled())
         this.setConfigField("preview", "alwaysOn", this.alwaysPreview)
         this.setPreviewKey(this.previewKey)
         val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
         val `message$iv`: java.lang.String = "[ShulkerPreview] Loaded external values: enable=${this.isEnabled()}, alwaysOn=${this.alwaysPreview}"
         if (MCLogger.IS_DEBUG) {
            `$this$voidrixDebug$iv`.info(`message$iv`)
         }
      }
   }

   private fun getSavedConfig(): Any? {
      return ReflectionHelper.getFieldValue$default(
         ReflectionHelper.INSTANCE, "com.misterpemodder.shulkerboxtooltip.ShulkerBoxTooltip", "savedConfig", null, 4, null
      )
   }

   private fun setPreviewKey(mcKey: MCKey) {
      var var10000: Any = this.getSavedConfig()
      if (var10000 != null) {
         val config: Any = var10000

         try {
            var10000 = this.getCategory(config, "controls")
            if (var10000 == null) {
               return
            }

            val var14: Field = var10000.getClass().getDeclaredField("previewKey")
            var14.setAccessible(true)
            val var15: Any = var14.get(var10000)
            if (var15 != null) {
               var15.getClass().getMethod("set", Key.class).invoke(var15, Type.KEYSYM.getOrCreate(mcKey.getCode()))
               val var20: Logger = this.getLogger().getValue() as Logger
               val newKey: java.lang.String = "[ShulkerPreview] Set previewKey to ${mcKey.getCode()}"
               if (MCLogger.IS_DEBUG) {
                  var20.info(newKey)
               }
            } else {
               var14.set(
                  var10000,
                  Class.forName("com.misterpemodder.shulkerboxtooltip.impl.util.Key")
                     .getConstructor(Key.class)
                     .newInstance(Type.KEYSYM.getOrCreate(mcKey.getCode()))
               )
               val var25: Logger = this.getLogger().getValue() as Logger
               val `message$iv`: java.lang.String = "[ShulkerPreview] Created new previewKey for ${mcKey.getCode()}"
               if (MCLogger.IS_DEBUG) {
                  var25.info(`message$iv`)
               }
            }
         } catch (var13: Exception) {
            val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
            val `message$ivx`: java.lang.String = "[ShulkerPreview] Failed to set previewKey: ${var13.getClass().getSimpleName()}: ${var13.getMessage()}"
            if (MCLogger.IS_DEBUG) {
               `$this$voidrixDebug$iv`.info(`message$ivx`)
            }
         }
      }
   }

   private fun getCategory(config: Any, categoryName: String): Any? {
      // $VF: Unable to resugar Kotlin loop from Java for loop
      var clazz: Class = config.getClass()
      while (true) {
         if (clazz != null) break
         try {
            val var4: Field = clazz.getDeclaredField(categoryName)
            var4.setAccessible(true)
            return var4.get(config)
         } catch (var5: NoSuchFieldException) {
         }

         clazz = clazz.getSuperclass()
      }

      return null
   }

   private fun setConfigField(categoryName: String, fieldName: String, value: Any?) {
      var var10000: Any = this.getSavedConfig()
      if (var10000 != null) {
         val config: Any = var10000

         try {
            var10000 = this.getCategory(config, categoryName)
            if (var10000 == null) {
               return
            }

            val var10: Field = var10000.getClass().getDeclaredField(fieldName)
            var10.setAccessible(true)
            var10.set(var10000, value)
         } catch (var9: Exception) {
            val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
            val `message$iv`: java.lang.String = "[ShulkerPreview] Failed to set $categoryName.$fieldName: ${var9.getClass().getSimpleName()}: ${var9.getMessage()}"
            if (MCLogger.IS_DEBUG) {
               `$this$voidrixDebug$iv`.info(`message$iv`)
            }
         }
      }
   }

   private fun saveConfig() {
      val var10000: Any = this.getSavedConfig()
      if (var10000 != null) {
         ReflectionHelper.INSTANCE.invokeMethod("com.misterpemodder.shulkerboxtooltip.impl.config.ConfigurationHandler", "saveToFile", null, arrayOf(var10000))
      }
   }
}
