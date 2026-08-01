package gg.norisk.client.v2.modules.impl

import com.mojang.blaze3d.resource.CrossFrameResourcePool
import com.mojang.blaze3d.resource.GraphicsResourceAllocator
import gg.norisk.compat.client.MCLogger
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelTargetBundle
import net.minecraft.client.renderer.PostChain
import net.minecraft.resources.Identifier
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nMotionBlurModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 MotionBlurModule.kt\ngg/norisk/client/v2/modules/impl/MotionBlurModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 OwoIdentifier.kt\ngg/norisk/compat/resource/OwoIdentifierKt\n*L\n1#1,182:1\n40#2:183\n21#3:184\n*S KotlinDebug\n*F\n+ 1 MotionBlurModule.kt\ngg/norisk/client/v2/modules/impl/MotionBlurModule\n*L\n156#1:183\n149#1:184\n*E\n"])
public object MotionBlurModule : Module("MotionBlur", ModuleCategory.VISUAL, false, false, false, 28) {
   private final val log: Logger = MCLogger.getLogger("MotionBlur")
   private final var debugLogged: Boolean

   public final val strength: Number by ValueApiKt.numeric$default(0.7, RangesKt.rangeTo(0.01, 0.99) as ClosedRange, 0.01, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return strength$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }


   public final val POST_EFFECT: Identifier

   public fun renderMotionBlur(pool: CrossFrameResourcePool) {
      if (!this.isEnabled()) {
         debugLogged = false
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val var4: PostChain = var10000.getShaderManager().getPostChain(POST_EFFECT, LevelTargetBundle.MAIN_TARGETS)
         if (var4 == null) {
            if (!debugLogged) {
               log.error("[MotionBlur] getPostChain returned NULL for ${POST_EFFECT}")
               debugLogged = true
            }
         } else {
            if (!debugLogged) {
               log.info("[MotionBlur] Shader loaded! strength=${this.strength}")
               debugLogged = true
            }

            var4.process(var10000.gameRenderer.mainRenderTarget(), pool as GraphicsResourceAllocator)
         }
      }
   }

   @JvmStatic
   fun {
      val var10000: Identifier = Identifier.fromNamespaceAndPath("noriskclient", "motion_blur_simple")
      POST_EFFECT = var10000
   }
}
