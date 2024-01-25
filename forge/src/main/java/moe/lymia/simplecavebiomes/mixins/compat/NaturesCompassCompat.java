package moe.lymia.simplecavebiomes.mixins.compat;

import com.blackgear.cavebiomes.core.api.CaveBiomeAPI;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import moe.lymia.simplecavebiomes.ScbConfig;
import moe.lymia.simplecavebiomes.SimpleCaveBiomes;
import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import moe.lymia.simplecavebiomes.world.BiomeSourceExtension;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeSearchWorker.class)
public class NaturesCompassCompat {
    @Shadow
    public World world;
    @Unique
    private boolean isCaveBiomeSearch = false;
    @Unique
    private MultiNoiseBiomeSource caveBiomes = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInit(World world, PlayerEntity player, ItemStack stack, Biome biome, BlockPos startPos,
            CallbackInfo ci) {
        if (world instanceof ServerWorld && SimpleCaveBiomesAPI.isCaveBiome(biome.getRegistryName())) {
            ServerWorld serverWorld = (ServerWorld) world;
            BiomeSource source = serverWorld.getChunkManager().getChunkGenerator().getBiomeSource();
            BiomeSourceExtension extension = (BiomeSourceExtension) source;
            CaveBiomeProvider provider = extension.scb$getCaveBiomeProvider();
            if (provider != null) {
                if (ScbConfig.isDebug()) SimpleCaveBiomes.LOGGER.info(
                        "NaturesCompassCompat.injectInit: " + world + " / " + biome.getRegistryName() + " / " +
                                startPos);

                caveBiomes = provider.getCaveBiomeSource();
                isCaveBiomeSearch = true;
            }
        }
    }

    @Redirect(method = "doWork", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/BiomeAccess;" +
            "getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;"))
    private Biome getBiomeHook(BiomeAccess access, BlockPos pos) {
        if (isCaveBiomeSearch) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            Chunk chunk = this.world.getChunk(x >> 2, z >> 2, ChunkStatus.BIOMES, false);

            if (chunk != null) return access.getBiome(pos);
            else return caveBiomes.getBiomeForNoiseGen(x >> 2, y >> 2, z >> 2);
        } else {
            return access.getBiome(pos);
        }
    }
}
