package gg.voidrix.client.v2.modules.keystrokes

import gg.voidrix.client.v2.modules.cps.CPS
import gg.voidrix.compat.event.KeyEventData
import gg.voidrix.compat.event.KeyEvents
import gg.voidrix.compat.event.MouseClickEventData
import gg.voidrix.compat.event.MouseEvents
import gg.voidrix.compat.resource.MCKey
import gg.voidrix.compat.resource.MCKeyType
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.RainbowTextUtilsKt
import gg.voidrix.compat.text.TextKt
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.UIContainers
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.ParentUIComponent
import gg.voidrix.owolib.owo.ui.core.Positioning
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.hud.AbstractHud
import gg.voidrix.ui.api.hud.DynamicBackground
import gg.voidrix.ui.api.hud.IContentBackground
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.api.value.GenericValue
import gg.voidrix.ui.api.value.NumericValue
import gg.voidrix.ui.api.value.Value
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.components.voidrix.VoidrixMultiColorPicker
import gg.voidrix.ui.modules.v3.V3Button
import gg.voidrix.ui.utils.OwoLibExtensions
import gg.voidrix.ui.v2.hud.Background.CornerStyle
import gg.voidrix.ui.v2.hud.Background.Type
import java.awt.Color
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Arrays
import java.util.NoSuchElementException
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayListSerializer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.jetbrains.annotations.NotNull
import org.joml.Matrix4f

@SourceDebugExtension(["SMAP\nKeystrokes.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Keystrokes.kt\ngg/voidrix/client/v2/modules/keystrokes/Keystrokes\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 5 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 6 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,691:1\n295#2,2:692\n295#2,2:694\n295#2,2:696\n295#2,2:698\n1563#2:701\n1634#2,3:702\n1563#2:705\n1634#2,3:706\n1563#2:709\n1634#2,3:710\n1#3:700\n1310#4,2:713\n1310#4,2:715\n1310#4,2:717\n355#5:719\n40#5:720\n356#5:721\n67#6:722\n269#6:723\n*S KotlinDebug\n*F\n+ 1 Keystrokes.kt\ngg/voidrix/client/v2/modules/keystrokes/Keystrokes\n*L\n57#1:692,2\n62#1:694,2\n67#1:696,2\n74#1:698,2\n91#1:701\n91#1:702,3\n102#1:705\n102#1:706,3\n106#1:709\n106#1:710,3\n666#1:713,2\n673#1:715,2\n680#1:717,2\n123#1:719\n123#1:720\n123#1:721\n135#1:722\n135#1:723\n*E\n"])
public object Keystrokes : AbstractHud("Keystrokes", false, false, false, 14), IContentBackground {
   public open val seoTags: Array<String>
   private final var keyWrapper: gg.voidrix.client.v2.modules.keystrokes.Keystrokes.GridKeyWrapper?
   internal final var designerPreviewWrapper: gg.voidrix.client.v2.modules.keystrokes.Keystrokes.GridKeyWrapper?
   internal final var designerScreenOpen: Boolean

   private final var storedLayout: gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredLayout
      private final get() {
         return storedLayout$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as Keystrokes.StoredLayout
      }

