package gg.norisk.client.v2.mixin.branding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.norisk.compat.auth.NoriskAuth;
import gg.norisk.compat.loader.ModLoadingHelper;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientBrandRetriever.class, remap = false)
public class BrandingClientBrandRetrieverMixin {
   @ModifyReturnValue(method = "getClientModName", at = @At("RETURN"), remap = false)
   private static String nrc$brandClientModName(String original) {
      String prefix = NoriskAuth.INSTANCE.isExperimental() ? "§c" : "";
      String version = ModLoadingHelper.INSTANCE.getModVersion("nrcclient");
      String suffix = version != null ? ":" + version.split("[-+]", 2)[0] : "";
      return prefix + "nrc" + suffix;
   }
}
