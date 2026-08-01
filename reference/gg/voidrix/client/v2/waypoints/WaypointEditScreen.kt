package gg.voidrix.client.v2.waypoints

import gg.voidrix.client.v2.waypoints.source.VoidrixWaypointSource
import gg.voidrix.compat.input.InputModifiers
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.resource.MCKeyType
import gg.voidrix.compat.scale.IVoidrixScreen
import gg.voidrix.compat.waypoint.source.SourceWaypoint
import gg.voidrix.compat.waypoint.source.WaypointSource
import gg.voidrix.owolib.owo.ui.base.BaseOwoScreen
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.OwoUIAdapter
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.input.KeyEvent
import gg.voidrix.owolib.owo.ui.util.FocusHandler
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixInputField
import gg.voidrix.ui.screen.IAutoFocusSearchScreen
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen

@SourceDebugExtension(["SMAP\nWaypointEditScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointEditScreen.kt\ngg/voidrix/client/v2/waypoints/WaypointEditScreen\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,149:1\n355#2:150\n40#2:151\n356#2:152\n328#2:153\n40#2:154\n355#2:155\n40#2:156\n356#2:157\n328#2:158\n40#2:159\n*S KotlinDebug\n*F\n+ 1 WaypointEditScreen.kt\ngg/voidrix/client/v2/waypoints/WaypointEditScreen\n*L\n126#1:150\n126#1:151\n126#1:152\n83#1:153\n83#1:154\n85#1:155\n85#1:156\n85#1:157\n87#1:158\n87#1:159\n*E\n"])
public class WaypointEditScreen(existing: SourceWaypoint? = null,
      dimensionKey: String? = null,
      targetSource: WaypointSource = VoidrixWaypointSource.INSTANCE as WaypointSource,
      parentScreen: Screen? = null
   ) : BaseOwoScreen(null, 1),
   IVoidrixScreen,
   IAutoFocusSearchScreen {
   private final val existing: SourceWaypoint?
   private final val dimensionKey: String?
   private final val targetSource: WaypointSource
   private final val parentScreen: Screen?
   private final var editComponent: WaypointEditComponent?
   private final val openKey: MCKey
   private final var openKeyWasReleased: Boolean

   init {
      this.existing = existing
      this.dimensionKey = dimensionKey
      this.targetSource = targetSource
      this.parentScreen = parentScreen
      this.openKey = WaypointModule.INSTANCE.createWaypointKey
      this.openKeyWasReleased = this.openKey.getType() != MCKeyType.KEYBOARD
   }

   protected open fun beforeRender(graphics: OwoUIGraphics) {
      if (!this.openKeyWasReleased && !InputModifiers.isKeyDown(this.openKey.getCode())) {
         this.openKeyWasReleased = true
      }
   }

   public open val autoFocusSearchField: VoidrixInputField?
      public open get() {
         return if (this.openKeyWasReleased) (if (this.editComponent != null) this.editComponent.nameField else null) else null
      }


   protected open fun owoKeyPressed(input: KeyEvent): Boolean {
      val ec: WaypointEditComponent = this.editComponent
      if (this.editComponent != null && input.getKey() == KeyEvent.Companion.getKEY_ENTER() && !StringsKt.isBlank(ec.nameField.getText())) {
         var var6: UIComponent
         run label42@{
            val var10000: OwoUIAdapter = this.getUiAdapterOrNull()
            if (var10000 != null) {
               val var4: FlowLayout = var10000.rootComponent as FlowLayout
               if (var10000.rootComponent as FlowLayout != null) {
                  val var5: FocusHandler = var4.focusHandler()
                  if (var5 != null) {
                     var6 = var5.focused()
                     return@label42
                  }
               }
            }

            var6 = null
         }

         if ((var6 == null || var6 is VoidrixInputField) && ec.confirm()) {
            UISounds.playButtonSound()
            this.onClose()
            return true
         }
      }

      return super.owoKeyPressed(input)
   }

   protected open fun createAdapter(): OwoUIAdapter<FlowLayout> {
      return OwoUIAdapter.Companion.create(this as Screen, { hSizing: Sizing, vSizing: Sizing ->
         FlowLayout.Companion.vertical(hSizing, vSizing)
      })
   }

   protected open fun build(rootComponent: FlowLayout) {
      var var10000: java.lang.String = this.dimensionKey
      if (this.dimensionKey == null) {
         var10000 = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
         if (var10000 == null) {
            var10000 = "overworld"
         }
      }

      this.editComponent = WaypointEditComponent.Companion.openScreenDialog(rootComponent, this.existing, this.targetSource, var10000, { 
         var var10000: Minecraft = Minecraft.getInstance()
         if (var10000.gui.screen() is WaypointEditScreen) {
            if (`this$0`.parentScreen != null) {
               val var4: Screen = `this$0`.parentScreen
               var10000 = Minecraft.getInstance()
               var10000.gui.setScreen(var4)
            } else {
               var10000 = Minecraft.getInstance()
               val var10: Screen = var10000.gui.screen()
               if (var10 != null) {
                  var10.onClose()
               }
            }
         }

         Unit.INSTANCE
      })
   }

   public open fun onClose() {
      if (this.parentScreen != null) {
         val `screen$iv`: Screen = this.parentScreen
         val var10000: Minecraft = Minecraft.getInstance()
         var10000.gui.setScreen(`screen$iv`)
      } else {
         super.onClose()
      }
   }

   public open fun isPauseScreen(): Boolean {
      return false
   }

   fun WaypointEditScreen() {
      this(null, null, null, null, 15, null)
   }
}
