//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.blackgear.cavebiomes.core.api;

import com.blackgear.cavebiomes.core.CBAConfig;
import com.google.common.base.Preconditions;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesObjects;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraftforge.common.BiomeDictionary;

public final class CaveBiomeAPI {
    private CaveBiomeAPI() {}

    public static final BiomeDictionary.Type UNDERGROUND = SimpleCaveBiomesObjects.UNDERGROUND;
    private static MultiNoiseBiomeSource caveBiomeSource;

    public static void initializeCaveBiomes(Registry<Biome> biomeRegistry, long seed) {
        caveBiomeSource = CaveLayer.create(biomeRegistry, seed);
    }

    public static Biome injectCaveBiomes(Biome surfaceBiomes, int x, int y, int z) {
        Biome caveBiomes = caveBiomeSource.getBiomeForNoiseGen(x, 0, z);
        if ((float) y <= 12.0F + surfaceBiomes.getDepth() * 3.0F && y >= 1) {
            if ((Boolean) CBAConfig.GENERATE_DEFAULT_CAVES.get()) {
                return caveBiomes;
            }

            if (caveBiomes.getCategory() != Category.EXTREME_HILLS) {
                return caveBiomes;
            }
        }

        return surfaceBiomes;
    }

    /**
     * Registers a new cave biome.
     *
     * @param biome      The biome to register.
     * @param noisePoint The point on the multinoise to generate the biome at.
     */
    public static void addCaveBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noisePoint) {
        Preconditions.checkNotNull(biome, "`biome` must not be `null`");
        SimpleCaveBiomesAPI.addCaveBiome(biome.getValue(), noisePoint);

    }

    /**
     * Registers a new cave biome.
     *
     * @param biome      The biome to register.
     * @param noisePoint The point on the multinoise to generate the biome at.
     */
    public static void addCaveBiome(Biome biome, Biome.MixedNoisePoint noisePoint) {
        Preconditions.checkNotNull(biome, "`biome` must not be `null`");
        Preconditions.checkNotNull(biome.getRegistryName(), "`biome` registry name must not be `null`");
        SimpleCaveBiomesAPI.addCaveBiome(biome.getRegistryName(), noisePoint);
    }

    /**
     * Does nothing.
     */
    @Deprecated
    public static void addDefaultCaves() {
        // does nothing
    }
}
