package gg.voidrix.client.v2.modules.itemmodel

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import gg.voidrix.client.v2.modules.itemmodel.ItemTransformation.Transformation
import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.compat.render.ItemModelTransformHelper
import gg.voidrix.compat.render.VoidrixItemDisplayContext
import gg.voidrix.compat.serialization.IdentifierSerializer
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.MapValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.modules.IModuleScreen
import java.util.LinkedHashMap
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.joml.Quaternionfc

@SourceDebugExtension(["SMAP\nItemModel.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemModel.kt\ngg/voidrix/client/v2/modules/itemmodel/ItemModel\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,457:1\n384#2,7:458\n1#3:465\n328#4:466\n40#4:467\n*S KotlinDebug\n*F\n+ 1 ItemModel.kt\ngg/voidrix/client/v2/modules/itemmodel/ItemModel\n*L\n65#1:458,7\n441#1:466\n441#1:467\n*E\n"])
public object ItemModel : Module("Item Model", ModuleCategory.QUALITY_OF_LIFE, false, true, false, 20) {
   public open val seoTags: Array<String>

   public final var transformations: MutableMap<Identifier, ItemTransformation>
      public final get() {
         return transformations$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MutableMap<Identifier, ItemTransformation>
      }

      public final set(<set-?>) {
         transformations$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public final var modelPreview: Boolean = true
      internal set

   public final var settingsOpen: Boolean
      internal set

   public final var currentMode: VoidrixItemDisplayContext = VoidrixItemDisplayContext.FIRST_PERSON_RIGHT_HAND
      internal set

   public final var advancedExpanded: Boolean
      internal set

   public final var currentItemId: Identifier?
      internal set

   public final var clipboardTransformation: ItemTransformation?
      internal set

   public fun getTransformation(itemId: Identifier): ItemTransformation {
      val `$this$getOrPut$iv`: java.util.Map = this.transformations
      val `value$iv`: Any = `$this$getOrPut$iv`.get(itemId)
      val var10000: Any
      if (`value$iv` == null) {
         val var6: Any = ItemTransformation(null, 1, null)
         `$this$getOrPut$iv`.put(itemId, var6)
         var10000 = var6
      } else {
         var10000 = `value$iv`
      }

      return var10000 as ItemTransformation
   }

   public fun saveTransformations() {
      this.transformations = MapsKt.toMutableMap(this.transformations)
   }

   private fun resolveTransform(transformation: ItemTransformation, context: VoidrixItemDisplayContext): Transformation? {
      val var10000: ItemTransformation.Transformation = transformation.transformations.get(context)
      if (var10000 != null) {
         return var10000
      } else {
         var var6: VoidrixItemDisplayContext
         when (ItemModel.WhenMappings.$EnumSwitchMapping$0[context.ordinal()]) {
            1 -> var6 = VoidrixItemDisplayContext.FIRST_PERSON_RIGHT_HAND
            2 -> var6 = VoidrixItemDisplayContext.THIRD_PERSON_RIGHT_HAND
            else -> return null
         }

         return transformation.transformations.get(var6)
      }
   }

   @JvmStatic
   public fun transform(poseStack: PoseStack, displayContext: ItemDisplayContext, itemStack: ItemStack, leftHanded: Boolean) {
      if (INSTANCE.isEnabled()) {
         val var10000: VoidrixItemDisplayContext = VoidrixItemDisplayContext.Companion.fromMc(displayContext)
         if (var10000 != null) {
            INSTANCE.applyTransformInternal(poseStack, var10000, itemStack, leftHanded)
         }
      }
   }

   private fun applyTransformInternal(poseStack: PoseStack, context: VoidrixItemDisplayContext, itemStack: ItemStack, leftHanded: Boolean) {
      val t: ItemModel = this

      var `$this$applyTransformInternal_u24lambda_u244`: ItemModel
      try {
         `$this$applyTransformInternal_u24lambda_u244` = t
         `$this$applyTransformInternal_u24lambda_u244` = (ItemModel)Result.constructor_impl/* $VF was: constructor-impl */(
            MCRegistryKt.itemRegistryId(itemStack)
         )
      } catch (var10: java.lang.Throwable) {
         `$this$applyTransformInternal_u24lambda_u244` = (ItemModel)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var10))
      }

