package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.auth.VoidrixAuth
import gg.voidrix.compat.icon.VoidrixPlayerCache
import gg.voidrix.compat.nametag.NameTagManager
import gg.voidrix.cosmetics.v2.ui.plus.PlusExtraNameTag
import gg.voidrix.networking.model.core.VoidrixUserMinimal
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.modules.v3.V3Surfaces
import java.util.UUID
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft

@VoidrixMiniTag(tags = ["nametags"])
@SourceDebugExtension(["SMAP\nNameTagsModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NameTagsModule.kt\ngg/voidrix/client/v2/modules/impl/NameTagsModule\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,86:1\n1#2:87\n*E\n"])
public object NameTagsModule : Module("NameTags", ModuleCategory.VISUAL, false, false, false, 20) {
   public final val f5: Boolean by ValueApiKt.boolean$default(true, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return f5$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final val textShadow: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return textShadow$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   public final val noBackground: Boolean by ValueApiKt.boolean$default(false, null, null, null, 14, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return noBackground$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         var var11: Boolean
         run label32@{
            settingsPanel.child(V3Surfaces.categoryHeader$default(V3Surfaces.INSTANCE, "Extra NameTag", null, 2, null) as UIComponent)
            val uuid: UUID = VoidrixAuth.INSTANCE.getPlayerUuid()
            if (uuid != null) {
               val var10000: VoidrixUserMinimal = VoidrixPlayerCache.INSTANCE.getCached(uuid)
               val var10: java.lang.Boolean = if (var10000 != null) var10000.isVoidrixPlus() else null
               if (var10 != null) {
                  var11 = var10
                  return@label32
               }
            }

            var11 = false
         }

         val isVoidrixPlus: Boolean = var11
         val var3: NameTagsModule = INSTANCE

         try {
            val var8: Any = Result.constructor_impl/* $VF was: constructor-impl */(
               settingsPanel.child(PlusExtraNameTag(isVoidrixPlus, Sizing.Companion.fill(95), null, null, 12, null) as UIComponent)
            )
         } catch (var7: java.lang.Throwable) {
            val `$this$buildCustomUi_u24lambda_u243_u24lambda_u242`: Any = Result.constructor_impl/* $VF was: constructor-impl */(ResultKt.createFailure(var7))
         }

         Unit.INSTANCE
      }
   }

   @JvmStatic
   public fun shouldShowOwnNametag(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.f5 && !Minecraft.getInstance().options.getCameraType().isFirstPerson()
   }
}
