package robmart.mod.rpgmodeeffects.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import robmart.mod.rpgmodecore.api.event.LivingEntityEvents;
import robmart.mod.rpgmodeeffects.common.entity.effect.CharmStatusEffect;
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

        LivingEntityEvents.DAMAGE_EVENT.register((target, source, amount) -> {
            LivingEntity sourceEntity = (LivingEntity) source.getSource();

            if (sourceEntity != null && sourceEntity.hasStatusEffect(RPGStatusEffects.CHARM)) {
                for (StatusEffectInstance instance : sourceEntity.getStatusEffects()){
                    if (instance.getEffectType() instanceof CharmStatusEffect charmInstance) {
                        if (charmInstance.getAttacker() == sourceEntity) {
                            return ActionResult.FAIL;
                        }
                    }
                }
            }

            return ActionResult.PASS;
        });
    }
}
