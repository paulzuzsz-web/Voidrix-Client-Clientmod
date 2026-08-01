package gg.norisk.client.v2.resourcepackorganizer

import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.container.UIContainers
import gg.norisk.owolib.owo.ui.core.HorizontalAlignment
import gg.norisk.owolib.owo.ui.core.Sizing
import gg.norisk.owolib.owo.ui.layers.Layers
import gg.norisk.owolib.owo.ui.layers.Layer.Instance
import gg.norisk.ui.modules.v3.V3Checkbox
import net.minecraft.client.gui.screens.packs.PackSelectionScreen

public object RpoToggleLayer {
   @JvmStatic
   public fun init() {
      Layers.add(
         { hSizing: Sizing, vSizing: Sizing ->
            UIContainers.verticalFlow(hSizing, vSizing)
         },
         { instance: Instance ->
            // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
            // java.lang.NullPointerException: Cannot invoke "java.util.List.stream()" because the return value of "org.jetbrains.java.decompiler.modules.decompiler.stats.Statement.getExprents()" is null
            //   at org.vineflower.kotlin.expr.KNewExprent.lambda$toJava$0(KNewExprent.java:128)
            //   at java.base/java.util.stream.ReferencePipeline$7$1FlatMap.accept(ReferencePipeline.java:288)
            //   at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1716)
            //   at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:570)
            //   at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:560)
            //   at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:635)
            //   at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(AbstractPipeline.java:291)
            //   at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:652)
            //   at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:658)
            //   at java.base/java.util.stream.ReferencePipeline.toList(ReferencePipeline.java:663)
            //   at org.vineflower.kotlin.expr.KNewExprent.toJava(KNewExprent.java:131)
            //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1054)
            //   at org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:1151)
            //   at org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.toJava(InvocationExprent.java:921)
         },
         arrayOf(PackSelectionScreen::class.java)
      )
   }

   private fun newColumn(): FlowLayout {
      val var1: FlowLayout = UIContainers.verticalFlow(Sizing.Companion.content(), Sizing.Companion.content())
      var1.gap(1)
      var1.horizontalAlignment(HorizontalAlignment.LEFT)
      var1.allowOverflow(true)
      return var1
   }

   private fun makeToggle(label: String, initial: Boolean, onChange: (Boolean) -> Unit): V3Checkbox {
      val cb: V3Checkbox = V3Checkbox()
      cb.label(label)
      cb.labelShadow(true)
      cb.checked(initial)
      cb.onChanged().subscribe({ newValue: Boolean ->
         `$onChange`(newValue)
      })
      return cb
   }
}
