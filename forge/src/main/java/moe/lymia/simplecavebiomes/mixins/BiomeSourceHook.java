package moe.lymia.simplecavebiomes.mixins;

import moe.lymia.simplecavebiomes.ScbConfig;
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
    private volatile boolean scb$initRun = false;
    @Unique
    private final Object scb$lock = new Object();

    @Override
    public CaveBiomeProvider scb$getCaveBiomeProvider() {
        return scb$caveBiomeProvider;
    }

    @Override
    public void scb$initGeneration(ServerWorld world) {
        if (!scb$initRun) {
            synchronized (scb$lock) {
                if (!scb$initRun) {
                    Identifier dimensionId = world.getRegistryKey().getValue();
                    if (ScbConfig.isDebug()) SimpleCaveBiomes.LOGGER.info("initGeneration for " + dimensionId);
                    if (ScbConfig.isDimensionWhitelisted(dimensionId)) {
                        if (ScbConfig.isDebug()) SimpleCaveBiomes.LOGGER.info("(enabled for " + dimensionId + ")");

                        BiomeSource source = (BiomeSource) (Object) this;
                        Registry<Biome> biomes = world.getRegistryManager().get(ForgeRegistries.Keys.BIOMES);
                        long seed = dimensionId.toString().hashCode() ^ world.getSeed();
                        scb$caveBiomeProvider = new CaveBiomeProvider(source, biomes, seed, dimensionId);
                    }
                    scb$initRun = true;
                }
            }
        }
    }
}
