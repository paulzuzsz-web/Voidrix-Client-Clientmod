package gg.norisk.client.v2.modules.impl

import com.mojang.blaze3d.platform.NativeImage
import gg.norisk.compat.event.ClientEvents
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import java.awt.Color
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.texture.OverlayTexture

public object HitColorModule : Module("HitColor", ModuleCategory.VISUAL, true, false, false, 24) {
   public final val color: Color by ValueApiKt.attribute$default(Color(255, 0, 0, 100), true, null, null, { color: Color ->
      INSTANCE.reload()
      Unit.INSTANCE
   }, 12, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return color$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as Color
      }


   public final val defaultColor: Color = Color(255, 0, 0, 100)

   public open fun onEnable() {
      super.onEnable()
      this.reload()
   }

   public open fun onDisable() {
      super.onDisable()
      this.reload()
   }

   public fun reload() {
      Minecraft.getInstance().execute({ 
         val var10000: OverlayTexture = Minecraft.getInstance().gameRenderer.overlayTexture()
         (var10000 as HitColorModule.IOverlayTextureExt).nrc_reload()
      })
   }

   private fun getColorInt(red: Int, green: Int, blue: Int, alpha: Int): Int {
      return (255 - alpha shl 24) + (blue shl 16) + (green shl 8) + red
   }

   private fun getColorIntArgb(red: Int, green: Int, blue: Int, alpha: Int): Int {
      return (255 - alpha shl 24) + (red shl 16) + (green shl 8) + blue
   }

   public fun reloadOverlay(texture: DynamicTexture) {
      val var10000: NativeImage = texture.getPixels()
      if (var10000 != null) {
         val nativeImage: NativeImage = var10000

         repeat(15) { i ->
            repeat(15) { j ->
               if (!this.isEnabled() && !HitColorPreview.INSTANCE.active) {
                  if (i < 8) {
                     nativeImage.setPixel(j, i, -1291911168)
                  } else {
                     nativeImage.setPixel(j, i, this.withAlpha((int)((1.0F - (float)j / 15.0F * 0.75F) * 255.0F), -1))
                  }
               } else if (i < 8) {
                  val k: Color = this.color
                  nativeImage.setPixel(j, i, this.getColorIntArgb(k.getRed(), k.getGreen(), k.getBlue(), k.getAlpha()))
               }
            }
         }

         texture.upload()
      }
   }

   public fun withAlpha(alpha: Int, rgb: Int): Int {
      return alpha shl 24 or rgb and 16777215
   }

   @JvmStatic
   fun {
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         HitColorPreview.INSTANCE.tick()
         Unit.INSTANCE
      })
   }

   public interface IOverlayTextureExt {
      public abstract fun nrc_reload() {
      }
   }
}
