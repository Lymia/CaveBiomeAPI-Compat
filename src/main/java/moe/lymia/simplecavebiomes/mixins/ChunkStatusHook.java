package moe.lymia.simplecavebiomes.mixins;

import moe.lymia.simplecavebiomes.ScbConfig;
import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(ChunkStatus.class)
public class ChunkStatusHook {
    @Inject(method = {"method_12151", "func_222605_b"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen" +
            "/chunk/ChunkGenerator;generateFeatures(Lnet/minecraft/world/ChunkRegion;" +
            "Lnet/minecraft/world/gen/StructureAccessor;)V"))
    private static void beforeGenerateFeatures(ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator,
            StructureManager structureManager, ServerLightingProvider lightingProvider, Function function,
            List surroundingChunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture> cir) {
        // initialize the extension if possible
        BiomeSourceExtension extension = (BiomeSourceExtension) generator.getBiomeSource();
        extension.scb$initGeneration(world);
    }

    @Redirect(method = {"method_12151", "func_222605_b"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world" +
            "/gen/chunk/ChunkGenerator;generateFeatures(Lnet/minecraft/world/ChunkRegion;" +
            "Lnet/minecraft/world/gen/StructureAccessor;)V"))
    private static void hookGenerateFeatures(ChunkGenerator instance, ChunkRegion region, StructureAccessor accessor) {
        instance.generateFeatures(region, accessor);

        BiomeSourceExtension extension = (BiomeSourceExtension) instance.getBiomeSource();
        CaveBiomeProvider provider = extension.scb$getCaveBiomeProvider();
        if (provider != null) {
            provider.generateCaveFeatures(instance, region);
        }
    }

    @Inject(method = {"method_17034", "func_222598_a"}, at = @At("HEAD"))
    private static void applyRealBiomeMap(ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator,
            StructureManager structureManager, ServerLightingProvider lightingProvider, Function function,
            List surroundingChunks, Chunk chunk, CallbackInfoReturnable<CompletableFuture> cir) {
        if (!ScbConfig.isUseRealBiomes()) return;

        BiomeSourceExtension extension = (BiomeSourceExtension) generator.getBiomeSource();
        CaveBiomeProvider provider = extension.scb$getCaveBiomeProvider();
        if (provider != null) {
            provider.applyCaveBiomes(world.getRegistryManager().get(Registry.BIOME_KEY), chunk);
        }
    }
}
