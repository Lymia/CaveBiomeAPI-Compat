package moe.lymia.simplecavebiomes.api;

import com.blackgear.cavebiomes.mixin.NetherBiomeProviderAccessor;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import moe.lymia.simplecavebiomes.CaveBiomeAPICompat;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Manages the list of cave biomes and helps create the biome map generator.
 */
public final class SimpleCaveBiomesAPI {
    private SimpleCaveBiomesAPI() {}

    private static final Multimap<Identifier, Biome.MixedNoisePoint> NOISE_POINTS = HashMultimap.create();

    /**
     * Adds a new cave biome to the generation list. This may be called multiple times with the same registry key, in
     * which case, the biome is registered to multiple points in the noise map.
     *
     * @param biome      THe biome to register as a cave biome.
     * @param noisePoint The point on the multinoise to generate the biome at.
     */
    public static synchronized void addCaveBiome(Identifier biome, Biome.MixedNoisePoint noisePoint) {
        Preconditions.checkNotNull(biome, "`biome` must not be `null`");
        Preconditions.checkNotNull(noisePoint, "`noisePoint` must not be `null`");
        NOISE_POINTS.put(biome, noisePoint);
        CaveBiomeAPICompat.COMPAT_NOISE_LIST.put(RegistryKey.of(CaveBiomeAPICompat.REGISTRY_BIOME, biome), noisePoint);
    }

    /**
     * Adds a point where non-biome caves generate.
     *
     * @param noisePoint The point on the multinoise to generate normal caves at.
     */
    public static void addDefaultCaveBiome(Biome.MixedNoisePoint noisePoint) {
        Preconditions.checkNotNull(noisePoint, "`noisePoint` must not be `null`");
    }

    /**
     * Checks whether a biome is a cave biome.
     *
     * @param biome The biome to check.
     * @return Whether the given biome is registered as a cave biome.
     */
    public synchronized static boolean isCaveBiome(Identifier biome) {
        Preconditions.checkNotNull(biome, "`biome` must not be `null`");
        return NOISE_POINTS.containsKey(biome);
    }

    /**
     * Creates the biome generator for the biome list.
     *
     * @param biomes The biome registry to retrieve the biomes objects from.
     * @param seed   The seed for the cave biome generator.
     * @return The biome generator.
     */
    public synchronized static MultiNoiseBiomeSource createNoise(Registry<Biome> biomes, long seed) {
        Preconditions.checkNotNull(biomes, "`biomes` must not be `null`");

        List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> caveBiomes = new ArrayList<>();
        CaveBiomeAPICompat.COMPAT_BIOME_LIST.clear();
        NOISE_POINTS.forEach((identifier, point) -> {
            var optBiome = biomes.getOrEmpty(identifier);
            if (!optBiome.isPresent()) throw new IllegalArgumentException("Biome `" + identifier + "` does not exist!");
            var biome = optBiome.get();
            caveBiomes.add(Pair.of(point, () -> biome));
            CaveBiomeAPICompat.COMPAT_BIOME_LIST.add(biome);
        });

        var temperature = createParams(-9, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
        var humidity = createParams(-7, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0);
        var altitude = createParams(-9, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0);
        var weirdness = createParams(-7, 1.0, 2.0, 1.0, 0.0, 0.0, 0.0, 0.0);
        return NetherBiomeProviderAccessor.createMultiNoiseBiomeSource(seed, caveBiomes, temperature, humidity,
                altitude, weirdness, Optional.empty());
    }

    private static MultiNoiseBiomeSource.NoiseParameters createParams(int firstOctave, double... amplitudes) {
        return new MultiNoiseBiomeSource.NoiseParameters(firstOctave, new DoubleArrayList(amplitudes));
    }
}
