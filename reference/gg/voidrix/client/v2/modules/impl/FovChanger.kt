package gg.voidrix.client.v2.modules.impl

import gg.voidrix.compat.client.MCCamera
import gg.voidrix.compat.client.MCLivingEntityKt
import gg.voidrix.compat.client.MCLogger
import gg.voidrix.compat.event.FovEvents
import gg.voidrix.compat.event.FovModifyEvent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.owolib.owo.ui.core.Sizing
import gg.voidrix.owolib.owo.ui.core.UIComponent
import gg.voidrix.ui.api.annotations.Category
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.GenericValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import gg.voidrix.ui.components.voidrix.VoidrixCollapsibleContainer
import gg.voidrix.ui.components.voidrix.VoidrixSliderWithInput
import gg.voidrix.ui.components.voidrix.VoidrixValueLine
import gg.voidrix.ui.config.profile.Profile
import java.util.ArrayList
import kotlin.jvm.internal.SourceDebugExtension
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger

@SourceDebugExtension(["SMAP\nFovChanger.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FovChanger.kt\ngg/voidrix/client/v2/modules/impl/FovChanger\n+ 2 MCLogger.kt\ngg/voidrix/compat/client/MCLoggerKt\n+ 3 MCGameOptions.kt\ngg/voidrix/compat/client/MCGameOptions\n+ 4 MCClient.kt\ngg/voidrix/compat/client/MCClient\n+ 5 MCLivingEntity.kt\ngg/voidrix/compat/client/MCLivingEntityKt\n+ 6 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,192:1\n63#2:193\n63#2:205\n63#2:206\n63#2:210\n13#3:194\n34#3:196\n30#3:197\n34#3:199\n32#3:200\n81#3:202\n21#3:207\n34#3:209\n32#3:211\n13#3:215\n21#3:217\n81#3:219\n32#3:221\n81#3:223\n13#3:236\n34#3:238\n30#3:239\n40#4:195\n40#4:198\n40#4:201\n40#4:203\n40#4:204\n40#4:208\n40#4:212\n40#4:216\n40#4:218\n40#4:220\n40#4:222\n40#4:224\n40#4:237\n40#4:240\n66#5:213\n85#5:214\n808#6,11:225\n*S KotlinDebug\n*F\n+ 1 FovChanger.kt\ngg/voidrix/client/v2/modules/impl/FovChanger\n*L\n131#1:193\n63#1:205\n65#1:206\n70#1:210\n133#1:194\n134#1:196\n134#1:197\n139#1:199\n140#1:200\n142#1:202\n66#1:207\n69#1:209\n71#1:211\n125#1:215\n163#1:217\n164#1:219\n182#1:221\n183#1:223\n157#1:236\n170#1:238\n176#1:239\n133#1:195\n134#1:198\n140#1:201\n142#1:203\n62#1:204\n66#1:208\n71#1:212\n125#1:216\n163#1:218\n164#1:220\n182#1:222\n183#1:224\n157#1:237\n176#1:240\n95#1:213\n108#1:214\n148#1:225,11\n*E\n"])
public object FovChanger : Module("Fov Changer", ModuleCategory.PVP, false, false, false, 20) {
   private final val log: Logger = MCLogger.getLogger("FovChanger")
   public open val seoTags: Array<String>

