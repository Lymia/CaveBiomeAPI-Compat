package moe.lymia.simplecavebiomes.otg;

import com.blackgear.cavebiomes.core.CBAConfig;
import com.blackgear.cavebiomes.core.api.CaveLayer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

public final class CaveBiomeProvider {
    private final MultiNoiseBiomeSource caveBiomeSource;

    public CaveBiomeProvider(Registry<Biome> biomeRegistry, long seed) {
        this.caveBiomeSource = CaveLayer.create(biomeRegistry, seed);
    }

    public Biome getBiome(Biome surfaceBiome, int x, int y, int z) {
        if ((float)y <= 12.0F + surfaceBiome.getDepth() * 4.0F && y >= 1) {
            Biome caveBiomes = caveBiomeSource.getBiomeForNoiseGen(x, y, z);
            if (CBAConfig.GENERATE_DEFAULT_CAVES.get()) {
                return caveBiomes;
            }
            if (caveBiomes.getCategory() != Biome.Category.EXTREME_HILLS) {
                return caveBiomes;
            }
        }
        return surfaceBiome;
    }
}
