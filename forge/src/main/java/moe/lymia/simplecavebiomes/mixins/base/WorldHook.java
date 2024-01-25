package moe.lymia.simplecavebiomes.mixins.base;

import moe.lymia.simplecavebiomes.ScbConfig;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class WorldHook {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;" +
            "getBiomeAccessType()Lnet/minecraft/world/biome/source/BiomeAccessType;"))
    private BiomeAccessType hookDimensionType(DimensionType instance) {
        World self = (World) (Object) this;
        Identifier dimensionId = self.getRegistryKey().getValue();
        BiomeAccessType accessType = instance.getBiomeAccessType();

        if (ScbConfig.isDimensionWhitelisted(dimensionId)) {
            if (accessType instanceof HorizontalVoronoiBiomeAccessType) return VoronoiBiomeAccessType.INSTANCE;
        }
        return accessType;
    }
}
