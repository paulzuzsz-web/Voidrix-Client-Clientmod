package gg.norisk.client.v2.debug

import gg.norisk.compat.resource.DynamicTextureCache
import gg.norisk.compat.resource.NrcTexture
import gg.norisk.compat.resource.NrcTextureMetadata
import gg.norisk.owolib.owo.ui.base.BaseOwoScreen
import gg.norisk.owolib.owo.ui.component.DynamicTextureComponent
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.component.TextureComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.ScrollContainer
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.ScrollContainer.Scrollbar
import gg.norisk.owolib.owo.ui.core.Color
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.OwoUIAdapter
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import java.io.File
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nNrcTextureDebugScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcTextureDebugScreen.kt\ngg/norisk/client/v2/debug/NrcTextureDebugScreen\n+ 2 OwoIdentifier.kt\ngg/norisk/compat/resource/OwoIdentifierKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 Text.kt\ngg/norisk/compat/text/TextKt\n*L\n1#1,190:1\n21#2:191\n40#3:192\n1#4:193\n66#5:194\n66#5:195\n66#5:196\n66#5:197\n66#5:198\n66#5:199\n66#5:200\n66#5:201\n66#5:202\n66#5:203\n66#5:204\n66#5:205\n66#5:206\n66#5:207\n66#5:208\n66#5:209\n66#5:210\n66#5:211\n*S KotlinDebug\n*F\n+ 1 NrcTextureDebugScreen.kt\ngg/norisk/client/v2/debug/NrcTextureDebugScreen\n*L\n20#1:191\n113#1:192\n117#1:194\n121#1:195\n124#1:196\n128#1:197\n129#1:198\n132#1:199\n134#1:200\n136#1:201\n138#1:202\n139#1:203\n140#1:204\n142#1:205\n144#1:206\n146#1:207\n147#1:208\n148#1:209\n155#1:210\n158#1:211\n*E\n"])
public class NrcTextureDebugScreen : BaseOwoScreen(null, 1) {
   private final val testTextureId: Identifier
   private final lateinit var statusLabel: LabelComponent
   private final lateinit var isLoadedLabel: LabelComponent
   private final lateinit var frameCountLabel: LabelComponent
   private final lateinit var capeTypeLabel: LabelComponent
   private final lateinit var textureTypeLabel: LabelComponent
   private final lateinit var screenshotStatusLabel: LabelComponent

   protected open fun createAdapter(): OwoUIAdapter<FlowLayout> {
      return OwoUIAdapter.Companion.create(this as Screen, gg/norisk/client/v2/debug/NrcTextureDebugScreen##Lambda_0_71())
   }

   protected open fun build(rootComponent: FlowLayout) {
      rootComponent.surface(Surface.Companion.flat(1996488704))
      rootComponent.padding(Insets.Companion.of(20))
      rootComponent.gap(10)
      rootComponent.horizontalAlignment(HorizontalAlignment.CENTER)
      val title: LabelComponent = LabelComponent("NrcTexture Debug")
      title.shadow(true)
      title.color(Color.Companion.ofRgb(16733525))
      title.horizontalSizing(Sizing.Companion.fill(100))
      title.horizontalTextAlignment(HorizontalAlignment.CENTER)
      rootComponent.child(title as UIComponent)
      val content: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(80), Sizing.Companion.content())
      content.surface(Surface.DARK_PANEL)
      content.padding(Insets.Companion.of(12))
      content.gap(8)
      content.child(this.sectionLabel("NrcTexture Interception Test") as UIComponent)
      content.child(this.descriptionLabel("Identifier: ${this.testTextureId}") as UIComponent)
      val texturePreview: TextureComponent = TextureComponent(this.testTextureId, 0, 0, 64, 64, 64, 64)
      texturePreview.sizing(Sizing.Companion.fixed(64), Sizing.Companion.fixed(64))
      content.child(texturePreview as UIComponent)
      this.statusLabel = LabelComponent("Status: Checking...")
      var var10000: LabelComponent = this.statusLabel
      if (this.statusLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
         var10000 = null
      }

      var10000.color(Color.Companion.ofRgb(16777045))
      var10000 = this.statusLabel
      if (this.statusLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
         var10000 = null
      }

      var10000.shadow(true)
      var var10001: LabelComponent = this.statusLabel
      if (this.statusLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
         var10001 = null
      }

      content.child(var10001 as UIComponent)
      this.isLoadedLabel = LabelComponent("isLoaded: -")
      var10000 = this.isLoadedLabel
      if (this.isLoadedLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("isLoadedLabel")
         var10000 = null
      }

      var10000.color(Color.WHITE)
      var10001 = this.isLoadedLabel
      if (this.isLoadedLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("isLoadedLabel")
         var10001 = null
      }

      content.child(var10001 as UIComponent)
      this.frameCountLabel = LabelComponent("Frames: -")
      var10000 = this.frameCountLabel
      if (this.frameCountLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("frameCountLabel")
         var10000 = null
      }

      var10000.color(Color.WHITE)
      var10001 = this.frameCountLabel
      if (this.frameCountLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("frameCountLabel")
         var10001 = null
      }

      content.child(var10001 as UIComponent)
      this.capeTypeLabel = LabelComponent("capeType: -")
      var10000 = this.capeTypeLabel
      if (this.capeTypeLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("capeTypeLabel")
         var10000 = null
      }

      var10000.color(Color.WHITE)
      var10001 = this.capeTypeLabel
      if (this.capeTypeLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("capeTypeLabel")
         var10001 = null
      }

      content.child(var10001 as UIComponent)
      this.textureTypeLabel = LabelComponent("Texture type: -")
      var10000 = this.textureTypeLabel
      if (this.textureTypeLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
         var10000 = null
      }

      var10000.color(Color.WHITE)
      var10001 = this.textureTypeLabel
      if (this.textureTypeLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
         var10001 = null
      }

      content.child(var10001 as UIComponent)
      content.child(this.sectionLabel("DynamicTextureCache Screenshot") as UIComponent)
      this.screenshotStatusLabel = LabelComponent("Screenshot: Searching...")
      var10000 = this.screenshotStatusLabel
      if (this.screenshotStatusLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
         var10000 = null
      }

      var10000.color(Color.Companion.ofRgb(16777045))
      var10000 = this.screenshotStatusLabel
      if (this.screenshotStatusLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
         var10000 = null
      }

      var10000.shadow(true)
      var10001 = this.screenshotStatusLabel
      if (this.screenshotStatusLabel == null) {
         Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
         var10001 = null
      }

      content.child(var10001 as UIComponent)
      content.child(DynamicTextureComponent({ 
         DynamicTextureCache.getRandomScreenshotTexture()
      }, 128, 72) as UIComponent)
      val scrollContainer: ScrollContainer = UIContainers.verticalScroll(Sizing.Companion.fill(100), Sizing.Companion.fill(100), content as UIComponent)
      scrollContainer.scrollbar(Scrollbar.Companion.vanilla())
      rootComponent.child(scrollContainer as UIComponent)
   }

