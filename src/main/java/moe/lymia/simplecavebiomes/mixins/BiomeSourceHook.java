package moe.lymia.simplecavebiomes.mixins;

import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BiomeSource.class)
public class BiomeSourceHook implements BiomeSourceExtension {
    @Unique
    private CaveBiomeProvider scb$caveBiomeProvider;

    @Override
    public CaveBiomeProvider scb$getCaveBiomeProvider() {
        return scb$caveBiomeProvider;
    }
}