      val var10000: Identifier = (
         if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$applyTransformInternal_u24lambda_u244`))
            null
            else
            `$this$applyTransformInternal_u24lambda_u244`
      ) as Identifier
      if (var10000 != null) {
         val var14: ItemTransformation = this.transformations.get(var10000)
         if (var14 != null) {
            val var15: ItemTransformation.Transformation = this.resolveTransform(var14, context)
            if (var15 != null) {
               if (!var15.isDefault()) {
                  ItemModelTransformHelper.applyTransform(poseStack, var15.translation, var15.rotation, var15.scale, leftHanded, context)
               }
            }
         }
      }
   }

   @JvmStatic
   public fun transformRenderState(poseStack: PoseStack, displayContext: ItemDisplayContext, itemStack: ItemStack) {
      if (INSTANCE.isEnabled()) {
         val var10000: VoidrixItemDisplayContext = VoidrixItemDisplayContext.Companion.fromMc(displayContext)
         if (var10000 != null) {
            val t: ItemModel = INSTANCE

            var `$this$transformRenderState_u24lambda_u245`: Any
            try {
               `$this$transformRenderState_u24lambda_u245` = Result.constructor_impl/* $VF was: constructor-impl */(MCRegistryKt.itemRegistryId(itemStack))
            } catch (var9: java.lang.Throwable) {
               `$this$transformRenderState_u24lambda_u245` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var9))
            }

            val var12: Identifier = (
               if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$transformRenderState_u24lambda_u245`))
                  null
                  else
                  `$this$transformRenderState_u24lambda_u245`
            ) as Identifier
            if (var12 != null) {
               val var13: ItemTransformation = INSTANCE.transformations.get(var12)
               if (var13 != null) {
                  val var14: ItemTransformation.Transformation = INSTANCE.resolveTransform(var13, var10000)
                  if (var14 != null) {
                     if (!var14.isDefault()) {
                        ItemModelTransformHelper.applyTransform(poseStack, var14.translation, var14.rotation, var14.scale, displayContext.leftHand(), var10000)
                     }
                  }
               }
            }
         }
      }
   }

   @JvmStatic
   public fun hasNonDefaultGuiTransform(itemStack: ItemStack): Boolean {
      if (!INSTANCE.isEnabled()) {
         return false
      } else {
         val t: ItemModel = INSTANCE

         var `$this$hasNonDefaultGuiTransform_u24lambda_u246`: Any
         try {
            `$this$hasNonDefaultGuiTransform_u24lambda_u246` = Result.constructor_impl/* $VF was: constructor-impl */(MCRegistryKt.itemRegistryId(itemStack))
         } catch (var6: java.lang.Throwable) {
            `$this$hasNonDefaultGuiTransform_u24lambda_u246` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var6))
         }

         val var10000: Identifier = (
            if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$hasNonDefaultGuiTransform_u24lambda_u246`))
               null
               else
               `$this$hasNonDefaultGuiTransform_u24lambda_u246`
         ) as Identifier
         if (var10000 == null) {
            return false
         } else {
            val var9: ItemTransformation = INSTANCE.transformations.get(var10000)
            label49@
            if (var9 == null) {
               return false
            } else {
               val var10: ItemTransformation.Transformation = var9.transformations.get(VoidrixItemDisplayContext.GUI)
               return var10 != null && !var10.isDefault()
            }
         }
      }
   }

   @JvmStatic
   public fun getGuiTransformFactors(itemStack: ItemStack): FloatArray? {
      if (!INSTANCE.isEnabled()) {
         return null
      } else {
         val t: ItemModel = INSTANCE

         var `$this$getGuiTransformFactors_u24lambda_u247`: Any
         try {
            `$this$getGuiTransformFactors_u24lambda_u247` = Result.constructor_impl/* $VF was: constructor-impl */(MCRegistryKt.itemRegistryId(itemStack))
         } catch (var6: java.lang.Throwable) {
            `$this$getGuiTransformFactors_u24lambda_u247` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var6))
         }

         val var10000: Identifier = (
            if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$getGuiTransformFactors_u24lambda_u247`))
               null
               else
               `$this$getGuiTransformFactors_u24lambda_u247`
         ) as Identifier
         if (var10000 == null) {
            return null
         } else {
            val var9: ItemTransformation = INSTANCE.transformations.get(var10000)
            if (var9 == null) {
               return null
            } else {
               val var10: ItemTransformation.Transformation = var9.transformations.get(VoidrixItemDisplayContext.GUI)
               if (var10 == null) {
                  return null
               } else {
                  return if (var10.isDefault())
                     null
                     else
                     floatArrayOf(
                        var10.scale.getX(),
                        var10.scale.getY(),
                        var10.scale.getZ(),
                        var10.translation.getX(),
                        var10.translation.getY(),
                        var10.translation.getZ()
                     )
                  }
            }
         }
      }
   }

   @JvmStatic
   public fun renderGroundPreviewItems(poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector, cameraX: Double, cameraY: Double, cameraZ: Double) {
      val var10000: ItemStack = getPreviewItemStack()
      if (var10000 != null) {
         val previewStack: ItemStack = var10000
         val mc: Minecraft = Minecraft.getInstance()
         if (mc.player != null) {
            val player: LocalPlayer = mc.player
            val resolver: ItemModelResolver = mc.getItemModelResolver()
            val playerBlockX: Int = Mth.floor(player.getX())
            val playerBlockY: Int = Mth.floor(player.getY())
            val playerBlockZ: Int = Mth.floor(player.getZ())
            val renderState: java.util.List = CollectionsKt.createListBuilder()
            val ageInTicks: java.util.List = renderState

            for (offset in -2..2) {
               for (worldX in -2..2) {
                  ageInTicks.add(intArrayOf(offset, worldX))
               }
            }

            val offsets: java.util.List = CollectionsKt.build(renderState)
            val var30: ItemStackRenderState = ItemStackRenderState()
            val var31: Float = player.tickCount + mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)

            for (var33 in offsets) {
               val var34: Double = playerBlockX + var33[0] + 0.5
               val worldY: Double = playerBlockY
               val worldZ: Double = playerBlockZ + var33[1] + 0.5
               resolver.updateForTopItem(var30, previewStack, ItemDisplayContext.GROUND, player.level(), null, 0)
               if (!var30.isEmpty()) {
                  poseStack.pushPose()
                  poseStack.translate(var34 - cameraX, worldY - cameraY, worldZ - cameraZ)
                  val bob: Float = Mth.sin((double)((var31 + (float)(var33[0] * 3 + var33[1] * 7)) / 10.0F)) * 0.1F + 0.1F
                  val spin: Float = ItemEntity.getSpin(var31, (float)(var33[0] * 3 + var33[1] * 7))
                  poseStack.translate(0.0, (double)(bob + -((float)var30.getModelBoundingBox().minY) + 0.0625F), 0.0)
                  poseStack.mulPose(Axis.YP.rotation(spin) as Quaternionfc)
                  var30.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0)
                  poseStack.popPose()
               }
            }
         }
      }
   }

   @JvmStatic
   public fun getPreviewItemStack(): ItemStack? {
      if (!INSTANCE.isEnabled() || !modelPreview) {
         return null
      } else if (!INSTANCE.isInItemModelScreen()) {
         return null
      } else if (currentItemId == null) {
         return null
      } else {
         val itemId: Identifier = currentItemId
         val var1: ItemModel = INSTANCE

         var `$this$getPreviewItemStack_u24lambda_u249`: Any
         try {
            val var10000: java.lang.String = itemId.toString()
            `$this$getPreviewItemStack_u24lambda_u249` = Result.constructor_impl/* $VF was: constructor-impl */(
               MCRegistryKt.defaultStack$default(MCRegistryKt.mcItem(var10000), 0, 1, null)
            )
         } catch (var4: java.lang.Throwable) {
            `$this$getPreviewItemStack_u24lambda_u249` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
         }

         return (
            if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$getPreviewItemStack_u24lambda_u249`))
               null
               else
               `$this$getPreviewItemStack_u24lambda_u249`
         ) as ItemStack
      }
   }

   private fun isInItemModelScreen(): Boolean {
      if (!settingsOpen) {
         return false
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val screen: Screen = var10000.gui.screen()
         if (screen != null && screen is IModuleScreen) {
            return true
         } else {
            settingsOpen = false
            modelPreview = false
            return false
         }
      }
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         settingsOpen = true
         ItemModelEditor.INSTANCE.build(settingsPanel, INSTANCE)
         Unit.INSTANCE
      }
   }

   public open fun createdAt(): Long {
      return 1776412800000L
   }

   @JvmStatic
   fun {
      val var4: MapValue = ValueApiKt.map$default({ 
         LinkedHashMap() as java.util.Map
      }, IdentifierSerializer() as KSerializer, ItemTransformation.Companion.serializer(), null, null, null, 56, null)
      var4.setHiddenInGui(true)
      transformations$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
   }
}
