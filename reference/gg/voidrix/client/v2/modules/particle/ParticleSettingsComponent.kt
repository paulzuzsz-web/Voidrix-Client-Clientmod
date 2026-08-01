package gg.voidrix.client.v2.modules.particle

import gg.voidrix.compat.client.MCParticles
import gg.voidrix.compat.client.ParticleSpriteInfo
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.component.TextureComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.ScrollContainer
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.ScrollContainer.Scrollbar
import gg.voidrix.owolib.owo.ui.core.CursorStyle
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.input.MouseButtonEvent
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixCheckbox
import gg.voidrix.ui.components.voidrix.VoidrixInputField
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.components.voidrix.VoidrixSliderWithInput
import gg.voidrix.ui.modules.v3.RightShiftMenuV3Screen
import gg.voidrix.ui.modules.v3.V3Surfaces
import gg.voidrix.ui.modules.v3.V3Theme
import gg.voidrix.ui.modules.v3.V3ValueLine
import java.util.ArrayList
import java.util.Arrays
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.Ref.BooleanRef
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

@SourceDebugExtension(["SMAP\nParticleSettingsComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ParticleSettingsComponent.kt\ngg/voidrix/client/v2/modules/particle/ParticleSettingsComponent\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 5 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n+ 6 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,307:1\n67#2:308\n269#2:309\n67#2:310\n269#2:311\n67#2:328\n269#2:329\n1#3:312\n8#4,4:313\n78#5,6:317\n72#5,4:323\n87#5:327\n774#6:330\n865#6,2:331\n*S KotlinDebug\n*F\n+ 1 ParticleSettingsComponent.kt\ngg/voidrix/client/v2/modules/particle/ParticleSettingsComponent\n*L\n46#1:308\n46#1:309\n57#1:310\n57#1:311\n287#1:328\n287#1:329\n178#1:313,4\n179#1:317,6\n179#1:323,4\n179#1:327\n102#1:330\n102#1:331,2\n*E\n"])
public object ParticleSettingsComponent {
   private final var expandedTypeId: String?
   private final var pendingRebuild: (() -> Unit)?

