package gg.voidrix.client.v2.modules.keystrokes

import gg.voidrix.compat.client.MCSounds
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.scale.IVoidrixScreen
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.TextKt
import gg.voidrix.compat.v2.hud.HudManager
import gg.voidrix.owolib.owo.ui.base.BaseOwoScreen
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.ScrollContainer
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.Color
import gg.voidrix.owolib.owo.ui.core.CursorStyle
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIAdapter
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.modules.DraggableModuleEditWrapper
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.components.voidrix.VoidrixSliderWithInput
import gg.voidrix.ui.modules.v3.V3Border
import gg.voidrix.ui.modules.v3.V3Button
import gg.voidrix.ui.modules.v3.V3ModuleSettingsPanel
import gg.voidrix.ui.modules.v3.V3Surfaces
import gg.voidrix.ui.modules.v3.V3Theme
import java.util.Arrays
import java.util.Locale
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nKeystrokeLayoutEditorScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 KeystrokeLayoutEditorScreen.kt\ngg/voidrix/client/v2/modules/keystrokes/KeystrokeLayoutEditorScreen\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 4 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n+ 5 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 6 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1011:1\n328#2:1012\n40#2:1013\n355#2:1046\n40#2:1047\n356#2:1048\n8#3,4:1014\n8#3,4:1049\n78#4,6:1018\n72#4,4:1024\n87#4:1028\n90#4,6:1030\n72#4,4:1036\n99#4:1040\n90#4,6:1053\n72#4,4:1059\n99#4:1063\n90#4,6:1064\n72#4,4:1070\n99#4:1074\n67#5:1029\n67#5:1041\n67#5:1042\n269#5:1043\n269#5:1044\n1#6:1045\n*S KotlinDebug\n*F\n+ 1 KeystrokeLayoutEditorScreen.kt\ngg/voidrix/client/v2/modules/keystrokes/KeystrokeLayoutEditorScreen\n*L\n75#1:1012\n75#1:1013\n225#1:1046\n225#1:1047\n225#1:1048\n228#1:1014,4\n322#1:1049,4\n229#1:1018,6\n229#1:1024,4\n229#1:1028\n230#1:1030,6\n230#1:1036,4\n230#1:1040\n323#1:1053,6\n323#1:1059,4\n323#1:1063\n324#1:1064,6\n324#1:1070,4\n324#1:1074\n230#1:1029\n341#1:1041\n365#1:1042\n373#1:1043\n459#1:1044\n*E\n"])
public class KeystrokeLayoutEditorScreen : BaseOwoScreen(null, 1), IVoidrixScreen {
   protected open val useBlurredBackground: Boolean = true
   private final lateinit var editor: KeystrokeGridEditor
   private final lateinit var propertiesContent: FlowLayout
   private final lateinit var hudLayer: FlowLayout
   private final var showPreview: Boolean = true
   private final var pendingAutoListen: Boolean
   private final val previousScreen: Screen?

   protected open fun createAdapter(): OwoUIAdapter<FlowLayout> {
      return OwoUIAdapter.Companion.create(this as Screen, { h: Sizing, v: Sizing ->
         val var2: FlowLayout = UIContainers.verticalFlow(h, v)
         var2.allowOverflow(true)
         var2
      })
   }

