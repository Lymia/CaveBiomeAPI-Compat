package moe.lymia.simplecavebiomes.mixins;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin({MultiNoiseBiomeSource.class})
public interface MultiNoiseBiomeSourceAccessor {
    @Invoker("<init>")
    static MultiNoiseBiomeSource create(long seed, List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomePoints,
            MultiNoiseBiomeSource.NoiseParameters temperatureNoiseParameters,
            MultiNoiseBiomeSource.NoiseParameters humidityNoiseParameters,
            MultiNoiseBiomeSource.NoiseParameters altitudeNoiseParameters,
            MultiNoiseBiomeSource.NoiseParameters weirdnessNoiseParameters,
            Optional<Pair<Registry<Biome>, MultiNoiseBiomeSource.Preset>> instance) {
        throw new UnsupportedOperationException();
    }
}