   @Category(name = "GENERAL OPTIONS")
   @NotNull
   public final var aimingMultiplier: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.0, 5.0) as ClosedRange, 0.01F, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return aimingMultiplier$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Number
      }

      public final set(<set-?>) {
         aimingMultiplier$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   @Category(name = "EFFECT OPTIONS")
   @NotNull
   public final var speedOneFov: Number by ValueApiKt.numeric$default(70.0, IntRange(30, 110) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
         public final get() {
         return speedOneFov$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as java.lang.Number
      }

      public final set(<set-?>) {
         speedOneFov$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   public final var speedTwoPlusFov: Number by ValueApiKt.numeric$default(70.0, IntRange(30, 110) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[2])
         public final get() {
         return speedTwoPlusFov$delegate.getValue(this as ValueHolder, $$delegatedProperties[2]) as java.lang.Number
      }

      public final set(<set-?>) {
         speedTwoPlusFov$delegate.setValue(this as ValueHolder, $$delegatedProperties[2], var1)
      }


   public final var slownessFov: Number by ValueApiKt.numeric$default(70.0, IntRange(30, 110) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[3])
         public final get() {
         return slownessFov$delegate.getValue(this as ValueHolder, $$delegatedProperties[3]) as java.lang.Number
      }

      public final set(<set-?>) {
         slownessFov$delegate.setValue(this as ValueHolder, $$delegatedProperties[3], var1)
      }


   public final var effectMultiplier: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.0, 5.0) as ClosedRange, 0.01F, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[4])
         public final get() {
         return effectMultiplier$delegate.getValue(this as ValueHolder, $$delegatedProperties[4]) as java.lang.Number
      }

      public final set(<set-?>) {
         effectMultiplier$delegate.setValue(this as ValueHolder, $$delegatedProperties[4], var1)
      }


   @Category(name = "SPRINT OPTIONS")
   @NotNull
   public final var sprintingFov: Number by ValueApiKt.numeric$default(70.0, IntRange(30, 110) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[5])
         public final get() {
         return sprintingFov$delegate.getValue(this as ValueHolder, $$delegatedProperties[5]) as java.lang.Number
      }

      public final set(<set-?>) {
         sprintingFov$delegate.setValue(this as ValueHolder, $$delegatedProperties[5], var1)
      }


   public final var sprintMultiplier: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.0, 5.0) as ClosedRange, 0.01F, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[6])
         public final get() {
         return sprintMultiplier$delegate.getValue(this as ValueHolder, $$delegatedProperties[6]) as java.lang.Number
      }

      public final set(<set-?>) {
         sprintMultiplier$delegate.setValue(this as ValueHolder, $$delegatedProperties[6], var1)
      }


   @Category(name = "FLYING OPTIONS")
   @NotNull
   public final var flyingFov: Number by ValueApiKt.numeric$default(70.0, IntRange(30, 110) as ClosedRange, null, null, null, null, 60, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[7])
         public final get() {
         return flyingFov$delegate.getValue(this as ValueHolder, $$delegatedProperties[7]) as java.lang.Number
      }

      public final set(<set-?>) {
         flyingFov$delegate.setValue(this as ValueHolder, $$delegatedProperties[7], var1)
      }


   public final var flyingMultiplier: Number by ValueApiKt.numeric$default(1.0, RangesKt.rangeTo(0.0, 5.0) as ClosedRange, 0.01F, null, null, null, 56, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[8])
         public final get() {
         return flyingMultiplier$delegate.getValue(this as ValueHolder, $$delegatedProperties[8]) as java.lang.Number
      }

      public final set(<set-?>) {
         flyingMultiplier$delegate.setValue(this as ValueHolder, $$delegatedProperties[8], var1)
      }


   public final var syncedMinecraftValues: gg.voidrix.client.v2.modules.impl.FovChanger.SyncedMinecraftValues
      public final get() {
         return syncedMinecraftValues$delegate.getValue(this as ValueHolder, $$delegatedProperties[9]) as FovChanger.SyncedMinecraftValues
      }

      public final set(<set-?>) {
         syncedMinecraftValues$delegate.setValue(this as ValueHolder, $$delegatedProperties[9], var1)
      }


   public open fun beforeMinecraftOptionsSave() {
      if (MCLogger.IS_DEBUG) {
         log.info("Saving Minecraft FOV, FOV Effects to Voidrix")
      }

      val var10003: Minecraft = Minecraft.getInstance()
      val var9: Int = (int)((double)(var10003.options.fov().get() as java.lang.Number).intValue())
      var var10004: java.lang.Number = Minecraft.getInstance()
      var10004 = (java.lang.Number)var10004.options.fovEffectScale().get()
      this.syncedMinecraftValues = FovChanger.SyncedMinecraftValues(var9, var10004.doubleValue())
   }

   public open fun resetToDefault() {
      var var10000: Minecraft = Minecraft.getInstance()
      var10000.options.fovEffectScale().set(1.0)
      var10000 = Minecraft.getInstance()
      var10000.options.save()
      super.resetToDefault()
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return lambda_15@{ settingsPanel: FlowLayout ->
         val `$this$buildCustomUi_u24lambda_u2415_u24lambda_u2414`: java.lang.Iterable = settingsPanel.children()
         val var6: java.util.Collection = ArrayList()

         for (`element$iv$iv` in `$this$buildCustomUi_u24lambda_u2415_u24lambda_u2414`) {
            if (`element$iv$iv` is VoidrixCollapsibleContainer) {
               var6.add(`element$iv$iv`)
            }
         }

         val var10000: VoidrixCollapsibleContainer = CollectionsKt.firstOrNull(var6 as java.util.List) as VoidrixCollapsibleContainer
         if (var10000 == null) {
            return@lambda_15 Unit.INSTANCE
         } else {
            val fovLine: VoidrixValueLine = VoidrixValueLine(Sizing.Companion.fill(95), Sizing.Companion.content())
            fovLine.title("Default Fov")
            var var10005: java.lang.Number = Minecraft.getInstance()
            val var12: VoidrixSliderWithInput = VoidrixSliderWithInput(
               10.0, 130.0, (var10005.options.fov().get() as java.lang.Number).intValue(), 70.0, 1.0, false, 0, 0, 0, 0.0F, 0.0F, 1760, null
            )
            var12.onChanged().subscribe({ value: Double ->
               var var10000: Minecraft = Minecraft.getInstance()
               var10000.options.fov().set((int)value)
               var10000 = Minecraft.getInstance()
               var10000.options.save()
            })
            fovLine.right(var12 as UIComponent)
            var10000.child(0, fovLine as UIComponent)
            val var14: VoidrixValueLine = VoidrixValueLine(Sizing.Companion.fill(95), Sizing.Companion.content())
            var14.title("Dynamic Fov / Fov Effects")
            var10005 = Minecraft.getInstance()
            var10005 = (java.lang.Number)var10005.options.fovEffectScale().get()
            val var18: VoidrixSliderWithInput = VoidrixSliderWithInput(0.0, 1.0, var10005.doubleValue(), 1.0, 0.01, false, 0, 0, 2, 0.0F, 0.0F, 1760, null)
            var18.onChanged().subscribe({ value: Double ->
               var var10000: Minecraft = Minecraft.getInstance()
               var10000.options.fovEffectScale().set(value)
               var10000 = Minecraft.getInstance()
               var10000.options.save()
            })
            var14.right(var18 as UIComponent)
            var10000.child(1, var14 as UIComponent)
            return@lambda_15 Unit.INSTANCE
         }
      }
   }

   public open fun createdAt(): Long {
      return 1744532285000L
   }

   @JvmStatic
   fun {
      val var4: GenericValue = ValueApiKt.generic$default({ 
         FovChanger.SyncedMinecraftValues(null, null, 3, null)
      }, FovChanger.SyncedMinecraftValues.Companion.serializer(), null, null, null, null, null, 124, null)
      var4.setUiCondition({ 
         false
      })
      syncedMinecraftValues$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[9])
      Profile.Companion.getPROFILE_LOADED_EVENT().listen(lambda_5@{ profile: Profile ->
         var var10000: Minecraft = Minecraft.getInstance()
         if (var10000.options == null) {
            return@lambda_5 Unit.INSTANCE
         } else {
            val var11: Logger = log
            val `message$iv`: java.lang.String = "Loading Minecraft Fov Options for ${profile.getName()}"
            if (MCLogger.IS_DEBUG) {
               var11.info(`message$iv`)
            }

            val var22: Int = INSTANCE.syncedMinecraftValues.fov
            if (var22 != null) {
               val var12: Int = var22.intValue()
               val `message$ivx`: java.lang.String = "Applying FOV $var12"
               if (MCLogger.IS_DEBUG) {
                  log.info(`message$ivx`)
               }

               val `$i$f$voidrixDebug`: Double = var12
               var10000 = Minecraft.getInstance()
               var10000.options.fov().set((int)`$i$f$voidrixDebug`)
            }

            val var24: java.lang.Double = INSTANCE.syncedMinecraftValues.fovEffects
            if (var24 != null) {
               val var13: Double = var24.doubleValue()
               val `message$ivx`: java.lang.String = "Applying FOV Effects $var13"
               if (MCLogger.IS_DEBUG) {
                  log.info(`message$ivx`)
               }

               var10000 = Minecraft.getInstance()
               var10000.options.fovEffectScale().set(var13)
            }

            return@lambda_5 Unit.INSTANCE
         }
      })
      FovEvents.INSTANCE.getFlyingFovMultiplierEvent().listen(lambda_6@{ event: FovModifyEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_6 Unit.INSTANCE
         } else {
            event.setFov(INSTANCE.flyingMultiplier.doubleValue() * event.getFov())
            return@lambda_6 Unit.INSTANCE
         }
      })
      FovEvents.INSTANCE.getAimingFovMultiplierEvent().listen(lambda_7@{ event: FovModifyEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_7 Unit.INSTANCE
         } else {
            event.setFov(INSTANCE.aimingMultiplier.doubleValue() * event.getFov())
            return@lambda_7 Unit.INSTANCE
         }
      })
      FovEvents.INSTANCE.getAfterWalkingFovMultiplierEvent().listen(lambda_9@{ event: FovModifyEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_9 Unit.INSTANCE
         } else {
            val var10000: Entity = MCCamera.getCameraEntity()
            if (var10000 == null) {
               return@lambda_9 Unit.INSTANCE
            } else {
               var multiplier: Double = 0.0
               multiplier = event.getFov()
               if (var10000.isSprinting()) {
                  multiplier *= INSTANCE.sprintMultiplier.doubleValue()
               }

               val var9: LivingEntity = var10000 as? LivingEntity
               if ((var10000 as? LivingEntity) != null) {
                  val var10: java.util.Collection = var9.getActiveEffects()
                  if (!var10.isEmpty()) {
                     multiplier *= INSTANCE.effectMultiplier.doubleValue()
                  }
               }

               event.setFov(multiplier)
               return@lambda_9 Unit.INSTANCE
            }
         }
      })
      FovEvents.INSTANCE.getDefaultFovEvent().listen(lambda_10@{ event: FovModifyEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_10 Unit.INSTANCE
         } else {
            val var10000: Entity = MCCamera.getCameraEntity()
            if (var10000 == null) {
               return@lambda_10 Unit.INSTANCE
            } else {
               val livingEntity: LivingEntity = var10000 as? LivingEntity
               if ((var10000 as? AbstractClientPlayer) != null && (var10000 as? AbstractClientPlayer).getAbilities().flying) {
                  event.setFov(INSTANCE.flyingFov.doubleValue())
                  return@lambda_10 Unit.INSTANCE
               } else {
                  val var8: MobEffectInstance = if (livingEntity != null) MCLivingEntityKt.getSpeedEffect(livingEntity) else null
                  if ((if (livingEntity != null) MCLivingEntityKt.getSlownessEffect(livingEntity) else null) != null) {
                     event.setFov(INSTANCE.slownessFov.doubleValue())
                  } else if (var8 != null && var8.getAmplifier() == 0) {
                     event.setFov(INSTANCE.speedOneFov.doubleValue())
                  } else if ((if (var8 != null) var8.getAmplifier() else 0) > 0) {
                     event.setFov(INSTANCE.speedTwoPlusFov.doubleValue())
                  } else if (var10000.isSprinting()) {
                     event.setFov(INSTANCE.sprintingFov.doubleValue())
                  } else {
                     val var10001: Minecraft = Minecraft.getInstance()
                     event.setFov((double)(var10001.options.fov().get() as java.lang.Number).intValue())
                  }

                  return@lambda_10 Unit.INSTANCE
               }
            }
         }
      })
   }

   @Serializable
   public data class SyncedMinecraftValues(fov: Int? = null, fovEffects: Double? = null) {
      public final val fov: Int?
      public final val fovEffects: Double?

      init {
         this.fov = fov
         this.fovEffects = fovEffects
      }

      public operator fun component1(): Int? {
         return this.fov
      }

      public operator fun component2(): Double? {
         return this.fovEffects
      }

      public fun copy(fov: Int? = this.fov, fovEffects: Double? = this.fovEffects): gg.voidrix.client.v2.modules.impl.FovChanger.SyncedMinecraftValues {
         return FovChanger.SyncedMinecraftValues(fov, fovEffects)
      }

      public override fun toString(): String {
         return "SyncedMinecraftValues(fov=${this.fov}, fovEffects=${this.fovEffects})"
      }

      public override fun hashCode(): Int {
         return (if (this.fov == null) 0 else this.fov.hashCode()) * 31 + (if (this.fovEffects == null) 0 else this.fovEffects.hashCode())
      }

      public override operator fun equals(other: Any?): Boolean {
         label28@
         if (this === other) {
            return true
         } else {
            return other is FovChanger.SyncedMinecraftValues
               && this.fov == (other as FovChanger.SyncedMinecraftValues).fov
               && this.fovEffects == (other as FovChanger.SyncedMinecraftValues).fovEffects
            }
      }

      fun SyncedMinecraftValues() {
         this(null, null, 3, null)
      }

      public companion object {
         public fun serializer(): KSerializer<gg.voidrix.client.v2.modules.impl.FovChanger.SyncedMinecraftValues> {
            return FovChanger.SyncedMinecraftValues.$serializer.INSTANCE as KSerializer<FovChanger.SyncedMinecraftValues>
         }
      }
   }
}
