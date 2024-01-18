package moe.lymia.simplecavebiomes;

import net.minecraft.util.Identifier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = SimpleCaveBiomes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ScbConfig {
    private ScbConfig() {}

    public static final ForgeConfigSpec COMMON_CONFIG;

    private static volatile boolean IS_DEBUG = false;
    private static volatile boolean USE_REAL_BIOMES = true;
    private static volatile boolean GENERATE_CAVE_BIOME = false;
    private static volatile Set<String> WHITELISTED_DIMENSIONS = null;

    private static final ForgeConfigSpec.BooleanValue OPT_IS_DEBUG_MODE;
    private static final ForgeConfigSpec.BooleanValue OPT_USE_REAL_BIOMES;
    private static final ForgeConfigSpec.BooleanValue OPT_GENERATE_CAVE_BIOME;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> OPT_WHITELISTED_DIMENSIONS;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();

        OPT_USE_REAL_BIOMES = configBuilder.comment(
                "Whether to actually set the biome underground to the proper cave biome rather than the surface biome" +
                        ". If this is disabled, cave biome features will still generate, but the biome will be " +
                        "considered as the surface biome.").define("use_real_biomes", true);
        OPT_WHITELISTED_DIMENSIONS = configBuilder.comment("A list of dimensions to generate cave biomes in.")
                .defineList("whitelisted_dimensions", Collections.singletonList("minecraft:overworld"),
                        x -> x instanceof String);
        OPT_GENERATE_CAVE_BIOME = configBuilder.comment(
                        "Whether to generate the generic cave biome underground. This causes problems, and is " +
                                "generally not recommended except for debugging purposes.")
                .define("generate_cave_biome", false);
        OPT_IS_DEBUG_MODE = configBuilder.comment(
                "Whether to print extra debugging information to the log.\nThis should be enabled unless the " +
                        "developer asks you to.").define("is_debug_mode", false);

        COMMON_CONFIG = configBuilder.build();
    }

    @SubscribeEvent
    public static void configChangedEvent(ModConfig.ModConfigEvent event) {
        // Process debug mode.
        boolean is_debug = ScbConfig.OPT_IS_DEBUG_MODE.get();
        if (is_debug && !IS_DEBUG) SimpleCaveBiomes.LOGGER.info("Simple Cave Biomes: Debug mode enabled!");
        IS_DEBUG = is_debug;

        // Process other options.
        USE_REAL_BIOMES = OPT_USE_REAL_BIOMES.get();
        GENERATE_CAVE_BIOME = OPT_GENERATE_CAVE_BIOME.get();
        WHITELISTED_DIMENSIONS = new HashSet<>(OPT_WHITELISTED_DIMENSIONS.get());
    }

    public static boolean isDebug() {
        return IS_DEBUG;
    }

    public static boolean isUseRealBiomes() {
        return USE_REAL_BIOMES;
    }

    public static boolean isGenerateCaveBiome() {
        return GENERATE_CAVE_BIOME;
    }

    public static boolean isDimensionWhitelisted(Identifier dimension) {
        Set<String> whitelist = WHITELISTED_DIMENSIONS;
        if (whitelist == null) return false;
        else return whitelist.contains(dimension.toString());
    }
}
