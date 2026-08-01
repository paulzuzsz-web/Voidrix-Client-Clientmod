package gg.norisk.client.v2.modules.chatheads

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuSampler
import com.mojang.blaze3d.textures.GpuTextureView
import gg.norisk.ui.client.gui.render.state.TexturedQuadGuiElementRenderStateDouble
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.font.glyphs.BakedSheetGlyph.GlyphInstance
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.state.gui.GlyphRenderState
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import net.minecraft.client.renderer.state.gui.GuiRenderState
import net.minecraft.client.renderer.state.gui.GuiTextRenderState
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.PlayerSkin
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fc

public object ChatHeads_v1_21_6 {
   public final var matrix: Matrix3x2fc?
      internal set

   public final var bounds: ScreenRectangle?
      internal set

   public final var textGuiElementRenderState: GuiTextRenderState?
      internal set

   public fun renderHead_v1_21_6(
      instance: GuiRenderState,
      state: GuiElementRenderState,
      original: Operation<Void>,
      glyph: GlyphInstance,
      guiRenderState: GuiRenderState
   ) {
      val var8: GuiTextRenderState = textGuiElementRenderState
      val textures: PlayerSkin = if ((textGuiElementRenderState as? ChatHeads_v1_21_6.ITextGuiElementRenderStateExt) != null)
         (textGuiElementRenderState as? ChatHeads_v1_21_6.ITextGuiElementRenderStateExt).nrc_currentSkinTextures
         else
         null
         if (textures != null) {
         var var11: Boolean
         run label77@{
            val var10000: IDrawnGlyphExt = glyph as? IDrawnGlyphExt
            if ((glyph as? IDrawnGlyphExt) != null) {
               val var10: Int = var10000.nrc_charCode
               if (var10 != null) {
                  var11 = (char)var10.intValue() == 'ꯟ'
                  return@label77
               }
            }

            var11 = false
         }

         if (var11) {
            matrix = if ((state as? GlyphRenderState) != null) (state as? GlyphRenderState).pose() else null
            bounds = if ((state as? GlyphRenderState) != null) (state as? GlyphRenderState).bounds() else null
            this.draw(
               GuiGraphicsExtractor(Minecraft.getInstance(), guiRenderState, 0, 0),
               textures,
               glyph.x,
               glyph.y - 0.55F,
               8,
               this.getWhiteWithGlyphAlpha(glyph.color)
            )
            return
         }
      }

      original.call(arrayOf(instance, state))
   }

   public fun getWhiteWithGlyphAlpha(glyphColor: Int): Int {
      return (glyphColor ushr 24) shl 24 or 16777215
   }

   public fun draw(context: GuiGraphicsExtractor, textures: PlayerSkin, x: Number, y: Number, size: Number, color: Int) {
      this.draw(context, textures.body().texturePath(), x, y, size, true, false, color)
   }

   public fun draw(
      context: GuiGraphicsExtractor,
      texture: Identifier?,
      x: Number,
      y: Number,
      size: Number,
      hatVisible: Boolean,
      upsideDown: Boolean,
      color: Int
   ) {
      this.drawTextureDouble(
         context,
         RenderPipelines.GUI_TEXTURED,
         texture,
         x,
         y,
         8.0F,
         8 + (if (upsideDown) 8 else 0),
         size,
         size,
         8,
         8 * (if (upsideDown) -1 else 1),
         64,
         64,
         color
      )
      if (hatVisible) {
         this.drawHat(context, texture, x, y, size, upsideDown, color)
      }
   }

   private fun drawHat(context: GuiGraphicsExtractor, texture: Identifier?, x: Number, y: Number, size: Number, upsideDown: Boolean, color: Int) {
      this.drawTextureDouble(
         context,
         RenderPipelines.GUI_TEXTURED,
         texture,
         x,
         y,
         40.0F,
         8 + (if (upsideDown) 8 else 0),
         size,
         size,
         8,
         8 * (if (upsideDown) -1 else 1),
         64,
         64,
         color
      )
   }

   public fun GuiGraphicsExtractor.drawTextureDouble(
      pipeline: RenderPipeline?,
      sprite: Identifier?,
      x: Number,
      y: Number,
      u: Number,
      v: Number,
      width: Number,
      height: Number,
      regionWidth: Number,
      regionHeight: Number,
      textureWidth: Number,
      textureHeight: Number,
      color: Int
   ) {
      this.drawTexturedQuadDouble(
         `$this$drawTextureDouble`,
         pipeline,
         sprite,
         x,
         x.doubleValue() + width.doubleValue(),
         y,
         y.doubleValue() + height.doubleValue(),
         (u.doubleValue() + (double)0.0F) / textureWidth.doubleValue(),
         (u.doubleValue() + regionWidth.doubleValue()) / textureWidth.doubleValue(),
         (v.doubleValue() + (double)0.0F) / textureHeight.doubleValue(),
         (v.doubleValue() + regionHeight.doubleValue()) / textureHeight.doubleValue(),
         color
      )
   }

   private fun GuiGraphicsExtractor.drawTexturedQuadDouble(
      pipeline: RenderPipeline?,
      sprite: Identifier?,
      x1: Number,
      x2: Number,
      y1: Number,
      y2: Number,
      u1: Number,
      u2: Number,
      v1: Number,
      v2: Number,
      color: Int
   ) {
      val var10000: TextureManager = Minecraft.getInstance().getTextureManager()
      if (sprite != null) {
         val gpuTextureView: GpuTextureView = var10000.getTexture(sprite).getTextureView()
         val sampler: GpuSampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)
         val var16: GuiRenderState = `$this$drawTexturedQuadDouble`.guiRenderState
         val var10001: TexturedQuadGuiElementRenderStateDouble = TexturedQuadGuiElementRenderStateDouble
         if (pipeline != null) {
            val var10004: TextureSetup = TextureSetup.singleTexture(gpuTextureView, sampler)
            val var15: Matrix3x2fc = matrix
            val var10005: Matrix3x2f = matrix as? Matrix3x2f
            if ((matrix as? Matrix3x2f) != null) {
               var10001./* $VF: Unable to resugar constructor */<init>(
                  pipeline,
                  var10004,
                  var10005,
                  x1.floatValue(),
                  y1.floatValue(),
                  x2.floatValue(),
                  y2.floatValue(),
                  u1.floatValue(),
                  u2.floatValue(),
                  v1.floatValue(),
                  v2.floatValue(),
                  color,
                  bounds
               )
               var16.addGlyphToCurrentLayer(var10001 as GuiElementRenderState)
            }
         }
      }
   }

   public interface ITextGuiElementRenderStateExt {
      public var nrc_currentSkinTextures: PlayerSkin?
         internal final set
   }
}
