@file:SourceDebugExtension(["SMAP\nItemHighlighterListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ItemHighlighterListComponent.kt\ngg/norisk/client/v2/modules/itemhighlighter/ItemHighlighterListComponentKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,152:1\n1869#2,2:153\n*S KotlinDebug\n*F\n+ 1 ItemHighlighterListComponent.kt\ngg/norisk/client/v2/modules/itemhighlighter/ItemHighlighterListComponentKt\n*L\n145#1:153,2\n*E\n"])

package gg.norisk.client.v2.modules.itemhighlighter

import gg.norisk.owolib.owo.ui.component.LabelComponent
import gg.norisk.owolib.owo.ui.container.FlowLayout
import gg.norisk.owolib.owo.ui.core.UIComponent
import gg.norisk.ui.components.nrc.NrcCollapsibleContainer
import gg.norisk.ui.modules.v3.RightShiftMenuV3Screen
import gg.norisk.ui.modules.v3.V3Theme
import kotlin.jvm.internal.SourceDebugExtension

private fun styleCollapsibleV3(collapsible: NrcCollapsibleContainer) {
   collapsible.getTitleLabel().shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
   collapsible.getTitleLabel().setAutoColorSupplier({ 
      V3Theme.INSTANCE.grayColor(12)
   })
   val var2: Any = CollectionsKt.firstOrNull(collapsible.getTitleLayout().children())
   val titleRow: FlowLayout = var2 as? FlowLayout
   if ((var2 as? FlowLayout) != null) {
      val var10000: java.util.List = titleRow.children()
      if (var10000 != null) {
         for (`element$iv` in var10000) {
            val child: UIComponent = `element$iv` as UIComponent
            if (`element$iv` as UIComponent is LabelComponent && `element$iv` as UIComponent != collapsible.getTitleLabel()) {
               (child as LabelComponent).shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
               (child as LabelComponent).setAutoColorSupplier({ 
                  V3Theme.INSTANCE.grayColor(11)
               })
            }
         }
      }
   }
}
