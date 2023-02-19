package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Map;

public class SleepStatusEffect extends RPGStatusEffect {

    protected SleepStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xbf2271);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "c30124e9-2e74-43ea-be3b-aecb58370149", -1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "a6aab48e-7a1b-43f2-85fa-b307bf5a9294", -1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(EntityAttributes.GENERIC_FLYING_SPEED, "1307cb8c-509f-42d3-b5c8-52f4969bcf79", -1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierEntry : getAttributeModifiers().entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance((EntityAttribute) ((Map.Entry) entityAttributeEntityAttributeModifierEntry).getKey());
            if (entityAttributeInstance != null) {
                EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier) ((Map.Entry) entityAttributeEntityAttributeModifierEntry).getValue();
                entityAttributeInstance.removeModifier(entityAttributeModifier);
                entityAttributeInstance.addPersistentModifier(new EntityAttributeModifier(entityAttributeModifier.getId(), this.getTranslationKey() + " " + amplifier, this.adjustModifierAmount(amplifier, entityAttributeModifier), entityAttributeModifier.getOperation()));
            }
        }
    }
}
