package gg.norisk.client.v2.mixin.glintcolorizer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gg.norisk.client.v2.modules.glintcolorizer.GlintColorizerModule;
import gg.norisk.client.v2.modules.glintcolorizer.NrcGlintRenderTypes;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EquipmentLayerRenderer.class)
public abstract class GlintArmorMixin {
   @WrapOperation(
      method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorEntityGlint()Lnet/minecraft/client/renderer/rendertype/RenderType;"
      )
   )
   private RenderType nrc$redirectArmorGlint(Operation<RenderType> original) {
      return GlintColorizerModule.isActive() ? NrcGlintRenderTypes.armorEntityGlint() : (RenderType)original.call(new Object[0]);
   }
}
