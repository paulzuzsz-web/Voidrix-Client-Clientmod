package gg.norisk.client.v2.modules.ping

import gg.norisk.compat.client.MCClient
import gg.norisk.compat.nametag.NameTagConfig
import gg.norisk.compat.nametag.NameTagManager
import gg.norisk.compat.nametag.NameTagRenderEvent
import gg.norisk.compat.nametag.NameTagRenderEventKt
import gg.norisk.compat.nametag.NameTagTextModifyEvent
import gg.norisk.compat.nametag.NameTagTextModifyEventKt
import gg.norisk.compat.tablist.TabListIconEvent
import gg.norisk.compat.tablist.TabListIconEventKt
import gg.norisk.compat.text.LiteralTextBuilder
import gg.norisk.compat.text.RainbowTextUtilsKt
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.component.UIComponents
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.SingleTextHud
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.utils.OwoLibExtensions
import gg.norisk.ui.v2.hud.AnchorPointPosition
import java.awt.geom.Point2D
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nPingHud.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PingHud.kt\ngg/norisk/client/v2/modules/ping/PingHud\n+ 2 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt\n+ 3 TextBuilder.kt\ngg/norisk/compat/text/LiteralTextBuilder\n+ 4 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 5 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n*L\n1#1,182:1\n8#2,4:183\n8#2,4:201\n8#2,4:238\n78#3,6:187\n72#3,4:193\n87#3:197\n103#3,6:205\n72#3,4:211\n112#3:215\n78#3,6:216\n72#3,4:222\n87#3:226\n103#3,6:227\n72#3,4:233\n112#3:237\n103#3,6:242\n72#3,4:248\n112#3:252\n78#3,6:253\n72#3,4:259\n87#3:263\n103#3,6:264\n72#3,4:270\n112#3:274\n127#4:198\n40#4:199\n941#5:200\n*S KotlinDebug\n*F\n+ 1 PingHud.kt\ngg/norisk/client/v2/modules/ping/PingHud\n*L\n124#1:183,4\n99#1:201,4\n105#1:238,4\n125#1:187,6\n125#1:193,4\n125#1:197\n100#1:205,6\n100#1:211,4\n100#1:215\n101#1:216,6\n101#1:222,4\n101#1:226\n102#1:227,6\n102#1:233,4\n102#1:237\n106#1:242,6\n106#1:248,4\n106#1:252\n107#1:253,6\n107#1:259,4\n107#1:263\n108#1:264,6\n108#1:270,4\n108#1:274\n65#1:198\n65#1:199\n65#1:200\n*E\n"])
public object PingHud : SingleTextHud(
      "Ping", "{ping} ms", null, TextKt.getLiteral("{ping} wird mit dem aktuellen Ping ersetzt") as Component, false, false, false, 116
   ) {
   @Category(name = "Display")
   @NotNull
   public final var showHud: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return showHud$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showHud$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public final var usePingColor: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return usePingColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         usePingColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Tab")
   @NotNull
   public final var renderPingInTab: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return renderPingInTab$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         renderPingInTab$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   @Category(name = "NameTag")
   @NotNull
   public final var pingNameTagPosition: PingPosition by ValueApiKt.enum$default(PingPosition.NONE, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return pingNameTagPosition$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as PingPosition
      }

      public final set(<set-?>) {
         pingNameTagPosition$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   @Category(name = "NameTag")
   @NotNull
   public final var pingNameTagScale: Number by ValueApiKt.numeric$default(1.0F, RangesKt.rangeTo(0.1F, 2.0F) as ClosedRange, 0.01F, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return pingNameTagScale$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Number
      }

      public final set(<set-?>) {
         pingNameTagScale$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   @Category(name = "NameTag")
   @NotNull
   public final var pingNameTagBackground: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return pingNameTagBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         pingNameTagBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   private fun registerEvents() {
      TabListIconEventKt.getTabListIconEvent().listen(lambda_0@{ event: TabListIconEvent ->
         if (INSTANCE.isEnabled() && INSTANCE.renderPingInTab) {
            val ping: Int = RangesKt.coerceAtLeast(MCClient.getPlayerPingByUuid(event.getUuid()), 0)
            event.setPingReplacementText("$pingms")
            event.setPingReplacementColor(if (INSTANCE.usePingColor) INSTANCE.getPingColor(ping) else 16777215)
            event.setCancelPingBars(true)
            val var10000: Minecraft = Minecraft.getInstance()
            val var8: Font = var10000.font
            val var9: java.lang.String = event.getPingReplacementText()
            event.setPingExtraWidth(RangesKt.coerceAtLeast(var8.width(var9) - 10, 0))
            return@lambda_0 Unit.INSTANCE
         } else {
            return@lambda_0 Unit.INSTANCE
         }
      })
      NameTagRenderEventKt.getAfterRenderNameTagEvent()
         .listen(
            lambda_1@{ event: NameTagRenderEvent ->
               if (INSTANCE.isEnabled() && INSTANCE.pingNameTagPosition === PingPosition.ABOVE) {
                  val var10000: UUID = event.getUuid()
                  if (var10000 == null) {
                     return@lambda_1 Unit.INSTANCE
                  } else {
                     NameTagManager.INSTANCE
                        .renderNameTag(
                           INSTANCE.getPingText(var10000),
                           NameTagConfig(INSTANCE.pingNameTagScale.floatValue(), INSTANCE.pingNameTagBackground, false, 0.0, 8, null),
                           event.getNameTagPos(),
                           event.isSneaking(),
                           event.getSquaredDistanceToCamera(),
                           event.getContext()
                        )
                        return@lambda_1 Unit.INSTANCE
                  }
               } else {
                  return@lambda_1 Unit.INSTANCE
               }
            }
         )
         NameTagRenderEventKt.getBeforeRenderNameTagEvent()
         .listen(
            lambda_2@{ event: NameTagRenderEvent ->
               if (INSTANCE.isEnabled() && INSTANCE.pingNameTagPosition === PingPosition.BELOW) {
                  val var10000: UUID = event.getUuid()
                  if (var10000 == null) {
                     return@lambda_2 Unit.INSTANCE
                  } else {
                     NameTagManager.INSTANCE
                        .renderNameTag(
                           INSTANCE.getPingText(var10000),
                           NameTagConfig(INSTANCE.pingNameTagScale.floatValue(), INSTANCE.pingNameTagBackground, true, 0.0, 8, null),
                           event.getNameTagPos(),
                           event.isSneaking(),
                           event.getSquaredDistanceToCamera(),
                           event.getContext()
                        )
                        return@lambda_2 Unit.INSTANCE
                  }
               } else {
                  return@lambda_2 Unit.INSTANCE
               }
            }
         )
         NameTagTextModifyEventKt.getNameTagTextModifyEvent().listen(lambda_5@{ event: NameTagTextModifyEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_5 Unit.INSTANCE
         } else if (INSTANCE.pingNameTagPosition != PingPosition.LEFT && INSTANCE.pingNameTagPosition != PingPosition.RIGHT) {
            return@lambda_5 Unit.INSTANCE
         } else {
            val pingText: Component = INSTANCE.getPingText(event.getUuid())
            val var10000: NameTagTextModifyEvent
            val var10001: Component
            if (INSTANCE.pingNameTagPosition === PingPosition.LEFT) {
               val var4: LiteralTextBuilder = LiteralTextBuilder(null, true)
               var4.getAppendTasks().add(PingHud$registerEvents$lambda$5$lambda$3$$inlined$text$default$1(var4, pingText, true))
               var4.getAppendTasks().add(PingHud$registerEvents$lambda$5$lambda$3$$inlined$text$default$2(var4, " ", true))
               var4.getAppendTasks().add(PingHud$registerEvents$lambda$5$lambda$3$$inlined$text$default$3(var4, event.getDisplayName(), true))
               var10000 = event
               var10001 = var4.build() as Component
            } else {
               val var16: LiteralTextBuilder = LiteralTextBuilder(null, true)
               var16.getAppendTasks().add(PingHud$registerEvents$lambda$5$lambda$4$$inlined$text$default$1(var16, event.getDisplayName(), true))
               var16.getAppendTasks().add(PingHud$registerEvents$lambda$5$lambda$4$$inlined$text$default$2(var16, " ", true))
               var16.getAppendTasks().add(PingHud$registerEvents$lambda$5$lambda$4$$inlined$text$default$3(var16, pingText, true))
               var10000 = event
               var10001 = var16.build() as Component
            }

            var10000.setDisplayName(var10001)
            return@lambda_5 Unit.INSTANCE
         }
      })
   }

   public open fun getParsedText(): String {
      return StringsKt.replace(this.getText(), "{ping}", java.lang.String.valueOf(RangesKt.coerceAtLeast(MCClient.getPlayerPing(), 0)), true)
   }

   public open fun shouldStopRenderExecution(): Boolean {
      return !this.showHud
   }

   public fun getPingText(uuid: UUID): Component {
      val ping: Int = RangesKt.coerceAtLeast(MCClient.getPlayerPingByUuid(uuid), 0)
      val color: Int = this.getPingColor(ping)
      val var6: LiteralTextBuilder = LiteralTextBuilder(null, true)
      var6.getAppendTasks().add(PingHud$getPingText$lambda$7$$inlined$text$default$1(var6, "$pingms", true, color))
      return var6.build() as Component
   }

   public open fun hudComponent(): UIComponent? {
      val flow: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      val bg: LabelComponent = UIComponents.label(TextKt.getLiteral("") as Component)
      bg.shadow(true)
      bg.setAutoTextSupplier(
         { 
            val color: Int = if (INSTANCE.usePingColor)
               INSTANCE.getPingColor(MCClient.getPlayerPing())
               else
               OwoLibExtensions.INSTANCE.toJavaColor(INSTANCE.getMultiColor().getColor()).getRGB()
               val pos: Point2D = AnchorPointPosition.toGlobalPos$default(INSTANCE.getAnchorPosition(), 0, 0, 3, null)
            RainbowTextUtilsKt.rainbowText(
               INSTANCE.getParsedText(),
               INSTANCE.getMultiColor().isRainbow(),
               INSTANCE.getMultiColor().isPositionalRainbow(),
               color,
               (int)pos.getX(),
               (int)pos.getY()
            ) as Component
         }
      )
      flow.child(bg as UIComponent)
      flow.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      val var6: DynamicBackground = this.getDynamicBackground()
      if (var6.isDynamic()) {
         flow.sizing(Sizing.Companion.content(var6.getDynamicWidth()), Sizing.Companion.content(var6.getDynamicHeight()))
      } else {
         flow.sizing(Sizing.Companion.fixed(var6.getStaticWidth()), Sizing.Companion.fixed(var6.getStaticHeight()))
      }

      flow.surface(this.getBackground().toSurface())
      return flow.id(this.getName())
   }

   private fun interpolateColor(value: Int, min: Int, max: Int, startColor: Int, endColor: Int): Int {
      val ratio: Float = RangesKt.coerceIn((float)(value - min) / (float)(max - min), 0.0F, 1.0F)
      return (int)((1 - ratio) * (startColor shr 16 and 255) + ratio * (endColor shr 16 and 255)) shl 16 or (int)(
         (1 - ratio) * (startColor shr 8 and 255) + ratio * (endColor shr 8 and 255)
      ) shl 8 or (int)((1 - ratio) * (startColor and 255) + ratio * (endColor and 255))
   }

   public fun getPingColor(ping: Int): Int {
      return if (ping < 30)
         65280
         else
         (
            if (ping < 60)
               this.interpolateColor(ping, 30, 60, 65280, 56320)
               else
               (
                  if (ping < 80)
                     this.interpolateColor(ping, 60, 80, 56320, 16776960)
                     else
                     (if (ping < 100) this.interpolateColor(ping, 80, 100, 16776960, 16753920) else this.interpolateColor(ping, 100, 200, 16753920, 16711680))
               )
         )
      }

   @JvmStatic
   fun {
      INSTANCE.registerEvents()
   }
}
