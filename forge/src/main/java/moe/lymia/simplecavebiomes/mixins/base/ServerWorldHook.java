package moe.lymia.simplecavebiomes.mixins.base;

import moe.lymia.simplecavebiomes.world.ServerWorldLocateBiomePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(ServerWorld.class)
public class ServerWorldHook {
    @Nullable
    @Overwrite
    public BlockPos locateBiome(Biome biome, BlockPos pos, int radius, int j) {
        ServerWorld self = (ServerWorld) (Object) this;
        return self.getChunkManager().getChunkGenerator().getBiomeSource()
                .locateBiome(pos.getX(), pos.getY(), pos.getZ(), radius, j, new ServerWorldLocateBiomePredicate(biome),
                        self.random, true);
    }
}
