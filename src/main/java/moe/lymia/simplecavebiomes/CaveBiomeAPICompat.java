package moe.lymia.simplecavebiomes;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * Helper class for SimpleCaveBiomes/CaveBiomeAPI compatibility. Not public API.
 */
public class CaveBiomeAPICompat {
    public static final List<Biome> COMPAT_BIOME_LIST = new ArrayList<>();
    public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> COMPAT_NOISE_LIST = new HashMap<>();

    public static final List<Biome> LOCKED_BIOME_LIST = Collections.unmodifiableList(COMPAT_BIOME_LIST);
    public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> LOCKED_NOISE_LIST =
            Collections.unmodifiableMap(COMPAT_NOISE_LIST);

    public static final RegistryKey<Registry<Biome>> REGISTRY_BIOME =
            RegistryKey.ofRegistry(ForgeRegistries.BIOMES.getRegistryName());
}