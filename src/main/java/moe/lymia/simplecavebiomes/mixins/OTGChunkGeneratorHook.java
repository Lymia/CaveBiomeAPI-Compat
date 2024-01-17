package moe.lymia.simplecavebiomes.mixins;

import com.blackgear.cavebiomes.core.api.CaveLayer;
import com.blackgear.cavebiomes.core.utils.FeatureGenerationHelper;
import com.pg85.otg.forge.gen.OTGNoiseChunkGenerator;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OTGNoiseChunkGenerator.class)
public class OTGChunkGeneratorHook {
    @Unique
    private BiomeSource biomeSource;

    @Inject(at = @At("TAIL"), method =
            "<init>(Ljava/lang/String;Ljava/lang/String;" + "Lnet/minecraft/world/biome" + "/source/BiomeSource;" +
                    "Lnet/minecraft/world/biome/source/BiomeSource;" + "JLjava/util/function/Supplier;)V")
    private void init(String presetFolderName, String dimConfigName, BiomeSource biomeProvider1,
            BiomeSource biomeProvider2, long seed, Supplier dimensionSettingsSupplier, CallbackInfo ci) {
        biomeSource = biomeProvider1;
    }

    @Inject(at = @At("RETURN"), method = "generateFeatures")
    private void cba$generateUndergroundFeatures(ChunkRegion region, StructureAccessor manager, CallbackInfo ci) {
        int mainChunkX = region.getCenterChunkX();
        int mainChunkZ = region.getCenterChunkZ();
        int x = mainChunkX * 16;
        int z = mainChunkZ * 16;
        BlockPos pos = new BlockPos(x, 0, z);
        Biome biome = this.biomeSource.getBiomeForNoiseGen((mainChunkX << 2) + 2, 10, (mainChunkZ << 2) + 2);

        if (CaveLayer.CAVE_BIOME_LIST.contains(biome)) {
            ChunkRandom random = new ChunkRandom();
            long seed = random.setPopulationSeed(region.getSeed(), x, z);

            try {
                FeatureGenerationHelper.generateOnlyFeatures(biome, (ChunkGenerator) (Object) this, region, seed,
                        random, pos);
            } catch (Exception var15) {
                CrashReport report = CrashReport.create(var15, "Biome decoration");
                report.addElement("Generation").add("CenterX", mainChunkX).add("CenterZ", mainChunkZ).add("Seed", seed)
                        .add("Biome", biome);
                throw new CrashException(report);
            }
        }
    }
}
