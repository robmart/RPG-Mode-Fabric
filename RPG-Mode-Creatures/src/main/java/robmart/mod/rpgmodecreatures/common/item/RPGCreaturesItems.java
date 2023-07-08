package robmart.mod.rpgmodecreatures.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import robmart.mod.rpgmodecore.common.item.RPGItems;
import robmart.mod.rpgmodecreatures.common.entity.RPGCreaturesEntityTypes;

public class RPGCreaturesItems extends RPGItems {
    public static Item RAT_SPAWN_EGG;
    public static Item NAGA_SPAWN_EGG;

    public static void initialize() {
        RAT_SPAWN_EGG = of("rat_spawn_egg", new SpawnEggItem(RPGCreaturesEntityTypes.RAT, 0xc3B3331, 0xc665D5A, new FabricItemSettings()), ItemGroups.SPAWN_EGGS);
        NAGA_SPAWN_EGG = of("naga_spawn_egg", new SpawnEggItem(RPGCreaturesEntityTypes.NAGA, 0xc18422E, 0xc22473F, new FabricItemSettings()), ItemGroups.SPAWN_EGGS);
    }
}
