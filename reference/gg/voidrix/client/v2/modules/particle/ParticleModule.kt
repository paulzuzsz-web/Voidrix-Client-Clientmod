package gg.voidrix.client.v2.modules.particle

import gg.voidrix.compat.client.MCParticles
import gg.voidrix.compat.event.ClientEvents
import gg.voidrix.compat.event.CombatEvents
import gg.voidrix.compat.event.PlayerHitEntityEvent
import gg.voidrix.owolib.owo.ui.container.FlowLayout
import gg.voidrix.ui.api.module.Module
import gg.voidrix.ui.api.module.ModuleCategory
import gg.voidrix.ui.api.value.MapValue
import gg.voidrix.ui.api.value.ValueApiKt
import gg.voidrix.ui.api.value.ValueHolder
import java.util.LinkedHashMap
import kotlin.jvm.internal.SourceDebugExtension
import kotlin.jvm.internal.StringCompanionObject
import kotlinx.serialization.builtins.BuiltinSerializersKt

@SourceDebugExtension(["SMAP\nParticleModule.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ParticleModule.kt\ngg/voidrix/client/v2/modules/particle/ParticleModule\n+ 2 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,79:1\n384#2,7:80\n1869#3,2:87\n1740#3,3:89\n1#4:92\n*S KotlinDebug\n*F\n+ 1 ParticleModule.kt\ngg/voidrix/client/v2/modules/particle/ParticleModule\n*L\n52#1:80,7\n61#1:87,2\n68#1:89,3\n*E\n"])
public object ParticleModule : Module("Particles", ModuleCategory.VISUAL, false, false, false, 20) {
   public final var settingsOpen: Boolean
      internal set

   public final var previewTypeId: String?
      internal set

   public final var previewEnabled: Boolean = true
      internal set

   public final var previewSpawnActive: Boolean
      internal set

   public open val seoTags: Array<String>

   public final val showOnSelf: Boolean by ValueApiKt.boolean$default(true, "Show On Self", null, null, 12, null)
      .provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[0])
         public final get() {
         return showOnSelf$delegate.getValue(this as ValueHolder, $$delegatedProperties[0]) as java.lang.Boolean
      }


   public final var particleSettings: MutableMap<String, ParticleTypeSettings>
      public final get() {
         return particleSettings$delegate.getValue(this as ValueHolder, $$delegatedProperties[1]) as MutableMap<java.lang.String, ParticleTypeSettings>
      }

      public final set(<set-?>) {
         particleSettings$delegate.setValue(this as ValueHolder, $$delegatedProperties[1], var1)
      }


   @JvmStatic
   public fun getSettings(typeId: String): ParticleTypeSettings? {
      return INSTANCE.particleSettings.get(MCParticles.INSTANCE.normalizeId(typeId))
   }

   public fun getOrCreateSettings(typeId: String): ParticleTypeSettings {
      val normalized: java.lang.String = MCParticles.INSTANCE.normalizeId(typeId)
      val `$this$getOrPut$iv`: java.util.Map = this.particleSettings
      val `value$iv`: Any = `$this$getOrPut$iv`.get(normalized)
      val var10000: Any
      if (`value$iv` == null) {
         val var7: Any = ParticleTypeSettings(false, 0.0, 0.0, 0, 0, false, 63, null)
         `$this$getOrPut$iv`.put(normalized, var7)
         var10000 = var7
      } else {
         var10000 = `value$iv`
      }

      return var10000 as ParticleTypeSettings
   }

   public fun saveSettings() {
      this.particleSettings = MapsKt.toMutableMap(this.particleSettings)
   }

   public fun setAllEnabled(enabled: Boolean) {
      for (`element$iv` in MCParticles.INSTANCE.getAllParticleTypeIds()) {
         INSTANCE.getOrCreateSettings(`element$iv` as java.lang.String).enabled = enabled
      }

      this.saveSettings()
   }

   public fun areAllEnabled(): Boolean {
      val `$this$all$iv`: java.lang.Iterable = MCParticles.INSTANCE.getAllParticleTypeIds()
      val var7: Boolean
      if (`$this$all$iv` is java.util.Collection && (`$this$all$iv` as java.util.Collection).isEmpty()) {
         var7 = true
      } else {
         for (`element$iv` in `$this$all$iv`) {
            val var10000: ParticleTypeSettings = getSettings(`element$iv` as java.lang.String)
            if (var10000 != null && !var10000.enabled) {
               return false
            }
         }

         var7 = true
      }

      return var7
   }

   public open fun buildCustomUi(): (FlowLayout) -> Unit {
      return { settingsPanel: FlowLayout ->
         ParticleSettingsComponent.INSTANCE.build(settingsPanel, INSTANCE)
         Unit.INSTANCE
      }
   }

   public open fun createdAt(): Long {
      return 1776412800000L
   }

   @JvmStatic
   fun {
      ClientEvents.INSTANCE.getClientTickEvent().listen({ it: Unit ->
         ParticlePreviewSpawner.INSTANCE.tick()
         Unit.INSTANCE
      })
      CombatEvents.INSTANCE.getPlayerHitEntityEvent().listen(lambda_1@{ event: PlayerHitEntityEvent ->
         if (!INSTANCE.isEnabled()) {
            return@lambda_1 Unit.INSTANCE
         } else {
            for (var2 in INSTANCE.particleSettings.entrySet()) {
               val typeId: java.lang.String = var2.getKey() as java.lang.String
               val settings: ParticleTypeSettings = var2.getValue() as ParticleTypeSettings
               if (settings.spawnOnHit && settings.enabled) {
                  MCParticles.INSTANCE.spawnTrackingEmitter(event.getAttackedEntityId(), typeId)
               }
            }

            return@lambda_1 Unit.INSTANCE
         }
      })
      val var4: MapValue = ValueApiKt.map$default({ 
         LinkedHashMap() as java.util.Map
      }, BuiltinSerializersKt.serializer(StringCompanionObject.INSTANCE), ParticleTypeSettings.Companion.serializer(), null, null, null, 56, null)
      var4.setHiddenInGui(true)
      particleSettings$delegate = var4.provideDelegate(INSTANCE as ValueHolder, $$delegatedProperties[1])
   }
}
