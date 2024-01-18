package moe.lymia.simplecavebiomes;

import moe.lymia.simplecavebiomes.api.SimpleCaveBiomesAPI;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = SimpleCaveBiomes.MOD_ID)
public class SimpleCaveBiomes {
    public static final String MOD_ID = "simplecavebiomes";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    {
        LOGGER.info("Initializing Simple Cave Biomes...");
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ScbConfig.COMMON_CONFIG);
        ScbRegistries.BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SimpleCaveBiomesAPI.addDefaultCaveBiome(new Biome.MixedNoisePoint(0, 0, 0, 0, 0));
    }
}
