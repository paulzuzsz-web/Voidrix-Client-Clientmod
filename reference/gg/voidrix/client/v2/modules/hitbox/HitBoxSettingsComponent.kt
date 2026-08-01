package gg.voidrix.client.v2.modules.hitbox

import gg.voidrix.client.v2.modules.hitbox.HitBox.Type
import gg.voidrix.owolib.owo.ui.component.ConditionalComponent
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.components.voidrix.VoidrixCheckbox
import gg.voidrix.ui.components.voidrix.VoidrixCollapsibleContainer
import gg.voidrix.ui.components.voidrix.VoidrixSliderWithInput
import gg.voidrix.ui.modules.v3.V3ColorSwatch
import gg.voidrix.ui.modules.v3.V3Theme
import gg.voidrix.ui.modules.v3.V3ValueLine
import gg.voidrix.ui.utils.OwoLibExtensions
import java.awt.Color
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension

@SourceDebugExtension(["SMAP\nHitBoxSettingsComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HitBoxSettingsComponent.kt\ngg/voidrix/client/v2/modules/hitbox/HitBoxSettingsComponent\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,198:1\n1869#2,2:199\n*S KotlinDebug\n*F\n+ 1 HitBoxSettingsComponent.kt\ngg/voidrix/client/v2/modules/hitbox/HitBoxSettingsComponent\n*L\n56#1:199,2\n*E\n"])
public object HitBoxSettingsComponent {
   private final val collapsibles: ArrayList<VoidrixCollapsibleContainer> = ArrayList()

   public fun build(settingsPanel: FlowLayout, module: HitBox) {
      settingsPanel.clearChildren()
      collapsibles.clear()

      for (type in HitBox.Type.getEntries()) {
         val collapsible: <unrepresentable> = object : VoidrixCollapsibleContainer {
            public open fun toggleExpansion() {
               super.toggleExpansion()
               if (this.expanded()) {
                  for (`element$iv` in HitBoxSettingsComponent.collapsibles) {
                     val it: VoidrixCollapsibleContainer = `element$iv` as VoidrixCollapsibleContainer
                     if (`element$iv` as VoidrixCollapsibleContainer != this && (`element$iv` as VoidrixCollapsibleContainer).expanded()) {
                        it.toggleExpansion()
                     }
                  }

                  HitBoxPreview.INSTANCE.previewType = type
               } else {
                  HitBoxPreview.INSTANCE.previewType = null
               }
            }
         }
         this.styleCollapsibleV3(collapsible)
         collapsible.child(this.buildTypeSettings(module, type) as UIComponent)
         collapsibles.add(collapsible)
         settingsPanel.child(collapsible as UIComponent)
      }
   }

   private fun styleCollapsibleV3(collapsible: VoidrixCollapsibleContainer) {
      collapsible.getTitleLabel().setAutoColorSupplier({ 
         V3Theme.INSTANCE.grayColor(12)
      })
      val var3: Any = CollectionsKt.firstOrNull(collapsible.getTitleLayout().children())
      val titleRow: FlowLayout = var3 as? FlowLayout
      if ((var3 as? FlowLayout) != null) {
         val var10000: java.util.List = titleRow.children()
         if (var10000 != null) {
            for (`element$iv` in var10000) {
               val child: UIComponent = `element$iv` as UIComponent
               if (`element$iv` as UIComponent is LabelComponent && `element$iv` as UIComponent != collapsible.getTitleLabel()) {
                  (child as LabelComponent).setAutoColorSupplier({ 
                     V3Theme.INSTANCE.grayColor(11)
                  })
               }
            }
         }
      }
   }

