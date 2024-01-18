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
    private static volatile Set<String> WHITELISTED_DIMENSIONS = null;

    private static final ForgeConfigSpec.BooleanValue OPT_IS_DEBUG_MODE;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> OPT_WHITELISTED_DIMENSIONS;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();

        OPT_IS_DEBUG_MODE = configBuilder.comment(
                        "Whether to print extra debugging information to the log.\nThis should be enabled unless the " +
                                "developer asks you to.")
                .define("is_debug_mode", false);
        OPT_WHITELISTED_DIMENSIONS = configBuilder.comment("A list of dimensions to generate cave biomes in.")
                .defineList("whitelisted_dimensions", Collections.singletonList("minecraft:overworld"),
                        x -> x instanceof String);

        COMMON_CONFIG = configBuilder.build();
    }

    @SubscribeEvent
    public static void configChangedEvent(ModConfig.ModConfigEvent event) {
        // Process debug mode.
        boolean is_debug = ScbConfig.OPT_IS_DEBUG_MODE.get();
        if (is_debug && !IS_DEBUG) SimpleCaveBiomes.LOGGER.info("Simple Cave Biomes: Debug mode enabled!");
        IS_DEBUG = is_debug;

        // Process dimension whitelist.
        WHITELISTED_DIMENSIONS = new HashSet<>(OPT_WHITELISTED_DIMENSIONS.get());
    }

    public static boolean isDebug() {
        return IS_DEBUG;
    }

    public static boolean isDimensionWhitelisted(Identifier dimension) {
        Set<String> whitelist = WHITELISTED_DIMENSIONS;
        if (whitelist == null) return false;
        else return whitelist.contains(dimension.toString());
    }
}
