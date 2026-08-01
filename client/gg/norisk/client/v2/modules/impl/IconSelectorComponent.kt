package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.auth.NoriskAuth
import gg.norisk.compat.http.ApiErrorTranslationKt
import gg.norisk.compat.icon.NrcPlayerCache
import gg.norisk.compat.text.TextKt
import gg.norisk.cosmetics.v2.sync.NrcCustomIconService
import gg.norisk.cosmetics.v2.ui.WardrobeCache
import gg.norisk.networking.model.core.NoRiskUserMinimal
import gg.norisk.networking.model.cosmetics.CosmeticUser
import gg.norisk.networking.model.cosmetics.CustomIcon
import gg.norisk.networking.model.cosmetics.CustomIconInfo
import gg.norisk.networking.model.cosmetics.SupportACreatorCode
import gg.norisk.networking.model.cosmetics.api.WardrobeResponse
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.component.TextureComponent
import gg.norisk.owolib.owo.ui.component.UIComponents
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.Size
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.owolib.owo.ui.util.UISounds
import gg.norisk.ui.components.nrc.NrcLabelButton
import gg.norisk.ui.modules.v3.V3Theme
import gg.norisk.ui.theme.ThemeModule
import gg.norisk.ui.v2.toast.components.NrcToastComponent.Builder
import java.util.ArrayList
import java.util.Arrays
import java.util.Locale
import java.util.UUID
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.Identifier

@SourceDebugExtension(["SMAP\nIconSelectorComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IconSelectorComponent.kt\ngg/norisk/client/v2/modules/impl/IconSelectorComponent\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 Text.kt\ngg/norisk/compat/text/TextKt\n+ 4 OwoIdentifier.kt\ngg/norisk/compat/resource/OwoIdentifierKt\n*L\n1#1,196:1\n774#2:197\n865#2,2:198\n1869#2:200\n1869#2,2:201\n1870#2:203\n67#3:204\n67#3:206\n67#3:207\n67#3:208\n21#4:205\n*S KotlinDebug\n*F\n+ 1 IconSelectorComponent.kt\ngg/norisk/client/v2/modules/impl/IconSelectorComponent\n*L\n65#1:197\n65#1:198,2\n79#1:200\n84#1:201,2\n79#1:203\n94#1:204\n177#1:206\n178#1:207\n184#1:208\n110#1:205\n*E\n"])
public class IconSelectorComponent : FlowLayout(Sizing.Companion.fill(100), Sizing.Companion.content(), Algorithm.VERTICAL) {
   private final val perRow: Int = 6
   private final val tileGap: Int = 4
   private final var tileSize: Int = 48
   private final var lastLaidOutWidth: Int = -1

   public open fun layout(space: Size) {
      super.layout(space)
      val w: Int = (int)this.width
      if ((int)this.width > 0 && (int)this.width != this.lastLaidOutWidth) {
         this.lastLaidOutWidth = w
         val newSize: Int = RangesKt.coerceIn((w - (this.perRow - 1) * this.tileGap) / this.perRow, 32, 72)
         if (newSize != this.tileSize) {
            this.tileSize = newSize
            this.build()
            super.layout(space)
         }
      }
   }

