package com.blackgear.cavebiomes.core.api;

import com.google.common.base.Preconditions;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesObjects;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Compatibility shim for CaveBiomeAPI.
 */
public final class CaveBiomeAPI {
    private CaveBiomeAPI() {}

    public static final BiomeDictionary.Type UNDERGROUND = SimpleCaveBiomesObjects.UNDERGROUND;

    @Deprecated
    public static void initializeCaveBiomes(Registry<Biome> biomeRegistry, long seed) {
        // does nothing
    }

    @Deprecated
    public static Biome injectCaveBiomes(Biome surfaceBiomes, int x, int y, int z) {
        // also does nothing
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

    @Deprecated
    public static void addDefaultCaves() {
        // still does nothing
    }
}
