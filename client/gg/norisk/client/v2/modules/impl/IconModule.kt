package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.annotations.NrcMiniTag
import gg.norisk.compat.icon.IconResolver
import gg.norisk.compat.icon.NrcPlayerCache
import gg.norisk.compat.icon.IconResolver.IconResult
import gg.norisk.compat.nametag.NameTagIconEvent
import gg.norisk.compat.nametag.NameTagIconEventKt
import gg.norisk.compat.tablist.TabListIconEvent
import gg.norisk.compat.tablist.TabListIconEventKt
import gg.norisk.networking.model.core.NoRiskUserMinimal
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.modules.v3.V3Surfaces
import java.util.Arrays
import java.util.UUID
import kotlin.enums.EnumEntries
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.jetbrains.annotations.NotNull

@NrcMiniTag(tags = ["icon"])
@SourceDebugExtension(["SMAP\nIconModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IconModule.kt\ngg/norisk/client/v2/modules/impl/IconModule\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Text.kt\ngg/norisk/compat/text/TextKt\n*L\n1#1,93:1\n1#2:94\n67#3:95\n269#3:96\n*S KotlinDebug\n*F\n+ 1 IconModule.kt\ngg/norisk/client/v2/modules/impl/IconModule\n*L\n56#1:95\n56#1:96\n*E\n"])
public object IconModule : Module("Icon", ModuleCategory.VISUAL, false, true, false, 20) {
   @Category(name = "Branding")
   @NotNull
   public final val size: Number by ValueApiKt.numeric$default(1.0F, RangesKt.rangeTo(0.5F, 2.0F) as ClosedRange, 0.1F, "Branding Size", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return size$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }


   @Category(name = "Branding")
   @NotNull
   public final val margin: Number by ValueApiKt.numeric$default(5.0F, RangesKt.rangeTo(0.0F, 50.0F) as ClosedRange, 0.1F, "Margin", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return margin$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Number
      }


   @Category(name = "Branding")
   @NotNull
   public final val position: gg.norisk.client.v2.modules.impl.IconModule.BrandingPosition by ValueApiKt.enum$default(
         IconModule.BrandingPosition.BOTTOM_RIGHT, "Branding Position", null, null, null, 28, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return position$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as IconModule.BrandingPosition
      }


   @Category(name = "Branding")
   @NotNull
   public final val tiktokMode: Boolean by ValueApiKt.boolean$default(false, "TikTok Mode", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return tiktokMode$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   @Category(name = "Tab & Nametag")
   @NotNull
   public final val iconPosition: gg.norisk.client.v2.modules.impl.IconModule.IconPosition by ValueApiKt.enum$default(
         IconModule.IconPosition.NAME, "Icon Position", null, null, null, 28, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return iconPosition$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as IconModule.IconPosition
      }


   @Category(name = "Tab & Nametag")
   @NotNull
   public final val disableIcon: Boolean by ValueApiKt.boolean$default(false, "Disable Icon", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return disableIcon$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }


   @Category(name = "Tab & Nametag")
   @NotNull
   public final val showInNametag: Boolean by ValueApiKt.boolean$default(true, "Nametag", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return showInNametag$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }


   @Category(name = "Tab & Nametag")
   @NotNull
   public final val showInTabList: Boolean by ValueApiKt.boolean$default(true, "Tab List", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return showInTabList$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Boolean
      }


   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         val var10002: V3Surfaces = V3Surfaces.INSTANCE
         var `$this$buildCustomUi_u24lambda_u241_u24lambda_u240`: Array<Any> = arrayOfNulls(0)
         val var10003: MutableComponent = Component.translatable(
            "nrc.icon.label.selector",
            Arrays.copyOf(`$this$buildCustomUi_u24lambda_u241_u24lambda_u240`, `$this$buildCustomUi_u24lambda_u241_u24lambda_u240`.length)
         )
         val var11: java.lang.String = (var10003 as Component).getString()
         settingsPanel.child(0, V3Surfaces.categoryHeader$default(var10002, var11, null, 2, null) as UIComponent)
         val var6: IconModule = INSTANCE

         try {
            `$this$buildCustomUi_u24lambda_u241_u24lambda_u240` = (Object[])Result.constructor_impl/* $VF was: constructor-impl */(
               settingsPanel.child(1, IconSelectorComponent() as UIComponent)
            )
         } catch (var4: java.lang.Throwable) {
            `$this$buildCustomUi_u24lambda_u241_u24lambda_u240` = (Object[])Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var4))
         }

         Unit.INSTANCE
      }
   }

   private fun applyNameTagIcon(uuid: UUID, event: NameTagIconEvent) {
      val var10000: NoRiskUserMinimal = NrcPlayerCache.INSTANCE.getCached(uuid)
      if (var10000 != null) {
         val icon: IconResult = IconResolver.INSTANCE.resolve(var10000)
         event.setIconTexture(icon.getTexture())
         if (icon.isPlus()) {
            event.setV0(0.5F)
            event.setV1(1.0F)
         }
      }
   }

   private fun applyTabListIcon(uuid: UUID, event: TabListIconEvent) {
      var var10000: NoRiskUserMinimal = NrcPlayerCache.INSTANCE.getCached(uuid)
      if (var10000 == null) {
         val var7: java.lang.String = event.getPlayerName()
         var10000 = if (var7 != null) NrcPlayerCache.INSTANCE.findCachedByNameInText(var7) else null
         if (var10000 == null) {
            return
         }
      }

      val icon: IconResult = IconResolver.INSTANCE.resolve(var10000)
      event.setIconTexture(icon.getTexture())
      if (icon.isPlus()) {
         event.setV(8.0F)
      }
   }

   @JvmStatic
   fun {
      NameTagIconEventKt.getNameTagIconEvent().listen(lambda_2@{ event: NameTagIconEvent ->
         if (!INSTANCE.disableIcon && INSTANCE.showInNametag && !event.isSneaking()) {
            val var10000: UUID = event.getUuid()
            if (var10000 == null) {
               return@lambda_2 Unit.INSTANCE
            } else {
               INSTANCE.applyNameTagIcon(var10000, event)
               return@lambda_2 Unit.INSTANCE
            }
         } else {
            return@lambda_2 Unit.INSTANCE
         }
      })
      TabListIconEventKt.getTabListIconEvent().listen(lambda_3@{ event: TabListIconEvent ->
         if (!INSTANCE.disableIcon && INSTANCE.showInTabList) {
            event.setIconPosition(INSTANCE.iconPosition.name())
            INSTANCE.applyTabListIcon(event.getUuid(), event)
            return@lambda_3 Unit.INSTANCE
         } else {
            return@lambda_3 Unit.INSTANCE
         }
      })
   }

   public enum class BrandingPosition {
      TOP_LEFT,
      TOP_RIGHT,
      BOTTOM_LEFT,
      BOTTOM_RIGHT;

      @JvmStatic
      fun getEntries(): EnumEntries<IconModule.BrandingPosition> {
         $ENTRIES
      }
   }

   public enum class IconPosition {
      NAME,
      NAME_WITH_SPACE,
      PING;

      @JvmStatic
      fun getEntries(): EnumEntries<IconModule.IconPosition> {
         $ENTRIES
      }
   }
}
