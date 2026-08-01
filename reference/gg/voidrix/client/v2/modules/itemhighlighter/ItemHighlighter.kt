package gg.voidrix.client.v2.modules.itemhighlighter

import com.mojang.blaze3d.PrimitiveTopology
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexConsumer
import gg.voidrix.client.v2.modules.shinypots.ShinyPots
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.client.MCRegistryKt
import gg.voidrix.compat.serialization.IdentifierSerializer
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.utils.OwoLibExtensionsKt
import java.awt.Color
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.LinkedHashSet
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.feature.ItemFeatureRenderer
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import net.minecraft.resources.Identifier
import net.minecraft.util.Util
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fc
import org.joml.Matrix4f
import org.joml.Vector2f
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nItemHighlighter.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemHighlighter.kt\ngg/voidrix/client/v2/modules/itemhighlighter/ItemHighlighter\n+ 2 OwoLibExtensions.kt\ngg/voidrix/ui/utils/OwoLibExtensionsKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,837:1\n153#2,3:838\n165#2,3:852\n808#3,11:841\n808#3,11:855\n295#3,2:866\n*S KotlinDebug\n*F\n+ 1 ItemHighlighter.kt\ngg/voidrix/client/v2/modules/itemhighlighter/ItemHighlighter\n*L\n293#1:838,3\n300#1:852,3\n293#1:841,11\n300#1:855,11\n300#1:866,2\n*E\n"])
public object ItemHighlighter : Module("Item Highlighter", ModuleCategory.QUALITY_OF_LIFE, false, false, false, 28) {
   private final val ihLogger: Logger = MCLogger.getLogger("ItemHighlighter")
   public final val GLINT_GUI_PIPELINE: RenderPipeline

   public final var items: MutableMap<Identifier, ItemHighlighterDto> by ValueApiKt.map$default(
         { 
            val var0: java.util.Map = LinkedHashMap()
            val map: java.util.Map = var0
            var var3: ItemHighlighter = INSTANCE

            try {
               val goldenAppleId: Identifier = MCRegistryKt.registryId(MCRegistryKt.mcItem("minecraft:potion"))
               map.put(goldenAppleId, ItemHighlighterDto(goldenAppleId, false, null, null, 14, null))
               val var12: Any = Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
            } catch (var9: java.lang.Throwable) {
               val `$this$items_delegate_u24lambda_u244_u24lambda_u243_u24lambda_u242`: Any = Result.constructor_impl/* $VF was: constructor-impl */(
                  ResultKt.createFailure(var9)
               )
            }

            var3 = INSTANCE

            try {
               val var19: Identifier = MCRegistryKt.registryId(MCRegistryKt.mcItem("minecraft:splash_potion"))
               map.put(var19, ItemHighlighterDto(var19, false, null, null, 14, null))
               val var14: Any = Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
            } catch (var8: java.lang.Throwable) {
               val var13: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var8))
            }

            var3 = INSTANCE

