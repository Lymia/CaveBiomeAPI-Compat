package com.blackgear.cavebiomes.core.registries;

import moe.lymia.simplecavebiomes.ScbRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

// (this class is used by Darker Depths, despite not being an API class)

/**
 * Compatibility shim for CaveBiomeAPI.
 */
public final class CaveBiomes {
    private CaveBiomes() {}

    public static final DeferredRegister<Biome> BIOMES = ScbRegistries.BIOMES;
    public static final RegistryObject<Biome> CAVE = ScbRegistries.CAVE;

    public static Biome makeDefaultCaves() {
        throw new RuntimeException("not implemented");
    }
}
