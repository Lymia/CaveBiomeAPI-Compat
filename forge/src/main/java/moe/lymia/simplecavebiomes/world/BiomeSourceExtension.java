package moe.lymia.simplecavebiomes.world;

import net.minecraft.server.world.ServerWorld;

public interface BiomeSourceExtension {
    CaveBiomeProvider scb$getCaveBiomeProvider();
    void scb$initGeneration(ServerWorld world);
}