   protected open fun build(rootComponent: FlowLayout) {
      Keystrokes.INSTANCE.designerScreenOpen = true
      val settingsPanel: KeystrokeGridEditor = KeystrokeGridEditor(30, 16, 8, 0, 8, null)
      settingsPanel.loadKeys(Keystrokes.INSTANCE.currentLayout)
      settingsPanel.onLayoutChange = { keys: java.util.List ->
         Keystrokes.INSTANCE.currentLayout = CollectionsKt.toMutableList(keys)
         `this$0`.rebuildProperties()
         Unit.INSTANCE
      }
      settingsPanel.onSelectionChange = { it: Int ->
         `this$0`.rebuildProperties()
         Unit.INSTANCE
      }
      settingsPanel.onKeyCreated = { it: Int ->
         `this$0`.pendingAutoListen = true
         `this$0`.rebuildProperties()
         Unit.INSTANCE
      }
      settingsPanel.onRequestBind = { it: Int ->
         `this$0`.pendingAutoListen = true
         `this$0`.rebuildProperties()
         Unit.INSTANCE
      }
      this.editor = settingsPanel
      val var16: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var16.gap(4)
      var16.padding(Insets.Companion.of(2))
      this.propertiesContent = var16
      val var19: ScrollContainer = UIContainers.verticalScroll(
         Sizing.Companion.fixed(200),
         Sizing.Companion.fixed(180),
         V3ModuleSettingsPanel(Keystrokes.INSTANCE as ValueHolder, null, Sizing.Companion.fill(), null, 10, null) as UIComponent
      )
      val editorColumn: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.fixed(129))
      editorColumn.gap(3)
      editorColumn.alignment(HorizontalAlignment.LEFT, VerticalAlignment.TOP)
      var var10001: KeystrokeGridEditor = this.editor
      if (this.editor == null) {
         Intrinsics.throwUninitializedPropertyAccessException("editor")
         var10001 = null
      }

