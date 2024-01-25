package moe.lymia.simplecavebiomes.mixins.base;

import moe.lymia.simplecavebiomes.ScbConfig;
import moe.lymia.simplecavebiomes.ScbRegistries;
import moe.lymia.simplecavebiomes.SimpleCaveBiomes;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import moe.lymia.simplecavebiomes.world.ServerWorldLocateBiomePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.function.Predicate;

@Mixin(BiomeSource.class)
public abstract class BiomeSourceHook implements BiomeSourceExtension {
    @Unique
    private volatile CaveBiomeProvider caveBiomeProvider = null;
    @Unique
    private volatile boolean initRun = false;
    @Unique
    private final Object lock = new Object();

    @Override
    public CaveBiomeProvider scb$getCaveBiomeProvider() {
        return caveBiomeProvider;
    }

    @Override
    public void scb$initGeneration(ServerWorld world) {
        if (!initRun) {
            synchronized (lock) {
                if (!initRun) {
                    Identifier dimensionId = world.getRegistryKey().getValue();
                    if (ScbConfig.isDebug()) SimpleCaveBiomes.LOGGER.info("initGeneration for " + dimensionId);
                    if (ScbConfig.isDimensionWhitelisted(dimensionId)) {
                        if (ScbConfig.isDebug()) SimpleCaveBiomes.LOGGER.info("(enabled for " + dimensionId + ")");

                        BiomeSource source = (BiomeSource) (Object) this;
                        Registry<Biome> biomes = world.getRegistryManager().get(ForgeRegistries.Keys.BIOMES);
                        long seed = dimensionId.toString().hashCode() ^ world.getSeed();
                        caveBiomeProvider = new CaveBiomeProvider(source, biomes, seed, dimensionId);
                    }
                    initRun = true;
                }
            }
        }
    }

    @Inject(method = "locateBiome(IIIIILjava/util/function/Predicate;Ljava/util/Random;Z)" +
            "Lnet/minecraft/util/math/BlockPos;", at = @At("HEAD"), cancellable = true)
    private void locateBiomeCheckFromWorld(int x, int y, int z, int radius, int m, Predicate<Biome> predicate,
            Random random, boolean bl, CallbackInfoReturnable<BlockPos> cir) {
        CaveBiomeProvider provider = caveBiomeProvider;
        if (provider != null && predicate instanceof ServerWorldLocateBiomePredicate) {
            ServerWorldLocateBiomePredicate biomePredicate = (ServerWorldLocateBiomePredicate) predicate;
            if (!ScbConfig.isGenerateCaveBiome() && biomePredicate.targetBiome == ScbRegistries.CAVE.get()) return;
            if (SimpleCaveBiomesAPI.isCaveBiome(biomePredicate.targetBiome.getRegistryName())) {
                cir.setReturnValue(
                        provider.getCaveBiomeSource().locateBiome(x, y, z, radius, m, predicate, random, bl));
            }
        }
    }

    @Inject(method = "locateBiome(IIIIILjava/util/function/Predicate;Ljava/util/Random;Z)" +
            "Lnet/minecraft/util/math/BlockPos;", at = @At("TAIL"), cancellable = true)
    private void locateBiomeFallbackMixin(int x, int y, int z, int radius, int m, Predicate<Biome> predicate,
            Random random, boolean bl, CallbackInfoReturnable<BlockPos> cir) {
        if (cir.getReturnValue() == null && !(predicate instanceof ServerWorldLocateBiomePredicate)) {
            CaveBiomeProvider provider = caveBiomeProvider;
            if (provider != null && SimpleCaveBiomesAPI.acceptsCaveBiomes(predicate)) {
                Predicate<Biome> mappedPredicate = predicate;
                if (!ScbConfig.isGenerateCaveBiome()) {
                    Biome caveBiome = ScbRegistries.CAVE.get();
                    mappedPredicate = biome -> biome != caveBiome && predicate.test(biome);
                }
                cir.setReturnValue(
                        provider.getCaveBiomeSource().locateBiome(x, y, z, radius, m, mappedPredicate, random, bl));
            }
        }
    }
}