      private final set(<set-?>) {
         storedLayout$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public open var dynamicBackground: DynamicBackground = DynamicBackground(false, 0, 0, 0, 0, 31, null)
      internal final set

   @Category(name = "Display")
   @NotNull
   public final var multiColor: MultiColor by ValueApiKt.generic$default({ 
      MultiColor(null, false, false, 7, null)
   }, MultiColor.Companion.serializer(), "Color", null, null, null, { var0: ParentUIComponent, var1: Function0 ->
      VoidrixMultiColorPicker(INSTANCE.multiColor, null, null, null, 14, null) as UIComponent
   }, 56, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return multiColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MultiColor
      }

      public final set(<set-?>) {
         multiColor$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @Category(name = "Settings")
   @NotNull
   public final val cellPx: Number
      public final get() {
         return cellPx$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val fontSize: Number by ValueApiKt.numeric$default(10.0, RangesKt.rangeTo(6.0, 24.0) as ClosedRange, 0.5, "Font Size", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return fontSize$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val keyGap: Number by ValueApiKt.numeric$default(
         2.0, RangesKt.rangeTo(0.0, 10.0) as ClosedRange, 0.5, "Key Gap", null, { it: java.lang.Number ->
            INSTANCE.rebuildAllWrappers()
            Unit.INSTANCE
         }, 16, null
      )
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return keyGap$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Number
      }


   @Category(name = "Settings")
   @NotNull
   public final val pressBackgroundColor: Color by ValueApiKt.attribute$default(Color(255, 255, 255, 93), false, "Press Background", null, null, 26, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return pressBackgroundColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as Color
      }


   @Category(name = "Settings")
   @NotNull
   public final val pressTextColor: Color
      public final get() {
         return pressTextColor$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as Color
      }


   @Category(name = "Settings")
   @NotNull
   public final val showCps: Boolean by ValueApiKt.boolean$default(false, "Show CPS", null, { it: Boolean ->
      INSTANCE.rebuildAllWrappers()
      Unit.INSTANCE
   }, 4, null).provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
      public final get() {
         return showCps$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Boolean
      }


   @Category(name = "Settings")
   @NotNull
   public final val fadeDelayMs: Number by ValueApiKt.numeric$default(75, IntRange(0, 500) as ClosedRange, 5, "Key Fade Delay", null, null, 48, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return fadeDelayMs$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Number
      }


   private final val poseMethodsCache: Array<Method?>

   private fun rebuildAllWrappers() {
      if (keyWrapper != null) {
         keyWrapper.rebuildLayout()
      }

      if (designerPreviewWrapper != null) {
         designerPreviewWrapper.rebuildLayout()
      }
   }

   internal fun setCellPx(value: Int) {
      val var5: java.util.Iterator = this.getValues().iterator()

      var var10000: Any
      while (true) {
         if (var5.hasNext()) {
            val `element$iv`: Any = var5.next()
            if (!((`element$iv` as Value).getName() == "cellPx")) {
               continue
            }

            var10000 = (Value)`element$iv`
            break
         }

         var10000 = null
         break
      }

      var10000 = var10000 as? Value
      if ((var10000 as? Value) != null) {
         var10000.set(value)
      }
   }

   internal fun setKeyGap(value: Double) {
      val var6: java.util.Iterator = this.getValues().iterator()

      var var10000: Any
      while (true) {
         if (var6.hasNext()) {
            val `element$iv`: Any = var6.next()
            if (!((`element$iv` as Value).getName() == "keyGap")) {
               continue
            }

            var10000 = (Value)`element$iv`
            break
         }

         var10000 = null
         break
      }

      var10000 = var10000 as? Value
      if ((var10000 as? Value) != null) {
         var10000.set(value)
      }
   }

   internal fun setShowCps(value: Boolean) {
      val var5: java.util.Iterator = this.getValues().iterator()

      var var10000: Any
      while (true) {
         if (var5.hasNext()) {
            val `element$iv`: Any = var5.next()
            if (!((`element$iv` as Value).getName() == "showCps")) {
               continue
            }

            var10000 = (Value)`element$iv`
            break
         }

         var10000 = null
         break
      }

      var10000 = var10000 as? Value
      if ((var10000 as? Value) != null) {
         var10000.set(value)
      }
   }

   internal fun setHudScale(value: Float) {
      val var5: java.util.Iterator = this.getValues().iterator()

      var var10000: Any
      while (true) {
         if (var5.hasNext()) {
            val `element$iv`: Any = var5.next()
            if (!((`element$iv` as Value).getName() == "hudScale")) {
               continue
            }

            var10000 = (Value)`element$iv`
            break
         }

         var10000 = null
         break
      }

      var10000 = var10000 as? Value
      if ((var10000 as? Value) != null) {
         var10000.set(value)
      }
   }

   private fun defaultStoredLayout(): gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredLayout {
      val keys: java.util.List = KeystrokePresets.INSTANCE.WASD.keys
      val ox: java.util.Iterator = keys.iterator()
      if (!ox.hasNext()) {
         throw NoSuchElementException()
      } else {
         val oy: KeystrokePreset.PresetEntry = ox.next() as KeystrokePreset.PresetEntry
         var var20: Int = oy.cellX + oy.widthCells

         while (ox.hasNext()) {
            val itx: KeystrokePreset.PresetEntry = ox.next() as KeystrokePreset.PresetEntry
            val var24: Int = itx.cellX + itx.widthCells
            if (var20 < var24) {
               var20 = var24
            }
         }

         val var21: java.util.Iterator = keys.iterator()
         if (!var21.hasNext()) {
            throw NoSuchElementException()
         } else {
            val itx: KeystrokePreset.PresetEntry = var21.next() as KeystrokePreset.PresetEntry
            var var26: Int = itx.cellY + itx.heightCells

            while (var21.hasNext()) {
               val itxx: KeystrokePreset.PresetEntry = var21.next() as KeystrokePreset.PresetEntry
               val var30: Int = itxx.cellY + itxx.heightCells
               if (var26 < var30) {
                  var26 = var30
               }
            }

            val var19: Int = (30 - var20) / 2
            val var22: Int = (16 - var26) / 2
            val var32: java.lang.Iterable = keys
            val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(keys, 10))

            for (`item$iv$iv` in var32) {
               `destination$iv$iv`.add(
                  Keystrokes.StoredKey(
                     (`item$iv$iv` as KeystrokePreset.PresetEntry).cellX + var19,
                     (`item$iv$iv` as KeystrokePreset.PresetEntry).cellY + var22,
                     (`item$iv$iv` as KeystrokePreset.PresetEntry).widthCells,
                     (`item$iv$iv` as KeystrokePreset.PresetEntry).heightCells,
                     (`item$iv$iv` as KeystrokePreset.PresetEntry).keyName
                  )
               )
            }

            return Keystrokes.StoredLayout(`destination$iv$iv` as MutableList<Keystrokes.StoredKey>)
         }
      }
   }

   public final var currentLayout: MutableList<PlacedKey>
      public final get() {
         val `$this$map$iv`: java.lang.Iterable = this.storedLayout.keys
         val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(`$this$map$iv`, 10))

         for (`item$iv$iv` in `$this$map$iv`) {
            `destination$iv$iv`.add(
               PlacedKey(
                  (`item$iv$iv` as Keystrokes.StoredKey).x,
                  (`item$iv$iv` as Keystrokes.StoredKey).y,
                  (`item$iv$iv` as Keystrokes.StoredKey).w,
                  (`item$iv$iv` as Keystrokes.StoredKey).h,
                  MCKey.Companion.fromName((`item$iv$iv` as Keystrokes.StoredKey).keyName)
               )
            )
         }

         return CollectionsKt.toMutableList(`destination$iv$iv` as java.util.List)
      }

      public final set(value) {
         val `$this$map$iv`: java.lang.Iterable = value
         val `destination$iv$iv`: java.util.Collection = ArrayList(CollectionsKt.collectionSizeOrDefault(value, 10))

         for (`item$iv$iv` in `$this$map$iv`) {
            `destination$iv$iv`.add(
               Keystrokes.StoredKey(
                  (`item$iv$iv` as PlacedKey).cellX,
                  (`item$iv$iv` as PlacedKey).cellY,
                  (`item$iv$iv` as PlacedKey).widthCells,
                  (`item$iv$iv` as PlacedKey).heightCells,
                  (`item$iv$iv` as PlacedKey).key.getName()
               )
            )
         }

         this.storedLayout = Keystrokes.StoredLayout(`destination$iv$iv` as MutableList<Keystrokes.StoredKey>)
      }


   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return lambda_18@{ settingsPanel: FlowLayout ->
         if (designerScreenOpen) {
            return@lambda_18 Unit.INSTANCE
         } else {
            val openEditor: Function0 = { 
               UISounds.playButtonSound()
               val `screen$iv`: Screen = KeystrokeLayoutEditorScreen() as Screen
               val var10000: Minecraft = Minecraft.getInstance()
               var10000.gui.setScreen(`screen$iv`)
               Unit.INSTANCE
            }
            val var3: FlowLayout = UIContainers.horizontalFlow(Sizing.Companion.content(), Sizing.Companion.content())
            var3.gap(8)
            var3.verticalAlignment(VerticalAlignment.CENTER)
            var3.padding(Insets.Companion.of(4, 6, 4, 4))
            var3.child(KeystrokesLayoutPreviewComponent(130, 64, 0, 0, { 
               INSTANCE.currentLayout
            }, { 
               `$openEditor`()
               Unit.INSTANCE
            }, 12, null) as UIComponent)
            val `$this$buildCustomUi_u24lambda_u2418_u24lambda_u2417_u24lambda_u2416`: Array<Any> = arrayOfNulls(0)
            val var10003: MutableComponent = Component.translatable(
               "voidrix.ui.modules.keystrokes.customize",
               Arrays.copyOf(
                  `$this$buildCustomUi_u24lambda_u2418_u24lambda_u2417_u24lambda_u2416`,
                  `$this$buildCustomUi_u24lambda_u2418_u24lambda_u2417_u24lambda_u2416`.length
               )
            )
            val var15: java.lang.String = (TextKt.toSmallCapsText(var10003 as Component) as Component).getString()
            val var11: V3Button = V3Button(var15, { var1: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
               `$openEditor`()
               Unit.INSTANCE
            })
            var11.padding(Insets.Companion.of(5, 5, 12, 12))
            var3.child(var11 as UIComponent)
            settingsPanel.child(0, var3 as UIComponent)
            return@lambda_18 Unit.INSTANCE
         }
      }
   }

