package gg.norisk.client.v2.modules.potion

import com.mojang.blaze3d.platform.Window
import gg.norisk.client.v2.mixin.potion.InGameHudAccessor
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.PeriodicLogManager
import gg.norisk.compat.scale.IWindowScaleExt
import gg.norisk.compat.scale.ScaledResolution
import gg.norisk.compat.scale.WindowScaleManager
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.owolib.owo.ui.core.VerticalAlignment
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.hud.AbstractHud
import gg.norisk.ui.api.hud.Alignment
import gg.norisk.ui.api.hud.AnchorPoint
import gg.norisk.ui.api.hud.DynamicBackground
import gg.norisk.ui.api.hud.IContentBackground
import gg.norisk.ui.api.hud.IDynamicBackground
import gg.norisk.ui.api.hud.IDynamicBackgroundKt
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.components.nrc.NrcBackgroundPicker
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.v2.hud.AnchorPointPosition
import gg.norisk.ui.v2.hud.Background
import gg.norisk.ui.v2.hud.Background.Type
import java.util.ArrayList
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.Hud
import net.minecraft.client.resources.language.I18n
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nPotionStatus.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PotionStatus.kt\ngg/norisk/client/v2/modules/potion/PotionStatus\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,422:1\n185#2:423\n40#2:424\n328#2:428\n40#2:429\n40#2:430\n774#3:425\n865#3,2:426\n*S KotlinDebug\n*F\n+ 1 PotionStatus.kt\ngg/norisk/client/v2/modules/potion/PotionStatus\n*L\n94#1:423\n94#1:424\n125#1:428\n125#1:429\n130#1:430\n117#1:425\n117#1:426,2\n*E\n"])
public object PotionStatus : AbstractHud("Potion Status", false, true, false, 10), IContentBackground {
   public open var background: Background by ValueApiKt.attribute$default({ 
      Background(Type.BLANK, null, 0.0F, 0.0F, 0.0F, null, 62, null)
   }, null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      NrcBackgroundPicker(INSTANCE.background, null) as UIComponent
   }, 14, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public open get() {
         return background$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as Background
      }

