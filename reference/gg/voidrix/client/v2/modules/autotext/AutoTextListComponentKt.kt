@file:SourceDebugExtension(["SMAP\nAutoTextListComponent.kt\nKotlin\n*S Kotlin\n*F\n+ 1 AutoTextListComponent.kt\ngg/voidrix/client/v2/modules/autotext/AutoTextListComponentKt\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,169:1\n1869#2,2:170\n*S KotlinDebug\n*F\n+ 1 AutoTextListComponent.kt\ngg/voidrix/client/v2/modules/autotext/AutoTextListComponentKt\n*L\n161#1:170,2\n*E\n"])

package gg.voidrix.client.v2.modules.autotext

import gg.voidrix.owolib.owo.ui.component.LabelComponent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.components.voidrix.VoidrixCollapsibleContainer
import gg.voidrix.ui.modules.v3.RightShiftMenuV3Screen
import gg.voidrix.ui.modules.v3.V3Theme
import kotlin.jvm.internal.SourceDebugExtension

private fun styleCollapsibleV3(collapsible: VoidrixCollapsibleContainer) {
   collapsible.getTitleLabel().shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
   collapsible.getTitleLabel().setAutoColorSupplier({ 
      V3Theme.INSTANCE.accentColor(11)
   })
   val `$i$f$forEach`: Any = CollectionsKt.firstOrNull(collapsible.getTitleLayout().children())
   val var10000: FlowLayout = `$i$f$forEach` as? FlowLayout
   if ((`$i$f$forEach` as? FlowLayout) != null) {
      for (`element$iv` in var10000.children()) {
         val child: UIComponent = `element$iv` as UIComponent
         if (`element$iv` as UIComponent is LabelComponent && `element$iv` as UIComponent != collapsible.getTitleLabel()) {
            (child as LabelComponent).shadow(RightShiftMenuV3Screen.Companion.getUseTextShadow())
            (child as LabelComponent).setAutoColorSupplier({ 
               V3Theme.INSTANCE.accentColor(9)
            })
         }
      }
   }
}
