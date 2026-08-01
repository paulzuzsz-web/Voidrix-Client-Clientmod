package gg.voidrix.client.v2.modules.impl

import com.mojang.blaze3d.resource.CrossFrameResourcePool
import com.mojang.blaze3d.resource.GraphicsResourceAllocator
import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelTargetBundle
import net.minecraft.client.renderer.PostChain
import net.minecraft.resources.Identifier
import org.slf4j.Logger

@VoidrixMiniTag(tags = ["colorsaturation"])
@SourceDebugExtension(["SMAP\nColorSaturationModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ColorSaturationModule.kt\ngg/voidrix/client/v2/modules/impl/ColorSaturationModule\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n*L\n1#1,205:1\n40#2:206\n21#3:207\n*S KotlinDebug\n*F\n+ 1 ColorSaturationModule.kt\ngg/voidrix/client/v2/modules/impl/ColorSaturationModule\n*L\n176#1:206\n169#1:207\n*E\n"])
public object ColorSaturationModule : Module("Color Saturation", ModuleCategory.VISUAL, false, false, false, 28) {
   private final val log: Logger = MCLogger.getLogger("ColorSaturation")
   private final var debugLogged: Boolean

   public final val hue: Number by ValueApiKt.numeric$default(0.0, RangesKt.rangeTo(-1.0, 1.0) as ClosedRange, 0.01, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return hue$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }


   public final val saturation: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.0, 2.0) as ClosedRange, 0.01, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return saturation$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Number
      }


   public final val brightness: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.2, 2.0) as ClosedRange, 0.01, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return brightness$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Number
      }


   public final val contrast: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.15, 1.5) as ClosedRange, 0.01, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return contrast$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }


   public final val POST_EFFECT: Identifier

   public fun renderColorSaturation(pool: CrossFrameResourcePool) {
      if (!this.isEnabled()) {
         debugLogged = false
      } else {
         val var10000: Minecraft = Minecraft.getInstance()
         val var4: PostChain = var10000.getShaderManager().getPostChain(POST_EFFECT, LevelTargetBundle.MAIN_TARGETS)
         if (var4 == null) {
            if (!debugLogged) {
               log.error("[ColorSaturation] getPostChain returned NULL for ${POST_EFFECT}")
               debugLogged = true
            }
         } else {
            if (!debugLogged) {
               log.info("[ColorSaturation] Shader loaded! hue=${this.hue} sat=${this.saturation} bright=${this.brightness} contrast=${this.contrast}")
               debugLogged = true
            }

            var4.process(var10000.gameRenderer.mainRenderTarget(), pool as GraphicsResourceAllocator)
         }
      }
   }

   @JvmStatic
   fun {
      val var10000: Identifier = Identifier.fromNamespaceAndPath("voidrix", "color_saturation")
      POST_EFFECT = var10000
   }
}