   public fun build(settingsPanel: FlowLayout, module: ParticleModule) {
      ParticleModule.INSTANCE.settingsOpen = true
      ParticleModule.INSTANCE.previewTypeId = expandedTypeId
      val topRow: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      topRow.gap(4)
      topRow.verticalAlignment(VerticalAlignment.CENTER)
      topRow.margins(Insets.Companion.bottom(3))
      val searchField: VoidrixInputField = VoidrixInputField(120, "", 0.75F, true)
      searchField.horizontalSizing(Sizing.Companion.expand(100))
      searchField.setUseV3Colors(true)
      searchField.setPlaceholder(TextKt.getLiteral("Search particles...") as Component)
      topRow.child(searchField as UIComponent)
      ParticleModule.INSTANCE.previewEnabled = true
      val listChild: Array<Any> = arrayOfNulls(0)
      var var10002: MutableComponent = Component.translatable("voidrix.particle.button.all_on", Arrays.copyOf(listChild, listChild.length))
      val var32: java.lang.String = (TextKt.toSmallCapsText(var10002 as Component) as Component).getString()
      val var13: VoidrixLabelButton = VoidrixLabelButton(var32, { var2: VoidrixLabelButton, var3: Double, var5: Double, var7: Int ->
         UISounds.playButtonSound()
         `$module`.setAllEnabled(true)
         `$settingsPanel`.clearChildren()
         INSTANCE.build(`$settingsPanel`, `$module`)
         Unit.INSTANCE
      })
      var13.setUseV3Colors(true)
      var13.verticalSizing(Sizing.Companion.fixed(10))
      var13.padding(Insets.Companion.of(0, 0, 4, 4))
      var13.getLabel().scale(0.7F)
      val `args$ivx`: Array<Any> = arrayOfNulls(0)
      var10002 = Component.translatable("voidrix.particle.button.all_off", Arrays.copyOf(`args$ivx`, `args$ivx`.length))
      val var34: java.lang.String = (TextKt.toSmallCapsText(var10002 as Component) as Component).getString()
      val var19: VoidrixLabelButton = VoidrixLabelButton(var34, { var2: VoidrixLabelButton, var3: Double, var5: Double, var7: Int ->
         UISounds.playButtonSound()
         `$module`.setAllEnabled(false)
         `$settingsPanel`.clearChildren()
         INSTANCE.build(`$settingsPanel`, `$module`)
         Unit.INSTANCE
      })
      var19.setUseV3Colors(true)
      var19.verticalSizing(Sizing.Companion.fixed(10))
      var19.padding(Insets.Companion.of(0, 0, 4, 4))
      var19.getLabel().scale(0.7F)
      topRow.child(var13 as UIComponent)
      topRow.child(var19 as UIComponent)
      settingsPanel.child(topRow as UIComponent)
      val var25: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      var25.gap(2)
      val var20: FlowLayout = var25
      val var26: <unrepresentable> = object : ScrollContainer<FlowLayout> {
         private final var sized: Boolean

         public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
            if (!this.sized) {
               this.sized = true
               var p: UIComponent = this.parent() as UIComponent

               while (p != null && p !is ScrollContainer) {
                  p = p.parent() as UIComponent
               }

               if (p != null) {
                  this.verticalSizing(
                     Sizing.Companion.fixed(RangesKt.coerceAtLeast((int)((p as ScrollContainer).y() + (p as ScrollContainer).height() - this.y()), 50))
                  )
               }
            }

            super.draw(context, mouseX, mouseY, partialTicks, delta)
         }
      }
      var26.scrollbar(Scrollbar.Companion.flat(V3Surfaces.INSTANCE.scrollbarColor()))
      var26.scrollbarThiccness(2.0)
      settingsPanel.child(var26 as UIComponent)
      val var30: java.util.List = MCParticles.INSTANCE.getAllParticleTypeIds()
      pendingRebuild = { 
         build$rebuildList(`$listScroll`, `$listChild`, `$searchField`, `$allIds`, `$module`, true)
         Unit.INSTANCE
      }
      searchField.onChanged().subscribe({ it: java.lang.String ->
         `$listScroll`.scrollTo(0.0, true)
         build$rebuildList$default(`$listScroll`, `$listChild`, `$searchField`, `$allIds`, `$module`, false, 32, null)
      })
      build$rebuildList$default(var26, var20, searchField, var30, module, false, 32, null)
   }

   private fun buildParticleRow(typeId: String, module: ParticleModule): FlowLayout {
      val settings: ParticleTypeSettings = module.getOrCreateSettings(typeId)
      val displayName: java.lang.String = CollectionsKt.joinToString$default(
         StringsKt.split$default(
            StringsKt.replace$default(StringsKt.substringAfter$default(typeId, ":", null, 2, null), "_", " ", false, 4, null), arrayOf(" "), false, 0, 6, null
         ),
         " ",
         null,
         null,
         0,
         null,
         { it: java.lang.String ->
            var var10: java.lang.String
            if (it.length() > 0) {
               val var10000: StringBuilder = StringBuilder()
               var10 = java.lang.String.valueOf(it.charAt(0))
               var10 = var10.toUpperCase(Locale.ROOT)
               val var9: StringBuilder = var10000.append((Object)var10)
               val var10001: java.lang.String = it.substring(1)
               var10 = var9.append(var10001).toString()
            } else {
               var10 = it
            }

            var10 as java.lang.CharSequence
         },
         30,
         null
      )
      val var24: Boolean = expandedTypeId == typeId
      val container: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
      container.gap(0)
      val header: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.fill(), Sizing.Companion.fixed(14))
      header.padding(Insets.Companion.of(0, 0, 4, 6))
      header.verticalAlignment(VerticalAlignment.CENTER)
      header.cursorStyle(CursorStyle.HAND)
      header.gap(4)
      header.allowOverflow(true)
      val headerHovered: BooleanRef = BooleanRef()
      header.mouseEnter().subscribe({ 
         `$headerHovered`.element = true
      })
      header.mouseLeave().subscribe({ 
         `$headerHovered`.element = false
      })
      header.surface({ context: OwoUIGraphics, component: ParentUIComponent ->
         val x: Double = component.x()
         val y: Double = component.y()
         val w: Double = component.width()
         val h: Double = component.height()
         val bg: Int = if (`$isExpanded`) V3Theme.INSTANCE.accentAlpha(3, 60) else (if (`$headerHovered`.element) V3Theme.INSTANCE.grayAlpha(4, 60) else 0)
         if (bg != 0) {
            context.fill(x, y, x + w, y + h, bg)
         }
      })
      val sprite: ParticleSpriteInfo = ParticleSpriteLookup.INSTANCE.get(typeId)
      if (sprite != null) {
         val nameWrapper: TextureComponent = TextureComponent(
            sprite.getAtlasLocation(),
            sprite.getUPx(),
            sprite.getVPx(),
            sprite.getRegionWidth(),
            sprite.getRegionHeight(),
            sprite.getAtlasWidth(),
            sprite.getAtlasHeight()
         )
         nameWrapper.sizing(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
         header.child(nameWrapper as UIComponent)
      } else {
         header.child(UIContainers.horizontalFlow(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10)) as UIComponent)
      }

      val var26: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.expand(100), Sizing.Companion.content())
      var26.verticalAlignment(VerticalAlignment.CENTER)
      val enabledCheckbox: LiteralTextBuilder = LiteralTextBuilder(null, true)
      enabledCheckbox.getAppendTasks().add(ParticleSettingsComponent$buildParticleRow$lambda$13$$inlined$text$default$1(enabledCheckbox, displayName, true))
      if (!settings.enabled) {
         enabledCheckbox.setStrikethrough(true)
      }

      val var27: LabelComponent = LabelComponent(enabledCheckbox.build() as Component)
      var27.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      var27.setAutoColorSupplier({ 
         if (`$settings`.enabled) V3Theme.INSTANCE.grayColor(12) else V3Theme.INSTANCE.grayColor(9)
      })
      var26.child(var27 as UIComponent)
      header.child(var26 as UIComponent)
      val var28: LabelComponent = LabelComponent(TextKt.getLiteral("") as Component)
      var28.scale(0.7F)
      var28.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
      var28.setAutoColorSupplier({ 
         V3Theme.INSTANCE.accentColor(11)
      })
      var28.margins(Insets.Companion.right(4))
      var28.setAutoTextSupplier({ 
         val var6: MutableComponent
         if (`$settings`.multiplier == 1.0 && `$settings`.scale == 1.0 && `$settings`.color == 0 && `$settings`.overlayColor == 0) {
            var6 = TextKt.getLiteral("")
         } else {
            val multStr: java.lang.String = "${(int)(`$settings`.multiplier * 100)}%"
            val var5: Array<Any> = arrayOf(`$settings`.scale)
            val var10000: java.lang.String = java.lang.String.format("%.1f", Arrays.copyOf(var5, var5.length))
            var6 = TextKt.getLiteral("$multStr $var10000×")
         }

         var6 as Component
      })
      header.child(var28 as UIComponent)
      val var29: VoidrixCheckbox = VoidrixCheckbox(0.7F)
      var29.checked(settings.enabled)
      var29.onChanged().subscribe({ checked: Boolean ->
         `$settings`.enabled = checked
         `$module`.saveSettings()
      })
      V3ValueLine.Companion.styleV3(var29 as UIComponent)
      header.child(var29 as UIComponent)
      header.mouseDown().subscribe(lambda_18@{ click: MouseButtonEvent, var3: Boolean ->
         if (click.getButton() != 0) {
            return@lambda_18 false
         } else {
            UISounds.playButtonSound()
            expandedTypeId = if (`$isExpanded`) null else `$typeId`
            ParticleModule.INSTANCE.previewTypeId = expandedTypeId
            if (pendingRebuild != null) {
               pendingRebuild()
            }

            return@lambda_18 true
         }
      })
      container.child(header as UIComponent)
      if (var24) {
         val var30: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.fill(), Sizing.Companion.content())
         var30.padding(Insets.Companion.of(2, 4, 12, 4))
         var30.gap(2)
         var30.surface({ context: OwoUIGraphics, component: ParentUIComponent ->
            val x: Double = component.x()
            val y: Double = component.y()
            context.fill(x, y, x + component.width(), y + component.height(), V3Theme.INSTANCE.grayAlpha(2, 40))
         })
         val var31: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
         var31.title("Multiplier")
         val var32: VoidrixSliderWithInput = VoidrixSliderWithInput(0.0, 10.0, settings.multiplier, 1.0, 0.05, true, 50, 24, 2, 0.65F, 0.55F)
         var32.horizontalSizing(Sizing.Companion.content())
         var32.onChanged().subscribe({ v: Double ->
            `$settings`.multiplier = v
            `$module`.saveSettings()
         })
         V3ValueLine.Companion.styleV3(var32 as UIComponent)
         var31.right(var32 as UIComponent)
         var30.child(var31 as UIComponent)
         val var34: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
         var34.title("Scale")
         val var35: VoidrixSliderWithInput = VoidrixSliderWithInput(0.25, 3.0, settings.scale, 1.0, 0.05, true, 50, 24, 2, 0.65F, 0.55F)
         var35.horizontalSizing(Sizing.Companion.content())
         var35.onChanged().subscribe({ v: Double ->
            `$settings`.scale = v
            `$module`.saveSettings()
         })
         V3ValueLine.Companion.styleV3(var35 as UIComponent)
         var34.right(var35 as UIComponent)
         var30.child(var34 as UIComponent)
         val var37: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
         val `$i$f$asString`: Array<Any> = arrayOfNulls(0)
         val var10001: MutableComponent = Component.translatable("voidrix.particle.value.spawnOnHit", Arrays.copyOf(`$i$f$asString`, `$i$f$asString`.length))
         val var41: java.lang.String = (var10001 as Component).getString()
         var37.title(var41)
         val var39: VoidrixCheckbox = VoidrixCheckbox(0.7F)
         var39.margins(Insets.Companion.bottom(1))
         var39.checked(settings.spawnOnHit)
         var39.onChanged().subscribe({ checked: Boolean ->
            `$settings`.spawnOnHit = checked
            `$module`.saveSettings()
         })
         V3ValueLine.Companion.styleV3(var39 as UIComponent)
         var37.right(var39 as UIComponent)
         var30.child(var37 as UIComponent)
         container.child(var30 as UIComponent)
      }

      return container
   }

   @JvmStatic
   fun `build$rebuildList`(
      listScroll: <unrepresentable>,
      listChild: FlowLayout,
      searchField: VoidrixInputField,
      allIds: MutableList<java.lang.String>,
      `$module`: ParticleModule,
      preserveScroll: Boolean
   ) {
      val savedScroll: Double = if (preserveScroll) listScroll.scrollOffset else 0.0
      val savedPos: Double = if (preserveScroll) listScroll.currentScrollPosition else 0.0
      listChild.clearChildren()
      var var10000: java.lang.String = searchField.text().toLowerCase(Locale.ROOT)
      val query: java.lang.String = var10000
      val var23: java.util.List
      if (StringsKt.isBlank(var10000)) {
         var23 = allIds
      } else {
         val `$this$filterTo$iv$iv`: java.lang.Iterable = allIds
         val `destination$iv$iv`: java.util.Collection = ArrayList()

         for (`element$iv$iv` in `$this$filterTo$iv$iv`) {
            var10000 = (`element$iv$iv` as java.lang.String).toLowerCase(Locale.ROOT)
            if (StringsKt.contains$default(var10000, query, false, 2, null)) {
               `destination$iv$iv`.add(`element$iv$iv`)
            }
         }

         var23 = `destination$iv$iv` as java.util.List
      }

      for (var22 in var23) {
         listChild.child(INSTANCE.buildParticleRow(var22, `$module`) as UIComponent)
      }

      if (preserveScroll) {
         listScroll.scrollOffset = savedScroll
         listScroll.currentScrollPosition = savedPos
      }
   }
}
