package moe.lymia.simplecavebiomes.mixins;

import com.mojang.datafixers.DataFixer;
import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerHook {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ServerWorld arg, LevelStorage.Session arg2, DataFixer dataFixer,
            StructureManager structureManager, Executor workerExecutor, ChunkGenerator chunkGenerator, int viewDistance,
            boolean bl, WorldGenerationProgressListener arg5, Supplier supplier, CallbackInfo ci) {
        if (chunkGenerator != null) {
            BiomeSource source = chunkGenerator.getBiomeSource();
            if (source instanceof BiomeSourceExtension) {
                ((BiomeSourceExtension) source).scb$initGeneration(arg);
            }
        }
    }
}
