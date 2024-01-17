package moe.lymia.simplecavebiomes.mixins;

import com.pg85.otg.forge.biome.OTGBiomeProvider;
import moe.lymia.simplecavebiomes.world.CaveBiomeProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OTGBiomeProvider.class)
public class OTGBiomeProviderHook {
    @Unique
    private CaveBiomeProvider caveBiomeProvider;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(String presetFolderName, long seed, boolean legacyBiomeInitLayer, boolean largeBiomes,
            Registry<Biome> registry, CallbackInfo info) {
        caveBiomeProvider = new CaveBiomeProvider(registry, seed);
    }

    @Inject(at = @At("RETURN"), method = "getBiomeForNoiseGen", cancellable = true)
    private void hookBiomeSample(int biomeX, int biomeY, int biomeZ, CallbackInfoReturnable<Biome> info) {
        info.setReturnValue(caveBiomeProvider.getBiome(info.getReturnValue(), biomeX, biomeY, biomeZ));
    }
}
