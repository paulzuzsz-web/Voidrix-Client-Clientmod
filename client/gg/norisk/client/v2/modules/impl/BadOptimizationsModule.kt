package gg.norisk.client.v2.modules.impl

import gg.norisk.compat.icon.atlas.IconAtlasManager
import gg.norisk.compat.render.BadOptimizationsManager
import gg.norisk.compat.render.IBadOptimizationsSettings
import gg.norisk.compat.render.NrcBindSkip
import gg.norisk.compat.skin.atlas.SkinAtlasManager
import gg.norisk.compat.text.TextKt
import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.core.Insets
import gg.norisk.owolib.owo.ui.core.OwoUIGraphics
import gg.norisk.owolib.owo.ui.core.ParentUIComponent
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.core.Surface
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.ui.api.annotations.Category
import gg.norisk.ui.api.module.Module
import gg.norisk.ui.api.module.ModuleCategory
import gg.norisk.ui.api.value.Value
import gg.norisk.ui.api.value.ValueHolder
import gg.norisk.ui.api.value.ValueJsonKt
import gg.norisk.ui.components.nrc.NrcCheckbox
import gg.norisk.ui.modules.v3.RightShiftMenuV3Screen
import gg.norisk.ui.modules.v3.V3Theme
import gg.norisk.ui.modules.v3.V3ValueLine
import java.util.Arrays
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.internal.BooleanSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.jetbrains.annotations.NotNull

public object BadOptimizationsModule : Module("Bad Optimizations", ModuleCategory.QUALITY_OF_LIFE, false, false, false, 20), IBadOptimizationsSettings {
   public open val seoTags: Array<String>
   private final val skinAtlasToggle: gg.norisk.client.v2.modules.impl.BadOptimizationsModule.DescribedToggle =
      BadOptimizationsModule.DescribedToggle(true, "Skin Atlas")
      private final val iconAtlasToggle: gg.norisk.client.v2.modules.impl.BadOptimizationsModule.DescribedToggle =
      BadOptimizationsModule.DescribedToggle(true, "Icon Atlas")
      private final val bindSkipToggle: gg.norisk.client.v2.modules.impl.BadOptimizationsModule.DescribedToggle =
      BadOptimizationsModule.DescribedToggle(true, "Render Pass Bind Skip")

