package gg.norisk.client.v2.modules.glintcolorizer

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.AddressMode
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuSampler
import java.io.Closeable
import java.io.InputStream
import kotlin.jvm.internal.SourceDebugExtension
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.feature.ItemFeatureRenderer
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.OutputTarget
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.rendertype.TextureTransform
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.ARGB
import net.minecraft.util.Util
import org.jetbrains.annotations.NotNull
import org.joml.Matrix4f

@SourceDebugExtension(["SMAP\nNrcGlintRenderTypes.kt\nKotlin\n*S Kotlin\n*F\n+ 1 NrcGlintRenderTypes.kt\ngg/norisk/client/v2/modules/glintcolorizer/NrcGlintRenderTypes\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,563:1\n1#2:564\n*E\n"])
public object NrcGlintRenderTypes {
   private final val GRAYSCALE_GLINT_ITEM: Identifier = Identifier.fromNamespaceAndPath("noriskclient", "dynamic/grayscale_glint_item")
   private final val GRAYSCALE_GLINT_ARMOR: Identifier = Identifier.fromNamespaceAndPath("noriskclient", "dynamic/grayscale_glint_armor")

   @JvmField
   public final var texturesRegistered: Boolean
      private set

   @JvmField
   @NotNull
   public final val NRC_GLINT_TEXTURING: TextureTransform = TextureTransform("nrc_glint_texturing", { 
      INSTANCE.setupNrcGlintTexturing(8.0F)
   })

   @JvmField
   @NotNull
   public final val NRC_ENTITY_GLINT_TEXTURING: TextureTransform = TextureTransform("nrc_entity_glint_texturing", { 
      INSTANCE.setupNrcGlintTexturing(0.5F)
   })

   @JvmField
   @NotNull
   public final val NRC_ARMOR_GLINT_TEXTURING: TextureTransform = TextureTransform("nrc_armor_glint_texturing", { 
      INSTANCE.setupNrcGlintTexturing(0.16F)
   })

   private final val VANILLA_SAMPLER: () -> GpuSampler = { 
      RenderSystem.getSamplerCache().getSampler(AddressMode.REPEAT, AddressMode.REPEAT, FilterMode.NEAREST, FilterMode.LINEAR, false)
   }

   @JvmField
   @NotNull
   public final val NRC_GLINT: RenderType

   @JvmField
   @NotNull
   public final val NRC_GLINT_TRANSLUCENT: RenderType

   @JvmField
   @NotNull
   public final val NRC_ENTITY_GLINT: RenderType

   @JvmField
   @NotNull
   public final val NRC_ARMOR_ENTITY_GLINT: RenderType

   @JvmStatic
   public fun registerGrayscaleTextures() {
      val mc: Minecraft = Minecraft.getInstance()
      val textureManager: TextureManager = mc.getTextureManager()
      val resourceManager: ResourceManager = mc.getResourceManager()
      var var10000: NrcGlintRenderTypes = INSTANCE
      var var10003: Identifier = ItemFeatureRenderer.ENCHANTED_GLINT_ITEM
      var var10004: Identifier = GRAYSCALE_GLINT_ITEM
      var10000.createAndRegister(textureManager, resourceManager, var10003, var10004)
      var10000 = INSTANCE
      var10003 = ItemFeatureRenderer.ENCHANTED_GLINT_ARMOR
      var10004 = GRAYSCALE_GLINT_ARMOR
      var10000.createAndRegister(textureManager, resourceManager, var10003, var10004)
      texturesRegistered = true
   }

   private fun createAndRegister(textureManager: TextureManager, resourceManager: ResourceManager, source: Identifier, target: Identifier) {
      val var10000: DynamicTexture = this.createGrayscaleTexture(resourceManager, source)
      if (var10000 != null) {
         textureManager.register(target, var10000 as AbstractTexture)
      }
   }