   private fun buildTypeSettings(module: HitBox, type: Type): FlowLayout {
      val settings: HitBox.HitBoxSettings = module.getSettings(type)
      val content: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      val visibleLine: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
      visibleLine.title("Visible")
      val visibleCheckbox: VoidrixCheckbox = VoidrixCheckbox(0.75F)
      visibleCheckbox.checked(settings.visible)
      visibleCheckbox.onChanged().subscribe({ checked: Boolean ->
         `$settings`.visible = checked
         INSTANCE.saveSettings(`$module`)
      })
      visibleLine.right(visibleCheckbox as UIComponent)
      content.child(visibleLine as UIComponent)
      val lineWidthLine: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
      lineWidthLine.title("Line Width")
      val colorLine: VoidrixSliderWithInput = VoidrixSliderWithInput(0.5, 10.0, settings.lineWidth, 1.0, 0.5, false, 0, 0, 1, 0.0F, 0.0F, 1760, null)
      colorLine.onChanged().subscribe({ value: Double ->
         `$settings`.lineWidth = (float)value
         INSTANCE.saveSettings(`$module`)
      })
      lineWidthLine.right(colorLine as UIComponent)
      content.child(lineWidthLine as UIComponent)
      val var15: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
      var15.title("Color")
      val var16: V3ColorSwatch = V3ColorSwatch(OwoLibExtensions.INSTANCE.toJavaColor(settings.multiColor.getColor()), false, 0, 4, null)
      var16.setColorSupplier({ 
         OwoLibExtensions.INSTANCE.toJavaColor(`$module`.getSettings(`$type`).multiColor.getColor())
      })
      var16.setShowRainbow(true)
      var16.setRainbow(settings.multiColor.isRainbow())
      var16.setPositionalRainbow(settings.multiColor.isPositionalRainbow())
      var16.onChanged()
         .subscribe(
            { color: Color ->
               `$settings`.multiColor
                  .setColor(
                     gg.voidrix.owolib.owo.ui.core.Color(
                        (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
                     )
                  )
                  INSTANCE.saveSettings(`$module`)
            }
         )
         var16.setOnRainbowChanged({ it: Boolean ->
         `$settings`.multiColor.setRainbow(it)
         INSTANCE.saveSettings(`$module`)
         Unit.INSTANCE
      })
      var16.setOnPositionalChanged({ it: Boolean ->
         `$settings`.multiColor.setPositionalRainbow(it)
         INSTANCE.saveSettings(`$module`)
         Unit.INSTANCE
      })
      var15.right(var16 as UIComponent)
      content.child(var15 as UIComponent)
      if (type != HitBox.Type.SELF) {
         val var17: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
         var17.title("Target Highlight")
         val lookVectorCheckbox: VoidrixCheckbox = VoidrixCheckbox(0.75F)
         lookVectorCheckbox.checked(settings.dynamicColor)
         lookVectorCheckbox.onChanged().subscribe({ checked: Boolean ->
            `$settings`.dynamicColor = checked
            INSTANCE.saveSettings(`$module`)
         })
         var17.right(lookVectorCheckbox as UIComponent)
         content.child(var17 as UIComponent)
         val facingColorLine: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
         facingColorLine.title("Target Color")
         val facingSwatch: V3ColorSwatch = V3ColorSwatch(OwoLibExtensions.INSTANCE.toJavaColor(settings.enemyColor.getColor()), false, 0, 4, null)
         facingSwatch.setColorSupplier({ 
            OwoLibExtensions.INSTANCE.toJavaColor(`$module`.getSettings(`$type`).enemyColor.getColor())
         })
         facingSwatch.setShowRainbow(true)
         facingSwatch.setRainbow(settings.enemyColor.isRainbow())
         facingSwatch.setPositionalRainbow(settings.enemyColor.isPositionalRainbow())
         facingSwatch.onChanged()
            .subscribe(
               { color: Color ->
                  `$settings`.enemyColor
                     .setColor(
                        gg.voidrix.owolib.owo.ui.core.Color(
                           (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
                        )
                     )
                     INSTANCE.saveSettings(`$module`)
               }
            )
            facingSwatch.setOnRainbowChanged({ it: Boolean ->
            `$settings`.enemyColor.setRainbow(it)
            INSTANCE.saveSettings(`$module`)
            Unit.INSTANCE
         })
         facingSwatch.setOnPositionalChanged({ it: Boolean ->
            `$settings`.enemyColor.setPositionalRainbow(it)
            INSTANCE.saveSettings(`$module`)
            Unit.INSTANCE
         })
         facingColorLine.right(facingSwatch as UIComponent)
         content.child(ConditionalComponent({ 
            `$settings`.dynamicColor
         }, facingColorLine as UIComponent, null, null, 12, null) as UIComponent)
      }

      val var18: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
      var18.title("Look Vector")
      val var19: VoidrixCheckbox = VoidrixCheckbox(0.75F)
      var19.checked(settings.showLookVector)
      var19.onChanged().subscribe({ checked: Boolean ->
         `$settings`.showLookVector = checked
         INSTANCE.saveSettings(`$module`)
      })
      var18.right(var19 as UIComponent)
      content.child(var18 as UIComponent)
      val var20: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
      var20.title("Facing Color")
      val var21: V3ColorSwatch = V3ColorSwatch(OwoLibExtensions.INSTANCE.toJavaColor(settings.facingColor.getColor()), false, 0, 4, null)
      var21.setColorSupplier({ 
         OwoLibExtensions.INSTANCE.toJavaColor(`$module`.getSettings(`$type`).facingColor.getColor())
      })
      var21.setShowRainbow(true)
      var21.setRainbow(settings.facingColor.isRainbow())
      var21.setPositionalRainbow(settings.facingColor.isPositionalRainbow())
      var21.onChanged()
         .subscribe(
            { color: Color ->
               `$settings`.facingColor
                  .setColor(
                     gg.voidrix.owolib.owo.ui.core.Color(
                        (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F
                     )
                  )
                  INSTANCE.saveSettings(`$module`)
            }
         )
         var21.setOnRainbowChanged({ it: Boolean ->
         `$settings`.facingColor.setRainbow(it)
         INSTANCE.saveSettings(`$module`)
         Unit.INSTANCE
      })
      var21.setOnPositionalChanged({ it: Boolean ->
         `$settings`.facingColor.setPositionalRainbow(it)
         INSTANCE.saveSettings(`$module`)
         Unit.INSTANCE
      })
      var20.right(var21 as UIComponent)
      content.child(ConditionalComponent({ 
         `$settings`.showLookVector
      }, var20 as UIComponent, null, null, 12, null) as UIComponent)
      return content
   }

   private fun saveSettings(module: HitBox) {
      module.settings = MapsKt.toMutableMap(module.settings)
   }
}