            try {
               val var20: Identifier = MCRegistryKt.registryId(MCRegistryKt.mcItem("minecraft:golden_apple"))
               map.put(var20, ItemHighlighterDto(var20, false, null, null, 14, null))
               val var16: Any = Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
            } catch (var7: java.lang.Throwable) {
               val var15: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var7))
            }

            var0
         },
         IdentifierSerializer() as KSerializer,
         ItemHighlighterDto.Companion.serializer(),
         null,
         null,
         null,
         56,
         null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return items$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MutableMap<Identifier, ItemHighlighterDto>
      }

      public final set(<set-?>) {
         items$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   private final val colorRenderedPositions: MutableSet<Long> = LinkedHashSet() as java.util.Set

   private fun computeGlintTexMatrix(scale: Float = 8.0F): Matrix4f {
      val mc: Minecraft = Minecraft.getInstance()
      val var10000: Double = Util.getMillis()
      val var10001: Any = mc.options.glintSpeed().get()
      val l: Long = (long)(var10000 * (var10001 as java.lang.Number).doubleValue() * 8.0)
      val var7: Matrix4f = Matrix4f().translation(-((float)(l % 110000L) / 110000.0F), (float)(l % 30000L) / 30000.0F, 0.0F).rotateZ(0.17453294F).scale(scale)
      return var7
   }

   private fun transformUv(mat: Matrix4f, u: Float, v: Float): Vector2f {
      val out: Vector2f = Vector2f()
      out.x = mat.m00() * u + mat.m10() * v + mat.m30()
      out.y = mat.m01() * u + mat.m11() * v + mat.m31()
      return out
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         settingsPanel.clearChildren()
         settingsPanel.child(ItemHighlighterSearchComponent(null, null, 3, null) as UIComponent)
         val var1: ItemHighlighterListComponent = ItemHighlighterListComponent()
         var1.build(CollectionsKt.toList(INSTANCE.items.values()))
         settingsPanel.child(var1 as UIComponent)
         Unit.INSTANCE
      }
   }

   public fun add(identifier: Identifier, root: ParentUIComponent) {
      UISounds.playButtonSound()
      this.items.put(identifier, ItemHighlighterDto(identifier, false, null, null, 14, null))
      val `array$iv`: ArrayList = ArrayList()
      root.collectDescendants(`array$iv`)
      val `$this$filterIsInstanceTo$iv$iv$iv`: java.lang.Iterable = `array$iv`
      val `destination$iv$iv$iv`: java.util.Collection = ArrayList()

      for (`element$iv$iv$iv` in `$this$filterIsInstanceTo$iv$iv$iv`) {
         if (`element$iv$iv$iv` is ItemHighlighterListComponent) {
            `destination$iv$iv$iv`.add(`element$iv$iv$iv`)
         }
      }

      val var10000: ItemHighlighterListComponent = CollectionsKt.firstOrNull(`destination$iv$iv$iv` as java.util.List) as ItemHighlighterListComponent
      if (var10000 != null) {
         val var10001: ItemHighlighterDto = this.items.get(identifier)
         if (var10001 == null) {
            return
         }

         var10000.add(var10001)
      }
   }

   public fun remove(dto: ItemHighlighterDto, root: ParentUIComponent) {
      UISounds.playButtonSound()
      dto.isEnabled = false
      this.items.remove(dto.itemId)
      var var10000: UIComponent = dto.itemId.toString()
      val `id$iv`: java.lang.String = var10000
      val `array$iv`: ArrayList = ArrayList()
      root.collectDescendants(`array$iv`)
      val `$this$filterIsInstanceTo$iv$iv$iv`: java.lang.Iterable = `array$iv`
      val `element$iv$iv`: java.util.Collection = ArrayList()

      for (`element$iv$iv$iv` in `$this$filterIsInstanceTo$iv$iv$iv`) {
         if (`element$iv$iv$iv` is ItemHighlighterListComponent.ItemHighlighterListEntry) {
            `element$iv$iv`.add(`element$iv$iv$iv`)
         }
      }

      val var17: java.util.Iterator = (`element$iv$iv` as java.util.List).iterator()

      while (true) {
         if (var17.hasNext()) {
            val var18: Any = var17.next()
            if (!((var18 as UIComponent).id() == `id$iv`)) {
               continue
            }

            var10000 = (UIComponent)var18
            break
         }

         var10000 = null
         break
      }

      val var3: ItemHighlighterListComponent.ItemHighlighterListEntry = var10000 as ItemHighlighterListComponent.ItemHighlighterListEntry
      if (var10000 as ItemHighlighterListComponent.ItemHighlighterListEntry != null) {
         OwoLibExtensionsKt.removeFromParent(var3 as UIComponent)
      }
   }

   private fun posKey(x: Int, y: Int): Long {
      return (long)x shl 32 or y and 4294967295L
   }

   public fun highlightItemEndRender(x: Int, y: Int) {
      colorRenderedPositions.remove(this.posKey(x, y))
   }

   public fun highlightItemBefore(stack: ItemStack, x: Int, y: Int, guiGraphics: GuiGraphicsExtractor) {
      if (this.isEnabled()) {
         val var10000: Item = stack.getItem()
         val var9: ItemHighlighterDto = this.items.get(MCRegistryKt.registryId(var10000))
         if (var9 != null) {
            if (var9.isEnabled) {
               if (var9.mode === HighlightMode.COLOR) {
                  if (colorRenderedPositions.add(this.posKey(x, y))) {
                     val c: Color = var9.highlightColor.getChromaOrDefault()
                     guiGraphics.fill(x, y, x + 16, y + 16, c.getAlpha() shl 24 or c.getRed() shl 16 or c.getGreen() shl 8 or c.getBlue())
                  }
               }
            }
         }
      }
   }

   public fun highlightItemAfter(stack: ItemStack, x: Int, y: Int, guiGraphics: GuiGraphicsExtractor) {
      val var10000: Item = stack.getItem()
      val itemId: Identifier = MCRegistryKt.registryId(var10000)
      val dto: ItemHighlighterDto = this.items.get(itemId)
      if (this.isEnabled() && dto != null && dto.isEnabled) {
         if (dto.mode === HighlightMode.COLOR) {
            colorRenderedPositions.remove(this.posKey(x, y))
            return
         }
      } else if (!ShinyPots.INSTANCE.isEnabled() || !ShinyPots.INSTANCE.isPotion(itemId)) {
         return
      }

      this.drawGlintSubmit(guiGraphics, x, y, 16)
   }

   private fun drawGlintSubmit(guiGraphics: GuiGraphicsExtractor, x: Int, y: Int, size: Int) {
      val x2: Int = x + size
      val y2: Int = y + size
      val texMat: Matrix4f = this.computeGlintTexMatrix(0.5F)
      val uv00: Vector2f = this.transformUv(texMat, 0.0F, 0.0F)
      val uv10: Vector2f = this.transformUv(texMat, 1.0F, 0.0F)
      val uv11: Vector2f = this.transformUv(texMat, 1.0F, 1.0F)
      val uv01: Vector2f = this.transformUv(texMat, 0.0F, 1.0F)
      val color: Int = -1
      val pose: Matrix3x2f = Matrix3x2f(guiGraphics.pose() as Matrix3x2fc)
      val scissor: ScreenRectangle = guiGraphics.scissorStack.peek()
      val textureSetup: TextureSetup = TextureSetup.singleTexture(
         Minecraft.getInstance().getTextureManager().getTexture(ItemFeatureRenderer.ENCHANTED_GLINT_ITEM).getTextureView(),
         RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)
      )
      val bounds: ScreenRectangle = ScreenRectangle(x, y, size, size)
      guiGraphics.guiRenderState.addGuiElement(object : GuiElementRenderState {
         public open fun buildVertices(vc: VertexConsumer) {
            vc.addVertexWith2DPose(pose as Matrix3x2fc, (float)x, (float)y).setUv(uv00.x, uv00.y).setColor(color)
            vc.addVertexWith2DPose(pose as Matrix3x2fc, (float)x, (float)y2).setUv(uv01.x, uv01.y).setColor(color)
            vc.addVertexWith2DPose(pose as Matrix3x2fc, (float)x2, (float)y2).setUv(uv11.x, uv11.y).setColor(color)
            vc.addVertexWith2DPose(pose as Matrix3x2fc, (float)x2, (float)y).setUv(uv10.x, uv10.y).setColor(color)
         }

         public open fun pipeline(): RenderPipeline {
            return ItemHighlighter.INSTANCE.GLINT_GUI_PIPELINE
         }

         public open fun textureSetup(): TextureSetup {
            val var1: TextureSetup = textureSetup
            return var1
         }

         public open fun scissorArea(): ScreenRectangle? {
            return scissor
         }

         public open fun bounds(): ScreenRectangle {
            return bounds
         }
      })
   }

   public open fun createdAt(): Long {
      return 1776412800000L
   }

   @JvmStatic
   fun {
      val var10000: RenderPipeline = RenderPipelines.register(
         RenderPipeline.builder(arrayOf(RenderPipelines.GUI_TEXTURED_SNIPPET))
            .withLocation("pipeline/voidrix_gui_glint")
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(ColorTargetState(BlendFunction.GLINT))
            .build()
      )
      GLINT_GUI_PIPELINE = var10000
   }
}