   private fun createGrayscaleTexture(resourceManager: ResourceManager, source: Identifier): DynamicTexture? {
      var resource: DynamicTexture
      try {
         val var10000: Resource = resourceManager.getResource(source).orElse(null) as Resource
         if (var10000 == null) {
            return null
         }

         val var4: Closeable = var10000.open()
         var var5: java.lang.Throwable = null

         var var17: DynamicTexture
         try {
            val original: NativeImage = NativeImage.read(var4 as InputStream)
            val grayscale: NativeImage = original.mappedCopy({ abgr: Int ->
               val argb: Int = ARGB.fromABGR(abgr)
               val lum: Int = Math.max(ARGB.red(argb), Math.max(ARGB.green(argb), ARGB.blue(argb)))
               ARGB.toABGR(ARGB.color(ARGB.alpha(argb), lum, lum, lum))
            })
            original.close()
            var17 = DynamicTexture({ 
               "nrc_grayscale_glint"
            }, grayscale)
         } catch (var13: java.lang.Throwable) {
            var5 = var13
            throw var13
         } finally {
            CloseableKt.closeFinally(var4, var5)
         }

         resource = var17
      } catch (var15: Exception) {
         resource = null
      }

      return resource
   }

   private fun setupNrcGlintTexturing(vanillaScale: Float): Matrix4f {
      val glintSpeed: Double = Minecraft.getInstance().gameRenderer.gameRenderState().optionsRenderState.glintSpeed
      val l: Long = (long)(Util.getMillis() * glintSpeed * 8.0 * GlintColorizerModule.getSpeedMultiplier())
      val var10000: Matrix4f = Matrix4f()
         .translation(-((float)(l % 110000L) / 110000.0F), (float)(l % 30000L) / 30000.0F, 0.0F)
         .rotateZ(GlintColorizerModule.getRotationRadians())
         .scale(vanillaScale * GlintColorizerModule.getScaleValue())
         return var10000
   }

   @JvmStatic
   public fun glint(): RenderType {
      val var10000: RenderType
      if (GlintColorizerModule.isActive()) {
         var10000 = NRC_GLINT
      } else {
         var10000 = RenderTypes.glint()
      }

      return var10000
   }

   @JvmStatic
   public fun glintTranslucent(): RenderType {
      val var10000: RenderType
      if (GlintColorizerModule.isActive()) {
         var10000 = NRC_GLINT_TRANSLUCENT
      } else {
         var10000 = RenderTypes.glintTranslucent()
      }

      return var10000
   }

   @JvmStatic
   public fun entityGlint(): RenderType {
      val var10000: RenderType
      if (GlintColorizerModule.isActive()) {
         var10000 = NRC_ENTITY_GLINT
      } else {
         var10000 = RenderTypes.entityGlint()
      }

      return var10000
   }

   @JvmStatic
   public fun armorEntityGlint(): RenderType {
      val var10000: RenderType
      if (GlintColorizerModule.isActive()) {
         var10000 = NRC_ARMOR_ENTITY_GLINT
      } else {
         var10000 = RenderTypes.armorEntityGlint()
      }

      return var10000
   }

   @JvmStatic
   public fun isNrcGlint(rt: RenderType): Boolean {
      return rt === NRC_GLINT || rt === NRC_GLINT_TRANSLUCENT || rt === NRC_ENTITY_GLINT || rt === NRC_ARMOR_ENTITY_GLINT
   }

   @JvmStatic
   fun {
      var var10000: RenderType = RenderType.create("nrc_glint", RenderSetup.builder(RenderPipelines.GLINT).withTexture("Sampler0", GRAYSCALE_GLINT_ITEM, { 
         `$tmp0`() as GpuSampler
      }).setTextureTransform(NRC_GLINT_TEXTURING).createRenderSetup())
      NRC_GLINT = var10000
      var10000 = RenderType.create("nrc_glint_translucent", RenderSetup.builder(RenderPipelines.GLINT).withTexture("Sampler0", GRAYSCALE_GLINT_ITEM, { 
         `$tmp0`() as GpuSampler
      }).setTextureTransform(NRC_GLINT_TEXTURING).setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).createRenderSetup())
      NRC_GLINT_TRANSLUCENT = var10000
      var10000 = RenderType.create("nrc_entity_glint", RenderSetup.builder(RenderPipelines.GLINT).withTexture("Sampler0", GRAYSCALE_GLINT_ITEM, { 
         `$tmp0`() as GpuSampler
      }).setTextureTransform(NRC_ENTITY_GLINT_TEXTURING).createRenderSetup())
      NRC_ENTITY_GLINT = var10000
      var10000 = RenderType.create("nrc_armor_entity_glint", RenderSetup.builder(RenderPipelines.GLINT).withTexture("Sampler0", GRAYSCALE_GLINT_ARMOR, { 
         `$tmp0`() as GpuSampler
      }).setTextureTransform(NRC_ARMOR_GLINT_TEXTURING).setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).createRenderSetup())
      NRC_ARMOR_ENTITY_GLINT = var10000
   }
}
