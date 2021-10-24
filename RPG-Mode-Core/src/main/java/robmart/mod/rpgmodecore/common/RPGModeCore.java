package robmart.mod.rpgmodecore.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.ActionResult;
import robmart.mod.rpgmodecore.api.event.LivingEntityEvents;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGModeCore implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register((server -> {
            Reference.MINECRAFT_SERVER = server;
        }));

        ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
            Reference.MINECRAFT_SERVER = null;
        }));
    }
}
