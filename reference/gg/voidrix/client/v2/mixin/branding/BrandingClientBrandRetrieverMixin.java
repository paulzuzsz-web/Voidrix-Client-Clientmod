package gg.voidrix.client.v2.mixin.branding;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gg.voidrix.compat.auth.VoidrixAuth;
import gg.voidrix.compat.loader.ModLoadingHelper;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientBrandRetriever.class, remap = false)
public class BrandingClientBrandRetrieverMixin {
   @ModifyReturnValue(method = "getClientModName", at = @At("RETURN"), remap = false)
   private static String voidrix$brandClientModName(String original) {
      String prefix = VoidrixAuth.INSTANCE.isExperimental() ? "§c" : "";
      String version = ModLoadingHelper.INSTANCE.getModVersion("voidrix");
      String suffix = version != null ? ":" + version.split("[-+]", 2)[0] : "";
      return prefix + "voidrix" + suffix;
   }
}
