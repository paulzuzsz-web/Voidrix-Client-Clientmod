package gg.voidrix.client.v2.serverstyling

import com.mojang.blaze3d.platform.NativeImage
import gg.voidrix.client.v2.serverstyling.data.StyledServer
import gg.voidrix.compat.asset.VoidrixAssetReader
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.ui.utils.DevUtilsKt
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.util.ArrayList
import java.util.LinkedHashSet
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nServerStylingRenderer.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ServerStylingRenderer.kt\ngg/voidrix/client/v2/serverstyling/ServerStylingRenderer\n+ 2 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,184:1\n21#2:185\n40#3:186\n40#3:188\n1#4:187\n774#5:189\n865#5,2:190\n*S KotlinDebug\n*F\n+ 1 ServerStylingRenderer.kt\ngg/voidrix/client/v2/serverstyling/ServerStylingRenderer\n*L\n89#1:185\n92#1:186\n134#1:188\n160#1:189\n160#1:190,2\n*E\n"])
public object ServerStylingRenderer {
   private final val BACKGROUND_DIMENSIONS: Pair<Int, Int> = TuplesKt.to(1920, 1080)
   private const val BACKGROUND_OVERLAY_COLOR: Int = 1291845632
   private final val registeredDevTextures: MutableSet<String> = LinkedHashSet() as java.util.Set

   @JvmStatic
   public fun onRenderHead(ctx: GuiGraphicsExtractor, serverData: ServerData, x: Int, y: Int, w: Int, h: Int) {
      val var10000: java.lang.String = serverData.ip
      val var7: StyledServer = ServerStylingManager.getStyledServer(var10000)
      if (var7 != null) {
         INSTANCE.render(OwoUIGraphics.Companion.of(ctx), var7, x, y, w, h)
      }
   }

   @JvmStatic
   public fun shouldSkipDefaultIcon(serverIp: String): Boolean {
      return ServerStylingManager.getStyledServer(serverIp) != null
   }

   private fun render(g: OwoUIGraphics, server: StyledServer, x: Int, y: Int, w: Int, h: Int) {
      g.push()
      this.renderBackground(g, server, x, y, w, h)
      this.renderIcon(g, server, x, y)
      g.pop()
   }

   private fun renderBackground(g: OwoUIGraphics, server: StyledServer, x: Int, y: Int, w: Int, h: Int) {
      val var10000: Identifier = this.loadTexture(this.cleanAssetPath(server.assets.background))
      if (var10000 != null) {
         val var8: Pair = BACKGROUND_DIMENSIONS
         val imgW: Int = (BACKGROUND_DIMENSIONS.component1() as java.lang.Number).intValue()
         val imgH: Int = (var8.component2() as java.lang.Number).intValue()
         val ar: AspectRatioResult = this.calculateAspectRatio(w, h, imgW, imgH)
         g.drawTextureScaled(
            var10000,
            (double)x,
            (double)y,
            (double)w,
            (double)h,
            (double)ar.u,
            (double)ar.v,
            (double)ar.sourceW,
            (double)ar.sourceH,
            (double)imgW,
            (double)imgH,
            false
         )
         g.fill((double)x, (double)y, (double)(x + w), (double)(y + h), 1291845632)
      }
   }

   private fun renderIcon(g: OwoUIGraphics, server: StyledServer, x: Int, y: Int) {
      val var10000: Identifier = this.loadTexture(this.cleanAssetPath(server.assets.icon))
      if (var10000 != null) {
         g.drawTexture(var10000, (double)x, (double)y, 0.0, 0.0, 32.0, 32.0, 32.0, 32.0)
      }
   }

   private fun loadTexture(assetPath: String): Identifier? {
      val var10000: Identifier = Identifier.fromNamespaceAndPath("voidrix", "merged/$assetPath")
      val var9: Minecraft = Minecraft.getInstance()
      if (!var9.getResourceManager().getResource(var10000).isPresent()) {
         return if (this.loadDevTexture(assetPath, var10000)) var10000 else null
      } else {
         return var10000
      }
   }

   private fun loadDevTexture(assetPath: String, identifier: Identifier): Boolean {
      if (registeredDevTextures.contains(assetPath)) {
         return true
      } else {
         val var10000: ByteArray = VoidrixAssetReader.tryReadBytes$default(VoidrixAssetReader.INSTANCE, "voidrix", "merged/$assetPath", null, null, 12, null)
         if (var10000 == null) {
            return false
         } else {
            val bytes: ByteArray = var10000

            var nativeImage: Boolean
            try {
               val e: Closeable = ByteArrayInputStream(bytes)
               var `$i$f$getInstance`: java.lang.Throwable = null

               var var18: NativeImage
               try {
                  var18 = NativeImage.read(e as ByteArrayInputStream)
               } catch (var12: java.lang.Throwable) {
                  `$i$f$getInstance` = var12
                  throw var12
               } finally {
                  CloseableKt.closeFinally(e, `$i$f$getInstance`)
               }

               val var16: DynamicTexture = DynamicTexture({ 
                  `$assetPath`
               }, var18)
               val var19: Minecraft = Minecraft.getInstance()
               var19.getTextureManager().register(identifier, var16 as AbstractTexture)
               registeredDevTextures.add(assetPath)
               DevUtilsKt.voidrixDebug(this, "Loaded dev texture: $assetPath")
               nativeImage = true
            } catch (var14: Exception) {
               DevUtilsKt.voidrixError(this, "Failed to load dev texture voidrix:merged/$assetPath: ${var14.getMessage()}", var14)
               nativeImage = false
            }

            return nativeImage
         }
      }
   }

   private fun cleanAssetPath(assetPath: String): String {
      var path: java.lang.String = StringsKt.replace$default(assetPath, "\\", "/", false, 4, null)

      while (StringsKt.startsWith$default(path, "./", false, 2, null) || StringsKt.startsWith$default(path, "../", false, 2, null)) {
         path = StringsKt.removePrefix(StringsKt.removePrefix(path, "./"), "../")
      }

      val `$this$filterTo$iv$iv`: java.lang.Iterable = StringsKt.split$default(path, arrayOf("/"), false, 0, 6, null)
      val `destination$iv$iv`: java.util.Collection = ArrayList()

      for (`element$iv$iv` in `$this$filterTo$iv$iv`) {
         if (!(`element$iv$iv` as java.lang.String == ".")
            && !(`element$iv$iv` as java.lang.String == "..")
            && (`element$iv$iv` as java.lang.String).length() > 0) {
            `destination$iv$iv`.add(`element$iv$iv`)
         }
      }

      return CollectionsKt.joinToString$default(`destination$iv$iv` as java.util.List, "/", null, null, 0, null, null, 62, null)
   }

   private fun calculateAspectRatio(targetW: Int, targetH: Int, imgW: Int, imgH: Int): AspectRatioResult {
      return if ((float)targetW / targetH > (float)imgW / imgH)
         AspectRatioResult(0.0F, (imgH - (int)(imgW / ((float)targetW / targetH))) / 2, imgW, (int)(imgW / ((float)targetW / targetH)))
         else
         AspectRatioResult((imgW - (int)(imgH * ((float)targetW / targetH))) / 2, 0.0F, (int)(imgH * ((float)targetW / targetH)), imgH)
      }
}
