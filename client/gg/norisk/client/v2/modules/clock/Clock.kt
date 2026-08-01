package gg.norisk.client.v2.modules.clock

import gg.norisk.compat.text.LiteralTextBuilder
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.UIComponents
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.hud.SingleTextHud
import gg.norisk.ui.components.nrc.NrcCollapsibleContainer
import gg.norisk.ui.theme.ThemeModule
import gg.norisk.ui.theme.ThemeModuleKt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nClock.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Clock.kt\ngg/norisk/client/v2/modules/clock/Clock\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Text.kt\ngg/norisk/compat/text/TextKt\n+ 4 TextBuilder.kt\ngg/norisk/compat/text/TextBuilderKt\n+ 5 TextBuilder.kt\ngg/norisk/compat/text/LiteralTextBuilder\n*L\n1#1,82:1\n1#2:83\n67#3:84\n8#4,4:85\n78#5,6:89\n72#5,4:95\n87#5:99\n*S KotlinDebug\n*F\n+ 1 Clock.kt\ngg/norisk/client/v2/modules/clock/Clock\n*L\n72#1:84\n31#1:85,4\n32#1:89,6\n32#1:95,4\n32#1:99\n*E\n"])
public object Clock : SingleTextHud("Clock", "HH:mm:ss", null, null, false, false, false, 124) {
   public open val seoTags: Array<String>

   public open fun getParsedText(): String {
      val var2: Clock = this

      var `$this$getParsedText_u24lambda_u240`: Any
      try {
         `$this$getParsedText_u24lambda_u240` = Result.constructor_impl/* $VF was: constructor-impl */(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(var2.getText()))
         )
      } catch (var5: java.lang.Throwable) {
         `$this$getParsedText_u24lambda_u240` = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var5))
      }

      var var10000: java.lang.String = (
         if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$getParsedText_u24lambda_u240`)) null else `$this$getParsedText_u24lambda_u240`
      ) as java.lang.String
      if (var10000 == null) {
         var10000 = "Error: Wrong format"
      }

      return var10000
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         val var4: LiteralTextBuilder = LiteralTextBuilder(null, true)
         var4.getAppendTasks().add(Clock$buildCustomUi$lambda$3$lambda$1$$inlined$text$default$1(var4, "Documentation", true))
         var4.setColor(ThemeModule.INSTANCE.getFontColor().getRGB())
         var4.setBold(true)
         val collapsible: NrcCollapsibleContainer = NrcCollapsibleContainer(var4.build() as Component, true, null, null, 12, null)
         val var19: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
            .child(UIComponents.label(TextKt.getLiteral("Examples:") as Component).horizontalTextAlignment(HorizontalAlignment.LEFT) as UIComponent)
            val var22: LocalDateTime = LocalDateTime.now()
         val var23: Clock = INSTANCE
         var23.addFormatExample(var19, "yyyy-MM-dd HH:mm:ss", var22)
         INSTANCE.addFormatExample(var19, "dd.MM.yyyy", var22)
         INSTANCE.addFormatExample(var19, "HH:mm", var22)
         INSTANCE.addFormatExample(var19, "EEEE, MMMM d, yyyy", var22)
         INSTANCE.addFormatExample(var19, "h:mm a", var22)
         INSTANCE.addFormatExample(var19, "dd/MM/yyyy HH:mm:ss.SSS", var22)
         collapsible.child(
            var19.gap(5)
               .horizontalAlignment(HorizontalAlignment.LEFT)
               .padding(Insets.Companion.of(5))
               .surface(Surface.Companion.flat(ThemeModuleKt.withAlpha(ThemeModule.INSTANCE.getBackgroundColor(), 60).getRGB())) as UIComponent
         )
         settingsPanel.child(collapsible as UIComponent)
         Unit.INSTANCE
      }
   }

   private fun addFormatExample(parent: FlowLayout, pattern: String, dateTime: LocalDateTime) {
      var var10001: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
         .child(UIComponents.label(TextKt.getLiteral(pattern) as Component).horizontalSizing(Sizing.Companion.fixed(120)))
         .child(UIComponents.label(TextKt.getLiteral(" -> ") as Component) as UIComponent)
         val var4: Clock = this

      var it: Clock
      try {
         it = var4
         val var10000: java.lang.String = dateTime.format(DateTimeFormatter.ofPattern(pattern))
         it = (Clock)Result.constructor_impl/* $VF was: constructor-impl */(TextKt.getLiteral(var10000))
      } catch (var13: java.lang.Throwable) {
         it = (Clock)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var13))
      }

      var var20: FlowLayout = parent
      var10001 = var10001
      val var10002: Any
      if (Result.exceptionOrNull_impl/* $VF was: exceptionOrNull-impl */(it) == null) {
         var10002 = it
      } else {
         val `args$iv`: Array<Any> = arrayOfNulls(0)
         val var21: MutableComponent = Component.translatable("nrc.clock.label.invalid_format", Arrays.copyOf(`args$iv`, `args$iv`.length))
         var20 = parent
         var10001 = var10001
         var10002 = var21
      }

      var20.child(
         var10001.child(UIComponents.label(var10002 as Component).horizontalSizing(Sizing.Companion.fill(100)))
            .horizontalAlignment(HorizontalAlignment.LEFT)
            .verticalAlignment(VerticalAlignment.CENTER)
            .padding(Insets.Companion.vertical(2)) as UIComponent
      )
   }
}
