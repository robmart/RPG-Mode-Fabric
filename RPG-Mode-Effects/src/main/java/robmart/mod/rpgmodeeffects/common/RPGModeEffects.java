package robmart.mod.rpgmodeeffects.common;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.ActionResult;
import robmart.mod.rpgmodecore.api.event.LivingEntityEvents;
import robmart.mod.rpgmodeeffects.common.entity.effect.CharmStatusEffect;
import robmart.mod.rpgmodeeffects.common.entity.effect.IStatusEffectTarget;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGStatusEffects;
import robmart.mod.rpgmodeeffects.common.entity.effect.SleepStatusEffect;

public class RPGModeEffects implements ModInitializer {
    @Override
    public void onInitialize() {
        RPGStatusEffects.register();

        LivingEntityEvents.JUMPING_EVENT.register((entity -> {
            if (entity.hasStatusEffect(RPGStatusEffects.PETRIFICATION) || entity.hasStatusEffect(RPGStatusEffects.SLEEP)) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        }));

        LivingEntityEvents.DAMAGE_EVENT.register((target, source, amount) -> {
            LivingEntity attacker = (LivingEntity) source.getAttacker();

            if (attacker != null && attacker.hasStatusEffect(RPGStatusEffects.CHARM)) { //Charm target can't attack source
                for (StatusEffectInstance instance : attacker.getStatusEffects()){
                    if (instance.getEffectType() instanceof CharmStatusEffect) {
                        if (((IStatusEffectTarget) instance).getTarget() == attacker) {
                            return ActionResult.FAIL;
                        }
                    }
                }
            }

            if (attacker != null && target.hasStatusEffect(RPGStatusEffects.CHARM)) { //Charm removed if target is attacked by source
                for (StatusEffectInstance instance : target.getStatusEffects()){
                    if (instance.getEffectType() instanceof CharmStatusEffect) {
                        if (((IStatusEffectTarget) instance).getAttacker() == attacker) {
                            target.removeStatusEffect(RPGStatusEffects.CHARM);
                            return ActionResult.PASS;
                        }
                    }
                }
            }

            if (target.hasStatusEffect(RPGStatusEffects.SLEEP)) { //Sleep removed when taking damage
                for (StatusEffectInstance instance : target.getStatusEffects()){
                    if (instance.getEffectType() instanceof SleepStatusEffect) {
                        target.removeStatusEffect(RPGStatusEffects.SLEEP);
                        return ActionResult.PASS;
                    }
                }
            }

            return ActionResult.PASS;
        });
    }
}
