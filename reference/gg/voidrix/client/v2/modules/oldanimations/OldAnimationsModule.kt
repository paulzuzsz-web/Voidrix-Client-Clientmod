package gg.voidrix.client.v2.modules.oldanimations

import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import org.jetbrains.annotations.NotNull

public object OldAnimationsModule : Module("Old Animations", ModuleCategory.PVP, false, true, false, 20) {
   @Category(name = "First Person")
   @NotNull
   public final val blockHitting: Boolean by ValueApiKt.boolean$default(true, "Block Hitting", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return blockHitting$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   @Category(name = "First Person")
   @NotNull
   public final val oldItemSwitch: Boolean by ValueApiKt.boolean$default(true, "Item Switch", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return oldItemSwitch$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Boolean
      }


   @Category(name = "First Person")
   @NotNull
   public final val itemUsageSwinging: Boolean by ValueApiKt.boolean$default(true, "Usage Swinging", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return itemUsageSwinging$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Boolean
      }


   @Category(name = "Player Model")
   @NotNull
   public final val oldSneakAnimation: Boolean by ValueApiKt.boolean$default(true, "Sneak Animation", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return oldSneakAnimation$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Boolean
      }


   @Category(name = "Player Model")
   @NotNull
   public final val armorHurtTint: Boolean by ValueApiKt.boolean$default(true, "Armor Hurt Tint", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return armorHurtTint$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Boolean
      }


   @Category(name = "Fixes")
   @NotNull
   public final val rightClickFix: Boolean by ValueApiKt.boolean$default(true, "Right Click Fix", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return rightClickFix$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Boolean
      }


   @Category(name = "Visual")
   @NotNull
   public final val oldDamageTilt: Boolean by ValueApiKt.boolean$default(true, "Damage Tilt", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return oldDamageTilt$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Boolean
      }


   @Category(name = "Visual")
   @NotNull
   public final val oldHeartAnimation: Boolean by ValueApiKt.boolean$default(true, "Heart Animation", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return oldHeartAnimation$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Boolean
      }


   @JvmStatic
   public fun isBlockHittingEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.blockHitting
   }

   @JvmStatic
   public fun isOldItemSwitchEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.oldItemSwitch
   }

   @JvmStatic
   public fun isItemUsageSwingingEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.itemUsageSwinging
   }

   @JvmStatic
   public fun isOldSneakAnimationEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.oldSneakAnimation
   }

   @JvmStatic
   public fun isArmorHurtTintEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.armorHurtTint
   }

   @JvmStatic
   public fun isOldDamageTiltEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.oldDamageTilt
   }

   @JvmStatic
   public fun isOldHeartAnimationEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.oldHeartAnimation
   }

   @JvmStatic
   public fun isRightClickFixEnabled(): Boolean {
      return INSTANCE.isEnabled() && INSTANCE.rightClickFix
   }
}
