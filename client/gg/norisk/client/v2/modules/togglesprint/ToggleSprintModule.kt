package gg.norisk.client.v2.modules.togglesprint

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.blaze3d.platform.InputConstants.Key
import com.mojang.blaze3d.platform.InputConstants.Type
import gg.norisk.compat.client.MCLogger
import gg.norisk.compat.client.MCLoggerKt
import gg.norisk.compat.event.ClientEvents
import gg.norisk.compat.event.KeyEventData
import gg.norisk.compat.event.KeyEvents
import gg.norisk.compat.event.MouseClickEventData
import gg.norisk.compat.event.MouseEvents
import gg.norisk.compat.event.MouseScrollEventData
import gg.norisk.compat.input.InputModifiers
import gg.norisk.compat.mixin.input.KeyBindingPressedAccessor
import gg.norisk.compat.mixin.input.KeyMappingAccessor
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
import gg.norisk.ui.api.hud.SingleTextHud
import gg.norisk.ui.api.value.BooleanValue
import gg.norisk.ui.api.value.NumericValue
import gg.norisk.ui.api.value.ValueApiKt
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.modules.IModuleScreen
import gg.norisk.ui.utils.OwoLibExtensions
import gg.norisk.ui.v2.hud.AnchorPointPosition
import java.awt.geom.Point2D
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.ToggleKeyMapping
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@SourceDebugExtension(["SMAP\nToggleSprintModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ToggleSprintModule.kt\ngg/norisk/client/v2/modules/togglesprint/ToggleSprintModule\n+ 2 MCClient.kt\ngg/norisk/compat/client/MCClient\n*L\n1#1,409:1\n40#2:410\n40#2:411\n40#2:412\n40#2:413\n40#2:414\n185#2:415\n40#2:416\n185#2:417\n40#2:418\n328#2:419\n40#2:420\n328#2:421\n40#2:422\n328#2:423\n40#2:424\n185#2:425\n40#2:426\n328#2:427\n40#2:428\n*S KotlinDebug\n*F\n+ 1 ToggleSprintModule.kt\ngg/norisk/client/v2/modules/togglesprint/ToggleSprintModule\n*L\n208#1:410\n219#1:411\n234#1:412\n271#1:413\n284#1:414\n340#1:415\n340#1:416\n355#1:417\n355#1:418\n357#1:419\n357#1:420\n114#1:421\n114#1:422\n121#1:423\n121#1:424\n140#1:425\n140#1:426\n187#1:427\n187#1:428\n*E\n"])
public object ToggleSprintModule : SingleTextHud("ToggleSprint", "{status}", null, null, false, false, false, 124) {
   private final val debugLogger: Logger = MCLogger.getLogger("Voidrix-ToggleSprint")
   private const val DEBUG_CATEGORY: String = "togglesprint"
   private const val REPRESS_BURST_TICKS: Int = 10
   private final var repressTicksRemaining: Int
   private final var sprintKeyPhysicallyHeldCached: Boolean

   public final var isSprintToggled: Boolean
      private set

   @Category(name = "Fly Speed")
   @NotNull
   public final val enableFlyBoost: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return enableFlyBoost$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   @Category(name = "Fly Speed")
   @NotNull
   public final var flyBoost: Number
      public final get() {
         return flyBoost$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Number
      }

