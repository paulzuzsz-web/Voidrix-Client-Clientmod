package gg.voidrix.client.v2.modules.autotext

import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixCollapsibleContainer
import gg.voidrix.ui.components.voidrix.VoidrixConfirmDialog
import gg.voidrix.ui.components.voidrix.VoidrixInputField
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.modules.v3.RightShiftMenuV3Screen
import gg.voidrix.ui.modules.v3.V3Button
import gg.voidrix.ui.modules.v3.V3RowActions
import gg.voidrix.ui.modules.v3.V3Theme
import gg.voidrix.ui.modules.v3.V3ValueLine
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nAutoTextListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoTextListComponent.kt\ngg/voidrix/client/v2/modules/autotext/AutoTextListComponent\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,169:1\n67#2:170\n67#2:171\n*S KotlinDebug\n*F\n+ 1 AutoTextListComponent.kt\ngg/voidrix/client/v2/modules/autotext/AutoTextListComponent\n*L\n53#1:170\n57#1:171\n*E\n"])
public class AutoTextListComponent(horizontalSizing: Sizing = Sizing.Companion.fill(), verticalSizing: Sizing = Sizing.Companion.content()) : FlowLayout(
      horizontalSizing, verticalSizing, Algorithm.VERTICAL
   ) {
   init {
      this.gap(2)
   }

   public fun build(entries: MutableList<AutoTextEntry>) {
      this.clearChildren()
      if (entries.isEmpty()) {
         this.child(this.buildEmptyState())
      } else {
         for (entry in entries) {
            this.child(AutoTextListComponent.AutoTextEntryCard(entry) as UIComponent)
         }
      }
   }

   private fun buildEmptyState(): UIComponent {
      val `key$iv`: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      `key$iv`.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
      `key$iv`.padding(Insets.Companion.vertical(14))
      `key$iv`.gap(2)
      var var10: Array<Any> = arrayOfNulls(0)
      var var10003: MutableComponent = Component.translatable("voidrix.autotext.empty.title", Arrays.copyOf(var10, var10.length))
      val var7: LabelComponent = LabelComponent(TextKt.toSmallCapsText(var10003 as Component) as Component)
      var7.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      var7.setAutoColorSupplier({ 
         V3Theme.INSTANCE.grayColor(11)
      })
      `key$iv`.child(var7 as UIComponent)
      var10 = arrayOfNulls(0)
      var10003 = Component.translatable("voidrix.autotext.empty.subtitle", Arrays.copyOf(var10, var10.length))
      val var9: LabelComponent = LabelComponent(var10003 as Component)
      var9.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      var9.setAutoColorSupplier({ 
         V3Theme.INSTANCE.grayColor(8)
      })
      `key$iv`.child(var9 as UIComponent)
      return `key$iv` as UIComponent
   }

   fun AutoTextListComponent() {
      this(null, null, 3, null)
   }

   @SourceDebugExtension(["SMAP\nAutoTextListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoTextListComponent.kt\ngg/voidrix/client/v2/modules/autotext/AutoTextListComponent$AutoTextEntryCard\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,169:1\n67#2:170\n269#2:171\n67#2:172\n269#2:173\n67#2:174\n269#2:175\n67#2:176\n67#2:177\n*S KotlinDebug\n*F\n+ 1 AutoTextListComponent.kt\ngg/voidrix/client/v2/modules/autotext/AutoTextListComponent$AutoTextEntryCard\n*L\n97#1:170\n97#1:171\n116#1:172\n116#1:173\n128#1:174\n128#1:175\n139#1:176\n141#1:177\n*E\n"])
   public inner class AutoTextEntryCard(entry: AutoTextEntry) : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
      public final val entry: AutoTextEntry
      private final val collapsible: VoidrixCollapsibleContainer
      private final val collapsibleChild: FlowLayout
      private final val onOffButton: V3Button
      private final val deleteButton: V3Button

      init {
         this.entry = entry
         this.collapsible = VoidrixCollapsibleContainer(
            TextKt.toSmallCapsText(this.entryTitleText()) as Component, StringsKt.isBlank(this.entry.text), Sizing.Companion.fill(), null, 8, null
         )
         val var3: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         var3.gap(0)
         this.collapsibleChild = var3
         this.onOffButton = V3RowActions.INSTANCE.toggleButton({ 
            `this$0`.entry.isEnabled
         }, { 
            `this$0`.entry.isEnabled = !`this$0`.entry.isEnabled
            AutoText.INSTANCE.updateProfile()
            Unit.INSTANCE
         })
         this.deleteButton = V3RowActions.deleteButton$default(V3RowActions.INSTANCE, 0, { 
            `this$0`.confirmDelete()
            Unit.INSTANCE
         }, 1, null)
         AutoTextListComponentKt.access$styleCollapsibleV3(this.collapsible)
         this.collapsible.getTitleLayout().horizontalSizing(Sizing.Companion.fill())
         this.collapsible.getTitleLayout().child(V3RowActions.INSTANCE.rightAlignedRow(arrayOf(this.onOffButton, this.deleteButton)) as UIComponent)
         this.id(this.entry.id)
         this.child(this.collapsible as UIComponent)
         this.collapsible.child(this.collapsibleChild as UIComponent)
         var var30: FlowLayout = this.collapsibleChild
         val var12: V3ValueLine = V3ValueLine(null, null, 3, null)
         var12.margins(Insets.Companion.bottom(1))
         var `$i$f$asString`: Array<Any> = arrayOfNulls(0)
         var var32: MutableComponent = Component.translatable("voidrix.autotext.field.message", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
         val var33: java.lang.String = (var32 as Component).getString()
         var12.title(var33)
         val var23: VoidrixInputField = VoidrixInputField(140, this.entry.text, 0.75F, true)
         var23.setUseV3Colors(true)
         var23.maxLength(1024)
         var23.text(this.entry.text)
         var23.onChanged().subscribe({ newText: java.lang.String ->
            `this$0`.entry.text = newText
            `this$0`.collapsible.getTitleLabel().text(TextKt.toSmallCapsText(`this$0`.entryTitleText()) as Component)
         })
         var12.right(var23 as UIComponent)
         var30.child(var12 as UIComponent)
         var30 = this.collapsibleChild
         val var13: V3ValueLine = V3ValueLine(null, null, 3, null)
         var13.margins(Insets.Companion.bottom(1))
         `$i$f$asString` = arrayOfNulls(0)
         var32 = Component.translatable("voidrix.autotext.field.keybind", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
         val var35: java.lang.String = (var32 as Component).getString()
         var13.title(var35)
         var13.right(AutoTextKeysButton({ 
            `this$0`.entry.keys
         }, { it: java.util.List ->
            `this$0`.entry.keys = it
            Unit.INSTANCE
         }, 140) as UIComponent)
         var30.child(var13 as UIComponent)
      }

      private fun entryTitleText(): String {
         val raw: java.lang.String = StringsKt.trim(this.entry.text).toString()
         val var7: java.lang.String
         if (raw.length() == 0) {
            val `$i$f$asString`: Array<Any> = arrayOfNulls(0)
            val var10000: MutableComponent = Component.translatable("voidrix.autotext.entry.untitled", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
            var7 = (var10000 as Component).getString()
         } else {
            var7 = if (raw.length() <= 24) raw else "${StringsKt.take(raw, 24)}…"
         }

         return var7
      }

      private fun confirmDelete() {
         UISounds.playButtonSound()
         val list: AutoTextListComponent = AutoTextListComponent.this
         val var10000: ParentUIComponent = AutoTextListComponent.this.root()
         if (var10000 != null) {
            VoidrixConfirmDialog.Companion.openConfirmDialog(var10000, { dialog: VoidrixConfirmDialog ->
               val var10000: LabelComponent = dialog.getTitle()
               var `args$iv`: Array<Any> = arrayOfNulls(0)
               val var10001: MutableComponent = Component.translatable("voidrix.autotext.delete.title", Arrays.copyOf(`args$iv`, `args$iv`.length))
               var10000.text(var10001 as Component)
               `args$iv` = arrayOf(`this$0`.entryTitleText())
               val var10003: MutableComponent = Component.translatable("voidrix.autotext.delete.message", Arrays.copyOf(`args$iv`, `args$iv`.length))
               dialog.content(LabelComponent(var10003 as Component) as UIComponent)
               dialog.getConfirmButton().onClick({ var3: VoidrixLabelButton, var4: Double, var6: Double, var8: Int ->
                  UISounds.playButtonSound()
                  AutoText.INSTANCE.removeEntry(`this$0`.entry, `$list`)
                  `$dialog`.close()
                  Unit.INSTANCE
               })
               Unit.INSTANCE
            })
         }
      }
   }
}