   @Category(name = "Optimizations")
   @NotNull
   public final val skinAtlas: Boolean by skinAtlasToggle.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      public final get() {
         return skinAtlas$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val iconAtlas: Boolean by iconAtlasToggle.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
      public final get() {
         return iconAtlas$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val renderPassBindSkip: Boolean by bindSkipToggle.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
      public final get() {
         return renderPassBindSkip$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public open val moduleEnabled: Boolean
      public open get() {
         return this.isEnabled()
      }


   public open fun toggleSkinAtlas(): Boolean {
      return this.flip(skinAtlasToggle)
   }

   public open fun toggleIconAtlas(): Boolean {
      return this.flip(iconAtlasToggle)
   }

   public open fun toggleRenderPassBindSkip(): Boolean {
      return this.flip(bindSkipToggle)
   }

   private fun flip(toggle: gg.norisk.client.v2.modules.impl.BadOptimizationsModule.DescribedToggle): Boolean {
      toggle.set(!toggle.get() as java.lang.Boolean)
      return toggle.get() as java.lang.Boolean
   }

   public open fun enableChangeCallback(enabled: Boolean) {
      super.enableChangeCallback(enabled)
      this.applyFlags()
   }

   private fun applyFlags() {
      SkinAtlasManager.INSTANCE.setEnabled(this.isEnabled() && this.skinAtlas)
      IconAtlasManager.INSTANCE.setEnabled(this.isEnabled() && this.iconAtlas)
      NrcBindSkip.setEnabled(this.isEnabled() && this.renderPassBindSkip)
   }

   @JvmStatic
   fun {
      INSTANCE.applyFlags()
      BadOptimizationsManager.INSTANCE.setSettings(INSTANCE)
   }

   @SourceDebugExtension(["SMAP\nBadOptimizationsModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 BadOptimizationsModule.kt\ngg/norisk/client/v2/modules/impl/BadOptimizationsModule$DescribedToggle\n+ 2 Json.kt\nkotlinx/serialization/json/JsonKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 Text.kt\ngg/norisk/compat/text/TextKt\n*L\n1#1,136:1\n324#2:137\n335#2:139\n1#3:138\n67#4:140\n269#4:141\n*S KotlinDebug\n*F\n+ 1 BadOptimizationsModule.kt\ngg/norisk/client/v2/modules/impl/BadOptimizationsModule$DescribedToggle\n*L\n81#1:137\n84#1:139\n100#1:140\n100#1:141\n*E\n"])
   private class DescribedToggle(value: Boolean, displayName: String) : Value(value, displayName, null, { it: Boolean ->
         BadOptimizationsModule.INSTANCE.applyFlags()
         Unit.INSTANCE
      }) {
      public open fun serialize(isDefault: Boolean): JsonElement {
         val `$this$encodeToJsonElement$iv`: Json = ValueJsonKt.getValueJson()
         val `value$iv`: Any = if (isDefault) this.getDefaultValue() as java.lang.Boolean else this.getValue() as java.lang.Boolean
         `$this$encodeToJsonElement$iv`.getSerializersModule()
         return `$this$encodeToJsonElement$iv`.encodeToJsonElement(BooleanSerializer.INSTANCE as SerializationStrategy, `value$iv`)
      }

      public open fun deserialize(data: JsonElement) {
         val var2: BadOptimizationsModule.DescribedToggle = this

         try {
            val var8: BadOptimizationsModule.DescribedToggle = var2
            val `$this$decodeFromJsonElement$iv`: Json = ValueJsonKt.getValueJson()
            `$this$decodeFromJsonElement$iv`.getSerializersModule()
            var8.set(`$this$decodeFromJsonElement$iv`.decodeFromJsonElement(BooleanSerializer.INSTANCE as DeserializationStrategy, data))
            val var9: Any = Result.constructor_impl/* $VF was: constructor-impl */(Unit.INSTANCE)
         } catch (var7: java.lang.Throwable) {
            val `$this$deserialize_u24lambda_u241`: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var7))
         }
      }

      public open fun buildElement(parent: ParentUIComponent, updateLabel: () -> Unit): UIComponent {
         // $VF: Couldn't be decompiled
         // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
         // java.lang.IllegalStateException: Anonymous class does not have Class Kotlin metadata
         //   at org.vineflower.kotlin.KotlinWriter.writeClassDefinition(KotlinWriter.java:742)
         //   at org.vineflower.kotlin.KotlinWriter.writeClass(KotlinWriter.java:309)
         //   at org.vineflower.kotlin.expr.KNewExprent.toJava(KNewExprent.java:178)
         //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1054)
         //   at org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:1151)
         //   at org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.toJava(InvocationExprent.java:921)
         //
         // Bytecode:
         // 00: aload 1
         // 01: ldc "parent"
         // 03: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullParameter (Ljava/lang/Object;Ljava/lang/String;)V
         // 06: aload 2
         // 07: ldc "updateLabel"
         // 09: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullParameter (Ljava/lang/Object;Ljava/lang/String;)V
         // 0c: new gg/norisk/ui/components/nrc/NrcCheckbox
         // 0f: dup
         // 10: fconst_0
         // 11: bipush 1
         // 12: aconst_null
         // 13: invokespecial gg/norisk/ui/components/nrc/NrcCheckbox.<init> (FILkotlin/jvm/internal/DefaultConstructorMarker;)V
         // 16: astore 3
         // 17: aload 3
         // 18: astore 4
         // 1a: bipush 0
         // 1b: istore 5
         // 1d: aload 4
         // 1f: aload 0
         // 20: invokevirtual gg/norisk/client/v2/modules/impl/BadOptimizationsModule$DescribedToggle.getValue ()Ljava/lang/Object;
         // 23: checkcast java/lang/Boolean
         // 26: invokevirtual java/lang/Boolean.booleanValue ()Z
         // 29: invokevirtual gg/norisk/ui/components/nrc/NrcCheckbox.checked (Z)Lgg/norisk/owolib/owo/ui/component/SmallCheckboxComponent;
         // 2c: pop
         // 2d: aload 4
         // 2f: invokevirtual gg/norisk/ui/components/nrc/NrcCheckbox.onChanged ()Lgg/norisk/owolib/owo/util/EventSource;
         // 32: new gg/norisk/client/v2/modules/impl/BadOptimizationsModule$DescribedToggle$buildElement$1$1
         // 35: dup
         // 36: aload 0
         // 37: invokespecial gg/norisk/client/v2/modules/impl/BadOptimizationsModule$DescribedToggle$buildElement$1$1.<init> (Lgg/norisk/client/v2/modules/impl/BadOptimizationsModule$DescribedToggle;)V
         // 3a: invokevirtual gg/norisk/owolib/owo/util/EventSource.subscribe (Ljava/lang/Object;)Lgg/norisk/owolib/owo/util/EventSource$Subscription;
         // 3d: pop
         // 3e: aload 4
         // 40: getstatic gg/norisk/owolib/owo/ui/core/Insets.Companion Lgg/norisk/owolib/owo/ui/core/Insets$Companion;
         // 43: bipush 1
         // 44: invokevirtual gg/norisk/owolib/owo/ui/core/Insets$Companion.of (I)Lgg/norisk/owolib/owo/ui/core/Insets;
         // 47: invokevirtual gg/norisk/ui/components/nrc/NrcCheckbox.margins (Lgg/norisk/owolib/owo/ui/core/Insets;)Lgg/norisk/owolib/owo/ui/base/BaseUIComponent;
         // 4a: pop
         // 4b: nop
         // 4c: aload 3
         // 4d: checkcast gg/norisk/owolib/owo/ui/core/UIComponent
         // 50: areturn
      }

      public open fun buildCustomLine(verticalWrapper: FlowLayout): Boolean {
         val line: V3ValueLine = V3ValueLine(Sizing.Companion.fill(), Sizing.Companion.content())
         var var10001: java.lang.String = this.getDisplayName()
         if (var10001 == null) {
            var10001 = this.getName()
         }

         line.title(var10001)
         line.right(this.buildElement(line as ParentUIComponent, { 
            Unit.INSTANCE
         }))
         val key: java.lang.String = "nrc.ui.modules.value.${this.getName()}.description"
         val block: Array<Any> = arrayOfNulls(0)
         val var10000: MutableComponent = Component.translatable(key, Arrays.copyOf(block, block.length))
         val var17: java.lang.String = (var10000 as Component).getString()
         if (var17 == key) {
            verticalWrapper.child(line as UIComponent)
            return true
         } else {
            val var11: <unrepresentable> = object : FlowLayout {
               public final var blockHovered: Boolean
                  internal set

               {
                  this.mouseEnter().subscribe({ 
                     `this$0`.blockHovered = true
                  })
                  this.mouseLeave().subscribe({ 
                     `this$0`.blockHovered = false
                  })
               }

               public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
                  this.surface(if (this.blockHovered) Surface.Companion.flat(V3Theme.INSTANCE.grayBg(3)) else Surface.BLANK)
                  super.draw(context, mouseX, mouseY, partialTicks, delta)
               }
            }
            line.setForcedSurface(Surface.BLANK)
            var11.child(line as UIComponent)
            val var14: LabelComponent = LabelComponent(TextKt.getLiteral(var17) as Component)
            var14.shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
            var14.scale(0.7F)
            var14.maxWidth(280)
            var14.lineSpacing(0)
            var14.setAutoColorSupplier({ 
               V3Theme.INSTANCE.grayColor(9)
            })
            var14.margins(Insets.Companion.of(0, 6, 6, 0))
            var11.child(var14 as UIComponent)
            verticalWrapper.child(var11 as UIComponent)
            return true
         }
      }
   }
}
