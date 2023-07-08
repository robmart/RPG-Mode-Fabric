package robmart.mod.rpgmodecreatures.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.item.RPGItems;
import robmart.mod.rpgmodecore.common.reference.Reference;
import robmart.mod.rpgmodecreatures.common.RPGModeCreatures;

public class RPGCreaturesItems extends RPGItems {
    public static Item RAT_SPAWN_EGG;
    public static Item NAGA_SPAWN_EGG;

    public static void initialize() {
        RAT_SPAWN_EGG = of("rat_spawn_egg", new SpawnEggItem(RPGModeCreatures.RAT, 0xc3B3331, 0xc665D5A, new FabricItemSettings()), ItemGroups.SPAWN_EGGS);
        NAGA_SPAWN_EGG = of("naga_spawn_egg", new SpawnEggItem(RPGModeCreatures.NAGA, 0xc18422E, 0xc22473F, new FabricItemSettings()), ItemGroups.SPAWN_EGGS);
    }
}