      public final set(<set-?>) {
         flyBoost$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Fly Speed")
   @NotNull
   public final val changeBoostWithScroll: Boolean
      public final get() {
         return changeBoostWithScroll$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   @Category(name = "Fly Speed")
   @NotNull
   public final val flyBoostScrollIncrement: Number
      public final get() {
         return flyBoostScrollIncrement$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }


   @Category(name = "Display")
   @NotNull
   public final val showInHud: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return showInHud$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   public open fun getParsedText(): String {
      return this.getHudInfoText()
   }

   public open fun hudComponent(): UIComponent? {
      if (!this.showInHud) {
         return null
      } else {
         val wrapper: FlowLayout = UIContainers.horizontalFlow(
            Sizing.Companion.content(this.getDynamicBackground().getDynamicWidth()), Sizing.Companion.content(this.getDynamicBackground().getDynamicHeight())
         )
         wrapper.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
         wrapper.surface(this.getBackground().toSurface())
         wrapper.allowOverflow(true)
         val var3: LabelComponent = UIComponents.label(TextKt.getLiteral("") as Component)
         var3.shadow(true)
         var3.setAutoTextSupplier(
            { 
               val pos: Point2D = AnchorPointPosition.toGlobalPos$default(INSTANCE.getAnchorPosition(), 0, 0, 3, null)
               RainbowTextUtilsKt.rainbowText(
                  INSTANCE.getParsedText(),
                  INSTANCE.getMultiColor().isRainbow(),
                  INSTANCE.getMultiColor().isPositionalRainbow(),
                  OwoLibExtensions.INSTANCE.toJavaColor(INSTANCE.getMultiColor().getColor()).getRGB(),
                  (int)pos.getX(),
                  (int)pos.getY()
               ) as Component
            }
         )
         wrapper.child(var3 as UIComponent)
         return wrapper.id(this.getName())
      }
   }

   public open fun shouldStopRenderExecution(): Boolean {
      return StringsKt.isBlank(this.getHudInfoText())
   }

   public open fun onEnable() {
      isSprintToggled = true

      try {
         val var10000: Minecraft = Minecraft.getInstance()
         var10000.options.toggleSprint().set(true)
      } catch (var2: Exception) {
      }

      this.pressSprintKey()
   }

   public open fun onDisable() {
      isSprintToggled = false

      try {
         val var10000: Minecraft = Minecraft.getInstance()
         var10000.options.toggleSprint().set(false)
      } catch (var3: Exception) {
      }

      try {
         val var4: KeyMapping = this.getSprintKey()
         (var4 as KeyBindingPressedAccessor).nrc$setPressed(false)
      } catch (var2: Exception) {
      }
   }

   private fun getSprintKey(): KeyMapping {
      val var10000: Minecraft = Minecraft.getInstance()
      val var2: KeyMapping = var10000.options.keySprint
      return var2
   }

   private fun sprintKeyMatchesMouseButton(button: Int): Boolean {
      val var10000: KeyMapping = this.getSprintKey()
      val key: Key = (var10000 as KeyMappingAccessor).nrc$getKey()
      return key.getType() === Type.MOUSE && key.getValue() == button
   }

   private fun toggleSprintFromUserInput(source: String) {
      isSprintToggled = !isSprintToggled
      val var10000: KeyMapping = this.getSprintKey()
      (var10000 as KeyBindingPressedAccessor).nrc$setPressed(isSprintToggled)
      MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "$source toggle: isSprintToggled=${isSprintToggled}")
   }

   private fun pressSprintKey() {
      if (this.isEnabled() && isSprintToggled) {
         val var10000: KeyMapping = this.getSprintKey()
         (var10000 as KeyBindingPressedAccessor).nrc$setPressed(true)
         MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "pressSprintKey: forced true")
      }
   }

   @JvmStatic
   public fun handleStickyPress(stickyKeyBinding: ToggleKeyMapping, pressed: Boolean, ci: CallbackInfo) {
      if (INSTANCE.isEnabled()) {
         val var10000: Minecraft = Minecraft.getInstance()
         if (stickyKeyBinding === var10000.options.keySprint) {
            ci.cancel()
         }
      }
   }

   @JvmStatic
   public fun handleStickyRelease(stickyKeyBinding: ToggleKeyMapping, ci: CallbackInfo) {
      if (INSTANCE.isEnabled() && isSprintToggled) {
         val var10000: Minecraft = Minecraft.getInstance()
         if (stickyKeyBinding === var10000.options.keySprint) {
            ci.cancel()
         }
      }
   }

   @JvmStatic
   public fun onToggleSprintOptionChanged(value: Boolean) {
      INSTANCE.setEnabled(value)
   }

   @JvmStatic
   public fun shouldPreventSprintRelease(keyCode: Int): Boolean {
      return false
   }

