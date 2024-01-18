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
@Mod.EventBusSubscriber(modid = SimpleCaveBiomes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SimpleCaveBiomes {
    public static final String MOD_ID = "simplecavebiomes";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static volatile boolean IS_DEBUG = false;

    {
        LOGGER.info("Initializing Simple Cave Biomes...");
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ScbConfig.COMMON);
        ScbRegistries.BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SimpleCaveBiomesAPI.addDefaultCaveBiome(new Biome.MixedNoisePoint(0, 0, 0, 0, 0));
    }

    @SubscribeEvent
    public static void configChangedEvent(ModConfig.ModConfigEvent event) {
        if (ScbConfig.IS_DEBUG_MODE.get()) {
            if (!IS_DEBUG) LOGGER.info("Simple Cave Biomes: Debug mode enabled!");
            IS_DEBUG = true;
        } else {
            IS_DEBUG = false;
        }
    }
}
