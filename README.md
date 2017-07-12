# PlusTiC
Minecraft mod for adding new tools to and integrating various mods with Tinkers Construct

CurseForge page: https://minecraft.curseforge.com/projects/plustic
## New tool(s)
- Katana. A fast two-handed weapon that deals increasing damage the more mobs you kill.
- Laser Gun. A ranged weapon that requires durability and energy (Forge, Tesla, RF).
## A note about Natural Pledge
Starting in 5.1.2.0, PlusTiC forces out and *overrides* Natural Pledge TiC materials by default. This can be changed in the config so that PlusTiC does *not* add Botania materials if Natural Pledge is loaded.
## Supported mods
- LandCore (by this mod's author)
- LandCraft (also by this mod's author)
- Biomes o Plenty by Glitchfiend
- Project Red by MrTJP
- Mekanism by aidencbrady
- Botania by Vazkii
- Advanced Rocketry by zmaster587
- ArmorPlus by Moritz30
- EnderIO by crazypants
- Thermal Foundation by CoFH
- Draconic Evolution by brandon3055
- Actually Additions by Ellpeck
- Natura by mDiyo
- Psi by Vazkii
- Avaritia by brandon3055
- MineFactory Reloaded by skyboy026
- Galacticraft by micdoodle8
- Survivalist by gigaherz
- ProjectE by sinkillerj
## Using the source and building
After cloning this repository, run the commands (with working directory in the folder with the repository)
```
./gradlew setupDecompWorkspace
./gradlew setupDevWorkspace
```
For Eclipse, run
```
./gradlew eclipse
```
and add the jars in ./libs to the build path.
Build with
```
./gradlew build
```