   public open fun tick() {
      super.tick()
      this.updateNrcTextureStatus()
      this.updateScreenshotStatus()
   }

   private fun updateNrcTextureStatus() {
      val var10000: Minecraft = Minecraft.getInstance()
      val textureManager: TextureManager = var10000.getTextureManager()
      val `text$iv`: NrcTextureDebugScreen = this

      var `$i$f$literalText`: Any
      try {
         `$i$f$literalText` = Result.constructor_impl/* $VF was: constructor-impl */(textureManager.getTexture(`text$iv`.testTextureId))
      } catch (var6: java.lang.Throwable) {
         `$i$f$literalText` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var6))
      }

      val var7: Any = if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$i$f$literalText`)) null else `$i$f$literalText`
      if (var7 is NrcTexture) {
         var var43: LabelComponent = this.textureTypeLabel
         if (this.textureTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
            var43 = null
         }

         var var10001: MutableComponent = Component.literal("Texture type: NrcTexture (intercepted!)")
         var43.text(var10001 as Component)
         var var44: LabelComponent = this.textureTypeLabel
         if (this.textureTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
            var44 = null
         }

         var44.color(Color.Companion.ofRgb(5635925))
         if ((var7 as NrcTexture).isLoaded()) {
            var var45: LabelComponent = this.statusLabel
            if (this.statusLabel == null) {
               Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
               var45 = null
            }

            var10001 = Component.literal("Status: Loaded!")
            var45.text(var10001 as Component)
            var var46: LabelComponent = this.statusLabel
            if (this.statusLabel == null) {
               Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
               var46 = null
            }

            var46.color(Color.Companion.ofRgb(5635925))
         } else {
            var var47: LabelComponent = this.statusLabel
            if (this.statusLabel == null) {
               Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
               var47 = null
            }

            var10001 = Component.literal("Status: Loading...")
            var47.text(var10001 as Component)
            var var48: LabelComponent = this.statusLabel
            if (this.statusLabel == null) {
               Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
               var48 = null
            }

            var48.color(Color.Companion.ofRgb(16777045))
         }

         var var49: LabelComponent = this.isLoadedLabel
         if (this.isLoadedLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("isLoadedLabel")
            var49 = null
         }

         var10001 = Component.literal("isLoaded: ${(var7 as NrcTexture).isLoaded()}")
         var49.text(var10001 as Component)
         var var50: LabelComponent = this.frameCountLabel
         if (this.frameCountLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("frameCountLabel")
            var50 = null
         }

         var10001 = Component.literal("Frames: ${(var7 as NrcTexture).getFrames().size()}")
         var50.text(var10001 as Component)
         val var14: NrcTextureMetadata = (var7 as NrcTexture).getNrcTextureMetadata()
         var var51: LabelComponent = this.capeTypeLabel
         if (this.capeTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("capeTypeLabel")
            var51 = null
         }

         run label154@{
            if (var14 != null) {
               var70 = var14.getCapeType()
               if (var70 != null) {
                  return@label154
               }
            }

            var70 = "none"
         }

         var10001 = Component.literal("capeType: $var70")
         var51.text(var10001 as Component)
      } else if (var7 != null) {
         var var52: LabelComponent = this.textureTypeLabel
         if (this.textureTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
            var52 = null
         }

         var var72: MutableComponent = Component.literal("Texture type: ${(var7.getClass()::class).getSimpleName()} (NOT intercepted)")
         var52.text(var72 as Component)
         var var53: LabelComponent = this.textureTypeLabel
         if (this.textureTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
            var53 = null
         }

         var53.color(Color.Companion.ofRgb(16733525))
         var var54: LabelComponent = this.statusLabel
         if (this.statusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
            var54 = null
         }

         var72 = Component.literal("Status: Fallback (no NrcTexture)")
         var54.text(var72 as Component)
         var var55: LabelComponent = this.statusLabel
         if (this.statusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
            var55 = null
         }

         var55.color(Color.Companion.ofRgb(16733525))
         var var56: LabelComponent = this.isLoadedLabel
         if (this.isLoadedLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("isLoadedLabel")
            var56 = null
         }

         var72 = Component.literal("isLoaded: n/a")
         var56.text(var72 as Component)
         var var57: LabelComponent = this.frameCountLabel
         if (this.frameCountLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("frameCountLabel")
            var57 = null
         }

         var72 = Component.literal("Frames: n/a")
         var57.text(var72 as Component)
         var var58: LabelComponent = this.capeTypeLabel
         if (this.capeTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("capeTypeLabel")
            var58 = null
         }

         var72 = Component.literal("capeType: n/a")
         var58.text(var72 as Component)
      } else {
         var var59: LabelComponent = this.textureTypeLabel
         if (this.textureTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
            var59 = null
         }

         var var77: MutableComponent = Component.literal("Texture type: null (not registered)")
         var59.text(var77 as Component)
         var var60: LabelComponent = this.textureTypeLabel
         if (this.textureTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("textureTypeLabel")
            var60 = null
         }

         var60.color(Color.Companion.ofRgb(16733525))
         var var61: LabelComponent = this.statusLabel
         if (this.statusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
            var61 = null
         }

         var77 = Component.literal("Status: No texture found")
         var61.text(var77 as Component)
         var var62: LabelComponent = this.statusLabel
         if (this.statusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusLabel")
            var62 = null
         }

         var62.color(Color.Companion.ofRgb(16733525))
         var var63: LabelComponent = this.isLoadedLabel
         if (this.isLoadedLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("isLoadedLabel")
            var63 = null
         }

         var77 = Component.literal("isLoaded: n/a")
         var63.text(var77 as Component)
         var var64: LabelComponent = this.frameCountLabel
         if (this.frameCountLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("frameCountLabel")
            var64 = null
         }

         var77 = Component.literal("Frames: n/a")
         var64.text(var77 as Component)
         var var65: LabelComponent = this.capeTypeLabel
         if (this.capeTypeLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("capeTypeLabel")
            var65 = null
         }

         var77 = Component.literal("capeType: n/a")
         var65.text(var77 as Component)
      }
   }

   private fun updateScreenshotStatus() {
      val screenshot: File = DynamicTextureCache.getRandomScreenshot()
      if (screenshot != null) {
         var var10000: LabelComponent = this.screenshotStatusLabel
         if (this.screenshotStatusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
            var10000 = null
         }

         val var10001: MutableComponent = Component.literal("Screenshot: ${screenshot.getName()}")
         var10000.text(var10001 as Component)
         var10000 = this.screenshotStatusLabel
         if (this.screenshotStatusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
            var10000 = null
         }

         var10000.color(Color.Companion.ofRgb(5635925))
      } else {
         var var7: LabelComponent = this.screenshotStatusLabel
         if (this.screenshotStatusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
            var7 = null
         }

         val var9: MutableComponent = Component.literal("Screenshot: No screenshots found")
         var7.text(var9 as Component)
         var7 = this.screenshotStatusLabel
         if (this.screenshotStatusLabel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenshotStatusLabel")
            var7 = null
         }

         var7.color(Color.Companion.ofRgb(16733525))
      }
   }

   private fun sectionLabel(text: String): LabelComponent {
      val var2: LabelComponent = LabelComponent(text)
      var2.shadow(true)
      var2.color(Color.Companion.ofRgb(5614335))
      var2.margins(Insets.Companion.top(6))
      return var2
   }

   private fun descriptionLabel(text: String): LabelComponent {
      val var2: LabelComponent = LabelComponent(text)
      var2.color(Color.Companion.ofRgb(8947848))
      return var2
   }
}