   private fun updateSprintKeyCache() {
      val sprintKey: KeyMapping = this.getSprintKey()
      val key: Key = (sprintKey as KeyMappingAccessor).nrc$getKey()
      if (key == InputConstants.UNKNOWN) {
         sprintKeyPhysicallyHeldCached = false
      } else {
         val var10000: Type = key.getType()
var var3: Boolean
         when (if (var10000 == null) -1 else ToggleSprintModule.WhenMappings.$EnumSwitchMapping$0[var10000.ordinal()]) {
            1 -> var3 = InputModifiers.isKeyDown(key.getValue())
            2 -> var3 = InputModifiers.isMouseButtonDown(key.getValue())
            else -> var3 = false
         }

         sprintKeyPhysicallyHeldCached = var3
      }
   }

   private fun isSprintKeyPhysicallyHeld(): Boolean {
      return sprintKeyPhysicallyHeldCached
   }

   @JvmStatic
   public fun getFlySpeed(original: Float): Float {
      if (INSTANCE.isEnabled() && INSTANCE.enableFlyBoost) {
         val var10000: Minecraft = Minecraft.getInstance()
         if (var10000.player == null) {
            return original
         } else {
            return if (var10000.player.isCreative() && INSTANCE.isSprintKeyPhysicallyHeld()) original * INSTANCE.flyBoost.floatValue() else original
         }
      } else {
         return original
      }
   }

