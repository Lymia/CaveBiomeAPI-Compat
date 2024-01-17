package moe.lymia.simplecavebiomes.world;

import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

public final class CaveBiomeProvider {
    private final MultiNoiseBiomeSource caveBiomeSource;

    public CaveBiomeProvider(Registry<Biome> biomes, long seed) {
        this.caveBiomeSource = SimpleCaveBiomesAPI.createNoise(biomes, seed);
    }

    public Biome getBiome(Biome surfaceBiome, int x, int y, int z) {
        if ((float)y <= 12.0F + surfaceBiome.getDepth() * 4.0F && y >= 1) {
            Biome caveBiomes = caveBiomeSource.getBiomeForNoiseGen(x, y, z);
            if (caveBiomes.getCategory() != Biome.Category.EXTREME_HILLS) {
                return caveBiomes;
            }
        }
        return surfaceBiome;
    }
}
