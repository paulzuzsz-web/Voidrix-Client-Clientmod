package gg.voidrix.client.v2.modules.thirdparty

import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.loader.ReflectionHelper
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nThreeDSkinModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ThreeDSkinModule.kt\ngg/voidrix/client/v2/modules/thirdparty/ThreeDSkinModule\n+ 2 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,127:1\n63#2:128\n63#2:129\n63#2:132\n1#3:130\n40#4:131\n*S KotlinDebug\n*F\n+ 1 ThreeDSkinModule.kt\ngg/voidrix/client/v2/modules/thirdparty/ThreeDSkinModule\n*L\n79#1:128\n102#1:129\n123#1:132\n116#1:131\n*E\n"])
public object ThreeDSkinModule : ThirdPartyModule("3D Skin", ModuleCategory.VISUAL, false, "skinlayers3d", "tr7zw", "3D Skin Layers", null, 64) {
   private const val CONFIG_CLASS: String = "dev.tr7zw.skinlayers.versionless.ModBase"
   private const val INSTANCE_CLASS: String = "dev.tr7zw.skinlayers.SkinLayersModBase"
   public open val seoTags: Array<String>

   @Category(name = "PERFORMANCE")
   @NotNull
   public final var detailDistance: Number by ValueApiKt.numeric$default(
         14.0, RangesKt.rangeTo(5.0, 40.0) as ClosedRange, 1.0, null, null, { it: java.lang.Number ->
            INSTANCE.setConfig("renderDistanceLOD", it.intValue())
            INSTANCE.saveAndRefresh()
            Unit.INSTANCE
         }, 24, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return detailDistance$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }

