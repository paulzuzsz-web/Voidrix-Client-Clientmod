package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.scale.WindowScaleManager
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphicsImpl
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.math.MathKt
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.resources.Identifier

@VoidrixMiniTag(tags = ["branding"])
@SourceDebugExtension(["SMAP\nBrandingOverlay.kt\nKotlin\n*S Kotlin\n*F\n+ 1 BrandingOverlay.kt\ngg/voidrix/client/v2/modules/impl/BrandingOverlay\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n*L\n1#1,187:1\n239#2:188\n40#2:189\n21#3:190\n*S KotlinDebug\n*F\n+ 1 BrandingOverlay.kt\ngg/voidrix/client/v2/modules/impl/BrandingOverlay\n*L\n104#1:188\n104#1:189\n26#1:190\n*E\n"])
public object BrandingOverlay {
   public final val LOGO_TEXTURE: Identifier
   private const val LOGO_WIDTH: Double = 128.0
   private const val LOGO_HEIGHT: Double = 26.5

   private fun getLogoWidth(): Double {
      return 128.0 * IconModule.INSTANCE.size.doubleValue()
   }

   private fun getLogoHeight(): Double {
      return 26.5 * IconModule.INSTANCE.size.doubleValue()
   }

   private fun createGraphics(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int): OwoUIGraphics {
      return OwoUIGraphicsImpl.Companion.of(guiGraphics, mouseX, mouseY) as OwoUIGraphics
   }

   @JvmStatic
   public fun handleScreenRendering(screenWidth: Int, screenHeight: Int, guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
      if (IconModule.INSTANCE.isEnabled()) {
         INSTANCE.renderCornerLogo(INSTANCE.createGraphics(guiGraphics, mouseX, mouseY), (double)screenWidth, (double)screenHeight)
      }
   }

   @JvmStatic
   public fun handleAboveInventoryRendering(
      guiGraphics: GuiGraphicsExtractor,
      mouseX: Int,
      mouseY: Int,
      leftPos: Int,
      topPos: Int,
      imageWidth: Int,
      isCreative: Boolean
   ) {
      if (IconModule.INSTANCE.isEnabled() && IconModule.INSTANCE.tiktokMode) {
         INSTANCE.renderAboveInventory(INSTANCE.createGraphics(guiGraphics, mouseX, mouseY), leftPos, topPos, imageWidth, isCreative)
      }
   }

   private fun renderCornerLogo(graphics: OwoUIGraphics, screenWidth: Double, screenHeight: Double) {
      val w: Double = this.getLogoWidth()
      val h: Double = this.getLogoHeight()
      val margin: Double = IconModule.INSTANCE.margin.doubleValue()
      var var10000: Double
      if (WindowScaleManager.INSTANCE.isRenderingWithCustomScale()) {
         var10000 = WindowScaleManager.INSTANCE.getCurrentScale()
      } else {
         val var25: Minecraft = Minecraft.getInstance()
         var10000 = var25.getWindow().getGuiScale()
      }

      val var23: Float = (float)(1.0 / var10000 * 3.0)
val var24: Double = screenWidth / (float)(1.0 / var10000 * 3.0)
val scaledScreenHeight: Double = screenHeight / (float)(1.0 / var10000 * 3.0)
      when (BrandingOverlay.WhenMappings.$EnumSwitchMapping$0[IconModule.INSTANCE.position.ordinal()]) {
         1, 2 -> var10000 = margin
         3, 4 -> var10000 = var24 - w - margin
         else -> throw NoWhenBranchMatchedException()
      }

      when (BrandingOverlay.WhenMappings.$EnumSwitchMapping$0[IconModule.INSTANCE.position.ordinal()]) {
         1, 3 -> var10000 = margin
         2, 4 -> var10000 = scaledScreenHeight - h - margin
         else -> throw NoWhenBranchMatchedException()
      }

      graphics.push()
      graphics.scale(var23, var23)
      graphics.translate((float)var10000, (float)var10000)
      graphics.drawTextureScaled(LOGO_TEXTURE, 0.0, 0.0, w, h, 0.0, 0.0, w, h, w, h, true)
      graphics.pop()
   }

   private fun renderAboveInventory(graphics: OwoUIGraphics, leftPos: Int, topPos: Int, imageWidth: Int, isCreative: Boolean) {
      val w: Double = this.getLogoWidth()
      val h: Double = this.getLogoHeight()
      val margin: Double = IconModule.INSTANCE.margin.doubleValue()
      val x: Double = leftPos + (imageWidth - w) / 2.0
      var yOffset: Int = 0
      if (isCreative) {
         yOffset = MathKt.roundToInt(24.0)
      }

      val y: Double = topPos - h - margin - yOffset
      graphics.push()
      graphics.drawTextureScaled(LOGO_TEXTURE, x, y, w, h, 0.0, 0.0, w, h, w, h, true)
      graphics.pop()
   }

   @JvmStatic
   fun {
      val var10000: Identifier = Identifier.fromNamespaceAndPath("voidrix-client", "textures/voidrix-logo-text.png")
      LOGO_TEXTURE = var10000
   }
}
