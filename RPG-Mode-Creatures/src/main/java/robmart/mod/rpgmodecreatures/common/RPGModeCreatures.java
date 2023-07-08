package robmart.mod.rpgmodecreatures.common;

import net.fabricmc.api.ModInitializer;
import robmart.mod.rpgmodecreatures.common.entity.RPGCreaturesEntityTypes;
import robmart.mod.rpgmodecreatures.common.item.RPGCreaturesItems;
import robmart.mod.rpgmodecreatures.common.sound.RPGCreaturesSounds;
import software.bernie.geckolib.GeckoLib;


public class RPGModeCreatures implements ModInitializer {

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        RPGCreaturesEntityTypes.initialize();
        RPGCreaturesSounds.initialize();
        RPGCreaturesItems.initialize();
    }
}
