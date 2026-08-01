package gg.voidrix.client.v2.modules.crosshair

import com.mojang.blaze3d.platform.Window
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.event.CrosshairRenderEvent
import gg.voidrix.compat.scale.IWindowScaleExt
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.BooleanValue
import gg.voidrix.ui.api.value.ColorAttributeValue
import gg.voidrix.ui.api.value.NumericValue
import gg.voidrix.ui.api.value.TextValue
import gg.voidrix.ui.api.value.Value
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixPixelGridDrawComponent
import java.awt.Color
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet
import java.util.LinkedHashMap
import java.util.Map.Entry
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.json.JsonElement
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import org.jetbrains.annotations.NotNull
import org.lwjgl.opengl.GL11

@SourceDebugExtension(["SMAP\nCustomCrosshair.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CustomCrosshair.kt\ngg/voidrix/client/v2/modules/crosshair/CustomCrosshair\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 OwoIdentifier.kt\ngg/voidrix/compat/resource/OwoIdentifierKt\n*L\n1#1,560:1\n295#2,2:561\n239#3:563\n40#3:564\n40#3:565\n239#3:566\n40#3:567\n185#3:568\n40#3:569\n239#3:570\n40#3:571\n1#4:572\n39#5:573\n39#5:574\n39#5:575\n*S KotlinDebug\n*F\n+ 1 CustomCrosshair.kt\ngg/voidrix/client/v2/modules/crosshair/CustomCrosshair\n*L\n175#1:561,2\n302#1:563\n302#1:564\n416#1:565\n418#1:566\n418#1:567\n436#1:568\n436#1:569\n356#1:570\n356#1:571\n431#1:573\n432#1:574\n433#1:575\n*E\n"])
public object CustomCrosshair : Module("Custom Crosshair", ModuleCategory.VISUAL, false, false, false, 28) {
   public open val seoTags: Array<String>

   private final var customPixelDataEncoded: String
      private final get() {
         return customPixelDataEncoded$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.String
      }

