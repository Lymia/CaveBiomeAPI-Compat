# SimpleCaveBiomes

A drop-in replacement for CaveBiomeAPI with better mod compatibility.

You can find the original project here: [CaveBiomeAPI](https://www.curseforge.com/minecraft/mc-mods/cavebiomeapi)

## Limitations & Technical Details

Unlike CaveBiomeAPI, SimpleCaveBiomes does not actually load the biomes into the world, only generates the features
from them. As a result, cave biomes cannot override spawn tables and will not show in the F3 menu or minimap mods.

In return, this mod is fully compatible with mods like [Embeddium](https://github.com/embeddedt/embeddium/issues/172)
(CaveBiomeAPI breaks grass color when used with Embeddium) and OpenTerrainGenerator (which would require specific hooks
to support), as it does not need to do anything fancy to worldgen.
