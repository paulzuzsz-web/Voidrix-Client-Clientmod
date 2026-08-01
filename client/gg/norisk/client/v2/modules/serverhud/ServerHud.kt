package gg.norisk.client.v2.modules.serverhud

import gg.norisk.compat.client.MCClient
import gg.norisk.compat.resource.DynamicTextureCache
import gg.norisk.compat.text.RainbowTextUtilsKt
import gg.norisk.owolib.owo.ui.component.DynamicTextureComponent
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.component.UIComponents
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.AbstractHud
import gg.norisk.ui.api.hud.AnchorPoint
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.IContentBackground
import gg.norisk.ui.api.hud.IDynamicBackground
import gg.norisk.ui.api.hud.IDynamicBackgroundKt
import gg.norisk.ui.api.serializable.MultiColor
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.components.nrc.NrcMultiColorPicker
import gg.norisk.ui.v2.hud.AnchorPointPosition
import java.awt.geom.Point2D
import java.util.Base64
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.jetbrains.annotations.NotNull

public object ServerHud : AbstractHud("Server", false, false, false, 14), IContentBackground {
   public open val seoTags: Array<String>

   @Category(name = "Display")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public final var multiColor: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(null, false, false, 7, null)
   }, MultiColor.Companion.serializer(), "Color", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      NrcMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "ICON")
   @NotNull
   public final var showImage: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return showImage$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showImage$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   public final var imageSize: Number by ValueApiKt.numeric$default(24, IntRange(16, 64) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return imageSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }

      public final set(<set-?>) {
         imageSize$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.TOP_CENTER, 0.5, 0.05, null, 8, null)
         }
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   public open fun hudComponent(): UIComponent {
      return ServerHud.ServerDisplayComponent(null, null, 3, null) as UIComponent
   }

   private fun loadServerIconBytes(): ByteArray? {
      val var10000: ByteArray = MCClient.getCurrentServerIconBytes()
      if (var10000 == null) {
         return null
      } else {
         val bytes: ByteArray = var10000
         if (var10000.length == 0) {
            return null
         } else {
            var var2: ByteArray
            try {
               var2 = Base64.getDecoder().decode(bytes)
            } catch (var4: Exception) {
               var2 = var10000
            }

            return var2
         }
      }
   }

   @SourceDebugExtension(["SMAP\nServerHud.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ServerHud.kt\ngg/norisk/client/v2/modules/serverhud/ServerHud$ServerDisplayComponent\n+ 2 OwoIdentifier.kt\ngg/norisk/compat/resource/OwoIdentifierKt\n*L\n1#1,160:1\n39#2:161\n*S KotlinDebug\n*F\n+ 1 ServerHud.kt\ngg/norisk/client/v2/modules/serverhud/ServerHud$ServerDisplayComponent\n*L\n132#1:161\n*E\n"])
   private class ServerDisplayComponent(horizontalSizing: Sizing = Sizing.Companion.content(ServerHud.INSTANCE.dynamicBackground.getDynamicWidth()),
      verticalSizing: Sizing = Sizing.Companion.content(ServerHud.INSTANCE.dynamicBackground.getDynamicHeight())
   ) : FlowLayout(horizontalSizing, verticalSizing, Algorithm.HORIZONTAL) {
      private final var lastServerName: String?
      private final var lastShowImage: Boolean = ServerHud.INSTANCE.showImage
      private final var lastImageSize: Int = ServerHud.INSTANCE.imageSize.intValue()

      init {
         this.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
         this.allowOverflow(true)
         this.rebuildContent()
      }

      private fun rebuildContent() {
         this.surface(ServerHud.INSTANCE.getBackground().toSurface())
         this.clearChildren()
         var var10000: java.lang.String = MCClient.getCurrentServerIp()
         if (var10000 == null) {
            var10000 = "Singleplayer"
         }

         this.lastServerName = var10000
         this.lastShowImage = ServerHud.INSTANCE.showImage
         this.lastImageSize = ServerHud.INSTANCE.imageSize.intValue()
         if (ServerHud.INSTANCE.showImage) {
            this.addIconComponent(var10000)
         }

         val textWrapper: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
         textWrapper.allowOverflow(true)
         textWrapper.margins(Insets.Companion.horizontal(2))
         val pos: Point2D = AnchorPointPosition.toGlobalPos$default(ServerHud.INSTANCE.getAnchorPosition(), 0, 0, 3, null)
         val var5: LabelComponent = LabelComponent(
            RainbowTextUtilsKt.rainbowText(
               var10000,
               ServerHud.INSTANCE.multiColor.isRainbow(),
               ServerHud.INSTANCE.multiColor.isPositionalRainbow(),
               ServerHud.INSTANCE.multiColor.getChromaOrDefault().getRGB(),
               (int)pos.getX(),
               (int)pos.getY()
            ) as Component
         )
         var5.shadow(true)
         var5.setAutoTextSupplier(
            { 
               var var10000: java.lang.String = MCClient.getCurrentServerIp()
               if (var10000 == null) {
                  var10000 = "Singleplayer"
               }

               val p: Point2D = AnchorPointPosition.toGlobalPos$default(ServerHud.INSTANCE.getAnchorPosition(), 0, 0, 3, null)
               RainbowTextUtilsKt.rainbowText(
                  var10000,
                  ServerHud.INSTANCE.multiColor.isRainbow(),
                  ServerHud.INSTANCE.multiColor.isPositionalRainbow(),
                  ServerHud.INSTANCE.multiColor.getChromaOrDefault().getRGB(),
                  (int)p.getX(),
                  (int)p.getY()
               ) as Component
            }
         )
         textWrapper.child(var5 as UIComponent)
         this.child(textWrapper as UIComponent)
      }

      private fun addIconComponent(serverName: String) {
         val size: Int = ServerHud.INSTANCE.imageSize.intValue()
         if (!(serverName == "Singleplayer") && DynamicTextureCache.getOrLoadTexture("server_icon:$serverName", { 
            ServerHud.INSTANCE.loadServerIconBytes()
         }) != null) {
            this.child(DynamicTextureComponent({ 
               DynamicTextureCache.getOrLoadTexture("server_icon:$`$serverName`", { 
                  ServerHud.INSTANCE.loadServerIconBytes()
               })
            }, size, size) as UIComponent)
            return
         } else {
            try {
               val var10000: Identifier = Identifier.withDefaultNamespace("textures/misc/unknown_server.png")
               this.child(UIComponents.texture(var10000, 0, 0, size, size, size, size) as UIComponent)
            } catch (var6: Exception) {
            }
         }
      }

      protected open fun parentUpdate(delta: Float, mouseX: Int, mouseY: Int) {
         super.parentUpdate(delta, mouseX, mouseY)
         var var10000: java.lang.String = MCClient.getCurrentServerIp()
         if (var10000 == null) {
            var10000 = "Singleplayer"
         }

         if (!(var10000 == this.lastServerName)
            || ServerHud.INSTANCE.showImage != this.lastShowImage
            || ServerHud.INSTANCE.imageSize.intValue() != this.lastImageSize) {
            if (!(var10000 == this.lastServerName)) {
               DynamicTextureCache.invalidateByKey("server_icon:${this.lastServerName}")
            }

            this.rebuildContent()
         }
      }

      fun ServerDisplayComponent() {
         this(null, null, 3, null)
      }
   }
}
