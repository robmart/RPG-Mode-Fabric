package robmart.mod.rpgmodeeffects.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.ActionResult;
import robmart.mod.rpgmodecore.api.event.LivingEntityEvents;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGStatusEffects;

public class RPGModeEffects implements ModInitializer {
    @Override
    public void onInitialize() {
        RPGStatusEffects.register();

        LivingEntityEvents.JUMPING_EVENT.register((entity -> {
            if (entity.hasStatusEffect(RPGStatusEffects.PETRIFICATION)) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        }));
    }
}