   private fun getHudInfoText(): String {
      var var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player == null) {
         return "[No Player]"
      } else {
         val player: LocalPlayer = var10000.player
         var10000 = Minecraft.getInstance()
         val screen: Screen = var10000.gui.screen()
         if (screen is IModuleScreen) {
            return if (this.isEnabled()) "[Sprinting (Toggled)]" else "[Toggle Sprint: Off]"
         } else if (screen == null) {
            if (player.isPassenger()) {
               return "[Riding]"
            } else if (player.isCrouching()) {
               return "[Sneaking (Key Held)]"
            } else {
               val var7: Boolean = this.isSprintKeyPhysicallyHeld()
               if (player.getAbilities().flying) {
                  if (this.isEnabled() && this.enableFlyBoost && player.isCreative() && var7) {
                     val var10: Array<Any> = arrayOf(this.flyBoost)
                     val var13: java.lang.String = java.lang.String.format("[Flying (%.2fx boost)]", Arrays.copyOf(var10, var10.length))
                     return var13
                  } else {
                     return "[Flying]"
                  }
               } else if (player.isSprinting()) {
                  if (var7) {
                     return "[Sprinting (Held)]"
                  } else {
                     return if (this.isEnabled() && isSprintToggled) "[Sprinting (Toggled)]" else "[Sprinting (Vanilla)]"
                  }
               } else if (var7) {
                  return "[Sprinting (Held)]"
               } else {
                  return if (this.isEnabled() && isSprintToggled) "[Sprinting (Toggled)]" else " "
               }
            }
         } else {
            return if (this.isEnabled() && isSprintToggled) "[Sprinting (Toggled)]" else " "
         }
      }
   }

   @JvmStatic
   fun {
      var var3: NumericValue = ValueApiKt.numeric$default(3.0, RangesKt.rangeTo(1.0, 8.0) as ClosedRange, 1.0, null, null, null, 56, null)
      var3.setUiCondition({ 
         INSTANCE.enableFlyBoost
      })
      flyBoost$delegate = var3.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      val var4: BooleanValue = ValueApiKt.boolean$default(true, null, null, null, 14, null)
      var4.setUiCondition({ 
         INSTANCE.enableFlyBoost
      })
      changeBoostWithScroll$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      var3 = ValueApiKt.numeric$default(0.1, RangesKt.rangeTo(0.0, 3.0) as ClosedRange, 0.01, null, null, null, 56, null)
      var3.setUiCondition({ 
         INSTANCE.enableFlyBoost && INSTANCE.changeBoostWithScroll
      })
      flyBoostScrollIncrement$delegate = var3.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_8@{ event: KeyEventData ->
         if (INSTANCE.isEnabled() && event.isClicked()) {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() != null) {
               return@lambda_8 Unit.INSTANCE
            } else {
               if (event.matchesKeyBinding(INSTANCE.getSprintKey())) {
                  INSTANCE.toggleSprintFromUserInput("keyEvent")
               }

               return@lambda_8 Unit.INSTANCE
            }
         } else {
            return@lambda_8 Unit.INSTANCE
         }
      })
      MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_9@{ event: MouseClickEventData ->
         if (INSTANCE.isEnabled() && event.getAction() == 1) {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() != null) {
               return@lambda_9 Unit.INSTANCE
            } else {
               if (INSTANCE.sprintKeyMatchesMouseButton(event.getButton())) {
                  INSTANCE.toggleSprintFromUserInput("mouseClickEvent")
               }

               return@lambda_9 Unit.INSTANCE
            }
         } else {
            return@lambda_9 Unit.INSTANCE
         }
      })
      MouseEvents.INSTANCE
         .getMouseScrollEvent()
         .listen(
            lambda_10@{ event: MouseScrollEventData ->
               if (INSTANCE.isEnabled() && INSTANCE.enableFlyBoost && INSTANCE.changeBoostWithScroll) {
                  val var10000: Minecraft = Minecraft.getInstance()
                  if (var10000.player == null) {
                     return@lambda_10 Unit.INSTANCE
                  } else {
                     if (var10000.player.isCreative() && INSTANCE.isSprintKeyPhysicallyHeld()) {
                        val var7: Double = INSTANCE.flyBoostScrollIncrement.doubleValue()
                        INSTANCE.flyBoost = if (event.getVertical() > 0.0)
                           RangesKt.coerceIn(INSTANCE.flyBoost.doubleValue() + var7, 1.0, 8.0) as java.lang.Number
                           else
                           RangesKt.coerceIn(INSTANCE.flyBoost.doubleValue() - var7, 1.0, 8.0) as java.lang.Number
                        }

                     return@lambda_10 Unit.INSTANCE
                  }
               } else {
                  return@lambda_10 Unit.INSTANCE
               }
            }
         )
         ClientEvents.INSTANCE.getJoinEvent().listen({ it: Unit ->
         MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "joinEvent fired, enabled=${INSTANCE.isEnabled()}")
         if (INSTANCE.isEnabled()) {
            repressTicksRemaining = 10
            INSTANCE.pressSprintKey()
         }

         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getRespawnEvent().listen({ it: Unit ->
         MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "respawnEvent fired, enabled=${INSTANCE.isEnabled()}")
         if (INSTANCE.isEnabled()) {
            repressTicksRemaining = 10
            INSTANCE.pressSprintKey()
         }

         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getDeathEvent().listen({ it: Unit ->
         MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "deathEvent fired, enabled=${INSTANCE.isEnabled()}")
         Unit.INSTANCE
      })
      ClientEvents.INSTANCE.getClientTickEvent().listen(lambda_14@{ it: Unit ->
         if (INSTANCE.isEnabled()) {
            INSTANCE.updateSprintKeyCache()
         }

         if (INSTANCE.isEnabled() && isSprintToggled) {
            val var10000: Minecraft = Minecraft.getInstance()
            if (var10000.gui.screen() == null) {
               val var4: KeyMapping = INSTANCE.getSprintKey()
               if (!(var4 as KeyBindingPressedAccessor).nrc$isPressed()) {
                  MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "continuous enforcement: restoring sprint key")
                  INSTANCE.pressSprintKey()
               }
            }
         }

         if (INSTANCE.isEnabled() && repressTicksRemaining > 0) {
            INSTANCE.pressSprintKey()
            repressTicksRemaining += -1
            if (repressTicksRemaining == 0) {
               MCLoggerKt.nrcDebugLog(debugLogger, "togglesprint", "repress burst complete")
            }

            return@lambda_14 Unit.INSTANCE
         } else {
            return@lambda_14 Unit.INSTANCE
         }
      })
   }

   public interface StickKeyBindingExt {
      public var isNRCPressed: Boolean
         internal final set
   }
}
