package gg.voidrix.client.v2.modules.resourcepackdisplay

import gg.voidrix.compat.client.MCResourcePacks
import gg.voidrix.compat.client.ResourcePackInfo
import gg.voidrix.compat.resource.DynamicTextureCache
import gg.voidrix.compat.text.RainbowTextUtilsKt
import gg.voidrix.owolib.owo.ui.component.BoxComponent
import gg.voidrix.owolib.owo.ui.component.DynamicTextureComponent
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.component.UIComponents
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.AbstractHud
import gg.voidrix.ui.api.hud.AnchorPoint
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.IContentBackground
import gg.voidrix.ui.api.hud.IDynamicBackground
import gg.voidrix.ui.api.hud.IDynamicBackgroundKt
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.api.value.NumericValue
import gg.voidrix.ui.api.value.TextValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixMultiColorPicker
import gg.voidrix.ui.modules.IModuleScreen
import gg.voidrix.ui.v2.hud.AnchorPointPosition
import java.awt.geom.Point2D
import java.util.ArrayList
import java.util.HashSet
import java.util.Locale
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nResourcePackDisplay.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ResourcePackDisplay.kt\ngg/voidrix/client/v2/modules/resourcepackdisplay/ResourcePackDisplay\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,299:1\n1669#2,8:300\n295#2,2:308\n295#2,2:311\n295#2,2:313\n1#3:310\n*S KotlinDebug\n*F\n+ 1 ResourcePackDisplay.kt\ngg/voidrix/client/v2/modules/resourcepackdisplay/ResourcePackDisplay\n*L\n125#1:300,8\n128#1:308,2\n130#1:311,2\n132#1:313,2\n*E\n"])
public object ResourcePackDisplay : AbstractHud("Resource Pack Display", false, false, false, 14), IContentBackground {
   public open val seoTags: Array<String>
   private final var cachedPackInfo: ResourcePackInfo?
   private final var lastSelectedPack: String = ""

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
      VoidrixMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var autoMode: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return autoMode$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         autoMode$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   public final var autoModePosition: Number
      public final get() {
         return autoModePosition$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }

      public final set(<set-?>) {
         autoModePosition$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   public final val selectedTexturePack: String
      public final get() {
         return selectedTexturePack$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.String
      }


   public final var showImage: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return showImage$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showImage$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   public final var showDescription: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return showDescription$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showDescription$delegate.setValue(this as ValueHolder, $$delegatedProperties[6], var1)
      }


   public final var imageSize: Number by ValueApiKt.numeric$default(24, IntRange(16, 64) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return imageSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Number
      }

      public final set(<set-?>) {
         imageSize$delegate.setValue(this as ValueHolder, $$delegatedProperties[7], var1)
      }


