package gg.norisk.client.v2.modules.scoreboard

import gg.norisk.client.v2.mixin.scoreboard.GuiScoreboardAccessor
import gg.norisk.client.v2.modules.impl.ClearBackgroundModule
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.text.NrcTextShaderCache
import gg.norisk.compat.text.NrcTextShaderContext
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.AbstractHud
import gg.norisk.ui.api.hud.AnchorPoint
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.IContentBackground
import gg.norisk.ui.api.hud.IDynamicBackground
import gg.norisk.ui.api.hud.IDynamicBackgroundKt
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.v2.hud.AnchorPointPosition
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.numbers.NumberFormat
import net.minecraft.network.chat.numbers.StyledFormat
import net.minecraft.world.scores.DisplaySlot
import net.minecraft.world.scores.Objective
import net.minecraft.world.scores.Scoreboard
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType
import org.jetbrains.annotations.NotNull

@SourceDebugExtension(["SMAP\nScoreboardModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ScoreboardModule.kt\ngg/norisk/client/v2/modules/scoreboard/ScoreboardModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n*L\n1#1,443:1\n328#2:444\n40#2:445\n127#2:446\n40#2:447\n941#3:448\n*S KotlinDebug\n*F\n+ 1 ScoreboardModule.kt\ngg/norisk/client/v2/modules/scoreboard/ScoreboardModule\n*L\n104#1:444\n104#1:445\n197#1:446\n197#1:447\n211#1:448\n*E\n"])
public object ScoreboardModule : AbstractHud("Scoreboard", false, true, true, 2), IContentBackground {
   @Category(name = "Settings")
   @NotNull
   public final var showNumbers: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return showNumbers$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showNumbers$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public final var fontShadow: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return fontShadow$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         fontShadow$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var hideScoreboard: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return hideScoreboard$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         hideScoreboard$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   @Category(name = "Display")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   @JvmStatic
   public final var lastObjective: Objective?

   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   public open fun hudComponent(): UIComponent? {
      return ScoreboardModule.ScoreboardComponent() as UIComponent
   }

   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.MIDDLE_RIGHT, 1.0, 0.27, null, 8, null)
         }
      }


   public open fun useGuiScale(): Boolean {
      return true
   }

   public open fun shouldStopRenderExecution(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      return var10000.gui.screen() !is IModuleScreen && !this.isScoreboardProvidedByVanilla()
   }

   private fun isScoreboardProvidedByVanilla(): Boolean {
      val var10000: ClientLevel = Minecraft.getInstance().level
      if (var10000 != null) {
         val var3: Scoreboard = var10000.getScoreboard()
         if (var3 != null) {
            val var4: Objective = var3.getDisplayObjective(DisplaySlot.SIDEBAR)
            if (var4 == null) {
               return false
            }

            return var4.getScoreboard().listPlayerScores(var4).stream().anyMatch({ p0: Any ->
               `$tmp0`(p0)
            })
         }
      }

      return false
   }

   private fun collectEntries(objective: Any): List<gg.norisk.client.v2.modules.scoreboard.ScoreboardModule.SidebarEntry> {
      val var10000: Minecraft = Minecraft.getInstance()
      val var9: Font = var10000.font
      val var7: Objective = objective as Objective
      val var8: Scoreboard = (objective as Objective).getScoreboard()
      val numberFormat: NumberFormat = (objective as Objective).numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT as NumberFormat)
      val var6: java.util.List = var8.listPlayerScores(var7).stream().filter({ p0: Any ->
         `$tmp0`(p0)
      }).sorted(GuiScoreboardAccessor.getScoreDisplayOrder()).limit(15L).map({ p0: Any ->
         `$tmp0`(p0) as ScoreboardModule.SidebarEntry
      }).toList()
      return var6
   }

   private fun getTitle(objective: Any): Component {
      val var10000: Component = (objective as Objective).getDisplayName()
      return var10000
   }

   private fun dummyObjective(): Any {
      val dummyScoreboard: Scoreboard = Scoreboard()
      val dummyObjective: Objective = Objective(
         dummyScoreboard, "SCOREBOARD", ObjectiveCriteria.DUMMY, TextKt.getLiteral("SCOREBOARD") as Component, RenderType.INTEGER, false, null
      )

      for (i in 1..10) {
         dummyScoreboard.getOrCreatePlayerScore({ 
            "$`$i`. Linie"
         }, dummyObjective).set(i)
      }

      return dummyObjective
   }

   @JvmStatic
   fun {
      ClientEvents.INSTANCE.getDisconnectEvent().listen({ it: Unit ->
         lastObjective = null
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
         lastObjective = null
         Unit.INSTANCE
      })
   }

   @SourceDebugExtension(["SMAP\nScoreboardModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ScoreboardModule.kt\ngg/norisk/client/v2/modules/scoreboard/ScoreboardModule$ScoreboardComponent\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClientKt\n*L\n1#1,443:1\n127#2:444\n40#2:445\n328#2:449\n40#2:450\n127#2:451\n40#2:452\n942#3:446\n941#3:447\n942#3:448\n942#3:453\n941#3:454\n942#3:455\n*S KotlinDebug\n*F\n+ 1 ScoreboardModule.kt\ngg/norisk/client/v2/modules/scoreboard/ScoreboardModule$ScoreboardComponent\n*L\n346#1:444\n346#1:445\n364#1:449\n364#1:450\n378#1:451\n378#1:452\n349#1:446\n351#1:447\n354#1:448\n381#1:453\n383#1:454\n386#1:455\n*E\n"])
   public class ScoreboardComponent : FlowLayout(
         Sizing.Companion.content(ScoreboardModule.INSTANCE.dynamicBackground.getDynamicWidth()),
         Sizing.Companion.content(ScoreboardModule.INSTANCE.dynamicBackground.getDynamicHeight()),
         Algorithm.VERTICAL
      ) {
      private final val innerComponent: FlowLayout

      private fun setWidthAndHeight() {
         var var10000: Any = lastObjective
         if (var10000 == null) {
            var10000 = ScoreboardModule.INSTANCE.dummyObjective()
         }

         val var23: Minecraft = Minecraft.getInstance()
         val var24: Font = var23.font
         val textRenderer: Font = var24
         val var13: java.util.List = ScoreboardModule.INSTANCE.collectEntries(var10000)
         var var15: Int = var24.width(ScoreboardModule.INSTANCE.getTitle(var10000) as FormattedText)
         val var16: Int = var24.width(": ")

         for (var19 in var13) {
            var15 = Math.max(var15, textRenderer.width(var19.name as FormattedText) + (if (var19.scoreWidth > 0) var16 + var19.scoreWidth else 0))
         }

         this.innerComponent.sizing(Sizing.Companion.fixed(var15 + 3 + 3), Sizing.Companion.fixed(var13.size() * 9 + 10))
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         var objective: Any = lastObjective
         var var10000: Minecraft = Minecraft.getInstance()
         val inEditor: Boolean = var10000.gui.screen() is IModuleScreen
         if (objective == null && inEditor) {
            objective = ScoreboardModule.INSTANCE.dummyObjective()
         }

         if (objective != null && (!ScoreboardModule.INSTANCE.hideScoreboard || inEditor)) {
            if (ClearBackgroundModule.INSTANCE.isEnabled() && ClearBackgroundModule.INSTANCE.scoreboard) {
               this.surface(Surface.BLANK)
            } else {
               this.surface(ScoreboardModule.INSTANCE.getBackground().toSurface())
            }

            var10000 = Minecraft.getInstance()
            val var46: Font = var10000.font
            val var30: Font = var46
            val var32: java.util.List = ScoreboardModule.INSTANCE.collectEntries(objective)
            val var33: Component = ScoreboardModule.INSTANCE.getTitle(objective)
            val titleWidth: Int = var46.width(var33 as FormattedText)
            var var34: Int = titleWidth
            val var35: Int = var46.width(": ")

            for (var38 in var32) {
               var34 = Math.max(var34, var30.width(var38.name as FormattedText) + (if (var38.scoreWidth > 0) var35 + var38.scoreWidth else 0))
            }

            val var37: Int = var34 + 3 + 3
            val var39: Int = var32.size()
            this.innerComponent.sizing(Sizing.Companion.fixed(var37 + 0), Sizing.Companion.fixed(var39 * 9 + 10 + 0))
            val var44: Double = this.innerComponent.y() + 10 + 0 / 2
            val originX: Double = this.innerComponent.x() + 3 + 0 / 2
            val sidebarRight: Double = originX + var37 + 2 - 3 - 3
            super.draw(context, mouseX, mouseY, partialTicks, delta)
            NrcTextShaderCache.setCurrentContext(NrcTextShaderContext.SCOREBOARD)

            try {
               context.drawString(var30, var33, originX + (double)(var37 / 2) - (double)(titleWidth / 2), var44 - (double)9, -1, false)

               repeat(var39) { t ->
                  val entry: ScoreboardModule.SidebarEntry = var32.get(t) as ScoreboardModule.SidebarEntry
                  val entryY: Double = var44 + t * 9
                  context.drawString(var30, entry.name, originX, var44 + (double)(t * 9), -1, ScoreboardModule.INSTANCE.fontShadow)
                  if (ScoreboardModule.INSTANCE.showNumbers) {
                     context.drawString(var30, entry.scoreText, sidebarRight - (double)entry.scoreWidth, entryY, -1, ScoreboardModule.INSTANCE.fontShadow)
                  }
               }
            } finally {
               NrcTextShaderCache.setCurrentContext(null)
            }
         } else {
            this.surface(Surface.BLANK)
         }
      }
   }

   private data class SidebarEntry(name: Component, scoreText: String, scoreWidth: Int) {
      public final val name: Component
      public final val scoreText: String
      public final val scoreWidth: Int

      init {
         this.name = name
         this.scoreText = scoreText
         this.scoreWidth = scoreWidth
      }

      public operator fun component1(): Component {
         return this.name
      }

      public operator fun component2(): String {
         return this.scoreText
      }

      public operator fun component3(): Int {
         return this.scoreWidth
      }

      public fun copy(name: Component = this.name, scoreText: String = this.scoreText, scoreWidth: Int = this.scoreWidth): gg.norisk.client.v2.modules.scoreboard.ScoreboardModule.SidebarEntry {
         return ScoreboardModule.SidebarEntry(name, scoreText, scoreWidth)
      }

      public override fun toString(): String {
         return "SidebarEntry(name=${this.name}, scoreText=${this.scoreText}, scoreWidth=${this.scoreWidth})"
      }

      public override fun hashCode(): Int {
         return (this.name.hashCode() * 31 + this.scoreText.hashCode()) * 31 + Integer.hashCode(this.scoreWidth)
      }

      public override operator fun equals(other: Any?): Boolean {
         label34@
         if (this === other) {
            return true
         } else {
            return other is ScoreboardModule.SidebarEntry
               && this.name == (other as ScoreboardModule.SidebarEntry).name
               && this.scoreText == (other as ScoreboardModule.SidebarEntry).scoreText
               && this.scoreWidth == (other as ScoreboardModule.SidebarEntry).scoreWidth
            }
      }
   }
}