      public open set(<set-?>) {
         background$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "Display")
   @NotNull
   public open var dynamicBackground: DynamicBackground by ValueApiKt.generic$default({ 
      INSTANCE.getDefaultDynamicBackground()
   }, DynamicBackground.Companion.serializer(), "Padding", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      IDynamicBackgroundKt.createDynamicBackgroundSliderWrapper(INSTANCE as IDynamicBackground) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public open get() {
         return dynamicBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as DynamicBackground
      }

      public open set(<set-?>) {
         dynamicBackground$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var showTime: Boolean by ValueApiKt.boolean$default(true, "Show Time", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return showTime$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         showTime$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var hideVanillaPotionStatus: Boolean by ValueApiKt.boolean$default(false, "Hide Vanilla Potion Status", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return hideVanillaPotionStatus$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         hideVanillaPotionStatus$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var drawSprite: Boolean by ValueApiKt.boolean$default(true, "Draw Sprite", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return drawSprite$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         drawSprite$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var dontShiftInventory: Boolean by ValueApiKt.boolean$default(true, "Don't Shift Inventory", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return dontShiftInventory$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         dontShiftInventory$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final var dontShowInInventory: Boolean by ValueApiKt.boolean$default(false, "Don't Show In Inventory", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return dontShowInInventory$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }

      public final set(<set-?>) {
         dontShowInInventory$delegate.setValue(this as ValueHolder, $$delegatedProperties[6], var1)
      }


   public final var potionX: Double?
      internal set

   public final var potionY: Double?
      internal set

   public open val defaultPosition: () -> AnchorPointPosition
      public open get() {
         return { 
            AnchorPointPosition(AnchorPoint.TOP_RIGHT, 0.9906250000000001, 0.025, null, 8, null)
         }
      }


   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   public open fun useGuiScale(): Boolean {
      return true
   }

   public open fun shouldStopRenderExecution(): Boolean {
      return getPotionEffects$default(this, null, 1, null).isEmpty()
   }

   public fun getPotionEffects(vanilla: Collection<MobEffectInstance>? = null): Collection<MobEffectInstance> {
      var var10000: java.util.Collection = vanilla
      if (vanilla == null) {
         val var15: Minecraft = Minecraft.getInstance()
         var10000 = if (var15.player != null) var15.player.getActiveEffects() else null
         if (var10000 == null) {
            var10000 = CollectionsKt.emptyList()
         }
      }

      if (this.isEnabled() && this.isInModuleScreen() && var10000.isEmpty()) {
         var10000 = CollectionsKt.mutableListOf(arrayOf(MobEffectInstance(MobEffects.SPEED, 420, 3)))
      } else {
         val var13: java.lang.Iterable = var10000
         val var14: java.util.Collection = ArrayList()

         for (`element$iv$iv` in var13) {
            if ((`element$iv$iv` as MobEffectInstance).showIcon()) {
               var14.add(`element$iv$iv`)
            }
         }

         var10000 = var14 as java.util.List
      }

      return var10000
   }

   private fun isInModuleScreen(): Boolean {
      val var10000: Minecraft = Minecraft.getInstance()
      return var10000.gui.screen() is IModuleScreen
   }

   public fun drawStatusEffectOverlay(context: GuiGraphicsExtractor, statusEffectInstance: MobEffectInstance, x: Int, y: Int) {
      val var10000: Minecraft = Minecraft.getInstance()
      val var12: java.lang.String = this.getDurationAsString(statusEffectInstance)
      context.text(var10000.font, var12, x + 13 - var10000.font.width(var12) / 2, y + 14, -1711276033)
      val amplifier: Int = statusEffectInstance.getAmplifier()
      if (amplifier > 0) {
         val amplifierString: java.lang.String = if (amplifier < 10) I18n.get("enchantment.level.${amplifier + 1}", arrayOfNulls(0)) else "**"
         context.text(var10000.font, amplifierString, x + 22 - var10000.font.width(amplifierString), y + 3, -1711276033)
      }
   }

   private fun getDurationAsString(effect: MobEffectInstance): String {
      if (effect.isInfiniteDuration()) {
         val var10000: java.lang.String = I18n.get("effect.duration.infinite", arrayOfNulls(0))
         return var10000
      } else {
         val seconds: Int = effect.getDuration() / 20
         return if (seconds >= 3600) "${seconds / 3600}h" else (if (seconds >= 60) "${seconds / 60}m" else java.lang.String.valueOf(seconds))
      }
   }

   public fun getXOffset(constant: Int, isMc: Boolean): Int {
      val alignment: Alignment = this.getAnchorPosition().getAnchor().getAlignmentOfComponent()
      return if (alignment.isLeft()) -constant else (if (alignment.isCenter()) -constant else constant)
   }

   public open fun hudComponent(): UIComponent {
      val wrapper: FlowLayout = UIContainers.horizontalFlow(
         Sizing.Companion.content(this.dynamicBackground.getDynamicWidth()), Sizing.Companion.content(this.dynamicBackground.getDynamicHeight())
      )
      wrapper.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      wrapper.surface(this.background.toSurface())
      wrapper.allowOverflow(true)
      wrapper.child(PotionStatus.PotionStatusComponent(wrapper, null, null, 6, null) as UIComponent)
      return wrapper as UIComponent
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @SourceDebugExtension(["SMAP\nPotionStatus.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PotionStatus.kt\ngg/norisk/client/v2/modules/potion/PotionStatus$PotionStatusComponent\n+ 2 MCLogger.kt\ngg/norisk/compat/client/MCLoggerKt\n+ 3 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,422:1\n189#2,4:423\n194#2:428\n40#3:427\n40#3:429\n40#3:430\n*S KotlinDebug\n*F\n+ 1 PotionStatus.kt\ngg/norisk/client/v2/modules/potion/PotionStatus$PotionStatusComponent\n*L\n246#1:423,4\n246#1:428\n247#1:427\n291#1:429\n292#1:430\n*E\n"])
   public class PotionStatusComponent(wrapper: FlowLayout,
      horizontalSizing: Sizing = Sizing.Companion.fixed(100),
      verticalSizing: Sizing = Sizing.Companion.fixed(100)
   ) : FlowLayout(horizontalSizing, verticalSizing, Algorithm.VERTICAL) {
      public final val wrapper: FlowLayout
      private final val log: Logger

      init {
         this.wrapper = wrapper
         this.allowOverflow(true)
         val var4: Pair = this.getEffectCounts(PotionStatus.getPotionEffects$default(PotionStatus.INSTANCE, null, 1, null))
         this.setWidthAndHeight((var4.component1() as java.lang.Number).intValue(), (var4.component2() as java.lang.Number).intValue())
         this.child(UIContainers.verticalFlow(Sizing.Companion.fixed(1), Sizing.Companion.fixed(1)) as UIComponent)
         this.log = MCLogger.getLogger("PotionStatus")
      }

      public fun setWidthAndHeight(beneficial: Int, harmful: Int) {
         this.horizontalSizing(Sizing.Companion.fixed(25 * Math.max(harmful, beneficial)))
         this.verticalSizing(Sizing.Companion.fixed((if (beneficial > 0) 25 else 0) + (if (harmful > 0) 25 else 0)))
         if (beneficial == 0 && harmful == 0) {
            this.sizing(Sizing.Companion.content())
         }
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         val `$this$nrcDebugEvery_u24default$iv`: Logger = this.log
         if (MCLogger.IS_DEBUG && PeriodicLogManager.INSTANCE.shouldLog("PotionStatus.draw", 2000L)) {
            val var10000: Minecraft = Minecraft.getInstance()
            val window: Window = var10000.getWindow()
            val var22: IWindowScaleExt = window as IWindowScaleExt
            val wsm: WindowScaleManager = WindowScaleManager.INSTANCE
            val var24: Double = var22.getNrc_mcScaleFactor()
            val var10001: Int = window.getGuiScale()
            val var10002: Int = wsm.getCurrentScale()
            val var10003: Double = wsm.getScaleDiff()
            val var10004: ScaledResolution = var22.getNrc_scaledResolution()
            val var30: Int = if (var10004 != null) var10004.getScaleFactor() else null
            val var10005: ScaledResolution = wsm.getScaledResolution()
            `$this$nrcDebugEvery_u24default$iv`.info(
               "PotionStatus DEBUG: nrc_mcScaleFactor=$var24, window.guiScale=$var10001, DEFAULT_SCALE=3, currentScale=$var10002, scaleDiff=$var10003, nrc_scaledResolution=$var30, scaledResolution=${if (var10005
                     != null)
                  var10005.getScaleFactor()
                  else
                  null}"
            )
         }

         val var17: Pair = this.getEffectCounts(PotionStatus.getPotionEffects$default(PotionStatus.INSTANCE, null, 1, null))
         val var18: Int = (var17.component1() as java.lang.Number).intValue()
         val var19: Int = (var17.component2() as java.lang.Number).intValue()
         if (var18 == 0 && var19 == 0) {
            this.wrapper.surface(Surface.BLANK)
            super.draw(context, mouseX, mouseY, partialTicks, delta)
         } else {
            this.wrapper.surface(PotionStatus.INSTANCE.background.toSurface())
            this.setWidthAndHeight(var18, var19)
            val maxEffects: Int = Math.max(var19, var18)
            PotionStatus.INSTANCE.potionX = this.x()
               + (double)(
                  if (!PotionStatus.INSTANCE.getAnchorPosition().getAnchor().getAlignmentOfComponent().isLeft()
                        && !PotionStatus.INSTANCE.getAnchorPosition().getAnchor().getAlignmentOfComponent().isCenter())
                     25 * maxEffects
                     else
                     -(25 * maxEffects) / Math.max(1, maxEffects)
               )
               PotionStatus.INSTANCE.potionY = this.y() + (if (var18 == 0 && var19 > 0) -25.0 else 0.0)

            try {
               val var25: Minecraft = Minecraft.getInstance()
               val var26: Hud = var25.gui.hud
               val var27: InGameHudAccessor = var26 as InGameHudAccessor
               val var28: GuiGraphicsExtractor = context as GuiGraphicsExtractor
               val var29: Minecraft = Minecraft.getInstance()
               var27.invokeRenderEffects(var28, var29.getDeltaTracker())
            } catch (var16: Exception) {
            }

            PotionStatus.INSTANCE.potionX = null
            PotionStatus.INSTANCE.potionY = null
            super.draw(context, mouseX, mouseY, partialTicks, delta)
         }
      }

      public fun getEffectCounts(statusEffects: Collection<MobEffectInstance>): Pair<Int, Int> {
         var beneficialEffectCount: Int = 0
         var harmfulEffectCount: Int = 0

         for (statusEffectInstance in statusEffects) {
            if ((statusEffectInstance.getEffect().value() as MobEffect).isBeneficial()) {
               beneficialEffectCount++
            } else {
               harmfulEffectCount++
            }
         }

         return Pair(beneficialEffectCount, harmfulEffectCount)
      }
   }
}
