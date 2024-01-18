package moe.lymia.simplecavebiomes.api;

import moe.lymia.simplecavebiomes.CaveBiomeAPIShim;
import moe.lymia.simplecavebiomes.ScbRegistries;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;

public class SimpleCaveBiomesObjects {
    public static final BiomeDictionary.Type UNDERGROUND = BiomeDictionary.Type.getType("UNDERGROUND");

    public static final MultiNoiseBiomeSource.Preset CAVE_BIOME_PROVIDER =
            new MultiNoiseBiomeSource.Preset(new Identifier(CaveBiomeAPIShim.MOD_ID, "cave_biome_provider"),
                    (preset, registry, seed) -> SimpleCaveBiomesAPI.createNoise(registry, seed));

    public static final RegistryObject<Biome> CAVE = ScbRegistries.CAVE;
}