   public final var useCustomName: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return useCustomName$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         useCustomName$delegate.setValue(this as ValueHolder, $$delegatedProperties[8], var1)
      }


   public final val customName: String
      public final get() {
         return customName$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.String
      }


   private final val defaultPackInfo: ResourcePackInfo =
      ResourcePackInfo("vanilla", "Default", "The default look of Minecraft", null, null, false, false, 120, null)

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
      return ResourcePackDisplay.ResourcePackDisplayComponent(null, null, 3, null) as UIComponent
   }

   private fun getSelectedPack(): ResourcePackInfo? {
      if (this.autoMode) {
         return this.getFirstUserTexturePack()
      } else {
         val currentInput: java.lang.String = StringsKt.trim(this.selectedTexturePack).toString()
         if (cachedPackInfo != null && lastSelectedPack == currentInput) {
            return cachedPackInfo
         } else {
            val packInfo: ResourcePackInfo = this.findTexturePack(currentInput)
            cachedPackInfo = packInfo
            return packInfo
         }
      }
   }

   private fun getFirstUserTexturePack(): ResourcePackInfo {
      try {
         val packs: java.util.List = CollectionsKt.reversed(MCResourcePacks.getSelectedPacks())
         val position: Int = this.autoModePosition.intValue()
         val selectedPack: ResourcePackInfo = if (packs.size() >= position)
            packs.get(position - 1) as ResourcePackInfo
            else
            CollectionsKt.firstOrNull(packs) as ResourcePackInfo
            var var10000: ResourcePackInfo = selectedPack
         if (selectedPack == null) {
            var10000 = defaultPackInfo
         }

         return var10000
      } catch (var4: Exception) {
         return defaultPackInfo
      }
   }

   private fun findTexturePack(input: String): ResourcePackInfo? {
      val `$this$distinctBy$iv`: java.lang.Iterable = CollectionsKt.plus(MCResourcePacks.getSelectedPacks(), MCResourcePacks.getAvailablePacks())
      val it: HashSet = HashSet()
      val var7: ArrayList = ArrayList()

      for (itx in `$this$distinctBy$iv`) {
         if (it.add((itx as ResourcePackInfo).getId())) {
            var7.add(itx)
         }
      }

      val distinctPacks: java.util.List = var7
      val var24: java.util.Iterator = var7.iterator()

      var var10000: Any
      while (true) {
         if (var24.hasNext()) {
            val var30: Any = var24.next()
            if (!StringsKt.equals((var30 as ResourcePackInfo).getName(), input, true)) {
               continue
            }

            var10000 = var30
            break
         }

         var10000 = null
         break
      }

      val var12: ResourcePackInfo = var10000 as ResourcePackInfo
      if (var10000 as ResourcePackInfo != null) {
         return var12
      } else {
         val var25: java.util.Iterator = distinctPacks.iterator()

         while (true) {
            if (var25.hasNext()) {
               val var31: Any = var25.next()
               if (!StringsKt.equals((var31 as ResourcePackInfo).getId(), input, true)) {
                  continue
               }

               var10000 = var31
               break
            }

            var10000 = null
            break
         }

         val var13: ResourcePackInfo = var10000 as ResourcePackInfo
         if (var10000 as ResourcePackInfo != null) {
            return var13
         } else {
            val var26: java.util.Iterator = distinctPacks.iterator()

            while (true) {
               if (var26.hasNext()) {
                  val var32: Any = var26.next()
                  if (!StringsKt.contains((var32 as ResourcePackInfo).getName(), input, true)) {
                     continue
                  }

                  var10000 = var32
                  break
               }

               var10000 = null
               break
            }

            val var14: ResourcePackInfo = var10000 as ResourcePackInfo
            if (var10000 as ResourcePackInfo != null) {
               return var14
            } else {
               val var10002: java.lang.String = input.toLowerCase(Locale.ROOT)
               return ResourcePackInfo(
                  StringsKt.replace$default(var10002, " ", "_", false, 4, null), input, "Pack not found", null, null, false, false, 120, null
               )
            }
         }
      }
   }

   private fun buildNameText(pack: ResourcePackInfo): Component {
      val pos: Point2D = AnchorPointPosition.toGlobalPos$default(this.getAnchorPosition(), 0, 0, 3, null)
      if (this.useCustomName && !StringsKt.isBlank(this.customName)) {
         return RainbowTextUtilsKt.rainbowText(
            StringsKt.trim(this.customName).toString(),
            this.multiColor.isRainbow(),
            this.multiColor.isPositionalRainbow(),
            this.multiColor.getChromaOrDefault().getRGB(),
            (int)pos.getX(),
            (int)pos.getY()
         ) as Component
      } else if (this.multiColor.isRainbow()) {
         return RainbowTextUtilsKt.rainbowText(
            pack.getName(), true, this.multiColor.isPositionalRainbow(), this.multiColor.getChromaOrDefault().getRGB(), (int)pos.getX(), (int)pos.getY()
         ) as Component
      } else {
         return if (pack.getHasCustomNameFormatting())
            pack.getFormattedName()
            else
            RainbowTextUtilsKt.rainbowText$default(pack.getName(), false, false, this.multiColor.getChromaOrDefault().getRGB(), 0, 0, 48, null) as Component
         }
   }

   private fun buildDescriptionText(pack: ResourcePackInfo): Component {
      return if (pack.getHasCustomDescriptionFormatting())
         pack.getFormattedDescription()
         else
         RainbowTextUtilsKt.rainbowText$default(
            pack.getDescription(), false, false, if (!(pack.getDescription() == "Pack not found")) 12632256 else 16733525, 0, 0, 48, null
         ) as Component
      }

   public open fun createdAt(): Long {
      return 1742025600000L
   }

   @JvmStatic
   fun {
      val var4: NumericValue = ValueApiKt.numeric$default(2, IntRange(1, 10) as ClosedRange, null, null, null, null, 60, null)
      var4.setUiCondition({ 
         INSTANCE.autoMode
      })
      autoModePosition$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
      val var5: TextValue = ValueApiKt.text$default("Default", false, null, null, { newPack: java.lang.String ->
         if (!(newPack == lastSelectedPack)) {
            lastSelectedPack = newPack
            cachedPackInfo = null
            DynamicTextureCache.invalidateByKey("pack_icon:${lastSelectedPack}")
         }

         Unit.INSTANCE
      }, 14, null)
      var5.setUiCondition({ 
         !INSTANCE.autoMode
      })
      selectedTexturePack$delegate = var5.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
      val var6: TextValue = ValueApiKt.text$default("Custom Pack", false, null, null, null, 30, null)
      var6.setUiCondition({ 
         INSTANCE.useCustomName
      })
      customName$delegate = var6.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
   }

   @SourceDebugExtension(["SMAP\nResourcePackDisplay.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ResourcePackDisplay.kt\ngg/voidrix/client/v2/modules/resourcepackdisplay/ResourcePackDisplay$ResourcePackDisplayComponent\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,299:1\n1#2:300\n39#3:301\n328#4:302\n40#4:303\n*S KotlinDebug\n*F\n+ 1 ResourcePackDisplay.kt\ngg/voidrix/client/v2/modules/resourcepackdisplay/ResourcePackDisplay$ResourcePackDisplayComponent\n*L\n265#1:301\n280#1:302\n280#1:303\n*E\n"])
   private class ResourcePackDisplayComponent(horizontalSizing: Sizing = Sizing.Companion
            .content(ResourcePackDisplay.INSTANCE.dynamicBackground.getDynamicWidth()),
      verticalSizing: Sizing = Sizing.Companion.content(ResourcePackDisplay.INSTANCE.dynamicBackground.getDynamicHeight())
   ) : FlowLayout(horizontalSizing, verticalSizing, Algorithm.HORIZONTAL) {
      private final var lastPackId: String?
      private final var lastShowImage: Boolean = ResourcePackDisplay.INSTANCE.showImage
      private final var lastShowDescription: Boolean = ResourcePackDisplay.INSTANCE.showDescription
      private final var lastImageSize: Int = ResourcePackDisplay.INSTANCE.imageSize.intValue()

      init {
         this.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
         this.allowOverflow(true)
         this.rebuildContent()
      }

      private fun rebuildContent() {
         this.surface(ResourcePackDisplay.INSTANCE.getBackground().toSurface())
         this.clearChildren()
         val pack: ResourcePackInfo = ResourcePackDisplay.INSTANCE.getSelectedPack()
         this.lastPackId = if (pack != null) pack.getId() else null
         this.lastShowImage = ResourcePackDisplay.INSTANCE.showImage
         this.lastShowDescription = ResourcePackDisplay.INSTANCE.showDescription
         this.lastImageSize = ResourcePackDisplay.INSTANCE.imageSize.intValue()
         if (pack == null) {
            val var10: Point2D = AnchorPointPosition.toGlobalPos$default(ResourcePackDisplay.INSTANCE.getAnchorPosition(), 0, 0, 3, null)
            val var11: LabelComponent = LabelComponent(
               RainbowTextUtilsKt.rainbowText(
                  "No Pack",
                  ResourcePackDisplay.INSTANCE.multiColor.isRainbow(),
                  ResourcePackDisplay.INSTANCE.multiColor.isPositionalRainbow(),
                  ResourcePackDisplay.INSTANCE.multiColor.getChromaOrDefault().getRGB(),
                  (int)var10.getX(),
                  (int)var10.getY()
               ) as Component
            )
            var11.shadow(true)
            this.child(var11 as UIComponent)
         } else {
            val isValid: Boolean = !(pack.getDescription() == "Pack not found")
            if (ResourcePackDisplay.INSTANCE.showImage) {
               this.addIconComponent(pack, isValid)
            }

            val textWrapper: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
            textWrapper.allowOverflow(true)
            textWrapper.margins(Insets.Companion.horizontal(2))
            val descLabel: LabelComponent = LabelComponent(ResourcePackDisplay.INSTANCE.buildNameText(pack))
            descLabel.shadow(true)
            descLabel.setAutoTextSupplier(
               { 
                  val currentPack: ResourcePackInfo = ResourcePackDisplay.INSTANCE.getSelectedPack()
                  if (currentPack != null)
                     ResourcePackDisplay.INSTANCE.buildNameText(currentPack)
                     else
                     RainbowTextUtilsKt.rainbowText$default(
                        "No Pack", false, false, ResourcePackDisplay.INSTANCE.multiColor.getChromaOrDefault().getRGB(), 0, 0, 48, null
                     ) as Component
                  }
            )
            textWrapper.child(descLabel as UIComponent)
            if (ResourcePackDisplay.INSTANCE.showDescription && pack.getDescription().length() > 0) {
               val var15: LabelComponent = LabelComponent(ResourcePackDisplay.INSTANCE.buildDescriptionText(pack))
               var15.shadow(true)
               var15.setAutoTextSupplier(
                  { 
                     val currentPack: ResourcePackInfo = ResourcePackDisplay.INSTANCE.getSelectedPack()
                     if (currentPack != null)
                        ResourcePackDisplay.INSTANCE.buildDescriptionText(currentPack)
                        else
                        RainbowTextUtilsKt.rainbowText$default("", false, false, 12632256, 0, 0, 48, null) as Component
                     }
               )
               textWrapper.child(var15 as UIComponent)
            }

            this.child(textWrapper as UIComponent)
         }
      }

      private fun addIconComponent(pack: ResourcePackInfo, isValid: Boolean) {
         val size: Int = ResourcePackDisplay.INSTANCE.imageSize.intValue()
         if (DynamicTextureCache.getOrLoadTexture("pack_icon:${pack.getId()}", { 
            MCResourcePacks.loadPackIcon(`$pack`.getId())
         }) != null) {
            this.child(DynamicTextureComponent({ 
               DynamicTextureCache.getOrLoadTexture("pack_icon:${`$pack`.getId()}", { 
                  MCResourcePacks.loadPackIcon(`$pack`.getId())
               })
            }, size, size) as UIComponent)
         } else {
            try {
               val var10000: Identifier = Identifier.withDefaultNamespace("textures/misc/unknown_pack.png")
               this.child(UIComponents.texture(var10000, 0, 0, size, size, size, size) as UIComponent)
            } catch (var9: Exception) {
               val `path$iv`: BoxComponent = UIComponents.box(Sizing.Companion.fixed(size), Sizing.Companion.fixed(size))
               this.surface(Surface.Companion.flat(if (isValid) -10066330 else -43691))
               this.child(`path$iv` as UIComponent)
            }
         }
      }

      protected open fun parentUpdate(delta: Float, mouseX: Int, mouseY: Int) {
         super.parentUpdate(delta, mouseX, mouseY)
         val var10000: Minecraft = Minecraft.getInstance()
         val inModulesScreen: Boolean = var10000.gui.screen() is IModuleScreen
         val var9: ResourcePackInfo = ResourcePackDisplay.INSTANCE.getSelectedPack()
         if (inModulesScreen
            || !((if (var9 != null) var9.getId() else null) == this.lastPackId)
            || ResourcePackDisplay.INSTANCE.showImage != this.lastShowImage
            || ResourcePackDisplay.INSTANCE.showDescription != this.lastShowDescription
            || ResourcePackDisplay.INSTANCE.imageSize.intValue() != this.lastImageSize) {
            this.rebuildContent()
         }
      }

      fun ResourcePackDisplayComponent() {
         this(null, null, 3, null)
      }
   }
}
