# SimpleCaveBiomes

A rewrite of CaveBiomeAPI with better mod compatibility.

Only Forge is currently supported. There are no significant mods that use the Fabric version to my knowledge. Please
submit an issue if you know of a mod worth porting it to Fabric for.

You can find the original project here:
[CaveBiomeAPI](https://www.curseforge.com/minecraft/mc-mods/cavebiomeapi) by blackgear27 

## Installation

* Remove CaveBiomeAPI from your mods directory.
* Install SimpleCaveBiomes in its place.

## Mod Compatibility & Limitations

SimpleCaveBiomes fixes the following known incompatibilities with CaveBiomeAPI:

* Dynamic Trees (no longer crashes)
* Embeddium (No longer prevents biome colors from working)

It fully works with both mods known to use CaveBiomeAPI:

* [Darker Depths](https://www.curseforge.com/minecraft/mc-mods/darker-depths) for 1.16.5
* [Caves and Cliffs Backport](https://www.curseforge.com/minecraft/mc-mods/caves-and-cliffs-backport)

Unlike CaveBiomeAPI, SimpleCaveBiomes does not add new carvers to attempt to replicate 1.18 cave caverns. Use
[YUNG's Better Caves](https://www.curseforge.com/minecraft/mc-mods/yungs-better-caves) or a similar mod instead. They
do it better.
