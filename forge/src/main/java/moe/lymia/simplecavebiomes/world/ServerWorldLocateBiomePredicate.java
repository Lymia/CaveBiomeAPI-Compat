package moe.lymia.simplecavebiomes.world;

import net.minecraft.world.biome.Biome;

import java.util.function.Predicate;

public class ServerWorldLocateBiomePredicate implements Predicate<Biome> {
    public final Biome targetBiome;

    public ServerWorldLocateBiomePredicate(Biome targetBiome) {
        this.targetBiome = targetBiome;
    }

    @Override
    public boolean test(Biome biome) {
        return biome == targetBiome;
    }
}
