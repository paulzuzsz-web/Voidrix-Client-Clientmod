package gg.voidrix.client.v2.mixin.particle;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {
   @Accessor("resourceManager")
   ParticleResources voidrix$getResourceManager();
}
