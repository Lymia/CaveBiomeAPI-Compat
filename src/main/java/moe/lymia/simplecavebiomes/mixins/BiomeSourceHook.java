package moe.lymia.simplecavebiomes.mixins;

import moe.lymia.simplecavebiomes.SimpleCaveBiomes;
import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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
    private volatile boolean scb$processed = false;
    @Unique
    private final Object scb$lock = new Object();

    @Override
    public CaveBiomeProvider scb$getCaveBiomeProvider() {
        return scb$caveBiomeProvider;
    }

    @Override
    public void scb$initGeneration(ServerWorld world) {
        if (!scb$processed) {
            synchronized (scb$lock) {
                if (!scb$processed) {
                    Identifier dimensionName = world.getRegistryKey().getValue();
                    if (SimpleCaveBiomes.IS_DEBUG) SimpleCaveBiomes.LOGGER.info("initGeneration for " + dimensionName);

                    // TODO: This shouldn't be hardcoded.
                    if (dimensionName.equals(new Identifier("minecraft", "overworld"))) {
                        if (SimpleCaveBiomes.IS_DEBUG) SimpleCaveBiomes.LOGGER.info("(enabled for " + dimensionName + ")");
                        Registry<Biome> biomes = world.getRegistryManager().get(ForgeRegistries.Keys.BIOMES);
                        scb$caveBiomeProvider = new CaveBiomeProvider(biomes, world.getSeed(), dimensionName);
                    }

                    scb$processed = true;
                }
            }
        }
    }
}
