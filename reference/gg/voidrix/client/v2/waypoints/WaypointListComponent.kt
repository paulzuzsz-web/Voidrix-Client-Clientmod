package gg.voidrix.client.v2.waypoints

import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.client.MCLoggerKt
import gg.voidrix.compat.text.LiteralTextBuilder
import gg.voidrix.compat.text.TextKt
import gg.voidrix.compat.waypoint.source.SourceWaypoint
import gg.voidrix.compat.waypoint.source.WaypointSource
import gg.voidrix.compat.waypoint.source.WaypointSourceRegistry
import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.container.FlowLayout.Algorithm
import gg.voidrix.owolib.owo.ui.core.Color
import gg.voidrix.owolib.owo.ui.core.HorizontalAlignment
import gg.voidrix.owolib.owo.ui.core.Insets
import gg.voidrix.owolib.owo.ui.core.OwoUIGraphics
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.Surface
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.owolib.owo.ui.core.VerticalAlignment
import gg.voidrix.owolib.owo.ui.util.UISounds
import gg.voidrix.ui.components.voidrix.VoidrixDropdownComponent
import gg.voidrix.ui.components.voidrix.VoidrixLabelButton
import gg.voidrix.ui.modules.v3.V3Button
import gg.voidrix.ui.modules.v3.V3RowActions
import gg.voidrix.ui.modules.v3.V3Surfaces
import gg.voidrix.ui.modules.v3.V3Theme
import java.util.Arrays
import java.util.Locale
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nWaypointListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent\n+ 2 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,269:1\n67#2:270\n*S KotlinDebug\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent\n*L\n48#1:270\n*E\n"])
public class WaypointListComponent : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
   private final val logger: Logger = MCLogger.getLogger("Voidrix-WaypointList")

   private fun build() {
      this.clearChildren()
      val all: java.util.List = WaypointSourceRegistry.INSTANCE.allSources()
      val sources: java.util.List = WaypointSourceRegistry.INSTANCE.availableSources()
      MCLoggerKt.voidrixDebugLog(
         this.logger,
         "waypoints",
         "build: registered=${all.size()} available=${sources.size()} ${CollectionsKt.joinToString$default(
            all, ", ", null, null, 0, null, { it: WaypointSource ->
               ("${it.getId()}(avail=${it.isAvailable()})") as java.lang.CharSequence
            }, 30, null
         )}"
      )
      if (sources.isEmpty()) {
         val var10001: gg.voidrix.owolib.owo.ui.component.LabelComponent.Companion = LabelComponent.Companion
         val var9: Array<Any> = arrayOfNulls(0)
         val var10002: MutableComponent = Component.translatable("voidrix.ui.label.no_waypoints", Arrays.copyOf(var9, var9.length))
         val var8: LabelComponent = var10001.create(var10002 as Component)
         var8.horizontalSizing(Sizing.Companion.fill())
         var8.margins(Insets.Companion.of(20))
         this.child(var8 as UIComponent)
      } else {
         for (source in sources) {
            this.child(WaypointListComponent.SourceSection(source) as UIComponent)
         }
      }
   }

   public fun refresh() {
      this.build()
   }

   @SourceDebugExtension(["SMAP\nWaypointListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,269:1\n1#2:270\n*E\n"])
   public companion object {
      private fun formatDimensionName(key: String): String {
         val var2: java.lang.String = StringsKt.replace$default(key, "_", " ", false, 4, null)
         var var11: java.lang.String
         if (var2.length() > 0) {
            val var10000: StringBuilder = StringBuilder()
            var11 = java.lang.String.valueOf(var2.charAt(0))
            var11 = var11.toUpperCase(Locale.ROOT)
            val var10: StringBuilder = var10000.append((Object)var11)
            val var10001: java.lang.String = var2.substring(1)
            var11 = var10.append(var10001).toString()
         } else {
            var11 = var2
         }

         return var11
      }
   }

   @SourceDebugExtension(["SMAP\nWaypointListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Text.kt\ngg/voidrix/compat/text/TextKt\n*L\n1#1,269:1\n1#2:270\n67#3:271\n66#3:272\n*S KotlinDebug\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection\n*L\n142#1:271\n113#1:272\n*E\n"])
   private inner class SourceSection(source: WaypointSource) : FlowLayout(Sizing.Companion.fill(), Sizing.Companion.content(), Algorithm.VERTICAL) {
      public final val source: WaypointSource
      private final var selectedDimension: String
      private final val listContainer: FlowLayout

      init {
         this.source = source
         this.selectedDimension = this.pickDefaultDim()
         val var3: FlowLayout = FlowLayout.Companion.vertical(Sizing.Companion.fill(), Sizing.Companion.content())
         var3.padding(Insets.Companion.top(3))
         this.listContainer = var3
         this.gap(2)
         this.padding(Insets.Companion.top(4))
         this.rebuild()
      }

      private fun pickDefaultDim(): String {
         val dims: java.util.List = this.source.listDimensions()
         val current: java.lang.String = PersistentWaypointStore.INSTANCE.getCurrentDimensionKey()
         var var10000: java.lang.String
         if (current != null && dims.contains(current)) {
            var10000 = current
         } else if (!dims.isEmpty()) {
            var10000 = CollectionsKt.first(dims) as java.lang.String
         } else {
            var10000 = current
            if (current == null) {
               var10000 = "overworld"
            }
         }

         return var10000
      }

      public fun rebuild() {
         this.clearChildren()
         this.child(V3Surfaces.categoryHeader$default(V3Surfaces.INSTANCE, this.source.getDisplayName(), null, 2, null) as UIComponent)
         val dims: FlowLayout = FlowLayout.Companion.horizontal(Sizing.Companion.fill(), Sizing.Companion.content())
         dims.gap(4)
         dims.verticalAlignment(VerticalAlignment.CENTER)
         dims.padding(Insets.Companion.horizontal(2))
         val var9: java.util.Collection = this.source.listDimensions()
         val var8: java.util.List = (if (var9.isEmpty()) CollectionsKt.listOf(this.selectedDimension) else var9) as java.util.List
         if (!var8.contains(this.selectedDimension)) {
            this.selectedDimension = CollectionsKt.first(var8) as java.lang.String
         }

         val var10: VoidrixDropdownComponent = VoidrixDropdownComponent(var8, this.selectedDimension, { it: java.lang.String ->
            val var10000: MutableComponent = Component.literal(WaypointListComponent.Companion.formatDimensionName(it))
            var10000 as Component
         }, Sizing.Companion.fill(50), null, 16, null)
         var10.onChanged().subscribe({ dim: java.lang.String ->
            `this$0`.selectedDimension = dim
            `this$0`.rebuildListOnly()
         })
         dims.child(var10 as UIComponent)
         if (this.source.isWritable()) {
            val var5: V3Button = V3Button(TextKt.toSmallCaps("+ New"), { button: VoidrixLabelButton, var2: Double, var4: Double, var6: Int ->
               UISounds.playButtonSound()
               `this$0`.openEditDialog(button as UIComponent, null)
               Unit.INSTANCE
            })
            var5.getLabel().scale(0.75F)
            dims.child(var5 as UIComponent)
         }

         this.child(dims as UIComponent)
         this.child(this.listContainer as UIComponent)
         this.rebuildListOnly()
      }

      private fun rebuildListOnly() {
         // $VF: Couldn't be decompiled
         // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
         // java.lang.IllegalStateException: Anonymous class does not have Class Kotlin metadata
         //   at org.vineflower.kotlin.KotlinWriter.writeClassDefinition(KotlinWriter.java:742)
         //   at org.vineflower.kotlin.KotlinWriter.writeClass(KotlinWriter.java:309)
         //   at org.vineflower.kotlin.expr.KNewExprent.toJava(KNewExprent.java:178)
         //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.wrapOperandString(FunctionExprent.java:770)
         //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.wrapOperandString(FunctionExprent.java:736)
         //
         // Bytecode:
         // 000: aload 0
         // 001: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.listContainer Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 004: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.clearChildren ()Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 007: pop
         // 008: aload 0
         // 009: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.source Lgg/voidrix/compat/waypoint/source/WaypointSource;
         // 00c: aload 0
         // 00d: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.selectedDimension Ljava/lang/String;
         // 010: invokeinterface gg/voidrix/compat/waypoint/source/WaypointSource.list (Ljava/lang/String;)Ljava/util/List; 2
         // 015: astore 1
         // 016: aload 1
         // 017: invokeinterface java/util/List.isEmpty ()Z 1
         // 01c: ifeq 07d
         // 01f: aload 0
         // 020: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.listContainer Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 023: getstatic gg/voidrix/owolib/owo/ui/component/LabelComponent.Companion Lgg/voidrix/owolib/owo/ui/component/LabelComponent$Companion;
         // 026: ldc_w "voidrix.ui.label.no_waypoints"
         // 029: astore 2
         // 02a: bipush 0
         // 02b: anewarray 285
         // 02e: astore 3
         // 02f: bipush 0
         // 030: istore 4
         // 032: aload 2
         // 033: aload 3
         // 034: aload 3
         // 035: arraylength
         // 036: invokestatic java/util/Arrays.copyOf ([Ljava/lang/Object;I)[Ljava/lang/Object;
         // 039: invokestatic net/minecraft/network/chat/Component.translatable (Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;
         // 03c: dup
         // 03d: ldc_w "translatable(...)"
         // 040: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
         // 043: checkcast net/minecraft/network/chat/Component
         // 046: invokevirtual gg/voidrix/owolib/owo/ui/component/LabelComponent$Companion.create (Lnet/minecraft/network/chat/Component;)Lgg/voidrix/owolib/owo/ui/component/LabelComponent;
         // 049: astore 2
         // 04a: aload 2
         // 04b: astore 3
         // 04c: astore 8
         // 04e: bipush 0
         // 04f: istore 4
         // 051: aload 3
         // 052: getstatic gg/voidrix/owolib/owo/ui/core/Sizing.Companion Lgg/voidrix/owolib/owo/ui/core/Sizing$Companion;
         // 055: invokevirtual gg/voidrix/owolib/owo/ui/core/Sizing$Companion.fill ()Lgg/voidrix/owolib/owo/ui/core/Sizing;
         // 058: invokevirtual gg/voidrix/owolib/owo/ui/component/LabelComponent.horizontalSizing (Lgg/voidrix/owolib/owo/ui/core/Sizing;)Lgg/voidrix/owolib/owo/ui/core/UIComponent;
         // 05b: pop
         // 05c: aload 3
         // 05d: getstatic gg/voidrix/owolib/owo/ui/core/HorizontalAlignment.CENTER Lgg/voidrix/owolib/owo/ui/core/HorizontalAlignment;
         // 060: invokevirtual gg/voidrix/owolib/owo/ui/component/LabelComponent.horizontalTextAlignment (Lgg/voidrix/owolib/owo/ui/core/HorizontalAlignment;)Lgg/voidrix/owolib/owo/ui/component/LabelComponent;
         // 063: pop
         // 064: aload 3
         // 065: getstatic gg/voidrix/owolib/owo/ui/core/Insets.Companion Lgg/voidrix/owolib/owo/ui/core/Insets$Companion;
         // 068: bipush 10
         // 06a: invokevirtual gg/voidrix/owolib/owo/ui/core/Insets$Companion.of (I)Lgg/voidrix/owolib/owo/ui/core/Insets;
         // 06d: invokevirtual gg/voidrix/owolib/owo/ui/component/LabelComponent.margins (Lgg/voidrix/owolib/owo/ui/core/Insets;)Lgg/voidrix/owolib/owo/ui/base/BaseUIComponent;
         // 070: pop
         // 071: nop
         // 072: aload 8
         // 074: aload 2
         // 075: checkcast gg/voidrix/owolib/owo/ui/core/UIComponent
         // 078: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.child (Lgg/voidrix/owolib/owo/ui/core/UIComponent;)Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 07b: pop
         // 07c: return
         // 07d: aload 1
         // 07e: checkcast java/lang/Iterable
         // 081: bipush 2
         // 082: invokestatic kotlin/collections/CollectionsKt.chunked (Ljava/lang/Iterable;I)Ljava/util/List;
         // 085: invokeinterface java/util/List.iterator ()Ljava/util/Iterator; 1
         // 08a: astore 2
         // 08b: aload 2
         // 08c: invokeinterface java/util/Iterator.hasNext ()Z 1
         // 091: ifeq 133
         // 094: aload 2
         // 095: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
         // 09a: checkcast java/util/List
         // 09d: astore 3
         // 09e: getstatic gg/voidrix/owolib/owo/ui/container/FlowLayout.Companion Lgg/voidrix/owolib/owo/ui/container/FlowLayout$Companion;
         // 0a1: getstatic gg/voidrix/owolib/owo/ui/core/Sizing.Companion Lgg/voidrix/owolib/owo/ui/core/Sizing$Companion;
         // 0a4: invokevirtual gg/voidrix/owolib/owo/ui/core/Sizing$Companion.fill ()Lgg/voidrix/owolib/owo/ui/core/Sizing;
         // 0a7: getstatic gg/voidrix/owolib/owo/ui/core/Sizing.Companion Lgg/voidrix/owolib/owo/ui/core/Sizing$Companion;
         // 0aa: invokevirtual gg/voidrix/owolib/owo/ui/core/Sizing$Companion.content ()Lgg/voidrix/owolib/owo/ui/core/Sizing;
         // 0ad: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout$Companion.horizontal (Lgg/voidrix/owolib/owo/ui/core/Sizing;Lgg/voidrix/owolib/owo/ui/core/Sizing;)Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 0b0: astore 5
         // 0b2: aload 5
         // 0b4: astore 6
         // 0b6: bipush 0
         // 0b7: istore 7
         // 0b9: aload 6
         // 0bb: bipush 5
         // 0bc: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.gap (I)Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 0bf: pop
         // 0c0: aload 6
         // 0c2: getstatic gg/voidrix/owolib/owo/ui/core/Insets.Companion Lgg/voidrix/owolib/owo/ui/core/Insets$Companion;
         // 0c5: bipush 1
         // 0c6: invokevirtual gg/voidrix/owolib/owo/ui/core/Insets$Companion.of (I)Lgg/voidrix/owolib/owo/ui/core/Insets;
         // 0c9: bipush 4
         // 0ca: invokevirtual gg/voidrix/owolib/owo/ui/core/Insets.withLeft (I)Lgg/voidrix/owolib/owo/ui/core/Insets;
         // 0cd: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.padding (Lgg/voidrix/owolib/owo/ui/core/Insets;)Lgg/voidrix/owolib/owo/ui/core/ParentUIComponent;
         // 0d0: pop
         // 0d1: aload 6
         // 0d3: bipush 1
         // 0d4: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.allowOverflow (Z)Lgg/voidrix/owolib/owo/ui/core/ParentUIComponent;
         // 0d7: pop
         // 0d8: nop
         // 0d9: aload 5
         // 0db: astore 4
         // 0dd: aload 3
         // 0de: invokeinterface java/util/List.iterator ()Ljava/util/Iterator; 1
         // 0e3: astore 5
         // 0e5: aload 5
         // 0e7: invokeinterface java/util/Iterator.hasNext ()Z 1
         // 0ec: ifeq 123
         // 0ef: aload 5
         // 0f1: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
         // 0f6: checkcast gg/voidrix/compat/waypoint/source/SourceWaypoint
         // 0f9: astore 6
         // 0fb: aload 4
         // 0fd: new gg/voidrix/client/v2/waypoints/WaypointListComponent$WaypointEntry
         // 100: dup
         // 101: aload 6
         // 103: aload 0
         // 104: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.source Lgg/voidrix/compat/waypoint/source/WaypointSource;
         // 107: aload 0
         // 108: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.selectedDimension Ljava/lang/String;
         // 10b: new gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection$rebuildListOnly$2
         // 10e: dup
         // 10f: aload 0
         // 110: invokespecial gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection$rebuildListOnly$2.<init> (Ljava/lang/Object;)V
         // 113: checkcast kotlin/jvm/functions/Function0
         // 116: invokespecial gg/voidrix/client/v2/waypoints/WaypointListComponent$WaypointEntry.<init> (Lgg/voidrix/compat/waypoint/source/SourceWaypoint;Lgg/voidrix/compat/waypoint/source/WaypointSource;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V
         // 119: checkcast gg/voidrix/owolib/owo/ui/core/UIComponent
         // 11c: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.child (Lgg/voidrix/owolib/owo/ui/core/UIComponent;)Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 11f: pop
         // 120: goto 0e5
         // 123: aload 0
         // 124: getfield gg/voidrix/client/v2/waypoints/WaypointListComponent$SourceSection.listContainer Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 127: aload 4
         // 129: checkcast gg/voidrix/owolib/owo/ui/core/UIComponent
         // 12c: invokevirtual gg/voidrix/owolib/owo/ui/container/FlowLayout.child (Lgg/voidrix/owolib/owo/ui/core/UIComponent;)Lgg/voidrix/owolib/owo/ui/container/FlowLayout;
         // 12f: pop
         // 130: goto 08b
         // 133: return
      }

      private fun onWaypointChanged() {
         this.rebuildListOnly()
      }

      private fun openEditDialog(source_: UIComponent, waypoint: SourceWaypoint?) {
         WaypointEditComponent.Companion.openDialog(source_, waypoint, this.source, this.selectedDimension, { 
            `this$0`.rebuildListOnly()
            Unit.INSTANCE
         })
      }
   }

   @SourceDebugExtension(["SMAP\nWaypointListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent$WaypointEntry\n+ 2 TextBuilder.kt\ngg/voidrix/compat/text/TextBuilderKt\n*L\n1#1,269:1\n11#2:270\n*S KotlinDebug\n*F\n+ 1 WaypointListComponent.kt\ngg/voidrix/client/v2/waypoints/WaypointListComponent$WaypointEntry\n*L\n238#1:270\n*E\n"])
   private class WaypointEntry(waypoint: SourceWaypoint, source: WaypointSource, dimensionKey: String, onChanged: () -> Unit) : FlowLayout(
         Sizing.Companion.fill(49), Sizing.Companion.fixed(15), Algorithm.HORIZONTAL
      ) {
      public final val waypoint: SourceWaypoint
      public final val source: WaypointSource
      public final val dimensionKey: String
      public final val onChanged: () -> Unit

      public final var isHovered: Boolean
         internal set

      public final val editButton: V3Button
      public final val hideButton: V3Button
      public final val deleteButton: V3Button
      public final val actionWrapper: FlowLayout

      init {
         this.waypoint = waypoint
         this.source = source
         this.dimensionKey = dimensionKey
         this.onChanged = onChanged
         this.editButton = V3RowActions.editButton$default(V3RowActions.INSTANCE, 0, { 
            WaypointEditComponent.Companion.openDialog(`this$0` as UIComponent, `this$0`.waypoint, `this$0`.source, `this$0`.dimensionKey, { 
               `this$0`.onChanged()
               Unit.INSTANCE
            })
            Unit.INSTANCE
         }, 1, null)
         this.hideButton = V3RowActions.visibilityButton$default(
            V3RowActions.INSTANCE,
            { 
               `this$0`.waypoint.getDisabled()
            },
            0,
            { 
               `this$0`.source
                  .update(
                     `this$0`.dimensionKey,
                     SourceWaypoint.copy$default(
                        `this$0`.waypoint, null, null, null, 0, 0, 0, 0, null, null, !`this$0`.waypoint.getDisabled(), false, 0L, null, 7679, null
                     )
                  )
                  `this$0`.onChanged()
               Unit.INSTANCE
            },
            2,
            null
         )
         this.deleteButton = V3RowActions.deleteButton$default(V3RowActions.INSTANCE, 0, { 
            `this$0`.source.delete(`this$0`.dimensionKey, `this$0`.waypoint.getId())
            `this$0`.onChanged()
            Unit.INSTANCE
         }, 1, null)
         this.actionWrapper = if (this.source.isWritable())
            V3RowActions.INSTANCE.hoverActionRow(arrayOf(this.editButton, this.hideButton, this.deleteButton))
            else
            V3RowActions.INSTANCE.hoverActionRow(arrayOfNulls(0))
            this.padding(Insets.Companion.of(1))
         this.mouseEnter().subscribe({ 
            `this$0`.isHovered = true
         })
         this.mouseLeave().subscribe({ 
            `this$0`.isHovered = false
         })
         this.verticalAlignment(VerticalAlignment.CENTER)
         this.horizontalAlignment(HorizontalAlignment.CENTER)
         this.allowOverflow(true)
         var var16: FlowLayout = FlowLayout.Companion.horizontal(Sizing.Companion.fill(50), Sizing.Companion.fill())
         var16.allowOverflow(true)
         var16.gap(5)
         var16.verticalAlignment(VerticalAlignment.CENTER)
         val nameText: FlowLayout = FlowLayout.Companion.horizontal(Sizing.Companion.fixed(10), Sizing.Companion.fixed(10))
         nameText.surface(Surface.Companion.flat(this.waypoint.getColor() or -16777216))
         var16.child(nameText as UIComponent)
         val var12: LiteralTextBuilder = LiteralTextBuilder(this.waypoint.getName(), true)
         if (this.waypoint.getDisabled()) {
            var12.setStrikethrough(true)
         }

         val var22: LabelComponent = LabelComponent.Companion.create(var12.build() as Component)
         if (this.waypoint.getDisabled()) {
            var22.color(Color.Companion.ofArgb(-7829368))
         }

         var16.child(var22 as UIComponent)
         this.child(var16 as UIComponent)
         var16 = FlowLayout.Companion.horizontal(Sizing.Companion.fill(50), Sizing.Companion.fill())
         var16.horizontalAlignment(HorizontalAlignment.RIGHT)
         var16.verticalAlignment(VerticalAlignment.CENTER)
         this.child(var16 as UIComponent)
      }

      public open fun draw(context: OwoUIGraphics, mouseX: Int, mouseY: Int, partialTicks: Float, delta: Float) {
         if (this.isHovered) {
            this.surface(Surface.Companion.flat(V3Theme.INSTANCE.grayBg(4)))
         } else {
            this.surface(Surface.BLANK)
         }

         val var7: Any = CollectionsKt.getOrNull(this.children(), 1)
         val var10000: FlowLayout = var7 as? FlowLayout
         if ((var7 as? FlowLayout) == null) {
            super.draw(context, mouseX, mouseY, partialTicks, delta)
         } else {
            if (this.source.isWritable()) {
               if (this.isHovered && !var10000.children().contains(this.actionWrapper)) {
                  var10000.child(this.actionWrapper as UIComponent)
               } else if (!this.isHovered && var10000.children().contains(this.actionWrapper)) {
                  var10000.removeChild(this.actionWrapper as UIComponent)
               }
            }

            super.draw(context, mouseX, mouseY, partialTicks, delta)
         }
      }
   }
}