      private final set(<set-?>) {
         customPixelDataEncoded$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   private final var customBrushColorHex: String
      private final get() {
         return customBrushColorHex$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.String
      }

      private final set(<set-?>) {
         customBrushColorHex$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Editor")
   @NotNull
   public final val pixelEditor: Unit by object : Value<Unit> {
      public open val hasLabel: Boolean

      public open fun serialize(isDefault: Boolean): JsonElement? {
         return null
      }

      public open fun deserialize(data: JsonElement) {
      }

      public open fun buildCustomLine(verticalWrapper: FlowLayout): Boolean {
         val var10001: CustomCrosshairPixelSection = CustomCrosshairPixelSection
         var var10003: java.util.Map = CustomCrosshair.INSTANCE.getAppliedPixelData()
         if (var10003 == null) {
            var10003 = MapsKt.emptyMap()
         }

         var10001./* $VF: Unable to resugar constructor */<init>(var10003, CustomCrosshair.INSTANCE.getBrushColorRgb(), { it: java.util.Map ->
            CustomCrosshair.INSTANCE.setAppliedPixelData(it)
            Unit.INSTANCE
         }, { it: Int ->
            CustomCrosshair.INSTANCE.setBrushColorRgb(it)
            Unit.INSTANCE
         })
         verticalWrapper.child(var10001 as UIComponent)
         return true
      }
   }.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      public final get() {
         pixelEditor$delegate.getValue(this as ValueHolder, $$delegatedProperties[2])
         return Unit.INSTANCE
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val style: CrosshairStyle by ValueApiKt.enum$default(CrosshairStyle.CROSS, null, null, null, null, 30, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return style$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as CrosshairStyle
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val dynamicColor: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return dynamicColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val enemyColor: Color
      public final get() {
         return enemyColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as Color
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val invertedColor: Boolean by ValueApiKt.boolean$default(false, "Inverted Color", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return invertedColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val scaleIndicator: Boolean by ValueApiKt.boolean$default(true, "Scale Attack Indicator", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return scaleIndicator$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Boolean
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val f5: Boolean by ValueApiKt.boolean$default(false, "Show in Third Person", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return f5$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Boolean
      }


   @Category(name = "Strokes")
   @NotNull
   public final val dot: Boolean
      public final get() {
         return dot$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as java.lang.Boolean
      }


   @Category(name = "Strokes")
   @NotNull
   public final val color: Color
      public final get() {
         return color$delegate.getValue(this as ValueHolder, $$delegatedProperties[10]) as Color
      }


   @Category(name = "Strokes")
   @NotNull
   public final val thickness: Number
      public final get() {
         return thickness$delegate.getValue(this as ValueHolder, $$delegatedProperties[11]) as java.lang.Number
      }


   @Category(name = "Strokes")
   @NotNull
   public final val size: Number
      public final get() {
         return size$delegate.getValue(this as ValueHolder, $$delegatedProperties[12]) as java.lang.Number
      }


   @Category(name = "Strokes")
   @NotNull
   public final val gap: Number
      public final get() {
         return gap$delegate.getValue(this as ValueHolder, $$delegatedProperties[13]) as java.lang.Number
      }


   @Category(name = "Strokes")
   @NotNull
   public final val rotation: Number
      public final get() {
         return rotation$delegate.getValue(this as ValueHolder, $$delegatedProperties[14]) as java.lang.Number
      }


   @Category(name = "Strokes")
   @NotNull
   public final val scale: Number
      public final get() {
         return scale$delegate.getValue(this as ValueHolder, $$delegatedProperties[15]) as java.lang.Number
      }


   @Category(name = "Outline")
   @NotNull
   public final val outline: Boolean
      public final get() {
         return outline$delegate.getValue(this as ValueHolder, $$delegatedProperties[16]) as java.lang.Boolean
      }


   @Category(name = "Outline")
   @NotNull
   public final val outlineColor: Color
      public final get() {
         return outlineColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[17]) as Color
      }


   @Category(name = "Outline")
   @NotNull
   public final val outlineThickness: Number
      public final get() {
         return outlineThickness$delegate.getValue(this as ValueHolder, $$delegatedProperties[18]) as java.lang.Number
      }


   @Category(name = "Crosshair")
   @NotNull
   public final val customScale: Number
      public final get() {
         return customScale$delegate.getValue(this as ValueHolder, $$delegatedProperties[19]) as java.lang.Number
      }


   private final var appliedPixelData: Map<Pair<Int, Int>, Int>?

   public final var activeGrid: VoidrixPixelGridDrawComponent?
      internal set

   private final var cachedRects: List<gg.voidrix.client.v2.modules.crosshair.CustomCrosshair.PixelRect>?
   private final var cachedRectsSource: Map<Pair<Int, Int>, Int>?
   private final val ATTACK_INDICATOR_FULL: Identifier
   private final val ATTACK_INDICATOR_BG: Identifier
   private final val ATTACK_INDICATOR_PROGRESS: Identifier

   private final val isCustom: Boolean
      private final get() {
         return this.style === CrosshairStyle.CUSTOM
      }


   public fun getAppliedPixelData(): Map<Pair<Int, Int>, Int>? {
      if (appliedPixelData == null && this.customPixelDataEncoded.length() > 0) {
         appliedPixelData = this.deserializePixelData(this.customPixelDataEncoded)
      }

      return appliedPixelData
   }

   public fun setAppliedPixelData(data: Map<Pair<Int, Int>, Int>) {
      appliedPixelData = data
      cachedRects = null
      cachedRectsSource = null
      this.customPixelDataEncoded = this.serializePixelData(data)
   }

   public fun getBrushColorRgb(): Int {
      val var10000: Int = StringsKt.toIntOrNull(this.customBrushColorHex, 16)
      return var10000 ?: 16777215
   }

   public fun setBrushColorRgb(rgb: Int) {
      val var4: Array<Any> = arrayOf(rgb and 16777215)
      val var10001: java.lang.String = java.lang.String.format("%06X", Arrays.copyOf(var4, var4.length))
      this.customBrushColorHex = var10001
   }

   private fun serializePixelData(data: Map<Pair<Int, Int>, Int>): String {
      return CollectionsKt.joinToString$default(data.entrySet(), ";", null, null, 0, null, { var0: Entry ->
         val cell: Pair = var0.getKey() as Pair
         val rgb: Int = (var0.getValue() as java.lang.Number).intValue()
         val var10000: Any = cell.getFirst()
         val var10001: Any = cell.getSecond()
         val var5: Array<Any> = arrayOf(rgb and 16777215)
         val var10002: java.lang.String = java.lang.String.format("%06X", Arrays.copyOf(var5, var5.length))
         ("$var10000,$var10001,$var10002") as java.lang.CharSequence
      }, 30, null)
   }

   private fun deserializePixelData(encoded: String): Map<Pair<Int, Int>, Int> {
      if (StringsKt.isBlank(encoded)) {
         return MapsKt.emptyMap()
      } else {
         val result: java.util.Map = LinkedHashMap()

         for (var10 in StringsKt.split$default(encoded, arrayOf(";"), false, 0, 6, null)) {
            val tokens: java.util.List = StringsKt.split$default(var10, arrayOf(","), false, 3, 2, null)
            if (tokens.size() == 3) {
               var var13: Int = StringsKt.toIntOrNull(tokens.get(0) as java.lang.String)
               if (var13 != null) {
                  val var11: Int = var13
                  var13 = StringsKt.toIntOrNull(tokens.get(1) as java.lang.String)
                  if (var13 != null) {
                     val y: Int = var13
                     var13 = StringsKt.toIntOrNull(tokens.get(2) as java.lang.String, 16)
                     if (var13 != null) {
                        result.put(TuplesKt.to(var11, y), var13)
                     }
                  }
               }
            }
         }

         return result
      }
   }

   public open fun resetToDefault() {
      super.resetToDefault()
      appliedPixelData = null
      cachedRects = null
      cachedRectsSource = null
      if (activeGrid != null) {
         activeGrid.loadCells(MapsKt.emptyMap())
      }
   }

   public fun activateCustom() {
      if (!this.isCustom) {
         val var5: java.util.Iterator = this.getValues().iterator()

         var var10000: Any
         while (true) {
            if (var5.hasNext()) {
               val `element$iv`: Any = var5.next()
               if (!((`element$iv` as Value).getName() == "style")) {
                  continue
               }

               var10000 = `element$iv`
               break
            }

            var10000 = null
            break
         }

         val styleVal: Value = var10000 as? Value
         if ((var10000 as? Value) != null) {
            styleVal.set(CrosshairStyle.CUSTOM)
         }
      }
   }

   private fun isDynamicEnemyHit(): Boolean {
      return this.dynamicColor && MCClient.isLivingAndAlive(MCClient.getCrosshairEntity())
   }

   private fun useInvertedBlend(): Boolean {
      return this.invertedColor && !this.isDynamicEnemyHit()
   }

   private fun resolveColor(): Int {
      if (this.isCustom) {
         return -1
      } else {
         return if (this.isDynamicEnemyHit()) this.enemyColor.getRGB() else (if (this.invertedColor) -1 else this.color.getRGB())
      }
   }

   private fun decomposePixelsToRects(pixels: Map<Pair<Int, Int>, Int>): List<gg.voidrix.client.v2.modules.crosshair.CustomCrosshair.PixelRect> {
      if (pixels.isEmpty()) {
         return CollectionsKt.emptyList()
      } else {
         val visited: HashSet = HashSet(pixels.size())
         val rects: ArrayList = ArrayList()

         for (cell in CollectionsKt.sortedWith(pixels.keySet(), ComparisonsKt.compareBy(arrayOf({ it: Pair ->
            it.getSecond() as java.lang.Comparable
         }, { it: Pair ->
            it.getFirst() as java.lang.Comparable
         })))) {
            if (!visited.contains(cell)) {
               val x0: Int = (cell.component1() as java.lang.Number).intValue()
               val y0: Int = (cell.component2() as java.lang.Number).intValue()
               val color: Int = (MapsKt.getValue(pixels, cell) as java.lang.Number).intValue()
               var w: Int = 1

               while (true) {
                  val h: Pair = TuplesKt.to(x0 + w, y0)
                  if (visited.contains(h)) {
                     break
                  }

                  val var24: Int = pixels.get(h) as Int
                  if (var24 == null) {
                     break
                  }

                  if (var24 != color) {
                     break
                  }

                  w++
               }

               var var18: Int = 1

               label102@ while (true) {
                  val var19: Int = y0 + var18
                  var dx: Int = 0

                  for (dxx in w..dx) {
                     val c: Pair = TuplesKt.to(x0 + dx, var19)
                     if (visited.contains(c)) {
                        break@label102
                     }

                     val var25: Int = pixels.get(c) as Int
                     if (var25 == null) {
                        break@label102
                     }

                     if (var25 != color) {
                        break@label102
                     }
                  }

                  var18++
               }

               var var20: Int = 0

               for (var21 in var18..var20) {
                  var var22: Int = 0

                  for (var23 in w..var22) {
                     visited.add(TuplesKt.to(x0 + var22, y0 + var20))
                  }
               }

               rects.add(CustomCrosshair.PixelRect(x0, y0, w, var18, color))
            }
         }

         return rects
      }
   }

   private fun getDecomposedRects(): List<gg.voidrix.client.v2.modules.crosshair.CustomCrosshair.PixelRect> {
      val var10000: java.util.Map = this.getAppliedPixelData()
      if (var10000 == null) {
         return CollectionsKt.emptyList()
      } else if (cachedRects != null && cachedRectsSource === var10000) {
         return cachedRects
      } else {
         val computed: java.util.List = this.decomposePixelsToRects(var10000)
         cachedRects = computed
         cachedRectsSource = var10000
         return computed
      }
   }

   private fun renderCustomPixels(g: OwoUIGraphics) {
      val rects: java.util.List = this.getDecomposedRects()
      if (!rects.isEmpty()) {
         val isEnemy: Boolean = this.isDynamicEnemyHit()
         val inverted: Boolean = this.invertedColor && !isEnemy

         for (r in rects) {
            val pixelColor: Int = if (isEnemy) this.enemyColor.getRGB() else (if (inverted) -1 else r.color)
            val x1: Double = r.x - 7.5
            val y1: Double = r.y - 7.5
            val x2: Double = x1 + r.w
            val y2: Double = y1 + r.h
            if (inverted) {
               g.fillInverted(x1, y1, x2, y2, pixelColor)
            } else {
               g.fill(x1, y1, x2, y2, pixelColor or -16777216)
            }
         }
      }
   }

   private fun renderCrosshair(g: OwoUIGraphics, rawContext: Any?, screenWidth: Int, screenHeight: Int) {
      val centerX: Double = screenWidth / 2.0
      val centerY: Double = screenHeight / 2.0
      val drawColor: Int = this.resolveColor()
      val useInverted: Boolean = this.useInvertedBlend()
      if (useInverted) {
         this.setupInvertedBlend()
      }

      g.push()
      g.translate((float)centerX, (float)centerY)
      val var10000: Minecraft = Minecraft.getInstance()
      val mcGuiScale: Double = var10000.getWindow().getGuiScale() * this.getScaleDiff()
      val var16: Float = if (this.isCustom)
         RangesKt.coerceIn(this.customScale.floatValue(), if (mcGuiScale > 0.0) (float)(1.0 / mcGuiScale) else 0.1F, 3.0F)
         else
         RangesKt.coerceIn(this.scale.floatValue(), if (mcGuiScale > 0.0) (float)(1.0 / mcGuiScale) else 0.1F, 3.0F)
         g.scale(var16, var16)
      if (!this.isCustom) {
         g.rotate(this.rotation.floatValue())
      }

      if (this.isCustom) {
         this.renderCustomPixels(g)
      } else {
         val var17: CrosshairStyleRenderer = this.style.renderer
         if (var17 != null) {
            var17.draw(
               g,
               0.0,
               0.0,
               drawColor,
               this.thickness.intValue(),
               this.size.intValue(),
               this.gap.intValue(),
               this.dot,
               !useInverted && this.outline,
               this.outlineColor.getRGB(),
               this.outlineThickness.doubleValue(),
               useInverted
            )
         }
      }

      g.pop()
      if (useInverted) {
         this.teardownInvertedBlend()
      }

      this.drawAttackIndicator(rawContext, g, centerX, centerY)
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         settingsPanel.surface(
            { g: OwoUIGraphics, comp: ParentUIComponent ->
               if (`$existingSurface` != null) {
                  `$existingSurface`.draw(g, comp)
               }

               val wasScissor: Boolean = GL11.glIsEnabled(3089)
               if (wasScissor) {
                  GL11.glDisable(3089)
               }

               val cx: Double = g.guiWidth() / 2.0
               val cy: Double = g.guiHeight() / 2.0
               val scaleCompensation: Double = INSTANCE.getScaleDiff()
               val drawColor: Int = INSTANCE.resolveColor()
               val useInverted: Boolean = INSTANCE.useInvertedBlend()
               if (useInverted) {
                  INSTANCE.setupInvertedBlend()
               }

               g.push()
               g.translate((float)cx, (float)cy)
               val var10000: Minecraft = Minecraft.getInstance()
               val mcGuiScale: Double = var10000.getWindow().getGuiScale() * scaleCompensation
               val var17: Float = if (INSTANCE.isCustom)
                  (float)(
                     RangesKt.coerceIn(INSTANCE.customScale.floatValue(), if (mcGuiScale > 0.0) (float)(1.0 / mcGuiScale) else 0.1F, 3.0F) * scaleCompensation
                  )
                  else
                  (float)(RangesKt.coerceIn(INSTANCE.scale.floatValue(), if (mcGuiScale > 0.0) (float)(1.0 / mcGuiScale) else 0.1F, 3.0F) * scaleCompensation)
                  g.scale(var17, var17)
               if (!INSTANCE.isCustom) {
                  g.rotate(INSTANCE.rotation.floatValue())
               }

               if (INSTANCE.isCustom) {
                  INSTANCE.renderCustomPixels(g)
               } else {
                  val var18: CrosshairStyleRenderer = INSTANCE.style.renderer
                  if (var18 != null) {
                     var18.draw(
                        g,
                        0.0,
                        0.0,
                        drawColor,
                        INSTANCE.thickness.intValue(),
                        INSTANCE.size.intValue(),
                        INSTANCE.gap.intValue(),
                        INSTANCE.dot,
                        !useInverted && INSTANCE.outline,
                        INSTANCE.outlineColor.getRGB(),
                        INSTANCE.outlineThickness.doubleValue(),
                        useInverted
                     )
                  }
               }

               g.pop()
               if (useInverted) {
                  INSTANCE.teardownInvertedBlend()
               }

               if (wasScissor) {
                  GL11.glEnable(3089)
               }
            }
         )
         Unit.INSTANCE
      }
   }

   private fun setupInvertedBlend() {
   }

   private fun teardownInvertedBlend() {
   }

   private fun getDynamicColor(): Int {
      return if (MCClient.isLivingAndAlive(MCClient.getCrosshairEntity())) this.enemyColor.getRGB() else this.color.getRGB()
   }

   private fun getScaleDiff(): Double {
      var var10000: Minecraft = Minecraft.getInstance()
      val var6: Window = var10000.getWindow()
      val ext: IWindowScaleExt = var6 as IWindowScaleExt
      if ((var6 as IWindowScaleExt).getVoidrix_scaledResolution() == null) {
         return 1.0
      } else {
         var10000 = Minecraft.getInstance()
         val customScale: Double = var10000.getWindow().getGuiScale()
         return if (customScale > 0.0) ext.getVoidrix_mcScaleFactor() / customScale else 1.0
      }
   }

   private fun drawAttackIndicator(rawContext: Any?, g: OwoUIGraphics, centerX: Double, centerY: Double) {
      val var10000: Minecraft = Minecraft.getInstance()
      if (var10000.player != null) {
         val player: LocalPlayer = var10000.player
         if (MCClient.isAttackIndicatorCrosshair()) {
            val f: Float = player.getAttackStrengthScale(0.0F)
            var var16: Boolean = false
            val var17: Any = MCClient.getCrosshairEntity()
            if (var17 != null && MCClient.isLivingAndAlive(var17) && f >= 1.0F) {
               var16 = player.getCurrentItemAttackStrengthDelay() > 5.0F
            }

            val guiGraphics: GuiGraphicsExtractor = rawContext as GuiGraphicsExtractor
            val activeScale: Float = if (this.isCustom) this.customScale.floatValue() else this.scale.floatValue()
            if (this.scaleIndicator) {
               g.push()
               g.translate((float)centerX, (float)centerY)
               g.scale(RangesKt.coerceIn(activeScale, 0.1F, 3.0F), RangesKt.coerceIn(activeScale, 0.1F, 3.0F))
            }

            val j: Int = if (this.scaleIndicator) 9 else (int)centerY - 7 + 16
            val k: Int = if (this.scaleIndicator) -8 else (int)centerX - 8
            if (var16) {
               guiGraphics.blitSprite(RenderPipelines.CROSSHAIR, ATTACK_INDICATOR_FULL, k, j, 16, 16)
            } else if (f < 1.0F) {
               val l: Int = (int)(f * 17.0F)
               guiGraphics.blitSprite(RenderPipelines.CROSSHAIR, ATTACK_INDICATOR_BG, k, j, 16, 4)
               guiGraphics.blitSprite(RenderPipelines.CROSSHAIR, ATTACK_INDICATOR_PROGRESS, 16, 4, 0, 0, k, j, l, 4)
            }

            if (this.scaleIndicator) {
               g.pop()
            }
         }
      }
   }

   public open fun createdAt(): Long {
      return 1776412800000L
   }

   @JvmStatic
   fun {
      var var4: TextValue = ValueApiKt.text$default("", false, null, null, null, 30, null)
      var4.setHiddenInGui(true)
      customPixelDataEncoded$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      var4 = ValueApiKt.text$default("FFFFFF", false, null, null, null, 30, null)
      var4.setHiddenInGui(true)
      customBrushColorHex$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      var var10000: Color = Color.RED
      val var7: ColorAttributeValue = ValueApiKt.attribute$default(var10000, false, null, null, null, 28, null)
      var7.setUiCondition({ 
         INSTANCE.dynamicColor
      })
      enemyColor$delegate = var7.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
      val var8: BooleanValue = ValueApiKt.boolean$default(true, null, null, null, 14, null)
      var8.setUiCondition({ 
         !INSTANCE.isCustom
      })
      dot$delegate = var8.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
      var10000 = Color.WHITE
      val var9: ColorAttributeValue = ValueApiKt.attribute$default(var10000, true, null, null, null, 28, null)
      var9.setUiCondition({ 
         !INSTANCE.isCustom
      })
      color$delegate = var9.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[10])
      val var10: NumericValue = ValueApiKt.numeric$default(1, IntRange(1, 15) as ClosedRange, null, null, null, null, 60, null)
      var10.setUiCondition({ 
         !INSTANCE.isCustom
      })
      thickness$delegate = var10.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[11])
      val var11: NumericValue = ValueApiKt.numeric$default(4, IntRange(0, 15) as ClosedRange, null, null, null, null, 60, null)
      var11.setUiCondition({ 
         !INSTANCE.isCustom
      })
      size$delegate = var11.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[12])
      val var12: NumericValue = ValueApiKt.numeric$default(0, IntRange(0, 15) as ClosedRange, null, null, null, null, 60, null)
      var12.setUiCondition({ 
         !INSTANCE.isCustom
      })
      gap$delegate = var12.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[13])
      val var13: NumericValue = ValueApiKt.numeric$default(0, IntRange(0, 360) as ClosedRange, null, null, null, null, 60, null)
      var13.setUiCondition({ 
         !INSTANCE.isCustom
      })
      rotation$delegate = var13.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[14])
      val var14: NumericValue = ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.1, 3.0) as ClosedRange, 0.01, null, null, null, 56, null)
      var14.setUiCondition({ 
         !INSTANCE.isCustom
      })
      scale$delegate = var14.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[15])
      val var15: BooleanValue = ValueApiKt.boolean$default(false, null, null, null, 14, null)
      var15.setUiCondition({ 
         !INSTANCE.isCustom
      })
      outline$delegate = var15.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[16])
      var10000 = Color.BLACK
      val var16: ColorAttributeValue = ValueApiKt.attribute$default(var10000, true, null, null, null, 28, null)
      var16.setUiCondition({ 
         !INSTANCE.isCustom
      })
      outlineColor$delegate = var16.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[17])
      val var17: NumericValue = ValueApiKt.numeric$default(1.5, RangesKt.rangeTo(0.0, 2.0) as ClosedRange, 0.01, null, null, null, 56, null)
      var17.setUiCondition({ 
         !INSTANCE.isCustom
      })
      outlineThickness$delegate = var17.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[18])
      val var18: NumericValue = ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.1, 3.0) as ClosedRange, 0.01, null, null, null, 56, null)
      var18.setUiCondition({ 
         INSTANCE.isCustom
      })
      customScale$delegate = var18.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[19])
      CrosshairRenderEvent.Companion
         .getEvent()
         .listen(
            lambda_28@{ event: CrosshairRenderEvent ->
               if (!INSTANCE.isEnabled()) {
                  return@lambda_28 Unit.INSTANCE
               } else {
                  event.setCancelled(true)
                  if (!MCClient.isFirstPerson() && !INSTANCE.f5) {
                     return@lambda_28 Unit.INSTANCE
                  } else {
                     INSTANCE.renderCrosshair(
                        OwoUIGraphics.Companion.of(event.getRawContext()), event.getRawContext(), event.getScreenWidth(), event.getScreenHeight()
                     )
                     return@lambda_28 Unit.INSTANCE
                  }
               }
            }
         )
         val var53: Identifier = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_full")
      ATTACK_INDICATOR_FULL = var53
      val var54: Identifier = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background")
      ATTACK_INDICATOR_BG = var54
      val var55: Identifier = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress")
      ATTACK_INDICATOR_PROGRESS = var55
   }

   private data class PixelRect(x: Int, y: Int, w: Int, h: Int, color: Int) {
      public final val x: Int
      public final val y: Int
      public final val w: Int
      public final val h: Int
      public final val color: Int

      init {
         this.x = x
         this.y = y
         this.w = w
         this.h = h
         this.color = color
      }

      public operator fun component1(): Int {
         return this.x
      }

      public operator fun component2(): Int {
         return this.y
      }

      public operator fun component3(): Int {
         return this.w
      }

      public operator fun component4(): Int {
         return this.h
      }

      public operator fun component5(): Int {
         return this.color
      }

      public fun copy(x: Int = this.x, y: Int = this.y, w: Int = this.w, h: Int = this.h, color: Int = this.color): gg.voidrix.client.v2.modules.crosshair.CustomCrosshair.PixelRect {
         return CustomCrosshair.PixelRect(x, y, w, h, color)
      }

      public override fun toString(): String {
         return "PixelRect(x=${this.x}, y=${this.y}, w=${this.w}, h=${this.h}, color=${this.color})"
      }

      public override fun hashCode(): Int {
         return (((Integer.hashCode(this.x) * 31 + Integer.hashCode(this.y)) * 31 + Integer.hashCode(this.w)) * 31 + Integer.hashCode(this.h)) * 31
            + Integer.hashCode(this.color)
         }

      public override operator fun equals(other: Any?): Boolean {
         label46@
         if (this === other) {
            return true
         } else {
            return other is CustomCrosshair.PixelRect
               && this.x == (other as CustomCrosshair.PixelRect).x
               && this.y == (other as CustomCrosshair.PixelRect).y
               && this.w == (other as CustomCrosshair.PixelRect).w
               && this.h == (other as CustomCrosshair.PixelRect).h
               && this.color == (other as CustomCrosshair.PixelRect).color
            }
      }
   }
}
