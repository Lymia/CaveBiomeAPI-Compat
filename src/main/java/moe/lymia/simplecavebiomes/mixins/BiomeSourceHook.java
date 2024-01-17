package moe.lymia.simplecavebiomes.mixins;

import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BiomeSource.class)
public class BiomeSourceHook implements BiomeSourceExtension {
    @Unique
    private volatile CaveBiomeProvider scb$caveBiomeProvider = null;
    @Unique
    private final Object scb$lock = new Object();

    @Override
    public CaveBiomeProvider scb$getCaveBiomeProvider() {
        return scb$caveBiomeProvider;
    }

    @Override
    public void scb$initGeneration(ServerWorld world) {
        if (scb$caveBiomeProvider == null) {
            synchronized (scb$lock) {
                if (scb$caveBiomeProvider == null) {
                    Registry<Biome> biomes = world.getRegistryManager().get(ForgeRegistries.Keys.BIOMES);
                    scb$caveBiomeProvider = new CaveBiomeProvider(biomes, world.getSeed());
                }
            }
        }
    }
}
