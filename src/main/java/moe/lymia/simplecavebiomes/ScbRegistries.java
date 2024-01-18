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
        Biome.Builder biome = new Biome.Builder();
        biome.precipitation(Biome.Precipitation.RAIN);
        biome.category(Biome.Category.EXTREME_HILLS);
        biome.depth(0.125F).scale(0.05F).temperature(0.8F).downfall(0.4F);

        BiomeEffects.Builder biomeEffects = new BiomeEffects.Builder();
        biomeEffects.waterColor(0x3f76e4).waterFogColor(0x050533).fogColor(0xC0D8FF).skyColor(0x79A7FF);
        biomeEffects.moodSound(BiomeMoodSound.CAVE);
        biome.effects(biomeEffects.build());

        SpawnSettings.Builder biomeSpawn = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addBatsAndMonsters(biomeSpawn);
        biome.spawnSettings(biomeSpawn.build());

        GenerationSettings.Builder biomeGeneration = new GenerationSettings.Builder();
        biomeGeneration.surfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
        DefaultBiomeFeatures.addDefaultUndergroundStructures(biomeGeneration);
        DefaultBiomeFeatures.addLandCarvers(biomeGeneration);
        biome.generationSettings(biomeGeneration.build());

        return biome.build();
    });
}
