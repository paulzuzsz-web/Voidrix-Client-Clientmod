package gg.voidrix.client.v2.modules.blockoutline

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.PoseStack.Pose
import gg.voidrix.client.v2.utils.ColorUtils
import java.awt.Color
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.core.Direction
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Matrix4f
import org.joml.Matrix4fc

public object BlockOverlayRenderer {
   private const val DEPTH_OFFSET: Float = 0.001F

   @JvmStatic
   public fun submitFill(collector: SubmitNodeCollector, poseStack: PoseStack, shape: VoxelShape) {
      if (BlockOutline.INSTANCE.isEnabled() && BlockOutline.INSTANCE.overlayEnabled) {
         val overlayColor: Color = if (BlockOutline.INSTANCE.overlayRgb) Color(ColorUtils.rainbowEffect()) else BlockOutline.INSTANCE.overlayColor
         collector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), { pose: Pose, buffer: VertexConsumer ->
            val m: Matrix4f = pose.pose()

            for (aabb in `$shape`.toAabbs()) {
               val x0: Float = (float)aabb.minX
               val y0: Float = (float)aabb.minY
               val z0: Float = (float)aabb.minZ
               val x1: Float = (float)aabb.maxX
               val y1: Float = (float)aabb.maxY
               val z1: Float = (float)aabb.maxZ
               val var10000: BlockOverlayRenderer = INSTANCE
               var10000.putQuad(buffer, m, Direction.DOWN, x0, y0 - 0.001F, z0, x1, y0 - 0.001F, z1, `$r`, `$g`, `$b`, `$a`)
               INSTANCE.putQuad(buffer, m, Direction.UP, x0, y1 + 0.001F, z0, x1, y1 + 0.001F, z1, `$r`, `$g`, `$b`, `$a`)
               INSTANCE.putQuad(buffer, m, Direction.NORTH, x0, y0, z0 - 0.001F, x1, y1, z0 - 0.001F, `$r`, `$g`, `$b`, `$a`)
               INSTANCE.putQuad(buffer, m, Direction.SOUTH, x0, y0, z1 + 0.001F, x1, y1, z1 + 0.001F, `$r`, `$g`, `$b`, `$a`)
               INSTANCE.putQuad(buffer, m, Direction.WEST, x0 - 0.001F, y0, z0, x0 - 0.001F, y1, z1, `$r`, `$g`, `$b`, `$a`)
               INSTANCE.putQuad(buffer, m, Direction.EAST, x1 + 0.001F, y0, z0, x1 + 0.001F, y1, z1, `$r`, `$g`, `$b`, `$a`)
            }
         })
      }
   }

   private fun putQuad(
      vc: VertexConsumer,
      pose: Matrix4f,
      direction: Direction,
      x0: Float,
      y0: Float,
      z0: Float,
      x1: Float,
      y1: Float,
      z1: Float,
      r: Float,
      g: Float,
      b: Float,
      a: Float
   ) {
      when (BlockOverlayRenderer.WhenMappings.$EnumSwitchMapping$0[direction.ordinal()]) {
         1 -> {
            vc.addVertex(pose as Matrix4fc, x0, y0, z0).setColor(r, g, b, a).setNormal(0.0F, -1.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z0).setColor(r, g, b, a).setNormal(0.0F, -1.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z1).setColor(r, g, b, a).setNormal(0.0F, -1.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x0, y0, z1).setColor(r, g, b, a).setNormal(0.0F, -1.0F, 0.0F)
         }
         2 -> {
            vc.addVertex(pose as Matrix4fc, x0, y0, z1).setColor(r, g, b, a).setNormal(0.0F, 1.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z1).setColor(r, g, b, a).setNormal(0.0F, 1.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z0).setColor(r, g, b, a).setNormal(0.0F, 1.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x0, y0, z0).setColor(r, g, b, a).setNormal(0.0F, 1.0F, 0.0F)
         }
         3 -> {
            vc.addVertex(pose as Matrix4fc, x0, y1, z0).setColor(r, g, b, a).setNormal(0.0F, 0.0F, -1.0F)
            vc.addVertex(pose as Matrix4fc, x1, y1, z0).setColor(r, g, b, a).setNormal(0.0F, 0.0F, -1.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z0).setColor(r, g, b, a).setNormal(0.0F, 0.0F, -1.0F)
            vc.addVertex(pose as Matrix4fc, x0, y0, z0).setColor(r, g, b, a).setNormal(0.0F, 0.0F, -1.0F)
         }
         4 -> {
            vc.addVertex(pose as Matrix4fc, x0, y0, z1).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 1.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z1).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 1.0F)
            vc.addVertex(pose as Matrix4fc, x1, y1, z1).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 1.0F)
            vc.addVertex(pose as Matrix4fc, x0, y1, z1).setColor(r, g, b, a).setNormal(0.0F, 0.0F, 1.0F)
         }
         5 -> {
            vc.addVertex(pose as Matrix4fc, x0, y0, z0).setColor(r, g, b, a).setNormal(-1.0F, 0.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x0, y0, z1).setColor(r, g, b, a).setNormal(-1.0F, 0.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x0, y1, z1).setColor(r, g, b, a).setNormal(-1.0F, 0.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x0, y1, z0).setColor(r, g, b, a).setNormal(-1.0F, 0.0F, 0.0F)
         }
         6 -> {
            vc.addVertex(pose as Matrix4fc, x1, y0, z1).setColor(r, g, b, a).setNormal(1.0F, 0.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y0, z0).setColor(r, g, b, a).setNormal(1.0F, 0.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y1, z0).setColor(r, g, b, a).setNormal(1.0F, 0.0F, 0.0F)
            vc.addVertex(pose as Matrix4fc, x1, y1, z1).setColor(r, g, b, a).setNormal(1.0F, 0.0F, 0.0F)
         }
         else -> throw NoWhenBranchMatchedException()
      }
   }
}
