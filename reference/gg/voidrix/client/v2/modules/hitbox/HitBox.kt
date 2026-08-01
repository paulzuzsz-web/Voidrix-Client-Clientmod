package gg.voidrix.client.v2.modules.hitbox

import gg.voidrix.compat.annotations.VoidrixMiniTag
import gg.voidrix.compat.client.MCClient
import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.serializable.MultiColor
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.awt.Color
import java.util.LinkedHashMap
import java.util.Map.Entry
import kotlin.enums.EnumEntries
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.StringCompanionObject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.BuiltinSerializersKt
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile

@VoidrixMiniTag(tags = ["hitbox"])
@SourceDebugExtension(["SMAP\nHitBox.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HitBox.kt\ngg/voidrix/client/v2/modules/hitbox/HitBox\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,669:1\n384#2,7:670\n465#2:677\n415#2:678\n1252#3,4:679\n1193#3,2:683\n1267#3,4:685\n*S KotlinDebug\n*F\n+ 1 HitBox.kt\ngg/voidrix/client/v2/modules/hitbox/HitBox\n*L\n137#1:670,7\n131#1:677\n131#1:678\n131#1:679,4\n126#1:683,2\n126#1:685,4\n*E\n"])
public object HitBox : Module("Hitbox", ModuleCategory.VISUAL, false, false, false, 28) {
   public open val seoTags: Array<String>
   private final val defaultSettings: Map<String, gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings>

   public final var settings: MutableMap<String, gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings>
      public final get() {
         return settings$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as MutableMap<java.lang.String, HitBox.HitBoxSettings>
      }

      public final set(<set-?>) {
         settings$delegate.setValue(this as ValueHolder, $$delegatedProperties[0], var1)
      }


   public final val isRenderActive: Boolean
      public final get() {
         return this.isEnabled() || HitBoxPreview.INSTANCE.active
      }


