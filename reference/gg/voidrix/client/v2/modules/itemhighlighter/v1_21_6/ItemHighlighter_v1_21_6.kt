package gg.voidrix.client.v2.modules.itemhighlighter.v1_21_6

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuTextureView
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.state.gui.BlitRenderState
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import net.minecraft.resources.Identifier
import org.joml.Matrix3x2f
import org.joml.Matrix3x2fc

public object ItemHighlighter_v1_21_6 {
   public fun GuiGraphicsExtractor.drawGlint_v1_21_6(
      pipeline: RenderPipeline,
      sprite: Identifier,
      x1: Int,
      x2: Int,
      y1: Int,
      y2: Int,
      u1: Float,
      u2: Float,
      v1: Float,
      v2: Float,
      color: Int
   ) {
      val gpuTextureView: GpuTextureView = Minecraft.getInstance().getTextureManager().getTexture(sprite).getTextureView()
      this.drawGlint_v1_21_6(`$this$drawGlint_v1_21_6`, pipeline, gpuTextureView, x1, y1, x2, y2, u1, u2, v1, v2, color)
   }

   public fun GuiGraphicsExtractor.drawGlint_v1_21_6(
      pipeline: RenderPipeline,
      texture: GpuTextureView,
      x1: Int,
      y1: Int,
      x2: Int,
      y2: Int,
      u1: Float,
      u2: Float,
      v1: Float,
      v2: Float,
      color: Int
   ) {
      `$this$drawGlint_v1_21_6`.guiRenderState
         .addGuiElement(
            BlitRenderState(
               pipeline,
               TextureSetup.singleTexture(texture, RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)),
               Matrix3x2f(`$this$drawGlint_v1_21_6`.pose() as Matrix3x2fc),
               x1,
               y1,
               x2,
               y2,
               u1,
               v1,
               u2,
               v2,
               color,
               `$this$drawGlint_v1_21_6`.scissorStack.peek()
            ) as GuiElementRenderState
         )
      }
}
