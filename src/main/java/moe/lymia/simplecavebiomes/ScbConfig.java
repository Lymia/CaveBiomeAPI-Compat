package moe.lymia.simplecavebiomes;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ScbConfig {
    private ScbConfig() {}

    public static final ForgeConfigSpec COMMON;

    public static final ForgeConfigSpec.BooleanValue IS_DEBUG_MODE;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();

        IS_DEBUG_MODE = configBuilder.comment(
                "Whether to print extra debugging information to console. This should be enabled unless the " +
                        "developer asks you to.").define("is_debug_mode", false);

        COMMON = configBuilder.build();
    }
}
