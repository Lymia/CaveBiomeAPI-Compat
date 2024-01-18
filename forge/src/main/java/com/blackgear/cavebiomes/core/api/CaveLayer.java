package com.blackgear.cavebiomes.core.api;

import com.google.common.base.Preconditions;
import moe.lymia.simplecavebiomes.CaveBiomeAPICompat;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesObjects;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

import java.util.List;
import java.util.Map;

/**
 * A class that helps handle the noise map used to generate cave biomes.
 * <p>
 * Compatibility shim for CaveBiomeAPI.
 */
public final class CaveLayer {
    private CaveLayer() {}

    /**
     * An immutable map of cave biomes to their multi noise parameters.
     */
    public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> CAVE_BIOMES =
            CaveBiomeAPICompat.LOCKED_NOISE_LIST;

    /**
     * An immutable list of cave biomes.
     */
    public static final List<Biome> CAVE_BIOME_LIST = CaveBiomeAPICompat.LOCKED_BIOME_LIST;

    /**
     * Creates a new cave biome map for a given biome registry and world seed.
     */
    public static MultiNoiseBiomeSource create(Registry<Biome> biomes, long seed) {
        return SimpleCaveBiomesAPI.createNoise(biomes, seed);
    }

    /**
     * Registers a new cave biome.
     *
     * @param biome      The biome to register.
     * @param noisePoint The point on the multinoise to generate the biome at.
     */
    public static void addCaveBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noisePoint) {
        Preconditions.checkNotNull(biome, "`biome` must not be `null`");
        Preconditions.checkNotNull(biome.getRegistryName(), "`biome` registry name must not be `null`");
        Preconditions.checkArgument(biome.getRegistryName().equals(CaveBiomeAPICompat.REGISTRY_BIOME.getValue()),
                "`biome` must belong to the biomes registry.");
        SimpleCaveBiomesAPI.addCaveBiome(biome.getValue(), noisePoint);
    }

    /**
     * The preset used for generating cave biomes.
     */
    public static final MultiNoiseBiomeSource.Preset CENTER_BIOME_PROVIDER =
            SimpleCaveBiomesObjects.CAVE_BIOME_PROVIDER;
}