   public fun getSettings(type: gg.voidrix.client.v2.modules.hitbox.HitBox.Type): gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings {
      val `$this$getOrPut$iv`: java.util.Map = this.settings
      val `key$iv`: Any = type.name()
      val `value$iv`: Any = `$this$getOrPut$iv`.get(`key$iv`)
      var var9: Any
      if (`value$iv` == null) {
         run label20@{
            var9 = defaultSettings.get(type.name())
            if (var9 != null) {
               var9 = ((HitBox.HitBoxSettings)var9).deepCopy()
               if (var9 != null) {
                  return@label20
               }
            }

            var9 = HitBox.HitBoxSettings(false, 0.0F, null, null, false, false, null, 127, null)
         }

         `$this$getOrPut$iv`.put(`key$iv`, var9)
         var9 = var9
      } else {
         var9 = `value$iv`
      }

      return var9 as HitBox.HitBoxSettings
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         HitBoxSettingsComponent.INSTANCE.build(settingsPanel, INSTANCE)
         Unit.INSTANCE
      }
   }

   @JvmStatic
   public fun getEffectiveColor(entity: Any, settings: gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings): Color {
      return if (settings.dynamicColor && entity == MCClient.getCrosshairEntity())
         settings.enemyColor.getChromaOrDefault()
         else
         settings.multiColor.getChromaOrDefault()
      }

   @JvmStatic
   public fun shouldSkipHitbox(entity: Any): Boolean {
      val className: java.lang.String = entity.getClass().getName()
      return StringsKt.contains$default(className, "CosmeticShowcasePlayer", false, 2, null)
         || StringsKt.contains$default(className, "FakePlayer", false, 2, null)
      }

   @JvmStatic
   fun {
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         HitBoxPreview.INSTANCE.tick()
         Unit.INSTANCE
      })
      val var12: java.lang.Iterable = HitBox.Type.getEntries() as java.lang.Iterable
      val `destination$iv$iv`: java.util.Map = LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(var12, 10)), 16))

      for (`element$iv$iv` in var12) {
         val var13: Pair = TuplesKt.to(
            (`element$iv$iv` as HitBox.Type).name(),
            HitBox.HitBoxSettings(
               false,
               0.0F,
               null,
               null,
               `element$iv$iv` as HitBox.Type != HitBox.Type.SELF && `element$iv$iv` as HitBox.Type != HitBox.Type.PLAYER,
               false,
               null,
               111,
               null
            )
         )
         `destination$iv$iv`.put(var13.getFirst(), var13.getSecond())
      }

      defaultSettings = `destination$iv$iv`
      settings$delegate = ValueApiKt.map$default({ 
            val `$this$mapValuesTo$iv$iv`: java.util.Map = defaultSettings
            val `destination$iv$iv`: java.util.Map = LinkedHashMap(MapsKt.mapCapacity(defaultSettings.size()))

            for (`element$iv$iv$iv` in `$this$mapValuesTo$iv$iv`.entrySet()) {
               `destination$iv$iv`.put((`element$iv$iv$iv` as Entry).getKey(), ((`element$iv$iv$iv` as Entry).getValue() as HitBox.HitBoxSettings).deepCopy())
            }

            MapsKt.toMutableMap(`destination$iv$iv`)
         }, BuiltinSerializersKt.serializer(StringCompanionObject.INSTANCE), HitBox.HitBoxSettings.Companion.serializer(), null, null, null, 56, null)
         .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
      }

   @Serializable
   public data class HitBoxSettings(visible: Boolean = true,
      lineWidth: Float = 1.0F,
      multiColor: MultiColor = MultiColor(null, false, false, 7, null),
      facingColor: MultiColor = MultiColor(gg.voidrix.owolib.owo.ui.core.Color.Companion.ofArgb(-16776961), false, false, 6, null),
      showLookVector: Boolean = false,
      dynamicColor: Boolean = false,
      enemyColor: MultiColor = MultiColor(gg.voidrix.owolib.owo.ui.core.Color.Companion.ofArgb(-65536), false, false, 6, null)
   ) {
      public final var visible: Boolean
         internal set

      public final var lineWidth: Float
         internal set

      public final var multiColor: MultiColor
         internal set

      public final var facingColor: MultiColor
         internal set

      public final var showLookVector: Boolean
         internal set

      public final var dynamicColor: Boolean
         internal set

      public final var enemyColor: MultiColor
         internal set

      init {
         this.visible = visible
         this.lineWidth = lineWidth
         this.multiColor = multiColor
         this.facingColor = facingColor
         this.showLookVector = showLookVector
         this.dynamicColor = dynamicColor
         this.enemyColor = enemyColor
      }

      public fun deepCopy(): gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings {
         return copy$default(
            this,
            false,
            0.0F,
            MultiColor.copy$default(this.multiColor, null, false, false, 7, null),
            MultiColor.copy$default(this.facingColor, null, false, false, 7, null),
            false,
            false,
            MultiColor.copy$default(this.enemyColor, null, false, false, 7, null),
            51,
            null
         )
      }

      public operator fun component1(): Boolean {
         return this.visible
      }

      public operator fun component2(): Float {
         return this.lineWidth
      }

      public operator fun component3(): MultiColor {
         return this.multiColor
      }

      public operator fun component4(): MultiColor {
         return this.facingColor
      }

      public operator fun component5(): Boolean {
         return this.showLookVector
      }

      public operator fun component6(): Boolean {
         return this.dynamicColor
      }

      public operator fun component7(): MultiColor {
         return this.enemyColor
      }

      public fun copy(
         visible: Boolean = this.visible,
         lineWidth: Float = this.lineWidth,
         multiColor: MultiColor = this.multiColor,
         facingColor: MultiColor = this.facingColor,
         showLookVector: Boolean = this.showLookVector,
         dynamicColor: Boolean = this.dynamicColor,
         enemyColor: MultiColor = this.enemyColor
      ): gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings {
         return HitBox.HitBoxSettings(visible, lineWidth, multiColor, facingColor, showLookVector, dynamicColor, enemyColor)
      }

      public override fun toString(): String {
         return "HitBoxSettings(visible=${this.visible}, lineWidth=${this.lineWidth}, multiColor=${this.multiColor}, facingColor=${this.facingColor}, showLookVector=${this.showLookVector}, dynamicColor=${this.dynamicColor}, enemyColor=${this.enemyColor})"
      }

      public override fun hashCode(): Int {
         return (
                  (
                           (
                                    (
                                             (java.lang.Boolean.hashCode(this.visible) * 31 + java.lang.Float.hashCode(this.lineWidth)) * 31
                                                + this.multiColor.hashCode()
                                          )
                                          * 31
                                       + this.facingColor.hashCode()
                                 )
                                 * 31
                              + java.lang.Boolean.hashCode(this.showLookVector)
                        )
                        * 31
                     + java.lang.Boolean.hashCode(this.dynamicColor)
               )
               * 31
            + this.enemyColor.hashCode()
         }

      public override operator fun equals(other: Any?): Boolean {
         label58@
         if (this === other) {
            return true
         } else {
            return other is HitBox.HitBoxSettings
               && this.visible == (other as HitBox.HitBoxSettings).visible
               && java.lang.Float.compare(this.lineWidth, (other as HitBox.HitBoxSettings).lineWidth) == 0
               && this.multiColor == (other as HitBox.HitBoxSettings).multiColor
               && this.facingColor == (other as HitBox.HitBoxSettings).facingColor
               && this.showLookVector == (other as HitBox.HitBoxSettings).showLookVector
               && this.dynamicColor == (other as HitBox.HitBoxSettings).dynamicColor
               && this.enemyColor == (other as HitBox.HitBoxSettings).enemyColor
            }
      }

      fun HitBoxSettings() {
         this(false, 0.0F, null, null, false, false, null, 127, null)
      }

      public companion object {
         public fun serializer(): KSerializer<gg.voidrix.client.v2.modules.hitbox.HitBox.HitBoxSettings> {
            return HitBox.HitBoxSettings.$serializer.INSTANCE as KSerializer<HitBox.HitBoxSettings>
         }
      }
   }

   public enum class Type(displayName: String) {
      SELF("Self"),
      PLAYER("Players"),
      MOBS("Mobs"),
      ITEM("Items"),
      PROJECTILE("Projectiles"),
      EXPERIENCE("Experience Orbs"),
      OTHER("Other");

      public final val displayName: String

      init {
         this.displayName = displayName
      }

      @JvmStatic
      fun getEntries(): EnumEntries<HitBox.Type> {
         $ENTRIES
      }

      @SourceDebugExtension(["SMAP\nHitBox.kt\nKotlin\n*S Kotlin\n*F\n+ 1 HitBox.kt\ngg/voidrix/client/v2/modules/hitbox/HitBox$Type$Companion\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 MCClient.kt\ngg/voidrix/compat/client/MCClient\n*L\n1#1,669:1\n1#2:670\n185#3:671\n40#3:672\n*S KotlinDebug\n*F\n+ 1 HitBox.kt\ngg/voidrix/client/v2/modules/hitbox/HitBox$Type$Companion\n*L\n106#1:671\n106#1:672\n*E\n"])
      public companion object {
         public fun classify(entity: Any): gg.voidrix.client.v2.modules.hitbox.HitBox.Type? {
            val `$i$f$getPlayer`: HitBox.Type = HitBoxPreview.INSTANCE.forcedType(entity)
            if (`$i$f$getPlayer` != null) {
               return `$i$f$getPlayer`
            } else if (entity !is Entity) {
               return null
            } else if (HitBox.shouldSkipHitbox(entity)) {
               return null
            } else if (entity is Player) {
               val var10001: Minecraft = Minecraft.getInstance()
               return if (entity == var10001.player) HitBox.Type.SELF else HitBox.Type.PLAYER
            } else if (entity is Mob) {
               return HitBox.Type.MOBS
            } else if (entity is ItemEntity) {
               return HitBox.Type.ITEM
            } else if (entity is Projectile) {
               return HitBox.Type.PROJECTILE
            } else {
               return if (entity is ExperienceOrb) HitBox.Type.EXPERIENCE else HitBox.Type.OTHER
            }
         }
      }
   }
}