   public fun build() {
      this.clearChildren()
      this.gap(4)
      this.padding(Insets.Companion.vertical(4))
      this.horizontalAlignment(HorizontalAlignment.CENTER)
      val playerUuid: UUID = NoriskAuth.INSTANCE.getPlayerUuid()
      if (playerUuid == null) {
         this.child(this.emptyStateLabel("nrc.icon.label.not_logged_in") as UIComponent)
      } else {
         var var37: java.util.List
         run label91@{
            val var10000: WardrobeResponse = WardrobeCache.INSTANCE.getCached()
            if (var10000 != null) {
               val var36: CosmeticUser = var10000.getCosmeticUser()
               if (var36 != null) {
                  var37 = var36.getOwnedItems()
                  if (var37 != null) {
                     return@label91
                  }
               }
            }

            var37 = CollectionsKt.emptyList()
         }

         var ownedItems: java.util.List
         var isNrcPlus: Boolean
         run label94@{
            ownedItems = var37
            val user: NoRiskUserMinimal = NrcPlayerCache.INSTANCE.getCached(playerUuid)
            isNrcPlus = user != null && user.isNoRiskPlus()
            if (user != null) {
               val var38: SupportACreatorCode = user.getSupportACreatorCode()
               if (var38 != null) {
                  var39 = var38.getCode()
                  return@label94
               }
            }

            var39 = null
         }

         val creatorCode: java.lang.String = var39
         val `$i$f$forEach`: java.lang.Iterable = CustomIconInfo.Companion.getICONS()
         val var10: java.util.Collection = ArrayList()

         for (var13 in `$i$f$forEach`) {
            if (var13 as CustomIcon == CustomIconInfo.Companion.getDEFAULT() || ownedItems.contains((var13 as CustomIcon).getUuid())) {
               var10.add(var13)
            }
         }

         val flatItems: java.util.List = var10 as java.util.List
         if ((var10 as java.util.List).isEmpty()) {
            this.child(this.emptyStateLabel("nrc.icon.label.no_icons") as UIComponent)
         } else {
            val var22: FlowLayout = FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.VERTICAL)
            var22.gap(4)
            var22.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            val var21: FlowLayout = var22

            for (var28 in CollectionsKt.chunked(flatItems, this.perRow)) {
               val var29: java.util.List = var28 as java.util.List
               val var31: FlowLayout = FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.HORIZONTAL)
               var31.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
               var31.gap(this.tileGap)
               val row: FlowLayout = var31

               for (`element$ivx` in var29) {
                  row.child(this.buildIconEntry(`element$ivx` as CustomIcon, isNrcPlus, creatorCode, playerUuid) as UIComponent)
               }

               var21.child(row as UIComponent)
            }

            this.child(var21 as UIComponent)
         }
      }
   }

   private fun emptyStateLabel(key: String): LabelComponent {
      val `args$iv`: Array<Any> = arrayOfNulls(0)
      val var10002: MutableComponent = Component.translatable(key, Arrays.copyOf(`args$iv`, `args$iv`.length))
      val var5: LabelComponent = LabelComponent(TextKt.toSmallCapsText(var10002 as Component) as Component, 0.9F)
      var5.setAutoColorSupplier({ 
         V3Theme.INSTANCE.grayColor(10)
      })
      var5.margins(Insets.Companion.vertical(6))
      return var5
   }

   private fun buildIconEntry(icon: CustomIcon, isNrcPlus: Boolean, creatorCode: String?, playerUuid: UUID): FlowLayout {
      val size: Int = this.tileSize
      val iconSize: Int = this.tileSize - 6
      val var10000: Identifier = Identifier.fromNamespaceAndPath(
         "noriskclient", this.getIconPath(icon, if (icon == CustomIconInfo.Companion.getCREATOR_CODE()) creatorCode else null)
      )
      val var18: TextureComponent = UIComponents.texture(var10000, 0, if (isNrcPlus) iconSize else 0, iconSize, iconSize, iconSize, iconSize * 2)
      val var19: Function0 = { 
         var var4: UUID
         run label31@{
            val var10000: NoRiskUserMinimal = NrcPlayerCache.INSTANCE.getCached(`$playerUuid`)
            if (var10000 != null) {
               val var3: CustomIconInfo = var10000.getCustomIconInfo()
               if (var3 != null) {
                  var4 = var3.getCurrentIcon()
                  return@label31
               }
            }

            var4 = null
         }

         if (`$icon` == CustomIconInfo.Companion.getDEFAULT())
            var4 == null || var4 == CustomIconInfo.Companion.getDEFAULT().getUuid()
            else
            var4 == `$icon`.getUuid()
         }
      val labelScale: NrcLabelButton = NrcLabelButton(" ", lambda_8@{ button: NrcLabelButton, var3: Double, var5: Double, mouse: Int ->
         if (mouse != 0) {
            return@lambda_8 Unit.INSTANCE
         } else if (button.isSelected()() as java.lang.Boolean) {
            return@lambda_8 Unit.INSTANCE
         } else {
            UISounds.playButtonSound()
            `this$0`.selectIcon(`$icon`)
            return@lambda_8 Unit.INSTANCE
         }
      })
      labelScale.setSelected(var19)
      labelScale.setUseV3Colors(true)
      labelScale.sizing(Sizing.Companion.fixed(size))
      labelScale.setForcedBorderColor(
         { 
            if (`$isSelectedFn`())
               (if (ThemeModule.INSTANCE.getNightMode()) V3Theme.INSTANCE.accent(11) else V3Theme.INSTANCE.accent(9))
               else
               (if (`$entry`.isHovered()) V3Theme.INSTANCE.accent(8) else V3Theme.INSTANCE.accent(6))
            }
      )
      labelScale.child(var18 as UIComponent)
      val var22: LabelComponent = LabelComponent(TextKt.toSmallCapsText(StringsKt.replace$default(icon.getName(), '_', ' ', false, 4, null)) as Component, 0.7F)
      var22.setMaxEllipsis(size - 4)
      var22.setAutoColorSupplier(
         { 
            if (`$isSelectedFn`())
               V3Theme.INSTANCE.accentColor(11)
               else
               (if (`$entry`.isHovered()) V3Theme.INSTANCE.grayColor(12) else V3Theme.INSTANCE.grayColor(10))
            }
      )
      val var24: FlowLayout = FlowLayout(Sizing.Companion.fixed(size), Sizing.Companion.content(), Algorithm.VERTICAL)
      var24.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var24.gap(3)
      var24.child(labelScale as UIComponent)
      var24.child(var22 as UIComponent)
      return var24
   }

   private fun selectIcon(icon: CustomIcon) {
      NrcCustomIconService.INSTANCE
         .selectIcon(
            icon,
            { 
               var var10000: Builder = Builder.success$default(Builder(null, null, 3, null), false, 1, null)
               var `args$iv`: Array<Any> = arrayOfNulls(0)
               var var10001: MutableComponent = Component.translatable("nrc.icon.toast.title", Arrays.copyOf(`args$iv`, `args$iv`.length))
               var10000 = var10000.title(TextKt.toSmallCapsText(var10001 as Component) as Component)
               `args$iv` = arrayOfNulls(0)
               var10001 = Component.translatable("nrc.icon.toast.updated", Arrays.copyOf(`args$iv`, `args$iv`.length))
               var10000.description(var10001 as Component).withCloseButton(true).build().show()
               Unit.INSTANCE
            },
            { it: java.lang.Throwable ->
               val var10000: Builder = Builder.error$default(Builder(null, null, 3, null), false, 1, null)
               val `args$iv`: Array<Any> = arrayOfNulls(0)
               val var10001: MutableComponent = Component.translatable("nrc.icon.toast.title", Arrays.copyOf(`args$iv`, `args$iv`.length))
               var10000.title(TextKt.toSmallCapsText(var10001 as Component) as Component)
                  .description(ApiErrorTranslationKt.toTranslatedErrorText(it, "nrc.icon.toast.error", false) as Component)
                  .withCloseButton(true)
                  .build()
                  .show()
                  Unit.INSTANCE
            }
         )
      }

   private fun getIconPath(icon: CustomIcon, creatorCode: String?): String {
      if (creatorCode != null) {
         val var3: java.lang.String = creatorCode.toLowerCase(Locale.ROOT)
         return "creatorcode/icons/$var3.png"
      } else {
         val var10000: java.lang.String = icon.getName().toLowerCase(Locale.ROOT)
         return "textures/iconfont/$var10000_icons.png"
      }
   }
}
