package gg.voidrix.client.v2.modules.tnttimer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.ChatFormatting
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.Font.DisplayMode
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import org.jetbrains.annotations.NotNull
import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Quaternionfc

@SourceDebugExtension(["SMAP\nTntTimer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 TntTimer.kt\ngg/voidrix/client/v2/modules/tnttimer/TntTimer\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,292:1\n40#2:293\n40#2:294\n127#2:295\n40#2:296\n40#2:297\n127#2:298\n40#2:299\n66#3:300\n66#3:301\n66#3:302\n66#3:303\n*S KotlinDebug\n*F\n+ 1 TntTimer.kt\ngg/voidrix/client/v2/modules/tnttimer/TntTimer\n*L\n46#1:293\n93#1:294\n94#1:295\n94#1:296\n144#1:297\n145#1:298\n145#1:299\n166#1:300\n169#1:301\n182#1:302\n185#1:303\n*E\n"])
public object TntTimer : Module("TNT Timer", ModuleCategory.QUALITY_OF_LIFE, false, false, false, 28) {
   @Category(name = "Style")
   @NotNull
   public final var coloredLabel: Boolean by ValueApiKt.boolean$default(true, "Colored Label", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return coloredLabel$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         coloredLabel$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Style")
   @NotNull
   public final var showBackground: Boolean by ValueApiKt.boolean$default(true, "Show Background", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return showBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Style")
   @NotNull
   public final var showShadow: Boolean by ValueApiKt.boolean$default(false, "Text Shadow", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return showShadow$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showShadow$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   public open val seoTags: Array<String>
   private final val pendingDraws: ArrayList<gg.voidrix.client.v2.modules.tnttimer.TntTimer.PendingTntDraw> = ArrayList()

   @JvmStatic
   public fun queueEntityDraw(fuse: Int, matrices: PoseStack, entityHeight: Float) {
      if (fuse > 0) {
         val var10000: Minecraft = Minecraft.getInstance()
         val var5: Camera = var10000.gameRenderer.mainCamera
         matrices.pushPose()
         matrices.translate(0.0F, entityHeight + 0.5F, 0.0F)
         matrices.mulPose(Axis.YP.rotationDegrees(-var5.yRot()) as Quaternionfc)
         matrices.mulPose(Axis.XP.rotationDegrees(var5.xRot()) as Quaternionfc)
         matrices.scale(-0.025F, -0.025F, 0.025F)
         pendingDraws.add(TntTimer.PendingTntDraw(Matrix4f(matrices.last().pose() as Matrix4fc), fuse))
         matrices.popPose()
      }
   }

   @JvmStatic
   public fun drainPendingDraws() {
      if (!pendingDraws.isEmpty()) {
         pendingDraws.clear()
      }
   }

   @JvmStatic
   public fun processEntity(fuse: Int, matrices: PoseStack, entityHeight: Float) {
      if (fuse > 0) {
         val var10000: Minecraft = Minecraft.getInstance()
         val var8: Minecraft = Minecraft.getInstance()
         val var7: Camera = var10000.gameRenderer.mainCamera
         matrices.pushPose()
         matrices.translate(0.0F, entityHeight + 0.5F, 0.0F)
         matrices.mulPose(Axis.YP.rotationDegrees(-var7.yRot()) as Quaternionfc)
         matrices.mulPose(Axis.XP.rotationDegrees(var7.xRot()) as Quaternionfc)
         matrices.scale(-0.025F, -0.025F, 0.025F)
         matrices.popPose()
      }
   }

   @JvmStatic
   public fun submitEntityText(fuse: Int, matrices: PoseStack, entityHeight: Float, collector: SubmitNodeCollector) {
      if (fuse > 0) {
         val var10000: Minecraft = Minecraft.getInstance()
         val var13: Minecraft = Minecraft.getInstance()
         val var14: Font = var13.font
         val var11: Camera = var10000.gameRenderer.mainCamera
         matrices.pushPose()
         matrices.translate(0.0F, entityHeight + 0.5F, 0.0F)
         matrices.mulPose(Axis.YP.rotationDegrees(-var11.yRot()) as Quaternionfc)
         matrices.mulPose(Axis.XP.rotationDegrees(var11.xRot()) as Quaternionfc)
         matrices.scale(-0.025F, -0.025F, 0.025F)
         val var12: Component = INSTANCE.getTimeComponent(fuse)
         collector.submitText(
            matrices,
            (float)(-var14.width(var12 as FormattedText) / 2),
            2.0F,
            var12.getVisualOrderText(),
            INSTANCE.showShadow,
            DisplayMode.SEE_THROUGH,
            15728640,
            -1,
            if (INSTANCE.showBackground) 1056964608 else 0,
            0
         )
         matrices.popPose()
      }
   }

   private fun getTimeComponent(ticks: Int): Component {
      if (ticks > 72000) {
         val var19: MutableComponent = Component.literal("${ticks / 20 / 3600} h")
         return var19 as Component
      } else if (ticks > 1200) {
         val var18: MutableComponent = Component.literal("${ticks / 20 / 60} m")
         return var18 as Component
      } else {
         val s: Int = ticks / 20
         val ms: Int = ticks % 20 / 2
         if (this.isEnabled() && this.coloredLabel) {
            var var10000: ChatFormatting
            when (s) {
               0 -> var10000 = ChatFormatting.DARK_RED
               1 -> var10000 = ChatFormatting.RED
               2 -> var10000 = ChatFormatting.YELLOW
               3 -> var10000 = ChatFormatting.GREEN
               else -> var10000 = null
            }

            if (var10000 != null) {
               val var16: MutableComponent = Component.literal("$s.$ms s")
               val var17: MutableComponent = var16.withStyle(Style.EMPTY.withColor(var10000))
               return var17 as Component
            }
         }

         val var15: MutableComponent = Component.literal("$s.$ms s")
         return var15 as Component
      }
   }

   private data class PendingTntDraw(pose: Matrix4f, fuse: Int) {
      public final val pose: Matrix4f
      public final val fuse: Int

      init {
         this.pose = pose
         this.fuse = fuse
      }

      public operator fun component1(): Matrix4f {
         return this.pose
      }

      public operator fun component2(): Int {
         return this.fuse
      }

      public fun copy(pose: Matrix4f = this.pose, fuse: Int = this.fuse): gg.voidrix.client.v2.modules.tnttimer.TntTimer.PendingTntDraw {
         return TntTimer.PendingTntDraw(pose, fuse)
      }

      public override fun toString(): String {
         return "PendingTntDraw(pose=${this.pose}, fuse=${this.fuse})"
      }

      public override fun hashCode(): Int {
         return this.pose.hashCode() * 31 + Integer.hashCode(this.fuse)
      }

      public override operator fun equals(other: Any?): Boolean {
         label28@
         if (this === other) {
            return true
         } else {
            return other is TntTimer.PendingTntDraw
               && this.pose == (other as TntTimer.PendingTntDraw).pose
               && this.fuse == (other as TntTimer.PendingTntDraw).fuse
            }
      }
   }
}
