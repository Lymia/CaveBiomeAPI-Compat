package moe.lymia.simplecavebiomes.world;

import moe.lymia.simplecavebiomes.ScbConfig;
import moe.lymia.simplecavebiomes.SimpleCaveBiomes;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;
import java.util.function.Supplier;

public final class CaveBiomeProvider {
    private final MultiNoiseBiomeSource caveBiomeSource;
    private final Identifier dimension;

    public CaveBiomeProvider(Registry<Biome> biomes, long seed, Identifier dimension) {
        this.caveBiomeSource = SimpleCaveBiomesAPI.createNoise(biomes, seed);
        this.dimension = dimension;
    }

    public Biome getCaveBiome(int x, int y, int z) {
        return caveBiomeSource.getBiomeForNoiseGen(x, y, z);
    }

    public Biome filterCaveBiome(Biome surfaceBiome, int x, int y, int z) {
        if ((float) y <= 12.0F + surfaceBiome.getDepth() * 4.0F && y >= 1) {
            Biome caveBiomes = getCaveBiome(x, y, z);
            if (caveBiomes.getCategory() != Biome.Category.EXTREME_HILLS) {
                return caveBiomes;
            }
        }
        return surfaceBiome;
    }

    public void generateCaveFeatures(ChunkGenerator generator, ChunkRegion region) {
        int mainChunkX = region.getCenterChunkX();
        int mainChunkZ = region.getCenterChunkZ();
        int x = mainChunkX * 16;
        int z = mainChunkZ * 16;
        BlockPos pos = new BlockPos(x, 0, z);
        Biome caveBiome = getCaveBiome((mainChunkX << 2) + 2, 10, (mainChunkZ << 2) + 2);

        if (ScbConfig.isDebug()) {
            SimpleCaveBiomes.LOGGER.info(
                    "generateCaveFeatures | dimension: " + dimension + ", chunk: " + mainChunkX + ", " + mainChunkZ +
                            ", biome: " + caveBiome.getRegistryName());
        }

        if (SimpleCaveBiomesAPI.isCaveBiome(caveBiome.getRegistryName())) {
            ChunkRandom random = new ChunkRandom();
            long seed = random.setPopulationSeed(region.getSeed(), x, z);

            try {
                generateOnlyFeatures(caveBiome, generator, region, seed, random, pos);
            } catch (Exception e) {
                CrashReport report = CrashReport.create(e, "Cave biome decoration");
                report.addElement("Generation").add("CenterX", mainChunkX).add("CenterZ", mainChunkZ).add("Seed", seed)
                        .add("Biome", caveBiome);
                throw new CrashException(report);
            }
        }
    }

    private static void generateOnlyFeatures(Biome biome, ChunkGenerator generator, ChunkRegion region, long seed,
            ChunkRandom rand, BlockPos pos) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().getFeatures();

        int stages = GenerationStep.Feature.values().length;
        for (int generationStageIndex = 0; generationStageIndex < stages; ++generationStageIndex) {
            int featureSeed = 1001;
            if (features.size() > generationStageIndex) {
                for (Supplier<ConfiguredFeature<?, ?>> supplier : features.get(generationStageIndex)) {
                    ConfiguredFeature<?, ?> configuredFeature = supplier.get();
                    rand.setPopulationSeed(seed, featureSeed, generationStageIndex);

                    try {
                        configuredFeature.generate(region, generator, rand, pos);
                    } catch (Exception e) {
                        CrashReport crashReport = CrashReport.create(e, "Cave feature placement");
                        crashReport.addElement("Feature").add("Id", Registry.FEATURE.getId(configuredFeature.feature))
                                .add("Config", configuredFeature.config)
                                .add("Description", configuredFeature.feature.toString());
                        throw new CrashException(crashReport);
                    }
                }
            }
        }

    }
}