      editorColumn.child(var10001 as UIComponent)
      editorColumn.child(this.buildEditorToolbar() as UIComponent)
      val var23: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.fixed(180))
      var23.gap(6)
      var23.alignment(HorizontalAlignment.LEFT, VerticalAlignment.TOP)
      var23.child(editorColumn as UIComponent)
      var23.child(this.buildPresetRow() as UIComponent)
      val var26: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fixed(492), Sizing.Companion.fixed(204))
      var26.padding(Insets.Companion.of(12))
      var26.gap(10)
      var26.alignment(HorizontalAlignment.LEFT, VerticalAlignment.TOP)
      var26.surface(V3Surfaces.INSTANCE.contentSurface())
      var26.child(var23 as UIComponent)
      var26.child(var19 as UIComponent)
      val var10000: Sizing = Sizing.Companion.fixed(468)
      val var42: Sizing = Sizing.Companion.fixed(70)
      var var10002: FlowLayout = this.propertiesContent
      if (this.propertiesContent == null) {
         Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
         var10002 = null
      }

      val var27: ScrollContainer = UIContainers.verticalScroll(var10000, var42, var10002 as UIComponent)
      var panelContainer: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fixed(468), Sizing.Companion.fixed(70))
      panelContainer.gap(10)
      panelContainer.alignment(HorizontalAlignment.LEFT, VerticalAlignment.TOP)
      panelContainer.child(var27 as UIComponent)
      panelContainer = UIContainers.verticalFlow(Sizing.Companion.fixed(492), Sizing.Companion.content())
      panelContainer.child(this.buildNavbar() as UIComponent)
      panelContainer.child(var26 as UIComponent)
      panelContainer.child(this.buildFooter() as UIComponent)
      var var34: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.fill())
      var34.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var34.padding(Insets.Companion.of(20))
      var34.child(panelContainer as UIComponent)
      rootComponent.child(var34 as UIComponent)
      var34 = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.fill())
      var34.allowOverflow(true)
      var34.positioning(Positioning.Companion.absolute(0, 0))
      this.hudLayer = var34
      var var43: FlowLayout = this.hudLayer
      if (this.hudLayer == null) {
         Intrinsics.throwUninitializedPropertyAccessException("hudLayer")
         var43 = null
      }

      rootComponent.child(var43 as UIComponent)

      try {
         HudManager.INSTANCE.removeHud()
      } catch (var15: Exception) {
      }

      this.rebuildHudPreview()
      this.rebuildProperties()
   }

   private fun rebuildHudPreview() {
      if (this.hudLayer != null) {
         var var10000: FlowLayout = this.hudLayer
         if (this.hudLayer == null) {
            Intrinsics.throwUninitializedPropertyAccessException("hudLayer")
            var10000 = null
         }

         var10000.clearChildren()
         if (this.showPreview) {
            var10000 = this.hudLayer
            if (this.hudLayer == null) {
               Intrinsics.throwUninitializedPropertyAccessException("hudLayer")
               var10000 = null
            }

            var10000.child(DraggableModuleEditWrapper(Keystrokes.INSTANCE.toDraggableElement(), false, false, null, null, null, 56, null) as UIComponent)
         }
      }
   }

   private fun buildNavbar(): FlowLayout {
      val var1: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(32))
      var1.surface(V3Surfaces.INSTANCE.navbarSurface(false))
      var1.verticalAlignment(VerticalAlignment.CENTER)
      var1.padding(Insets.Companion.horizontal(8))
      val var4: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var4.verticalAlignment(VerticalAlignment.CENTER)
      var4.padding(Insets.Companion.of(2, 2, 4, 4))
      var4.cursorStyle(CursorStyle.HAND)
      val hovered: BooleanRef = BooleanRef()
      var4.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      var4.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      var4.surface({ context: OwoUIGraphics, component: ParentUIComponent ->
         if (`$hovered`.element) {
            context.fill(component.x(), component.y(), component.x() + component.width(), component.y() + component.height(), V3Theme.INSTANCE.gray(4))
         }
      })
      var4.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         val `screen$iv`: Screen = `this$0`.previousScreen
         val var10000: Minecraft = Minecraft.getInstance()
         var10000.gui.setScreen(`screen$iv`)
         true
      })
      val var10: LiteralTextBuilder = LiteralTextBuilder(null, true)
      var10.getAppendTasks().add(KeystrokeLayoutEditorScreen$buildNavbar$lambda$22$lambda$21$lambda$19$$inlined$text$default$1(var10, "< ", true))
      val var28: Array<Any> = arrayOfNulls(0)
      val var10000: MutableComponent = Component.translatable("voidrix.ui.modules.value.keystrokes", Arrays.copyOf(var28, var28.length))
      var10.getAppendTasks()
         .add(
            KeystrokeLayoutEditorScreen$buildNavbar$lambda$22$lambda$21$lambda$19$$inlined$text$default$2(
               var10, TextKt.toSmallCapsText(var10000 as Component), true
            )
         )
         val var22: LabelComponent = LabelComponent(var10.build() as Component)
      var22.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(11)))
      var4.child(var22 as UIComponent)
      var1.child(var4 as UIComponent)
      var1.child(UIContainers.horizontalFlow(Sizing.Companion.expand(), Sizing.Companion.fixed(1)) as UIComponent)
      var1.child(this.buildPreviewToggle() as UIComponent)
      return var1
   }

   private fun buildEditorToolbar(): FlowLayout {
      val var1: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(14), Sizing.Companion.fixed(129))
      var1.gap(3)
      var1.horizontalAlignment(HorizontalAlignment.CENTER)
      var1.child(this.toolbarIconButton("+", "Add Key", true, { 
         var var10000: KeystrokeGridEditor = `this$0`.editor
         if (`this$0`.editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editor")
            var10000 = null
         }

         if (KeystrokeGridEditor.spawnKey$default(var10000, 0, 0, 3, null)) {
            `this$0`.pendingAutoListen = true
            `this$0`.rebuildProperties()
         }

         Unit.INSTANCE
      }) as UIComponent)
      var1.child(this.toolbarIconButton("×", "Delete Selected Key", false, { 
         var var10000: KeystrokeGridEditor = `this$0`.editor
         if (`this$0`.editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editor")
            var10000 = null
         }

         var10000.deleteSelected()
         `this$0`.rebuildProperties()
         Unit.INSTANCE
      }) as UIComponent)
      return var1
   }

   private fun toolbarIconButton(label: String, tooltip: String, accent: Boolean, onClick: () -> Unit): FlowLayout {
      val var5: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(14), Sizing.Companion.fixed(14))
      var5.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      var5.cursorStyle(CursorStyle.HAND)
      var5.tooltip(TextKt.getLiteral(tooltip) as Component)
      val hovered: BooleanRef = BooleanRef()
      var5.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      var5.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      var5.surface({ context: OwoUIGraphics, component: ParentUIComponent ->
         val base: Int = if (`$accent`) V3Theme.INSTANCE.accent(4) else V3Theme.INSTANCE.gray(4)
         val hot: Int = if (`$accent`) V3Theme.INSTANCE.accent(6) else V3Theme.INSTANCE.gray(6)
         val x0: Double = component.x()
         val y0: Double = component.y()
         val x1: Double = component.x() + component.width()
         val y1: Double = component.y() + component.height()
         context.fill(x0, y0, x1, y1, if (`$hovered`.element) hot else base)
         V3Border.INSTANCE.draw(context, x0, y0, x1 - x0, y1 - y0, if (`$accent`) V3Theme.INSTANCE.accent(8) else V3Theme.INSTANCE.gray(7))
      })
      var5.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         MCSounds.INSTANCE.playButtonSound()
         `$onClick`()
         true
      })
      val var9: LabelComponent = LabelComponent(TextKt.getLiteral(label) as Component)
      var9.color(Color.Companion.ofArgb(if (accent) V3Theme.INSTANCE.accent(12) else V3Theme.INSTANCE.gray(12)))
      var5.child(var9 as UIComponent)
      return var5
   }

   private fun buildPreviewToggle(): FlowLayout {
      val var1: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var1.verticalAlignment(VerticalAlignment.CENTER)
      var1.padding(Insets.Companion.of(3, 3, 6, 6))
      var1.cursorStyle(CursorStyle.HAND)
      val hovered: BooleanRef = BooleanRef()
      var1.mouseEnter().subscribe({ 
         `$hovered`.element = true
      })
      var1.mouseLeave().subscribe({ 
         `$hovered`.element = false
      })
      var1.surface(
         { context: OwoUIGraphics, component: ParentUIComponent ->
            val fill: Int = if (`$hovered`.element)
               (if (`this$0`.showPreview) V3Theme.INSTANCE.accent(6) else V3Theme.INSTANCE.gray(5))
               else
               (if (`this$0`.showPreview) V3Theme.INSTANCE.accent(5) else V3Theme.INSTANCE.gray(4))
               val x0: Double = component.x()
            val y0: Double = component.y()
            val x1: Double = component.x() + component.width()
            val y1: Double = component.y() + component.height()
            context.fill(x0, y0, x1, y1, fill)
            V3Border.INSTANCE.draw(context, x0, y0, x1 - x0, y1 - y0, if (`this$0`.showPreview) V3Theme.INSTANCE.accent(9) else V3Theme.INSTANCE.gray(7))
         }
      )
      var1.mouseDown().subscribe({ var1: MouseButtonEvent, var2: Boolean ->
         MCSounds.INSTANCE.playButtonSound()
         `this$0`.showPreview = !`this$0`.showPreview
         `this$0`.rebuildHudPreview()
         true
      })
      val var5: LabelComponent = LabelComponent(TextKt.getLiteral("") as Component)
      var5.setAutoTextSupplier(
         { 
            val var3: LiteralTextBuilder = LiteralTextBuilder(null, true)
            var3.getAppendTasks()
               .add(
                  KeystrokeLayoutEditorScreen$buildPreviewToggle$lambda$41$lambda$40$lambda$38$lambda$37$$inlined$text$default$1(
                     var3, TextKt.toSmallCapsText("preview "), true
                  )
               )
               var3.getAppendTasks()
               .add(
                  KeystrokeLayoutEditorScreen$buildPreviewToggle$lambda$41$lambda$40$lambda$38$lambda$37$$inlined$text$default$2(
                     var3, if (`this$0`.showPreview) TextKt.toSmallCapsText("on") else TextKt.toSmallCapsText("off"), true, `this$0`
                  )
               )
               var3.build() as Component
         }
      )
      var5.setAutoColorSupplier({ 
         Color.Companion.ofArgb(if (`this$0`.showPreview) V3Theme.INSTANCE.accent(11) else V3Theme.INSTANCE.gray(11))
      })
      var1.child(var5 as UIComponent)
      return var1
   }

   private fun buildFooter(): FlowLayout {
      val var1: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(18))
      var1.surface(V3Surfaces.INSTANCE.footerSurface(false))
      var1.verticalAlignment(VerticalAlignment.CENTER)
      var1.horizontalAlignment(HorizontalAlignment.RIGHT)
      var1.padding(Insets.Companion.of(4, 4, 6, 6))
      val `$this$buildFooter_u24lambda_u2443_u24lambda_u2442`: Array<Any> = arrayOfNulls(0)
      val var10003: MutableComponent = Component.translatable(
         "voidrix.ui.modules.keystrokes.designer",
         Arrays.copyOf(`$this$buildFooter_u24lambda_u2443_u24lambda_u2442`, `$this$buildFooter_u24lambda_u2443_u24lambda_u2442`.length)
      )
      val var8: LabelComponent = LabelComponent(TextKt.toSmallCapsText(var10003 as Component) as Component)
      var8.scale(0.5F)
      var8.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(9)))
      var1.child(var8 as UIComponent)
      return var1
   }

   private fun buildPresetRow(): FlowLayout {
      val var2: FlowLayout = FlowLayout(Sizing.Companion.fixed(241), Sizing.Companion.content(), Algorithm.LTR_TEXT)
      val `$this$buildPresetRow_u24lambda_u2449`: FlowLayout = var2
      var2.gap(3)

      for (`$this$buildPresetRow_u24lambda_u2449_u24lambda_u2448` in KeystrokePresets.INSTANCE.all) {
         `$this$buildPresetRow_u24lambda_u2449`.child(this.presetChip(`$this$buildPresetRow_u24lambda_u2449_u24lambda_u2448`.displayName, { 
            var var10000: KeystrokeGridEditor = `this$0`.editor
            if (`this$0`.editor == null) {
               Intrinsics.throwUninitializedPropertyAccessException("editor")
               var10000 = null
            }

            var10000.replaceAllCentered(KeystrokePreset.materialize$default(`$preset`, 0, 0, 3, null))
            if (KeystrokePresets.INSTANCE.cpsPresets.contains(`$preset`.id)) {
               Keystrokes.INSTANCE.setShowCps$voidrix_client(true)
            }

            Unit.INSTANCE
         }) as UIComponent)
      }

      `$this$buildPresetRow_u24lambda_u2449`.child(this.presetChip("Keyboard", { 
         var var10000: KeystrokeGridEditor = `this$0`.editor
         if (`this$0`.editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editor")
            var10000 = null
         }

         var10000.replaceAllCentered(KeystrokeKeyboardPreset.INSTANCE.materialize())
         Unit.INSTANCE
      }) as UIComponent)
      `$this$buildPresetRow_u24lambda_u2449`.child(this.presetChip("Full Keyboard", { 
         var var10000: KeystrokeGridEditor = `this$0`.editor
         if (`this$0`.editor == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editor")
            var10000 = null
         }

         var10000.replaceAllCentered(KeystrokeKeyboardPreset.INSTANCE.materializeFull())
         Unit.INSTANCE
      }) as UIComponent)
      val var9: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fixed(241), Sizing.Companion.content())
      var9.gap(4)
      val var14: Array<Any> = arrayOfNulls(0)
      val var10003: MutableComponent = Component.translatable("voidrix.ui.modules.keystrokes.presets", Arrays.copyOf(var14, var14.length))
      val var13: LabelComponent = LabelComponent(TextKt.toSmallCapsText(var10003 as Component) as Component)
      var13.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(10)))
      var9.child(var13 as UIComponent)
      var9.child(var2 as UIComponent)
      return var9
   }

   private fun presetChip(name: String, onClick: () -> Unit): V3Button {
      val var10002: java.lang.String = (TextKt.toSmallCapsText(name) as Component).getString()
      val var6: V3Button = V3Button(var10002, { var1: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
         MCSounds.INSTANCE.playButtonSound()
         `$onClick`()
         Unit.INSTANCE
      })
      var6.padding(Insets.Companion.of(2, 2, 6, 6))
      var6.margins(Insets.Companion.bottom(2))
      return var6
   }

   private fun buildFooterHint(): LabelComponent {
      val var1: LabelComponent = LabelComponent(
         TextKt.getLiteral("drag empty area = new key  •  click = select  •  drag corners = resize  •  DEL = remove") as Component
      )
      var1.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(9)))
      var1.horizontalSizing(Sizing.Companion.fill(100))
      return var1
   }

   private fun section(title: String, build: (FlowLayout) -> Unit): FlowLayout {
      val var3: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var3.gap(5)
      var3.child(this.sectionLabel(title) as UIComponent)
      build(var3)
      return var3
   }

   private fun divider(): FlowLayout {
      val var1: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(1))
      var1.surface(Surface.Companion.flat(V3Theme.INSTANCE.grayAlpha(5, 200)))
      return var1
   }

   private fun sectionLabel(text: String): LabelComponent {
      val var10002: java.lang.String = text.toUpperCase(Locale.ROOT)
      val var2: LabelComponent = LabelComponent(TextKt.getLiteral(var10002) as Component)
      var2.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(10)))
      var2.horizontalSizing(Sizing.Companion.fill(100))
      return var2
   }

   private fun buildSlider(label: String, min: Double, max: Double, initial: Double, stepSize: Double, decimals: Int, onChange: (Double) -> Unit): FlowLayout {
      val var12: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var12.alignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER)
      var12.gap(6)
      val var15: LabelComponent = LabelComponent(TextKt.getLiteral(label) as Component)
      var15.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(11)))
      var15.horizontalSizing(Sizing.Companion.fixed(24))
      var12.child(var15 as UIComponent)
      val var19: VoidrixSliderWithInput = VoidrixSliderWithInput(min, max, initial, null, stepSize, false, 120, 26, decimals, 0.0F, 0.0F, 1576, null)
      var19.horizontalSizing(Sizing.Companion.fixed(150))
      var19.onChanged().subscribe({ value: Double ->
         `$onChange`(value)
      })
      var12.child(var19 as UIComponent)
      return var12
   }

   private fun rebuildProperties() {
      var var10000: FlowLayout = this.propertiesContent
      if (this.propertiesContent == null) {
         Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
         var10000 = null
      }

      var10000.clearChildren()
      var var26: KeystrokeGridEditor = this.editor
      if (this.editor == null) {
         Intrinsics.throwUninitializedPropertyAccessException("editor")
         var26 = null
      }

      val selected: PlacedKey = var26.getSelectedKey()
      if (selected == null) {
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         var10000.child(this.sectionLabel("no key selected") as UIComponent)
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         val var12: LabelComponent = LabelComponent(TextKt.getLiteral("drag on empty grid area\nto create a key") as Component)
         var12.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(9)))
         var12.horizontalSizing(Sizing.Companion.fill(100))
         var10000.child(var12 as UIComponent)
         this.pendingAutoListen = false
      } else {
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         var10000.child(this.sectionLabel("selected") as UIComponent)
         val var28: java.lang.String
         if (selected.key.isUnknown()) {
            var28 = "unbound"
         } else {
            var28 = selected.key.getDisplayName().getString()
         }

         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         var10000.child(this.infoLine("key", var28) as UIComponent)
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         var10000.child(this.spacer(4) as UIComponent)
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         var10000.child(this.sectionLabel("binding") as UIComponent)
         val var13: Boolean = this.pendingAutoListen
         this.pendingAutoListen = false
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         val var15: PressToBindButton = PressToBindButton(selected.key, { newKey: MCKey ->
            var var10000: KeystrokeGridEditor = `this$0`.editor
            if (`this$0`.editor == null) {
               Intrinsics.throwUninitializedPropertyAccessException("editor")
               var10000 = null
            }

            var10000.setSelectedKey(newKey)
            Unit.INSTANCE
         }, var13)
         var15.horizontalSizing(Sizing.Companion.fill(100))
         var10000.child(var15 as UIComponent)
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         var10000.child(this.spacer(4) as UIComponent)
         var10000 = this.propertiesContent
         if (this.propertiesContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("propertiesContent")
            var10000 = null
         }

         val var16: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
         var16.gap(4)
         var var7: V3Button = V3Button("unbind", { var1: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
            var var10000: KeystrokeGridEditor = `this$0`.editor
            if (`this$0`.editor == null) {
               Intrinsics.throwUninitializedPropertyAccessException("editor")
               var10000 = null
            }

            var10000.setSelectedKey(MCKey.Companion.getUNKNOWN())
            Unit.INSTANCE
         })
         var7.padding(Insets.Companion.of(4))
         var7.horizontalSizing(Sizing.Companion.fill(50))
         var16.child(var7 as UIComponent)
         var7 = V3Button("delete", { var1: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
            var var10000: KeystrokeGridEditor = `this$0`.editor
            if (`this$0`.editor == null) {
               Intrinsics.throwUninitializedPropertyAccessException("editor")
               var10000 = null
            }

            var10000.deleteSelected()
            Unit.INSTANCE
         })
         var7.padding(Insets.Companion.of(4))
         var7.horizontalSizing(Sizing.Companion.fill(50))
         var16.child(var7 as UIComponent)
         var10000.child(var16 as UIComponent)
      }
   }

   private fun infoLine(key: String, value: String): FlowLayout {
      val var3: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(100), Sizing.Companion.content())
      var var6: LabelComponent = LabelComponent(TextKt.getLiteral(key) as Component)
      var6.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(9)))
      var6.horizontalSizing(Sizing.Companion.fixed(24))
      var3.child(var6 as UIComponent)
      var6 = LabelComponent(TextKt.getLiteral(value) as Component)
      var6.color(Color.Companion.ofArgb(V3Theme.INSTANCE.gray(12)))
      var3.child(var6 as UIComponent)
      return var3
   }

   private fun spacer(h: Int): FlowLayout {
      return UIContainers.verticalFlow(Sizing.Companion.fill(100), Sizing.Companion.fixed(h))
   }

   public open fun isPauseScreen(): Boolean {
      return false
   }

   public open fun dispose() {
      this.releaseDesignerState()
      super.dispose()
   }

   protected open fun onScreenClosed() {
      this.releaseDesignerState()
      super.onScreenClosed()
   }

   private fun releaseDesignerState() {
      Keystrokes.INSTANCE.designerPreviewWrapper = null
      Keystrokes.INSTANCE.designerScreenOpen = false

      try {
         HudManager.INSTANCE.rebuildHud()
      } catch (var2: Exception) {
      }
   }

   public companion object {
      private const val GRID_COLS: Int = 30
      private const val GRID_ROWS: Int = 16
      private const val GRID_CELL_PX: Int = 8
      private const val EDITOR_WIDTH: Int = 241
      private const val EDITOR_HEIGHT: Int = 129
      private const val NAVBAR_HEIGHT: Int = 32
      private const val FOOTER_HEIGHT: Int = 18
      private const val SIDEBAR_HEIGHT: Int = 180
      private const val SIDEBAR_WIDTH: Int = 200
      private const val TOOLBAR_WIDTH: Int = 14
      private const val SIDEBAR_PADDING: Int = 8
      private const val SIDEBAR_INNER_WIDTH: Int = 184
      private const val BOTTOM_ROW_HEIGHT: Int = 70
      private const val INNER_GAP: Int = 10
      private const val PANEL_PADDING: Int = 12
      private const val TOOLBAR_GAP: Int = 3
      private const val CONTENT_WIDTH: Int = 468
      private const val PANEL_WIDTH: Int = 492
      private const val LABEL_WIDTH: Int = 24
      private const val SLIDER_WIDTH: Int = 120
      private const val INPUT_WIDTH: Int = 26
   }
}
