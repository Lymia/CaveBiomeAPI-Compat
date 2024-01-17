package moe.lymia.simplecavebiomes;

import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ScbRegistries {
    private ScbRegistries() {}

    public static final DeferredRegister<Biome> BIOMES =
            DeferredRegister.create(ForgeRegistries.BIOMES, SimpleCaveBiomes.MOD_ID);

    public static final RegistryObject<Biome> CAVE = BIOMES.register("caves", () -> {
        var biome = new Biome.Builder();
        biome.category(Biome.Category.EXTREME_HILLS);
        biome.depth(0.125F).scale(0.05F).temperature(0.8F).downfall(0.4F);

        var biomeEffects = new BiomeEffects.Builder();
        biomeEffects.waterColor(0x3f76e4).waterFogColor(0x050533).fogColor(0xC0D8FF).skyColor(0x79A7FF);
        biomeEffects.moodSound(BiomeMoodSound.CAVE);
        biome.effects(biomeEffects.build());

        var biomeSpawn = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addBatsAndMonsters(biomeSpawn);
        biome.spawnSettings(biomeSpawn.build());

        var biomeGeneration = (new GenerationSettings.Builder()).surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        DefaultBiomeFeatures.addDefaultUndergroundStructures(biomeGeneration);
        DefaultBiomeFeatures.addLandCarvers(biomeGeneration);
        biome.generationSettings(biomeGeneration.build());

        return biome.build();
    });
}