   public open fun getDefaultDynamicBackground(): DynamicBackground {
      return DynamicBackground(true, 0, 0, 0, 0, 24, null)
   }

   public open fun hudComponent(): UIComponent {
      val wrapper: Keystrokes.GridKeyWrapper = Keystrokes.GridKeyWrapper()
      keyWrapper = wrapper
      return wrapper as UIComponent
   }

   private fun readPoseMatrix(context: OwoUIGraphics): FloatArray? {
      val var2: Keystrokes = this

      var `$this$readPoseMatrix_u24lambda_u2430`: Keystrokes
      try {
         `$this$readPoseMatrix_u24lambda_u2430` = var2
         var ps: Method = poseMethodsCache[0]
         if (poseMethodsCache[0] == null) {
            var var10000: Method = context.getClass().getMethods()
            val lastM: Array<Any> = var10000 as Array<Any>
            var poseM: Int = 0
            val matrix: Int = lastM.length

            while (true) {
               if (poseM >= matrix) {
                  var10000 = null
                  break
               }

               val `$this$firstOrNull$iv`: Any = lastM[poseM]
               if ((lastM[poseM] as Method).getName() == "poseStack" && (lastM[poseM] as Method).getParameterCount() == 0) {
                  var10000 = (Method)`$this$firstOrNull$iv`
                  break
               }

               poseM++
            }

            var10000 = var10000
            if (var10000 == null) {
               return null
            }

            ps = var10000
            poseMethodsCache[0] = var10000
         }

         var var39: Any = ps.invoke(context)
         if (var39 == null) {
            return null
         }

         var var21: Method = poseMethodsCache[1]
         if (poseMethodsCache[1] == null) {
            var39 = var39.getClass().getMethods()
            val var23: Array<Any> = var39 as Array<Any>
            var var27: Int = 0
            val var30: Int = var23.length

            while (true) {
               if (var27 >= var30) {
                  var39 = null
                  break
               }

               val var33: Any = var23[var27]
               if ((var23[var27] as Method).getName() == "last" && (var23[var27] as Method).getParameterCount() == 0) {
                  var39 = var33
                  break
               }

               var27++
            }

            var39 = var39 as Method
            if (var39 as Method == null) {
               return null
            }

            var21 = (Method)var39
            poseMethodsCache[1] = (Method)var39
         }

         var39 = var21.invoke(var39)
         if (var39 == null) {
            return null
         }

         var var24: Method = poseMethodsCache[2]
         if (poseMethodsCache[2] == null) {
            val var44: Array<Method> = var39.getClass().getMethods()
            val var28: Array<Any> = var44
            var var34: Int = 0
            val var35: Int = var28.length

            while (true) {
               if (var34 >= var35) {
                  var39 = null
                  break
               }

               val var36: Any = var28[var34]
               if ((var28[var34] as Method).getName() == "pose" && (var28[var34] as Method).getParameterCount() == 0) {
                  var39 = var36
                  break
               }

               var34++
            }

            var39 = var39 as Method
            if (var39 as Method == null) {
               return null
            }

            var24 = (Method)var39
            poseMethodsCache[2] = (Method)var39
         }

         val var32: Any = var24.invoke(var39)
         val var47: Matrix4f = var32 as? Matrix4f
         if ((var32 as? Matrix4f) == null) {
            return null
         }

         `$this$readPoseMatrix_u24lambda_u2430` = (Keystrokes)Result.constructor_impl/* $VF was: constructor-impl */(
            floatArrayOf(var47.m00(), var47.m11(), var47.m30(), var47.m31())
         )
      } catch (var18: java.lang.Throwable) {
         `$this$readPoseMatrix_u24lambda_u2430` = (Keystrokes)Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var18))
      }

      return (if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$readPoseMatrix_u24lambda_u2430`)) null else `$this$readPoseMatrix_u24lambda_u2430`) as FloatArray
   }

   public open fun createdAt(): Long {
      return 1742169600000L
   }

   @JvmStatic
   fun {
      val var4: GenericValue = ValueApiKt.generic$default({ 
         INSTANCE.defaultStoredLayout()
      }, Keystrokes.StoredLayout.Companion.serializer(), null, null, { it: Keystrokes.StoredLayout ->
         INSTANCE.rebuildAllWrappers()
         Unit.INSTANCE
      }, null, null, 108, null)
      var4.setHiddenInGui(true)
      storedLayout$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      val var5: NumericValue = ValueApiKt.numeric$default(20, IntRange(10, 40) as ClosedRange, null, "Key Size", null, { it: java.lang.Number ->
         INSTANCE.rebuildAllWrappers()
         Unit.INSTANCE
      }, 20, null)
      var5.setHiddenInGui(true)
      cellPx$delegate = var5.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      val var10000: Color = Color.WHITE
      pressTextColor$delegate = ValueApiKt.attribute$default(var10000, true, "Press Text Color", null, null, 24, null)
         .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         MouseEvents.INSTANCE.getMouseClickEvent().listen(lambda_25@{ event: MouseClickEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_25 Unit.INSTANCE
         } else {
            if (keyWrapper != null) {
               val var10000: java.util.List = keyWrapper.getKeyComponents()
               if (var10000 != null) {
                  for (keyComp in var10000) {
                     val k: MCKey = keyComp.key
                     if (k.getType() === MCKeyType.MOUSE && !k.isUnknown() && k.getCode() == event.getButton()) {
                        keyComp.isPressed = event.getAction() == 1
                     }
                  }

                  return@lambda_25 Unit.INSTANCE
               }
            }

            return@lambda_25 Unit.INSTANCE
         }
      })
      KeyEvents.INSTANCE.getKeyEvent().listen(lambda_26@{ event: KeyEventData ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_26 Unit.INSTANCE
         } else {
            if (keyWrapper != null) {
               val var10000: java.util.List = keyWrapper.getKeyComponents()
               if (var10000 != null) {
                  for (keyComp in var10000) {
                     val k: MCKey = keyComp.key
                     if (k.getType() === MCKeyType.KEYBOARD && !k.isUnknown() && k.getCode() == event.getKey()) {
                        keyComp.isPressed = event.getAction() != 0
                     }
                  }

                  return@lambda_26 Unit.INSTANCE
               }
            }

            return@lambda_26 Unit.INSTANCE
         }
      })
   }

   @SourceDebugExtension(["SMAP\nKeystrokes.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Keystrokes.kt\ngg/voidrix/client/v2/modules/keystrokes/Keystrokes$GridKeyWrapper\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,691:1\n1#2:692\n*E\n"])
   internal class GridKeyWrapper : FlowLayout(Sizing.Companion.content(), Sizing.Companion.content(), Algorithm.VERTICAL) {
      private final val keyComps: MutableList<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.SimpleKeyComponent> = ArrayList() as java.util.List

      init {
         this.allowOverflow(true)
         this.rebuildLayout()
      }

      protected open fun drawChildren(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float, children: List<UIComponent>) {
         if (!children.isEmpty()) {
            for (c in children) {
               if (c is Keystrokes.SimpleKeyComponent) {
                  (c as Keystrokes.SimpleKeyComponent).drawSurface$voidrix_client(context, mouseX, mouseY, partialTicks, delta)
               }
            }

            var var10000: GuiGraphicsExtractor = context as? GuiGraphicsExtractor
            if ((context as? GuiGraphicsExtractor) != null) {
               var10000.nextStratum()
            }

            for (var11 in children) {
               if (var11 is Keystrokes.SimpleKeyComponent) {
                  (var11 as Keystrokes.SimpleKeyComponent).drawPressOverlay$voidrix_client(context)
               }
            }

            var10000 = context as? GuiGraphicsExtractor
            if ((context as? GuiGraphicsExtractor) != null) {
               var10000.nextStratum()
            }

            for (var12 in children) {
               if (var12 is Keystrokes.SimpleKeyComponent) {
                  (var12 as Keystrokes.SimpleKeyComponent).drawForeground$voidrix_client(context, mouseX, mouseY, partialTicks, delta)
               }
            }
         }
      }

      public fun rebuildLayout() {
         this.clearChildren()
         this.keyComps.clear()
         val layout: java.util.List = Keystrokes.INSTANCE.currentLayout
         val cs: Int = Keystrokes.INSTANCE.cellPx.intValue()
         val halfCs: Int = cs / 2
         val gap: Int = (int)RangesKt.coerceAtLeast(Keystrokes.INSTANCE.keyGap.doubleValue(), 0.0)
         if (layout.isEmpty()) {
            val var14: Int = cs * 2
            this.horizontalSizing(Sizing.Companion.fixed(cs * 2))
            this.verticalSizing(Sizing.Companion.fixed(cs))
            val var17: Keystrokes.SimpleKeyComponent = Keystrokes.SimpleKeyComponent(
               MCKey.Companion.getUNKNOWN(), Sizing.Companion.fixed(var14), Sizing.Companion.fixed(cs)
            )
            var17.positioning(Positioning.Companion.absolute(0, 0))
            this.keyComps.add(var17)
            this.child(var17 as UIComponent)
         } else {
            val totalSubW: java.util.Iterator = layout.iterator()
            if (!totalSubW.hasNext()) {
               throw NoSuchElementException()
            } else {
               var var18: Int = (totalSubW.next() as PlacedKey).cellX

               while (totalSubW.hasNext()) {
                  val var22: Int = (totalSubW.next() as PlacedKey).cellX
                  if (var18 > var22) {
                     var18 = var22
                  }
               }

               val minCellX: Int = var18
               val var19: java.util.Iterator = layout.iterator()
               if (!var19.hasNext()) {
                  throw NoSuchElementException()
               } else {
                  var var24: Int = (var19.next() as PlacedKey).cellY

                  while (var19.hasNext()) {
                     val var29: Int = (var19.next() as PlacedKey).cellY
                     if (var24 > var29) {
                        var24 = var29
                     }
                  }

                  val minCellY: Int = var24
                  val var25: java.util.Iterator = layout.iterator()
                  if (!var25.hasNext()) {
                     throw NoSuchElementException()
                  } else {
                     var var31: Int = (var25.next() as PlacedKey).right

                     while (var25.hasNext()) {
                        val var36: Int = (var25.next() as PlacedKey).right
                        if (var31 < var36) {
                           var31 = var36
                        }
                     }

                     val var16: Int = var31 - var18
                     val var32: java.util.Iterator = layout.iterator()
                     if (!var32.hasNext()) {
                        throw NoSuchElementException()
                     } else {
                        var var38: Int = (var32.next() as PlacedKey).bottom

                        while (var32.hasNext()) {
                           val var42: Int = (var32.next() as PlacedKey).bottom
                           if (var38 < var42) {
                              var38 = var42
                           }
                        }

                        var18 = var38 - var24
                        this.horizontalSizing(Sizing.Companion.fixed(var16 * halfCs + Math.max(0, var16 - 1) * gap))
                        this.verticalSizing(Sizing.Companion.fixed(var18 * halfCs + Math.max(0, var18 - 1) * gap))

                        for (var33 in layout) {
                           val var44: Keystrokes.SimpleKeyComponent = Keystrokes.SimpleKeyComponent(
                              var33.key,
                              Sizing.Companion.fixed(var33.widthCells * halfCs + Math.max(0, var33.widthCells - 1) * gap),
                              Sizing.Companion.fixed(var33.heightCells * halfCs + Math.max(0, var33.heightCells - 1) * gap)
                           )
                           var44.positioning(
                              Positioning.Companion.absolute((var33.cellX - minCellX) * (halfCs + gap), (var33.cellY - minCellY) * (halfCs + gap))
                           )
                           this.keyComps.add(var44)
                           this.child(var44 as UIComponent)
                        }
                     }
                  }
               }
            }
         }
      }

      public fun getKeyComponents(): List<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.SimpleKeyComponent> {
         return this.keyComps
      }
   }

   @SourceDebugExtension(["SMAP\nKeystrokes.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Keystrokes.kt\ngg/voidrix/client/v2/modules/keystrokes/Keystrokes$SimpleKeyComponent\n+ 2 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClientKt\n+ 4 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n+ 5 TextBuilder.kt\ngg/voidrix/compat/text/LiteralTextBuilder\n*L\n1#1,691:1\n40#2:692\n127#2:693\n40#2:694\n239#2:696\n40#2:697\n941#3:695\n941#3:713\n8#4,4:698\n78#5,6:702\n72#5,4:708\n87#5:712\n*S KotlinDebug\n*F\n+ 1 Keystrokes.kt\ngg/voidrix/client/v2/modules/keystrokes/Keystrokes$SimpleKeyComponent\n*L\n423#1:692\n477#1:693\n477#1:694\n490#1:696\n490#1:697\n478#1:695\n507#1:713\n502#1:698,4\n502#1:702,6\n502#1:708,4\n502#1:712\n*E\n"])
   internal class SimpleKeyComponent(key: MCKey, horizontalSizing: Sizing, verticalSizing: Sizing) : FlowLayout(
         horizontalSizing, verticalSizing, Algorithm.VERTICAL
      ) {
      public final val key: MCKey

      public final var isPressed: Boolean
         public final set(value) {
            if (value) {
               this.isPressed = true
            } else if (this.isPressed) {
               this.isPressed = false
               this.releaseTime = System.currentTimeMillis()
            }
         }


      private final var releaseTime: Long

      init {
         this.key = key
         this.allowOverflow(true)
         this.surface(Keystrokes.INSTANCE.getBackground().toSurface())
      }

      private final val pressAlpha: Float
         private final get() {
            if (this.isPressed) {
               return 1.0F
            } else {
               val delay: Long = Keystrokes.INSTANCE.fadeDelayMs.longValue()
               if (delay <= 0L) {
                  return 0.0F
               } else {
                  val elapsed: Long = System.currentTimeMillis() - this.releaseTime
                  return if (elapsed >= delay) 0.0F else 1.0F - (float)elapsed / (float)delay
               }
            }
         }


      private final val showsCps: Boolean
         private final get() {
            return this.key.getType() === MCKeyType.MOUSE && !this.key.isUnknown() && Keystrokes.INSTANCE.showCps
         }


      private fun getMainText(): String {
         return KeystrokeGridEditorKt.shortKeyLabel(this.key)
      }

      private fun getCpsText(): String {
         var var10000: Int
         when (this.key.getCode()) {
            0 -> var10000 = CPS.INSTANCE.getLeftCps()
            1 -> var10000 = CPS.INSTANCE.getRightCps()
            else -> var10000 = 0
         }

         return java.lang.String.valueOf(var10000)
      }

      private fun updateSurface() {
         this.surface(this.resolveBackgroundSurface())
      }

      private fun resolveBackgroundSurface(): Surface {
         if (Keystrokes.INSTANCE.getBackground().getType() === Type.VANILLA) {
            val radius: Float = Keystrokes.INSTANCE.getBackground().getRadius()
            val pixelated: Boolean = Keystrokes.INSTANCE.getBackground().getCornerStyle() === CornerStyle.PIXELATED
            val var4: Keystrokes.SimpleKeyComponent = this

            var `$this$resolveBackgroundSurface_u24lambda_u240`: Keystrokes.SimpleKeyComponent
            try {
               `$this$resolveBackgroundSurface_u24lambda_u240` = var4
               val var10000: Minecraft = Minecraft.getInstance()
               `$this$resolveBackgroundSurface_u24lambda_u240` = (Keystrokes.SimpleKeyComponent)Result.constructor_impl/* $VF was: constructor-impl */(
                  var10000.options.getBackgroundColor(0.4F)
               )
            } catch (var8: java.lang.Throwable) {
               `$this$resolveBackgroundSurface_u24lambda_u240` = (Keystrokes.SimpleKeyComponent)Result.constructor_impl/* $VF was: constructor-impl */(
                  ResultKt.createFailure(var8)
               )
            }

            val color: Int = ((
                  if (Result.isFailure_impl/* $VF was: isFailure-impl */(`$this$resolveBackgroundSurface_u24lambda_u240`))
                     1711276032
                     else
                     `$this$resolveBackgroundSurface_u24lambda_u240`
               ) as java.lang.Number)
               .intValue()
               return if (pixelated) Surface.Companion.pixelRoundedRect(color, radius) else Surface.Companion.roundedRect(color, radius)
         } else {
            return Keystrokes.INSTANCE.getBackground().toSurface()
         }
      }

      private fun drawKeyText(
         context: OwoUIGraphics,
         text: String,
         bx: Double,
         by: Double,
         bw: Double,
         bh: Double,
         useKeyStyling: Boolean = false,
         maxScaleOverride: Float? = null,
         fitHeightOverride: Double? = null
      ) {
         if (text.length() != 0 && !(bw <= 0.0) && !(bh <= 0.0)) {
            val isSpace: Boolean = useKeyStyling && this.key.getName() == "key.keyboard.space"
            val useRainbow: Boolean = !this.isPressed && Keystrokes.INSTANCE.multiColor.isRainbow()
            val color: Int = if (this.isPressed)
               Keystrokes.INSTANCE.pressTextColor.getRGB()
               else
               (if (!useRainbow) OwoLibExtensions.INSTANCE.toJavaColor(Keystrokes.INSTANCE.multiColor.getColor()).getRGB() else -1)
               val maxScale: Float = maxScaleOverride ?: Keystrokes.INSTANCE.fontSize.floatValue() / 8.0F
            val fitHeight: Double = fitHeightOverride ?: (if (isSpace) Keystrokes.INSTANCE.cellPx.doubleValue() else bh)
            if (!useRainbow) {
               if (useKeyStyling) {
                  KeystrokeGridEditorKt.drawFittedKeyLabel$default(context, this.key, bx, by, bw, bh, color, true, 0.0, maxScale, 0.0F, fitHeight, 1280, null)
               } else {
                  KeystrokeGridEditorKt.drawFittedText$default(context, text, bx, by, bw, bh, color, true, 0.0, maxScale, 0.0F, fitHeight, 1280, null)
               }
            } else {
               var var10000: Minecraft = Minecraft.getInstance()
               val var61: Font = var10000.font
               val font: Font = var61
               val var53: Float = var61.width(text)
               if (!(var53 <= 0.0F)) {
                  val scale: Float = RangesKt.coerceAtLeast(
                     Math.min(
                        (float)RangesKt.coerceAtLeast(bw - 1.5 * (double)2, 1.0) / var53,
                        Math.min((float)RangesKt.coerceAtLeast(fitHeight - 1.5 * (double)2, 1.0) / 8.0F, maxScale)
                     ),
                     0.35F
                  )
                  val scaledW: Float = var53 * scale
                  val scaledH: Float = 8.0F * scale
                  val sx: Float = (float)Math.floor(bx + (bw - (double)scaledW) / 2.0)
                  val sy: Float = (float)Math.floor(by + (bh - (double)scaledH) / 2.0)
                  var10000 = Minecraft.getInstance()
                  val guiScale: Float = RangesKt.coerceAtLeast((float)((double)var10000.getWindow().getGuiScale()), 1.0F)
                  context.push()
                  context.translate(0.0F, 1.0F / guiScale)
                  context.translate(sx, sy)
                  context.scale(scale, scale)
                  var var57: Double = 0.0
                  var colorRef: Int = if (Keystrokes.INSTANCE.multiColor.isPositionalRainbow()) (int)sx else 0
                  val colorY: Long = if (Keystrokes.INSTANCE.multiColor.isPositionalRainbow()) (long)sy else 0L
                  var var38: Int = 0

                  for (var39 in text.length()..var38) {
                     val s: java.lang.String = java.lang.String.valueOf(text.charAt(var38))
                     val c: Int = RainbowTextUtilsKt.offsetRainbowEffect((long)colorRef, colorY)
                     if (isSpace) {
                        val var46: LiteralTextBuilder = LiteralTextBuilder(null, true)
                        var46.getAppendTasks().add(Keystrokes$SimpleKeyComponent$drawKeyText$lambda$2$$inlined$text$default$1(var46, s, true, c))
                        context.drawString(font, var46.build() as Component, var57, 0.0, c, true)
                     } else {
                        context.drawString(font, s, var57, 0.0, c, true)
                     }

                     val var58: Int = font.width(s)
                     var57 += var58
                     colorRef += var58
                  }

                  context.pop()
               }
            }
         }
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         this.drawSurface$voidrix_client(context, mouseX, mouseY, partialTicks, delta)
         this.drawPressOverlay$voidrix_client(context)
         this.drawForeground$voidrix_client(context, mouseX, mouseY, partialTicks, delta)
      }

      internal fun drawSurface(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         this.updateSurface()
         val var6: Pair = this.computePixelSnap(context)
         val dxSnap: Float = (var6.component1() as java.lang.Number).floatValue()
         val dySnap: Float = (var6.component2() as java.lang.Number).floatValue()
         context.push()
         if (dxSnap != 0.0F || dySnap != 0.0F) {
            context.translate(dxSnap, dySnap)
         }

         super.draw(context, mouseX, mouseY, partialTicks, delta)
         context.pop()
      }

      internal fun drawPressOverlay(context: OwoUIGraphics) {
         val alpha: Float = this.pressAlpha
         if (!(alpha <= 0.0F)) {
            val base: Int = Keystrokes.INSTANCE.pressBackgroundColor.getRGB()
            val overlayAlpha: Int = RangesKt.coerceIn((int)((float)(base ushr 24 and 255) * alpha), 0, 255)
            if (overlayAlpha > 0) {
               val overlayColor: Int = overlayAlpha shl 24 or base and 16777215
               val var7: Pair = this.computePixelSnap(context)
               val dxSnap: Float = (var7.component1() as java.lang.Number).floatValue()
               val dySnap: Float = (var7.component2() as java.lang.Number).floatValue()
               context.push()
               if (dxSnap != 0.0F || dySnap != 0.0F) {
                  context.translate(dxSnap, dySnap)
               }

               val bx: Double = this.x()
               val by: Double = this.y()
               val bw: Double = this.width()
               val bh: Double = this.height()
               if (Keystrokes.INSTANCE.getBackground().getType().isRounded()) {
                  if (Keystrokes.INSTANCE.getBackground().getCornerStyle() === CornerStyle.PIXELATED) {
                     context.fillPixelRoundedRect(bx, by, bw, bh, Keystrokes.INSTANCE.getBackground().getRadius(), overlayColor)
                  } else {
                     context.fillRoundedRect(bx, by, bw, bh, Keystrokes.INSTANCE.getBackground().getRadius(), overlayColor)
                  }
               } else {
                  context.fill(bx, by, bx + bw, by + bh, overlayColor)
               }

               context.pop()
            }
         }
      }

      internal fun drawForeground(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         val var6: Pair = this.computePixelSnap(context)
         val dxSnap: Float = (var6.component1() as java.lang.Number).floatValue()
         val dySnap: Float = (var6.component2() as java.lang.Number).floatValue()
         context.push()
         if (dxSnap != 0.0F || dySnap != 0.0F) {
            context.translate(dxSnap, dySnap)
         }

         val bx: Double = this.x()
         val by: Double = this.y()
         val bw: Double = this.width()
         val bh: Double = this.height()
         val innerX: Double = bx + 2.0
         val innerY: Double = by + 2.0
         val innerW: Double = RangesKt.coerceAtLeast(bw - (double)2 * 2.0, 1.0)
         val innerH: Double = RangesKt.coerceAtLeast(bh - (double)2 * 2.0, 1.0)
         if (this.showsCps) {
            val mainH: Double = innerH * 0.6
            drawKeyText$default(
               this, context, this.getMainText(), innerX, innerY, innerW, innerH * 0.6, true, null, Keystrokes.INSTANCE.cellPx.doubleValue() * 0.65, 128, null
            )
            drawKeyText$default(
               this,
               context,
               this.getCpsText(),
               innerX,
               innerY + mainH,
               innerW,
               innerH - mainH,
               false,
               null,
               Keystrokes.INSTANCE.cellPx.doubleValue() * 0.45,
               192,
               null
            )
         } else {
            drawKeyText$default(this, context, this.getMainText(), innerX, innerY, innerW, innerH, true, null, null, 384, null)
         }

         context.pop()
      }

      private fun computePixelSnap(context: OwoUIGraphics): Pair<Float, Float> {
         val var10000: FloatArray = Keystrokes.INSTANCE.readPoseMatrix(context)
         if (var10000 == null) {
            return TuplesKt.to(0.0F, 0.0F)
         } else {
            val m00: Float = var10000[0]
            val m11: Float = var10000[1]
            val m30: Float = var10000[2]
            val m31: Float = var10000[3]
            if (m00 != 0.0F && m11 != 0.0F) {
               val screenX: Double = this.x() * m00 + m30
               val screenY: Double = this.y() * m11 + m31
               return TuplesKt.to((float)((Math.rint(screenX) - screenX) / (double)m00), (float)((Math.rint(screenY) - screenY) / (double)m11))
            } else {
               return TuplesKt.to(0.0F, 0.0F)
            }
         }
      }
   }

   @Serializable
   internal data class StoredKey(x: Int, y: Int, w: Int, h: Int, keyName: String) {
      public final val x: Int
      public final val y: Int
      public final val w: Int
      public final val h: Int
      public final val keyName: String

      init {
         this.x = x
         this.y = y
         this.w = w
         this.h = h
         this.keyName = keyName
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

      public operator fun component5(): String {
         return this.keyName
      }

      public fun copy(x: Int = this.x, y: Int = this.y, w: Int = this.w, h: Int = this.h, keyName: String = this.keyName): gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredKey {
         return Keystrokes.StoredKey(x, y, w, h, keyName)
      }

      public override fun toString(): String {
         return "StoredKey(x=${this.x}, y=${this.y}, w=${this.w}, h=${this.h}, keyName=${this.keyName})"
      }

      public override fun hashCode(): Int {
         return (((Integer.hashCode(this.x) * 31 + Integer.hashCode(this.y)) * 31 + Integer.hashCode(this.w)) * 31 + Integer.hashCode(this.h)) * 31
            + this.keyName.hashCode()
         }

      public override operator fun equals(other: Any?): Boolean {
         label46@
         if (this === other) {
            return true
         } else {
            return other is Keystrokes.StoredKey
               && this.x == (other as Keystrokes.StoredKey).x
               && this.y == (other as Keystrokes.StoredKey).y
               && this.w == (other as Keystrokes.StoredKey).w
               && this.h == (other as Keystrokes.StoredKey).h
               && this.keyName == (other as Keystrokes.StoredKey).keyName
            }
      }

      public companion object {
         public fun serializer(): KSerializer<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredKey> {
            return Keystrokes.StoredKey.$serializer.INSTANCE as KSerializer<Keystrokes.StoredKey>
         }
      }
   }

   @Serializable
   internal data class StoredLayout(keys: List<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredKey> = CollectionsKt.emptyList()) {
      public final val keys: List<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredKey>
      @JvmField
      @JvmStatic
      private KSerializer<Object>[] $childSerializers = arrayOf(ArrayListSerializer(Keystrokes.StoredKey.$serializer.INSTANCE as KSerializer));

      init {
         this.keys = keys
      }

      public operator fun component1(): List<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredKey> {
         return this.keys
      }

      public fun copy(keys: List<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredKey> = this.keys): gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredLayout {
         return Keystrokes.StoredLayout(keys)
      }

      public override fun toString(): String {
         return "StoredLayout(keys=${this.keys})"
      }

      public override fun hashCode(): Int {
         return this.keys.hashCode()
      }

      public override operator fun equals(other: Any?): Boolean {
         label22@
         if (this === other) {
            return true
         } else {
            return other is Keystrokes.StoredLayout && this.keys == (other as Keystrokes.StoredLayout).keys
         }
      }

      fun StoredLayout() {
         this(null, 1, null)
      }

      public companion object {
         public fun serializer(): KSerializer<gg.voidrix.client.v2.modules.keystrokes.Keystrokes.StoredLayout> {
            return Keystrokes.StoredLayout.$serializer.INSTANCE as KSerializer<Keystrokes.StoredLayout>
         }
      }
   }
}