      public final set(<set-?>) {
         detailDistance$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "PLAYER MODEL")
   @NotNull
   public final var showHat: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableHat", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return showHat$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showHat$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var showJacket: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableJacket", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      public final get() {
         return showJacket$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showJacket$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   public final var showLeftSleeve: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableLeftSleeve", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
      public final get() {
         return showLeftSleeve$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showLeftSleeve$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   public final var showRightSleeve: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableRightSleeve", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
      public final get() {
         return showRightSleeve$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showRightSleeve$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   public final var showLeftPants: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableLeftPants", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
      public final get() {
         return showLeftPants$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showLeftPants$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   public final var showRightPants: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableRightPants", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
      public final get() {
         return showRightPants$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showRightPants$delegate.setValue(this as ValueHolder, $$delegatedProperties[6], var1)
      }


   public final var voxelSize: Number by ValueApiKt.numeric$default(
         1.15, RangesKt.rangeTo(1.0, 1.4) as ClosedRange, 0.01, null, null, { it: java.lang.Number ->
            INSTANCE.setConfig("baseVoxelSize", it.floatValue())
            INSTANCE.saveAndRefresh()
            Unit.INSTANCE
         }, 24, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return voxelSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Number
      }

      public final set(<set-?>) {
         voxelSize$delegate.setValue(this as ValueHolder, $$delegatedProperties[7], var1)
      }


   public final var torsoVoxelWidth: Number by ValueApiKt.numeric$default(
         1.05, RangesKt.rangeTo(1.0, 1.4) as ClosedRange, 0.01, null, null, { it: java.lang.Number ->
            INSTANCE.setConfig("bodyVoxelWidthSize", it.floatValue())
            INSTANCE.saveAndRefresh()
            Unit.INSTANCE
         }, 24, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return torsoVoxelWidth$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Number
      }

      public final set(<set-?>) {
         torsoVoxelWidth$delegate.setValue(this as ValueHolder, $$delegatedProperties[8], var1)
      }


   public final var headVoxelSize: Number by ValueApiKt.numeric$default(
         1.18, RangesKt.rangeTo(1.0, 1.25) as ClosedRange, 0.01, null, null, { it: java.lang.Number ->
            INSTANCE.setConfig("headVoxelSize", it.floatValue())
            INSTANCE.saveAndRefresh()
            Unit.INSTANCE
         }, 24, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
         public final get() {
         return headVoxelSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.Number
      }

      public final set(<set-?>) {
         headVoxelSize$delegate.setValue(this as ValueHolder, $$delegatedProperties[9], var1)
      }


   @Category(name = "SKULL MODEL")
   @NotNull
   public final var skullBlocks: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableSkulls", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
      public final get() {
         return skullBlocks$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         skullBlocks$delegate.setValue(this as ValueHolder, $$delegatedProperties[10], var1)
      }


   public final var skullItems: Boolean by ValueApiKt.boolean$default(true, null, null, { it: Boolean ->
      INSTANCE.setConfig("enableSkullsItems", it)
      INSTANCE.saveAndRefresh()
      Unit.INSTANCE
   }, 6, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[11])
      public final get() {
         return skullItems$delegate.getValue(this as ValueHolder, $$delegatedProperties[11]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         skullItems$delegate.setValue(this as ValueHolder, $$delegatedProperties[11], var1)
      }


   public final var skullVoxelSize: Number by ValueApiKt.numeric$default(
         1.1, RangesKt.rangeTo(1.0, 1.2) as ClosedRange, 0.01, null, null, { it: java.lang.Number ->
            INSTANCE.setConfig("skullVoxelSize", it.floatValue())
            INSTANCE.saveAndRefresh()
            Unit.INSTANCE
         }, 24, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[12])
         public final get() {
         return skullVoxelSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[12]) as java.lang.Number
      }

      public final set(<set-?>) {
         skullVoxelSize$delegate.setValue(this as ValueHolder, $$delegatedProperties[12], var1)
      }


   public open fun createdAt(): Long {
      return 1744532285000L
   }

   public open fun applyBlurInModuleScreen(): Boolean {
      return false
   }

   public override fun setupReflectionHooks() {
      this.loadExternalValues()
   }

   public open fun resetToDefault() {
      super.resetToDefault()
      this.loadExternalValues()
   }

   public open fun updateProfile() {
      super.updateProfile()
      this.saveAndRefresh()
   }

   private fun loadExternalValues() {
      if (this.isThirdPartyModInstalled()) {
         this.setConfig("enableHat", this.showHat)
         this.setConfig("enableJacket", this.showJacket)
         this.setConfig("enableLeftSleeve", this.showLeftSleeve)
         this.setConfig("enableRightSleeve", this.showRightSleeve)
         this.setConfig("enableLeftPants", this.showLeftPants)
         this.setConfig("enableRightPants", this.showRightPants)
         this.setConfig("baseVoxelSize", this.voxelSize.floatValue())
         this.setConfig("bodyVoxelWidthSize", this.torsoVoxelWidth.floatValue())
         this.setConfig("headVoxelSize", this.headVoxelSize.floatValue())
         this.setConfig("enableSkulls", this.skullBlocks)
         this.setConfig("enableSkullsItems", this.skullItems)
         this.setConfig("skullVoxelSize", this.skullVoxelSize.floatValue())
         this.setConfig("renderDistanceLOD", this.detailDistance.intValue())
         val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
         if (MCLogger.IS_DEBUG) {
            `$this$voidrixDebug$iv`.info("[3DSkin] Loaded external values")
         }
      }
   }

   private fun getConfig(): Any? {
      val modern: Any = ReflectionHelper.getFieldValue$default(ReflectionHelper.INSTANCE, "dev.tr7zw.skinlayers.versionless.ModBase", "config", null, 4, null)
      return modern ?: ReflectionHelper.getFieldValue$default(ReflectionHelper.INSTANCE, "dev.tr7zw.skinlayers.SkinLayersModBase", "config", null, 4, null)
   }

   private fun getInstance(): Any? {
      return ReflectionHelper.getFieldValue$default(ReflectionHelper.INSTANCE, "dev.tr7zw.skinlayers.SkinLayersModBase", "instance", null, 4, null)
   }

   private fun setConfig(fieldName: String, value: Any?) {
      if (this.isThirdPartyModInstalled()) {
         val var10000: Any = this.getConfig()
         if (var10000 != null) {
            val config: Any = var10000

            try {
               val e: Field = config.getClass().getDeclaredField(fieldName)
               e.setAccessible(true)
               e.set(config, value)
            } catch (var8: Exception) {
               val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
               val `message$iv`: java.lang.String = "[3DSkin] Failed to set config.$fieldName: ${var8.getMessage()}"
               if (MCLogger.IS_DEBUG) {
                  `$this$voidrixDebug$iv`.info(`message$iv`)
               }
            }
         }
      }
   }

   private fun saveAndRefresh() {
      if (this.isThirdPartyModInstalled()) {
         try {
            var var10000: Any = this.getInstance()
            if (var10000 == null) {
               return
            }

            var10000 = var10000.getClass().getMethods()
            val var15: Array<Any> = var10000 as Array<Any>
            var var5: Int = 0
            val var6: Int = var15.length

            while (true) {
               if (var5 >= var6) {
                  var10000 = null
                  break
               }

               val var7: Any = var15[var5]
               if ((var15[var5] as Method).getName() == "writeConfig" && (var15[var5] as Method).getParameterCount() == 0) {
                  var10000 = var7
                  break
               }

               var5++
            }

            val var13: Method = var10000 as Method
            if (var10000 as Method != null) {
               var13.invoke(var10000)
            }

            val var25: Minecraft = Minecraft.getInstance()
            val var14: LocalPlayer = var25.player
            if (var25.player != null) {
               val var26: Array<Method> = var10000.getClass().getMethods()
               val var19: Array<Any> = var26
               var var20: Int = 0
               val var21: Int = var19.length

               while (true) {
                  if (var20 >= var21) {
                     var10000 = null
                     break
                  }

                  val var22: Any = var19[var20]
                  if ((var19[var20] as Method).getName() == "refreshLayers" && (var19[var20] as Method).getParameterCount() == 1) {
                     var10000 = var22
                     break
                  }

                  var20++
               }

               val var17: Method = var10000 as Method
               if (var10000 as Method != null) {
                  var17.invoke(var10000, var14)
               }
            }
         } catch (var12: Exception) {
            val `$this$voidrixDebug$iv`: Logger = this.getLogger().getValue() as Logger
            val `message$iv`: java.lang.String = "[3DSkin] Error saving/refreshing: ${var12.getMessage()}"
            if (MCLogger.IS_DEBUG) {
               `$this$voidrixDebug$iv`.info(`message$iv`)
            }
         }
      }
   }
}
